import data.User
import java.time.*
import java.time.format.DateTimeFormatter

object Credentials {
    private var userNickName: String? = null
    private var userID: Long? = null
    private var userName: String? = null
    private var userSurname: String? = null
    private var initDate: LocalDate? = null

    private var creationDate: String? = null


    fun loadCredentials(user: User) {
        this.userNickName = user.nickName
        this.userID = user.id
        this.userName = user.nickName
        this.userSurname = user.surname
        this.creationDate = user.createdDate

        val formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd")
        this.initDate = LocalDate.parse(creationDate,formatter)
    }
}
