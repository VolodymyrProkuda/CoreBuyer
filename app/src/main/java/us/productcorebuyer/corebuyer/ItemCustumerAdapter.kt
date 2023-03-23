package us.productcorebuyer.corebuyer

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView

class ItemCustumerAdapter(private val context: Context,
                          var dataset: MutableList<ItemLocalDB>
) : RecyclerView.Adapter<ItemCustomerViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemCustomerViewHolder {
        // create a new view
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.customer_list_item, parent, false)

        return ItemCustomerViewHolder(adapterLayout).linkAdapter(this)
    }

    override fun onBindViewHolder(holder: ItemCustomerViewHolder, position: Int) {
        val item = dataset[position]

        holder.textCustumer1.text = " Customer Name: "+ item.buyerName
        holder.textCompany1.text = " Company Name: "+item.companyName
        holder.textAdress1.text = " Address: "+item.address
        holder.textEmail1.text = " Email: "+item.email
        holder.textSeller1.text = " Seller Name: "+item.sellerName
        holder.textTell1.text = " Phone: "+item.tell
        holder.textEin1.text = " EIN NUMBER: "+item.einNumber
    }

    override fun getItemCount() = dataset.size

}

class ItemCustomerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val textCustumer1: TextView = view.findViewById(R.id.item_customer_list)
    val textCompany1: TextView = view.findViewById(R.id.item_company_list)
    val textAdress1: TextView = view.findViewById(R.id.item_adress_list)
    val textEmail1: TextView = view.findViewById(R.id.item_email_list)
    val textSeller1: TextView = view.findViewById(R.id.item_seller_list)
    val textTell1: TextView = view.findViewById(R.id.item_tell_list)
    val textEin1: TextView = view.findViewById(R.id.item_ein_list)

    val imageViewCustomerCheck : ImageView = view.findViewById(R.id.imageViewCustomerCheck)
    val imageViewCustomerDelete: ImageView = view.findViewById(R.id.imageViewCustomerDelete)
    private lateinit var adapter : ItemCustumerAdapter

    init {

        imageViewCustomerCheck.setOnClickListener {

            val intentToMain = Intent(view.context, MainActivity::class.java)

            intentToMain.putExtra( "data_company_name", adapter.dataset[adapterPosition].companyName)
            intentToMain.putExtra("data_tell", adapter.dataset[adapterPosition].tell)

            intentToMain.putExtra("data_ein_number", adapter.dataset[adapterPosition].einNumber)
            intentToMain.putExtra("data_seller_namer",adapter.dataset[adapterPosition].sellerName)
            intentToMain.putExtra("data_email", adapter.dataset[adapterPosition].email)
            intentToMain.putExtra("data_adress", adapter.dataset[adapterPosition].address)
            intentToMain.putExtra("data_buyer_name",adapter.dataset[adapterPosition].buyerName)

            intentToMain.putExtra("id_license_photo", adapter.dataset[adapterPosition].idLicensePhotoString)
            intentToMain.putExtra("seller_namer_photo", adapter.dataset[adapterPosition].companyNamePhotoString)
            intentToMain.putExtra("company_name_photo", adapter.dataset[adapterPosition].companyNamePhotoString)

            intentToMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            view.context.startActivity(intentToMain)
        }

        imageViewCustomerDelete.setOnClickListener {
            adapter.dataset.removeAt(adapterPosition)
            adapter.notifyItemRemoved(adapterPosition)

        }


       

    }
    fun linkAdapter(adapter : ItemCustumerAdapter): ItemCustomerViewHolder {
        this.adapter = adapter
        return this
    }
    fun localIntentToMain(){

    }


}
