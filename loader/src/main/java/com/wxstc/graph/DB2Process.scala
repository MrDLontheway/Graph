package com.wxstc.graph
import java.util
import java.util.Properties

import com.wuxi.scistor.compass.importer.common.dto.form.TaskMappingFormDTO
import com.wuxi.scistor.compass.importer.common.pojo.CompassInstanceCooker
import com.wxscistor.concurrent.MGraphDBManager
import com.wxscistor.config.VertexiumConfig
import com.wxscistor.util.AuthUtils
import com.wxstc.graph.LoadData.{resolveGraphEData, resolveGraphVData}
import org.apache.commons.collections.CollectionUtils
import org.apache.spark.TaskContext
import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.{Row, SparkSession}
import org.vertexium.{Element, ElementBuilder}
import org.vertexium.accumulo.{AccumuloAuthorizations, AccumuloGraph}

import scala.util.control.Breaks.{break, breakable}

object DB2Process extends Serializable {
  val threadBulkSize = VertexiumConfig.properties.getProperty("spark.partion.threadbulksize","10000").toInt
  val jdbcFetchSize = VertexiumConfig.properties.getProperty("spark.partion.jdbcFetchSize","1000").toInt

  val jdbcNumPartitions = "6"
  val partitionColumn = "id"
  val lowerBound = "0"
  val upperBound = "100000000000"

  val jdbcUrl = "jdbc:gbase://192.168.31.193:5258/testdb?autoReconnect=true"
  val table = "testdb.user"
  val username = "root"
  val passwrod = "123456"
  val driverClass = "com.gbase.jdbc.Driver"

  def run(): Unit = {

    /** ***************************************************************************************************************
      * sparksession
      */
    val spark = SparkSession
      .builder()
      .master("local")
      .appName(this.getClass.getName)
      .config("spark.logConf", true)
      .getOrCreate()
    spark.sparkContext.setLogLevel("info")

    val pros = new Properties

    pros.put("user", username)
    pros.put("password", passwrod)
    pros.put("driver", driverClass)

    pros.put("fetchsize", jdbcFetchSize)
    pros.put("partitionColumn", partitionColumn)
    pros.put("lowerBound", lowerBound)
    pros.put("upperBound", upperBound)
    pros.put("numPartitions", jdbcNumPartitions)

    val res = spark
      .read
      .format("jdbc")
      //(select * from testdb.user) t
      .jdbc(jdbcUrl, table, pros)

    //mapping bean
    val dto: TaskMappingFormDTO = null

    val schema = res.schema

    res.rdd.foreachPartition(partion => {
      val graph = MGraphDBManager.getAccumuloGraph(VertexiumConfig.properties.getProperty("TABLE_NAME_PREFIX","vertexium"))
      val auth = AuthUtils.getRootAuth(graph)

      ProcessPartionData(schema,dto,graph,auth,partion)

      MGraphDBManager.executeBulk.shutdown()
      breakable {
        while (true) {
          if (MGraphDBManager.executeBulk.isTerminated()) {
            graph.flush()
            break
          }
          Thread.sleep(2000)
        }
      }
    })

    res.explain()
    res.printSchema()
    println(res.rdd.partitions.size)
    res.show()

    Thread.sleep(1000000L)
  }

  def ProcessPartionData(schema: StructType,
                     dto: TaskMappingFormDTO,
                     graph: AccumuloGraph,
                     auth: AccumuloAuthorizations,
                     partion: scala.Iterator[Row]): Unit ={

    var addVertexs = new util.ArrayList[ElementBuilder[Element]]()
    var addEdges = new util.ArrayList[ElementBuilder[Element]]()

    try {
      partion.foreach(row=>{
        val data = new util.HashMap[String, AnyRef]()

        schema.foreach(field=>{
          data.put(field.name,row.getAs(field.name))
        })

        val res = CompassInstanceCooker.cookCompassObjectInstance(dto, data)
        val vertexs = resolveGraphVData(graph, dto, res)
        addVertexs.addAll(vertexs)
        if (!CollectionUtils.isEmpty(dto.getLinksInfo())) {
          val edges = resolveGraphEData(graph, dto, res)
          addEdges.addAll(edges)
        }

        if (addVertexs.size() >= threadBulkSize) {
          MGraphDBManager.asyncBulkData(graph, auth, addVertexs)
          addVertexs = new util.ArrayList[ElementBuilder[Element]]()
        }

        if (addEdges.size() >= threadBulkSize) {
          MGraphDBManager.asyncBulkData(graph, auth, addEdges)
          addEdges = new util.ArrayList[ElementBuilder[Element]]()
        }
      })
    } catch {
        case e: Exception =>
        e.printStackTrace()
    }

    if(addVertexs.size() > 0) MGraphDBManager.asyncBulkData(graph, auth, addVertexs)
    if(addEdges.size() > 0) MGraphDBManager.asyncBulkData(graph, auth, addEdges)
  }
}
