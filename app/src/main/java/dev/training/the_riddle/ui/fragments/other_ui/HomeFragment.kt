package dev.training.the_riddle.ui.fragments.other_ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import dev.training.the_riddle.R
import dev.training.the_riddle.databinding.FragmentHomeBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var navController: NavController
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
        navController = findNavController()
        setupClicksListeners()

        lifecycleScope.launch {
            delay(650)
            setBtnAnimation()
        }
//        Handler(Looper.myLooper()!!).postDelayed(this::setBtnAnimation, 650)
    }

    private fun setupClicksListeners() {
        with(binding) {
            btnHomeStartGame.setOnClickListener {
                navController.navigate(R.id.action_homeFragment_to_playFragment)
            }

            btnHomeSettings.setOnClickListener {
                navController.navigate(R.id.action_homeFragment_to_settingsFragment)
            }

            btnHomeExit.setOnClickListener {
//                navController.navigate(R.id.action_homeFragment_to_playFragment)
                onDestroy()
            }
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