import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Random;

/**
 * Created by dolci on 3/12/2018.
 */
public class Chromosom {
    int[] vector;
    int[][] flowMatrix;
    int[][] distanceMatrix;

    public Chromosom(int[] vector,int[][] flowMatrix, int[][] distanceMatrix){
        this.vector=vector;
        this.flowMatrix=flowMatrix;
        this.distanceMatrix=distanceMatrix;
    }

    public String toString(){
        String array="";
        for (int i=0; i<vector.length; i++){
            array+=vector[i]+",";
        }
        return array;

    }

    public int[] getVector() {
        return vector;
    }

    public int cost(){
        int cost=0;
        for(int i=0; i<vector.length;i++){
            for(int j=i; j<vector.length;j++){
                if(vector[i]!=vector[j]) {
                    cost+=flowMatrix[vector[i]][vector[j]]*distanceMatrix[i][j];
                }
            }
        }

        return cost;
    }



    public  Chromosom[] crossover(int crossoverPoint,Chromosom chromosom){
        Chromosom[] crossovers=new Chromosom[2];

        int[] vector1= new int[this.vector.length];
        int[] vector2= new int[this.vector.length];

        for(int i=0; i<this.vector.length;i++){
            if(crossoverPoint<i){
                vector1[i]=this.vector[i];
                vector2[i]=chromosom.vector[i];
            }else {
                vector1[i]=chromosom.vector[i];
                vector2[i]=this.vector[i];
            }
        }


        //tutaj naprawa

        for (int i = 0; i<this.vector.length ; i++) {
            for (int j = i+1; j <this.vector.length ; j++) {
                if(vector1[i]==vector1[j]){
                    int tmp=vector1[i];
                    vector1[i]=vector2[i];
                    vector2[i]=tmp;
                }
            }
            //System.out.println("----------naprawa");
        }

        Chromosom child1 = new Chromosom(vector1,this.flowMatrix,this.distanceMatrix);
        Chromosom child2 = new Chromosom(vector2,this.flowMatrix,this.distanceMatrix);

        crossovers[0]=child1;
        crossovers[1]=child2;
        return crossovers;
    }

    public  Chromosom mutation(double pm){
        int[] mutateVector= Arrays.copyOf(this.vector,this.vector.length);

        /*System.out.println("przedmutacja"+Arrays.toString(mutateVector));
        for (int i = 0; i <vector.length ; i++) {
            if(Math.random()<=pm){
                int tmp=vector[i];
                if(i==vector.length-1){
                    mutateVector[i]=vector[0];
                    mutateVector[0]=tmp;
                } else {
                    System.out.println("-----------");
                    System.out.println(vector[i+1]);
                    System.out.println(tmp);
                    System.out.println("-----------");
                    mutateVector[i]=vector[i+1];
                    mutateVector[i+1]=tmp;
                }
            }
        }
        System.out.println("pomutacji"+Arrays.toString(mutateVector));*/
      //  System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
      //  System.out.println(Arrays.toString(vector));
      //  System.out.println(Arrays.toString(mutateVector));
        //brac pierwszy gen losowac jeden

        int px;
        int py;

        Random random=new Random();

        px=random.nextInt(this.vector.length);
        py=random.nextInt(this.vector.length);

        while (px==py){
            py=random.nextInt(this.vector.length);
        }

        mutateVector[px]=this.vector[py];
        mutateVector[py]=this.vector[px];

        return new Chromosom(mutateVector,this.flowMatrix,this.distanceMatrix);

    }
}
