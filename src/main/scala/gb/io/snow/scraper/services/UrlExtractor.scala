package gb.io.snow.scraper.services

import gb.io.snow.scraper.Run.dictMonth
import org.jsoup.Jsoup
import org.jsoup.select.Elements

import collection.JavaConverters._

trait UrlExtractor {
  def getUrls(url: String): List[String]
}

case class DayUrlExtractor() extends UrlExtractor {
  override def getUrls(url: String): List[String] ={
    // TO DO: check url exists
    //"downloads panel-pane pane-entity-field pane-node-field-download"
    val doc: Elements = Jsoup.connect(url).get().body().getElementsByClass("row row-flex")
    val urls: Elements = doc.select("a[href]")
    val allUrls: List[String] = urls.eachAttr("href").asScala.toList
    val urlFinal: List[String] = allUrls.filter(element => ! element.toString.contains("matutino"))
    urlFinal
  }
}

case class MonthUrlExtractor() extends UrlExtractor {
  override def getUrls(url: String): List[String] ={
    // TO DO: check url exists
    //val testPage : String = "https://www.argentina.gob.ar/coronavirus/informes-diarios/reportes"
    val allMonthsUrls = Jsoup.connect(url).get().body().getElementsByClass("active").select("a[href]")
    println( Jsoup.connect(url).get().body().getElementsByClass("active").select("a[href]") )
    val listAllMonthsUrls: List[String] = allMonthsUrls.eachAttr("href").asScala.toList
    println(listAllMonthsUrls)
    val listAllCompletedMonthsUrls: List[String] = listAllMonthsUrls.map(element => "https://www.argentina.gob.ar"+element.toString)
    def stringCointainsSomeOfList(s:String, list:List[String]) : Boolean ={
      list.exists(m => s.toLowerCase.contains(m.toLowerCase) )
    }
    val finalMonthsUrls: List[String] = listAllCompletedMonthsUrls.filter(s => stringCointainsSomeOfList(s, dictMonth.values.toList ) )
    println( finalMonthsUrls)
    finalMonthsUrls
  }
}