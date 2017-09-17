/*
A document object represents a single document in memory.
Knowing responsibilities:
  1. its path
  2. its contents as a string
  3. its vector
*/

import java.io.File;
import java.util.*;
import java.nio.*;

public class Document {
    // class variables
    private String myPath;
    private String myContents;
    private HashMap<String, Integer> myVector;
    private String[] myWords;
    private Double myVectorLength;
    private Double myWordLength;
    private Double myNumSpaces;
//    private Double dotProduct;
    private static int docID = 0;
    private Integer myID;

    public Document(String path) {
        this.myPath = "";
        this.myPath = path;
        this.myContents = "";
        this.myVector = new HashMap<String, Integer>();
        this.myVectorLength = 0.0;
        this.myWordLength = 0.0;
        this.myID = docID++;

        this.lineify();
        this.vectorify();
    }

    public HashMap<String, Integer> getMyVector() {
        return this.myVector;
    }

    public String getMyPath() {
        return this.myPath;
    }

    public String getMyContents() {
        return this.myContents;
    }

    public String[] getMyWords() {
        return this.myWords;
    }

//    public Double getDotProduct() {
//        return this.dotProduct;
//    }
//
//    public void setDotProduct(double dotProduct) {
//        this.dotProduct = new Double(dotProduct);
//    }

    public Double getmyVectorLength() {
        return this.myVectorLength;
    }

    public Double getmyWordLength() {
      return this.myWordLength;
    }

    public Double getmyNumSpaces() {
      return this.myNumSpaces;
    }

    public Integer getMyID() {
        return myID;
    }

    public void vectorify() {
        // Turns a string into a vector

        this.myWords = this.myContents.split("\\s");
        for(String word: this.myWords) {
            //this.myVectorLength += Math.pow((double)word.length(), 2.0);
            // System.out.println("word: " + word + ", len: " +word.length());
          this.myWordLength += word.length();
          if(this.myVector.containsKey(word)) {
              this.myVector.replace(word, 1 + this.myVector.get(word));
          }
          else
              this.myVector.put(word, 1);
        }

        this.myNumSpaces = (double)(this.myWords.length - 1);

        for(String word: this.myVector.keySet()) {
            this.myVectorLength += Math.pow((double)this.myVector.get(word), 2.0);
        }
        this.myVectorLength = Math.sqrt(this.myVectorLength);
    }

    public void lineify() {
        // Turns the contents of a file into a single string.
        try {
            Scanner reader = new Scanner(new File(this.myPath));
            while (reader.hasNext()) {
                this.myContents += reader.next().toLowerCase().replaceAll("[^a-zA-z0-9_]", "") + " ";
            }
        }
        catch (Exception e) {
            System.out.println("Couldn't open file.");
        }
    }

    public Double doDotProduct(Document other) {
        int ans = 0;
        for(String s: this.myVector.keySet()) {
            if(other.getMyVector().containsKey(s)) {
                ans += (this.getMyVector().get(s) * other.getMyVector().get(s));
            }
        }
        return (double)ans/(this.getmyVectorLength() * other.getmyVectorLength());
    }

    public String toString() {
        String ans = "";
        ans += "ID: " + this.myID + "\n";
        ans += "Path: " + this.myPath + "\n";
        ans += "String: " + this.myContents + "\n";
        return ans;
    }
}
