package TSP;

import java.io.IOException;

public class main {
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
			GA ga = new GA(100, 10, 100, 0.9, 0.05);
			ga.solve();
	}
}
