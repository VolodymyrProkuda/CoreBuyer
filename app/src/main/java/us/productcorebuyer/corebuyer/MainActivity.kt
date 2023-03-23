package us.productcorebuyer.corebuyer

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.view.View.MeasureSpec
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import com.google.android.material.textfield.TextInputLayout
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {
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


  private val itemLocalDBs = mutableListOf<ItemLocalDB>()
  private lateinit var itemLocalDBone : ItemLocalDB

  lateinit var currentPhotoPath: String
// EVN Auto Recycle Inc \n 1230 Ave X apt 2B \n 646 633 5420 \n Evgeniy27m@gmail.com
//  private lateinit var menu : Menu

  private lateinit var buttonSearch : Button
  private lateinit var buttonSave : Button
  private lateinit var buttonNext : Button
  private lateinit var buttonChoose : Button
  private lateinit var imageButtonCompany : ImageButton
  private lateinit var imageButtonSeller : ImageButton
  private lateinit var imageButtonLicencePhoto: ImageButton
  private lateinit var radioSelectSearch : RadioGroup
  private lateinit var textAdr : TextView


  private lateinit var data_ein_number : TextInputLayout
  private lateinit var data_seller_name : TextInputLayout
  private lateinit var data_email : TextInputLayout
  private lateinit var data_tell : TextInputLayout
  private lateinit var data_adress : TextInputLayout
  private lateinit var data_company_name : TextInputLayout
  private lateinit var data_buyer_name : TextInputLayout

  private  var idLicenseBitmapByteArray : ByteArray = byteArrayOf(0x2E)
  private  var companyNameBitmapByteArray : ByteArray = byteArrayOf(0x2E)
  private  var sellerNameBitmapByteArray : ByteArray = byteArrayOf(0x2E)

  private  var idLicensePhotoPath = ""
  private  var sellerNamePhotoPath = ""
  private  var companyNamePhotoPath = ""


  private val CAMERA_PERMISSION_CODE = 1000
  private val IMAGE_CAPTURE_CODE = 1001
  private var imageUri: Uri? = null
  private var imageUriIdLicence: Uri? = null
  private var imageUriCompnyN: Uri? = null
  private var imageUriSellerN: Uri? = null
  private var imageViewIdLicence: ImageView? = null
  private var buttonNum = 0

  override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      setContentView(R.layout.activity_main)

      data_ein_number = findViewById(R.id.ein_number)
      data_seller_name = findViewById(R.id.seller_name)
      data_email = findViewById(R.id.email)
      data_tell = findViewById(R.id.tell)
      data_adress = findViewById(R.id.adress)
      data_company_name = findViewById(R.id.company_name)
      data_buyer_name = findViewById(R.id.buyer_name)

      buttonChoose = findViewById(R.id.button_choose)
      buttonNext = findViewById(R.id.buttonNext)
      buttonSearch = findViewById(R.id.button_search)
      buttonSave = findViewById(R.id.button_save)
      imageButtonCompany = findViewById<ImageButton>(R.id.imageButtonCompany)
      imageButtonSeller = findViewById<ImageButton>(R.id.imageButtonSeller)
      imageButtonLicencePhoto = findViewById<ImageButton>(R.id.imageButtonLicencePhoto)
      radioSelectSearch = findViewById(R.id.radio_group_search)
      textAdr = findViewById(R.id.textAdr)

      textAdr.text = DISCLAIMER1+"\n"+DISCLAIMER2+"\n"+DISCLAIMER3+"\n"+DISCLAIMER4+"\n"+DISCLAIMER5+"\n"+DISCLAIMER6


       itemLocalDBone = ItemLocalDB(
           intent.getStringExtra("data_company_name").toString(),
           intent.getStringExtra("data_tell").toString(),
           intent.getStringExtra("data_ein_number").toString(),
           intent.getStringExtra("data_seller_namer").toString(),
           intent.getStringExtra("data_email").toString(),
           intent.getStringExtra("data_adress").toString(),
           intent.getStringExtra("data_buyer_name").toString(),
           intent.getStringExtra("id_license_photo").toString(),
           intent.getStringExtra("company_name_photo").toString(),
           intent.getStringExtra("seller_namer_photo").toString()
       )



/*
       itemLocalDBone.einNumber =
       itemLocalDBone.sellerName =
       itemLocalDBone.email =
       itemLocalDBone.tell =
       itemLocalDBone.address =
       itemLocalDBone.companyName
       itemLocalDBone.buyerName =
       itemLocalDBone.idLicensePhotoString =
       itemLocalDBone.sellerNamePhotoString =
       itemLocalDBone.companyNamePhotoString =

*/


       if ((itemLocalDBone.companyName!="null")&&(itemLocalDBone.tell!="null")) {
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

      }




      buttonChoose.setOnClickListener {
          val intentToCustomerChoose = Intent(this, CustomerListActivity::class.java)
          startActivity(intentToCustomerChoose)
      }
      buttonSave.setOnClickListener {
          saveToLocalDB()
      }
      buttonSearch.setOnClickListener {
          loadFromLocalDB()
      }
      buttonNext.setOnClickListener {
          onSave()
      }
      imageButtonCompany.setOnClickListener {
          // Request permission
          val permissionGranted = requestCameraPermission()
          if (permissionGranted) {
              // Open the camera interface
              buttonNum = 1
              openCameraInterface()
              //Try

          }
      }
      imageButtonSeller.setOnClickListener {
          // Request permission
          val permissionGranted = requestCameraPermission()
          if (permissionGranted) {
              // Open the camera interface
              buttonNum = 2
              openCameraInterface()
          }
      }
      imageButtonLicencePhoto.setOnClickListener {
          // Request permission
          val permissionGranted = requestCameraPermission()
          if (permissionGranted) {
              // Open the camera interface
              buttonNum = 3
              openCameraInterface()

          }
      }



  }
  override fun onCreateOptionsMenu(menu: Menu): Boolean {
      // Inflate the menu; this adds items to the action bar if it is present.
      menuInflater.inflate(R.menu.core_menu, menu)
      menu.findItem(R.id.id_email).setVisible(false)
      menu.findItem(R.id.id_print).setVisible(false)
      menu.findItem(R.id.id_createPDF).setVisible(false)
      return true
  }
  override fun onOptionsItemSelected(item: MenuItem): Boolean {
      return when (item.itemId) {
          R.id.id_save -> {
              saveToLocalDB()
              true
          }
          R.id.id_forward -> {
              onSave()
              true
          }
          else -> super.onOptionsItemSelected(item)
      }
  }

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


  @SuppressLint("QueryPermissionsNeeded")
  private fun openCameraInterface() {
    /*  val values = ContentValues()
      values.put(MediaStore.Images.Media.TITLE, "Take Picture")
      values.put(MediaStore.Images.Media.DESCRIPTION, "Camera picture for sample app")
      imageUri = this?.contentResolver?.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
*/
/*
      Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
          // Ensure that there's a camera activity to handle the intent
          takePictureIntent.resolveActivity(packageManager)?.also {
              // Create the File where the photo should go
              val photoFile: File? = try {
                  createImageFileInAppDir()
              } catch (ex: IOException) {
                 null
              }
              // Continue only if the File was successfully created
              photoFile?.also {
                  imageUri = FileProvider.getUriForFile(
                      this,
                      "us.productcorebuyer.corebuyer.fileprovider",
                      it
                  )
                  takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
                  startActivityForResult(takePictureIntent, IMAGE_CAPTURE_CODE)
              }
          }
      }

*/

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

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
      super.onActivityResult(requestCode, resultCode, data)

      // Callback from camera intent
      if (resultCode == Activity.RESULT_OK){
          // Set image captured to image view
          if (buttonNum==1)  {
              imageButtonCompany.setImageURI(imageUri)
              imageUriCompnyN=imageUri

              companyNamePhotoPath = currentPhotoPath
          }

          if (buttonNum==2) {
              imageButtonSeller.setImageURI(imageUri)
              imageUriSellerN=imageUri

              sellerNamePhotoPath = currentPhotoPath
          }

          if (buttonNum==3) {
              imageButtonLicencePhoto.setImageURI(imageUri);
              imageUriIdLicence=imageUri
          //    idLicenseBitmapByteArray=convertImToByteArr(imageButtonLicencePhoto)
            //  imageUriIdLicence = currentPhotoPath.toUri()
             // buttonSave.text = imageUriIdLicence.toString()
              idLicensePhotoPath = currentPhotoPath

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

  private fun onSave(){

      if ((data_tell.editText?.text.toString()!="")&&
          (data_company_name.editText?.text.toString()!="")
      ) {
          val intentToProduct = Intent(this, ProductActivity::class.java)

        //  intentToProduct.putExtra("PhotoPict", currentPhotoPath)
          intentToProduct.putExtra( "data_company_name", data_company_name.editText?.text.toString())
          intentToProduct.putExtra("data_tell", data_tell.editText?.text.toString())


          intentToProduct.putExtra("data_ein_number", if (data_ein_number.editText?.text.toString()!="") data_ein_number.editText?.text.toString() else "empty")
          intentToProduct.putExtra("data_seller_namer",if (data_seller_name.editText?.text.toString()!="") data_seller_name.editText?.text.toString() else "empty")
          intentToProduct.putExtra("data_email", if ((data_email.editText?.text.toString()!="")) data_email.editText?.text.toString() else "empty")
          intentToProduct.putExtra("data_adress", if ((data_adress.editText?.text.toString()!=""))data_adress.editText?.text.toString()else "empty")
          intentToProduct.putExtra("data_buyer_name",if ((data_buyer_name.editText?.text.toString()!="")) data_buyer_name.editText?.text.toString()else "empty")
/*
          (companyNamePhotoPath!="")&&
          (sellerNamePhotoPath!="")&&
          (idLicensePhotoPath!="")
  */
          intentToProduct.putExtra("id_license_photo", idLicensePhotoPath)
          intentToProduct.putExtra("seller_namer_photo", sellerNamePhotoPath)
          intentToProduct.putExtra("company_name_photo", companyNamePhotoPath)
          intentToProduct.putExtra("lastUri",imageUri.toString())

          intentToProduct.putExtra("imageUriIdLicence",imageUriIdLicence.toString())
          intentToProduct.putExtra("imageUriCompnyN",imageUriCompnyN.toString())
          intentToProduct.putExtra("imageUriSellerN",imageUriSellerN.toString())


          /*
          intentToProduct.putExtra("id_license_photo", idLicenseBitmapByteArray)
          intentToProduct.putExtra("seller_namer_photo", sellerNameBitmapByteArray)
          intentToProduct.putExtra("company_name_photo", companyNameBitmapByteArray)
*/

          startActivity(intentToProduct)
      }
  }

/*  fun  convertBase64StringToBitmap(source: String ): Bitmap {
      val rawBitmap : ByteArray = Base64.decode(source.toByteArray(), Base64.DEFAULT);
      val  bitmap : Bitmap  = BitmapFactory.decodeByteArray(rawBitmap, 0, rawBitmap.size);
      return bitmap;
  }*/


  fun convertImToByteArr( imview: ImageView) : ByteArray {
     val imageView : ImageView = imview
      imageView.setDrawingCacheEnabled(true)
      imageView.measure(
          MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
          MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
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

  fun getByteArrayfromBitmap(bitmap: Bitmap): ByteArray? {
      val bos = ByteArrayOutputStream()
      bitmap.compress(CompressFormat.JPEG, 60, bos)
      return bos.toByteArray()
  }

  fun getBitmapfromByteArray(bitmap: ByteArray): Bitmap? {
      return BitmapFactory.decodeByteArray(bitmap, 0, bitmap.size)
  }

  @Throws(IOException::class)
  private fun createImageFileInAppDir(): File {
      val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
      val imagePath =  getExternalFilesDir(Environment.DIRECTORY_PICTURES)

      return   File.createTempFile( "JPEG_${timeStamp}_", ".jpg",imagePath
          ).apply { currentPhotoPath = absolutePath }
  }


//Local DB
  fun saveToLocalDB(){

  val sPrefLoad = getPreferences(MODE_PRIVATE)
  val savedText: String = sPrefLoad.getString("numOfItemLocalDB", "0").toString()
  var localItemsInDB = savedText.toInt()


  //Toast.makeText(this, "Text find", Toast.LENGTH_SHORT).show()


      var st = ""


  if ((data_tell.editText?.text.toString()!="")&&
      (data_company_name.editText?.text.toString()!="")
  ) {
      localItemsInDB++
      st =  (data_company_name.editText?.text.toString())+"\n"+ (data_tell.editText?.text.toString())+"\n"

      st +=   (if (data_ein_number.editText?.text.toString()!="") data_ein_number.editText?.text.toString() else "empty")+ "\n"
      st +=   (if (data_seller_name.editText?.text.toString()!="") data_seller_name.editText?.text.toString() else "empty")+ "\n"
      st +=   (if ((data_email.editText?.text.toString()!="")) data_email.editText?.text.toString() else "empty")+ "\n"
      st +=   (if ((data_adress.editText?.text.toString()!=""))data_adress.editText?.text.toString()else "empty")+ "\n"
      st +=   (if ((data_buyer_name.editText?.text.toString()!="")) data_buyer_name.editText?.text.toString()else "empty")+ "\n"
      st +=   (if ((idLicensePhotoPath!="")) idLicensePhotoPath else "empty")+ "\n"
      st +=   (if ((companyNamePhotoPath!="")) companyNamePhotoPath else "empty")+ "\n"
      st +=   (if ((sellerNamePhotoPath!="")) sellerNamePhotoPath else "empty")


  val sPrefSave = getPreferences(MODE_PRIVATE)
  val ed: SharedPreferences.Editor = sPrefSave.edit()
  ed.putString("numOfItemLocalDB", localItemsInDB.toString())
  ed.putString(localItemsInDB.toString(), st)
  ed.apply()
  Toast.makeText(this, "Item saved in local DB", Toast.LENGTH_SHORT).show()
          }
  }

  fun loadFromLocalDB(){
      var searchType = "Comp"
      var st = ""
      var indexResultSearch = 0
      var lines: MutableList<String> = mutableListOf()
      var compArr: MutableList<String> = mutableListOf()
      var custArr: MutableList<String> = mutableListOf()
      var sellArr: MutableList<String> = mutableListOf()

      val sPrefLoad = getPreferences(MODE_PRIVATE)
      val savedText: String = sPrefLoad.getString("numOfItemLocalDB", "0").toString()
      val localItemsInDB = savedText.toInt()
      if (localItemsInDB==0) Toast.makeText(this@MainActivity, "DB is empty", Toast.LENGTH_SHORT).show() else
      {
          for (i in 1..localItemsInDB) {
          st = sPrefLoad.getString(i.toString(), "").toString()
          lines = st.lines().toMutableList()
          itemLocalDBone = ItemLocalDB(lines[0],lines[1],lines[2],lines[3],lines[4],lines[5],lines[6],lines[7],lines[8],lines[9])
          itemLocalDBs.add(itemLocalDBone)
          }
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

      }



    //  val itemLocalDBs = mutableListOf<ItemLocalDB>()
    //  lateinit var itemLocalDBone : ItemLocalDB

  }



}