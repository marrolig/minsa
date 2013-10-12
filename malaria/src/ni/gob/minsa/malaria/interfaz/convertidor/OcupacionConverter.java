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
 * @author Marlon Arr�liga
 * @author <a href=mailto:marrolig@hotmail.com>marrolig@hotmail.com</a>
 * @version 1.0, &nbsp; 23/07/2012
 * @since jdk1.6.0_21
 */
@FacesConverter(value="ocupacionConverter")
public class OcupacionConverter implements Converter {

	// ------------------------------------------- Acciones

	/**
	 * Obtiene el actual objeto Ocupacion a partir de la conversi�n de la �nica representaci�n
	 * de Cadena de Texto (String) de dicho objeto almacenado en el valor enviado

	 * @param pContexto  Contexto de la acci�n
	 * @param pComponente Componente implicado en la acci�n
	 * @param pValorEnviado Representaci�n en cadena de texto del Objeto {@link Ocupacion} actual
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
					throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error de Conversi�n", "No es una ocupaci�n v�lida")); 
				}
			} catch(NumberFormatException exception) {
		    	throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error de Conversi�n", "No es una ocupaci�n v�lida"));
			}
		}
	}
	/**
	 * Convierte el objeto {@link Ocupacion} a su �nica representaci�n en
	 * cadena de texto (String)
	 * 
	 * @param pContexto  Contexto de la acci�n
	 * @param pComponente Componente implicado en la acci�n
	 * @param pValor Objeto a convertir a su representaci�n de cadena de texto
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
