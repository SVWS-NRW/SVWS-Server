package de.svws_nrw.db.dto.current.gost.klausurplanung;

import de.svws_nrw.db.DBEntityManager;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
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
@JsonPropertyOrder({"Schueler_ID", "Vorgabe_ID", "Zeitzugabe", "Bemerkungen"})
public final class DTOGostKlausurenNtaZeiten {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOGostKlausurenNtaZeiten e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOGostKlausurenNtaZeiten e WHERE e.Schueler_ID = ?1 AND e.Vorgabe_ID = ?2";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOGostKlausurenNtaZeiten e WHERE e.Schueler_ID IS NOT NULL AND e.Vorgabe_ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Schueler_ID */
	public static final String QUERY_BY_SCHUELER_ID = "SELECT e FROM DTOGostKlausurenNtaZeiten e WHERE e.Schueler_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Schueler_ID */
	public static final String QUERY_LIST_BY_SCHUELER_ID = "SELECT e FROM DTOGostKlausurenNtaZeiten e WHERE e.Schueler_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Vorgabe_ID */
	public static final String QUERY_BY_VORGABE_ID = "SELECT e FROM DTOGostKlausurenNtaZeiten e WHERE e.Vorgabe_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Vorgabe_ID */
	public static final String QUERY_LIST_BY_VORGABE_ID = "SELECT e FROM DTOGostKlausurenNtaZeiten e WHERE e.Vorgabe_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Zeitzugabe */
	public static final String QUERY_BY_ZEITZUGABE = "SELECT e FROM DTOGostKlausurenNtaZeiten e WHERE e.Zeitzugabe = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Zeitzugabe */
	public static final String QUERY_LIST_BY_ZEITZUGABE = "SELECT e FROM DTOGostKlausurenNtaZeiten e WHERE e.Zeitzugabe IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Bemerkungen */
	public static final String QUERY_BY_BEMERKUNGEN = "SELECT e FROM DTOGostKlausurenNtaZeiten e WHERE e.Bemerkungen = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Bemerkungen */
	public static final String QUERY_LIST_BY_BEMERKUNGEN = "SELECT e FROM DTOGostKlausurenNtaZeiten e WHERE e.Bemerkungen IN ?1";

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
		return Vorgabe_ID == other.Vorgabe_ID;
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
