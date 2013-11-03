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
			oResultado.setMensaje("El n�mero de caso es requerido");
			return oResultado;
		}
		if(pInvestigacionMalaria.getConfirmacionEntidad()==null || 
				(pInvestigacionMalaria.getConfirmacionEntidad().getCodigo()==null || pInvestigacionMalaria.getConfirmacionEntidad().getCodigo().trim().equals(""))){
			oResultado.setOk(false);
			oResultado.setMensaje("Debe indicar el resultado del diagn�stico por parte del laboratorio del SILAIS");
			return oResultado;
		}
		if(pInvestigacionMalaria.getConfirmacionCndr()==null || 
				(pInvestigacionMalaria.getConfirmacionCndr().getCodigo()==null || pInvestigacionMalaria.getConfirmacionCndr().getCodigo().trim().equals(""))){
			oResultado.setOk(false);
			oResultado.setMensaje("Debe indicar el resultado del diagn�stico por parte del laboratorio del CNDR");
			return oResultado;
		}
		if(pInvestigacionMalaria.getTratamientoCompleto()==null){
			oResultado.setOk(false);
			oResultado.setMensaje("Debe indicar indicar si el tratamiento antimal�rico del presente caso ha sido completado o no");
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
				oResultado.setMensaje("Si se efectuaron muestreos posteriores para el control parasitario, debe indicar el n�mero de d�as posteriores al tratamiento en el que se efectu�");
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
			oResultado.setMensaje("Si se establece una fecha de fin de tratamiento debe indicar tambi�n la fecha de inicio de tratamiento");
			return oResultado;
		}
		if(pInvestigacionMalaria.getConvivientesTratados()==null){
			oResultado.setOk(false);
			oResultado.setMensaje("Debe indicar el n�mero de personas que conviven con el paciente y que han " +
					"sido tratadas de forma preventiva con quimioprofilaxis antimal�rico. valor m�nimo 0 ");
			return oResultado;
		}
		if(pInvestigacionMalaria.getColateralesTratados()==null){
			oResultado.setOk(false);
			oResultado.setMensaje("Debe indicar el N�mero de personas tratadas de forma preventiva con quimioprofilaxis antimal�rico, " +
					"que viven en las cercan�as del lugar de residencia del paciente objeto de la investigaci�n epidemiol�gica, valor m�nimo 0 ");
			return oResultado;
		}
		
		if(pInvestigacionMalaria.getCondicionFinalVivo()!=null){
			if(pInvestigacionMalaria.getCondicionFinalVivo().intValue()==0 && pInvestigacionMalaria.getFechaDefuncion()==null){
				oResultado.setMensaje("Si la persona ha fallecido debe indicar la fache de defunci�n");
				oResultado.setOk(false);
				return oResultado;
			}
			if(pInvestigacionMalaria.getFechaDefuncion().after(new Date())){
				oResultado.setMensaje("La fecha de defunci�n no puede ser posterior a la actual");
				oResultado.setOk(false);
				return oResultado;
			}
			if(pInvestigacionMalaria.getFechaDefuncion().after(pInvestigacionMalaria.getMuestreoHematico().getFechaToma())){
				oResultado.setMensaje("La fecha de defunci�n no puede ser posterior a la fecha de toma de muestra");
				oResultado.setOk(false);
				return oResultado;
			}
		}else{
			oResultado.setMensaje("Debe indicar la condici�n final de la persona objeto de la investigaci�n epidemiol�gica. Vivo o Fallecido");
			oResultado.setOk(false);
			return oResultado;
		}
		
		if(pInvestigacionMalaria.getAutomedicacion().intValue()==1 && (pInvestigacionMalaria.getMedicamentosAutomedicacion()==null || pInvestigacionMalaria.getMedicamentosAutomedicacion().trim().equals(""))){
			oResultado.setMensaje("Si la persona se automedic�, debe ingresar la descripci�n de los medicamentos utilizados en la automedicaci�n");
			oResultado.setOk(false);
			return oResultado;
		}
		
		//Validando datos de investigaci�n asociados a investigaci�n de medicamentos
		if(pInvestigacionMedicamento!=null){
			for(int i=0;oResultado.isOk()==false || pInvestigacionMedicamento.size() <=i;i++){
				oResultado = validarInvestigacionMedicamento(pInvestigacionMedicamento.get(0));
			}
			if(oResultado.isOk()==false) return oResultado;
		}
		
		//Validando datos de investigaci�n asociados a investigaci�n malaria
		if(pInvestigacionMalaria.getSintomatico().intValue()==1 && pInvestigacionSintoma==null){
			oResultado.setOk(false);
			oResultado.setMensaje("Si la persona es sintom�tica debe definir la informaci�n relacionada a investigaci�n de sintomas");
			return oResultado;
		}else if(pInvestigacionMalaria.getSintomatico().intValue()==1 && pInvestigacionSintoma!=null){
			oResultado = validarInvestigacionSintoma(pInvestigacionSintoma);
			if(oResultado.isOk()==false) return oResultado;
		}
		
		//Validadon datos de investigacion asociados a lugares
		if(pInvestigacionMalaria.getViajesZonaRiesgo().intValue()==1 && pInvestigacionLugar==null){
			oResultado.setOk(false);
			oResultado.setMensaje("Si declara que la persona ha realizado viajes en los �ltimos 30 d�as a zonas mal�ricas, debe definir el lugar visitado");
			return oResultado;
		}else if(pInvestigacionMalaria.getViajesZonaRiesgo().intValue()==1 && pInvestigacionLugar!=null){
			oResultado = validarInvestigacionLugar(pInvestigacionLugar);
			if(oResultado.isOk()==false) return oResultado;
		}
		
		//Validando datos de investigaci�n asociados a investigaci�n de transfusiones
		if(pInvestigacionMalaria.getTransfusion().intValue()==0 && pInvestigacionTransfusion==null){
			oResultado.setOk(false);
			oResultado.setMensaje("Si la persona ha recibido una transfusi�n debe definir la informaci�n relacionada a investigaci�n de transfusi�n");
			return oResultado;
		}else if(pInvestigacionMalaria.getTransfusion().intValue()==1 && pInvestigacionTransfusion!=null){
			oResultado = validarInvestigacionTransfusion(pInvestigacionTransfusion);
			if(oResultado.isOk()==false) return oResultado;
		}
		if(pInvestigacionMalaria.getManejoClinico().intValue()==1 && pInvestigacionTransfusion==null){
			oResultado.setOk(false);
			oResultado.setMensaje("Si se indica que hubo manejo cl�nico terap�utico, se debe definir la informaci�n relacionada a investigaci�n hospitalaria");
			return oResultado;
		}else if(pInvestigacionMalaria.getManejoClinico().intValue()==1 && pInvestigacionTransfusion==null){
			oResultado = validarInvestigacionHospitalario(pInvestigacionHospitalario);
			if(oResultado.isOk()==false) return oResultado;
		}
		
		//Validando datos vinculados a Investigaci�n hospitalario
		if(pInvestigacionHospitalario!=null){
			if(pInvestigacionHospitalario.getFechaIngreso()!=null){
				if(pInvestigacionMalaria.getFechaDefuncion().after(pInvestigacionHospitalario.getFechaIngreso())){
					oResultado.setMensaje("La fecha de defunci�n no puede ser posterior a la fecha de ingreso a hospitalizaci�n");
					oResultado.setOk(false);
					return oResultado;
				}
			}
		}
		//Validando datos vinculados a Investigaci�n sintomas
		if(pInvestigacionSintoma!=null){
			if(pInvestigacionSintoma.getFechaInicioSintomas()!=null){
				if(pInvestigacionMalaria.getFechaDefuncion().after(pInvestigacionSintoma.getFechaInicioSintomas())){
					oResultado.setMensaje("La fecha de defunci�n no puede ser posterior a la fecha de inicio de sintomas");
					oResultado.setOk(false);
					return oResultado;
				}
			}
			if(pInvestigacionSintoma.getFechaInicioSintomas()!=null){
				if(pInvestigacionSintoma.getInvestigacionMalaria().getInicioTratamiento().before(pInvestigacionSintoma.getFechaInicioSintomas())){
					oResultado.setOk(false);
					oResultado.setMensaje("La fecha de inicio del tratamiento no puede ser inferior a la fecha de inicio de los s�ntomas");
					return oResultado;
				}
			}
		}
		
		if(pInvestigacionMalaria.getClasificacionClinica()==null || pInvestigacionMalaria.getClasificacionClinica().getCatalogoId() == 0){
			oResultado.setOk(false);
			oResultado.setMensaje("La clasificaci�n cl�nica del caso mal�rico es requerida");
			return oResultado;
		}
		
		if(pInvestigacionMalaria.getTipoComplicacion()==null || pInvestigacionMalaria.getTipoComplicacion().getCatalogoId() == 0){
			oResultado.setOk(false);
			oResultado.setMensaje("El tipo de complicaci�n del caso mal�rico es requerido");
			return oResultado;
		}
		
		if(pInvestigacionMalaria.getTipoRecurrencia()==null || pInvestigacionMalaria.getTipoRecurrencia().getCatalogoId() == 0){
			oResultado.setOk(false);
			oResultado.setMensaje("El tipo de infecci�n del caso mal�rico es requerido");
			return oResultado;
		}
		
		if(pInvestigacionMalaria.getClasificacionCaso()==null || pInvestigacionMalaria.getClasificacionCaso().getCatalogoId() == 0){
			oResultado.setOk(false);
			oResultado.setMensaje("La clasificaci�n seg�n el origen de la infecci�n es requerida");
			return oResultado;
		}

		if(pInvestigacionMalaria.getResponsable()==null||pInvestigacionMalaria.getResponsable().equals("")){
			oResultado.setOk(false);
			oResultado.setMensaje("Debe indicar el nombre de la persona que efectu� la investigaci�n");
			return oResultado;
		}
		
		if(pInvestigacionMalaria.getResponsable()==null||pInvestigacionMalaria.getResponsable().equals("")){
			oResultado.setOk(false);
			oResultado.setMensaje("Debe indicar el nombre del epidemi�logo que particip� en la investigaci�n");
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
			oResultado.setMensaje("La fecha de transfusi�n no puede ser posterior a la fecha actual");
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
				oResultado.setMensaje("La fecha de defunci�n no puede ser posterior a la fecha de ingreso a hospitalizaci�n");
				oResultado.setOk(false);
				return oResultado;
			}
		}
		
		if(pInvestigacionHospitalario.getUnidad()==null){
			oResultado.setMensaje("La unidad de hospitalizaci�n es requerida");
			oResultado.setOk(false);
			return oResultado;
		}
		
		if(pInvestigacionHospitalario.getMunicipio()==null){
			oResultado.setMensaje("El municipio en el que se encuentra la unidad de hospitalizaci�n es requerida");
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
			oResultado.setMensaje("Si la infeccion no ocurre en el lugar de residencia de la persona, debe indicar el pa�s donde se produjo la infecci�n");
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
			oResultado.setMensaje("Si el paciente es sintom�tico debe indicar la fecha de inicio de s�ntomas");
			return oResultado;
		}
		
		if(pInvestigacionSintoma.getFechaInicioSintomas()!=null){
			if(pInvestigacionSintoma.getInvestigacionMalaria().getInicioTratamiento().before(pInvestigacionSintoma.getFechaInicioSintomas())){
				oResultado.setOk(false);
				oResultado.setMensaje("La fecha de inicio del tratamiento no puede ser inferior a la fecha de inicio de los s�ntomas");
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
			oResultado.setMensaje("Debe indicar el medicamento antimal�rico utilizado");
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
			oResultado.setMensaje("Debe indicar el pa�s en el cual se presentaron s�ntomas ");
			return oResultado;
		}
		if(pLugarAnte.getPais().getCodigoAlfados().equals(Utilidades.PAIS_CODIGO) && 
				(pLugarAnte.getMunicipio()==null || pLugarAnte.getMunicipio().getDivisionPoliticaId() < 1)){
			oResultado.setOk(false);
			oResultado.setMensaje("Si el lugar donde inician los s�ntomas es nacional, debe indicarse el municipio");
			return oResultado;
		}
		if(pLugarAnte.getFechaUltima()==null){
			oResultado.setOk(false);
			oResultado.setMensaje("Debe indicar la fecha de �ltimo d�a de estad�a en el lugar visitado");
			return oResultado;
		}
		
		if(pLugarAnte.getFechaUltima().after(new Date())){
			oResultado.setMensaje("la fecha de �ltimo d�a de estad�a no puede ser posterior a la fecha actual");
			oResultado.setOk(false);
			return oResultado;
		}
		if(pLugarAnte.getEstadia()==null){
			oResultado.setOk(false);
			oResultado.setMensaje("Debe indicar el n�mero de d�as de permanencia en el lugar visitado");
			return oResultado;
		}
		if(pLugarAnte.getPersonasSintomas()==null){
			oResultado.setOk(false);
			oResultado.setMensaje("Debe indicar si se encontr� con personas con malaria o fiebre en el lugar referido");
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
			oResultado.setMensaje("Debe indicar el pa�s en el cual se presentaron s�ntomas ");
			return oResultado;
		}
		if(pLugarOtro.getPais().getCodigoAlfados().equals(Utilidades.PAIS_CODIGO) && 
				(pLugarOtro.getMunicipio()==null || pLugarOtro.getMunicipio().getDivisionPoliticaId() < 1)){
			oResultado.setOk(false);
			oResultado.setMensaje("Si el lugar donde inician los s�ntomas es nacional, debe indicarse el municipio");
			return oResultado;
		}
		if(pLugarOtro.getMesInicio()==null || (pLugarOtro.getMesInicio().intValue() ==0)){
			oResultado.setOk(false);
			oResultado.setMensaje("Debe indicar el mes de inicio de los s�ntomas");
			return oResultado;
		}
		if(pLugarOtro.getA�oInicio()==null || (pLugarOtro.getA�oInicio().intValue() == 0)){
			oResultado.setOk(false);
			oResultado.setMensaje("Debe indicar el a�o de inicio de los s�ntomas");
			return oResultado;
		}
		if(pLugarOtro.getEstadia()==null){
			oResultado.setOk(false);
			oResultado.setMensaje("Debe indicar el n�mero de d�as de permanencia en el lugar visitado");
			return oResultado;
		}
		if(pLugarOtro.getDiagnosticoPositivo()==null){
			oResultado.setOk(false);
			oResultado.setMensaje("Debe indicar si existi� un diagn�stico positivo de malaria");
			return oResultado;
		}
		if(pLugarOtro.getAutomedicacion()==null){
			oResultado.setOk(false);
			oResultado.setMensaje("Debe indicar si la persona se automedic� o no");
			return oResultado;
		}
		if(pLugarOtro.getTratamientoCompleto()==null){
			oResultado.setOk(false);
			oResultado.setMensaje("Debe indicar si la persona complet� el tratamiento antimal�rico");
			return oResultado;
		}
		oResultado.setOk(true);
		return oResultado;
	}
}
