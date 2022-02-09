import java.io.*;
import java.lang.*;
import java.util.*;

public class familyTree{
    int count = 0;
    boolean checkInput = true;
    String rootOfTree;
    private class Node{
        public String name;
        public String parent;
        public int gen;
        public Node(String mem, String par){
            name = mem;
            parent = par;
        }
        public Node(String mem, String par, int gen){
            name = mem;
            parent = par;
            this.gen = gen;
        }
    }

    private class Generation{
        public ArrayList<Node> nodes;


        public Generation(String name, String parent){
            nodes =  new ArrayList<Node>();
            nodes.add(0, new Node(name, parent));
        }

        public Generation(ArrayList<Node> children){
            nodes = new ArrayList<Node>();
            nodes.addAll(children);
        }

        public boolean areChildren(ArrayList<Node> parent){
            for(Node child : nodes){
                for(Node par : parent) {
                    if(child.parent.equals(par.name)){
                        return true;
                    }
                }
            }
            return false;
        }
    }

    private class Queue{
        private ArrayList<Node> queue;
        private ArrayList<String> parents;

        public Queue(){
            queue = new ArrayList<Node>();
            parents = new ArrayList<String>();
        }

        public void enqueue(String name, String parent){
            queue.add(new Node(name, parent));
            parents.add(new String(parent));
        }

        public boolean containsParent(String parent){
            if(parents.contains(parent)) return true;
            else return false;
        }

        public ArrayList<Node> dequeue(String parent){
            ArrayList<Node> children = new ArrayList<Node>();
            for(Node node : this.queue){
                if(node.parent.equals(parent)){
                    children.add(node);
                }
            }
            parents.remove(parent);
            return children;
        }

        public boolean empty(){
            if(queue.size() > 0) return false;
            else return true;
        }

    }

    private ArrayList<Generation> genTree;
    private Queue queue;

    public familyTree() {
        genTree = new ArrayList<Generation>();
        queue = new Queue();
    }

    private void addChildren(int i, String parent){ //adds children nodes whose parent matches the input string
        if(genTree.size() > i+1 && genTree.get(i+1).areChildren(genTree.get(i).nodes)) {
            genTree.get(i+1).nodes.addAll(queue.dequeue(parent));
        } else if (genTree.size() > i+1 && !genTree.get(i+1).areChildren(genTree.get(i).nodes)){
            Generation gen = new Generation(queue.dequeue(parent));
            genTree.add(i+1, gen);
        } else {
            Generation gen = new Generation(queue.dequeue(parent));
            genTree.add(gen);
        }
    }

    private BufferedReader br;

    public void checkInput(String filename){//check if data is consistent or not. If consistant, return true, if not consistant, return false
        String line;
        ArrayList<String> inputNames = new ArrayList<String>();
        ArrayList<String> inputParents = new ArrayList<String>();

        try{
            br = new BufferedReader(new FileReader(filename));
            br.readLine();
            while((line = br.readLine()) != null){
                String[] names = line.split(" ");
                inputNames.add(names[0]);
                inputParents.add(names[1]);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //System.out.print(inputNames);
        //System.out.println();
        //System.out.print(inputParents);
        //System.out.println();

        for (int i = 0; i < inputNames.size(); i++) {
            String nameCheck = inputNames.get(i);
            for (int j = i+1; j < inputNames.size(); j++) {
                if(nameCheck.equals(inputNames.get(j))){
                    System.out.println("data is not consistent");
                    checkInput = false;
                }

            }
            
        }


    }

    public void findRoot(String filename){//find a root of the familyTree
        String line;
        ArrayList<String> inputNames = new ArrayList<String>();
        ArrayList<String> inputParents = new ArrayList<String>();

        try{
            br = new BufferedReader(new FileReader(filename));
            br.readLine();
            while((line = br.readLine()) != null){
                String[] names = line.split(" ");
                inputNames.add(names[0]);
                inputParents.add(names[1]);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(!inputParents.contains("NULL")){
            for (int i = 0; i < inputParents.size(); i++) {
                String parentName = inputParents.get(i);
                if(inputNames.contains(parentName)){
                    continue;
                }
                else{
                    rootOfTree = parentName;
                    break;
                }
            }
            System.out.println("Root of tree is: " + rootOfTree);


            try{
                FileWriter fw = new FileWriter((filename), true);
                BufferedWriter bw = new BufferedWriter(fw);
                bw.newLine();
                bw.write(rootOfTree + " " + "NULL");
                bw.close();
                fw.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void fillTree(String filename){//create family tree
        String line;


        try{
            br = new BufferedReader(new FileReader(filename));
            br.readLine();
            while((line = br.readLine()) != null){
                String[] names = line.split(" ");

                boolean found = false;
                if(names[1].equals("NULL")){
                    Generation gen = new Generation(names[0], names[1]);
                    genTree.add(0, gen);
                    found = true;
                    //System.out.println("0");
                }

                queue.enqueue(names[0], names[1]);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        for(int i = 0; i < genTree.size() && !queue.empty(); i++){ //System.out.println("Loop1");
//            System.out.println("i=" +i);
//            for(Node node3: genTree.get(i).nodes){
//                System.out.println ("" + node3.name );
//            }

            for(Node node : genTree.get(i).nodes){ //System.out.println("Loop2");
                if(queue.containsParent(node.name)){
                    //System.out.println();
                    this.addChildren(i, node.name);

                }
            }
        }
    }


    public void searchName(){//input a name and return its generation
        int count = 0;
        boolean found = false;
        System.out.println("inputs a name of a person");
        Scanner scanner = new Scanner(System.in);
        try {
            String input = scanner.nextLine();
            for(int i = 0; i < genTree.size(); i++){
                for(Node node : genTree.get(i).nodes){
                    if(input.equals(node.name)){
                        found = true;
                        System.out.println(i+1 + "th generation");
                        System.out.println("number of Descendants: " + countDescendants(node.name, i+1) );
                        break;
                    }
                }
            }
            if(found == false){
                System.out.println("No person was found, please type the correct name");
                searchName();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int countDescendants(String searchedName, int gen) { //return the number of descendants of the given person

        ArrayList<Node> cache = new ArrayList<Node>();
        int j=0;
        if (gen == genTree.size()) {
            return count;
        }
        else {
            for (int i = (gen); i <= genTree.size() ; i++){
                //System.out.println("for1");
                for (Node node : genTree.get(i-1).nodes) {
                    if (node.parent.equals(searchedName)) {
                        count++;
                        Node found = new Node(node.name, node.parent, gen+1);
                        cache.add(found);
                    }
                }

            }
        }
        for (int i = 0; i < cache.size(); i++) {
            countDescendants(cache.get(i).name, cache.get(i).gen);
        }
        return count;
    }

    public void getInput(){
        ArrayList<String> name = new ArrayList<String>();
        name.add("");
        Scanner add = new Scanner(System.in);
        try {
            String input = add.nextLine();
            while(input != "***"){
                name.add(input);
            }
            for (int i = 0; i < name.size(); i++) {
                name.get(i);
            }
        }

        catch (Exception e) {
            e.printStackTrace();
        }
    }
    /*try {
        String line = input.nextLine();
        while((line != "***")){
            String[] names = line.split(" ");

            boolean found = false;
            if(names[1].equals("NULL")){
                Generation gen = new Generation(names[0], names[1]);
                genTree.add(0, gen);
                found = true;
                //System.out.println("0");
            }

            queue.enqueue(names[0], names[1]);
        }
    }*/

    public static void main (String[] args){
        familyTree tree = new familyTree();
        String filename = "D:\\Study\\Toshiba_OJT\\MiniProjectDSA\\src\\main\\java\\readThis.txt";
        tree.checkInput(filename);
        tree.findRoot(filename);
        if(tree.checkInput){
            tree.fillTree(filename);
            tree.searchName();
        }
    }
}