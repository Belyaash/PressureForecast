package RecyclerAdapters

import android.provider.Settings.Global.getString
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pressureforecast.R
import model.WeatherNote
import utils.ImageGetter
import java.lang.String.format
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*

class WeatherNoteAdapter(val weatherNotesList: List<WeatherNote>):
    RecyclerView.Adapter<WeatherNoteAdapter.NoteViewHolder>() {


    inner class NoteViewHolder(itemView: View):RecyclerView.ViewHolder(itemView),
            View.OnCreateContextMenuListener{
        private val TimeTextView: TextView = itemView.findViewById(R.id.time)
        private val TemperatureTextView: TextView = itemView.findViewById(R.id.temperature)
        private val PressureTextView: TextView = itemView.findViewById(R.id.pressure)
        private val PulseTextView:TextView = itemView.findViewById(R.id.pulse)
        private val WeatherImage:ImageView = itemView.findViewById(R.id.weather_image)

        fun bind(note: WeatherNote){

            TimeTextView.text = SimpleDateFormat("HH:mm").format(note.dateTime)
            TemperatureTextView.text = note.temperatureC.toString() + "°C"
            PressureTextView.text = note.sys.toString() + '/' + note.dya.toString()
            PulseTextView.text = note.pulse.toString()
            WeatherImage.setImageResource(ImageGetter.getWeatherImage(note.weather))
            itemView.setOnCreateContextMenuListener(this)
        }

        override fun onCreateContextMenu(
            menu: ContextMenu,
            view: View?,
            menuinfo: ContextMenu.ContextMenuInfo?
        ) {
            menu.add(this.adapterPosition, 1, 0, R.string.change)
            menu.add(this.adapterPosition, 2, 1, R.string.delete)
        }
    }

    fun getItem(position: Int): WeatherNote{
        return weatherNotesList[position]
    }
//
//    fun removeItem(position: Int){
//        weatherNotesList.removeAt(position)
//        notifyDataSetChanged()
//    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.weather_note_item, parent, false)

        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bind(weatherNotesList[position])
    }

    override fun getItemCount(): Int {
        return weatherNotesList.size
    }
}