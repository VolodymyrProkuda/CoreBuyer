package us.productcorebuyer.corebuyer

import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.ObjectId
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.RealmSet
import io.realm.kotlin.types.annotations.PrimaryKey

open class ItemDataOrder () : RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId.create()
    var customerName: String = ""
    var companyName: String = ""
    var adress: String = ""
    var tell: String = ""
    var email: String = ""
    var sellerName: String = ""
    var einNumber: String = ""
    var itemOrderQuantity: RealmList<Int> = realmListOf(0)
    var itemOrderPrice : RealmList<String> = realmListOf("")
    var fullOrderQuantity : Int = 0
    var fullOrderPrice : String = ""
    var currentReceiptDate : String = ""
    var paymentMethod : String = ""
    var idLicencePhoto : ByteArray = byteArrayOf(0x2E)
    var companyNamePhoto : ByteArray = byteArrayOf(0x2E)
    var sellerNamePhoto : ByteArray = byteArrayOf(0x2E)
    var productArrayPhoto : RealmList<ByteArray> = realmListOf(byteArrayOf(0x2E))
    
}