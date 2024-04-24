package exercises.typeclass.laws.discipline

import exercises.typeclass.laws.EqLaws
import org.typelevel.discipline.Laws

trait EqTests[A] extends Laws {
  def laws: EqLaws[A]

  // TODO #14: Define a RuleSet containing the laws in EqLaws


  // TODO #15: Define a companion object with an 'apply' method so that we can
  //           easily instantiate tests with e.g. EqTests[Int]
}
