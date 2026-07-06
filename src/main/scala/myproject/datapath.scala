package myproject
import chisel3._

class Datapath(width: Int) extends Module {
  val io = IO(new Bundle {
    val raddr1 = Input(UInt(3.W))
    val raddr2 = Input(UInt(3.W))
    val waddr  = Input(UInt(3.W))
    val wen    = Input(Bool())
    val op     = Input(UInt(3.W))
    val result = Output(UInt(width.W))
  })

  val regFile = Module(new RegFile(width, 8))
  val alu     = Module(new Alu(width))

  regFile.io.raddr1 := io.raddr1
  regFile.io.raddr2 := io.raddr2
  regFile.io.waddr  := io.waddr
  regFile.io.wen    := io.wen

  alu.io.a  := regFile.io.rdata1
  alu.io.b  := regFile.io.rdata2
  alu.io.op := io.op

  regFile.io.wdata := alu.io.out
  io.result := alu.io.out
}

object DatapathMain extends App with emitrtl.Toplevel {
  lazy val topModule = new Datapath(8)
  chisel2firrtl()
  firrtl2sv()
}

