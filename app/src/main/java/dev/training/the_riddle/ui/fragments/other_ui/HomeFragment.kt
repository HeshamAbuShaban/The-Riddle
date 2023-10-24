package dev.training.the_riddle.ui.fragments.other_ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import dev.training.the_riddle.R
import dev.training.the_riddle.databinding.FragmentHomeBinding
import dev.training.the_riddle.utils.GeneralUtils

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializer()
    }

    private fun initializer() {
        setupClicksListeners()
        Handler(Looper.myLooper()!!).postDelayed(this::setBtnAnimation, 650)
    }

    private fun setupClicksListeners() {
        with(binding) {
            val u = GeneralUtils.getInstance()
            var s = "undefined"
            btnHomeStartGame.setOnClickListener {
                s = "start"
            }

            btnHomeSettings.setOnClickListener {
                s = "settings"
            }

            btnHomeExit.setOnClickListener {
                s = "exit"
            }

            u.showSnackBar(binding.root, s)
        }
    }

    private fun setBtnAnimation() {
        val animation = AnimationUtils.loadAnimation(requireContext(), R.anim.bounce)
        with(binding) {
            btnHomeStartGame.startAnimation(animation)
            btnHomeSettings.startAnimation(animation)
            btnHomeExit.startAnimation(animation)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        clearAnimationForActViews()
        // here i close the appMusic when close
//        MusicController.getInstance().closeMusicWhenAppClosed()
    }

    private fun clearAnimationForActViews() {
        with(binding) {
            btnHomeStartGame.clearAnimation()
            btnHomeSettings.clearAnimation()
            btnHomeExit.clearAnimation()
        }
    }

}