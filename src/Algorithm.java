import java.io.*;
import java.lang.reflect.Array;
import java.util.*;

/**
 * Created by dolci on 3/12/2018.
 */
public class Algorithm {
    int POP_SIZE=100; //wielkosc populacji
    int GEN=100; //liczba pokolen
    double Px=0.7; //prawdopodobienstwo krzyzowania
    double Pm=0.01; //prawdopdobienstow mutacji
    int Tour=5; //liczba osobnikow w turnieju

    private Random random=new Random();

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

        List<Chromosom> competitors=new ArrayList<>();
        for (int i = 0; i < population.length ; i++) {
            competitors.add(population[i]);

        }
        competitors.sort(new Comparator<Chromosom>() {
            @Override
            public int compare(Chromosom o1, Chromosom o2) {
                if (o1.cost() > o2.cost()) return 1;
                else if (o1.cost() == o2.cost()) return 0;
                else return -1;
            }
        });



        double min=competitors.get(0).cost();
       // System.out.println("wektor rozwiazan"+Arrays.toString(competitors.get(0).getVector()));
      //  System.out.println("koszt "+competitors.get(0).cost());
        double max=competitors.get(POP_SIZE-1).cost();
        ArrayList<Chromosom> roulettePopulation= new ArrayList<>();

        for (int i = 0; i < 40 ; i++) {
            double current=1.0/competitors.get(i).cost();
            double normalization =(current-min)/(max-min);
            int howManyToAdd = (int) ((1-normalization)*100.00);
            for (int j = 0; j <howManyToAdd ; j++) {
                roulettePopulation.add(competitors.get(i));
            }
        }

        ArrayList<Chromosom> selectedPopulation = new ArrayList<>();
        for(int k = 0 ; k<POP_SIZE; k++) {
            selectedPopulation.add(roulettePopulation.get(random.nextInt(roulettePopulation.size()-1)));
        }

        for (int i = 0; i < POP_SIZE ; i++) {
            newPopulation[i]=selectedPopulation.get(i);
        }

        //System.out.println(newPopulation.toString());
       // System.out.println(min+"<"+max);
        population=newPopulation;
      /*  for (int i = 0; i <population.length ; i++) {
            costs[i]=competitors.get(POP_SIZE-1).cost()+competitors.get(0).cost()-competitors.get(i).cost();
            sumCost+=costs[i];
        }

        for (int i = 0; i < population.length ; i++) {
            probabilitySum+=costs[i]/sumCost;
            rouletteWheel[i]=probabilitySum;
        }
        for (int i = 0; i <rouletteWheel.length ; i++) {

            System.out.println(rouletteWheel[i]);
        }
        for (int i = 0; i <population.length ; i++) {
            double random=Math.random();
            for (int j = 0; j < rouletteWheel.length; j++) {
                if(random<=rouletteWheel[j]){
                    newPopulation[i]=population[j];
                    break;
                }
            }
        }*/


       /* for (int i = 0; i < population.length ; i++) {
            double tmp=population[i].cost();
            sumCost+=1/tmp;
            costs[i]=1/tmp;

        }
        */
        /*for (int i = 0; i < population.length ; i++) {
            probabilitySum+=costs[i]/sumCost;
            rouletteWheel[i]=probabilitySum;
        }
        for (int i = 0; i <rouletteWheel.length ; i++) {
            System.out.println(rouletteWheel[i]);
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
        }*/

        //population=newPopulation;
    }

    public void tournament(){
        Chromosom[] newPopulation=new Chromosom[population.length];
        Random random=this.random;
        for (int k = 0; k < population.length ; k++) {
            List<Chromosom> competitors=new ArrayList<>();
            for (int i = 0; i < Tour ; i++) {
                competitors.add(population[random.nextInt(POP_SIZE)]);
            }
            competitors.sort(new Comparator<Chromosom>() {
                @Override
                public int compare(Chromosom o1, Chromosom o2) {
                    if (o1.cost() > o2.cost()) return 1;
                    else if (o1.cost() == o2.cost()) return 0;
                    else return -1;
                }
            });
            newPopulation[k]=competitors.get(0);
            //System.out.print( competitors.get(0).cost()+"<"+competitors.get(Tour-1).cost()+" |");
        }
       // System.out.println();


      /*  for (int k = 0; k < population.length ; k++) {

            int [] tournament=new int[Tour];
            int[] costs = new int[Tour];
            List<Chromosom> competitors = new ArrayList<>();
            for (int i = 0; i <tournament.length ; i++) {
                //tournament[i]=random.nextInt(POP_SIZE);
                competitors.add(population[random.nextInt(POP_SIZE)]);
            //    costs[i]=population[tournament[i]].cost();
            }
            competitors.sort(new Comparator<Chromosom>() {
                @Override
                public int compare(Chromosom o1, Chromosom o2) {
                    if (o1.cost() > o2.cost()) return 1;
                    else if (o1.cost() == o2.cost()) return 0;
                    else return -1;
                }
            });

            //int tmp[]=costs.clone();
            //Arrays.sort(tmp);
            //int best=0;
            //for (int i = 0; i <costs.length ; i++) {
            //    if(tmp[0]==costs[i]){
            //        best=i;
            //        break;
              //  }
            //}
             newPopulation[k]=competitors.get(0);
        }*/

        population=newPopulation;
    }

    public void mutation(){
        /*for (int i = 0; i < population.length ; i++) {
            if(Math.random()>=Pm){
                population[i]=population[i].mutation();
            }
        }*/
        for (int i = 0; i < population.length ; i++) {
            population[i]=population[i].mutation(Pm);
            /*if(Math.random()>=Pm){
            }*/
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

        double[][] sumResults=new double[GEN*10][4];
        for (int k = 0; k < 10 ; k++) {
            this.generatePopulation(flowMatrix[0].length,flowMatrix,distanceMatrix);

            double[][] results = new double[GEN][4];
            for (int i = 0; i < GEN - 1; i++) {
                double[] tmpResult = this.results();

                results[i][0] = i;
                results[i][1] = tmpResult[0];
                results[i][2] = tmpResult[1];
                results[i][3] = tmpResult[2];

                this.tournament();
                this.crossover();
                this.mutation();
            }
            double[] tmpResult = this.results();

            results[GEN - 1][0] = GEN - 1;
            results[GEN - 1][1] = tmpResult[0];
            results[GEN - 1][2] = tmpResult[1];
            results[GEN - 1][3] = tmpResult[2];

            for (int n = k*GEN; n <(k*GEN+GEN) ; n++) {
                sumResults[n][0]=results[n%GEN][0];
                sumResults[n][1]=results[n%GEN][1];
                sumResults[n][2]=results[n%GEN][2];
                sumResults[n][3]=results[n%GEN][3];
                //System.out.print(n+",");
            }
        }
     /*   System.out.println(sumResults.length+" dlugosc");
        System.out.println(sumResults[999][1]+" wartosc 1000 0");
        System.out.println(sumResults[199][1]+" wartosc 200 0");
        System.out.println(sumResults[99][1]+" wartosc 100 0");*/

        this.resultsToFile(sumResults,resultPath);
    }

    public void runAlgorithmWithRoulette(String filePath,String resultPath) throws FileNotFoundException {
        ReadFile readFile=new ReadFile(filePath);
        ArrayList<int[][]> arrayList=readFile.fileToMatrix();
        int[][] flowMatrix =arrayList.get(0);
        int[][] distanceMatrix = arrayList.get(1);

        double[][] sumResults=new double[GEN*10][4];
        for (int k = 0; k < 10 ; k++) {
            this.generatePopulation(flowMatrix[0].length,flowMatrix,distanceMatrix);

            double[][] results = new double[GEN][4];
            for (int i = 0; i < GEN - 1; i++) {
                double[] tmpResult = this.results();

                results[i][0] = i;
                results[i][1] = tmpResult[0];
                results[i][2] = tmpResult[1];
                results[i][3] = tmpResult[2];

                this.rulette();
                this.crossover();
                this.mutation();
            }
            double[] tmpResult = this.results();

            results[GEN - 1][0] = GEN - 1;
            results[GEN - 1][1] = tmpResult[0];
            results[GEN - 1][2] = tmpResult[1];
            results[GEN - 1][3] = tmpResult[2];

            for (int n = k*100; n <(k*100+100) ; n++) {
                sumResults[n][0]=results[n%100][0];
                sumResults[n][1]=results[n%100][1];
                sumResults[n][2]=results[n%100][2];
                sumResults[n][3]=results[n%100][3];
                //System.out.print(n+",");
            }
        }
     /*   System.out.println(sumResults.length+" dlugosc");
        System.out.println(sumResults[999][1]+" wartosc 1000 0");
        System.out.println(sumResults[199][1]+" wartosc 200 0");
        System.out.println(sumResults[99][1]+" wartosc 100 0");*/

        this.resultsToFile(sumResults,resultPath);

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
            for (int i = 0; i <array.length ; i++) {
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

        double[][] sumResults=new double[GEN*10][4];
        for (int k = 0; k < 10 ; k++) {
           //this.generatePopulation(flowMatrix[0].length,flowMatrix,distanceMatrix);

            double[][] results = new double[GEN][4];
            for (int i = 0; i < GEN - 1; i++) {
                this.generatePopulation(flowMatrix[0].length,flowMatrix,distanceMatrix);
                double[] tmpResult = this.results();

                results[i][0] = i;
                results[i][1] = tmpResult[0];
                results[i][2] = tmpResult[1];
                results[i][3] = tmpResult[2];

             //   this.rulette();
               // this.crossover();
                //this.mutation();
            }
            double[] tmpResult = this.results();

            results[GEN - 1][0] = GEN - 1;
            results[GEN - 1][1] = tmpResult[0];
            results[GEN - 1][2] = tmpResult[1];
            results[GEN - 1][3] = tmpResult[2];

            for (int n = k*100; n <(k*100+100) ; n++) {
                sumResults[n][0]=results[n%100][0];
                sumResults[n][1]=results[n%100][1];
                sumResults[n][2]=results[n%100][2];
                sumResults[n][3]=results[n%100][3];
                //System.out.print(n+",");
            }
        }

        this.resultsToFile(sumResults,resultPath);
    }

    public void greedyAlgorithm(String filePath,String resultPath) throws FileNotFoundException {
        ReadFile readFile=new ReadFile(filePath);
        ArrayList<int[][]> arrayList=readFile.fileToMatrix();
        int[][] flowMatrix =arrayList.get(0);
        int[][] distanceMatrix = arrayList.get(1);
        this.generatePopulation(flowMatrix[0].length,flowMatrix,distanceMatrix);

       /* double[][] results=new double[GEN][4];

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

        this.resultsToFile(results,resultPath);*/
        //Random random = new Random();
        int[] results=new int[400];
        int counter=0;
        int startFabric;
        int startFabric2;
        for (int x = 0; x <20 ; x++) {
            for (int s = 0; s <20 ; s++) {
                if(x==s){

                } else {
                    startFabric=x;
                    startFabric2=s;
                    //int startFabric=random.nextInt(flowMatrix[0].length);
                    int[] myvector=new int[flowMatrix[0].length];
                    for (int i = 0; i < myvector.length; i++) {
                        myvector[i]=startFabric;
                    }
                    //int startFabric2=random.nextInt(flowMatrix[0].length);
                    myvector[1]=startFabric2;
                    HashSet<Integer> placedFabric=new HashSet<>();
                    placedFabric.add(startFabric);

                    // System.out.println(Arrays.toString(myvector));
                    int currentBestCost=1000000;
                    int currentBestFabric=0;
                    for (int i = 2; i <myvector.length ; i++) {
                        for (int j = 0; j < myvector.length ; j++) {
                            if(placedFabric.contains(j)){
                                //  System.out.println("contains:"+j);
                            } else {
                                myvector[i]=j;
                                // System.out.println(Arrays.toString(myvector));
                                int tmpCost=new Chromosom(myvector,flowMatrix,distanceMatrix).cost();
                                if(tmpCost<currentBestCost){
                                    currentBestCost=tmpCost;
                                    currentBestFabric=j;
                                }
                            }
                        }
                        placedFabric.add(currentBestFabric);
                        myvector[i]=currentBestFabric;
                        currentBestCost=100000;
                    }
                    results[counter]=new Chromosom(myvector,flowMatrix,distanceMatrix).cost();
                    counter++;
                }
            }
        }

        try {
            File file = new File(resultPath);
            file.setWritable(true);
            file.setReadable(true);
            FileWriter fileWriter = new FileWriter(resultPath);
            BufferedWriter writer =new BufferedWriter(fileWriter);
            for (int i = 0; i <results.length ; i++) {
                System.out.println(results[i]);
                writer.write(results[i]+",");
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

       // System.out.println(Arrays.toString(myvector));
       // Chromosom mychrom = new Chromosom(myvector,flowMatrix,distanceMatrix);
       // System.out.println("koszt "+mychrom.cost());
        //int[] myvector2={8,15,16,14,19,6,7,17,1,12,10,11,5,20,2,3,4,9,18,13};
        //int[] myvector2={7,14,15,13,18,5,6,16,0,11,9,10,4,19,1,2,3,8,17,12};
        //int[] myvector2={2,9,10,1,11,4,5,6,7,0,3,8};
       // Chromosom chromosom=new Chromosom(myvector2,flowMatrix,distanceMatrix);
       // System.out.println(chromosom.cost());
        /*System.out.println("flow");
        for (int i = 0; i <flowMatrix[0].length ; i++) {
            for (int j = 0; j <flowMatrix[0].length ; j++) {
                System.out.print(flowMatrix[i][j]+",");
            }
            System.out.println();
        }
        System.out.println("distance");
        for (int i = 0; i <flowMatrix[0].length ; i++) {
            for (int j = 0; j <flowMatrix[0].length ; j++) {
                System.out.print(distanceMatrix[i][j]+",");
            }
            System.out.println();
        }*/

    }
    public static void main(String[] args) throws FileNotFoundException {
        Algorithm algorithm=new Algorithm();
        long startTime = System.currentTimeMillis();
        algorithm.runAlgorithmWithTour("C:/Users/dolci/Desktop/had20.dat","C:\\Users\\dolci\\Desktop\\wyniki\\resultTour.csv");
        long stopTime = System.currentTimeMillis();
        long elapsedTime = stopTime - startTime;
        System.out.println(elapsedTime);
        long startTime2 = System.currentTimeMillis();
        algorithm.runAlgorithmWithRoulette("C:/Users/dolci/Desktop/had20.dat","C:\\Users\\dolci\\Desktop\\wyniki\\resultRoulette.csv");
        long stopTime2 = System.currentTimeMillis();
        long elapsedTime2 = stopTime2 - startTime2;
        System.out.println(elapsedTime2);
        long startTime3 = System.currentTimeMillis();
        algorithm.randomAlgorithm("C:/Users/dolci/Desktop/had20.dat","C:\\Users\\dolci\\Desktop\\wyniki\\random.csv");
        long stopTime3 = System.currentTimeMillis();
        long elapsedTime3 = stopTime3 - startTime3;
        System.out.println(elapsedTime3);

       algorithm.greedyAlgorithm("C:/Users/dolci/Desktop/had20.dat","C:\\Users\\dolci\\Desktop\\wyniki\\greedy.csv");
    }

}
