package gb.io.snow.scraper.services

import gb.io.snow.scraper.models.CovidData
import parser.extraction.Extractor._
import parser.utils.{Number}
import java.io.File


trait PdfReader {
  def readPdf(filePath: String): Option[String] //CovidData cambié pata ir viendo resultados
}

case class reader(filePath: String) extends PdfReader {
  override def readPdf(filePath: String)= {
    val file = new File(filePath)
    println("The length of the file id " + file.length())
    // si devuelvo el file (y cambio los tipos de salida), corre bien.
    val extractedText = readPDF(file)
    extractedText

    // con esto extractedText ya se rompe.
    //val keyword = Map("cases" -> Number())
    //val js : List[String] = getJSONObjects(extractedText, keyword)
    //js

  }
}