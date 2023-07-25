package cloud.shoplive.sample.shortform

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import cloud.shoplive.sample.R
import cloud.shoplive.sdk.common.ShopLiveCommonError
import cloud.shoplive.sdk.shorts.ShopLiveShortform
import cloud.shoplive.sdk.shorts.ShopLiveShortformBaseTypeHandler
import cloud.shoplive.sdk.shorts.ShopLiveShortformHorizontalTypeView

class ShortformSampleAdapter(
    private val enablePlayLiveData: LiveData<Boolean?>,
    private val snapLiveData: LiveData<Boolean>,
    private val onlyWifiLiveData: LiveData<Boolean>
) :
    ListAdapter<ShortformSampleData, RecyclerView.ViewHolder>(object :
        DiffUtil.ItemCallback<ShortformSampleData>() {
        override fun areContentsTheSame(oldItem: ShortformSampleData, newItem: ShortformSampleData) =
            true

        override fun areItemsTheSame(oldItem: ShortformSampleData, newItem: ShortformSampleData) =
            oldItem == newItem
    }) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return HorizontalViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.holder_shortform_horizontal_type, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = currentList.getOrNull(position) ?: return
        if (holder is HorizontalViewHolder) {
            holder.shortsHorizontalTypeView.onBindViewHolder(item)
        }

        if (holder is Binder) {
            holder.onBind(item)
        }
    }

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        super.onViewAttachedToWindow(holder)
        if (holder is HorizontalViewHolder) {
            holder.shortsHorizontalTypeView.onViewAttachedToWindow()
        }

        if (holder is ViewDetector) {
            holder.onViewAttachedToWindow()
        }
    }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        super.onViewDetachedFromWindow(holder)
        if (holder is HorizontalViewHolder) {
            holder.shortsHorizontalTypeView.onViewDetachedFromWindow()
        }

        if (holder is ViewDetector) {
            holder.onViewDetachedFromWindow()
        }
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        super.onViewRecycled(holder)
        if (itemCount <= holder.layoutPosition || RecyclerView.NO_POSITION == holder.layoutPosition) {
            return
        }
        val item = currentList.getOrNull(holder.layoutPosition) ?: return
        if (holder is HorizontalViewHolder) {
            holder.shortsHorizontalTypeView.onViewRecycled(item)
        }
    }

    inner class HorizontalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        Binder, ViewDetector {
        private val titleView: TextView = itemView.findViewById(R.id.titleView)
        val shortsHorizontalTypeView: ShopLiveShortformHorizontalTypeView =
            itemView.findViewById(R.id.horitontalTypeView)

        private val enablePlayObserver = Observer<Boolean?> { isPlay ->
            isPlay ?: return@Observer
            if (isPlay) {
                shortsHorizontalTypeView.enablePlayVideos()
            } else {
                shortsHorizontalTypeView.disablePlayVideos()
            }
        }
        private val snapObserver = Observer<Boolean> { isChecked ->
            if (isChecked) {
                shortsHorizontalTypeView.enableSnap()
                shortsHorizontalTypeView.setPlayableType(ShopLiveShortform.PlayableType.FIRST)
            } else {
                shortsHorizontalTypeView.disableSnap()
                shortsHorizontalTypeView.setPlayableType(ShopLiveShortform.PlayableType.ALL)
            }
        }
        private val onlyWifiObserver = Observer<Boolean> { isChecked ->
            shortsHorizontalTypeView.setPlayOnlyWifi(isChecked)
        }

        init {
            shortsHorizontalTypeView.enableShuffle()
            shortsHorizontalTypeView.setVisibleViewCount(false)
            if (snapLiveData.value == true) {
                shortsHorizontalTypeView.enableSnap()
                shortsHorizontalTypeView.setPlayableType(ShopLiveShortform.PlayableType.FIRST)
            } else {
                shortsHorizontalTypeView.disableSnap()
                shortsHorizontalTypeView.setPlayableType(ShopLiveShortform.PlayableType.ALL)
            }

            shortsHorizontalTypeView.setViewType(ShopLiveShortform.CardViewType.CARD_TYPE1)
            shortsHorizontalTypeView.setPlayOnlyWifi(onlyWifiLiveData.value == true)
            shortsHorizontalTypeView.handler = object : ShopLiveShortformBaseTypeHandler() {
                override fun onError(error: ShopLiveCommonError) {
                    Toast.makeText(
                        itemView.context,
                        error.message ?: error.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        override fun onBind(data: ShortformSampleData) {
            shortsHorizontalTypeView.setHashTags(data.hashTags, data.tagSearchOperator)
            shortsHorizontalTypeView.setBrand(data.brand)
            shortsHorizontalTypeView.submit()
            titleView.text = data.getTitle()
        }

        override fun onViewAttachedToWindow() {
            enablePlayLiveData.observeForever(enablePlayObserver)
            snapLiveData.observeForever(snapObserver)
            onlyWifiLiveData.observeForever(onlyWifiObserver)
        }

        override fun onViewDetachedFromWindow() {
            enablePlayLiveData.removeObserver(enablePlayObserver)
            snapLiveData.removeObserver(snapObserver)
            onlyWifiLiveData.removeObserver(onlyWifiObserver)
        }
    }

    interface ViewDetector {
        fun onViewAttachedToWindow()
        fun onViewDetachedFromWindow()
    }

    interface Binder {
        fun onBind(data: ShortformSampleData)
    }
}