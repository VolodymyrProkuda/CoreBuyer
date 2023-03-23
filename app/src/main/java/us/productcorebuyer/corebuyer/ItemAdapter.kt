package us.productcorebuyer.corebuyer

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ItemAdapter(
    private val context: Context,
    var dataset: MutableList<ElementPhotoPrice>
) : RecyclerView.Adapter<ItemViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        // create a new view
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item, parent, false)

        return ItemViewHolder(adapterLayout).linkAdapter(this)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = dataset[position]

        holder.textView1.text = item.quantity.toString()
        holder.imageView.setImageURI(item.imagePhotoUri)
        holder.textView2.text = item.textPrice
 /*       holder.imageViewDelete.setOnClickListener {

        }*/
    }

    override fun getItemCount() = dataset.size


}

class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val textView1: TextView = view.findViewById(R.id.item_title1)
    val imageView: ImageView = view.findViewById(R.id.item_image)
    val textView2: TextView = view.findViewById(R.id.item_title2)
    val imageViewDelete: ImageView = view.findViewById(R.id.imageViewDelete)
    private lateinit var adapter : ItemAdapter

    init {
        imageViewDelete.setOnClickListener {
            adapter.dataset.removeAt(adapterPosition)
            adapter.notifyItemRemoved(adapterPosition)
        }
    }
    fun linkAdapter(adapter : ItemAdapter): ItemViewHolder {
        this.adapter = adapter
        return this
    }
}

