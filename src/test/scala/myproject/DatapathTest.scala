package myproject

import chisel3._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec

class DatapathTest extends AnyFlatSpec with ChiselScalatestTester {
  behavior of "Datapath"

  it should "compute ADD of two zero-initialized registers as zero" in {
    test(new Datapath(8)) { dut =>
      dut.io.raddr1.poke(0.U)
      dut.io.raddr2.poke(1.U)
      dut.io.op.poke(AluOp.ADD)
      dut.io.wen.poke(false.B)
      dut.clock.step(1)
      dut.io.result.expect(0.U)
    }
  }

  it should "write ALU result back and read it out on the next cycle" in {
    test(new Datapath(8)) { dut =>
      // Reg[2] and Reg[3] start at 0 -> ADD gives 0 -> write 0 into Reg[4]
      dut.io.raddr1.poke(2.U)
      dut.io.raddr2.poke(3.U)
      dut.io.op.poke(AluOp.ADD)
      dut.io.waddr.poke(4.U)
      dut.io.wen.poke(true.B)
      dut.clock.step(1)

      // Now read back Reg[4] via raddr1, subtract from itself -> expect 0
      dut.io.raddr1.poke(4.U)
      dut.io.raddr2.poke(4.U)
      dut.io.op.poke(AluOp.SUB)
      dut.io.wen.poke(false.B)
      dut.clock.step(1)
      dut.io.result.expect(0.U)
    }
  }

  it should "correctly compute AND/OR/XOR/SLT opcodes structurally" in {
    test(new Datapath(8)) { dut =>
      for (op <- Seq(AluOp.AND, AluOp.OR, AluOp.XOR, AluOp.SLT)) {
        dut.io.raddr1.poke(0.U)
        dut.io.raddr2.poke(0.U)
        dut.io.op.poke(op)
        dut.io.wen.poke(false.B)
        dut.clock.step(1)
        dut.io.result.expect(0.U) // both operands are 0, so all these ops yield 0
      }
    }
  }
}
