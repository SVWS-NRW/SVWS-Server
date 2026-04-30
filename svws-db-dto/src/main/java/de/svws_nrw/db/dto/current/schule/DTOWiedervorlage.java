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
@JsonPropertyOrder({"id", "personTyp", "idLehrer", "idSchueler", "idErzieher", "bemerkung", "tsAngelegt", "tsWiedervorlage", "tsErledigt", "idBenutzer", "idBenutzerErledigt", "idBenutzergruppe", "automatischErledigt"})
public final class DTOWiedervorlage {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOWiedervorlage e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOWiedervorlage e WHERE e.id = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM DTOWiedervorlage e WHERE e.id IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOWiedervorlage e WHERE e.id IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes id */
	public static final String QUERY_BY_ID = "SELECT e FROM DTOWiedervorlage e WHERE e.id = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes id */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM DTOWiedervorlage e WHERE e.id IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes personTyp */
	public static final String QUERY_BY_PERSONTYP = "SELECT e FROM DTOWiedervorlage e WHERE e.personTyp = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes personTyp */
	public static final String QUERY_LIST_BY_PERSONTYP = "SELECT e FROM DTOWiedervorlage e WHERE e.personTyp IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes idLehrer */
	public static final String QUERY_BY_IDLEHRER = "SELECT e FROM DTOWiedervorlage e WHERE e.idLehrer = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes idLehrer */
	public static final String QUERY_LIST_BY_IDLEHRER = "SELECT e FROM DTOWiedervorlage e WHERE e.idLehrer IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes idSchueler */
	public static final String QUERY_BY_IDSCHUELER = "SELECT e FROM DTOWiedervorlage e WHERE e.idSchueler = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes idSchueler */
	public static final String QUERY_LIST_BY_IDSCHUELER = "SELECT e FROM DTOWiedervorlage e WHERE e.idSchueler IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes idErzieher */
	public static final String QUERY_BY_IDERZIEHER = "SELECT e FROM DTOWiedervorlage e WHERE e.idErzieher = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes idErzieher */
	public static final String QUERY_LIST_BY_IDERZIEHER = "SELECT e FROM DTOWiedervorlage e WHERE e.idErzieher IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes bemerkung */
	public static final String QUERY_BY_BEMERKUNG = "SELECT e FROM DTOWiedervorlage e WHERE e.bemerkung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes bemerkung */
	public static final String QUERY_LIST_BY_BEMERKUNG = "SELECT e FROM DTOWiedervorlage e WHERE e.bemerkung IN ?1";

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

	/** Die Datenbankabfrage für DTOs anhand des Attributes idBenutzer */
	public static final String QUERY_BY_IDBENUTZER = "SELECT e FROM DTOWiedervorlage e WHERE e.idBenutzer = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes idBenutzer */
	public static final String QUERY_LIST_BY_IDBENUTZER = "SELECT e FROM DTOWiedervorlage e WHERE e.idBenutzer IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes idBenutzerErledigt */
	public static final String QUERY_BY_IDBENUTZERERLEDIGT = "SELECT e FROM DTOWiedervorlage e WHERE e.idBenutzerErledigt = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes idBenutzerErledigt */
	public static final String QUERY_LIST_BY_IDBENUTZERERLEDIGT = "SELECT e FROM DTOWiedervorlage e WHERE e.idBenutzerErledigt IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes idBenutzergruppe */
	public static final String QUERY_BY_IDBENUTZERGRUPPE = "SELECT e FROM DTOWiedervorlage e WHERE e.idBenutzergruppe = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes idBenutzergruppe */
	public static final String QUERY_LIST_BY_IDBENUTZERGRUPPE = "SELECT e FROM DTOWiedervorlage e WHERE e.idBenutzergruppe IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes automatischErledigt */
	public static final String QUERY_BY_AUTOMATISCHERLEDIGT = "SELECT e FROM DTOWiedervorlage e WHERE e.automatischErledigt = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes automatischErledigt */
	public static final String QUERY_LIST_BY_AUTOMATISCHERLEDIGT = "SELECT e FROM DTOWiedervorlage e WHERE e.automatischErledigt IN ?1";

	/** ID des Eintrags für die Wiedervorlage */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long id;

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
	public Long idLehrer;

	/** Die Schüler-ID des Benutzers, falls PersonTyp sich auf einen Lehrer bezieht */
	@Column(name = "Schueler_ID")
	@JsonProperty
	public Long idSchueler;

	/** Die Erzieher-ID des Benutzers, falls PersonTyp sich auf einen Lehrer bezieht */
	@Column(name = "Erzieher_ID")
	@JsonProperty
	public Long idErzieher;

	/** Die Bemerkung des Eintrags für die Wiedervorlage */
	@Column(name = "Bemerkung")
	@JsonProperty
	public String bemerkung;

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
	public Long idBenutzer;

	/** Die ID des Benutzers, welcher den Eintrag erledigt hat */
	@Column(name = "Benutzer_ID_Erledigt")
	@JsonProperty
	public Long idBenutzerErledigt;

	/** Die ID der Benutzergruppe, welcher der Eintrag für die Wiedervorlage zur Bearbeitung angezeigt werden soll oder null. */
	@Column(name = "Benutzergruppe_ID")
	@JsonProperty
	public Long idBenutzergruppe;

	/** Gibt an, dass der Eintrag automatisch als erledigt markiert werden soll, wenn er einem Benutzer nach dem Wiedervorlage-Zeitpunkt angezeigt wurde. */
	@Column(name = "AutomatischErledigt")
	@JsonProperty
	@Convert(converter = Boolean01Converter.class)
	@JsonSerialize(using = Boolean01ConverterSerializer.class)
	@JsonDeserialize(using = Boolean01ConverterDeserializer.class)
	public Boolean automatischErledigt;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOWiedervorlage ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOWiedervorlage() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOWiedervorlage ohne eine Initialisierung der Attribute.
	 * @param id   der Wert für das Attribut id
	 * @param bemerkung   der Wert für das Attribut bemerkung
	 * @param automatischErledigt   der Wert für das Attribut automatischErledigt
	 */
	public DTOWiedervorlage(final long id, final String bemerkung, final Boolean automatischErledigt) {
		this.id = id;
		if (bemerkung == null) {
			throw new NullPointerException("bemerkung must not be null");
		}
		this.bemerkung = bemerkung;
		this.automatischErledigt = automatischErledigt;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		DTOWiedervorlage other = (DTOWiedervorlage) obj;
		return id == other.id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Long.hashCode(id);
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "DTOWiedervorlage(id=" + this.id + ", personTyp=" + this.personTyp + ", idLehrer=" + this.idLehrer + ", idSchueler=" + this.idSchueler + ", idErzieher=" + this.idErzieher + ", bemerkung=" + this.bemerkung + ", tsAngelegt=" + this.tsAngelegt + ", tsWiedervorlage=" + this.tsWiedervorlage + ", tsErledigt=" + this.tsErledigt + ", idBenutzer=" + this.idBenutzer + ", idBenutzerErledigt=" + this.idBenutzerErledigt + ", idBenutzergruppe=" + this.idBenutzergruppe + ", automatischErledigt=" + this.automatischErledigt + ")";
	}

}
