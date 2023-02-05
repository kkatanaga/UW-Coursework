def main():
    selection = ''
    type = ''
    type_set = {'I', 'Q', 'M'}
    prev_type = ''
    exit_key = 'E'

    while selection != exit_key:
        print("U for User input sorting")
        print("C for Coded input sorting")
        print("E to Exit")
        selection = input("Input: ").upper()
        if selection == 'E':
            break

        while selection == 'U' and type != exit_key:
            type = input(
                "I for Insertion sort, Q for Quick sort, M for Merge sort: "
            ).upper()
            if type == exit_key:
                break
            elif type not in type_set:
                type = prev_type
            prev_type = type

            input_sequence = input(
                "Enter a string of values to sort, separated by commas: ")

            if type == 'I' or type == 'Q':
                array_sequence = arrayFrom(input_sequence)
                print(f"Unsorted: {array_sequence}")

                if type == 'I':
                    insertionSort(array_sequence)
                    print(f"Insertion: {array_sequence}")
                else:
                    quickSortRecursive(array_sequence, 0,
                                       len(array_sequence)-1)
                    print(f"Quick: {array_sequence}")

                reverseArray(array_sequence, len(array_sequence))
                print(f"Reverse: {array_sequence}")
                
            elif type == 'M':
                queue_sequence = queueFrom(input_sequence)
                print(f"Unsorted: {queue_sequence.printList()}")

                sorted_queue = mergeSortQueue(queue_sequence,
                                              queue_sequence.nodes)
                print(f"Merge:   {sorted_queue.printList()}")
                print(f"Reverse: {reverseArray(sorted_queue).printList()}")

        type = ''
        
        if selection == 'C':
            test_string = "4,33,2,6,92,2,8,12,64"

            test_array = arrayFrom(test_string)
            print(f"\nUnsorted: {test_array}\n")
            
            insertionSort(test_array)
            print(f"Insertion: {test_array}")
            reverseArray(test_array, len(test_array))
            print(f"Reverse:   {test_array}")

            test_array = arrayFrom(test_string)
            quickSortRecursive(test_array, 0, len(test_array) - 1)
            print(f"Quick:   {test_array}")
            reverseArray(test_array, len(test_array))
            print(f"Reverse: {test_array}")

            test_queue = queueFrom(test_string)
            merge_sort = mergeSortQueue(test_queue, test_queue.nodes)
            print(f"Merge:   {merge_sort.printList()}")
            print(f"Reverse: {reverseQueue(merge_sort).printList()}\n")

def reverseArray(array, size=0, start=0):
    if start >= size:
        return

    array[start], array[size-1] = array[size-1], array[start]

    return reverseArray(array, size-1, start+1)

def reverseQueue(queue):
    if queue.nodes == 0:
        return queue

    item = queue.dequeue()
    reverseQueue(queue)
    queue.enqueue(item)

    return queue
    
def arrayFrom(input=""):
    if input == "":
        raise Exception("Error")

    array = []
    num = ""

    for i in input:
        if i != ",":
            num += i
        else:
            array.append(int(num))
            num = ""
    array.append(int(num))
    return array

# Best case = O(n), Worst case = O(n^2) 
def insertionSort(array):
    array_len = len(array)

    for i in range(1, array_len):
        for j in range(i, 0, -1):
            if array[j - 1] > array[j]:
                array[j], array[j-1] = array[j-1], array[j]
            else:
                break

    return

# Best case = O(n log n), Worst case = O(n^2)
def quickSortRecursive(array, start, end):
    if start >= end:
        return

    pivot = array[end]
    left = start
    right = end - 1

    while left <= right:
        while left <= right and array[left] <= pivot:
            left += 1
            
        while left <= right and pivot <= array[right]:
            right -= 1

        if left < right:
            array[left], array[right] = array[right], array[left]
            
    array[left], array[end] = array[end], array[left]

    quickSortRecursive(array, start, left - 1)
    quickSortRecursive(array, left + 1, end)

    return

class Node:
    def __init__(self, n):
        self.item = n
        self.next = None

    def printNode(self):
        if self.next == None:
            return f"{self.item}"

        return f"{self.item} -> {self.next.printNode()}"

class List:
    def __init__(self):
        self.first = None
        self.last = None
        self.nodes = 0

    def enqueue(self, n):
        new_node = Node(n)

        if self.last == None:
            self.last = new_node
            self.first = new_node
        else:
            self.last.next = new_node
            self.last = new_node

        self.nodes += 1

    def dequeue(self):
        if self.first == None:
            raise Exception("List is already empty.")

        temp = self.first

        if self.first == self.last:
            self.first = None
            self.last = None
        else:
            self.first = self.first.next

        temp.next = None
        self.nodes -= 1
        return temp.item

    def front(self):
        return self.first.item

    def getLast(self):
        return self.last.item

    def size(self):
        return self.nodes

    def isEmpty(self):
        return True if self.nodes == 0 else False

    def printList(self):
        if self.first == None:
            return "List is empty."

        return self.first.printNode()

def queueFrom(input=""):
    if input == "":
        raise Exception("Error")

    queue = List()
    num = ""

    for i in input:
        if i != ",":
            num += i
        else:
            queue.enqueue(int(num))
            num = ""
    queue.enqueue(int(num))
    return queue

# Best case = O(n log n), Worst case = O(n log n)
def mergeSortQueue(input_queue, queue_size=0):
    if queue_size <= 1:
        return input_queue

    left_start = input_queue.first
    right_end = input_queue.last
    traverse_node = input_queue.first
    pivot = int((queue_size+1) / 2)
    for i in range(pivot):
        if i == pivot-1:
            left_end = traverse_node
            right_start = traverse_node.next

        traverse_node = traverse_node.next

    left_end.next = None
    
    left_queue = List()
    left_queue.first = left_start
    left_queue.last = left_end
    
    right_queue = List()
    right_queue.first = right_start
    right_queue.last = right_end

    left_queue = mergeSortQueue(left_queue, pivot)
    right_queue = mergeSortQueue(right_queue, queue_size - pivot)

    return merge(left_queue, right_queue)

# O(n + m)
def merge(queue_A, queue_B):
    try:
        queue_C = List()
        node_A = queue_A.first
        node_B = queue_B.first

        while node_A != None or node_B != None:
            if node_A == None:
                queue_C.enqueue(node_B.item)
                node_B = node_B.next
            elif node_B == None:
                queue_C.enqueue(node_A.item)
                node_A = node_A.next
            elif node_A.item < node_B.item:
                queue_C.enqueue(node_A.item)
                node_A = node_A.next
            else:
                queue_C.enqueue(node_B.item)
                node_B = node_B.next

    except Exception as e:
        return e

    return queue_C

main()