package intermediaries

import java.time.LocalTime

class EpicGamesIntermediary(price: Double) : Intermediary(price) {
    override fun processPurchase(): Double {
        super.processPurchase()
        val currentTime: LocalTime = LocalTime.now()
        val lowerLimit = LocalTime.of(20,0)
        val upperLimit = LocalTime.of(23,59)

        return if((currentTime.isAfter(lowerLimit)) && (currentTime.isBefore(upperLimit))) {
            (price + price.times(0.01))
        }
        else {
            (price + price.times(0.03))
        }
    }
}