package annotators;

import java.util.Iterator;
import java.util.StringTokenizer;

import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.FSIndex;
import org.apache.uima.jcas.JCas;

import defecto.NoDetector;

public class NegatedSentenceAnnotator extends JCasAnnotator_ImplBase {

	@Override
	public void process(JCas jCas) throws AnalysisEngineProcessException {
		
		String docText = jCas.getDocumentText();
		int posAux = 0;
		StringTokenizer tokenizer = new StringTokenizer(docText,"\t\n\r.<.>/?\";:[{]}\\|=+()!", true);

		
		//Obtenemos los índices de las anotaciones producidas por el "NoDetectorAnnotator"
		FSIndex<NoDetector> noIndex = jCas.getAnnotationIndex(NoDetector.type);
		
		//Tengo que ver el índice de cada anotación
		Iterator<NoDetector> noIter = noIndex.iterator();
		while(noIter.hasNext()) {
			NoDetector annotation = noIter.next();
			annotation.getBegin();
		}

	}

}
