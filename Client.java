import java.io.*;
import java.net.*;
import java.util.*;

public class Client
{
	private static InetAddress host;
	private static final int PORT = 1234;

	public static void main(String[] args)
	{
		try
		{
			host = InetAddress.getLocalHost();
		}
		catch(UnknownHostException uhEx)
		{
			System.out.println("\nHost ID not found!\n");
			System.exit(1);
		}
		sendMessages(args[0]);
	}

	private static void sendMessages(String name)
	{
		Socket socket = null;

		try
		{


			socket = new Socket(host,PORT);

			//Scanner networkInput = new Scanner(socket.getInputStream());
			PrintWriter networkOutput = new PrintWriter(socket.getOutputStream(),true);

			networkOutput.println(name);

			ServerListener listener = new ServerListener(socket);
			listener.start();

			//Set up stream for keyboard entry...
			Scanner userEntry = new Scanner(System.in);

			String message, response;
			do
			{
				System.out.print(
							"Enter message ('QUIT' to exit): ");
				message =  userEntry.nextLine();
				networkOutput.println(message);
			}while (!message.equals("QUIT"));
		}
		catch(IOException ioEx)
		{
			ioEx.printStackTrace();
		}

		finally
		{
			try
			{
				System.out.println("\nClosing connection...");
				socket.close();
			}
			catch(IOException ioEx)
			{
				System.out.println("Unable to disconnect!");
				System.exit(1);
			}
		}
	}
}

class ServerListener extends Thread{
	Socket socket;
	public ServerListener(Socket socket){
		this.socket = socket;
	}

	public void run(){
		try{
			Scanner networkInput = new Scanner(socket.getInputStream());
			String response;
			while(true){
				response = networkInput.nextLine();
				System.out.println("\nSERVER> " + response);
				this.sleep(1000);
			}
		}catch(IOException ioEx){
			ioEx.printStackTrace();
		}catch(InterruptedException e){
			e.printStackTrace();
		}
	}
}
