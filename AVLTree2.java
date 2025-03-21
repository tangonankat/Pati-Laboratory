import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class TreeNode {
    int value, height;
    TreeNode left, right;

    TreeNode(int item) {
        value = item;
        height = 1;
        left = right = null;
    }
}

class AVLTree {
    TreeNode root;

    int height(TreeNode node) {
        if (node == null) return 0;
        return node.height;
    }

    int getBalance(TreeNode node) {
        if (node == null) return 0;
        return height(node.left) - height(node.right);
    }

    TreeNode rightRotate(TreeNode y) {
        TreeNode x = y.left;
        TreeNode T2 = x.right;

        x.right = y;
        y.left = T2;

        y.height = Math.max(height(y.left), height(y.right)) + 1;
        x.height = Math.max(height(x.left), height(x.right)) + 1;

        return x;
    }

    TreeNode leftRotate(TreeNode x) {
        TreeNode y = x.right;
        TreeNode T2 = y.left;

        y.left = x;
        x.right = T2;

        x.height = Math.max(height(x.left), height(x.right)) + 1;
        y.height = Math.max(height(y.left), height(y.right)) + 1;

        return y;
    }

    void insert(int value) {
        root = insertRec(root, value);
    }

    TreeNode insertRec(TreeNode node, int value) {
        if (node == null) return new TreeNode(value);

        if (value < node.value) {
            node.left = insertRec(node.left, value);
        } else if (value > node.value) {
            node.right = insertRec(node.right, value);
        } else {
            return node;
        }

        node.height = 1 + Math.max(height(node.left), height(node.right));
        int balance = getBalance(node);

        if (balance > 1 && value < node.left.value) {
            return rightRotate(node);
        }
        if (balance < -1 && value > node.right.value) {
            return leftRotate(node);
        }
        if (balance > 1 && value > node.left.value) {
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }
        if (balance < -1 && value < node.right.value) {
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }

        return node;
    }

    void delete(int value) {
        root = deleteRec(root, value);
    }

    TreeNode deleteRec(TreeNode root, int value) {
        if (root == null) return root;

        if (value < root.value) {
            root.left = deleteRec(root.left, value);
        } else if (value > root.value) {
            root.right = deleteRec(root.right, value);
        } else {
            if (root.left == null || root.right == null) {
                TreeNode temp = (root.left != null) ? root.left : root.right;
                if (temp == null) {
                    temp = root;
                    root = null;
                } else {
                    root = temp;
                }
            } else {
                TreeNode temp = minValueNode(root.right);
                root.value = temp.value;
                root.right = deleteRec(root.right, temp.value);
            }
        }

        if (root == null) return root;

        root.height = 1 + Math.max(height(root.left), height(root.right));
        int balance = getBalance(root);

        if (balance > 1 && getBalance(root.left) >= 0) {
            return rightRotate(root);
        }
        if (balance > 1 && getBalance(root.left) < 0) {
            root.left = leftRotate(root.left);
            return rightRotate(root);
        }
        if (balance < -1 && getBalance(root.right) <= 0) {
            return leftRotate(root);
        }
        if (balance < -1 && getBalance(root.right) > 0) {
            root.right = rightRotate(root.right);
            return leftRotate(root);
        }

        return root;
    }

    TreeNode minValueNode(TreeNode node) {
        TreeNode current = node;
        while (current.left != null) current = current.left;
        return current;
    }

    void fillArray(TreeNode node, int[] array, int index) {
        if (node == null) return;
        array[index] = node.value;
        fillArray(node.left, array, 2 * index + 1);
        fillArray(node.right, array, 2 * index + 2);
    }

    void preorder() {
        List<Integer> result = new ArrayList<>();
        preorderRec(root, result);
        printTraversal(result);
    }

    void preorderRec(TreeNode node, List<Integer> result) {
        if (node != null) {
            result.add(node.value);
            preorderRec(node.left, result);
            preorderRec(node.right, result);
        }
    }

    void inorder() {
        List<Integer> result = new ArrayList<>();
        inorderRec(root, result);
        printTraversal(result);
    }

    void inorderRec(TreeNode node, List<Integer> result) {
        if (node != null) {
            inorderRec(node.left, result);
            result.add(node.value);
            inorderRec(node.right, result);
        }
    }

    void postorder() {
        List<Integer> result = new ArrayList<>();
        postorderRec(root, result);
        printTraversal(result);
    }

    void postorderRec(TreeNode node, List<Integer> result) {
        if (node != null) {
            postorderRec(node.left, result);
            postorderRec(node.right, result);
            result.add(node.value);
        }
    }

    private void printTraversal(List<Integer> result) {
        for (int i = 0; i < result.size(); i++) {
            System.out.print(result.get(i));
            if (i < result.size() - 1) System.out.print(", ");
        }
        System.out.println();
    }

    int getArraySize() {
        int h = height(root);
        return (int) Math.pow(2, h) - 1;
    }
}

public class AVLTree2 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        AVLTree avl = new AVLTree();

        while (true) {
            System.out.println("\nEnter operation (e.g., 'Insert: 10, 35, 50' or 'Delete: 35, 48'). Type 'reset' to clear the tree or 'done' to exit:");
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("done")) {
                System.out.println("Exiting the program.");
                break; // Exit the loop and terminate the program
            }

            if (input.equalsIgnoreCase("reset")) {
                avl = new AVLTree();
                System.out.println("AVL tree has been reset.");
                continue;
            }

            String[] parts = input.split(":", 2);
            if (parts.length != 2) {
                System.out.println("Invalid input format. Use 'Insert: values' or 'Delete: values'.");
                continue;
            }

            String operation = parts[0].trim();
            String[] values = parts[1].trim().split(",");

            for (String value : values) {
                try {
                    int num = Integer.parseInt(value.trim());
                    if (operation.equalsIgnoreCase("Insert")) {
                        avl.insert(num);
                    } else if (operation.equalsIgnoreCase("Delete")) {
                        avl.delete(num);
                    } else {
                        System.out.println("Invalid operation. Use 'Insert' or 'Delete'.");
                        break;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid value: " + value + ". Please enter valid integers.");
                }
            }

            int arraySize = avl.getArraySize();
            int[] arrayRepresentation = new int[arraySize];
            avl.fillArray(avl.root, arrayRepresentation, 0);

            System.out.println("\n1-D Array Representation:");
            System.out.println("Size of array = 2^K - 1 = 2^" + avl.height(avl.root) + " - 1 = " + arraySize + " nodes");
            System.out.print("Declaration = int AVL[" + arraySize + "] = {");
            for (int i = 0; i < arrayRepresentation.length; i++) {
                System.out.print(arrayRepresentation[i]);
                if (i < arrayRepresentation.length - 1) System.out.print(", ");
            }
            System.out.println("}\n");

            for (int i = 0; i < arrayRepresentation.length; i++) {
                System.out.printf("%-8d", i);
            }
            System.out.println();
            for (int i = 0; i < arrayRepresentation.length; i++) {
                System.out.printf("%-8d", arrayRepresentation[i]);
            }
            System.out.println("\n");

            System.out.println("Traversals:");
            System.out.print("Preorder = ");
            avl.preorder();
            System.out.print("Inorder = ");
            avl.inorder();
            System.out.print("Postorder = ");
            avl.postorder();
        }

        scanner.close(); // Close the scanner before exiting
    }
}