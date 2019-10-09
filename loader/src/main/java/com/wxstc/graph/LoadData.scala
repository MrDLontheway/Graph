package com.wxstc.graph

import java.util

import com.alibaba.fastjson.{JSON, TypeReference}
import com.wuxi.scistor.compass.importer.common.dto.CompassObjectDTO
import com.wuxi.scistor.compass.importer.common.dto.form.TaskMappingFormDTO
import com.wuxi.scistor.compass.importer.common.pojo.{CompassInstanceCooker, CsvHandler}
import com.wxscistor.concurrent.MGraphDBManager
import com.wxscistor.config.VertexiumConfig
import com.wxscistor.di.resolve.compass.CompassResolveServiceImpl
import com.wxscistor.util.AuthUtils
import org.apache.commons.collections.CollectionUtils
import org.apache.spark.sql.SparkSession
import org.vertexium.accumulo.{AccumuloAuthorizations, AccumuloGraph}
import org.vertexium.{Element, ElementBuilder}

import scala.util.control.Breaks._
import scala.collection.JavaConverters._

object LoadData {

  def main(args: Array[String]): Unit = {

    /** ***************************************************************************************************************
      * sparksession
      */
    val spark = SparkSession
      .builder()
      .appName(this.getClass.getName)
      .config("spark.logConf", true)
      .getOrCreate()
    spark.sparkContext.setLogLevel("INFO")

    val mappingPath = VertexiumConfig.properties.getProperty("mapping.path")
    val datapth = VertexiumConfig.properties.getProperty("data.path")

    var mapping: String = ""

    spark.sparkContext
      .textFile(mappingPath)
      .collect()
      .foreach(line => {
        mapping += line
      })
    val dto: TaskMappingFormDTO =
      JSON.parseObject(mapping, classOf[TaskMappingFormDTO])

    val rdd = spark.sparkContext.textFile(datapth)

    rdd.foreachPartition(partion => {
      val graph = MGraphDBManager.getAccumuloGraph(VertexiumConfig.properties.getProperty("TABLE_NAME_PREFIX","vertexium"))
      val auth = AuthUtils.getRootAuth(graph)

      VertexiumConfig.properties.getProperty("data.type").toLowerCase match {
        case "csv" => {
          processCSVData(graph, auth,dto,partion)
        }

        case "json" => {
          processJSONData(graph, auth,dto,partion)
        }
      }

      MGraphDBManager.executeBulk.shutdown()
      breakable {
        while (true) {
          if (MGraphDBManager.executeBulk.isTerminated()) {
            graph.flush()
            break
          }
          Thread.sleep(2000);
        }
      }
    })

    spark.sparkContext.stop()
  }

  def resolveGraphVData(
      graph: AccumuloGraph,
      mapping: TaskMappingFormDTO,
      data: util.Map[String, util.List[CompassObjectDTO]]
  ): java.util.List[org.vertexium.ElementBuilder[Element]] = {
    val res = new java.util.ArrayList[org.vertexium.ElementBuilder[Element]]()
    val service = new CompassResolveServiceImpl()
    service.setAccumuloGraph(graph)
    val dsr = StartProcess.dsr

    data.asScala.foreach(v => {
      v._2.asScala.foreach(vertex => {
        try {
          val vertexbuilder: ElementBuilder[Element] =
            service.resolveKafkaJsonDataV2("", dsr, JSON.toJSON(vertex).toString)
          res.add(vertexbuilder)
        } catch {
            case e: Exception =>
            e.printStackTrace()
        }
      })
    })
    res
  }

  def resolveGraphEData(
      graph: AccumuloGraph,
      mapping: TaskMappingFormDTO,
      data: util.Map[String, util.List[CompassObjectDTO]]
  ): java.util.List[org.vertexium.ElementBuilder[Element]] = {
    val res = new java.util.ArrayList[org.vertexium.ElementBuilder[Element]]()
    val service = new CompassResolveServiceImpl()
    service.setAccumuloGraph(graph)
    val dsr = StartProcess.dsr

    val linkres = CompassInstanceCooker.cookCompassLinkInstance(mapping, data)
    linkres.asScala.foreach(edge => {
      val edgeBuilder =
        service.resolveKafkaJsonEdgeDataV2(dsr, JSON.toJSON(edge).toString)
      res.add(edgeBuilder)
    })
    res
  }

  def processCSVData(graph: AccumuloGraph,
                     auth: AccumuloAuthorizations,
                     dto:TaskMappingFormDTO,
                     partion:scala.Iterator[String]): Unit = {
    var addVertexs = new util.ArrayList[ElementBuilder[Element]]()
    var addEdges = new util.ArrayList[ElementBuilder[Element]]()
    val separator = VertexiumConfig.properties.getProperty("data.csv.separator")
    partion.foreach(f = line => {
      try {
        val data: util.Map[String, AnyRef] =
          CsvHandler.line2Map(line, null, separator)
        //===========================
        val res = CompassInstanceCooker.cookCompassObjectInstance(dto, data)
        val vertexs = resolveGraphVData(graph, dto, res)
        addVertexs.addAll(vertexs)
        if (!CollectionUtils.isEmpty(dto.getLinksInfo())) {
          val edges = resolveGraphEData(graph, dto, res)
          addEdges.addAll(edges)
        }

        if (addVertexs.size() >= 10000) {
          MGraphDBManager.asyncBulkData(graph, auth, addVertexs)
          addVertexs = new util.ArrayList[ElementBuilder[Element]]()
        }

        if (addEdges.size() >= 10000) {
          MGraphDBManager.asyncBulkData(graph, auth, addEdges)
          addEdges = new util.ArrayList[ElementBuilder[Element]]()
        }

      } catch {
          case e: Exception =>
          e.printStackTrace()
      }

  })
    if(addVertexs.size()>0) MGraphDBManager.asyncBulkData(graph, auth, addVertexs)
    if(addEdges.size()>0) MGraphDBManager.asyncBulkData(graph, auth, addEdges)
  }

  def processJSONData(graph: AccumuloGraph,
                      auth: AccumuloAuthorizations,
                      dto:TaskMappingFormDTO,
                      partion:scala.Iterator[String]): Unit = {
    var addVertexs = new util.ArrayList[ElementBuilder[Element]]()
    var addEdges = new util.ArrayList[ElementBuilder[Element]]()

    partion.foreach(line => {
      try {
        val dataMap: java.util.Map[String, Object] =
          JSON.parseObject(
            line,
            new TypeReference[java.util.Map[String, Object]]() {}
          )
        val d = new util.HashMap[String, Object]()
        d.put("record", JSON.toJSON(dataMap))

        //===========================
        val res = CompassInstanceCooker.cookCompassObjectInstance(dto, d)
        val vertexs = resolveGraphVData(graph, dto, res)
        addVertexs.addAll(vertexs)
        if (!CollectionUtils.isEmpty(dto.getLinksInfo())) {
          val edges = resolveGraphEData(graph, dto, res)
          addEdges.addAll(edges)
        }

        if (addVertexs.size() >= 10000) {
          MGraphDBManager.asyncBulkData(graph, auth, addVertexs)
          addVertexs = new util.ArrayList[ElementBuilder[Element]]()
        }

        if (addEdges.size() >= 10000) {
          MGraphDBManager.asyncBulkData(graph, auth, addEdges)
          addEdges = new util.ArrayList[ElementBuilder[Element]]()
        }
      } catch {
          case e: Exception =>
          e.printStackTrace()
      }
    })

    if(addVertexs.size()>0) MGraphDBManager.asyncBulkData(graph, auth, addVertexs)
    if(addEdges.size()>0) MGraphDBManager.asyncBulkData(graph, auth, addEdges)
  }
}
