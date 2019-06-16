package racine

import java.io.{File, PrintWriter}

import org.apache.spark.sql.{DataFrame, Dataset, SparkSession}
import org.slf4j.LoggerFactory
import processing.SVD.{DOC_x_CONCEPT, TERM_x_CONCEPT}
import processing.TF_IDF.{DOC_IDS, TERM_IDS}
import processing.TextToParagraph.{ACTOR, ID}
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


    val paragraphs: Dataset[(ID, (ACTOR, String))] = book.flatMap(TextToParagraph.transform("id00056", "id00614"))

    val paragraphsAsBoW: Dataset[(String, Set[String])] = paragraphs.mapPartitions { iter =>
      val pipeline = NlpPipeline()
      iter.map { case (id, contents) =>
        (id, pipeline.normalize(contents._1, contents._2))
      }
    }

    val tuple: ((DOC_IDS, TERM_IDS), DataFrame) = TF_IDF.docTermFrequen(spark, paragraphsAsBoW)

    val termsId = tuple._1._2
    val writer = new PrintWriter(new File("termsId.txt"))
    writer.write(termsId.mkString("\n"))
    writer.close()

    LOG.info("Nombre de documents : " + tuple._1._1.size)
    LOG.info("Nombre de termes : " + termsId.length)

    val u_v: (DOC_x_CONCEPT, TERM_x_CONCEPT) = SVD.doIt(tuple._2)

    val topConceptTerms = topTermsInTopConcepts(u_v._2, 10, 10, termsId)
    val topConceptDocs = topDocsInTopConcepts(u_v._1, 10, 10, tuple._1._1)
    for ((terms, docs) <- topConceptTerms.zip(topConceptDocs)) {
      println("Concept terms: " + terms.map(_._1).mkString(", "))
      println("Concept docs: " + docs.map(_._1).mkString(", "))
      println()
    }

    val wordsCount = paragraphsAsBoW.flatMap(x => x._2)
      .map(word => (word, 1))
      .rdd
      .reduceByKey((a, b) => a + b)
      .filter { case (k, v) => v > 1 }
      .map { case (k, v) => (k + " ") * v }
    wordsCount.saveAsTextFile("wordcount")
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
