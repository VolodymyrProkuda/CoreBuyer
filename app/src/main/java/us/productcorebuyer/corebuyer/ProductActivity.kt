package us.productcorebuyer.corebuyer

import android.Manifest
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.*
import android.graphics.pdf.PdfDocument
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.ViewTreeObserver
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputLayout
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.internal.platform.runBlocking
import io.realm.kotlin.mongodb.App
import io.realm.kotlin.mongodb.Credentials
import io.realm.kotlin.mongodb.sync.SyncConfiguration
import io.realm.kotlin.types.RealmList
import java.io.*
import java.text.SimpleDateFormat
import java.util.*


class ProductActivity : AppCompatActivity() {
/*
    private val DISCLAIMER1 = "Ev Logistics Inc"
    private val DISCLAIMER2 = "1230 Ave X, Apt 2B"
    private val DISCLAIMER3 = "Brooklyn, NY, 11235"
    private val DISCLAIMER4 = "Buyer: Evgeniy"
    private val DISCLAIMER5 = "6466335420"
    private val DISCLAIMER6 = "evgeniy39mm@gmail.com"
  */
/*
    private val DISCLAIMER1 = "Terrikon inc"
    private val DISCLAIMER2 = "Buyer: Anton"
    private val DISCLAIMER3 = "150 Langham st,"
    private val DISCLAIMER4 = "Brooklyn, NY, 11235"
    private val DISCLAIMER5 = "201 561 - 1410"
    private val DISCLAIMER6 = "Terrikon.inc@gmail.com"
    */


    private val DISCLAIMER1 = "ARC Recycling, LLC"
    private val DISCLAIMER2 = "1130-1132 N Godfrey Street"
    private val DISCLAIMER3 = "ALLENTOWN, PA, 18109"
    private val DISCLAIMER4 = "(201)-647-6376"
    private val DISCLAIMER5 = "Buyer: Roman"
    private val DISCLAIMER6 = "Arcrecycling2014@gmail.com"






    private val DISCLOSURE1 = "This is a Bill of Sale scrap metal. I state that I am the legal"
    private val DISCLOSURE2 = "owner of described scrap metal and that I have the authority to"
    private val DISCLOSURE3 = "sell it to the buyer I hereby acknowledge that I have received"
    private val DISCLOSURE4 = "the sum stated above as payment is full."

    private lateinit var paint: DrawView

    private var locationManager : LocationManager? = null
    private var locationString: String = ""

    lateinit var currentPhotoPath: String
    private val elementsPhotoPrice = mutableListOf<ElementPhotoPrice>()

    private val CAMERA_PERMISSION_CODE = 1000
    private val IMAGE_CAPTURE_CODE = 1001
    private var imageUri: Uri? = null
    private var lastUri: Uri? = null

    private var imageUriIdLicence: Uri? = null
    private var imageUriCompnyN: Uri? = null
    private var imageUriSellerN: Uri? = null
    private var imageUriSignatueS: Uri? = null
    private var pdfFileUri: Uri? = null
    private lateinit var bitmapSignature : Bitmap

    //   private var imageView: ImageView? = null
    private var productType = "(Oem)"

    private var textInNotes = ""
    private lateinit var editTextNotes : TextInputLayout
    private lateinit var imageButtonNotes: ImageButton
    private lateinit var imageButtonEOM: ImageButton
    private lateinit var imageButtonAddElement:ImageButton
    private lateinit var oneElementPhotoPrice : ElementPhotoPrice
    private lateinit var oemPrice : TextInputLayout
    private lateinit var oemOneTypeQuantity : TextInputLayout
    private lateinit var recyclerView : RecyclerView
    private lateinit var oemAftermGroup : RadioGroup
    private lateinit var radioGroupPayment : RadioGroup
    private lateinit var textViewQuantitySet : TextView
    private lateinit var textViewAvarageSet : TextView
    private lateinit var textViewTotalSet : TextView
    private lateinit var textViewTimeDate : TextView


    private  var data_idLicenseBitmapByteArray : ByteArray = byteArrayOf(0x2E)
    private  var data_companyNameBitmapByteArray : ByteArray = byteArrayOf(0x2E)
    private  var data_sellerNameBitmapByteArray : ByteArray = byteArrayOf(0x2E)
 //   private  var bb : ByteArray = byteArrayOf(0x2E)
    private  var data_idLicensePhoto = ""
    private  var data_companyNamePhoto = ""
    private  var data_sellerNamePhoto = ""
    private var buttonNum = 0
    private var photoNotesString = ""

    var paymentMethodGet = ""
    var data2_ein_number = ""
    var data2_seller_namer = ""
    var data2_email = ""
    var data2_tell = ""
    var data2_adress = ""
    var data2_company_name = ""
    var data2_buyer_name = ""
    var a1=0; var a2=0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)

        paint = findViewById(R.id.draw_view)
        val vto = paint.viewTreeObserver
        vto.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                paint.viewTreeObserver.removeOnGlobalLayoutListener(this)
                val width = paint.measuredWidth
                val height = paint.measuredHeight
                paint.init(height, width)
            }
        })

        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager?


         //define the listener
         val locationListener: LocationListener = object : LocationListener {
           override fun onLocationChanged(location: Location) {
             locationString = ("" + location.longitude + "E :" + location.latitude+ "N ")
           }
         }
        try {
            // Request location updates
            locationManager?.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0L, 0f, locationListener)
        } catch(ex: SecurityException) {
            Log.d("myTag", "Security Exception, no location available")
        }
        val formatter1 = SimpleDateFormat("d MMM yyyy, HH:mm:ss z")
        val timeDateNow1 = formatter1.format(Date())



         data2_ein_number = intent.getStringExtra("data_ein_number").toString()
         data2_seller_namer = intent.getStringExtra("data_seller_namer").toString()
         data2_email = intent.getStringExtra("data_email").toString()
         data2_tell = intent.getStringExtra("data_tell").toString()
         data2_adress = intent.getStringExtra("data_adress").toString()
         data2_company_name = intent.getStringExtra("data_company_name").toString()
         data2_buyer_name = intent.getStringExtra("data_buyer_name").toString()


         data_idLicensePhoto = intent.getStringExtra("id_license_photo")!!
         data_sellerNamePhoto = intent.getStringExtra("seller_namer_photo")!!
         data_companyNamePhoto = intent.getStringExtra("company_name_photo")!!
         imageUriIdLicence = intent.getStringExtra("imageUriIdLicence")!!.toUri()
         imageUriCompnyN = intent.getStringExtra("imageUriCompnyN")!!.toUri()
         imageUriSellerN = intent.getStringExtra("imageUriSellerN")!!.toUri()


        imageButtonNotes = findViewById(R.id.imageButtonNotes)
        editTextNotes = findViewById(R.id.edit_notes)

        imageButtonEOM = findViewById(R.id.imageButtonEOM)
        textViewTimeDate = findViewById(R.id.textViewTimeDate)

        textViewTimeDate.text = timeDateNow1 + " \n \n" + locationString
      //  imageButtonEOM.setImageURI(data_sellerNamePhoto.toUri())


        textViewQuantitySet = findViewById(R.id.textViewQuantitySet)
        textViewAvarageSet = findViewById(R.id.textViewAvarageSet)
        textViewTotalSet = findViewById(R.id.textViewTotalSet)

        oemAftermGroup = findViewById<RadioGroup>(R.id.oemAftermGroup)
        radioGroupPayment = findViewById<RadioGroup>(R.id.radioGroupPayment)

        recyclerView = findViewById<RecyclerView>(R.id.recyclerView)


        imageButtonAddElement = findViewById(R.id.imageButtonAddElement)
        oemPrice = findViewById(R.id.oem_price)
        oemOneTypeQuantity  = findViewById(R.id.oem_one_type_quant)
        imageButtonEOM.setOnClickListener {
            // Request permission
            val permissionGranted = requestCameraPermission()
            if (permissionGranted) {
                buttonNum = 1
                openCameraInterface()
            }
          //  Log.v("QUICKSTART1", "imageButtonEOM.setOnClickListener")
        }
        imageButtonNotes.setOnClickListener{
            val permissionGranted = requestCameraPermission()
            if (permissionGranted) {
                buttonNum = 2
                openCameraInterface()
            }

        }



        imageButtonAddElement.setOnClickListener {
                productType = when (oemAftermGroup.checkedRadioButtonId) {
                R.id.radioButtonOEM -> "(Oem)"
                R.id.radioButtonAftermarket -> "(Aftermarket)"
                else -> "(Aftermarket)"
                }

          //  Log.v("QUICKSTART1", "Start imageButtonAddElement.setOnClickListener")


            if ((oemPrice.editText?.text.toString()!="") && (oemOneTypeQuantity.editText?.text.toString()!="") && (imageUri != null)) {
                oneElementPhotoPrice.textPrice = " $"+oemPrice.editText?.text.toString() + " " + productType
                oneElementPhotoPrice.quantity = oemOneTypeQuantity.editText?.text.toString().toInt()
                oneElementPhotoPrice.intPrice = oemPrice.editText?.text.toString().toDouble()
            elementsPhotoPrice.add(oneElementPhotoPrice)

                Log.v("QUICKSTART1", "oneElementPhotoPrice   ${oneElementPhotoPrice.textPrice} ${oneElementPhotoPrice.quantity} ${oneElementPhotoPrice.intPrice}")

            recyclerView.adapter = ItemAdapter(this, elementsPhotoPrice)
            recyclerView.refreshDrawableState()}



             a1=0; a2=0.0
            if (elementsPhotoPrice.size>0) {
                for (i in 0..elementsPhotoPrice.size - 1) {
                    a1 += elementsPhotoPrice[i].quantity
                    a2 += (elementsPhotoPrice[i].intPrice).toDouble()
                }
            }
            textViewQuantitySet.text = "  " + a1.toString().replace(",",".")
            textViewAvarageSet.text = "  " +"%.2f".format(a2/a1.toDouble()).replace(",",".")
            textViewTotalSet.text = "  " + "%.2f".format(a2).replace(",",".")
            oemPrice.editText?.getText()?.clear()
            oemOneTypeQuantity.editText?.getText()?.clear()
            imageButtonEOM.setImageResource(R.drawable.ic_baseline_add_a_photo)
        }

        val textViewDisclosure = findViewById<TextView>(R.id.textViewDisclosure)
        textViewDisclosure.text = "This is a Bill of Sale scrap metal. I state that I am the legal owner of described scrap metal and that I have the authority to sell it to the buyer \n \n I hereby acknowledge that I have received the sum stated above as payment is full."


        // Initialize data


        recyclerView.adapter = ItemAdapter(this, elementsPhotoPrice)

        // Use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true)




    }

    //section menu

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.core_menu, menu)
        menu.findItem(R.id.id_save).setVisible(false)
        menu.findItem(R.id.id_forward).setVisible(false)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.id_save -> {

             //   synchronizeWithMongoDB()
                true
            }
            R.id.id_print -> {

                onPrint()


                true
            }
            R.id.id_email -> {

                sendEmail()

                true
            }
            R.id.id_createPDF -> {

                generatePDF()

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    //section camera

    private fun requestCameraPermission(): Boolean {
        var permissionGranted = false

        // If system os is Marshmallow or Above, we need to request runtime permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            val cameraPermissionNotGranted = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_DENIED
            if (cameraPermissionNotGranted){
                val permission = arrayOf(Manifest.permission.CAMERA)

                // Display permission dialog
                requestPermissions(permission, CAMERA_PERMISSION_CODE)
            }
            else{
                // Permission already granted
                permissionGranted = true
            }
        }
        else{
            // Android version earlier than M -> no need to request permission
            permissionGranted = true
        }

        return permissionGranted
    }
    // Handle Allow or Deny response from the permission dialog
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode === CAMERA_PERMISSION_CODE) {
            if (grantResults.size === 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                // Permission was granted
                openCameraInterface()
            }
            else{
                // Permission was denied
                showAlert("Camera permission was denied. Unable to take a picture.");
            }
        }
    }

    private fun openCameraInterface() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val photoFile: File? = try {
            createImageFileInAppDir()
        } catch (ex: IOException) {
            // Error occurred while creating the File
            null
        }
        photoFile?.also { file ->
            imageUri = FileProvider.getUriForFile(
                this,
                "us.productcorebuyer.corebuyer.fileprovider",
                file
            )

            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
        }

        // intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        // Launch intent
        startActivityForResult(intent, IMAGE_CAPTURE_CODE)
    }

    @Throws(IOException::class)
    private fun createImageFileInAppDir(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imagePath =  getExternalFilesDir(Environment.DIRECTORY_PICTURES)

        return   File.createTempFile( "JPEG_${timeStamp}_", ".jpg",imagePath
        ).apply { currentPhotoPath = absolutePath }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Callback from camera intent
        if (resultCode == Activity.RESULT_OK){
            // Set image captured to image view
            if (buttonNum==1) {
                imageButtonEOM.setImageURI(imageUri)
                oneElementPhotoPrice = ElementPhotoPrice(1, imageUri, "0$", 0.0, currentPhotoPath)
            }
            if (buttonNum==2){
                imageButtonNotes.setImageURI(imageUri)
                photoNotesString = currentPhotoPath
            }
        }
        else {
            // Failed to take picture
            showAlert("Failed to take camera picture")
        }
    }

    private fun showAlert(message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(message)
        //  builder.setPositiveButton(R.string.ok_button_title, null)

        val dialog = builder.create()
        dialog.show()
    }

    // section cloud DB


    fun synchronizeWithMongoDB(){
        val _itemOrderQuantity : RealmList<Int> = realmListOf(0)
        for (ite in elementsPhotoPrice)
            _itemOrderQuantity.add(ite.quantity)

        val _itemOrderPrice : RealmList<String> = realmListOf("")
        for (ite in elementsPhotoPrice)
            _itemOrderPrice.add(ite.textPrice)

        val _productArrayPhoto : RealmList<ByteArray> = realmListOf(byteArrayOf(0x2E))
        for (ite in elementsPhotoPrice)
            _productArrayPhoto.add(convertUriImToByteArr(ite.imagePhotoUriString))

        data_idLicenseBitmapByteArray = convertUriImToByteArr(data_idLicensePhoto)
        data_companyNameBitmapByteArray = convertUriImToByteArr(data_companyNamePhoto)
        data_sellerNameBitmapByteArray = convertUriImToByteArr(data_sellerNamePhoto)



        val formatter = SimpleDateFormat("d MMM yyyy, EEE, HH:mm:ss z")
        val timeDateNow = formatter.format(Date())

        paymentMethodGet = when (radioGroupPayment.checkedRadioButtonId) {
            R.id.radioCheck -> "Check"
            R.id.radioWire -> "Wire"
            else -> "Cash"
        }
//data-rpxli Part2  data-ygdij  TransHistory
        val app = App.create("data-rpxli")
        runBlocking {
            val user = app.login(Credentials.anonymous())
            val config = SyncConfiguration.Builder(user, "Part2", setOf(ItemDataOrder::class))
                // specify name so realm doesn't just use the "default.realm" file for this user
                .name("Part2")
             //   .waitForInitialRemoteData(timeout = 3.seconds)
                .build()
            val realm = Realm.open(config)
            realm.write {
                // create a  object in the realm
                val itemDataOrder = this.copyToRealm(ItemDataOrder().apply {
                     customerName = data2_buyer_name
                     companyName = data2_company_name
                     adress = data2_adress
                     tell = data2_tell
                     email = data2_email
                     sellerName = data2_seller_namer
                     einNumber = data2_ein_number
                     itemOrderQuantity = _itemOrderQuantity
                     itemOrderPrice = _itemOrderPrice
                     fullOrderQuantity  = a1
                     fullOrderPrice  = "%.2f".format(a2).replace(",",".")
                     currentReceiptDate = timeDateNow
                     paymentMethod = paymentMethodGet
                     idLicencePhoto = data_idLicenseBitmapByteArray
                     companyNamePhoto =  data_companyNameBitmapByteArray
                     sellerNamePhoto = data_sellerNameBitmapByteArray
                     productArrayPhoto = _productArrayPhoto



                })
            }
            realm.close()
        }

    }

    //Section image to bytearray


    fun convertUriImToByteArr(imUriString: String) : ByteArray{
/*

            var  bitmap: Bitmap? =null;
            try {
                val f =  File(imUriString);
                val options: BitmapFactory.Options = BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                bitmap = BitmapFactory.decodeStream(FileInputStream(f), null, options);
            } catch  (e: IOException) {

            }

*/


        val bitmap = BitmapFactory.decodeFile(imUriString)

        val width: Int = bitmap.getWidth()
        val height: Int = bitmap.getHeight()
        val halfWidth = 120
        val halfHeight = (height / width * halfWidth).toInt()

        val bitmap2 = Bitmap.createScaledBitmap(
            bitmap, halfWidth,
            halfHeight, false
        )

        return getByteArrayfromBitmap(bitmap2)
    }


/*
    fun convertImToByteArr( imview: ImageView) : ByteArray {
        val imageView : ImageView = imview
        imageView.setDrawingCacheEnabled(true)
        imageView.measure(
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        )
        imageView.layout(0, 0, imageView.getMeasuredWidth(), imageView.getMeasuredHeight()
        )
        imageView.buildDrawingCache(true)
        val bitmap: Bitmap = Bitmap.createBitmap(imageView.getDrawingCache())
        imageView.setDrawingCacheEnabled(false)



        val width: Int = bitmap.getWidth()
        val height: Int = bitmap.getHeight()
        val halfWidth = 240
        val halfHeight = (height / width * 240).toInt()

        val bitmap2 = Bitmap.createScaledBitmap(
            bitmap, halfWidth,
            halfHeight, false
        )

        return getByteArrayfromBitmap(bitmap2)!!


        //  val bitm = getBitmapfromByteArray(bb!!)
        //  imageButtonSeller.setImageBitmap(bitm)

    }

 */
    fun getByteArrayfromBitmap(bitmap: Bitmap): ByteArray {
        val bos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bos)
        return bos.toByteArray()
    }
 //  @RequiresApi(Build.VERSION_CODES.O)
    fun strToIm(imUriString: String) : ByteArray{

     var encoded1 : ByteArray = byteArrayOf(0x2E)
/*
     val file1 = File(imUriString.toUri())
     val fis = FileInputStream(file1)
     val bis = BufferedInputStream(fis)
     val buffer = ByteArray(file1.length().toInt())

     bis.read(buffer)

     fis.close()

*/

     //encoded = FileUtils.readFileToByteArray(file)


      /*  var encoded : ByteArray = byteArrayOf(0x2E)
        try {
             encoded = Files.readAllBytes(Paths.get(imUriString))

        } catch (e: IOException) {

        }*/

        return encoded1
    }



    //section printer
   fun onPrint(){

        val intentToPrint = Intent(this, PrintActivity::class.java)

        //  intentToProduct.putExtra("PhotoPict", currentPhotoPath)

        intentToPrint.putExtra("data_ein_number", data2_ein_number)
        intentToPrint.putExtra("data_seller_namer",data2_seller_namer)
        intentToPrint.putExtra("data_email", data2_email)
        intentToPrint.putExtra("data_tell", data2_tell)
        intentToPrint.putExtra("data_adress", data2_adress)
        intentToPrint.putExtra( "data_company_name", data2_company_name)
        intentToPrint.putExtra("data_buyer_name", data2_buyer_name)

        val __itemOrderQuantity : ArrayList<Int> = arrayListOf(0)
        val __itemOrderPrice : ArrayList<String> = arrayListOf("")
        for (itte in elementsPhotoPrice){
            __itemOrderQuantity.add(itte.quantity)
            __itemOrderPrice.add(itte.textPrice)
        }
        intentToPrint.putIntegerArrayListExtra("itemOrderQuantity",__itemOrderQuantity);
        intentToPrint.putStringArrayListExtra("itemOrderPrice",__itemOrderPrice);
        intentToPrint.putExtra("Total Quantity", a1.toString())
        intentToPrint.putExtra("Total Price", "%.2f".format(a2).replace(",","."))

        val formatter = SimpleDateFormat("d MMM yyyy, HH:mm:ss z")
        val timeDateNow = formatter.format(Date())

        intentToPrint.putExtra("Time And Date", timeDateNow)
        intentToPrint.putExtra("GeoLocation",locationString)

        textInNotes = editTextNotes.editText?.text.toString()
        var arStrList1: ArrayList<String> = arrayListOf()
        var mutStrList2: MutableList<String> = mutableListOf()
        mutStrList2 = textInNotes.lines().toMutableList()
        for (cc in mutStrList2) arStrList1.add(cc)

        paymentMethodGet = when (radioGroupPayment.checkedRadioButtonId) {
            R.id.radioCheck -> "Check"
            R.id.radioWire -> "Wire"
            else -> "Cash"
        }
        intentToPrint.putExtra("paymentMethodGet", paymentMethodGet)
        intentToPrint.putStringArrayListExtra("notesString",arStrList1)

        startActivity(intentToPrint)

   }

    //Email
    private fun sendEmail() {

        storeBitmap(paint.save()!!)


        var fS: String = ""

        fS =    " " + DISCLAIMER1 +
                "\n " + DISCLAIMER2 +
                "\n " + DISCLAIMER3 +
                " \n " + DISCLAIMER4 +
                " \n " + DISCLAIMER5 +
                " \n " + DISCLAIMER6 +
                " \n " +
                " \n Customer Name: " + data2_buyer_name +
                " \n Company Name: " + data2_company_name +
                " \n Address: " + data2_adress +
                " \n DPhone: " + data2_tell +
                " \n E-mail: " + data2_email +
                " \n Seller Name: " + data2_seller_namer +
                " \n EIN NUMBER: " + data2_ein_number +
        " \n "
        var q = ""; var p =0;
        for ( i in 0..(elementsPhotoPrice.size-1)) {
             q =  elementsPhotoPrice[i].textPrice
             p = elementsPhotoPrice[i].quantity
            fS += " \n $i) For $p units $q"
        }
        fS += "\n ___________________"
        fS += "\n Total Quantity: ${a1}"
        fS += "\n Total Price: ${"%.2f".format(a2).replace(",",".")}"
        paymentMethodGet = when (radioGroupPayment.checkedRadioButtonId) {
            R.id.radioCheck -> "Check"
            R.id.radioWire -> "Wire"
            else -> "Cash"
        }
        fS += "\n "+ "Payment method: ${paymentMethodGet}"
        fS += "\n "+ "Notes: "
        textInNotes = editTextNotes.editText?.text.toString()
        val mutStrList1 = textInNotes.lines().toMutableList()
        for ( i in 0..(mutStrList1.size-1)) {
            fS += "\n  ${mutStrList1[i]}"
        }
        fS += "\n " + DISCLOSURE1
        fS += "\n" + DISCLOSURE2
        fS += "\n " + DISCLOSURE3
        fS += "\n " + DISCLOSURE4

        fS += "\n Geolocation: $locationString"

   /*     val file1 = File(data_idLicensePhoto)
        val contentUri1 = FileProvider.getUriForFile(this@ProductActivity,
            "us.productcorebuyer.corebuyer.fileprovider",
            file1);

        val file2 = File(data_sellerNamePhoto)
        val contentUri2 = FileProvider.getUriForFile(this@ProductActivity,
            "us.productcorebuyer.corebuyer.fileprovider",
            file2);
        val file3 = File(data_companyNamePhoto)
        val contentUri3 = FileProvider.getUriForFile(this@ProductActivity,
            "us.productcorebuyer.corebuyer.fileprovider",
            file3);*/
        /*ACTION_SEND action to launch an email client installed on your Android device.*/
      //  uris.add(data_idLicensePhoto.toUri())
      //  uris.add(data_companyNamePhoto.toUri())
      //  uris.add(data_sellerNamePhoto.toUri())

          
        val uris = ArrayList<Uri>()
       // uris.add(Uri.fromFile(File(data_idLicensePhoto)))


        if (imageUriSignatueS!=null) uris.add(imageUriSignatueS!!)
        if (pdfFileUri!=null) uris.add(pdfFileUri!!)

       // uris.add(Uri.parse(File(data_sellerNamePhoto).toString()))
        for (elem in elementsPhotoPrice)
            uris.add(elem.imagePhotoUri!!)

      //  if (data_sellerNamePhoto!="") uris.add(imageUriSellerN!!)//
      //  if (data_companyNamePhoto!="") uris.add(imageUriCompnyN!!)//
      //  if (data_idLicensePhoto!="") uris.add(imageUriIdLicence!!)//

        val mIntent = Intent(Intent.ACTION_SEND_MULTIPLE)

        /*To send an email you need to specify mailto: as URI using setData() method
        and data type will be to text/plain using setType() method*/

       // mIntent.data = Uri.parse("mailto:")
        mIntent.type = "text/plain"
        // put recipient email in intent
        /* recipient is put as array because you may wanna send email to multiple emails
           so enter comma(,) separated emails, it will be stored in array*/

        mIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        mIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)

        //mIntent.putExtra(Intent.EXTRA_STREAM, uris)

        //mIntent.setType("image/png");

        val formatter1 = SimpleDateFormat("yyyyMMdd_HHmmss")
        val timeDateNow1 = formatter1.format(Date())


        val aS : Array<String> = arrayOf("Arcrecycling2014@gmail.com",data2_email)
        mIntent.putExtra(Intent.EXTRA_EMAIL, aS)
        //put the Subject in the intent
        mIntent.putExtra(Intent.EXTRA_SUBJECT, "receipt_"+timeDateNow1)
        //put the message in the intent
        mIntent.putExtra(Intent.EXTRA_TEXT, fS)
        mIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);


        try {
            //start email intent
            startActivity(Intent.createChooser(mIntent, "Choose Email Client..."))
        }
        catch (e: Exception){
            //if any thing goes wrong for example no email client application or any exception
            //get and show exception message
            Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
        }

    }
    //Signature Canvas
    private fun storeBitmap(bitmap: Bitmap) {
        val filename = "${System.currentTimeMillis()}_signature.jpg"
        var fos: OutputStream? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            contentResolver?.also { resolver ->
                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                    put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                }
                val imageUri: Uri? =
                    resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                fos = imageUri?.let { resolver.openOutputStream(it)  }
                imageUriSignatueS = imageUri

            }
        } else {
            val imagesDir =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            val image = File(imagesDir, filename)
            fos = FileOutputStream(image)
        }
        fos?.use {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
         //   Toast.makeText(this@ProductActivity, "Saved to Photos", Toast.LENGTH_LONG).show()
            it.close()
            bitmapSignature = bitmap
        }

    }
    //Create PDF

    fun generatePDF() {
        storeBitmap(paint.save()!!)

        var fS: String = ""
        val scaleAll = 5



        // creating an object variable
        // for our PDF document.
        if (checkPermissions1()) {
            // if permission is granted we are displaying a toast message.
            Toast.makeText(this@ProductActivity, "Permissions Granted..", Toast.LENGTH_SHORT).show()
        } else {
            // if the permission is not granted
            // we are calling request permission method.
            requestPermission1()
        }



        // bmp = BitmapFactory.decodeResource(resources, R.drawable.core_icon)

        var bmp1 = if ((data_idLicensePhoto!="") && (data_idLicensePhoto!="empty")) BitmapFactory.decodeFile(data_idLicensePhoto) else Bitmap.createBitmap(100*scaleAll,100*scaleAll,Bitmap.Config.ARGB_8888)
        var scaledbmp1 = Bitmap.createScaledBitmap(bmp1, 280*scaleAll, 280*scaleAll, false)

        //var bmp2 = BitmapFactory.decodeFile(imageUriSignatueS.toString())
        var scaledbmp2 = Bitmap.createScaledBitmap(bitmapSignature, 180*scaleAll, 80*scaleAll, false)

        var bmp3 = if ((data_companyNamePhoto!="") && (data_companyNamePhoto!="empty")) BitmapFactory.decodeFile(data_companyNamePhoto) else Bitmap.createBitmap(100*scaleAll,100*scaleAll,Bitmap.Config.ARGB_8888)
        var scaledbmp3 = Bitmap.createScaledBitmap(bmp3, 280*scaleAll, 280*scaleAll, false)

        var bmp4 = if ((data_sellerNamePhoto!="") && (data_sellerNamePhoto!="empty")) BitmapFactory.decodeFile(data_sellerNamePhoto) else Bitmap.createBitmap(100*scaleAll,100*scaleAll,Bitmap.Config.ARGB_8888)
        var scaledbmp4 = Bitmap.createScaledBitmap(bmp4, 280*scaleAll, 280*scaleAll, false)

        var bmpList : MutableList<Bitmap> = mutableListOf()
        var bmpScaledList : MutableList<Bitmap> = mutableListOf()

        for (cc in elementsPhotoPrice) {
            bmpList.add(if (cc.imagePhotoUriString!="") BitmapFactory.decodeFile(cc.imagePhotoUriString) else Bitmap.createBitmap(100*scaleAll,100*scaleAll,Bitmap.Config.ARGB_8888))

        }
        for (dd in bmpList){
            bmpScaledList.add(Bitmap.createScaledBitmap(dd, 280*scaleAll, 280*scaleAll, false))
        }

        var bmp5 = if (photoNotesString!="") BitmapFactory.decodeFile(photoNotesString) else Bitmap.createBitmap(100*scaleAll,100*scaleAll,Bitmap.Config.ARGB_8888)
        var scaledbmp5 = Bitmap.createScaledBitmap(bmp5, 280*scaleAll, 280*scaleAll, false)


        var pageHeight = 2000*scaleAll + elementsPhotoPrice.size*300*scaleAll
        var pageWidth = 300*scaleAll
        val alignT = 20F*scaleAll

        var pdfDocument: PdfDocument = PdfDocument()

        // two variables for paint "paint" is used
        // for drawing shapes and we will use "title"
        // for adding text in our PDF file.
        var paint1: Paint = Paint()
        var title1: Paint = Paint()

        // we are adding page info to our PDF file
        // in which we will be passing our pageWidth,
        // pageHeight and number of pages and after that
        // we are calling it to create our PDF.
        var myPageInfo: PdfDocument.PageInfo? =
            PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create()

        // below line is used for setting
        // start page for our PDF file.
        var myPage: PdfDocument.Page = pdfDocument.startPage(myPageInfo)

        // creating a variable for canvas
        // from our page of PDF.
        var canvas1: Canvas = myPage.canvas




        // below line is used to draw our image on our PDF file.
        // the first parameter of our drawbitmap method is
        // our bitmap
        // second parameter is position from left
        // third parameter is position from top and last
        // one is our variable for paint.


        // below line is used for adding typeface for
        // our text which we will be adding in our PDF file.
        title1.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL))

        // below line is used for setting text size
        // which we will be displaying in our PDF file.
        title1.textSize = 15F*scaleAll


        // below line is sued for setting color
        // of our text inside our PDF file.
        title1.setColor(ContextCompat.getColor(this@ProductActivity, R.color.black))

        // below line is used to draw text in our PDF file.
        // the first parameter is our text, second parameter
        // is position from start, third parameter is position from top
        // and then we are passing our variable of paint which is title.
      //   canvas1.drawText("A portal for IT professionals.", 209F, 100F, title1)
      //  canvas1.drawText("Geeks for Geeks", 209F, 80F, title1)
        title1.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL))
        title1.setColor(ContextCompat.getColor(this@ProductActivity, R.color.black))
        title1.textSize = 15F*scaleAll

        var plusPoints = 40F*scaleAll

        // below line is used for setting
        // our text to center of PDF.
        title1.textSize = 13F*scaleAll
        title1.isFakeBoldText = true
        title1.textAlign = Paint.Align.LEFT
        canvas1.drawText("   " + DISCLAIMER1, 100F*scaleAll, plusPoints, title1)
        title1.textSize = 12F*scaleAll
        plusPoints +=16*scaleAll
        title1.isFakeBoldText = false
        canvas1.drawText(" \n " + DISCLAIMER2, 100F*scaleAll, plusPoints, title1)
        plusPoints +=14*scaleAll
        canvas1.drawText(" \n " + DISCLAIMER3, 100F*scaleAll, plusPoints, title1)
        plusPoints +=14*scaleAll
        canvas1.drawText(" \n " + DISCLAIMER4, 100F*scaleAll, plusPoints, title1)
        plusPoints +=14*scaleAll
        canvas1.drawText(" \n " + DISCLAIMER5, 100F*scaleAll, plusPoints, title1)
        plusPoints +=14*scaleAll
        canvas1.drawText(" \n " + DISCLAIMER6, 100F*scaleAll, plusPoints, title1)
        plusPoints +=14*scaleAll
        title1.textAlign = Paint.Align.LEFT
        canvas1.drawText(" \n ", alignT, plusPoints, title1)
        plusPoints +=14*scaleAll
        canvas1.drawText(" \n Customer Name: " + data2_buyer_name, alignT, plusPoints, title1)
        plusPoints +=14*scaleAll
        canvas1.drawText(" \n Company Name: " + data2_company_name, alignT, plusPoints, title1)
        plusPoints +=14*scaleAll
        canvas1.drawText(" \n Address: " + data2_adress, alignT, plusPoints, title1)
        plusPoints +=14*scaleAll
        canvas1.drawText(" \n DPhone: " + data2_tell, alignT, plusPoints, title1)
        plusPoints +=14*scaleAll
        canvas1.drawText(" \n E-mail: " + data2_email, alignT, plusPoints, title1)
        plusPoints +=14*scaleAll
        canvas1.drawText(" \n Seller Name: " + data2_seller_namer, alignT, plusPoints, title1)
        plusPoints +=14*scaleAll
        canvas1.drawText(" \n EIN NUMBER: " + data2_ein_number, alignT, plusPoints, title1)
        plusPoints +=14*scaleAll
        canvas1.drawText(" \n " , alignT, plusPoints, title1)
        plusPoints +=20*scaleAll
        title1.textSize = 15F*scaleAll
        var q = ""; var p =0;
        for ( i in 0..(elementsPhotoPrice.size-1)) {
            q =  elementsPhotoPrice[i].textPrice
            p = elementsPhotoPrice[i].quantity
            canvas1.drawText("  $i) For $p units $q", alignT, plusPoints, title1)
            plusPoints +=20*scaleAll
        }
        canvas1.drawText("\n ___________________" , alignT, plusPoints, title1)
        plusPoints +=20*scaleAll
        canvas1.drawText("\n Total Quantity: ${a1}" , alignT, plusPoints, title1)
        plusPoints +=20*scaleAll
        canvas1.drawText("\n Total Price: ${"%.2f".format(a2).replace(",",".")}" , alignT, plusPoints, title1)
        paymentMethodGet = when (radioGroupPayment.checkedRadioButtonId) {
            R.id.radioCheck -> "Check"
            R.id.radioWire -> "Wire"
            else -> "Cash"
        }
        plusPoints +=20*scaleAll
        canvas1.drawText("\n Payment method: ${paymentMethodGet}" , alignT, plusPoints, title1)
        plusPoints +=20*scaleAll
        canvas1.drawText(" \n " , alignT, plusPoints, title1)
        plusPoints +=20*scaleAll
        title1.textSize = 8F*scaleAll

        canvas1.drawText("--------------------------------" , alignT, plusPoints, title1)
        plusPoints +=12*scaleAll

        canvas1.drawText("Notes: " , alignT, plusPoints, title1)
        plusPoints +=12*scaleAll

        textInNotes = editTextNotes.editText?.text.toString()
        var mutStrList = textInNotes.lines().toMutableList()
        for ( i in 0..(mutStrList.size-1)) {
            canvas1.drawText(mutStrList[i] , alignT, plusPoints, title1)
            plusPoints +=12*scaleAll
        }
        canvas1.drawText("--------------------------------" , alignT, plusPoints, title1)
        plusPoints +=24*scaleAll
        canvas1.drawText(" " + DISCLOSURE1 , alignT, plusPoints, title1)
        plusPoints +=12*scaleAll
        canvas1.drawText(" " + DISCLOSURE2 , alignT, plusPoints, title1)
        plusPoints +=12*scaleAll
        canvas1.drawText(" " + DISCLOSURE3 , alignT, plusPoints, title1)
        plusPoints +=12*scaleAll
        canvas1.drawText(" " + DISCLOSURE4, alignT, plusPoints, title1)
        plusPoints +=40*scaleAll
        title1.textSize = 15F*scaleAll

        val formatter = SimpleDateFormat("d MMM yyyy, HH:mm:ss z")
        val timeDateNow = formatter.format(Date())



        canvas1.drawText(timeDateNow , 40F*scaleAll, plusPoints, title1)
        plusPoints +=20*scaleAll
        title1.textSize = 12F*scaleAll
        canvas1.drawText("Geolocation: $locationString" , 40F*scaleAll, plusPoints, title1)
        plusPoints +=20*scaleAll
        canvas1.drawText("Signature" , 40F*scaleAll, plusPoints, title1)
        plusPoints +=20*scaleAll
        canvas1.drawBitmap(scaledbmp2, 40F*scaleAll, plusPoints, paint1)
        plusPoints +=90*scaleAll
        canvas1.drawBitmap(scaledbmp1, 10F*scaleAll, plusPoints, paint1)
        plusPoints +=290*scaleAll
        canvas1.drawBitmap(scaledbmp3, 10F*scaleAll, plusPoints, paint1)
        plusPoints +=290*scaleAll
        canvas1.drawBitmap(scaledbmp4, 10F*scaleAll, plusPoints, paint1)
        plusPoints +=290*scaleAll
        for (dd in bmpScaledList){
            canvas1.drawBitmap(dd, 10F*scaleAll, plusPoints, paint1)
            plusPoints +=290*scaleAll
        }
        canvas1.drawBitmap(scaledbmp5, 10F*scaleAll, plusPoints, paint1)





        // after adding all attributes to our
        // PDF file we will be finishing our page.
        pdfDocument.finishPage(myPage)

        // below line is used to set the name of
        // our PDF file and its path.

/*
        val filename1 = "GFG.pdf"
        var fos1: OutputStream? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            contentResolver?.also { resolver ->
                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, filename1)
                  //  put(MediaStore.MediaColumns.MIME_TYPE, "pdf")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
                }
                val imageUri: Uri? =
                    resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                fos1 = imageUri?.let { resolver.openOutputStream(it) }


            }
        } else {
            val imagesDir =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            val image = File(imagesDir, filename1)
            fos1 = FileOutputStream(image)
        }
*/
        val formatter1 = SimpleDateFormat("yyyyMMdd_HHmmss")
        val timeDateNow1 = formatter1.format(Date())

        val file1: File = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "receipt_"+timeDateNow1+".pdf")
       // val file2: File = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "receipt_"+timeDateNow1+".pdf")


            val file2: File? = try {
                val imagePath2 =  getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                File.createTempFile( "receipt_"+timeDateNow1,".pdf",imagePath2).apply {  }

            } catch (ex: IOException) {
                // Error occurred while creating the File
                null
            }
            file2?.also { file ->
            pdfFileUri = FileProvider.getUriForFile(
                    this,
                    "us.productcorebuyer.corebuyer.fileprovider",
                    file
                )
            }

            // intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            // Launch intent





       // pdfFileUri = file1.toUri()
     //   pdfFileUri =  pdfFileUri.toString().replace("file:","content:").toUri()
       /*
        pdfFileUri = FileProvider.getUriForFile(
            this@ProductActivity,
            "us.productcorebuyer.corebuyer.fileprovider",
            file1
        )
*/

        try {
            // after creating a file name we will
            // write our PDF file to that location.
            pdfDocument.writeTo(FileOutputStream(file1))
            pdfDocument.writeTo(FileOutputStream(file2))

            // on below line we are displaying a toast message as PDF file generated..
            Toast.makeText(applicationContext, "PDF file generated..", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            // below line is used
            // to handle error
            e.printStackTrace()

            // on below line we are displaying a toast message as fail to generate PDF
            Toast.makeText(applicationContext, "Fail to generate PDF file..", Toast.LENGTH_SHORT)
                .show()
        }
        // after storing our pdf to that
        // location we are closing our PDF file.
        pdfDocument.close()
    }
    fun requestPermission1() {
        val PERMISSION_CODE = 101
        // on below line we are requesting read and write to
        // storage permission for our application.
        ActivityCompat.requestPermissions(
            this@ProductActivity,
            arrayOf(READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE), PERMISSION_CODE
        )
    }
    fun checkPermissions1(): Boolean {
        // on below line we are creating a variable for both of our permissions.

        // on below line we are creating a variable for
        // writing to external storage permission
        var writeStoragePermission = ContextCompat.checkSelfPermission(
            applicationContext,
            WRITE_EXTERNAL_STORAGE
        )

        // on below line we are creating a variable
        // for reading external storage permission
        var readStoragePermission = ContextCompat.checkSelfPermission(
            applicationContext,
            READ_EXTERNAL_STORAGE
        )

        // on below line we are returning true if both the
        // permissions are granted and returning false
        // if permissions are not granted.
        return writeStoragePermission == PackageManager.PERMISSION_GRANTED
                && readStoragePermission == PackageManager.PERMISSION_GRANTED
    }



}