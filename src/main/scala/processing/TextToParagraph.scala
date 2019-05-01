package processing

import org.apache.commons.text.StringEscapeUtils
import org.htmlcleaner.{HtmlCleaner, TagNode}


object TextToParagraph {
  private val cleaner = new HtmlCleaner

  type Paragraph = String
  type ID = String

  def transform(from: String, to: String): String => List[(ID, String)] = (readHtml andThen
    keepParagraphs andThen
    takeFrom(from) andThen
    takeTo(to) andThen
    convertToString andThen
    cleanParagraphs
    ) (_)

  val readHtml: String => TagNode = cleaner.clean

  def takeFrom(from: String): List[TagNode] => List[TagNode] = _.dropWhile(node => node.getAttributeByName("id") != from)

  def takeTo(to: String): List[TagNode] => List[TagNode] = _.takeWhile(node => node.getAttributeByName("id") != to)

  def keepParagraphs(rootNode: TagNode): List[TagNode] = rootNode.getElementsByName("p", true).toList

  def convertToString(nodes: List[TagNode]): List[(ID, String)] = nodes.map(x => (x.getAttributeByName("id"), x.getText.toString))

  def cleanParagraphs(paragraphs: List[(ID, String)]): List[(ID, String)] = paragraphs
    .map(cleanParagraph)

  def cleanParagraph(paragraph: (ID, String)): (ID, String) = (paragraph._1,
    StringEscapeUtils.unescapeHtml4(paragraph._2).lines.
      map(removeTrailingNumber).
      map(_.replace("\u00a0", "")).
      filter(!_.isEmpty).
      map(_.trim).
      mkString("\n").trim)

  private def removeTrailingNumber: String => String = _.replaceAll("[1-9]*[05]$", "")
}
