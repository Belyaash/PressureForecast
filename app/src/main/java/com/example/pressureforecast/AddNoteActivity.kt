package com.example.pressureforecast

import ViewModels.AddNoteViewModel
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.example.pressureforecast.databinding.ActivityAddChangeNoteBinding
import com.example.pressureforecast.databinding.ActivityMainBinding
import database.AppDatabase
import database.DatabaseSingleton
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import model.OpenMeteoWeather
import model.WeatherCode
import model.WeatherNote
import services.WeatherService
import utils.ImageGetter
import java.util.*

class AddNoteActivity: AppCompatActivity() {

    val db by lazy {
        DatabaseSingleton.getDB(this)
    }
    lateinit var viewModel: AddNoteViewModel
    private lateinit var binding: ActivityAddChangeNoteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setContentView(R.layout.activity_add_change_note)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_change_note)
        viewModel = AddNoteViewModel(intent.getIntExtra("id", 0),
            this, resources.configuration.locale, applicationContext)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        val saveButton: Button = findViewById(R.id.save)
        saveButton.isEnabled = true
        observeViewModel()
    }

    fun observeViewModel() {
        val saveButton: Button = findViewById(R.id.save)
        val weatherImage: ImageView = findViewById(R.id.weather_ico)
        viewModel.weatherCode.observe(this, androidx.lifecycle.Observer {
            weatherImage.setImageResource(ImageGetter.getWeatherImage(it))
        })

        saveButton.setOnClickListener {
            viewModel.saveOrUpdate()
            if (viewModel.buttonEnabled.value!!){
                onBackPressed()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}