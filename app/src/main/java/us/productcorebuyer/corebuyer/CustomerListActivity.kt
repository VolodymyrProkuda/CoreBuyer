package us.productcorebuyer.corebuyer

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView

class CustomerListActivity : AppCompatActivity() {

    private val itemLocalDBs = mutableListOf<ItemLocalDB>()
    private lateinit var itemLocalDBone : ItemLocalDB

    private lateinit var recyclerCustomerView : RecyclerView
    private var startSizeOfLocalDB = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_list)

        loadFromLocalDB()

        recyclerCustomerView = findViewById<RecyclerView>(R.id.recyclerViewCustomerList)
        recyclerCustomerView.adapter = ItemCustumerAdapter(this, itemLocalDBs)

        recyclerCustomerView.setHasFixedSize(true)
        startSizeOfLocalDB = itemLocalDBs.size

    }

    override fun onDestroy() {
        super.onDestroy()
        Toast.makeText(this,"Select success!" +itemLocalDBs.size.toString(),Toast.LENGTH_SHORT).show()
        if (startSizeOfLocalDB!=itemLocalDBs.size) saveToLocalDBWithDelete()
    }

    fun saveToLocalDBWithDelete(){
        var st = ""
        val sPrefSave = getSharedPreferences("MainActivity",MODE_PRIVATE)
        val ed: SharedPreferences.Editor = sPrefSave.edit()
        ed.putString("numOfItemLocalDB", itemLocalDBs.size.toString())//"5"

        for (i in 0..itemLocalDBs.size-1) {
            st =  (itemLocalDBs[i].companyName)+"\n"+ (itemLocalDBs[i].tell)+"\n"
            st +=   (itemLocalDBs[i].einNumber)+ "\n"
            st +=   (itemLocalDBs[i].sellerName)+ "\n"
            st +=   (itemLocalDBs[i].email)+ "\n"
            st +=   (itemLocalDBs[i].address)+ "\n"
            st +=   (itemLocalDBs[i].buyerName)+ "\n"
            st +=   (itemLocalDBs[i].idLicensePhotoString)+ "\n"
            st +=   (itemLocalDBs[i].companyNamePhotoString)+ "\n"
            st +=   (itemLocalDBs[i].sellerNamePhotoString)
            ed.putString((i+1).toString(), st)
        }

        ed.apply()

    }


    fun loadFromLocalDB(){
      //  var searchType = "Comp"
        var st = ""
        var indexResultSearch = 0
        var lines: MutableList<String> = mutableListOf()
      /*
        var compArr: MutableList<String> = mutableListOf()
        var custArr: MutableList<String> = mutableListOf()
        var sellArr: MutableList<String> = mutableListOf()
*/
        val sPrefLoad = getSharedPreferences("MainActivity",MODE_PRIVATE)
        val savedText: String = sPrefLoad.getString("numOfItemLocalDB", "0").toString()
        val localItemsInDB = savedText.toInt()
        if (localItemsInDB==0) Toast.makeText(this@CustomerListActivity, "DB is empty", Toast.LENGTH_SHORT).show() else
        {
            for (i in 1..localItemsInDB) {
                st = sPrefLoad.getString(i.toString(), "").toString()
                lines = st.lines().toMutableList()
                itemLocalDBone = ItemLocalDB(lines[0],lines[1],lines[2],lines[3],lines[4],lines[5],lines[6],lines[7],lines[8],lines[9])
                itemLocalDBs.add(itemLocalDBone)
            }
            /*
            searchType = when (radioSelectSearch.checkedRadioButtonId) {
                R.id.radioButton_Comp -> "Comp"
                R.id.radioButton_Cust -> "Cust"
                else -> "Sell"
            }

            if (searchType == "Comp" ) {
                for (ii in itemLocalDBs) compArr.add(ii.companyName)
                indexResultSearch = compArr.lastIndexOf(data_company_name.editText?.text.toString())+1
                compArr.clear()
            }
            if (searchType == "Cust" ) {
                for (ii in itemLocalDBs) custArr.add(ii.buyerName)
                indexResultSearch = custArr.lastIndexOf(data_buyer_name.editText?.text.toString())+1
                custArr.clear()
            }
            if (searchType == "Sell" ) {
                for (ii in itemLocalDBs) sellArr.add(ii.sellerName)
                indexResultSearch = sellArr.lastIndexOf(data_seller_name.editText?.text.toString())+1
                sellArr.clear()
            }
            */
          /*
            if (indexResultSearch==0 ) Toast.makeText(this@MainActivity, "Wrong request!", Toast.LENGTH_SHORT).show() else{

                itemLocalDBone = itemLocalDBs[indexResultSearch-1]

                data_company_name.editText?.setText(itemLocalDBone.companyName)
                data_tell.editText?.setText(itemLocalDBone.tell)
                data_ein_number.editText?.setText(itemLocalDBone.einNumber)
                data_email.editText?.setText(itemLocalDBone.email)
                data_adress.editText?.setText(itemLocalDBone.address)
                data_buyer_name.editText?.setText(itemLocalDBone.buyerName)
                data_seller_name.editText?.setText(itemLocalDBone.sellerName)
                idLicensePhotoPath = itemLocalDBone.idLicensePhotoString
                companyNamePhotoPath = itemLocalDBone.companyNamePhotoString
                sellerNamePhotoPath = itemLocalDBone.sellerNamePhotoString
                if (idLicensePhotoPath!="empty") imageButtonLicencePhoto.setImageURI(idLicensePhotoPath.toUri())
                if (companyNamePhotoPath!="empty") imageButtonCompany.setImageURI(companyNamePhotoPath.toUri())
                if (sellerNamePhotoPath!="empty") imageButtonSeller.setImageURI(sellerNamePhotoPath.toUri())

                indexResultSearch = 0
            }
*/
        }



        //  val itemLocalDBs = mutableListOf<ItemLocalDB>()
        //  lateinit var itemLocalDBone : ItemLocalDB

    }



}