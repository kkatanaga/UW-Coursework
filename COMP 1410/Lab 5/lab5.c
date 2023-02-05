#include <stdio.h>
#include <assert.h>
#include <string.h>
#include <stdbool.h>

// Definition of structure for storing student information
struct student {
	int id;
	char name[20];
};

// find_id(id, arr, n, found_name) searches for a student with given id in arr;
//   returns true if such a student is found and found_name is updated to
//   hold the student's name; otherwise returns false
// requires: arr has length n
//           students in arr have unique ids
//           found_name points to enough memory to hold a name
bool find_id(int id, struct student arr[], int n, char * found_name) {
	for (int i = 0; i < n; i++) {
		if (arr[i].id == id) {
			strcpy(found_name, arr[i].name);
			return true;
		}
	}

	return false;
}

// find_name(name, arr, n, ids) searches for student(s) with given name in arr;
//   returns the number of students found and the array ids is updated to
//   contain the id numbers of those students
// requires: arr has length n
//           students in arr have unique ids
//           ids points to enough memory to hold the found student ids
int find_name(char * name, struct student arr[], int n, int ids[]) {
	int name_count = 0;
	for (int i = 0; i < n; i++) {
		if (strcmp(arr[i].name, name) == 0) {
			ids[name_count] = arr[i].id;
			name_count++;
		}
	}
	return name_count;
}

// change_name(s, new_name) renames the student s to have the name given by
//   new_name
// requires: s points to a valid student that can be modified
//           new_name points to a valid string of length at most 19
void change_name(struct student * s, char * new_name) {
	strcpy(s->name, new_name);
}

int main(void) {
	struct student one = {8273, "James"};
	struct student two = {5223, "Bob"};
	struct student three = {3744, "Peter"};
	struct student four = {9918, "John"};
	struct student five = {5732, "John"};
	struct student six = {1889, "Jahn"};

	struct student students[] = {one, two, three, four, five, six};

	int id_list[6] = {};

	char found[20] = {};

	// find_id()
	assert(find_id(3744, students, 6, found) == true);		// Find Peter using his id
	assert(strcmp(found, "Peter") == 0);					// Check if the name in the structure is Peter

	assert(find_id(8273, students, 6, found) == true);		// Find James using his id
	assert(strcmp(found, "James") == 0);					// Check if the name in the structure is James

	assert(find_id(1111, students, 6, found) == false);		// Fail case; no one has an id of 1111
															// Fake id; no name assert needed

	// find_name()
	assert(find_name("Bob", students, 6, id_list) == 1);	// Checks how many Bob's are in the list of students
	assert(id_list[0] == 5223);								// Checks if Bob's id is in the list

	assert(find_name("James", students, 6, id_list) == 1);	// Checks how many James's are in the list of students
	assert(id_list[0] == 8273);								// Checks if James's id is in the list

	assert(find_name("John", students, 6, id_list) == 2);	// Checks how many John's are in the list of students
	assert(id_list[0] == 9918);								// Checks if the id of the first John is in the first index of the list
	assert(id_list[1] == 5732);								// Checks if the id of the second John is in the second index of the list

	// change_name()
	change_name(&students[0], "Keigo");						// Changes the first student's name to Keigo
	assert(strcmp(one.name, "Keigo") < 0);					// Checks that the name of the first student in the original struct hasn't changed
	assert(strcmp(students[0].name, "Keigo") == 0);			// Checks that the name of the first student in the students list has changed

	change_name(&students[3], "Jahn");						// Changes the fourth student's name to Jahn
	assert(strcmp(four.name, "Jahn") > 0);					// Checks that the name of the fourth student in the original struct hasn't changed
	assert(strcmp(students[3].name, "Jahn") == 0);			// Checks that the name of the fourth student in the students list has changed

	// Test find_name() after changing names
	assert(find_name("John", students, 6, id_list) == 1);	// Checks how many John's are in the list of students
	assert(id_list[0] == 5732);								// Checks that only the second John is in the list

	assert(find_name("Jahn", students, 6, id_list) == 2);	// Checks how many Jahn's are in the list of students
	assert(id_list[0] == 9918);								// Checks if the id of the first Jahn is in the first index of the list
	assert(id_list[1] == 1889);								// Checks if the id of the second Jahn is in the second index of the list
	
   puts("All tests passed successfully.");
}