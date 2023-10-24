package dev.training.the_riddle.adapters

import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.viewpager2.adapter.FragmentStateAdapter
import dev.training.the_riddle.app_system.constants.RiddleType
import dev.training.the_riddle.data.local.entities.Riddle
import dev.training.the_riddle.ui.fragments.RiddleFragment

class RiddleAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    private val diffUtil: DiffUtil.ItemCallback<Riddle> = object : DiffUtil.ItemCallback<Riddle>() {
            override fun areItemsTheSame(oldItem: Riddle, newItem: Riddle): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Riddle, newItem: Riddle): Boolean {
                return oldItem == newItem
            }
        }
    private val differ = AsyncListDiffer(this, diffUtil)

    fun riddles(): List<Riddle> = differ.currentList
    fun riddles(riddles: List<Riddle>) = differ.submitList(riddles)

    override fun createFragment(position: Int): Fragment {
        val (idRiddle, rQ, _, riddleTimeBySec1, ans1, ans2, ans3, ans4, theRightInTextAnswer, _, currentRiddleType) = riddles()[position]

        when (currentRiddleType) {
            RiddleType.TrueOrFalse.riddleTypeNum -> {
                val isTheQuestionTrue = theRightInTextAnswer.toBoolean()
                return RiddleFragment.newInstance(idRiddle, rQ, isTheQuestionTrue, riddleTimeBySec1)
            }
            RiddleType.ChooseTheCorrectAnswer.riddleTypeNum -> {
                return RiddleFragment.newInstance(
                    idRiddle,
                    rQ,
                    ans1,
                    ans2,
                    ans3,
                    ans4,
                    theRightInTextAnswer,
                    riddleTimeBySec1
                )
            }
            RiddleType.CompleteTheSentence.riddleTypeNum -> {
                val riddleTimeBySec = riddles()[position].riddleTimeBySec
                return RiddleFragment.newInstance(idRiddle, rQ, theRightInTextAnswer, riddleTimeBySec)
            }
            else -> throw IllegalStateException("Unexpected value: $currentRiddleType")
        }
    }

    override fun getItemCount(): Int = riddles().size

}