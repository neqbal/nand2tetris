import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class VMTranslator {
    static List<String> lines = new ArrayList<>();
    public void converter() {
        virtualmachine vm = new virtualmachine();
        List<String> hack = new ArrayList<>();
        for(String s: lines) {
            String str[] = s.split(" ");
            System.out.println(str[0]);
            //for(String o: str) System.out.print(o);
            String assembled = vm.command(str);
            hack.add(assembled);
        }
        hack.add("(stop)\n@stop\n0;JMP");

        try {
            File obj = new File("./ProgramFlow/FibonacciSeries/FibonacciSeries.asm");
            if(obj.createNewFile()) System.out.println("file created");
            else System.out.println("file already exists");
        } catch (IOException e){
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
        File obj = new File("./ProgramFlow/FibonacciSeries/FibonacciSeries.vm");
        try {
            Scanner sc = new Scanner(obj);
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
        vmt.converter();
    }
}