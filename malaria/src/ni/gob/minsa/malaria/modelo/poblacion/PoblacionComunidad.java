// -----------------------------------------------
// PoblacionComunidad.java
// -----------------------------------------------
package ni.gob.minsa.malaria.modelo.poblacion;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.*;

import org.eclipse.persistence.annotations.Cache;

/**
 * Clase de persistencia asociada a la tabla POBLACION_COMUNIDADES 
 * en el esquema GENERAL.
 * 
 * @author Marlon Arr�liga
 * @author <a href=mailto:marrolig@hotmail.com>marrolig@hotmail.com</a>
 * @version 1.0, &nbsp; 09/05/2012
 * @since jdk1.6.0_21
 */
@Entity
@Table(name="POBLACION_COMUNIDADES",schema="GENERAL",
			uniqueConstraints=@UniqueConstraint(columnNames={"A�O","COMUNIDAD"}))
@Cache(alwaysRefresh=true,disableHits=true)
@NamedQueries({
	@NamedQuery(
			name="poblacionComunidadesPorSector",
			query="select new ni.gob.minsa.malaria.modelo.poblacion.noEntidad.PoblacionAnualComunidad(" +
					"	pc.a�o, pc.poblacionComunidadId, pc.comunidad.comunidadId, pc.comunidad.codigo, " +
					"	pc.comunidad.nombre, pc.comunidad.tipoArea, pc.hogares, pc.manzanas, " +
					"	pc.poblacion, pc.viviendas) from PoblacionComunidad pc " +
					"where pc.comunidad.sector.codigo=:pSector and " +
					"      pc.a�o=:pA�o " +
					"order by pc.comunidad.codigo"),
	@NamedQuery(
		name="poblacionAnualPorSector",
		query="select new ni.gob.minsa.malaria.modelo.poblacion.noEntidad.PoblacionAnualSector(p.a�o, s.sectorId, s.codigo, s.nombre, " +
											  "SIZE(s.comunidades), " +
											  "SUM(p.hogares), " +
											  "SUM(p.manzanas), " +
											  "SUM(p.poblacion), " +
											  "SUM(p.viviendas)) " +
					"from Sector s JOIN s.comunidades c JOIN c.poblaciones p " +
						"where p.a�o=:pA�o and s.unidad.codigo=:pUnidad " +
						"group by p.a�o, s.sectorId, s.codigo, s.nombre " +
						"order by s.codigo"),
	@NamedQuery(
		name="resumenPoblacionSector",
		query="select new ni.gob.minsa.malaria.modelo.poblacion.noEntidad.ResumenPoblacion(c.tipoArea, " +
										  "COUNT(c), " +
										  "SUM(p.hogares), " +
										  "SUM(p.manzanas), " +
										  "SUM(p.poblacion), " +
										  "SUM(p.viviendas)) " +
	                 "from Sector s JOIN s.comunidades c JOIN c.poblaciones p " +
					 	"where p.a�o=:pA�o and s.unidad.codigo=:pUnidad " +
					 	"group by c.tipoArea " +
						"order by c.tipoArea"),
	@NamedQuery(
			name="resumenPoblacionComunidad",
			query="select new ni.gob.minsa.malaria.modelo.poblacion.noEntidad.ResumenPoblacion(c.tipoArea, " +
											  "COUNT(c), " +
											  "SUM(p.hogares), " +
											  "SUM(p.manzanas), " +
											  "SUM(p.poblacion), " +
											  "SUM(p.viviendas)) " +
					  "from Comunidad c JOIN c.poblaciones p " +
					  	"where p.a�o=:pA�o and c.sector.codigo=:pSector " +
					  	"group by c.tipoArea " +
						"order by c.tipoArea")
})
public class PoblacionComunidad implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="POBLACION_COMUNIDADES_POBLACIONCOMUNIDADID_GENERATOR", sequenceName="GENERAL.POBCOMS_SEQ",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="POBLACION_COMUNIDADES_POBLACIONCOMUNIDADID_GENERATOR")
	@Column(name="POBLACION_COMUNIDAD_ID")
	private long poblacionComunidadId;

	@Column(nullable=false)
	private BigDecimal a�o;

    @ManyToOne(targetEntity=Comunidad.class,fetch=FetchType.LAZY)
	@JoinColumn(name="COMUNIDAD",referencedColumnName="CODIGO",nullable=false)
	private Comunidad comunidad;

    private BigDecimal hogares;

	private BigDecimal manzanas;

	@Column(nullable=false)
	private BigDecimal poblacion;

	private BigDecimal viviendas;
	
	@SuppressWarnings("unused")
	@Transient
	private boolean valido;
	
    public PoblacionComunidad() {
    }

	public PoblacionComunidad(long poblacionComunidadId, BigDecimal a�o,
			Comunidad comunidad, BigDecimal hogares, BigDecimal manzanas,
			BigDecimal poblacion, BigDecimal viviendas) {
		super();
		this.poblacionComunidadId = poblacionComunidadId;
		this.a�o = a�o;
		this.comunidad = comunidad;
		this.hogares = hogares;
		this.manzanas = manzanas;
		this.poblacion = poblacion;
		this.viviendas = viviendas;
	}

	public long getPoblacionComunidadId() {
		return this.poblacionComunidadId;
	}

	public void setPoblacionComunidadId(long poblacionComunidadId) {
		this.poblacionComunidadId = poblacionComunidadId;
	}

	public BigDecimal getA�o() {
		return this.a�o;
	}

	public void setA�o(BigDecimal a�o) {
		this.a�o = a�o;
	}

	public BigDecimal getHogares() {
		return this.hogares;
	}

	public void setHogares(BigDecimal hogares) {
		this.hogares = hogares;
	}

	public BigDecimal getManzanas() {
		return this.manzanas;
	}

	public void setManzanas(BigDecimal manzanas) {
		this.manzanas = manzanas;
	}

	public BigDecimal getPoblacion() {
		return this.poblacion;
	}

	public void setPoblacion(BigDecimal poblacion) {
		this.poblacion = poblacion;
	}

	public BigDecimal getViviendas() {
		return this.viviendas;
	}

	public void setViviendas(BigDecimal viviendas) {
		this.viviendas = viviendas;
	}

	public void setComunidad(Comunidad comunidad) {
		this.comunidad = comunidad;
	}

	public Comunidad getComunidad() {
		return comunidad;
	}
	
	public boolean isValido() {
		
		boolean iValido=true;
		if ((this.poblacion==null) || this.poblacion.intValue()==0) {
			if (this.hogares!=null && this.hogares.intValue()>0) iValido=false;
			if (iValido && this.viviendas!=null && this.viviendas.intValue()>0) iValido=false;
			if (iValido && this.manzanas!=null && this.manzanas.intValue()>0) iValido=false;
		}

		return iValido;
	}

}