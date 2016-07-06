package mtanalysis.fixpoint;

import java.util.Map;
import java.util.Properties;
import java.util.Vector;

import glat.parser.Glat;
import glat.program.GlatProgram;
import glat.program.Node;
import mtanalysis.domains.AbstractState;
import mtanalysis.domains.intervals.IntervalsAbstDomain;
import mtanalysis.stores.Store;
import mtanalysis.strategies.SimpleStrategy;

public class ProgTest {

	private static String basePath = System.getProperty("user.home") + "/Systems/glat/workspace/Glat";

	public static void main(String[] args) throws Exception {
		Glat g = new Glat();
		GlatProgram p = g.parse(new String[] { basePath + "/examples/example5" });
		Properties prop = new Properties();
		prop.put(SeqAnalysis.NameProp.DOMAIN, IntervalsAbstDomain.class);
		// prop.put(SeqFixpoint.NameProp.DOMAIN, SignAbstDomain.class);
		// prop.put(SeqFixpoint.NameProp.DOMAIN, ConstPropDomain.class);
		//launchSeqAnalysis(prop,p);
		launchThAnalysis(prop,p);
	}
	
	public static void launchSeqAnalysis(Properties prop, GlatProgram p){
		SeqAnalysis sq;
		try {
			sq = new SeqAnalysis(prop);	
			sq.start(p);
			System.out.println("Analysis Result: \n" + prettyprint(sq.getResult()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void launchThAnalysis(Properties prop, GlatProgram p){
		ThSimpleAnalysis ths;
		try {
			ths = new ThSimpleAnalysis(prop);	
			ths.start(p);
			ths.getResult();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String prettyprint(Map<Object, Object> result) {
		String str = "";
		for (Object k : result.keySet()) {
			str += k + " \n" + prettyprint((Store<Node, AbstractState>) result.get(k));
		}
		result.forEach((k, v) -> System.out.println());
		return str;
	}

	public static String prettyprint(Store<Node, AbstractState> v) {
		return v.toString();
	}
	
}
