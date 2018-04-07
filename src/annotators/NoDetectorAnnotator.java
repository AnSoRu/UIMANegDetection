package annotators;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceAccessException;
import org.apache.uima.resource.ResourceInitializationException;

import defecto.NoDetector;



public class NoDetectorAnnotator extends JCasAnnotator_ImplBase {

	//private Pattern noPattern = Pattern.compile("\\b(No|no).\\p{Punct}\\b");
	//private Pattern noPattern = Pattern.compile("(No|no|ni|Tampoco|tampoco|Nunca|nunca|Sin|sin|Ningún|ningún)[\\s][a-zA-Z\\s]*[^\\p{Punct}]");

	//Fijarse en esta direccion
	//http://sujitpal.blogspot.com.es/2011/06/uima-analysis-engine-for-keyword.html
	
	//private Set<Pattern> patternSet;
	
	//Map
	private StringMapResource_impl mMap;
	private Map<String,String> mapAux;
	private String anterior;
	
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
			mapAux = mMap.getMap();
			anterior = "";
		} catch (ResourceAccessException e) {
			e.printStackTrace();
		}
	}


	@Override
	public void process(JCas jCas) throws AnalysisEngineProcessException {
		/*String docText = jCas.getDocumentText();
		Matcher matcher = noPattern.matcher(docText);
		while(matcher.find()) {
			NoDetector noAnnotation = new NoDetector(jCas);
			noAnnotation.setBegin(matcher.start());
			noAnnotation.setEnd(matcher.end());
			noAnnotation.addToIndexes();
		}*/
		String docText = jCas.getDocumentText();
		/*for(Pattern pattern : patternSet) {
			Matcher matcher = pattern.matcher(docText);
			int pos = 0;
			while(matcher.find(pos)) {
				pos = matcher.end();
				NoDetector annotation = new NoDetector(jCas);
				annotation.setBegin(matcher.start());
				annotation.setEnd(pos);
				annotation.addToIndexes();				
			}
		}*/
		int posAux = 0;
		StringTokenizer tokenizer = new StringTokenizer(docText,"\t\n\r.<.>/?\";:[{]}\\|=+()!", true);
		while(tokenizer.hasMoreTokens()) {
			String token = tokenizer.nextToken();
			System.out.println("El token es ");
			System.out.println(token);
			
			anterior = anterior + token;
			
			System.out.println("El anterior es -> " + anterior);
			System.out.println("El anterior con trim es -> " + anterior.trim());
			//Buscar en el map para ver si es una palabra de negación
			//Negacion es siempre un string vacio es solo para saber si es != null
			Set<String> it = mapAux.keySet();
			for(String sAux : it) {
				System.out.println("Evaluando si contiene " + sAux);
				if(isContained(anterior,sAux)) {
				//if(anterior.contains(sAux)) {
					System.out.println("La oracion contiene " + sAux);
					System.out.println("Comienza en : " + anterior.indexOf(sAux));
					posAux = anterior.indexOf(sAux);
					int inicio = anterior.indexOf(sAux);
					int fin = inicio + sAux.length();
					NoDetector annotation = new NoDetector(jCas);
					annotation.setBegin(inicio);
					//annotation.setEnd(pos + token.length());
					annotation.setEnd(fin);
					annotation.addToIndexes();
					//break;
				}
			}
			posAux = posAux + anterior.length();
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