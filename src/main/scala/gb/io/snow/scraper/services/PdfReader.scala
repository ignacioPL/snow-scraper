package gb.io.snow.scraper.services

import gb.io.snow.scraper.models.CovidData
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.text.PDFTextStripper

trait PdfReader {
  def readPdf(byte: Array[Byte]): CovidData
}

case class PdfReaderImpl() extends PdfReader {

  override def readPdf(byte: Array[Byte]): CovidData = {
    if (byte.nonEmpty ) {
      // get the text
      val document: PDDocument = PDDocument.load(byte)
      val pdfStripper = new PDFTextStripper()
      val text: String = pdfStripper.getText(document)
      document.close()
      // get date
      val dateString: String = text.split("REPORTE DIARIO VESPERTINO NRO")(0).trim
      val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
      val date: LocalDate = LocalDate.parse(dateString,formatter)
      // get number of cases
      val stringPostNumberOfCases: String = text.split("Hoy fueron confirmados ")(1)
      val numberOfCases: Int = stringPostNumberOfCases.split(" nuevos casos de COVID-19")(0).toInt
      CovidData(date, numberOfCases)
    } else {
      CovidData(null,0) }
  }
}