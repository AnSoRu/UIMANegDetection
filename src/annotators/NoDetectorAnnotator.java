package annotators;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.FSIndex;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceAccessException;
import org.apache.uima.resource.ResourceInitializationException;

import auxiliar.Oracion;
import defecto.NoDetector;



public class NoDetectorAnnotator extends JCasAnnotator_ImplBase {

	//private Pattern noPattern = Pattern.compile("\\b(No|no).\\p{Punct}\\b");
	//private Pattern noPattern = Pattern.compile("(No|no|ni|Tampoco|tampoco|Nunca|nunca|Sin|sin|Ningún|ningún)[\\s][a-zA-Z\\s]*[^\\p{Punct}]");

	//Fijarse en esta direccion
	//http://sujitpal.blogspot.com.es/2011/06/uima-analysis-engine-for-keyword.html

	//private Set<Pattern> patternSet;

	//Map
	private StringMapResource_impl mMap;
	//private Map<String,String> mapAux;
	private List<String> listaPalabras;
	private String anterior;
	static String[] SENTENCE;
	private List<Oracion> oraciones;
	private List<Integer> longitudOraciones; //Longitud acumulada
	private int idOracion;

	//ParsePosition pp = new ParsePosition(0);

	// ****************************************
	// * Static vars holding break iterators
	// ****************************************
	static final BreakIterator sentenceBreak = BreakIterator.getSentenceInstance(Locale.US);

	@Override
	public void initialize(UimaContext aContext) throws ResourceInitializationException {
		super.initialize(aContext);
		try {
			/*SharedSetResource res = (SharedSetResource)aContext.getResourceObject("");
			patternSet = new HashSet<Pattern>();
			for(String patternString : res.getConfig()) {
				patternSet.add(Pattern.compile(patternString));
			}*/
			mMap = (StringMapResource_impl)getContext().getResourceObject("Dictionary");
			//mapAux = mMap.getMap();
			listaPalabras = mMap.getLista();
			oraciones = new ArrayList<Oracion>();
			longitudOraciones = new ArrayList<Integer>();
			idOracion = 0;
			anterior = "";
		} catch (ResourceAccessException e) {
			e.printStackTrace();
		}
	}


	@Override
	public void process(JCas jCas) throws AnalysisEngineProcessException {

		String docText = jCas.getDocumentText();
		System.out.println("################################");
		System.out.println("El texto es ");
		System.out.println(docText);

		try {
			File temp = File.createTempFile("tempfile",".txt");
			FileOutputStream fio = new FileOutputStream(temp);
			fio.write(docText.getBytes());
			fio.close();
			Scanner sentence = new Scanner(temp);
			ArrayList<String> sentenceList = new ArrayList<String>();
			while(sentence.hasNextLine()) {
				sentenceList.add(sentence.nextLine());
			}
			sentence.close();

			String[] sentenceArray = sentenceList.toArray(new String[sentenceList.size()]);

			for (int r=0;r<sentenceArray.length;r++)
			{
				System.out.println("R vale = " + r);
				System.out.println(sentenceArray[r]);
				SENTENCE = sentenceArray[r].split("(?<=[.!?])\\s*");
				for (int i=0;i<SENTENCE.length;i++)
				{
					System.out.println("Sentence " + (i+1) + ": " + SENTENCE[i]);
					oraciones.add(new Oracion(SENTENCE[i],idOracion));
					idOracion ++;
					longitudOraciones.add(SENTENCE[i].length());
				}
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		int posAux = 0;
		//StringTokenizer tokenizer = new StringTokenizer(docText,"\t\r\n.<>/?\";:[{]}\\|=+()!", false);
		//StringTokenizer tokenizer = new StringTokenizer(docText,"(?<=[.!?])\\s*", false);
		//StringTokenizer tokenizer = new StringTokenizer(docText,".", false);
		System.out.println("############################");
		System.out.println("Comenzando bucle while");
		int i = 0;
		while(i < oraciones.size()) {
			//String token = tokenizer.nextToken();
			Oracion oAux = oraciones.get(i);
			String token = oAux.getOracion();
			System.out.println("##############################");
			System.out.println("El token es ");
			System.out.println(token);
			System.out.println("La longitud es " + token.length());

			//anterior = anterior + token;

			//System.out.println("El anterior es -> " + anterior);
			//System.out.println("El anterior con trim es -> " + anterior.trim());
			//Buscar en el map para ver si es una palabra de negación
			//Negacion es siempre un string vacio es solo para saber si es != null
			//Set<String> it = mapAux.keySet();
			for(String sAux : listaPalabras) {
				System.out.println("Evaluando si contiene " + sAux);
				if(isContained(token,sAux)) {
					
		            int idOracion = oAux.getId();
		            int inicio = 0;
		            int fin = 0;
		            int j = 0;
		            if(idOracion > 0) {
		            	int longitudTotal = 0;
		            	while(j < idOracion) {
		            		longitudTotal = longitudTotal + longitudOraciones.get(j);
		            		j++;
		            	}
		            	inicio = token.indexOf(sAux) + longitudTotal;
		            	fin = inicio + sAux.length();
		            	longitudOraciones.add(idOracion,token.length());
		            }else {
		            	inicio = token.indexOf(sAux);
		            	fin = inicio + sAux.length();
		            	longitudOraciones.add(idOracion, token.length());
		            }
					//if(anterior.contains(sAux)) {
					System.out.println("La oracion contiene " + sAux);
					System.out.println("Comienza en : " + inicio);
					///
					posAux = token.indexOf(sAux);
				    //inicio = token.indexOf(sAux);
					//fin = inicio + sAux.length();
					///
					
					NoDetector annotation = new NoDetector(jCas);
					annotation.setBegin(inicio);
					annotation.setEnd(fin);
					annotation.setIdOracion(idOracion);
					///////////////////////////////////////////////////
					//Obtenemos los índices de las anotaciones producidas por el "NoDetectorAnnotator"
					FSIndex noIndex = jCas.getAnnotationIndex(NoDetector.type);
					if(noIndex.size()>0) {
						//System.out.println("#######################");
						//System.out.println("Hay cosas");
						if(noIndex.find(annotation)==null) {
							System.out.println("No existe aún la anotación");
							annotation.addToIndexes();
							//////////////////////////////////////////////////
							System.out.println("////////////////////////////");
							System.out.println("ANOTADA 1 VEZ");
						}
					}else {
						//System.out.println("No hay na de na");
						annotation.addToIndexes();
						//////////////////////////////////////////////////
						System.out.println("////////////////////////////");
						System.out.println("ANOTADA 1 VEZ");
					}
				}
			}
			posAux = posAux + token.length();
			i++;
			//anterior = "";
		}
	}

	private static boolean isContained(String source, String subItem) {
		String pattern = "\\b" + subItem + "\\b";
		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(source);
		return m.find();
	}
}