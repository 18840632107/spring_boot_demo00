package virtualNetworkEmbedding;

public class main {

	public static void main(String[] args) {		
		GA ga = new GA(100, 100, 0.9, 0.05);
		ga.solve();
	}

}
