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
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import cloud.shoplive.sample.PreferencesUtilImpl
import cloud.shoplive.sample.R
import cloud.shoplive.sample.UserType
import cloud.shoplive.sample.data.SharedPreferenceStorage
import cloud.shoplive.sample.databinding.ActivityUserBinding
import cloud.shoplive.sdk.ShopLiveUser
import cloud.shoplive.sdk.ShopLiveUserGender

class UserActivity : AppCompatActivity() {
    companion object {
        fun buildIntent(context: Context): Intent {
            return Intent(context, UserActivity::class.java)
        }
    }

    private val binding: ActivityUserBinding by lazy {
        ActivityUserBinding.inflate(layoutInflater)
    }

    private val viewModel: UserViewModel by viewModels {
        viewModelFactory {
            initializer {
                UserViewModel(PreferencesUtilImpl(SharedPreferenceStorage(this@UserActivity)))
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        title = getString(R.string.title_user)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.tvTokenGuide.paintFlags = Paint.UNDERLINE_TEXT_FLAG

        observeData()
        setClickListener()

//        viewModel.loadJwt()
//        viewModel.loadType()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.user_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
            R.id.action_save -> saveData()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun observeData() {
        viewModel.user.observe(this) {
            it ?: return@observe
            setUserData(it)
        }

        viewModel.jwt.observe(this) {
            binding.etToken.setText(it)
        }

        viewModel.authType.observe(this) { authType ->
            when (authType) {
                0 -> binding.rgAuth.check(R.id.rbUser)
                1 -> binding.rgAuth.check(R.id.rbToken)
                2 -> binding.rgAuth.check(R.id.rbGuest)
            }
        }
    }

    private fun setUserData(shopLiveUser: ShopLiveUser) {
        binding.etUserId.setText(shopLiveUser.userId)
        binding.etUserName.setText(shopLiveUser.userName)
        binding.etAge.setText(shopLiveUser.age.toString())
        binding.etUserScore.setText(shopLiveUser.userScore.toString())

        kotlin.runCatching {
            shopLiveUser.gender
        }.getOrNull().run {
            when (this) {
                ShopLiveUserGender.Male -> binding.rgGender.check(R.id.rbMale)
                ShopLiveUserGender.Female -> binding.rgGender.check(R.id.rbFemale)
                else -> binding.rgGender.check(R.id.rbNone)
            }
        }
    }

    private fun saveData() {
        (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).run {
            hideSoftInputFromWindow(binding.etUserId.windowToken, 0)
        }
        ShopLiveUser().apply {
            userId = binding.etUserId.text.toString()
            userName = binding.etUserName.text.toString()
            age = binding.etAge.text.toString().toIntOrNull()
            runCatching {
                when (binding.rgGender.checkedRadioButtonId) {
                    R.id.rbMale -> ShopLiveUserGender.Male
                    R.id.rbFemale -> ShopLiveUserGender.Female
                    else -> ShopLiveUserGender.Neutral
                }
            }.getOrNull().run {
                gender = this
            }
            userScore = binding.etUserScore.text.toString().toIntOrNull()
        }.run {
            viewModel.saveUserData(this)
        }
        viewModel.saveJwt(binding.etToken.text.toString())
        viewModel.saveAuthType(
            when (binding.rgAuth.checkedRadioButtonId) {
                R.id.rbUser -> UserType.USER
                R.id.rbToken -> UserType.JWT
                else -> UserType.GUEST
            }
        )
        finish()
    }

    private fun setClickListener() {
        binding.tvTokenGuide.setOnClickListener {
            val intent =
                Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.label_token_guide_link)))
            startActivity(intent)
        }

        binding.rgAuth.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rbUser -> setUserUiMode()
                R.id.rbToken -> setTokenUiMode()
                R.id.rbGuest -> setGuestUiMode()
            }
        }
    }

    private fun setUserUiMode() {
        binding.tvAuth.text = getString(R.string.label_use_user)
        binding.userLayout.visibility = View.VISIBLE
        binding.tvTokenGuide.visibility = View.GONE
        binding.etTokenLayout.visibility = View.GONE
    }

    private fun setTokenUiMode() {
        binding.tvAuth.text = getString(R.string.label_use_token)
        binding.userLayout.visibility = View.VISIBLE
        binding.tvTokenGuide.visibility = View.VISIBLE
        binding.etTokenLayout.visibility = View.VISIBLE
    }

    private fun setGuestUiMode() {
        binding.tvAuth.text = getString(R.string.label_use_guest)
        binding.userLayout.visibility = View.GONE
    }
}