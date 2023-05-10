package intermediaries

import java.time.DayOfWeek
import java.time.LocalDate

class NakamaIntermediary() : IntermediaryInterface {
    private val FIRST_COMMISSION = 0.03
    private val SECOND_COMMISSION = 0.0075
    override fun processPurchase(price: Double): Double {
        val currentDate: LocalDate = LocalDate.now()

        return if (currentDate.dayOfWeek == DayOfWeek.SATURDAY || currentDate.dayOfWeek == DayOfWeek.SUNDAY) {
            (price.plus(price.times(FIRST_COMMISSION)))
        } else {
            (price.plus(price.times(SECOND_COMMISSION)))
        }
    }
}