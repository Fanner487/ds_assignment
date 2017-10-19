public class BidItem{
	
	Item item;
	int currentBid;

	public BidItem(Item item){

		this.item = item;
		this.currentBid = item.getReservePrice();
	}

	public Item getItem(){
		return item;
	}

	public synchronized void setItem(Item item){
		this.item = item;
	}

	public synchronized int getCurrentBid(){
		return currentBid;
	}

	public synchronized void setCurrentBid(int bid){
		this.currentBid = (int)bid;
	}

}