package cloud.shoplive.sample.core

data class SellerStoreData(
    val campaignKey: String?,
    val campaignStatus: String?,
    val campaignTitle: String?,
    val seller: Seller?
)

data class SellerSubscriptionData(
    val campaignKey: String?,
    val campaignStatus: String?,
    val campaignTitle: String?,
    val isLogin: Boolean,
    val saved: Boolean,
    val seller: Seller?
)

data class Seller(
    val description: String?,
    val name: String?,
    val profileUrl: String?,
    val sellerId: Int,
    val sellerIdentifier: String,
    val storeUrl: String?
)
