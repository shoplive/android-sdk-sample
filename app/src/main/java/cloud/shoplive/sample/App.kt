package cloud.shoplive.sample

import android.app.Application
import cloud.shoplive.sdk.*

class App: Application() {

    override fun onCreate() {
        super.onCreate()

        ShopLive.init(this)
        ShopLive.setAccessKey(getString(R.string.accessKey))
    }
}