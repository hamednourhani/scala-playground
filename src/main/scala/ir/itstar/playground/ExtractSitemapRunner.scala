package ir.itstar.playground

import ir.itstar.playground.utils.SitemapUtil._

object ExtractSitemapRunner extends App {

  val orderSitemapUrls =
    readFileFromResource("sitemap.xml").mkString("")
  val matchData = extractUrls(orderSitemapUrls, "example.com", "https")
  val list = for (i <- matchData) yield i.group(1)
  println(list.size)
  list.foreach(println)


}
