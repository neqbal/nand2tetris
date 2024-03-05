public class virtualmachine {
    int label = 0;
    public String command(String[] ins) {
        String c = "";
        String jump = "";
        //System.out.println(ins[0]);
        if (ins[0].equals("eq")){ jump += "JEQ"; System.out.println(ins[0]);}
        else if(ins[0].equals("lt")) jump += "JLT";
        else if(ins[0].equals("gt")) jump += "JGT";

        switch(ins[0]) {
            case "push":
                c = seg(ins);            
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
            /* case "lt":
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
                break;        */       

        }
        return c;
    }
    
    public String seg(String []ins) {
        String c = "";
        switch(ins[1]) {
            case "constant":
                c += "@" + ins[2] + "\n";
                c += "D=A\n";
                c += "@SP\n";
                c += "A=M\n";
                c += "M=D\n";
                c += "@SP\n";
                c += "M=M+1\n"; 
                //System.out.println(c);
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
