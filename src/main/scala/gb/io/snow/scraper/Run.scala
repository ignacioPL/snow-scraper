package gb.io.snow.scraper

import gb.io.snow.scraper.models.CovidData
import gb.io.snow.scraper.services.{DownloaderImpl, PdfReaderImpl,WriterImpl}

object Run extends App{

  println("Starting...")
  val dateUrl: String = "04-05-20"
  val StartUrl: String = "https://www.argentina.gob.ar/sites/default/files/"
  val possibleEndUrl: List[String] = List("-reporte-vespertino-covid-19.pdf",
    "_reporte_vespertino_covid-19.pdf",
    "_reporte_vespertino_covid_19.pdf",
    "_reporte-vespertino-covid-19.pdf",
    "_reporte_vespertino_covid_19_0.pdf",
    "_reporte_vespertino_covid_19_1.pdf")
  val possibleUrl: List[String] = possibleEndUrl.map(StartUrl + dateUrl + _ )

  val documentAsByte: Option[Array[Byte]] = possibleUrl.map(
    url => DownloaderImpl().downloadLastData(url)).find(
    x => x.nonEmpty)

  val covidData: CovidData = PdfReaderImpl().readPdf(documentAsByte,dateUrl)
  println(covidData)

  val writerImpl: WriterImpl = WriterImpl()
  writerImpl.writeCsv("C:/Users/gisela.bellisomi/Documents/personal/corona/snow-scraper/casos_crorona_arggob.csv",
      "C:/Users/gisela.bellisomi/Documents/personal/corona/snow-scraper/casos_crorona_byprov.csv",
      covidData)
  println("Finish")
}
