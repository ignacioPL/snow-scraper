package gb.io.snow.scraper

import java.time.LocalDate
import java.time.format.DateTimeFormatter

import scala.util.matching.Regex
import gb.io.snow.scraper.models.CovidData
import gb.io.snow.scraper.services.{DayUrlExtractor, DownloaderImpl, MonthUrlExtractor, PdfReaderImpl, WriterImpl}

object Run extends App{

  println("Starting...")

  val csvFileTotalCases: String  = "C:/Users/gisela.bellisomi/Documents/personal/corona/snow-scraper/casos_crorona_arggob.csv"
  val csvFileCasesByProv: String  = "C:/Users/gisela.bellisomi/Documents/personal/corona/snow-scraper/casos_crorona_byprov.csv"

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

  val monthsUrl: List[String] = MonthUrlExtractor().getUrls("https://www.argentina.gob.ar/coronavirus/informes-diarios/reportes")

  for (currentMonthUrl <- monthsUrl){
    println("New: " + currentMonthUrl)
    val dailyUrls: List[String] = DayUrlExtractor().getUrls(currentMonthUrl)

    for (url <- dailyUrls ){

      val dateUrl: Option[LocalDate] = extractDateFromUrl(url)
      // TO DO: think how to get info from reports before 2020-03-16. There is no pattern.
      if ( IsGraterThanMinDateAllowed(dateUrl) ) {

        val documentAsByte: Array[Byte] = DownloaderImpl().downloadLastData(url)
        val covidData: CovidData = PdfReaderImpl().readPdf(documentAsByte, dateUrl)
        println(covidData)
        WriterImpl().writeCsv(csvFileTotalCases, csvFileCasesByProv, covidData)

      }
    }
  }

  println("Finish")

  private def extractDateFromUrl(url: String): Option[LocalDate] ={
    val dateAsString: Option[String] = pattern.findFirstIn(url)
    if (dateAsString.isDefined) {
      val formatter = DateTimeFormatter.ofPattern("[dd-MM-yyyy][d-MM-yyyy][d-MM-yy][dd-MM-yy]")
      val date: LocalDate = LocalDate.parse(dateAsString.get, formatter)
      Option(date)
    } else { None }
  }

  private def IsGraterThanMinDateAllowed(dateUrl: Option[LocalDate]): Boolean={
    val formatter = DateTimeFormatter.ofPattern("[dd-MM-yyyy][d-MM-yyyy][d-MM-yy][dd-MM-yy]")
    val minDateAllowed = LocalDate.parse("16-03-20",formatter)

    dateUrl.isDefined && dateUrl.get.compareTo(minDateAllowed)>0

  }
}

