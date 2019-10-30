import java.lang.Object;
import java.util.Comparator;
import java.util.ArrayList;

public class Block {

    private int d1;
    private int d2;
    private int height;
    /** stores which block this should be stacked 
        underneath to obtain the maximum height */
    private Block maxBlock; 
    private int hashCode; 

    /**
     * Initializes a Block object with a length, width, and height
     * and sets its hashcode to the hashcode of the dimension arraylist
     *
     * @param dims: an arraylist of l, w, h
     */
    public Block(ArrayList<Integer> dims) {
        d1 = dims.get(0);
        d2 = dims.get(1);
        height = dims.get(2);
        maxBlock = this;
        hashCode = dims.hashCode();
    }

    /**
     * Getter for the first dimension of the block
     */
    public int getD1() {
        return d1;
    }

    /**
     * Getter for the second dimension of the block
     */
    public int getD2() {
        return d2;
    }

    /**
     * Getter for the height of the block
     */
    public int getHeight() {
        return height;
    }

    /**
     * Setter for the maximum block 
     */
    public void setMaxBlock(Block max) {
        maxBlock = max;
    }

    /**
     * Getter for the maximum block 
     */
    public Block getMaxBlock() {
        return maxBlock;
    }

    @Override
    /**
     * Overrides the equals method inherited from Object

     * @returns true if the two blocks have the same
     * dimensions and orientation
     */
    public boolean equals(Object o) {
        if (!(o instanceof Block))
            return false;

        Block b = (Block) o;

        if (b == this)
            return true;

        return d1 == b.getD1() && d2 == b.getD2() && height == b.getHeight();
    }

    @Override
    /**
     * Overrides the hashCode method inherited from Object

     * @returns the hashCode of the dimension array passed in
     * from the constructor
     */ 
    public int hashCode() {
        return hashCode;
    }

    /**
     * Overrides the toString method inherited from Object
     *
     * @returns a string with the first dim, second dim, and height
     */ 
    public String toString() {
        return d1 + " " + d2 + " " + height;
    }

    /**
     * Overrides the compare method to sort Block objects
     * by their first dimension
     */ 
    static class sortByD1 implements Comparator<Block> {
        public int compare(Block b1, Block b2) {
            return b1.getD1() - b2.getD1();
        }
    }
}
