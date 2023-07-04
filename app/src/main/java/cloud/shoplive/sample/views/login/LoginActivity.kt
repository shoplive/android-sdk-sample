package cloud.shoplive.sample.views.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import cloud.shoplive.sample.R
import cloud.shoplive.sample.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    companion object {
        const val USER_ID = "userId"
        fun buildIntent(context: Context) : Intent {
            return Intent(context, LoginActivity::class.java)
        }
    }

    private val binding: ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        title = getString(R.string.title_login)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel.done.observe(this) { userId ->
            setResult(RESULT_OK, Intent().apply {
                putExtra(USER_ID, userId)
            })
            finish()
        }

        binding.etId.setText("shoplive")
        binding.etPw.setText("shoplive")

        binding.btLogin.setOnClickListener {
            viewModel.saveUser(this@LoginActivity, binding.etId.text.toString())
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