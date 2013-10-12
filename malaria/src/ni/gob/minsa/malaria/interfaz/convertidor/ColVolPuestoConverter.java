// -----------------------------------------------
// ColVolPuestoConverter.java
// -----------------------------------------------
package ni.gob.minsa.malaria.interfaz.convertidor;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import ni.gob.minsa.ciportal.dto.InfoResultado;
import ni.gob.minsa.malaria.datos.vigilancia.PuestoNotificacionDA;
import ni.gob.minsa.malaria.modelo.vigilancia.PuestoNotificacion;
import ni.gob.minsa.malaria.modelo.vigilancia.noEntidad.ColVolPuesto;
import ni.gob.minsa.malaria.servicios.vigilancia.PuestoNotificacionService;


/**
 * Conversor vinculado a la clase {@link ColVolPuesto} y que sirve de soporte
 * a la capa de interfaz. 
 *
 * <p>
 * @author Marlon Arróliga
 * @author <a href=mailto:marrolig@hotmail.com>marrolig@hotmail.com</a>
 * @version 1.0, &nbsp; 30/01/2013
 * @since jdk1.6.0_21
 */
@FacesConverter(value="colVolPuestoConverter")
public class ColVolPuestoConverter implements Converter {

	// ------------------------------------------- Acciones

	/**
	 * Obtiene el actual objeto ColVolPuesto a partir de la conversión de la única representación
	 * de Cadena de Texto (String) de dicho objeto almacenado en el valor enviado

	 * @param pContexto  Contexto de la acción
	 * @param pComponente Componente implicado en la acción
	 * @param pValorEnviado Representación en cadena de texto del Objeto {@link ColVolPuesto} actual
	 * @return Objeto actual representado por la cadena de texto
	 */	
	public Object getAsObject(FacesContext pContexto, UIComponent pComponente, String pValorEnviado) {
		
		if (pValorEnviado.trim().equals("")) {
			return null;
		} 
		else {
			try {
				PuestoNotificacionService PuestoNotificacionService=new PuestoNotificacionDA();
				
				System.out.println("=========================================");
				System.out.println(pValorEnviado);
				System.out.println("=========================================");
				
				InfoResultado oResultado=PuestoNotificacionService.Encontrar(Long.parseLong(pValorEnviado));
				if (oResultado.isOk()) {
					PuestoNotificacion oPuestoNotificacion = ((PuestoNotificacion)oResultado.getObjeto());
					ColVolPuesto oColVolPuesto = new ColVolPuesto();
					oColVolPuesto.setClave(oPuestoNotificacion.getClave());
					oColVolPuesto.setNombreColVol(oPuestoNotificacion.getColVol().getSisPersona().getNombreCompleto());
					oColVolPuesto.setPuestoNotificacionId(oPuestoNotificacion.getPuestoNotificacionId());
					return (oColVolPuesto);
				}
				else {
					throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error de Conversión", "No es un Colaborador Voluntario vinculado a un Puesto de Notificación")); 
				}
			} catch(NumberFormatException exception) {
		    	throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error de Formato", "No es una Colaborador Voluntario vinculado a un Puesto de Notificación"));
			}
		}
	}
	/**
	 * Convierte el objeto {@link ColVolPuesto} a su única representación en
	 * cadena de texto (String)
	 * 
	 * @param pContexto  Contexto de la acción
	 * @param pComponente Componente implicado en la acción
	 * @param pValor Objeto a convertir a su representación de cadena de texto
	 * @return Cadena de texto que representa al objeto actual
	 */
	public String getAsString(FacesContext pContexto, UIComponent pComponente, Object pValor) {
		if (pValor == null) {
			return null;
		} 
		else {
			System.out.println("-------------------------------------");
			System.out.println(pValor);
			System.out.println("-------------------------------------");
			return String.valueOf(((ColVolPuesto) pValor).getPuestoNotificacionId());
		}
	}
}
