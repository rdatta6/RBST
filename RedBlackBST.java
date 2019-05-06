package com.company;
import java.util.*;

public class RedBlackBST<Key extends Comparable<Key>, Value> {
    private static final boolean RED = true;
    private static final boolean BLACK = false;

    private Node root; //Most of the code is verbatim from the book

    public class Node {
        Key key;
        Value value;
        Node left, right;
        int N;
        boolean color;

        Node(Key key, Value value, int N, boolean color) {
            this.key = key;
            this.value = value;
            this.N = N;
            this.color = color;
        }
    }

    Node rotateLeft(Node h) {
        Node x = h.right;
        h.right = x.left;
        x.left = h;
        x.color = h.color;
        h.color = RED;
        x.N = h.N;
        h.N = 1 + size(h.left) + size(h.right);
        return x;
    }

    Node rotateRight(Node h) {
        Node x = h.left;
        h.left = x.right;
        x.right = h;
        x.color = h.color;
        h.color = RED;
        x.N = h.N;
        h.N = 1 + size(h.left) + size(h.right);
        return x;
    }

    private boolean isRed(Node x) {
        if (x == null) return false;
        return x.color == RED;
    }

    void flipColors(Node h) {
        h.color = RED;
        h.left.color = BLACK;
        h.right.color = BLACK;
    }

    private int size(Node x) {
        if (x == null)
        {return 0;}
        else
            {return x.N;}
    }

    public boolean isEmpty(Node h) {
        return h.N == 0;
    }

    public Key min() {
        return min(root).key;
    }

    private Node min(Node x) {
        if (x.left == null) {return x;}
        return min(x.left);
    }


    private Value get(Node x, Key key) {  // Return value associated with key in the subtree rooted at x;
        // return null if key not present in subtree rooted at x.
        if (x == null) {
            return null;
        }
        int cmp = key.compareTo(x.key);
        if (cmp < 0) {
            return get(x.left, key);
        } else if (cmp > 0) {
            return get(x.right, key);
        } else {
            return x.value;
        }
    }

    public void put(Key key, Value val)
    {  // Search for key. Update value if found; grow table if new.
        root = put(root, key, val);
        root.color = BLACK;
    }

    private Node put(Node h, Key key, Value val) {
        if (h == null) {
            return new Node(key, val, 1, true);
        }
        int cmp = key.compareTo(h.key);
        if (cmp < 0) {
            h.left = put(h.left, key, val);
        } else if (cmp > 0) {
            h.right = put(h.right, key, val);
        } else h.value = val;
        if (isRed(h.right) && !isRed(h.left)) {
            h = rotateLeft(h);
        }
        if (isRed(h.left) && isRed(h.left.left)) {
            h = rotateRight(h);
        }
        if (isRed(h.left) && isRed(h.right)) {
            flipColors(h);
        }
        h.N = size(h.left) + size(h.right) + 1;
        return h;
    }

    private Node moveRedLeft(Node h) {  // Assuming that h is red and both h.left and h.left.left
        // are black, make h.left or one of its children red.
        flipColors1(h);
        if (isRed(h.right.left)) {
            h.right = rotateRight(h.right);
            h = rotateLeft(h);
        }
        return h;
    }

    public void deleteMin() {
        if (!isRed(root.left) && !isRed(root.right)) {
            root.color = RED;
        }
        root = deleteMin(root);
        if (!isEmpty(root)) {
            root.color = RED;
        }
    }

    private Node deleteMin(Node h) {
        if (h.left == null) {
            {return null;}
        }
        if (!isRed(h.left) && !isRed(h.left.left)) {
            h = moveRedLeft(h);}
            h.left = deleteMin(h.left);
        return balance(h);
    }

    void flipColors1(Node h) {
        if (h.color == RED){
            h.color = BLACK;
        }
        else {h.color = RED;}
        if (h.left.color == BLACK) {
            h.left.color = RED;
        }
        else {h.left.color = BLACK;}
        if (h.right.color == BLACK){
            h.right.color = RED;
        }
        else {h.right.color = BLACK;}
    }

    private Node balance(Node h) {
        if (isRed(h.right)) {
            h = rotateLeft(h);
        }
        if (isRed(h.right) && !isRed(h.left)) {
            h = rotateLeft(h);
        }
        if (isRed(h.left) && isRed(h.left.left)) {
            h = rotateRight(h);
        }
        if (isRed(h.left) && isRed(h.right)) {
            flipColors1(h);
        }
        h.N = size(h.left) + size(h.right) + 1;
        return h;
    }


    public void deleteMax() {
        if (!isRed(root.left) && !isRed(root.right))
        {root.color = RED;}
        root = deleteMax(root);
        if (!isEmpty(root)) {root.color = BLACK;}
    }

    private Node deleteMax(Node h) {
        if (isRed(h.left))
        {h = rotateRight(h);}
        if (h.right == null)
        {return null;}
        if (!isRed(h.right) && !isRed(h.right.left))
        {h = moveRedRight(h);}
        h.right = deleteMax(h.right);
        return balance(h);
    }

    private Node moveRedRight(Node h) {
        flipColors1(h);
        if (h.left != null){
        if (!isRed(h.left.left))
        {h = rotateRight(h);}
        }
        return h;
    }

    public void delete(Key key) {
        if (!isRed(root.left) && !isRed(root.right)){
            root.color = RED;}
        root = delete(root, key);
        if (!isEmpty(root)) {root.color = BLACK;}
    }

    private Node delete(Node h, Key key) {
        if (key.compareTo(h.key) < 0) {
            if (!isRed(h.left) && !isRed(h.left.left))
            {h = moveRedLeft(h);}
            h.left = delete(h.left, key);
        } else {
            if (isRed(h.left))
            {h = rotateRight(h);}
            if (key.compareTo(h.key) == 0 && (h.right == null))
            {return null;}
            if (!isRed(h.right) && !isRed(h.right.left))
            {h = moveRedRight(h);}
            if (key.compareTo(h.key) == 0) {
                h.value = get(h.right, min(h.right).key);
                h.key = min(h.right).key;
                h.right = deleteMin(h.right);
            }
            else {h.right = delete(h.right, key);};
        }
        return balance(h);
    }
    private void print(Node x)
    {
        if (x == null) {return;}
        print(x.left);
        print(x.right);
        System.out.println(x.key);
        if (isRed(x)){System.out.println('R');}
    }


    public static void main(String[] args) {
        RedBlackBST Table = new RedBlackBST();
        for (int i = 1; i < 61; i++) {
            Table.put(i,i);
        }
        Table.print(Table.root);
        System.out.println("MODIFIED");
        for (int i = 1; i < 21; i++) {
            Table.delete(i);
        }
        Table.print(Table.root);
            }
}

/*1
3
2
5
7
6
4
9
11
10
13
15
14
12
8
17
19
18
21
23
22
20
25
27
26
29
31
30
28
24
16
R
33
35
34
37
39
38
36
41
43
42
45
47
46
44
40
R
49
51
50
53
55
54
52
R
57
59
R
60
58
56
48
32
MODIFIED
21
23
22
25
27
26
24
R
29
31
30
28
33
35
34
37
39
38
36
32
41
43
42
45
47
46
44
49
51
50
53
55
54
52
R
57
59
R
60
58
56
48
40*/
