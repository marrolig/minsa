package ni.gob.minsa.malaria.interfaz.supervision;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import ni.gob.minsa.ciportal.dto.InfoSesion;

/**
 * Servicio para la capa de presentación de la página 
 * supervision/visitaPueto.xhtml
 *
 * <p>
 * @author Félix Medina
 * @author <a href=mailto:medina.fx@gmail.com>medina.fx@gmail.com</a>
 * @version 1.0, &nbsp; 09/11/2013
 * @since jdk1.6.0_21
 */
@ManagedBean
@ViewScoped
public class VisitaPuestoBean implements Serializable{
	private static final long serialVersionUID = 1L;

	protected InfoSesion infoSesion;
	private int capaActiva;
	
	private int modo;
}
