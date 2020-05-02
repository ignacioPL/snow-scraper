package gb.io.snow.scraper

import gb.io.snow.scraper.models.CovidData
import gb.io.snow.scraper.services.{DownloaderImpl, PdfReaderImpl,WriterImpl}

object Run extends App{

  println("Starting...")
  val dateUrl: String = "30-04-20"
  val StartUrl: String = "https://www.argentina.gob.ar/sites/default/files/"
  val possibleUrl: List[String] = List(StartUrl + dateUrl + "-reporte-vespertino-covid-19.pdf",
    StartUrl + dateUrl + "_reporte_vespertino_covid-19.pdf",
    StartUrl + dateUrl + "_reporte_vespertino_covid_19.pdf",
    StartUrl + dateUrl + "_reporte-vespertino-covid-19.pdf",
    StartUrl + dateUrl + "_reporte_vespertino_covid_19_0.pdf",
    StartUrl + dateUrl + "_reporte_vespertino_covid_19_1.pdf")

  //val downloaderImpl: DownloaderImpl = DownloaderImpl()
  //val documentAsByte: Array[Byte] = downloaderImpl.downloadLastData(url)

  val documentAsByteList: List[Array[Byte]] = possibleUrl.map(
    url => DownloaderImpl().downloadLastData(url)).filter(
    x => x.nonEmpty)
  if(documentAsByteList.length==1) {
    val documentAsByte: Array[Byte] = documentAsByteList.head
    val pdfReaderImpl: PdfReaderImpl = PdfReaderImpl()
    val covidData: CovidData = pdfReaderImpl.readPdf(documentAsByte)
    println(covidData)

    val writerImpl: WriterImpl = WriterImpl()
    writerImpl.writeCsv("C:/Users/gisela.bellisomi/Documents/personal/corona/snow-scraper/casos_crorona_arggob.csv",
      "C:/Users/gisela.bellisomi/Documents/personal/corona/snow-scraper/casos_crorona_byprov.csv",
      covidData)
  }
  println("Finish")
}
