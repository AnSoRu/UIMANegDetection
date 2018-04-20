package auxiliar;
import java.io.UnsupportedEncodingException;

public class OneSentenceText {
	
	
	public String toOneSentenceText(String text) throws UnsupportedEncodingException {
		byte[] data = text.getBytes();
		int i = 0;
		while(i < data.length) {
			if((data[i]=='\n')) {
				data[i-1]='\0' ; 
				data[i] = '\0' ;
			}
			else {
				if(data[i]=='\t') {
					data[i] = '\0';
				}
			}
			i++;
		}
		String salida = new String(data,"UTF-8");
		salida = salida.replace("\0","");
		return salida;
	}
	
	
	
	/*public static void main(String[] args) {
		Path path = Paths.get("C:\\Users\\Angel\\Desktop\\Jugar.txt");
		try {
			byte[] data = Files.readAllBytes(path);
			int i = 0;
			while(i < data.length) {
				if((data[i]=='\n')) {
					data[i-1]='\0' ; 
					data[i] = '\0' ;
				}
				else {
					if(data[i]=='\t') {
						data[i] = '\0';
					}
				}
				i++;
			}
			String salida = new String(data,"UTF-8");
			salida = salida.replace("\0","");
			PrintWriter out = new PrintWriter("C:\\Users\\Angel\\Desktop\\Salida.txt");
			out.print(salida);
			out.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}*/
}