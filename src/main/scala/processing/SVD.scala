package processing

import org.apache.spark.mllib.linalg.{Matrix, Vectors, Vector => MLLibVector}
import org.apache.spark.ml.linalg.{Vector => MLVector}
import org.apache.spark.mllib.linalg.distributed.RowMatrix
import org.apache.spark.sql.DataFrame

class SVD {

  type TERM_x_CONCEPT = Matrix

  type DOC_x_CONCEPT = RowMatrix

  def doIt(docTermMatrix: DataFrame): (DOC_x_CONCEPT, TERM_x_CONCEPT ) = {
    val vecRdd = docTermMatrix.select("tfidfVec").rdd.map { row =>
      Vectors.fromML(row.getAs[MLVector]("tfidfVec"))
    }
    vecRdd.cache()
    val mat = new RowMatrix(vecRdd)
    val k = 1000
    val svd = mat.computeSVD(k, computeU=true)
    (svd.U, svd.V)
  }

}
