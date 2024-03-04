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
            String assembled = vm.command(str);
            hack.add(assembled);
        }

        try {
            File obj = new File("./StackArithmetic/SimpleAdd/SimpleAdd.asm");
            if(obj.createNewFile()) System.out.println("file created");
            else System.out.println("file already exists");
        } catch (IOException e){
            System.out.println("error");
            e.printStackTrace();
        }

        try {
            FileWriter obj = new FileWriter("./StackArithmetic/SimpleAdd/SimpleAdd.asm");
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
        VMTranslator vmt = new VMTranslator();
        File obj = new File("./StackArithmetic/SimpleAdd/SimpleAdd.vm");
        try {
            Scanner sc = new Scanner(obj);
            while(sc.hasNextLine()) {
                String data = sc.nextLine();
                if(data != "" && !data.contains("//")) lines.add(data);
            }
            sc.close();
        } catch (FileNotFoundException e) {
            System.out.println("Scanner error");
            e.printStackTrace();
        }
        vmt.converter();
    }
}