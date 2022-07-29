package com.fita.test.takehometest.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.fita.test.core.di.util.ViewModelFactory
import com.fita.test.takehometest.R
import com.fita.test.takehometest.databinding.ActivityMainBinding
import com.fita.test.takehometest.di.injector.Injector
import com.fita.test.takehometest.model.TrackData
import com.fita.test.takehometest.utils.AdapterListener
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class MainActivity : AppCompatActivity(), AdapterListener<TrackData> {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: MainAdapter
    private var mediaPlayer: MediaPlayer = MediaPlayer()
    private var countDown: CountDownTimer? = null
    private val handler = Handler(Looper.getMainLooper())
    private var position: Int = 0
    private var currentStop: Int = 0
    private var mediaUri: String = ""
    private var doubleClickForPrev: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        Injector.getInstance(this)?.androidInjector()?.inject(this)
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        onInitView()
        onBindingListener()
        onObserverData()
    }

    private fun onInitView() {

        binding.rvItems.apply {
            layoutManager = LinearLayoutManager(
                this@MainActivity,
                LinearLayoutManager.VERTICAL,
                false
            )
            itemAnimator = null
        }
    }

    private fun onBindingListener() {
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            @SuppressLint("SetTextI18n")
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                return
            }

            override fun afterTextChanged(p0: Editable?) {
                if (!p0.isNullOrBlank()) {
                    startTimer(p0.toString())
                }
            }
        })

        binding.ivPause.setOnClickListener {
            mediaPlayer.stop()
            currentStop = mediaPlayer.currentPosition
            it.visibility = View.GONE
            binding.ivPlay.visibility = View.VISIBLE
        }

        binding.ivPlay.setOnClickListener {
            mediaPlayer.reset()
            mediaPlayer.setDataSource(mediaUri)
            mediaPlayer.prepareAsync()
            mediaPlayer.seekTo(currentStop)
            mediaPlayer.start()
            it.visibility = View.GONE
            binding.ivPause.visibility = View.VISIBLE
        }

        binding.ivNext.setOnClickListener {
            if (position < viewModel.trackData.size - 1) {
                adapter.hideIndicator(position)
                position++
                mediaPlayer.reset()
                mediaPlayer.setDataSource(viewModel.trackData[position].previewUrl)
                mediaPlayer.prepareAsync()
                adapter.showIndicator(position)
            }
        }

        binding.ivPrev.setOnClickListener {
            if (doubleClickForPrev) {
                if (position != 0) {
                    adapter.hideIndicator(position)
                    position--
                    mediaPlayer.reset()
                    mediaPlayer.setDataSource(viewModel.trackData[position].previewUrl)
                    mediaPlayer.prepareAsync()
                    adapter.showIndicator(position)
                }
                return@setOnClickListener
            }

            this.doubleClickForPrev = true
            Handler(Looper.getMainLooper()).postDelayed({
                mediaPlayer.reset()
                mediaPlayer.setDataSource(viewModel.trackData[position].previewUrl)
                mediaPlayer.prepareAsync()
                adapter.showIndicator(position)
                doubleClickForPrev = false
            }, 1000)
        }
    }

    private fun onObserverData() {
        viewModel.getResponseData().observeForever {
            adapter = MainAdapter(it)
            binding.rvItems.adapter = adapter
            adapter.listener = this
        }

        viewModel.getResponseMessage().observeForever {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }

        viewModel.isShowLoading().observeForever {
            if (it) {
                binding.viewLoading.root.visibility = View.VISIBLE
                binding.rvItems.visibility = View.GONE
            } else {
                binding.viewLoading.root.visibility = View.GONE
                binding.rvItems.visibility = View.VISIBLE
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun playMusic(uri: String?) {
        mediaPlayer.reset()
        mediaPlayer.setDataSource(uri)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            handler.removeCallbacks(runnable)
            binding.ivPlay.visibility = View.GONE
            binding.ivPause.visibility = View.VISIBLE
            adapter.dismissLoading(position)
            adapter.showIndicator(position)
            it.start()
            progressBar(it)
            onTrackBar(it)
        }

        mediaPlayer.setOnCompletionListener {
            adapter.hideIndicator(position)
            currentStop = 0
            binding.ivPlay.visibility = View.VISIBLE
            binding.ivPause.visibility = View.GONE
            binding.sbProgress.progress = currentStop
            binding.tvProgress.text = "00:00/00:00"
        }
        mediaPlayer.setOnErrorListener { _, code, extra ->
            return@setOnErrorListener viewModel.extractPlayerError(code, extra)
        }
    }

    private var duration: Int = mediaPlayer.duration
    private var runnable = object : Runnable {
        override fun run() {
            val currentPosition = mediaPlayer.currentPosition / 1000
            binding.sbProgress.progress = currentPosition
            val timeRemaining = mediaPlayer.currentPosition
            val progressSec = String.format(
                "%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(timeRemaining.toLong()),
                TimeUnit.MILLISECONDS.toSeconds(timeRemaining.toLong()) - TimeUnit.MINUTES.toSeconds(
                    TimeUnit.MILLISECONDS.toMinutes(timeRemaining.toLong())
                )
            )

            val maxSec = String.format(
                "%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(duration.toLong()),
                TimeUnit.MILLISECONDS.toSeconds(duration.toLong()) - TimeUnit.MINUTES.toSeconds(
                    TimeUnit.MILLISECONDS.toMinutes(duration.toLong())
                )
            )
            val text = "$progressSec/$maxSec"
            binding.tvProgress.text = text
            handler.postDelayed(this, 1000)
        }
    }

    private fun progressBar(mediaPlayer: MediaPlayer) {
        duration = mediaPlayer.duration
        binding.sbProgress.max = duration / 1000
        runOnUiThread(runnable)
    }

    private fun onTrackBar(mediaPlayer: MediaPlayer) {
        binding.sbProgress.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, progress: Int, userTouch: Boolean) {
                if (userTouch) mediaPlayer.seekTo(progress * 1000)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {

            }

        })
    }

    private fun startTimer(query: String) {
        cancelTimer()
        countDown = object : CountDownTimer(1000, 1000) {
            override fun onTick(p0: Long) {
                return
            }

            override fun onFinish() {
                hideKeyboard()
                viewModel.loadData(query)
            }

        }
        countDown!!.start()
    }

    private fun cancelTimer() {
        if (countDown != null)
            countDown!!.cancel()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.reset()
        handler.removeCallbacks(runnable)
    }

    override fun onClick(context: Context, data: TrackData, position: Int) {
        this.position = position
        if (data.previewUrl != null) {
            this.mediaUri = data.previewUrl!!
            playMusic(mediaUri)
            if (!binding.cvIndicator.isShown) {
                val animation = AnimationUtils.loadAnimation(this, R.anim.show_up)
                binding.cvIndicator.animation = animation
                binding.cvIndicator.visibility = View.VISIBLE
            }
        } else {
            adapter.dismissLoading(position)
            adapter.hideIndicator(position)
            Toast.makeText(this, "Preview Content not Available", Toast.LENGTH_SHORT).show()
        }
    }

    private fun Activity.hideKeyboard() {
        hideKeyboard(currentFocus ?: View(this))
    }

    private fun Context.hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }
}
