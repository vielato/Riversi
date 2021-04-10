import java.util.Scanner;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args)
	{	
		Scanner scanner = new Scanner(System.in);
		
		System.out.print("\n\n********Welcome to our game********\n\n");
		
		//choose ai's oppontent(human or machine)
		int aisOpponent=checkInput(1,2,"Enter ai's opponent: \n1 - Try beating the computer\n2 - Watch two computers playing together\n", 
									scanner, false);

		//show the letter of each player
		System.out.print("\n\nAI as Computer player: X (Black)\n");
		if(aisOpponent==1)
			System.out.print("Human as player: O (White)\n");
		else
			System.out.print("AI as Human player: O (White)\n");

		//choose who is going to play first
		int firstPlayer=checkInput(1,2,"Choose who is going to play first: \n1 - X(black)\n2 - O(white)\n", 
									scanner, false);
		
		//enter the difficulty level
		int input=checkInput(1,3,"Enter difficulty: \n1 - Easy\n2 - Medium\n3 - Difficult\n", 
							scanner, false);
		int depth = input + 1;

		//initiallize players
        Player OPlayer = new Player(2, Board.O);
		Player XPlayer = new Player(depth, Board.X);
		Board board = new Board();

		board.print();

		if(firstPlayer==2)
			board.setLastLetterPlayed(Board.X);

		boolean stopX=false;
		boolean stopO=false;

		int[] rows=new int[]{1,2,3,4,5,6,7,8};
		char[] columns=new char[]{'A','B','C','D','E','F','G','H'};
		//While the game has not finished and at least one player can move
		while(!board.isTerminal() && (stopX==false||stopO==false))
		{
			switch (board.getLastLetterPlayed())
			{
                //If X played last, then O plays now
				case Board.X:
					System.out.println("O moves");
					Move OMove;
					if(aisOpponent==1){
						OMove=showMoves(board, scanner, columns);
					}else{
						OMove = OPlayer.MiniMaxAlphaBetaPruning(board);
					}			
                    if(!board.makeMove(OMove.getRow(), OMove.getCol(), Board.O)){
						System.out.println("No possible move detected for you in this round:(\n");
						stopO=true;
					}
					else{
						System.out.println("Moved successfully to: ("+rows[OMove.getRow()]+", "+columns[OMove.getCol()]+')');
						stopO=false;
					} 
					break;
                //If O played last, then X plays now
				case Board.O:
                    System.out.println("X moves");
					Move XMove = XPlayer.MiniMaxAlphaBetaPruning(board);					
                    if(!board.makeMove(XMove.getRow(), XMove.getCol(), Board.X)){
						System.out.println("No possible move detected for you in this round:(\n");
						stopX=true;
					}
					else{
						System.out.println("Moved successfully to: ("+rows[XMove.getRow()]+", "+columns[XMove.getCol()]+')');
						stopX=false;
					} 
					break;
				default:
					break;
			}
			try{
				Thread.sleep(1000);
				board.print();
            }catch(InterruptedException ex){
                Thread.currentThread().interrupt();
            }
		}
		//print the result of the game
		board.gameAnalysis();
   }

   //check the input is the integer range
   static int checkInput(int a, int b, String message, Scanner scanner, boolean isChar){	   
		boolean notValid=true;	
		int input=0;
		char in=' ';
		char[] columns=new char[]{'A','B','C','D','E','F','G','H'};
		do{
			try{				
				System.out.println(message);
				if(isChar){
					in= scanner.next().charAt(0);			
					for(int i=0;i<8;i++){
						if(columns[i]==in)
							input=i+1;
					}
				}
				else
					input = Integer.parseInt(scanner.next());

				if(input>=a && input<=b)
					notValid=false;
				else
					throw new Exception("Wrong input entered");
			}catch(Exception e){
				System.out.println("\nTry again, giving a valid input\n");
				
				notValid=true;
			}
		}while(notValid);
		
		return input;		
   }

   //show the valid moves to the user and check if he has choosen a valid one
   static Move showMoves(Board board, Scanner scanner, char[] columns){
	   //find and save user's possible moves
	   ArrayList<Move> moves=new ArrayList<Move>();
	   Move move=new Move();
	   int count=0;
	   for (int i=0;i<8;i++){
		   for(int j=0;j<8;j++){
			   if(board.isValid(i,j,Board.O)){
					System.out.println("Valid Move: ("+(i+1)+", "+columns[j]+")");
					count++;
					moves.add(new Move(i,j));
			   }
		   }
	   }

	   if(count==0)
		   System.out.println("No Valid Moves Detected");
		else{
			//check that the user chooses the correct movement
			boolean notValid=true;
			do{					
				try{	
					int row=checkInput(1,8,"Give the number of the row",scanner,false)-1;					
					int column=checkInput(1,8,"Give the letter of the column",scanner, true)-1;

					for(Move m:moves){
						if(row==m.getRow()&&column==m.getCol()){
							notValid=false;
							move.setRow(row);
							move.setCol(column);
						}
					}
					
					if(notValid)
						throw new Exception("Wrong move entered");
				}catch(Exception e){
					System.out.println("\nTry again, giving a valid move\n");
				}
			}while(notValid);
		}
		return move;
   }
}