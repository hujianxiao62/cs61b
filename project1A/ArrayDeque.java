public class ArrayDeque<Type> {

    private Type[] myArray ;
    private int size;
    private int nextFirt;
    private int nextLast;
    private static int initialSize = 8;
    private static int RFACTOR = 2;


    public ArrayDeque(){
        myArray = (Type[]) new Object[initialSize];
        size = 0;
        nextFirt = 3;
        nextLast = 4;
    }

    public void addFirst(Type item){
        this.ArrayReSize();

        myArray[nextFirt] = item;
        nextFirt = minusOne(nextFirt);
        size ++;
    }

    public void addLast(Type item){
        ArrayReSize();

        myArray[nextLast] = item;
        nextLast = plusOne(nextLast);
        size ++;
    }

    public boolean isEmpty(){
        if(size == 0){return true;}
        else {return false;}
    }

    public int size(){
        return size;
    }

    public void printDeque(){
        int index = nextFirt;
        while (index != nextLast){
            System.out.print(myArray[index] + " ");
            index = plusOne(index);
        }
    }

    public Type removeFirst(){
        if(size == 0){
            return null;
        }
        else {
            Type tmp = myArray[plusOne(nextFirt)];
            myArray[plusOne(nextFirt)] = null;
            nextFirt = plusOne(nextFirt);
            ArrayReSize();
            return tmp;
        }
    }

    public Type removeLast(){
        if(size == 0){
            return null;
        }
        else {
            Type tmp = myArray[minusOne(nextLast)];
            myArray[minusOne(nextLast)] = null;
            nextLast = minusOne(nextLast);
            ArrayReSize();
            return tmp;
        }
    }

    public Type get(int index){
        return myArray[Math.floorMod(nextFirt + index - 2,myArray.length)];
    }

    private int minusOne(int index){
        return (Math.floorMod(index-1, myArray.length));
    }

    private int plusOne(int index){
        return (Math.floorMod(index+1, myArray.length));
    }

    private void ArrayReSize(){
        if(myArray.length >= 16 || size/myArray.length < 0.25){ReSize(1/RFACTOR);}
        if(size == myArray.length){ReSize(RFACTOR);}
    }

    private void ReSize(float RFACTOR){


        Type tmp[] = (Type[]) new Object[Math.round(myArray.length*RFACTOR)];
        if(nextFirt < nextLast){
            System.arraycopy(myArray,nextFirt+1,tmp,0,size);
        }else{
            System.arraycopy(myArray,0,tmp,0,nextLast-1);
            System.arraycopy(myArray,nextFirt+1,tmp,nextFirt+1,myArray.length-nextFirt-1);
        }
        myArray = tmp;

    }



}





