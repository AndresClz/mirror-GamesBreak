import data.User
import repositories.RepositoryInterface
import repositories.UserRepository

object UserSession {
    private var userRepository: RepositoryInterface<User> = UserRepository
    private var isLogged: Boolean = false
    fun doLoginWith(user: String,
                    password: String,
                    onSuccess: (User) -> Unit,
                    onFailure: () -> Unit) {
        userRepository.getByID(user).let {it
            if (it != null) {
                if (password == it.password){
                    Credentials.loadCredentials(it)
                    isLogged = true
                    onSuccess(it)
                }
            } else {
                onFailure()
            }
        }
    }
    fun isUserLogged(): Boolean {
        return this.isLogged
    }
}