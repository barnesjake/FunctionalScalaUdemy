import java.nio.ByteBuffer

trait ByteEncoder[A] {
  def encode(a: A): Array[Byte]
}

object ByteEncoder {
  //  implicit val stringByteEncoder: ByteEncoder[String] = new ByteEncoder[String] {
  //    override def encode(s: String): Array[Byte] = s.getBytes
  //  }
  implicit val stringByteEncoder: ByteEncoder[String] = instance[String](s => s.getBytes)
  implicit val intByteEncoder: ByteEncoder[Int] = instance[Int] { n =>
    val bb = ByteBuffer.allocate(4)
    bb.putInt(n)
    bb.array()
  }

  //  implicit val optionStringEncoder: ByteEncoder[Option[String]] = {
  //    case Some(value) => stringByteEncoder.encode(value)
  //    case None => Array[Byte]()
  //  }

  implicit def optionEncoder[A](implicit en: ByteEncoder[A]): ByteEncoder[Option[A]] = {
    case Some(value) => en.encode(value)
    case None => Array[Byte]()
  }


  def apply[A](implicit ev: ByteEncoder[A]): ByteEncoder[A] = ev

  def instance[A](f: A => Array[Byte]): ByteEncoder[A] = new ByteEncoder[A] {
    override def encode(a: A): Array[Byte] = f(a)
  }
}

//implicit object Rot3StringByteEncoder extends ByteEncoder[String] {
//  override def encode(a: String): Array[Byte] = a.getBytes.map(b => (b + 3).toByte)
//}
//implicit val Rot3StringByteEncoder: ByteEncoder[String] =
//  ByteEncoder.instance[String](_.getBytes.map(b => (b + 3).toByte))

trait ByteDecoder[A] {
  def decode(bytes: Array[Byte]): A
}

object ByteDecoder {
  implicit val stringByteDecoder: ByteDecoder[String] =
    instance[String]((a: Array[Byte]) => a.map(_.toChar).mkString)

  implicit val intByteDecoder: ByteDecoder[Int] =
    instance[Int]{ (a: Array[Byte]) =>
        val bb = ByteBuffer.allocate(4)
        bb.put(a)
        bb.flip()
        bb.getInt
    }

  def apply[A](implicit ev: ByteDecoder[A]): ByteDecoder[A] = ev

  def instance[A](f: Array[Byte] => A): ByteDecoder[A] = new ByteDecoder[A] {
    override def decode(bytes: Array[Byte]): A = f(bytes)
  }
}

ByteDecoder[String].decode(Array(98, 105, 101, 110, 32, 58, 41))

ByteEncoder[String].encode("hello")
ByteEncoder[Int].encode(123)
ByteEncoder[Option[String]].encode(Some("hello"))
ByteEncoder[Option[String]].encode(None)
ByteEncoder[Option[Int]].encode(Some(123))
ByteEncoder[Option[Int]].encode(None)

//--------------------------------------------------------------------------------
//implicit class ByteEncoderOps[A](a: A) {
//making it a value class as below improves performance
implicit class ByteEncoderOps[A](val a: A) extends AnyVal {
  def encode(implicit enc: ByteEncoder[A]): Array[Byte] = {
    enc.encode(a)
  }
}

//implicit class ByteDecoderOps[A](a: Array[Byte]){
implicit class ByteDecoderOps[A](val a: Array[Byte]) extends AnyVal {
  def decode[A](implicit dec: ByteDecoder[A]): A = {
    dec.decode(a)
  }
}

5.encode
"hello world".encode
Array[Byte](0, 0, 0, 5).decode[Int]
Array[Byte](104, 101, 108, 108, 111, 32, 119, 111, 114, 108, 100).decode

