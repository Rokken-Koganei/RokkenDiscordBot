package com.rokken.discord.role

import java.time.LocalDate
import java.time.Month

class CalcGrade {
    /**
     * 何期かを求める
     * @param currentGrade 現在の学年
     * @return 何期生か
     */
    fun calcGrade(currentGrade: Int) : Int {
        val startYear = 2008
        val b1Year = calcB1Year(currentGrade)

        return b1Year - startYear
    }

    /**
     * 1 年生の時の年度を求める
     * @param currentGrade 現在の年生
     * @return 1 年生の時の年度
     */
    private fun calcB1Year(currentGrade: Int) : Int {
        val date = LocalDate.now()
        val nowYear = date.year
        val nowMonth = date.month

        // 月が 1-3 の間以外なら
        // 年を 1 増やす
        if (nowMonth > Month.MARCH)
            nowYear + 1

        // 1 年生の時の年度を返す
        return nowYear - currentGrade
    }
}