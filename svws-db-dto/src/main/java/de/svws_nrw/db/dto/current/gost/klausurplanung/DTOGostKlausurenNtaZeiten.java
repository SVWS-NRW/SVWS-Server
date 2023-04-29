package de.svws_nrw.db.dto.current.gost.klausurplanung;

import de.svws_nrw.db.DBEntityManager;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle Gost_Klausuren_NtaZeiten.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@IdClass(DTOGostKlausurenNtaZeitenPK.class)
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Gost_Klausuren_NtaZeiten")
@NamedQuery(name = "DTOGostKlausurenNtaZeiten.all", query = "SELECT e FROM DTOGostKlausurenNtaZeiten e")
@NamedQuery(name = "DTOGostKlausurenNtaZeiten.schueler_id", query = "SELECT e FROM DTOGostKlausurenNtaZeiten e WHERE e.Schueler_ID = :value")
@NamedQuery(name = "DTOGostKlausurenNtaZeiten.schueler_id.multiple", query = "SELECT e FROM DTOGostKlausurenNtaZeiten e WHERE e.Schueler_ID IN :value")
@NamedQuery(name = "DTOGostKlausurenNtaZeiten.vorgabe_id", query = "SELECT e FROM DTOGostKlausurenNtaZeiten e WHERE e.Vorgabe_ID = :value")
@NamedQuery(name = "DTOGostKlausurenNtaZeiten.vorgabe_id.multiple", query = "SELECT e FROM DTOGostKlausurenNtaZeiten e WHERE e.Vorgabe_ID IN :value")
@NamedQuery(name = "DTOGostKlausurenNtaZeiten.zeitzugabe", query = "SELECT e FROM DTOGostKlausurenNtaZeiten e WHERE e.Zeitzugabe = :value")
@NamedQuery(name = "DTOGostKlausurenNtaZeiten.zeitzugabe.multiple", query = "SELECT e FROM DTOGostKlausurenNtaZeiten e WHERE e.Zeitzugabe IN :value")
@NamedQuery(name = "DTOGostKlausurenNtaZeiten.bemerkungen", query = "SELECT e FROM DTOGostKlausurenNtaZeiten e WHERE e.Bemerkungen = :value")
@NamedQuery(name = "DTOGostKlausurenNtaZeiten.bemerkungen.multiple", query = "SELECT e FROM DTOGostKlausurenNtaZeiten e WHERE e.Bemerkungen IN :value")
@NamedQuery(name = "DTOGostKlausurenNtaZeiten.primaryKeyQuery", query = "SELECT e FROM DTOGostKlausurenNtaZeiten e WHERE e.Schueler_ID = ?1 AND e.Vorgabe_ID = ?2")
@NamedQuery(name = "DTOGostKlausurenNtaZeiten.all.migration", query = "SELECT e FROM DTOGostKlausurenNtaZeiten e WHERE e.Schueler_ID IS NOT NULL AND e.Vorgabe_ID IS NOT NULL")
@JsonPropertyOrder({"Schueler_ID", "Vorgabe_ID", "Zeitzugabe", "Bemerkungen"})
public final class DTOGostKlausurenNtaZeiten {

	/** ID des Schülers */
	@Id
	@Column(name = "Schueler_ID")
	@JsonProperty
	public long Schueler_ID;

	/** ID der Klausurvorgaben */
	@Id
	@Column(name = "Vorgabe_ID")
	@JsonProperty
	public long Vorgabe_ID;

	/** Das Dauer der Zeitzugabe in Minuten */
	@Column(name = "Zeitzugabe")
	@JsonProperty
	public int Zeitzugabe;

	/** Text für Ergänzungen/Bemerkungen zum Nachteilsausgleich */
	@Column(name = "Bemerkungen")
	@JsonProperty
	public String Bemerkungen;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOGostKlausurenNtaZeiten ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOGostKlausurenNtaZeiten() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOGostKlausurenNtaZeiten ohne eine Initialisierung der Attribute.
	 * @param Schueler_ID   der Wert für das Attribut Schueler_ID
	 * @param Vorgabe_ID   der Wert für das Attribut Vorgabe_ID
	 * @param Zeitzugabe   der Wert für das Attribut Zeitzugabe
	 */
	public DTOGostKlausurenNtaZeiten(final long Schueler_ID, final long Vorgabe_ID, final int Zeitzugabe) {
		this.Schueler_ID = Schueler_ID;
		this.Vorgabe_ID = Vorgabe_ID;
		this.Zeitzugabe = Zeitzugabe;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOGostKlausurenNtaZeiten other = (DTOGostKlausurenNtaZeiten) obj;
		if (Schueler_ID != other.Schueler_ID)
			return false;

		if (Vorgabe_ID != other.Vorgabe_ID)
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Long.hashCode(Schueler_ID);

		result = prime * result + Long.hashCode(Vorgabe_ID);
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "DTOGostKlausurenNtaZeiten(Schueler_ID=" + this.Schueler_ID + ", Vorgabe_ID=" + this.Vorgabe_ID + ", Zeitzugabe=" + this.Zeitzugabe + ", Bemerkungen=" + this.Bemerkungen + ")";
	}

}
