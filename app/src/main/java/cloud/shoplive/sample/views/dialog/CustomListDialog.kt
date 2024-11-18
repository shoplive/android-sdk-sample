package cloud.shoplive.sample.views.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import cloud.shoplive.sample.R

class CustomListDialog<T : Any>(
    context: Context, private val list: List<T> = emptyList(), private val callback: (T) -> Unit
) : Dialog(context) {

    private val recyclerView: RecyclerView by lazy {
        findViewById(R.id.recyclerView)
    }

    private val adapter: Adapter<T> by lazy {
        Adapter(callback)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_custom_list)

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
        adapter.submitList(list)
    }

    fun setData(data: List<T>) {
        adapter.submitList(data)
    }
}

private class Adapter<T : Any>(private val callback: (T) -> Unit) :
    ListAdapter<T, Adapter<T>.ViewHolder>(object :
        DiffUtil.ItemCallback<T>() {
        override fun areContentsTheSame(
            oldItem: T,
            newItem: T
        ) = true

        override fun areItemsTheSame(
            oldItem: T,
            newItem: T
        ) = oldItem == newItem
    }) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.holder_custom_list, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val text: TextView = view.findViewById(R.id.text)

        fun onBind(item: T) {
            text.text = item.toString()

            itemView.setOnClickListener {
                callback.invoke(item)
            }
        }
    }
}
