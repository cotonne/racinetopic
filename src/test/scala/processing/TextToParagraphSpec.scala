package processing

import org.scalatest._

import scala.io.Source

class TextToParagraphSpec extends FlatSpec with Matchers {

  private val html = Source.fromInputStream(getClass.getResourceAsStream("/pg15790.html")).getLines().mkString("\n")

  private val paragraphs = TextToParagraph.transform("id00056", "id00614")(html)


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
    paragraphs.find(_._1 == "id00071").head._2 shouldBe
      ("ESTHER.", """Est-ce toi, chere Élise?  O jour trois fois heureux!
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
    val p = paragraphs.map(_._2)
    p should not contain "[1]These words recur most frequently in her later correspondence with\nSt. Cyr."
    p should not contain "3 ce lieu, of course is St. Cyr."
  }

  "Text" should "be linked to his author" in {
    val aHtml =
      """
        |<html>
        |<head></head>
        |<body>
        |<h4 id="id00055">  LA PIETE.</h4>
        |
        |<p id="id00056">&nbsp;&nbsp;Du séjour bienheureux de la Divinité
        |
        |&nbsp;&nbsp;Je descends dans ce lieu, par la Grace habité.
        |
        |&nbsp;&nbsp;L'Innocence s'y plaît, ma compagne éternelle,
        |
        |&nbsp;&nbsp;Et n'a point sous les cieux d'asile plus fidèle.
        |
        |&nbsp;&nbsp;Ici, loin du tumulte, aux devoirs les plus saints            5
        |
        |&nbsp;&nbsp;Tout un peuple naissant est formé par mes mains.
        |
        |&nbsp;&nbsp;Je nourris dans son coeur la semence féconde
        |
        |&nbsp;&nbsp;Des vertus dont il doit sanctifier le monde.
        |
        |&nbsp;&nbsp;Un roi qui me protége, un roi victorieux,
        |
        |&nbsp;&nbsp;A commis à mes soins ce dépôt précieux.                     10
        |
        |&nbsp;&nbsp;C'est lui qui rassembla ces colombes timides,
        |
        |&nbsp;&nbsp;Éparses en cent lieux, sans secours et sans guides.
        |
        |&nbsp;&nbsp;Pour elles à sa porte élevant ce palais,
        |
        |&nbsp;&nbsp;Il leur y fit trouver l'abondance et la paix.
        |</p>
        |
        |<p id="id00058">&nbsp;&nbsp;Tu lui donnes un fils prompt à le seconder,
        |
        |&nbsp;&nbsp;Qui sait combattre, plaire, obéir, commander;               50
        |
        |&nbsp;&nbsp;Un fils qui, comme lui, suivi de la victoire,
        |
        |&nbsp;&nbsp;Semble à gagner son coeur borner toute sa gloire,
        |
        |&nbsp;&nbsp;Un fils à tous ses vceux avec amour soumis,
        |
        |&nbsp;&nbsp;L'éternel désespoir de tous ses ennemis.
        |
        |&nbsp;&nbsp;Pareil à ces esprits que ta Justice envoie,                 55
        |
        |&nbsp;&nbsp;Quand son roi lui dit: «Pars», il s'élance avec joie,
        |
        |&nbsp;&nbsp;Du tonnerre vengeur s'en va tout embraser,
        |
        |&nbsp;&nbsp;Et tranquille à ses pieds revient le déposer.
        |</p>
        |</body></html>
      """.stripMargin

    val tuples = TextToParagraph.transform("id00055", "id00614")(aHtml)

    tuples shouldEqual Seq(
      ("id00056",
        ("LA PIETE.",
          """Du séjour bienheureux de la Divinité
            |Je descends dans ce lieu, par la Grace habité.
            |L'Innocence s'y plaît, ma compagne éternelle,
            |Et n'a point sous les cieux d'asile plus fidèle.
            |Ici, loin du tumulte, aux devoirs les plus saints
            |Tout un peuple naissant est formé par mes mains.
            |Je nourris dans son coeur la semence féconde
            |Des vertus dont il doit sanctifier le monde.
            |Un roi qui me protége, un roi victorieux,
            |A commis à mes soins ce dépôt précieux.
            |C'est lui qui rassembla ces colombes timides,
            |Éparses en cent lieux, sans secours et sans guides.
            |Pour elles à sa porte élevant ce palais,
            |Il leur y fit trouver l'abondance et la paix.""".stripMargin)),
      ("id00058",
        ("LA PIETE.",
          """Tu lui donnes un fils prompt à le seconder,
            |Qui sait combattre, plaire, obéir, commander;
            |Un fils qui, comme lui, suivi de la victoire,
            |Semble à gagner son coeur borner toute sa gloire,
            |Un fils à tous ses vceux avec amour soumis,
            |L'éternel désespoir de tous ses ennemis.
            |Pareil à ces esprits que ta Justice envoie,
            |Quand son roi lui dit: «Pars», il s'élance avec joie,
            |Du tonnerre vengeur s'en va tout embraser,
            |Et tranquille à ses pieds revient le déposer.""".stripMargin))
    )
  }
}
