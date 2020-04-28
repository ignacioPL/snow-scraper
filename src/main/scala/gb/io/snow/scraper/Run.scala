package gb.io.snow.scraper


import gb.io.snow.scraper.models.CovidData
import gb.io.snow.scraper.services.{PdfReaderImpl}

object Run extends App{

  println("Starting...")

  val pdfReaderImpl = PdfReaderImpl("testfile.pdf")
  val covidData: CovidData = pdfReaderImpl.readPdf(filePath = "testfile.pdf")
  // TO DO: remove enters from date
  println(covidData)

  println("Finish")
}
