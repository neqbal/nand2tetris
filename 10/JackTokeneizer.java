import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
    public JackTokeneizer(Scanner sc, String path, String name) {
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
       
        writetoken(path, name);
        //for(String s: tokenized) System.out.println(s);
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
            for(int i=0; i<s.length(); ++i) {
                if(s.charAt(i) >= 65 && s.charAt(i) <=90 || s.charAt(i) >= 97 && s.charAt(i) <= 122) {
                    int j = identorkeyword(i, s);
                    tokenized.add(s.substring(i, j));
                    i=j-1;
                } else if(s.charAt(i) == '"') {
                    int j = stringconstant(i, s);
                    tokenized.add(s.substring(i, j+1));
                    i = j;
                } else {
                    if(s.charAt(i) != ' ') {
                        tokenized.add(""+s.charAt(i));
                    }
                }
            }
        }
    }
    public int stringconstant(int i, String s) {
        for(int j=i+1; j<s.length(); ++j) {
            if(s.charAt(j) == '"') {
                return j;
            }
        }
        return i;
    }
    public int identorkeyword(int i, String s) {
        for(int j=i; j<s.length(); ++j) {
            if(symbols.contains(""+s.charAt(j)) || s.charAt(j) == '"' || s.charAt(j) == ' ') {
                return j; 
            } 
        }
        return i+1;
    }
    public void writetoken(String path, String name) {
        System.out.println(path);
        try {
            File f = new File(path.replace(name, "/output/") + name.replace(".jack", "lT.xml"));
            if(f.createNewFile()) System.out.println("file created");
            else System.out.println("file already created");
        } catch(IOException e) {
            System.out.println("hoolhohl;oh");
        }

        try {
            FileWriter w = new FileWriter(path.replace(name, "/output/") + name.replace(".jack", "lT.xml"));
            w.write("<tokens>\n");
            for(String s: tokenized) {
                String type = tokentype(s);
                String n = "";
                if(type.equals("keyword")) n += "<keyword> " + s + " </keyword>\n";
                else if(type.equals("symbol")) {
                    if (s.equals("<")) s = "&lt";
                    else if(s.equals(">")) s = "&gt"; 
                    n += "<symbol> " + s + " </symbol>\n";
                }
                else if(type.equals("integerConstant"))  n += "<integerConstant> " + s + " </inetegerConstant>\n";
                else if(type.equals("stringConstant")) n += "<stringConstant> " + s.subSequence(1, s.length() - 1) + " </stringConstant>\n";
                else n += "<identifier> " + s + " </identifier>\n"; 
                w.write(n);
            }
            w.write("</tokens>");
            w.close();
        } catch(IOException e) {
            System.out.println("whajndksm");
        }

    }
    public String tokentype(String s) {
        if(keyword.contains(s)) return "keyword";
        else if(symbols.contains(s)) return "symbol";
        else if(Character.isDigit(s.charAt(0))) return "integerConstant";
        else if(s.charAt(0) == '"') return "stringConstant";
        return "identifier";
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
