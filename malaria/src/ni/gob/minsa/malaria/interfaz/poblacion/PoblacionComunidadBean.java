// -----------------------------------------------
// PoblacionComunidadBean.java
// -----------------------------------------------

package ni.gob.minsa.malaria.interfaz.poblacion;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;

import ni.gob.minsa.ciportal.dto.InfoResultado;
import ni.gob.minsa.ciportal.dto.InfoSesion;
import ni.gob.minsa.malaria.datos.estructura.UnidadDA;
import ni.gob.minsa.malaria.datos.poblacion.ComunidadDA;
import ni.gob.minsa.malaria.datos.poblacion.ControlUnidadPoblacionDA;
import ni.gob.minsa.malaria.datos.poblacion.PoblacionComunidadDA;
import ni.gob.minsa.malaria.modelo.estructura.EntidadAdtva;
import ni.gob.minsa.malaria.modelo.estructura.Unidad;
import ni.gob.minsa.malaria.modelo.poblacion.Comunidad;
import ni.gob.minsa.malaria.modelo.poblacion.ControlUnidadPoblacion;
import ni.gob.minsa.malaria.modelo.poblacion.PoblacionComunidad;
import ni.gob.minsa.malaria.modelo.poblacion.noEntidad.PoblacionAnualComunidad;
import ni.gob.minsa.malaria.modelo.poblacion.noEntidad.PoblacionAnualSector;
import ni.gob.minsa.malaria.modelo.poblacion.noEntidad.ResumenPoblacion;
import ni.gob.minsa.malaria.servicios.estructura.UnidadService;
import ni.gob.minsa.malaria.servicios.poblacion.ComunidadService;
import ni.gob.minsa.malaria.servicios.poblacion.ControlUnidadPoblacionService;
import ni.gob.minsa.malaria.servicios.poblacion.PoblacionComunidadService;
import ni.gob.minsa.malaria.soporte.Mensajes;
import ni.gob.minsa.malaria.soporte.Utilidades;

/**
 * Servicio para la capa de presentación de la página 
 * poblacion/poblacionComunidad.xhtml
 *
 * <p>
 * @author Marlon Arróliga
 * @author <a href=mailto:marrolig@hotmail.com>marrolig@hotmail.com</a>
 * @version 1.0, &nbsp; 08/05/2012
 * @since jdk1.6.0_21
 */
@ManagedBean
@ViewScoped
public class PoblacionComunidadBean implements Serializable {

	private static final long serialVersionUID = 1L;

	protected InfoSesion infoSesion;
	
	// objetos para poblar el combo de entidades administrativas
	private List<EntidadAdtva> entidades;
	
	// identificador de la entidad seleccionada
	private long entidadSelectedId;
	
	// objetos para poblar el combo de unidades de salud, unidad seleccionada e identificador
	private List<Unidad> unidades;
	private Unidad unidadSelected;
	private long unidadSelectedId;

	// mapeo para poblar el combo de años
	private Map<Integer,Integer> años = new HashMap<Integer,Integer>();
	// año seleccionado
	private Integer añoSelected;
	private Integer añoActual;

	// sector seleccionado 
	private String nombreSectorSelected;

	// objetos para poblar la grilla de población de sectores
	private List<PoblacionAnualSector> poblacionAnualSectores;
	private PoblacionAnualSector poblacionAnualSectorSelected;
	
	// objeto para poblar la grilla resumen del sector
	private List<ResumenPoblacion> resumenPoblacionSectores;
	private ResumenPoblacion totalPoblacionSectores;
	
	// objetos para poblar la grilla de población de comunidades
	private List<PoblacionAnualComunidad> poblacionAnualComunidades;
	
	private List<ResumenPoblacion> resumenPoblacionComunidades;
	private ResumenPoblacion totalPoblacionComunidades;

	private boolean plan;
	
	// capa=1 mostrará la grilla de sectores
	// capa=2 mostrará la grilla de comunidades
	private int capa;
	
	// notificación de validación correcta o incorrecta al guardar
	private boolean validacionOK;
	// utilizada para control de interfaz
	private boolean existenDatosPoblacion;
	private boolean cambiosPendientes;
	
	private static PoblacionComunidadService poblacionComunidadService=new PoblacionComunidadDA();
	private static ControlUnidadPoblacionService controlUnidadPoblacionService = new ControlUnidadPoblacionDA();
	private static UnidadService unidadService = new UnidadDA();
	private static ComunidadService comunidadService = new ComunidadDA();
	
	// --------------------------------------- Constructor
	
	public PoblacionComunidadBean() {

		this.infoSesion=Utilidades.obtenerInfoSesion();
		this.entidades= new ArrayList<EntidadAdtva>();
		this.unidades= new ArrayList<Unidad>();
		
		this.poblacionAnualSectores = new ArrayList<PoblacionAnualSector>();
		this.poblacionAnualComunidades = new ArrayList<PoblacionAnualComunidad>();
		this.resumenPoblacionSectores = new ArrayList<ResumenPoblacion>();
		this.resumenPoblacionComunidades = new ArrayList<ResumenPoblacion>();
		
		this.entidadSelectedId=0;
		this.unidadSelectedId=0;
		
		// obtiene los datos para el combo de años: año actual, año siguiente y 5 años atrás

		this.años=Utilidades.ObtenerAños(Utilidades.AñoActual().intValue()-5,true);
		this.añoActual=Utilidades.AñoActual();
		this.añoSelected=this.añoActual;
		
		// obtiene los datos para el combo de entidades autorizadas
		// únicamente se podrán seleccionar aquellas entidades administrativas
		// asociadas a las unidades de salud con autorización explícita
		
		this.entidades=ni.gob.minsa.malaria.reglas.Operacion.entidadesAutorizadas(this.infoSesion.getUsuarioId(),false);
		if ((this.entidades!=null) && (this.entidades.size()>0)) {
			this.entidadSelectedId=this.entidades.get(0).getEntidadAdtvaId();
		}

		// si se ha encontrado una entidad administrativa, se seleccionará por omisión
		// la primera unidad de salud asociada a dicha unidad de salud
		if (this.entidadSelectedId!=0) {
			obtenerUnidades();
		}

	}
	
	// ------------------------------------------- Métodos
	
	/**
	 * Inicializa las propiedades a un estado en el cual no se encuentra
	 * ningún sector seleccionado.
	 */
	protected void iniciarPrimerCapa() {
		
		this.capa=1;
		this.validacionOK=true;
		obtenerPoblacionSectores();
	
	}

	protected void iniciarSegundaCapa() {
		
		if (this.poblacionAnualSectorSelected==null) return;
		this.capa=2;
		this.validacionOK=true;

		this.nombreSectorSelected=this.poblacionAnualSectorSelected.getCodigo() + " - " +this.poblacionAnualSectorSelected.getNombre(); 
		obtenerPoblacionComunidades();
		
	}

	/**
	 * Evento llamado al efectuar un cambio de unidad.  
	 * 
	 * Si aún no se han confirmado los datos, se mostrarán todos los sectores
	 * activos, vinculados a comunidades que tengan o no datos de población.  
	 * Si los datos ya han sido confirmados, únicamente se mostrarán aquellos 
	 * sectores vinculados a comunidades que posean datos de población.  
	 * En este caso, el resumen de datos de población únicamente contabilizará 
	 * las comunidades con datos poblacionales.
	 */
	public void obtenerPoblacionSectores() {

		this.poblacionAnualSectorSelected=null;
		this.poblacionAnualSectores.clear();
		this.resumenPoblacionSectores.clear();
		
		verificarPlan();
		
		this.poblacionAnualSectores=poblacionComunidadService.PoblacionSectoresPorUnidad(this.unidadSelected.getCodigo(), this.añoSelected, this.plan);
		this.resumenPoblacionSectores=poblacionComunidadService.ResumenPoblacionSectoresPorUnidad(this.unidadSelected.getCodigo(), this.añoSelected, this.plan);
		
		Collections.sort(this.resumenPoblacionSectores, ResumenPoblacion.ORDEN_TIPO_AREA);
		
		this.totalPoblacionSectores=new ResumenPoblacion();
   	 	for (int i=0;i<this.resumenPoblacionSectores.size();i++) {
   	 		ResumenPoblacion oResumenPoblacion = this.resumenPoblacionSectores.get(i);
   	 		if (oResumenPoblacion.getComunidades()!=null) {
   	 			this.totalPoblacionSectores.setComunidades(this.totalPoblacionSectores.getComunidades()==null?oResumenPoblacion.getComunidades():this.totalPoblacionSectores.getComunidades()+oResumenPoblacion.getComunidades());
   	 		}
   	 		if (oResumenPoblacion.getHogares()!=null) {
   	 			this.totalPoblacionSectores.setHogares(this.totalPoblacionSectores.getHogares()==null?oResumenPoblacion.getHogares():this.totalPoblacionSectores.getHogares().add(oResumenPoblacion.getHogares()));
   	 		}
   	 		if (oResumenPoblacion.getManzanas()!=null) {
   	 		this.totalPoblacionSectores.setManzanas(this.totalPoblacionSectores.getManzanas()==null?oResumenPoblacion.getManzanas():this.totalPoblacionSectores.getManzanas().add(oResumenPoblacion.getManzanas()));
   	 		}
   	 		if (oResumenPoblacion.getViviendas()!=null) {
   	 			this.totalPoblacionSectores.setViviendas(this.totalPoblacionSectores.getViviendas()==null?oResumenPoblacion.getViviendas():this.totalPoblacionSectores.getViviendas().add(oResumenPoblacion.getViviendas()));
   	 		}
   	 		if (oResumenPoblacion.getPoblacion()!=null) {
   	 			this.totalPoblacionSectores.setPoblacion(this.totalPoblacionSectores.getPoblacion()==null?oResumenPoblacion.getPoblacion():this.totalPoblacionSectores.getPoblacion().add(oResumenPoblacion.getPoblacion()));
   	 		}
   	 	}
   	 	
   	 	this.existenDatosPoblacion=false;
   	 	if (this.totalPoblacionSectores.getPoblacion()!=null && this.totalPoblacionSectores.getPoblacion().compareTo(new BigDecimal(0))==1) this.existenDatosPoblacion=true; 

	}

	public void cambiarAño() {
		
		if ((this.entidadSelectedId==0)||(this.unidadSelectedId==0)) return;
		
		if (this.capa==1) {
			// en este proceso se verifica si los datos ya han sido confirmados
			obtenerPoblacionSectores();
		} else {
			// se debe verificar el plan cuando se encuentra la capa 2 activa y
			// se genera la población de las comunidades sin pasar por el sector
			verificarPlan();
			obtenerPoblacionComunidades();
		}
	}

	public void verificarPlan() {

		// si existe el registro, ya se ha efectuado la confirmación de la población cubierta
		// por la unidad de salud
		this.plan=true;
		if (controlUnidadPoblacionService.ControlPoblacionPorUnidad(this.añoSelected, this.unidadSelected.getCodigo())!=null) {
			this.plan=false;
		}
	}

	public void cambiarUnidad() {
		
		InfoResultado oResultado=unidadService.Encontrar(this.unidadSelectedId);
		if (!oResultado.isOk()) {
			FacesContext.getCurrentInstance().addMessage(null, Mensajes.enviarMensaje(oResultado));
			return;
		}

		Unidad oUnidad=(Unidad)oResultado.getObjeto();
		this.unidadSelected=oUnidad;

		obtenerPoblacionSectores();
		
	}
	
	/**
	 * Evento llamado al seleccionar un sector de la grilla de poblaciones.  
	 * 
	 * Si aún no se han confirmado los datos, se mostrarán todas las comunidades 
	 * activas, tengan o no datos de población.  
	 * Si los datos ya han sido confirmados, únicamente se mostrarán aquellas 
	 * comunidades que poseen datos de población.  En este caso, el resumen de
	 * datos de población únicamente contabilizará las comunidades con datos
	 * poblacionales.
	 */
	public void obtenerPoblacionComunidades() {

		// si se ha producido un error de validación de al menos un registro
		// se mantendrá la misma información
		if (this.validacionOK) {
			this.cambiosPendientes=false;
			this.poblacionAnualComunidades.clear();			
			this.poblacionAnualComunidades=poblacionComunidadService.PoblacionComunidadesPorSector(this.poblacionAnualSectorSelected.getCodigo(), this.añoSelected, this.plan);
		}
		this.resumenPoblacionComunidades.clear();
		this.resumenPoblacionComunidades=poblacionComunidadService.ResumenPoblacionComunidadesPorSector(this.poblacionAnualSectorSelected.getCodigo(), this.añoSelected, this.plan);

		Collections.sort(this.resumenPoblacionComunidades, ResumenPoblacion.ORDEN_TIPO_AREA);
		
		this.totalPoblacionComunidades=new ResumenPoblacion();
   	 	for (int i=0;i<this.resumenPoblacionComunidades.size();i++) {
   	 		ResumenPoblacion oResumenPoblacion = this.resumenPoblacionComunidades.get(i);
   	 		if (oResumenPoblacion.getComunidades()!=null) {
   	 			this.totalPoblacionComunidades.setComunidades(this.totalPoblacionComunidades.getComunidades()==null?oResumenPoblacion.getComunidades():this.totalPoblacionComunidades.getComunidades()+oResumenPoblacion.getComunidades());
   	 		}
   	 		if (oResumenPoblacion.getHogares()!=null) {
   	 			this.totalPoblacionComunidades.setHogares(this.totalPoblacionComunidades.getHogares()==null?oResumenPoblacion.getHogares():this.totalPoblacionComunidades.getHogares().add(oResumenPoblacion.getHogares()));
   	 		}
   	 		if (oResumenPoblacion.getManzanas()!=null) {
   	 		this.totalPoblacionComunidades.setManzanas(this.totalPoblacionComunidades.getManzanas()==null?oResumenPoblacion.getManzanas():this.totalPoblacionComunidades.getManzanas().add(oResumenPoblacion.getManzanas()));
   	 		}
   	 		if (oResumenPoblacion.getViviendas()!=null) {
   	 			this.totalPoblacionComunidades.setViviendas(this.totalPoblacionComunidades.getViviendas()==null?oResumenPoblacion.getViviendas():this.totalPoblacionComunidades.getViviendas().add(oResumenPoblacion.getViviendas()));
   	 		}
   	 		if (oResumenPoblacion.getPoblacion()!=null) {
   	 			this.totalPoblacionComunidades.setPoblacion(this.totalPoblacionComunidades.getPoblacion()==null?oResumenPoblacion.getPoblacion():this.totalPoblacionComunidades.getPoblacion().add(oResumenPoblacion.getPoblacion()));
   	 		}
   	 	}
		
	}

	/**
	 * Obtiene las unidades de salud con autorización explícita
	 * asociadas a una entidad administrativa (ímplicita) 
	 */
	public void obtenerUnidades() {

		this.unidadSelected=null;
		this.unidadSelectedId=0;
		
		this.unidades=ni.gob.minsa.malaria.reglas.Operacion.unidadesAutorizadasPorEntidad(this.infoSesion.getUsuarioId(), this.entidadSelectedId, 0,true,null);
		if ((this.unidades!=null) && (this.unidades.size()>0)) {
			this.unidadSelectedId=this.unidades.get(0).getUnidadId();
			this.unidadSelected=this.unidades.get(0);
			
			iniciarPrimerCapa();
		}

	}

	public void notificarCambio() {
		
		this.cambiosPendientes=true;
	}
	
	public void onPoblacionSectorSelected(SelectEvent iEvento) {

		iniciarSegundaCapa();

	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void activarPrimerCapa(ActionEvent pEvento){

		RequestContext context = RequestContext.getCurrentInstance();
		
		if (this.cambiosPendientes) {
	        context.addCallbackParam("cambiosPendientes", true);  
		} else {
			context.addCallbackParam("cambiosPendientes", false);
			String aComponentes[] = {"frmPoblacionComunidad:capa1", "frmPoblacionComunidad:capa2", "frmPoblacionComunidad:cboUnidad", "frmPoblacionComunidad:cboEntidad"};
	        Collection iComponentes = Arrays.asList(aComponentes);
			context.update(iComponentes);
			iniciarPrimerCapa();
		}
	}
	
	/**
	 * Evento ejecutado desde la confirmación del retorno
	 * a la grilla de población por sectores.  Los cambios
	 * pendientes de guardar se pierden.
	 */
	public void regresarPrimerCapa(ActionEvent pEvento) {

		this.cambiosPendientes=false;
		iniciarPrimerCapa();
	}
	
	/**
	 * Guarda la comunidad.  Si la propiedad <b>comunidad_id=0</b> se creará un
	 * nuevo registro, caso contrario, actualizará la información en la
	 * base de datos.
	 * 
	 * @param pEvento ActionEvent
	 */
	public void guardar(ActionEvent pEvento){

		boolean iGuardadoOK=true;

		this.validacionOK=true;
		
		// antes de guardar los datos, se verificará si la información
		// cumple con la regla de validación:
		// No pueden existir hogares, viviendas y manzanas si haber
		// establecido un dato de población.
		for(PoblacionAnualComunidad oPoblacionAnualComunidad:this.poblacionAnualComunidades) {

			if (!oPoblacionAnualComunidad.isValido()) {
				this.validacionOK=false;
				break;
			}
		}
		
		if (!this.validacionOK) {
			FacesContext.getCurrentInstance().addMessage(null, Mensajes.enviarMensaje(FacesMessage.SEVERITY_WARN, "Los registros no pueden ser guardados. Revise los datos y vuelva a intentarlo.  Los datos asociados a las comunidades señaladas no pueden poseer datos de hogares, manzanas o viviendas, sin poseer población.", "Incluya un dato de población superior a cero o bien, anule los datos de hogares, manzanas y/o viviendas."));
			return;
		}
		
		for(PoblacionAnualComunidad oPoblacionAnualComunidad:this.poblacionAnualComunidades) {
			
			InfoResultado oResultado=new InfoResultado();
			
			if (oPoblacionAnualComunidad.getPoblacionComunidadId()==0) { 
				
				// si el dato de población no se ha establecido o es cero, debe
				// verificarse si se ha establecido alguno de los otros datos, en cuyo
				// caso no podrá agregarse el registro por infringir la validación, i.e.
				// debe existir el dato de población para que los otros puedan ser declarados.

				// si el dato de población no se ha establecido o es cero y ninguno
				// de los otros datos se ha establecido, el registro no será agregado.
		    
				if ((oPoblacionAnualComunidad.getPoblacion()!=null) && !oPoblacionAnualComunidad.getPoblacion().equals(new BigDecimal(0))) {
					if (oPoblacionAnualComunidad.getHogares()!=null && oPoblacionAnualComunidad.getHogares().compareTo(new BigDecimal(0))==0) oPoblacionAnualComunidad.setHogares(null);
					if (oPoblacionAnualComunidad.getViviendas()!=null && oPoblacionAnualComunidad.getViviendas().compareTo(new BigDecimal(0))==0) oPoblacionAnualComunidad.setViviendas(null);
					if (oPoblacionAnualComunidad.getManzanas()!=null && oPoblacionAnualComunidad.getManzanas().compareTo(new BigDecimal(0))==0) oPoblacionAnualComunidad.setManzanas(null);
					
					PoblacionComunidad oPoblacionComunidad = new PoblacionComunidad();
					oPoblacionComunidad.setAño(oPoblacionAnualComunidad.getAño());
					
					InfoResultado oComResultado=comunidadService.Encontrar(oPoblacionAnualComunidad.getComunidadId());
					if (oComResultado.isOk()) { 
						oPoblacionComunidad.setComunidad((Comunidad)oComResultado.getObjeto());
					}
					oPoblacionComunidad.setHogares(oPoblacionAnualComunidad.getHogares());
					oPoblacionComunidad.setViviendas(oPoblacionAnualComunidad.getViviendas());
					oPoblacionComunidad.setManzanas(oPoblacionAnualComunidad.getManzanas());
					oPoblacionComunidad.setPoblacion(oPoblacionAnualComunidad.getPoblacion());
					oResultado = poblacionComunidadService.Agregar(oPoblacionComunidad);
				} 
			}
			else {				
				// elimina las poblaciones sin datos, pero si existen datos de manzanas
				// hogares y viviendas, no lo eliminará y se notificará al usuario mediante
				// icono a la derecha del código de la comunidad
				if ((oPoblacionAnualComunidad.getPoblacion()==null) || oPoblacionAnualComunidad.getPoblacion().intValue()==0) {

					oResultado=poblacionComunidadService.Eliminar(oPoblacionAnualComunidad.getPoblacionComunidadId());
				} 
				else {

					PoblacionComunidad oPoblacionComunidad = new PoblacionComunidad();
					oPoblacionComunidad.setAño(oPoblacionAnualComunidad.getAño());
					
					InfoResultado oComResultado=comunidadService.Encontrar(oPoblacionAnualComunidad.getComunidadId());
					if (oComResultado.isOk()) { 
						oPoblacionComunidad.setComunidad((Comunidad)oComResultado.getObjeto());
					}
					
					oPoblacionComunidad.setPoblacionComunidadId(oPoblacionAnualComunidad.getPoblacionComunidadId());
					oPoblacionComunidad.setPoblacion(oPoblacionAnualComunidad.getPoblacion());
					
					// poblacion no es nulo y es diferente de cero
					if (oPoblacionAnualComunidad.getHogares()!=null && oPoblacionAnualComunidad.getHogares().equals(new BigDecimal(0))) oPoblacionAnualComunidad.setHogares(null);
					if (oPoblacionAnualComunidad.getViviendas()!=null && oPoblacionAnualComunidad.getViviendas().equals(new BigDecimal(0))) oPoblacionAnualComunidad.setViviendas(null);
					if (oPoblacionAnualComunidad.getManzanas()!=null && oPoblacionAnualComunidad.getManzanas().equals(new BigDecimal(0))) oPoblacionAnualComunidad.setManzanas(null);
					
					oPoblacionComunidad.setHogares(oPoblacionAnualComunidad.getHogares());
					oPoblacionComunidad.setViviendas(oPoblacionAnualComunidad.getViviendas());
					oPoblacionComunidad.setManzanas(oPoblacionAnualComunidad.getManzanas());
					
					oResultado=poblacionComunidadService.Guardar(oPoblacionComunidad);
				}
			}
			
			if (!oResultado.isOk()) {
				iGuardadoOK=false;
				FacesMessage msg=Mensajes.enviarMensaje(oResultado);
				if (msg!=null){
					FacesContext.getCurrentInstance().addMessage(null, Mensajes.enviarMensaje(oResultado));
				}
				break;
			}
		}
		
		if (iGuardadoOK) {
			this.cambiosPendientes=false;
			obtenerPoblacionComunidades();
			FacesContext.getCurrentInstance().addMessage(null, Mensajes.enviarMensaje(FacesMessage.SEVERITY_INFO, Mensajes.REGISTRO_GUARDADO, ""));
		}

	}

	/**
	 * Elimina los datos de población para las comunidades vinculadas al
	 * sector seleccionado.
	 * 
	 * @param pEvento ActionEvent
	 */
	public void eliminar(ActionEvent pEvento){

		if (!this.existenDatosPoblacion) return;
		
		this.validacionOK=true;
		
		InfoResultado oResultado = new InfoResultado();

		oResultado=poblacionComunidadService.EliminarPorSector(this.poblacionAnualSectorSelected.getCodigo(), this.añoSelected);
		if (!oResultado.isOk()) {
			FacesMessage msg = Mensajes.enviarMensaje(oResultado);
			if (msg!=null) {
				FacesContext.getCurrentInstance().addMessage(null, msg);
			}
		} else {
			FacesContext.getCurrentInstance().addMessage(null, Mensajes.enviarMensaje(FacesMessage.SEVERITY_INFO, Mensajes.REGISTRO_ELIMINADO, "Los datos de población del año " + this.añoSelected.toString() + " de las comunidades asociadas al sector seleccionado, han sido eliminadas."));
		}
		obtenerPoblacionComunidades();
	}

	/**
	 * Cancela las modificaciones realizadas sobre los datos de población
	 * de las comunidades asociadas al sector y año seleccionado, restaurando
	 * la información de la base de datos.
	 * 
	 * @param pEvento ActionEvent
	 */
	public void cancelar(ActionEvent pEvento){
		
		this.validacionOK=true;
		obtenerPoblacionComunidades();
		
	}
	
	public void confirmar(ActionEvent pEvento) {
		
		if (!this.existenDatosPoblacion) return;
		
		InfoResultado oResultado = new InfoResultado();
		ControlUnidadPoblacion oControlUnidadPoblacion = new ControlUnidadPoblacion();
		oControlUnidadPoblacion.setAño(this.añoSelected);
		oControlUnidadPoblacion.setUnidad(this.unidadSelected);
		oControlUnidadPoblacion.setUsuarioRegistro(this.infoSesion.getUsername());
		oControlUnidadPoblacion.setFechaRegistro(Calendar.getInstance().getTime());
		
		oResultado=controlUnidadPoblacionService.Agregar(oControlUnidadPoblacion);
		if (!oResultado.isOk()) {
			FacesMessage msg = Mensajes.enviarMensaje(oResultado);
			if (msg!=null) {
				FacesContext.getCurrentInstance().addMessage(null, msg);
			}
		} else {
			FacesContext.getCurrentInstance().addMessage(null, Mensajes.enviarMensaje(FacesMessage.SEVERITY_INFO, "Los datos de población del año " + this.añoSelected.toString() + " de las comunidades asociadas a los sectores atendidos por la unidad de salud seleccionada han sido confirmados.",""));
		}
		
		obtenerPoblacionSectores();

	}

	public void setResumenPoblacionSectores(List<ResumenPoblacion> resumenPoblacionSectores) {
		this.resumenPoblacionSectores = resumenPoblacionSectores;
	}

	public List<ResumenPoblacion> getResumenPoblacionSectores() {
		return resumenPoblacionSectores;
	}

	/**
	 * @param nombreSectorSelected the nombreSectorSelected to set
	 */
	public void setNombreSectorSelected(String nombreSectorSelected) {
		this.nombreSectorSelected = nombreSectorSelected;
	}

	/**
	 * @return the nombreSectorSelected
	 */
	public String getNombreSectorSelected() {
		return nombreSectorSelected;
	}

	public List<EntidadAdtva> getEntidades() {
		return entidades;
	}

	public void setEntidades(List<EntidadAdtva> entidades) {
		this.entidades = entidades;
	}

	public long getEntidadSelectedId() {
		return entidadSelectedId;
	}

	public void setEntidadSelectedId(long entidadSelectedId) {
		this.entidadSelectedId = entidadSelectedId;
	}

	public List<Unidad> getUnidades() {
		return unidades;
	}

	public void setUnidades(List<Unidad> unidades) {
		this.unidades = unidades;
	}

	public Unidad getUnidadSelected() {
		return unidadSelected;
	}

	public void setUnidadSelected(Unidad unidadSelected) {
		this.unidadSelected = unidadSelected;
	}

	public long getUnidadSelectedId() {
		return unidadSelectedId;
	}

	public void setUnidadSelectedId(long unidadSelectedId) {
		this.unidadSelectedId = unidadSelectedId;
	}

	public Map<Integer, Integer> getAños() {
		return años;
	}

	public void setAños(Map<Integer, Integer> años) {
		this.años = años;
	}

	public Integer getAñoSelected() {
		return añoSelected;
	}

	public void setAñoSelected(Integer añoSelected) {
		this.añoSelected = añoSelected;
	}

	public Integer getAñoActual() {
		return añoActual;
	}

	public void setAñoActual(Integer añoActual) {
		this.añoActual = añoActual;
	}

	public List<PoblacionAnualSector> getPoblacionAnualSectores() {
		return poblacionAnualSectores;
	}

	public void setPoblacionAnualSectores(
			List<PoblacionAnualSector> poblacionAnualSectores) {
		this.poblacionAnualSectores = poblacionAnualSectores;
	}

	public PoblacionAnualSector getPoblacionAnualSectorSelected() {
		return poblacionAnualSectorSelected;
	}

	public void setPoblacionAnualSectorSelected(
			PoblacionAnualSector poblacionAnualSectorSelected) {
		this.poblacionAnualSectorSelected = poblacionAnualSectorSelected;
	}

	public List<ResumenPoblacion> getResumenPoblacionComunidades() {
		return resumenPoblacionComunidades;
	}

	public void setResumenPoblacionComunidades(
			List<ResumenPoblacion> resumenPoblacionComunidades) {
		this.resumenPoblacionComunidades = resumenPoblacionComunidades;
	}

	public boolean isPlan() {
		return plan;
	}

	public void setPlan(boolean plan) {
		this.plan = plan;
	}

	public int getCapa() {
		return capa;
	}

	public void setCapa(int capa) {
		this.capa = capa;
	}

	public boolean isValidacionOK() {
		return validacionOK;
	}

	public void setValidacionOK(boolean validacionOK) {
		this.validacionOK = validacionOK;
	}

	public boolean isExistenDatosPoblacion() {
		return existenDatosPoblacion;
	}

	public void setExistenDatosPoblacion(boolean existenDatosPoblacion) {
		this.existenDatosPoblacion = existenDatosPoblacion;
	}

	public void setTotalPoblacionSectores(ResumenPoblacion totalPoblacionSectores) {
		this.totalPoblacionSectores = totalPoblacionSectores;
	}

	public ResumenPoblacion getTotalPoblacionSectores() {
		return totalPoblacionSectores;
	}

	public void setPoblacionAnualComunidades(
			List<PoblacionAnualComunidad> poblacionAnualComunidades) {
		this.poblacionAnualComunidades = poblacionAnualComunidades;
	}

	public List<PoblacionAnualComunidad> getPoblacionAnualComunidades() {
		return poblacionAnualComunidades;
	}

	public void setTotalPoblacionComunidades(ResumenPoblacion totalPoblacionComunidades) {
		this.totalPoblacionComunidades = totalPoblacionComunidades;
	}

	public ResumenPoblacion getTotalPoblacionComunidades() {
		return totalPoblacionComunidades;
	}

	public void setCambiosPendientes(boolean cambiosPendientes) {
		this.cambiosPendientes = cambiosPendientes;
	}

	public boolean isCambiosPendientes() {
		return cambiosPendientes;
	}
	
}
