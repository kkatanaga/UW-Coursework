#include <stdio.h>
#include <assert.h>
#include <string.h>
#include <stdlib.h>
#include <stdbool.h>

// Tree node holding the numeric value that a word compresses to
struct treenode {
    char word[10];
    int value;
    struct treenode * left;
    struct treenode * right;
};

struct treenode * new_leaf(char word[], int value) {
    struct treenode * leaf = malloc(sizeof(struct treenode));
    strcpy(leaf->word, word);
    leaf->value = value;
    leaf->left = NULL; leaf->right = NULL;
    return leaf;
}

// insert_node(word, value, tree) inserts a new node containing the given word
//   and value into the tree with given root (or NULL denoting an empty tree)
//   returns the root node of the tree following the insert
// requires: word is not already in the given tree
//           tree satisfies the ordering property
struct treenode * insert_node(char word[], int value, struct treenode * tree) {
    if ( !tree )
        return new_leaf(word, value);

    struct treenode * cur_node = tree;
    int compare_value = 0;

    while (1) {
        compare_value = strcmp(word, cur_node->word);

        if (compare_value < 0) {                                // Given word has less value than the word in the parent; go left
            if ( !(cur_node->left) ) {
                cur_node->left = new_leaf(word, value);
                return tree;
            }
            cur_node = cur_node->left;
        }
        else if (compare_value > 0) {                           // Given word has more value than the word in the parent; go right
            if ( !(cur_node->right) ) {
                cur_node->right = new_leaf(word, value);
                return tree;
            }
            cur_node = cur_node->right;
        }
    }
    
    return tree;
}

// lookup_word(word, tree) returns the numeric value associated with the given
//   word in the given tree (or 0 if the word does not appear in the tree);
//   tree points to the root node or is NULL (denoting an empty tree)
// requires: tree satisfies the ordering property
int lookup_word(char word[], struct treenode * tree) {
    while (tree) {
        int compare_value = strcmp(word, tree->word);

        if (compare_value == 0)
            return tree->value;
        else if (compare_value < 0)
            tree = tree->left;
        else
            tree = tree->right;
    }

    return 0;
}

// free_tree(tree) frees all memory associated in the tree with given root node
void free_tree(struct treenode * tree) {
    if (tree->left)
        free_tree(tree->left);

    if (tree->right)
        free_tree(tree->right);
    
    free(tree);
}

void print_tree(struct treenode * root) {
    if (root->left != NULL)
        print_tree(root->left);
    
    printf("%s ", root->word);

    if (root->right != NULL)
        print_tree(root->right);
}

int main(void) {
    struct treenode * root = NULL;
    char input[10] = {};
    int word_count = 1;
    int word_value = 0;

    puts("Enter a sentence/words separated by spaces, and ends with a period.");
    while( scanf("%9s", input) && strcmp(input, ".") ) {
        word_value = lookup_word(input, root);
        if (word_value == 0) {
            root = insert_node(input, word_count, root);
            ++word_count;
            printf("%s ", input);
        }
        else {
            printf("%d ", word_value);
        }
    }
    puts("");

    print_tree(root);
    free_tree(root);

    puts("\n\nRandom Words Tree");
    struct treenode * test_root = insert_node("tractor", 1, NULL);
    test_root = insert_node("umbrella", 2, test_root);
    test_root = insert_node("water", 3, test_root);
    test_root = insert_node("apple", 4, test_root);
    test_root = insert_node("cream", 5, test_root);
    test_root = insert_node("butter", 6, test_root);
    test_root = insert_node("elevator", 7, test_root);

    assert(lookup_word("cream", test_root) == 5);
    assert(lookup_word("water", test_root) == 3);
    assert(lookup_word("tractor", test_root) == 1);
    assert(lookup_word("apple", test_root) == 4);
    assert(lookup_word("umbrella", test_root) == 2);
    assert(lookup_word("elevator", test_root) == 7);
    assert(lookup_word("butter", test_root) == 6);

    print_tree(test_root);
    free_tree(test_root);
}