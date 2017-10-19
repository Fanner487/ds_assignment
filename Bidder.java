import java.io.*;
import java.net.*;
import java.util.*;

public class Bidder{

	private Socket socket;
	private String name;

	public Bidder(Socket socket, String name){

		this.socket = socket;
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setName(String name){
		this.name = name;
	}

	public Socket getSocket(){
		return socket;
	}

	public void setSocket(Socket socket){
		this.socket = socket;
	}
}