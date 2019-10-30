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

public class BlockStacking {

    /** number of types of blocks */
    int n;

    /** stores all the different types of blocks and their rotations */
    ArrayList<Block> blocks;

    /**
     * dp table where heightsDP(i) is the maximum block stacking height if we
     * use base i
     */
    HashMap<Block, Integer> heightsDP;

    int maxHeight;
    Block maxBlock;

    public BlockStacking(File input, File output) {
        blocks = new ArrayList<Block>();
        heightsDP = new HashMap<Block, Integer>();
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
                heightsDP.put(block, block.getHeight());

                dims.add(1, dims.remove(2));
                block = new Block(dims);
                heightsDP.put(block, block.getHeight());

                dims.add(0, dims.remove(2));
                dims.add(1, dims.remove(2));
                block = new Block(dims);
                heightsDP.put(block, block.getHeight());

            }

            // create the sorted list of blocks
            blocks = new ArrayList<Block>(heightsDP.keySet());
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

                    // stack the substacks onto base to get total height
                    int dp = baseHeight + heightsDP.get(current);

                    // we can add the block to the base, so we accumulate height
                    if(dp > localMax) {
                        localMaxBlock = current;
                        localMax = dp;
                    }
                }
            }

            // update the maximum height of the base used
            heightsDP.put(base, localMax);
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
