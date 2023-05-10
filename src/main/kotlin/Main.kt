fun main() {
    startInitialMenu()
}

fun startInitialMenu(){
    print("Bienvenido a GamesBreak \nQue desea hacer?")
    showUserOptions()

}
fun showUserOptions() {
    print("\n 1. Iniciar Sesion \t 2.Ver Juegos Disponible\n")
    var userOption = readln()
    while (userOption != "1" && userOption != "2"){
        print("$userOption no es una opcion valida, por favor ingrese una opcion nuevamente: \n1. Iniciar Sesion \t 2.Ver Juegos Disponible\n")
        userOption = readln()
    }
    when (userOption) {
        "1"-> startLogin()
        "2"-> showGames()
    }
}
fun startLogin(){
    print("Ingrese su nickname: \n")
    val userNickname = readln()
    print("Ingrese su contrasena: \n")
    val userPassword = readln()
    UserSession.doLoginWith(userNickname,userPassword,
        onSuccess = {user ->
            print("Bienvenido ${user.name}")
            showGames()
        },
       onFailure =  {
            print("Usuario o contrasena incorrecto\n")
           showUserOptions()
       })
}


fun showGames(){
    print("\nShowing Games")
}