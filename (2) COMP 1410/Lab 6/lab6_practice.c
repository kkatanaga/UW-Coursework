/* ============================================================================

COMP-1410 Lab 6

============================================================================ */



#include <stdio.h>

#include <assert.h>

#include <string.h>

#include <stdlib.h>

#include <stdbool.h>



// Definition of structure for storing student information

struct student {

 int id;

 char * name;

};



// create_student(id, name) creates a new student record with given id and name;

//  allocates memory to store structure and name (must free with free_student)

//  and returns NULL if memory allocation fails

// requires: name points to a valid string

struct student * create_student(int id, char * name) {

   struct student * s = (struct student *)malloc(sizeof(struct student));

   if(s == NULL) {

       return NULL;

   }

   s->id = id;

   if(name == NULL) {

       free(s);

       return NULL;

   }

   s->name = (char *)malloc((strlen(name) + 1) * sizeof(char));

   if(s->name == NULL) {

       s->name = NULL;

   } else {

       strcpy(s->name, name);

   }

   return s;

}



int main() {

   int id = 110084783;

   char * name = "Bob";

   struct student * s1 = create_student(id, name);

   assert(s1->id == id);

   assert(strcmp(s1->name, name) == 0);

   //char * name2 = "Bobby";

   //assert(change_name(s1, name2));

   //assert(strcmp(s1->name, name2) == 0);

   //free_student(s1);

   printf("All test cases passed.\n");
}