package ni.gob.minsa.malaria.reglas;


import java.util.Date;
import java.util.List;

import ni.gob.minsa.ciportal.dto.InfoResultado;
import ni.gob.minsa.malaria.modelo.investigacion.InvestigacionHospitalario;
import ni.gob.minsa.malaria.modelo.investigacion.InvestigacionLugar;
import ni.gob.minsa.malaria.modelo.investigacion.InvestigacionMalaria;
import ni.gob.minsa.malaria.modelo.investigacion.InvestigacionMedicamento;
import ni.gob.minsa.malaria.modelo.investigacion.InvestigacionSintoma;
import ni.gob.minsa.malaria.modelo.investigacion.InvestigacionTransfusion;
import ni.gob.minsa.malaria.modelo.investigacion.SintomaLugarAnte;
import ni.gob.minsa.malaria.modelo.investigacion.SintomaLugarOtro;
import ni.gob.minsa.malaria.soporte.Utilidades;

public class InvestigacionValidacion {
	
	public static InfoResultado validarInvestigacionMalaria(
			InvestigacionMalaria pInvestigacionMalaria,
			InvestigacionSintoma pInvestigacionSintoma,
			List<InvestigacionMedicamento> pInvestigacionMedicamento,
			InvestigacionLugar pInvestigacionLugar,
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
			oResultado.setMensaje("Debe indicar el resultado del diagnóstico por parte del laboratorio del SILAIS");
			return oResultado;
		}
		if(pInvestigacionMalaria.getConfirmacionCndr()==null || 
				(pInvestigacionMalaria.getConfirmacionCndr().getCodigo()==null || pInvestigacionMalaria.getConfirmacionCndr().getCodigo().trim().equals(""))){
			oResultado.setOk(false);
			oResultado.setMensaje("Debe indicar el resultado del diagnóstico por parte del laboratorio del CNDR");
			return oResultado;
		}
		if(pInvestigacionMalaria.getTratamientoCompleto()==null){
			oResultado.setOk(false);
			oResultado.setMensaje("Debe indicar indicar si el tratamiento antimalárico del presente caso ha sido completado o no");
			return oResultado;
			
		}
		if(pInvestigacionMalaria.getTratamientoSupervisado()==null){
			oResultado.setOk(false);
			oResultado.setMensaje("Debe indicar indicar si el tratamiento se hizo de forma supervisada (en boca) o no.");
			return oResultado;
		}
		if(pInvestigacionMalaria.getControlParasitario()==null){
			oResultado.setOk(false);
			oResultado.setMensaje("Debe indicar indicar  si se efectuaron muestreos posteriores para el control parasitario.");
			return oResultado;
			 
		}
		if(pInvestigacionMalaria.getControlParasitario().intValue()==1){
			if(pInvestigacionMalaria.getDiasPosterioresControl()==null){
				oResultado.setOk(false);
				oResultado.setMensaje("Si se efectuaron muestreos posteriores para el control parasitario, debe indicar el número de días posteriores al tratamiento en el que se efectuó");
				return oResultado;
			}
			if(pInvestigacionMalaria.getResultadoControlPositivo()==null){
				oResultado.setOk(false);
				oResultado.setMensaje("Si se efectuaron muestreos posteriores para el control parasitario, debe indicar si el control parasitario fue negativo o no");
				return oResultado;
			}
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
			if(pInvestigacionMalaria.getCondicionFinalVivo().intValue()==0 && pInvestigacionMalaria.getFechaDefuncion()==null){
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
		}else{
			oResultado.setMensaje("Debe indicar la condición final de la persona objeto de la investigación epidemiológica. Vivo o Fallecido");
			oResultado.setOk(false);
			return oResultado;
		}
		
		if(pInvestigacionMalaria.getAutomedicacion().intValue()==1 && (pInvestigacionMalaria.getMedicamentosAutomedicacion()==null || pInvestigacionMalaria.getMedicamentosAutomedicacion().trim().equals(""))){
			oResultado.setMensaje("Si la persona se automedicó, debe ingresar la descripción de los medicamentos utilizados en la automedicación");
			oResultado.setOk(false);
			return oResultado;
		}
		
		//Validando datos de investigación asociados a investigación de medicamentos
		if(pInvestigacionMedicamento!=null){
			for(int i=0;oResultado.isOk()==false || pInvestigacionMedicamento.size() <=i;i++){
				oResultado = validarInvestigacionMedicamento(pInvestigacionMedicamento.get(0));
			}
			if(oResultado.isOk()==false) return oResultado;
		}
		
		//Validando datos de investigación asociados a investigación malaria
		if(pInvestigacionMalaria.getSintomatico().intValue()==1 && pInvestigacionSintoma==null){
			oResultado.setOk(false);
			oResultado.setMensaje("Si la persona es sintomática debe definir la información relacionada a investigación de sintomas");
			return oResultado;
		}else if(pInvestigacionMalaria.getSintomatico().intValue()==1 && pInvestigacionSintoma!=null){
			oResultado = validarInvestigacionSintoma(pInvestigacionSintoma);
			if(oResultado.isOk()==false) return oResultado;
		}
		
		//Validadon datos de investigacion asociados a lugares
		if(pInvestigacionMalaria.getViajesZonaRiesgo().intValue()==1 && pInvestigacionLugar==null){
			oResultado.setOk(false);
			oResultado.setMensaje("Si declara que la persona ha realizado viajes en los últimos 30 días a zonas maláricas, debe definir el lugar visitado");
			return oResultado;
		}else if(pInvestigacionMalaria.getViajesZonaRiesgo().intValue()==1 && pInvestigacionLugar!=null){
			oResultado = validarInvestigacionLugar(pInvestigacionLugar);
			if(oResultado.isOk()==false) return oResultado;
		}
		
		//Validando datos de investigación asociados a investigación de transfusiones
		if(pInvestigacionMalaria.getTransfusion().intValue()==0 && pInvestigacionTransfusion==null){
			oResultado.setOk(false);
			oResultado.setMensaje("Si la persona ha recibido una transfusión debe definir la información relacionada a investigación de transfusión");
			return oResultado;
		}else if(pInvestigacionMalaria.getTransfusion().intValue()==1 && pInvestigacionTransfusion!=null){
			oResultado = validarInvestigacionTransfusion(pInvestigacionTransfusion);
			if(oResultado.isOk()==false) return oResultado;
		}
		if(pInvestigacionMalaria.getManejoClinico().intValue()==1 && pInvestigacionTransfusion==null){
			oResultado.setOk(false);
			oResultado.setMensaje("Si se indica que hubo manejo clínico terapéutico, se debe definir la información relacionada a investigación hospitalaria");
			return oResultado;
		}else if(pInvestigacionMalaria.getManejoClinico().intValue()==1 && pInvestigacionTransfusion==null){
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
		
		if(pInvestigacionMalaria.getClasificacionClinica()==null || pInvestigacionMalaria.getClasificacionClinica().getCatalogoId() == 0){
			oResultado.setOk(false);
			oResultado.setMensaje("La clasificación clínica del caso malárico es requerida");
			return oResultado;
		}
		
		if(pInvestigacionMalaria.getTipoComplicacion()==null || pInvestigacionMalaria.getTipoComplicacion().getCatalogoId() == 0){
			oResultado.setOk(false);
			oResultado.setMensaje("El tipo de complicación del caso malárico es requerido");
			return oResultado;
		}
		
		if(pInvestigacionMalaria.getTipoRecurrencia()==null || pInvestigacionMalaria.getTipoRecurrencia().getCatalogoId() == 0){
			oResultado.setOk(false);
			oResultado.setMensaje("El tipo de infección del caso malárico es requerido");
			return oResultado;
		}
		
		if(pInvestigacionMalaria.getClasificacionCaso()==null || pInvestigacionMalaria.getClasificacionCaso().getCatalogoId() == 0){
			oResultado.setOk(false);
			oResultado.setMensaje("La clasificación según el origen de la infección es requerida");
			return oResultado;
		}

		if(pInvestigacionMalaria.getResponsable()==null||pInvestigacionMalaria.getResponsable().equals("")){
			oResultado.setOk(false);
			oResultado.setMensaje("Debe indicar el nombre de la persona que efectuó la investigación");
			return oResultado;
		}
		
		if(pInvestigacionMalaria.getResponsable()==null||pInvestigacionMalaria.getResponsable().equals("")){
			oResultado.setOk(false);
			oResultado.setMensaje("Debe indicar el nombre del epidemiólogo que participó en la investigación");
			return oResultado;
		}
		
		if(pInvestigacionMalaria.getCasoCerrado().intValue()==1){
			if(pInvestigacionMalaria.getFechaCierreCaso()==null){
				oResultado.setOk(false);
				oResultado.setMensaje("Si el caso se va a cerrar, debe indicar la fecha de cierre");
				return oResultado;
			}
			
			if(pInvestigacionMalaria.getFechaCierreCaso().after(new Date())){
				oResultado.setOk(false);
				oResultado.setMensaje("La fecha de cierre no puede ser posterior a la fecha actual");
				return oResultado;
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
			oResultado.setMensaje("La unidad de hospitalización es requerida");
			oResultado.setOk(false);
			return oResultado;
		}
		
		if(pInvestigacionHospitalario.getMunicipio()==null){
			oResultado.setMensaje("El municipio en el que se encuentra la unidad de hospitalización es requerida");
			oResultado.setOk(false);
			return oResultado;
		}
		
		oResultado.setOk(true);
		return oResultado;
	}
	
	public static InfoResultado validarInvestigacionLugar(InvestigacionLugar pInvestigacionLugar){
		InfoResultado oResultado = new InfoResultado();
		oResultado.setOk(true);
		
		if (pInvestigacionLugar==null) {
			return oResultado;
		}
		if(pInvestigacionLugar.getInfeccionResidencia().intValue()!=1 && pInvestigacionLugar.getPais()==null){
			oResultado.setOk(false);
			oResultado.setMensaje("Si la infeccion no ocurre en el lugar de residencia de la persona, debe indicar el país donde se produjo la infección");
			return oResultado;
		}
		
		if(pInvestigacionLugar.getInfeccionResidencia().intValue()!=1){
			if(pInvestigacionLugar.getPais().getCodigoAlfados().equalsIgnoreCase(Utilidades.PAIS_CODIGO)==false && pInvestigacionLugar.getMunicipio()==null){
				oResultado.setOk(false);
				oResultado.setMensaje("Si la infeccion no ocurre en el lugar de residencia de la persona y el lugar donde se produces es a nivel nacional, debe indicar el municipio");
				return oResultado;
			}
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
		
		if(pInvestigacionSintoma.getSintomatico().intValue()==1 && pInvestigacionSintoma.getFechaInicioSintomas()==null){
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

		if(pInvestigacionSintoma.getEstadoFebril()==null || pInvestigacionSintoma.getEstadoFebril().getCodigo().trim().equals("")){
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
	
	public static InfoResultado validarSintomaLugarAnte(SintomaLugarAnte pLugarAnte){
		InfoResultado oResultado = new InfoResultado();
		oResultado.setOk(true);
		
		if (pLugarAnte==null) {
			return oResultado;
		}
		if(pLugarAnte.getPais()==null || pLugarAnte.getPais().getPaisId() < 1){
			oResultado.setOk(false);
			oResultado.setMensaje("Debe indicar el país en el cual se presentaron síntomas ");
			return oResultado;
		}
		if(pLugarAnte.getPais().getCodigoAlfados().equals(Utilidades.PAIS_CODIGO) && 
				(pLugarAnte.getMunicipio()==null || pLugarAnte.getMunicipio().getDivisionPoliticaId() < 1)){
			oResultado.setOk(false);
			oResultado.setMensaje("Si el lugar donde inician los síntomas es nacional, debe indicarse el municipio");
			return oResultado;
		}
		if(pLugarAnte.getFechaUltima()==null){
			oResultado.setOk(false);
			oResultado.setMensaje("Debe indicar la fecha de último día de estadía en el lugar visitado");
			return oResultado;
		}
		
		if(pLugarAnte.getFechaUltima().after(new Date())){
			oResultado.setMensaje("la fecha de último día de estadía no puede ser posterior a la fecha actual");
			oResultado.setOk(false);
			return oResultado;
		}
		if(pLugarAnte.getEstadia()==null){
			oResultado.setOk(false);
			oResultado.setMensaje("Debe indicar el número de días de permanencia en el lugar visitado");
			return oResultado;
		}
		if(pLugarAnte.getPersonasSintomas()==null){
			oResultado.setOk(false);
			oResultado.setMensaje("Debe indicar si se encontró con personas con malaria o fiebre en el lugar referido");
			return oResultado;
		}
		oResultado.setOk(true);
		return oResultado;
	}
	
	public static InfoResultado validarSintomaLugarOtro(SintomaLugarOtro pLugarOtro){
		InfoResultado oResultado = new InfoResultado();
		oResultado.setOk(true);
		
		if (pLugarOtro==null) {
			return oResultado;
		}
		if(pLugarOtro.getPais()==null || pLugarOtro.getPais().getPaisId() < 1){
			oResultado.setOk(false);
			oResultado.setMensaje("Debe indicar el país en el cual se presentaron síntomas ");
			return oResultado;
		}
		if(pLugarOtro.getPais().getCodigoAlfados().equals(Utilidades.PAIS_CODIGO) && 
				(pLugarOtro.getMunicipio()==null || pLugarOtro.getMunicipio().getDivisionPoliticaId() < 1)){
			oResultado.setOk(false);
			oResultado.setMensaje("Si el lugar donde inician los síntomas es nacional, debe indicarse el municipio");
			return oResultado;
		}
		if(pLugarOtro.getMesInicio()==null || (pLugarOtro.getMesInicio().intValue() ==0)){
			oResultado.setOk(false);
			oResultado.setMensaje("Debe indicar el mes de inicio de los síntomas");
			return oResultado;
		}
		if(pLugarOtro.getAñoInicio()==null || (pLugarOtro.getAñoInicio().intValue() == 0)){
			oResultado.setOk(false);
			oResultado.setMensaje("Debe indicar el año de inicio de los síntomas");
			return oResultado;
		}
		if(pLugarOtro.getEstadia()==null){
			oResultado.setOk(false);
			oResultado.setMensaje("Debe indicar el número de días de permanencia en el lugar visitado");
			return oResultado;
		}
		if(pLugarOtro.getDiagnosticoPositivo()==null){
			oResultado.setOk(false);
			oResultado.setMensaje("Debe indicar si existió un diagnóstico positivo de malaria");
			return oResultado;
		}
		if(pLugarOtro.getAutomedicacion()==null){
			oResultado.setOk(false);
			oResultado.setMensaje("Debe indicar si la persona se automedicó o no");
			return oResultado;
		}
		if(pLugarOtro.getTratamientoCompleto()==null){
			oResultado.setOk(false);
			oResultado.setMensaje("Debe indicar si la persona completó el tratamiento antimalárico");
			return oResultado;
		}
		oResultado.setOk(true);
		return oResultado;
	}
}
