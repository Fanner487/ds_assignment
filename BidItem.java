public class BidItem{
	
	private Item item;
	private int currentBid;
	private String currentBidder;

	public BidItem(Item item){

		this.item = item;
		this.currentBid = item.getReservePrice();
		this.currentBidder = "";
	}

	public synchronized Item getItem(){
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

	public synchronized String getCurrentBidder(){
		return currentBidder;
	}

	public synchronized void setCurrentBidder(String bidder){
		this.currentBidder = bidder;
	}

}