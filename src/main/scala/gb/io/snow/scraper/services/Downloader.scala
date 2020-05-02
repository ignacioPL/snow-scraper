package gb.io.snow.scraper.services
import scalaj.http._

import scala.util.{Success, Try}

trait Downloader {
  def downloadLastData(url :String): Array[Byte]
}

case class DownloaderImpl() extends Downloader  {
  override def downloadLastData(url :String): Array[Byte] = {

    val byteDocument: Try[HttpResponse[Array[Byte]]]= Try( Http(url).asBytes )

    val bodyDocument: Array[Byte] = byteDocument match {
      case Success(value) if value.code==200 => value.body
      case _ => Array.emptyByteArray
    }
    bodyDocument
  }
}