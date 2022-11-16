package cloud.shoplive.sample

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI

class MainActivity : AppCompatActivity() {

    companion object {
        private const val ACCESS_KEY = "accessKey"
        private const val CAMPAIGN_KEY = "campaignKey"

        fun buildIntentFromDeeplink(
            context: Context,
            accessKey: String?,
            campaignKey: String?
        ): Intent {
            val intent = Intent(context, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            intent.putExtra(ACCESS_KEY, accessKey)
            intent.putExtra(CAMPAIGN_KEY, campaignKey)
            return intent
        }
    }

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // action bar
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = getString(R.string.app_name)

        // navigation controller
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        appBarConfiguration = AppBarConfiguration(navController.graph)
        NavigationUI.setupActionBarWithNavController(this, navController)

        val accessKey = intent.getStringExtra(ACCESS_KEY)
        val campaignKey = intent.getStringExtra(CAMPAIGN_KEY)
        if (accessKey?.isNotEmpty() == true && campaignKey?.isNotEmpty() == true) {
            viewModel.playFromDeeplink(accessKey, campaignKey)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, appBarConfiguration)
    }
}