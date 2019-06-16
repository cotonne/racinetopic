package processing

import java.io.{File, PrintWriter}

import org.apache.spark.ml.linalg.{Vector => MLVector}
import org.apache.spark.mllib.linalg.distributed.RowMatrix
import org.apache.spark.mllib.linalg.{DenseMatrix, Matrix, Vectors}
import org.apache.spark.sql.DataFrame

object SVD {

  type TERM_x_CONCEPT = Matrix

  type DOC_x_CONCEPT = RowMatrix

  def doIt(docTermMatrix: DataFrame): (DOC_x_CONCEPT, TERM_x_CONCEPT) = {
    val vecRdd = docTermMatrix.select("tfidfVec").rdd.map { row =>
      Vectors.fromML(row.getAs[MLVector]("tfidfVec"))
    }
    vecRdd.cache()

    val mat = new RowMatrix(vecRdd)

    val k = 100
    val svd = mat.computeSVD(k, computeU = true)

    val array: Array[Double] = vecRdd.collect().flatMap(_.toArray)
    val dm = new DenseMatrix(mat.numRows().toInt, mat.numCols().toInt, array)
    print("Num row = " + mat.numRows() + "\n")
    print("Num col = " + mat.numCols() + "\n")
    val writer = new PrintWriter(new File("test.txt"))
    // sed -r 's/ {1,}/,/g' test.txt > test.csv
    writer.write(dm.toString(10000, 10000000))
    writer.close()

    (svd.U, svd.V)
  }

}
