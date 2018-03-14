import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * Created by dolci on 3/12/2018.
 */
public class Algorithm {
    int POP_SIZE=100; //wielkosc populacji
    int GEN=100; //liczba pokolen
    double Px=0.7; //prawdopodobienstwo krzyzowania
    double Pm=0.01; //prawdopdobienstow mutacji
    int Tour=5; //liczba osobnikow w turnieju

    Chromosom[] population= new Chromosom[POP_SIZE];

    public int[] RandomizeArray(int[] array){
        Random rgen = new Random();  // Random number generator

        for (int i=0; i<array.length; i++) {
            int randomPosition = rgen.nextInt(array.length);
            int temp = array[i];
            array[i] = array[randomPosition];
            array[randomPosition] = temp;
        }

        return array;
    }

    public void generatePopulation(int chromosomsLength,int[][] flowMatrix, int[][] distanceMatrix){
        int[] initialChromosom=new int[chromosomsLength];

        for (int j = 0; j < chromosomsLength ; j++) {
            initialChromosom[j]=j;
        }

        for (int i = 0; i <POP_SIZE ; i++) {
            int[] array= Arrays.copyOf(RandomizeArray(initialChromosom),chromosomsLength);
            population[i]=new Chromosom(array,flowMatrix,distanceMatrix);
        }
    }

    public int cost(Chromosom chromosom,int[][] flowMatrix, int[][] distanceMatrix){
        int[] vector=chromosom.getVector();
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

    public Chromosom[] crossover(){
        Chromosom[] chromosoms=new Chromosom[2];

        Random random=new Random();

        int random1=random.nextInt(population.length);
        int random2=random.nextInt(population.length);

        if(Math.random()>=Px){
            chromosoms[0]=population[random1].crossover(5,population[random2])[0];
            chromosoms[1]=population[random1].crossover(5,population[random2])[1];
        }else {
            chromosoms[0]=population[random1];
            chromosoms[1]=population[random2];
        }


        return  chromosoms;
    }

    public void rulette(){
        double sumCost=0;
        double[] costs=new double[population.length];
        double[] rouletteWheel=new double[population.length];
        double probabilitySum=0;
        Chromosom[] newPopulation=new Chromosom[population.length];

        for (int i = 0; i < population.length ; i++) {
            double tmp=population[i].cost();
            sumCost+=1/tmp;
            costs[i]=1/tmp;

        }
        for (int i = 0; i < population.length ; i++) {
            probabilitySum+=costs[i]/sumCost;
            rouletteWheel[i]=probabilitySum;
        }
        rouletteWheel[population.length-1]=1.0;

        for (int i = 0; i <population.length ; i++) {
            double random=Math.random();
            for (int j = 0; j < rouletteWheel.length; j++) {
                if(random<=rouletteWheel[j]){
                    newPopulation[i]=population[j];
                    break;
                }
            }
        }

        population=newPopulation;
    }

    public void tournament(){
        Chromosom[] newPopulation=new Chromosom[population.length];
        Random random=new Random();

        for (int k = 0; k < population.length ; k++) {

            int [] tournament=new int[Tour];
            int[] costs = new int[Tour];
            for (int i = 0; i <tournament.length ; i++) {
                tournament[i]=random.nextInt(POP_SIZE);
                costs[i]=population[tournament[i]].cost();
            }
            int tmp[]=costs.clone();
            Arrays.sort(tmp);
            int best=0;
            for (int i = 0; i <costs.length ; i++) {
                if(tmp[0]==costs[i]){
                    best=i;
                    break;
                }
            }
             newPopulation[k]=population[tournament[best]];
        }

        population=newPopulation;
    }

    public void mutation(){
        for (int i = 0; i < population.length ; i++) {
            if(Math.random()>=Pm){
                population[i]=population[i].mutation();
            }
        }
    }

    public double[] results(){
        double[] results= new double[3];
        int[] costs= new int[POP_SIZE];
        double sumCost=0;

        for (int i = 0; i < POP_SIZE ; i++) {
            int tmp=population[i].cost();
            costs[i]=tmp;
            sumCost+=tmp;
        }

        Arrays.sort(costs);
        results[0]=costs[0];
        results[1]=sumCost/POP_SIZE;
        results[2 ]=costs[POP_SIZE-1];
        return results;
    }

    public void runAlgorithmWithTour(String filePath,String resultPath) throws FileNotFoundException {
        ReadFile readFile=new ReadFile(filePath);
        ArrayList<int[][]> arrayList=readFile.fileToMatrix();
        int[][] flowMatrix =arrayList.get(0);
        int[][] distanceMatrix = arrayList.get(1);
        this.generatePopulation(flowMatrix[0].length,flowMatrix,distanceMatrix);

        double[][] results=new double[GEN][4];

        for (int i = 0; i <GEN-1 ; i++) {
            double[] tmpResult=this.results();

            results[i][0]=i;
            results[i][1]=tmpResult[0];
            results[i][2]=tmpResult[1];
            results[i][3]=tmpResult[2];

            this.tournament();
            this.crossover();
            this.mutation();
        }
        double[] tmpResult=this.results();

        results[GEN-1][0]=GEN-1;
        results[GEN-1][1]=tmpResult[0];
        results[GEN-1][2]=tmpResult[1];
        results[GEN-1][3]=tmpResult[2];

        this.resultsToFile(results,resultPath);
    }

    public void runAlgorithmWithRoulette(String filePath,String resultPath) throws FileNotFoundException {
        ReadFile readFile=new ReadFile(filePath);
        ArrayList<int[][]> arrayList=readFile.fileToMatrix();
        int[][] flowMatrix =arrayList.get(0);
        int[][] distanceMatrix = arrayList.get(1);
        this.generatePopulation(flowMatrix[0].length,flowMatrix,distanceMatrix);

        double[][] results=new double[GEN][4];

        for (int i = 0; i <GEN-1 ; i++) {
            double[] tmpResult=this.results();

            results[i][0]=i;
            results[i][1]=tmpResult[0];
            results[i][2]=tmpResult[1];
            results[i][3]=tmpResult[2];

            this.rulette();
            this.crossover();
            this.mutation();
        }
        double[] tmpResult=this.results();

        results[GEN-1][0]=GEN-1;
        results[GEN-1][1]=tmpResult[0];
        results[GEN-1][2]=tmpResult[1];
        results[GEN-1][3]=tmpResult[2];

        this.resultsToFile(results,resultPath);
    }

    public void resultsToFile(double[][] array ,String filePathName){
        try {
            File file = new File(filePathName);
            file.setWritable(true);
            file.setReadable(true);
            FileWriter fileWriter = new FileWriter(filePathName);
            BufferedWriter writer =new BufferedWriter(fileWriter);
            writer.write("PopulationNumber,Best,Average,Worst");
            writer.newLine();
            for (int i = 0; i <GEN ; i++) {
                writer.write(array[i][0]+","+array[i][1]+","+array[i][2]+","+array[i][3]);
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void randomAlgorithm(String filePath,String resultPath) throws FileNotFoundException {
        ReadFile readFile=new ReadFile(filePath);
        ArrayList<int[][]> arrayList=readFile.fileToMatrix();
        int[][] flowMatrix =arrayList.get(0);
        int[][] distanceMatrix = arrayList.get(1);
        this.generatePopulation(flowMatrix[0].length,flowMatrix,distanceMatrix);

        double[][] results=new double[GEN][4];

        for (int i = 0; i <GEN-1 ; i++) {
            double[] tmpResult=this.results();

            results[i][0]=i;
            results[i][1]=tmpResult[0];
            results[i][2]=tmpResult[1];
            results[i][3]=tmpResult[2];

            this.generatePopulation(flowMatrix[0].length,flowMatrix,distanceMatrix);
        }
        double[] tmpResult=this.results();

        results[GEN-1][0]=GEN-1;
        results[GEN-1][1]=tmpResult[0];
        results[GEN-1][2]=tmpResult[1];
        results[GEN-1][3]=tmpResult[2];

        this.resultsToFile(results,resultPath);

    }

    public static void main(String[] args) throws FileNotFoundException {
        Algorithm algorithm=new Algorithm();
        algorithm.runAlgorithmWithRoulette("C:/Users/dolci/Desktop/had12.dat","C:\\Users\\dolci\\Desktop\\wyniki\\had12result.csv");
        algorithm.randomAlgorithm("C:/Users/dolci/Desktop/had12.dat","C:\\Users\\dolci\\Desktop\\wyniki\\randomhad12result.csv");
    }

}
