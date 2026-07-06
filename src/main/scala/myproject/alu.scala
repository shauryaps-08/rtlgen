package myproject
import chisel3._
import chisel3.util._

object AluOp {
  val ADD  = 0.U(3.W)
  val SUB  = 1.U(3.W)
  val AND  = 2.U(3.W)
  val OR   = 3.U(3.W)
  val XOR  = 4.U(3.W)
  val SLT  = 5.U(3.W)
}

class Alu(width: Int) extends Module {
  val io = IO(new Bundle {
    val a   = Input(UInt(width.W))
    val b   = Input(UInt(width.W))
    val op  = Input(UInt(3.W))
    val out = Output(UInt(width.W))
  })

  io.out := MuxLookup(io.op, 0.U)(Seq(
    AluOp.ADD -> (io.a + io.b),
    AluOp.SUB -> (io.a - io.b),
    AluOp.AND -> (io.a & io.b),
    AluOp.OR  -> (io.a | io.b),
    AluOp.XOR -> (io.a ^ io.b),
    AluOp.SLT -> (io.a < io.b)
  ))
}

