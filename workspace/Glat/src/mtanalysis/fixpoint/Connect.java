package mtanalysis.fixpoint;

import java.util.List;

import org.jgrapht.alg.KosarajuStrongConnectivityInspector;
import org.jgrapht.alg.cycle.JohnsonSimpleCycles;
import org.jgrapht.alg.cycle.SzwarcfiterLauerSimpleCycles;
import org.jgrapht.alg.cycle.TarjanSimpleCycles;
import org.jgrapht.alg.cycle.TiernanSimpleCycles;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DirectedSubgraph;

import glat.program.ControlFlowGraph;
import glat.program.GlatNode;
import glat.program.GlatTransition;

public class Connect {
	public static void askConnectivity2(int N,DefaultDirectedGraph<GlatNode, GlatTransition> defaultDirectedGraph) {
		// computes all the strongly connected components of the directed graph
		KosarajuStrongConnectivityInspector sci =
           new KosarajuStrongConnectivityInspector(defaultDirectedGraph);
       List<DirectedSubgraph> stronglyConnectedSubgraphs = sci.stronglyConnectedSubgraphs();
       // prints the strongly connected components
       DefaultDirectedGraph<GlatNode, GlatTransition> gh;

       if(stronglyConnectedSubgraphs.size()>0){
    	   for(int i = 0; i< N; i++)
    		   System.out.print("\t");
    	   System.out.println("Strongly connected components:");
       }
       for (int i2 = 0; i2 < stronglyConnectedSubgraphs.size(); i2++) {
    	   for(int i = 0; i< N; i++)
    		   System.out.print("\t");
           System.out.println(stronglyConnectedSubgraphs.get(i2));
          // System.out.println((stronglyConnectedSubgraphs.get(i2)).getClass());
           gh = new DefaultDirectedGraph<GlatNode, GlatTransition>(GlatTransition.class);
           boolean first = true;
		for ( Object t : stronglyConnectedSubgraphs.get(i2).edgeSet()){
			if(first){first=false;continue;}
			GlatTransition tr = (GlatTransition)t;
			GlatNode src = (GlatNode)tr.getSrcNode();
			GlatNode trg = (GlatNode)tr.getTargetNode();
			gh.addVertex(src);
			gh.addVertex(trg);
			gh.addEdge(src,trg , tr);
           }
           
          // System.out.println(gh);
           askConnectivity2(N+1,gh);
       }
       System.out.println();
       
       

	}
	
	public static void askConnectivity(ControlFlowGraph cfg) {
		askConnectivity2(0,cfg.getGraph());

        // Cycle
        TarjanSimpleCycles tsc = new TarjanSimpleCycles<>(cfg.getGraph());
        
        List tscc = tsc.findSimpleCycles();
        
        System.out.println("Tarjan Simple Cycles:");
        for (int i2 = 0; i2 < tscc.size(); i2++) {
            System.out.println(tscc.get(i2));
        }
        System.out.println();
        
        
        SzwarcfiterLauerSimpleCycles SZsc = new SzwarcfiterLauerSimpleCycles<>(cfg.getGraph());
        
        List SZscc = SZsc.findSimpleCycles();
        
        System.out.println("Szwarcfiter Simple Cycles:");
        for (int i2 = 0; i2 < SZscc.size(); i2++) {
            System.out.println(SZscc.get(i2));
        }
        System.out.println();
        
        TiernanSimpleCycles ttsc = new TiernanSimpleCycles<>(cfg.getGraph());
        
        List ttscc = ttsc.findSimpleCycles();
        
        System.out.println("Tiernan Simple Cycles:");
        for (int i2 = 0; i2 < ttscc.size(); i2++) {
            System.out.println(ttscc.get(i2));
        }
        System.out.println();
        
        JohnsonSimpleCycles jsc = new JohnsonSimpleCycles<>(cfg.getGraph());
        
        List jscc = jsc.findSimpleCycles();
        
        System.out.println("Johnson Simple Cycles:");
        for (int i2 = 0; i2 < jscc.size(); i2++) {
            System.out.println(jscc.get(i2));
        }
        System.out.println();
		
	}	
}
