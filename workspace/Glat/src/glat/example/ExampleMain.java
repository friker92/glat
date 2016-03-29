package glat.example;

import java.io.FileNotFoundException;
import java.util.Vector;

import glat.Glat;
import glat.ParseException;
import glat.example.direct.ExampleAnalysis;
import glat.example.direct.PositiveNegativeAnalysis;
import glat.example.fixpoint.PosNegFixPoint;
import glat.program.Program;

public class ExampleMain {
	public static void main(String[] args) {
		Glat g = new Glat();

		try {
			Program p = g.parse(args);
			Vector <Analysis> va = new Vector<Analysis>();
			va.add(new ExampleAnalysis(p));
			va.add(new PosNegFixPoint(p));		
			//va.add(new PositiveNegativeAnalysis(p));
			for (Analysis a : va){
				System.out.println("\n\n\n*******************************************\n\n\n");
				a.title();
				if(a.run())
					a.print();
				else
					System.out.println("FAIL");
			}
		} catch (FileNotFoundException e) {
			System.out.println("FILE NOT FOUND!!!");
			e.printStackTrace();
		} catch (ParseException e) {
			System.out.println("You have NOT written in my language");
			e.printStackTrace();
		}
	}
}
