package cloud.shoplive.sample.shortform

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import cloud.shoplive.sample.PreferencesUtilImpl
import cloud.shoplive.sample.R
import cloud.shoplive.sample.databinding.ActivityNativeShortformBinding
import cloud.shoplive.sample.extension.showShareDialog
import cloud.shoplive.sample.views.dialog.CustomListDialog
import cloud.shoplive.sdk.common.ShopLiveCommon
import cloud.shoplive.sdk.common.ShopLiveCommonCustomizeData
import cloud.shoplive.sdk.common.ShopLiveCommonCustomizeDialogData
import cloud.shoplive.sdk.common.ShopLiveCommonError
import cloud.shoplive.sdk.common.ShopLiveCommonUser
import cloud.shoplive.sdk.common.ShopLiveCommonUserGender
import cloud.shoplive.sdk.common.extension.toDp
import cloud.shoplive.sdk.common.utils.ShopLiveDataSaver
import cloud.shoplive.sdk.editor.ShopLiveCoverPicker
import cloud.shoplive.sdk.editor.ShopLiveCoverPickerCustomizeData
import cloud.shoplive.sdk.editor.ShopLiveCoverPickerData
import cloud.shoplive.sdk.editor.ShopLiveCoverPickerHandler
import cloud.shoplive.sdk.editor.ShopLiveCoverPickerUrlData
import cloud.shoplive.sdk.editor.ShopLiveCoverPickerVisibleActionButton
import cloud.shoplive.sdk.editor.ShopLiveEditorLocalData
import cloud.shoplive.sdk.editor.ShopLiveEditorResultData
import cloud.shoplive.sdk.editor.ShopLiveImageEditor
import cloud.shoplive.sdk.editor.ShopLiveImageEditorData
import cloud.shoplive.sdk.editor.ShopLiveImageEditorHandler
import cloud.shoplive.sdk.editor.ShopLiveShortformEditor
import cloud.shoplive.sdk.editor.ShopLiveShortformEditorAspectRatio
import cloud.shoplive.sdk.editor.ShopLiveShortformEditorHandler
import cloud.shoplive.sdk.editor.ShopLiveShortformEditorVisibleActionButton
import cloud.shoplive.sdk.editor.ShopLiveShortformEditorVisibleContentData
import cloud.shoplive.sdk.editor.ShopLiveVideoEditor
import cloud.shoplive.sdk.editor.ShopLiveVideoEditorCustomizeCropData
import cloud.shoplive.sdk.editor.ShopLiveVideoEditorCustomizeData
import cloud.shoplive.sdk.editor.ShopLiveVideoEditorCustomizeFilterData
import cloud.shoplive.sdk.editor.ShopLiveVideoEditorCustomizeMainData
import cloud.shoplive.sdk.editor.ShopLiveVideoEditorCustomizePlaybackSpeedData
import cloud.shoplive.sdk.editor.ShopLiveVideoEditorCustomizeVoluemeData
import cloud.shoplive.sdk.editor.ShopLiveVideoEditorData
import cloud.shoplive.sdk.editor.ShopLiveVideoEditorHandler
import cloud.shoplive.sdk.editor.ShopLiveVideoUploaderData
import cloud.shoplive.sdk.network.ShopLiveNetwork
import cloud.shoplive.sdk.shorts.ShopLiveShortform
import cloud.shoplive.sdk.shorts.ShopLiveShortformHandler
import cloud.shoplive.sdk.shorts.ShopLiveShortformMessageListener
import cloud.shoplive.sdk.shorts.ShopLiveShortformPlayEnableListener
import cloud.shoplive.sdk.shorts.ShopLiveShortformPreviewData
import cloud.shoplive.sdk.shorts.ShopLiveShortformProductListener
import cloud.shoplive.sdk.shorts.ShopLiveShortformShareData
import cloud.shoplive.sdk.shorts.ShopLiveShortformUrlListener
import cloud.shoplive.sdk.shorts.ShopLiveShortformWebView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import java.util.concurrent.atomic.AtomicBoolean


class NativeShortformActivity : AppCompatActivity() {

    companion object {
        const val PAGE_SHORTS_MAIN = 0
        const val PAGE_SHORTS_VERTICAL = 1
        const val PAGE_SHORTS_HORIZONTAL = 2
        const val PAGE_SHORTS_FULL = 3

        private const val KEY_PICK_VISUAL_VIDEO_REQUEST = "key_pick_visual_video_request"
        private const val KEY_PICK_VISUAL_IMAGE_REQUEST = "key_pick_visual_image_request"

        private var videoEditorData = ShopLiveVideoEditorData()
        private var imageEditorData = ShopLiveImageEditorData()
        private var coverPickerData = ShopLiveCoverPickerData()

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
                PAGE_SHORTS_MAIN -> getString(R.string.shortform_tab_main)
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


    private val editorDialog: CustomListDialog<String> by lazy {
        val uploadEditorString = getString(R.string.shortform_editor_upload)
        val videoEditorString = getString(R.string.shortform_video_editor_only)
        val imageEditorString = getString(R.string.shortform_image_editor_only)
        val coverPickerString = getString(R.string.shortform_cover_picker_only)

        val list =
            listOf(uploadEditorString, videoEditorString, imageEditorString, coverPickerString)
        CustomListDialog(this, list, callback = {
            when (it) {
                uploadEditorString -> showShortformEditor()
                videoEditorString -> showVideoEditor()
                imageEditorString -> showImageEditor()
                coverPickerString -> showCoverPicker()
            }
            editorDialog.dismiss()
        })
    }

    private val optionDialog: ShortformOptionDialog by lazy {
        ShortformOptionDialog(context = this) { data ->
            data.userId?.let { userId ->
                val accessKey = ShopLiveCommon.getAccessKey() ?: return@ShortformOptionDialog
                ShopLiveCommon.setUser(
                    accessKey,
                    ShopLiveCommonUser(userId).apply {
                        userName = data.userName
                        age = data.age
                        gender = when (data.gender) {
                            ShopLiveCommonUserGender.MALE.text -> ShopLiveCommonUserGender.MALE
                            ShopLiveCommonUserGender.FEMALE.text -> ShopLiveCommonUserGender.FEMALE
                            ShopLiveCommonUserGender.NEUTRAL.text -> ShopLiveCommonUserGender.NEUTRAL
                            else -> null
                        }
                        userScore = data.userScore
                    })
            } ?: kotlin.run {
                ShopLiveCommon.clearAuth()
            }
            if (ShopLiveCommon.getAccessKey() != data.accessKey) {
                ShopLiveCommon.setAccessKey(data.accessKey)
                preferencesUtil.accessKey = data.accessKey
                ShopLiveNetwork.clearShortsConfig()
            }
            viewModel.setShortformOption(data)
            viewModel.needInitializeTabFlow.value = emptySet()
            binding.pager.currentItem = 0
            recreate()
            viewModel.submitLiveData.value = binding.pager.currentItem
            optionDialog.dismiss()
        }
    }
    private var isMuted = false

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

        ShopLiveShortform.setHandler(object : ShopLiveShortformHandler() {
            override fun getOnClickProductListener(): ShopLiveShortformProductListener {
                return ShopLiveShortformProductListener { context, data, product ->
                    // Something landing customer
                    product.name?.let { name ->
                        Toast.makeText(this@NativeShortformActivity, name, Toast.LENGTH_SHORT)
                            .show()
                    }
                    val activity = context as? Activity ?: return@ShopLiveShortformProductListener
                    ShopLiveShortform.close()
                    ShopLiveShortform.showPreview(
                        activity,
                        ShopLiveShortformPreviewData().apply {
                            shortsId = data?.shortsId
                            productId = product.productId
                            isMuted = this@NativeShortformActivity.isMuted
                        })
                }
            }

            override fun getOnClickBannerListener(): ShopLiveShortformUrlListener {
                return ShopLiveShortformUrlListener { context, _, url ->
                    // Something landing customer
                    Toast.makeText(context, url, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onError(context: Context, error: ShopLiveCommonError) {
                Toast.makeText(
                    this@NativeShortformActivity,
                    error.message ?: error.toString(),
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onEvent(
                context: Context,
                messenger: ShopLiveShortformMessageListener?,
                command: String,
                payload: Map<String, Any?>
            ) {
                when (command) {
                    "VIDEO_MUTED" -> {
                        isMuted = true
                    }

                    "VIDEO_UNMUTED" -> {
                        isMuted = false
                    }
                }
            }

            override fun onShare(context: Context, data: ShopLiveShortformShareData) {
                showShareDialog(data.title ?: return)
            }
        })

        binding.uploadButton.setOnClickListener {
            editorDialog.dismiss()
            editorDialog.show()
        }

        binding.settingButton.setOnClickListener {
            optionDialog.dismiss()
            optionDialog.show()
        }

        viewModel.visibleDetailTypeDataLiveData.observe(this) {
            ShopLiveShortform.setVisibleDetailTypeViews(it)
        }
    }

    private fun showShortformEditor() {
        if (viewModel.isCustomShortform) {
            ShopLiveCommon.setCustomize(
                ShopLiveCommonCustomizeData(
                    dialogData = ShopLiveCommonCustomizeDialogData(
                        backgroundRes = R.drawable.ic_editor_custom_background
                    )
                )
            )
        } else {
            ShopLiveCommon.setCustomize(ShopLiveCommonCustomizeData())
        }
        ShopLiveShortformEditor(this)
            .apply {
                setVideoEditorData(videoEditorData)
                setVideoEditorCustomize(
                    if (viewModel.isCustomShortform) {
                        ShopLiveVideoEditorCustomizeData(
                            mainData = ShopLiveVideoEditorCustomizeMainData(
                                titleTextSize = 16f,
                                backButtonRes = 0,
                                backButtonBackgroundRes = R.drawable.ic_editor_custom_back,
                                closeButtonRes = 0,
                                closeButtonBackgroundRes = R.drawable.ic_editor_custom_close,
                                confirmButtonTextColor = Color.BLACK,
                                confirmButtonBackgroundRes = R.drawable.ic_editor_custom_button_background,
                                videoPlayerRadius = 0f,
                                trimColor = Color.parseColor("#245EFF"),
                                trimHandleColor = Color.WHITE
                            ),
                            volumeData = ShopLiveVideoEditorCustomizeVoluemeData(
                                sliderBackgroundRes = R.drawable.ic_editor_custom_slider_background,
                                confirmButtonBackgroundRes = R.drawable.ic_editor_custom_button_background,
                                playButtonBackgroundRes = R.drawable.ic_editor_custom_icon_background,
                                pauseButtonBackgroundRes = R.drawable.ic_editor_custom_icon_background,
                            ),
                            cropData = ShopLiveVideoEditorCustomizeCropData(
                                confirmButtonBackgroundRes = R.drawable.ic_editor_custom_button_background,
                                playButtonBackgroundRes = R.drawable.ic_editor_custom_icon_background,
                                pauseButtonBackgroundRes = R.drawable.ic_editor_custom_icon_background,
                                cropColor = Color.parseColor("#245EFF"),
                            ),
                            filterData = ShopLiveVideoEditorCustomizeFilterData(
                                sliderBackgroundRes = R.drawable.ic_editor_custom_slider_background,
                                confirmButtonBackgroundRes = R.drawable.ic_editor_custom_button_background,
                                playButtonBackgroundRes = R.drawable.ic_editor_custom_icon_background,
                                pauseButtonBackgroundRes = R.drawable.ic_editor_custom_icon_background,
                                filterRadius = 0f,
                                filterSelectorRes = R.drawable.ic_editor_custom_filter_selector,
                            ),
                            playbackSpeedData = ShopLiveVideoEditorCustomizePlaybackSpeedData(
                                sliderBackgroundRes = R.drawable.ic_editor_custom_slider_background,
                                confirmButtonBackgroundRes = R.drawable.ic_editor_custom_button_background,
                                playButtonBackgroundRes = R.drawable.ic_editor_custom_icon_background,
                                pauseButtonBackgroundRes = R.drawable.ic_editor_custom_icon_background,
                            )
                        )
                    } else {
                        ShopLiveVideoEditorCustomizeData()
                    }
                )
                setCoverPickerData(coverPickerData)
                setCoverPickerCustomize(
                    if (viewModel.isCustomShortform) {
                        ShopLiveCoverPickerCustomizeData(
                            titleTextSize = 16f,
                            backButtonRes = 0,
                            backButtonBackgroundRes = R.drawable.ic_editor_custom_back,
                            confirmButtonTextColor = Color.BLACK,
                            confirmButtonBackgroundRes = R.drawable.ic_editor_custom_button_background,
                            cropColor = Color.parseColor("#245EFF"),
                            sliderThumbColor = Color.parseColor("#245EFF"),
                            sliderThumbRadius = 4.toDp(this@NativeShortformActivity),
                            cameraRollButtonBackgroundRes = R.drawable.ic_editor_custom_button_background,
                            videoPlayerRadius = 0f
                        )
                    } else {
                        ShopLiveCoverPickerCustomizeData()
                    }
                )
                setVideoUploaderData(ShopLiveVideoUploaderData().apply {
                    visibleContentData =
                        ShopLiveShortformEditorVisibleContentData().apply {
                            isDescriptionVisible = true
                            isTagsVisible = true
                        }
                })
                setHandler(object : ShopLiveShortformEditorHandler() {
                    override fun onSuccess(
                        activity: ComponentActivity,
                        resultData: ShopLiveEditorResultData
                    ) {
                        super.onSuccess(activity, resultData)
                        Toast.makeText(
                            this@NativeShortformActivity,
                            "onComplete",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    override fun onEvent(
                        context: Context,
                        command: String,
                        payload: Map<String, Any?>
                    ) {
                        super.onEvent(context, command, payload)
                        if (viewModel.isShowEditorEvent) {
                            Toast.makeText(
                                this@NativeShortformActivity,
                                listOfNotNull(
                                    "onEvent : $command", if (payload.isNotEmpty()) {
                                        "payload : $payload"
                                    } else {
                                        null
                                    }
                                ).joinToString(" "),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onError(context: Context, error: ShopLiveCommonError) {
                        super.onError(context, error)
                        Toast.makeText(
                            this@NativeShortformActivity,
                            "onError : $error",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    override fun onCancel() {
                        super.onCancel()
                        Toast.makeText(
                            this@NativeShortformActivity,
                            "onClosed",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
            }.start()
    }

    private val videoEditorLock = AtomicBoolean(false)
    private fun showVideoEditor() {
        if (videoEditorLock.getAndSet(true)) {
            return
        }
        this.activityResultRegistry
            .register(
                KEY_PICK_VISUAL_VIDEO_REQUEST,
                ActivityResultContracts.PickVisualMedia()
            ) {
                if (it != null) {
                    ShopLiveVideoEditor(this)
                        .setData(ShopLiveVideoEditorData())
                        .setHandler(object : ShopLiveVideoEditorHandler() {
                            override fun onSuccess(
                                videoEditorActivity: ComponentActivity,
                                result: ShopLiveEditorResultData
                            ) {
                                super.onSuccess(videoEditorActivity, result)
                                Toast.makeText(
                                    this@NativeShortformActivity,
                                    "onComplete : ${result.toString()}",
                                    Toast.LENGTH_SHORT
                                ).show()
                                ShopLiveVideoEditor.close()
                            }

                            override fun onError(context: Context, error: ShopLiveCommonError) {
                                super.onError(context, error)
                                Toast.makeText(
                                    this@NativeShortformActivity,
                                    "onError : $error",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                            override fun onCancel() {
                                super.onCancel()
                                Toast.makeText(
                                    this@NativeShortformActivity,
                                    "onClosed",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }).start(ShopLiveEditorLocalData(it))
                }
                videoEditorLock.set(false)
            }.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.VideoOnly))
    }

    private val imageEditorLock = AtomicBoolean(false)
    private fun showImageEditor() {
        if (imageEditorLock.getAndSet(true)) {
            return
        }
        this.activityResultRegistry
            .register(
                KEY_PICK_VISUAL_IMAGE_REQUEST,
                ActivityResultContracts.PickVisualMedia()
            ) {
                if (it != null) {
                    ShopLiveImageEditor(this)
                        .setData(ShopLiveImageEditorData())
                        .setHandler(object : ShopLiveImageEditorHandler() {
                            override fun onSuccess(result: ShopLiveEditorResultData) {
                                super.onSuccess(result)
                                Toast.makeText(
                                    this@NativeShortformActivity,
                                    "onComplete : ${result.toString()}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                            override fun onError(error: ShopLiveCommonError) {
                                super.onError(error)
                                Toast.makeText(
                                    this@NativeShortformActivity,
                                    "onError : $error",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                            override fun onCancel() {
                                super.onCancel()
                                Toast.makeText(
                                    this@NativeShortformActivity,
                                    "onClosed",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }).start(ShopLiveEditorLocalData(it))
                }
                imageEditorLock.set(false)
            }.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun showCoverPicker() {
        ShopLiveCoverPicker(this)
            .setData(ShopLiveCoverPickerData().apply {
                this.visibleActionButton = ShopLiveCoverPickerVisibleActionButton(true)
            })
            .setHandler(object : ShopLiveCoverPickerHandler() {
                override fun onSuccess(
                    coverPickerActivity: ComponentActivity,
                    result: ShopLiveEditorResultData
                ) {
                    super.onSuccess(coverPickerActivity, result)
                    Toast.makeText(
                        coverPickerActivity,
                        result.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                    ShopLiveCoverPicker.close()
                }

                override fun onError(context: Context, error: ShopLiveCommonError) {
                    super.onError(context, error)
                    Toast.makeText(
                        this@NativeShortformActivity,
                        error.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onCancel() {
                    super.onCancel()
                    Toast.makeText(this@NativeShortformActivity, "onClosed", Toast.LENGTH_SHORT)
                        .show()
                }
            })
            .start(ShopLiveCoverPickerUrlData("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"))
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