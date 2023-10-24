package dev.training.the_riddle.ui.fragments.other_ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dev.training.the_riddle.databinding.FrgamentSettingsBinding

class SettingsFragment : Fragment() {
    private lateinit var binding: FrgamentSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FrgamentSettingsBinding.inflate(layoutInflater)
        return binding.root
    }

}