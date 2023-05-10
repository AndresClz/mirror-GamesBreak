package intermediaries

class SteamIntermediary() : IntermediaryInterface {
    private val FIRST_COMMISSION = 0.02
    override fun processPurchase(price: Double): Double {
        return (price.plus(price.times(FIRST_COMMISSION)))
    }
}