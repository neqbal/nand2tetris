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

    HashMap<String, variableinfo> classvar = new HashMap<>();
    HashMap<String, variableinfo> subroutinevar = new HashMap<>();
    static int staticcount = 0;
    static int fieldcount = 0;

    public SymbolTable() {
    }

    public void cscope(Element classVarDec) {
        NodeList nList = classVarDec.getChildNodes();

        String kind = nList.item(0).getTextContent();
        String type = nList.item(1).getTextContent();

        for(int i=2; i<nList.getLength(); ++i) {
            String nodename = nList.item(i).getNodeName();
            String name = nList.item(i).getTextContent();
            if(nodename.equals(" identifier ")) {
                if(kind.equals(" static ")) classvar.put(name, new variableinfo(type, kind, staticcount++));
                else classvar.put(name, new variableinfo(type, kind, fieldcount++));
            }
        }
    }

    public void SubroutineScope(Element sub, String type) {
        
    }

    public void ClearSubroutineTable() {

    }
}
