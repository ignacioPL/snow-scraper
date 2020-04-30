package gb.io.snow.scraper.services

import gb.io.snow.scraper.models.CovidData
import java.io.File
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.text.PDFTextStripper
import scala.util.{Failure, Success, Try}


trait PdfReader {
  //def readPdf(filePath: String): CovidData
  def readPdf(byte: Array[Byte]): CovidData
}

case class PdfReaderImpl() extends PdfReader {

  override def readPdf(byte: Array[Byte]): CovidData= {
    if (byte.nonEmpty ) {

      val document: PDDocument = PDDocument.load(byte)
      val pdfStripper = new PDFTextStripper()
      val text: String = pdfStripper.getText(document)
      document.close()

      val date: String = text.split("REPORTE DIARIO VESPERTINO NRO")(0).trim
      val stringPostNumberOfCases: String = text.split("Hoy fueron confirmados ")(1)
      val numberOfCases: Int = stringPostNumberOfCases.split(" nuevos casos de COVID-19")(0).toInt
      CovidData(date, numberOfCases)
    } else {
      CovidData("",0) }
  }
}