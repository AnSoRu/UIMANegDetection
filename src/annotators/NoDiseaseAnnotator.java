package annotators;

import java.util.ArrayList;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceAccessException;
import org.apache.uima.resource.ResourceInitializationException;

import defecto.NoDisease;

public class NoDiseaseAnnotator extends JCasAnnotator_ImplBase {
	
	//Map
	StringMapResource_impl mMap;
	@Override
	public void initialize(UimaContext aContext) throws ResourceInitializationException {
		super.initialize(aContext);
		try {
			mMap = (StringMapResource_impl)getContext().getResourceObject("Diseases");
			//mapAux = mMap.getMap();
		} catch (ResourceAccessException e) {
			e.printStackTrace();
		}
	}

	//Se trata de traerse todas las anotaciones de tipo NoDetector
	@Override
	public void process(JCas jCas) throws AnalysisEngineProcessException {
		
		jCas.getDocumentText();
		
		new ArrayList<NoDisease>();
	}

}
