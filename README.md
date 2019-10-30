<h1 align="center">CS140 HW9 Block Stacking [40 points]</h1>
<p>
</p>

> by Gabriel Alzate and Janice Lee


## Files
There are 2 files for this solution: `BlockStacking.java` and `Block.java`.
It also takes in an `input.txt` and writes to a file `output.txt` (automatically
generated). The purpose of each file is as follows:

#### 1) BlockStacking.java

This file contains the algorithm that finds the tallest possible tower of blocks.

#### 2) Block.java

This file structures a `Block` class that contains
* block dimensions
* a pointer to the block above it in an optimal block tower

#### 3) input.txt

The first line of this file contains the number of different block types and
the different blocks represented by their own dimensions, length-by-width-by-height.
Here is what it looks like:

```sh
3
2 6 8
4 4 4
1 10 4
```

You can run the program through the command line:

```sh
$ javac *.java
$ java BlockStacking input.txt output.txt
```

