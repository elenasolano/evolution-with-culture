// CS81 - Final Project
// Maria-Elena Solano

package main;
import  java.util.List;
import  java.util.ArrayList;

// This class implements a chromosome of possible values.
public class pop extends ArrayList<chromo>{

  final int          length;
  final int          chromo_length;
  final List<Number> alleles;
  List<Double>       fitness;


  public pop(int _length, int _chromo_length, List<Number> _alleles){
    super();

    length        = _length;
    chromo_length = _chromo_length;
    alleles       = _alleles;
    fitness       = new ArrayList<Double>();

    for(int i=0; i < length; i++){
      add(new chromo(chromo_length, alleles));
      fitness.add(0.0);
    }

  }


  public pop copy(){
    pop P_new = new pop(length, chromo_length, alleles);

    for(int i=0; i<length; i++){
      P_new.set(i, get(i));
    }

    return P_new;
  }

  
  public String toString(String prefix){

    String str = "";

    for(int i=0; i<length; i++){
      str += String.format("%s|%s|%s\n", prefix, fitness.get(i), get(i));
    }

    return str;

  }

}


























