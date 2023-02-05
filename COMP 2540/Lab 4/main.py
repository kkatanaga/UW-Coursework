from time import time
from random import randint
import othersort

def main():
    while True:
        print("U for User input sorting")
        print("T for time table using random numbers")
        print("E to Exit")
        selection = input("Input: ").upper()

        if selection == "E":
            break
        
        while selection == "U":
            input_string = input("Enter the values to be sorted, delimited by commas: ")
            if input_string[0].upper() == "E":
                break

            new_heap = heapFrom(input_string)
            print(f"Unsorted array: {new_heap.heapList}")
            sorted_array = heapSort(new_heap)
            print(f"Sorted array: {sorted_array}\n")
        
        if selection == "T":
            n_bar           = "  n  "
            heap_bar        = "    Heap Sort   "
            quick_bar       = "   Quick Sort   "
            insertion_bar   = " Insertion Sort "
            merge_bar       = "   Merge Sort   "

            n_bar_len = len(n_bar)
            heap_bar_len = len(heap_bar)
            quick_bar_len = len(quick_bar)
            insertion_bar_len = len(insertion_bar)
            merge_bar_len = len(merge_bar)

            full_top = "|" + n_bar + "|" + heap_bar + "|" + quick_bar + "|" + insertion_bar + "|" + merge_bar + "|"
            separation = "|" + ("-" * n_bar_len) + "|" + ("-" * heap_bar_len) + "|" + ("-" * quick_bar_len) + "|" + ("-" * insertion_bar_len) + "|" + ("-" * merge_bar_len) + "|"

            print(full_top)
            print(separation)

            for n in range(3,21):
                printSection(n_bar_len, -n)

                random_array = []
                max_bound = pow(2, n)
                for i in range(max_bound):
                    random_array.append(randint(0, max_bound))
                
                heap_array = heapFrom(random_array, True)
                quick_array = random_array.copy()
                insertion_array = random_array.copy()
                merge_queue = othersort.queueFrom(random_array)

                # Heap
                start = time()
                heap_array = heapSort(heap_array)
                end = time()

                printSection(heap_bar_len, end-start)

                # Quick
                start = time()
                othersort.quickSortRecursive(quick_array, 0, max_bound-1)
                end = time()
                
                printSection(quick_bar_len, end-start)

                # Insertion
                if n < 16:
                    start = time()
                    othersort.insertionSort(insertion_array)
                    end = time()

                    printSection(insertion_bar_len, end-start)
                else:
                    printSection(insertion_bar_len, -1)

                # Merge
                start = time()
                othersort.mergeSortQueue(merge_queue, merge_queue.size())
                end = time()
                
                printSection(merge_bar_len, end-start)
                print()

def printSection(len_top, time_result):
    left_spaces = ""

    if -time_result == 1:
        sect = "--------------"
    elif -time_result >= 3:
        sect = str(-time_result)
        left_spaces = "|"
    else:
        sect = f"{(time_result):.11f}"

    sect_len = len(sect)
    spaces = ( " " * int( (len_top - sect_len) // 2 ) )

    left_spaces += spaces
    if -time_result >= 3:
        if sect_len % 2 == 0:
            left_spaces += " "
    else:
        if sect_len % 2 != 0:
            left_spaces += " "

    print(left_spaces + sect + spaces + "|", end="")

# O(n log n)
def heapSort(input_heap):
    sorted_array = []

    while not input_heap.isEmpty():
        sorted_array.append(input_heap.removeMin())

    return sorted_array

def heapFrom(input_string, isInt=False):
    new_heap = Heap()

    if isInt == True:
        for i in input_string:
            new_heap.insert(i)
        
        return new_heap

    sub_string = ""
    for i in input_string:
        if i == ",":
            new_heap.insert(int(sub_string))
            sub_string = ""

        else:
            sub_string += i

    new_heap.insert(int(sub_string))
    return new_heap

class Heap:
    def __init__(self):
        self.heapList = [0]
        self.size = 0

    # O(log n)
    def insert(self, new_item):
        if self.size == 0:
            self.heapList.append(new_item)
            self.size += 1
            return
        
        self.heapList.append(new_item)
        self.size += 1

        # Upheap
        parent_index = 1
        current_index = self.size

        while current_index > 1:
            parent_index = int(current_index / 2)
            parent = self.heapList[parent_index]

            if new_item < parent:
                self.heapList[current_index], self.heapList[parent_index] = self.heapList[parent_index], self.heapList[current_index]
            current_index = parent_index

    # O(log n)
    def removeMin(self):
        if self.size == 0:
            raise Exception("The heap is empty!")
            
        root_item = self.heapList[1]
        
        if self.size == 1:
            del self.heapList[1]
            self.size -= 1
            return root_item
            
        self.heapList[1] = self.heapList[self.size]
        del self.heapList[self.size]
        self.size -= 1
        
        # Downheap
        parent_index = 1
        parent = self.heapList[1]

        left_index = parent_index * 2
        left = parent + 1
        right = parent + 2

        while left_index <= self.size:    
            left = self.heapList[left_index]
            if left_index < self.size:
                right = self.heapList[left_index + 1]
            else:
                right = left + 1

            if left <= parent and left <= right:
                self.heapList[left_index], self.heapList[parent_index] = self.heapList[parent_index], self.heapList[left_index]
                parent_index = left_index
            elif right <= parent:
                self.heapList[left_index + 1], self.heapList[parent_index] = self.heapList[parent_index], self.heapList[left_index + 1]
                parent_index = left_index + 1
            else:
                break
            
            left_index = parent_index * 2

        return root_item

    # O(1)
    def min(self):
        return self.heapList[1]

    # O(1)
    def size(self):
        return self.size

    # O(1)
    def isEmpty(self):
        return True if self.size == 0 else False
    
main()