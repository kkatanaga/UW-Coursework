package a3;

import java.util.Iterator;
import java.util.Stack;

public class P2 {

	public static void main(String[] args) {
		// Generate binary tree of 9 student records
		BinaryTree tree = new BinaryTree();
		tree.insert(8, "Iuturna", "Juno", "Computer Science");
		tree.insert(1, "Naenia", "Turnus", "Computer Science");
		tree.insert(6, "Giove", "Mercurius", "Computer Science");
		tree.insert(3, "Nisus", "Auster", "Computer Science");
		tree.insert(4, "Vulcanus", "Liber", "Computer Science");
		tree.insert(9, "Levana", "Fauna", "Computer Science");
		tree.insert(7, "Remus", "Evandrus", "Computer Science");
		tree.insert(2, "Minerve", "Nona", "Computer Science");
		tree.insert(5, "Remus", "Hersilia", "Computer Science");

		// Uncomment to print using recursion (without iterator)
//		tree.inorder();
//		tree.preorder();
//		tree.postorder();

//		System.out.println(tree.search(9).firstName());
		
		// Use inorder iterator
		InorderBT inorderIterator = new InorderBT(tree.root());
		System.out.print("Inorder Traversal:\t");
		while (inorderIterator.hasNext()) {			// hasNext() used
			Node node = inorderIterator.next();		// next() used
			System.out.print(node.studentID() + "  ->  ");
		}

		System.out.println();
		
		// Use preorder iterator
		PreorderBT preorderIterator = new PreorderBT(tree.root());
		System.out.print("Preorder Traversal:\t");
		while (preorderIterator.hasNext()) {		// hasNext() used
			Node node = preorderIterator.next();	// next() used
			System.out.print(node.studentID() + "  ->  ");
		}
		
		System.out.println();

		// Use postorder iterator
		PostorderBT postorderIterator = new PostorderBT(tree.root());
		System.out.print("Postorder Traversal:\t");
		while (postorderIterator.hasNext()) {		// hasNext() used
			Node node = postorderIterator.next();	// next() used
			System.out.print(node.studentID() + "  ->  ");
		}
	}

}

// Node & Binary Tree implementation
// 	modified from https://www.geeksforgeeks.org/implementing-a-binary-tree-in-java/

//Node Class
class Node {
	int studentID;
	String firstName, lastName, department;
	Node left, right;

	public Node(int id, String firstName, String lastName, String department) {
		this.studentID = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.department = department;
		left = right = null;
	}

	public int studentID() {
		return this.studentID;
	}
	
	public String fullName() {
		return this.firstName + " " + this.lastName;
	}
	
	public String firstName() {
		return this.firstName;
	}

	public String lastName() {
		return this.lastName;
	}
	
	public String department() {
		return this.department;
	}
}

//BinaryTree Class
class BinaryTree {
	Node root;

	public BinaryTree() {
		root = null;
	}

	public Node root() {
		return root;
	}

	// Method to insert a new node with given key
	public void insert(int id, String firstName, String lastName, String department) {
		root = insertRec(root, new Node(id, firstName, lastName, department));
	}

	// A recursive function to insert a new key in BST
	private Node insertRec(Node root, Node newNode) {
		// If the tree is empty, return a new node
		if (root == null) {
			root = newNode;
			return root;
		}

		// Otherwise, recur down the tree
		int newKey = newNode.studentID();
		if (newKey < root.studentID)
			root.left = insertRec(root.left, newNode);
		else if (newKey > root.studentID)
			root.right = insertRec(root.right, newNode);

		// return the (unchanged) node pointer
		return root;
	}

	// Method to print the tree preorder
	public void preorder() {
		preorderRec(root);
		System.out.println();
	}

	// A utility function to do preorder traversal of BST
	private void preorderRec(Node root) {
		if (root != null) {
			System.out.print(root.studentID + "  ->  ");
			preorderRec(root.left);
			preorderRec(root.right);
		}
	}

	// Method to print the tree inorder
	public void inorder() {
		inorderRec(root);
		System.out.println();
	}

	// A utility function to do inorder traversal of BST
	private void inorderRec(Node root) {
		if (root != null) {
			inorderRec(root.left);
			System.out.print(root.studentID + "  ->  ");
			inorderRec(root.right);
		}
	}

	// Method to print the tree postorder
	public void postorder() {
		postorderRec(root);
		System.out.println();
	}

	// A utility function to do postorder traversal of BST
	private void postorderRec(Node root) {
		if (root != null) {
			postorderRec(root.left);
			postorderRec(root.right);
			System.out.print(root.studentID + "  ->  ");
		}
	}

	// Method to search for a key in the tree
	public Node search(int studentID) {
		return searchRec(root, studentID);
	}

	// A utility function to search for a key in BST
	private Node searchRec(Node root, int studentID) {
		if (root == null)
			return root;

		if (root.studentID == studentID)
			return root;

		if (studentID < root.studentID)
			return searchRec(root.left, studentID);
		else
			return searchRec(root.right, studentID);
	}
}

class BTIterator {
	protected Node current, rightMost;

	protected BTIterator(Node root) {
		this.current = root;
		this.rightMost = null;
	}
}

// Modified from https://www.geeksforgeeks.org/binary-tree-iterator-for-inorder-traversal/
// Inorder & Preorder both use Morris Traversal. Postorder uses a stack.
class InorderBT extends BTIterator implements Iterator<Node> {
	public InorderBT(Node root) {
		super(root);
	}

	@Override
	public boolean hasNext() {
		return current != null;
	}

	@Override
	public Node next() {
		// Nothing on left. Move pointer to right before returning the root
		if (current.left == null) {
			Node temp = current;
			current = current.right;
			return temp;
		}

		rightMost = current.left;	// Move pointer to the root of the left subtree
		
		// Move pointer to the rightmost leaf of said subtree
		while (rightMost.right != null && rightMost.right != current)
			rightMost = rightMost.right;

		if (rightMost.right == null) {
			rightMost.right = current;	// Set rightmost leaf to point to parent of said subtree
			current = current.left;		// Move main pointer to the left subtree
		} else {	// (rightMost.right != null && rightMost.right == current)
			rightMost.right = null;		// Remove pointer of rightmost leaf we set earlier
			Node temp = current;
			current = current.right;	// Move main pointer to the right subtree
			return temp;				// Return parent of subtree
		}

		return next();	// Repeat above on the left subtree
	}
}

class PreorderBT extends BTIterator implements Iterator<Node> {
	public PreorderBT(Node root) {
		super(root);
	}

	@Override
	public boolean hasNext() {
		return current != null;
	}

	@Override
	public Node next() {
		// Leaf reached. Move pointer to the root of subtree before returning the leaf.
		if (current.left == null) {
			Node temp = current;
			current = current.right;
			return temp;
		}

		rightMost = current.left;

		while (rightMost.right != null && rightMost.right != current)
			rightMost = rightMost.right;

		if (rightMost.right == null) {
			rightMost.right = current;
			Node temp = current;
			current = current.left;
			return temp;
		} else {
			rightMost.right = null;
			current = current.right;
		}

		return next();	// Repeat above on the left subtree
	}
}

class PostorderBT implements Iterator<Node> {
	private Node current;
	private Stack<Node> stack;
	private boolean goDown;

	public PostorderBT(Node root) {
		current = root;
		stack = new Stack<Node>();
		goDown = true;
	}

	@Override
	public boolean hasNext() {
		return current != null;
	}

	@Override
	public Node next() {
		Node temp = current;	// hasNext() ensures temp & current is not null for every next() call
		
		// modified from https://www.geeksforgeeks.org/iterative-postorder-traversal-using-stack/
		while (true) {
			// Loop until leftmost node is reached.
			while (current != null && goDown) {
				stack.push(current);
				current = current.left;
			}
			
			// If stack is empty, traversal is finished
			if (stack.empty())
				break;
			
			// After left subtree is traversed,
			// traverse the right subtree.
			if (current != stack.peek().right) {
				current = stack.peek().right;
				goDown = true;
				continue;
			}
			
			// Pop stack; popped nodes are in postorder.
			current = stack.pop();
			goDown = false;	// Start going up the roots if none of the above conditions are met.
			
			temp = current;
			if (stack.empty())
				current = null;
			break;
		}
		
		return temp;
	}
}