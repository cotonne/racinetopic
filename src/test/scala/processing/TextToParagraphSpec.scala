package processing

import org.scalatest._

import scala.io.Source

class TextToParagraphSpec extends FlatSpec with Matchers {

  val html = Source.fromInputStream(getClass.getResourceAsStream("/pg15790.html")).getLines().mkString("\n")

  val paragraphs = TextToParagraph.transform("id00056", "id00614")(html)


  "cleanParagraph" should "remove blank lines, trims and remove final number" in {
    val initialText =
      """
        |  Est-ce toi, chere Élise?  O jour trois fois heureux!
        |
        |  Que béni soit le del qui te rend à mes voeux,
        |
        |  Toi qui de Benjamin comme moi descendue,
        |
        |  Fus de mes premiers ans la compagne assidue,
        |
        |  Et qui, d'un même joug souffrant l'oppression,               5
        |
        |  M'aidais à soupirer les malheurs de Sion.
        |
        |  Combien ce temps encore est cher à ma mémoire!
        |
        |  Mais toi, de ton Esther ignorais-tu la gloire?
        |
        |  Depuis plus de six mois que je te fais chercher,
        |
        |  Quel climat, quel désert a donc pu te cacher?               10
      """.stripMargin
    val cleanText = TextToParagraph.cleanParagraph(("", initialText))._2

    print(cleanText(cleanText.length - 2).toInt)
    cleanText shouldEqual
      """Est-ce toi, chere Élise?  O jour trois fois heureux!
        |Que béni soit le del qui te rend à mes voeux,
        |Toi qui de Benjamin comme moi descendue,
        |Fus de mes premiers ans la compagne assidue,
        |Et qui, d'un même joug souffrant l'oppression,
        |M'aidais à soupirer les malheurs de Sion.
        |Combien ce temps encore est cher à ma mémoire!
        |Mais toi, de ton Esther ignorais-tu la gloire?
        |Depuis plus de six mois que je te fais chercher,
        |Quel climat, quel désert a donc pu te cacher?""".stripMargin
  }

  "Text" should "be transform in paragraph" in {
    paragraphs.find(_._1 == "id00071").head._2 shouldBe
      """Est-ce toi, chere Élise?  O jour trois fois heureux!
        |Que béni soit le del qui te rend à mes voeux,
        |Toi qui de Benjamin comme moi descendue,
        |Fus de mes premiers ans la compagne assidue,
        |Et qui, d'un même joug souffrant l'oppression,
        |M'aidais à soupirer les malheurs de Sion.
        |Combien ce temps encore est cher à ma mémoire!
        |Mais toi, de ton Esther ignorais-tu la gloire?
        |Depuis plus de six mois que je te fais chercher,
        |Quel climat, quel désert a donc pu te cacher?""".stripMargin
  }

  "Text" should "provide book content" in {
    val p = paragraphs.map(_._2)
    p should not contain "[1]These words recur most frequently in her later correspondence with\nSt. Cyr."
    p should not contain "3 ce lieu, of course is St. Cyr."
  }
}
