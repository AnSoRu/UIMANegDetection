import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.uima.UIMAFramework;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.analysis_engine.TextAnalysisEngine;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.cas.Type;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.ResourceSpecifier;
import org.apache.uima.util.InvalidXMLException;
import org.apache.uima.util.XMLInputSource;

import defecto.NoDetector;

@SuppressWarnings("deprecation")
public class NegationAnalyzer {

	//Clase que representa al programa
	
	//Tabla para almacenar de cada oración las anotaciones que tienen asociadas para facilitar posteriormente
	//la búsqueda
	//La clave es el id de la oración y el valor almacenado es una lista de las anotaciones que hay en esa oración
	private static HashMap<Integer,List<NoDetector>> anotaciones;

	public NegationAnalyzer() {
		try {

			File descriptor = new File("D:\\EclipseWorkspace\\UIMANegDetection\\desc\\NoDetectorAnnotator.xml");
			File inputFile = new File("D:\\Politecnica\\MásterIngInf\\4ºSemestre\\TFM\\PruebasAnotadorNegacion\\data\\Prueba1.txt");


			XMLInputSource in = new XMLInputSource(descriptor);
			ResourceSpecifier specifier = UIMAFramework.getXMLParser().parseResourceSpecifier(in);

			TextAnalysisEngine tae = UIMAFramework.produceTAE(specifier);
			JCas tCas = tae.newJCas();

			FileInputStream fis = new FileInputStream(inputFile);
			byte [] contents = new byte[(int)inputFile.length()];
			fis.read(contents);
			fis.close();
			String document = new String(contents);
			
			anotaciones = new HashMap<Integer,List<NoDetector>>();

			tCas.setDocumentText(document);
			tae.process(tCas);

			storeAnnotations(tCas);
			
			tae.destroy();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidXMLException e) {
			e.printStackTrace();
		} catch (ResourceInitializationException e) {
			e.printStackTrace();
		} catch (AnalysisEngineProcessException e) {
			e.printStackTrace();
		}
	}//end Constructor
	
	private static void storeAnnotations(JCas jcas) {
		Type tipo = jcas.getTypeSystem().getType("defecto.NoDetector");
		FSIterator<Annotation> iter = jcas.getAnnotationIndex(tipo).iterator();
		while(iter.isValid()) {
			FeatureStructure fs = iter.get();
			NoDetector annot = (NoDetector)fs;
			//System.out.println("Anotación (Covered Text) > " + annot.getCoveredText());
			System.out.println("Comienzo de anotación " + annot.getBegin() + " hasta " + annot.getEnd() + " ID de la oración "+ annot.getIdOracion());
			System.out.println("Oración " + annot.getOracionString() );
			//anotaciones.add(index, element);
			List<NoDetector> aux = anotaciones.get(annot.getIdOracion());
			if(aux==null) {
				//Creo una nueva lista
				List<NoDetector> nuevaLista = new ArrayList<NoDetector>();
				nuevaLista.add(annot);
				anotaciones.put(annot.getIdOracion(),nuevaLista);
			}else {
				aux.add(annot);
				anotaciones.put(annot.getIdOracion(),aux);
			}
			iter.moveToNext();
		}
	}

	public boolean isNegated(String concept, int sentenceId) {
		//Primero obtener la oración y las anotaciones
		//Segundo ver si está el concepto en la oración
		//
		List<NoDetector> anotacionesSentence = anotaciones.get(sentenceId);
		boolean res = false;
		boolean encontrado = false;
		if(anotacionesSentence!=null) {//Caso en el que existe la oración con ese id
			if(!anotacionesSentence.isEmpty()) {//Caso en el que existan anotaciones en esa oración
				for(NoDetector nD: anotacionesSentence) {
					//Obtengo la oración asociada a la anotación
					String sentence = nD.getOracionString();
					System.out.println("##########################");
					//Busco si encuentro el concept
					int indice = sentence.indexOf(concept);
					if(indice == -1) { //No se ha encontrado el concepto en la oración
						encontrado = false;
						break;
					}else { //Se ha encontrado el concepto en la oración
						encontrado = true;
						break;
					}
				}
				if(!encontrado) {
					//No existe el concepto en esa frase
					System.out.println("No se ha encontrado el concepto " + concept +" en la oracion " + sentenceId);
					res = false;
				}else {
					res = true;
				}
			}else {//Caso en el que no existan anotaciones en esa oración
				System.out.println("No existen anotaciones en esa oracion");
				res = false;
			}
		}else {
			System.out.println("No existe la oración con ese id");
			res = false;
		}
		return res;
	}
	
	public static void main(String [] args) {
		NegationAnalyzer nA = new NegationAnalyzer();
		System.out.println(nA.isNegated("enfermedad",2));
	}

}
