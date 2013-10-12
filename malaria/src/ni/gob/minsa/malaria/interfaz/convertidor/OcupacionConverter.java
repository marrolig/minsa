// -----------------------------------------------
// OcupacionConverter.java
// -----------------------------------------------
package ni.gob.minsa.malaria.interfaz.convertidor;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import ni.gob.minsa.ciportal.dto.InfoResultado;
import ni.gob.minsa.malaria.datos.general.OcupacionDA;
import ni.gob.minsa.malaria.modelo.general.Ocupacion;
import ni.gob.minsa.malaria.servicios.general.OcupacionService;


/**
 * Conversor vinculado a la clase {@link Ocupacion} y que sirve de soporte
 * a la capa de interfaz. 
 *
 * <p>
 * @author Marlon Arróliga
 * @author <a href=mailto:marrolig@hotmail.com>marrolig@hotmail.com</a>
 * @version 1.0, &nbsp; 23/07/2012
 * @since jdk1.6.0_21
 */
@FacesConverter(value="ocupacionConverter")
public class OcupacionConverter implements Converter {

	// ------------------------------------------- Acciones

	/**
	 * Obtiene el actual objeto Ocupacion a partir de la conversión de la única representación
	 * de Cadena de Texto (String) de dicho objeto almacenado en el valor enviado

	 * @param pContexto  Contexto de la acción
	 * @param pComponente Componente implicado en la acción
	 * @param pValorEnviado Representación en cadena de texto del Objeto {@link Ocupacion} actual
	 * @return Objeto actual representado por la cadena de texto
	 */	
	public Object getAsObject(FacesContext pContexto, UIComponent pComponente, String pValorEnviado) {
		
		if (pValorEnviado.trim().equals("")) {
			return null;
		} 
		else {
			try {
				OcupacionService ocupacionService=new OcupacionDA();
				InfoResultado oResultado=ocupacionService.Encontrar(Long.parseLong(pValorEnviado));
				if (oResultado.isOk()) {
					return ((Ocupacion)oResultado.getObjeto());
				}
				else {
					throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error de Conversión", "No es una ocupación válida")); 
				}
			} catch(NumberFormatException exception) {
		    	throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error de Conversión", "No es una ocupación válida"));
			}
		}
	}
	/**
	 * Convierte el objeto {@link Ocupacion} a su única representación en
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
			return String.valueOf(((Ocupacion) pValor).getOcupacionId());
		}
	}
}
