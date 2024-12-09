package cloud.shoplive.sample.shortform

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.recyclerview.widget.LinearLayoutManager
import cloud.shoplive.sample.PreferencesUtilImpl
import cloud.shoplive.sample.databinding.FragmentShortformDetailTypeBinding
import cloud.shoplive.sample.databinding.FragmentShortformHorizontalTypeBinding
import cloud.shoplive.sample.databinding.FragmentShortformMainTypeBinding
import cloud.shoplive.sample.databinding.FragmentShortformVerticalTypeBinding
import cloud.shoplive.sample.extension.toDp
import cloud.shoplive.sample.shortform.NativeShortformActivity.Companion.PAGE_SHORTS_FULL
import cloud.shoplive.sample.shortform.NativeShortformActivity.Companion.PAGE_SHORTS_HORIZONTAL
import cloud.shoplive.sample.shortform.NativeShortformActivity.Companion.PAGE_SHORTS_MAIN
import cloud.shoplive.sample.shortform.NativeShortformActivity.Companion.PAGE_SHORTS_VERTICAL
import cloud.shoplive.sdk.shorts.ShopLiveShortformCollectionData
import cloud.shoplive.sdk.shorts.ShopLiveShortformHandler
import cloud.shoplive.sdk.shorts.ShopLiveShortformPlayEnableListener
import cloud.shoplive.sdk.shorts.ShopLiveShortformSubmitListener
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class ShortformMainFragment : Fragment(), ShopLiveShortformPlayEnableListener,
    ShopLiveShortformSubmitListener {
    companion object {
        fun newInstance() = ShortformMainFragment()
    }

    private var _binding: FragmentShortformMainTypeBinding? = null
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
        _binding = FragmentShortformMainTypeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.shortsCardTypeView.setSpanCount(1)
        binding.shortsCardTypeView.setViewType(viewModel.getSavedCardType())
        binding.shortsCardTypeView.handler = ShortformSampleData.handler

        viewModel.shortsCollectionIdLiveData.observe(requireActivity()) {
            binding.shortsCardTypeView.setShortsCollectionId(it)
        }
        viewModel.hashTagLiveData.observe(requireActivity()) { (hashTags, operator) ->
            binding.shortsCardTypeView.setHashTags(hashTags, operator)
        }
        viewModel.brandLiveData.observe(requireActivity()) {
            binding.shortsCardTypeView.setBrands(it)
        }
        viewModel.skusLiveData.observe(requireActivity()) {
            binding.shortsCardTypeView.setSkus(it)
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
            if (viewModel.submitLiveData.value != PAGE_SHORTS_MAIN) {
                return@observe
            }
            if (it) {
                binding.shortsCardTypeView.enablePlayVideos()
            } else {
                binding.shortsCardTypeView.disablePlayVideos()
            }
        }
        viewModel.isEnablePlayWifiLiveData.observe(requireActivity()) {
            binding.shortsCardTypeView.setPlayOnlyWifi(it)
        }
        viewModel.radiusLiveData.observe(requireActivity()) {
            binding.shortsCardTypeView.setRadius(it?.toDp(requireActivity()) ?: return@observe)
        }
        viewModel.submitLiveData.observe(requireActivity()) {
            if (it == PAGE_SHORTS_MAIN) submit()
        }

        initializeCollectFlow()
    }

    private fun initializeCollectFlow() {
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            viewModel.needInitializeTabFlow
                .map { !it.contains(PAGE_SHORTS_MAIN) }
                .filter { it }
                .onEach {
                    submit()
                    viewModel.initializeTab(PAGE_SHORTS_MAIN)
                }.collect()
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
        binding.shortsVerticalTypeView.handler = ShortformSampleData.handler

        viewModel.shortsCollectionIdLiveData.observe(requireActivity()) {
            binding.shortsVerticalTypeView.setShortsCollectionId(it)
        }
        viewModel.hashTagLiveData.observe(requireActivity()) { (hashTags, operator) ->
            binding.shortsVerticalTypeView.setHashTags(hashTags, operator)
        }
        viewModel.brandLiveData.observe(requireActivity()) {
            binding.shortsVerticalTypeView.setBrands(it)
        }
        viewModel.skusLiveData.observe(requireActivity()) {
            binding.shortsVerticalTypeView.setSkus(it)
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
            if (viewModel.submitLiveData.value != PAGE_SHORTS_VERTICAL) {
                return@observe
            }
            if (it) {
                binding.shortsVerticalTypeView.enablePlayVideos()
            } else {
                binding.shortsVerticalTypeView.disablePlayVideos()
            }
        }
        viewModel.isEnablePlayWifiLiveData.observe(requireActivity()) {
            binding.shortsVerticalTypeView.setPlayOnlyWifi(it)
        }
        viewModel.radiusLiveData.observe(requireActivity()) {
            binding.shortsVerticalTypeView.setRadius(it?.toDp(requireActivity()) ?: return@observe)
        }
        viewModel.submitLiveData.observe(requireActivity()) {
            if (it == PAGE_SHORTS_VERTICAL) submit()
        }

        initializeCollectFlow()
    }

    private fun initializeCollectFlow() {
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            viewModel.needInitializeTabFlow
                .map { !it.contains(PAGE_SHORTS_VERTICAL) }
                .filter { it }
                .onEach {
                    submit()
                    viewModel.initializeTab(PAGE_SHORTS_VERTICAL)
                }.collect()
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

        viewModel.submitLiveData.observe(requireActivity()) {
            if (it == PAGE_SHORTS_HORIZONTAL) submit()
        }

        initializeCollectFlow()
    }

    private fun initializeCollectFlow() {
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            viewModel.needInitializeTabFlow
                .map { !it.contains(PAGE_SHORTS_HORIZONTAL) }
                .filter { it }
                .onEach {
                    submit()
                    viewModel.initializeTab(PAGE_SHORTS_HORIZONTAL)
                }
                .collect()
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
                                    skus = viewModel.skusLiveData.value
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
    }

    override fun disablePlayVideos() {
        enablePlayLiveData.value = false
    }
}

class ShortformFullFragment : Fragment(), ShopLiveShortformPlayEnableListener,
    ShopLiveShortformSubmitListener {
    companion object {
        fun newInstance() = ShortformFullFragment()
    }

    private var _binding: FragmentShortformDetailTypeBinding? = null
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
        _binding = FragmentShortformDetailTypeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.shortsCollectionIdLiveData.observe(requireActivity()) {
            binding.shortsDetailTypeView.setShortsCollectionId(it)
        }
        viewModel.hashTagLiveData.observe(requireActivity()) { (hashTags, operator) ->
            binding.shortsDetailTypeView.setHashTags(hashTags, operator)
        }
        viewModel.brandLiveData.observe(requireActivity()) {
            binding.shortsDetailTypeView.setBrands(it)
        }
        viewModel.skusLiveData.observe(requireActivity()) {
            binding.shortsDetailTypeView.setSkus(it)
        }
        viewModel.isVisibleTitleLiveData.observe(requireActivity()) {
            binding.shortsDetailTypeView.setVisibleTitle(it)
        }
        viewModel.isVisibleBrandLiveData.observe(requireActivity()) {
            binding.shortsDetailTypeView.setVisibleBrand(it)
        }
        viewModel.isVisibleProductCountLiveData.observe(requireActivity()) {
            binding.shortsDetailTypeView.setVisibleProductCount(it)
        }
        viewModel.isVisibleViewCountLiveData.observe(requireActivity()) {
            binding.shortsDetailTypeView.setVisibleViewCount(it)
        }
        viewModel.isEnableShuffleLiveData.observe(requireActivity()) {
            if (it) {
                binding.shortsDetailTypeView.enableShuffle()
            } else {
                binding.shortsDetailTypeView.disableShuffle()
            }
        }
        viewModel.isEnablePlayVideosLiveData.observe(requireActivity()) {
            if (viewModel.submitLiveData.value != PAGE_SHORTS_FULL) {
                return@observe
            }
            if (it) {
                binding.shortsDetailTypeView.enablePlayVideos()
            } else {
                binding.shortsDetailTypeView.disablePlayVideos()
            }
        }
        viewModel.isEnablePlayWifiLiveData.observe(requireActivity()) {
            binding.shortsDetailTypeView.setPlayOnlyWifi(it)
        }
        viewModel.radiusLiveData.observe(requireActivity()) {
            binding.shortsDetailTypeView.setRadius(it?.toDp(requireActivity()) ?: return@observe)
        }
        viewModel.submitLiveData.observe(requireActivity()) {
            if (it == PAGE_SHORTS_FULL) submit()
        }
        binding.shortsDetailTypeView.handler = ShortformSampleData.handler
        initializeCollectFlow()
    }

    private fun initializeCollectFlow() {
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            viewModel.needInitializeTabFlow
                .map { !it.contains(PAGE_SHORTS_FULL) }
                .filter { it }
                .onEach {
                    submit()
                    viewModel.initializeTab(PAGE_SHORTS_FULL)
                }.collect()
        }
    }

    override fun submit() {
        _binding?.shortsDetailTypeView?.submit()
        lifecycleScope.launch {
            delay(300)
            _binding?.shortsDetailTypeView?.scrollToTop(true)
        }
    }

    override fun enablePlayVideos() {
        _binding?.shortsDetailTypeView?.enablePlayVideos()
    }

    override fun disablePlayVideos() {
        _binding?.shortsDetailTypeView?.disablePlayVideos()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}