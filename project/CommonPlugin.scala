import sbt._
import sbt.plugins.JvmPlugin

object CommonPlugin extends AutoPlugin {

  object autoImport {
    private lazy val memeidV = "0.1"
    lazy val memeid          = Seq(
      "com.47deg" %% "memeid4s",
      "com.47deg" %% "memeid4s-http4s",
      "com.47deg" %% "memeid4s-cats",
      "com.47deg" %% "memeid4s-circe",
      "com.47deg" %% "memeid4s-scalacheck",
      "com.47deg" %% "memeid4s-literal"
    ).map(_ % memeidV)
  }

  override def requires: Plugins = JvmPlugin

  override def trigger: PluginTrigger = allRequirements
}
