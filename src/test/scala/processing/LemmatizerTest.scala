package processing

import org.scalatest.{FunSpec, Matchers}

class LemmatizerTest extends FunSpec with Matchers {

  describe("Lemmatizer") {
    val lemmatizer = new FrenchLemmaAndPOSAnnotator()
    it("should correctly recognize lemmes") {
      val lemme = lemmatizer.transform(("aidais", "vinf"))
      lemme shouldBe("aider", "VERB")
    }
  }

}
