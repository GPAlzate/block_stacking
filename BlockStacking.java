import java.io.File;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.util.HashMap;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * Class that solves the block stacking problem. The algorithm is as follows:
 * 
 *      - First generate all rotations for each block. We store them in a HashMap
 *      that maps every block orientation to what will eventually be the maximum
 *      height of the block tower with some block rotation `i` as the base.
 * 
 *      - For every block rotation, sort the dimensions such that the shortest
 *      side is the first dimension value.
 * 
 *      - We then sort the entire list of block rotations by the first (i.e. the
 *      shortest) dimension.
 * 
 *      - What we now have is a sorted list of blocks, and a mapping from each
 *      block `i` to the maximum height tower given blocks 0, 1, ..., i. Suppose
 *      we With this,
 *      TODO:
 * 
 * Running time:
 * 
 *      - We create 3n different block orientations (in the worst case) and then
 *      sort them, which takes Θ(n*log(n)) time.
 * 
 *      - We find the maximum block tower height in Θ(n^2) time. This is because
 *      for every block `i,` we find the maximum block tower height among blocks
 *      0, ..., i. The number of operations is therefore ∑i from 0 to 3n,
 *      which is in Θ(n^2). We have a tight bound because for every block before
 *      `i` in the sorted list, we have to compare the dimensions to see if the
 *      block is stackable.
 * 
 *      - Thus, the entire algorithm runs in Θ(n*log(n) + n^2) ∈ Θ(n^2). 
 *      
 * 
 */
public class BlockStacking {

    /** number of types of blocks */
    int n;

    /** stores all the different types of blocks and their rotations */
    ArrayList<Block> blocks;

    /**
     * dp table where stackHeight(i) is the maximum block stacking height if we
     * use base i
     */
    HashMap<Block, Integer> stackHeight;

    int maxHeight;
    Block maxBlock;

    public BlockStacking(File input, File output) {
        blocks = new ArrayList<Block>();
        stackHeight = new HashMap<Block, Integer>();
        maxHeight = 0;
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
            ArrayList<Integer> dims;

            // the line from the file that holds block timensions
            String[] line;

            // read in all blocks
            while (sc.hasNext()) {

                // create block with dimensions l * w * h
                dims = new ArrayList<Integer>(3);
                line = sc.next().split("\\s");
                for(String s : line)
                    dims.add(Integer.parseInt(s));

                // smallest dimension of the block goes first for easy comparison
                // later
                Collections.sort(dims);

                // put all rotations of block, assigning initial stack height to
                // its own height
                Block block = new Block(dims);
                stackHeight.put(block, block.getHeight());

                dims.add(1, dims.remove(2));
                block = new Block(dims);
                stackHeight.put(block, block.getHeight());

                dims.add(0, dims.remove(2));
                dims.add(1, dims.remove(2));
                block = new Block(dims);
                stackHeight.put(block, block.getHeight());

            }

            // create the sorted list of blocks
            blocks = new ArrayList<Block>(stackHeight.keySet());
            Collections.sort(blocks, new Block.sortByD1());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    private void stackBlocks(){

        // vbls to save lists: `base` holds the working base block, while
        // `current` holds the blocks we're stacking on top of the base
        Block base = null, current = null;

        // saves the maximum stack height
        int globalMax = -1;

        Block globalMaxBlock = null;
        for(int i = 0; i < blocks.size(); i++){

            // iterate through the blocks if we were to use them as the base
            base = blocks.get(i);

            // height of current base
            int baseHeight = base.getHeight();

            // calculate maximum height using the current base
            int localMax = baseHeight;

            Block localMaxBlock = base;
            for(int j = 0; j < i; j++){

                // placing current on top of base
                current = blocks.get(j);

                // can only place if length & width are smaller than those of base
                if(current.getD1() < base.getD1() &&
                        current.getD2() < base.getD2()){

                    // stack the sub-twoers onto base to get total height
                    int dp = baseHeight + stackHeight.get(current);

                    // we can add the block to the base, so we accumulate height
                    if(dp > localMax) {
                        localMaxBlock = current;
                        localMax = dp;
                    }
                }
            }

            // update the maximum height of the base used
            stackHeight.put(base, localMax);
            base.setMaxBlock(localMaxBlock);

            // if we find a new tallest stack, replace it
            if(localMax > globalMax) {
                globalMax = localMax;
                globalMaxBlock = base;
            }
        }

        
        maxHeight = globalMax;
        maxBlock = globalMaxBlock;

    }

    public void outputBlocks(File output) {
        ArrayList<String> stack = new ArrayList<String>();
        
        Block block = maxBlock;

        System.out.println(block);
        System.out.println(maxHeight);

        stack.add(block.toString() + "\n");
        while(block.getMaxBlock() != block) {
            stack.add(block.getMaxBlock().toString() + "\n");
            block = block.getMaxBlock();
        }
        System.out.println(stack);

        try {
            FileWriter fw = new FileWriter(output);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(stack.size() + "\n");
            for (String s : stack) 
                bw.write(s);
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        File input = new File(args[0]);
        File output = new File(args[1]);
        BlockStacking test = new BlockStacking(input, output);
        test.stackBlocks();
        test.outputBlocks(output);
    }

}
