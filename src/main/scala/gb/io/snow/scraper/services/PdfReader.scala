package gb.io.snow.scraper.services

import gb.io.snow.scraper.models.CovidData
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.text.PDFTextStripper
import scala.util.matching.Regex

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

      val date: LocalDate = getDateFromTextString(text)
      val numberOfCases: Int = getTotalCasesFromTextString(text)
      //val mapProvCases: Map[String,Int] = getCasesByProvFromTextString(text)
      val mapProvCases: Map[String,Int] = getCasesByProvFromTextString_v2(text)
      CovidData(date,numberOfCases,mapProvCases)
    } else {
      CovidData(null,0,null) }
  }
  def getDateFromTextString(text: String): LocalDate = {
    val dateString: String = text.split("REPORTE DIARIO VESPERTINO NRO")(0).replace("\n", "").trim
    println(dateString)

    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    LocalDate.parse(dateString,formatter)
  }
  def getTotalCasesFromTextString(text: String): Int ={
    val stringPostNumberOfCases: String = text.split("Hoy fueron confirmados ")(1)
    val numberOfCases: Int = stringPostNumberOfCases.split(" nuevos casos de COVID-19")(0).toInt
    numberOfCases
  }
  def getCasesByProvFromTextString(text: String): Map[String, Int] ={
    val textProvincias: String = text.split("Detalle por provincia")(1)

    val listProv: List[String] = List("Buenos Aires","Ciudad de Buenos Aires","Catamarca","Chaco","Chubut","Córdoba","Corrientes","Entre Ríos","Formosa","Jujuy","La Pampa","La Rioja","Mendoza","Misiones","Neuquén","Río Negro","Salta","San Juan","San Luis", "Santa Cruz","Santa Fe","Santiago del Estero","Tierra del Fuego","Tucumán")
    val casesProv: List[Int] = listProv.filter(
      prov => textProvincias.contains(prov)).map(
      prov => textProvincias.split(prov)(1).split('/')(0).split('|')(0).trim.replace("*","").toInt )
    listProv.zip(casesProv).toMap
  }
  def getCasesByProvFromTextString_v2(text: String) :Map[String,Int] ={
    val textProvincias: String = text.split("Detalle por provincia")(1)
    textProvincias.split("\n").map(
      line => ( getProvFromLine(line),getCasesFromLine(line) ) ).filter(
      x => x._1.isDefined & x._2 >= 0 ).map(
      x => (x._1.get,x._2)).toMap
    // TO DO: una vez q termina el listado de provncias, si llegara a haber una oracion que contena una provincia y un numero lo va a tomar
  }
  private def getProvFromLine(line: String): Option[String] = {
    val listProv: List[String] = List("Buenos Aires","CABA","Catamarca","Chaco","Chubut","Córdoba","Corrientes","Entre Ríos","Formosa","Jujuy","La Pampa","La Rioja","Mendoza","Misiones","Neuquén","Río Negro","Salta","San Juan","San Luis", "Santa Cruz","Santa Fe","Santiago del Estero","Tierra del Fuego","Tucumán")
    listProv.find( prov => lineContainsProv(line,prov) )
  }
  private def lineContainsProv(line:String,prov:String): Boolean ={
    prov match {
      case "CABA" => line.contains("Ciudad de Buenos Aires") | line.contains("Ciudad Autónoma de Buenos Aires")
      case "Buenos Aires" => line.contains("Buenos Aires") & ! line.contains("Ciudad de Buenos Aires") & ! line.contains("Ciudad Autónoma de Buenos Aires")
      case value => line.contains(value)
    }
  }
  private def getCasesFromLine(line: String): Int ={
    val pattern: Regex = "([0-9]+)".r
    val cases = pattern.findFirstMatchIn(line) match {
      case Some(value) => value.toString().toInt
      case None => -1
    }
    cases
  }
}