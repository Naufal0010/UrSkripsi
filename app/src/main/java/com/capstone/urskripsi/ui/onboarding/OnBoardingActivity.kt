package com.capstone.urskripsi.ui.onboarding

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.capstone.urskripsi.R
import com.capstone.urskripsi.databinding.ActivityOnBoardingBinding
import com.capstone.urskripsi.helper.PreferencesHelper
import com.capstone.urskripsi.ui.login.LoginActivity
import com.capstone.urskripsi.utils.Constant.Companion.FIRST_TIME_RUN
import com.capstone.urskripsi.utils.Constant.Companion.PREF_ON_BOARDING
import com.capstone.urskripsi.utils.Utility.hide
import com.capstone.urskripsi.utils.Utility.show
import com.google.android.material.tabs.TabLayout

class OnBoardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnBoardingBinding
    private lateinit var onBoardingAdapter: OnBoardingAdapter
    private lateinit var sharedPreferences: PreferencesHelper
    private var position = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnBoardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = PreferencesHelper(this, PREF_ON_BOARDING)

        // go MainActivity if not first time run
        if (restorePrefData()) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        // setup data on boarding
        val onBoardingData: ArrayList<OnBoarding> = ArrayList()
        onBoardingData.apply {
            add(
                OnBoarding(
                    R.drawable.on_boarding_bg_1,
                    applicationContext.resources.getString(R.string.title1),
                    applicationContext.resources.getString(R.string.description1)
                )
            )
            add(
                OnBoarding(
                    R.drawable.on_boarding_bg_2,
                    applicationContext.resources.getString(R.string.title2),
                    applicationContext.resources.getString(R.string.description2)
                )
            )
            add(
                OnBoarding(
                    R.drawable.on_boarding_bg_3,
                    applicationContext.resources.getString(R.string.title3),
                    applicationContext.resources.getString(R.string.description3)
                )
            )
        }
        // set viewpager
        setOnBoardingViewPager(onBoardingData)

        with(binding) {
            // to save value position in viewpager
            position = viewPagerOnBoarding.currentItem
            layoutIndicator.apply {
                tvNext.setOnClickListener {
                    if (position < onBoardingData.size) {
                        position++
                        viewPagerOnBoarding.currentItem = position
                    }
                }

                btnGetStarted.setOnClickListener {
                    // save data
                    savePrefData()
                    startActivity(Intent(this@OnBoardingActivity, LoginActivity::class.java))
                    finish()
                }

                // set text Get Started or Next
                tabLayout.addOnTabSelectedListener(object :
                    TabLayout.OnTabSelectedListener {
                    override fun onTabSelected(tab: TabLayout.Tab?) {
                        position = tab!!.position
                        if (tab.position == onBoardingData.size - 1) {
                            btnGetStarted.show()
                            tvNext.hide()
                            tabLayout.hide()
                            dotsIndicator.hide()
                        } else {
                            btnGetStarted.hide()
                            tvNext.show()
                            tabLayout.show()
                            dotsIndicator.show()
                            tvNext.text = getString(R.string.next)
                        }
                    }

                    override fun onTabUnselected(tab: TabLayout.Tab?) {}
                    override fun onTabReselected(tab: TabLayout.Tab?) {}
                })
            }
        }
    }

    private fun setOnBoardingViewPager(onBoarding: List<OnBoarding>) {
        onBoardingAdapter = OnBoardingAdapter(this, onBoarding)
        binding.apply {
            viewPagerOnBoarding.adapter = onBoardingAdapter
            layoutIndicator.tabLayout.setupWithViewPager(viewPagerOnBoarding)
            layoutIndicator.dotsIndicator.setViewPager(viewPagerOnBoarding)
        }
    }

    private fun savePrefData() {
        sharedPreferences.setDataBoolean(FIRST_TIME_RUN, true)
    }

    private fun restorePrefData(): Boolean = sharedPreferences.getDataBoolean(FIRST_TIME_RUN)
}