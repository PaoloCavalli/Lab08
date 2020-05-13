package it.polito.tdp.extflightdelays.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.extflightdelays.db.ExtFlightDelaysDAO;

public class Model {

	private SimpleWeightedGraph<Airport, DefaultWeightedEdge> grafo;
	private Map<Integer, Airport> idMap;
	private ExtFlightDelaysDAO dao ;
	
	public Model() {
		idMap = new HashMap<Integer, Airport>();
		this.dao = new  ExtFlightDelaysDAO();
		this.dao.loadAllAirports(idMap);
	}
	public void creaGrafo(int x ) {
		this.grafo= new SimpleWeightedGraph<Airport, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		//Aggiungo tutti i vertici perchè non ho nessun controllo sui vertici
	     Graphs.addAllVertices(this.grafo, idMap.values());
	     
	     //Dobbiamo lavorare sugli archi
	     for(Rotta r : dao.getRotte(idMap, x)) {
	    	 
	    	 DefaultWeightedEdge e = this.grafo.getEdge(r.getA1(), r.getA1());
	    	 
	    	 if(e == null) {
	    		 Graphs.addEdge(this.grafo, r.getA1(), r.getA2(), r.getPeso());
	    	 }else {
	    		 // se ho già quest'arco ne aggiorno il peso!
	    		 double peso = grafo.getEdgeWeight(e);
	    		 double newPeso = (peso + r.getPeso())/2;
	    		 grafo.setEdgeWeight(e, newPeso);
	    	 }
	     }
	}
	
	public int nVertici() {
		return this.grafo.vertexSet().size();
	}
	
	public int nArchi() {
		return this.grafo.edgeSet().size();
	}
	
	public List<Rotta > getRotte(){
		
		List <Rotta> rotte = new ArrayList<>();
		
		for(DefaultWeightedEdge e : this.grafo.edgeSet()) {
		  Rotta r = new Rotta(this.grafo.getEdgeSource(e),this.grafo.getEdgeTarget(e),this.grafo.getEdgeWeight(e));
		  rotte.add(r);
		}
		return rotte;
	}
}
