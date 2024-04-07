import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.w3c.dom.Element;

public class CompilationEngine {
    NodeList nList;
    static int i=-1;
    List<String> list = new ArrayList<>();
    List<String> opterm = new ArrayList<>();
    public CompilationEngine(String path, String name) throws ParserConfigurationException, SAXException, IOException { 

        list.add("identifier"); list.add("integerConstant"); list.add("stringConstant");
        list.add(" true "); list.add(" false "); list.add(" null "); list.add(" this "); 

        opterm.add(" + "); opterm.add(" - "); opterm.add(" * "); 
        opterm.add(" / "); opterm.add(" & "); opterm.add(" | "); 
        opterm.add(" < "); opterm.add(" > "); opterm.add(" = ");
        try {
            File f = new File(path.replace(name, "/output/") + name.replace(".jack", "l.xml"));
            if(f.createNewFile()) System.out.println("file created");
            else System.out.println("file already created");
        } catch(IOException e) {
            System.out.println("ashgbdfnj");
        }
        try {
            File tokenisedf = new File(path.replace(name, "/output/") + name.replace(".jack", "lT.xml"));

            DocumentBuilderFactory dbfactory = DocumentBuilderFactory.newInstance();
            
            DocumentBuilder dBuilder = dbfactory.newDocumentBuilder();
            
            Document doc1 = dBuilder.parse(tokenisedf);
            Document doc2 = dBuilder.newDocument();

            doc1.getDocumentElement().normalize();

            System.out.println(doc1.getDocumentElement().getNodeName());

            nList = doc1.getDocumentElement().getChildNodes();
/*             for(int i=0; i<nList.getLength(); ++i) {
                System.out.println(nList.item(i).getNodeName() + " " + i + nList.item(i).getTextContent());
            }  */

            //System.out.println(nList.item(0).getNodeName());
            CompileClass(doc2);
            write(path, name, doc2);
            
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void write(String path, String name, Document doc) throws TransformerException {
        try (FileOutputStream output = new FileOutputStream(path.replace(name, "/output/") + name.replace(".jack", "l.xml"))) {
            writeXml(doc, output);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    private static void writeXml(Document doc, OutputStream output) throws TransformerException {

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();

        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(output);

        transformer.transform(source, result);

    }

    public Node getNextToken(String c) {
        if(i == nList.getLength()) return null;
        if(c.equals("check")) {
            return nList.item(i+2);
        }
        if(c.equals("next")) {
            i=i+2;
            return nList.item(i);
        }
        return null;
    }

    public void CompileClass(Document doc) {
        i=-1;
        System.out.println("entered compile class");
        Element rootElement = doc.createElement("class");
        doc.appendChild(rootElement);
        System.out.println(getNextToken("check").getTextContent());
        rootElement.appendChild(doc.importNode(getNextToken("next"), true));
        System.out.println(getNextToken("check").getTextContent());
        rootElement.appendChild(doc.importNode(getNextToken("next"), true));
        System.out.println(getNextToken("check").getTextContent());
        rootElement.appendChild(doc.importNode(getNextToken("next"), true));
        System.out.println(getNextToken("check").getTextContent());
        do {
            if(getNextToken("check") == null) {
                return;
            }
            else if(getNextToken("check").getTextContent().equals(" static ") || getNextToken("check").getTextContent().equals(" field ")) {
                System.out.println(getNextToken("check").getTextContent());
                CompileClassVarDec(rootElement, doc);
            }

            else if(getNextToken("check").getTextContent().equals(" function ") || getNextToken("check").getTextContent().equals(" constructor ") || getNextToken("check").getTextContent().equals(" method ") ) {
              
                CompileSubroutine(rootElement, doc);
            } 
        } while(i<(nList.getLength()-4));
        rootElement.appendChild(doc.importNode(getNextToken("next"), true));
        System.out.println("exit compile class");
    }

    public void CompileClassVarDec(Element rootElement, Document doc) {
        System.out.println("entered compile var dec");
        Element classVarDec = doc.createElement("classVarDec");
        rootElement.appendChild(classVarDec);
        Node n;
        do {
            n = getNextToken("next");
            classVarDec.appendChild(doc.importNode(n, true));
        } while(!n.getTextContent().equals(" ; "));
        System.out.println("exit class var dec");
    }

    public void CompileSubroutine(Element rootElement, Document doc) {
        System.out.println("entered compile subroutine");
        Element subroutine = doc.createElement("subroutineDec");
        rootElement.appendChild(subroutine);
        Node n;
        do {
            n = getNextToken("next");
            subroutine.appendChild(doc.importNode(n, true));
            if(n.getTextContent().equals(" ( " )) {
                CompileParameterList(subroutine, doc);
                
            }
        } while(!getNextToken("check").getTextContent().equals(" { "));
        if(getNextToken("check").getTextContent().equals(" { ")) {
            CompileSubroutineBody(subroutine, doc);
          
        }
       System.out.println("exit subroutine");
    }

    public void CompileSubroutineBody(Element subroutine, Document doc) {
        System.out.println("entered subroutine body");
        Element subroutineB = doc.createElement("subroutineBody");
        subroutine.appendChild(subroutineB);
        Node n = getNextToken("next");
        subroutineB.appendChild(doc.importNode(n, true));
        do {
            if(getNextToken("check").getTextContent().equals(" var ")) {
                CompileVarDec(subroutineB, doc);
             
            } else {
                CompileStatement(subroutineB, doc);
            }
       
        } while(!getNextToken("check").getTextContent().equals(" } "));
        subroutineB.appendChild(doc.importNode(getNextToken("next"), true));
        System.out.println("exit subroutine body");
    } 

    public void CompileVarDec(Element subroutineB, Document doc) {
        System.out.println("entered compile var dec");
        Element varDec = doc.createElement("varDec");
        subroutineB.appendChild(varDec);
        Node n;
        do {
            n = getNextToken("next");
            varDec.appendChild(doc.importNode(n, true));
           
        } while(!n.getTextContent().equals(" ; "));
        System.out.println("exit var dec");
    }

    public void CompileParameterList(Element subroutine, Document doc) {
        System.out.println("entered compile parameter list");
        Element parameterList = doc.createElement("parameterList");
        subroutine.appendChild(parameterList);
        Node n;
        if(getNextToken("check").getTextContent().equals(" ) ")) {
            parameterList.setTextContent("\n");
        } else {
            do { 
                n = getNextToken("next");
                parameterList.appendChild(doc.importNode(n, true));
            }while(!getNextToken("check").getTextContent().equals(" ) "));
        }
        System.out.println("exit parameter list");
    }

    public void CompileStatement(Element subroutineB, Document doc) {
        System.out.println("enter compile statement");
        Element statements = doc.createElement("statements");
        subroutineB.appendChild(statements);
       
        if(getNextToken("check").getTextContent().equals(" } ")) {
            statements.setTextContent("\n");
        } else {
            do {
                if(getNextToken("check").getTextContent().equals(" let ")) {
                 
                    CompileLet(statements, doc);
                }
                else if( getNextToken("check").getTextContent().equals(" do ")) {
                    CompileDo(statements, doc);
                }
                else if( getNextToken("check").getTextContent().equals(" while ")) {
                    CompileWhile(statements, doc);
                }
                else if(getNextToken("check").getTextContent().equals(" return ")) {
                    CompileReturn(statements, doc);
                }
                else if(getNextToken("check").getTextContent().equals(" if ")) {
                    CompileIf(statements, doc);
                    //System.out.println(getNextToken("check").getTextContent());
                }
            } while(!getNextToken("check").getTextContent().equals(" } "));
        }
        System.out.println("exit compile statement");
    }

    public void CompileLet(Element statements, Document doc) {
        System.out.println("enter compile let");
        Element letStatement = doc.createElement("letStatement");
        statements.appendChild(letStatement);
        Node n;
        do {
            n = getNextToken("next");
            letStatement.appendChild(doc.importNode(n, true));
        
            if(n.getTextContent().equals(" = ") || n.getTextContent().equals(" [ ")) {
                CompileExpression(letStatement, doc, n.getTextContent());
            }
            
        } while(!n.getTextContent().equals(" ; "));
        System.out.println("exit compile let");

    }

    public void CompileDo(Element statements, Document doc) {
        System.out.println("enter compile do");
        Element doStatement = doc.createElement("doStatement");
        statements.appendChild(doStatement);
        Node n;
        do {
            n = getNextToken("next");
            doStatement.appendChild(doc.importNode(n, true));
            if(n.getTextContent().equals(" ( ")) {
                CompileExpressionList(doStatement, doc);
            }
        } while(!n.getTextContent().equals(" ; "));

        System.err.println("exit do");
    }

    public void CompileWhile(Element statements, Document doc) {
        System.out.println("enter compile while");
        Element whileStatement = doc.createElement("whileStatement");
        statements.appendChild(whileStatement);
        Node n;
        do {
            n = getNextToken("next");
            whileStatement.appendChild(doc.importNode(n, true));
            
            if(n.getTextContent().equals(" ( ")) {
                CompileExpression(whileStatement, doc, n.getTextContent());
            }
        } while(!n.getTextContent().equals(" ) "));
        int flag = 0;
        if(getNextToken("check").getTextContent().equals(" { ")) {
            whileStatement.appendChild(doc.importNode(getNextToken("next"), true));
            flag = 1;
        }
        CompileStatement(whileStatement, doc);

        if(flag == 1) {
            whileStatement.appendChild(doc.importNode(getNextToken("next"), true));
        }

        System.out.println("exit compile while");
    }

    public void CompileReturn(Element statements, Document doc) {
        System.out.println("enter compile return");
        Element returnStatement = doc.createElement("returnStatement");
        statements.appendChild(returnStatement);
        Node n;
        do {
            n = getNextToken("next");
            returnStatement.appendChild(doc.importNode(n, true));
            System.out.println(n.getTextContent());
            if(list.contains(getNextToken("check").getNodeName())) {
                CompileExpression(returnStatement, doc, null);
            }

        } while(!n.getTextContent().equals(" ; "));
        System.out.println("exit compile return");
    }

    public void CompileIf(Element statements, Document doc) {
        System.out.println("enter compile if");
        Element ifStatement = doc.createElement("ifStatement");
        statements.appendChild(ifStatement);
        Node n;
        do {
            n = getNextToken("next");
            ifStatement.appendChild(doc.importNode(n, true));
            if(n.getTextContent().equals(" ( ")) {
                CompileExpression(ifStatement, doc, n.getTextContent());
            }
        } while(!n.getTextContent().equals(" ) "));
        int flag = 0;
        if(getNextToken("check").getTextContent().equals(" { ")) {
            System.out.println(getNextToken("check").getTextContent());
            ifStatement.appendChild(doc.importNode(getNextToken("next"), true));
            flag = 1;
        }
        CompileStatement(ifStatement, doc);
        if(flag == 1) {
            n = getNextToken("next");
            ifStatement.appendChild(doc.importNode(n, true));
        }
        n = getNextToken("check");
        if(n.getTextContent().equals(" else ")) {
            ifStatement.appendChild(doc.importNode(getNextToken("next"), true));
            if(getNextToken("check").getTextContent().equals(" { ")) {
                ifStatement.appendChild(doc.importNode(getNextToken("next"), true));
            }

            CompileStatement(ifStatement, doc);
            ifStatement.appendChild(doc.importNode(getNextToken("next"), true));
        }
        System.out.println("exit compile if");
    }

    public void CompileExpression(Element exp, Document doc, String s) {
        System.out.println("enter compile expression");
        List<String> list = new ArrayList<>();

        list.add("identifier"); list.add("integerConstant"); list.add("stringConstant");
        list.add(" true "); list.add(" false "); list.add(" null "); list.add(" this "); 

        Element expression = doc.createElement("expression");
        exp.appendChild(expression);
        Node n;
        do {
            n = getNextToken("check");
            String p = n.getNodeName();
            
            if(list.contains(p) || list.contains(n.getTextContent())) {
                CompileTerm(expression, doc, " ");
            } else if(n.getTextContent().equals(" ( ")){
                CompileTerm(expression, doc, "term");
            } else if(!expression.hasChildNodes() && n.getTextContent().equals(" - ") || n.getTextContent().equals(" ~ ")) {
                CompileTerm(expression, doc, "op");
            } else {   
                expression.appendChild(doc.importNode(getNextToken("next"), true));
            }
        } while(!getNextToken("check").getTextContent().equals(" ) ")
                && !getNextToken("check").getTextContent().equals(" ] ")
                && !getNextToken("check").getTextContent().equals(" ; ")
                && !getNextToken("check").getTextContent().equals(" , "));
        System.out.println("exit compile expressiom");
    }

    public void CompileTerm(Element expression, Document doc, String s) {

        System.out.println("entered compile term");
        Element term = doc.createElement("term");
        expression.appendChild(term);
        Node n;
        do {
            n = getNextToken("next");
            term.appendChild(doc.importNode(n, true));
            if(n.getTextContent().equals(" ( ") && !s.equals("term")) {
               CompileExpressionList(term, doc);
            }
            else if(n.getTextContent().equals(" [ ") || s.equals("term")) {
                CompileExpression(term, doc, null);
                term.appendChild(doc.importNode(getNextToken("next"), true));
            } else if(s.equals("op")) {
                CompileTerm(term, doc, " ");
            }
           
        } while(!opterm.contains(getNextToken("check").getTextContent())
                && !getNextToken("check").getTextContent().equals(" ; ")
                && !getNextToken("check").getTextContent().equals(" ) ")
                && !getNextToken("check").getTextContent().equals(" ] ")
                && !getNextToken("check").getTextContent().equals(" , "));
        System.out.println("exit compile term");
    } 

    public void CompileExpressionList (Element term, Document doc) {
        System.out.println("entered compile list");
        Element explist = doc.createElement("expressionList");
        term.appendChild(explist);
        //explist.setTextContent("absdjn");
        if(getNextToken("check").getTextContent().equals(" ) ")) {
            explist.setTextContent("\n");
        } else {
            do {
                CompileExpression(explist, doc, null);
                if(getNextToken("check").getTextContent().equals(" , ")) {
                    explist.appendChild(doc.importNode(getNextToken("next"), true));
                }
            } while(!getNextToken("check").getTextContent().equals(" ) ")); 

            System.out.println("awersdtfcgvhjbknl,gjmvfcf");
            
        }
        term.appendChild(doc.importNode(getNextToken("next"), true));
        System.out.println("exit compile list");
    }
}
