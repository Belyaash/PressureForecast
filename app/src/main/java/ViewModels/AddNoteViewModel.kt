package ViewModels

import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.databinding.ObservableField
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson

import database.DatabaseSingleton
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import model.OpenMeteoWeather
import model.WeatherCode
import model.WeatherNote
import network.OpenMeteoWeatherInteractor
import services.WeatherService
import utils.WeatherCodeConverter
import java.text.SimpleDateFormat
import java.util.*

class AddNoteViewModel(val id: Int,val lifecycleOwner: LifecycleOwner,
                       val locale: Locale, val context: Context):BaseObservable() {

    init {
        if (id == 0){
            WeatherService.weather.observe(lifecycleOwner, androidx.lifecycle.Observer {
                weather = WeatherService.weather.value!!
                temperatureText = weather!!.temperature.toInt().toString()
                weatherCode.value = WeatherCodeConverter.OpenMeteoConvert(weather!!.weathercode)
            })
        }
        else {
            var updateNote: WeatherNote? = null
            GlobalScope.launch {
                GlobalScope.async {
                    updateNote = db!!.weatherNoteDao().getById(id)
                }.await()
                sys = updateNote?.sys
                dia = updateNote?.dya
                pul = updateNote?.pulse
                temperature = updateNote?.temperatureC
                weatherCode.postValue(updateNote?.weather)
                date = updateNote?.dateTime!!
                notifyPropertyChanged(3)

            }
        }
    }


    val db by lazy {
        DatabaseSingleton.db
    }

    var sys: Int? = null

    @Bindable
    var sysText: String = ""
        get() {
            return if (sys == null){
                ""
            } else sys.toString()
        }
        set(value) {
            sys = value.toIntOrNull()
            field = value
            updateIsSaveButtonEnabled()
            notifyPropertyChanged(0)
        }


    var dia: Int? = null

    @Bindable
    var diaText: String = ""
        get() {
            return if (dia == null){
                ""
            } else dia.toString()
        }
        set(value) {
            dia = value.toIntOrNull()
            field = value
            updateIsSaveButtonEnabled()
            notifyPropertyChanged(1)
        }

    var pul: Int? = null

    @Bindable
    var pulText: String = ""
        get() {
            return if (pul == null){
                ""
            } else pul.toString()
        }
        set(value) {
            pul = value.toIntOrNull()
            field = value
            updateIsSaveButtonEnabled()
            notifyPropertyChanged(2)
        }


    var date:Date = Date()

    @Bindable
    var dateText: String = ""
        get() {
            val inputFormat = SimpleDateFormat("dd MMMM", locale)
            return inputFormat.format(date)
        }
        set(value) {
            field = value
            notifyPropertyChanged(3)
        }

    var temperature :Int? = null

    @Bindable
    var temperatureText : String = "°C"
        get() {
            return if (temperature == null){
                " °C"
            } else temperature.toString()+" °C"
        }
        set(value) {
            temperature = value.toIntOrNull()
            field = value
            notifyPropertyChanged(5)
        }

    var weatherCode : MutableLiveData<WeatherCode> = MutableLiveData(WeatherCode.Sunny)

    var weather: OpenMeteoWeather? = WeatherService.weather.value
    var buttonEnabled = MutableLiveData<Boolean>(false)

    fun saveOrUpdate(){
        if (buttonEnabled.value == true){
            if (id==0){
                date = Date()
                val insertNote = WeatherNote(
                    id = null,
                    dateTime = Date(),
                    weather = weatherCode.value!!,
                    sys = sys!!,
                    dya = dia!!,
                    pulse = pul!!,
                    temperatureC = temperature!!
                )
                GlobalScope.launch {
                    db!!.weatherNoteDao().insert(insertNote)
                }
            }
            else{
                var updateNote: WeatherNote
                GlobalScope.launch {
                    updateNote = db!!.weatherNoteDao().getById(id)
                    updateNote.sys = sys!!
                    updateNote.dya = dia!!
                    updateNote.pulse = pul!!
                    db!!.weatherNoteDao().update(updateNote)
                    date = updateNote.dateTime
                }
            }
        }

        else{
            if (sys == null || dia == null || pul == null){
                Toast.makeText(context, "Введите все данные", Toast.LENGTH_LONG).show()
            }
            else if (sys !in 70..300){
                Toast.makeText(context, "Неверное систолическое давнение", Toast.LENGTH_LONG).show()
            }
            else if (dia !in 40..200){
                Toast.makeText(context, "Неверное диастолическое давнение", Toast.LENGTH_LONG).show()
            }
            else if (pul !in 30..260){
                Toast.makeText(context, "Неверное значение пульса", Toast.LENGTH_LONG).show()
            }
            else if (sys!! < dia!!){
                Toast.makeText(context, "Систолическое давление не может быть ниже диастолического", Toast.LENGTH_LONG).show()
            }
        }

    }

    private fun updateIsSaveButtonEnabled(){
        buttonEnabled.value =
                    sys in 70..300
                    && dia in 40..200
                    && pul in 30..260
                            && sys!! > dia!!
    }

}