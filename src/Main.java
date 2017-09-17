import javax.print.Doc;
import java.io.File;
import java.util.ArrayList;
import java.lang.Exception;

//import java.Document;
public class Main {
    public static void main(String[] args) {

        // if(args.length)
        // System.out.println("args: " + args + ", len: " + args.length);

        if(args.length == 1) {
          ArrayList<Document> docs = new ArrayList<Document>();
          // String curDir = "I:\\MSIT\\IT\\projects\\testing";
          String curDir = args[0];

          try {
            for(File file: new File(curDir).listFiles()) {
                if(file.isFile() && file.getName().contains(".txt")) {
                    docs.add(new Document(file.getPath()));
                }
            }

            // for(Document d: docs) {
            //   System.out.println(d);
            // }

            bagOfWords(docs);
            LCS(docs);
          } catch (Exception e) {
            System.out.println("Could not open directory: " + curDir);
            // System.out.println(e.getMessage());
          }
        } else {
          System.out.println("Wrong number of args!");
          System.out.println("Usage: java Main <directory>");
        }
    }

    private static void bagOfWords(ArrayList<Document> docs) {

        System.out.println("");

        Double freqs[][] = new Double[docs.size()][docs.size()];
        for(Document d_one: docs) {
            System.out.println(d_one.getMyID() + " -> " + d_one.getMyPath());
            for(Document d_two: docs) {
                if(d_one.getMyID().equals(d_two.getMyID()))
                    freqs[d_one.getMyID()][d_two.getMyID()] = -1.0;
                else
                    freqs[d_one.getMyID()][d_two.getMyID()] = d_one.doDotProduct(d_two);
            }
        }

        System.out.print("\nBag of Words: ");
        printMatrix(freqs, docs.size());
    }

    private static void LCS(ArrayList<Document> docs) {
        Double freqs[][] = new Double[docs.size()][docs.size()];
        String[] one, two;
        int i = 0, j = 0, iter_one = 0, iter_two = 0,
                matches = 0, spaces = 0, total_len = 0;
        double ans = 0;

        for(Document d_one: docs) {
            one = d_one.getMyWords();
            for(Document d_two: docs) {
                two = d_two.getMyWords();

                if(d_one.getMyID().equals(d_two.getMyID()))
                    freqs[d_one.getMyID()][d_two.getMyID()] = -1.0;
                // Compare the words in each file
                else {

                    /*
                    for(i = 0; i < one.length; i++)
                        System.out.print(one[i] + " ");
                    System.out.println("");

                    for(i = 0; i < two.length; i++)
                        System.out.print(two[i] + " ");
                    System.out.println("");
                    */
//                    System.out.println("one: " + one.toString() + ", two: " + two.toString());
                    i = 0;
                    while (i < one.length) {
                        j = 0;
                        while (j < two.length) {
                            // if there is a match
                            if (one[i].compareTo(two[j]) == 0) {
//                                System.out.println("matched " + one[i] + " and " + two[j]);
                                matches = 1;
                                total_len += two[j].length();
                                iter_one = i + 1;
                                iter_two = j + 1;

                                while (iter_one < one.length &&
                                        iter_two < two.length &&
                                        one[iter_one].compareTo(two[iter_two]) == 0) {
//                                    System.out.println("matched " + one[iter_one] + " and " + two[iter_two]);
                                    matches++;
                                    total_len += two[iter_two].length();
                                    iter_one++;
                                    iter_two++;
                                }

                                // record
                                spaces = matches - 1;
                                total_len += spaces;
                                if (total_len > ans)
                                    ans = total_len;
                                matches = 0;
                                spaces = 0;
                                total_len = 0;
                            }

                            // record
                            spaces = matches - 1;
                            total_len += spaces;
                            if (total_len > ans)
                                ans = total_len;
                            matches = 0;
                            spaces = 0;
                            total_len = 0;
                            j++;
                        }

                        // record
                        spaces = matches - 1;
                        total_len += spaces;
                        if (total_len > ans)
                            ans = total_len;
                        matches = 0;
                        spaces = 0;
                        total_len = 0;
                        i++;
                    }
//                    System.out.println("ans: " + ans + ", first: " + d_one.getMyContents() + ", second: " + d_two.getMyContents());
                    freqs[d_one.getMyID()][d_two.getMyID()] = (ans * 2)/(d_one.getmyWordLength() + d_one.getmyNumSpaces() + d_two.getmyWordLength() + d_two.getmyNumSpaces());
                }

                ans = 0;
            }
        }

        System.out.print("\nLongest Common Subsequence: ");
        printMatrix(freqs, docs.size());
    }

    private static void printMatrix(Double[][] mat, int size) {
        System.out.printf("\n files ");
        for(int i = 0; i < size; i++)
            System.out.printf("%8d", i);
        System.out.println("");

        for(int i = 0; i < size; i++) {
            System.out.printf("% 4d    ", i);
            for (int j = 0; j < size; j++) {
                System.out.printf("%7.3f ", mat[i][j]);
            }
            System.out.println("");
        }
    }
}
