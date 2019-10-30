import java.lang.Object;
import java.util.Comparator;
import java.util.ArrayList;

public class Block {

    private int d1;
    private int d2;
    private int height;
    private Block maxBlock;
    private int hashCode; 

    public Block(ArrayList<Integer> dims) {
        d1 = dims.get(0);
        d2 = dims.get(1);
        height = dims.get(2);
        maxBlock = this;
        hashCode = dims.hashCode();
    }

    public int getD1() {
        return d1;
    }

    public int getD2() {
        return d2;
    }

    public int getHeight() {
        return height;
    }

    public void setMaxBlock(Block max) {
        maxBlock = max;
    }

    public Block getMaxBlock() {
        return maxBlock;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Block))
            return false;

        Block b = (Block) o;

        if (b == this)
            return true;

        return d1 == b.getD1() && d2 == b.getD2() && height == b.getHeight();
    }

    @Override
    public int hashCode() {
        return hashCode;
    }

    public String toString() {
        return d1 + " " + d2 + " " + height;
    }

    static class sortByD1 implements Comparator<Block> {
        public int compare(Block b1, Block b2) {
            return b1.getD1() - b2.getD1();
        }
    }
}
