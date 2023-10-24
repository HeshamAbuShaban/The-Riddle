package dev.training.the_riddle.ui.fragments.game_ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dev.training.the_riddle.adapters.levels.LevelAdapter
import dev.training.the_riddle.data.local.access.DBViewModel
import dev.training.the_riddle.databinding.FragmentPlayBinding
import dev.training.the_riddle.utils.GeneralUtils

class PlayFragment : Fragment() {
    private lateinit var binding: FragmentPlayBinding

    private lateinit var levelAdapter: LevelAdapter

    //..ViewModels
    private val dbVModel: DBViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentPlayBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        setupLevelAdapter()
        displayLevels()
    }

    private fun setupLevelAdapter() {
        levelAdapter = LevelAdapter().apply {
            registerLevelCallback(LevelCallbackImpl())
        }
        with(binding.recyclerView) {
            adapter = levelAdapter
        }
    }

    private fun displayLevels() {
        val array = levelAdapter.levels()
        val sharedScoreValue = 8
        //..observe on the live data of the Levels
        //.update the adapter accordingly by the differ
        dbVModel.allLevel.observe(viewLifecycleOwner) {
            for (i in array.indices) {

                Log.i("PlayFragment", "InSideFL: ScoreWorkingOn=>$sharedScoreValue")

                array[i].levelOpenStatus = array[i].minPointToUnlock <= sharedScoreValue

                Log.i("PlayFragment", ":IsOpen :" +
                            array[i].levelNum + "=>" + array[i].levelOpenStatus)
            }

            levelAdapter.levels(it)
        }
    }

    private inner class LevelCallbackImpl : LevelAdapter.LevelCallback {
        override fun onClick(levelNum: Int, levelOpenStatus: Boolean) {

            if (levelOpenStatus) {
                //..TODO:Navigate with safeArgs
                /*
                val intent = Intent(re, RiddleFragment::class.java)
                intent.putExtra("level_no", level_no)
                startActivity(intent)
                 */
            } else {
                GeneralUtils.getInstance().showSnackBar(
                    binding.root,
                    "Closed Level Collect More Point and come back"
                )
            }

        }
    }
}