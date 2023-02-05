def main():
    selection_prompt = "Enter 0 (balance), 1 (merge), x (Exit): "
    loop_prompt = "Enter x to exit, nothing to keep going: "

    while True:
        selection = input(selection_prompt).lower()
        loop = ""
        if selection == "x":
            break

        while loop != "x":
            if selection == "0":
                print(balance())
            elif selection == "1":
                print(merge().printList())

            print()
            loop = input(loop_prompt).lower()

    print("Closing the program...")
    
def merge(input_A="", input_B=""):
    if input_A == "":
        input_A = input("Enter the first sequence of numbers separated by commas: ")
        if len(input_A) == 0:
            raise Exception("Input NULL")
        queue_A = queueFrom(input_A)
        
    if input_B == "":
        input_B = input("Enter the second sequence of numbers separated by commas: ")
        if len(input_B) == 0:
            raise Exception("Input NULL")
        queue_B = queueFrom(input_B)
    
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

    queue_C.printList()
    return queue_C

def queueFrom(input):
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
        
    def addFirst(self, n):
        new_node = Node(n)
        
        if self.first == None:
            self.first = new_node
            self.last = new_node
        else:
            new_node.next = self.first
            self.first = new_node
        
        self.nodes += 1
            
    def removeFirst(self):
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
        
    def getFirst(self):
        return self.first.item

    def addLast(self, n):
        new_node = Node(n)
        
        if self.last == None:
            self.last = new_node
            self.first = new_node
        else:
            self.last.next = new_node
            self.last = new_node

        self.nodes += 1

    def removeLast(self):
        if self.last == None:
            raise Exception("List is already empty.")
            
        temp = self.last

        if self.last == self.first:
            self.last = None
            self.first = None
        else:
            node = self.first
            while node.next != self.last:
                node = node.next

            self.last = node
            node.next = None

        self.nodes -=1
        return temp.item

    def getLast(self):
        return self.last.item

    def enqueue(self, n):
        self.addLast(n)

    def dequeue(self):
        return self.removeFirst()

    def front(self):
        return self.getFirst()
        
    def size(self):
        return self.nodes

    def isEmpty(self):
        return True if self.nodes == 0 else False
        
    def printList(self):
        if self.first == None:
            return "List is empty."

        return self.first.printNode()

def balance(user_input=""):
    success_message = "Your string is balanced."
    failure_message = "Your string is unbalanced."
    close_dict = {")":"(", "]":"[", "}":"{"}
    full_list = [   "(","[","{",")","]","}", 
                    "0","1","2","3","4","5","6","7","8","9",
                    "+", "-", "*", "/"]

    if user_input == "":
        user_input = input("Enter a string: ")
        if len(user_input) == 0:
            return failure_message

    print(user_input)
    balanceList = List()

    # O(n) since we check for an unknown character, matching bracket, and open bracket per iteration.
    try:
        for i in user_input:
            # Stop the program if i is an unknown character
            # O(1) since full_list is a list of constant length (20 characters)
            if i not in full_list:
                raise Exception("Unknown character found.")

            elif i in close_dict and balanceList.getFirst() != close_dict[i]:
                return failure_message
                
            # Pop if i is a matching closed bracket with the top, i.e ']' matches '[' and ')' matches '(', etc.
            elif i in close_dict and balanceList.getFirst() == close_dict[i]:
                balanceList.removeFirst()

            # Push if i is an open bracket
            elif i in full_list[:3]:
                balanceList.addFirst(i)

    except Exception as e:
        return e

    if balanceList.size() == 0:
        return success_message

    return failure_message

main()