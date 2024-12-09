package cloud.shoplive.sample.shortform

import android.content.Context
import android.graphics.Rect
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
import cloud.shoplive.sample.extension.toDp
import cloud.shoplive.sdk.common.ShopLiveCommonError
import cloud.shoplive.sdk.network.request.ShopLiveShortformTagSearchOperator
import cloud.shoplive.sdk.shorts.ShopLiveShortform
import cloud.shoplive.sdk.shorts.ShopLiveShortformHandler
import cloud.shoplive.sdk.shorts.ShopLiveShortformHorizontalTypeView

class ShortformSampleAdapter(
    private val viewModel: ShortformViewModel,
    private val enablePlayLiveData: LiveData<Boolean?>
) :
    ListAdapter<ShortformSampleData, RecyclerView.ViewHolder>(object :
        DiffUtil.ItemCallback<ShortformSampleData>() {
        override fun areContentsTheSame(
            oldItem: ShortformSampleData,
            newItem: ShortformSampleData
        ) = true

        override fun areItemsTheSame(oldItem: ShortformSampleData, newItem: ShortformSampleData) =
            false
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

        init {
            shortsHorizontalTypeView.setPlayableType(ShopLiveShortform.PlayableType.FIRST)
            shortsHorizontalTypeView.setViewType(viewModel.getSavedCardType())
            shortsHorizontalTypeView.handler = ShortformSampleData.handler
        }

        override fun onBind(data: ShortformSampleData) {
            titleView.text = data.title
            shortsHorizontalTypeView.submit(data.collectionData, data.response)
            shortsHorizontalTypeView.scrollToTop(false)
        }

        private val enablePlayObserver = Observer<Boolean?> { isPlay ->
            isPlay ?: return@Observer
            if (isPlay) {
                shortsHorizontalTypeView.enablePlayVideos()
            } else {
                shortsHorizontalTypeView.disablePlayVideos()
            }
        }

        private val playableTypeObserver = Observer<ShopLiveShortform.PlayableType> {
            shortsHorizontalTypeView.setPlayableType(it)
        }

        private val hashTagObserver =
            Observer<Pair<List<String>?, ShopLiveShortformTagSearchOperator>> { (hashTags, operator) ->
                shortsHorizontalTypeView.setHashTags(hashTags, operator)
            }

        private val brandObserver = Observer<List<String>?> {
            shortsHorizontalTypeView.setBrands(it)
        }

        private val isVisibleTitleObserver = Observer<Boolean> {
            shortsHorizontalTypeView.setVisibleTitle(it)
        }

        private val isVisibleBrandObserver = Observer<Boolean> {
            shortsHorizontalTypeView.setVisibleBrand(it)
        }

        private val isVisibleProductCountObserver = Observer<Boolean> {
            shortsHorizontalTypeView.setVisibleProductCount(it)
        }

        private val isVisibleViewCountObserver = Observer<Boolean> {
            shortsHorizontalTypeView.setVisibleViewCount(it)
        }

        private val isEnableShuffleObserver = Observer<Boolean> {
            if (it) {
                shortsHorizontalTypeView.enableShuffle()
            } else {
                shortsHorizontalTypeView.disableShuffle()
            }
        }

        private val isEnableSnapObserver = Observer<Boolean> {
            if (it) {
                shortsHorizontalTypeView.enableSnap()
            } else {
                shortsHorizontalTypeView.disableSnap()
            }
        }

        private val isEnablePlayVideosObserver = Observer<Boolean> {
            if (it) {
                shortsHorizontalTypeView.enablePlayVideos()
            } else {
                shortsHorizontalTypeView.disablePlayVideos()
            }
        }

        private val isEnablePlayWifiObserver = Observer<Boolean> {
            shortsHorizontalTypeView.setPlayOnlyWifi(it)
        }

        private val radiusObserver = Observer<Int?> {
            shortsHorizontalTypeView.setRadius(it?.toDp(itemView.context) ?: return@Observer)
        }

        override fun onViewAttachedToWindow() {
            enablePlayLiveData.observeForever(enablePlayObserver)
            viewModel.playableTypeLiveData.observeForever(playableTypeObserver)
            viewModel.hashTagLiveData.observeForever(hashTagObserver)
            viewModel.brandLiveData.observeForever(brandObserver)
            viewModel.isVisibleTitleLiveData.observeForever(isVisibleTitleObserver)
            viewModel.isVisibleBrandLiveData.observeForever(isVisibleBrandObserver)
            viewModel.isVisibleProductCountLiveData.observeForever(isVisibleProductCountObserver)
            viewModel.isVisibleViewCountLiveData.observeForever(isVisibleViewCountObserver)
            viewModel.isEnableShuffleLiveData.observeForever(isEnableShuffleObserver)
            viewModel.isEnableSnapLiveData.observeForever(isEnableSnapObserver)
            viewModel.isEnablePlayVideosLiveData.observeForever(isEnablePlayVideosObserver)
            viewModel.isEnablePlayWifiLiveData.observeForever(isEnablePlayWifiObserver)
            viewModel.radiusLiveData.observeForever(radiusObserver)
        }

        override fun onViewDetachedFromWindow() {
            enablePlayLiveData.removeObserver(enablePlayObserver)
            viewModel.playableTypeLiveData.removeObserver(playableTypeObserver)
            viewModel.hashTagLiveData.removeObserver(hashTagObserver)
            viewModel.brandLiveData.removeObserver(brandObserver)
            viewModel.isVisibleTitleLiveData.removeObserver(isVisibleTitleObserver)
            viewModel.isVisibleBrandLiveData.removeObserver(isVisibleBrandObserver)
            viewModel.isVisibleProductCountLiveData.removeObserver(isVisibleProductCountObserver)
            viewModel.isVisibleViewCountLiveData.removeObserver(isVisibleViewCountObserver)
            viewModel.isEnableShuffleLiveData.removeObserver(isEnableShuffleObserver)
            viewModel.isEnableSnapLiveData.removeObserver(isEnableSnapObserver)
            viewModel.isEnablePlayVideosLiveData.removeObserver(isEnablePlayVideosObserver)
            viewModel.isEnablePlayWifiLiveData.removeObserver(isEnablePlayWifiObserver)
            viewModel.radiusLiveData.removeObserver(radiusObserver)
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