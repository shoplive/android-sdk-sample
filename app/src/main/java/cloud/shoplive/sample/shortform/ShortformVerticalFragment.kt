package cloud.shoplive.sample.shortform

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import cloud.shoplive.sample.databinding.FragmentShortformVerticalTypeBinding
import cloud.shoplive.sdk.common.ShopLiveCommonError
import cloud.shoplive.sdk.shorts.ShopLiveShortformBaseTypeHandler
import cloud.shoplive.sdk.shorts.ShopLiveShortformPlayEnableListener
import cloud.shoplive.sdk.shorts.ShopLiveShortformScrollableListener
import cloud.shoplive.sdk.shorts.ShopLiveShortformSubmitListener

class ShortformVerticalFragment : Fragment(), ShopLiveShortformPlayEnableListener,
    ShopLiveShortformSubmitListener, ShopLiveShortformScrollableListener {
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
        binding.shortsVerticalTypeView.spanCount = 2
        if (binding.shortsVerticalTypeSnap.isChecked) {
            binding.shortsVerticalTypeView.enableSnap()
        }
        binding.shortsVerticalTypeSnap.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.shortsVerticalTypeView.enableSnap()
            } else {
                binding.shortsVerticalTypeView.disableSnap()
            }
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

    override fun scrollToTop(isSmooth: Boolean) {
        _binding?.shortsVerticalTypeView?.scrollToTop(isSmooth)
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
