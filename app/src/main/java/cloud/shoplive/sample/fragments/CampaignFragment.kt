package cloud.shoplive.sample.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import cloud.shoplive.sample.CampaignSettings
import cloud.shoplive.sample.R
import com.google.android.material.textfield.TextInputEditText

class CampaignFragment: Fragment() {

    private lateinit var etAccessKey: TextInputEditText
    private lateinit var etCampaignKey: TextInputEditText

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_campaign, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        etAccessKey = view.findViewById(R.id.etAccessKey)
        etCampaignKey = view.findViewById(R.id.etCampaignKey)

        etAccessKey.setText(CampaignSettings.accessKey(requireContext()))
        etCampaignKey.setText(CampaignSettings.campaignKey(requireContext()))
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.campaign_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.action_save -> {
                val accessKey = etAccessKey.text.toString()
                val campaignKey = etCampaignKey.text.toString()

                CampaignSettings.accessKey(requireContext(), accessKey)
                CampaignSettings.campaignKey(requireContext(), campaignKey)

                activity?.supportFragmentManager?.popBackStack()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}