import sbt._
import Keys._

import Dependencies._

object Shared {
  lazy val sparkVersion = SettingKey[String]("x-spark-version")

  lazy val hadoopVersion = SettingKey[String]("x-hadoop-version")

  lazy val jets3tVersion = SettingKey[String]("x-jets3t-version")

  lazy val sharedSettings:Seq[Def.Setting[_]] = Seq(
    scalaVersion := "2.10.4",
    sparkVersion  :=  defaultSparkVersion,
    hadoopVersion :=  defaultHadoopVersion,
    jets3tVersion :=  defaultJets3tVersion,
    libraryDependencies += guava
  )

  lazy val sparkSettings:Seq[Def.Setting[_]] = Seq(
    libraryDependencies <++= (sparkVersion, hadoopVersion, jets3tVersion) { (sv, hv, jv) =>
      val libs = Seq(
        //sparkRepl(sv), → spark-repl:1.2.0 not yet published → lib/spark-repl_2.10-1.2.0-notebook.jar to be used
        breeze,
        sparkSQL(sv),
        hadoopClient(hv),
        jets3t(jv),
        "org.bdgenomics.adam" % "adam-core" % "0.16.0" excludeAll(ExclusionRule("commons-codec", "commons-codec")),
        "org.bdgenomics.adam" % "adam-apis" % "0.16.0" excludeAll(ExclusionRule("commons-codec", "commons-codec")),
        commonsCodec
      )
      libs
    },
    unmanagedJars in Compile += (baseDirectory in "sparkNotebook").value / "lib/spark-repl_2.10-1.2.0-notebook.jar"
  )
}
