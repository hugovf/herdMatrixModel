package fr.cirad.herdmatrixmodel

import breeze.linalg._
import com.typesafe.scalalogging.Logger

object HerdMatrixModel extends App {

  override def main(args: Array[String]) = {

    if (args.isEmpty) Utils.logger.error("The parameter CSV file is missing (full path in argument)")
    else {
      val csvFile = new java.io.File(args.head)

      val demographicStages = Data.readFile(csvFile)

      demographicStages.foreach { stages =>
        val stageTransitionMatrix = Utils.stageTransitionMatrix(stages)
        val survivalMatrix = Utils.survivalMatrix(stages)
        val fecundityMatrix = Utils.fecundityMatrix(stages)

        val aMatrix = stageTransitionMatrix * survivalMatrix * fecundityMatrix
      }
    }

  }
}