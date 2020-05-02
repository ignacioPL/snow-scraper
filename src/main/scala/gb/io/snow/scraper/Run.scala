package gb.io.snow.scraper

import gb.io.snow.scraper.models.CovidData
import gb.io.snow.scraper.services.{DownloaderImpl, PdfReaderImpl,WriterImpl}

object Run extends App{

  println("Starting...")
  val url: String = "https://www.argentina.gob.ar/sites/default/files/28-04-20-reporte-vespertino-covid-19.pdf"
  val downloaderImpl: DownloaderImpl = DownloaderImpl()
  val documentAsByte: Array[Byte] = downloaderImpl.downloadLastData(url)

  val pdfReaderImpl: PdfReaderImpl = PdfReaderImpl()
  val covidData: CovidData = pdfReaderImpl.readPdf(documentAsByte)
  println(covidData)

  val writerImpl: WriterImpl = WriterImpl()
  writerImpl.writeCsv("C:/Users/gisela.bellisomi/Documents/personal/corona/snow-scraper/casos_crorona_arggob.csv",covidData)

  println("Finish")
}
