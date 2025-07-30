import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

class JackAnalyzer {
    static List<String> code;
    public static void main(String args[]) throws ParserConfigurationException, SAXException, IOException {
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
                    sc.close();
                } catch(FileNotFoundException e) {
                    System.out.println("file not found");
                }

                CompilationEngine ce = new CompilationEngine(file[i].getPath(), file[i].getName());
            }
        } else {
            try {
                File f = new File(args[0]);
                Scanner sc = new Scanner(f);
                JackTokeneizer jt =  new JackTokeneizer(sc, f.getPath(), f.getName());
                CompilationEngine ce = new CompilationEngine(f.getPath(), f.getName());
                sc.close();
            } catch(FileNotFoundException e) {

            }
            
        }
        //System.out.println(file[0]);
    }
}