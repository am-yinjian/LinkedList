//Written by Maggie Jiang, Jian0886
public class LinkedList<T extends Comparable<T>> implements List<T> {
    private Node<T> head;
    private int size;           //Create a variable to keep track of linkedlist size
    private boolean isSorted;

    public LinkedList(){
        head = new Node<>(null);            //Initalized head of the list to be null so a dummy node
        size = 0;                                  //Initialized to be zero as we have a null head
        isSorted = true;                        //Initalized to true since there isnt any elements in yet
    }
    public boolean add(T element) {
        if (element == null) {                  //Checks if element is null
            return false;
        }else{
            Node<T> newNode = new Node<>(element);
            Node<T> current = head;
            while (current.getNext() != null) {             //check if it's the last element or not
                current = current.getNext();                //moves on to next node if it isnt the last element
            }
            current.setNext(newNode);
            size ++;                        //change size up one
            isSorted = false;               //set to false first since we don't know if list is sorted anymore but if called isSorted it will automatically update using tempSort
            return true;
        }
    }

    public boolean add(int index, T element) {
        if (element == null || index < 0 || index > size) {         //check if element is null or not in bounds
            return false;
        }else{
            Node<T> newNode = new Node<>(element);
            Node<T> current = head;
            for (int i = 0; i < index; i++){
                current = current.getNext();                //iternates until the node right before index
            }
            newNode.setNext(current.getNext());     //Sets the node that was orginally next at index to new node
            current.setNext(newNode);                 //Sets new node as next before the given index
            isSorted = false;               //set to false first since we don't know if list is sorted anymore but if called isSorted it will automatically update using tempSort
            size ++;
            return true;
        }
    }

    public void clear() {
        head.setNext(null);             //changes the next node to null after head
        size = 0;
        isSorted = false;

    }

    public T get(int index) {
        if (index >= size || index < 0){            //See if index is in bounds
            return null;
        }else{
            Node<T> current = head.getNext();           //Gets the next element since we will stop right before index
            for (int i = 0; i < index; i++){
                current = current.getNext();            //iterates and gets node at index
            }
            return current.getData();
        }
    }

    public int indexOf(T element) {
        Node<T> current = head.getNext();                               //Starts at the next node since head is null
        int i = 0;
        if (isSorted){                                                  //If link is sorted
            for (i = 0; i< size; i++){
                if (current.getData().compareTo(element) == 0){
                    return i;
                }else if(current.getData().compareTo(element) > 0) {            //Return -1 early if element not found when element is smaller than current
                    return -1;                                                  //Since if there is no element equal to the element before than then element does not exist
                }
                else{
                    current = current.getNext();
                }
            }
        }else {
            for (i = 0; i < size; i++) {
                if (current.getData().equals(element)) {            //Checks if current node data matches the element
                    return i;
                }
                current = current.getNext();                    //if not check the next node
            }
        }
        return -1;                                  //If none matches within the linked list size it will return -1
    }

    public boolean isEmpty() {
        if (size == 0){             //If size is zero there is nothing in the list
            return true;
        }else {
            return false;
        }
    }

    public int size() {
        return size;
    }

    public void sort() {
        if (!isSorted) {                //Doesnt sort if isSorted is already true
            boolean swapped = true;
            while (swapped) {
                swapped = false;
                Node<T> current = head.getNext();
                Node<T> prev = head;
                while (current != null && current.getNext() != null) {      //Checks if either is the end or null of the list
                    if (current.getData().compareTo(current.getNext().getData()) > 0) {     //compareto returns 1 if current's elemtn is bigger than previous
                        Node<T> nextNode = current.getNext();
                        prev.setNext(nextNode);                 //Swappes current and prev node postions
                        current.setNext(nextNode.getNext());
                        nextNode.setNext(current);
                        swapped = true;
                    }
                    prev = current;             //If in order goes to next nodes
                    current = current.getNext();
                }
            }
            isSorted = true;  //Changes isSorted to true since it has been sorted
        }
    }


    public T remove(int index) {
        if (index < 0 || index >= size) {           //Checks for bounds
            return null;
        } else {
            Node<T> current = head;
            for (int i = 0; i < index; i++) {
                current = current.getNext();                //iterates until we get the node before the index
            }
            Node<T> removedNode = current.getNext();        //assign the removenode to a variable
            current.setNext(removedNode.getNext());            //assign previous node to skip the remvaed node
            size--;                             //decrease size
            isSorted = tempSort();              //checks if it is sorted or not
            return removedNode.getData();           //returns the element we removed
        }
    }

    public void removeDuplicates() {
        Node<T> current = head.getNext();
        while (current != null) {
            Node<T> dup = current;
            while (dup.getNext() != null) {     //Check if it is the last node in list
                if (current.getData().equals(dup.getNext().getData())) {        //Checks if dup matches current nodes data
                    dup.setNext(dup.getNext().getNext());       //removes the duplicate data if it matches
                    size--;         //decrease size
                } else {
                    dup = dup.getNext();            //If element not matches then iterate to the rest of the nodes
                }
            }
            current = current.getNext();   //Iterates through rest of the nodes
        }
    }


    public void reverse() {
        Node<T> prev = null;
        Node<T> cur = head.getNext();
        Node<T> next = null;
        while (cur != null){                //Check if is last node
            next = cur.getNext();              //Flips the nodes around
            cur.setNext(prev);
            prev = cur;
            cur = next;
        }
        head.setNext(prev);         //changes the head to the end of the list
    }

    public void exclusiveOr(List<T> otherList) {
        if (otherList == null) {                            //Checks if list is empty or not if yes than just return nothing
            return;
        }

        LinkedList<T> other = (LinkedList<T>) otherList;            //Simplifys otherList into just other and set to linkedlist type

        sort();                                                     //cleans up both of the lists by sorting and removing duplicates first
        removeDuplicates();
        other.sort();
        other.removeDuplicates();

        Node<T> current = head.getNext();
        Node<T> otherCurrent = other.head.getNext();
        Node<T> prev = head;

        while (current != null && otherCurrent != null) {           //Checks if end of list
            int comparison = current.getData().compareTo(otherCurrent.getData());           //simplify comparing the list by using a variable
            if (comparison == 0) {                  //If the elements are equal
                prev.setNext(current.getNext());        //Removes both elements from list
                size--;
                Node<T> temp = current.getNext();
                current.setNext(null);
                current = temp;
                otherCurrent = otherCurrent.getNext();
            } else if (comparison < 0) {            //If current is greater than other element
                prev = current;                     //Keeps this element and moves on
                current = current.getNext();
            } else {                                //If other element is greater than current
                Node<T> newNode = new Node<>(otherCurrent.getData());       //Adds other's data to currents list
                newNode.setNext(current);
                prev.setNext(newNode);
                prev = newNode;
                otherCurrent = otherCurrent.getNext();
                size++;
            }
        }


        while (otherCurrent != null) {          // Adds remaining elements from otherList if current is smaller than other
            Node<T> newNode = new Node<>(otherCurrent.getData());
            prev.setNext(newNode);
            prev = newNode;
            otherCurrent = otherCurrent.getNext();
            size++;
        }
        isSorted = true;            //Assign to true since it was sorted beforehand and when changed
        removeDuplicates();     //Remove any duplicates just in case
    }

    public T getMin() {
        if (isEmpty()) {    //Checks if the list exist
            return null;
        }
        Node<T> current = head.getNext();
        T min = current.getData();
        if (isSorted){                                              //if list is already sorted returns first element since it is in ascending order
            return min;
        }
        while (current != null) {                                   //iterates throught the whole list
            if (current.getData().compareTo(min) < 0) {             //If current element is less than min
                min = current.getData();                               //assign min to new data
            }
            current = current.getNext();
        }
        return min;
    }

    public T getMax() {
        if (isEmpty()) {            //Checks if list exist
            return null;
        }
        Node<T> current = head.getNext();       //Assigns to head next since head is null
        T max = current.getData();              //Assigns max to the first nodes element
        if (isSorted){                          //If list already sorted reverse the list and return first node as it is in ascending order
            reverse();
            return max;
        }
        while (current != null) {
            if (current.getData().compareTo(max) > 0) {         //checks if current node is bigger
                max = current.getData();                        //If yes reqassigns max to new nodes element
            }
            current = current.getNext();                    //Iterates to the rest of the list
        }
        return max;
    }

    public String toString(){
        Node<T>  current = this.head.getNext();
        String fin = " ";
        while (current != null){        //Checks if it is the end of the list
            fin += current.getData();       //Adds elemtent to string
            fin += "\n";                    //Adds new line
            current = current.getNext();       //Iterates till end of the list
        }
        return fin;
    }


    public boolean tempSort(){      //Helper to see if the list is sorted or not
        if (!isSorted) {
            Node<T> current = head.getNext();
            while (current != null && current.getNext() != null) {
                if (current.getData().compareTo(current.getNext().getData()) > 0) {
                    return false; // If current node is greater than next node, list is not sorted
                }
                current = current.getNext();
            }
        }
        return true; // If no out-of-order elements found, list is sorted
    }

    public boolean isSorted() {
        isSorted = tempSort();                  //Retest if it is sorted or not and updates variable
        return isSorted;
    }
}
