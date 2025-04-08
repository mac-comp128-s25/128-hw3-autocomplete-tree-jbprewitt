package autoComplete;

import java.util.ArrayList;
import java.util.Map;

/**
 * A prefix tree used for autocompletion. The root of the tree just stores links to child nodes (up to 26, one per letter).
 * Each child node represents a letter. A path from a root's child node down to a node where isWord is true represents the sequence
 * of characters in a word.
 */
public class PrefixTree {
    private TreeNode root; 

    // Number of words contained in the tree
    private int size;

    public PrefixTree(){
        root = new TreeNode();
    }

    /**
     * Adds the word to the tree where each letter in sequence is added as a node
     * If the word, is already in the tree, then this has no effect.
     * @param word
     */
    public void add(String word){
        TreeNode current = root;
        boolean isNewWord = false;

        for (int i = 0; i < word.length(); i++){
            char c = word.charAt(i);
            if (!current.children.containsKey(c)){
                TreeNode newNode = new TreeNode();
                newNode.letter = c;
                current.children.put(c, newNode);
                isNewWord = true;
            }
            current = current.children.get(c);
        }

        if (!current.isWord){
            current.isWord = true;
            size++;
        }

    }

    /**
     * Checks whether the word has been added to the tree
     * @param word
     * @return true if contained in the tree.
     */
    public boolean contains(String word){
        TreeNode current = root;

        for (int i = 0; i < word.length(); i++){
            char c = word.charAt(i);
            if (!current.children.containsKey(c)){
                return false;
            }
            current = current.children.get(c);
        }
        return current.isWord;
    }

    /**
     * Finds the words in the tree that start with prefix (including prefix if it is a word itself).
     * The order of the list can be arbitrary.
     * @param prefix
     * @return list of words with prefix
     */
    public ArrayList<String> getWordsForPrefix(String prefix){
        ArrayList<String> results = new ArrayList<>();
        TreeNode current = root;

        for(int i = 0; i<prefix.length();i++){
            char c = prefix.charAt(i);
            if(!current.children.containsKey(c)){
                return results;
            }
            current = current.children.get(c);
        }

        collectWords(current, prefix, results);
        return results;
    }

    /**
     * @return the number of words in the tree
     */
    public int size(){
        return size;
    }

    /**
     * Collects all complete words in the subtree from given node 
     * Adds word to list that begins with provided prefix
     * @param node the current node of the tree
     * @param wordSoFar the prefix build up from the root to current node
     * @param results list of the matching words
     */
    private void collectWords(TreeNode node, String wordSoFar, ArrayList<String> results) {
        if (node.isWord) {
            results.add(wordSoFar);
        }

        for (Map.Entry<Character, TreeNode> entry : node.children.entrySet()){
            collectWords(entry.getValue(), wordSoFar +entry.getKey(), results);
        }
    }
    
}
