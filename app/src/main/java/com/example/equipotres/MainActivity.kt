package com.example.equipotres

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.example.equipotres.databinding.ActivityMainBinding
import com.example.equipotres.ui.retos.RetosActivity
import android.net.Uri
import android.widget.Toast
import androidx.core.animation.addListener
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import com.example.equipotres.view.RetoDialogFragment
import kotlin.random.Random


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var countdownTimer: CountDownTimer
    private lateinit var mediaPlayer: MediaPlayer
    private var isAudioPlaying = true // Estado inicial del audio
    private lateinit var spinSound: MediaPlayer
    private var lastRotation = 0f
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        setupToolbar() // configuracion del toolbar
        setupAudioButton() // Configurar el boton de audio
        setupBottleRotation()

        mediaPlayer = MediaPlayer.create(this, R.raw.sound_main).apply {
            isLooping = true
            start()
        }

        // Inicializar el sonido de giro de la botella
        spinSound = MediaPlayer.create(this, R.raw.spinning_bottle)
        // Iniciar el contador regresivo
       // startCountdown()

        // Configurar el botón "Presióname" para que parpadee
        makeButtonBlink(binding.bButton)

        // funcion para abrir la ventana de retos
        val btnAdd = findViewById<ImageButton>(R.id.btn_add)
        btnAdd.setOnClickListener {
            mediaPlayer.pause()
            val intent = Intent(this, RetosActivity::class.java)
            startActivity(intent)
        }

        binding.bButton.setOnClickListener {
            startBottleSpin()
        }
        // funcion para abrir la ventana de instrucciones
//        val btnGame = findViewById<ImageButton>(R.id.btn_game)
//        btnGame.setOnClickListener {
//            mediaPlayer.pause() // Pausa la música de fondo
//            val instruccionesFragment = InstruccionesFragment()
//            supportFragmentManager.beginTransaction()
//                .replace(R.id.fragment_container, instruccionesFragment)
//                .addToBackStack(null) // Permite volver al fragmento anterior
//                .commit()
//        }
    }

    private fun setupBottleRotation() {

        binding.imgBotella.viewTreeObserver.addOnGlobalLayoutListener {

            binding.imgBotella.rotation = lastRotation
        }
    }

    private fun startBottleSpin() {
        binding.bButton.isInvisible  = true

        binding.imgBotella.pivotX = binding.imgBotella.width / 2f
        binding.imgBotella.pivotY = binding.imgBotella.height / 2f

        if (isAudioPlaying) {
            mediaPlayer.pause()
        }

        val duration = 4000L // 3 segundos
        val randomAngle = lastRotation + Random.nextInt(720, 1440)
        val rotationAnimator = ObjectAnimator.ofFloat(binding.imgBotella, "rotation", lastRotation, randomAngle)

        rotationAnimator.duration = duration
        rotationAnimator.start()

        spinSound.start()

        rotationAnimator.addListener(onEnd = {
            spinSound.pause()
            spinSound.seekTo(0)
            lastRotation = randomAngle % 360
            startCountdown()
        })
    }



    private fun showChallengeDialog() {
        val dialog = RetoDialogFragment()

        dialog.onDismissListener = {
            if (isAudioPlaying) {
                mediaPlayer.start()
            }
        }

        dialog.show(supportFragmentManager, "RetoDialogFragment")
    }

    private fun setupToolbar(){
        val toolbar = binding.contentToolbar.toolbar
        setSupportActionBar(toolbar)
        val btnStar = toolbar.findViewById<ImageButton>(R.id.btn_star)
        btnStar.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://play.google.com/store/apps/details?id=com.nequi.MobileApp&hl=es_419&gl=es")
            startActivity(intent)
        }
    }

    private fun setupAudioButton() {
        val audioButton = binding.contentToolbar.toolbar.findViewById<ImageButton>(R.id.btn_audio_on)

        audioButton.setOnClickListener {
            if (isAudioPlaying) {
                // Si la música está sonando, pausar la música y cambiar el ícono
                mediaPlayer.pause()
                audioButton.setImageResource(R.drawable.ico_volume_off) // Cambiar a ícono de apagado
                isAudioPlaying = false
            } else {
                // Si la música está pausada, reiniciarla y cambiar el ícono
                mediaPlayer.start()
                audioButton.setImageResource(R.drawable.ico_volume_on) // Cambiar a ícono de encendido
                isAudioPlaying = true
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity() // Cierra todas las actividades y sale de la aplicación
    }

    private fun startCountdown() {
        binding.contador.isVisible = true
        binding.contador.text = "3"

        countdownTimer = object : CountDownTimer(4000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val secondsLeft = (millisUntilFinished / 1000).toInt()
                binding.contador.text = secondsLeft.toString()
            }

            override fun onFinish() {
                binding.contador.text = "0"
                binding.contador.isVisible = false
                showChallengeDialog()
                binding.bButton.isVisible = true
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
        mediaPlayer.pause() // Pausa el sonido si la actividad se pausa
    }

    override fun onResume() {
        super.onResume()
        mediaPlayer.start() // Reanuda el sonido cuando la actividad vuelve a primer plano
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release() // Libera los recursos del MediaPlayer
    }
}
