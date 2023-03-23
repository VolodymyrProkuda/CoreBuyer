package us.productcorebuyer.corebuyer

import android.net.Uri
import android.widget.TextView

data class ElementPhotoPrice(
    var quantity: Int,
    val imagePhotoUri: Uri?,
    var textPrice: String,
    var intPrice: Double,
    //var prodIemPhotoByteArray: ByteArray?,
    var imagePhotoUriString: String
    )