package intermediaries

class SteamIntermediary(price: Double) : Intermediary(price) {
    override fun processPurchase(): Double {
        super.processPurchase()
        return (price + price.times(0.02))
    }
}