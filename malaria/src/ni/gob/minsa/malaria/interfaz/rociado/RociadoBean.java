package ni.gob.minsa.malaria.interfaz.rociado;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import ni.gob.minsa.malaria.datos.estructura.EntidadAdtvaDA;
import ni.gob.minsa.malaria.datos.general.CatalogoElementoDA;
import ni.gob.minsa.malaria.datos.poblacion.ComunidadDA;
import ni.gob.minsa.malaria.datos.poblacion.DivisionPoliticaDA;
import ni.gob.minsa.malaria.datos.poblacion.SectorDA;
import ni.gob.minsa.malaria.datos.rociado.RociadoChkListDA;
import ni.gob.minsa.malaria.datos.rociado.RociadoDA;
import ni.gob.minsa.malaria.modelo.encuesta.AbundanciaFauna;
import ni.gob.minsa.malaria.modelo.estructura.EntidadAdtva;
import ni.gob.minsa.malaria.modelo.poblacion.Comunidad;
import ni.gob.minsa.malaria.modelo.poblacion.DivisionPolitica;
import ni.gob.minsa.malaria.modelo.rociado.ChecklistMalaria;
import ni.gob.minsa.malaria.modelo.rociado.EquiposMalaria;
import ni.gob.minsa.malaria.modelo.rociado.InsecticidasMalaria;
import ni.gob.minsa.malaria.servicios.estructura.EntidadAdtvaService;
import ni.gob.minsa.malaria.servicios.general.CatalogoElementoService;
import ni.gob.minsa.malaria.servicios.poblacion.ComunidadService;
import ni.gob.minsa.malaria.servicios.poblacion.DivisionPoliticaService;
import ni.gob.minsa.malaria.servicios.poblacion.SectorService;
import ni.gob.minsa.malaria.servicios.rociado.RociadoChkListServices;
import ni.gob.minsa.malaria.servicios.rociado.RociadoServices;

@ManagedBean
@ViewScoped
public class RociadoBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4250206745795866734L;

	
	private static RociadoServices srvRociado = new RociadoDA();
	private static RociadoChkListServices srvChkList = new RociadoChkListDA();
	private static EntidadAdtvaService srvSilais = new EntidadAdtvaDA();
	private static DivisionPoliticaService srvMunicipio = new DivisionPoliticaDA();
	private static SectorService srvSector = new SectorDA();
	private static ComunidadService srvComunidad = new ComunidadDA();
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static CatalogoElementoService<ChecklistMalaria,Integer> srvItemsListaCat = new CatalogoElementoDA(ChecklistMalaria.class,"ChecklistMalaria");
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static CatalogoElementoService<EquiposMalaria,Integer> srvEquiposCat = new CatalogoElementoDA(EquiposMalaria.class,"EquiposMalaria");
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static CatalogoElementoService<InsecticidasMalaria,Integer> srvInsecticidaCat = new CatalogoElementoDA(InsecticidasMalaria.class,"InsecticidasMalaria");
	
	private Short frmInp_Control;
	
	private Long frmSom_Silais;
	private String frmSom_Municipio;
	private String codSector;
	private Comunidad frmInp_Comunidad;
	
	private Long frmSom_Equipo;
	private Long frmInp_Carga;
	private String frmInp_Boquilla;
	private Long frmInp_Insecticida;
	private Long frmInp_Formula;
	private Short frmInp_Ciclo;
	private Date frmDate_Fecha;
	
	private Integer frmInp_viProgramadas;
	private Short frmInp_VidesAdecuado;
	private Short frmInp_VidesNoAdecuado;
	private Short frmInp_ViRociadas;
	
	private Short frmInp_ViNoRxRenuente;
	private Short frmInp_ViNoRxCerradas;
	private Short frmInp_ViNoRxConstruccion;
	private Short frmInp_ViNoRxEnfermos;
	private Short frmInp_ViNoRxOtros;
	
	private Short frmInp_HabRociadas;
	private Short frmInp_HabNoRociadas;
	
	private String frmInp_Rociador;
	
	private List<Long> frmSom_CheckList;
	
	private List<ChecklistMalaria> itemsCheckList;
	private List<EquiposMalaria> itemsEquipos;
	private List<EntidadAdtva> itemsSilais;
	private List<DivisionPolitica> itemsMunicipios;
	
	
	
	
	
}
