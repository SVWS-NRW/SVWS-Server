package de.nrw.schule.svws.db.dto.current.views.statkue;

import de.nrw.schule.svws.db.DBEntityManager;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
/**
 * Diese Klasse dient als DTO für die Datenbank-View Statkue_Einschulungsart.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Statkue_Einschulungsart")
@NamedQuery(name="DTOStatkueEinschulungsart.all", query="SELECT e FROM DTOStatkueEinschulungsart e")
@NamedQuery(name="DTOStatkueEinschulungsart.art", query="SELECT e FROM DTOStatkueEinschulungsart e WHERE e.Art = :value")
@NamedQuery(name="DTOStatkueEinschulungsart.art.multiple", query="SELECT e FROM DTOStatkueEinschulungsart e WHERE e.Art IN :value")
@NamedQuery(name="DTOStatkueEinschulungsart.sortierung", query="SELECT e FROM DTOStatkueEinschulungsart e WHERE e.Sortierung = :value")
@NamedQuery(name="DTOStatkueEinschulungsart.sortierung.multiple", query="SELECT e FROM DTOStatkueEinschulungsart e WHERE e.Sortierung IN :value")
@NamedQuery(name="DTOStatkueEinschulungsart.beschreibung", query="SELECT e FROM DTOStatkueEinschulungsart e WHERE e.Beschreibung = :value")
@NamedQuery(name="DTOStatkueEinschulungsart.beschreibung.multiple", query="SELECT e FROM DTOStatkueEinschulungsart e WHERE e.Beschreibung IN :value")
@NamedQuery(name="DTOStatkueEinschulungsart.geaendert", query="SELECT e FROM DTOStatkueEinschulungsart e WHERE e.geaendert = :value")
@NamedQuery(name="DTOStatkueEinschulungsart.geaendert.multiple", query="SELECT e FROM DTOStatkueEinschulungsart e WHERE e.geaendert IN :value")
@NamedQuery(name="DTOStatkueEinschulungsart.gueltigvon", query="SELECT e FROM DTOStatkueEinschulungsart e WHERE e.gueltigVon = :value")
@NamedQuery(name="DTOStatkueEinschulungsart.gueltigvon.multiple", query="SELECT e FROM DTOStatkueEinschulungsart e WHERE e.gueltigVon IN :value")
@NamedQuery(name="DTOStatkueEinschulungsart.gueltigbis", query="SELECT e FROM DTOStatkueEinschulungsart e WHERE e.gueltigBis = :value")
@NamedQuery(name="DTOStatkueEinschulungsart.gueltigbis.multiple", query="SELECT e FROM DTOStatkueEinschulungsart e WHERE e.gueltigBis IN :value")
@NamedQuery(name="DTOStatkueEinschulungsart.primaryKeyQuery", query="SELECT e FROM DTOStatkueEinschulungsart e WHERE e.Art = ?1")
@JsonPropertyOrder({"Art","Sortierung","Beschreibung","geaendert","gueltigVon","gueltigBis"})
public class DTOStatkueEinschulungsart {

	/** Das Kürzel der Einschulungsart. */
	@Id
	@Column(name = "Art")
	@JsonProperty
	public String Art;

	/** Zur Kompatibilität vorhanden */
	@Column(name = "Sortierung")
	@JsonProperty
	public Integer Sortierung;

	/** Die textuelle Beschreibung der Einschulungsart */
	@Column(name = "Beschreibung")
	@JsonProperty
	public String Beschreibung;

	/** Datum der letzten Änderung (hier zur Kompatibilität vorhanden) */
	@Column(name = "geaendert")
	@JsonProperty
	public String geaendert;

	/** Gibt die Gültigkeit ab welchem Schuljahr an */
	@Column(name = "gueltigVon")
	@JsonProperty
	public String gueltigVon;

	/** Gibt die Gültigkeit bis zu welchem Schuljahr an */
	@Column(name = "gueltigBis")
	@JsonProperty
	public String gueltigBis;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOStatkueEinschulungsart ohne eine Initialisierung der Attribute.
	 */
	private DTOStatkueEinschulungsart() {
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOStatkueEinschulungsart other = (DTOStatkueEinschulungsart) obj;
		if (Art == null) {
			if (other.Art != null)
				return false;
		} else if (!Art.equals(other.Art))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((Art == null) ? 0 : Art.hashCode());
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "DTOStatkueEinschulungsart(Art=" + this.Art + ", Sortierung=" + this.Sortierung + ", Beschreibung=" + this.Beschreibung + ", geaendert=" + this.geaendert + ", gueltigVon=" + this.gueltigVon + ", gueltigBis=" + this.gueltigBis + ")";
	}

}