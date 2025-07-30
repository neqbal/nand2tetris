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
            System.out.println(assembled);
            hack.add(assembled);
        }
        hack.add("(stop)\n@stop\n0;JMP");

        try {
            File obj = new File("./MemoryAccess/StaticTest/StaticTest.asm");
            if(obj.createNewFile()) System.out.println("file created");
            else System.out.println("file already exists");
        } catch (IOException e){
            System.out.println("error");
            e.printStackTrace();
        }

        try {
            FileWriter obj = new FileWriter("./MemoryAccess/StaticTest/StaticTest.asm");
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
        File obj = new File("./MemoryAccess/StaticTest/StaticTest.vm");
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