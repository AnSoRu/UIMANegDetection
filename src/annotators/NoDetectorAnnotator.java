package annotators;
import java.util.HashSet;
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
import org.apache.uima.resource.SharedResourceObject;

import defecto.NoDetector;



public class NoDetectorAnnotator extends JCasAnnotator_ImplBase {

	//private Pattern noPattern = Pattern.compile("\\b(No|no).\\p{Punct}\\b");
	private Pattern noPattern = Pattern.compile("(No|no|ni|Tampoco|tampoco|Nunca|nunca|Sin|sin|Ningún|ningún)[\\s][a-zA-Z\\s]*[^\\p{Punct}]");

	//Fijarse en esta direccion
	//http://sujitpal.blogspot.com.es/2011/06/uima-analysis-engine-for-keyword.html
	
	private Set<Pattern> patternSet;
	
	//Map
	private StringMapResource mMap;
	
	@Override
	public void initialize(UimaContext aContext) throws ResourceInitializationException {
		super.initialize(aContext);
		try {
			/*SharedSetResource res = (SharedSetResource)aContext.getResourceObject("");
			patternSet = new HashSet<Pattern>();
			for(String patternString : res.getConfig()) {
				patternSet.add(Pattern.compile(patternString));
			}*/
			mMap = (StringMapResource)getContext().getResourceObject("Dictionary");
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
		int pos = 0;
		StringTokenizer tokenizer = new StringTokenizer(docText, " \t\n\r.<.>/?\";:[{]}\\|=+()!", true);
		while(tokenizer.hasMoreTokens()) {
			String token = tokenizer.nextToken();
			//Buscar en el map para ver si es una palabra de negación
			String negacion = mMap.get(token);
			if(negacion!=null) {
				NoDetector annotation = new NoDetector(jCas);
				annotation.setBegin(pos);
				annotation.setEnd(pos + token.length());
				annotation.addToIndexes();
			}
			pos += token.length();
		}
	}

}
