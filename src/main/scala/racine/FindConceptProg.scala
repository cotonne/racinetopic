package racine

import org.apache.spark.sql.{DataFrame, Dataset, SparkSession}
import org.slf4j.LoggerFactory
import processing.SVD.{DOC_x_CONCEPT, TERM_x_CONCEPT}
import processing.TF_IDF.{DOC_IDS, TERM_IDS}
import processing.TextToParagraph.ID
import processing.{NlpPipeline, SVD, TF_IDF, TextToParagraph}

import scala.collection.mutable.ArrayBuffer

object FindConceptProg {

  private val LOG = LoggerFactory.getLogger(FindConceptProg.getClass)

  def main(args: Array[String]): Unit = {

    val spark = SparkSession.builder
      .appName("FindConcept")
      .getOrCreate()

    import spark.implicits._

    val aBookByRacine = args(0)

    val book: Dataset[String] = spark.sparkContext.wholeTextFiles(aBookByRacine).toDS().map(_._2)


    val paragraphs: Dataset[(ID, String)] = book.flatMap(TextToParagraph.transform("id00056", "id00614"))

    val paragraphsAsBoW: Dataset[(String, Set[String])] = paragraphs.mapPartitions { iter =>
      val pipeline = NlpPipeline()
      iter.map { case (id, contents) =>
        (id, pipeline.normalize(contents))
      }
    }

    val tuple: ((DOC_IDS, TERM_IDS), DataFrame) = TF_IDF.docTermFrequen(spark, paragraphsAsBoW)

    LOG.info("Nombre de documents : " + tuple._1._1.size)
    LOG.info("Nombre de termes : " + tuple._1._2.length)

    val u_v: (DOC_x_CONCEPT, TERM_x_CONCEPT) = SVD.doIt(tuple._2)

    val topConceptTerms = topTermsInTopConcepts(u_v._2, 4, 10, tuple._1._2)
    val topConceptDocs = topDocsInTopConcepts(u_v._1, 4, 10, tuple._1._1)
    for ((terms, docs) <- topConceptTerms.zip(topConceptDocs)) {
      println("Concept terms: " + terms.map(_._1).mkString(", "))
      println("Concept docs: " + docs.map(_._1).mkString(", "))
      println()
    }
  }

  def topTermsInTopConcepts(
                             V: TERM_x_CONCEPT,
                             numConcepts: Int,
                             numTerms: Int,
                             termIds: TERM_IDS)
  : Seq[Seq[(String, Double)]] = {
    val topTerms = new ArrayBuffer[Seq[(String, Double)]]()
    val arr = V.toArray
    for (i <- 0 until numConcepts) {
      val offs = i * V.numRows
      val termWeights = arr.slice(offs, offs + V.numRows).zipWithIndex
      val sorted = termWeights.sortBy(-_._1)
      topTerms += sorted.take(numTerms).map {
        case (score, id) => (termIds(id), score)
      }
    }
    topTerms
  }

  def topDocsInTopConcepts(
                            U: DOC_x_CONCEPT,
                            numConcepts: Int,
                            numDocs: Int,
                            docIds: DOC_IDS)
  : Seq[Seq[(String, Double)]] = {
    val topDocs = new ArrayBuffer[Seq[(String, Double)]]()
    for (i <- 0 until numConcepts) {
      val docWeights = U.rows.map(_.toArray(i)).zipWithUniqueId()
      topDocs += docWeights.top(numDocs).map {
        case (score, id) => (docIds(id), score)
      }
    }
    topDocs
  }
}
