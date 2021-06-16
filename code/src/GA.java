// CS81 - Final Project
// Maria-Elena Solano

package main;
import  java.util.List;
import  java.util.ArrayList;
import  java.util.stream.*;
import  java.io.FileWriter;
import  java.io.IOException;


public class GA implements Runnable {


  // Interface to pass the fitness function as a parameter.
  public interface fun {
    public double eval(chromo c);
  }


  // Main parameters
  final int    n_repl;                 // Replication number
  final int    N;                      // Population size
  final int    T;                      // Number of generations
  final double eps_m;                  // Mutation rate
  final double eps_c;                  // Crossover rate
  final int    M;                      // Chromosome length
  final List<Number> alleles;          // Alleles
  final fun    F;                      // Fitness function

  // Population
  pop  P;

  // Weights;
  List<Double> weights;

  // Stats
  List<Double>  F_best;
  List<Double>  F_mean;

  // Misc
  int           t;                     // Current generation
  String        prefix;                // String prefix to use


  // Constructor
  public GA(int          _n_repl,
            pop          _P,
            int          _N, 
            int          _T, 
            double       _eps_m, 
            double       _eps_c, 
            int          _M, 
            List<Number> _alleles,
            fun          _F){

    n_repl  = _n_repl;
    P       = _P;
    N       = _N;
    T       = _T;
    eps_m   = _eps_m;
    eps_c   = _eps_c;
    M       = _M;
    alleles = _alleles;
    F       = _F;

    F_best  = new ArrayList<Double>();
    F_mean  = new ArrayList<Double>();

    prefix = "GA";

  }


  void evaluate_pop(){

    // Evaluate the fitness function and selection weights
    P.fitness = P.parallelStream().map(e -> F.eval(e)).collect(Collectors.toList());
    weights   = np.normalize(P.fitness);

    // Update statistics
    F_best.add(P.fitness.stream().mapToDouble(e -> e).max().orElse(0.0));
    F_mean.add(P.fitness.stream().mapToDouble(e -> e).average().orElse(0.0));

    // Save state
    try{
      FileWriter fw = new FileWriter(
        String.format("../../results/%02d_%s_results.csv", n_repl, prefix), (t > 0));
      fw.write(P.toString(String.format("%02d",t)));
    }
    catch (IOException ex){ }


    System.out.println(String.format("%s,%02d,%f,%f", prefix, t, F_best.get(t), F_mean.get(t)));
    //System.out.println("\n\nGAfitness = " + P.fitness + "\nGAweights = " + weights);

  }


  chromo select(){

    return np.sample_from(P, weights).copy();

  }


  List<chromo> cross_and_mutate(chromo parent1, chromo parent2){

    List<chromo> offspring  = new ArrayList<chromo>();
    chromo       offspring1 = parent1.copy();
    chromo       offspring2 = parent2.copy();
    int          crosspoint;

    if(np.uniform() < eps_c){

      crosspoint = np.randint(1, M - 2);
    
      for(int i=crosspoint; i<M; i++){
        offspring1.set(i, parent2.get(i));
        offspring2.set(i, parent1.get(i));
      }

    }

    offspring.add(mutate(offspring1));
    offspring.add(mutate(offspring2));

    return offspring;

  }


  chromo mutate(chromo child){

    for(int i=0; i<M; i++){

      if(np.uniform() < eps_m){
        child.set(i, np.random_choice(np.copy_except(alleles, child.get(i))));
      }

    }

    return child;

  }


  void one_generation(){

    pop P_new = new pop(N, M, alleles);


    for(int i=0; i<N; i+=2){

      List<chromo> offspring = cross_and_mutate(select(), select());

      if((i+1) < N){
        P_new.set(i,   offspring.get(0));
        P_new.set(i+1, offspring.get(1));
      }
      else{
        P_new.set(i, np.random_choice(offspring));
      }
    }

    P = P_new;

  }

  
  public void run(){

    for(t=0, evaluate_pop(); t<T; t++, evaluate_pop()){
      one_generation();
    }

  }

}

