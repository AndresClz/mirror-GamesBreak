package repositories

import data.User

object UserRepository {

    private val users = mutableListOf<User>()

    init {
        users.add(User(1504L, "BRIAN_BAYARRI", "abc123", "Brian", "Bayarri", 350.50, "2022/12/10"))
        users.add(User(2802L, "AHOZ", "lock_password", "Aylen", "Hoz", 200.50, "2021/01/11"))
        users.add(User(1510L, "Diegote", "@12345", "Diego", "Gonzales", 12.0, "2018/04/15"))
    }

    fun get(): List<User> = users

    fun login(nickName: String, password: String) : User? {
        for(user in users)
            if(user.nickName == nickName && user.password == password) return user

        return null
    }

}