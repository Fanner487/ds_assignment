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
			ClientHandler handler = new ClientHandler(client, clients, bidItem);

			handler.start();//As usual, this method calls run.
		}while (true);
	}
}

class ClientHandler extends Thread{
	private Socket client;
	private Scanner input;
	private PrintWriter output;
	private ArrayList<Socket> clients;
	private BidItem bidItem;


	// Whenever clients send in bid, verify if it is bigger than current bid
	


	public ClientHandler(Socket socket, ArrayList<Socket> clients, BidItem bidItem){
		//Set up reference to associated socket...
		this.clients = clients;
		client = socket;
		this.bidItem = bidItem;

		try{
			input = new Scanner(client.getInputStream());
			output = new PrintWriter(client.getOutputStream(),true);
		}
		catch(IOException ioEx)
		{
			ioEx.printStackTrace();
		}
	}

	public void broadcast(String message){
		for(Socket cli : clients){
			// if(cli != client){
			// 	try{
			// 		synchronized(this){
			// 			output = new PrintWriter(cli.getOutputStream(),true);
			// 			output.println("ECHO: " + message);
			// 		}
			// 	}catch(IOException e){
			// 		e.printStackTrace();
			// 	}
			// }
			
			try{
				synchronized(this){

					output = new PrintWriter(cli.getOutputStream(),true);

					int newBid = Integer.parseInt(message);

					if(checkBidOverCurrent(newBid)){

						bidItem.setCurrentBid(newBid);

						output.println("Allowed: " + newBid);
						output.println("Current bid: " + bidItem.getCurrentBid());

					}
					else{
						output.println("Not Allowed: " + message);
						output.println("Current bid: " + bidItem.getCurrentBid());
					}
					
					
				}
			}catch(IOException e){
				e.printStackTrace();
			}
			
		}
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
			if (client!=null){
				System.out.println("Closing down connection...");
				client.close();
			}
		}
		catch(IOException ioEx){
			System.out.println("Unable to disconnect!");
		}
	}

	private boolean checkBidOverCurrent(int input){

		boolean result = false;

		if(input > this.bidItem.getCurrentBid()){
			result = true;
		}
		else{
			result = false;
		}

		return result;

	}
}
