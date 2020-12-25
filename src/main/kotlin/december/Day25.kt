package december

import AbstractDay

class Day25 : AbstractDay() {

    private val input = obtainInput { it.trim().toLong() }

    override fun part1(): String {
        val (cpk, dpk) = input
        val sn = 7L

        var cls = 0L
        var cpk1 = 1L
        while (true) {
            cls++
            cpk1 *= sn
            cpk1 %= 20201227
            if (cpk1 == cpk) break
        }

        var dls = 0L
        var dpk1 = 1L
        while (true) {
            dls++
            dpk1 *= sn
            dpk1 %= 20201227
            if (dpk1 == dpk) break
        }

        val dek = transform(cpk, dls)
        val cek = transform(dpk, cls)

        return if (dek == cek) dek.toString() else error("Door EK does not match Card EK ($dek, $cek)")
    }

    private fun transform(sn: Long, ls: Long): Long {
        var v = 1L
        for (i in 1..ls) {
            v *= sn
            v %= 20201227
        }
        return v
    }

    override fun part2(): String = "Given for free!"

}