#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdbool.h>
#include <assert.h>

// Definition of structure for storing student information
struct student {
  int id;
  char * name;
};

// create_student(id, name) creates a new student record with given id and name;
//   allocates memory to store structure and name (must free with free_student)
//   and returns NULL if memory allocation fails
// requires: name points to a valid string
struct student * create_student(int id, char * name) {
    struct student * new = (struct student*) malloc(sizeof(struct student));
    if (new == NULL)
        return NULL;

    new->id = id;
    new->name = (char*) malloc( (strlen(name) + 1) * sizeof(char) );
    if (new->name == NULL)
        return NULL;
    strcpy(new->name, name);

    return new;
}

// change_name(s, new_name) renames the student s to have the name given by
//   new_name; allocates memory to hold new_name (must free with free_student)
//   returns true when the name was successfully changed
// requires: s points to a valid student and new_name points to a valid string
bool change_name(struct student * s, char * new_name) {
    s->name = realloc(s->name, (strlen(new_name) + 1) * sizeof(char));      // Reallocate name to the size of new_name
	if (s->name == NULL)
		return false;                                                       // Return false when the memory for name in struct student is empty
		
    strcpy(s->name, new_name);
    return !strcmp(s->name, new_name);                                      // Returns true when strcmp() returns 0 for full equality; NOT of 0 = 1 or true
}

// free_student(s) frees the memory associated with the student record s
// requires: s is a student record created using dynamic memory allocation
void free_student(struct student * s) {
    printf("Removing student \"%s\" from the structure...\n", s->name);
    free(s->name);                                                          // free() the allocated memory for name
    free(s);                                                                // free() the allocated memory for struct student
}

int main(void) {
    char name_1[] = "James";
    int id_1 = 1111;
    char name_2[] = "Michael";

    struct student * student_ptr;

    // Save a student's information with name_1 and id_1
    student_ptr = create_student(id_1, name_1);
    assert(strcmp(student_ptr->name, name_1) == 0);
    assert(student_ptr->id == id_1);
    
    // Print the saved information
    printf("Student: %s\n", student_ptr->name);
    printf("Id: %d\n", student_ptr->id);

    // Change the student's name with name_2 (initially name_1)
    assert(change_name(student_ptr, name_2) == true);
    assert(strcmp(student_ptr->name, name_2) == 0);
    
    // Print the new information
    printf("Student: %s\n", student_ptr->name);
    printf("Id: %d\n", student_ptr->id);

    // Free all allocated memory
    free_student(student_ptr);

    puts("All tests passed successfully.");
}