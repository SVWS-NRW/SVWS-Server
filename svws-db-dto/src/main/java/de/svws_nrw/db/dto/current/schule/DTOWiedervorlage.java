package de.svws_nrw.db.dto.current.schule;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.current.Boolean01Converter;
import de.svws_nrw.db.converter.current.PersonTypNullableConverter;

import de.svws_nrw.core.types.schule.PersonTyp;


import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.svws_nrw.csv.converter.current.Boolean01ConverterSerializer;
import de.svws_nrw.csv.converter.current.Boolean01ConverterDeserializer;
import de.svws_nrw.csv.converter.current.PersonTypNullableConverterSerializer;
import de.svws_nrw.csv.converter.current.PersonTypNullableConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle Wiedervorlage.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Wiedervorlage")
@JsonPropertyOrder({"ID", "personTyp", "Lehrer_ID", "Schueler_ID", "Erzieher_ID", "Bemerkung", "tsAngelegt", "tsWiedervorlage", "tsErledigt", "Benutzer_ID", "Benutzer_ID_Erledigt", "Benutzergruppe_ID", "AutomatischErledigt"})
public final class DTOWiedervorlage {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOWiedervorlage e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOWiedervorlage e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM DTOWiedervorlage e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOWiedervorlage e WHERE e.ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ID */
	public static final String QUERY_BY_ID = "SELECT e FROM DTOWiedervorlage e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ID */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM DTOWiedervorlage e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes personTyp */
	public static final String QUERY_BY_PERSONTYP = "SELECT e FROM DTOWiedervorlage e WHERE e.personTyp = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes personTyp */
	public static final String QUERY_LIST_BY_PERSONTYP = "SELECT e FROM DTOWiedervorlage e WHERE e.personTyp IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Lehrer_ID */
	public static final String QUERY_BY_LEHRER_ID = "SELECT e FROM DTOWiedervorlage e WHERE e.Lehrer_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Lehrer_ID */
	public static final String QUERY_LIST_BY_LEHRER_ID = "SELECT e FROM DTOWiedervorlage e WHERE e.Lehrer_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Schueler_ID */
	public static final String QUERY_BY_SCHUELER_ID = "SELECT e FROM DTOWiedervorlage e WHERE e.Schueler_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Schueler_ID */
	public static final String QUERY_LIST_BY_SCHUELER_ID = "SELECT e FROM DTOWiedervorlage e WHERE e.Schueler_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Erzieher_ID */
	public static final String QUERY_BY_ERZIEHER_ID = "SELECT e FROM DTOWiedervorlage e WHERE e.Erzieher_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Erzieher_ID */
	public static final String QUERY_LIST_BY_ERZIEHER_ID = "SELECT e FROM DTOWiedervorlage e WHERE e.Erzieher_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Bemerkung */
	public static final String QUERY_BY_BEMERKUNG = "SELECT e FROM DTOWiedervorlage e WHERE e.Bemerkung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Bemerkung */
	public static final String QUERY_LIST_BY_BEMERKUNG = "SELECT e FROM DTOWiedervorlage e WHERE e.Bemerkung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes tsAngelegt */
	public static final String QUERY_BY_TSANGELEGT = "SELECT e FROM DTOWiedervorlage e WHERE e.tsAngelegt = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes tsAngelegt */
	public static final String QUERY_LIST_BY_TSANGELEGT = "SELECT e FROM DTOWiedervorlage e WHERE e.tsAngelegt IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes tsWiedervorlage */
	public static final String QUERY_BY_TSWIEDERVORLAGE = "SELECT e FROM DTOWiedervorlage e WHERE e.tsWiedervorlage = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes tsWiedervorlage */
	public static final String QUERY_LIST_BY_TSWIEDERVORLAGE = "SELECT e FROM DTOWiedervorlage e WHERE e.tsWiedervorlage IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes tsErledigt */
	public static final String QUERY_BY_TSERLEDIGT = "SELECT e FROM DTOWiedervorlage e WHERE e.tsErledigt = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes tsErledigt */
	public static final String QUERY_LIST_BY_TSERLEDIGT = "SELECT e FROM DTOWiedervorlage e WHERE e.tsErledigt IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Benutzer_ID */
	public static final String QUERY_BY_BENUTZER_ID = "SELECT e FROM DTOWiedervorlage e WHERE e.Benutzer_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Benutzer_ID */
	public static final String QUERY_LIST_BY_BENUTZER_ID = "SELECT e FROM DTOWiedervorlage e WHERE e.Benutzer_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Benutzer_ID_Erledigt */
	public static final String QUERY_BY_BENUTZER_ID_ERLEDIGT = "SELECT e FROM DTOWiedervorlage e WHERE e.Benutzer_ID_Erledigt = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Benutzer_ID_Erledigt */
	public static final String QUERY_LIST_BY_BENUTZER_ID_ERLEDIGT = "SELECT e FROM DTOWiedervorlage e WHERE e.Benutzer_ID_Erledigt IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Benutzergruppe_ID */
	public static final String QUERY_BY_BENUTZERGRUPPE_ID = "SELECT e FROM DTOWiedervorlage e WHERE e.Benutzergruppe_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Benutzergruppe_ID */
	public static final String QUERY_LIST_BY_BENUTZERGRUPPE_ID = "SELECT e FROM DTOWiedervorlage e WHERE e.Benutzergruppe_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes AutomatischErledigt */
	public static final String QUERY_BY_AUTOMATISCHERLEDIGT = "SELECT e FROM DTOWiedervorlage e WHERE e.AutomatischErledigt = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes AutomatischErledigt */
	public static final String QUERY_LIST_BY_AUTOMATISCHERLEDIGT = "SELECT e FROM DTOWiedervorlage e WHERE e.AutomatischErledigt IN ?1";

	/** ID des Eintrags für die Wiedervorlage */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long ID;

	/** Der Typ der Person, welche der Wiedevorlage zugeordnet ist (S=Schueler L=Lehrer E=Erzieher) */
	@Column(name = "PersonTyp")
	@JsonProperty
	@Convert(converter = PersonTypNullableConverter.class)
	@JsonSerialize(using = PersonTypNullableConverterSerializer.class)
	@JsonDeserialize(using = PersonTypNullableConverterDeserializer.class)
	public PersonTyp personTyp;

	/** Die Lehrer-ID des Benutzers, falls PersonTyp sich auf einen Lehrer bezieht */
	@Column(name = "Lehrer_ID")
	@JsonProperty
	public Long Lehrer_ID;

	/** Die Schüler-ID des Benutzers, falls PersonTyp sich auf einen Lehrer bezieht */
	@Column(name = "Schueler_ID")
	@JsonProperty
	public Long Schueler_ID;

	/** Die Erzieher-ID des Benutzers, falls PersonTyp sich auf einen Lehrer bezieht */
	@Column(name = "Erzieher_ID")
	@JsonProperty
	public Long Erzieher_ID;

	/** Die Bemerkung des Eintrags für die Wiedervorlage */
	@Column(name = "Bemerkung")
	@JsonProperty
	public String Bemerkung;

	/** Der Zeitpunkt, wann der Eintrag für die Wiedervorlage angelegt wurde */
	@Column(name = "tsAngelegt")
	@JsonProperty
	public String tsAngelegt;

	/** Der Zeitpunkt, ab wann der Eintrag zur Wiedervorlage angezeigt werden soll */
	@Column(name = "tsWiedervorlage")
	@JsonProperty
	public String tsWiedervorlage;

	/** Der Zeitpunkt, wann der Eintrag als erledigt markiert wurde */
	@Column(name = "tsErledigt")
	@JsonProperty
	public String tsErledigt;

	/** Die ID des Benutzers, welcher den Eintrag zur Wiedervorlage angelegt hat und dem er auch angezeigt wird. */
	@Column(name = "Benutzer_ID")
	@JsonProperty
	public Long Benutzer_ID;

	/** Die ID des Benutzers, welcher den Eintrag erledigt hat */
	@Column(name = "Benutzer_ID_Erledigt")
	@JsonProperty
	public Long Benutzer_ID_Erledigt;

	/** Die ID der Benutzergruppe, welcher der Eintrag für die Wiedervorlage zur Bearbeitung angezeigt werden soll oder null. */
	@Column(name = "Benutzergruppe_ID")
	@JsonProperty
	public Long Benutzergruppe_ID;

	/** Gibt an, dass der Eintrag automatisch als erledigt markiert werden soll, wenn er einem Benutzer nach dem Wiedervorlage-Zeitpunkt angezeigt wurde. */
	@Column(name = "AutomatischErledigt")
	@JsonProperty
	@Convert(converter = Boolean01Converter.class)
	@JsonSerialize(using = Boolean01ConverterSerializer.class)
	@JsonDeserialize(using = Boolean01ConverterDeserializer.class)
	public Boolean AutomatischErledigt;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOWiedervorlage ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOWiedervorlage() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOWiedervorlage ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Bemerkung   der Wert für das Attribut Bemerkung
	 * @param AutomatischErledigt   der Wert für das Attribut AutomatischErledigt
	 */
	public DTOWiedervorlage(final long ID, final String Bemerkung, final Boolean AutomatischErledigt) {
		this.ID = ID;
		if (Bemerkung == null) {
			throw new NullPointerException("Bemerkung must not be null");
		}
		this.Bemerkung = Bemerkung;
		this.AutomatischErledigt = AutomatischErledigt;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOWiedervorlage other = (DTOWiedervorlage) obj;
		return ID == other.ID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Long.hashCode(ID);
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "DTOWiedervorlage(ID=" + this.ID + ", personTyp=" + this.personTyp + ", Lehrer_ID=" + this.Lehrer_ID + ", Schueler_ID=" + this.Schueler_ID + ", Erzieher_ID=" + this.Erzieher_ID + ", Bemerkung=" + this.Bemerkung + ", tsAngelegt=" + this.tsAngelegt + ", tsWiedervorlage=" + this.tsWiedervorlage + ", tsErledigt=" + this.tsErledigt + ", Benutzer_ID=" + this.Benutzer_ID + ", Benutzer_ID_Erledigt=" + this.Benutzer_ID_Erledigt + ", Benutzergruppe_ID=" + this.Benutzergruppe_ID + ", AutomatischErledigt=" + this.AutomatischErledigt + ")";
	}

}
