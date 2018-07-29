package wine.orchestration;

public class main {
	public static SmallNetwork smallNetwork = new SmallNetwork();
	public static MediumNetwork mediumNetwork = new MediumNetwork();
	public static LargeNetwork largeNetwork = new LargeNetwork();

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SfcRequest sfcRequest = new SfcRequest(1, 1);	
		sfcRequest.getWiNEOrchestrationResult();;
		//SfcUtil.printSmallNetworkResource();
		
		
	}
}
