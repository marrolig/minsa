package ni.gob.minsa.malaria.interfaz.investigacion;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import ni.gob.minsa.ciportal.dto.InfoSesion;

/**
 * Servicio para la capa de presentación de la página 
 * investigacion/investigacionMalaria.xhtml
 *
 * <p>
 * @author Félix Medina
 * @author <a href=mailto:medina.fx@gmail.com>medina.fx@gmail.com</a>
 * @version 1.0, &nbsp; 20/10/2013
 * @since jdk1.6.0_21
 */
@ManagedBean
@ViewScoped
public class InvestigacionMalariaBean implements Serializable{
private static final long serialVersionUID = 1L;
	
	private InfoSesion infoSesion;

}
