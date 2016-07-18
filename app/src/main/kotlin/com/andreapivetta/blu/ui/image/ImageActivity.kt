package com.andreapivetta.blu.ui.image

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import com.andreapivetta.blu.R
import kotlinx.android.synthetic.main.activity_image.*

class ImageActivity : AppCompatActivity() {

    companion object {
        val TAG_IMAGES = "images"
        val TAG_CURRENT_ITEM = "current_item"

        fun launch(context: Context, imageUrls: Array<String>) {
            val intent = Intent(context, ImageActivity::class.java)
            intent.putExtra(ImageActivity.TAG_IMAGES, imageUrls)
            context.startActivity(intent)
        }
    }

    private lateinit var images: List<String>
    private var isToolbarVisible = true
    private val handler: Handler = Handler()
    private var hideToolbarRunnable = Runnable { hideToolbar() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)

        images = intent.getStringArrayExtra(TAG_IMAGES).asList()
        val currentItem = intent.getIntExtra(TAG_CURRENT_ITEM, 0)
        val toolbarTitle = if (images.size > 1)
            getString(R.string.m_of_n, currentItem + 1, images.size) else ""
        toolbar.title = toolbarTitle
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { finish() }

        val viewPager = findViewById(R.id.photosViewPager) as ViewPager
        viewPager.adapter = ImageFragmentPagerAdapter(supportFragmentManager)
        viewPager.currentItem = currentItem
        viewPager.setPageTransformer(true, { view, position ->
            val pageWidth = view.width

            if (position < -1) {
                view.alpha = 0.toFloat()
            } else if (position <= 0) {
                view.alpha = 1.toFloat()
                view.translationX = 0.toFloat()
                view.scaleX = 1.toFloat()
                view.scaleY = 1.toFloat()
            } else if (position <= 1) {
                view.alpha = 1 - position
                view.translationX = pageWidth * -position

                val scaleFactor = 0.75f + (1 - 0.75f) * (1 - Math.abs(position))
                view.scaleX = scaleFactor
                view.scaleY = scaleFactor
            } else view.alpha = 0.toFloat()
        })

        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                if (images.size > 1) {
                    supportActionBar?.title = getString(R.string.m_of_n, position + 1, images.size)
                    showToolbar()
                    hideToolbarDelay()
                }
            }
        })
    }

    fun hideToolbarDelay() {
        handler.removeCallbacks(hideToolbarRunnable)
        handler.postDelayed(hideToolbarRunnable, 2000)
    }

    fun hideToolbar() {
        if (isToolbarVisible) {
            toolbar.animate().translationY(-toolbar.bottom.toFloat()).setInterpolator(AccelerateInterpolator()).start()
            isToolbarVisible = false
        }
    }

    fun showToolbar() {
        if (!isToolbarVisible) {
            toolbar.animate().translationY(0.toFloat()).setInterpolator(DecelerateInterpolator()).start()
            isToolbarVisible = true
        }
    }

    inner class ImageFragmentPagerAdapter(supportFragmentManager: FragmentManager) :
            FragmentPagerAdapter(supportFragmentManager) {

        override fun getItem(position: Int) = ImageFragment.newInstance(images[position])

        override fun getCount() = images.size
    }

}
