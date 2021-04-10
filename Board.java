import java.util.ArrayList;

public class Board{
	/*----------------INITIALISIATIONS-------------------*/
	public static final int X = 1;
	public static final int O = -1;
	public static final int EMPTY = 0;

	private Move lastMove;//last move that lead to this board
	private int lastLetterPlayed;
	private int[][] gameBoard;
	private ArrayList<Move> validMoves;
	/*---------------END INITIALISIATIONS----------------*/
	




	/*---------------CONSTRUCTORS----------------*/
    public Board(){
		validMoves=new ArrayList<Move>();
		lastMove=new Move();
		lastLetterPlayed = O;
        gameBoard=new int[8][8];
        for(int i=0; i<8; i++)
		{
			for(int j=0; j<8; j++)
			{	
				//an eimaste sto kentro tou tamplo, topothethse ta xrwmatismena diskia
				if(i==3 & j==3|| i==4 & j==4)
					gameBoard[i][j]=X;
				else if(i==3 & j==4|| i==4 & j==3)
					gameBoard[i][j]=O;
				else
					gameBoard[i][j]=EMPTY;
				
			}
		}
	}

	public Board(Board board)
	{	
		validMoves=new ArrayList<Move>();
		lastMove = board.lastMove;
		lastLetterPlayed = board.lastLetterPlayed;
		gameBoard = new int[8][8];
		for(int i=0; i<8; i++)
		{
			for(int j=0; j<8; j++)
			{
				gameBoard[i][j] = board.gameBoard[i][j];
			}
		}
	}
	/*---------------END CONSTRUCTORS----------------*/





	/*-----------------GETTERS AND SETTERS-----------------*/
	public Move getLastMove()
	{
		return lastMove;
	}
	public int getLastLetterPlayed()
	{
		return lastLetterPlayed;
	}	
	public int[][] getGameBoard()
	{
		return gameBoard;
	}

	public void setLastMove(Move lastMove)
	{
		this.lastMove.setRow(lastMove.getRow());
		this.lastMove.setCol(lastMove.getCol());
		this.lastMove.setValue(lastMove.getValue());
	}
	
	public void setLastLetterPlayed(int lastLetterPlayed)
	{
		this.lastLetterPlayed = lastLetterPlayed;
	}
	
	public void setGameBoard(int[][] gameBoard)
	{
		for(int i=0; i<8; i++)
		{
			for(int j=0; j<8; j++)
			{
				this.gameBoard[i][j] = gameBoard[i][j];
			}
		}
	}
	/*---------------END GETTERS AND SETTERS----------------*/






	/*-------------------BOARD FUNCTIONS--------------------*/
	//prints the result of the game
	public void gameAnalysis(){
		int countX = 0, countO = 0;
		for(int row=0; row<8; row++){
			for(int col=0; col<8; col++){
				if(gameBoard[row][col] == O)
					countO++;
				if(gameBoard[row][col] == X)
					countX++;				
			}
		}
		if(countX>countO)
			System.out.println("Congratulations X, you are the big winner!");
		else if(countX<countO)
			System.out.println("Congratulations O, you are the big winner!");
		else
			System.out.println("Sorry guys, none of you managed to win! You ended with a tie :(");
		System.out.println("Game ended with a total of:\t"+countX+"  X-tiles\t and "+countO+"  O-tiles" );
	}

	//Make a move; it places a letter in the board
	public boolean makeMove(int row, int col, int letter)
	{	
		lastLetterPlayed = letter;
		if(letter==Board.O)validMoves.clear();//empty the list of valid moves
		if(isValid(row, col, letter)){
			for(Move m:validMoves)
				makeLines(m.getRow()-row,m.getRow(),m.getCol()-col, m.getCol(), letter);

			gameBoard[row][col] = letter;
			lastMove = new Move(row, col);
			validMoves.clear();//empty the list of valid moves
			return true;
		}else{			
			return false;
		}
	}

    //Checks whether a move is valid; whether a square is empty
	public boolean isValid(int row, int col, int symbol)
	{
		
		//invalid an vriskomaste ektos oriwn pinaka diskiwn
		if ((row <= -1) || (col <= -1) || (row > 7) || (col > 7))
			return false;

		//invalid an to diskio einai keno giati den tha apotelei kinhsh
		if(gameBoard[row][col] != EMPTY)
			return false;
			
		//invalid an den uparxoun geitonikes theseis antithetou xrwmatos
		ArrayList<Move> opposites=findOpposites(row, col, symbol);
		if (opposites.isEmpty()) 
			return false;

			
		/*
		* invalid an den sxhmatizetai grammh metaxu tou tile pou prosthetoume
		* kai enos uparxontos idiou xrwmmatos sthn idia grammh 
		*/
		int countInvalid=0;
		int dRow, dCol;
		for(Move m:opposites){
			dRow=m.getRow()-row;
			dCol=m.getCol()-col;
			if(!checkLine(dRow,m.getRow(),dCol,m.getCol(),symbol))
				countInvalid++;
			else{
				validMoves.add(m);
			}
		}
		if(countInvalid==opposites.size())
			return false;
		 
		return true;
	}
	
	private ArrayList<Move> findOpposites(int row, int col, int symbol){
		//elekse an uparxoun geitonikes theseis antithetou xrwmatos
		int oppositeSymbol= -symbol;
		ArrayList<Move> opposites=new ArrayList<Move>();
		if(row>0){
			//an mporoume na kinhthoume pros ta panw
			if(gameBoard[row-1][col]==oppositeSymbol)
				opposites.add(new Move(row-1, col));	
							
			if(col>0){
				if(gameBoard[row-1][col-1]==oppositeSymbol)
					opposites.add(new Move(row-1, col-1));	
			}
			if(col<7){
				if(gameBoard[row-1][col+1]==oppositeSymbol)
					opposites.add(new Move(row-1, col+1));
			}

		}
		if(row<7){
			//an mporoume na kinhthoume pros ta katw
			if(gameBoard[row+1][col]==oppositeSymbol)
				opposites.add(new Move(row+1, col));	

			if(col<7){
				if(gameBoard[row+1][col+1]==oppositeSymbol)
					opposites.add(new Move(row+1, col+1));	
			}
			if(col>0){
				if(gameBoard[row+1][col-1]==oppositeSymbol)
					opposites.add(new Move(row+1, col-1));
			}		

		}
		//an mporoume na kinhtoume mono dexia-aristera
		if(col<7){
			if(gameBoard[row][col+1]==oppositeSymbol)
				opposites.add(new Move(row, col+1));	
		}if(col>0){
			if(gameBoard[row][col-1]==oppositeSymbol)
				opposites.add(new Move(row, col-1));	
		}
		return opposites;
	}

	 /*
	 *check if there is a tile of the current playing symbol 
	 * forming a line between the one we wish to add
	 * and those with the opponents symbol
	 */
	private boolean checkLine(int dRow,int row,int dCol,int col,int symbol){
		if(gameBoard[row][col] == symbol)
			return true;
		if(gameBoard[row][col] == 0)
			return false;	
		if((row+dRow<0) ||(row+dRow>7))
			return false;
		if((col+dCol<0) ||(col+dCol>7))
			return false;

		return checkLine(dRow,row+dRow,dCol,col+dCol,symbol);
	}

	private void makeLines(int dRow,int row,int dCol,int col,int symbol){	
		if(gameBoard[row][col]==symbol){
			return;
		}
		else{		
			gameBoard[row][col]= symbol;//change the symbol of the pile so that it matches with the one of the player's 			
			makeLines(dRow,row+dRow,dCol,col+dCol,symbol);
		}
	}
	
	public ArrayList<Board> getChildren(int symbol){
		ArrayList<Board> children = new ArrayList<Board>();
		for(int row=0; row<8; row++){
			for(int col=0; col<8; col++){
				if(isValid(row, col, symbol)){
					Board child = new Board(this);
					child.makeMove(row, col, symbol);
					children.add(child);
				}
			}
		}
		return children;
	}

	//function terminal
	public boolean isTerminal(){
		int countDisks = 0;
		for(int row=0; row<8; row++){
			for(int col=0; col<8; col++){
				if(gameBoard[row][col] == O || gameBoard[row][col] == X){
					countDisks++;
				}
			}
		}
		if (countDisks == 64){
			return true;
		}else{
			return false;
		}
	}

    /*
     * The heuristic we use to evaluate is
	 * gia kathe telikh katastash, dwse perissoterous pontous
	 * an kapoios paikths exei pionia stis gwnies, ligotera an exei stis pleures k 
	 * akoma ligotera stis upoloipies theseis tou tableau
	*/    
    public int evaluate()
    {
		int[][] value={
			{1000,10,10,10,10,10,10,1000},
			{10,1,1,1,1,1,1,10},
			{10,1,1,1,1,1,1,10},
			{10,1,1,1,1,1,1,10},
			{10,1,1,1,1,1,1,10},
			{10,1,1,1,1,1,1,10},
			{10,1,1,1,1,1,1,10},
			{1000,10,10,10,10,10,10,1000}
		};
	    int Xscore=0;
		int Oscore=0;
		for (int row=0;row<8;row++){
			for(int col=0;col<8;col++){
				if(gameBoard[row][col]==O){
					Oscore+=value[row][col];
				}
				else if(gameBoard[row][col]==X){
					Xscore+=value[row][col];					
				}
			}
		}
        return Xscore - Oscore;
    }
  
   
    //Prints the board
	public void print()
	{
        System.out.println("* A B C D E F G H *");
        int numbers[]=new int[]{1,2,3,4,5,6,7,8};
		for(int row=0; row<8; row++)
		{
			System.out.print(numbers[row]+" ");
			for(int col=0; col<8; col++)
			{
				switch (gameBoard[row][col])
				{
					case X:
						System.out.print("X ");
						break;
					case O:
						System.out.print("O ");
						break;
					case EMPTY:
						System.out.print("- ");
						break;
					default:
						break;
				}
			}				
			System.out.println("*");
		}
		System.out.println("* * * * * * * * * *\n");
	}
	/*---------------END BOARD FUNCTIONS----------------*/
}