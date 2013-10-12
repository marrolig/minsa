package ni.gob.minsa.malaria.modelo.vigilancia.noEntidad;

import java.io.Serializable;

/**
 * Clase de soporte para la presentación de los colaboradores voluntarios
 * declarados como puestos de notificación vinculados a una unidad de salud
 * <p>
 * @author Marlon Arróliga
 * @author <a href=mailto:marrolig@hotmail.com>marrolig@hotmail.com</a>
 * @version 1.0, &nbsp; 05/09/2013
 * @since jdk1.6.0_21
 */
public class ColVolPuesto implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private long puestoNotificacionId;
	private String nombreColVol;
	private String clave;
             
	public ColVolPuesto() {}

	public void setPuestoNotificacionId(long puestoNotificacionId) {
		this.puestoNotificacionId = puestoNotificacionId;
	}

	public long getPuestoNotificacionId() {
		return puestoNotificacionId;
	}

	public void setNombreColVol(String nombreColVol) {
		this.nombreColVol = nombreColVol;
	}

	public String getNombreColVol() {
		return nombreColVol;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	public String getClave() {
		return clave;
	}

	
	
}
