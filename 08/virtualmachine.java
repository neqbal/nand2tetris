public class virtualmachine {
    int label = 0;
    String cond = "";
    public String command(String[] ins) {
        String c = "";
        String jump = "";
        //System.out.println(ins[0]);
        if (ins[0].equals("eq")) jump += "JEQ";
        else if(ins[0].equals("lt")) jump += "JLT";
        else if(ins[0].equals("gt")) jump += "JGT";
        cond = jump;

        switch(ins[0]) {
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
                if(!cond.equals("")) {
                    c += "D+1;JEQ\n";
                } else {
                    c += "D;JGT\n";
                }
                break;
            
            case "push":
            case "pop":
                c = seg(ins[0], ins);
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
                label++;
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
                label++;
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
                label++;
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
                label++;
                //System.out.println(label);
                break;    
        }
        return c;
    }
    
    public String seg(String instruction, String []ins) {
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
                r += "@" + Integer.toString(400 + Integer.parseInt(ins[2])) + "\n";
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
                r += "@" + Integer.toString(300 + Integer.parseInt(ins[2])) + "\n";
                break;
            case "temp":
                r += "@" + "R" + Integer.toString(5 + Integer.parseInt(ins[2])) + "\n";
                break;
            case "pointer":
                r += "@R" + Integer.toString(3 + Integer.parseInt(ins[2])) + "\n";
                break;
            case "static":
                r += "@" + Integer.toString(16 + Integer.parseInt(ins[2])) + "\n";

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
