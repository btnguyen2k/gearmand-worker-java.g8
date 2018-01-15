import com.typesafe.config._

val conf       = ConfigFactory.parseFile(new File("conf/application.conf")).resolve()
val appName    = conf.getString("app.name").toLowerCase().replaceAll("\\\\W+", "-")
val appVersion = conf.getString("app.version")

lazy val root = (project in file(".")).enablePlugins(JavaAppPackaging, DockerPlugin).settings(
    name         := appName,
    version      := appVersion,
    organization := "$organization$"
)

/*----------------------------------------------------------------------*/

fork := true

val _mainClass = "com.github.btnguyen2k.gearmanworker.Bootstrap"

/* Packaging options */
mainClass in (Compile, packageBin)       := Some(_mainClass)
sources in (Compile, doc)                := Seq.empty
publishArtifact in (Compile, packageDoc) := false
publishArtifact in (Compile, packageSrc) := false
autoScalaLibrary                         := false
// add conf/ directory
mappings in Universal                    ++= (baseDirectory.value / "conf" * "*" get) map(x => x -> ("conf/" + x.getName))

/* Compiling  options */
javacOptions ++= Seq("-source", "1.8", "-target", "1.8")

/* Run options */
javaOptions  ++= collection.JavaConverters.propertiesAsScalaMap(System.getProperties)
    .map{ case (key,value) => "-D" + key + "=" +value }.toSeq
mainClass in (Compile, run) := Some(_mainClass)

/* Eclipse settings */
EclipseKeys.projectFlavor                := EclipseProjectFlavor.Java                   // Java project. Don't expect Scala IDE
EclipseKeys.executionEnvironment         := Some(EclipseExecutionEnvironment.JavaSE18)  // expect Java 1.8

/* Dependencies */
val _slf4jVersion = "1.7.25"

libraryDependencies ++= Seq(
    "org.slf4j"                  % "slf4j-api"                    % _slf4jVersion
   ,"org.slf4j"                  % "log4j-over-slf4j"             % _slf4jVersion
   ,"ch.qos.logback"             % "logback-classic"              % "1.2.3"
   
   ,"org.apache.commons"         % "commons-lang3"                % "3.7"
   ,"com.typesafe"               % "config"                       % "1.3.2"
   
   ,"com.github.ddth"            % "ddth-commons-core"            % "0.7.1.1"
)
