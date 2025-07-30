import java.util.HashMap;

public class hack {
    HashMap<String, String> comp_inst = new HashMap<>();
    HashMap<String, String> dest_inst = new HashMap<>();
    HashMap<String, String> jump_inst = new HashMap<>();
    HashMap<String, Integer> symbol = new HashMap<>();
    int variable_count = 16;
    int ins_count = 0;
    public hack() {
        comp_inst.put("0", "101010"); comp_inst.put("1", "111111"); comp_inst.put("-1", "111010");
        comp_inst.put("D", "001100"); comp_inst.put("A", "110000"); comp_inst.put("!D", "001101");
        comp_inst.put("!A", "110001"); comp_inst.put("-D", "001111"); comp_inst.put("-A", "110011");
        comp_inst.put("D+1", "011111"); comp_inst.put("A+1", "110111"); comp_inst.put("D-1", "001110");
        comp_inst.put("A-1", "110010"); comp_inst.put("D+A", "000010"); comp_inst.put("D-A", "010011");
        comp_inst.put("A-D", "000111"); comp_inst.put("D&A", "000000"); comp_inst.put("D|A", "010101");

        dest_inst.put("", "000"); dest_inst.put("M", "001"); dest_inst.put("D", "010");
        dest_inst.put("MD", "011"); dest_inst.put("A", "100"); dest_inst.put("AM", "101");
        dest_inst.put("AD", "110"); dest_inst.put("AMD", "111");

        jump_inst.put("", "000"); jump_inst.put("JGT", "001"); jump_inst.put("JEQ", "010");
        jump_inst.put("JGE", "011"); jump_inst.put("JLT", "100"); jump_inst.put("JNE", "101");
        jump_inst.put("JLE", "110"); jump_inst.put("JMP", "111");

        symbol.put("SP", 0); symbol.put("LCL", 1); symbol.put("ARG", 2);
        symbol.put("THIS", 3); symbol.put("THAT", 4); symbol.put("SCREEN", 16384);
        symbol.put("KBD", 24576); symbol.put("R0", 0); symbol.put("R1", 1);
        symbol.put("R2", 2); symbol.put("R3", 3); symbol.put("R4", 4);
        symbol.put("R5", 5); symbol.put("R6", 6); symbol.put("R7", 7);
        symbol.put("R8", 8); symbol.put("R9", 9); symbol.put("R10", 10);
        symbol.put("R11", 11); symbol.put("R12", 12); symbol.put("R13", 13);
        symbol.put("R14", 14); symbol.put("R15", 15);
    }

    public void init_sym(String n, String m, int i) {
        symbol.put(n.substring(1, n.length() - 1), i);
        System.out.println(n + " " + i);
    }

    public String init_var(String n) {
        String a="";
        if(!symbol.containsKey(n)) {
            a += "@" + Integer.toString(variable_count);
            symbol.put(n, variable_count);
            ++variable_count;
        } else {
            a += "@" + Integer.toString(symbol.get(n));
            
        }
        return a;
    }
    /*0 for symbols
      1 for A command
      2 for C commandt
    */
    public int commandTypeU(String n) {
        //System.out.println(n);
        if(n.charAt(0) == '@') {
            if(Character.isDigit(n.charAt(1))) return 1;
            else return 3;   
        }
        if(n.charAt(0) == '(') {
            return 0;
        }
        return 2;
    }

    //return a binary of A instruction
    public String binary(int n) {
        String bin = "";
        if(n == 0) {
            bin = "0";
        }
        else if(n == 1) {
            bin = "1";

        } else {
            while(n!=0) {
                int bit = n%2;
                bin += Integer.toString(bit);
                n = n / 2;
            }
        }
        
        System.out.println(bin);
        char word[] = new char[16];
        for(int i=0; i<16 - bin.length(); ++i) {
            word[i] = '0';
        }
        int count = bin.length()-1;
        for(int i = 16 - bin.length(); i< 16; ++i) {
            word[i] = bin.charAt(count--);
        }
        return String.valueOf(word);
    }


    public String c_command(String n) {
        String dest="", comp="", jmp="";
        int a = 0, b = n.length();
        for(int i = 0; i<n.length(); ++i) {
            if(n.charAt(i) == '=') {
                dest = n.substring(0, i);
                a = i+1;
            }
            if(n.charAt(i) == ';') {
                jmp = n.substring(i+1);
                b = i;
                break;
            }
        }

        comp = n.substring(a, b);
        System.out.println("comp = " + comp );
        System.out.println("dest = " + dest );
        System.out.println("jmp = " + jmp );
        return cbin(dest, comp, jmp);
    }

    public String cbin(String dest, String comp, String jmp) {
        String bin = "";
        char word[] = new char[4];
        word[0] = '1';
        word[1] = '1';
        word[2] = '1';
        word[3] = '0';
        for(int i = 0; i<comp.length(); ++i) {
            if(comp.charAt(i) == 'M') {
                word[3] = '1';
                break;
            }
        }
        String str = new String(word);
        //System.out.println(str);
        System.out.println(comp_inst.get(comp.replaceAll("M", "A")));
        bin += str + comp_inst.get(comp.replaceAll("M", "A")) + dest_inst.get(dest) + jump_inst.get(jmp);
        return bin;
    }
    
}