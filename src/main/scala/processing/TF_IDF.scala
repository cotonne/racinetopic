package processing

import org.apache.spark.sql
import org.apache.spark.sql._
import org.apache.spark.sql.functions._
import org.apache.spark.ml.feature.CountVectorizer

class TF_IDF(private val spark: SparkSession) {

  import spark.implicits._


  private val ID_COLUMN_NAME = "id"

  def docTermFrequen(terms: DataFrame): ((Map[Long, String], Array[String]), DataFrame) = {
    val termsDF = terms.toDF(ID_COLUMN_NAME, "terms")
    // filter out all documents that have zero or one term
    val filtered = termsDF.where(size($"terms") > 1)


    val numTerms = 20000
    /**
      * The CountVectorizer is an Estimator that can help compute the term frequencies
      * for us. The CountVectorizer scans the data to build up a vocabulary, a mapping of
      * integers to terms, encapsulated in the CountVectorizerModel , a Transformer . The
      * CountVectorizerModel can then be used to generate a term frequency Vector for
      * each document. The vector has a component for each term in the vocabulary, and the
      * value for each component is the number of times the term appears in the document.
      *
      */
    val countVectorizer = new CountVectorizer().
      setInputCol("terms")
      .setOutputCol("termFreqs").
      setVocabSize(numTerms)
    val vocabModel = countVectorizer.fit(filtered)
    val docTermFreqs = vocabModel.transform(filtered)
    docTermFreqs.cache()
    val termIds: Array[String] = vocabModel.vocabulary
    val docIds: Map[Long, String] = docTermFreqs.rdd.map(_.getString(0)).
      zipWithUniqueId().
      map(_.swap).
      collect().toMap
    ((docIds, termIds), tfIdf(docTermFreqs))
  }

  private def tfIdf(docTermFreqs: sql.DataFrame): DataFrame = {
    import org.apache.spark.ml.feature.IDF
    val idf = new IDF().setInputCol("termFreqs").setOutputCol("tfidfVec")
    val idfModel = idf.fit(docTermFreqs)
    val docTermMatrix = idfModel.transform(docTermFreqs).select(ID_COLUMN_NAME, "tfidfVec")
    docTermMatrix
  }


}
