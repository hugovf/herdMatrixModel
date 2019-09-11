package fr.cirad.herdmatrixmodel

import com.typesafe.scalalogging.Logger
import fr.cirad.herdmatrixmodel.Data.DemographicStages
import breeze.linalg._

object Utils {

  val logger = Logger("HerdMatrixModel")


  def fecundityMatrix(stages: DemographicStages) = {

    val numberOfFemaleStages = stages.females.length
    val numberOfMaleStages = stages.males.length

    val femaleFecundities = DenseVector(stages.females.tail.map {_.femaleFecundity}: _ *).toDenseMatrix
    val maleFecundities = DenseVector(stages.females.tail.map {_.maleFecundity}: _*).toDenseMatrix

    val femaleFecundityMatrix = DenseMatrix.horzcat(
      DenseMatrix.vertcat(femaleFecundities, DenseMatrix.eye[Double](numberOfFemaleStages - 1)),
      DenseMatrix.zeros[Double](numberOfFemaleStages, numberOfMaleStages - 1)
    )

    val maleFecundityMatrix = DenseMatrix.horzcat(
      DenseMatrix.vertcat(maleFecundities, DenseMatrix.zeros[Double](numberOfMaleStages - 1, numberOfFemaleStages - 1)),
      DenseMatrix.vertcat(DenseMatrix.zeros[Double](1, numberOfMaleStages - 1), DenseMatrix.eye[Double](numberOfMaleStages - 1)),
    )

    DenseMatrix.vertcat(
      femaleFecundityMatrix,
      maleFecundityMatrix
    )
  }

  def stageTransitionMatrix(stages: DemographicStages) = {

    val numberOfFemaleStages = stages.females.length
    val numberOfMaleStages = stages.males.length

    val femaleGrowth = DenseVector(stages.females.map {_.growingRate}: _ *)
    val maleGrowth = DenseVector(stages.males.map {_.growingRate}: _*)

    val remainFemaleMatrix = DenseMatrix.horzcat(
      diag(femaleGrowth(0 to -2)),
      DenseMatrix.zeros[Double](numberOfFemaleStages - 1 , 1)
    )

    val changeStageFemaleMatrix = DenseMatrix.horzcat(
      DenseMatrix.zeros[Double](numberOfFemaleStages - 1 , 1),
      diag(femaleGrowth(1 to -1).map( 1 - _))
    )

    val growthFemaleMatrix = remainFemaleMatrix + changeStageFemaleMatrix


    val remainMaleMatrix = DenseMatrix.horzcat(
      diag(maleGrowth(0 to -2)),
      DenseMatrix.zeros[Double](numberOfMaleStages - 1 , 1)
    )

    val changeStageMaleMatrix = DenseMatrix.horzcat(
      DenseMatrix.zeros[Double](numberOfMaleStages - 1 , 1),
      diag(maleGrowth(1 to -1).map( 1 - _))
    )

    val growthMaleMatrix = remainMaleMatrix + changeStageMaleMatrix


    DenseMatrix.vertcat(
      DenseMatrix.horzcat(growthFemaleMatrix, DenseMatrix.zeros[Double](numberOfFemaleStages - 1, numberOfMaleStages)),
      DenseMatrix.horzcat(DenseMatrix.zeros[Double](numberOfMaleStages - 1, numberOfFemaleStages), growthMaleMatrix)
    )
  }

  def survivalMatrix(stages: DemographicStages) = {
    diag(DenseVector(stages.map{s=> 1 - s.deathProbability - s.offtakeProbability}: _*))
  }

}
