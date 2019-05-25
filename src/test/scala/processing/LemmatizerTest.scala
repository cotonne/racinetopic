package processing

import dk.dren.hunspell.Hunspell
import org.scalatest.{FunSpec, Matchers}

class LemmatizerTest extends FunSpec with Matchers {

  describe("Lemmatizer") {
    val lemmatizer: CustomFLLemmatizer = new CustomFLLemmatizer()
    val hunspellDictionary: Hunspell#Dictionary = Hunspell.getInstance.getDictionary("dictionaries/fr_FR/fr")
    val annotator = new FrenchLemmaAndPOSAnnotator(lemmatizer, hunspellDictionary)
    it("should correctly recognize lemmes") {
      val lemme = annotator.transform(("aidais", "vinf"))
      lemme shouldBe("aider", "VERB")
    }
  }

}
