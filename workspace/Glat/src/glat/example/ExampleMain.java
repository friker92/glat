package glat.example;

import java.io.FileNotFoundException;

import glat.Glat;
import glat.ParseException;
import glat.program.Program;

public class ExampleMain {
	public static void main(String[] args) {
		Glat g = new Glat();

		try {
			Program p = g.parse(args);
			Analysis a = new ExampleAnalysis(p);
			Analysis b = new PositiveNegativeAnalysis(p);
			if(a.run()){
				System.out.println("Analysis A:");
				a.print();
			}else
				System.err.println("Analysis A have a run error");
			System.out.println("\n\n\n*******************************************\n\n\n");
			if(b.run()){
				System.out.println("Analysis B:");
				b.print();
			}else
				System.err.println("Analysis B have a run error");
		} catch (FileNotFoundException e) {
			System.out.println("FILE NOT FOUND!!!");
			e.printStackTrace();
		} catch (ParseException e) {
			System.out.println("You have NOT written in my language");
			e.printStackTrace();
		}
	}
}
