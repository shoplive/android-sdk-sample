package cloud.shoplive.sample.fragment

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import cloud.shoplive.sample.CampaignSettings
import cloud.shoplive.sample.R
import cloud.shoplive.sample.databinding.FragmentUserBinding
import cloud.shoplive.sdk.ShopLiveUser
import cloud.shoplive.sdk.ShopLiveUserGender
import java.lang.NumberFormatException

class UserFragment: Fragment() {

    private var _binding: FragmentUserBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        _binding = FragmentUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvTokenGuide.paintFlags = Paint.UNDERLINE_TEXT_FLAG
        binding.tvTokenGuide.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.label_token_guide_link)))
            startActivity(intent)
        }

        binding.rgAuth.setOnCheckedChangeListener { _, checkedId ->
            when(checkedId) {
                R.id.rbUser -> {
                    binding.tvAuth.text = getString(R.string.label_use_user)
                    binding.scrollView.visibility = View.VISIBLE
                    binding.tvTokenGuide.visibility = View.GONE
                    binding.etTokenLayout.visibility = View.GONE
                }
                R.id.rbToken -> {
                    binding.tvAuth.text = getString(R.string.label_use_token)
                    binding.scrollView.visibility = View.VISIBLE
                    binding.tvTokenGuide.visibility = View.VISIBLE
                    binding.etTokenLayout.visibility = View.VISIBLE
                }
                R.id.rbGuest -> {
                    binding.tvAuth.text = getString(R.string.label_use_guest)
                    binding.scrollView.visibility = View.GONE
                }
            }
        }

        when(CampaignSettings.authType(requireContext())) {
            0 -> binding.rgAuth.check(R.id.rbUser)
            1 -> binding.rgAuth.check(R.id.rbToken)
            2 -> binding.rgAuth.check(R.id.rbGuest)
        }
        setUser(CampaignSettings.user(requireContext()))
        binding.etToken.setText(CampaignSettings.jwt(requireContext()))

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.user_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.action_save -> {
                saveUser()
                CampaignSettings.jwt(requireContext(), binding.etToken.text.toString())
                CampaignSettings.authType(
                    requireContext(), when (binding.rgAuth.checkedRadioButtonId) {
                        R.id.rbUser -> 0
                        R.id.rbToken -> 1
                        else -> 2
                    }
                )
                activity?.supportFragmentManager?.popBackStack()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setUser(user: ShopLiveUser?) {
        user?.let {
            binding.etUserId.setText(it.userId)
            binding.etUserName.setText(it.userName)

            try {
                binding.etAge.setText(it.age.toString())
            } catch (e: NumberFormatException) {
                binding.etAge.setText("")
                e.printStackTrace()
            }

            try {
                it.userScore?.let { score ->
                    binding.etUserScore.setText(score.toString())
                }
            } catch (e: NumberFormatException) {
                binding.etUserScore.setText("")
                e.printStackTrace()
            }

            try {
                when(it.gender) {
                    ShopLiveUserGender.Male -> binding.rgGender.check(R.id.rbMale)
                    ShopLiveUserGender.Female -> binding.rgGender.check(R.id.rbFemale)
                    else -> binding.rgGender.check(R.id.rbNone)
                }
            } catch (e: Exception) {
                binding.rgGender.check(R.id.rbNone)
                e.printStackTrace()
            }
        }
    }

    private fun saveUser() {
        val inputManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(binding.etUserId.windowToken, 0)

        val user = ShopLiveUser().apply {
            userId = binding.etUserId.text.toString()
            userName = binding.etUserName.text.toString()

            try {
                age = binding.etAge.text.toString().toInt()
            } catch (e: NumberFormatException) {
                e.printStackTrace()
            }

            try {
                gender = when(binding.rgGender.checkedRadioButtonId) {
                    R.id.rbMale -> {
                        ShopLiveUserGender.Male
                    }
                    R.id.rbFemale -> {
                        ShopLiveUserGender.Female
                    }
                    else -> {
                        ShopLiveUserGender.Neutral
                    }
                }
            } catch (e: Exception) {
                gender = ShopLiveUserGender.Neutral
                e.printStackTrace()
            }

            try {
                userScore = binding.etUserScore.text.toString().toInt()
            } catch (e: NumberFormatException) {
                e.printStackTrace()
            }
        }

        CampaignSettings.user(requireContext(), user)
    }
}