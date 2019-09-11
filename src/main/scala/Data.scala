package fr.cirad.herdmatrixmodel

object Data {

  def readFile(csvFile: java.io.File): DemographicStages = {

    val bufferedSource = io.Source.fromFile(csvFile)
    val stages = (for {
      line <- bufferedSource.getLines().toSeq.tail
    } yield {
      val cols = line.split(",").map(_.trim)
      Stage(
        if (cols(0) == "F") Female else Male,
        cols(1).toInt,
        cols(2),
        cols(3),
        cols(4),
        cols(5).toDouble,
        cols(6).toDouble,
        cols(7).toDouble,
        cols(8).toDouble,
        cols(9).toDouble,
        cols(10).toDouble
      )
    }).toList

    bufferedSource.close
    stages
  }



  implicit def stringToMonth(s: String): Month = {
    if (s == "Inf") Infinite
    else Length(s.toInt)
  }

  type DemographicStages = List[Stage]

  implicit class DemoStagesDecorator(d:DemographicStages) {

    def females = d.filter{_.sex == Female}

    def males = d.filter{_.sex == Male}
  }

  trait Sex

  object Female extends Sex

  object Male extends Sex

  trait Month

  object Infinite extends Month

  case class Length(value: Int) extends Month

  case class Stage(
                    sex: Sex,
                    ageClass: Int,
                    ageClassDuration: Month,
                    ageClassStart: Month,
                    ageClassEnd: Month,
                    parturitionRate: Double,
                    femaleFecundity: Double,
                    maleFecundity: Double,
                    deathProbability: Double,
                    offtakeProbability: Double,
                    growingRate: Double
                  )

}
