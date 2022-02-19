fun buildMenu(): String{
    return "1-> Start New Game;\n2-> Exit Game.\n"
}

fun invalid(): String{
    return "Invalid response."
}

fun indiceCoord(columns: Int, lines: Int, numColumns: Int): Int{
    return (columns-1) + (lines-1) * numColumns
}

fun checkIsNumber(number: String): Boolean {
    if (number == "") return false
    var count = 0

    while (count < number.length){
        if (number[count] !in ('0'..'9')) return false
        count ++
    }
    return true
}

fun checkName(number: String): Boolean{
    var position = 0
    var nEspacos = 0
    var temMaiuscula = false

    do{
        if(number[position] == ' ' && number[0] != ' '){
            nEspacos += 1
        }
        position++
    } while (position <= number.length - 1)
    position = 0

    do{
        if(number[0] in 'A'..'Z' && number[position + 1] in 'A'..'Z' &&  number[position] == ' '){
            temMaiuscula = true
        }
        position++
    }while (position < number.length - 1)

    return nEspacos >= 1 && temMaiuscula
}

fun showChessLegendOrPieces(message: String): Boolean? {

    return if (message.length == 1 && (message[0] == 'y' || message[0] == 'Y')) {
        true
    } else if (message.length == 1 && (message[0] == 'n' || message[0] == 'N')){
        false
    } else {
        null
    }
}

fun buildBoard(numColumns: Int, numLines: Int, showLegend: Boolean= false,showPieces: Boolean= false, pieces: Array<Pair<String,String>?>): String {
    val esc: String = 27.toChar().toString()

    val startBlue = "$esc[30;44m"
    val startGrey = "$esc[30;47m"
    val startWhite = "$esc[30;30m"
    val end = "$esc[0m"

    var chessBoard = ""

    if (showLegend) {
        chessBoard += "$startBlue   $end"
        var column = 1
        while (column <= numColumns){
            chessBoard += "$startBlue ${'A' + column - 1} $end"
            column++
        }
        chessBoard += "$startBlue   $end\n"
    }

    var line = 1
    while (line <= numLines) {
        if (showLegend) chessBoard += "$startBlue $line $end"
        var column = 1
        while (column <= numColumns) {
            var color : String
            var piece : String
            val cartoon = pieces[indiceCoord(column, line, numColumns)]

            if (cartoon == null){
                piece = ""
                color = ""
            } else {
                piece = cartoon.first
                color = cartoon.second
            }

            val finalPiece = convertStringToUnicode(piece, color)
            if ((line + column) % 2 == 0){
                if (showPieces){
                    chessBoard += "$startWhite $finalPiece $end"
                } else {
                    chessBoard += "$startWhite   $end"
                }
            } else {
                if (showPieces){
                    chessBoard += "$startGrey $finalPiece $end"
                } else {
                    chessBoard += "$startGrey   $end"
                }
            }

            column++
        }
        chessBoard += if (showLegend) "$startBlue   $end\n" else "\n"
        line++
    }

    if (showLegend) {
        var column = 1
        while (column <= numColumns + 2) {
            chessBoard += "$startBlue   $end"
            column++
        }
        chessBoard += "\n"
    }

    return chessBoard
}

fun convertStringToUnicode(piece: String, color: String): String {
    return when (Pair(piece,color)) {
        (Pair("Q","w")) -> "\u2655"
        (Pair("Q","b")) -> "\u265B"
        (Pair("B","w")) -> "\u2657"
        (Pair("B","b")) -> "\u265D"
        (Pair("T","w")) -> "\u2656"
        (Pair("T","b")) -> "\u265C"
        (Pair("K","w")) -> "\u2654"
        (Pair("K","b")) -> "\u265A"
        (Pair("H","w")) -> "\u2658"
        (Pair("H","b")) -> "\u265E"
        (Pair("P","w")) -> "\u2659"
        (Pair("P","b")) -> "\u265F"
        else -> " "
    }
}

fun isCoordinateInsideChess (coord: Pair<Int, Int>,numColumns: Int,numLines: Int):Boolean {
    val column = coord.first
    val line = coord.second

    when (numColumns*numLines){
        64 -> {
            if (column <= 8 && line <= 8 && column > 0 && line > 0){
                return true
            }
        }
        49 -> {
            if (column <= 7 && line <= 7 && column > 0 && line > 0){
                return true
            }
        }
        42 -> {
            if (column <= 7 && line <= 6 && column > 0 && line > 0){
                return true
            }
        }
        36 -> {
            if (column <= 6 && line <= 6 && column > 0 && line > 0){
                return true
            }
        }
        16 -> {
            if (column <= 4 && line <= 4 && column > 0 && line > 0){
                return true
            }
        }
        else -> return false
    }
    return false
}

fun checkRightPieceSelected(pieceColor: String, turn: Int): Boolean {
    return (pieceColor == "b" && turn == 1) || (pieceColor == "w" && turn == 0)
}

fun getCoordinates (readText: String?):Pair<Int, Int>? {
    val coord : Pair<Int, Int>?

    if (readText != null && (readText.length in 2..2)){
        val indice1 = readText[0].toString()
        var indice2 = readText[1].toString()

        when (indice2){
            "a", "A" -> indice2 = "1"
            "b", "B" -> indice2 = "2"
            "c", "C" -> indice2 = "3"
            "d", "D" -> indice2 = "4"
            "e", "E" -> indice2 = "5"
            "f", "F" -> indice2 = "6"
            "g", "G" -> indice2 = "7"
            "h", "H" -> indice2 = "8"

            else -> null
        }
        coord = Pair(indice1.toInt(), indice2.toInt())
        return coord
    }
    return null
}

fun createInitialBoard(numColumns: Int, numLines: Int): Array<Pair<String,String>?> {
    if (numLines == 4 && numColumns == 4){
        return arrayOf(null, null, Pair("T", "b"), Pair("B", "b"),
                null, null, null, null,
                null, null, null, null,
                Pair("T", "w"), Pair("Q", "w"), null, null)
    }
    else if (numLines == 6 && numColumns == 6){
        return arrayOf(Pair("H", "b"), Pair("B", "b"), Pair("Q", "b"), Pair("K", "b"), Pair("B", "b"), Pair("T", "b"),
                Pair("P", "b"), Pair("P", "b"), Pair("P", "b"), Pair("P", "b"), Pair("P", "b"), Pair("P", "b"),
                null, null, null, null, null, null,
                null, null, null, null, null, null,
                Pair("P", "w"), Pair("P", "w"), Pair("P", "w"), Pair("P", "w"), Pair("P", "w"), Pair("P", "w"),
                Pair("H", "w"), Pair("B", "w"), Pair("K", "w"), Pair("Q", "w"), Pair("B", "w"), Pair("T", "w"))

    }
    else if (numLines == 7 && numColumns == 6){
        return arrayOf(Pair("T", "b"), Pair("B", "b"), Pair("Q", "b"), Pair("K", "b"), Pair("B", "b"), Pair("H", "b"),
                Pair("P", "b"), Pair("P", "b"), Pair("P", "b"), Pair("P", "b"), Pair("P", "b"), Pair("P", "b"),
                null, null, null, null, null, null,
                null, null, null, null, null, null,
                null, null, null, null, null, null,
                Pair("P", "w"), Pair("P", "w"), Pair("P", "w"), Pair("P", "w"), Pair("P", "w"), Pair("P", "w"),
                Pair("T", "w"), Pair("B", "w"), Pair("K", "w"), Pair("Q", "w"), Pair("B", "w"), Pair("H", "w"))
    }
    else if (numLines == 7 && numColumns == 7){
        return arrayOf(Pair("T", "b"), Pair("H", "b"), Pair("B", "b"), Pair("K", "b"), Pair("B", "b"), Pair("H", "b"), Pair("T", "b"),
                Pair("P", "b"), Pair("P", "b"), Pair("P", "b"), Pair("P", "b"), Pair("P", "b"), Pair("P", "b"), Pair("P", "b"),
                null, null, null, null, null, null, null,
                null, null, null, null, null, null, null,
                null, null, null, null, null, null, null,
                Pair("P", "w"), Pair("P", "w"), Pair("P", "w"), Pair("P", "w"), Pair("P", "w"), Pair("P", "w"), Pair("P", "w"),
                Pair("T", "w"), Pair("H", "w"), Pair("B", "w"), Pair("K", "w"), Pair("B", "w"), Pair("H", "w"), Pair("T", "w"))
    }
    else if (numLines == 8 && numColumns == 8){
        return arrayOf(Pair("T", "b"), Pair("H", "b"), Pair("B", "b"), Pair("Q", "b"), Pair("K", "b"), Pair("B", "b"), Pair("H", "b"), Pair("T", "b"),
               Pair("P", "b"), Pair("P", "b"), Pair("P", "b"), Pair("P", "b"), Pair("P", "b"), Pair("P", "b"), Pair("P", "b"), Pair("P", "b"),
                null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null,
                Pair("P", "w"), Pair("P", "w"), Pair("P", "w"), Pair("P", "w"), Pair("P", "w"), Pair("P", "w"), Pair("P", "w"), Pair("P", "w"),
                Pair("T", "w"), Pair("H", "w"), Pair("B", "w"), Pair("K", "w"), Pair("Q", "w"), Pair("B", "w"), Pair("H", "w"), Pair("T", "w"))
    }
    else return arrayOf()
}

fun createTotalPiecesAndTurn(numColumns: Int, numLines: Int): Array<Int?> {
    val storage: Array<Int?>

    if (numColumns in 5..8 && numLines in 5..8){
        val totalPiecesBlack = numColumns * 2
        val totalPiecesWhite = numColumns * 2
        storage = arrayOf(totalPiecesWhite, totalPiecesBlack, 0)
        return storage

    } else if  (numColumns == 4 && numLines == 4){
        storage = arrayOf(2,2,0)
        return storage

    } else {
        storage = arrayOf()
        return storage
    }
}

fun friendlyTeam (pieces: Array<Pair<String, String>?>, currentCoord: Pair<Int, Int>, targetCoord: Pair<Int, Int>, numColumns: Int  ): Boolean{
    val indice = indiceCoord(currentCoord.second, currentCoord.first, numColumns)
    val targetIndice = indiceCoord(targetCoord.second, targetCoord.first, numColumns)
    val currentPiece = pieces[indice] ?: Pair("", "")
    val currentTargetPiece = pieces[targetIndice] ?: Pair("", "")

    return currentTargetPiece.second != currentPiece.second
}

fun movePiece( pieces : Array<Pair<String, String>?>, numColumns: Int,
               numLines: Int, currentCoord: Pair<Int, Int>, targetCoord: Pair<Int, Int>,
               totalPiecesAndTurn : Array<Int>): Boolean{
    val currentPieceIndex = indiceCoord(currentCoord.second, currentCoord.first, numColumns)
    val currentSelectedPiece = pieces[currentPieceIndex] ?: Pair("", "")
    val currentTargetIndex = indiceCoord(targetCoord.second, targetCoord.first, numColumns)
    val currentSelectedTarget = pieces[currentTargetIndex] ?: Pair("", "")

    if (isValidTargetPiece(currentSelectedPiece, currentCoord, targetCoord, pieces, numColumns, numLines)){
        if (totalPiecesAndTurn[2] == 0){
            if (pieces[currentTargetIndex] == null){
                totalPiecesAndTurn[2] = 1
                pieces[currentTargetIndex] = pieces[currentPieceIndex]
                pieces[currentPieceIndex] = null
                return true
            } else {
                totalPiecesAndTurn[2] = 1
                totalPiecesAndTurn[1] -= 1
                pieces[currentTargetIndex] = pieces[currentPieceIndex]
                pieces[currentPieceIndex] = null
            }
        } else {
            if (pieces[currentTargetIndex] == null){
                totalPiecesAndTurn[2] = 0
                pieces[currentTargetIndex] = pieces[currentPieceIndex]
                pieces[currentPieceIndex] = null

                return true
            } else {
                totalPiecesAndTurn[2] = 0
                totalPiecesAndTurn[0] -= 1
                pieces[currentTargetIndex] = pieces[currentPieceIndex]
                pieces[currentPieceIndex] = null
            }
        }
    }
    return false
}

fun startNewGame (whitePlayer: String, blackPlayer: String, pieces : Array<Pair<String, String>?>,
                  totalPiecesAndTurn : Array<Int?>, numColumns: Int,numLines: Int,
                  showLegend: Boolean= false, showPieces: Boolean = false){
    val totalPieceAndTurn = createTotalPiecesAndTurn(numColumns, numLines)
    var targetCoord : Pair<Int, Int>?
    var currentCoord : Pair<Int, Int>?
    var currentCoord2 : Pair<Int, Int>
    var currentCoord3 : Int
    var currentSelectedPiece = Pair("", "")
    val totalPiecesAndTurn2 = arrayOf(0, 0, 0)

            for(count in 0..2){
        val index = totalPieceAndTurn[count]
        totalPiecesAndTurn2 [count] = index!!
    }

    do {
        if (totalPiecesAndTurn2[2] == 0) {
            do{
                do {
                    do {
                        println(buildBoard(numColumns, numLines, showLegend, showPieces, pieces))
                        println("$whitePlayer, choose a piece (e.g 2D).\nMenu-> m;")
                        val option = readLine()
                        if (option == "m") return

                        currentCoord = getCoordinates(option)
                        currentCoord2 = currentCoord ?: Pair(1, 1)
                        currentCoord3 = indiceCoord(currentCoord2.second, currentCoord2.first, numColumns)
                        currentSelectedPiece = pieces[currentCoord3] ?: Pair("", "")

                        if (currentCoord == null) println(invalid())

                    } while (currentCoord == null)

                    if(!checkRightPieceSelected(currentSelectedPiece.second, totalPiecesAndTurn2[2])) println(invalid())

                } while (!checkRightPieceSelected(currentSelectedPiece.second, totalPiecesAndTurn2[2]))

                    println("$whitePlayer, choose a target piece (e.g 2D).\nMenu-> m;")
                    val option = readLine()
                    targetCoord = getCoordinates(option)

                    if (targetCoord == null) println(invalid())

                } while (targetCoord == null)

            movePiece(pieces, numColumns, numLines, currentCoord2, targetCoord, totalPiecesAndTurn2)

        } else {

            do {
                do{

                    do {
                        println(buildBoard(numColumns, numLines, showLegend, showPieces, pieces))
                        println("$blackPlayer, choose a piece (e.g 2D).\nMenu-> m;")
                        val option = readLine()
                        if (option == "m") return

                        currentCoord = getCoordinates(option)
                        currentCoord2 = currentCoord ?: Pair(1, 1)
                        currentCoord3 = indiceCoord(currentCoord2.second, currentCoord2.first, numColumns)
                        currentSelectedPiece = pieces[currentCoord3] ?: Pair("", "")

                        if (currentCoord == null) println(invalid())

                    } while (currentCoord == null)

                    if(!checkRightPieceSelected(currentSelectedPiece.second, totalPiecesAndTurn2[2])) println(invalid())

                } while (!checkRightPieceSelected(currentSelectedPiece.second, totalPiecesAndTurn2[2]))

                println("$blackPlayer, choose a target piece (e.g 2D).\nMenu-> m;")
                val option = readLine()
                targetCoord = getCoordinates(option)

                if (targetCoord == null) println(invalid())

            } while (targetCoord == null)

            movePiece(pieces, numColumns, numLines, currentCoord2, targetCoord, totalPiecesAndTurn2)
        }

    } while (totalPieceAndTurn[0] != 0 || totalPieceAndTurn[1] != 0)

    if (totalPieceAndTurn[1] == 0) { println("Congrats! $blackPlayer wins!")
    } else if (totalPieceAndTurn[0] == 0) { println("Congrats! $whitePlayer wins!")}

}

fun isValidTargetPiece(currentSelectedPiece: Pair<String, String>,currentCoord : Pair<Int, Int>,
                       targetCoord : Pair<Int, Int>, pieces : Array<Pair<String, String>?>, numColumns: Int,
                       numLines: Int): Boolean {
    val pieceType = currentSelectedPiece.first

    return when (pieceType) {
        "P" -> isKnightValid(currentCoord, targetCoord, pieces, numColumns, numLines)
        "Q" -> isQueenValid(currentCoord, targetCoord, pieces, numColumns, numLines)
        "K" -> isKingValid(currentCoord, targetCoord, pieces, numColumns, numLines)
        "B" -> isBishopValid(currentCoord, targetCoord, pieces, numColumns, numLines)
        "H" -> isHorseValid(currentCoord, targetCoord, pieces, numColumns, numLines)
        "T" -> isTowerValid(currentCoord, targetCoord, pieces, numColumns, numLines)

        else -> false
    }
}

fun isKnightValid(currentCoord: Pair<Int, Int>,targetCoord: Pair<Int, Int>,
                  pieces: Array<Pair<String, String>?>,numColumns: Int,numLines: Int):Boolean {
    if (isCoordinateInsideChess(targetCoord, numColumns, numLines) && friendlyTeam(pieces, currentCoord, targetCoord, numColumns)) {
        val currentCoordFirst = currentCoord.first
        val currentCoordSecond = currentCoord.second
        val validMoveFront = Pair(currentCoordFirst + 1, currentCoordSecond)
        val validMoveBack = Pair(currentCoordFirst - 1, currentCoordSecond)

        return ((validMoveFront == targetCoord) || (validMoveBack == targetCoord))
    }
    return false
}

fun isTowerValid(currentCoord: Pair<Int, Int>,targetCoord: Pair<Int, Int>,
                 pieces: Array<Pair<String, String>?>,numColumns: Int,numLines: Int):Boolean {
    if (isCoordinateInsideChess(targetCoord, numColumns, numLines) && friendlyTeam(pieces, currentCoord, targetCoord, numColumns)) {

        val currentCoordFirst = currentCoord.first
        val currentCoordSecond = currentCoord.second
        val validMoveFront = Pair(targetCoord.first, currentCoordSecond)
        val validMoveSide = Pair(currentCoordFirst, targetCoord.second)

        return ((validMoveFront == targetCoord) || (validMoveSide == targetCoord))
    }
    return false
}

fun isHorseValid(currentCoord: Pair<Int, Int>,targetCoord : Pair<Int, Int>,
                 pieces : Array<Pair<String, String>?>,numColumns: Int, numLines: Int): Boolean{
    if (isCoordinateInsideChess(targetCoord, numColumns, numLines) && friendlyTeam(pieces, currentCoord, targetCoord, numColumns)) {
        val currentCoordFirst = currentCoord.first
        val currentCoordSecond = currentCoord.second
        val validMove1 = Pair(currentCoordFirst + 2, currentCoordSecond + 1)
        val validMove2 = Pair(currentCoordFirst + 2, currentCoordSecond - 1)
        val validMove3 = Pair(currentCoordFirst - 2, currentCoordSecond + 1)
        val validMove4 = Pair(currentCoordFirst - 2, currentCoordSecond - 1)
        val validMove5 = Pair(currentCoordFirst + 1, currentCoordSecond + 2)
        val validMove6 = Pair(currentCoordFirst + 1, currentCoordSecond - 2)
        val validMove7 = Pair(currentCoordFirst - 1, currentCoordSecond + 2)
        val validMove8 = Pair(currentCoordFirst - 1, currentCoordSecond - 2)

        return  ((validMove1 == targetCoord) || (validMove2 == targetCoord) || (validMove3 == targetCoord) ||
                 (validMove4 == targetCoord) || (validMove5 == targetCoord) || (validMove6 == targetCoord) ||
                 (validMove7 == targetCoord) || (validMove8 == targetCoord))
    }
    return false
}

fun isKingValid(currentCoord: Pair<Int, Int>,targetCoord : Pair<Int, Int>,
                pieces: Array<Pair<String, String>?>,numColumns: Int,numLines: Int):Boolean {
    if (isCoordinateInsideChess(targetCoord, numColumns, numLines) && friendlyTeam(pieces, currentCoord, targetCoord, numColumns)) {
        val currentCoordFirst = currentCoord.first
        val currentCoordSecond = currentCoord.second
        val validMoveFront = Pair(currentCoordFirst + 1, currentCoordSecond)
        val validMoveBack = Pair(currentCoordFirst - 1, currentCoordSecond)
        val validMoveSide1 = Pair(currentCoordFirst, currentCoordSecond + 1)
        val validMoveSide2 = Pair(currentCoordFirst, currentCoordSecond - 1)
        val validMoveDia1 = Pair(currentCoordFirst - 1, currentCoordSecond + 1)
        val validMoveDia2 = Pair(currentCoordFirst - 1, currentCoordSecond - 1)
        val validMoveDia3 = Pair(currentCoordFirst + 1, currentCoordSecond - 1)
        val validMoveDia4 = Pair(currentCoordFirst + 1, currentCoordSecond + 1)

        return ((validMoveFront == targetCoord) || (validMoveBack == targetCoord) || (validMoveSide1 == targetCoord) ||
                (validMoveSide2 == targetCoord) || (validMoveDia1 == targetCoord) || (validMoveDia2 == targetCoord) ||
                (validMoveDia3 == targetCoord) || (validMoveDia4 == targetCoord))
    }
    return false
}

fun isQueenValid(currentCoord: Pair<Int, Int>,targetCoord: Pair<Int, Int>,
                 pieces: Array<Pair<String, String>?>,numColumns: Int,numLines: Int):Boolean{
    if (isCoordinateInsideChess(targetCoord, numColumns, numLines) && friendlyTeam(pieces, currentCoord, targetCoord, numColumns)) {
        val currentCoordFirst = currentCoord.first
        val currentCoordSecond = currentCoord.second
        var count = 0

        while ((currentCoordFirst + count in 1..8) && (currentCoordSecond + count in 1..8)){
            count ++
            val validMove = Pair(currentCoordFirst + count, currentCoordSecond + count)

            if (targetCoord == validMove){
                return targetCoord == validMove
            }
        }
        count = 0
        while ((currentCoordFirst - count in 1..8) && (currentCoordSecond + count in 1..8)){
            count ++
            val validMove = Pair(currentCoordFirst - count, currentCoordSecond + count)

            if (targetCoord == validMove){
                return targetCoord == validMove
            }
        }
        count = 0
        while ((currentCoordFirst + count in 1..8) && (currentCoordSecond - count in 1..8)){
            count ++
            val validMove = Pair(currentCoordFirst + count, currentCoordSecond - count)

            if (targetCoord == validMove){
                return targetCoord == validMove
            }
        }
        count = 0
        while ((currentCoordFirst - count in 1..8) && (currentCoordSecond - count in 1..8)){
            count ++
            val validMove = Pair(currentCoordFirst - count, currentCoordSecond - count)

            if (targetCoord == validMove){
                return targetCoord == validMove
            }
        }
        val validMoveFront = Pair(targetCoord.first, currentCoordSecond)
        val validMoveSide = Pair(currentCoordFirst, targetCoord.second)

        return ((validMoveFront == targetCoord) || (validMoveSide == targetCoord))
    }
    return false
}

fun isBishopValid(currentCoord: Pair<Int, Int>,targetCoord: Pair<Int, Int>,
                  pieces: Array<Pair<String, String>?>,numColumns: Int,numLines: Int): Boolean{
    if (isCoordinateInsideChess(targetCoord, numColumns, numLines) && friendlyTeam(pieces, currentCoord, targetCoord, numColumns)) {
            val currentCoordFirst = currentCoord.first
            val currentCoordSecond = currentCoord.second
            var count = 0

        while ((currentCoordFirst + count in 1..8) && (currentCoordSecond + count in 1..8)){
            count ++
            val validMove = Pair(currentCoordFirst + count, currentCoordSecond + count)

            if (targetCoord == validMove){
                return targetCoord == validMove
            }
        }
            count = 0
        while ((currentCoordFirst - count in 1..8) && (currentCoordSecond + count in 1..8)){
            count ++
            val validMove = Pair(currentCoordFirst - count, currentCoordSecond + count)

            if (targetCoord == validMove){
                return targetCoord == validMove
            }
        }
            count = 0
        while ((currentCoordFirst + count in 1..8) && (currentCoordSecond - count in 1..8)){
            count ++
            val validMove = Pair(currentCoordFirst + count, currentCoordSecond - count)

            if (targetCoord == validMove){
                return targetCoord == validMove
            }
        }
        count = 0
        while ((currentCoordFirst - count in 1..8) && (currentCoordSecond - count in 1..8)){
            count ++
            val validMove = Pair(currentCoordFirst - count, currentCoordSecond - count)

            if (targetCoord == validMove){
                return targetCoord == validMove
            }
        }
    }
    return false
}

fun validBoard(numColumns: Int, numLines: Int): Boolean{
    when (numColumns*numLines){
        64 -> {
                return true
        }
        49 -> {
                return true
        }
        42 -> {
                return true
        }
        36 -> {
                return true
        }
        16 -> {
                return true
        }
        else -> return false
    }
}

fun main() {

    println("Welcome to the Chess Board Game!")
    do {
        /*Menu Pincipal*/
        var escolha: Int? = 1
        var nomeJogador1 = ""
        var nomeJogador2 = ""

        do {
            if (escolha != 1 && escolha != 2) {
                println(invalid())
            }

            println(buildMenu())
            escolha = readLine()!!.toIntOrNull()     /*1 = Começar o Jogo | 0 = Sair do Jogo*/
        } while (escolha != 1 && escolha != 2)


        /*Escolha do Menu*/
        if (escolha == 1) {
            /*Início do Jogo*/
            /*Primeiro Jogador*/
            do {
                println("First player name?\n")

                nomeJogador1= readLine()!!.toString()

                if (!checkName(nomeJogador1)) {
                    println(invalid())
                }
            } while (!checkName(nomeJogador1))

            /*Segundo Jogador*/
            do {
                println("Second player name?\n")

                nomeJogador2= readLine()!!.toString()

                if (!checkName(nomeJogador2)) {
                    println(invalid())
                }
            } while (!checkName(nomeJogador2))

        } else if (escolha == 2) {
            /*Encerra o Programa*/
            return
        }

        /*Escolha do Tabuleiro*/
            /*Escolha Default*/
        var numColumns: Int
        var numLines: Int
        var showLegend: Boolean?
        var showPieces: Boolean?

            do {
                do {
                    do{
                        println("How many chess columns?\n")
                        numColumns = readLine()!!.toInt()
                            if (!checkIsNumber(numColumns.toString()) && numColumns in 4..8) println(invalid())

                    } while (!checkIsNumber(numColumns.toString()) && numColumns in 4..8)

                    println("How many chess lines?\n")
                    numLines = readLine()!!.toInt()

                    if (!checkIsNumber(numLines.toString()) && numLines in 4..8) println(invalid())

                } while (!checkIsNumber(numLines.toString()) && numLines in 4..8)

                if (!validBoard(numColumns, numLines)){
                    println(invalid())
                }

            } while (!validBoard(numColumns, numLines))

        do {
            println("Show legend (y/n)?\n")
            showLegend = showChessLegendOrPieces(readLine()!!.toString())

            if (showLegend == null) println(invalid())

        } while (showLegend != true && showLegend != false)

        do {
            println("Show pieces (y/n)?\n")
            showPieces = showChessLegendOrPieces(readLine()!!.toString())

            if (showPieces == null) println(invalid())

        } while (showPieces != true && showPieces != false)

        /*Impressão do Tabuleiro*/
        val pieces = createInitialBoard(numColumns, numLines)
        val totalPiecesAndTurn = createTotalPiecesAndTurn(numColumns, numLines)
        startNewGame(nomeJogador1, nomeJogador2, pieces, totalPiecesAndTurn, numColumns, numLines, showLegend, showPieces)

        /*Loop Infinito do Jogo*/
        val loop = 1
    } while (loop != 0)
}