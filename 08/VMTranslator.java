import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class VMTranslator {
    static List<String> lines = new ArrayList<>();
    static List<String> hack = new ArrayList<>();
    static int sys = 0;
    static int ret = -1, label = -1;
    public void converter(String name) {
        virtualmachine vm = new virtualmachine();
        
        for(String s: lines) {
            String str[] = s.split(" ");
            //System.out.println(str[0]);
            if(str[0].equals("call")) ret++;
            if(str[0].equals("lt") || str[0].equals("eq") || str[0].equals("gt") || str[0].equals("neg")) label++;

            //for(String o: str) System.out.print(o);
            String assembled = vm.command(str, name, ret, label);
            hack.add(assembled);
        }
    }

    public void write(String dir) {
        try {
            File obj = new File("./ProgramFlow/FibonacciSeries/FibonacciSeries.asm");
            if(obj.createNewFile()) System.out.println("file created");
            else System.out.println("file already exists");
        } catch (IOException e) {
            System.out.println("error");
            e.printStackTrace();
        }

        try {
            FileWriter obj = new FileWriter("./ProgramFlow/FibonacciSeries/FibonacciSeries.asm");
            for(String s: hack) {
                obj.write(s);
            }
            obj.close();
        } catch (IOException e) {
            System.out.println("error");
            e.printStackTrace();
        }
    }

    public static void main(String args[]) {
        //System.out.println("akjsndkjasbndkj");
        VMTranslator vmt = new VMTranslator();
        File dir = new File(args[0]);
        File[] files = dir.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(".vm");
            }
        }); 
        //String temp = "./FunctionCalls/SimpleFunction" + "/" + "Sys" + ".vm";

/*         for(int i=0; i<args.length; ++i) {
            if(args[i].equals("Sys.vm")) {
                hack.add("@261\nD=A\n@SP\nM=D\n@LCL\nM=D\n");
                hack.add("@Sys.init\n0;JMP\n");
            }
        } */
        for(int k=0; k<files.length; ++k) {
            if(files[k].getName().equals("Sys" + ".vm")) {
                hack.add("@261\nD=A\n@SP\nM=D\n@LCL\nM=D\n");
                hack.add("@Sys.init\n0;JMP\n");
            }
        } 

        for(int j=0; j<files.length; ++j) {
            try {
                //File obj = new File(args[j]);
                Scanner sc = new Scanner(files[j]);
                while(sc.hasNextLine()) {
                    String data = sc.nextLine();
                    if(data != "" && data.charAt(0) != '/' && data.charAt(1) != '/') {
                        for(int i=0; i < data.length(); ++i) {
                            if(data.charAt(i) == '/') {
                                data = data.substring(0, i);
                                //System.out.println(data);
                                break;
                            }
                        }
                        lines.add(data.trim());  
                    }
                }
                sc.close();
            } catch (FileNotFoundException e) {
                System.out.println("Scanner error");
                e.printStackTrace();
            } 
            vmt.converter(files[j].getName());
            lines.clear();
            //hack.add("lol\n");
            System.out.println(files[j].getName());  
        }
        vmt.write(args[0]);
        //hack.add("(stop)\n@stop\n0;JMP\n");
        
    } 
}