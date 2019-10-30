import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public BlockStacking {

    int n;
    ArrayList<ArrayList<Integer>> blocks;

    public BlockStacking(File input) {
        blocks = new ArrayList<ArrayList<Integer>>();
    }

    private static ArrayList<ArrayList<Integer>> generate(File input) {
        Scanner sc = new Scanner(input);
        sc.useDelimiter("\n");
        n = sc.next();
        
        ArrayList<Integer> block;
        String[] line;

        while (sc.hasNext()) {
            block = new ArrayList<Integer>(n);
            line = sc.next().split("\\s");

            for(String s : line)
                block.add(Integer.parseInt(s));

            if(blocks.get(0) > blocks.get(1)){
                int temp = blocks.get(0);
                blocks.add(0, blocks.get(1));
                blocks.add(1, temp);
            }
            
            blocks.add(block);
        }

    }

    public static void main(String[] args) {
    }

}
