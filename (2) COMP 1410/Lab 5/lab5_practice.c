/* ===========================================================================

COMP-1410 Lab 5

=========================================================================== */



#include <stdio.h>

#include <assert.h>

#include <string.h>

#include <stdbool.h>



// Definition of structure for storing student information

struct student {

 int id;

 char name[20];

};



bool find_id(int id, struct student arr[], int n, char * found_name) ;



int main(void) {

   struct student s1 = {11002222, "bob"};

   struct student s2 = {11006623, "jimmy"};

   struct student s3 = {5222266, "phoebe"};

   struct student s4 = {123456789, "alice"};

   struct student s5 = {12344444, "alice"};

   struct student students[4] = {s1,s2,s3,s4};



   //find_name("alice") --> 2 --> ids[0] =123456789, ids[1] = 12344444



   //



   char found_name[20];

   char found_name2[20];



   struct student students2[2] = {{123333, "john"}, {54444, "tina"}};



   assert(find_id(11002222, students, 4, found_name) == true);

   assert(find_id(2, students, 4, found_name2) == false);

   assert(strcmp(found_name, "bob") == 0);



   //asert statements for change_name



   //struct student s6 = {12345555, "tom"};

   //change_name(s6, "jerry");

   //assert(strcmp(s6.name, "jerry") == 0);



   puts("all tests passed successfully");



   /*

   direct reference to struct student (s1)

   s1: id, name

   id = s1.id

   name = s1.name



   pointer to the struct student (* s1)

   s1: id, name

   id = s1->id == *(s1).id

   name = s1->name == *(s1).name

   */





}



// find_id(id, arr, n, found_name) searches for a student with given id in arr;

//  returns true if such a student is found and found_name is updated to

//  hold the student's name; otherwise returns false

// requires: arr has length n

//          students in arr have unique ids

//          found_name points to enough memory to hold a name

bool find_id(int id, struct student arr[], int n, char * found_name) {

   for(int i = 0; i < n; i++) {

       if(arr[i].id == id) {

           strcpy(found_name, arr[i].name);



           //strcpy(s1'sname, updated) - copies str2 into s1

           return true;

       }

   }

   return false;

}



// find_name(name, arr, n, ids) searches for student(s) with given name in arr;

//  returns the number of students found and the array ids is updated to

//  contain the id numbers of those students

// requires: arr has length n

//          students in arr have unique ids

//          ids points to enough memory to hold the found student ids

int find_name(char * name, struct student arr[], int n, int ids[]);

   // counter variable

   // iterate through array of students

   // if arr[i].name == name

   // add id to ids

   // inc counter



   // return number of students



// change_name(s, new_name) renames the student s to have the name given by

//  new_name

// requires: s points to a valid student that can be modified

//          new_name points to a valid string of length at most 19

void change_name(struct student * s, char * new_name);