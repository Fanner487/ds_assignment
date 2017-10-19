public class Item{

	String name;
	int reservePrice;

	public Item(String name, int reservePrice){

		this.name = name;
		this.reservePrice = reservePrice;
	}

	public Item(String name){

		this(name, 0);
	}

	public synchronized String getName(){

		return name;
	}

	public synchronized void xsetName(String name){

		this.name = name;
	}

	public synchronized int getReservePrice(){

		return reservePrice;
	}

	public synchronized void setReservePrice(int reservePrice){
		
		this.reservePrice = reservePrice;
	}
}