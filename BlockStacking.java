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
 *        that maps every block orientation to what will eventually be the maximum
 *        height of the block tower with some block rotation `i` as the base.
 *
 *      - For every block rotation, sort the dimensions such that the shortest
 *        side is the first dimension value.
 *
 *      - We then sort the entire list of block rotations by the first (i.e. the
 *        shortest) dimension.
 *
 *      - What we now have is a sorted list of blocks, and a mapping from each
 *        block `i` to the maximum height tower given blocks 0, 1, ..., i. Then,
 *        we can obtain the maximum possible height with the recurrence relation
 *
 *          stackHeight(i) = maximum height of the tower with block `i` as the base
 *          stackHeight(i) = - 0                                if i = 0
 *                           - height[i] + max(stackHeight(j))  otherwise
 *
 *        where the max is taken over all j < i such that the jth block can be
 *        stacked upon the ith block (i.e. block `j`'s base is strictly smaller
 *        than block `i`'s base).
 *
 *      - In order to reconstruct the solution, for each block `i`, we store a pointer
 *        to the block that produces the maximum height when it is stacked upon block `i`.
 *        The solution can be reconstructed by following the pointers back from the block
 *        that gives the overall maximum height tower.
 *
 * Running time:
 *
 *      - We create 3n different block orientations (in the worst case) and then
 *        sort them, which takes Θ(n*log(n)) time.
 *
 *      - We find the maximum block tower height in Θ(n^2) time. This is because
 *        for every block `i,` we find the maximum block tower height among blocks
 *        0, ..., i. The number of operations is therefore ∑i from 0 to 3n,
 *        which is in Θ(n^2). We have a tight bound because for every block before
 *        `i` in the sorted list, we have to compare the dimensions to see if the
 *        block is stackable.
 *
 *      - Thus, the entire algorithm runs in Θ(n*log(n) + n^2) ∈ Θ(n^2).
 *
 * @authors Gabriel Alzate and Janice Lee
 * @date October 30th 2019
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

    /** max height of the tower */
    int maxHeight;

    /** base of the max height tower */
    Block maxBase;

    /**
     * Initializes a BlockStacking object and reads in the
     * block information from the input file
     *
     * @params input  the file containing the types of blocks
     *         output the file to which the soln should be written
     */
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


    /**
     * Finds the maximum possible height of the tower, as well as the 
     * base block for the maximum height tower. Goes through every block
     * and calculates the maximum height of the tower using that block
     * as the base, then memoizes the max height for each block so that
     * the value for a block does not have to be recalculated. 
     */
    private void stackBlocks(){

        // vbls to save lists: `base` holds the working base block, while
        // `current` holds the blocks we're stacking on top of the base
        Block base = null, current = null;

        // saves the maximum stack height
        int globalMax = -1;

        // saves the base block for the maximum stack height
        Block globalMaxBase = null;
        for(int i = 0; i < blocks.size(); i++){

            // iterate through the blocks if we were to use them as the base
            base = blocks.get(i);

            // height of current base
            int baseHeight = base.getHeight();

            // calculate maximum height using the current base
            int localMax = baseHeight;

            Block localMaxBase = base;
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
                        localMaxBase = current;
                        localMax = dp;
                    }
                }
            }

            // update the maximum height and block of the base used
            stackHeight.put(base, localMax);
            base.setMaxBlock(localMaxBase);

            // if we find a new tallest stack, replace it
            if(localMax > globalMax) {
                globalMax = localMax;
                globalMaxBase = base;
            }
        }

        maxHeight = globalMax;
        maxBase = globalMaxBase;

    }

    /**
     * Reconstructs the solution from the base of the maximum tower,
     * prints out information about the tallest tower, and then
     * writes the reconstructed solution to an output file.
     *
     * @params output: the file where the solution should be written
     */ 
    public void outputBlocks(File output) {

        // holds the blocks that form the maximum tower 
        ArrayList<String> stack = new ArrayList<String>();

        Block block = maxBase;

        // follow the pointers back to the top of the tower
        stack.add(block.toString() + "\n");
        while(block.getMaxBlock() != block) {
            stack.add(block.getMaxBlock().toString() + "\n");
            block = block.getMaxBlock();
        }

        // print out info about the tallest tower
        System.out.println("The tallest tower has "
                           + stack.size()
                           + " blocks and a height of "
                           + maxHeight);

        // write the solution to the output file
        try {
            
            FileWriter fw = new FileWriter(output);
            BufferedWriter bw = new BufferedWriter(fw);

            // write the number of blocks in the tower
            bw.write(stack.size() + "\n");

            // write the blocks in the tower
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
        BlockStacking stack = new BlockStacking(input, output);
        stack.stackBlocks();
        stack.outputBlocks(output);
    }

}
