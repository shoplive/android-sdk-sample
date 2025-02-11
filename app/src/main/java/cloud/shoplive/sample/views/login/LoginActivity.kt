package cloud.shoplive.sample.views.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import cloud.shoplive.sample.PreferencesUtilImpl
import cloud.shoplive.sample.R
import cloud.shoplive.sample.data.SharedPreferenceStorage

class LoginActivity : AppCompatActivity() {

    companion object {
        const val USER_ID = "userId"
        fun buildIntent(context: Context): Intent {
            return Intent(context, LoginActivity::class.java)
        }
    }

    private val viewModel: LoginViewModel by viewModels {
        viewModelFactory {
            initializer {
                LoginViewModel(PreferencesUtilImpl(SharedPreferenceStorage(this@LoginActivity)))
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            LoginScreen(
                viewModel = viewModel
            )
        }

        title = getString(R.string.title_login)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel.done.observe(this) { userId ->
            setResult(RESULT_OK, Intent().apply {
                putExtra(USER_ID, userId)
            })
            finish()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}