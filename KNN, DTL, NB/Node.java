import java.util.HashMap;
import java.util.Map;

public class Node {
private String attrName; //attribute Name
private Map<String,Node> maps;
private Node father;

public Node(){
    maps=new HashMap<String,Node>();
}

public Node(String attrName1,Map<String,Node> maps1,Node father1){
    maps=maps1;
    attrName=attrName1;
    father=father1;
}


public Node(String attrName, String classification, Attribute att, Node father) {
    super();
    this.attrName = attrName;
    this.father = father;
}

public void addToMap(String str, Node n){
    maps.put(str, n);
}


/**
 * @return the attrName
 */
public String getAttrName() {
    return attrName;
}


/**
 * @return the father
 */
public Node getFather() {
    return father;
}
/**
 * @param attrName the attrName to set
 */
public void setAttrName(String attrName) {
    this.attrName = attrName;
}

/**
 * @param father the father to set
 */
public void setFather(Node father) {
    this.father = father;
}

/**
 * @return the maps
 */
public Map<String, Node> getMaps() {
    return maps;
}

/**
 * @param maps the maps to set
 */
public void setMaps(Map<String, Node> maps) {
    this.maps = maps;
}

}
