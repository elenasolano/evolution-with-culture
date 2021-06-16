// CS81 - Final Project
// Maria-Elena Solano

package main;
import  java.util.List;
import  java.util.ArrayList;


// This class implements a chromosome of possible values.
public class chromo extends ArrayList<Number>{

  final List<Number> alleles;


  public chromo(int length, List<Number> _alleles){

    super();
    alleles = _alleles;

    for(int i=0; i<length; i++){
      add(alleles.get(np.randint(alleles.size()-1)));
    }

  }


  public chromo copy(){

    chromo new_chromo = new chromo(size(), alleles);

    for(int i=0; i<size(); i++){
      new_chromo.set(i, get(i));
    }

    return new_chromo;

  }

  public String toString(){

    String str = "";

    for(int i=0; i<size(); i++){
      str += String.format("%s|", get(i));
    }
    str += String.format("%s|", ge_pacman_controller.decode_chromo(this));

    return str;

  }

}


























