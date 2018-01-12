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

fork := true

val _mainClass = "com.github.btnguyen2k.gearmanworker.Bootstrap"

/* Compiling  options */
javacOptions ++= Seq("-source", "1.8", "-target", "1.8")
mainClass in (Compile, run) := Some(_mainClass)

/* Packaging options */
mainClass in (Compile, packageBin)       := Some(_mainClass)
sources in (Compile, doc)                := Seq.empty
publishArtifact in (Compile, packageDoc) := false
publishArtifact in (Compile, packageSrc) := false
// add conf/ directory
mappings in Universal                    ++= (baseDirectory.value / "conf" * "*" get) map(x => x -> ("conf/" + x.getName))

/* Eclipse settings */
EclipseKeys.projectFlavor                := EclipseProjectFlavor.Java                   // Java project. Don't expect Scala IDE
EclipseKeys.executionEnvironment         := Some(EclipseExecutionEnvironment.JavaSE18)  // expect Java 1.8

/* Dependencies */
val _slf4jVersion = "1.7.25"

libraryDependencies ++= Seq(
    "org.slf4j"                  % "slf4j-api"                    % _slf4jVersion
   ,"org.slf4j"                  % "log4j-over-slf4j"             % _slf4jVersion
   ,"org.slf4j"                  % "slf4j-simple"                 % _slf4jVersion
   
   ,"org.apache.commons"         % "commons-lang3"                % "3.7"
    
   ,"com.typesafe"               % "config"                       % "1.3.2"
)
