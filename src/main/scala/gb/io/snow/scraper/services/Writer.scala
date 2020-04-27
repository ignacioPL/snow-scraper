package gb.io.snow.scraper.services

import gb.io.snow.scraper.models.CovidData

trait Writer {
  def writeCsv(csvFile: String, data: CovidData)
}
