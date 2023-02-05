#include <stdio.h>
#include <string.h>
#include <assert.h>
#include <stdlib.h>

// Tree node storing a string of length at most 9
struct node {
  char str[10];
  struct node * left;
  struct node * right;
};

// create_node(str, left, right) creates and returns a tree node containing
//   given str and left/right pointers; caller must free allocated memory
// requires: left, right are NULL or point to tree nodes
// note: returns NULL if memory cannot be allocated
struct node * create_node(char str[], struct node * left, struct node * right) {
    struct node * new_node = malloc(sizeof(struct node));
    if (new_node == NULL) 
        return NULL;

    strcpy(new_node->str, str);
    new_node->left = left;
    new_node->right = right;
    return new_node;
}

// free_tree(root) frees the memory associated with the given root node and
//   all of the node's children
// requires: root is NULL or the root of a tree allocated dynamically
void free_tree(struct node * root) {
    if (root == NULL) 
        return;

    free_tree(root->left);
    free_tree(root->right);

    free(root);
}

// print_tree(root) prints the contents of the tree using an in-order traversal
// requires: root is NULL or points to a valid tree root node
void print_tree(struct node * root) {
    if (root == NULL) 
        return;

    print_tree(root->left);
    
    printf("%s ", root->str);

    print_tree(root->right);
}

// height(root) returns the height of the tree with given root
// requires: root is NULL or points to a valid tree root node
int height(struct node * root) {
    if (root == NULL) 
        return 0;
    
    int left_height = height(root->left);
    int right_height = height(root->right);

    if (left_height > right_height)
        return left_height + 1;
    else
        return right_height + 1;
}

// concat_leaves(root, str) modifies str to be a string concatenation of the
//   leaf nodes of the tree with given root using an in-order traversal;
//   returns the length of the concatenated string
// requires: str points to enough memory to store the output string
//           root is NULL or points to a valid tree root node
int concat_leaves(struct node * root, char * str) {
    if (root == NULL) 
        return 0;

    concat_leaves(root->left, str);
    
    strcat(str, root->str);

    concat_leaves(root->right, str);
    
    return strlen(str);
}

int main(void) {
    // [Case 1] Same tree as the example in the lab 
    struct node * food_tree = create_node("pear", NULL, NULL);
    struct node * temp = create_node("apple", NULL, NULL);
    food_tree = create_node("fruit", temp, food_tree);
    temp = create_node("meat", NULL, NULL);
    food_tree = create_node("food", temp, food_tree);

    print_tree(food_tree);                                                  // Print the strings in the food tree, separated by spaces.

    int food_height = height(food_tree);                                    // Calculate the height of the food tree (should be 3, like in the lab example)
    assert(food_height == 3);
    printf("\nHeight: %d\n", food_height);

    char * food_string = calloc(30, sizeof(char));
    int food_string_length = concat_leaves(food_tree, food_string);         // Create a concatenated string out of the food tree, starting from the left, then parent, then right node.
    assert(strcmp(food_string, "meatfoodapplefruitpear") == 0);
    assert(food_string_length == 22);
    printf("String: %s\nLength: %d\n", food_string, food_string_length);
    
    // Cleaning up
    free_tree(food_tree);
    free(food_string);

    puts("");

    // [Case 2] Tree with one item/node
    struct node * one_tree = create_node("one", NULL, NULL);

    print_tree(one_tree);                                                   // Print the string in the one-item tree

    int one_height = height(one_tree);                                      // Calculate the height of the one-item tree
    assert(one_height == 1);
    printf("\nHeight: %d\n", one_height);

    char * one_string = calloc(10, sizeof(char));
    int one_string_length = concat_leaves(one_tree, one_string);            // Create the concatenated string out of one-item tree
    assert(strcmp(one_string, "one") == 0);
    assert(one_string_length == 3);
    printf("String: %s\nLength: %d\n", one_string, one_string_length);

    // Cleaning up
    free_tree(one_tree);
    free(one_string);

    // [Case 3] Empty tree
    assert(height(NULL) == 0);                                              // Test height() with input of an empty tree (NULL)
    assert(concat_leaves(NULL, NULL) == 0);                                 // Test concat_leaves() with inputs of an empty tree and empty string (NULL, NULL)
}