package ni.gob.minsa.malaria.reglas;


import java.util.Date;

import ni.gob.minsa.ciportal.dto.InfoResultado;
import ni.gob.minsa.malaria.modelo.investigacion.InvestigacionSintoma;

public class InvestigacionValidacion {
	
	public static InfoResultado validarInvestigacionSintoma(InvestigacionSintoma pInvestigacionSintoma){
		InfoResultado oResultado = new InfoResultado();
		
		if (pInvestigacionSintoma.getFechaInicioSintomas()==null) {
			oResultado.setOk(false);
			oResultado.setMensaje("La fecha de inicio de sintomas es requerida");
			return oResultado;
		}
		
		if (pInvestigacionSintoma.getFechaInicioSintomas().after(new Date())) {
			oResultado.setOk(false);
			oResultado.setMensaje("La fecha de inicio de inicio de sintomas no puede ser posterior a la fecha actual");
			return oResultado;
		}
		
		oResultado.setOk(true);
		
		return oResultado;
	}
}
