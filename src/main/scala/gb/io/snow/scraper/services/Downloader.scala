package gb.io.snow.scraper.services
import scalaj.http._

trait Downloader {
  def downloadLastData(url :String): Array[Byte]
}

case class DownloaderImpl() extends Downloader  {
  override def downloadLastData(url :String): Array[Byte] = {
    val byte: Array[Byte] = Http(url).asBytes.body
    byte
  }
}