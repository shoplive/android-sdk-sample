package cloud.shoplive.sample.shortform

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ShortformTabAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    private val _currentList = mutableListOf<Fragment>()
    val currentList: List<Fragment>
        get() = _currentList

    fun submitList(list: List<Fragment>) {
        _currentList.clear()
        _currentList.addAll(list)
    }

    override fun getItemCount(): Int {
        return _currentList.size
    }

    override fun createFragment(position: Int): Fragment {
        return _currentList[position]
    }
}