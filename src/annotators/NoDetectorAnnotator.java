package annotators;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.Charsets;
import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.text.AnnotationIndex;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceAccessException;
import org.apache.uima.resource.ResourceInitializationException;

import auxiliar.OneSentenceText;
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
	static String[] SENTENCE;
	private List<Oracion> oraciones;
	private List<Integer> longitudOraciones; //Longitud acumulada
	private OneSentenceText oST;

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
			oST = new OneSentenceText();
		} catch (ResourceAccessException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public void process(JCas jCas) throws AnalysisEngineProcessException {

		byte[] docText = null;
		docText = jCas.getDocumentText().getBytes();
		String docTextAux = null;
		docTextAux = new String(docText,Charsets.UTF_8);
		System.out.println("################################");
		System.out.println("El texto es ");
		System.out.println(docText);
		try {
			//Se eliminan los saltos de línea retornos de carro y tabuladores

			/*File temp = File.createTempFile("tempfile",".txt");
			FileOutputStream fio = new FileOutputStream(temp);
			fio.write(docText.getBytes());
			fio.close();*/

			//Este es el texto sobre el que voy a realizar anotaciones
			//No me importa la parte visual

			String oneSentence = oST.toOneSentenceText(docTextAux); 
			System.out.println("??????????????????????????????????");
			System.out.println("La transformada es ");
			System.out.println(oneSentence);
			//En la aplicación de visualización a partir de ahora no van a coincidir la anotación justo encima del texto correspondiente
			//pues estoy transformando el texto de entrada para que sea más rápida la búsqueda, quitando los retornos de carro y saltos de línea.
			//Puesto que estoy dependiendo al 100% de la posición de los caracteres en el texto para poder anotar, transformo todo el texto
			//en una sola línea
			//En definitiva el texto sobre el que anoto no contiene ni retornos de carro 
			//No es el objetivo de este proyecto que visualmente se vea la anotación.

			int posAux = 0;
			//StringTokenizer tokenizer = new StringTokenizer(docText,"\t\r\n.<>/?\";:[{]}\\|=+()!", false);
			//StringTokenizer tokenizer = new StringTokenizer(docText,"(?<=[.!?])\\s*", false);
			//StringTokenizer tokenizer = new StringTokenizer(docText,".", false);
			StringTokenizer tokenizer = new StringTokenizer(oneSentence,".",false);
			int idOracionAux = 0;
			while(tokenizer.hasMoreTokens()) {
				System.out.println("IIIIIIIIIIIIIIIIIIIIIIIIII");
				String oracionAux = tokenizer.nextToken();
				System.out.println("SE HA ENCONTRADO LA ORACIÓN " + oracionAux);
				int inicioOracion = oneSentence.indexOf(oracionAux);
				int finOracion = inicioOracion + oracionAux.length();
				System.out.println("Comienza en " + inicioOracion);
				System.out.println("Termina en " + finOracion);
				Oracion oAux = new Oracion(idOracionAux, oracionAux,inicioOracion,finOracion);
				oraciones.add(oAux);
				idOracionAux++;
			}
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
						inicio = token.indexOf(sAux);
						fin = inicio + sAux.length();
						longitudOraciones.add(idOracion,token.length());



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

						System.out.println("Se ha creado una anotación en la oración " + idOracion);
						System.out.println("La anotacion comienza en " + inicio);
						System.out.println("La anotación termina en " + fin);

						///////////////////////////////////////////////////
						//Obtenemos los índices de las anotaciones producidas por el "NoDetectorAnnotator"
						AnnotationIndex<Annotation> noIndex = jCas.getAnnotationIndex(NoDetector.type);
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
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static boolean isContained(String source, String subItem) {
		String pattern = "\\b" + subItem + "\\b";
		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(source);
		return m.find();
	}
}