package annotators;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
	private Map<String,String> mapAux;

	
	@Override
	public void initialize(UimaContext aContext) throws ResourceInitializationException {
		super.initialize(aContext);
		try {
			mMap = (StringMapResource_impl)getContext().getResourceObject("Diseases");
			mapAux = mMap.getMap();
		} catch (ResourceAccessException e) {
			e.printStackTrace();
		}
	}

	//Se trata de traerse todas las anotaciones de tipo NoDetector
	@Override
	public void process(JCas jCas) throws AnalysisEngineProcessException {
		
		String text = jCas.getDocumentText();
		
		/*Iterar sobre todas las anotaciones de tipo NoDetector. Hacer un Span hasta la 
		 siguiente anotacion. Si en el span hemos encontrado una enfermedad lo apuntamos como 
		 que no hay enfermedad y creamos una anotacion de tipo NoDisease. Añadimos esa anotación a una lista,
		 para posteriormente añadirla al CAS final. Se hace esto porque no está permitido añadir a un índice
		 sobre el cual estamos iterando
		*/
		List<NoDisease> noDiseases = new ArrayList<NoDisease>();
	}

}
