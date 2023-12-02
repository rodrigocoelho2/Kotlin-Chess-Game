# Chess Board Game

Welcome to the Chess Board Game! This is a console-based chess game implemented in Kotlin. Players can choose to start a new game, enter their names, and play against each other on a customizable chessboard.

## Features

- **Customizable Chessboard:** Choose the number of columns and lines for the chessboard (minimum 4x4 and maximum 8x8).
- **Player Names:** Enter the names of the two players.
- **Show Legend:** Optionally display column and row labels on the chessboard.
- **Show Pieces:** Optionally display chess pieces on the board.
- **Piece Movements:** Valid movements for each type of chess piece are implemented.
- **Turn-Based Gameplay:** Players take turns making moves.
- **Game End:** The game ends when one player captures all the opponent's pieces.

## Instructions

1. **Build Menu:**
   ```kotlin
   fun buildMenu(): String {
       return "1-> Start New Game;\n2-> Exit Game.\n"
   }
   ```

2. **Coordinate System:**
   - Columns: A to H (from left to right)
   - Rows: 1 to 8 (from bottom to top)

3. **Piece Representation:**
   - Q: Queen
   - B: Bishop
   - T: Rook (Tower)
   - K: King
   - H: Knight
   - P: Pawn

4. **Gameplay:**
   - Players take turns making moves.
   - Enter the coordinates of the piece you want to move and the target coordinates.
   - Example: To move a piece at 2D to 4D, enter "2D" as the current coordinates and "4D" as the target coordinates.

5. **Legend and Pieces:**
   - Legend: Column and row labels are displayed by default.
   - Pieces: Chess pieces are not displayed by default. Enable the option to show pieces in the menu.

6. **Game End:**
   - The game ends when one player captures all the opponent's pieces.
   - The winning player is congratulated at the end.

## How to Play

1. Run the program.
2. Choose option 1 to start a new game.
3. Enter the names of the two players.
4. Configure the chessboard settings (columns, lines, legend, and pieces).
5. Follow the on-screen instructions to make moves.
6. The game ends when one player wins.

Enjoy the Chess Board Game!
