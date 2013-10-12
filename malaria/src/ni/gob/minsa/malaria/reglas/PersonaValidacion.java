package ni.gob.minsa.malaria.reglas;

import java.util.Calendar;

import ni.gob.minsa.ciportal.dto.InfoResultado;
import ni.gob.minsa.ejbPersona.dto.Persona;
import ni.gob.minsa.malaria.modelo.sis.SisPersona;
import ni.gob.minsa.malaria.soporte.Utilidades;

public class PersonaValidacion {

	/**
	 * Implementa un conjunto de validaciones sobre el objeto {@link Persona}
	 * 
	 * @param pPersona
	 * @return
	 */
	public static InfoResultado validar(Persona pPersona) {
		
		InfoResultado oResultado=new InfoResultado();
		
		if (pPersona.getFechaNacimiento()==null) {
			oResultado.setOk(false);
			oResultado.setMensaje("La fecha de nacimiento es requerida");
			return oResultado;
		}
		
		if (pPersona.getFechaNacimiento()!=null && !pPersona.getFechaNacimiento().before(Calendar.getInstance().getTime())) {
			oResultado.setOk(false);
			oResultado.setMensaje("La fecha de nacimiento no puede ser superior a la fecha actual");
			return oResultado;
		}
		
		if ((pPersona.getAseguradoNumero()!=null && pPersona.getTipoAsegCodigo()==null) || 
				(pPersona.getAseguradoNumero()==null && pPersona.getTipoAsegCodigo()!=null)) {
			oResultado.setOk(false);
			oResultado.setMensaje("Debe especificarse tanto el tipo de asegurado como el número de asegurado, o ninguno.");
			return oResultado;
		}
			
		if ((pPersona.getIdentNumero()!=null && pPersona.getIdentCodigo()==null) || 
				(pPersona.getIdentNumero()==null && pPersona.getIdentCodigo()!=null)) {
			oResultado.setOk(false);
			oResultado.setMensaje("Debe especificarse tanto el tipo de identificación como el número de la identificación, o ninguno.");
			return oResultado;
		}
		
		if (pPersona.getPaisNacCodigoAlfados()!=null && 
				pPersona.getPaisNacCodigoAlfados().equals(Utilidades.PAIS_CODIGO) && 
				pPersona.getMuniNacCodigoNac()==null) {

			oResultado.setOk(false);
			oResultado.setMensaje("Debe especificar el municipio de nacimiento.");
			return oResultado;
		}
		
		if (pPersona.getPaisNacCodigoAlfados()!=null && 
				!pPersona.getPaisNacCodigoAlfados().equals(Utilidades.PAIS_CODIGO) && 
				pPersona.getMuniNacCodigoNac()!=null) {

			oResultado.setOk(false);
			oResultado.setMensaje("Si el nacimiento se produjo fuera del país, no declare el municipio.");
			return oResultado;
		}

		// el control de datos de residencia debe ser 3 para ser válida
		int iResidencia=0;
		iResidencia=iResidencia+(pPersona.getMuniResiCodigoNac()!=null?1:0);
		iResidencia=iResidencia+(pPersona.getComuResiCodigo()!=null?1:0);
		iResidencia=iResidencia+((pPersona.getDireccionResi()!=null && !pPersona.getDireccionResi().trim().isEmpty())?1:0);
		
		if (iResidencia<3) {
			oResultado.setOk(false);
			oResultado.setMensaje("Todos los datos vinculados a la residencia o domicilio de la persona son requeridos");
			return oResultado;
		}

		if (pPersona.getPrimerNombre().trim()==null) {
			oResultado.setOk(false);
			oResultado.setMensaje("El primer nombre es requerido");
			return oResultado;
		}
		
		if (pPersona.getPrimerNombre().trim()==null) {
			oResultado.setOk(false);
			oResultado.setMensaje("El primer apellido es requerido");
			return oResultado;
		}
		
		if (pPersona.getSexoCodigo()==null) {
			oResultado.setOk(false);
			oResultado.setMensaje("La declaración del sexo de la persona es requerida");
			return oResultado;
		}
		
		oResultado.setOk(true);
		return oResultado;
	}
	
	/**
	 * Verifica si el objeto {@link Persona} contiene los mismos datos
	 * que el objeto {@link SisPersona} 
	 * 
	 * @param pPersona     Objeto {@link Persona}
	 * @param pSisPersona  Objeto {@link SisPersona}
	 * @return <code>true</code> significa que son iguales o <code>false</code>, que son diferentes
	 */
	public static boolean comparar(Persona pPersona,SisPersona pSisPersona) {

		if (!Utilidades.compararCadenas(pPersona.getPrimerNombre(), pSisPersona.getPrimerNombre())) return false;
		if (!Utilidades.compararCadenas(pPersona.getSegundoNombre(), pSisPersona.getSegundoNombre())) return false;
		if (!Utilidades.compararCadenas(pPersona.getPrimerApellido(), pSisPersona.getPrimerApellido())) return false;
		if (!Utilidades.compararCadenas(pPersona.getSegundoApellido(), pSisPersona.getSegundoApellido())) return false;
		if (!Utilidades.compararCadenas(pPersona.getSexoCodigo(), pSisPersona.getSexo()!=null?pSisPersona.getSexo().getCodigo():null)) return false;
		if (!Utilidades.compararCadenas(pPersona.getDireccionResi(),pSisPersona.getDireccionResidencia())) return false;
		if (!Utilidades.compararCadenas(pPersona.getIdentNumero(), pSisPersona.getIdentificacion())) return false;
		if (!Utilidades.compararCadenas(pPersona.getTelefonoResi(), pSisPersona.getTelefonoResidencia())) return false;
		if (!Utilidades.compararCadenas(pPersona.getTelefonoMovil(), pSisPersona.getTelefonoMovil())) return false;
		if (!Utilidades.compararCadenas(pPersona.getComuResiCodigo(), pSisPersona.getComunidadResidencia()!=null?pSisPersona.getComunidadResidencia().getCodigo():null)) return false;
		if (!Utilidades.compararCadenas(pPersona.getEscolaridadCodigo(),pSisPersona.getEscolaridad()!=null?pSisPersona.getEscolaridad().getCodigo():null)) return false;
		if (!Utilidades.compararCadenas(pPersona.getEstadoCivilCodigo(),pSisPersona.getEstadoCivil()!=null?pSisPersona.getEstadoCivil().getCodigo():null)) return false;
		if (!Utilidades.compararCadenas(pPersona.getEtniaCodigo(),pSisPersona.getEtnia()!=null?pSisPersona.getEtnia().getCodigo():null)) return false;
		if (!Utilidades.compararCadenas(pPersona.getIdentCodigo(),pSisPersona.getTipoIdentificacion()!=null?pSisPersona.getTipoIdentificacion().getCodigo():null)) return false;
		if (!Utilidades.compararCadenas(pPersona.getMuniNacCodigoNac(), pSisPersona.getMunicipioNacimiento()!=null?pSisPersona.getMunicipioNacimiento().getCodigoNacional():null)) return false;
		if (!Utilidades.compararCadenas(pPersona.getMuniResiCodigoNac(), pSisPersona.getMunicipioResidencia()!=null?pSisPersona.getMunicipioResidencia().getCodigoNacional():null)) return false;
		if (!Utilidades.compararCadenas(pPersona.getOcupacionCodigo(), pSisPersona.getOcupacion()!=null?pSisPersona.getOcupacion().getCodigo():null)) return false;
		if (!Utilidades.compararCadenas(pPersona.getPaisNacCodigoAlfados(), pSisPersona.getPaisNacimiento()!=null?pSisPersona.getPaisNacimiento().getCodigoAlfados():null)) return false;
		if (!Utilidades.compararCadenas(pPersona.getTipoAsegCodigo(), pSisPersona.getTipoAsegurado()!=null?pSisPersona.getTipoAsegurado().getCodigo():null)) return false;
		if (!Utilidades.compararCadenas(pPersona.getAseguradoNumero(), pSisPersona.getNumeroAsegurado())) return false;
		return true;
	}
}
