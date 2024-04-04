import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class JackAnalyser {
    static List<String> code;
    public static void main(String args[]) {
        if(!args[0].endsWith(".jack")) {
            File dir = new File(args[0]);
            File[] file = dir.listFiles(new FilenameFilter() { 
                public boolean accept(File dir, String name) {
                    return name.endsWith(".jack");
                }
            });
            for(int i=0; i<file.length; ++i) {
                try {
                    Scanner sc = new Scanner(file[i]);  
                    JackTokeneizer jt = new JackTokeneizer(sc, file[i].getPath(), file[i].getName());
                    //code = jt.abc();
                } catch(FileNotFoundException e) {
                    System.out.println("file not found");
                }

                /*try {
                    File f = new File(file[i].getName().replace(".jack", ".xml"));
                    if(f.createNewFile()) System.out.println("file created");
                    else System.out.println("file already exists");
                    
                } catch (IOException e) {
                    System.out.println("ullulululu");
                }*/
            }
        } 
        //System.out.println(file[0]);
    }
}