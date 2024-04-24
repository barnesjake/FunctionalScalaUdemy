package exercises.typeclass

import exercises.typeclass.Eq.instance

case class Person(name: String, id: Int)

object Person {
  object Instances {
    // TODO #9: Define an Eq instance for Person comparing them by name
    //          Extra points: receive an implicit instance for String and use it
    implicit def personNameEq(implicit eqString: Eq[String]): Eq[Person] = instance[Person] { (person1, person2) =>
      eqString.eq(person1.name, person2.name)
    }

    // TODO #10: Define an Eq instance for Person comparing them by id
    //           Extra points: receive an implicit instance for Int and use it
    implicit def personIdEq(implicit eqInt: Eq[Int]): Eq[Person] = instance[Person] { (person1, person2) =>
      eqInt.eq(person1.id, person2.id)
    }
  }
}
