package DataStructures;

import java.io.IOException;
import java.io.BufferedReader;
import java.util.List;
import java.util.ArrayList;

import ToolBox.FileInfo;
import ToolBox.Tools;
import DataStructures.CustomHashTable;

public class DataTable{

  public List<String[]> data_table;
  public int num_rows;
  private int current_table;
  private int current_table_start_index;
  private String delimiter;
  private FileInfo fi;

  //checksum used for file integrity verification
  private int checksum;

  public DataTable(FileInfo fi) {

    this.fi             = fi;
    this.data_table     = new ArrayList<String[]>();
    this.current_table  = 0;
    this.current_table_start_index = 0;
    this.num_rows       = 0;
    this.checksum       = 0;

  }

  //build the final table structure and popluate it with first table
  public void readFirstDataTable(){

    try{

      String line = "";

      while((line = this.fi.buff_read[0].readLine()) != null) {

        line = Tools.cleanString(line);

        String [] temp = line.split(this.fi.delimiter);
        addEntry(temp);

      }//while

      System.out.println("First table read ...");

      setCurrentTable(this.current_table+1);

    }catch (IOException e) {
        e.printStackTrace();
    }

  }//readFirstDataTable

  private void setCurrentTable(int currentTable){
    this.current_table  = currentTable;
    this.current_table_start_index = getTableStartIndex();
  }//setCurrentTable

  //get index -> where the current table entries will start in final table
  private int getTableStartIndex() {

    List<Integer> seams = this.fi.getTableSeamIndices();

    //go back to first table if currentTable > actual number of tables
    if(this.current_table >= seams.size()){
      this.current_table = 0;
      return seams.get(0);
    }

    return seams.get(this.current_table);

  }//getTableStartIndex

  //merge to tables using id as the unqiue key
  public void joinTables(BufferedReader bf, CustomHashTable ht){

    try{

      String line  = "";

      while((line = bf.readLine()) != null) {

        line = Tools.cleanString(line);

        String [] new_row = line.split(this.fi.delimiter);
        String    new_id  = new_row[0];

        //find hash value of id
        int hash = ht.entryToHash(new_id);
        List<Integer> bucket = ht.getBucket(hash);

        //if id exists in current hash table
        if(bucket != null) {
          mergeTableEntry(new_row, bucket);
        }

        //if id does not exist in hash table - add new row at end of data_table
        if(bucket == null) {
          addEntry(new_row);
          ht.addEntry(new_id, ht.num_entries-1);
        }

      }//while

      setCurrentTable(this.current_table+1);

    }catch (IOException e) {
        e.printStackTrace();
    }

  }//joinDataTables

  //add a new entry to the data table
  private void addEntry(String [] new_entry) {

    String row_content[] = new String[this.fi.num_headers];

    //set id
    row_content[0] = new_entry[0];

    int column = this.current_table_start_index;
    if(this.current_table == 0){column = 1;}

    for(int i = 1; i < new_entry.length; i++){

      row_content[column] = new_entry[i];
      updateCheckSum();
      column++;

    }

    this.data_table.add(row_content);
    this.num_rows++;

  }//addEntry

  //merge two rows from two files if id match occurs
  private void mergeTableEntry(String [] new_row,
                               List<Integer> bucket) {

    String new_id  = new_row[0];

    //go through indices stored in hash bucket
    for(int i = 0; i < bucket.size(); i++) {

      int table_index = bucket.get(i);

      String[] old_row = this.data_table.get(table_index);
      String   old_id  = old_row[0];

      if(old_id.equals(new_id)) {

        int column = this.current_table_start_index;
        for(int j = 1; j < new_row.length; j++) {
          old_row[column] = new_row[j];
          updateCheckSum();
          column++;
        }

        //reinsert the updated old row
        data_table.set(table_index,old_row);
        break;

      }//if

    }//for

  }//mergeTableEntry

  private void updateCheckSum(){
    this.checksum++;
  }//updateCheckSum

  public int getCheckSum(){
    return this.checksum;
  }//getCheckSum

}
