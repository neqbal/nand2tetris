import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

public class assembler {
    static List<String> lines = new ArrayList<String>();

    public void converter (List<String> l) {
        hack Hack = new hack();
        List<String> bin = new ArrayList<String>();

        //init symbols
        int j=0;
        for(int i=0; i<l.size(); ++i) {
            int cmdtype = Hack.commandTypeU(l.get(i));
            //System.out.println(l.get(i) + " " + j);
            if(cmdtype == 0) {    
                Hack.init_sym(l.get(i), l.get(i+1), j);
                //lines.remove(i);
                //System.out.println(j);
                continue;
            }
            ++j;
        }

        //init variables 
        for(int i=0; i<l.size(); ++i) {
            int cmdtype = Hack.commandTypeU(l.get(i));
            //System.out.println(l.get(i) + ""+ i);
            if(cmdtype == 3) {
                String a = Hack.init_var(l.get(i).substring(1));
                System.out.println(l.get(i) + " " + a);
                lines.set(i, a);
            }
        }
        for(String n: lines) {
            int cmdtype = Hack.commandTypeU(n);
            System.out.println(n);
            switch (cmdtype) {
                //A command
                case 1:
                    int adecimal = Integer.parseInt(n.substring(1));
                    String abin = Hack.binary(adecimal);
                    System.out.println(abin);
                    bin.add(abin + "\n");
                    break;
                //C command
                case 2:
                    String c = Hack.c_command(n.replaceAll("\\s+", ""));
                    System.out.println(c);
                    bin.add(c+"\n");
                    break;
            }
        }
        try {
            File myObj = new File("../rect/Rect.hack");
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        try {
            FileWriter mywriter = new FileWriter("../rect/Rect.hack");
            for(String s: bin) {
                mywriter.write(s);
            }
            mywriter.close();
        } catch(IOException e) {
            System.out.println("error");
            e.printStackTrace();
        }
    }

    
    public static void main(String args[]) {
        assembler ass = new assembler();
        File obj = new File("../rect/Rect.asm");
        try {
            Scanner reader = new Scanner(obj);
            while (reader.hasNextLine()) {
                String data = reader.nextLine();
                if(data != "" && !(data.contains("//"))) {
                    lines.add(data.replaceAll(" ", ""));
                    //System.out.println(data);
                } 
                
            }
            reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("scanner error");
            e.printStackTrace();
        }
        ass.converter(lines);
    }
}