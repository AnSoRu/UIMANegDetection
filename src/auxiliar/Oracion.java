package auxiliar;
public class Oracion {
	
	private String oracion;
	private int id;
	private int inicio;
	private int fin;

	public Oracion(int id, String oracion, int inicio, int fin) {
		this.oracion = oracion;
		this.id = id;
	}

	public String getOracion() {
		return oracion;
	}

	public void setOracion(String oracion) {
		this.oracion = oracion;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getInicio() {
		return inicio;
	}

	public void setInicio(int inicio) {
		this.inicio = inicio;
	}

	public int getFin() {
		return fin;
	}

	public void setFin(int fin) {
		this.fin = fin;
	}
}