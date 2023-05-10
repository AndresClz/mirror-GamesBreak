package intermediaries

import java.time.LocalTime

class EpicGamesIntermediary() : IntermediaryInterface {
    private val FIRST_COMMISSION = 0.01
    private val SECOND_COMMISSION = 0.03
    override fun processPurchase(price: Double): Double {
        val currentTime: LocalTime = LocalTime.now()

        val (lowerLimitHours, lowerLimitMinutes) = Pair(20, 0)
        val lowerLimit = LocalTime.of(lowerLimitHours, lowerLimitMinutes)

        val (upperLimitHours, upperLimitMinutes) = Pair(23, 59)
        val upperLimit = LocalTime.of(upperLimitHours, upperLimitMinutes)


        return if((currentTime.isAfter(lowerLimit)) && (currentTime.isBefore(upperLimit))) {
            (price.plus(price.times(FIRST_COMMISSION)))
        }
        else {
            (price.plus(price.times(SECOND_COMMISSION)))

        }
    }
}