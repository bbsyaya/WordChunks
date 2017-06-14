package com.appchamp.wordchunks.ui.aftergame

import android.arch.lifecycle.LifecycleFragment
import android.arch.lifecycle.ViewModelProviders
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.support.annotation.Nullable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.appchamp.wordchunks.R
import com.appchamp.wordchunks.ui.finish.FinishActivity
import com.appchamp.wordchunks.ui.game.GameActivity
import com.appchamp.wordchunks.util.Constants
import com.appchamp.wordchunks.util.Constants.CLUE_ID_KEY
import com.appchamp.wordchunks.util.Constants.COLOR_ID_KEY
import com.appchamp.wordchunks.util.Constants.FACT_ID_KEY
import com.appchamp.wordchunks.util.Constants.LEFT_ID_KEY
import kotlinx.android.synthetic.main.frag_level_solved.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.clearTop
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.startActivity
import java.util.*


class LevelSolvedFragment : LifecycleFragment(), AnkoLogger {

    private val viewModel by lazy {
        ViewModelProviders.of(activity).get(AfterGameViewModel::class.java)
    }

    @Nullable
    override fun onCreateView(inflater: LayoutInflater?, @Nullable container: ViewGroup?,
                              @Nullable savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.frag_level_solved, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rlNextLevel.setOnClickListener {
            val nextLevelId = viewModel.getNextLevelId()
            if (nextLevelId != null) {
                startGameActivity(nextLevelId)
            } else {
                startFinishActivity()
            }
        }
    }

    private fun startGameActivity(levelId: String) {
        startActivity(activity.intentFor<GameActivity>(
                Constants.EXTRA_LEVEL_ID to levelId).clearTop())
        activity.overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
    }

    private fun startFinishActivity() {
        activity.startActivity<FinishActivity>()
        activity.overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
        activity.finish()
    }

    private fun setPackColor() {
        val color = arguments.getInt(COLOR_ID_KEY)
        val drawable = imgRectBg.drawable as GradientDrawable
        drawable.setColor(color)
        tvNextLevelTitle.setTextColor(color)
    }

    private fun setClue() {
        tvNextLevelClue.text = arguments.getString(CLUE_ID_KEY)
    }

    private fun setFunFact() {
        tvFunFact.text = arguments.getString(FACT_ID_KEY)
    }

    private fun setLevelsLeft() {
        val left = arguments.getInt(LEFT_ID_KEY)
        when (left) {
            0 -> tvLevelsLeft.text = "YOU'VE FINISHED THE WHOLE PACK!"
            1 -> tvLevelsLeft.text = "ONLY ONE LEVEL LEFT IN PACK"
            -1 -> tvLevelsLeft.text = "YOU'VE FINISHED ALL PACKS AND LEVELS"
            else -> tvLevelsLeft.text = left.toString() + " LEVELS LEFT IN PACK"
        }
    }

    private fun setExcellent() {
        val res = context.resources
        val congrats = res.getStringArray(R.array.congrats)
        val i = Random().nextInt(congrats.size - 1)
        tvExcellent.text = congrats[i]
    }
}