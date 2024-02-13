package cloud.shoplive.sample.shortform

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import cloud.shoplive.sample.PreferencesUtilImpl
import cloud.shoplive.sample.R
import cloud.shoplive.sample.databinding.ActivityNativeShortformBinding
import cloud.shoplive.sdk.common.ShopLiveCommon
import cloud.shoplive.sdk.common.ShopLiveCommonError
import cloud.shoplive.sdk.common.ShopLiveCommonUser
import cloud.shoplive.sdk.common.ShopLiveCommonUserGender
import cloud.shoplive.sdk.common.utils.ShopLiveDataSaver
import cloud.shoplive.sdk.shorts.ShopLiveShortform
import cloud.shoplive.sdk.shorts.ShopLiveShortformFullTypeHandler
import cloud.shoplive.sdk.shorts.ShopLiveShortformNativeHandler
import cloud.shoplive.sdk.shorts.ShopLiveShortformPlayEnableListener
import cloud.shoplive.sdk.shorts.ShopLiveShortformProductListener
import cloud.shoplive.sdk.shorts.ShopLiveShortformRelatedData
import cloud.shoplive.sdk.shorts.ShopLiveShortformShareData
import cloud.shoplive.sdk.shorts.ShopLiveShortformUrlListener
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class NativeShortformActivity : AppCompatActivity() {

    companion object {
        const val PAGE_SHORTS_MAIN = 0
        const val PAGE_SHORTS_VERTICAL = 1
        const val PAGE_SHORTS_HORIZONTAL = 2
        const val PAGE_SHORTS_FULL = 3

        fun intent(context: Context): Intent {
            return Intent(context, NativeShortformActivity::class.java)
        }
    }

    private val binding: ActivityNativeShortformBinding by lazy {
        ActivityNativeShortformBinding.inflate(layoutInflater)
    }

    private val tabAdapter by lazy {
        ShortformTabAdapter(this).apply {
            submitList(
                listOf(
                    ShortformMainFragment.newInstance(),
                    ShortformVerticalFragment.newInstance(),
                    ShortformHorizontalFragment.newInstance(),
                    ShortformFullFragment.newInstance(),
                )
            )
        }
    }

    private val mediator by lazy {
        TabLayoutMediator(binding.tabLayout, binding.pager) { tab, position ->
            val text = when (position) {
                PAGE_SHORTS_MAIN -> getString(R.string.shortform_tab_card)
                PAGE_SHORTS_VERTICAL -> getString(R.string.shortform_tab_vertical)
                PAGE_SHORTS_HORIZONTAL -> getString(R.string.shortform_tab_horizontal)
                PAGE_SHORTS_FULL -> getString(R.string.shortform_tab_full)
                else -> ""
            }
            tab.text = text
        }
    }

    private val preferencesUtil by lazy {
        PreferencesUtilImpl(this@NativeShortformActivity)
    }
    private val viewModel: ShortformViewModel by viewModels {
        viewModelFactory {
            initializer {
                ShortformViewModel(preferencesUtil)
            }
        }
    }

    private var dialog: ShortformOptionDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        ShopLiveDataSaver.onCreate(savedInstanceState)

        binding.pager.adapter = tabAdapter
        binding.pager.offscreenPageLimit = tabAdapter.itemCount
        binding.pager.isUserInputEnabled = false
        mediator.attach()
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val position = tab?.position ?: return
                (tabAdapter.currentList.getOrNull(position) as? ShopLiveShortformPlayEnableListener)
                    ?.enablePlayVideos()
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                val position = tab?.position ?: return
                (tabAdapter.currentList.getOrNull(position) as? ShopLiveShortformPlayEnableListener)
                    ?.disablePlayVideos()
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                val position = tab?.position ?: return
                viewModel.submitLiveData.value = position
            }
        })

        ShopLiveShortform.setHandler(object : ShopLiveShortformFullTypeHandler() {
            override fun onError(context: Context, error: ShopLiveCommonError) {
                Toast.makeText(
                    this@NativeShortformActivity,
                    error.message ?: error.toString(),
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onEvent(command: String, payload: String?) {
                super.onEvent(command, payload)
                // Do something
            }

            override fun onShare(activity: Activity, data: ShopLiveShortformShareData) {
                showShareDialog(data.title ?: return)
            }
        })

        ShopLiveShortform.setNativeHandler(object : ShopLiveShortformNativeHandler() {
            override fun getOnClickProductListener(): ShopLiveShortformProductListener {
                return ShopLiveShortformProductListener { _, product ->
                    // Something landing customer
                    ShopLiveShortform.showPreview(
                        this@NativeShortformActivity,
                        ShopLiveShortformRelatedData().apply {
                            productId = product.productId
                        })
                }
            }

            override fun getOnClickBannerListener(): ShopLiveShortformUrlListener {
                return ShopLiveShortformUrlListener { _, url ->
                    // Something landing customer
                    Toast.makeText(this@NativeShortformActivity, url, Toast.LENGTH_SHORT).show()
                }
            }
        })

        binding.settingButton.setOnClickListener {
            dialog?.dismiss()
            dialog = ShortformOptionDialog(
                context = this@NativeShortformActivity,
            ) { data ->
                data.userId?.let { userId ->
                    val accessKey = ShopLiveCommon.getAccessKey() ?: return@ShortformOptionDialog
                    ShopLiveCommon.setUser(
                        accessKey,
                        ShopLiveCommonUser(userId).apply {
                            name = data.name
                            age = data.age
                            gender = when (data.gender) {
                                "m" -> ShopLiveCommonUserGender.MALE
                                "f" -> ShopLiveCommonUserGender.FEMALE
                                "n" -> ShopLiveCommonUserGender.NEUTRAL
                                else -> null
                            }
                            userScore = data.userScore
                        })
                } ?: kotlin.run {
                    ShopLiveCommon.clearAuth()
                }
                viewModel.setShortformOption(data)
                viewModel.submitLiveData.value = binding.pager.currentItem
                dialog?.dismiss()
                recreate()
                viewModel.needInitializeTabFlow.value = emptySet()
            }.apply {
                this.show()
            }
        }

        viewModel.visibleFullTypeDataLiveData.observe(this) {
            ShopLiveShortform.setVisibleFullTypeViews(it)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        ShopLiveDataSaver.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        ShopLiveDataSaver.onRestoreInstanceState(savedInstanceState)
    }

    override fun onBackPressed() {
        if (binding.pager.currentItem != 0) {
            binding.pager.currentItem = PAGE_SHORTS_MAIN
            return
        }
        super.onBackPressed()
    }
}

private fun Context.showShareDialog(shareUrl: String) {
    if (this is Activity && isFinishing) return

    val sendIntent = Intent(Intent.ACTION_SEND)
    sendIntent.putExtra(Intent.EXTRA_TEXT, shareUrl)
    sendIntent.type = "text/plain"

    val shareIntent = Intent.createChooser(sendIntent, null)
    startActivity(shareIntent)
}
