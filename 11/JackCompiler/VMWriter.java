import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

class VMWriter {
    StringBuilder sb = new StringBuilder();
    SymbolTable st;
    String classname;
    String subroutinename;
    String subroutinetype="";
    static int varcount = 0;
    static int fieldcount = 0;

    HashMap<String, String> os = new HashMap<>();
    HashMap<String, String> opterm = new HashMap<>();
    public VMWriter(SymbolTable st) {
        this.st = st;
        defineOS();
        defineopterm();
    }
    public void create(String path, String name) {
        classname = name.replace(".jack", "");
        System.out.println(classname);
        try {
            File f = new File(path.replace(name, name.replace(".jack", ".vm")));
            if(f.createNewFile()) System.out.println("new file created");
            else System.out.println("file alreeady");
        } catch (IOException e) {
            System.out.println("hgasvd");
        }
    }
    public void writefunc() {
        varcount = SymbolTable.getVarcount();
        sb.append("function " + classname.trim() + "." + subroutinename.trim() + " " + varcount + "\n");
    }

    public void writeconstruct() {
        varcount = SymbolTable.getVarcount();
        sb.append("function " + classname.trim() + "." + subroutinename.trim() + " " + varcount + "\n");
        fieldcount = SymbolTable.fieldcount;
        sb.append("push constant " + fieldcount + "\n");
        sb.append("call Memory.alloc 1\n");
        sb.append("pop pointer 0\n");
    }

    public void writemethod() {
        sb.append("push argument 0\n");
        sb.append("pop pointer 0\n");
    }

    public void writemethodcall() {
        sb.append("push pointer 0\n");
    }
    public void writevm(String path, String name) {
        try {
            FileWriter w = new FileWriter(path.replace(name, name.replace(".jack", ".vm")));
            System.out.println(path.replace(name, name.replace(".jack", ".vm")));
            String c = sb.toString();
            w.write(c);
            w.close();
        } catch (IOException e) {
            System.out.println("ajbsf");
        }
    }

    public void writelet(Node n) {
        NodeList nList = n.getChildNodes();
        String kind = st.getkind(nList.item(1).getTextContent().trim());
        int index = st.getindex(nList.item(1).getTextContent().trim());
        //System.out.println(kind);
        if(nList.item(1).getTextContent().trim().equals("instance")) {
            System.out.println(kind);
        }
        if(nList.item(2).getTextContent().equals(" [ ")) {
            writepush(nList.item(1).getTextContent().trim());
            sb.append("add\n");
        } else if(kind.equals("var")) {
            sb.append("pop local " + index + "\n");
        } else if(kind.equals("argument")) {
            sb.append("pop argument " + index + "\n");
        } else if(kind.equals("field")) {
            sb.append("pop this " + index + "\n");
        } else if(kind.equals("static")) {
            sb.append("pop static " + index + "\n");
        }
    }

    public void writeendarray() {
        sb.append("pop temp 0\n");
        sb.append("pop pointer 1\n");
        sb.append("push temp 0\n");
        sb.append("pop that 0\n");
    }

    public void writeterm(Node n) {
        NodeList nList = n.getChildNodes();
        String c="";
        if(nList.getLength()==1) {
            if(nList.item(0).getNodeName().equals("stringConstant")) {
                writestring(nList.item(0).getTextContent().substring(1, nList.item(0).getTextContent().length() - 1));
            } else if(nList.item(0).getNodeName().equals("integerConstant")) {
                sb.append("push constant " + Integer.parseInt(nList.item(0).getTextContent().trim()) + "\n");
            } else if(nList.item(0).getNodeName().equals("identifier")) {
                String str = nList.item(0).getTextContent().trim();
                writepush(str);
            } else if(nList.item(0).getNodeName().equals("keyword")) {
                if(nList.item(0).getTextContent().equals(" null ") || nList.item(0).getTextContent().equals(" false ")) {
                    sb.append("push constant 0\n");
                } else if (nList.item(0).getTextContent().equals(" true ")) {
                    sb.append("push constant 0\n");
                    sb.append("not\n");
                } else if(nList.item(0).getTextContent().equals(" this ")) {
                    sb.append("push pointer 0\n");
                }
            }
        } else {
            if(nList.item(1).getTextContent().equals(" [ ")) {
                writepush(nList.item(0).getTextContent().trim());
                sb.append("add\n");
                sb.append("pop pointer 1\n");
                sb.append("push that 0\n");
            } else if(nList.item(0).getNodeName().equals("identifier")){
                for(int i=0; i<nList.getLength(); ++i) {
                    if(nList.item(i).getTextContent().equals(" ( ")) {
                        break;
                    } else {
                        c+=nList.item(i).getTextContent().trim();
                    }
                }

                if(c.contains(".")) {
                    int o = c.indexOf(".");
                    String str = c.substring(0, o);
                    if(!st.gettype(str).equals("")) {
                        sb.append("call " + st.gettype(str) + "." + c.substring(o+1, c.length()) + " " + (CompilationEngine.countexpressionlist + 1) + "\n");
                    } else {
                        sb.append("call " + c + " " + CompilationEngine.countexpressionlist + "\n");
                    }
                    
                } else {
                    if(st.getkind(c).equals("")){
                        sb.append("call " + classname + "." + c + " " + (CompilationEngine.countexpressionlist + 1) + "\n");
                    }
                    else sb.append("call " + classname + "." + c + " " + (CompilationEngine.countexpressionlist+1)  + "\n");
                }
/*                     sb.append("call " + c + " " + CompilationEngine.countexpressionlist + "\n");
                } else {
                    sb.append("call " + classname + "." + c + " " + (CompilationEngine.countexpressionlist+1)  + "\n");
                } */
            }
        }
    }

    public void writedo(Node n) {
        NodeList nList = n.getChildNodes();
        String c="";
        for(int i=1; i<nList.getLength(); ++i) {
            if(nList.item(i).getTextContent().equals(" ( ")) {
                break;
            } else {
                c+=nList.item(i).getTextContent().trim();
            }
        }

        if(os.containsKey(c)) {
            sb.append(os.get(c) + "\n");
        } else {
            if(c.contains(".")) {
                int o = c.indexOf(".");
                String str = c.substring(0, o);
                if(!st.gettype(str).equals("")) {
                    sb.append("call " + st.gettype(str) + "." + c.substring(o+1, c.length()) + " " + (CompilationEngine.countexpressionlist + 1) + "\n");
                } else {
                    sb.append("call " + c + " " + CompilationEngine.countexpressionlist + "\n");
                }
                
            } else {
                if(st.getkind(c).equals("")){
                    sb.append("call " + classname + "." + c + " " + (CompilationEngine.countexpressionlist + 1) + "\n");
                }
                else sb.append("call " + classname + "." + c + " " + (CompilationEngine.countexpressionlist+1)  + "\n");
            }
            
        }
        sb.append("pop temp 0\n");
    }

    public void writeobjectmethodcall(String str) {
        //System.out.println("asuygdjahs *********" + str.trim());
        writepush(str.trim());
    }

    public void writepush(String str) {
        String kind = st.getkind(str);
        int index = st.getindex(str);
        if(kind.equals("var")) {
            sb.append("push local " + index + "\n");
        } else if(kind.equals("argument")) {
            sb.append("push argument " + index + "\n");
        } else if(kind.equals("static")) {
            sb.append("push static " + index + "\n");
        } else if(kind.equals("field")) {
            sb.append("push this " + index + "\n");
        }
    }

    public void writestring(String str) {

        sb.append("push constant " + str.length() + "\n");
        sb.append("call String.new 1\n");
        for(int i=0; i<str.length(); ++i) {
            sb.append("push constant " + (int)str.charAt(i) + "\n");
            sb.append("call String.appendChar 2\n");
        }
    }

    public void print() {
        System.out.println(sb.toString());
    }

    public void writearith(String op) {
        sb.append(opterm.get(op));
    }

    public void writeuniop(String op) {
        if(op.equals("-")) {
            sb.append("neg\n");
        } else if(op.equals("~")) {
            sb.append("not\n");
        }
    }

    public void writelabel(String str) {
        sb.append("label " + str + "\n");
    }

    public void writeif(String str) {
        sb.append("if-goto " + str + "\n");
    }

    public void writegoto(String str) {
        sb.append("goto " + str + "\n");
    }

    public void writeret() {
        if(subroutinetype.equals("void")) {
            sb.append("push constant 0\n");
            subroutinetype="";
        }
        sb.append("return\n");
    }
    public void defineOS() {
        os.put("Math.init", "call Math.init 0"); os.put("Math.abs", "call Math.abs 1"); os.put("Math.multiply", "call Math.multiply 2");
        os.put("Math.divide", "call Math.divide 2"); os.put("Math.min", "call Math.min 2"); os.put("Math.max", "call Math.max 2"); 
        os.put("Math.sqrt", "call Math.sqrt 1");
        
        os.put("Array.new", "call Array.new 1");

        os.put("Output.init", "call Output.init 0"); os.put("Output.moveCursor", "call Output.moveCursor 2"); os.put("Output.printChar", "call Output.printChar 1");
        os.put("Output.printString", "call Output.printString 1"); os.put("Output.printInt", "call Output.printInt 1"); os.put("Output.println", "call Output.println 0");
        os.put("Output.backSpace", "call Output.Space 0");

        os.put("Keyboard.init", "call Keyboard.init 0"); os.put("Keyboard.keyPressed", "call Keyboard.keyPressed 0"); os.put("Keyboard.readChar", "call Keyboard.readChar 0");
        os.put("Keyboard.readLine", "call Keyboard.readLine 1"); os.put("Keyboard.readInt", "call Keyboard.readInt 1");

    }

    public void defineopterm() {
        opterm.put("+", "add\n"); opterm.put("-", "sub\n"); opterm.put("*", "call Math.multiply 2\n"); 
        opterm.put("/", "call Math.divide 2\n"); opterm.put("&", "and\n"); opterm.put("|", "or\n"); 
        opterm.put("<", "lt\n"); opterm.put(">", "gt\n"); opterm.put("=", "eq\n");
    }
}