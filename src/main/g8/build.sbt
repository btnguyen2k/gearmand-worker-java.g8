import com.typesafe.config._

val conf       = ConfigFactory.parseFile(new File("src/universal/conf/application.conf")).resolve()
val appName    = conf.getString("app.name").toLowerCase().replaceAll("\\\\W+", "-")
val appVersion = conf.getString("app.version")

lazy val root = (project in file(".")).enablePlugins(JavaAppPackaging, DockerPlugin).settings(
    name         := appName,
    version      := appVersion,
    organization := "$organization$"
)

/*----------------------------------------------------------------------*/

mainClass in (Compile, run) := Some("com.github.btnguyen2k.gearmanworker.Bootstrap")
mainClass in (Compile, packageBin) := Some("com.github.btnguyen2k.gearmanworker.Bootstrap")

fork := true

javacOptions ++= Seq("-source", "1.8", "-target", "1.8")
sources in (Compile, doc) := Seq.empty
publishArtifact in (Compile, packageDoc) := false
publishArtifact in (Compile, packageSrc) := false

EclipseKeys.projectFlavor            := EclipseProjectFlavor.Java                   // Java project. Don't expect Scala IDE
EclipseKeys.executionEnvironment     := Some(EclipseExecutionEnvironment.JavaSE18)  // expect Java 1.8

val _slf4jVersion = "1.7.25"

libraryDependencies ++= Seq(
    "org.slf4j"                  % "slf4j-api"                    % _slf4jVersion
   ,"org.slf4j"                  % "log4j-over-slf4j"             % _slf4jVersion
   ,"org.slf4j"                  % "slf4j-simple"                 % _slf4jVersion
   
   ,"org.apache.commons"         % "commons-lang3"                % "3.7"
    
   ,"com.typesafe"               % "config"                       % "1.3.2"
)
