package ni.gob.minsa.malaria.modelo.encuesta.noEntidad;

import java.util.Date;

public class CriaderosUltimaNotificacion {
	
	private long criaderoId;
	private String codigo;
	private Date fechaUltimaNotificacion;
	private String nombre;
	private String comunidad;
	private String direccion;
	private String tipo;
	private String clasificacion;
	private long areaActual;
	private long distanciaCasa;
	
	
	public CriaderosUltimaNotificacion() {
		// TODO Auto-generated constructor stub
	}

	public CriaderosUltimaNotificacion(long criaderoId, String codigo,
			Date fechaUltimaNotificacion, String nombre, String comunidad,
			String direccion, String tipo, String clasificacion,
			long areaActual, long distanciaCasa) {
		this.criaderoId = criaderoId;
		this.codigo = codigo;
		this.fechaUltimaNotificacion = fechaUltimaNotificacion;
		this.nombre = nombre;
		this.comunidad = comunidad;
		this.direccion = direccion;
		this.tipo = tipo;
		clasificacion = clasificacion;
		this.areaActual = areaActual;
		this.distanciaCasa = distanciaCasa;
	}

	public long getCriaderoId() {
		return criaderoId;
	}

	public void setCriaderoId(long criaderoId) {
		this.criaderoId = criaderoId;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public Date getFechaUltimaNotificacion() {
		return fechaUltimaNotificacion;
	}

	public void setFechaUltimaNotificacion(Date fechaUltimaNotificacion) {
		this.fechaUltimaNotificacion = fechaUltimaNotificacion;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getComunidad() {
		return comunidad;
	}

	public void setComunidad(String comunidad) {
		this.comunidad = comunidad;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo; 
	}

	public String getClasificacion() {
		return clasificacion;
	}

	public void setClasificacion(String clasificacion) {
		this.clasificacion = clasificacion;
	}

	public long getAreaActual() {
		return areaActual;
	}

	public void setAreaActual(long areaActual) {
		this.areaActual = areaActual;
	}

	public long getDistanciaCasa() {
		return distanciaCasa;
	}

	public void setDistanciaCasa(long distanciaCasa) {
		this.distanciaCasa = distanciaCasa;
	}
	
	

	
	
}
