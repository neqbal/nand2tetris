import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class SymbolTable {
    
    public class variableinfo {
        String type;
        String kind;
        int index;
        variableinfo(String type, String kind, int index) {
            this.type = type;
            this.kind = kind;
            this.index = index;
        }
    }

    public HashMap<String, variableinfo> classvar = new HashMap<>();;
    public HashMap<String, variableinfo> subroutinevar = new HashMap<>();;
    static int staticcount = 0;
    static int fieldcount = 0;
    static int paramcount = 0;
    public static int varcount = 0;

    public SymbolTable() {
    }

    public void cscope(Element classVarDec) {

        NodeList nList = classVarDec.getChildNodes();
        
        String kind = nList.item(0).getTextContent().trim();
        String type = nList.item(1).getTextContent().trim();
        
        int temp;


        for(int i=2; i<nList.getLength();  ++i) {
            if(nList.item(i).getNodeName().equals("identifier")) {
                if(kind.equals("field")) {
                    classvar.put(nList.item(i).getTextContent().trim(), new variableinfo(type, kind, fieldcount++));
                    //System.out.println(nList.item(i).getTextContent().trim() + " " + type + " " + kind + " " + fieldcount);
                }
                else {
                    classvar.put(nList.item(i).getTextContent().trim(), new variableinfo(type, kind, staticcount++));
                    System.out.println(nList.item(i).getTextContent() + " " + type + " " + kind + " " + fieldcount);
                }
                
            }
        }
    }

    public void SubroutineScope(Element var) {

        NodeList nList = var.getChildNodes();
        
        String kind = nList.item(0).getTextContent().trim();
        String type = nList.item(1).getTextContent().trim();


        for(int i=2; i<nList.getLength();  ++i) {
            if(nList.item(i).getNodeName().equals("identifier")) {
                subroutinevar.put(nList.item(i).getTextContent().trim(), new variableinfo(type, kind, varcount++));
                //System.out.println(nList.item(i).getTextContent().trim() + " " + type + " " + kind + " " + varcount);
            }
        }

    }

    public void parameterTable(Element param) {
        //System.out.println("asdf");
        NodeList nList = param.getChildNodes();
        String kind = "argument";

        int i=1;
        while(i<nList.getLength()) {
            String text = nList.item(i).getTextContent();
            if(text.equals(" , ")) {
                i=i+2;
            } else {
                String type = nList.item(i-1).getTextContent().trim();
                String name = nList.item(i).getTextContent().trim();
                subroutinevar.put(name, new variableinfo(type, kind, paramcount++));
                //System.out.println(name + " " + type + " " + kind + " " + paramcount);
                i = i + 1; 
            }
        }
    }

    public void methodEntry(String str) {
        subroutinevar.put("this", new variableinfo(str, "argument", paramcount++));
    }

    

    public String getkind(String str) {
        if(subroutinevar.containsKey(str)) {
            return subroutinevar.get(str).kind;
        } else if(classvar.containsKey(str)) {
            return classvar.get(str).kind;
        }

        return "";
    }

    public String gettype(String str) {
        if(subroutinevar.containsKey(str)) {
            return subroutinevar.get(str).type;
        } else if(classvar.containsKey(str)) {
            return classvar.get(str).type;
        }

        return "";
    }

    public int getindex(String str) {
        if(subroutinevar.containsKey(str)) {
            return subroutinevar.get(str).index;
        } else if(classvar.containsKey(str)) {
            return classvar.get(str).index;
        }

        return 0;
    }

    public static int getVarcount() {
        //System.out.println(varcount);
        return varcount;
    }

    public void ClearSubroutineTable() {
        varcount = 0;
        paramcount = 0;
        subroutinevar.clear();
    }

    public void ClearClassTable() {
        staticcount = 0;
        fieldcount = 0;
        classvar.clear();
    }
}
