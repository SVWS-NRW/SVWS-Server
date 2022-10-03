package de.nrw.schule.svws.db.dto.rev8.schild.intern;

import de.nrw.schule.svws.db.DBEntityManager;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
/**
 * Diese Klasse dient als DTO für die Datenbanktabelle Schildintern_Zusatzinfos.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@IdClass(Rev8DTOInternZusatzinfosPK.class)
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Schildintern_Zusatzinfos")
@NamedQuery(name="Rev8DTOInternZusatzinfos.all", query="SELECT e FROM Rev8DTOInternZusatzinfos e")
@NamedQuery(name="Rev8DTOInternZusatzinfos.sgl_bkabschl", query="SELECT e FROM Rev8DTOInternZusatzinfos e WHERE e.SGL_BKAbschl = :value")
@NamedQuery(name="Rev8DTOInternZusatzinfos.sgl_bkabschl.multiple", query="SELECT e FROM Rev8DTOInternZusatzinfos e WHERE e.SGL_BKAbschl IN :value")
@NamedQuery(name="Rev8DTOInternZusatzinfos.jg_bkabschl", query="SELECT e FROM Rev8DTOInternZusatzinfos e WHERE e.JG_BKAbschl = :value")
@NamedQuery(name="Rev8DTOInternZusatzinfos.jg_bkabschl.multiple", query="SELECT e FROM Rev8DTOInternZusatzinfos e WHERE e.JG_BKAbschl IN :value")
@NamedQuery(name="Rev8DTOInternZusatzinfos.gueltigvon", query="SELECT e FROM Rev8DTOInternZusatzinfos e WHERE e.gueltigVon = :value")
@NamedQuery(name="Rev8DTOInternZusatzinfos.gueltigvon.multiple", query="SELECT e FROM Rev8DTOInternZusatzinfos e WHERE e.gueltigVon IN :value")
@NamedQuery(name="Rev8DTOInternZusatzinfos.gueltigbis", query="SELECT e FROM Rev8DTOInternZusatzinfos e WHERE e.gueltigBis = :value")
@NamedQuery(name="Rev8DTOInternZusatzinfos.gueltigbis.multiple", query="SELECT e FROM Rev8DTOInternZusatzinfos e WHERE e.gueltigBis IN :value")
@NamedQuery(name="Rev8DTOInternZusatzinfos.primaryKeyQuery", query="SELECT e FROM Rev8DTOInternZusatzinfos e WHERE e.SGL_BKAbschl = ?1 AND e.JG_BKAbschl = ?2")
@NamedQuery(name="Rev8DTOInternZusatzinfos.all.migration", query="SELECT e FROM Rev8DTOInternZusatzinfos e WHERE e.SGL_BKAbschl IS NOT NULL AND e.JG_BKAbschl IS NOT NULL")
@JsonPropertyOrder({"SGL_BKAbschl","JG_BKAbschl","gueltigVon","gueltigBis"})
public class Rev8DTOInternZusatzinfos {

	/** Schildintern Tabelle: für den WinSchildImport ??? DEPRECATED */
	@Id
	@Column(name = "SGL_BKAbschl")
	@JsonProperty
	public String SGL_BKAbschl;

	/** Schildintern Tabelle: für den WinSchildImport ??? DEPRECATED */
	@Id
	@Column(name = "JG_BKAbschl")
	@JsonProperty
	public String JG_BKAbschl;

	/** Schildintern Tabelle: Gültig ab Schuljahr */
	@Column(name = "gueltigVon")
	@JsonProperty
	public Integer gueltigVon;

	/** Schildintern Tabelle: Gültig bis Schuljahr */
	@Column(name = "gueltigBis")
	@JsonProperty
	public Integer gueltigBis;

	/**
	 * Erstellt ein neues Objekt der Klasse Rev8DTOInternZusatzinfos ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private Rev8DTOInternZusatzinfos() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse Rev8DTOInternZusatzinfos ohne eine Initialisierung der Attribute.
	 * @param SGL_BKAbschl   der Wert für das Attribut SGL_BKAbschl
	 * @param JG_BKAbschl   der Wert für das Attribut JG_BKAbschl
	 */
	public Rev8DTOInternZusatzinfos(final String SGL_BKAbschl, final String JG_BKAbschl) {
		if (SGL_BKAbschl == null) { 
			throw new NullPointerException("SGL_BKAbschl must not be null");
		}
		this.SGL_BKAbschl = SGL_BKAbschl;
		if (JG_BKAbschl == null) { 
			throw new NullPointerException("JG_BKAbschl must not be null");
		}
		this.JG_BKAbschl = JG_BKAbschl;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Rev8DTOInternZusatzinfos other = (Rev8DTOInternZusatzinfos) obj;
		if (SGL_BKAbschl == null) {
			if (other.SGL_BKAbschl != null)
				return false;
		} else if (!SGL_BKAbschl.equals(other.SGL_BKAbschl))
			return false;

		if (JG_BKAbschl == null) {
			if (other.JG_BKAbschl != null)
				return false;
		} else if (!JG_BKAbschl.equals(other.JG_BKAbschl))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((SGL_BKAbschl == null) ? 0 : SGL_BKAbschl.hashCode());

		result = prime * result + ((JG_BKAbschl == null) ? 0 : JG_BKAbschl.hashCode());
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "Rev8DTOInternZusatzinfos(SGL_BKAbschl=" + this.SGL_BKAbschl + ", JG_BKAbschl=" + this.JG_BKAbschl + ", gueltigVon=" + this.gueltigVon + ", gueltigBis=" + this.gueltigBis + ")";
	}

}