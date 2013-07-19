import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class nb_classifier {
	public static void main(String[] args) {
		if(args.length<3){
			System.out.println("Sorry, you must type in 3 parameters to run this program. ");
			System.out.println("You should add a third parameter as F to do Vocabulary modification");
			
		}
		else{
		String para1 = args[0];
		String para2 = args[1];
		int m=Integer.parseInt(args[2]);
		BufferedReaderExample1 b = new BufferedReaderExample1(args[0], args[1],m);}
		// b.get20words(b.mytableC,
		// b.Con_text,b.Con_fullTest.size(),b.Vocabulary.size());
		// System.out.println("aaa");
		// b.get20words(b.mytableL,
		// b.Lib_text,b.Lib_fullTest.size(),b.Vocabulary.size());
		// b.get20wordration(b.Vocabulary, b.mytableC, b.mytableL,
		// b.Con_fullTest.size(), b.Lib_fullTest.size());
	}
		// b.get20words(b.mytableC,
		// b.Con_text,b.Con_fullTest.size(),b.Vocabulary.size());
		// System.out.println("aaa");
		// b.get20words(b.mytableL,
		// b.Lib_text,b.Lib_fullTest.size(),b.Vocabulary.size());
		// b.get20wordration(b.Vocabulary, b.mytableC, b.mytableL,
		// b.Con_fullTest.size(), b.Lib_fullTest.size());
	

}

class BufferedReaderExample1 {
	int Con_number = 0;
	int Lib_number = 0;
	ArrayList<String> Con_fullTest = new ArrayList<String>();
	ArrayList<String> Lib_fullTest = new ArrayList<String>();
	Set<String> Con_text = new HashSet<String>();
	Set<String> Lib_text = new HashSet<String>();
	Set<String> Vocabulary = new HashSet<String>();
	Hashtable<String, Integer> mytableC = new Hashtable<String, Integer>();
	Hashtable<String, Integer> mytableL = new Hashtable<String, Integer>();
	Hashtable<String,Integer> Vtable=new Hashtable<String,Integer>();
	double Con_prior = 0;
	double Lib_prior = 0;
	public BufferedReader br = null;
	Set<String> Testfiles = new HashSet<String>();

	public void get20wordration(Set<String> Vocabulary,
			Hashtable<String, Integer> Contable,
			Hashtable<String, Integer> Libtable, int C, int L) {
		double pC = 0;
		double pL = 0;
		int V = Vocabulary.size();
		int Csize = C;
		int Lsize = L;
		Hashtable<Double, String> mytable = new Hashtable<Double, String>();

		String s = null;
		for (Iterator it = Vocabulary.iterator(); it.hasNext();) {
			int frequency_c = 0;
			int frequency_l = 0;
			double first = 0;
			double second = 0;
			s = it.next().toString();
			if (Contable.containsKey(s)) {
				frequency_c = Contable.get(s);
			}
			if (Libtable.containsKey(s)) {
				frequency_l = Libtable.get(s);
			}
			pC = ((double) (frequency_c + 1)) / ((double) (V + Csize));
			pL = ((double) (frequency_l + 1)) / ((double) (V + Lsize));
			first = Math.log(pC) - Math.log(pL);
			second = Math.log(pL) - Math.log(pC);
			if (second > 2.5) {
				mytable.put(second, s);
			}
		}
		Map.Entry[] set = getSortedHashtable(mytable);
		for (int i = set.length - 1; i > 0; i--) {

			System.out.println(set[i].getValue().toString() + "  "
					+ set[i].getKey().toString());

		}
	}

	public void get20words(Hashtable<String, Integer> table,
			Set<String> Con_text, int n, int v) {
		Hashtable<Integer, String> h = new Hashtable<Integer, String>();
		for (Iterator it = Con_text.iterator(); it.hasNext();) {
			String s = it.next().toString();
			int a = table.get(s);
			if (a > 500) {

				h.put(a, s);
				// System.out.println(s+"   "+a);
			}
		}
		Map.Entry[] set = getSortedHashtable(h);
		for (int i = set.length - 1; i > 0; i--) {

			double k = (double) ((double) (Integer.parseInt(set[i].getKey()
					.toString()) + 1) / (double) (n + v));

			System.out.println(set[i].getValue().toString() + "  "
					+ set[i].getKey().toString() + "  " + k);

		}

	}

	public static Map.Entry[] getSortedHashtable(Hashtable h) {

		Set set = h.entrySet();

		Map.Entry[] entries = (Map.Entry[]) set.toArray(new Map.Entry[set
				.size()]);

		Arrays.sort(entries, new Comparator() {

			public int compare(Object arg0, Object arg1) {
				Object key1 = ((Map.Entry) arg0).getKey();
				Object key2 = ((Map.Entry) arg1).getKey();
				return ((Comparable) key1).compareTo(key2);
			}

		});
		return entries;
	}

	public BufferedReaderExample1(String para1, String para2, int m) {
		String traindata = para1;
		String testdata = para2;
		{

			try {

				String sCurrentLine;
				String modifiedstring;

				br = new BufferedReader(new FileReader("split/" + traindata));

				while ((sCurrentLine = br.readLine()) != null) {
					sCurrentLine=sCurrentLine.toLowerCase();
					if (sCurrentLine.contains("con")) {
						Con_number++;
						modifiedstring = "data/" + sCurrentLine;
						readFile(modifiedstring, Con_text, Con_fullTest,
								mytableC);
					} else {
						Lib_number++;
						modifiedstring = "data/" + sCurrentLine;
						readFile(modifiedstring, Lib_text, Lib_fullTest,
								mytableL);
					}
				}
				Con_prior = new Double(
						((double) Con_number / (double) (Con_number + Lib_number)));
				Lib_prior = new Double(
						((double) Lib_number / (double) (Con_number + Lib_number)));
				// System.out.println(mytableC.size());
				// System.out.println(mytableL.size());
				

			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (br != null)
						br.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		
		
			
			Hashtable<String,Integer> topTbale =new Hashtable<String,Integer>();
			for (Iterator it = Vocabulary.iterator(); it.hasNext();) {
				String s = it.next().toString();
				int a = Vtable.get(s);
				if (a > 50) {

					topTbale.put(s, a);
					 //System.out.println(s+"   "+a);
				}
			}
			Map.Entry[] sortedtopV = getSortedHashtable2(topTbale);
		
			//modifyingV(Vocabulary,n,sortedtopV );
			//modifyingV(Vocabulary,50,sortedtopV );
			//modifyingV(Vocabulary,100,sortedtopV );
			//modifyingV(Vocabulary,30,sortedtopV );
			//modifyingV(Vocabulary,100,sortedtopV );
		
			// System.out.println(Con_text.size());
			// System.out.println(Lib_text.size());
			// System.out.println(Vocabulary.size());
			readTestingdata("split/" + testdata, Testfiles);
			int V = Vocabulary.size();
			int C = Con_fullTest.size();
			int L = Lib_fullTest.size();

			try {
				// Create file
				FileWriter fstream = new FileWriter("result_file_" + testdata);
				BufferedWriter out = new BufferedWriter(fstream);
				for (Iterator<String> it = Testfiles.iterator(); it.hasNext();) {
					String name = it.next().toString();
					String modifiedstring = "data/" + name;
					int label = readfileandcalculation(modifiedstring,
							Con_prior, Lib_prior, V, C, L,m);
					String mylabel = null;
					if (label == 0) {
						mylabel = "L";

					} else
						mylabel = "C";
					out.write(name + " " + mylabel + "\n");
					System.out.println(name + " " + mylabel);
				}
				// Close the output stream
				out.close();
			} catch (Exception e) {// Catch exception if any
				System.err.println("Error: " + e.getMessage());
			}

		}

	}
	 public void modifyingV(Set<String> Vocabulary,int threshold,Map.Entry[] sortedtopV ){
		 int k=sortedtopV.length-1;
		 for (int i=0;i<threshold;i++){
			 //System.out.println(sortedtopV[k-i].getKey().toString()+" "+sortedtopV[k-i].getValue().toString());
			 Vocabulary.remove(sortedtopV[k-i].getKey().toString());
			 
		 }
	 }
	
	
	 public static Map.Entry[] getSortedHashtable2(Hashtable h) {
		  Set set = h.entrySet();
		  Map.Entry[] entries = (Map.Entry[]) set.toArray(new Map.Entry[set
		    .size()]);

		  Arrays.sort(entries, new Comparator() {
		   public int compare(Object arg0, Object arg1) {
		    int key1 = Integer.parseInt(((Map.Entry) arg0).getValue()
		      .toString());
		    int key2 = Integer.parseInt(((Map.Entry) arg1).getValue()
		      .toString());
		    return ((Comparable) new Integer(key1)).compareTo(new Integer(    //---当然为了降序排只要把key1和key2互
		      key2));                                                                                                            //-----换一下
		   }
		  });

		  return entries;
		 }
	public void readFile(String input, Set<String> set,
			ArrayList<String> array, Hashtable<String, Integer> h) {
		BufferedReader br2 = null;
		{

			try {

				String sCurrentLine;

				br2 = new BufferedReader(new FileReader(input));

				while ((sCurrentLine = br2.readLine()) != null) {
					sCurrentLine=sCurrentLine.toLowerCase();
					set.add(sCurrentLine);
					array.add(sCurrentLine);
					Vocabulary.add(sCurrentLine);
					if (!Vtable.containsKey(sCurrentLine)) {
						Vtable.put(sCurrentLine, 1);
					} else {
						int a = Vtable.get(sCurrentLine);
						a++;
						Vtable.put(sCurrentLine, a);
						// if(a>5){
						// System.out.println(sCurrentLine);
						// }
					}
					if (!h.containsKey(sCurrentLine)) {
						h.put(sCurrentLine, 1);
					} else {
						int a = h.get(sCurrentLine);
						a++;
						h.put(sCurrentLine, a);
						// if(a>5){
						// System.out.println(sCurrentLine);
						// }
					}

				}

			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (br2 != null)
						br2.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		}
	}

	public int readfileandcalculation(String input, double prior_Con,
			double prior_Lib, int V, int Con, int Lib,int m) {
		BufferedReader br2 = null;
		double resultC = prior_Con;
		double resultL = prior_Lib;
		int result = 0;
		double pC = 0;
		double pL = 0;

		{

			try {

				String sCurrentLine;

				br2 = new BufferedReader(new FileReader(input));
				double kC1=(double)Con/(double)(Con+m);
				double kL1=(double)Lib/(double)(Lib+m);
				double kC2=(double)m/(double)(Con+m);
				double kL2=(double)m/(double)(Lib+m);
				while ((sCurrentLine = br2.readLine()) != null) {
					sCurrentLine=sCurrentLine.toLowerCase();
					if (Vocabulary.contains(sCurrentLine)) {
						double tmpC = 0;
						double tmpL = 0;
						double n=(double)Vtable.get(sCurrentLine);
						double q=(double)(n+1)/(double)(V+Con+Lib);
						// System.out.println(k);
						if (mytableC.containsKey(sCurrentLine)) {
				
							tmpC = kC1*(((double)mytableC.get(sCurrentLine))/(double)Con)+kC2*q;
						} else {
							tmpC = kC2*q;
						}
						if (mytableL.containsKey(sCurrentLine)) {
							tmpL = kC1*(((double)mytableL.get(sCurrentLine))/(double)Lib)+kC2*q;
							
						} else {
							tmpL = kC2*q;
						}

						pC = pC + Math.log(tmpC);
						pL = pL + Math.log(tmpL);
						

					}}
					resultC = Math.log(resultC) + pC;
					resultL = Math.log(resultL) + pL;
					if (resultC > resultL) {
						result = 1;
					} else
						result = 0;

				
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (br2 != null)
						br2.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		}
	
		return result;
	}

	public void readTestingdata(String input, Set<String> testfiles) {
		BufferedReader br2 = null;
		{

			try {

				String sCurrentLine;

				br2 = new BufferedReader(new FileReader(input));

				while ((sCurrentLine = br2.readLine()) != null) {
					testfiles.add(sCurrentLine);

				}

			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (br2 != null)
						br2.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		}
	}
}