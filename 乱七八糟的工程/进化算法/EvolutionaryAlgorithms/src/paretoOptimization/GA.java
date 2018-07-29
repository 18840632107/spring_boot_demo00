package paretoOptimization;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

public class GA {

	public double randomDouble(double start, double end) {
		double result = start + Math.random() * (end - start);
		return result;
	}

	public double objective1(ArrayList<Double> vector) {
		double sum = 0.0;
		for (int i = 0; i < vector.size(); i++) {
			sum += (vector.get(i)) * (vector.get(i));
		}
		return sum;
	}

	public double objective2(ArrayList<Double> vector) {
		double sum = 0.0;
		for (int i = 0; i < vector.size(); i++) {
			sum += (vector.get(i) - 2.0) * (vector.get(i) -2.0);
		}
		return sum;
	}

	public ArrayList<Double> decode(String bitstring, ArrayList<ArrayList<Integer>> search_space, int bits_per_param) {
		ArrayList<Double> vector = new ArrayList<Double>();
		for (int i = 0; i < search_space.size(); i++) {
			int off = i * bits_per_param;
			double sum = 0.0;
			String param = bitstring.substring(off, off + bits_per_param);
			StringBuilder copyOFparam = new StringBuilder(param);
			param = copyOFparam.reverse().toString();
			for (int j = 0; j < param.length(); j++) {
				sum += ((param.charAt(j) == '1') ? 1.0 : 0.0) * Math.pow(2.0, j);
			}
			int min = search_space.get(i).get(0);
			int max = search_space.get(i).get(1);
			double number = min + ((max - min) / ((Math.pow(2.0, bits_per_param) - 1.0))) * sum;
			vector.add(number);
		}
		return vector;
	}

	public String point_mutation(String bitstring, double rate) {
		String child = "";
		rate = 1.0 / bitstring.length();
		for (int i = 0; i < bitstring.length(); i++) {
			if (randomDouble(0.0, 1.0) < rate) {
				char stringbit = (bitstring.charAt(i) == '1') ? '0' : '1';
				child += stringbit;
			} else {
				child += bitstring.charAt(i);
			}
		}
		return child;
	}

	public Chromosome binary_tournament(ArrayList<Chromosome> pop) {
		Random random = new Random();
		int i = random.nextInt(pop.size());
		int j = random.nextInt(pop.size());
		while (j == i) {
			j = random.nextInt(pop.size());
		}
		if (pop.get(i).getFitness() < pop.get(j).getFitness()) {
			return pop.get(i);
		} else {
			return pop.get(j);
		}
	}

	public String crossover(String parent1, String parent2, double rate) {
		double random = randomDouble(0.0, 1.0);
		if (random >= rate) {
			return "" + parent1;
		}
		String child = "";
		for (int i = 0; i < parent1.length(); i++) {
			random = randomDouble(0.0, 1.0);
			if (random < 0.5) {
				child += parent1.charAt(i);
			} else {
				child += parent2.charAt(i);
			}
		}
		return child;
	}

	public ArrayList<Chromosome> reproduce(ArrayList<Chromosome> selected, int pop_size, double p_cross) {
		ArrayList<Chromosome> children = new ArrayList<>();
		for (int i = 0; i < selected.size(); i++) {
			Chromosome p1 = selected.get(i);
			Chromosome p2 = new Chromosome();
			if (children.size() >= pop_size) {
				break;
			}
			if (i == selected.size() - 1) {
				p2 = selected.get(0);
			} else {
				if (i % 2 == 0) {
					p2 = selected.get(i + 1);
				} else {
					p2 = selected.get(i - 1);
				}
			}
			Chromosome child = new Chromosome();
			String tmpBitString = crossover(p1.getBitstring(), p2.getBitstring(), p_cross);
			child.setBitstring(tmpBitString);
			double rate = 1.0 / child.getBitstring().length();
			tmpBitString = point_mutation(child.getBitstring(), rate);
			children.add(child);
		}
		return children;
	}

	public String random_bitstring(int num_bits) {
		StringBuilder sb = new StringBuilder("");
		for (int i = 0; i < num_bits; i++) {
			if (randomDouble(0.0, 1.0) < 0.5) {
				sb.append("0");
			} else {
				sb.append("1");
			}
		}
		return sb.toString();
	}

	public void calculate_objectives(ArrayList<Chromosome> pop, ArrayList<ArrayList<Integer>> search_space, int bits_per_param) {
		for (int i = 0; i < pop.size(); i++) {
			Chromosome p = pop.get(i);
			ArrayList<Double> vector = decode(p.getBitstring(), search_space, bits_per_param);
			p.setVector(vector);
			ArrayList<Double> objectives = new ArrayList<>();
			objectives.add(objective1(p.getVector()));
			objectives.add(objective2(p.getVector()));
			p.setObjectives(objectives);
		}
	}

	public boolean dominates(Chromosome p1, Chromosome p2) {
		for (int i = 0; i < p1.getObjectives().size(); i++) {
			if (p1.getObjectives().get(i) > p2.getObjectives().get(i)) {
				return false;
			}
		}
		return true;
	}

	public double weighted_sum(Chromosome x) {
		double sum = 0.0;
		for (int i = 0; i < x.getObjectives().size(); i++) {
			sum += x.getObjectives().get(i);
		}
		return sum;
	}

	// 欧几里得度量
	public double euclidean_distance(ArrayList<Double> c1, ArrayList<Double> c2) {
		double sum = 0.0;
		for (int i = 0; i < c1.size(); i++) {
			sum += (c1.get(i) - c2.get(i)) * (c1.get(i) - c2.get(i));
		}
		return Math.sqrt(sum);
	}

	// 获得某一对象的支配对象的集合
	public void calculate_dominated(ArrayList<Chromosome> pop) {
		for (int i = 0; i < pop.size(); i++) {
			ArrayList<Chromosome> dom_set_TMP = new ArrayList<>();
			Chromosome p1 = pop.get(i);
			for (int j = 0; j < pop.size(); j++) {
				Chromosome p2 = pop.get(j);
				if (!p1.equals(p2) && dominates(p1, p2)) {
					dom_set_TMP.add(p2);
				}
			}
			p1.setDom_set(dom_set_TMP);
		}
	}

	public double calculate_raw_fitness(Chromosome p1, ArrayList<Chromosome> pop) {
		double sum = 0.0;
		for (int i = 0; i < pop.size(); i++) {
			Chromosome p2 = pop.get(i);
			if (dominates(p2, p1)) {
				sum += (double)p2.getDom_set().size();
			}
		}
		return sum;
	}

	public double calculate_density(Chromosome p1, ArrayList<Chromosome> pop) {
		for (int i = 0; i < pop.size(); i++) {
			Chromosome p2 = pop.get(i);
			double dist = euclidean_distance(p1.getObjectives(), p2.getObjectives());
			p2.setDist(dist);
		}
		ArrayList<Chromosome> list = new ArrayList<>();
		list.addAll(pop);
		Collections.sort(list, new SortByDist());
		int k = (int) Math.sqrt(pop.size());
		return 1.0 / (list.get(k).getDist() + 2.0);
	}

	public void calculate_fitness(ArrayList<Chromosome> pop, ArrayList<Chromosome> archive,
			ArrayList<ArrayList<Integer>> search_space, int bits_per_param) {
		calculate_objectives(pop, search_space, bits_per_param);
		ArrayList<Chromosome> union = new ArrayList<>();
		union.addAll(archive);
		union.addAll(pop);
		calculate_dominated(union);
		for (int i = 0; i < union.size(); i++) {
			Chromosome p =union.get(i);
			double tmp1 = calculate_raw_fitness(p, union);
			p.setRaw_fitness(tmp1);
			double tmp2 = calculate_density(p, union);
			p.setDensity(tmp2);
			p.setFitness(tmp1 + tmp2);
		}
	}

	public ArrayList<Chromosome> environmental_selection(ArrayList<Chromosome> pop, ArrayList<Chromosome> archive,
			int archive_size) {
		ArrayList<Chromosome> union = new ArrayList<>();
		union.addAll(archive);
		union.addAll(pop);
		ArrayList<Chromosome> environment = new ArrayList<>();
		for (int i = 0; i < union.size(); i++) {
			if (union.get(i).getFitness() < 1.0) {
				environment.add(union.get(i));
			}
		}
		if (environment.size() < archive_size) {
			Collections.sort(union, new SortByFitness());
			for (int i = 0; i < union.size(); i++) {
				Chromosome p = union.get(i);
				if (p.getFitness() >= 1.0) {
					environment.add(p);
				}
				if (environment.size() >= archive_size) {
					break;
				}
			}
		} else if (environment.size() > archive_size) {
			while (environment.size() > archive_size) {
				int k = (int) Math.sqrt(environment.size());
				for (int i = 0; i < environment.size(); i++) {
					Chromosome p1 = environment.get(i);
					for (int j = 0; j < environment.size(); j++) {
						Chromosome p2 = environment.get(j);
						p2.setDist(euclidean_distance(p1.getObjectives(), p2.getObjectives()));
					}
					ArrayList<Chromosome> list = new ArrayList<>();
					list.addAll(environment);
					Collections.sort(list, new SortByDist());
					p1.setDensity(list.get(k).getDist());
				}
				Collections.sort(environment, new SortByDensity());
				environment.remove(0);
			}
		}
		return environment;
	}

	public ArrayList<Chromosome> search(ArrayList<ArrayList<Integer>> search_space, int max_gens, int pop_size, int archive_size,
			double p_cross) {
		int bits_per_param = 16;
		ArrayList<Chromosome> pop = new ArrayList<>();
		for (int i = 0; i < pop_size; i++) {
			Chromosome tmpChrom = new Chromosome();
			String tmpString = random_bitstring(search_space.size() * bits_per_param);
			tmpChrom.setBitstring(tmpString);
			pop.add(tmpChrom);
		}
		int gen = 0;
		ArrayList<Chromosome> archive = new ArrayList<>();
		while (true) {
			if (gen >= max_gens) {
				break;
			}
			calculate_fitness(pop, archive, search_space, bits_per_param);
			archive = environmental_selection(pop, archive, archive_size);
		    ArrayList<Chromosome>arrayBest = new ArrayList<>();
		    arrayBest.addAll(archive);
			Collections.sort(arrayBest, new SortByWeighted_Sum());
			Chromosome best = arrayBest.get(0);
			best.print(gen,best);
			ArrayList<Chromosome> selected = new ArrayList<>();
			for (int i = 0; i < pop_size; i++) {
				selected.add(binary_tournament(archive));
			}
			pop = reproduce(selected, pop_size, p_cross);
			gen++;
		}
		return archive;
	}
}

class SortByDist implements Comparator<Chromosome> {
	public int compare(Chromosome c1, Chromosome c2) {
		if (c1.getDist() > c2.getDist()) {
			return 1;
		} else if (c1.getDist() < c2.getDist()) {
			return -1;
		}
		else {
			return 0;
		}
	}
}

class SortByFitness implements Comparator<Chromosome> {
	public int compare(Chromosome c1, Chromosome c2) {
		if (c1.getFitness() > c2.getFitness()) {
			return 1;
		} else if (c1.getFitness() < c2.getFitness()) {
			return -1;
		}
		else {
			return 0;
		}
	}
}

class SortByDensity implements Comparator<Chromosome> {
	public int compare(Chromosome c1, Chromosome c2) {
		if (c1.getDensity() > c2.getDensity()) {
			return 1;
		} else if (c1.getDensity() < c2.getDensity()) {
			return -1;
		}
		else {
			return 0;
		}
	}
}

class SortByWeighted_Sum implements Comparator<Chromosome> {
	public int compare(Chromosome c1, Chromosome c2) {
		GA ga = new GA();
		double w1 = ga.weighted_sum(c1);
		double w2 = ga.weighted_sum(c2);
		if (w1 > w2) {
			return 1;
		} else if (w1 < w2) {
			return -1;
		}
		else {
			return 0;
		}
	}
}
