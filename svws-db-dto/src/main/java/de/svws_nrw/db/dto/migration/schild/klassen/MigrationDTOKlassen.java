package de.svws_nrw.db.dto.migration.schild.klassen;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.migration.MigrationBooleanPlusMinusDefaultMinusConverter;
import de.svws_nrw.db.converter.migration.MigrationBooleanPlusMinusDefaultPlusConverter;


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
import de.svws_nrw.csv.converter.migration.MigrationBooleanPlusMinusDefaultMinusConverterSerializer;
import de.svws_nrw.csv.converter.migration.MigrationBooleanPlusMinusDefaultMinusConverterDeserializer;
import de.svws_nrw.csv.converter.migration.MigrationBooleanPlusMinusDefaultPlusConverterSerializer;
import de.svws_nrw.csv.converter.migration.MigrationBooleanPlusMinusDefaultPlusConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle Klassen.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Klassen")
@JsonPropertyOrder({"ID", "Schuljahresabschnitts_ID", "Bezeichnung", "ASDKlasse", "Klasse", "Jahrgang_ID", "FKlasse", "VKlasse", "OrgFormKrz", "ASDSchulformNr", "Fachklasse_ID", "PruefOrdnung", "Sichtbar", "Sortierung", "Klassenart", "SommerSem", "NotenGesperrt", "AdrMerkmal", "KoopKlasse", "Ankreuzzeugnisse"})
public final class MigrationDTOKlassen {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM MigrationDTOKlassen e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM MigrationDTOKlassen e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM MigrationDTOKlassen e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM MigrationDTOKlassen e WHERE e.ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ID */
	public static final String QUERY_BY_ID = "SELECT e FROM MigrationDTOKlassen e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ID */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM MigrationDTOKlassen e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Schuljahresabschnitts_ID */
	public static final String QUERY_BY_SCHULJAHRESABSCHNITTS_ID = "SELECT e FROM MigrationDTOKlassen e WHERE e.Schuljahresabschnitts_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Schuljahresabschnitts_ID */
	public static final String QUERY_LIST_BY_SCHULJAHRESABSCHNITTS_ID = "SELECT e FROM MigrationDTOKlassen e WHERE e.Schuljahresabschnitts_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Bezeichnung */
	public static final String QUERY_BY_BEZEICHNUNG = "SELECT e FROM MigrationDTOKlassen e WHERE e.Bezeichnung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Bezeichnung */
	public static final String QUERY_LIST_BY_BEZEICHNUNG = "SELECT e FROM MigrationDTOKlassen e WHERE e.Bezeichnung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ASDKlasse */
	public static final String QUERY_BY_ASDKLASSE = "SELECT e FROM MigrationDTOKlassen e WHERE e.ASDKlasse = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ASDKlasse */
	public static final String QUERY_LIST_BY_ASDKLASSE = "SELECT e FROM MigrationDTOKlassen e WHERE e.ASDKlasse IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Klasse */
	public static final String QUERY_BY_KLASSE = "SELECT e FROM MigrationDTOKlassen e WHERE e.Klasse = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Klasse */
	public static final String QUERY_LIST_BY_KLASSE = "SELECT e FROM MigrationDTOKlassen e WHERE e.Klasse IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Jahrgang_ID */
	public static final String QUERY_BY_JAHRGANG_ID = "SELECT e FROM MigrationDTOKlassen e WHERE e.Jahrgang_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Jahrgang_ID */
	public static final String QUERY_LIST_BY_JAHRGANG_ID = "SELECT e FROM MigrationDTOKlassen e WHERE e.Jahrgang_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes FKlasse */
	public static final String QUERY_BY_FKLASSE = "SELECT e FROM MigrationDTOKlassen e WHERE e.FKlasse = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes FKlasse */
	public static final String QUERY_LIST_BY_FKLASSE = "SELECT e FROM MigrationDTOKlassen e WHERE e.FKlasse IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes VKlasse */
	public static final String QUERY_BY_VKLASSE = "SELECT e FROM MigrationDTOKlassen e WHERE e.VKlasse = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes VKlasse */
	public static final String QUERY_LIST_BY_VKLASSE = "SELECT e FROM MigrationDTOKlassen e WHERE e.VKlasse IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes OrgFormKrz */
	public static final String QUERY_BY_ORGFORMKRZ = "SELECT e FROM MigrationDTOKlassen e WHERE e.OrgFormKrz = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes OrgFormKrz */
	public static final String QUERY_LIST_BY_ORGFORMKRZ = "SELECT e FROM MigrationDTOKlassen e WHERE e.OrgFormKrz IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ASDSchulformNr */
	public static final String QUERY_BY_ASDSCHULFORMNR = "SELECT e FROM MigrationDTOKlassen e WHERE e.ASDSchulformNr = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ASDSchulformNr */
	public static final String QUERY_LIST_BY_ASDSCHULFORMNR = "SELECT e FROM MigrationDTOKlassen e WHERE e.ASDSchulformNr IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Fachklasse_ID */
	public static final String QUERY_BY_FACHKLASSE_ID = "SELECT e FROM MigrationDTOKlassen e WHERE e.Fachklasse_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Fachklasse_ID */
	public static final String QUERY_LIST_BY_FACHKLASSE_ID = "SELECT e FROM MigrationDTOKlassen e WHERE e.Fachklasse_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes PruefOrdnung */
	public static final String QUERY_BY_PRUEFORDNUNG = "SELECT e FROM MigrationDTOKlassen e WHERE e.PruefOrdnung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes PruefOrdnung */
	public static final String QUERY_LIST_BY_PRUEFORDNUNG = "SELECT e FROM MigrationDTOKlassen e WHERE e.PruefOrdnung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Sichtbar */
	public static final String QUERY_BY_SICHTBAR = "SELECT e FROM MigrationDTOKlassen e WHERE e.Sichtbar = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Sichtbar */
	public static final String QUERY_LIST_BY_SICHTBAR = "SELECT e FROM MigrationDTOKlassen e WHERE e.Sichtbar IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Sortierung */
	public static final String QUERY_BY_SORTIERUNG = "SELECT e FROM MigrationDTOKlassen e WHERE e.Sortierung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Sortierung */
	public static final String QUERY_LIST_BY_SORTIERUNG = "SELECT e FROM MigrationDTOKlassen e WHERE e.Sortierung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Klassenart */
	public static final String QUERY_BY_KLASSENART = "SELECT e FROM MigrationDTOKlassen e WHERE e.Klassenart = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Klassenart */
	public static final String QUERY_LIST_BY_KLASSENART = "SELECT e FROM MigrationDTOKlassen e WHERE e.Klassenart IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes SommerSem */
	public static final String QUERY_BY_SOMMERSEM = "SELECT e FROM MigrationDTOKlassen e WHERE e.SommerSem = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes SommerSem */
	public static final String QUERY_LIST_BY_SOMMERSEM = "SELECT e FROM MigrationDTOKlassen e WHERE e.SommerSem IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes NotenGesperrt */
	public static final String QUERY_BY_NOTENGESPERRT = "SELECT e FROM MigrationDTOKlassen e WHERE e.NotenGesperrt = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes NotenGesperrt */
	public static final String QUERY_LIST_BY_NOTENGESPERRT = "SELECT e FROM MigrationDTOKlassen e WHERE e.NotenGesperrt IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes AdrMerkmal */
	public static final String QUERY_BY_ADRMERKMAL = "SELECT e FROM MigrationDTOKlassen e WHERE e.AdrMerkmal = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes AdrMerkmal */
	public static final String QUERY_LIST_BY_ADRMERKMAL = "SELECT e FROM MigrationDTOKlassen e WHERE e.AdrMerkmal IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes KoopKlasse */
	public static final String QUERY_BY_KOOPKLASSE = "SELECT e FROM MigrationDTOKlassen e WHERE e.KoopKlasse = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes KoopKlasse */
	public static final String QUERY_LIST_BY_KOOPKLASSE = "SELECT e FROM MigrationDTOKlassen e WHERE e.KoopKlasse IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Ankreuzzeugnisse */
	public static final String QUERY_BY_ANKREUZZEUGNISSE = "SELECT e FROM MigrationDTOKlassen e WHERE e.Ankreuzzeugnisse = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Ankreuzzeugnisse */
	public static final String QUERY_LIST_BY_ANKREUZZEUGNISSE = "SELECT e FROM MigrationDTOKlassen e WHERE e.Ankreuzzeugnisse IN ?1";

	/** ID der Klasse in der Klassen- Versetzuungstabelle */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** ID des Schuljahresabschnittes aus der Tabelle Schuljahresabschnitte */
	@Column(name = "Schuljahresabschnitts_ID")
	@JsonProperty
	public Long Schuljahresabschnitts_ID;

	/** Bezeichnender Text für die Klasse */
	@Column(name = "Bezeichnung")
	@JsonProperty
	public String Bezeichnung;

	/** ASD-Jahrgang der Klasse */
	@Column(name = "ASDKlasse")
	@JsonProperty
	public String ASDKlasse;

	/** Kürzel der Klasse */
	@Column(name = "Klasse")
	@JsonProperty
	public String Klasse;

	/** ID des ASD-Jahrgangs */
	@Column(name = "Jahrgang_ID")
	@JsonProperty
	public Long Jahrgang_ID;

	/** Folgeklasse */
	@Column(name = "FKlasse")
	@JsonProperty
	public String FKlasse;

	/** Vorgängerklasse */
	@Column(name = "VKlasse")
	@JsonProperty
	public String VKlasse;

	/** Organisationsform der Klasse Kürzel IT.NRW */
	@Column(name = "OrgFormKrz")
	@JsonProperty
	public String OrgFormKrz;

	/** Schulgliederung in der Klasse */
	@Column(name = "ASDSchulformNr")
	@JsonProperty
	public String ASDSchulformNr;

	/** FID des Fachklasse nur BK SBK */
	@Column(name = "Fachklasse_ID")
	@JsonProperty
	public Long Fachklasse_ID;

	/** Prüfungsordnung für die Klasse */
	@Column(name = "PruefOrdnung")
	@JsonProperty
	public String PruefOrdnung;

	/** DEPRECATED: Gibt an ob eine Klasse sichtbar ist */
	@Column(name = "Sichtbar")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean Sichtbar;

	/** Sortierungnummer der Klasse */
	@Column(name = "Sortierung")
	@JsonProperty
	public Integer Sortierung;

	/** Klassenart */
	@Column(name = "Klassenart")
	@JsonProperty
	public String Klassenart;

	/** Beginn im Sommersemester nur WBK */
	@Column(name = "SommerSem")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean SommerSem;

	/** Noteneingabe für die Klasse gesperrt */
	@Column(name = "NotenGesperrt")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean NotenGesperrt;

	/** Adressmerkmal des Teilstandorts für die Klasse */
	@Column(name = "AdrMerkmal")
	@JsonProperty
	public String AdrMerkmal;

	/** Gibt an ob die Klasse eine KOOP-Klasse ist */
	@Column(name = "KoopKlasse")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean KoopKlasse;

	/** Gibt an ob in der Klasse Ankreuzeugnisse (GS) oder Kompentenzschreiben (andere) verwendet werden */
	@Column(name = "Ankreuzzeugnisse")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean Ankreuzzeugnisse;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOKlassen ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOKlassen() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOKlassen ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Schuljahresabschnitts_ID   der Wert für das Attribut Schuljahresabschnitts_ID
	 * @param Klasse   der Wert für das Attribut Klasse
	 */
	public MigrationDTOKlassen(final Long ID, final Long Schuljahresabschnitts_ID, final String Klasse) {
		if (ID == null) {
			throw new NullPointerException("ID must not be null");
		}
		this.ID = ID;
		if (Schuljahresabschnitts_ID == null) {
			throw new NullPointerException("Schuljahresabschnitts_ID must not be null");
		}
		this.Schuljahresabschnitts_ID = Schuljahresabschnitts_ID;
		if (Klasse == null) {
			throw new NullPointerException("Klasse must not be null");
		}
		this.Klasse = Klasse;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MigrationDTOKlassen other = (MigrationDTOKlassen) obj;
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
		return "MigrationDTOKlassen(ID=" + this.ID + ", Schuljahresabschnitts_ID=" + this.Schuljahresabschnitts_ID + ", Bezeichnung=" + this.Bezeichnung + ", ASDKlasse=" + this.ASDKlasse + ", Klasse=" + this.Klasse + ", Jahrgang_ID=" + this.Jahrgang_ID + ", FKlasse=" + this.FKlasse + ", VKlasse=" + this.VKlasse + ", OrgFormKrz=" + this.OrgFormKrz + ", ASDSchulformNr=" + this.ASDSchulformNr + ", Fachklasse_ID=" + this.Fachklasse_ID + ", PruefOrdnung=" + this.PruefOrdnung + ", Sichtbar=" + this.Sichtbar + ", Sortierung=" + this.Sortierung + ", Klassenart=" + this.Klassenart + ", SommerSem=" + this.SommerSem + ", NotenGesperrt=" + this.NotenGesperrt + ", AdrMerkmal=" + this.AdrMerkmal + ", KoopKlasse=" + this.KoopKlasse + ", Ankreuzzeugnisse=" + this.Ankreuzzeugnisse + ")";
	}

}
