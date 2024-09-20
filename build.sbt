
Global / onChangedBuildSource := ReloadOnSourceChanges

lazy val threetyrz =
  (project in file("."))
    .enablePlugins(ScalaJSPlugin)
    .settings(
      name         := "threetyrz",
      version      := "0.0.1",
      scalaVersion := "3.4.1",
      organization := "fractaltreehouse.com",
      libraryDependencies ++= Seq(
        "io.indigoengine" %%% "tyrian-zio" % "0.11.0",
        "dev.zio"         %%% "zio-interop-cats" % "23.1.0.3",
        "org.scalameta"   %%% "munit"     % "0.7.29" % Test
      ),
      testFrameworks += new TestFramework("munit.Framework"),
      scalaJSLinkerConfig ~= { _.withModuleKind(ModuleKind.CommonJSModule) },
      scalafixOnCompile := true,
      semanticdbEnabled := true,
      semanticdbVersion := scalafixSemanticdb.revision,
      autoAPIMappings   := true
    )
