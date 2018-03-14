import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by dolci on 3/2/2018.
 */
public class ReadFile {
    String filePath;

    ReadFile(String filePath) {
        this.filePath = filePath;
    }

    public ArrayList<int[][]> fileToMatrix() throws FileNotFoundException {
        int[][] flowtMatrix;
        int[][] distanceMatrix;
        int length =-1;

        Scanner scanner = new Scanner(new BufferedReader(new FileReader(new File(filePath))));
        length=scanner.nextInt();

        flowtMatrix=new int[length][length];
        distanceMatrix=new int[length][length];

        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                int x=scanner.nextInt();
                flowtMatrix[i][j] = x;
            }
        }

        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                int x=scanner.nextInt();
                distanceMatrix[i][j] = x;
            }

        }

        ArrayList<int[][]> arrayList = new ArrayList(2);
        arrayList.add(flowtMatrix);
        arrayList.add(distanceMatrix);
        return arrayList;
    }

}


