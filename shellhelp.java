import Indexing.MyIndexReader;
import Search.QueryRetrievalModel;
import Classes.Query;

public class shellhelp{
    private static Query makeQuery(String args[]){
        for (String s: args){
            System.out.println(s);
        }
        return new Query();
    }
    public static void main(String args[]) throws Exception{
        MyIndexReader ixreader = new MyIndexReader();
        QueryRetrievalModel model = new QueryRetrievalModel(ixreader);
        Query q = makeQuery(args);
    }
}
