// CS81 - Final Project
// Maria-Elena Solano

package main;
import  main.GA;
import  java.util.List;
import  java.util.stream.*;


public class MA extends GA {


  // Learning rate
  final double eta;


  // Constructor
  public MA(int          _n_repl,
            pop          _P,
            int          _N, 
            int          _T, 
            double       _eps_m, 
            double       _eps_c, 
            int          _M, 
            List<Number> _alleles,
            fun          _F){

  	super(_n_repl, _P, _N, _T, _eps_m, _eps_c, _M, _alleles, _F);

    prefix = "MA";

    // precalculated from:
    // eta = ln(sqrt(2*ln N / F_cumul_max))
    eta = -22.90313;

  }


  void evaluate_pop(){

    super.evaluate_pop();
    weights = np.normalize(P.fitness.stream().map(
      f -> Math.exp(eta*(1.0 - f/14420.0))                       // normalizing F first.
    ).collect(Collectors.toList()));

  }

 
}


























