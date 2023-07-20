package com.example.pressureforecast

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.pressureforecast.databinding.FragmentChartBinding
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.formatter.IValueFormatter
import com.github.mikephil.charting.utils.ViewPortHandler
import database.DatabaseSingleton
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class ChartFragment : Fragment() {

    val db by lazy {
        DatabaseSingleton.getDB(requireContext())
    }
    private lateinit var lineChart:LineChart

    private var _binding: FragmentChartBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentChartBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lineChart = view.findViewById(R.id.lineChart)

        db.weatherNoteDao().getAllAsc().observe(viewLifecycleOwner
        ) { notes ->
            run {
                try {
                    val upperPressures = ArrayList<Entry>()
                    val lowerPressures = ArrayList<Entry>()
                    val pulsePressures = ArrayList<Entry>()

                    for (note in notes){
                        upperPressures.add(Entry(note.dateTime.time.toFloat(), note.sys.toFloat()))
                        lowerPressures.add(Entry(note.dateTime.time.toFloat(), note.dya.toFloat()))
                        pulsePressures.add(Entry(note.dateTime.time.toFloat(), note.pulse.toFloat()))
                    }

                    val upperLine = LineDataSet(upperPressures, getString(R.string.systolic))
                    upperLine.lineWidth = 3f
                    upperLine.color = resources.getColor(R.color.orange)
                    upperLine.fillColor = resources.getColor(R.color.orange)
                    upperLine.fillAlpha = resources.getColor(R.color.orange)
                    upperLine.setCircleColor(resources.getColor(R.color.orange))
                    upperLine.valueTextSize=13f
                    upperLine.mode = LineDataSet.Mode.HORIZONTAL_BEZIER
                    upperLine.setDrawValues(false)

                    val lowerLine = LineDataSet(lowerPressures, getString(R.string.diastolic))
                    lowerLine.lineWidth = 3f
                    lowerLine.color = resources.getColor(R.color.green)
                    lowerLine.fillColor = resources.getColor(R.color.green)
                    lowerLine.fillAlpha = resources.getColor(R.color.green)
                    lowerLine.setCircleColor(resources.getColor(R.color.green))
                    lowerLine.valueTextSize=13f
                    lowerLine.mode = LineDataSet.Mode.HORIZONTAL_BEZIER
                    lowerLine.setDrawValues(false)

                    val pulseLine = LineDataSet(pulsePressures, getString(R.string.pulse))
                    pulseLine.lineWidth = 3f
                    pulseLine.color = resources.getColor(R.color.red)
                    pulseLine.fillColor = resources.getColor(R.color.red)
                    pulseLine.fillAlpha = resources.getColor(R.color.red)
                    pulseLine.setCircleColor(resources.getColor(R.color.red))
                    pulseLine.valueTextSize=13f
                    pulseLine.mode = LineDataSet.Mode.HORIZONTAL_BEZIER
                    pulseLine.setDrawValues(false)

                    lineChart.setNoDataText("No data")
                    lineChart.data = LineData(upperLine, lowerLine, pulseLine)
                    lineChart.xAxis.labelRotationAngle = 0f
                    lineChart.axisRight.isEnabled = false
                    var j = notes.last().dateTime.time.toFloat()
                    lineChart.xAxis.axisMaximum = j+100000f
                    lineChart.description.text = ""
                    lineChart.axisLeft.textSize=13f
                    lineChart.setExtraOffsets(0f,0f,20f,0f)
                    lineChart.axisLeft.xOffset = 10f
                    lineChart.setTouchEnabled(true)
                    lineChart.setPinchZoom(false)
                    lineChart.legend.textSize=10f
                    lineChart.legend.xEntrySpace = 10f
                    lineChart.animateX(0, Easing.EasingOption.EaseInExpo)

                    val xaxis:XAxis = lineChart.xAxis
                    xaxis.textSize=14f
                    xaxis.granularity=1f
                    xaxis.position = XAxis.XAxisPosition.BOTTOM
                    xaxis.setValueFormatter(object : IAxisValueFormatter {

                        override fun getFormattedValue(value: Float, axis: AxisBase?): String {
                            val inputFormat = SimpleDateFormat("dd.MM", resources.configuration.locale)
                            return inputFormat.format(value)
                        }
                    })
                }
                catch(e: Exception){
                    Log.e("Graph", e.message.toString())
                }
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}