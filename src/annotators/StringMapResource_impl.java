package annotators;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.uima.resource.DataResource;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.SharedResourceObject;

public class StringMapResource_impl implements StringMapResource,SharedResourceObject {
	
	private Map<String,String> mMap = new HashMap<>();

	@Override
	public void load(DataResource data) throws ResourceInitializationException {
		InputStream inputStream = null;
		try {
			inputStream = data.getInputStream();
			//leer las líneas
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
			String line;
			while ((line = reader.readLine()) != null) {
				mMap.put(line,"");
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

	@Override
	public String get(String aKey) {
		return (String)mMap.get(aKey);
	}
	
	public Map<String,String> getMap(){
		return this.mMap;
	}
	
}
