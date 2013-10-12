// -----------------------------------------------
// ColVolConverter.java
// -----------------------------------------------
package ni.gob.minsa.malaria.interfaz.convertidor;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import ni.gob.minsa.ciportal.dto.InfoResultado;
import ni.gob.minsa.malaria.datos.vigilancia.ColVolDA;
import ni.gob.minsa.malaria.modelo.vigilancia.ColVol;
import ni.gob.minsa.malaria.servicios.vigilancia.ColVolService;


/**
 * Conversor vinculado a la clase {@link ColVol} y que sirve de soporte
 * a la capa de interfaz. 
 *
 * <p>
 * @author Marlon Arr�liga
 * @author <a href=mailto:marrolig@hotmail.com>marrolig@hotmail.com</a>
 * @version 1.0, &nbsp; 30/01/2013
 * @since jdk1.6.0_21
 */
@FacesConverter(value="colVolConverter")
public class ColVolConverter implements Converter {

	// ------------------------------------------- Acciones

	/**
	 * Obtiene el actual objeto ColVol a partir de la conversi�n de la �nica representaci�n
	 * de Cadena de Texto (String) de dicho objeto almacenado en el valor enviado

	 * @param pContexto  Contexto de la acci�n
	 * @param pComponente Componente implicado en la acci�n
	 * @param pValorEnviado Representaci�n en cadena de texto del Objeto {@link ColVol} actual
	 * @return Objeto actual representado por la cadena de texto
	 */	
	public Object getAsObject(FacesContext pContexto, UIComponent pComponente, String pValorEnviado) {
		
		if (pValorEnviado.trim().equals("")) {
			return null;
		} 
		else {
			try {
				ColVolService ColVolService=new ColVolDA();
				InfoResultado oResultado=ColVolService.Encontrar(Long.parseLong(pValorEnviado));
				if (oResultado.isOk()) {
					return ((ColVol)oResultado.getObjeto());
				}
				else {
					throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error de Conversi�n", "No es un Colaborador Voluntario v�lido")); 
				}
			} catch(NumberFormatException exception) {
		    	throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error de Conversi�n", "No es una Colaborador Voluntario v�lido"));
			}
		}
	}
	/**
	 * Convierte el objeto {@link ColVol} a su �nica representaci�n en
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
			return String.valueOf(((ColVol) pValor).getColVolId());
		}
	}
}
