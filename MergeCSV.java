//import subclasses
import DataStructures.*;
import ToolBox.*;

public class MergeCSV {

    public static void main(String[] args) {

      long startTime = System.nanoTime();

      //get all relevant information regarding the .csv files
      FileInfo fi = new FileInfo(args);

      fi.setDelimiter(",");
      fi.initBuffReaders();
      fi.readHeaders();

      //create final data table, populate it with first data table
      DataTable dt = new DataTable(fi);
      dt.readFirstDataTable();

      //build lookup hash table according to first data table
      CustomHashTable ht = new CustomHashTable(dt.num_rows);
      ht.buildHashTable(dt);

      //read in other .csv tables and merge them one by one with hash lookup
      for(int i = 1; i < fi.num_files; i++){
        dt.joinTables(fi.buff_read[i], ht);
      }

      //create a new file, store results and run verification
      Tools.basicResultVerification(dt);
      Tools.writeToCSV(fi,dt,"results.csv");
      Tools.logTime(startTime);

    }//main

}
