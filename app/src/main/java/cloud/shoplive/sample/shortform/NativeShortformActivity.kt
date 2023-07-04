package cloud.shoplive.sample.shortform

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import cloud.shoplive.sample.R
import cloud.shoplive.sample.databinding.ActivityNativeShortformBinding
import cloud.shoplive.sdk.common.ShopLiveCommonError
import cloud.shoplive.sdk.network.response.ShopLiveShortformData
import cloud.shoplive.sdk.shorts.ShopLiveShortform
import cloud.shoplive.sdk.shorts.ShopLiveShortformFullTypeHandler
import cloud.shoplive.sdk.shorts.ShopLiveShortformPlayEnableListener
import cloud.shoplive.sdk.shorts.ShopLiveShortformScrollableListener
import cloud.shoplive.sdk.shorts.ShopLiveShortformSubmitListener
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class NativeShortformActivity : AppCompatActivity() {

    companion object {
        private const val PAGE_SHORTS_CARD = 0
        private const val PAGE_SHORTS_VERTICAL = 1
        private const val PAGE_SHORTS_HORIZONTAL = 2

        fun buildIntent(context: Context) : Intent {
            return Intent(context, NativeShortformActivity::class.java).apply {
                // Do nothing
            }
        }
    }

    private val backPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (binding.pager.currentItem != 0) {
                binding.pager.currentItem = PAGE_SHORTS_CARD
            } else {
                finish()
            }
        }
    }

    private val binding: ActivityNativeShortformBinding by lazy {
        ActivityNativeShortformBinding.inflate(layoutInflater)
    }

    private val tabAdapter by lazy {
        ShortformTabAdapter(this).apply {
            submitList(
                listOf(
                    ShortformCardFragment.newInstance(),
                    ShortformVerticalFragment.newInstance(),
                    ShortformHorizontalFragment.newInstance()
                )
            )
        }
    }

    private val mediator by lazy {
        TabLayoutMediator(binding.tabLayout, binding.pager) { tab, position ->
            val text = when (position) {
                PAGE_SHORTS_CARD -> getString(R.string.shortform_tab_card)
                PAGE_SHORTS_VERTICAL -> getString(R.string.shortform_tab_vertical)
                PAGE_SHORTS_HORIZONTAL -> getString(R.string.shortform_tab_horizontal)
                else -> ""
            }
            tab.text = text
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        onBackPressedDispatcher.addCallback(this, backPressedCallback)

        binding.pager.adapter = tabAdapter
        binding.pager.offscreenPageLimit = tabAdapter.itemCount
        binding.pager.isUserInputEnabled = false
        mediator.attach()
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val position = tab?.position ?: return
                (tabAdapter.currentList.getOrNull(position) as? ShopLiveShortformPlayEnableListener)
                    ?.enablePlayVideos()
                (tabAdapter.currentList.getOrNull(position) as? ShopLiveShortformSubmitListener)
                    ?.submit()
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                val position = tab?.position ?: return
                (tabAdapter.currentList.getOrNull(position) as? ShopLiveShortformPlayEnableListener)
                    ?.disablePlayVideos()
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                val position = tab?.position ?: return
                (tabAdapter.currentList.getOrNull(position) as? ShopLiveShortformScrollableListener)
                    ?.scrollToTop(false)
                (tabAdapter.currentList.getOrNull(position) as? ShopLiveShortformSubmitListener)
                    ?.submit()
            }
        })

        ShopLiveShortform.setHandler(object : ShopLiveShortformFullTypeHandler() {
            override fun onError(error: ShopLiveCommonError) {
                Toast.makeText(
                    this@NativeShortformActivity,
                    error.message ?: error.toString(),
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onShare(activity: Activity, data: ShopLiveShortformData?, url: String?) {
                // sharing
            }

            override fun onEvent(command: String, payload: String?) {

            }
        })

        ShopLiveShortform.observeProductUrl(this@NativeShortformActivity) { identifier, url ->
            startActivity(ShortformProductActivity.buildIntent(this, url))
        }
    }
}