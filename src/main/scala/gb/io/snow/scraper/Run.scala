package gb.io.snow.scraper

import com.itextpdf.text.pdf.PdfReader
import gb.io.snow.scraper.models.CovidData
import gb.io.snow.scraper.services.{reader}

object Run extends App{
  override def main(args: Array[String] ): Unit = {
    println("Starting...")
    println("hola")

    val r = reader("testfile.pdf")
    val rr = r.readPdf("testfile.pdf")
    println(rr)
    println("Finish")
}
}
