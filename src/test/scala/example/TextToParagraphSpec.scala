package example

import java.io.File

import org.scalatest._

class TextToParagraphSpec extends FlatSpec with Matchers {

  val html = new File(getClass.getResource("/pg15790.html").getPath)

  val paragraphs: Array[String] = TextToParagraph.transform(html, "id00056")


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
    val cleanText = TextToParagraph.cleanParagraph(initialText)

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
    paragraphs should contain(
      """Est-ce toi, chere Élise?  O jour trois fois heureux!
        |Que béni soit le del qui te rend à mes voeux,
        |Toi qui de Benjamin comme moi descendue,
        |Fus de mes premiers ans la compagne assidue,
        |Et qui, d'un même joug souffrant l'oppression,
        |M'aidais à soupirer les malheurs de Sion.
        |Combien ce temps encore est cher à ma mémoire!
        |Mais toi, de ton Esther ignorais-tu la gloire?
        |Depuis plus de six mois que je te fais chercher,
        |Quel climat, quel désert a donc pu te cacher?""".stripMargin)
  }

  "Text" should "provide book content" in {
    paragraphs should not contain "[1]These words recur most frequently in her later correspondence with\nSt. Cyr."
  }
}
