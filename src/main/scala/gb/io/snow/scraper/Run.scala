package gb.io.snow.scraper

import java.time.LocalDate
import java.time.temporal.ChronoUnit

import scala.util.matching.Regex
import gb.io.snow.scraper.models.CovidData
import gb.io.snow.scraper.services.{DownloaderImpl, PdfReaderImpl, UrlExtractorImpl, WriterImpl}

import scala.io.Source
import scala.util.Try

object Run extends App{

  println("Starting...")

  val csvFileTotalCases: String  = "C:/Users/gisela.bellisomi/Documents/personal/corona/snow-scraper/casos_crorona_arggob.csv"
  val csvFileCasesByProv: String  = "C:/Users/gisela.bellisomi/Documents/personal/corona/snow-scraper/casos_crorona_byprov.csv"
  val baseGovUrl: String = "https://www.argentina.gob.ar/coronavirus/informe-diario/"
  val pattern : Regex = new Regex("(\\d+)-(\\d+)-(\\d+)")
  val dictMonth = Map("JANUARY" -> "enero",
    "FEBRUARY" -> "febreo",
    "MARCH" -> "marzo",
    "APRIL" -> "abril",
    "MAY" -> "mayo",
    "JUNE" -> "junio",
    "JULY" -> "julio",
    "AUGUST" -> "agosto",
    "SEPTEMBER" -> "septiembre",
    "OCTOBER" -> "octubre",
    "NOVEMBER" -> "noviembre",
    "DECEMBER" -> "diciembre")

  val dateTo: LocalDate = java.time.LocalDate.now
  val lastDateInCsv: String = List(getLastDate(csvFileTotalCases),getLastDate(csvFileCasesByProv)).min
  val dateFrom: LocalDate = LocalDate.parse(lastDateInCsv)

  if (dateTo.compareTo(dateFrom)>0 ) {
    val setUniqueMonths: Set[String] = getMonthsBetweenDates(dateFrom,dateTo)

    for (month <- setUniqueMonths){
      val currentGovUrl: String = baseGovUrl+month+"2020"
      println("Month: " + month)
      println("Url: " + currentGovUrl)
      val urlCovidForMonth: Array[AnyRef] = UrlExtractorImpl().getUrls(currentGovUrl)
      if (month=="marzo"){
        for (url <- urlCovidForMonth if pattern.findFirstIn(url.toString).isDefined &&
            pattern.findFirstIn(url.toString).get.compareTo("16-03-20")>0  ){
          val dateUrl: Option[String] = pattern.findFirstIn(url.toString)
          val documentAsByte: Array[Byte] = DownloaderImpl().downloadLastData(url.toString)
          val covidData: CovidData = PdfReaderImpl().readPdf(documentAsByte, dateUrl)
          println(covidData)
          WriterImpl().writeCsv(csvFileTotalCases, csvFileCasesByProv, covidData)
        }
      } else {
        for (url <- urlCovidForMonth){
          val dateUrl: Option[String] = pattern.findFirstIn(url.toString)
          val documentAsByte: Array[Byte] = DownloaderImpl().downloadLastData(url.toString)
          val covidData: CovidData = PdfReaderImpl().readPdf(documentAsByte, dateUrl)
          println(covidData)
          WriterImpl().writeCsv(csvFileTotalCases, csvFileCasesByProv, covidData)
        }
      }
    }
  }
  println("Finish")

  private def getLastDate(csvFile: String): String = {

    val bufferedSourceTry = Try(Source.fromFile(csvFile))
    if( bufferedSourceTry.isSuccess){
      val bufferedSource = bufferedSourceTry.get
      val dates = bufferedSource.getLines.map(
        line => line.split(",")(0) ).toList
      bufferedSource.close
      dates.max
    } else {"2020-03-16"} // the first date where report has the same structure. CHECK

  }
  private def getMonthsBetweenDates(dateFrom: LocalDate, dateTo: LocalDate): Set[String] ={
    val numOfDaysBetween = ChronoUnit.DAYS.between(dateFrom, dateTo).toInt
    val listMonths: List[Option[String]] = (1 to numOfDaysBetween).toList.map(
      i => dictMonth.get(dateFrom.plusDays(i).getMonth.toString)
    )
    val setUniqueMonths: Set[String] = listMonths.filter( _.isDefined).map(opt => opt.get).toSet
    setUniqueMonths
  }
}

