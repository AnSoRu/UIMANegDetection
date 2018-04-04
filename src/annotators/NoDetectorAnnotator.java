package annotators;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;

import defecto.NoDetector;

public class NoDetectorAnnotator extends JCasAnnotator_ImplBase {

	//private Pattern noPattern = Pattern.compile("\\b(No|no).\\p{Punct}\\b");
	private Pattern noPattern = Pattern.compile("(No|no|ni|Tampoco|tampoco|Nunca|nunca|Sin|sin|Ningún|ningún)[\\s][a-zA-Z\\s]*[^\\p{Punct}]");

	@Override
	public void process(JCas jCas) throws AnalysisEngineProcessException {
		String docText = jCas.getDocumentText();
		Matcher matcher = noPattern.matcher(docText);
		while(matcher.find()) {
			NoDetector noAnnotation = new NoDetector(jCas);
			noAnnotation.setBegin(matcher.start());
			noAnnotation.setEnd(matcher.end());
			noAnnotation.addToIndexes();
		}
	}

}
