trait ByteEncoder[A] {
  def encode(a: A): Array[Byte]
}

object ByteEncoder {
  //  implicit val stringByteEncoder: ByteEncoder[String] = new ByteEncoder[String] {
  //    override def encode(s: String): Array[Byte] = s.getBytes
  //  }
  implicit val stringByteEncoder: ByteEncoder[String] = instance[String](s => s.getBytes)

  def apply[A](implicit ev: ByteEncoder[A]): ByteEncoder[A] = ev

  def instance[A](f: A => Array[Byte]): ByteEncoder[A] = new ByteEncoder[A] {
    override def encode(a: A): Array[Byte] = f(a)
  }
}

//implicit object Rot3StringByteEncoder extends ByteEncoder[String] {
//  override def encode(a: String): Array[Byte] = a.getBytes.map(b => (b + 3).toByte)
//}
implicit val Rot3StringByteEncoder: ByteEncoder[String] =
  ByteEncoder.instance[String](_.getBytes.map(b => (b + 3).toByte))

trait ByteDecoder[A] {
  def decode(bytes: Array[Byte]): A
}

object ByteDecoder {
  implicit val stringByteDecoder: ByteDecoder[String] =
    instance[String]((a: Array[Byte]) => a.map(_.toChar).mkString)

  def apply[A](implicit ev: ByteDecoder[A]): ByteDecoder[A] = ev

  def instance[A](f: Array[Byte] => A): ByteDecoder[A] = new ByteDecoder[A] {
    override def decode(bytes: Array[Byte]): A = f(bytes)
  }
}

ByteDecoder[String].decode(Array(98, 105, 101, 110, 32, 58, 41))