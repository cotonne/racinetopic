package processing

import org.scalatest.{FlatSpec, Matchers}

class NlpPipelineTest extends FlatSpec with Matchers {

  "NlpPipeline" should "transform a text to a normalized bag of words" in {
    val pipeline = NlpPipeline()
    val text = pipeline.plainTextToLemmas(
      """Est-ce toi, chere Élise?  O jour trois fois heureux!
        |Que béni soit le del qui te rend à mes voeux,
        |Toi qui de Benjamin comme moi descendue,
        |Et qui, d'un même joug souffrant l'oppression,
        |Fus de mes premiers ans la compagne assidue,
        |M'aidais à soupirer les malheurs de Sion.
        |Combien ce temps encore est cher à ma mémoire!
        |Mais toi, de ton Esther ignorais-tu la gloire?
        |Depuis plus de six mois que je te fais chercher,
        |Quel climat, quel désert a donc pu te cacher?""".stripMargin)
    text shouldBe Set(
      "chere", "élire", "jour", "trois", "fois", "heureux", "béni", "del", "rend", "voeux", "benjamin", "descendre", "joug", "souffrant", "oppression", "premier", "ans", "compagne", "assidue", "aidais", "soupirer", "malheur", "sion", "combien", "temps", "encore", "cher", "mémoire", "esther", "ignorais", "gloire", "depuis", "plus", "six", "mois", "fais", "chercher", "quel", "climat", "quel", "désert", "donc", "cacher")
  }

}
