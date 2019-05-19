package processing

import org.scalatest.{FunSpec, Matchers}

class LemmatizerTest extends FunSpec with Matchers {

  describe("Lemmatizer") {
    val lemmatizer: CustomFLLemmatizer = new CustomFLLemmatizer()
    val annotator = new FrenchLemmaAndPOSAnnotator(lemmatizer)
    it("should correctly recognize lemmes") {
      val lemme = annotator.transform(("aidais", "vinf"))
      lemme shouldBe("aider", "VERB")
    }
  }

}
