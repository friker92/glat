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
		GlatProgram p = g.parse(new String[] { basePath + "/examples/example3" });
		Properties prop = new Properties();
		prop.put(SeqAnalysis.NameProp.DOMAIN, IntervalsAbstDomain.class);
		// prop.put(SeqFixpoint.NameProp.DOMAIN, SignAbstDomain.class);
		// prop.put(SeqFixpoint.NameProp.DOMAIN, ConstPropDomain.class);

		SeqAnalysis sq = new SeqAnalysis(prop);
		// sq.analyze(p, new SignAbstDomain());
		// sq.analyze(p, new ConstPropDomain());
		sq.start(p);// , new IntervalsAbstDomain());
		System.out.println("Analysis Result: \n" + prettyprint(sq.getResult()));

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
