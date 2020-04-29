package gb.io.snow.scraper.services

import gb.io.snow.scraper.models.CovidData
import java.io.File
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.text.PDFTextStripper


trait PdfReader {
  //def readPdf(filePath: String): CovidData
  def readPdf(byte: Array[Byte]): CovidData
}

case class PdfReaderImpl() extends PdfReader {

  override def readPdf(byte: Array[Byte]): CovidData= {

    //val file = new File(filePath)
    val document: PDDocument = PDDocument.load(byte)

    val pdfStripper = new PDFTextStripper()
    val text: String = pdfStripper.getText(document)
    document.close()

    val date: String = text.split("REPORTE DIARIO VESPERTINO NRO")(0).trim
    // TO DO: remove enters from date string
    val stringPostNumberOfCases: String = text.split("Hoy fueron confirmados ")(1)
    val numberOfCases: Int = stringPostNumberOfCases.split(" nuevos casos de COVID-19")(0).toInt

    CovidData(date,numberOfCases)
  }
}