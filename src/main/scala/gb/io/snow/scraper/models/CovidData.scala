package gb.io.snow.scraper.models
import java.time.LocalDate

case class CovidData(date: LocalDate, cases: Int, mapProvCases: Map[String,Int])
