package de.nrw.schule.svws.db.dto.current.views.schildintern;

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
 * Diese Klasse dient als DTO für die Datenbank-View Schildintern_Berufsebene.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@IdClass(DTOSchildInternBerufsebenenPK.class)
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Schildintern_Berufsebene")
@NamedQuery(name="DTOSchildInternBerufsebenen.all", query="SELECT e FROM DTOSchildInternBerufsebenen e")
@NamedQuery(name="DTOSchildInternBerufsebenen.berufsebene", query="SELECT e FROM DTOSchildInternBerufsebenen e WHERE e.Berufsebene = :value")
@NamedQuery(name="DTOSchildInternBerufsebenen.berufsebene.multiple", query="SELECT e FROM DTOSchildInternBerufsebenen e WHERE e.Berufsebene IN :value")
@NamedQuery(name="DTOSchildInternBerufsebenen.kuerzel", query="SELECT e FROM DTOSchildInternBerufsebenen e WHERE e.Kuerzel = :value")
@NamedQuery(name="DTOSchildInternBerufsebenen.kuerzel.multiple", query="SELECT e FROM DTOSchildInternBerufsebenen e WHERE e.Kuerzel IN :value")
@NamedQuery(name="DTOSchildInternBerufsebenen.klartext", query="SELECT e FROM DTOSchildInternBerufsebenen e WHERE e.Klartext = :value")
@NamedQuery(name="DTOSchildInternBerufsebenen.klartext.multiple", query="SELECT e FROM DTOSchildInternBerufsebenen e WHERE e.Klartext IN :value")
@NamedQuery(name="DTOSchildInternBerufsebenen.gueltigvon", query="SELECT e FROM DTOSchildInternBerufsebenen e WHERE e.gueltigVon = :value")
@NamedQuery(name="DTOSchildInternBerufsebenen.gueltigvon.multiple", query="SELECT e FROM DTOSchildInternBerufsebenen e WHERE e.gueltigVon IN :value")
@NamedQuery(name="DTOSchildInternBerufsebenen.gueltigbis", query="SELECT e FROM DTOSchildInternBerufsebenen e WHERE e.gueltigBis = :value")
@NamedQuery(name="DTOSchildInternBerufsebenen.gueltigbis.multiple", query="SELECT e FROM DTOSchildInternBerufsebenen e WHERE e.gueltigBis IN :value")
@NamedQuery(name="DTOSchildInternBerufsebenen.primaryKeyQuery", query="SELECT e FROM DTOSchildInternBerufsebenen e WHERE e.Berufsebene = ?1 AND e.Kuerzel = ?2")
@JsonPropertyOrder({"Berufsebene","Kuerzel","Klartext","gueltigVon","gueltigBis"})
public class DTOSchildInternBerufsebenen {

	/** Die Berufsebene für Fachklassen */
	@Id
	@Column(name = "Berufsebene")
	@JsonProperty
	public Integer Berufsebene;

	/** Das Kürzel der Berufsebene */
	@Id
	@Column(name = "Kuerzel")
	@JsonProperty
	public String Kuerzel;

	/** Die Bezeichnung der Berufsebene */
	@Column(name = "Klartext")
	@JsonProperty
	public String Klartext;

	/** Gibt das Schuljahr an, ab dem die Berufsebene verwendet werden kann oder null, falls es keine Einschränkung gibt */
	@Column(name = "gueltigVon")
	@JsonProperty
	public Integer gueltigVon;

	/** Gibt das Schuljahr an, bis zu welchem die Berufsebene verwendet werden kann oder null, falls es keine Einschränkung gibt */
	@Column(name = "gueltigBis")
	@JsonProperty
	public Integer gueltigBis;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOSchildInternBerufsebenen ohne eine Initialisierung der Attribute.
	 */
	private DTOSchildInternBerufsebenen() {
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOSchildInternBerufsebenen other = (DTOSchildInternBerufsebenen) obj;
		if (Berufsebene == null) {
			if (other.Berufsebene != null)
				return false;
		} else if (!Berufsebene.equals(other.Berufsebene))
			return false;

		if (Kuerzel == null) {
			if (other.Kuerzel != null)
				return false;
		} else if (!Kuerzel.equals(other.Kuerzel))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((Berufsebene == null) ? 0 : Berufsebene.hashCode());

		result = prime * result + ((Kuerzel == null) ? 0 : Kuerzel.hashCode());
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "DTOSchildInternBerufsebenen(Berufsebene=" + this.Berufsebene + ", Kuerzel=" + this.Kuerzel + ", Klartext=" + this.Klartext + ", gueltigVon=" + this.gueltigVon + ", gueltigBis=" + this.gueltigBis + ")";
	}

}