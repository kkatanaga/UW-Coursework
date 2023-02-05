#include <stdio.h>
#include <assert.h>
#include <string.h>
#include <stdlib.h>
#include <stdbool.h>

// Linked list node that holds a student record
struct snode {
    int id;
    char * name;
    struct snode * next;
};

// Student list structure
struct slist {
    struct snode * front;
};

// create_list() returns an empty newly-created list of students
// note: caller must free using free_list
struct slist * create_list() {
	struct slist * new_list = malloc(sizeof(struct slist));
	assert(new_list != NULL);
	new_list->front = NULL;
	return new_list;
}

// insert_student(id, name, lst) attempts to add a student with given id and
//   name into the given list lst; if a student with that id is already in the
//   list then return false, otherwise lst is modified and true is returned
bool insert_student(int id, char name[], struct slist * lst) {
	// Checks if the name or list is empty
	if (!name || !lst)
		return false;

	struct snode * new_node = malloc(sizeof(struct snode));
	assert(new_node != NULL);
	new_node->next = NULL;

	if (lst->front == NULL|| id < lst->front->id) {
		new_node->next = lst->front;
		lst->front = new_node;
	}
	else {
		struct snode * prev_node = lst->front;

		while(prev_node->next != NULL && prev_node->next->id < id)
			prev_node = prev_node->next;

		if (prev_node->id == id) {
			free(new_node);
			return false;
		}

		new_node->next = prev_node->next;
		prev_node->next = new_node;
	}
	
	new_node->id = id;

	new_node->name = strdup(name);

	return true;
}

// remove_student(id, lst) attempts to remove a student with given id from the
//   given list and free the memory allocated to that student; true is returned
//   if successful and false otherwise
bool remove_student(int id, struct slist * lst) {
	// Checks if the list is empty
	if (!lst)
		return false;

	struct snode * prev_node = lst->front;

	while (prev_node->next != NULL && prev_node->next->id != id)
		prev_node = prev_node->next;

	if (prev_node->next == NULL)
		return false;

	struct snode * node = prev_node->next;
	prev_node->next = node->next;
	free(node->name);
	free(node);
	return true;
}

// find_student(id, lst) returns the name of the student with given id in the
//   given list lst in a dynamically-allocated string (that the caller must
//   free) or NULL if no student has that id
char * find_student(int id, struct slist * lst) {
	struct snode * node = lst->front;

	while (node != NULL && node->id != id)
		node = node->next;
	
	if (node == NULL)
		return NULL;
	
	return node->name;
}

// free_list (lst) deallocates all memory associated with the given list lst
//   including the memory used by the student records in the list
void free_list(struct slist * lst) {
	if (lst == NULL)
		return;

	struct snode * first_node = lst->front;

	while (first_node != NULL) {
		lst->front = first_node->next;
		free(first_node->name);
		free(first_node);
		first_node = lst->front;
	}

	free(lst);
}

// Makes big_node point to small_node.
// big_node is a node from the merged list, small_node is a node from one of the lists being merged.
void point_node(struct snode ** big_node, struct snode ** small_node) {
	(*big_node)->next = *small_node;
	*big_node = (*big_node)->next;
	*small_node = (*small_node)->next;
}

// merge_lists(out, lst1, lst2) merges the student records from lst1 and lst2
//   into the list out with the students in sorted order;
//   lst1 and lst2 are then freed
// requires: out starts as an empty list
void merge_lists(struct slist * out, struct slist * lst1, struct slist * lst2) {
	// Check if one of the two lists is empty to exit the function properly.
	if (lst1 == NULL || lst1->front == NULL) {			// If lst1 is empty, out points to lst2's front node. Free lst2, and return.
		out->front = lst2->front;
		free(lst2);										// Free lst2
		if (lst1)
			free(lst1);									// Free lst1 as well if the structure exists, but no nodes.
		return;
	}
	else if (lst2 == NULL || lst2->front == NULL) {		// If lst2 is empty, out points to lst1's front node. Free lst1, and return.
		out->front = lst1->front;
		free(lst1);										// Free lst1
		if (lst2)
			free(lst2);									// Free lst2 as well if the structure exists, but no nodes.
		return;
	}

	struct snode * lst1_node = lst1->front;				// lst1's node for iteration
	struct snode * lst2_node = lst2->front;				// lst2's node for iteration

	// Works on the wrapper structure
	if (lst1_node->id < lst2_node->id) {				// If lst1's front id is smaller, out's front points to lst1's front.
		out->front = lst1_node;
		lst1_node = lst1_node->next;
	}
	else {												// If lst2's front id is smaller, out's front points to lst2's front.
		out->front = lst2_node;
		lst2_node = lst2_node->next;
	}

	struct snode * out_node  = out->front;				// out's node for iteration

	// Works on the nodes
	while (lst1_node != NULL || lst2_node != NULL) {	// Exit loop once both nodes are NULL.
		if (lst1_node == NULL)							// If lst1_node is empty, focus on lst2's nodes
			point_node(&out_node, &lst2_node);
		else if (lst2_node == NULL)						// Else if lst2_node is empty, focus on lst1's nodes
			point_node(&out_node, &lst1_node);
		
		else if (lst1_node->id < lst2_node->id) 		// If the id in lst1_node is smaller, then it becomes out's next node.
			point_node(&out_node, &lst1_node);
		else 											// Else, the id in lst2_node is smaller, then it becomes out's next node.
			point_node(&out_node, &lst2_node);
	}

	free(lst1); free(lst2);
}

// Prints the list of nodes with their corresponding names and ids.
void print_list(struct slist * lst) {
	struct snode * next_node = lst->front;
	int counter = 1;
	puts("----------------------------------------------------------------");
	while (next_node != NULL) {
		printf("[Node %d] Name: %s\t| Id: %d\n", counter, next_node->name, next_node->id);
		next_node = next_node->next;
		++counter;
	}
	puts("----------------------------------------------------------------");
}

int main(void) {
	struct slist * list_1 = create_list();
	struct slist * list_2 = create_list();
	struct slist * list_3 = create_list();

	char * name;

	puts("First list");
	assert(insert_student(1100, "John", list_1) == true);
	assert(insert_student(1102, "Hal", list_1) == true);
	assert(insert_student(1101, "Bob", list_1) == true);
	assert(insert_student(1100, "Ray", list_1) == false);
	assert(insert_student(1104, "Max", list_1) == true);
	print_list(list_1);

	//Test one NULL input
	assert(insert_student(1105, NULL, list_1) == false);
	assert(insert_student(1105, "Peter", NULL) == false);

	puts("\nRemove \"Max\"");
	assert(remove_student(1104, list_1) == true);
	assert(remove_student(1104, list_1) == false);
	assert(remove_student(1105, list_1) == false);
	assert(remove_student(1101, NULL) == false);
	print_list(list_1);

	// Reinsert "Max"
	assert(insert_student(1104, "Max", list_1) == true);

	name = find_student(1101, list_1);
	assert(strcmp(name, "Bob") == 0);
	printf("Name of student 1101: %s\n", name);
	name = find_student(1105, list_1);
	assert(name == NULL);
	printf("Name of student 1105: %s\n", name);

	puts("\nSecond list");
	assert(insert_student(9334, "Jacob", list_2) == true);
	assert(insert_student(5322, "Tony", list_2) == true);
	assert(insert_student(7724, "Pops", list_2) == true);
	assert(insert_student(1262, "Mike", list_2) == true);
	print_list(list_2);

	name = find_student(5322, list_2);
	assert(strcmp(name, "Tony") == 0);
	printf("Name of student 5322: %s\n", name);

	puts("\nMerged list");
	merge_lists(list_3, list_1, list_2);
	print_list(list_3);

	puts("\nMerge with NULL");
	struct slist * list_4 = create_list();
	merge_lists(list_4, list_3, NULL);
	print_list(list_4);

	puts("\nMerge with empty list");
	struct slist * list_5 = create_list();
	struct slist * list_6 = create_list();
	merge_lists(list_6, list_5, list_4);
	print_list(list_6);

	free_list(list_6);

	puts("\nMerge two empty lists");
	struct slist * list_7 = create_list();
	struct slist * list_8 = create_list();
	struct slist * list_9 = create_list();
	merge_lists(list_9, list_8, list_7);
	print_list(list_9);
	free_list(list_9);

}