package de.svws_nrw.db.dto.migration.schild.schule;

import de.svws_nrw.db.DBEntityManager;
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
import de.svws_nrw.csv.converter.migration.MigrationBooleanPlusMinusDefaultPlusConverterSerializer;
import de.svws_nrw.csv.converter.migration.MigrationBooleanPlusMinusDefaultPlusConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle EigeneSchule_KAoADaten.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "EigeneSchule_KAoADaten")
@JsonPropertyOrder({"ID", "Curriculum", "Koordinator", "Berufsorientierungsbuero", "KooperationsvereinbarungAA", "NutzungReflexionsworkshop", "NutzungEntscheidungskompetenzI", "NutzungEntscheidungskompetenzII", "SchulnrEigner"})
public final class MigrationDTOEigeneSchuleKAoADaten {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM MigrationDTOEigeneSchuleKAoADaten e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM MigrationDTOEigeneSchuleKAoADaten e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM MigrationDTOEigeneSchuleKAoADaten e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM MigrationDTOEigeneSchuleKAoADaten e WHERE e.ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ID */
	public static final String QUERY_BY_ID = "SELECT e FROM MigrationDTOEigeneSchuleKAoADaten e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ID */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM MigrationDTOEigeneSchuleKAoADaten e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Curriculum */
	public static final String QUERY_BY_CURRICULUM = "SELECT e FROM MigrationDTOEigeneSchuleKAoADaten e WHERE e.Curriculum = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Curriculum */
	public static final String QUERY_LIST_BY_CURRICULUM = "SELECT e FROM MigrationDTOEigeneSchuleKAoADaten e WHERE e.Curriculum IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Koordinator */
	public static final String QUERY_BY_KOORDINATOR = "SELECT e FROM MigrationDTOEigeneSchuleKAoADaten e WHERE e.Koordinator = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Koordinator */
	public static final String QUERY_LIST_BY_KOORDINATOR = "SELECT e FROM MigrationDTOEigeneSchuleKAoADaten e WHERE e.Koordinator IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Berufsorientierungsbuero */
	public static final String QUERY_BY_BERUFSORIENTIERUNGSBUERO = "SELECT e FROM MigrationDTOEigeneSchuleKAoADaten e WHERE e.Berufsorientierungsbuero = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Berufsorientierungsbuero */
	public static final String QUERY_LIST_BY_BERUFSORIENTIERUNGSBUERO = "SELECT e FROM MigrationDTOEigeneSchuleKAoADaten e WHERE e.Berufsorientierungsbuero IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes KooperationsvereinbarungAA */
	public static final String QUERY_BY_KOOPERATIONSVEREINBARUNGAA = "SELECT e FROM MigrationDTOEigeneSchuleKAoADaten e WHERE e.KooperationsvereinbarungAA = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes KooperationsvereinbarungAA */
	public static final String QUERY_LIST_BY_KOOPERATIONSVEREINBARUNGAA = "SELECT e FROM MigrationDTOEigeneSchuleKAoADaten e WHERE e.KooperationsvereinbarungAA IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes NutzungReflexionsworkshop */
	public static final String QUERY_BY_NUTZUNGREFLEXIONSWORKSHOP = "SELECT e FROM MigrationDTOEigeneSchuleKAoADaten e WHERE e.NutzungReflexionsworkshop = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes NutzungReflexionsworkshop */
	public static final String QUERY_LIST_BY_NUTZUNGREFLEXIONSWORKSHOP = "SELECT e FROM MigrationDTOEigeneSchuleKAoADaten e WHERE e.NutzungReflexionsworkshop IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes NutzungEntscheidungskompetenzI */
	public static final String QUERY_BY_NUTZUNGENTSCHEIDUNGSKOMPETENZI = "SELECT e FROM MigrationDTOEigeneSchuleKAoADaten e WHERE e.NutzungEntscheidungskompetenzI = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes NutzungEntscheidungskompetenzI */
	public static final String QUERY_LIST_BY_NUTZUNGENTSCHEIDUNGSKOMPETENZI = "SELECT e FROM MigrationDTOEigeneSchuleKAoADaten e WHERE e.NutzungEntscheidungskompetenzI IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes NutzungEntscheidungskompetenzII */
	public static final String QUERY_BY_NUTZUNGENTSCHEIDUNGSKOMPETENZII = "SELECT e FROM MigrationDTOEigeneSchuleKAoADaten e WHERE e.NutzungEntscheidungskompetenzII = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes NutzungEntscheidungskompetenzII */
	public static final String QUERY_LIST_BY_NUTZUNGENTSCHEIDUNGSKOMPETENZII = "SELECT e FROM MigrationDTOEigeneSchuleKAoADaten e WHERE e.NutzungEntscheidungskompetenzII IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes SchulnrEigner */
	public static final String QUERY_BY_SCHULNREIGNER = "SELECT e FROM MigrationDTOEigeneSchuleKAoADaten e WHERE e.SchulnrEigner = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes SchulnrEigner */
	public static final String QUERY_LIST_BY_SCHULNREIGNER = "SELECT e FROM MigrationDTOEigeneSchuleKAoADaten e WHERE e.SchulnrEigner IN ?1";

	/** ID des KAoA-Dateneintrags für die Schule */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** KAOA Curriculumsangaben */
	@Column(name = "Curriculum")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean Curriculum;

	/** KAOA Koordinator */
	@Column(name = "Koordinator")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean Koordinator;

	/** Schule hat Beruforientierungsbüro */
	@Column(name = "Berufsorientierungsbuero")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean Berufsorientierungsbuero;

	/** Kooperationsvereinbarung KAOA geschlossen */
	@Column(name = "KooperationsvereinbarungAA")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean KooperationsvereinbarungAA;

	/** Reflexionsworkshops werden genutzt */
	@Column(name = "NutzungReflexionsworkshop")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean NutzungReflexionsworkshop;

	/** Nutzung der Entscheidungskompetenzen 1 */
	@Column(name = "NutzungEntscheidungskompetenzI")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean NutzungEntscheidungskompetenzI;

	/** Nutzung der Entscheidungskompetenzen 2 */
	@Column(name = "NutzungEntscheidungskompetenzII")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean NutzungEntscheidungskompetenzII;

	/** Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden */
	@Column(name = "SchulnrEigner")
	@JsonProperty
	public Integer SchulnrEigner;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOEigeneSchuleKAoADaten ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOEigeneSchuleKAoADaten() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOEigeneSchuleKAoADaten ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Curriculum   der Wert für das Attribut Curriculum
	 * @param Koordinator   der Wert für das Attribut Koordinator
	 * @param Berufsorientierungsbuero   der Wert für das Attribut Berufsorientierungsbuero
	 * @param KooperationsvereinbarungAA   der Wert für das Attribut KooperationsvereinbarungAA
	 * @param NutzungReflexionsworkshop   der Wert für das Attribut NutzungReflexionsworkshop
	 * @param NutzungEntscheidungskompetenzI   der Wert für das Attribut NutzungEntscheidungskompetenzI
	 * @param NutzungEntscheidungskompetenzII   der Wert für das Attribut NutzungEntscheidungskompetenzII
	 * @param SchulnrEigner   der Wert für das Attribut SchulnrEigner
	 */
	public MigrationDTOEigeneSchuleKAoADaten(final Long ID, final Boolean Curriculum, final Boolean Koordinator, final Boolean Berufsorientierungsbuero, final Boolean KooperationsvereinbarungAA, final Boolean NutzungReflexionsworkshop, final Boolean NutzungEntscheidungskompetenzI, final Boolean NutzungEntscheidungskompetenzII, final Integer SchulnrEigner) {
		if (ID == null) {
			throw new NullPointerException("ID must not be null");
		}
		this.ID = ID;
		if (Curriculum == null) {
			throw new NullPointerException("Curriculum must not be null");
		}
		this.Curriculum = Curriculum;
		if (Koordinator == null) {
			throw new NullPointerException("Koordinator must not be null");
		}
		this.Koordinator = Koordinator;
		if (Berufsorientierungsbuero == null) {
			throw new NullPointerException("Berufsorientierungsbuero must not be null");
		}
		this.Berufsorientierungsbuero = Berufsorientierungsbuero;
		if (KooperationsvereinbarungAA == null) {
			throw new NullPointerException("KooperationsvereinbarungAA must not be null");
		}
		this.KooperationsvereinbarungAA = KooperationsvereinbarungAA;
		if (NutzungReflexionsworkshop == null) {
			throw new NullPointerException("NutzungReflexionsworkshop must not be null");
		}
		this.NutzungReflexionsworkshop = NutzungReflexionsworkshop;
		if (NutzungEntscheidungskompetenzI == null) {
			throw new NullPointerException("NutzungEntscheidungskompetenzI must not be null");
		}
		this.NutzungEntscheidungskompetenzI = NutzungEntscheidungskompetenzI;
		if (NutzungEntscheidungskompetenzII == null) {
			throw new NullPointerException("NutzungEntscheidungskompetenzII must not be null");
		}
		this.NutzungEntscheidungskompetenzII = NutzungEntscheidungskompetenzII;
		if (SchulnrEigner == null) {
			throw new NullPointerException("SchulnrEigner must not be null");
		}
		this.SchulnrEigner = SchulnrEigner;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MigrationDTOEigeneSchuleKAoADaten other = (MigrationDTOEigeneSchuleKAoADaten) obj;
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
		return "MigrationDTOEigeneSchuleKAoADaten(ID=" + this.ID + ", Curriculum=" + this.Curriculum + ", Koordinator=" + this.Koordinator + ", Berufsorientierungsbuero=" + this.Berufsorientierungsbuero + ", KooperationsvereinbarungAA=" + this.KooperationsvereinbarungAA + ", NutzungReflexionsworkshop=" + this.NutzungReflexionsworkshop + ", NutzungEntscheidungskompetenzI=" + this.NutzungEntscheidungskompetenzI + ", NutzungEntscheidungskompetenzII=" + this.NutzungEntscheidungskompetenzII + ", SchulnrEigner=" + this.SchulnrEigner + ")";
	}

}
