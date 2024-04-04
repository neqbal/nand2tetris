import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Scanner;

public class JackTokeneizer {
    List<String> keyword = new ArrayList<>();
    List<String> symbols = new ArrayList<>();

    List<String> code = new ArrayList<>();
    List<String> tokenized = new ArrayList<>();
    boolean multlinecmt = false;
    public JackTokeneizer(Scanner sc) {
        System.out.println("ashbdjhas");
        initgrammer();
        while(sc.hasNextLine()) {
            String line = sc.nextLine();
            line = line.trim();
            if(line.equals("")) continue;
            String s = isComment(line);
            if(s.equals("INLC") && multlinecmt == false) {
                for(int i=0; i<line.length(); ++i) {
                    if(line.charAt(i) == '/' && line.charAt(i+1) == '/') {
                        code.add(line.substring(0, i).trim());
                    }
                }
            } else if(s.equals("STMLC")) {
                multlinecmt = true;
            } else if(s.equals("EDMLC")) {
                multlinecmt = false;
            } else if(s.equals("NC") && multlinecmt == false) {
                code.add(line);
            }
        }
        seperate();
        if(symbols.contains("" + '{')) System.out.println("agsdhjb");
       
        //writetoken(path, name);
        //for(String s: tokenized) System.out.print(s);
    }

    public String isComment(String line) {
        if(line.startsWith("//")) return "SLC";
        else if(line.startsWith("/*") && !line.endsWith("*/")) return "STMLC";
        else if(line.startsWith("/*") && line.endsWith("*/")) return "SLC";
        else if(line.endsWith("*/")) return "EDMLC";
        else if(line.contains("//")) return "INLC";
        return "NC";
    }

    public List<String> abc() {
        return code;
    }
    public void seperate() {
        for(String s: code) {
            String c="";
            for(int i=0; i<s.length(); ++i) {
                if(s.charAt(i) >= 65 && s.charAt(i) <=90 || s.charAt(i) >= 97 && s.charAt(i) <= 122) {
                    c += s.charAt(i);
                } else {
                    //System.out.println(s.charAt(i));
                    if(c.length()>0) {
                        tokenized.add(c);
                        c = "";
                    }
                    if(s.charAt(i) != ' ') {
                        tokenized.add(""+s.charAt(i));
                    }
                    
                }
            }
        }

    }
    public void writetoken(String path, String name) {
        //File f = new File(path + "/output/" + name.replace(".jack", "T.xml"));

    }

    public void initgrammer() {
        keyword.add("class"); keyword.add("constructor"); keyword.add("function");
        keyword.add("method"); keyword.add("field"); keyword.add("static"); keyword.add("var");
        keyword.add("int"); keyword.add("char"); keyword.add("boolean"); keyword.add("void"); keyword.add("true");
        keyword.add("false"); keyword.add("null"); keyword.add("this"); keyword.add("let"); keyword.add("do");
        keyword.add("if"); keyword.add("else"); keyword.add("while"); keyword.add("return");

        symbols.add("{"); symbols.add("}"); symbols.add("("); symbols.add(")"); symbols.add("["); symbols.add("]"); symbols.add(".");
        symbols.add(","); symbols.add(";"); symbols.add("+"); symbols.add("-"); symbols.add("*"); symbols.add("/"); symbols.add("&");
        symbols.add("|"); symbols.add("<"); symbols.add(">"); symbols.add("="); symbols.add("~");
    }
}
