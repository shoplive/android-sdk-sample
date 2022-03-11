package cloud.shoplive.sample.base

import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import cloud.shoplive.sample.R

open class ToolbarActivity: AppCompatActivity() {

    lateinit var contentLayout: FrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_toolbar)

        contentLayout = findViewById(R.id.contentLayout)

        val contentView = layoutInflater.inflate(layout(), null, false)
        contentLayout.addView(checkNotNull(contentView))

        setSupportActionBar(findViewById(R.id.toolbar))
        title = toolbarTitle()
    }

    open fun layout(): Int {
        return 0
    }

    open fun toolbarTitle(): String {
        return ""
    }
}