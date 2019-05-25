package processing

import org.apache.commons.text.StringEscapeUtils
import org.htmlcleaner.conditional.TagNodeNameCondition
import org.htmlcleaner.{HtmlCleaner, TagNode}

import scala.collection.JavaConverters._

object TextToParagraph {
  private val cleaner = new HtmlCleaner
  type BagOfWords = Set[String]
  type POSBagOfWords = Map[String, String]
  type ACTOR = String
  type Paragraph = String
  type ID = String
  type TAG = String // h5 | p

  def transform(from: String, to: String): String => List[(ID, (String, String))] = (readHtml andThen
    keepActorAndTexts andThen
    takeFrom(from) andThen
    takeTo(to) andThen
    extractText andThen
    cleanParagraphs andThen
    joinActorAndText
    ) (_)

  val readHtml: String => TagNode = cleaner.clean

  def takeFrom(from: String): List[TagNode] => List[TagNode] = _.dropWhile(node => node.getAttributeByName("id") != from)

  def takeTo(to: String): List[TagNode] => List[TagNode] = _.takeWhile(node => node.getAttributeByName("id") != to)

  private val TAGS = new AllCondition(
    new TagNodeNameCondition("p"),
    new TagNodeNameCondition("h4"),
    new TagNodeNameCondition("h5"))

  def keepActorAndTexts(rootNode: TagNode): List[TagNode] = rootNode.getElementList(TAGS, true).asScala.toList

  type A = (ID, String, TAG)

  def extractText(nodes: List[TagNode]): List[A] = nodes.map(x => (x.getAttributeByName("id"), x.getText.toString, x.getName.toLowerCase))

  def cleanParagraphs(paragraphs: List[A]): List[A] = paragraphs
    .map(x => (x._1, cleanParagraph(x._2), x._3))

  def cleanParagraph(text: String): String = StringEscapeUtils.unescapeHtml4(text).lines.
    map(removeTrailingNumber).
    map(_.replace("\u00a0", "")).
    filter(!_.isEmpty).
    map(_.trim).
    mkString("\n").trim

  type B = (ACTOR, List[(ID, (ACTOR, String))])

  def joinActorAndText(paragraphs: List[A]): List[(ID, (ACTOR, String))] = {
    val ZERO: B = ("", List[(ID, (ACTOR, String))]())
    val result: B = paragraphs.foldLeft(ZERO)((b, a) => TextToParagraph.build(b, a))
    result._2
  }

  def build(currentActorAndParagraphs: (B, A)): B = {
    val value: B = currentActorAndParagraphs._1
    val currentElement: A = currentActorAndParagraphs._2
    val tag = currentElement._3
    if (tag == "h5" || tag == "h4") {
      val actor = currentElement._2
      (actor, value._2)
    } else {
      val paragraph: (ID, (ACTOR, String)) = (currentElement._1, (value._1, currentElement._2))
      (value._1, value._2 :+ paragraph)
    }
  }

  private def removeTrailingNumber: String => String = _.replaceAll("[1-9]*[05]$", "")
}