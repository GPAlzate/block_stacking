import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.HashMap;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;

public class BlockStacking {
    
    /** number of types of blocks */
    int n;

    /** stores all the different types of blocks and their rotations */
    ArrayList<ArrayList<Integer>> blocks;

    /**
     * dp table where heightsDP(i) is the maximum block stacking height if we
     * use base i
     */
    HashMap<ArrayList<Integer>, Integer> heightsDP;

    public BlockStacking(File input) {
        blocks = new ArrayList<ArrayList<Integer>>();
        heightsDP = new HashMap<ArrayList<Integer>, Integer>();
        generate(input);
    }

    /**
     * Generates all the possible block rotations that we can use to create
     * our stack. Also sorts them by the 1st face dimension so we can iterate
     * through the blocks from smallest to largest so `stackBlocks()` can
     * calculate maximum heights using the optimal solution for every base block.
     * 
     * @param input the file containing all the different types of blocks
     */
    private void generate(File input) {

        try {

            // split file by new line
            Scanner sc = new Scanner(input);
            sc.useDelimiter("\n");

            // read in number of blocks
            n = Integer.parseInt(sc.next());

            // holds the block dimensions
            ArrayList<Integer> block;

            // the line from the file that holds block timensions
            String[] line;

            // read in all blocks
            while (sc.hasNext()) {

                // create block with dimensions l * w * h
                block = new ArrayList<Integer>(3);
                line = sc.next().split("\\s");
                for(String s : line)
                    block.add(Integer.parseInt(s));

                // smallest dimension of the block goes first for easy comparison
                // later
                Collections.sort(block);
                
                // put all rotations of block, assigning initial stack height to
                // its own height
                heightsDP.put(new ArrayList<> (block), block.get(2));

                block.add(1, block.remove(2));
                heightsDP.put(new ArrayList<> (block), block.get(2));

                block.add(0, block.remove(2));
                block.add(1, block.remove(2));
                heightsDP.put(new ArrayList<> (block), block.get(2));

            }

            // create the sorted list of blocks
            blocks = new ArrayList<ArrayList<Integer>>(heightsDP.keySet());
            Collections.sort(blocks, new Comparator<ArrayList<Integer>>(){
                    public int compare(ArrayList<Integer> a, ArrayList<Integer> b){
                        return a.get(0) - b.get(0);
                    }
                });


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private int stackBlocks(){

        // base case, 1 block in stack has max height of itself
        // heightsDP.put(blocks.get(0), blocks.get(0).get(2));

        // vbls to save lists: `base` holds the working base block, while
        // `current` holds the blocks we're stacking on top of the base
        ArrayList<Integer> base = null, current = null;

        // saves the maximum stack height
        int globalMax = -1;

        for(int i = 1; i < blocks.size(); i++){

            // iterate through the blocks if we were to use them as the base
            base = blocks.get(i);

            // height of current base
            int baseHeight = base.get(2);

            // calculate maximum height using the current base
            int localMax = baseHeight;
            for(int j = 0; j < i; j++){

                // placing current on top of base
                current = blocks.get(j);

                // can only place if length & width are smaller than those of base
                if(current.get(0) < base.get(0) && 
                        current.get(1) < base.get(1)){

                    // stack the substacks onto base to get total height
                    int dp = baseHeight + heightsDP.get(current);

                    // we can add the block to the base, so we accumulate height
                    if(dp > localMax)
                        localMax = dp;

                }
            }

            // update the maximum height of the base used
            heightsDP.put(base, localMax);

            // if we find a new tallest stack, replace it
            if(localMax > globalMax)
                globalMax = localMax;
        }

        return globalMax;

    }

    public static void main(String[] args) {
        File input = new File(args[0]);
        BlockStacking test = new BlockStacking(input);
        System.out.println(test.blocks.toString());
        System.out.println(test.stackBlocks());
        System.out.println(test.heightsDP.toString());
    }

}
