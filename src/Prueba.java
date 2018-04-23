import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

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
public class Prueba {
	
	public static void printAnnotations(JCas jcas) {
		Type tipo = jcas.getTypeSystem().getType("defecto.NoDetector");
		System.out.println("El tipo es " + tipo.getName());
		System.out.println("Tiene " + tipo.getNumberOfFeatures() + " propiedades");
		
		FSIterator<Annotation> iter = jcas.getAnnotationIndex(tipo).iterator();
		while(iter.isValid()) {
			FeatureStructure fs = iter.get();
			NoDetector annot = (NoDetector)fs;
			//System.out.println("Anotación (Covered Text) > " + annot.getCoveredText());
			System.out.println("Comienzo de anotación " + annot.getBegin() + " hasta " + annot.getEnd() + " de la oración " + annot.getOracionString() + " con id " + annot.getIdOracion());
			System.out.println();
			iter.moveToNext();
		}
	}

	public static void main(String[] args) {

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
			
			tCas.setDocumentText(document);
			tae.process(tCas);
			
			printAnnotations(tCas);
					
			tae.destroy();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidXMLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ResourceInitializationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (AnalysisEngineProcessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
