#include <stdio.h>
#include <math.h>

float circleArea(float radius);

/*\=============================================================|
|   Program Name: exercise_a.c                                  |
|   Name: Keigo Katanaga                                        |
|   Student ID: 110068805                                       |
|   Date: 11/22/2021                                            |
|   Description: Calculates the area of a circle given a radius |
|=============================================================\*/

int main()
{
    float diameter = 20;                        // Diameter for the sake of having an input
    float area;                                 // Resulting area

    area = circleArea(diameter/2);              // Calls on the function that calculates the area with a radius input
    printf("Area of the circle: %.2f", area);   // Prints the area
}

float circleArea(float radius)
{
    return M_PI * radius * radius;              // Returns the area of a circle
}