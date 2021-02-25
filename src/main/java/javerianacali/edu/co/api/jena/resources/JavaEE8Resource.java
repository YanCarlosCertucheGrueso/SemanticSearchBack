package javerianacali.edu.co.api.jena.resources;

import javerianacali.edu.co.api.ontology.Ontology;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;

/**
 *
 * @author jeank
 */
@Path("javaee8")
public class JavaEE8Resource {
    
    //  String with a query in sparql formart t
        static String PREFIX =   "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
                                    "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                                    "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                                    "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n";
        
        static String queryTest2 =   "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
                                    "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                                    "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                                    "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" + 
                                    "SELECT distinct ?x ( STR(?lab) as ?label ) WHERE {?x rdfs:label ?lab " +
                                    "} ORDER BY ?label";
        static String q ="";
 
    private final ExecutorService executorService = java.util.concurrent.Executors.newCachedThreadPool();
    
    @GET
    @Path("response/search/q={q}")
    @Produces("application/json")
    public void res(@Suspended
    final AsyncResponse asyncResponse, @PathParam("q") String query){
            Future<?> submit = executorService.submit(() -> {
                asyncResponse.resume(doRes(query));
            });
    }

    private Response doRes(String query) {
        System.out.println(query);
        String qfinal = PREFIX + "SELECT  ?x (STR(?lab) as ?label) " +
                                    "where {" +
                                    " ?x rdfs:label ?lab." +
                                    " FILTER regex(?lab, \""+(query)+"\", \"i\")" +
                                    "}" ;
        System.out.println(qfinal);
        String a = Ontology.GetResultAsString(qfinal);
        if("nada".equals(a)){
            a = "{results : { bindings:[{\"nada\"}]}}";
        }
        return Response
               .ok("ok")
               .header("Access-Control-Allow-Origin", "*")
               .entity(a)
               .build();
    }  
} 