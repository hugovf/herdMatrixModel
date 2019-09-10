package fr.cirad.herdmatrixmodel

import breeze.linalg._

object HerdMatrixModel extends App {

  override def main(args: Array[String]) = {
    println("Herd Matrix Model !")

    val vect = DenseVector.zeros[Double](5)

    println("Vect " + vect)

  }
}