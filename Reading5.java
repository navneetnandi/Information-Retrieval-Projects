package lucene;
import org.apache.lucene.index.*;
import java.io.*;
import java.util.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.*;
import java.nio.file.*;

public class Reading5 {
	
	
	static FileWriter fw ;
	public void readFile(HashMap<String, LinkedList<Integer>> map, String outputFileName, String inputFileName)throws IOException
	{
		String line = null, word = null;
		int i, beg=0, end=0;
		
		FileReader fl = new FileReader(inputFileName);
		BufferedReader br = new BufferedReader(fl);
		
		ArrayList<String> lineTerm = new ArrayList<String>();
		
		while((line = br.readLine()) != null)
		{
			
			beg = 0; end= 0;
			for(i=0; i<line.length(); i++)
			{
				if(line.charAt(i) == ' ')
				{
					end = i;
					word = line.substring(beg, end);
					
					// Printing the Postings List:
					getPostings(map, word, outputFileName);
					
					//Adding each word in the line to the ArrayList
					lineTerm.add(word);
					beg = i+1;
				}
			}
			
		}
		br.close();
	}
	
	
	public void getPostings(HashMap<String, LinkedList<Integer>> map, String str, String outputFileName )throws IOException
	{
		boolean ifc = true;
		ifc = map.containsKey(str);
		
		if (ifc){
			System.out.println("GetPostings");
			fw.write("GetPostings");
			System.out.println(str);
			fw.write(str);
			System.out.println("Postings List :" + map.get(str) );
			fw.write("Postings List :" + map.get(str));
		
		}
			
	}
	

	
	public static void main(String args[])throws IOException
	{
		
		
		String pathFromArg = "C://index";
		String outputFileFromArg = "G://output1.txt";
		String inputFileFromArg = "G://input1.txt";
		
		fw = new FileWriter(outputFileFromArg);
		
		//Creating the inverted index		
		//String indexPath = "C://index";
		Path path = Paths.get(pathFromArg);
		Directory dirIndex = FSDirectory.open(path);
		IndexReader indexReader = DirectoryReader.open(dirIndex);
				
		HashMap<String, LinkedList<Integer>> map = new HashMap<>();
				
		Fields fields = MultiFields.getFields(indexReader);
		int tcount = 0;
		
		for(String field: fields){
			if(field.equals("text_en") || field.equals("text_ru") || field.equals("text_ja") || field.equals("text_pt") || field.equals("text_de") || field.equals("text_fr") || field.equals("text_de") || field.equals("text_es") || field.equals("text_it") || field.equals("text_da") || field.equals("text_no") || field.equals("text_sv"))
			{
				
				Terms terms = fields.terms(field);
				TermsEnum termsEnum = terms.iterator();
				BytesRef text = termsEnum.next();
				
				while((text = termsEnum.next()) != null)
				{
					PostingsEnum postings =  MultiFields.getTermDocsEnum(indexReader, field, text);
					System.out.println(field + " " + text.utf8ToString() + " " + termsEnum.docFreq()) ;
					
					LinkedList l = new LinkedList<>();
					int id=0;
					
					while((id=postings.nextDoc()) != postings.NO_MORE_DOCS){
						
						l.add(id);
					}
					
					map.put(text.utf8ToString(), l);
						
					System.out.println(text.utf8ToString() + " ===> " + map.get(text.utf8ToString()));
					System.out.println();
											
					tcount++;
					
					
					
				}
				
			}
		}
			
		System.out.println("Total number of Documents is " + indexReader.maxDoc());
		System.out.println("Total number of Terms is " + tcount);
		
		//Object Creation
		Reading5 r5 = new Reading5();
		r5.readFile(map, outputFileFromArg, inputFileFromArg);
		
		
	}
}
