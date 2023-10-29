package dev.training.the_riddle.ui.fragments.game_ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import dev.training.the_riddle.R
import dev.training.the_riddle.adapters.RiddleAdapter
import dev.training.the_riddle.app_system.interfaces.AnswerCallback
import dev.training.the_riddle.app_system.interfaces.DialogListener
import dev.training.the_riddle.app_system.interfaces.FragmentAskForSkipListener
import dev.training.the_riddle.app_system.interfaces.TimerListener
import dev.training.the_riddle.data.local.access.DBViewModel
import dev.training.the_riddle.databinding.FragmentRiddleContainerBinding
import dev.training.the_riddle.ui.fragments.dialogs.FinishedRiddlesDialog
import dev.training.the_riddle.ui.fragments.dialogs.RiddleSkipDialog
import dev.training.the_riddle.ui.fragments.dialogs.RiddleSuccessDialog
import dev.training.the_riddle.ui.fragments.dialogs.RiddleTimeOutDialog
import dev.training.the_riddle.ui.fragments.dialogs.RiddleWrongDialog

class RiddleContainerFragment : Fragment(), AnswerCallback, TimerListener,
    FragmentAskForSkipListener, DialogListener {
    private lateinit var binding: FragmentRiddleContainerBinding
    private val dbVModel: DBViewModel by viewModels()

    private var levelNum = 1 // use the safe args instead of the getIntent
    private var scoreGeneral = 1 // use the globalValue Like sharedPref
    private lateinit var riddleAdapter: RiddleAdapter


    private var riddlesScoreInSingleLevel = 0.0f
    private var grantedScoreInSingleLevel = 0.0f

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding =
            FragmentRiddleContainerBinding.inflate(layoutInflater)/* ..Todo:get-The-safeArgs for LevelNum */
        riddleAdapter = RiddleAdapter(this@RiddleContainerFragment)
        val args: RiddleContainerFragmentArgs by navArgs()
        levelNum = args.levelNum
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }


    private fun init() {
        setupVPager()
        displayRiddles()
    }

    private fun setupVPager() {
        with(binding.viewPager2) {
            adapter = riddleAdapter
            isUserInputEnabled = false
        }
    }

    private fun displayRiddles() {
        dbVModel.getRiddleByLevelId(levelNum).observe(viewLifecycleOwner) { riddlesList ->
            // displayRCount
            binding.tvRiddleActRiddleCount.text = riddlesList.size.toString()
            // include data into adapter
            riddleAdapter.riddles(riddlesList)
            // cal RiddlesScore
            for (element in riddlesList) {
                riddlesScoreInSingleLevel += element.riddleGivenPoint
            }
        }
    }

    //..Listeners####################################################

    //..Answer*********************************************************************************************
    override fun onSuccess(riddleId: Int) {
        //dialog
        showSuccessDialog()
        //Score
        scoreGeneral += getGivenPointForRiddle(riddleId)
        grantedScoreInSingleLevel += getGivenPointForRiddle(riddleId)
        binding.tvRiddleActCurrentScore.text = scoreGeneral.toString()
        //movement
        moveToNextPager()
    }

    override fun onFailed(riddleId: Int) = RiddleWrongDialog.newInstance(getHintForDialog(riddleId))
        .show(childFragmentManager, "WrongAnswer")

    //..Skip*********************************************************************************************

    private var isClicked = false
    override fun onFloatingBtnSkipClicked() {
        showSkipDialog()
        if (isClicked) return
        isClicked = false
    }

    //..Timer*********************************************************************************************

    override fun setTimerTickDuration(remainingTime: Long) {
        with(binding.tvTimerForRiddle) {
            setTextColor(
                resources.getColor(
                    if (remainingTime <= 6200) R.color.shiny_red else R.color.white, null
                )
            )
            text = (remainingTime / 1000).toString()
        }
    }

    override fun onTimerFinished() = RiddleTimeOutDialog.newInstance("Sorry But The Time is Over")
        .show(childFragmentManager, "TimeOut")

    //..Dialogs*********************************************************************************************

    override fun onClickForWrongAnswer() {
        // do what u want if user press okay btn in dialogWrongAnswer
        //Do Not take away any amount of points
        Log.i("RCF", "onClick() returned: " + "Wrong Answer and OkayDia Clicked")
        moveToNextPager()
    }

    override fun onClickForSkippedDialog() {
        isClicked = true
        Log.i("RCF", "onClick() returned: " + "Skipped and OkayDiaSkip Clicked")
        moveToNextPagerSkipped()
    }

    // =========================================HELPER=============================
    private val viewPager2 get() =  binding.viewPager2

    private fun moveToNextPager() {
        val newCurrentViewItem: Int = viewPager2.currentItem
        if (newCurrentViewItem == viewPager2.adapter!!.itemCount - 1) {
            //** here reached the last page so do ->
            FinishedRiddlesDialog().show(childFragmentManager, "DoneRiddles")
//            Handler().postDelayed(this::onBackPressed, 3250) // DoPop
        } else {
//            //---------USER Status
//            m_riddles_answered_count++
//            //---------
            viewPager2.setCurrentItem(newCurrentViewItem + 1, false)
            binding.tvStandingRiddle.text = "${newCurrentViewItem + 2}"
        }
    }

    private fun moveToNextPagerSkipped() {
        scoreGeneral -= 3
        binding.tvRiddleActCurrentScore.text = scoreGeneral.toString()
        val newCurrentViewItem: Int = viewPager2.currentItem
        if (newCurrentViewItem == viewPager2.adapter!!.itemCount - 1) {
            //** here reached the last page so do ->
            FinishedRiddlesDialog().show(childFragmentManager, "DoneRiddles")
//            Handler().postDelayed(this::onBackPressed, 3250) //..DoPopInstead
        } else {
            viewPager2.currentItem = newCurrentViewItem + 1
            binding.tvStandingRiddle.text = "${newCurrentViewItem + 2}"
        }
    }

    private fun showSuccessDialog() =
        RiddleSuccessDialog().show(childFragmentManager, "RightAnswer")

    private fun showSkipDialog() = RiddleSkipDialog.newInstance("If you skip (-3) score !")
        .show(childFragmentManager, "Skip this Riddle")

    /*private fun riddleIdInArray(riddleId: Int): Int =
        riddleAdapter.riddles()[riddleId - 1].riddleNum*/

    private fun getHintForDialog(position: Int): String? =
        riddleAdapter.riddles()[position - 1].hintAnswer

    private fun getGivenPointForRiddle(position: Int): Int =
        riddleAdapter.riddles()[position - 1].riddleGivenPoint

    private fun updateLevelEvaluation() {
        val newLevelEvaluation: Float = (grantedScoreInSingleLevel / riddlesScoreInSingleLevel)
        Log.i("RCF", "percentage%:" + newLevelEvaluation * 100)
        dbVModel.updateLevelEvaluation(levelNum, newLevelEvaluation * 100)
    }
    // =========================================HELPER=============================

    //..LifeCycle
    override fun onDestroy() {
        super.onDestroy()
        updateLevelEvaluation()
    }


}