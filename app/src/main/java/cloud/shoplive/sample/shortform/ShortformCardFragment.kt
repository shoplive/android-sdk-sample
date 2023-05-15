package cloud.shoplive.sample.shortform

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.fragment.app.Fragment
import cloud.shoplive.sample.R
import cloud.shoplive.sample.databinding.FragmentShortformCardTypeBinding
import cloud.shoplive.sdk.common.ShopLiveCommonError
import cloud.shoplive.sdk.shorts.ShopLiveShortform
import cloud.shoplive.sdk.shorts.ShopLiveShortformBaseTypeHandler
import cloud.shoplive.sdk.shorts.ShopLiveShortformCardTypeView
import cloud.shoplive.sdk.shorts.ShopLiveShortformPlayEnableListener
import cloud.shoplive.sdk.shorts.ShopLiveShortformScrollableListener
import cloud.shoplive.sdk.shorts.ShopLiveShortformSubmitListener

class ShortformCardFragment : Fragment(), ShopLiveShortformPlayEnableListener,
    ShopLiveShortformSubmitListener,
    ShopLiveShortformScrollableListener {
    companion object {
        fun newInstance() = ShortformCardFragment()
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
        binding.shortsCardType1View.spanCount = 1
        binding.shortsCardType1View.setViewType(ShopLiveShortform.CardViewType.CARD_TYPE1)
        binding.shortsCardType2View.spanCount = 1
        binding.shortsCardType2View.setViewType(ShopLiveShortform.CardViewType.CARD_TYPE2)
        binding.shortsCardType3View.spanCount = 1
        binding.shortsCardType3View.setViewType(ShopLiveShortform.CardViewType.CARD_TYPE3)
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
        binding.shortsCardType3View.handler = object : ShopLiveShortformBaseTypeHandler() {
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
                R.id.shortsCardTypeRadioType3 -> {
                    toggleCardTypeView(ShopLiveShortform.CardViewType.CARD_TYPE3)
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

    override fun scrollToTop(isSmooth: Boolean) {
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
            } else if (_binding?.shortsCardType3View?.visibility == View.VISIBLE) {
                _binding?.shortsCardType3View
            } else {
                null
            }
        }

    private fun toggleCardTypeView(type: ShopLiveShortform.CardViewType) {
        when (type) {
            ShopLiveShortform.CardViewType.CARD_TYPE1 -> {
                _binding?.shortsCardType1View?.visibility = View.VISIBLE
                _binding?.shortsCardType2View?.visibility = View.INVISIBLE
                _binding?.shortsCardType3View?.visibility = View.INVISIBLE

                _binding?.shortsCardType1View?.enablePlayVideos()
                _binding?.shortsCardType2View?.disablePlayVideos()
                _binding?.shortsCardType3View?.disablePlayVideos()

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
                _binding?.shortsCardType3View?.visibility = View.INVISIBLE

                _binding?.shortsCardType1View?.disablePlayVideos()
                _binding?.shortsCardType2View?.enablePlayVideos()
                _binding?.shortsCardType3View?.disablePlayVideos()

                _binding?.shortsCardType2View?.startAnimation(
                    AnimationUtils.loadAnimation(
                        requireContext(),
                        R.anim.shoplive_fade_in
                    )
                )
            }
            ShopLiveShortform.CardViewType.CARD_TYPE3 -> {
                _binding?.shortsCardType1View?.visibility = View.INVISIBLE
                _binding?.shortsCardType2View?.visibility = View.INVISIBLE
                _binding?.shortsCardType3View?.visibility = View.VISIBLE

                _binding?.shortsCardType1View?.disablePlayVideos()
                _binding?.shortsCardType2View?.disablePlayVideos()
                _binding?.shortsCardType3View?.enablePlayVideos()

                _binding?.shortsCardType3View?.startAnimation(
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


