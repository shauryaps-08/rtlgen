package myproject
import chisel3._

class RegFile(width: Int, depth: Int) extends Module {
  val io = IO(new Bundle {
    val raddr1 = Input(UInt(log2Ceil(depth).W))
    val raddr2 = Input(UInt(log2Ceil(depth).W))
    val rdata1 = Output(UInt(width.W))
    val rdata2 = Output(UInt(width.W))
    val waddr  = Input(UInt(log2Ceil(depth).W))
    val wdata  = Input(UInt(width.W))
    val wen    = Input(Bool())
  })

  val regs = Mem(depth, UInt(width.W))

  io.rdata1 := regs.read(io.raddr1)
  io.rdata2 := regs.read(io.raddr2)

  when(io.wen) {
    regs.write(io.waddr, io.wdata)
  }
}

object log2Ceil {
  def apply(n: Int): Int = {
    var res = 0
    var v = 1
    while (v < n) { v *= 2; res += 1 }
    if (res == 0) 1 else res
  }
}
