package com.example.pressureforecast

import RecyclerAdapters.ItemSectionDecoration
import RecyclerAdapters.WeatherNoteAdapter
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.pressureforecast.HomeFragment.*
import com.example.pressureforecast.databinding.FragmentHomeBinding
import com.google.android.material.snackbar.Snackbar
import database.AppDatabase
import database.DatabaseSingleton
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import model.WeatherCode
import model.WeatherNote
import java.util.*


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class HomeFragment : Fragment() {

    val db by lazy {
        DatabaseSingleton.getDB(requireContext())
    }
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter:WeatherNoteAdapter
    private lateinit var rv: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        setDummiesInDb()
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rv = view.findViewById<RecyclerView>(R.id.weather_notes_rv)
        rv.layoutManager = LinearLayoutManager(activity)

        db.weatherNoteDao().getAll().observe(viewLifecycleOwner
        ) { notes ->
            run {
                adapter = WeatherNoteAdapter(notes)
                rv.adapter = adapter
                var itemSectionDecoration = ItemSectionDecoration(this.requireContext()) {
                    adapter.weatherNotesList
                }
                var c = rv.itemDecorationCount
                if (c>0){
                    rv.removeItemDecorationAt(0)
                }
                rv.addItemDecoration(itemSectionDecoration)
            }
        }


        binding.fab.setOnClickListener {
            val intent = Intent(activity, AddNoteActivity::class.java)
            val valueId: Int? = null
            intent.putExtra("id", valueId)
            startActivity(intent)
        }
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            1 -> {
                val intent = Intent(activity, AddNoteActivity::class.java)
                val valueId: Int? = adapter.getItem(item.groupId).id
                intent.putExtra("id", valueId)
                startActivity(intent)
                true
            }
            2 -> {
                GlobalScope.launch {
                    db.weatherNoteDao().delete(adapter.getItem(item.groupId))
                }
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun setDummiesInDb(){
        GlobalScope.launch{
            val lst = listOf<WeatherNote>(
                WeatherNote(null, Date(119, 0, 28, 12, 18),
                    WeatherCode.Sunny, 153, 87, 79, -18),
                WeatherNote(null, Date(119, 0, 29, 9, 48),
                    WeatherCode.Sunny, 152, 94, 74, -16),
                WeatherNote(null, Date(119, 0, 29, 12, 22),
                    WeatherCode.Clouds, 146, 92, 86, -16),
                WeatherNote(null, Date(119, 0, 29, 14, 28),
                    WeatherCode.Sunny, 135, 86, 80, -18),
                WeatherNote(null, Date(119, 0, 30, 11, 26),
                    WeatherCode.Sunny, 136, 98, 76, -14),
                WeatherNote(null, Date(119, 1, 0, 13, 26),
                    WeatherCode.Sunny, 144, 99, 74, -13),
                WeatherNote(null, Date(119, 1, 1, 8, 5),
                    WeatherCode.Sunny, 145, 86, 76, -18),
                WeatherNote(null, Date(119, 1, 3, 10, 31),
                    WeatherCode.Sunny, 148, 92, 72, -12),
                WeatherNote(null, Date(119, 1, 4, 11, 47),
                    WeatherCode.Sunny, 150, 93, 78, -18),
                WeatherNote(null, Date(119, 1, 4, 13, 7),
                    WeatherCode.Sunny, 142, 97, 81, -18),
                )
            for (weather in lst){
                db.weatherNoteDao().insert(weather)
            }
        }
    }
}