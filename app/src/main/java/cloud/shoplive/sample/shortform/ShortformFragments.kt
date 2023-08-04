package cloud.shoplive.sample.shortform

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cloud.shoplive.sample.PreferencesUtilImpl
import cloud.shoplive.sample.R
import cloud.shoplive.sample.databinding.FragmentShortformCardTypeBinding
import cloud.shoplive.sample.databinding.FragmentShortformHorizontalTypeBinding
import cloud.shoplive.sample.databinding.FragmentShortformVerticalTypeBinding
import cloud.shoplive.sample.extension.toDp
import cloud.shoplive.sdk.common.ShopLiveCommonError
import cloud.shoplive.sdk.shorts.ShopLiveShortform
import cloud.shoplive.sdk.shorts.ShopLiveShortformBaseTypeHandler
import cloud.shoplive.sdk.shorts.ShopLiveShortformCardTypeView
import cloud.shoplive.sdk.shorts.ShopLiveShortformCollectionData
import cloud.shoplive.sdk.shorts.ShopLiveShortformPlayEnableListener
import cloud.shoplive.sdk.shorts.ShopLiveShortformSubmitListener
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ShortformMainFragment : Fragment(), ShopLiveShortformPlayEnableListener,
    ShopLiveShortformSubmitListener {
    companion object {
        fun newInstance() = ShortformMainFragment()
    }

    private var _binding: FragmentShortformCardTypeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ShortformViewModel by activityViewModels {
        viewModelFactory {
            initializer {
                ShortformViewModel(PreferencesUtilImpl(requireContext()))
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShortformCardTypeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.shortsCardTypeView.setSpanCount(1)
        binding.shortsCardTypeView.setViewType(viewModel.getSavedCardType())
        submit()

        viewModel.hashTagLiveData.observe(requireActivity()) { (hashTags, operator) ->
            binding.shortsCardTypeView.setHashTags(hashTags, operator)
        }
        viewModel.brandLiveData.observe(requireActivity()) {
            binding.shortsCardTypeView.setBrands(it)
        }
        viewModel.isVisibleTitleLiveData.observe(requireActivity()) {
            binding.shortsCardTypeView.setVisibleTitle(it)
        }
        viewModel.isVisibleDescriptionLiveData.observe(requireActivity()) {
            binding.shortsCardTypeView.setVisibleDescription(it)
        }
        viewModel.isVisibleBrandLiveData.observe(requireActivity()) {
            binding.shortsCardTypeView.setVisibleBrand(it)
        }
        viewModel.isVisibleProductCountLiveData.observe(requireActivity()) {
            binding.shortsCardTypeView.setVisibleProductCount(it)
        }
        viewModel.isVisibleViewCountLiveData.observe(requireActivity()) {
            binding.shortsCardTypeView.setVisibleViewCount(it)
        }
        viewModel.isEnableShuffleLiveData.observe(requireActivity()) {
            if (it) {
                binding.shortsCardTypeView.enableShuffle()
            } else {
                binding.shortsCardTypeView.disableShuffle()
            }
        }
        viewModel.isEnableSnapLiveData.observe(requireActivity()) {
            if (it) {
                binding.shortsCardTypeView.enableSnap()
            } else {
                binding.shortsCardTypeView.disableSnap()
            }
        }
        viewModel.isEnablePlayVideosLiveData.observe(requireActivity()) {
            if (it) {
                binding.shortsCardTypeView.enablePlayVideos()
            } else {
                binding.shortsCardTypeView.disablePlayVideos()
            }
        }
        viewModel.radiusLiveData.observe(requireActivity()) {
            binding.shortsCardTypeView.setRadius(it?.toDp(requireActivity()) ?: 0f)
        }
        viewModel.distanceLiveData.observe(requireActivity()) {
            val distance = it ?: return@observe
            binding.shortsCardTypeView.addItemDecoration(object : RecyclerView.ItemDecoration() {
                override fun getItemOffsets(
                    outRect: Rect,
                    view: View,
                    parent: RecyclerView,
                    state: RecyclerView.State
                ) {
                    super.getItemOffsets(outRect, view, parent, state)
                    parent.getChildAdapterPosition(view)
                    val spanCount = binding.shortsCardTypeView.spanCount
                    if (spanCount == 1) {
                        outRect.top = (distance / 2).toDp(view.context).toInt()
                        outRect.bottom = (distance / 2).toDp(view.context).toInt()
                        outRect.left = distance.toDp(view.context).toInt()
                        outRect.right = distance.toDp(view.context).toInt()
                    } else {
                        if ((parent.getChildAdapterPosition(view) % spanCount) == 0) {
                            outRect.top = (distance / 2).toDp(view.context).toInt()
                            outRect.bottom = (distance / 2).toDp(view.context).toInt()
                            outRect.left = distance.toDp(view.context).toInt()
                            outRect.right = (distance / 2).toDp(view.context).toInt()
                        } else if ((parent.getChildAdapterPosition(view) % spanCount) == spanCount - 1) {
                            outRect.top = (distance / 2).toDp(view.context).toInt()
                            outRect.bottom = (distance / 2).toDp(view.context).toInt()
                            outRect.left = (distance / 2).toDp(view.context).toInt()
                            outRect.right = distance.toDp(view.context).toInt()
                        } else {
                            outRect.top = (distance / 2).toDp(view.context).toInt()
                            outRect.bottom = (distance / 2).toDp(view.context).toInt()
                            outRect.left = (distance / 2).toDp(view.context).toInt()
                            outRect.right = (distance / 2).toDp(view.context).toInt()
                        }
                    }
                }
            })
        }
        viewModel.submitLiveData.observe(requireActivity()) {
            submit()
        }
    }

    override fun submit() {
        _binding?.shortsCardTypeView?.submit()
        lifecycleScope.launch {
            delay(300)
            _binding?.shortsCardTypeView?.scrollToTop(true)
        }
    }

    override fun enablePlayVideos() {
        _binding?.shortsCardTypeView?.enablePlayVideos()
    }

    override fun disablePlayVideos() {
        _binding?.shortsCardTypeView?.disablePlayVideos()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

class ShortformVerticalFragment : Fragment(), ShopLiveShortformPlayEnableListener,
    ShopLiveShortformSubmitListener {
    companion object {
        fun newInstance() = ShortformVerticalFragment()
    }

    private var _binding: FragmentShortformVerticalTypeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ShortformViewModel by activityViewModels {
        viewModelFactory {
            initializer {
                ShortformViewModel(PreferencesUtilImpl(requireContext()))
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShortformVerticalTypeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.shortsVerticalTypeView.setSpanCount(2)
        binding.shortsVerticalTypeView.setViewType(viewModel.getSavedCardType())
        submit()

        viewModel.hashTagLiveData.observe(requireActivity()) { (hashTags, operator) ->
            binding.shortsVerticalTypeView.setHashTags(hashTags, operator)
        }
        viewModel.brandLiveData.observe(requireActivity()) {
            binding.shortsVerticalTypeView.setBrands(it)
        }
        viewModel.isVisibleTitleLiveData.observe(requireActivity()) {
            binding.shortsVerticalTypeView.setVisibleTitle(it)
        }
        viewModel.isVisibleBrandLiveData.observe(requireActivity()) {
            binding.shortsVerticalTypeView.setVisibleBrand(it)
        }
        viewModel.isVisibleProductCountLiveData.observe(requireActivity()) {
            binding.shortsVerticalTypeView.setVisibleProductCount(it)
        }
        viewModel.isVisibleViewCountLiveData.observe(requireActivity()) {
            binding.shortsVerticalTypeView.setVisibleViewCount(it)
        }
        viewModel.isEnableShuffleLiveData.observe(requireActivity()) {
            if (it) {
                binding.shortsVerticalTypeView.enableShuffle()
            } else {
                binding.shortsVerticalTypeView.disableShuffle()
            }
        }
        viewModel.isEnableSnapLiveData.observe(requireActivity()) {
            if (it) {
                binding.shortsVerticalTypeView.enableSnap()
            } else {
                binding.shortsVerticalTypeView.disableSnap()
            }
        }
        viewModel.isEnablePlayVideosLiveData.observe(requireActivity()) {
            if (it) {
                binding.shortsVerticalTypeView.enablePlayVideos()
            } else {
                binding.shortsVerticalTypeView.disablePlayVideos()
            }
        }
        viewModel.radiusLiveData.observe(requireActivity()) {
            binding.shortsVerticalTypeView.setRadius(it?.toDp(requireActivity()) ?: 0f)
        }
        viewModel.distanceLiveData.observe(requireActivity()) {
            val distance = it ?: return@observe
            binding.shortsVerticalTypeView.addItemDecoration(object :
                RecyclerView.ItemDecoration() {
                override fun getItemOffsets(
                    outRect: Rect,
                    view: View,
                    parent: RecyclerView,
                    state: RecyclerView.State
                ) {
                    super.getItemOffsets(outRect, view, parent, state)
                    parent.getChildAdapterPosition(view)
                    val spanCount = binding.shortsVerticalTypeView.spanCount
                    if ((parent.getChildAdapterPosition(view) % spanCount) == 0) {
                        outRect.top = (distance / 2).toDp(view.context).toInt()
                        outRect.bottom = (distance / 2).toDp(view.context).toInt()
                        outRect.left = distance.toDp(view.context).toInt()
                        outRect.right = (distance / 2).toDp(view.context).toInt()
                    } else if ((parent.getChildAdapterPosition(view) % spanCount) == spanCount - 1) {
                        outRect.top = (distance / 2).toDp(view.context).toInt()
                        outRect.bottom = (distance / 2).toDp(view.context).toInt()
                        outRect.left = (distance / 2).toDp(view.context).toInt()
                        outRect.right = distance.toDp(view.context).toInt()
                    } else {
                        outRect.top = (distance / 2).toDp(view.context).toInt()
                        outRect.bottom = (distance / 2).toDp(view.context).toInt()
                        outRect.left = (distance * 3 / 4).toDp(view.context).toInt()
                        outRect.right = (distance * 3 / 4).toDp(view.context).toInt()
                    }
                }
            })
        }
        viewModel.submitLiveData.observe(requireActivity()) {
            submit()
        }
    }

    override fun submit() {
        _binding?.shortsVerticalTypeView?.submit()
        lifecycleScope.launch {
            delay(300)
            _binding?.shortsVerticalTypeView?.scrollToTop(true)
        }
    }

    override fun enablePlayVideos() {
        _binding?.shortsVerticalTypeView?.enablePlayVideos()
    }

    override fun disablePlayVideos() {
        _binding?.shortsVerticalTypeView?.disablePlayVideos()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

class ShortformHorizontalFragment : Fragment(), ShopLiveShortformPlayEnableListener,
    ShopLiveShortformSubmitListener {
    companion object {
        fun newInstance() = ShortformHorizontalFragment()
    }

    private var _binding: FragmentShortformHorizontalTypeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ShortformViewModel by activityViewModels {
        viewModelFactory {
            initializer {
                ShortformViewModel(PreferencesUtilImpl(requireContext()))
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShortformHorizontalTypeBinding.inflate(inflater, container, false)
        return binding.root
    }

    private val enablePlayLiveData: MutableLiveData<Boolean?> = MutableLiveData(null)

    private val linearLayoutManager by lazy { LinearLayoutManager(requireContext()) }
    private val adapter by lazy {
        ShortformSampleAdapter(viewModel, enablePlayLiveData)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.shortsHorizontalTypeRecyclerView.layoutManager = linearLayoutManager
        binding.shortsHorizontalTypeRecyclerView.adapter = adapter
        binding.shortsHorizontalTypeRecyclerView.animation = null

        submit()

        viewModel.submitLiveData.observe(requireActivity()) {
            submit()
        }
    }

    override fun submit() {
        _binding ?: return
        lifecycleScope.launch {
            adapter.submitList(
                listOf(
                    async {
                        kotlin.runCatching {
                            ShortformSampleData.getData(
                                requireContext(),
                                "Custom",
                                ShopLiveShortformCollectionData().apply {
                                    tags = viewModel.hashTagLiveData.value?.first
                                    tagSearchOperator = viewModel.hashTagLiveData.value?.second
                                    brands = viewModel.brandLiveData.value
                                    shuffle = viewModel.isEnableShuffleLiveData.value ?: false
                                }
                            )
                        }.getOrNull()
                    },
                    async {
                        kotlin.runCatching {
                            ShortformSampleData.getData(
                                requireContext(),
                                "HashTags : life",
                                ShopLiveShortformCollectionData().apply {
                                    tags = listOf("life")
                                    shuffle = viewModel.isEnableShuffleLiveData.value ?: false
                                }
                            )
                        }.getOrNull()
                    },
                    async {
                        kotlin.runCatching {
                            ShortformSampleData.getData(
                                requireContext(),
                                "HashTags : fashion",
                                ShopLiveShortformCollectionData().apply {
                                    tags = listOf("fashion")
                                    shuffle = viewModel.isEnableShuffleLiveData.value ?: false
                                }
                            )
                        }.getOrNull()
                    },
                    async {
                        kotlin.runCatching {
                            ShortformSampleData.getData(
                                requireContext(),
                                "Brands : Shoplive",
                                ShopLiveShortformCollectionData().apply {
                                    brands = listOf("Shoplive")
                                    shuffle = viewModel.isEnableShuffleLiveData.value ?: false
                                }
                            )
                        }.getOrNull()
                    },
                ).awaitAll().filterNotNull()
            ) {
                linearLayoutManager.scrollToPositionWithOffset(0, 0)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun enablePlayVideos() {
        enablePlayLiveData.value = true
        enablePlayLiveData.value = null
    }

    override fun disablePlayVideos() {
        enablePlayLiveData.value = false
        enablePlayLiveData.value = null
    }
}