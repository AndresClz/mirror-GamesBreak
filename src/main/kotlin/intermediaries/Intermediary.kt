package intermediaries

open class Intermediary(override var price: Double) : IntermediaryInterface {
    override fun processPurchase(): Double {
        return price
    }

}