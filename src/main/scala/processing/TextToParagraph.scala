package processing

import java.io.File

import org.apache.commons.text.StringEscapeUtils
import org.htmlcleaner.{HtmlCleaner, TagNode}


object TextToParagraph {
  private val cleaner = new HtmlCleaner

  type Paragraph = String
  type ID = String

  def transform(file: File, from: String): Array[(ID, String)] = (readHtml andThen
    keepParagraphs andThen
    takeFrom(from) andThen
    convertToString andThen
    cleanParagraphs
    ) (file)

  val readHtml: File => TagNode = (file: File) => cleaner.clean(file)

  def takeFrom(from: String): Array[TagNode] => Array[TagNode] = _.dropWhile(node => node.getAttributeByName("id") != from)

  def keepParagraphs(rootNode: TagNode): Array[TagNode] = rootNode.getElementsByName("p", true)

  def convertToString(nodes: Array[TagNode]): Array[(ID, String)] = nodes.map(x => (x.getAttributeByName("id"), x.getText.toString))

  def cleanParagraphs(paragraphs: Array[(ID, String)]): Array[(ID, String)] = paragraphs
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
