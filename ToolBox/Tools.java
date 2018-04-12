package ToolBox;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

import DataStructures.DataTable;
import ToolBox.FileInfo;

public final class Tools{

  //Output to new csv file
  public static void writeToCSV(FileInfo fi,
                                DataTable dt,
                                String file_name) {

    System.out.println("Writing results ...");

    try {

        FileWriter writer = new FileWriter(file_name);

        for (int i = 0; i < fi.num_headers; i++) {
          writer.append(fi.headers.get(i));
          if(fi.num_headers > i+1){writer.append(",");}
        }

        writer.append("\n");

        for (int i = 0; i < dt.num_rows; i++) {

            String [] row = dt.data_table.get(i);

            for (int j = 0; j < fi.num_headers; j++) {

              if(row[j] == null){writer.append("");}
              else{writer.append(row[j]);}

              if(fi.num_headers > j+1){writer.append(",");}

            }

            writer.append("\n");

        }

        writer.close();

      }
      catch (IOException e){
        System.out.println("Exception: couldn't close file");
      }

  }//writeToCSV

  public static String cleanString(String line){

    //make sure everything is lower case and no "
    String new_string = line.replace("\"", "");
    new_string = new_string.toLowerCase();
    return new_string;

  }//cleanString

  public static void basicResultVerification(DataTable dt) {

    /*this is a stub - we're simply counting the number of elements before
     *and after processing. More sophisticated versions can do cross
     *referencing or other verification methods
     */

    System.out.println("Verification running...");

    int new_checksum = 0;

    for(int i = 0; i < dt.num_rows; i++){

      String [] row = dt.data_table.get(i);

      for(int j = 1; j < row.length; j++){
        if(row[j] != null){
          new_checksum++;
        }
      }

    }//for

    System.out.println("Checksum: " + new_checksum + " vs " + dt.getCheckSum());

  }//verifyResults

  public static void logTime(long startTime){

    long endTime   = System.nanoTime();
    long totalTime = endTime - startTime;
    double seconds = (double)totalTime / 1000000000.0;
    System.out.println("Runtime in seconds: " + seconds);

  }//logTime

  public static int customEntryToHash(String entry, int hashVal){
    return Math.abs(entry.hashCode()%hashVal);
  }//entryToHash

}
