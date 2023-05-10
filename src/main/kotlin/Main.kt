import data.Game
import data.Purchase
import intermediaries.*
import repositories.GameRepository
import repositories.PurchaseRepository
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter

fun main() {
    startInitialMenu()
    showMainMenu()
    showPurchaseMenu()
}

fun startInitialMenu(){
    print("Bienvenido a GamesBreak \nQue desea hacer?")
    showUserOptions()
}
fun showMainMenu() {
    var shouldShowGames = true

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
            "2"-> {
                showPurchaseHistory()
                print("Presione una tecla para continuar")
                readln()
            }
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
            val gamePurchased = GameRepository.getByID(purchase.gameId.toString())
                gamePurchased?.let {
                    print("${purchase.amount}".padEnd(10) + it.name.padEnd(30) + "${purchase.createdDate}\n")
                }
        }
    } else {
        print("No has comprado ningun juego :O \n Ve a la tienda y compra alguno :D\n")
    }
}

fun showPurchaseMenu() {
    print("Menu de compra \n")

    while(!UserSession.isUserLogged()){
        print("Si no esta logueado, no podemos continuar con la compra\n")
        startLogin()
        showGames()
    }

    var userCurrentMoney = Credentials.getUserMoney()
    print("\nSu saldo actual es de $userCurrentMoney")

    print("\nIngrese el ID del juego que quiere comprar o X para salir \n")
    var gameID = readln()

    while (gameID.lowercase() != "x") {
        var gameToBuy: Game? = GameRepository.getByID(gameID)
        var gameFound: Boolean = (if(gameToBuy == null) false else true)
        var canBePurchased: Boolean = (canUserBuyIt( if (gameFound) gameToBuy!!.price else 0.0))


        while (!gameFound || !canBePurchased) {
            if(!gameFound) {
                print("Juego no encontrado $gameID\n")
            }
            if (!canBePurchased) {
                print("Saldo Insuficiente")
            }
            print("\nSu saldo actual es de $userCurrentMoney")
            print("\nIngrese el ID del juego que quiere comprar o X para salir \n")
            gameID = readln()
            if (gameID.lowercase() == "x"){ return }
            gameToBuy = GameRepository.getByID(gameID)
            gameFound = (if(gameToBuy == null) false else true)
            canBePurchased = (canUserBuyIt( if (gameFound) gameToBuy!!.price else 0.0))

        }
        if (gameToBuy == null) return
        if (gameID.lowercase() == "x"){ return }

        print("Usted ha seleccionado ${gameToBuy.name} con un precio base de ${gameToBuy.price}\n")
        makePurchase(gameToBuy,userCurrentMoney)
        userCurrentMoney = Credentials.getUserMoney()
        showGames()
        print("\nSu saldo actual es de $userCurrentMoney")
        print("\nIngrese el ID del juego que quiere comprar o X para salir \n")
        gameID = readln()
    }


}

fun canUserBuyIt(gamePrice:Double): Boolean {
    val userMoney = Credentials.getUserMoney() ?: 0.0
    return userMoney > gamePrice
}
fun preparePurchase(game: Game): Purchase {
    val lastPurchaseID = PurchaseRepository.getAll()?.last()?.id?.plus(1L) ?: 0L
    val userID = Credentials.getUserID() ?: 0L
    val todayDate = LocalDate.now()
    val formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd")

    val formattedDate = todayDate.format(formatter)
    val price = processPrice(game.price)
    return Purchase(lastPurchaseID, userID, game.id, price, formattedDate)
}

fun processPrice(gamePrice: Double): Double {
    val intermediary: IntermediaryInterface
    print("Ingrese que intermediario quiere usar:")
    print("\n 1. Steam \t 2.Epic Games\t 3.Nakama: \n")
    var userIntermediary = readln()
    while (userIntermediary != "1" && userIntermediary != "2" && userIntermediary != "3") {
        print("$userIntermediary no es una opcion valida, por favor ingrese una opcion nuevamente: \n1. Steam \t2. Epic Games\n3.Nakama")
        userIntermediary = readln()
    }
    intermediary = when (userIntermediary) {
        "1" -> SteamIntermediary()
        "2" -> EpicGamesIntermediary()
        "3" -> NakamaIntermediary()
        else -> SteamIntermediary()
    }
    return intermediary.processPurchase(gamePrice)

}

fun makePurchase(game: Game, userCurrentMoney: Double?) {
    val newPurchase = preparePurchase(game)
    val newPrice = newPurchase.amount
    print("Esta por comprar " + game.name.padEnd(game.name.length) + " por un precio de " + newPrice.toString().padEnd(8)+"\n")
    print("Desea continuar? \n 1. Si \t 2. Cancelar\n")
    var shouldContinue = readln()
    while (shouldContinue != "1" && shouldContinue != "2" ){
        print("$shouldContinue no es una opcion valida, por favor ingrese una opcion nuevamente: \n1. Steam \t2. Epic Games\n3.Nakama")
        shouldContinue = readln()
    }
    when (shouldContinue) {
        "1"-> {
            PurchaseRepository.addItem(newPurchase)
            val newUserMoney = userCurrentMoney?.minus(newPrice)?.plus(applyCashback(newPurchase))
            if (newUserMoney != null) {
                Credentials.updateUserMoney(newUserMoney)
            }
            print("Gracias por su Compra!!\n")
            showPurchaseHistory()
            print("Presione una tecla para continuar")
            readln()
        }
        "2"-> {
            print("Esperemos que la proxima compre con nosotros :D\n")
        }

    }
}
fun applyCashback(lastPurchase: Purchase): Double {
    val currentDate: LocalDate = LocalDate.now()
    val userAccountCreationDate = Credentials.getUserCreationDate()
    val diff = Period.between(userAccountCreationDate,currentDate)
    val monthsDiff = diff.toTotalMonths().toInt()
    var cashBackAmount = 0.0

    if (monthsDiff <= 3) {
        cashBackAmount = lastPurchase.amount.times(0.05)
    } else if (monthsDiff <= 12) {
        cashBackAmount = lastPurchase.amount.times(0.03)
    }
    return  cashBackAmount
}