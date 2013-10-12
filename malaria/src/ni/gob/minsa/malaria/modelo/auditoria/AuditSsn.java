package ni.gob.minsa.malaria.modelo.auditoria;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.*;



/**
 * Clase de persistencia o entidad para la tabla
 * AUDIT_TRN
 * 
 */
@Entity
@Table(name="AUDIT_SSN",schema="GENERAL")
public class AuditSsn implements Serializable {
	
	private static final long serialVersionUID = 1L;

	public static final String UPDATE_OPERATION = "UPDATE";
    public static final String INSERT_OPERATION = "INSERT";
    public static final String DELETE_OPERATION = "DELETE";
    
	@Id
	@SequenceGenerator(name="AUDIT_SSN_AUDITSSNID_GENERATOR", sequenceName="GENERAL.AUDIT_SSN_SEQ",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="AUDIT_SSN_AUDITSSNID_GENERATOR")
	@Column(name="AUDIT_SSN_ID")
	private long auditSsnId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="FECHA",nullable=false,updatable=false)
	private Date fecha;

	@Column(name="TABLA",nullable=false,updatable=false)
	private String tabla;

	@Column(name="SENTENCIA_SQL",nullable=true,updatable=false)
	private String sentenciaSQL;
	
	@Column(name="OPERACION",nullable=false,updatable=false)
	private String operacion;
	
	@Column(name="SISTEMA",nullable=false,updatable=false)
	private String sistema;

	@Column(name="USERNAME",nullable=false,updatable=false)
	private String usuario; 
	
	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER, mappedBy = "auditSsn",targetEntity=AuditTrn.class)
	private Set<AuditTrn> columnas;


    public AuditSsn() {
    }

	public long getAuditSsnId() {
		return this.auditSsnId;
	}

	public void setAuditSsnId(long auditSsnId) {
		this.auditSsnId = auditSsnId;
	}

	public void setSistema(String sistema) {
		this.sistema = sistema;
	}

	public String getSistema() {
		return sistema;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setTabla(String tabla) {
		this.tabla = tabla;
	}

	public String getTabla() {
		return tabla;
	}

	public void setSentenciaSQL(String sentenciaSQL) {
		this.sentenciaSQL = sentenciaSQL;
	}

	public String getSentenciaSQL() {
		return sentenciaSQL;
	}

	public void setOperacion(String operacion) {
		this.operacion = operacion;
	}

	public String getOperacion() {
		return operacion;
	}
	
	public Set<AuditTrn> getColumnas()
    {
        return columnas;
    }
    public void setColumnas(Set<AuditTrn> columnas)
    {
        this.columnas = columnas;
    }

    /**
     * Establecer el hashCode al identificador de la tabla, i.e. auditSsnId
     */
    public int hashCode()
    {
        return (int)auditSsnId;
    }
    
    /**
     * Asignación de equivalencia basado en el identificador de la entitad
     */
    public boolean equals(Object obj)
    {
        if (obj instanceof AuditSsn)
        {
            if (((AuditSsn) obj).getAuditSsnId() == auditSsnId)
                return true;
        }
        return false;
    }
	
}