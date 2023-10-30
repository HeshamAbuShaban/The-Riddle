package dev.training.the_riddle.ui.fragments.other_ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dev.training.the_riddle.app_system.interfaces.DialogResetListener
import dev.training.the_riddle.app_system.prefs.ScoreSharedPreferences
import dev.training.the_riddle.databinding.FrgamentSettingsBinding
import dev.training.the_riddle.ui.fragments.dialogs.ResetDialog
import dev.training.the_riddle.utils.GeneralUtils

class SettingsFragment : Fragment(), DialogResetListener {
    private lateinit var binding: FrgamentSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FrgamentSettingsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        displayStatistics()
        setupClickListeners()
    }

    private fun displayStatistics() {
        with(binding) {
            tvSettingScore.text = ScoreSharedPreferences.getInstance().score.toString()
        }
    }

    private fun setupClickListeners() {
        with(binding) {
            resetDataBtn.setOnClickListener {
                ResetDialog().show(childFragmentManager, "ResetStatistics")
            }

            setDataBtn.setOnClickListener {
                GeneralUtils.getInstance().showSnackBar(root, "ComingSoon...")
            }
        }
    }

    override fun onResetCalled() {
        ScoreSharedPreferences.getInstance().removeValueOfKey("SCORE_KEY")
    }
}