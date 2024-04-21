package com.rokken.discord.role

import com.rokken.discord.DiscordMain
import net.dv8tion.jda.api.entities.Role

class GradeRole {
    private val cg = CalcGrade()
    private val guild = DiscordMain.rokkenGuild
    private val terms = ArrayList<Role?>(4)
    private val logger = org.slf4j.LoggerFactory.getLogger(this::class.java)

    init {
        terms.add(guild.getRoleById(RoleManager.GRADE_1))
        terms.add(guild.getRoleById(RoleManager.GRADE_2))
        terms.add(guild.getRoleById(RoleManager.GRADE_3))
        terms.add(guild.getRoleById(RoleManager.GRADE_4))
    }

    /**
     * 毎年年度が替わるごとに実行。
     * 期を更新するために使う。
     * @return 卒業するロール ID, 新入生の期
     */
    fun migrate(isRun: Boolean): Pair<Role, Int> {
        // 卒業する人の期を求める
        val gradGrade = cg.calcGrade(5)
        // 変更対象のロールを検索
        val termRole = findRole(gradGrade.toString())

        // ロール名変更処理
        if (isRun) {
            termRole.manager.setName("${gradGrade + 4}期").queue()
            logger.info("ロールのマイグレーションが完了しました。")
        }

        return Pair(termRole, gradGrade + 4)
    }

    /**
     * 選択した学年にふさわしいロールを取得する
     * @param selectedGrade 選択された学年
     * @return ?期のロール
     */
    fun getTermRole(selectedGrade: Int) : Role {
        return findRole(cg.calcGrade(selectedGrade).toString())
    }

    private fun findRole(termStr: String) : Role {
        for (term in terms) {
            // 名前に何期 (termStr) が含まれていたら返す
            if (term!!.name.contains(termStr))
                return term
        }
        return terms[0]!!
    }
}