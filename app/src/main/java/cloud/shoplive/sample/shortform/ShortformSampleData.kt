package cloud.shoplive.sample.shortform

import cloud.shoplive.sdk.network.request.ShopLiveShortformTagSearchOperator

data class ShortformSampleData(
    val brand: List<String> = emptyList(),
    val hashTags: List<String> = emptyList(),
    val tagSearchOperator: ShopLiveShortformTagSearchOperator = ShopLiveShortformTagSearchOperator.OR
) {
    fun getTitle(): String {
        return if (brand.isNotEmpty()) {
            "Brand : ${brand.joinToString(",")}"
        } else if (hashTags.isNotEmpty()) {
            "HashTags : ${hashTags.joinToString(",")}"
        } else {
            "ALL"
        }
    }
}