package gb.io.snow.scraper


import gb.io.snow.scraper.models.CovidData
import gb.io.snow.scraper.services.{DownloaderImpl, PdfReaderImpl}
import org.apache.pdfbox.pdmodel.PDDocument

object Run extends App{

  println("Starting...")
  val url: String = "https://www.argentina.gob.ar/sites/default/files/28-04-20-reporte-vespertino-covid-19.pdf"
  val downloaderImpl = DownloaderImpl()
  val documentAsByte: Array[Byte] = downloaderImpl.downloadLastData(url)

  val pdfReaderImpl = PdfReaderImpl()
  val covidData: CovidData = pdfReaderImpl.readPdf(documentAsByte)
  // TO DO: remove enters from date
  println(covidData)

  println("Finish")
}
