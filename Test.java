public class Test{
	
	public static void main(String[] args){

		Item item = new Item("Guitar");
		Item item2 = new Item("Spanner", 2);

		System.out.println(item.getReservePrice());
		System.out.println(item2.getReservePrice());

		BidItem bidItem = new BidItem(item2);

		System.out.println(bidItem.getCurrentBid());
	}
}