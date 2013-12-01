package ni.gob.minsa.malaria.modelo.vigilancia.noEntidad;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

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
	private Date fechaApertura;
	private Date fechaCierre;
	private boolean pasivo;
             
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

	public void setFechaApertura(Date fechaApertura) {
		this.fechaApertura = fechaApertura;
	}

	public Date getFechaApertura() {
		return fechaApertura;
	}

	public void setFechaCierre(Date fechaCierre) {
		this.fechaCierre = fechaCierre;
	}

	public Date getFechaCierre() {
		return fechaCierre;
	}

	public void setPasivo(boolean pasivo) {
		this.pasivo = pasivo;
	}

	public boolean isPasivo() {
		if (fechaApertura.after(Calendar.getInstance().getTime())) return true;
		if (fechaCierre!=null && fechaCierre.before(Calendar.getInstance().getTime())) return true;
		return false;
	}

	
	
}
