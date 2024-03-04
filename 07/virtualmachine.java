public class virtualmachine {
    public String command(String[] ins) {
        String c = "";
        switch(ins[0]) {
            case "push":
                c = seg(ins);            
                break;
            case "add":
                c = add(ins);

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
                System.out.println(c);
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
        c += "M=M+1";
        return c;
    }
}
