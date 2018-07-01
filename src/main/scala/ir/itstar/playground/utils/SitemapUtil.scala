package ir.itstar.playground.utils

import scala.io.{BufferedSource, Source}
import scala.util.matching.Regex

object SitemapUtil {

  private def sitemapUrlPattern(domain: String, protocol: String = "http") =
    s"""<loc>(?:\\s*)$protocol://$domain(.*)(?:\\s*)</loc>""".r


  def extractUrls(body: String, domain: String, protocol: String = "http"): Iterator[Regex.Match] = {
    sitemapUrlPattern(domain, protocol).findAllIn(body).matchData
  }

  def readFileFromResource(name: String): BufferedSource = {
    Source.fromResource(name)
  }

}
