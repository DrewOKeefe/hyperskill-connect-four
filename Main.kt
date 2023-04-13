package connectfour

import kotlin.system.exitProcess

var board = mutableListOf(mutableListOf<String>())
var rows = 0
var columns = 0
var player = ""
var p1 = ""
var p2 = ""
var marker = "o"
const val p1Marker = "o"
const val p2Marker = "*"
var p1Wins = 0
var p2Wins = 0
var gameCount = 1
var totalGames = 0

fun playerNames() {
    println("First player's name:")
    p1 = readln()
    player = p1
    println("Second player's name:")
    p2 = readln()
}

fun buildBoard() {
    while (true) {
        println("Set the board dimensions (Rows x Columns)")
        println("Press Enter for default (6 x 7)")
        var input = readln()
            .filter { !it.isWhitespace() }.lowercase()
        if (input.isEmpty()) input = "6x7"
        if (!input.matches("\\d+\\s*[xX]\\s*\\d+".toRegex())) {
            println("Invalid input")
            continue
        }
        rows = input.first().digitToInt()
        columns = input.last().digitToInt()
        if (rows !in 5..9) println("Board rows should be from 5 to 9")
        else if (columns !in 5..9) println("Board columns should be from 5 to 9")
        else {
            board = MutableList(rows) { MutableList(columns) {" "} }
            return
        }
    }
}

fun printBoard() {
    if (board[rows-1] == List(columns) {" "} && totalGames > 1)
        println ("Game #${gameCount}")
    for (i in 1..columns) print(" $i")
    println()
    for (i in 0 until rows) {
        for (j in 0 until  columns) {
            print("║${board[i][j]}")
        }
        print("║")
        println()
    }
    println("╚═${"╩═".repeat(columns-1)}╝")
}
 
fun playerInput() {
    println("$player's turn:")
    val colSelect = readln()
    when {
        colSelect == "end" -> {
            println("Game over!")
            return
        }
        colSelect.toIntOrNull() == null -> {
            println("Incorrect column number")
            return playerInput()
        }
        colSelect.toInt() !in 1..columns -> {
            println("The column number is out of range (1 - $columns)")
            return playerInput()
        }
        colSelect.toInt() in 1..columns -> {
            if (board[0][colSelect.toInt()-1] == " ") {
                for (rowSelect in rows - 1 downTo 0) {
                    if (board[rowSelect][colSelect.toInt()-1] == " ") {
                        if (player == p1) {
                            board[rowSelect][colSelect.toInt()-1] = marker
                            printBoard()
                            checkWin(player)
                            player = p2
                            marker = p2Marker
                            playerInput()
                        } else {
                            board[rowSelect][colSelect.toInt()-1] = marker
                            printBoard()
                            checkWin(player)
                            player = p1
                            marker = p1Marker
                            playerInput()
                        }
                    } else continue
                }
            } else {
                println("Column $colSelect is full")
                return playerInput()
            }
        }
    }
    return
}

fun winMessage() {
    if (totalGames != 1) println("Score\n$p1: $p1Wins $p2: $p2Wins")
    if (gameCount == totalGames) {
        println("Game over!")
        exitProcess(0)
    } else gameReset()
    }

fun checkWin(player: String) {
    var winner = ""
    for (row in rows - 1 downTo 0) {
        for (col in 0 until columns) {
            if (winner != "") {
                println("Player $winner won")
                if (winner == p1) p1Wins += 2 else p2Wins += 2
                winMessage()
                return
            }
            if (!board[0].contains(" ")) {
                println("It is a draw")
                p1Wins++
                p2Wins++
                winMessage()
                return
            }
            val input = board[row][col]
            if (input == " ") continue // skip if cell is blank
            if (col + 3 < columns &&
                input == board[row][col+1] && // check right
                input == board[row][col+2] &&
                input == board[row][col+3]
                ) winner = player
            if (row - 3 >= 0) {
                if (input == board[row - 1][col] && // check up
                    input == board[row - 2][col] &&
                    input == board[row - 3][col]
                ) winner = player
                if (col + 3 < columns &&
                    input == board[row - 1][col + 1] && // check up right
                    input == board[row - 2][col + 2] &&
                    input == board[row - 3][col + 3]
                ) winner = player
                if (col - 3 >= 0 &&
                    input == board[row - 1][col - 1] && // check up left
                    input == board[row - 2][col - 2] &&
                    input == board[row - 3][col - 3]
                ) winner = player
            } else continue
        }
    }
    return
}

fun multiGame () {
    while (true) {
        println(
            "Do you want to play single or multiple games?\n" +
                    "For a single game, input 1 or press Enter\n" +
                    "Input a number of games:"
        )
        var input = readln()
        if (input.isEmpty()) input = "1"
        if (input.toIntOrNull() == null) {
            println("Invalid input")
            continue
        }
        if (input.toInt() < 1) {
            println("Invalid input")
            continue
        }
        else {
            totalGames = input.toInt()
            println("$p1 VS $p2")
            println("$rows X $columns board")
            println(if (totalGames > 1)
                "Total $totalGames games" else "Single game")
        }
        return
    }
}

fun gameReset() {
    board = MutableList(rows) { MutableList(columns) {" "} }
    gameCount++
    player = if (gameCount % 2 == 0) p2 else p1
    marker = if (gameCount % 2 == 0) p2Marker else p1Marker
    printBoard()
    playerInput()
}

fun main() {
    println("Connect Four")
    playerNames()
    buildBoard()
    multiGame()
    printBoard()
    playerInput()
    return
}