package main;

import  java.util.List;
import  java.util.ArrayList;
import  javax.script.*;
import  pacman.game.Game;
import  pacman.game.Constants.MOVE;
import  pacman.controllers.Controller;
import  static pacman.game.Constants.*;


public final class ge_pacman_controller extends Controller<MOVE>{

  final ScriptEngine js;

  public ge_pacman_controller(chromo c){

  	super();
    js = (new ScriptEngineManager()).getEngineByName("nashorn");
    try{ js.eval(decode_chromo(c)); } catch(Exception e){ };
  
  }


  public MOVE getMove(Game game, long timeDue){

    MOVE move = MOVE.NEUTRAL;

    try{ move = (MOVE)(((Invocable)js).invokeFunction("get_move", game)); } catch(Exception e){ }
    
    return move;
  }


  public static List<Number> extend_chromo(chromo c){

    List<Number> c_ext = new ArrayList<Number>();

    for(int j=0; j<3; j++){
      for(int i=0; i<c.size(); i++){
        c_ext.add(c.get(i));
      }
    }

    return c_ext;

  }


  public static String decode_chromo(chromo c){

    List<Number> c_ext = extend_chromo(c);
    String code = "";

    code += "function get_move(gs){ ";
    code += "  var next = Java.type('pacman.game.Constants').MOVE.NEUTRAL; ";
    code += "  var ctl = Java.type('main.ge_pacman_controller'); ";
    code +=    g_setup(c_ext);
    code +=    g_main (c_ext);
    code += "  return next; ";
    code += "} ";

    return code;

  }


  public static String g_setup(List<Number> c){

    String code = "";

    code += String.format("  var thresholdDistanceGhosts = %s;  ", g_ghostThreshold(c));
    code += String.format("  var ws = %s;  ",                      g_window(c));
    code += String.format("  var avoidGhostDistance = %s;  ",      g_avoidDistance(c));
    code +=               "  var avgDistToGhosts = ctl.adtg(gs);  ";
    code +=               "  var inedibleGhostDistance = ctl.nig(gs);  ";
    code +=               "  var numPowerPills = ctl.ppd(gs);  ";

    return code;

  }


  public static String g_main(List<Number> c){

    String code = "";

    code += String.format("if(ctl.eg(gs) == 0){ %s } ", g_statements(c));
    code += String.format("else{ %s }  ",           g_statements(c));

    return code;

  }


  public static String g_statements(List<Number> c){

    String code = "";
    int i;

    if(c.size() > 0){

      i = c.get(0).intValue() % 2;  
      c.subList(0,1).clear();

      if(i == 0){
        code += g_ifs(c);
      }
      else if(i == 1){
        code += g_ifs  (c);
        code += g_elses(c); 
      }

    }
    
    return code;
  }


  public static String g_ifs(List<Number> c){

    String code = "";
    int i;

    if(c.size() > 0){

      i = c.get(0).intValue() % 4;  
      c.subList(0,1).clear();
   
      if(i == 0){
        code += String.format("if( %s ) ", g_condition(c));
        code += String.format("{ %s } ",   g_action   (c));
      }
      else if(i == 1){
        code += String.format("if( %s ) ", g_condition (c));
        code += String.format("{ %s } ",   g_statements(c));
      }
      else if(i == 2){
        code += String.format(
          "if( avgDistToGhosts %s thresholdDistanceGhosts ) ", g_lessX2(c));
        code += String.format("{ %s } ",                       g_actsOrStats(c));
      }
      else if(i == 3){
        code += String.format(
          "if( inedibleGhostDistance %s ws ) ", g_lessX2(c));
        code += String.format("{ %s } ",        g_avoidOrPPill(c));
      }

    }
    
    return code;
  }


  public static String g_elses(List<Number> c){

    String code = "";
    int i;

    if(c.size() > 0){

      i = c.get(0).intValue() % 2;  
      c.subList(0,1).clear();

      if(i == 0){
        code += String.format("else { %s } ", g_action(c));
      }
      else if(i == 1){
        code += String.format("else { %s } ", g_statements(c));
      }

    }
    
    return code;
  }


  public static String g_actsOrStats(List<Number> c){

    String code = "";
    int i;

    if(c.size() > 0){

      i = c.get(0).intValue() % 2;  
      c.subList(0,1).clear();

      if(i == 0){
        code += g_action(c);
      }
      else if(i == 1){
        code += g_statements(c);
      }

    }
    
    return code;
  }


  public static String g_action(List<Number> c){

    String code = "";
    int i;

    if(c.size() > 0){

      i = c.get(0).intValue() % 2;  
      c.subList(0,1).clear();

      if(i == 0){
        code += String.format(
          "next = ctl.gct(%s, gs, ws); ", g_closest(c));
      }
      else if(i == 1){
        code += "if ( numPowerPills > 0) ";
        code += String.format(
          "{ %s } else { next = ctl.gct(0, gs, ws); }", g_pPillAction(c));
      }

    }
    
    return code;
  }


  public static String g_closest(List<Number> c){

    String code = "";
    int i;

    if(c.size() > 0){

      i = c.get(0).intValue() % 3;  
      c.subList(0,1).clear();

      if(i == 0){
        code += "0";
      }
      else if(i == 1){
        code += "1";
      }
      else if(i == 2){
        code += "2";
      }

    }
    
    return code;
  }


  public static String g_avoidOrPPill(List<Number> c){

    String code = "";
    int i;

    if(c.size() > 0){

      i = c.get(0).intValue() % 2;  
      c.subList(0,1).clear();

      if(i == 0){
        code += g_avoidAction(c);
      }
      else if(i == 1){
        code += g_pPillAction(c);
      }

    }
    
    return code;
  }


  public static String g_avoidAction(List<Number> c){
 
    return "next = ctl.gct(1, gs, ws);  ";
 
  }


  public static String g_pPillAction(List<Number> c){

    return "next = ctl.gct(3, gs, ws);  ";

  }


  public static String g_condition(List<Number> c){

    String code = "";

    code += String.format("%s ", g_var(c));
    code += String.format("%s ", g_comparison(c));
    code += String.format("%s ", g_var(c));
    
    return code;
  }


  public static String g_var(List<Number> c){

    String code = "";
    int i;

    if(c.size() > 0){

      i = c.get(0).intValue() % 5;  
      c.subList(0,1).clear();

      if(i == 0){
        code += "thresholdDistanceGhosts";
      }
      else if(i == 1){
        code += "inedibleGhostDistance";
      }
      else if(i == 2){
        code += "avgDistToGhosts";
      }
      else if(i == 3){
        code += "avoidGhostDistance";
      }
      else if(i == 4){
        code += "ws";
      }

    }
    
    return code;
  }


  public static String g_ghostThreshold(List<Number> c){

    String code = "";
    int i;

    if(c.size() > 0){

      i = c.get(0).intValue() % 20;  
      c.subList(0,1).clear();

      code += String.format("%d", i + 1);

    }
    
    return code;
  }


  public static String g_avoidDistance(List<Number> c){

    String code = "";
    int i;

    if(c.size() > 0){

      i = c.get(0).intValue() % 15;  
      c.subList(0,1).clear();

      code += String.format("%d", i + 1);

    }
    
    return code;
  }


  public static String g_window(List<Number> c){

    String code = "";
    int i;

    if(c.size() > 0){

      i = c.get(0).intValue() % 10;  
      c.subList(0,1).clear();

      code += String.format("%d", 2*i + 1);

    }
    
    return code;
  }


  public static String g_comparison(List<Number> c){

    String code = "";
    int i;

    if(c.size() > 0){

      i = c.get(0).intValue() % 5;  
      c.subList(0,1).clear();

      if(i == 0){
        code += "<";
      }
      else if(i == 1){
        code += ">";
      }
      else if(i == 2){
        code += "<=";
      }
      else if(i == 3){
        code += ">=";
      }
      else if(i == 4){
        code += "==";
      }

    }
    
    return code;
  }


  public static String g_lessX2(List<Number> c){

    String code = "";
    int i;

    if(c.size() > 0){

      i = c.get(0).intValue() % 2;  
      c.subList(0,1).clear();

      if(i == 0){
        code += "<";
      }
      else if(i == 1){
        code += "<=";
      }

    }
    
    return code;
  }


  // NearestPowerPill(): goes to the nearest power pill (or do nothing if no active power pills)
  public static MOVE nppd(Game gs){

    MOVE  move               = MOVE.NEUTRAL;
    int   current            = gs.getPacmanCurrentNodeIndex();
    int[] active_power_pills = get_active_power_pills(gs);

    if(active_power_pills.length > 0){
      move = gs.getNextMoveTowardsTarget(current, 
        gs.getClosestNodeIndexFromNodeIndex(current, active_power_pills, DM.PATH), DM.PATH);
    }

    return move;

  }


  // NearestPill(): goes to the nearest pill (or do nothing if no active pills)
  // --unless a power pill is closer, in which case, move towards its vicinity.
  public static MOVE npd(Game gs){

    MOVE   move               = MOVE.NEUTRAL;
    int    curr               = gs.getPacmanCurrentNodeIndex();
    int[]  active_pills       = get_active_pills(gs);
    int[]  active_power_pills = get_active_power_pills(gs);
    int    closest_pill;
    int    closest_power_pill;
    double dist_to_closest_pill;
    double dist_to_closest_power_pill;

    if(active_pills.length > 0){

      closest_pill         = gs.getClosestNodeIndexFromNodeIndex(curr, active_pills, DM.PATH);
      dist_to_closest_pill = gs.getShortestPathDistance(curr, closest_pill);

      if(active_power_pills.length > 0){
        closest_power_pill = gs.getClosestNodeIndexFromNodeIndex(curr, active_power_pills, DM.PATH);  
        dist_to_closest_power_pill = gs.getShortestPathDistance(curr, closest_power_pill);

        if(dist_to_closest_power_pill < dist_to_closest_pill){
          move = gs.getNextMoveTowardsTarget(curr, 
            gs.getClosestNodeIndexFromNodeIndex(curr, 
              gs.getNeighbouringNodes(closest_power_pill), DM.PATH), DM.PATH);
        }
        else{
          move = gs.getNextMoveTowardsTarget(curr, closest_pill, DM.PATH);  
        }

      }
      else{
        move = gs.getNextMoveTowardsTarget(curr, closest_pill, DM.PATH);  
      }
      
    }

    return move;

  }


  // AvoidNearestGhost(): avoids the nearest ghost within a 'window' of the given size.
  public static MOVE ang(Game gs, int ws){

    MOVE  move              = MOVE.NEUTRAL;
    int   current           = gs.getPacmanCurrentNodeIndex();
    double d, min_dist      = 100000.0;
    int    i, closest_ghost = -1;
    
    for(GHOST ghost : GHOST.values()){
      i = gs.getGhostCurrentNodeIndex(ghost);
      d = gs.getShortestPathDistance(current, i);
      if(d < min_dist){
        min_dist = d;
        closest_ghost = i;
      }
    }
    if(gs.getLInftyDistance(current, closest_ghost) <= ws){
      move = gs.getNextMoveAwayFromTarget(current, closest_ghost, DM.PATH);      
    }

    return move;

  }



  // EatNearestGhost(): goes towards nearest edible ghost (or do nothing if no edible ghosts)
  public static MOVE ngd(Game gs){

    MOVE   move             = MOVE.NEUTRAL;
    int    current          = gs.getPacmanCurrentNodeIndex();
    double d, min_dist      = 100000.0;
    int    i, closest_ghost = -1;
    
    for(GHOST ghost : GHOST.values()){
      if(gs.isGhostEdible(ghost)){
        i = gs.getGhostCurrentNodeIndex(ghost);
        d = gs.getShortestPathDistance(current, i);
        if(d < min_dist){
          min_dist = d;
          closest_ghost = i;
        }
      }
    }
    if(closest_ghost != -1){
      move = gs.getNextMoveTowardsTarget(current, closest_ghost, DM.PATH);      
    }

    return move;

  }


  // Computes the (rounded) average distance to ghosts
  public static int adtg(Game gs){

    double s = 0, z = 0;
    int    current  = gs.getPacmanCurrentNodeIndex();

    for(GHOST ghost : GHOST.values()){
      s += gs.getShortestPathDistance(current, gs.getGhostCurrentNodeIndex(ghost));
      z += 1.0;
    }

    return (int)Math.round(s / z);

  }


  // Computes how many edible ghosts are there
  public static int eg(Game gs){

    int n_edible_ghosts = 0;

    for(GHOST ghost : GHOST.values()){
      if(gs.isGhostEdible(ghost)){
        n_edible_ghosts += 1;
      }
    }

    return n_edible_ghosts;

  }


  // Computes how many active power pills there are
  public static int ppd(Game gs){

    return get_active_power_pills(gs).length;

  }


  // Computes distance to nearest *in*edible ghost, if any
  public static int nig(Game gs){

    double d, min_dist = 100000.0;
    int    current     = gs.getPacmanCurrentNodeIndex();
    
    for(GHOST ghost : GHOST.values()){
      if(!gs.isGhostEdible(ghost)){
        d = gs.getShortestPathDistance(current, gs.getGhostCurrentNodeIndex(ghost));
        if(d < min_dist){
          min_dist = d;
        }
      }
    }

    return (int)Math.round(min_dist);

  }


  // GetClosest()
  public static MOVE gct(int which, Game gs, int ws){

    MOVE move = MOVE.NEUTRAL;

    if(which == 0){
      return npd(gs);
    }
    else if(which == 1){
      return ang(gs, ws);
    }
    else if(which == 2){
      return ngd(gs);
    }
    else if(which == 3){
      return nppd(gs);
    }

    return move;

  }


  public static int[] get_active_power_pills(Game gs){

    int[] active_power_pills;
    int[] all_power_pills = gs.getPowerPillIndices();
    ArrayList<Integer> active_power_pills_list = new ArrayList<Integer>();
    
    for(int i=0; i<all_power_pills.length; i++){
      if(gs.isPowerPillStillAvailable(i)){ 
        active_power_pills_list.add(all_power_pills[i]); 
      }
    }

    active_power_pills = new int[active_power_pills_list.size()];
    for(int i=0; i<active_power_pills.length; i++){
      active_power_pills[i] = active_power_pills_list.get(i);
    }
    
    return active_power_pills;

  }


  public static int[] get_active_pills(Game gs){

    int[] active_pills;
    int[] all_pills = gs.getPillIndices();
    ArrayList<Integer> active_pills_list = new ArrayList<Integer>();
    
    for(int i=0; i<all_pills.length; i++){
      if(gs.isPillStillAvailable(i)){ 
        active_pills_list.add(all_pills[i]); 
      }
    }

    active_pills = new int[active_pills_list.size()];
    for(int i=0; i<active_pills.length; i++){
      active_pills[i] = active_pills_list.get(i);
    }
    
    return active_pills;

  }


}













