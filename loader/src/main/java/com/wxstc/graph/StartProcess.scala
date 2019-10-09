package com.wxstc.graph
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.gson.Gson
import com.wxscistor.config.VertexiumConfig

object StartProcess extends App{
  println(args(0))
  println(" --------------base64 data  args(0)")
  args(0) = new String(new sun.misc.BASE64Decoder().decodeBuffer(args(0)), "UTF-8")

//  val mapper = new ObjectMapper()
//  val obj = mapper.readValue(args(0), classOf[Any])
//  println(mapper.writerWithDefaultPrettyPrinter.writeValueAsString(obj))

  var appConfig = null//new Gson().fromJson(args(0),classOf[StartProcess])

  //数据权限
  val dsr = VertexiumConfig.properties.getProperty("data.dsr","")

  DB2Process.run()
}

class StartProcess{

}
