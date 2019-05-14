package com.aqrlei.open.opensource


import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.aqrlei.open.views.CustomPopupMenu
import com.aqrlei.open.views.banner.BannerView
import com.aqrlei.open.views.dialog.BottomDialog
import com.aqrlei.open.views.dialog.CircleRotateProgressDialog
import com.aqrlei.open.views.dialog.HorizontalProgressDialog
import com.aqrlei.open.views.dialog.IPhoneStyleDialog
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.act_layout_banner.*
import kotlinx.android.synthetic.main.activity_main.*
import java.text.DateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private val loadingViewGroup by lazy { LayoutInflater.from(this).inflate(R.layout.progress_bar_loading, null) }


    private val listener = object : TabLayout.OnTabSelectedListener {
        override fun onTabSelected(p0: TabLayout.Tab?) {
            when (p0?.position) {
                0 -> {
                    labelTl.removeOnTabSelectedListener(this)
                    bannerTest()
                }
                1 -> customPopWindowTest()
                2 -> customDialogTest()
                3 -> bottomDialogTest()
                4 -> showCircleProgress()
                5 -> showHorizontalProgress()
                6 -> dimensionRadarViewTest()
                7 -> loadingTest()
            }
        }

        override fun onTabReselected(p0: TabLayout.Tab?) {

        }

        override fun onTabUnselected(p0: TabLayout.Tab?) {

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bannerTest()

       /* (window.decorView.findViewById<ViewGroup>(android.R.id.content)).run {
            addView(loadingViewGroup)

        }

        labelTl.apply {
            addTab(newTab().setText("Banner"))
            addTab(newTab().setText("CustomPop"))
            addTab(newTab().setText("IPhoneDialog"))
            addTab(newTab().setText("BottomDialog"))
            addTab(newTab().setText("CircleProgress"))
            addTab(newTab().setText("HorizontalProgress"))
            addTab(newTab().setText("DimensionRadar"))
            addTab(newTab().setText("ContentProgressDialog"))
        }
        addTabListenerTv.setOnClickListener {
            labelTl.addOnTabSelectedListener(listener)
            Toast.makeText(this, "add done", Toast.LENGTH_SHORT).show()
        }
        removeTabListenerTv.setOnClickListener {
            labelTl.removeOnTabSelectedListener(listener)
            Toast.makeText(this, "remove done", Toast.LENGTH_SHORT).show()
        }*/
    }


    private fun loadingTest() {
        loadingViewGroup.visibility = if (loadingViewGroup.visibility == View.VISIBLE) {

            View.GONE
        } else {
            loadingViewGroup.setOnClickListener(null)
            View.VISIBLE
        }
    }

    private fun showCircleProgress() {
        CircleRotateProgressDialog(this).apply {
            show()
        }
    }

    private fun showHorizontalProgress() {
        HorizontalProgressDialog(this).show()
    }

    private fun dimensionRadarViewTest() {
        with(drV) {
            addDimensionText("测试")
            maxSupportScoreNumber = 2
            replaceDimensionText(0, "测试啊")
            changeScoreColor(1, Color.parseColor("#88879ddf"))
            adjustItemScoreLevel(1, floatArrayOf(50F, 30F, 40F, 100F, 150F, 30F).toTypedArray())
        }
    }

    private fun bottomDialogTest() {
        val calender = Calendar.getInstance()
        val formatOne = DateFormat.getDateTimeInstance().format(calender.time)
        val formatOneDate = DateFormat.getDateInstance(DateFormat.SHORT).format(calender.time)
        val formatOneTime = DateFormat.getTimeInstance(DateFormat.SHORT).format(calender.time)
        //取反运算的验证
        BottomDialog.newInstance().apply {
            configureTitle("提示")
            configureNegativeButton("取消")
            configurePositiveButton("确定")
            configureNeutralButton("好的")
            setMCancelable(true)
            show(supportFragmentManager, BottomDialog.DefaultBottomDialogAdapter(
                    this@MainActivity,
                    listOf("${(1.inv())}", "${2.inv()}", "${3.inv()}", formatOne,
                            formatOneDate, formatOneTime)))

        }
    }

    private fun customPopWindowTest() {
        CustomPopupMenu.Builder
                .setPopCorner(CustomPopupMenu.PopCorner.BOTH_NOT_SHOW)
                .addPopWindowConfigure(CustomPopupMenu.PopConfigure(
                        arrowMarginRight = 14F,
                        marginRight = 8F,
                        marginTop = 38F,
                        itemPaddingLeft = 25F,
                        itemPaddingTop = 7F,
                        itemPaddingRight = 35F,
                        itemPaddingBottom = 7F,
                        itemDrawablePadding = 6F,
                        itemTextSize = 14F
                ))

                .addItemAction { i, popContent, _ ->
                    Log.d("Test", "pos: $i\t data: $popContent ")
                }
                .builder(this)
                .show(listOf(
                        CustomPopupMenu.PopContent(this.resources.getDrawable(R.mipmap.ic_launcher), "测试一"),
                        CustomPopupMenu.PopContent(this.resources.getDrawable(R.mipmap.ic_launcher_round), "测试二"),
                        CustomPopupMenu.PopContent(null, "测试三")), labelTl, Gravity.TOP)
    }

    private fun customDialogTest() {
        IPhoneStyleDialog.newInstance()
                .configureTitle("提示")
                .configureMsg("联系客户签约\n dddddd")
                .configureNegativeButton("稍后联系")
                .configurePositiveButton(
                        text = "立即联系",
                        textColor = Color.parseColor("#1b8fe6"),
                        action = {
                            Toast.makeText(this, "测试", Toast.LENGTH_SHORT).show()
                        })
                .setMCancelable(false)
                .show(supportFragmentManager)
    }

    private fun bannerTest() {
        bannerCl.visibility = View.VISIBLE
        val bannerList = ArrayList<Int>().apply {
            // add(R.drawable.banner_zero)
             }
        refreshBanner.setOnClickListener {
            bannerList.addAll(listOf(R.drawable.banner_one, R.drawable.banner_two, R.drawable.banner_three, R.drawable.banner_four))
            banner.refresh()
        }
        banner.setAdapterWithHolder(BannerAdapter(this,bannerList))

    }



    class BannerAdapter(context: Context, data: List<Int>)
        : BannerView.BannerViewAdapter<Int>(context, data) {
        override fun bindData(bannerLayout: Int, pos: Int, data: Int): View {
            val contentView = LayoutInflater.from(context).inflate(bannerLayout, null)
            (contentView as? ImageView)?.run {
                setImageResource(data)
                setOnClickListener {
                    Log.d("Banner", " the $pos view was clicked")
                }
            }
            return contentView
        }
    }
}
