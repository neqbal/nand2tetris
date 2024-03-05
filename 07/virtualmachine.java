public class virtualmachine {
    int label = 0;
    public String command(String[] ins) {
        String c = "";
        String jump = "";
        System.out.println(ins[0]);
        if (ins[0].equals("eq")){ jump += "JEQ"; System.out.println(ins[0]);}
        else if(ins[0].equals("lt")) jump += "JLT";
        else if(ins[0].equals("gt")) jump += "JGT";

        switch(ins[0]) {
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
            default :
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
                c += "D;" + jump + "\n";
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
        System.out.println(ins[0]);
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
