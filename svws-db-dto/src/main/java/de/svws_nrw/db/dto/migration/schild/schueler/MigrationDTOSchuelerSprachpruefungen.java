package de.svws_nrw.db.dto.migration.schild.schueler;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.migration.MigrationBoolean01Converter;


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
import de.svws_nrw.csv.converter.migration.MigrationBoolean01ConverterSerializer;
import de.svws_nrw.csv.converter.migration.MigrationBoolean01ConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle SchuelerSprachpruefungen.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "SchuelerSprachpruefungen")
@JsonPropertyOrder({"ID", "Schueler_ID", "Sprache", "ASDJahrgang", "Anspruchsniveau", "Pruefungsdatum", "ErsetzteSprache", "IstHSUPruefung", "IstFeststellungspruefung", "KannErstePflichtfremdspracheErsetzen", "KannZweitePflichtfremdspracheErsetzen", "KannWahlpflichtfremdspracheErsetzen", "KannBelegungAlsFortgefuehrteSpracheErlauben", "Referenzniveau", "NotePruefung"})
public final class MigrationDTOSchuelerSprachpruefungen {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM MigrationDTOSchuelerSprachpruefungen e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM MigrationDTOSchuelerSprachpruefungen e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM MigrationDTOSchuelerSprachpruefungen e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM MigrationDTOSchuelerSprachpruefungen e WHERE e.ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ID */
	public static final String QUERY_BY_ID = "SELECT e FROM MigrationDTOSchuelerSprachpruefungen e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ID */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM MigrationDTOSchuelerSprachpruefungen e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Schueler_ID */
	public static final String QUERY_BY_SCHUELER_ID = "SELECT e FROM MigrationDTOSchuelerSprachpruefungen e WHERE e.Schueler_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Schueler_ID */
	public static final String QUERY_LIST_BY_SCHUELER_ID = "SELECT e FROM MigrationDTOSchuelerSprachpruefungen e WHERE e.Schueler_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Sprache */
	public static final String QUERY_BY_SPRACHE = "SELECT e FROM MigrationDTOSchuelerSprachpruefungen e WHERE e.Sprache = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Sprache */
	public static final String QUERY_LIST_BY_SPRACHE = "SELECT e FROM MigrationDTOSchuelerSprachpruefungen e WHERE e.Sprache IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ASDJahrgang */
	public static final String QUERY_BY_ASDJAHRGANG = "SELECT e FROM MigrationDTOSchuelerSprachpruefungen e WHERE e.ASDJahrgang = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ASDJahrgang */
	public static final String QUERY_LIST_BY_ASDJAHRGANG = "SELECT e FROM MigrationDTOSchuelerSprachpruefungen e WHERE e.ASDJahrgang IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Anspruchsniveau */
	public static final String QUERY_BY_ANSPRUCHSNIVEAU = "SELECT e FROM MigrationDTOSchuelerSprachpruefungen e WHERE e.Anspruchsniveau = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Anspruchsniveau */
	public static final String QUERY_LIST_BY_ANSPRUCHSNIVEAU = "SELECT e FROM MigrationDTOSchuelerSprachpruefungen e WHERE e.Anspruchsniveau IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Pruefungsdatum */
	public static final String QUERY_BY_PRUEFUNGSDATUM = "SELECT e FROM MigrationDTOSchuelerSprachpruefungen e WHERE e.Pruefungsdatum = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Pruefungsdatum */
	public static final String QUERY_LIST_BY_PRUEFUNGSDATUM = "SELECT e FROM MigrationDTOSchuelerSprachpruefungen e WHERE e.Pruefungsdatum IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ErsetzteSprache */
	public static final String QUERY_BY_ERSETZTESPRACHE = "SELECT e FROM MigrationDTOSchuelerSprachpruefungen e WHERE e.ErsetzteSprache = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ErsetzteSprache */
	public static final String QUERY_LIST_BY_ERSETZTESPRACHE = "SELECT e FROM MigrationDTOSchuelerSprachpruefungen e WHERE e.ErsetzteSprache IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes IstHSUPruefung */
	public static final String QUERY_BY_ISTHSUPRUEFUNG = "SELECT e FROM MigrationDTOSchuelerSprachpruefungen e WHERE e.IstHSUPruefung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes IstHSUPruefung */
	public static final String QUERY_LIST_BY_ISTHSUPRUEFUNG = "SELECT e FROM MigrationDTOSchuelerSprachpruefungen e WHERE e.IstHSUPruefung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes IstFeststellungspruefung */
	public static final String QUERY_BY_ISTFESTSTELLUNGSPRUEFUNG = "SELECT e FROM MigrationDTOSchuelerSprachpruefungen e WHERE e.IstFeststellungspruefung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes IstFeststellungspruefung */
	public static final String QUERY_LIST_BY_ISTFESTSTELLUNGSPRUEFUNG = "SELECT e FROM MigrationDTOSchuelerSprachpruefungen e WHERE e.IstFeststellungspruefung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes KannErstePflichtfremdspracheErsetzen */
	public static final String QUERY_BY_KANNERSTEPFLICHTFREMDSPRACHEERSETZEN = "SELECT e FROM MigrationDTOSchuelerSprachpruefungen e WHERE e.KannErstePflichtfremdspracheErsetzen = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes KannErstePflichtfremdspracheErsetzen */
	public static final String QUERY_LIST_BY_KANNERSTEPFLICHTFREMDSPRACHEERSETZEN = "SELECT e FROM MigrationDTOSchuelerSprachpruefungen e WHERE e.KannErstePflichtfremdspracheErsetzen IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes KannZweitePflichtfremdspracheErsetzen */
	public static final String QUERY_BY_KANNZWEITEPFLICHTFREMDSPRACHEERSETZEN = "SELECT e FROM MigrationDTOSchuelerSprachpruefungen e WHERE e.KannZweitePflichtfremdspracheErsetzen = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes KannZweitePflichtfremdspracheErsetzen */
	public static final String QUERY_LIST_BY_KANNZWEITEPFLICHTFREMDSPRACHEERSETZEN = "SELECT e FROM MigrationDTOSchuelerSprachpruefungen e WHERE e.KannZweitePflichtfremdspracheErsetzen IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes KannWahlpflichtfremdspracheErsetzen */
	public static final String QUERY_BY_KANNWAHLPFLICHTFREMDSPRACHEERSETZEN = "SELECT e FROM MigrationDTOSchuelerSprachpruefungen e WHERE e.KannWahlpflichtfremdspracheErsetzen = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes KannWahlpflichtfremdspracheErsetzen */
	public static final String QUERY_LIST_BY_KANNWAHLPFLICHTFREMDSPRACHEERSETZEN = "SELECT e FROM MigrationDTOSchuelerSprachpruefungen e WHERE e.KannWahlpflichtfremdspracheErsetzen IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes KannBelegungAlsFortgefuehrteSpracheErlauben */
	public static final String QUERY_BY_KANNBELEGUNGALSFORTGEFUEHRTESPRACHEERLAUBEN = "SELECT e FROM MigrationDTOSchuelerSprachpruefungen e WHERE e.KannBelegungAlsFortgefuehrteSpracheErlauben = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes KannBelegungAlsFortgefuehrteSpracheErlauben */
	public static final String QUERY_LIST_BY_KANNBELEGUNGALSFORTGEFUEHRTESPRACHEERLAUBEN = "SELECT e FROM MigrationDTOSchuelerSprachpruefungen e WHERE e.KannBelegungAlsFortgefuehrteSpracheErlauben IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Referenzniveau */
	public static final String QUERY_BY_REFERENZNIVEAU = "SELECT e FROM MigrationDTOSchuelerSprachpruefungen e WHERE e.Referenzniveau = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Referenzniveau */
	public static final String QUERY_LIST_BY_REFERENZNIVEAU = "SELECT e FROM MigrationDTOSchuelerSprachpruefungen e WHERE e.Referenzniveau IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes NotePruefung */
	public static final String QUERY_BY_NOTEPRUEFUNG = "SELECT e FROM MigrationDTOSchuelerSprachpruefungen e WHERE e.NotePruefung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes NotePruefung */
	public static final String QUERY_LIST_BY_NOTEPRUEFUNG = "SELECT e FROM MigrationDTOSchuelerSprachpruefungen e WHERE e.NotePruefung IN ?1";

	/** ID des Sprachprüfungseintrags */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** ID des Schülers des Sprachprüfungseintrags */
	@Column(name = "Schueler_ID")
	@JsonProperty
	public Long Schueler_ID;

	/** Atomares Sprachkürzel aus StatKue_SVWS_ZulaessigeFaecher */
	@Column(name = "Sprache")
	@JsonProperty
	public String Sprache;

	/** ASDJahrgangsbezeichnung, in der die Sprachprüfung abgelegt wurde */
	@Column(name = "ASDJahrgang")
	@JsonProperty
	public String ASDJahrgang;

	/** Das Anspruchsniveau der Sprachprüfung (angelehnt an einen entsprechenden Schulabschluss) */
	@Column(name = "Anspruchsniveau_ID")
	@JsonProperty
	public Long Anspruchsniveau;

	/** Datum der Sprachprüfung */
	@Column(name = "Pruefungsdatum")
	@JsonProperty
	public String Pruefungsdatum;

	/** Atomares Sprachkürzel aus StatKue_SVWS_ZulaessigeFaecher für die ersetzte Sprache */
	@Column(name = "ErsetzteSprache")
	@JsonProperty
	public String ErsetzteSprache;

	/** Gibt an, dass die Prüfung eine Prüfung in der Herkunftssprache ist (BASS 13-61 Nr. 2). Entspricht dem Eintrag P in Schild 2 */
	@Column(name = "IstHSUPruefung")
	@JsonProperty
	@Convert(converter = MigrationBoolean01Converter.class)
	@JsonSerialize(using = MigrationBoolean01ConverterSerializer.class)
	@JsonDeserialize(using = MigrationBoolean01ConverterDeserializer.class)
	public Boolean IstHSUPruefung;

	/** Gibt an, ob die Prüfung eine Sprachfeststellungsprüfung ist (BASS 13-61 Nr. 1). Entspricht N in Schild 2 */
	@Column(name = "IstFeststellungspruefung")
	@JsonProperty
	@Convert(converter = MigrationBoolean01Converter.class)
	@JsonSerialize(using = MigrationBoolean01ConverterSerializer.class)
	@JsonDeserialize(using = MigrationBoolean01ConverterDeserializer.class)
	public Boolean IstFeststellungspruefung;

	/** Gibt an, ob die Sprachprüfung an die Stelle der ersten Pflichtfremdsprache treten kann */
	@Column(name = "KannErstePflichtfremdspracheErsetzen")
	@JsonProperty
	@Convert(converter = MigrationBoolean01Converter.class)
	@JsonSerialize(using = MigrationBoolean01ConverterSerializer.class)
	@JsonDeserialize(using = MigrationBoolean01ConverterDeserializer.class)
	public Boolean KannErstePflichtfremdspracheErsetzen;

	/** Gibt an, ob die Sprachprüfung an die Stelle der zweiten Pflichtfremdsprache treten kann */
	@Column(name = "KannZweitePflichtfremdspracheErsetzen")
	@JsonProperty
	@Convert(converter = MigrationBoolean01Converter.class)
	@JsonSerialize(using = MigrationBoolean01ConverterSerializer.class)
	@JsonDeserialize(using = MigrationBoolean01ConverterDeserializer.class)
	public Boolean KannZweitePflichtfremdspracheErsetzen;

	/** Gibt an, ob die Sprachprüfung an die Stelle einer Wahlpflichtfremdsprache der Klassen 05-07 treten kann */
	@Column(name = "KannWahlpflichtfremdspracheErsetzen")
	@JsonProperty
	@Convert(converter = MigrationBoolean01Converter.class)
	@JsonSerialize(using = MigrationBoolean01ConverterSerializer.class)
	@JsonDeserialize(using = MigrationBoolean01ConverterDeserializer.class)
	public Boolean KannWahlpflichtfremdspracheErsetzen;

	/** Gibt an, ob die Sprachprüfung nachweist, dass die Sprache als fortgeführte Fremdsprache in der Oberstufe belegt werden kann (BASS 13-61 Nr. 1 Abs. 11) */
	@Column(name = "KannBelegungAlsFortgefuehrteSpracheErlauben")
	@JsonProperty
	@Convert(converter = MigrationBoolean01Converter.class)
	@JsonSerialize(using = MigrationBoolean01ConverterSerializer.class)
	@JsonDeserialize(using = MigrationBoolean01ConverterDeserializer.class)
	public Boolean KannBelegungAlsFortgefuehrteSpracheErlauben;

	/** Das Sprachreferenzniveau der Sprachprüfung gemäß GeR */
	@Column(name = "Referenzniveau")
	@JsonProperty
	public String Referenzniveau;

	/** Note der Sprachprüfung, die herangezogen werden kann, falls die Note der Sprachprüfung an die Stelle einer Fremdsprachennote tritt */
	@Column(name = "NotePruefung")
	@JsonProperty
	public Integer NotePruefung;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOSchuelerSprachpruefungen ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOSchuelerSprachpruefungen() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOSchuelerSprachpruefungen ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Schueler_ID   der Wert für das Attribut Schueler_ID
	 * @param Sprache   der Wert für das Attribut Sprache
	 */
	public MigrationDTOSchuelerSprachpruefungen(final Long ID, final Long Schueler_ID, final String Sprache) {
		if (ID == null) {
			throw new NullPointerException("ID must not be null");
		}
		this.ID = ID;
		if (Schueler_ID == null) {
			throw new NullPointerException("Schueler_ID must not be null");
		}
		this.Schueler_ID = Schueler_ID;
		if (Sprache == null) {
			throw new NullPointerException("Sprache must not be null");
		}
		this.Sprache = Sprache;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MigrationDTOSchuelerSprachpruefungen other = (MigrationDTOSchuelerSprachpruefungen) obj;
		if (ID == null) {
			if (other.ID != null)
				return false;
		} else if (!ID.equals(other.ID))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ID == null) ? 0 : ID.hashCode());
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "MigrationDTOSchuelerSprachpruefungen(ID=" + this.ID + ", Schueler_ID=" + this.Schueler_ID + ", Sprache=" + this.Sprache + ", ASDJahrgang=" + this.ASDJahrgang + ", Anspruchsniveau=" + this.Anspruchsniveau + ", Pruefungsdatum=" + this.Pruefungsdatum + ", ErsetzteSprache=" + this.ErsetzteSprache + ", IstHSUPruefung=" + this.IstHSUPruefung + ", IstFeststellungspruefung=" + this.IstFeststellungspruefung + ", KannErstePflichtfremdspracheErsetzen=" + this.KannErstePflichtfremdspracheErsetzen + ", KannZweitePflichtfremdspracheErsetzen=" + this.KannZweitePflichtfremdspracheErsetzen + ", KannWahlpflichtfremdspracheErsetzen=" + this.KannWahlpflichtfremdspracheErsetzen + ", KannBelegungAlsFortgefuehrteSpracheErlauben=" + this.KannBelegungAlsFortgefuehrteSpracheErlauben + ", Referenzniveau=" + this.Referenzniveau + ", NotePruefung=" + this.NotePruefung + ")";
	}

}
