package mtanalysis.fixpoint;

import java.lang.reflect.Array;
import java.util.Map;
import java.util.Properties;
import glat.parser.Glat;
import glat.program.GlatProgram;
import glat.program.instructions.Call;
import mtanalysis.domains.intervals.IntervalsAbstDomain;
import mtanalysis.stores.SimpleStore;
import mtanalysis.stores.Store;
import mtanalysis.strategies.SimpleStrategy;

public class ProgTest {

	private static String basePath = System.getProperty("user.home") + "/Systems/glat/workspace/Glat";

	public static void main(String[] args) throws Exception {
		Glat g = new Glat();
		GlatProgram p = g.parse(new String[] { basePath + "/examples/example3" });
		Properties prop = new Properties();
		prop.put(SeqAnalysis.NameProp.STORE, SimpleStore.class);
		prop.put(SeqAnalysis.NameProp.STRATEGY, SimpleStrategy.class);
		prop.put(SeqAnalysis.NameProp.DOMAIN, IntervalsAbstDomain.class);
		// prop.put(SeqFixpoint.NameProp.DOMAIN, SignAbstDomain.class);
		// prop.put(SeqFixpoint.NameProp.DOMAIN, ConstPropDomain.class);

		SeqAnalysis sq = new SeqAnalysis(prop);
		// sq.analyze(p, new SignAbstDomain());
		// sq.analyze(p, new ConstPropDomain());
		sq.start(p);// , new IntervalsAbstDomain());
		System.out.println("Analysis Result: \n" + prettyprint(sq.getResult()));

	}

	private static String prettyprint(Map<Object, Object> result) {
		String str = "";
		for (Object k : result.keySet()) {
			str += k + " \n" + prettyprint((Store) result.get(k));
		}
		result.forEach((k, v) -> System.out.println());
		return str;
	}

	private static String prettyprint(Store v) {
		return v.toString();
	}
}
