from othersort import quickSortRecursive

from bintrees import AVLTree
from time import perf_counter_ns
from random import randint

def main():
    separation_bar = '-' * 87
    print(separation_bar)
    tree_one = BST()
    for i in range(1, 16):
        tree_one.insert(i)

    print(f"First Tree: {tree_one.showTree()}")

    print()
    print(testTime(tree_one, 1))
    print(testTime(tree_one, 15))
    print()

    tree_one.remove(5)
    print(f"Removed key 5: {tree_one.showTree()}")

    tree_one.remove(15)
    print(f"Removed key 15: {tree_one.showTree()}")

    tree_one.remove(1)
    print(f"Removed key 1: {tree_one.showTree()}")

    tree_one.insert(2)
    print(f"Inserted key 2: {tree_one.showTree()}")


    print(separation_bar)
    tree_two = BST()
    keys_two = [8,4,12,2,6,10,14,1,3,5,7,9,11,13,15]
    for i in keys_two:
        tree_two.insert(i)

    print(f"Second Tree: {tree_two.showTree()}")

    print()
    print(testTime(tree_two, 1))
    print(testTime(tree_two, 15))
    print()

    tree_two.remove(8)
    print(f"Removed key 8: {tree_two.showTree()}")

    print(separation_bar)
    
    printTable()


def treeHeight(root):
    if root is None or (root.left is None and root.right is None):
        return 0

    return max(treeHeight(root.left), treeHeight(root.right)) + 1

def testTime(tree, key):
    start = perf_counter_ns()
    for i in range(100_000):
        tree.search(key)
    end = perf_counter_ns()

    return f"Testing search({key}): {(end-start) / 1_000_000_000} seconds"

class Leaf:
    def __init__(self, k=None):
        self.key    = k
        self.value  = 1

        self.left   = None
        self.right  = None
    
    def search(self, key):
        if self.isExternal():
            return None
        
        if key < self.key:
            return self.left.search(key)
        elif key > self.key:
            return self.right.search(key)

        return self.key

    def insert(self, key):
        if self.key is None:
            self.key, self.left, self.right = key, Leaf(), Leaf()
            return True
        elif key <= self.key:
            return self.left.insert(key)
        elif key > self.key:
            return self.right.insert(key)
        

    def remove(self, key):
        if key < self.key:      # key is in left subtree
            self.left = self.left.remove(key)
            return self
        elif key > self.key:    # key is in right subtree
            self.right = self.right.remove(key)
            return self
        
        # else: key is at current subroot
        if self.left.isExternal():      # no left subtree or both left and right subtrees are empty
            new_parent = self.right
            self = None
            return new_parent
        elif self.right.isExternal():   # no right subtree
            new_parent = self.left
            self = None
            return new_parent
        
        # else: 2 subtrees
        new_parent = self.right
        while not new_parent.left.isExternal(): # find smallest key and its parent in right subtree
            new_pparent = new_parent
            new_parent = new_parent.left
            
        if new_parent != self.right:            # smallest key is further below the right of the subroot
            self.key = new_parent.key
            new_pparent.left = new_parent.remove(new_parent.key)
            return self

        # else: smallest key is immediately to the right of the subroot
        new_parent.left = self.left
        self.right = None
        return new_parent
        
    def isExternal(self):
        return True if self.key == None else False

    def showLeaves(self):
        if self.isExternal():
            return ""

        return self.left.showLeaves() + f"{self.key}, " + self.right.showLeaves()

class BST:
    def __init__(self):
        self.root = Leaf()
        self.leaves = 0
    
    def search(self, key):
        return self.root.search(key)

    def insert(self, key):
        if self.root.insert(key):
            self.leaves += 1

    def remove(self, key):
        self.root = self.root.remove(key)

    def showTree(self):
        tree_string = "[" + self.root.showLeaves()
        tree_string = tree_string[:len(tree_string)-2]
        return tree_string + "]"

def printTable():
    n_bar           = "  n  "
    binary_bar_A    = " BS Tree Height A  "
    binary_bar_B    = " BS Tree Height B  "
    avl_bar_A       = " AVL Tree Height A "
    avl_bar_B       = " AVL Tree Height B "

    n_bar_len = len(n_bar)
    binary_bar_len = len(binary_bar_A)
    avl_bar_len = len(avl_bar_A)

    full_top = "|" + n_bar + "|" + binary_bar_A + "|" + avl_bar_A + "|" + binary_bar_B + "|" + avl_bar_B + "|"
    separation = "|" + ("-" * n_bar_len) + "|" + ("-" * binary_bar_len) + "|" + ("-" * avl_bar_len) + "|" + ("-" * binary_bar_len) + "|" + ("-" * avl_bar_len) + "|"

    print(full_top)
    print(separation)

    for n in range(3,21):
        printSection(n_bar_len, -n)

        binary_tree_A, binary_tree_B = BST(), BST()
        avl_tree_A, avl_tree_B = AVLTree(), AVLTree()

        list_A = []
        max_bound = pow(2, n)
        for i in range(max_bound):
            rand_int = randint(0, 2_000_000)
            list_A.append(rand_int)
            binary_tree_A.insert(rand_int)
            avl_tree_A.insert(rand_int, None)
        
        printSection(binary_bar_len, treeHeight(binary_tree_A.root))
        printSection(avl_bar_len, treeHeight(avl_tree_A._root))
        
        quickSortRecursive(list_A, 0, max_bound-1)

        if max_bound < 513:
            for i in list_A:
                binary_tree_B.insert(i)

        for i in list_A:
            avl_tree_B.insert(i, None)

        printSection(binary_bar_len, treeHeight(binary_tree_B.root))
        printSection(avl_bar_len, treeHeight(avl_tree_B._root))
        print()

def printSection(len_top, time_result):
    left_spaces = ""

    # if -time_result == 1:
    #     sect = "--------------"
    if time_result < 0:
        sect = str(-time_result)
        left_spaces = "|"
    else:
        sect = f"{time_result}"

    sect_len = len(sect)
    spaces = ( " " * int( (len_top - sect_len) // 2 ) )

    left_spaces += spaces
    if time_result < 0:
        if sect_len % 2 == 0:
            left_spaces += " "
    else:
        if sect_len % 2 == 0:
            left_spaces += " "

    print(left_spaces + sect + spaces + "|", end="")

main()