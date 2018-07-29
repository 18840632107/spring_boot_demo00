package genetic.orchestration;

public class main {
	public static SmallNetwork smallNetwork = new SmallNetwork();
	public static MediumNetwork mediumNetwork = new MediumNetwork();
	public static LargeNetwork largeNetwork = new LargeNetwork();

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		GeneticAlgorithm ga = new GeneticAlgorithm(100, 100, 0.9, 0.05, 1, 3);
		ga.solve();
	}
}
