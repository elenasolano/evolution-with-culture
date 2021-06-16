// CS81 - Final Project
// Maria-Elena Solano

package main;
import  main.GA;
import  main.MA;
import  java.util.List;
import  java.util.EnumMap;
import  pacman.controllers.Controller;
import  pacman.controllers.examples.Legacy;
import  pacman.game.Game;
import  static pacman.game.Constants.*;



public final class experiment{

  public static void main(String[] args){

    // Parameters
          int    n_repl;
    final int    N       = 100;
    final int    T       = 100;
    final double eps_m   = 0.1;
    final double eps_c   = 0.7;
    final int    M       = 100;
    List<Number> alleles = np.range(0, 65536);
    
    // Try read the replication number from the parameters
    try{ n_repl = Integer.parseInt(args[0]); }
    catch(Exception e){ n_repl = 0; }

    // Initial population
    pop P = new pop(N, M, alleles);

    // Run MA,GA
    new Thread(
      new MA(n_repl, P.copy(), N, T, eps_m, eps_c, M, np.copy(alleles),
        (chromo c) -> F_ms_pacman(c))
    ).start();

    new Thread(
      new GA(n_repl, P.copy(), N, T, eps_m, eps_c, M, np.copy(alleles),
        (chromo c) -> F_ms_pacman(c))
    ).start();

    return;
  
  }


  public static double F_ms_pacman(chromo c){

    Game g;
    Controller<MOVE> ctl = new ge_pacman_controller(c);
    Controller<EnumMap<GHOST,MOVE>> ghosts;
    
    g = new Game(0, 3);
    ghosts = new Legacy();
    while(!g.gameOver()){ 
      g.advanceGame(ctl.getMove(g.copy(),-1), ghosts.getMove(g.copy(),-1)); }    
    
    return g.getScore();

  }

}


























