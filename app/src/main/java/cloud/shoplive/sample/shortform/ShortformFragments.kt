package cloud.shoplive.sample.shortform

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import cloud.shoplive.sample.R
import cloud.shoplive.sample.databinding.FragmentShortformCardTypeBinding
import cloud.shoplive.sample.databinding.FragmentShortformHorizontalTypeBinding
import cloud.shoplive.sample.databinding.FragmentShortformVerticalTypeBinding
import cloud.shoplive.sdk.common.ShopLiveCommonError
import cloud.shoplive.sdk.shorts.ShopLiveShortform
import cloud.shoplive.sdk.shorts.ShopLiveShortformBaseTypeHandler
import cloud.shoplive.sdk.shorts.ShopLiveShortformCardTypeView
import cloud.shoplive.sdk.shorts.ShopLiveShortformCollectionData
import cloud.shoplive.sdk.shorts.ShopLiveShortformPlayEnableListener
import cloud.shoplive.sdk.shorts.ShopLiveShortformSubmitListener
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

class ShortformMainFragment : Fragment(), ShopLiveShortformPlayEnableListener,
    ShopLiveShortformSubmitListener {
    companion object {
        fun newInstance() = ShortformMainFragment()
    }

    private var _binding: FragmentShortformCardTypeBinding? = null
    private val binding get() = _binding!!

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
        binding.shortsCardType1View.setSpanCount(1)
        binding.shortsCardType1View.setViewType(ShopLiveShortform.CardViewType.CARD_TYPE1)
        binding.shortsCardType2View.setSpanCount(1)
        binding.shortsCardType2View.setViewType(ShopLiveShortform.CardViewType.CARD_TYPE2)
        if (binding.shortsCardTypeSnap.isChecked) {
            currentCardTypeView?.enableSnap()
        }
        binding.shortsCardTypeSnap.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                currentCardTypeView?.enableSnap()
            } else {
                currentCardTypeView?.disableSnap()
            }
        }
        binding.shortsCardTypeWifiOnly.setOnCheckedChangeListener { _, isChecked ->
            binding.shortsCardType1View.setPlayOnlyWifi(isChecked)
            binding.shortsCardType2View.setPlayOnlyWifi(isChecked)
        }
        binding.shortsCardType1View.handler = object : ShopLiveShortformBaseTypeHandler() {
            override fun onError(error: ShopLiveCommonError) {
                Toast.makeText(
                    requireContext(),
                    error.message ?: error.toString(),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        binding.shortsCardType2View.handler = object : ShopLiveShortformBaseTypeHandler() {
            override fun onError(error: ShopLiveCommonError) {
                Toast.makeText(
                    requireContext(),
                    error.message ?: error.toString(),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        submit()

        binding.shortsCardTypeRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.shortsCardTypeRadioType1 -> {
                    toggleCardTypeView(ShopLiveShortform.CardViewType.CARD_TYPE1)
                    scrollToTop(false)
                    if (binding.shortsCardTypeSnap.isChecked) {
                        currentCardTypeView?.enableSnap()
                    } else {
                        currentCardTypeView?.disableSnap()
                    }
                    submit()
                }

                R.id.shortsCardTypeRadioType2 -> {
                    toggleCardTypeView(ShopLiveShortform.CardViewType.CARD_TYPE2)
                    scrollToTop(false)
                    if (binding.shortsCardTypeSnap.isChecked) {
                        currentCardTypeView?.enableSnap()
                    } else {
                        currentCardTypeView?.disableSnap()
                    }
                    submit()
                }
            }
        }
    }

    override fun submit() {
        currentCardTypeView?.submit()
    }

    private fun scrollToTop(isSmooth: Boolean) {
        currentCardTypeView?.scrollToTop(isSmooth)
    }

    override fun enablePlayVideos() {
        currentCardTypeView?.enablePlayVideos()
    }

    override fun disablePlayVideos() {
        currentCardTypeView?.disablePlayVideos()
    }

    private val currentCardTypeView: ShopLiveShortformCardTypeView?
        get() {
            return if (_binding?.shortsCardType1View?.visibility == View.VISIBLE) {
                _binding?.shortsCardType1View
            } else if (_binding?.shortsCardType2View?.visibility == View.VISIBLE) {
                _binding?.shortsCardType2View
            } else {
                null
            }
        }

    private fun toggleCardTypeView(type: ShopLiveShortform.CardViewType) {
        when (type) {
            ShopLiveShortform.CardViewType.CARD_TYPE1 -> {
                _binding?.shortsCardType1View?.visibility = View.VISIBLE
                _binding?.shortsCardType2View?.visibility = View.INVISIBLE

                _binding?.shortsCardType1View?.enablePlayVideos()
                _binding?.shortsCardType2View?.disablePlayVideos()

                _binding?.shortsCardType1View?.startAnimation(
                    AnimationUtils.loadAnimation(
                        requireContext(),
                        R.anim.shoplive_fade_in
                    )
                )
            }

            ShopLiveShortform.CardViewType.CARD_TYPE2 -> {
                _binding?.shortsCardType1View?.visibility = View.INVISIBLE
                _binding?.shortsCardType2View?.visibility = View.VISIBLE

                _binding?.shortsCardType1View?.disablePlayVideos()
                _binding?.shortsCardType2View?.enablePlayVideos()

                _binding?.shortsCardType2View?.startAnimation(
                    AnimationUtils.loadAnimation(
                        requireContext(),
                        R.anim.shoplive_fade_in
                    )
                )
            }
        }
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
        if (binding.shortsVerticalTypeSnap.isChecked) {
            binding.shortsVerticalTypeView.enableSnap()
        }
        binding.shortsVerticalTypeView.setViewType(ShopLiveShortform.CardViewType.CARD_TYPE2)
        binding.shortsVerticalTypeSnap.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.shortsVerticalTypeView.enableSnap()
            } else {
                binding.shortsVerticalTypeView.disableSnap()
            }
        }
        binding.shortsVerticalTypeWifiOnly.setOnCheckedChangeListener { _, isChecked ->
            binding.shortsVerticalTypeView.setPlayOnlyWifi(isChecked)
        }
        binding.shortsVerticalTypeView.handler = object : ShopLiveShortformBaseTypeHandler() {
            override fun onError(error: ShopLiveCommonError) {
                Toast.makeText(
                    requireContext(),
                    error.message ?: error.toString(),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        submit()
    }

    override fun submit() {
        _binding?.shortsVerticalTypeView?.submit()
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShortformHorizontalTypeBinding.inflate(inflater, container, false)
        return binding.root
    }

    private val enablePlayLiveData: MutableLiveData<Boolean?> = MutableLiveData(null)
    private val snapLiveData = MutableLiveData(false)
    private val onlyWifiLiveData = MutableLiveData(false)

    private val adapter = ShortformSampleAdapter(enablePlayLiveData, snapLiveData, onlyWifiLiveData)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.shortsHorizontalTypeRecyclerView.layoutManager =
            LinearLayoutManager(requireContext())
        binding.shortsHorizontalTypeRecyclerView.adapter = adapter

        binding.shortsHorizontalTypeSnap.setOnCheckedChangeListener { _, isChecked ->
            snapLiveData.value = isChecked
        }
        binding.shortsHorizontalTypeWifiOnly.setOnCheckedChangeListener { _, isChecked ->
            onlyWifiLiveData.value = isChecked
        }
        submit()
    }

    override fun submit() {
        lifecycleScope.launch {
            adapter.submitList(
                listOf(
                    async {
                        ShortformSampleData.convertData(
                            requireContext(),
                            "ALL",
                            ShopLiveShortformCollectionData()
                        )
                    },
                    async {
                        ShortformSampleData.convertData(
                            requireContext(),
                            "HashTags : shoplive",
                            ShopLiveShortformCollectionData().apply {
                                tags = listOf("shoplive")
                            }
                        )
                    },
                    async {
                        ShortformSampleData.convertData(
                            requireContext(),
                            "HashTags : test",
                            ShopLiveShortformCollectionData().apply {
                                tags = listOf("test")
                            }
                        )
                    },
                    async {
                        ShortformSampleData.convertData(
                            requireContext(),
                            "Brands : shoplive",
                            ShopLiveShortformCollectionData().apply {
                                brands = listOf("shoplive")
                            }
                        )
                    }
                ).awaitAll()
            )
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