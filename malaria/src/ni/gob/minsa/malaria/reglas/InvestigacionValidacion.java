package ni.gob.minsa.malaria.reglas;


import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import ni.gob.minsa.ciportal.dto.InfoResultado;
import ni.gob.minsa.malaria.modelo.investigacion.InvestigacionHospitalario;
import ni.gob.minsa.malaria.modelo.investigacion.InvestigacionMalaria;
import ni.gob.minsa.malaria.modelo.investigacion.InvestigacionMedicamento;
import ni.gob.minsa.malaria.modelo.investigacion.InvestigacionSintoma;
import ni.gob.minsa.malaria.modelo.investigacion.InvestigacionTransfusion;

public class InvestigacionValidacion {
	
	public static InfoResultado validarInvestigacionMalaria(
			InvestigacionMalaria pInvestigacionMalaria,
			InvestigacionSintoma pInvestigacionSintoma,
			List<InvestigacionMedicamento> pInvestigacionMedicamento,
			InvestigacionTransfusion pInvestigacionTransfusion,
			InvestigacionHospitalario pInvestigacionHospitalario){
		
		InfoResultado oResultado = new InfoResultado();
		oResultado.setOk(true);
		
		if (pInvestigacionMalaria==null) {
			return oResultado;
		}
		
		if(pInvestigacionMalaria.getNumeroCaso()==null || pInvestigacionMalaria.getNumeroCaso().trim().equals("")){
			oResultado.setOk(false);
			oResultado.setMensaje("El número de caso es requerido");
			return oResultado;
		}
		if(pInvestigacionMalaria.getConfirmacionEntidad()==null || 
				(pInvestigacionMalaria.getConfirmacionEntidad().getCodigo()==null || pInvestigacionMalaria.getConfirmacionEntidad().getCodigo().trim().equals(""))){
			oResultado.setOk(false);
			oResultado.setMensaje("Debe indicar el resultado del diagnóstico por parte del laboratorio del SILAIS ");
			return oResultado;
		}
		if(pInvestigacionMalaria.getConfirmacionCndr()==null || 
				(pInvestigacionMalaria.getConfirmacionCndr().getCodigo()==null || pInvestigacionMalaria.getConfirmacionCndr().getCodigo().trim().equals(""))){
			oResultado.setOk(false);
			oResultado.setMensaje("Debe indicar el resultado del diagnóstico por parte del laboratorio del CNDR ");
			return oResultado;
		}
		if(pInvestigacionMalaria.getInicioTratamiento()==null && pInvestigacionMalaria.getFinTratamiento()!=null){
			oResultado.setOk(false);
			oResultado.setMensaje("Si se establece una fecha de fin de tratamiento debe indicar también la fecha de inicio de tratamiento");
			return oResultado;
		}
		if(pInvestigacionMalaria.getConvivientesTratados()==null){
			oResultado.setOk(false);
			oResultado.setMensaje("Debe indicar el número de personas que conviven con el paciente y que han " +
					"sido tratadas de forma preventiva con quimioprofilaxis antimalárico. valor mínimo 0 ");
			return oResultado;
		}
		if(pInvestigacionMalaria.getColateralesTratados()==null){
			oResultado.setOk(false);
			oResultado.setMensaje("Debe indicar el Número de personas tratadas de forma preventiva con quimioprofilaxis antimalárico, " +
					"que viven en las cercanías del lugar de residencia del paciente objeto de la investigación epidemiológica, valor mínimo 0 ");
			return oResultado;
		}
		
		if(pInvestigacionMalaria.getCondicionFinalVivo()!=null){
			if(pInvestigacionMalaria.getCondicionFinalVivo()==BigDecimal.valueOf(0) && pInvestigacionMalaria.getFechaDefuncion()==null){
				oResultado.setMensaje("Si la persona ha fallecido debe indicar la fache de defunción");
				oResultado.setOk(false);
				return oResultado;
			}
			if(pInvestigacionMalaria.getFechaDefuncion().after(new Date())){
				oResultado.setMensaje("La fecha de defunción no puede ser posterior a la actual");
				oResultado.setOk(false);
				return oResultado;
			}
			if(pInvestigacionMalaria.getFechaDefuncion().after(pInvestigacionMalaria.getMuestreoHematico().getFechaToma())){
				oResultado.setMensaje("La fecha de defunción no puede ser posterior a la fecha de toma de muestra");
				oResultado.setOk(false);
				return oResultado;
			}
		}
		
		//Validando datos de investigación asociados a investigación de medicamentos
		if(pInvestigacionMedicamento!=null){
			for(int i=0;oResultado.isOk()==false || pInvestigacionMedicamento.size() <=i;i++){
				oResultado = validarInvestigacionMedicamento(pInvestigacionMedicamento.get(0));
			}
			if(oResultado.isOk()==false) return oResultado;
		}
		
		//Validando datos de investigación asociados a investigación malaria
		if(pInvestigacionMalaria.getSintomatico()==BigDecimal.valueOf(1) && pInvestigacionSintoma==null){
			oResultado.setOk(false);
			oResultado.setMensaje("Si la persona es sintomática debe definir la información relacionada a investigación de sintomas");
			return oResultado;
		}else if(pInvestigacionMalaria.getSintomatico()==BigDecimal.valueOf(1) && pInvestigacionSintoma!=null){
			oResultado = validarInvestigacionSintoma(pInvestigacionSintoma);
			if(oResultado.isOk()==false) return oResultado;
		}
		
		//Validando datos de investigación asociados a investigación de transfusiones
		if(pInvestigacionMalaria.getTransfusion()==BigDecimal.valueOf(1) && pInvestigacionTransfusion==null){
			oResultado.setOk(false);
			oResultado.setMensaje("Si la persona ha recibido una transfusión debe definir la información relacionada a investigación de transfusión");
			return oResultado;
		}else if(pInvestigacionMalaria.getTransfusion()==BigDecimal.valueOf(1) && pInvestigacionTransfusion!=null){
			oResultado = validarInvestigacionTransfusion(pInvestigacionTransfusion);
			if(oResultado.isOk()==false) return oResultado;
		}
		if(pInvestigacionMalaria.getManejoClinico()==BigDecimal.valueOf(1) && pInvestigacionTransfusion==null){
			oResultado.setOk(false);
			oResultado.setMensaje("Si se indica que hubo manejo clínico terapéutico, se debe definir la información relacionada a investigación hospitalaria");
			return oResultado;
		}else if(pInvestigacionMalaria.getManejoClinico()==BigDecimal.valueOf(1) && pInvestigacionTransfusion==null){
			oResultado = validarInvestigacionHospitalario(pInvestigacionHospitalario);
			if(oResultado.isOk()==false) return oResultado;
		}
		
		//Validando datos vinculados a Investigación hospitalario
		if(pInvestigacionHospitalario!=null){
			if(pInvestigacionHospitalario.getFechaIngreso()!=null){
				if(pInvestigacionMalaria.getFechaDefuncion().after(pInvestigacionHospitalario.getFechaIngreso())){
					oResultado.setMensaje("La fecha de defunción no puede ser posterior a la fecha de ingreso a hospitalización");
					oResultado.setOk(false);
					return oResultado;
				}
			}
		}
		//Validando datos vinculados a Investigación sintomas
		if(pInvestigacionSintoma!=null){
			if(pInvestigacionSintoma.getFechaInicioSintomas()!=null){
				if(pInvestigacionMalaria.getFechaDefuncion().after(pInvestigacionSintoma.getFechaInicioSintomas())){
					oResultado.setMensaje("La fecha de defunción no puede ser posterior a la fecha de inicio de sintomas");
					oResultado.setOk(false);
					return oResultado;
				}
			}
			if(pInvestigacionSintoma.getFechaInicioSintomas()!=null){
				if(pInvestigacionSintoma.getInvestigacionMalaria().getInicioTratamiento().before(pInvestigacionSintoma.getFechaInicioSintomas())){
					oResultado.setOk(false);
					oResultado.setMensaje("La fecha de inicio del tratamiento no puede ser inferior a la fecha de inicio de los síntomas");
					return oResultado;
				}
			}
		}
		oResultado.setOk(true);
		return oResultado;
	}
	
	public static InfoResultado validarInvestigacionTransfusion(InvestigacionTransfusion pInvestigacionTransfusion){
		InfoResultado oResultado = new InfoResultado();
		oResultado.setOk(true);
		
		if (pInvestigacionTransfusion==null) {
			return oResultado;
		}
		if(pInvestigacionTransfusion.getFechaTransfusion().after(new Date())){
			oResultado.setOk(false);
			oResultado.setMensaje("La fecha de transfusión no puede ser posterior a la fecha actual");
			return oResultado;
		}
		
		oResultado.setOk(true);
		return oResultado;
	}
	
	public static InfoResultado validarInvestigacionHospitalario(InvestigacionHospitalario pInvestigacionHospitalario){
		InfoResultado oResultado = new InfoResultado();
		oResultado.setOk(true);
		
		if (pInvestigacionHospitalario==null) {
			return oResultado;
		}
		
		if(pInvestigacionHospitalario.getFechaIngreso() != null) {
			if (pInvestigacionHospitalario.getInvestigacionMalaria().getFechaDefuncion().after(pInvestigacionHospitalario.getFechaIngreso())) {
				oResultado.setMensaje("La fecha de defunción no puede ser posterior a la fecha de ingreso a hospitalización");
				oResultado.setOk(false);
				return oResultado;
			}
		}
		
		if(pInvestigacionHospitalario.getUnidad()==null){
			oResultado.setMensaje("La unidad de hospitalización es requerido");
			oResultado.setOk(false);
			return oResultado;
		}
		
		if(pInvestigacionHospitalario.getMunicipio()==null){
			oResultado.setMensaje("El municipio en el que se encuentra la unidad de hospitalización es requerido");
			oResultado.setOk(false);
			return oResultado;
		}
		
		oResultado.setOk(true);
		return oResultado;
	}
	
	public static InfoResultado validarInvestigacionSintoma(InvestigacionSintoma pInvestigacionSintoma){
		InfoResultado oResultado = new InfoResultado();
		oResultado.setOk(true);
		
		if (pInvestigacionSintoma==null) {
			return oResultado;
		}
		
		if(pInvestigacionSintoma.getSintomatico()==BigDecimal.valueOf(1) && pInvestigacionSintoma.getFechaInicioSintomas()==null){
			oResultado.setOk(false);
			oResultado.setMensaje("Si el paciente es sintomático debe indicar la fecha de inicio de síntomas");
			return oResultado;
		}
		
		if(pInvestigacionSintoma.getFechaInicioSintomas()!=null){
			if(pInvestigacionSintoma.getInvestigacionMalaria().getInicioTratamiento().before(pInvestigacionSintoma.getFechaInicioSintomas())){
				oResultado.setOk(false);
				oResultado.setMensaje("La fecha de inicio del tratamiento no puede ser inferior a la fecha de inicio de los síntomas");
				return oResultado;
			}
		}

		if(pInvestigacionSintoma.getEstadoFebril()==null || pInvestigacionSintoma.getEstadoFebril().trim().equals("")){
			oResultado.setOk(false);
			oResultado.setMensaje("Debe indicar el estado febril del paciente.");
			return oResultado;
		}
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
	
	public static InfoResultado validarInvestigacionMedicamento(InvestigacionMedicamento pInvestigacionMedicamento){
		InfoResultado oResultado = new InfoResultado();
		oResultado.setOk(true);
		
		if (pInvestigacionMedicamento==null) {
			return oResultado;
		}
		
		if (pInvestigacionMedicamento.getMedicamento()==null || pInvestigacionMedicamento.getMedicamento().getCodigo().trim().equals("")) {
			oResultado.setOk(false);
			oResultado.setMensaje("Debe indicar el medicamento antimalárico utilizado");
			return oResultado;
		}
		oResultado.setOk(true);
		return oResultado;
	}
}
