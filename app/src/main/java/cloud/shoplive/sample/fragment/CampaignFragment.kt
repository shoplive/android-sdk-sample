package cloud.shoplive.sample.fragment

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import cloud.shoplive.sample.CampaignSettings
import cloud.shoplive.sample.R
import cloud.shoplive.sample.databinding.FragmentCampaignBinding

class CampaignFragment: Fragment() {

    private var _binding: FragmentCampaignBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        _binding = FragmentCampaignBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.etAccessKey.setText(CampaignSettings.accessKey(requireContext()))
        binding.etCampaignKey.setText(CampaignSettings.campaignKey(requireContext()))
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.campaign_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.action_save -> {
                val accessKey = binding.etAccessKey.text.toString()
                val campaignKey = binding.etCampaignKey.text.toString()

                CampaignSettings.accessKey(requireContext(), accessKey)
                CampaignSettings.campaignKey(requireContext(), campaignKey)

                activity?.supportFragmentManager?.popBackStack()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}