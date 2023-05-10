package intermediaries

import java.time.DayOfWeek
import java.time.LocalDate

class NakamaIntermediary(price: Double) : Intermediary(price) {
    override fun processPurchase(): Double {
        super.processPurchase()
        val currentDate: LocalDate = LocalDate.now()

        if (currentDate.dayOfWeek == DayOfWeek.SATURDAY || currentDate.dayOfWeek == DayOfWeek.SUNDAY) {
            return (price + price.times(0.03))
        } else {
            return (price + price.times(0.0075))
        }
    }
}