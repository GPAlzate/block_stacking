import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.HashMap;
import java.util.Collections;
import java.util.ArrayList;
import java.util.Comparator;

public class BlockStacking {

    int n;
    ArrayList<Block> blocks;
    HashMap<Block, Integer> heights;

    public BlockStacking(File input) {
        blocks = new ArrayList<Block>();
        heights = new HashMap<Block, Integer>();
        generate(input);
    }

    private void generate(File input) {

        try {
            Scanner sc = new Scanner(input);
            sc.useDelimiter("\n");
            n = Integer.parseInt(sc.next());

            Block block;
            String[] line;

            while (sc.hasNext()) {
                line = sc.next().split("\\s");

                ArrayList<Integer> dims = new ArrayList<Integer>();
                for (String s : line) {
                    dims.add(Integer.parseInt(s));
                }
                System.out.println(dims);
                heights.put(new Block(dims), 0);

                dims.add(1, dims.remove(2));
                heights.put(new Block(dims), 0);

                dims.add(0, dims.remove(2));
                dims.add(1, dims.remove(2));
                heights.put(new Block(dims), 0);
            }

            blocks = new ArrayList<Block>(heights.keySet());
            Collections.sort(blocks, new Block.sortByD1());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        File input = new File(args[0]);
        BlockStacking test = new BlockStacking(input);
        System.out.println(test.blocks.toString());
    }

}
