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
import java.lang.Math;

public class Document {
    // class variables
    private String myPath;
    private String myContents;
    private HashMap<String, Integer> myVector;
    private String[] myWords;
    private Double myVectorLength;
    private Double myWordLength;
    private Double myNumSpaces;
    private static int docID = 0;
    private Integer myID;
    private ArrayList<Integer> myKMap, myFingerprint;

    public Document(String path) {
        this.myPath = "";
        this.myPath = path;
        this.myContents = "";
        this.myVector = new HashMap<String, Integer>();
        this.myVectorLength = 0.0;
        this.myWordLength = 0.0;
        this.myID = docID++;
        this.myKMap = new ArrayList<Integer>();
        this.myFingerprint = new ArrayList<Integer>();

        this.lineify();
        this.vectorify();
        this.constructFingerprint();
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

    public ArrayList<Integer> getMyFingerprint() {
      return this.myFingerprint;
    }

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

    public Integer getMyKMapLength() {
      return this.myKMap.size();
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

    public void constructFingerprint() {
      /*
        To construct the fingerprint, we do the following:
        1. Smoosh the string, meaning remove spaces
        2. Make k-gram strings
        3. Run the k-gram strings through a hash
        4. Pick all k-gram hashes at position 0 mod p
      */

      String smoosh = this.myContents.replaceAll("\\s", "");
      String gram  = "";
      int k = 5, p = k + 1;

      for(int i = 0; i < smoosh.length() - k; i++) {
        gram = smoosh.substring(i, i + k);
        myKMap.add(applyHash(gram));
      }

      for(int i = 0; i < myKMap.size(); i++) {
        if(i % p == 0)
          myFingerprint.add(myKMap.get(i));
      }
    }

    private Integer applyHash(String word) {
      /*
        Applies a hash to word.
        The hash is the following:
        hash = ascii(word[n - 1]) * (10 ^ (0)) +
               ascii(word[n - 2]) * (10 ^ (1)) +
               .
               .
               .
               ascii(word[0]) * (10 ^ (n - 1))

               where n = strlen(word)
      */

      int ans = 0, power = 0, len = word.length();
      char c;

      for(int i = 0; i < len; i++) {
        c = word.charAt(i);
        power = (int)Math.pow(10, len - 1 - i);
        ans += (int)c * power;
      }

      return ans;
    }

    public Integer compareFingerprints(Document other) {
      /*
        Compares the fingerprints of two Document objects and returns
        the number of matched fingerprints.
      */
      int ans = 0;
      ArrayList<Integer> fp_other = other.getMyFingerprint();
      for(Integer fp: this.myFingerprint) {
        if(fp_other.contains(fp))
          ans++;
      }
      return ans;
    }

    public String toString() {
        String ans = "";
        ans += "ID: " + this.myID + "\n";
        ans += "Path: " + this.myPath + "\n";
        ans += "String: " + this.myContents + "\n";
        return ans;
    }
}
