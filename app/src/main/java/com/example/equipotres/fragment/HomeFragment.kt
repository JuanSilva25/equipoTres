package com.example.equipotres.fragment

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.example.equipotres.R
import com.example.equipotres.databinding.FragmentHomeBinding
import com.example.equipotres.ui.retos.RetosActivity
import androidx.navigation.fragment.findNavController
import kotlin.random.Random
import androidx.core.view.isVisible


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var countdownTimer: CountDownTimer
    private lateinit var mediaPlayer: MediaPlayer
    private var isAudioPlaying = true
    private var lastRotation = 0f
    private lateinit var spinSound: MediaPlayer

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        setupAudioButton()
        navigationFragmentRules()

        mediaPlayer = MediaPlayer.create(requireContext(), R.raw.sound_main)
        mediaPlayer.isLooping = true
        mediaPlayer.start()

        spinSound = MediaPlayer.create(requireContext(), R.raw.spinning_bottle)

        startCountdown()
        makeButtonBlink(binding.bButton)

        binding.bButton.setOnClickListener {
            startBottleSpin()
        }

        binding.contentToolbar.btnAdd.setOnClickListener {
            mediaPlayer.pause()
            val intent = Intent(requireContext(), RetosActivity::class.java)
            startActivity(intent)
        }

        binding.contentToolbar.btnShare.setOnClickListener {
            shareContent()
        }
    }

    private fun startBottleSpin() {
        binding.contador.visibility = View.VISIBLE

        binding.imgBotella.pivotX = binding.imgBotella.width / 2f
        binding.imgBotella.pivotY = binding.imgBotella.height / 2f

        if (isAudioPlaying) {
            mediaPlayer.pause()
        }

        val duration = 4000L // Duración de la animación
        val randomAngle = lastRotation + Random.nextInt(720, 1440)
        val rotationAnimator = ObjectAnimator.ofFloat(binding.imgBotella, "rotation", lastRotation, randomAngle)

        rotationAnimator.duration = duration
        rotationAnimator.start()

        spinSound.start()

        rotationAnimator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                spinSound.pause()
                spinSound.seekTo(0)
                lastRotation = randomAngle % 360
                startCountdown() // Iniciar el contador después de girar la botella
                binding.bButton.isVisible = true // Volver a mostrar el botón
            }
        })
    }

    private fun navigationFragmentRules(){
        binding.contentToolbar.btnGame.setOnClickListener() {
            findNavController().navigate(R.id.action_homeFragment_to_rulesFragment)
        }
    }

    private fun setupToolbar() {
        val toolbar = binding.contentToolbar.toolbar
        (activity as? AppCompatActivity)?.setSupportActionBar(toolbar)
        toolbar.findViewById<ImageButton>(R.id.btn_star).setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://play.google.com/store/apps/details?id=com.nequi.MobileApp&hl=es_419&gl=es")
            startActivity(intent)
        }
    }

    private fun setupAudioButton() {
        val audioButton = binding.contentToolbar.toolbar.findViewById<ImageButton>(R.id.btn_audio_on)
        audioButton.setOnClickListener {
            if (isAudioPlaying) {
                mediaPlayer.pause()
                audioButton.setImageResource(R.drawable.ico_volume_off)
                isAudioPlaying = false
            } else {
                mediaPlayer.start()
                audioButton.setImageResource(R.drawable.ico_volume_on)
                isAudioPlaying = true
            }
        }
    }

    private fun shareContent() {
        val textToShare = "App pico botella.\nSolo los valientes lo juegan!!\nhttps://play.google.com/store/apps/details?id=com.nequi.MobileApp&hl=es_419&gl=US"
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, textToShare)
            type = "text/plain"
        }
        startActivity(Intent.createChooser(shareIntent, "Compartir con"))
    }

    private fun startCountdown() {
        binding.contador.text = "3"
        countdownTimer = object : CountDownTimer(3000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                binding.contador.text = (millisUntilFinished / 1000).toInt().toString()
            }

            override fun onFinish() {
                binding.contador.text = "0"
                binding.contador.visibility = View.GONE
            }
        }.start()
    }

    private fun makeButtonBlink(button: Button) {
        val animator = ObjectAnimator.ofFloat(button, "alpha", 0f, 1f)
        animator.repeatCount = ValueAnimator.INFINITE
        animator.repeatMode = ValueAnimator.REVERSE
        animator.duration = 500
        animator.start()
    }

    override fun onPause() {
        super.onPause()
        if (isAudioPlaying) {
            mediaPlayer.pause()
        }
    }

    override fun onResume() {
        super.onResume()
        if (isAudioPlaying) {
            mediaPlayer.start()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mediaPlayer.release()
        spinSound.release() // Liberar recursos de spinSound
    }
}