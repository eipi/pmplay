name := """pmplay"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache,
  javaWs,
  "org.projectlombok" % "lombok" % "1.12.6",
  "com.google.code.gson" % "gson" % "2.2",
  "com.thoughtworks.xstream" % "xstream" % "1.4.7",
  "stax" % "stax" % "1.2.0",
  "joda-time" % "joda-time" % "2.5",
  "org.codehaus.jettison" % "jettison" % "1.3.7",
  "org.hamcrest" % "hamcrest-core" % "1.3",
  "org.apache.commons" % "commons-lang3" % "3.3.2",
  "net.sf.flexjson" % "flexjson" % "3.1"
)
