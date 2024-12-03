import java.util.*;

public class HW03 {
    public static void main(String[] args) {
        // Test cases for the functions

        // Testing the isMatching function
        String expression1 = "{[()]}";
        System.out.println("Is the expression " + expression1 + " balanced? " + isMatching(expression1));

        // Testing the reverse function
        String str1 = "abcd";
        System.out.println("Reversed string of " + str1 + ": " + reverse(str1));

        // Testing the isHTMLMatching function
        String html1 = "<div><p>Test</p></div>";
        System.out.println("Is the HTML " + html1 + " valid? " + isHTMLMatching(html1));

        // Testing the evaluate function
        String expression2 = "(15 + 5) * 3";
        System.out.println("The result of the expression '" + expression2 + "' is: " + evaluate(expression2));
    }

    public static boolean isMatching(String expression) {
        final String opening = "({[";
        final String closing = ")}]";
        IStack<Character> buffer = new LinkedStack<Character>();


        for (char c : expression.toCharArray()) {
            if (opening.indexOf(c) != -1)
                buffer.push(c);
            else if (closing.indexOf(c) != -1) {
                if (buffer.isEmpty())
                    return false;
                if (closing.indexOf(c) != opening.indexOf(buffer.pop()))
                    return false;
            }
        }

        return buffer.isEmpty();
    }

    public static String reverse(String str) {
        Stack<Character> stack = new Stack<>();

        for (char c : str.toCharArray()) {
            stack.push(c);
        }

        StringBuilder reversedString = new StringBuilder();

        while (!stack.isEmpty()) {
            reversedString.append(stack.pop());
        }

        return reversedString.toString();
    }

    public static boolean isHTMLMatching(String html) {
        IStack<String> buffer = new LinkedStack<String>();
        int i = html.indexOf("<");
        while (i != -1) {
            int k = html.indexOf(">", i + 1);

            if (k == -1) {
                return false;
            }
            String tag = html.substring(i + 1, k);

            if (!tag.startsWith("/"))
                buffer.push(tag);
            else {
                if (buffer.isEmpty())
                    return false;

                if (!tag.substring(1).equals(buffer.pop()))
                    return false;
            }

            i = html.indexOf("<", k + 1);
        }

        return buffer.isEmpty();
    }

    public static int evaluate(String expression) {
        Stack<Integer> values = new Stack<>();
        Stack<Character> operators = new Stack<>();

        int i = 0;

        while (i < expression.length()) {
            char c = expression.charAt(i);

            if (Character.isWhitespace(c)) {
                i++;
                continue;
            }

            if (Character.isDigit(c)) {
                int num = 0;
                while (i < expression.length() && Character.isDigit(expression.charAt(i))) {
                    num = num * 10 + (expression.charAt(i) - '0');
                    i++;
                }

                values.push(num);
            } else if (c == '(') {
                operators.push(c);
                i++;
            } else if (c == ')') {
                while (!operators.isEmpty() && operators.peek() != '(') {
                    values.push(performOperation(values.pop(), values.pop(), operators.pop()));
                }

                operators.pop();
                i++;
            } else if (c == '+' || c == '-' || c == '*' || c == '/') {
                while (!operators.isEmpty() && hasPrecedence(c, operators.peek())) {
                    values.push(performOperation(values.pop(), values.pop(), operators.pop()));
                }
                operators.push(c);
                i++;
            }
        }

        while (!operators.isEmpty()) {
            values.push(performOperation(values.pop(), values.pop(), operators.pop()));
        }

        return values.pop();
    }

    private static int performOperation(int b, int a, char operator) {
        switch (operator) {
            case '+':
                return a + b;
            case '-':
                return a - b;
            case '*':
                return a * b;
            case '/':
                return a / b;
        }

        return 0;
    }

    private static boolean hasPrecedence(char op1, char op2) {
        if (op2 == '(' || op2 == ')') {
            return false;
        }
        if ((op1 == '*' || op1 == '/') && (op2 == '+' || op2 == '-')) {
            return false;
        }
        return true;
    }
}

interface List<E> {
    int size();
    boolean isEmpty();
}

interface IStack<E> extends List<E> {
    void push(E e);
    E top();
    E pop();
}

interface IQueue<E> extends List<E> {
    void enqueue(E e);
    E dequeue();
    E first();
}

class ArrayStack<E> implements IStack<E> {
    private E[] data;
    private int size = 0;
    private static final int DEFAULT_CAPACITY = 1000;

    @SuppressWarnings("unchecked")
    public ArrayStack() {
        data = (E[]) new Object[DEFAULT_CAPACITY];
    }

    @SuppressWarnings("unchecked")
    public ArrayStack(int capacity) {
        data = (E[]) new Object[capacity];
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void push(E e) {
        if (size == data.length) throw new IllegalStateException("Stack is full");
        data[size++] = e;
    }

    public E top() {
        if (isEmpty()) return null;
        return data[size - 1];
    }

    public E pop() {
        if (isEmpty()) return null;
        E answer = data[--size];
        data[size] = null; // avoid loitering
        return answer;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < size; i++) {
            sb.append(data[i]);
            if (i < size - 1) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }
}

class Node<E> {
    private E data;
    private Node<E> next;

    public Node(E data, Node<E> next) {
        this.data = data;
        this.next = next;
    }

    public E getData() {
        return data;
    }

    public Node<E> getNext() {
        return next;
    }

    public void setNext(Node<E> next) {
        this.next = next;
    }

    @Override
    public String toString() {
        return data.toString();
    }
}

class LinkedStack<E> implements IStack<E> {
    private Node<E> top = null;
    private int size = 0;

    public LinkedStack() {}

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void push(E e) {
        top = new Node<>(e, top);
        size++;
    }

    public E top() {
        if (isEmpty()) return null;
        return top.getData();
    }

    public E pop() {
        if (isEmpty()) return null;
        E answer = top.getData();
        top = top.getNext();
        size--;
        return answer;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Node<E> current = top;
        sb.append("[");
        while (current != null) {
            sb.append(current.getData());
            if (current.getNext() != null) sb.append(", ");
            current = current.getNext();
        }
        sb.append("]");
        return sb.toString();
    }
}

class ArrayQueue<E> implements IQueue<E> {
    private E[] data;
    private int size = 0;
    private int front = 0;
    private int rear = 0;
    private static final int DEFAULT_CAPACITY = 1000;

    @SuppressWarnings("unchecked")
    public ArrayQueue() {
        data = (E[]) new Object[DEFAULT_CAPACITY];
    }

    @SuppressWarnings("unchecked")
    public ArrayQueue(int capacity) {
        data = (E[]) new Object[capacity];
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void enqueue(E e) {
        if (size == data.length) throw new IllegalStateException("Queue is full");
        data[rear] = e;
        rear = (rear + 1) % data.length;
        size++;
    }

    public E dequeue() {
        if (isEmpty()) return null;
        E answer = data[front];
        data[front] = null; // avoid loitering
        front = (front + 1) % data.length;
        size--;
        return answer;
    }

    public E first() {
        if (isEmpty()) return null;
        return data[front];
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        int i = front;
        while (i != rear) {
            sb.append(data[i]);
            if ((i + 1) % data.length != rear) sb.append(", ");
            i = (i + 1) % data.length;
        }
        sb.append("]");
        return sb.toString();
    }
}

class LinkedQueue<E> implements IQueue<E> {
    private Node<E> head = null;
    private Node<E> tail = null;
    private int size = 0;

    public LinkedQueue() {}

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void enqueue(E e) {
        Node<E> newNode = new Node<>(e, null);
        if (tail != null) {
            tail.setNext(newNode);
        }
        tail = newNode;
        if (head == null) {
            head = newNode;
        }
        size++;
    }

    public E dequeue() {
        if (isEmpty()) return null;
        E answer = head.getData();
        head = head.getNext();
        if (head == null) tail = null; // queue is now empty
        size--;
        return answer;
    }

    public E first() {
        if (isEmpty()) return null;
        return head.getData();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Node<E> current = head;
        sb.append("[");
        while (current != null) {
            sb.append(current.getData());
            if (current.getNext() != null) sb.append(", ");
            current = current.getNext();
        }
        sb.append("]");
        return sb.toString();
    }
}
