import com.typesafe.config._

val conf       = ConfigFactory.parseFile(new File("conf/application.conf")).resolve()
val appName    = conf.getString("app.name").toLowerCase().replaceAll("\\W+", "-")
val appVersion = conf.getString("app.version")

//sbtPlugin := true
//scalaVersion := "2.12.4"

lazy val root = (project in file(".")).enablePlugins(JavaAppPackaging, DockerPlugin).settings(
    name := "gearmand-worker-java.g8",
    //scriptedLaunchOpts ++= List("-Xms1024m", "-Xmx1024m", "-XX:ReservedCodeCacheSize=128m", "-XX:MaxPermSize=256m", "-Xss2m", "-Dfile.encoding=UTF-8"),
    resolvers += Resolver.url("typesafe", url("http://repo.typesafe.com/typesafe/ivy-releases/"))(Resolver.ivyStylePatterns)
)

//giter8.ScaffoldPlugin.scaffoldSettings

/*----------------------------------------------------------------------*/
// Convenient settings: mostly copied from src/g8/build.sbt
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

/* Docker packaging options */
dockerCommands := Seq()
import com.typesafe.sbt.packager.docker._
dockerCommands := Seq(
    Cmd("FROM", "openjdk:8-jre-alpine"),
    //Cmd("LABEL", "maintainer=\"$app_author$\""),
    Cmd("ADD", "opt /opt"),
    Cmd("RUN", "apk add --no-cache bash && ln -s /opt/docker /opt/" + appName + " && chown -R daemon:daemon /opt"),	
    Cmd("WORKDIR", "/opt/" + appName),
    Cmd("USER", "daemon"),
    ExecCmd("ENTRYPOINT", "./bin/" + appName)
)
packageName in Docker                 := appName
version in Docker                     := appVersion

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
