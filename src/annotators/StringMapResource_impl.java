package annotators;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import org.apache.uima.resource.DataResource;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.SharedResourceObject;

public class StringMapResource_impl implements StringMapResource,SharedResourceObject {
	
	//private Map<String,String> mMap = new HashMap<>();
	private List<String> listaPalabras = new ArrayList<String>();
	@Override
	public void load(DataResource data) throws ResourceInitializationException {
		InputStream inputStream = null;
		try {
			inputStream = data.getInputStream();
			//leer las líneas
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
			String line;
			System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
			while ((line = reader.readLine()) != null) {
				System.out.println("Introduciendo " + line);
				//mMap.put(line,"");
				listaPalabras.add(line);
			}
		} catch (IOException e) {
			throw new ResourceInitializationException(e);
		}finally {
			if(inputStream!=null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public List<String> getLista(){
		return this.listaPalabras;
	}

	@Override
	public String get(String aKey) {
		//return (String)mMap.get(aKey);
		return "";
	}
	
	/*public Map<String,String> getMap(){
		return this.mMap;
	}*/
	
}
