lazy val commonSettings = Seq(
  scalaVersion := "2.13.3",
  scalacOptions ++= Seq(
    "-Ymacro-annotations",
    "-language:higherKinds"
  )
)

lazy val `quadmist-common` = project
  .in(file("."))
  .aggregate(`quadmist-common-sub`.jvm, `quadmist-common-sub`.js)
  .settings(commonSettings)
  .settings(
    name := """quadmist-common""",
    version := "0.1.0-SNAPSHOT",
    organization := "com.lambdarat"
  )

lazy val `quadmist-common-sub` = crossProject(JSPlatform, JVMPlatform)
  .crossType(CrossType.Full)
  .in(file("."))
  .jvmSettings(
    libraryDependencies ++= scalaJSdeps.value ++ backendFuuid
  )
  .jvmSettings(commonSettings)
  .jsSettings(
    libraryDependencies ++= scalaJSdeps.value,
    scalaJSUseMainModuleInitializer := false
  )
  .jsSettings(commonSettings)

lazy val scalaJSdeps = Def.setting {
  lazy val scalatest  = Seq(
    "org.scalatest"     %%% "scalatest"       % "3.2.2",
    "org.scalatestplus" %%% "scalacheck-1-15" % "3.2.3.0"
  ).map(_ % Test)
  lazy val scalacheck = "org.scalacheck" %%% "scalacheck" % "1.15.1"

  lazy val catsV       = "2.2.0"
  lazy val cats        = Seq(
    "org.typelevel" %%% "cats-core",
    "org.typelevel" %%% "cats-effect"
  ).map(_ % catsV)

  lazy val enumeratumV = "1.6.1"
  lazy val enumeratum  = Seq(
    "com.beachape" %%% "enumeratum",
    "com.beachape" %%% "enumeratum-circe"
  ).map(_ % enumeratumV)

  lazy val newtype     = "io.estatico" %%% "newtype" % "0.4.4"

  lazy val circeV        = "0.13.0"
  lazy val circe         = Seq(
    "io.circe" %%% "circe-core",
    "io.circe" %%% "circe-generic",
    "io.circe" %%% "circe-generic-extras",
    "io.circe" %%% "circe-parser"
  ).map(_ % circeV)

  lazy val fuuidV        = "0.4.0"
  lazy val fuuid         = Seq(
    "io.chrisdavenport" %%% "fuuid-circe"
  ).map(_ % fuuidV)

  lazy val scalaJavatime = "io.github.cquiroz" %%% "scala-java-time" % "2.0.0"

  Seq(
    scalaJavatime,
    newtype,
    scalacheck % Test
  ) ++ circe ++ enumeratum ++ cats ++ scalatest ++ fuuid
}

addCompilerPlugin("org.typelevel" %% "kind-projector"     % "0.11.0" cross CrossVersion.full)
addCompilerPlugin("com.olegpy"    %% "better-monadic-for" % "0.3.1")
