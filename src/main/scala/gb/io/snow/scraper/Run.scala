package gb.io.snow.scraper

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

import gb.io.snow.scraper.models.CovidData
import gb.io.snow.scraper.services.{DownloaderImpl, PdfReaderImpl, WriterImpl}

import scala.io.Source
import scala.util.Try

object Run extends App{

  println("Starting...")
  val csvFileTotalCases: String  = "C:/Users/gisela.bellisomi/Documents/personal/corona/snow-scraper/casos_crorona_arggob.csv"
  val csvFileCasesByProv: String  = "C:/Users/gisela.bellisomi/Documents/personal/corona/snow-scraper/casos_crorona_byprov.csv"

  val StartUrl: String = "https://www.argentina.gob.ar/sites/default/files/"
  val possibleEndUrl: List[String] = List("-reporte-vespertino-covid-19.pdf",
    "_reporte_vespertino_covid-19.pdf",
    "_reporte_vespertino_covid_19.pdf",
    "_reporte-vespertino-covid-19.pdf",
    "_reporte_vespertino_covid_19_0.pdf",
    "_reporte_vespertino_covid_19_1.pdf")

  val dateTo: LocalDate = java.time.LocalDate.now
  val lastDateInCsv: String = List(getLastDate(csvFileTotalCases),getLastDate(csvFileCasesByProv)).min
  val dateFrom: LocalDate = LocalDate.parse(lastDateInCsv).plusDays(1)

  if (dateTo.compareTo(dateFrom)>0 ) {

    val numOfDaysBetween = ChronoUnit.DAYS.between(dateFrom, dateTo).toInt
    val listDates: List[String] = (0 to numOfDaysBetween).toList.map(
      i => dateFrom.plusDays(i).format(DateTimeFormatter.ofPattern("dd-MM-yy"))
    )

    for (dateUrl <- listDates) {
      val possibleUrl: List[String] = possibleEndUrl.map(StartUrl + dateUrl + _)

      val documentAsByte: Option[Array[Byte]] = possibleUrl.map(
        url => DownloaderImpl().downloadLastData(url)).find(
        x => x.nonEmpty)

      val covidData: CovidData = PdfReaderImpl().readPdf(documentAsByte, dateUrl)
      println(covidData)

      val writerImpl: WriterImpl = WriterImpl()
      writerImpl.writeCsv(csvFileTotalCases, csvFileCasesByProv, covidData)

    }
  }
  println("Finish")

  def getLastDate(csvFile: String): String = {

    val bufferedSourceTry = Try(Source.fromFile(csvFile))
    if( bufferedSourceTry.isSuccess){
      val bufferedSource = bufferedSourceTry.get
      val dates = bufferedSource.getLines.map(
        line => line.split(",")(0) ).toList
      bufferedSource.close
      dates.max
    } else {"2020-04-01"} // the first date where report has the same structure. CHECK

  }
}

