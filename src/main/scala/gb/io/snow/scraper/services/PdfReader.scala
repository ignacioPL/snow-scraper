package gb.io.snow.scraper.services

import gb.io.snow.scraper.models.CovidData

trait PdfReader {
  def readPdf(filePath: String): CovidData
}
