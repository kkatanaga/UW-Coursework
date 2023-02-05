#include <stdio.h>
#include <assert.h>
#include <string.h>
#include <stdlib.h>

// Linked list node storing a string of at most 4 characters
struct strnode {
  char str[5];
  struct strnode * next;
};

// create_node(str, next) creates a strnode containing the string str
//   and given next pointer; caller must free allocated memory using free_node
// requires: str has length at most 4
//           next is NULL or points to a strnode
// note: returns NULL if memory cannot be allocated
struct strnode * create_node(char str[], struct strnode * next) {
    struct strnode * new_node = malloc(sizeof(struct strnode));
    if (strlen(str) > 4 || new_node == NULL) {
        free(new_node);
        return NULL;
    }

    strcpy(new_node->str, str);
    new_node->next = next;
    return new_node;
}

// free_node(node) frees the memory associated with the given node; returns a
//   pointer to the next node in the list previously headed by the given node
// requires: node is a valid strnode allocated dynamically
struct strnode * free_node(struct strnode * node) {
    struct strnode * front = node->next;
    free(node);
    return front;
}

// concat_nodes(head, str) modifies str to be a string representation of the
//   contents of the list with given head
// requires: str points to enough memory to store the output string
//           head is NULL or points to a strnode
void concat_nodes(struct strnode * head, char * str) {
    if (head == NULL)
        return;
    
    strcat(str, head->str);
    if (head->next != NULL)
        strcat(str, " ");
    
    concat_nodes(head->next, str);
}

// prints the linked list starting from the head, separated by ->
void print_nodes(struct strnode * head) {
    if (head->next == NULL) {
        printf("%s\n", head->str);
        return;
    }

    printf("%s -> ", head->str);

    print_nodes(head->next);
}

int main(void) {
    // sea bee eh; strings list
    struct strnode * str_ptr = create_node("sea", NULL);                                        // Create the end node
    str_ptr = create_node("bee", str_ptr);                                                      // Create the middle node
    str_ptr = create_node("eh", str_ptr);                                                       // Create the front node

    printf("Strings List: ");
    print_nodes(str_ptr);

    char * string = calloc(11, sizeof(char));                                                   // Allocated enough memory to contain "eh bee sea"
    concat_nodes(str_ptr, string);
    assert(strcmp(string, "eh bee sea") == 0);                                                  // Assert the expected concatenation

    printf("Concatenated Strings: %s\n", string);

    while (str_ptr != NULL) {
        str_ptr = free_node(str_ptr);
    }

    puts("----------------------------------------------------------------");

    // A B C D E F G H I J K L M N O P Q R S T U V W X Y Z; alphabet list
    struct strnode * alpha_ptr = create_node("Z", NULL);                                        // Creates the end node that points to NULL
    char alphabet[2] = "\0";

    for (char i = 'Y'; i >= 'A'; i--) {
        alphabet[0] = i;                                                                        // Converts ASCII value to its character counterpart
        alpha_ptr = create_node(alphabet, alpha_ptr);                                           // Creates the preceding nodes that point to the next node
    }
    
    printf("Alphabet List: ");
    print_nodes(alpha_ptr);

    char * alpha_str = calloc(52, sizeof(char));                                                // Allocated enough memory to contain the entire alphabet separated by a space

    concat_nodes(alpha_ptr, alpha_str);
    assert(strcmp(alpha_str, "A B C D E F G H I J K L M N O P Q R S T U V W X Y Z") == 0);      // Assert the expected concatenation

    printf("Concatenated Alphabet: %s\n", alpha_str);

    while (alpha_ptr != NULL) {
        alpha_ptr = free_node(alpha_ptr);
    }
}