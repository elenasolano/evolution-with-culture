// CS81 - Final Project
// Maria-Elena Solano

package main;
import  java.util.List;
import  java.util.ArrayList;
import  java.util.Random;
import  java.security.SecureRandom;
import  java.nio.ByteBuffer;
import  java.util.stream.*;



public class np{


  public static long get_seed(){
    return ByteBuffer.wrap((new SecureRandom()).generateSeed(8)).getLong();
  }


  public static double uniform(){
    return uniform(1.0);
  }


  public static double uniform(double x){
    return (new Random(get_seed())).nextDouble()*x;
  }


  public static int randint(int to){
   return randint(0, to);
  }


  public static int randint(int from, int to){
   return from + (new Random(get_seed())).nextInt(to - from + 1);
  }


  public static double sum(List<Double> x){

    double s = 0.0;
    for(int i=0; i<x.size(); i++){
      s += x.get(i);
    }
    return s;

  }


  public static List<Double> normalize(List<Double> x){

    List<Double> x_hat = new ArrayList<Double>();
    double       Z = sum(x);

    for(int i=0; i<x.size(); i++){
      x_hat.add(x.get(i) / Z);
    }

    return x_hat;

  }


  public static <T> T sample_from(List<T> seq, List<Double> w){

    double  cumul = 0.0;
    double  spin  = np.uniform(sum(w));
    int     i;

    for(i=0; i<w.size(); i++){
      cumul += w.get(i);
      if(cumul >= spin){ break; };
    }

    return seq.get(i);

  }


  public static <T> T random_choice(List<T> seq){

    return seq.get(randint(seq.size() - 1));

  }


  public static List<Number> copy(List<Number> l){

    List<Number> new_l = new ArrayList<Number>();

    for(int i=0; i<l.size(); i++){
      new_l.add(l.get(i));  
    }

    return new_l;
  }


  public static List<Number> copy_except(List<Number> l, Number except){

    List<Number> new_l = new ArrayList<Number>();

    for(int i=0; i<l.size(); i++){
      Number x = l.get(i);
      if(x != except){
        new_l.add(x);  
      }
    }

    return new_l;
  }


  public static List<Number> range(int from, int to){
    return IntStream.range(from, to).boxed().collect(Collectors.toList());
  }


}

























