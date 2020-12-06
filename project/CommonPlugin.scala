import sbt._
import sbt.plugins.JvmPlugin

object CommonPlugin extends AutoPlugin {

  object autoImport {
    private lazy val fuuidV = "0.4.0"
    lazy val backendFuuid   = Seq(
      "io.chrisdavenport" %% "fuuid-http4s"
    ).map(_ % fuuidV)
  }

  override def requires: Plugins = JvmPlugin

  override def trigger: PluginTrigger = allRequirements
}
