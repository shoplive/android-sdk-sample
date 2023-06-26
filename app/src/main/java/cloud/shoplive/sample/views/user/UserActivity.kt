package cloud.shoplive.sample.views.user

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import cloud.shoplive.sample.CampaignSettings
import cloud.shoplive.sample.R
import cloud.shoplive.sample.databinding.ActivityUserBinding
import cloud.shoplive.sdk.ShopLiveUser
import cloud.shoplive.sdk.ShopLiveUserGender
import kotlinx.coroutines.launch

class UserActivity : AppCompatActivity() {

    companion object {
        fun buildIntent(context: Context): Intent {
            return Intent(context, UserActivity::class.java)
        }
    }

    private val binding: ActivityUserBinding by lazy {
        ActivityUserBinding.inflate(layoutInflater)
    }

    private val viewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        title = "User"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel.user.observe(this) {
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

        viewModel.jwt.observe(this) {
            binding.etToken.setText(it)
        }

        binding.tvTokenGuide.paintFlags = Paint.UNDERLINE_TEXT_FLAG
        binding.tvTokenGuide.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.label_token_guide_link)))
            startActivity(intent)
        }

        binding.rgAuth.setOnCheckedChangeListener { _, checkedId ->
            when(checkedId) {
                R.id.rbUser -> {
                    binding.tvAuth.text = getString(R.string.label_use_user)
                    binding.userLayout.visibility = View.VISIBLE
                    binding.tvTokenGuide.visibility = View.GONE
                    binding.etTokenLayout.visibility = View.GONE
                }
                R.id.rbToken -> {
                    binding.tvAuth.text = getString(R.string.label_use_token)
                    binding.userLayout.visibility = View.VISIBLE
                    binding.tvTokenGuide.visibility = View.VISIBLE
                    binding.etTokenLayout.visibility = View.VISIBLE
                }
                R.id.rbGuest -> {
                    binding.tvAuth.text = getString(R.string.label_use_guest)
                    binding.userLayout.visibility = View.GONE
                }
            }
        }

        when(CampaignSettings.authType(this)) {
            0 -> binding.rgAuth.check(R.id.rbUser)
            1 -> binding.rgAuth.check(R.id.rbToken)
            2 -> binding.rgAuth.check(R.id.rbGuest)
        }

        viewModel.loadUserData(this)
        viewModel.loadJwt(this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.user_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
            R.id.action_save -> {
                lifecycleScope.launch {
                    (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).run {
                        hideSoftInputFromWindow(binding.etUserId.windowToken, 0)
                    }

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
                    viewModel.saveUserData(this@UserActivity, user)

                    viewModel.saveJwt(this@UserActivity, binding.etToken.text.toString())

                    viewModel.saveAuthType(this@UserActivity, when(binding.rgAuth.checkedRadioButtonId) {
                        R.id.rbUser -> CampaignSettings.UserType.USER
                        R.id.rbToken -> CampaignSettings.UserType.JWT
                        else -> CampaignSettings.UserType.GUEST
                    })
                    finish()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }
}