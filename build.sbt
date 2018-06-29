name := """play-scala-IB"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.11"

scalacOptions ++= Seq("-unchecked", "-deprecation")

// to test production mode: sbt testProd
javaOptions += "-Dconfig.file=conf/dev.conf"

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % Test
libraryDependencies += jdbc
libraryDependencies += ehcache
libraryDependencies += "com.typesafe.play" %% "play-slick" % "3.0.0"
libraryDependencies += "javax.xml.bind" % "jaxb-api" % "2.3.0"

libraryDependencies ++= Seq(
  "com.adrianhurt" %% "play-bootstrap" % "1.2-P26-B3",
  "org.webjars" % "bootstrap" % "3.3.7" exclude("org.webjars", "jquery"),
  "org.webjars" % "jquery" % "3.2.1",
  "org.webjars.bower" % "moment" % "2.20.1",
  "org.webjars" % "font-awesome" % "4.7.0"
)

fork in Test := true
libraryDependencies += "com.typesafe.play" %% "play-ahc-ws" % "2.6.10" % Test
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % Test


import sys.process._ 

lazy val uiInit = taskKey[Unit]("Initialize a Polymer 3 startkit application.")
uiInit := {
      val s: TaskStreams = streams.value
      s.log.info("Please wait. It will take a while.")
      if((Process("polymer init --name polymer-3-starter-kit", baseDirectory.value /"ui") !) == 0){
        s.log.success("frontend initialized")
      } else {
        throw new IllegalStateException("frontend init failed!")
      }
}
lazy val uiBuild = taskKey[Unit]("Build frontend scripts")
uiBuild := {
      val s: TaskStreams = streams.value
      if((Process("polymer build", baseDirectory.value /"ui") !) == 0){
        s.log.success("frontend built")
      } else {
        throw new IllegalStateException("frontend failed to build !")
      }
}
lazy val uiRun = taskKey[Unit]("Execute frontend scripts as a standalone web polymer application")
uiRun := {
      val s: TaskStreams = streams.value
      if((Process("polymer serve", baseDirectory.value /"ui") !) == 0){
        s.log.success("frontend ready")
      } else {
        throw new IllegalStateException("frontend failed to start !")
      }
}

lazy val uiDeploy = taskKey[Unit]("Deploy files under ui/ to playframework application. Then use run to start the full stack application.")
uiDeploy := {
      val s: TaskStreams = streams.value
      uiBuild.value
      if((Process("cp ui/manifest.json public/") #&&
          Process("cp -r ui/build/es6-bundled/node_modules public/") #&&
          Process("cp -r ui/build/es6-bundled/src public/") #&&
          Process("cp ui/build/es6-bundled/index.html public/") #&&
          Process("cp ui/service-worker.js public/") #&&
	  Process("cp ui/sw-precache-config.js public/") ! ) == 0){ 
        s.log.success("cleaning ui built files done !")
      } else {
        throw new IllegalStateException("cleaning failed !")
      }
}
lazy val uiClean = taskKey[Unit]("Clean ui/build directory.")
uiClean := {
      val s: TaskStreams = streams.value
      if((Process("rm -rf build", baseDirectory.value /"ui") !) == 0){
        s.log.success("cleaning ui built files done !")
      } else {
        throw new IllegalStateException("cleaning failed !")
      }
}
lazy val uiCleanAll = taskKey[Unit]("Clean ui files under ui/build and playframework public/ folders. Source files stay untouched")
uiCleanAll := {
      uiClean.value
      val s: TaskStreams = streams.value
      (Process("rm -rf node_modules", baseDirectory.value /"public") ###
       Process("rm -rf src", baseDirectory.value /"public") ###
       Process("rm manifest.json", baseDirectory.value /"public") ###
       Process("rm sw-precache-config.js", baseDirectory.value /"public") ###
       Process("rm service-worker.js", baseDirectory.value /"public") !)
      s.log.success("cleaning ui built files under public folder done !")
}
