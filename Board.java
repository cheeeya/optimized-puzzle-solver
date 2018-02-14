import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class Board {
	private int myYLength;
	private int myXLength;
	private Piece[] myPieces; // Piece[y][x]
	private int[] myBoard;
	private int[] moveMade;
	private Board parent;
	private ArrayList<Board> possibleMoves;
	private ArrayList<Piece> pieces;

	public Board(int ylength, int xlength) {
		myYLength = ylength;
		myXLength = xlength;
		myPieces = new Piece[ylength * xlength];
		myBoard = new int[ylength * xlength];
		moveMade = null;
		parent = null;
		possibleMoves = new ArrayList<Board>();
		pieces = new ArrayList<Piece>();
	}

	public Board(int ylength, int xlength, int[] move, Board p) {
		myYLength = ylength;
		myXLength = xlength;
		myPieces = new Piece[ylength * xlength];
		myBoard = new int[ylength * xlength];
		moveMade = move;
		parent = p;
		possibleMoves = new ArrayList<Board>();
		pieces = new ArrayList<Piece>();
	}

	public void add(int[] coordinates) {
		if (coordinates[0] < 0 || coordinates[1] < 0 || coordinates[2] < 0
				|| coordinates[3] < 0 || coordinates[0] >= myYLength
				|| coordinates[2] >= myYLength || coordinates[1] >= myXLength
				|| coordinates[3] >= myXLength) {
			throw new IllegalArgumentException();
		}
		int y = coordinates[0];
		int x = coordinates[1];
		Piece p = new Piece(coordinates);
		pieces.add(p);
		for (int i = y; i < coordinates[2] + 1; i++) {
			for (int j = x; j < coordinates[3] + 1; j++) {
				myPieces[j + (i * myXLength)] = p;
				myBoard[j + (i * myXLength)] = p.getX() * p.getY() * p.height() * p.width();
			}
		}
	}

	public int[] getMoveMade() {
		return moveMade;
	}

	public Piece[] getPieces() {
		return myPieces;
	}

	public Piece getPiece(int index) {
		return myPieces[index];
	}
	
	public Piece getPiece2(int y, int x){
		return myPieces[x + (y * myXLength)];
	}
	
	public int[] getBoard(){
		return myBoard;
	}

	public Board getParent() {
		return parent;
	}

	public ArrayList<Board> neighbors() {
		return possibleMoves;
	}
	
	public ArrayList<Piece> listPieces() {
		return pieces;
	}

	public boolean boardMatch(Board goal) {
		HashSet<Piece> visited = new HashSet<Piece>();
		for (int i = 0; i < myYLength * myXLength; i++) {
			if (goal.getPiece(i) != null && !visited.contains(goal.getPiece(i))) {
				if (!goal.getPiece(i).equals(getPiece(i))) {
					return false;
				}
				visited.add(goal.getPiece(i));
			}
		}
		return true;
	}

	public void getMoves() {
		for (Piece p : pieces) {
			if (canMoveUp(p)) {
				possibleMoves.add(moveUp(p));
			}
			if (canMoveDown(p)) {
				possibleMoves.add(moveDown(p));
			}
			if (canMoveLeft(p)) {
				possibleMoves.add(moveLeft(p));
			}
			if (canMoveRight(p)) {
				possibleMoves.add(moveRight(p));
			}
		}
	}

	public Board moveUp(Piece p) {
		int[] coords = p.getCoords();
		int[] newCoords = new int[4];
		newCoords[0] = coords[0] - 1;
		newCoords[1] = coords[1];
		newCoords[2] = coords[2] - 1;
		newCoords[3] = coords[3];
		int[] move = { coords[0], coords[1], newCoords[0], newCoords[1] };
		return move(p, newCoords, move);
	}

	public Board moveDown(Piece p) {
		int[] coords = p.getCoords();
		int[] newCoords = new int[4];
		newCoords[0] = coords[0] + 1;
		newCoords[1] = coords[1];
		newCoords[2] = coords[2] + 1;
		newCoords[3] = coords[3];
		int[] move = { coords[0], coords[1], newCoords[0], newCoords[1] };
		return move(p, newCoords, move);
	}

	public Board moveLeft(Piece p) {
		int[] coords = p.getCoords();
		int[] newCoords = new int[4];
		newCoords[0] = coords[0];
		newCoords[1] = coords[1] - 1;
		newCoords[2] = coords[2];
		newCoords[3] = coords[3] - 1;
		int[] move = { coords[0], coords[1], newCoords[0], newCoords[1] };
		return move(p, newCoords, move);
	}

	public Board moveRight(Piece p) {
		int[] coords = p.getCoords();
		int[] newCoords = new int[4];
		newCoords[0] = coords[0];
		newCoords[1] = coords[1] + 1;
		newCoords[2] = coords[2];
		newCoords[3] = coords[3] + 1;
		int[] move = { coords[0], coords[1], newCoords[0], newCoords[1] };
		return move(p, newCoords, move);
	}

	private Board move(Piece p, int[] coordinates, int[] move) {
		Board afterMove = new Board(myYLength, myXLength, move, this);
		for (Piece piece : pieces) {
			if (piece == p) {
				afterMove.add(coordinates);
			} else {
				afterMove.add(piece.getCoords());
			}
		}
		return afterMove;
	}

	public boolean canMoveUp(Piece p) {
		int x = p.getX();
		int y = p.getY();
		if (y - 1 >= 0) {
			for (int i = 0; i < p.width(); i++) {
				if (myPieces[x + i + ((y - 1) * myXLength)] != null) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

	public boolean canMoveDown(Piece p) {
		int x = p.getX();
		int y = p.getY();
		if (y + p.height() < myYLength) {
			for (int i = 0; i < p.width(); i++) {
				if (myPieces[x + i + ((y + p.height()) * myXLength)] != null) {
					return false;
				}
			}
			return true;

		}
		return false;
	}

	public boolean canMoveLeft(Piece p) {
		int x = p.getX();
		int y = p.getY();
		if (x - 1 >= 0) {
			for (int i = 0; i < p.height(); i++) {
				if (myPieces[x - 1 + ((y + i) * myXLength)] != null) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

	public boolean canMoveRight(Piece p) {
		int x = p.getX();
		int y = p.getY();
		if (x + p.width() < myXLength) {
			for (int i = 0; i < p.height(); i++) {
				if (myPieces[x + p.width() + ((y + i) * myXLength)] != null) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Arrays.hashCode(myBoard);
	}

	@Override
	public boolean equals(Object o) {
		Board b = (Board) o;
		HashSet<Piece> visited = new HashSet<Piece>();
		for (int i = 0; i < myYLength * myXLength; i++) {
			if (getPiece(i) != null && !visited.contains(getPiece(i))) {
				if (!getPiece(i).equals(b.getPiece(i))) {
					return false;
				}
				visited.add(getPiece(i));
			}
		}
		return true;
	}

	private class Piece {
		private int[] myCoords;

		public Piece(int[] coordinates) {
			myCoords = coordinates;
		}

		public int height() {
			return myCoords[2] - myCoords[0] + 1;
		}

		public int width() {
			return myCoords[3] - myCoords[1] + 1;
		}

		public int getY() {
			return myCoords[0];
		}

		public int getX() {
			return myCoords[1];
		}

		public int[] getCoords() {
			return myCoords;
		}
		

		@Override
		public int hashCode() {
			StringBuilder hash = new StringBuilder("");
			hash.append(getY());
			hash.append(height());
			hash.append(getX());
			hash.append(width());
			return Integer.parseInt(hash.toString());
		}

		@Override
		public boolean equals(Object o) {
			Piece p = (Piece) o;
			if (p == null) {
				return false;
			}
			return Arrays.equals(myCoords, p.getCoords());
		}
	}
}