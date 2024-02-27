package cloud.shoplive.sample.shortform

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.recyclerview.widget.LinearLayoutManager
import cloud.shoplive.sample.PreferencesUtilImpl
import cloud.shoplive.sample.databinding.FragmentShortformCardTypeBinding
import cloud.shoplive.sample.databinding.FragmentShortformFullTypeBinding
import cloud.shoplive.sample.databinding.FragmentShortformHorizontalTypeBinding
import cloud.shoplive.sample.databinding.FragmentShortformVerticalTypeBinding
import cloud.shoplive.sample.extension.toDp
import cloud.shoplive.sample.shortform.NativeShortformActivity.Companion.PAGE_SHORTS_FULL
import cloud.shoplive.sample.shortform.NativeShortformActivity.Companion.PAGE_SHORTS_HORIZONTAL
import cloud.shoplive.sample.shortform.NativeShortformActivity.Companion.PAGE_SHORTS_MAIN
import cloud.shoplive.sample.shortform.NativeShortformActivity.Companion.PAGE_SHORTS_VERTICAL
import cloud.shoplive.sdk.common.ShopLiveCommonError
import cloud.shoplive.sdk.shorts.ShopLiveShortformBaseTypeHandler
import cloud.shoplive.sdk.shorts.ShopLiveShortformCollectionData
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

class ShortformDetailFragment : Fragment(), ShopLiveShortformPlayEnableListener,
    ShopLiveShortformSubmitListener {
    companion object {
        fun newInstance() = ShortformDetailFragment()
    }

    private var _binding: FragmentShortformFullTypeBinding? = null
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
        _binding = FragmentShortformFullTypeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.hashTagLiveData.observe(requireActivity()) { (hashTags, operator) ->
            binding.shortsFullTypeView.setHashTags(hashTags, operator)
        }
        viewModel.brandLiveData.observe(requireActivity()) {
            binding.shortsFullTypeView.setBrands(it)
        }
        viewModel.isVisibleTitleLiveData.observe(requireActivity()) {
            binding.shortsFullTypeView.setVisibleTitle(it)
        }
        viewModel.isVisibleBrandLiveData.observe(requireActivity()) {
            binding.shortsFullTypeView.setVisibleBrand(it)
        }
        viewModel.isVisibleProductCountLiveData.observe(requireActivity()) {
            binding.shortsFullTypeView.setVisibleProductCount(it)
        }
        viewModel.isVisibleViewCountLiveData.observe(requireActivity()) {
            binding.shortsFullTypeView.setVisibleViewCount(it)
        }
        viewModel.isEnableShuffleLiveData.observe(requireActivity()) {
            if (it) {
                binding.shortsFullTypeView.enableShuffle()
            } else {
                binding.shortsFullTypeView.disableShuffle()
            }
        }
        viewModel.isEnablePlayVideosLiveData.observe(requireActivity()) {
            if (it) {
                binding.shortsFullTypeView.enablePlayVideos()
            } else {
                binding.shortsFullTypeView.disablePlayVideos()
            }
        }
        viewModel.isEnablePlayWifiLiveData.observe(requireActivity()) {
            binding.shortsFullTypeView.setPlayOnlyWifi(it)
        }
        viewModel.radiusLiveData.observe(requireActivity()) {
            binding.shortsFullTypeView.setRadius(it?.toDp(requireActivity()) ?: return@observe)
        }
        viewModel.submitLiveData.observe(requireActivity()) {
            if (it == PAGE_SHORTS_FULL) submit()
        }
        binding.shortsFullTypeView.handler = object : ShopLiveShortformBaseTypeHandler() {
            override fun onError(context: Context, error: ShopLiveCommonError) {
                Toast.makeText(
                    requireContext(),
                    error.message ?: error.toString(),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

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
        _binding?.shortsFullTypeView?.submit()
        lifecycleScope.launch {
            delay(300)
            _binding?.shortsFullTypeView?.scrollToTop(true)
        }
    }

    override fun enablePlayVideos() {
        _binding?.shortsFullTypeView?.enablePlayVideos()
    }

    override fun disablePlayVideos() {
        _binding?.shortsFullTypeView?.disablePlayVideos()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
