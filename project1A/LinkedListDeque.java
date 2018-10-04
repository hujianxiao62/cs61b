/*
This code is to implement the requirements of cs61b project 1A linked list deque.
https://sp18.datastructur.es/materials/proj/proj1a/proj1a

@author
 hujianxiao62@gmail.com
 https://github.com/hujianxiao62
 */

public class LinkedListDeque<Type> {

    public node sentinel;
    public int size;


    public LinkedListDeque(){
        sentinel = new node(sentinel, null, sentinel);
        size = 0;
    }

    public LinkedListDeque(Type item){
        sentinel = new node(sentinel, null, sentinel);
        size = 0;
        this.addFirst(item);

    }

    public void addFirst(Type item){

        if(size != 0){

        node newNode = new node(null, item, null);
        newNode.next = sentinel.next;
        newNode.prep = sentinel;
        sentinel.next.prep = newNode;
        sentinel.next = newNode;

        size +=1;}

        else {
            node newNode = new node(null, item, null);
            sentinel.next = newNode;
            newNode.prep = sentinel;
            sentinel.prep = newNode;
            newNode.next = sentinel;

            size +=1;}

    }

    public void addLast(Type item){
        if(size != 0){
        node newNode = new node(null, item, null);
        newNode.next = sentinel;
        newNode.prep = sentinel.prep;
        sentinel.prep.next = newNode;
        sentinel.prep = newNode;

        size +=1;
        }
        else {
            this.addFirst(item);
        }
    }

    public boolean isEmpty(){
        if(size == 0){return true;}
        else {return false;}
    }

    public int size(){
        return size;
    }

    public void printDeque(){
        node p = sentinel.next;
        while(p != sentinel){
            System.out.print(p.item + " ");
            p = p.next;
        }

    }

    public Type removeFirst(){
        if(size == 0){return null;}

        else {
            Type itemFirst = sentinel.next.item;
            sentinel.next = sentinel.next.next;
            sentinel.next.prep = sentinel;
            size -= 1;
            return itemFirst;
        }

    }

    public Type removeLast(){
        if(size == 0){return null;}
        else {
            Type itemLast = sentinel.prep.item;
            sentinel.prep = sentinel.prep.prep;
            sentinel.prep.next = sentinel;
            size -= 1;
            return itemLast;
        }
    }

    public Type get(int index){
        node p = sentinel.next;
        if(index >size || index < 0 ){return null;}
        else {
            while (index != 0 ){
                p = p.next;
                index -= 1;
            }
        }
        return p.item;
    }

    public Type getRecursive(int index){
        if(index >size || index < 0 ){return null;}
        else {
            return Recursive(index,sentinel.next);
        }
    }

    private Type Recursive(int index, node p){
        if (index != 0){return Recursive(index-1, p.next);}
        else {return p.item;}
    }

    public class node {
        public node prep;
        public Type item;
        public node next;

        public node(node p, Type i, node n) {
            prep = p;
            item = i;
            next = n;
        }
    }

}
