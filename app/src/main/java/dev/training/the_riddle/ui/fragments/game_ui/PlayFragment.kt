package dev.training.the_riddle.ui.fragments.game_ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import dev.training.the_riddle.adapters.levels.LevelAdapter
import dev.training.the_riddle.app_system.prefs.ScoreSharedPreferences
import dev.training.the_riddle.data.local.access.DBViewModel
import dev.training.the_riddle.databinding.FragmentPlayBinding
import dev.training.the_riddle.utils.GeneralUtils

class PlayFragment : Fragment() {
    private lateinit var binding: FragmentPlayBinding

    private lateinit var levelAdapter: LevelAdapter
    private lateinit var navController: NavController

    //..ViewModels
    private val dbVModel: DBViewModel by viewModels()

    private var userScore = 10

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentPlayBinding.inflate(layoutInflater)
        navController = findNavController()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        displayStatics()
        setupLevelAdapter()
        displayLevels()
    }

    private fun displayStatics() {
        userScore = ScoreSharedPreferences.getInstance().score
        with(binding) {
            tvPlayActCurrentScore.text = userScore.toString()
        }
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
        //..observe on the live data of the Levels
        //.update the adapter accordingly by the differ
        dbVModel.allLevel.observe(viewLifecycleOwner) {
            levelAdapter.levels(it)
            val array = it
            for (i in array.indices) {
                if (array[i].minPointToUnlock <= userScore) {
                    dbVModel.updateLevelStatusOpen(true, array[i].levelNum)
                    array[i].levelOpenStatus = true
                    levelAdapter.levels(array)
                }
            }
        }
        /*
        //..OpenFirst
        dbVModel.updateLevelStatusOpen(true, 1)
        */
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

                navController.navigate(
                    PlayFragmentDirections.actionPlayFragmentToRiddleContainerFragment(
                        levelNum
                    )
                )
            } else {
                GeneralUtils.getInstance().showSnackBar(
                    binding.root,
                    "Closed Level Collect More Point and come back"
                )
            }

        }
    }
}