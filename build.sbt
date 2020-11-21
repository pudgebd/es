name := "es_s"

version := "1.0"

scalaVersion := "2.11.12"

lazy val esVer = "6.2.2"
lazy val luceneVer = "7.2.1"
lazy val log4jVer = "2.8.1"
lazy val slf4jVer = "1.7.25"

libraryDependencies ++= Seq(
    "commons-io" % "commons-io" % "2.5",
    "org.codehaus.jettison" % "jettison" % "1.3.8",
    "org.apache.httpcomponents" % "httpcore" % "4.2",
    "org.apache.httpcomponents" % "httpclient" % "4.2",
    "org.elasticsearch" % "elasticsearch" % esVer,
    "org.elasticsearch.client" % "transport" % esVer,
    "org.apache.lucene" % "lucene-analyzers-common" % luceneVer

).map(_.exclude("org.slf4j", "*")) //.map(_.exclude("com.google.guava", "*"))


libraryDependencies ++= Seq(
    "org.slf4j" % "slf4j-log4j12" % slf4jVer,
    "org.slf4j" % "slf4j-api" % slf4jVer,
    "org.slf4j" % "slf4j-simple" % slf4jVer,
    "org.apache.logging.log4j" % "log4j-core" % log4jVer,
    "org.apache.logging.log4j" % "log4j-api" % log4jVer
)

