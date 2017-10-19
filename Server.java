import java.io.*;
import java.net.*;
import java.util.*;

public class Server{
	private static ServerSocket serverSocket;
	private static final int PORT = 1234;
	// private static Vector<Socket> clients;
	private static ArrayList<Socket> clients;
	private static ArrayList<Bidder> bidders;
	// private static 

	public static void main(String[] args) throws IOException{

		Item item = new Item("Chair");
		BidItem bidItem = new BidItem(item);

		System.out.println("Item: " + bidItem.getItem().getName());
		System.out.println("Starting bid: " + bidItem.getCurrentBid());

		try{
			clients = new ArrayList<Socket>();
			bidders = new ArrayList<Bidder>();
			serverSocket = new ServerSocket(PORT);
		}
		catch (IOException ioEx){
			System.out.println("\nUnable to set up port!");
			System.exit(1);
		}

		do{
		
			//Wait for client...
			Socket client = serverSocket.accept();
			Scanner nameInput= new Scanner(client.getInputStream());
			String receivedName = nameInput.nextLine();
			
			// Make bidder with name and socket
			Bidder bidder = new Bidder(client, receivedName);
			System.out.println("JOINED: " + bidder.getName());

			//Add client to list
			clients.add(client);
			bidders.add(bidder);

		

			//Create a thread to handle communication with
			//this client and pass the constructor for this
			//thread a reference to the relevant socket...
			// ClientHandler handler = new ClientHandler(client, clients, bidItem);
			ClientHandler handler = new ClientHandler(bidder, bidders, bidItem);

			handler.start();//As usual, this method calls run.
		}while (true);
	}
}

class ClientHandler extends Thread{

	private Bidder bidder;
	private Scanner input;
	private PrintWriter output;
	private ArrayList<Bidder> bidders;
	private BidItem bidItem;


	public ClientHandler(Bidder bidder, ArrayList<Bidder> bidders, BidItem bidItem){
		//Set up reference to associated socket...
		this.bidders = bidders;
		this.bidder = bidder;
		this.bidItem = bidItem;

		try{
			input = new Scanner(bidder.getSocket().getInputStream());
			output = new PrintWriter(bidder.getSocket().getOutputStream(),true);
		}
		catch(IOException ioEx)
		{
			ioEx.printStackTrace();
		}
	}

	public void broadcast(String message){

		try{

			synchronized(this){
				int newBid = Integer.parseInt(message);

				if(isValidBid(newBid)){

				bidItem.setCurrentBid(newBid);
				bidItem.setCurrentBidder(bidder.getName());

			
					for(Bidder bi : bidders){

						output = new PrintWriter(bi.getSocket().getOutputStream(), true);
						output.println("Allowed: " + newBid);
						output.println("Current bidder: " + bidItem.getCurrentBidder());
						output.println("Current bid: " + bidItem.getCurrentBid());	
					}
				}
				else{
					output = new PrintWriter(bidder.getSocket().getOutputStream(),true);
					output.println("Bid too low!");
				}

			}
		}
		catch(IOException e){
				e.printStackTrace();
		}

		

		// Problem may lie in here with the loop
		// for(Bidder bi : bidders){
			
		// 	try{
		// 		synchronized(this){

		// 			output = new PrintWriter(bi.getSocket().getOutputStream(),true);

		// 			int newBid = Integer.parseInt(message);

		// 			System.out.println("Calling by: " + bi.getName());
		// 			if(isValidBid(newBid)){

		// 				bidItem.setCurrentBid(newBid);
		// 				bidItem.setCurrentBidder(bidder.getName());

		// 				// output.println("Allowed: " + newBid);
		// 				output.println("Current bidder: " + bidItem.getCurrentBidder());
		// 				output.println("Current bid: " + bidItem.getCurrentBid());

		// 			}
		// 			else{
		// 				// output.println("Not Allowed: " + message);
		// 				PrintWriter tooLowOutput = new PrintWriter(bidder.getSocket().getOutputStream(),true);
		// 				if(bi == bidder){
		// 					tooLowOutput.println("Bid too low!");
		// 				}
						
		// 				// output.println("Current bidder: " + bidItem.getCurrentBidder());
		// 				// output.println("Current bid: " + bidItem.getCurrentBid());
		// 			}
					
					
		// 		}
		// 	}catch(IOException e){
		// 		e.printStackTrace();
		// 	}
			
		// }
	}

	public void run(){
		String received;
		do{
			received = input.nextLine();

			System.out.println("Received: " + received);


			broadcast(received);
		//Repeat above until 'QUIT' sent by client...
		}while (!received.equals("QUIT"));

		try{
			if (bidder.getSocket() !=null){
				System.out.println("Closing down connection...");
				bidder.getSocket().close();
			}
		}
		catch(IOException ioEx){
			System.out.println("Unable to disconnect!");
		}
	}

	private boolean isValidBid(int input){

		System.out.println("-----------\nIn isValidBid\n-------");
		System.out.println("input: " + input);
		System.out.println("current: " + this.bidItem.getCurrentBid());
		System.out.println("------------------");

		boolean result = false;

		if(input > this.bidItem.getCurrentBid()){
			result = true;
		}
		else{
			result = false;
		}

		System.out.println(result);

		return result;

	}
}
