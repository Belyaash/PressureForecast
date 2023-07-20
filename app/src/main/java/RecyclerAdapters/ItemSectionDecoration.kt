package RecyclerAdapters

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.os.Build
import android.util.DisplayMetrics
import android.view.Display
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.content.getSystemService
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import model.WeatherNote

class ItemSectionDecoration(
    private val context: Context,
    private val getItemList: () -> List<WeatherNote>
) :RecyclerView.ItemDecoration(){

    private val dividerHeight = dipToPx(context, 1f)
    private val dividerPaint = Paint(Paint.ANTI_ALIAS_FLAG).also {
        it.color = Color.parseColor("#E1E3E5")
    }
    private val sectionItemWidth: Int by lazy {
        getScreenWidth(context)
    }

    private val sectionItemHeight: Int by lazy {
        dipToPx(context, 44f)
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        val layoutManager = parent.layoutManager
        if (layoutManager !is LinearLayoutManager){
            return
        }
        if (LinearLayoutManager.VERTICAL != layoutManager.orientation){
            return
        }
        val list = getItemList()
        if (list.isEmpty()){
            return
        }

        val position = parent.getChildAdapterPosition(view)
        if (position == 0){
            outRect.top = sectionItemHeight
            return
        }

        val currentModel = getItemList()[position]
        val previousModel = getItemList()[position-1]

        if (currentModel.dateTime.date != previousModel.dateTime.date){
            outRect.top = sectionItemHeight
        }
        else{
            outRect.top = dividerHeight
        }
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)

        val childCount = parent.childCount
        for (i in 0 until childCount){
            val childView: View = parent.getChildAt(i)
            val position:Int = parent.getChildAdapterPosition(childView)
            val itemModel = getItemList()[position]

            if(getItemList().isNotEmpty() &&
                (position == 0 || itemModel.dateTime.date != getItemList()[position-1].dateTime.date)){
                val top = childView.top - sectionItemHeight
                drawSectionView(c, itemModel, top)
            }
            else{
                drawDivider(c,childView)
            }
        }
    }

    private fun drawDivider(canvas: Canvas, childView: View){
        canvas.drawRect(
            0f,
            (childView.top - dividerHeight).toFloat(),
            childView.right.toFloat(),
            childView.top.toFloat(),
            dividerPaint)
    }

    private fun drawSectionView(canvas: Canvas,note: WeatherNote, top:Int){
        val view = DateViewHolder(context)
        view.setDate(note)

        val bitmap = getViewGroupBitmap(view)
        val bitmapCanvas = Canvas(bitmap)
        view.draw(bitmapCanvas)

        canvas.drawBitmap(bitmap, 0f,top.toFloat(),null)
    }

    private fun getViewGroupBitmap(viewGroup: ViewGroup):Bitmap{
        val layoutParams = ViewGroup.LayoutParams(sectionItemWidth, sectionItemHeight)
        viewGroup.layoutParams = layoutParams

        viewGroup.measure(
            View.MeasureSpec.makeMeasureSpec(sectionItemWidth,View.MeasureSpec.EXACTLY),
            View.MeasureSpec.makeMeasureSpec(sectionItemHeight,View.MeasureSpec.EXACTLY)
        )

        viewGroup.layout(0,0,sectionItemWidth,sectionItemHeight)

        val bitmap = Bitmap.createBitmap(viewGroup.width, viewGroup.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        viewGroup.draw(canvas)

        return bitmap
    }

    private fun dipToPx(context: Context, dipValue: Float): Int{
        return (dipValue* context.resources.displayMetrics.density).toInt()
    }

    private fun getScreenWidth(context: Context):Int{
        val ourMetrics = DisplayMetrics()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            val display = context.display
            display?.getRealMetrics(ourMetrics)
        }
        else{
            val display = (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
            display.getMetrics(ourMetrics)
        }
        return ourMetrics.widthPixels
    }
}