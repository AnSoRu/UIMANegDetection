package auxiliar;
public class Oracion {
	
	private String oracion;
	private int id;

	public Oracion(String oracion, int id) {
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
}