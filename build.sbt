name := "HerdMatrixModel"

version := "0.1"

organization := "fr.cirad"

crossScalaVersions := Seq("2.12.8")

lazy val herdmatrixmodel = Project("HeardMatrixModel", file(".")) enablePlugins (SbtOsgi) settings(
  libraryDependencies += "org.scalanlp" %% "breeze" % "1.0",
  
  OsgiKeys.exportPackage := Seq("fr.cirad.*;-split-package:=merge-first"),
  OsgiKeys.importPackage := Seq("*;resolution:=optional"),
  OsgiKeys.privatePackage := Seq("!scala.*,!java.*,!META-INF.*.RSA,!META-INF.*.SF,!META-INF.*.DSA,META-INF.services.*,META-INF.*,*"),
  OsgiKeys.embeddedJars := (Keys.externalDependencyClasspath in Compile).value map (_.data) filter (f => f.getName startsWith "gt-"),
  OsgiKeys.requireCapability := """osgi.ee;filter:="(&(osgi.ee=JavaSE)(version=1.8))""""
)
