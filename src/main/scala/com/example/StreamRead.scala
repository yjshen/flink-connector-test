package com.example

import java.util.Properties

import org.apache.flink.api.common.restartstrategy.RestartStrategies
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.table.api.scala.StreamTableEnvironment
import org.apache.flink.table.descriptors.Pulsar

import scala.beans.BeanProperty

case class NasaMission(
    @BeanProperty id: Int,
    @BeanProperty missionName: String,
    @BeanProperty startYear: Int,
    @BeanProperty endYear: Int)


object StreamRead {
  def main(args: Array[String]): Unit = {
    val prop = new Properties()
    prop.setProperty("service.url", "pulsar://localhost:6650")
    prop.setProperty("admin.url", "http://localhost:8080")
    prop.setProperty("startingOffsets", "earliest")
    prop.setProperty("topic", "nasa-topic")

    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.getConfig.disableSysoutLogging()
    env.getConfig.setRestartStrategy(RestartStrategies.fixedDelayRestart(4, 10000))
    env.enableCheckpointing(5000)
    env.setStreamTimeCharacteristic(TimeCharacteristic.ProcessingTime)

    val tEnv = StreamTableEnvironment.create(env)
    tEnv.connect(new Pulsar().properties(prop))
      .inAppendMode()
      .registerTableSource("nasa-data")

    val nasaTable = tEnv.scan("nasa-data")
    nasaTable.printSchema()
    val schema = nasaTable.getSchema
    val stream = tEnv.toAppendStream(nasaTable)(schema.toRowType)

    stream.print()
  }
}
