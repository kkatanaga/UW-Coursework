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

class MyQueue:
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

def queueFrom(input=None):
    if input == None:
        raise Exception("Error")

    queue = MyQueue()

    for num in input:
        queue.enqueue(num)
        
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
    
    left_queue = MyQueue()
    left_queue.first = left_start
    left_queue.last = left_end
    
    right_queue = MyQueue()
    right_queue.first = right_start
    right_queue.last = right_end

    left_queue = mergeSortQueue(left_queue, pivot)
    right_queue = mergeSortQueue(right_queue, queue_size - pivot)

    return merge(left_queue, right_queue)

# O(n + m)
def merge(queue_A, queue_B):
    try:
        queue_C = MyQueue()
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