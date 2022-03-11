package cloud.shoplive.sample.user

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.widget.AppCompatTextView
import cloud.shoplive.sample.CampaignSettings
import cloud.shoplive.sample.R
import cloud.shoplive.sample.base.ToolbarActivity
import cloud.shoplive.sdk.ShopLiveUser
import cloud.shoplive.sdk.ShopLiveUserGender
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.lang.NumberFormatException


class UserActivity: ToolbarActivity() {

    lateinit var rgAuth: RadioGroup
    lateinit var tvAuth: AppCompatTextView
    lateinit var scrollView: ScrollView
    lateinit var etUserId: TextInputEditText
    lateinit var etUserName: TextInputEditText
    lateinit var etAge: TextInputEditText
    lateinit var etUserScore: TextInputEditText
    lateinit var rgGender: RadioGroup
    lateinit var tvTokenGuide: AppCompatTextView
    lateinit var etTokenLayout: TextInputLayout
    lateinit var etToken: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        rgAuth = findViewById(R.id.rgAuth)
        tvAuth = findViewById(R.id.tvAuth)
        scrollView = findViewById(R.id.scrollView)
        etUserId = findViewById(R.id.etUserId)
        etUserName = findViewById(R.id.etUserName)
        etAge = findViewById(R.id.etAge)
        etUserScore = findViewById(R.id.etUserScore)
        rgGender = findViewById(R.id.rgGender)
        tvTokenGuide = findViewById(R.id.tvTokenGuide)
        etTokenLayout = findViewById(R.id.etTokenLayout)
        etToken = findViewById(R.id.etToken)

        tvTokenGuide.paintFlags = Paint.UNDERLINE_TEXT_FLAG
        tvTokenGuide.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.label_token_guide_link)))
            startActivity(intent)
        }

        rgAuth.setOnCheckedChangeListener { _, checkedId ->
            when(checkedId) {
                R.id.rbUser -> {
                    tvAuth.text = getString(R.string.label_use_user)
                    scrollView.visibility = View.VISIBLE
                    tvTokenGuide.visibility = View.GONE
                    etTokenLayout.visibility = View.GONE
                }
                R.id.rbToken -> {
                    tvAuth.text = getString(R.string.label_use_token)
                    scrollView.visibility = View.VISIBLE
                    tvTokenGuide.visibility = View.VISIBLE
                    etTokenLayout.visibility = View.VISIBLE
                }
                R.id.rbGuest -> {
                    tvAuth.text = getString(R.string.label_use_guest)
                    scrollView.visibility = View.GONE
                }
            }
        }

        when(CampaignSettings.authType(this)) {
            0 -> rgAuth.check(R.id.rbUser)
            1 -> rgAuth.check(R.id.rbToken)
            2 -> rgAuth.check(R.id.rbGuest)
        }
        setUser(CampaignSettings.user(this))
        etToken.setText(CampaignSettings.jwt(this))
    }

    override fun layout(): Int {
        return R.layout.activity_user
    }

    override fun toolbarTitle(): String {
        return getString(R.string.title_user)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.user_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> {
                finish()
            }
            R.id.action_save -> {
                saveUser()
                CampaignSettings.jwt(this, etToken.text.toString())
                CampaignSettings.authType(
                    this, when (rgAuth.checkedRadioButtonId) {
                        R.id.rbUser -> 0
                        R.id.rbToken -> 1
                        else -> 2
                    }
                )
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setUser(user: ShopLiveUser?) {
        user?.let {
            etUserId.setText(it.userId)
            etUserName.setText(it.userName)

            try {
                etAge.setText(it.age.toString())
            } catch (e: NumberFormatException) {
                etAge.setText("")
                e.printStackTrace()
            }

            try {
                it.userScore?.let { score ->
                    etUserScore.setText(score.toString())
                }
            } catch (e: NumberFormatException) {
                etUserScore.setText("")
                e.printStackTrace()
            }

            try {
                when(it.gender) {
                    ShopLiveUserGender.Male -> {
                        rgGender.check(R.id.rbMale)
                    }
                    ShopLiveUserGender.Female -> {
                        rgGender.check(R.id.rbFemale)
                    }
                    else -> {
                        rgGender.check(R.id.rbNone)
                    }
                }
            } catch (e: Exception) {
                rgGender.check(R.id.rbNone)
                e.printStackTrace()
            }
        }
    }

    private fun saveUser() {
        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(etUserId.windowToken, 0)

        val user = ShopLiveUser().apply {
            userId = etUserId.text.toString()
            userName = etUserName.text.toString()

            try {
                age = etAge.text.toString().toInt()
            } catch (e: NumberFormatException) {
                e.printStackTrace()
            }

            try {
                gender = when(rgGender.checkedRadioButtonId) {
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
                userScore = etUserScore.text.toString().toInt()
            } catch (e: NumberFormatException) {
                e.printStackTrace()
            }
        }

        CampaignSettings.user(this, user)
    }
}