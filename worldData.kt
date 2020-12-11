class Cell( val xCords: Int, val yCords: Int, var alive: Boolean = false) {
    var nextState: Int? = null
    var adjCells: ArrayList<Cell>? = null
 fun toChar(): Char { return if (this.alive) '*' else ' ' }
}

class World(private val width: Int, private val height: Int) {
    class LocationOccupied: Exception() // safety measure

    var refresh = 0
    private val cells = HashMap<String, Cell>()
    private val checkAdjDirs: Array<IntArray> = arrayOf(
            intArrayOf(-1, 1),  intArrayOf(0, 1),  intArrayOf(1, 1), // Top (X = -1/0/+1, Y = +1)
            intArrayOf(-1, 0),                     intArrayOf(1, 0), // look left & Look right (X = =1/+1, Y= 0)
            intArrayOf(-1, -1), intArrayOf(0, -1), intArrayOf(1, -1) // Down (X = -1/0/+1, Y = -1)
    )
    init {
        initCells()
        showAllNeighbours()
    }

    private fun initCells() {
        for (Col in 0 until height) {
            for (Row in 0 until width) {
                val alive = (Math.random() <= 0.40)   // % of cells that will be Alive
                addCell(Row, Col, alive)    // add the cells to the world
            }
        }
    }

    private fun addCell(Row: Int, Col: Int, alive: Boolean = false): Cell {
        if (cellLoc(Row, Col) != null) { throw LocationOccupied()  }  // Throw safety exception
            val cell = Cell(Row, Col, alive)
            cells["$Row-$Col"] = cell
            return cellLoc(Row, Col)!!  // !! - value expected is !null
    }

    private fun cellLoc(Row: Int, Col: Int): Cell? {
        return cells["$Row-$Col"]
    }

    fun checkWorld() {
        for ((_, cell) in cells) {
            val aliveNeighbours = isOccupied(cell)  // check if each cell has occupied neighbouring cells
            if ((!(cell.alive)) && aliveNeighbours == 3) { cell.nextState = 1 } // checking conditions
            else if (aliveNeighbours < 2 || aliveNeighbours > 3) { cell.nextState = 0 } // checking conditions
        }
        for ((_, cell) in cells) {
            if (cell.nextState == 1) { cell.alive = true }  // set the cell state for next iteration of game
            else if (cell.nextState == 0) { cell.alive = false }
        }
        refresh += 1
    }

    fun render(): String {
        val rendering = StringBuilder() // use StringBuilder to manipulate cellLoc val as string output
        for (Col in 0 until height) {
            for (Row in 0 until width) {
                val cell = cellLoc(Row, Col)!!  // expect non-null val
                rendering.append(cell.toChar())
            }
            rendering.append("\n")
        }
        return "$rendering"
    }

    private fun isOccupied(cell: Cell): Int {
        var occupied = 0
        val neighbours = neighbourList(cell)
        for (i in 0 until neighbours.size) {    // iterate through list of neighbours
            val neighbour = neighbours[i]
            if (neighbour.alive) { occupied += 1 }  // increment if cell has neighbour
        }
        return occupied
    }

    private fun showAllNeighbours() {
        for ((_, cell) in cells) {
            neighbourList(cell)
        }
    }

    private fun neighbourList(cell: Cell): ArrayList<Cell> {
        if (cell.adjCells == null) { // Only if there are none
            cell.adjCells = ArrayList()
            for ((rel_x, rel_y) in checkAdjDirs) { // check all directions
                val neighbour = cellLoc( (cell.xCords + rel_x), (cell.yCords + rel_y) ) // assign to (new) cell
                if (neighbour != null) { cell.adjCells!!.add(neighbour) }   // add a new neighbour in the list
            }
        }
        return cell.adjCells!!
    }
}

