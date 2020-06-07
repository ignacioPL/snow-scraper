package gb.io.snow.scraper.services

import gb.io.snow.scraper.models.CovidData
import better.files._


trait Writer {
  def writeCsv(csvFile: String, csvFileByProv: String, data: CovidData)
}
case class WriterImpl() extends Writer {
  override def writeCsv(csvFile: String, csvFileByProv: String, data: CovidData) ={

    if (data.date!=null ) {

      val file: File = File(csvFile)
      file.createIfNotExists()
      if (shouldAddData(file, data)) {
        file.appendLine(data.date + "," + data.cases)
        println("Data has being added to the file.")
        } else {
        println("Data already was in the file.")
      }

      val fileProv: File = File(csvFileByProv)
      fileProv.createIfNotExists()
      if (shouldAddData(fileProv, data)) {
        data.mapProvCases.foreach( x => fileProv.appendLine(data.date + ", " + x._1 + ", " + x._2.toString) )
        println("Data has being added to the file by prov.")
      } else {
        println("Data already was in the file by prov.")
      }

    }
  }
  //def fixFormatCovidData(data:CovidData) : CovidData ={
  // val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
  //  val dataAsDate: LocalDate = LocalDate.parse(data.date,formatter)
  //  val modifiedDate: String = dataAsDate.toString
  //  CovidData(modifiedDate,data.cases)
  //}
  def shouldAddData(file: File, data: CovidData): Boolean ={
    ! file.contentAsString.contains(data.date.toString)
  }
}
