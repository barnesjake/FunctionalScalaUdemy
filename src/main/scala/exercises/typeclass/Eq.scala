package exercises.typeclass

trait Eq[A] {
  // TODO #1: Define an 'eq' method that takes two A values as parameters, and returns a Boolean
  def eq(`this`: A, `that`: A): Boolean
}

object Eq {
  // TODO #2: Define the method 'apply' so we can summon instances from implicit scope
  def apply[A](implicit thing: Eq[A]): Eq[A] = thing

  // TODO #3: Define the method 'instance' so we can build instances of the Eq typeclass more easily.
  //          It should take as the only parameter a function of type (A, A) => Boolean
  def instance[A](f: (A, A) => Boolean): Eq[A] = new Eq[A] {
    override def eq(`this`: A, `that`: A): Boolean = f(`this`, `that`)
  }

  // TODO #4: Define an Eq instance for String
  implicit val stringEq: Eq[String] = instance[String]((str1, str2) => str1 == str2)

  // TODO #5: Define an Eq instance for Int
  implicit val intEq: Eq[Int] = instance[Int]((int1, int2) => int1 == int2)

  // TODO #6: Define an Eq instance for Person. Two persons are equal if both their names and ids are equal.
  //          Extra points: receive implicit instances for String and Int and use them
  implicit val personEq: Eq[Person] = instance[Person] { (person1, person2) =>
    (person1.name == person2.name) && (person1.id == person2.id)
    Eq[String].eq(person1.name, person2.name) && Eq[Int].eq(person1.id, person2.id)
  }

  // TODO #7: Provide a way to automatically derive instances for Eq[Option[A]] given that we have an implicit
  //          instance for Eq[A]
  implicit def optionEq[A](implicit eqA: Eq[A]): Eq[Option[A]] = instance[Option[A]] { (maybe1, maybe2) =>
    maybe2.flatMap(b => maybe1.map(a => eqA.eq(a, b))).getOrElse(false)
  }

  object Syntax {
    // TODO #8: Define a class 'EqOps' with a method 'eqTo' that enables the following syntax:
    //          "hello".eqTo("world")
    implicit class EqOps[A](a1: A) {
      def eqTo(a2: A)(implicit eqA: Eq[A]): Boolean = eqA.eq(a1, a2)
    }
  }
}

object Testing extends App {

  import Eq.Syntax._

  println("hello".eqTo("world"))
  println("foobar".eqTo("foobar"))
  println(1.eqTo(2))
  println(1.eqTo(1))
}
