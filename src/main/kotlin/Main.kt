import data.Game
import data.Purchase
import repositories.GameRepository
import repositories.PurchaseRepository

fun main() {
    startInitialMenu()
    showMainMenu()

}

fun startInitialMenu(){
    print("Bienvenido a GamesBreak \nQue desea hacer?")
    showUserOptions()
}
fun showMainMenu() {
    var shouldShowGames: Boolean = true

    if(UserSession.isUserLogged()) {
        print("\nQue desea hacer?")
        print("\n 1. Ver la tienda \t 2.Ver historial de compras\n")
        var userOption = readln()
        while (userOption != "1" && userOption != "2"){
            print("$userOption no es una opcion valida, por favor ingrese una opcion nuevamente: \n1. Ver la tienda \t 2.Ver historial de compras\n")
            userOption = readln()
        }
        when (userOption) {
            "1"-> {
                showGames()
                shouldShowGames =false
            }
            "2"-> showPurchaseHistory()
        }
    }
        if (shouldShowGames) {
            showGames()
        }
}
fun showUserOptions() {
    print("\n 1. Iniciar Sesion \t 2.Continuar sin iniciar Sesion\n")
    var userOption = readln()
    while (userOption != "1" && userOption != "2"){
        print("$userOption no es una opcion valida, por favor ingrese una opcion nuevamente: \n1. Iniciar Sesion \t 2.Ver Juegos Disponible\n")
        userOption = readln()
    }
    when (userOption) {
        "1"-> startLogin()
        "2"-> return
    }
}
fun startLogin(){
    print("Ingrese su nickname: \n")
    val userNickname = readln()
    print("Ingrese su contrasena: \n")
    val userPassword = readln()
    UserSession.doLoginWith(userNickname,userPassword,
        onSuccess = {_ ->
            print("Login Exitoso!")
        },
       onFailure =  {
           print("Usuario o contrasena incorrecto\n")
           print("\nQue desea hacer?")
           showUserOptions()
       })
}


fun showGames() {
    print("Bienvenido a la tienda! ${ if (UserSession.isUserLogged())  Credentials.getUserName() else "" }\n")
    print("Estos son los juegos disponibles:\n")
    val games: List<Game>? = GameRepository.getAll()
    print("Id".padEnd(5) + "Juego".padEnd(30) + "Precio\n")
    if (games != null) {
        for (game in games) {
           print("${game.id}".padEnd(5) + game.name.padEnd(30) + "${game.price}\n")
        }
    } else {
        print("No hay juegos Disponibles :( \n Intente mas tarde")
    }
}
fun showPurchaseHistory() {
    print("Este es tu historial de compras ${ Credentials.getUserName()}\n")
    val purchases: List<Purchase>? = PurchaseRepository.getAll()
    var filteredpurchases: List<Purchase>? = null
    if (purchases != null) {
        filteredpurchases = purchases.filter { purchase: Purchase -> purchase.userId == Credentials.getUserID() }
    }

    if (filteredpurchases != null) {
        print("Precio".padEnd(10) + "Juego".padEnd(30) + "Fecha de compra\n")
        for (purchase in filteredpurchases) {
            val gamePurchased = GameRepository.getByID(purchase.id.toString())
                gamePurchased?.let {
                    print("${purchase.amount}".padEnd(10) + it.name.padEnd(30) + "${purchase.createdDate}\n")
                }
        }
    } else {
        print("No has comprado ningun juego :O \n Ve a la tienda y compra alguno :D\n")
    }
}