from time import time

def main():
    selection_prompt = "Enter 0 (balance), 1 (recognizer), x (Exit): "
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
                print(recognizer())
            elif selection == "table":
                table()

            print()
            loop = input(loop_prompt).lower()

    print("Closing the program...")

class ArrayStack:
    # O(n) since self.array needs to append n times
    def __init__(self, string_length):
        self.max = string_length  # Maximum number of elements; length of input
        self.array = [""] * string_length  # Array has the length of input
        self.t = 0  # Index after the topmost element; also the number of elements

    # O(1) since assignment & lookup then incrementing all equal O(1)
    def push(self, element):
        if self.max == self.t:
            raise Exception("The Stack is Full.")

        self.array[self.t] = element
        self.t += 1

    # O(1) since decrementing, lookup & assignment, assignment & lookup, then returning all equal O(1)
    def pop(self):
        if self.isEmpty():
            raise Exception("The Stack is Empty.")

        self.t -= 1
        item = self.array[self.t]
        self.array[self.t] = ""
        return item

    # O(1) since we only return one element (top element) in an array
    def top(self):
        return self.array[self.t - 1]

    # O(1) since we only return the integer self.t's value
    def size(self) -> int:
        return self.t

    # O(1) since we only compare then return whether self.t is 0 or not
    def isEmpty(self) -> bool:
        return True if self.t == 0 else False

# O(n)
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

    balanceArray = ArrayStack(len(user_input))

    # O(n) since we check for an unknown character, matching bracket, and open bracket per iteration.
    try:
        for i in user_input:
            # Stop the program if i is an unknown character
            # O(1) since full_list is a list of constant length (20 characters)
            if i not in full_list:
                raise Exception("Unknown character found.")

            elif i in close_dict and balanceArray.top() != close_dict[i]:
                return failure_message
                
            # Pop if i is a matching closed bracket with the top, i.e ']' matches '[' and ')' matches '(', etc.
            elif i in close_dict and balanceArray.top() == close_dict[i]:
                balanceArray.pop()

            # Push if i is an open bracket
            elif i in full_list[:3]:
                balanceArray.push(i)

    except Exception as e:
        return e

    if balanceArray.isEmpty():
        return success_message

    return failure_message

def recognizer(user_input=""):
    success_message = "Your string is in the language."
    failure_message = "Your string is not in the language."
    lang_dict = {"1": "0"}
    collapsing = False  # A string collapses once a '0' precedes a '1'; keeps popping until empty

    if user_input == "":
        user_input = input("Enter a string: ")
        if len(user_input) == 0:
            return failure_message

    languageArray = ArrayStack(len(user_input))

    # O(n) since we check for an unknown character,
    try:
        for i in user_input:
            # Stop the program if i is an unknown character
            if i != "0" and i != "1":
                raise Exception("Unknown character found.")

            # Pop if i is a matching character with the top, i.e '1' matches '0' but not the other way around
            elif i in lang_dict and languageArray.top() == lang_dict[i]:
                languageArray.pop()
                collapsing = True  # collapsing means we only expect 1's in the rest of the strings

            # Collapsing failed; the rest of the string should only be 1's but we found a 0
            elif i == "0" and collapsing == True:
                return failure_message

            # Push if no matches and not yet closing
            else:
                languageArray.push(i)

    except Exception as e:
        return e

    if languageArray.isEmpty():
        return success_message

    return failure_message

def table():
    left_top = "2^n  = len(0^n)"
    right_top = "Time Elapsed (s)"
    print(f"\n{left_top} | {right_top}")

    bar = "-" * (len(left_top) + 1)
    bar += "|"
    bar += "-" * (len(right_top) + 1)
    print(bar)

    for n in range(1, 21):
        power = 2**n
        long_input = ("0" * power) + ("1" * power)

        start_time = time()
        recognizer(long_input)
        end_time = time()
        time_result = end_time - start_time

        if f"{time_result:.2f}" == "0.00":
            sum = 0
            for i in range(999):
                sum += time_result

                start_time = time()
                recognizer(long_input)
                end_time = time()

                time_result = end_time - start_time

            time_result /= 1000

        exponentiation = f"2^{n}"
        for i in range(5 - len(exponentiation)):
            exponentiation += " "

        left = f"{exponentiation}= {power}"
        for i in range(len(left_top) - len(left)):
            left += " "

        right = f"{time_result:.14f}"
        print(f"{left} | {right}")
      
main()