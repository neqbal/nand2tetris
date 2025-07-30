public class virtualmachine {
    //int label = 0;
    String cond = "";
    //int ret = 0;
    String jump = "";
    public String command(String[] ins, String name, int ret, int label) {
        String c = "";
        //System.out.println(ins[0]);
        if (ins[0].equals("eq")) {jump += "JEQ"; cond = jump;}
        else if(ins[0].equals("lt")) {jump += "JLT"; cond = jump;}
        else if(ins[0].equals("gt")) {jump += "JGT"; cond = jump;}
        
        //System.out.println(cond);
        switch(ins[0]) {
            case "function":
                c += "(" + ins[1] + ")\n";
                int local = Integer.parseInt(ins[2]);
                for(int i=0; i<local; ++i) {
                    c += "@SP\n";
                    c += "A=M\n";
                    c += "M=0\n";
                    c += "@SP\n";
                    c += "M=M+1\n"; 
                } 
                break;
            case "call":
                c += "//FUNCTION CALL START\n";
                c += "@return" + Integer.toString(ret) + "\n";
                c += "D=A\n";
                c += "@SP\n";
                c += "A=M\n";
                c += "M=D\n";
                c += "@SP\n";
                c += "M=M+1\n";
                c += "@LCL\n";
                c += "D=M\n";
                c += "@SP\n";
                c += "A=M\n";
                c += "M=D\n";
                c += "@SP\n";
                c += "M=M+1\n";
                c += "@ARG\n";
                c += "D=M\n";
                c += "@SP\n";
                c += "A=M\n";
                c += "M=D\n";
                c += "@SP\n";
                c += "M=M+1\n";
                c += "@THIS\n";
                c += "D=M\n";
                c += "@SP\n";
                c += "A=M\n";
                c += "M=D\n";
                c += "@SP\n";
                c += "M=M+1\n";
                c += "@THAT\n";
                c += "D=M\n";
                c += "@SP\n";
                c += "A=M\n";
                c += "M=D\n";
                c += "@SP\n";
                c += "M=M+1\n";


                c += "@SP\n";
                c += "D=M\n";
                c += "@" + ins[2] + "\n";
                c += "D=D-A\n";
                c += "@5\n";
                c += "D=D-A\n";
                c += "@ARG\n";
                c += "M=D\n";

                
                c += "@SP\n";
                c += "D=M\n";
                c += "@LCL\n";
                c += "M=D\n";
                c += "@" + ins[1] + "\n";
                c += "0;JMP\n";
                c += "(return" + Integer.toString(ret) + ")\n";
                c += "//FUNCTION CALL END\n";
                //ret++;
                System.out.println(ret);
                System.out.println(c);
                System.out.println("******************************************\n********\n********\n*****");
                break;

            case "return":
                
                c += "@LCL\n";
                c += "D=M\n";
                c += "@R14\n"; //frame
                c += "M=D\n";
                c += "@R14\n"; //frame
                c += "D=M\n";
                c += "@5\n";
                c += "D=D-A\n";
                c += "A=D\n";
                c += "D=M\n";
                c += "@R15\n"; //ret
                c += "M=D\n";
                c += "@SP\n";
                c += "M=M-1\n";
                c += "A=M\n";
                c += "D=M\n";
                c += "@ARG\n";
                c += "A=M\n";
                c += "M=D\n";
                c += "@ARG\n";
                c += "D=M+1\n";
                c += "@SP\n";
                c += "M=D\n";
                c += "@R14\n"; //frame
                c += "M=M-1\n";
                c += "A=M\n";
                c += "D=M\n";
                c += "@THAT\n";
                c += "M=D\n";
                c += "@R14\n";  //frame
                c += "M=M-1\n";
                c += "A=M\n";
                c += "D=M\n";
                c += "@THIS\n";
                c += "M=D\n";
                c += "@R14\n";  //frame
                c += "M=M-1\n";
                c += "A=M\n";
                c += "D=M\n";
                c += "@ARG\n";
                c += "M=D\n";
                c += "@R14\n";  //frame
                c += "M=M-1\n";
                c += "A=M\n";
                c += "D=M\n";
                c += "@LCL\n";
                c += "M=D\n";
                c += "@R15\n"; //ret
                c += "A=M\n";
                c += "0;JMP\n";
                break;
            
            case "label":
                c += "(" + ins[1] + ")\n";
                break;
            case "goto":
                c += "@" + ins[1] + "\n";
                c += "0;JMP\n";
                break;
            case "if-goto":
                c += "@SP\n";
                c += "M=M-1\n";
                c += "A=M\n";
                c += "D=M\n";
                c += "@" + ins[1] + "\n";
                if(cond.equals("")) {
                    c += "D;JGT\n";
                } else {
                    c += "D+1;JEQ\n";
                }
                cond = "";
                break;
            
            case "push":
            case "pop":
                c = seg(ins[0], ins, name);
                break;
            case "add":
                c = add(ins);
                break;
            case "sub":
                c = sub(ins);
                break;
            
            case "neg":
                c += "@SP\n";
                c += "M=M-1\n";
                c += "A=M\n";
                c += "M=-M\n";
                c += "@SP\n";
                c += "M=M+1\n";
                //label++;
                //System.out.println(label);
                break;

            case "and":
                c += "@SP\n";
                c += "M=M-1\n";
                c += "A=M\n";
                c += "D=M\n";
                c += "@SP\n";
                c += "M=M-1\n";
                c += "A=M\n";
                c += "M=M&D\n";
                c += "@SP\n";
                c += "M=M+1\n";
                //System.out.println(label);
                break;
            case "or":
                c += "@SP\n";
                c += "M=M-1\n";
                c += "A=M\n";
                c += "D=M\n";
                c += "@SP\n";
                c += "M=M-1\n";
                c += "A=M\n";
                c += "M=M|D\n";
                c += "@SP\n";
                c += "M=M+1\n";
                //System.out.println(label);
                break;      

            case "not":
                c += "@SP\n";
                c += "M=M-1\n";
                c += "A=M\n";
                c += "M=!M\n";
                c += "@SP\n";
                c += "M=M+1\n";
                //System.out.println(label);
                break;  
            
            case "gt":
                c += "@SP\n";
                c += "M=M-1\n";
                c += "A=M\n";
                c += "D=M\n";
                c += "@SP\n";
                c += "M=M-1\n";
                c += "A=M\n";
                c += "D=M-D\n";
                c += "M=-1\n";
                c += "@true" + Integer.toString(label) + "\n";
                c += "D;JGT\n";
                c += "@SP\n";
                c += "A=M\n";
                c += "M=0\n";
                c += "(true" + Integer.toString(label) + ")\n";
                c += "@SP\n";
                c += "M=M+1\n";
                //label++;
                //System.out.println(label);
                break;    

            case "eq" :
                c += "@SP\n";
                c += "M=M-1\n";
                c += "A=M\n";
                c += "D=M\n";
                c += "@SP\n";
                c += "M=M-1\n";
                c += "A=M\n";
                c += "D=M-D\n";
                c += "M=-1\n";
                c += "@true" + Integer.toString(label) + "\n";
                c += "D;JEQ\n";
                c += "@SP\n";
                c += "A=M\n";
                c += "M=0\n";
                c += "(true" + Integer.toString(label) + ")\n";
                c += "@SP\n";
                c += "M=M+1\n";
                //label++;
                //System.out.println(label);
                break; 
            case "lt" :
                c += "@SP\n";
                c += "M=M-1\n";
                c += "A=M\n";
                c += "D=M\n";
                c += "@SP\n";
                c += "M=M-1\n";
                c += "A=M\n";
                c += "D=M-D\n";
                c += "M=-1\n";
                c += "@true" + Integer.toString(label) + "\n";
                c += "D;JLT\n";
                c += "@SP\n";
                c += "A=M\n";
                c += "M=0\n";
                c += "(true" + Integer.toString(label) + ")\n";
                c += "@SP\n";
                c += "M=M+1\n";
                //label++;
                //System.out.println(label);
                break;    
        }
        return c;
    }
    
    public String seg(String instruction, String []ins, String name) {
        String r = "";
        String c = "";
        //System.out.println(ins[0]);
        switch(ins[1]) {
            case "constant":
                c += "@" + ins[2] + "\n";
                c += "D=A\n";
                c += "@SP\n";
                c += "A=M\n";
                c += "M=D\n";
                c += "@SP\n";
                c += "M=M+1\n"; 
                return c;
                //System.out.println(c);
            case "argument":
                r += "@ARG\n";
                r += "D=M\n";
                r += "@" + ins[2] + "\n";
                r += "A=A+D\n";
                break;
            case "this":
                r += "@THIS\n";
                r += "D=M\n";
                r += "@" + ins[2] + "\n";
                r += "A=A+D\n";
                break;
            case "that":
                r += "@THAT\n";
                r += "D=M\n";
                r += "@" + ins[2] + "\n";
                r += "A=A+D\n";
                break;
            case "local":
                r += "@LCL\n";
                r += "D=M\n";
                r += "@" + ins[2] + "\n";
                r += "A=A+D\n";
                break;
            case "temp":
                r += "@" + "R" + Integer.toString(5 + Integer.parseInt(ins[2])) + "\n";
                break;
            case "pointer":
                r += "@R" + Integer.toString(3 + Integer.parseInt(ins[2])) + "\n";
                break;
            case "static":
                r += "@" + name + "." + ins[2] + "\n";

        }
        switch(instruction) {
            case "push" :
                c+=r;
                c += "D=M\n";
                c += "@SP\n";
                c += "A=M\n";
                c += "M=D\n";
                c += "@SP\n";
                c += "M=M+1\n";
                break;
            case "pop":
                c += r;
                c += "D=A\n";
                c += "@R13\n";
                c += "M=D\n";
                c += "@SP\n";
                c += "M=M-1\n";
                c += "A=M\n";
                c += "D=M\n";
                c += "@R13\n";
                c += "A=M\n";
                c += "M=D\n";
                break;

        }
        return c;
    }

    public String add(String[] ins) {
        String c = "";
        c += "@SP\n";
        c += "M=M-1\n";
        c += "A=M\n";
        c += "D=M\n";
        c += "@SP\n";
        c += "M=M-1\n";
        c += "@SP\n";
        c += "A=M\n";
        c += "M=M+D\n";
        c += "@SP\n";
        c += "M=M+1\n";
        return c;
    }

    public String sub(String[] ins) {
        String c = "";
        c += "@SP\n";
        c += "M=M-1\n";
        c += "A=M\n";
        c += "D=M\n";
        c += "@SP\n";
        c += "M=M-1\n";
        c += "@SP\n";
        c += "A=M\n";
        c += "M=M-D\n";
        c += "@SP\n";
        c += "M=M+1\n";
        return c;   
    }
}
