package cloud.shoplive.sample.shortform

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import cloud.shoplive.sample.databinding.FragmentShortformHorizontalTypeBinding
import cloud.shoplive.sdk.common.ShopLiveCommonError
import cloud.shoplive.sdk.shorts.ShopLiveShortform
import cloud.shoplive.sdk.shorts.ShopLiveShortformBaseTypeHandler
import cloud.shoplive.sdk.shorts.ShopLiveShortformPlayEnableListener
import cloud.shoplive.sdk.shorts.ShopLiveShortformScrollableListener
import cloud.shoplive.sdk.shorts.ShopLiveShortformSubmitListener

class ShortformHorizontalFragment : Fragment(), ShopLiveShortformPlayEnableListener,
    ShopLiveShortformSubmitListener, ShopLiveShortformScrollableListener {
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.shortsHorizontalType1View.setPlayableType(ShopLiveShortform.PlayableType.CENTER)
        if (binding.shortsHorizontalTypeSnap.isChecked) {
            binding.shortsHorizontalType1View.enableSnap()
            binding.shortsHorizontalType1View.setPlayableType(ShopLiveShortform.PlayableType.FIRST)
        }
        binding.shortsHorizontalTypeSnap.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.shortsHorizontalType1View.enableSnap()
                binding.shortsHorizontalType1View.setPlayableType(ShopLiveShortform.PlayableType.FIRST)
            } else {
                binding.shortsHorizontalType1View.disableSnap()
                binding.shortsHorizontalType1View.setPlayableType(ShopLiveShortform.PlayableType.CENTER)
            }
        }
        binding.shortsHorizontalType1View.handler = object : ShopLiveShortformBaseTypeHandler() {
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
        _binding?.shortsHorizontalType1View?.submit()
    }

    override fun scrollToTop(isSmooth: Boolean) {
        _binding?.shortsHorizontalType1View?.scrollToTop(isSmooth)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun enablePlayVideos() {
        _binding?.shortsHorizontalType1View?.enablePlayVideos()
    }

    override fun disablePlayVideos() {
        _binding?.shortsHorizontalType1View?.disablePlayVideos()
    }
}