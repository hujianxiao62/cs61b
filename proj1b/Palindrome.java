public class Palindrome {
    public Deque<Character> wordToDeque(String word){
        char[] StrSplit = word.toCharArray();
        int i = 0;
        LinkedListDeque<Character> wordlist = new LinkedListDeque<>();
        while (i !=StrSplit.length){
            wordlist.addLast(StrSplit[i]);
            i++;
        }
        return  wordlist;
    }

    public boolean isPalindrome(String word){
        char[] StrSplit = word.toCharArray();

        for (int i = 0; i <= StrSplit.length/2-1; i++){
            if (StrSplit[i] != StrSplit[StrSplit.length-1-i]){
                return false;
            }
        }
        return true;
    }

}
