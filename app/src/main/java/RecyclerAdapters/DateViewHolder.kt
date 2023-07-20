package RecyclerAdapters

import android.content.Context
import android.widget.FrameLayout
import android.widget.TextView
import com.example.pressureforecast.R
import model.WeatherNote
import java.text.SimpleDateFormat
import java.util.*

class DateViewHolder(context: Context): FrameLayout(context){
    private lateinit var DateView: TextView

    init {
        inflate(context, R.layout.date_header, this)
        DateView = findViewById(R.id.date)
    }


    fun setDate(note: WeatherNote){
        val locale = resources.configuration.locale
        DateView.text = SimpleDateFormat("dd MMMM yyyy, EEEE",locale).format(note.dateTime)

    }

}