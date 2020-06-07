package gb.io.snow.scraper.services

import org.jsoup.Jsoup
import org.jsoup.select.Elements
import collection.JavaConverters._

trait UrlExtractor {
  def getUrls(url: String): List[String]
}

case class UrlExtractorImpl() extends UrlExtractor {
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