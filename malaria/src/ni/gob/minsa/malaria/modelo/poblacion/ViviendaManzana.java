// -----------------------------------------------
// ViviendaManzana.java
// -----------------------------------------------
package ni.gob.minsa.malaria.modelo.poblacion;

import java.io.Serializable;

import javax.persistence.*;

import org.eclipse.persistence.annotations.Cache;

/**
 * Clase de persistencia asociada a la tabla VIVIENDAS_MANZANAS 
 * en el esquema GENERAL.
 * 
 * @author Marlon Arróliga
 * @author <a href=mailto:marrolig@hotmail.com>marrolig@hotmail.com</a>
 * @version 1.0, &nbsp; 25/05/2012
 * @since jdk1.6.0_21
 */
@Entity
@Table(name="VIVIENDAS_MANZANAS",schema="GENERAL",
			uniqueConstraints=@UniqueConstraint(columnNames={"VIVIENDA","MANZANA"}))
@Cache(alwaysRefresh=true,disableHits=true)
public class ViviendaManzana implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="VIVMANZANAS_VIVMANZANAID_GENERATOR", sequenceName="GENERAL.VIVMANZANAS_SEQ",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="VIVMANZANAS_VIVMANZANAID_GENERATOR")
	@Column(name="VIVIENDA_MANZANA_ID",updatable=false,nullable=false)
	private long viviendaManzanaId;

	@OneToOne(targetEntity=Vivienda.class,fetch=FetchType.LAZY,optional=true,cascade=CascadeType.PERSIST)
	@JoinColumn(name="VIVIENDA",referencedColumnName="CODIGO",nullable=false,updatable=false)
	private Vivienda vivienda;

    @ManyToOne(targetEntity=Manzana.class,fetch=FetchType.LAZY,optional=true)
	@JoinColumn(name="MANZANA",referencedColumnName="CODIGO",nullable=false,updatable=false)
	private Manzana manzana;
	
    public ViviendaManzana() {
    }

	public void setViviendaManzanaId(long viviendaManzanaId) {
		this.viviendaManzanaId = viviendaManzanaId;
	}

	public long getViviendaManzanaId() {
		return viviendaManzanaId;
	}

	public void setVivienda(Vivienda vivienda) {
		this.vivienda = vivienda;
	}

	public Vivienda getVivienda() {
		return vivienda;
	}

	public void setManzana(Manzana manzana) {
		this.manzana = manzana;
	}

	public Manzana getManzana() {
		return manzana;
	}

}