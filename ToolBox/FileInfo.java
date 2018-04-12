package ToolBox;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;

public class FileInfo{

  public String [] file_paths;
  public int num_files;
  public BufferedReader [] buff_read;

  //list of headers and list with headers with duplicate id columns
  public int num_headers;
  public List<String> headers;
  public List<String> headers_duplicate_ids;

  public String delimiter;

  public FileInfo(String [] input_arguments){

    this.file_paths  = input_arguments;
    this.num_files   = input_arguments.length;
    this.buff_read   = new BufferedReader[num_files];

    this.num_headers = 0;
    this.headers     = new ArrayList<String>();
    this.headers_duplicate_ids = new ArrayList<String>();

    this.delimiter   = ",";

  }

  public void initBuffReaders(){

    System.out.println("Starting ...");

    try{

      for(int i = 0; i < this.num_files; i++){
        buff_read[i] = new BufferedReader(new FileReader(this.file_paths[i]));
      }

    }catch (FileNotFoundException e) {
        e.printStackTrace();
    }

  }//initBuffReaders

  public void readHeaders() {

    try{

      String first_line;
      for(int i = 0; i < this.num_files; i++){

        if((first_line = this.buff_read[i].readLine()) != null) {

          String [] temp = first_line.split(delimiter);
          for(int j = 0; j < temp.length; j++){

            //add header to list if not a duplicate - no duplicate ids
            if(headers.indexOf(temp[j]) == -1) {
              this.headers.add(temp[j]);
            }

            this.headers_duplicate_ids.add(temp[j]);

          }//for

        }//if

      }//for

      this.num_headers = headers.size();

    }catch (IOException e) {
        e.printStackTrace();
    }

  }//readHeaders

  //specifiy the delimiter symbol for this file. Default: ","
  public void setDelimiter(String delimiter){
    this.delimiter = delimiter;
  }

  public List<Integer> getTableSeamIndices(){

    List<Integer> table_seam_indices = new ArrayList<Integer>();

    int count = 0;
    for(int i = 0; i < this.headers_duplicate_ids.size(); i++){

      if(checkIfId(this.headers_duplicate_ids.get(i))) {
        table_seam_indices.add(i-count);
        if(table_seam_indices.size() > 1){count++;}
      }

    }

    return table_seam_indices;

  }//getTableSeamIndice

  private boolean checkIfId(String id){

    switch(id){
      case "id": return true;
      case "iD": return true;
      case "Id": return true;
      case "ID": return true;
      case "identifier": return true;
      case "IDENTIFIER": return true;
    }

    return false;

  }//checkIdId

}
