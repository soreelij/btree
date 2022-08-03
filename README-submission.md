# Team Members
| Last Name | First Name | GitHub User Name |
|-----------|------------|------------------|
| Hahle     | Natalie    | nataliehahle     |
| Sorensen  | Eli        | soreelij         |
| Cardoza   | Brandon    | BrandonCardoza   |

# Cache Performance Results

Tests were conducted on a free tier Amazon Web Services remote host with 1 GB of RAM. Run times were measured using the built-in `time` command
appended to program execution.

| Test                  | Arguments                              | Cache size | Time(s)                                 |
|-----------------------|----------------------------------------|------------|-----------------------------------------|
| `GeneBankCreateBTree` | `0 2 data/files_gbk/test3.gbk 7 1`     | no cache   | real: 3.547s, user: 2.027s, sys: 1.361s |  
| `GeneBankSearchBTree` | `0 test data/queries/query7 0`         | no cache   | real: 3.547s, user: 2.027s, sys: 1.361s |
| `GeneBankCreateBTree` | `1 2 data/files_gbk/test3.gbk 7 100 1` | 100        | real: 3.811s, user: 2.302s, sys: 1.495s |
| `GeneBankSearchBTree` | `1 test data/queries/query7 100 0`     | 100        | real: 0.655s, user: 0.478s, sys: 0.064s |
| `GeneBankCreateBTree` | `1 2 data/files_gbk/test3.gbk 7 500 1` | 500        | real: 3.837s, user: 2.321s, sys: 1.498s |
| `GeneBankSearchBTree` | `1 test data/queries/query7 500 0`     | 500        | real: 0.675s, user: 0.518s, sys: 0.046s |

As evidenced by the results, the true performance gains with cache utilization were greatest when a cache was utilized for `GeneBankSearchBTree`,
with caching on `GeneBankCreateBTree` providing no measurable advantage over no cache utilization.

# BTree Binary File Format and Layout
`BTree` objects are written to disk with Java's built-in `RandomAccessFile` class. Its file format and layout is as follows:

### Metadata
The beginning of every `BTree` file contains 8 bytes of metadata about any given tree, namely, the degree of the tree and
the address of the root node within the `RandomAccessFile`. Seeing as any call to `bTreeSplitChild` from within a `BTree` can
result in the root node's address moving from the beginning of the file to another position in the array of bytes, in order to keep
track of the root address, space for its value is written to the `RandomAccessFile` once upon `BTree` initialization. This value is overwritten
upon completion of `GeneBankCreateBTree`, to ensure that the final address to the root node of the tree is always the second value contained in a
`BTree` file.

| Data Item     | Type  | What is stored                                                |
|---------------|-------|---------------------------------------------------------------|
| `degree`      | `int` | the `degree` of this `BTree`                                  |
| `rootAddress` | `int` | `RandomAccessFile` address to the `root` node of this `BTree` |

### Node Layout
Following the metadata, the `RandomAccessFile` contains each `BTreeNode` of the `BTree`--though these nodes are not stored in level order,
because each node's write to disk includes addresses to each of its child nodes, `BTree` can still intuitively
locate a node in the file using the `seek()` method. `BTreeNode` data is written to the disk in the following order:

| Data Item      | Type                        | What is stored                                                       |
|----------------|-----------------------------|----------------------------------------------------------------------|
| `numberOfKeys` | `int`                       | the number of keys stored in this `BTreeNode`                        |
| `keys`         | `int[]`                     | array of all key values stored in this `BTreeNode`                   |
| `leaf`         | `boolean` (stored as `int`) | if this `BTreeNode` is a leaf node (`true` = `1`, `false` = `0`)     |
| `children`     | `int[]`                     | array of `RandomAccessFile` addresses to this `BTreeNode`'s children |



