package gb.io.snow.scraper.services

import gb.io.snow.scraper.models.CovidData
import better.files._
import java.time
import java.time.LocalDate
import java.time.format.DateTimeFormatter


trait Writer {
  def writeCsv(csvFile: String, data: CovidData)
}
case class WriterImpl() extends Writer {
  override def writeCsv(csvFile: String, data: CovidData) ={
    if (data.date.length>0) {
      val fixedData: CovidData = fixFormatCovidData(data)
      val file: File = File(csvFile)
      if (shouldAddData(file, fixedData)) {
        file.appendLine(fixedData.date + "," + fixedData.cases)
        println("Data has being added to the file.")
      } else {
        println("Data already was in the file.")
      }
    }
  }
  def fixFormatCovidData(data:CovidData) : CovidData ={
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    val dataAsDate = LocalDate.parse(data.date,formatter)
    val modifiedDate: String = dataAsDate.toString
    CovidData(modifiedDate,data.cases)
  }
  def shouldAddData(file: File, data: CovidData): Boolean ={
    ! file.contentAsString.contains(data.date)
  }
}
