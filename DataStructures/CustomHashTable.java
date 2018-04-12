package DataStructures;

import java.util.List;
import java.util.ArrayList;

import DataStructures.DataTable;

public class CustomHashTable{

  public int hash_table_size;
  public List<Integer> [] hash_table;

  public int num_entries;

  public CustomHashTable(int data_table_size) {
    double temp = data_table_size*1.2;
    this.hash_table_size = (int)temp;
    this.hash_table = new ArrayList[this.hash_table_size];
    this.num_entries = 0;
  }

  //make hash table storing buckets with indices pointing to ids in main table
  public void buildHashTable(DataTable dt) {

    for(int i = 0; i < dt.num_rows; i++){

      String new_id = dt.data_table.get(i)[0];
      addEntry(new_id, i);

    }

    System.out.println("Hash table complete ...");

  }//buildCustomHashTable

  //create new entry in hash table
  public void addEntry(String new_id, int index) {

    int hash = entryToHash(new_id);

    if(hash_table[hash] == null) {
      ArrayList<Integer> bucket = new ArrayList<Integer>();
      bucket.add(index);
      this.hash_table[hash] = bucket;
    }
    else{
      this.hash_table[hash].add(index);
    }

    this.num_entries++;

  }//addEntry

  public int entryToHash(String new_id){
    return Math.abs(new_id.hashCode()%this.hash_table_size);
  }//entryToHash

  public List<Integer> getBucket(int hash){
    List<Integer> bucket = this.hash_table[hash];
    return bucket;
  }//getBucket

}
