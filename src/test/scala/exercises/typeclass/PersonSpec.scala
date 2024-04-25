package exercises.typeclass

import exercises.typeclass.laws.discipline.EqTests

class PersonSpec extends MySpec {
  // TODO #17: Write tests for additional Eq instances defined in Person using
  //           Discipline and the 'checkAll' method
  checkAll("Eq[Person] by name", EqTests(Person.Instances.personNameEq).eq)
  checkAll("Eq[Person] by id", EqTests(Person.Instances.personIdEq).eq)
}