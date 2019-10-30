import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.HashMap;
import java.util.Collections;
import java.util.ArrayList;
import java.util.Comparator;

public class BlockStacking {

    int n;
    ArrayList<ArrayList<Integer>> blocks;
    HashMap<ArrayList<Integer>, Integer> heights;

    public BlockStacking(File input) {
        blocks = new ArrayList<ArrayList<Integer>>();
        heights = new HashMap<ArrayList<Integer>, Integer>();
        generate(input);
    }

    private void generate(File input) {

        try {
            Scanner sc = new Scanner(input);
            sc.useDelimiter("\n");
            n = Integer.parseInt(sc.next());

            ArrayList<Integer> block;
            String[] line;

            while (sc.hasNext()) {
                block = new ArrayList<Integer>(n);
                line = sc.next().split("\\s");

                for(String s : line)
                    block.add(Integer.parseInt(s));

                Collections.sort(block);
                
                heights.put(new ArrayList<> (block), 0);

                block.add(1, block.remove(2));
                heights.put(new ArrayList<> (block), 0);

                block.add(0, block.remove(2));
                block.add(1, block.remove(2));
                heights.put(new ArrayList<> (block), 0);

            }

            blocks = new ArrayList<ArrayList<Integer>>(heights.keySet());
            Collections.sort(blocks, new Comparator<ArrayList<Integer>>(){

                    public int compare(ArrayList<Integer> a, ArrayList<Integer> b){
                        return a.get(0) - b.get(0);
                    }

                });


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void stackBlocks(){

        // base case, 1 block in stack has max height of itself
        heights.put(blocks.get(0), blocks.get(0).get(2));

        List<Integer> current

        for(int i = 1; i < blocks.size(); i++){
            for(int j = 0; j < i; j++){

                

            }
        }

    }

    public static void main(String[] args) {
        File input = new File(args[0]);
        BlockStacking test = new BlockStacking(input);
        System.out.println(test.blocks.toString());
    }

}
