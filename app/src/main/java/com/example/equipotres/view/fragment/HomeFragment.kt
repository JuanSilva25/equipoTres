package com.example.equipotres.view.fragment

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Intent
import android.content.SharedPreferences
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
import androidx.navigation.fragment.findNavController
import com.example.equipotres.view.LoginActivity
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var countdownTimer: CountDownTimer
    private lateinit var mediaPlayer: MediaPlayer
    private var isAudioPlaying = true
    private lateinit var sharedPreferences: SharedPreferences

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
        navigationFragmentChallenges()

        sharedPreferences = requireActivity().getSharedPreferences("shared", Context.MODE_PRIVATE)
        setupLogoutButton()

        mediaPlayer = MediaPlayer.create(requireContext(), R.raw.sound_main)
        mediaPlayer.isLooping = true
        mediaPlayer.start()

        binding.bButton.isEnabled = false
        binding.bButton.visibility = View.INVISIBLE
        startInitialCounter()
        makeButtonBlink(binding.bButton)

        binding.contentToolbar.btnShare.setOnClickListener {
            shareContent()
        }

        // Configurar el botón para girar la botella
        setupCircularButton()
    }

    // Nueva función que se adapta del código que te dieron
    private fun setupCircularButton() {
        binding.bButton.setOnClickListener {
            Log.d("HomeFragment", "Botón presionado, bloqueando...")

            binding.bButton.clearAnimation()
            binding.bButton.isEnabled = false
            binding.bButton.visibility = View.INVISIBLE
            // Girar Botella
            startBottleRotation()
        }
    }

    private fun startBottleRotation() {
        // Random challenge call (si tienes el viewModel o algo similar)
       // viewModel.getRandomChallenge()

        // Ángulo aleatorio para la rotación (de 90 a 720 grados)
        val rotationAngle = (90..720).random().toFloat()

        // Animación de rotación
        val rotateAnimator = ObjectAnimator.ofFloat(binding.imgBotella, "rotation", binding.imgBotella.rotation, binding.imgBotella.rotation + rotationAngle)
        rotateAnimator.duration = 5000 // Duración de 5 segundos

        // Detener la rotación si el fragmento se detiene
        onStop()

        rotateAnimator.start()
        rotateAnimator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {

                startInitialCounter()
            }
        })
    }

    private fun startInitialCounter() {
        val handler = Handler(Looper.getMainLooper())
        var counter = 3

        binding.contador.visibility = View.VISIBLE

        val runnable = object : Runnable {
            override fun run() {
                if (counter >= 0) {
                    binding.contador.text = counter.toString()
                    counter--
                    handler.postDelayed(this, 1000)
                } else {
                    // Habilitar el botón y mostrar el cuadro de diálogo
                    binding.contador.visibility = View.GONE
                    Log.d("HomeFragment", "Contador terminado, desbloqueando botón...")
                    binding.bButton.isEnabled = true
                    binding.bButton.visibility = View.VISIBLE
                //showChallengeDialog()
                }
            }
        }
        handler.post(runnable)
    }

    private fun navigationFragmentRules(){
        binding.contentToolbar.btnGame.setOnClickListener() {
            findNavController().navigate(R.id.action_homeFragment_to_rulesFragment)
        }
    }

    private fun navigationFragmentChallenges(){
        binding.contentToolbar.btnAdd.setOnClickListener() {
            findNavController().navigate(R.id.action_homeFragment_to_challengesFragment)
        }
    }

    private fun setupLogoutButton() {
        binding.contentToolbar.toolbar.findViewById<View>(R.id.btn_logout).setOnClickListener {
            logOut()
        }
    }

    private fun logOut() {
        // Limpiar datos de SharedPreferences
        sharedPreferences.edit().clear().apply()

        // Redirigir al LoginActivity y limpiar la pila de actividades
        val intent = Intent(requireActivity(), LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK // Limpia la pila de actividades
        startActivity(intent)

        // Finaliza la actividad
        requireActivity().finish()
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
    }
}