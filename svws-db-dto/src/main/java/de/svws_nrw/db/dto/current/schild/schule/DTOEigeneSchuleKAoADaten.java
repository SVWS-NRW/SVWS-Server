package de.svws_nrw.db.dto.current.schild.schule;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.current.BooleanPlusMinusDefaultPlusConverter;


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
import de.svws_nrw.csv.converter.current.BooleanPlusMinusDefaultPlusConverterSerializer;
import de.svws_nrw.csv.converter.current.BooleanPlusMinusDefaultPlusConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle EigeneSchule_KAoADaten.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "EigeneSchule_KAoADaten")
@JsonPropertyOrder({"ID", "Curriculum", "Koordinator", "Berufsorientierungsbuero", "KooperationsvereinbarungAA", "NutzungReflexionsworkshop", "NutzungEntscheidungskompetenzI", "NutzungEntscheidungskompetenzII"})
public final class DTOEigeneSchuleKAoADaten {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOEigeneSchuleKAoADaten e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOEigeneSchuleKAoADaten e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM DTOEigeneSchuleKAoADaten e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOEigeneSchuleKAoADaten e WHERE e.ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ID */
	public static final String QUERY_BY_ID = "SELECT e FROM DTOEigeneSchuleKAoADaten e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ID */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM DTOEigeneSchuleKAoADaten e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Curriculum */
	public static final String QUERY_BY_CURRICULUM = "SELECT e FROM DTOEigeneSchuleKAoADaten e WHERE e.Curriculum = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Curriculum */
	public static final String QUERY_LIST_BY_CURRICULUM = "SELECT e FROM DTOEigeneSchuleKAoADaten e WHERE e.Curriculum IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Koordinator */
	public static final String QUERY_BY_KOORDINATOR = "SELECT e FROM DTOEigeneSchuleKAoADaten e WHERE e.Koordinator = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Koordinator */
	public static final String QUERY_LIST_BY_KOORDINATOR = "SELECT e FROM DTOEigeneSchuleKAoADaten e WHERE e.Koordinator IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Berufsorientierungsbuero */
	public static final String QUERY_BY_BERUFSORIENTIERUNGSBUERO = "SELECT e FROM DTOEigeneSchuleKAoADaten e WHERE e.Berufsorientierungsbuero = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Berufsorientierungsbuero */
	public static final String QUERY_LIST_BY_BERUFSORIENTIERUNGSBUERO = "SELECT e FROM DTOEigeneSchuleKAoADaten e WHERE e.Berufsorientierungsbuero IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes KooperationsvereinbarungAA */
	public static final String QUERY_BY_KOOPERATIONSVEREINBARUNGAA = "SELECT e FROM DTOEigeneSchuleKAoADaten e WHERE e.KooperationsvereinbarungAA = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes KooperationsvereinbarungAA */
	public static final String QUERY_LIST_BY_KOOPERATIONSVEREINBARUNGAA = "SELECT e FROM DTOEigeneSchuleKAoADaten e WHERE e.KooperationsvereinbarungAA IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes NutzungReflexionsworkshop */
	public static final String QUERY_BY_NUTZUNGREFLEXIONSWORKSHOP = "SELECT e FROM DTOEigeneSchuleKAoADaten e WHERE e.NutzungReflexionsworkshop = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes NutzungReflexionsworkshop */
	public static final String QUERY_LIST_BY_NUTZUNGREFLEXIONSWORKSHOP = "SELECT e FROM DTOEigeneSchuleKAoADaten e WHERE e.NutzungReflexionsworkshop IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes NutzungEntscheidungskompetenzI */
	public static final String QUERY_BY_NUTZUNGENTSCHEIDUNGSKOMPETENZI = "SELECT e FROM DTOEigeneSchuleKAoADaten e WHERE e.NutzungEntscheidungskompetenzI = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes NutzungEntscheidungskompetenzI */
	public static final String QUERY_LIST_BY_NUTZUNGENTSCHEIDUNGSKOMPETENZI = "SELECT e FROM DTOEigeneSchuleKAoADaten e WHERE e.NutzungEntscheidungskompetenzI IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes NutzungEntscheidungskompetenzII */
	public static final String QUERY_BY_NUTZUNGENTSCHEIDUNGSKOMPETENZII = "SELECT e FROM DTOEigeneSchuleKAoADaten e WHERE e.NutzungEntscheidungskompetenzII = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes NutzungEntscheidungskompetenzII */
	public static final String QUERY_LIST_BY_NUTZUNGENTSCHEIDUNGSKOMPETENZII = "SELECT e FROM DTOEigeneSchuleKAoADaten e WHERE e.NutzungEntscheidungskompetenzII IN ?1";

	/** ID des KAoA-Dateneintrags für die Schule */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long ID;

	/** KAOA Curriculumsangaben */
	@Column(name = "Curriculum")
	@JsonProperty
	@Convert(converter = BooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean Curriculum;

	/** KAOA Koordinator */
	@Column(name = "Koordinator")
	@JsonProperty
	@Convert(converter = BooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean Koordinator;

	/** Schule hat Beruforientierungsbüro */
	@Column(name = "Berufsorientierungsbuero")
	@JsonProperty
	@Convert(converter = BooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean Berufsorientierungsbuero;

	/** Kooperationsvereinbarung KAOA geschlossen */
	@Column(name = "KooperationsvereinbarungAA")
	@JsonProperty
	@Convert(converter = BooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean KooperationsvereinbarungAA;

	/** Reflexionsworkshops werden genutzt */
	@Column(name = "NutzungReflexionsworkshop")
	@JsonProperty
	@Convert(converter = BooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean NutzungReflexionsworkshop;

	/** Nutzung der Entscheidungskompetenzen 1 */
	@Column(name = "NutzungEntscheidungskompetenzI")
	@JsonProperty
	@Convert(converter = BooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean NutzungEntscheidungskompetenzI;

	/** Nutzung der Entscheidungskompetenzen 2 */
	@Column(name = "NutzungEntscheidungskompetenzII")
	@JsonProperty
	@Convert(converter = BooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean NutzungEntscheidungskompetenzII;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOEigeneSchuleKAoADaten ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOEigeneSchuleKAoADaten() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOEigeneSchuleKAoADaten ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Curriculum   der Wert für das Attribut Curriculum
	 * @param Koordinator   der Wert für das Attribut Koordinator
	 * @param Berufsorientierungsbuero   der Wert für das Attribut Berufsorientierungsbuero
	 * @param KooperationsvereinbarungAA   der Wert für das Attribut KooperationsvereinbarungAA
	 * @param NutzungReflexionsworkshop   der Wert für das Attribut NutzungReflexionsworkshop
	 * @param NutzungEntscheidungskompetenzI   der Wert für das Attribut NutzungEntscheidungskompetenzI
	 * @param NutzungEntscheidungskompetenzII   der Wert für das Attribut NutzungEntscheidungskompetenzII
	 */
	public DTOEigeneSchuleKAoADaten(final long ID, final Boolean Curriculum, final Boolean Koordinator, final Boolean Berufsorientierungsbuero, final Boolean KooperationsvereinbarungAA, final Boolean NutzungReflexionsworkshop, final Boolean NutzungEntscheidungskompetenzI, final Boolean NutzungEntscheidungskompetenzII) {
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
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOEigeneSchuleKAoADaten other = (DTOEigeneSchuleKAoADaten) obj;
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
		return "DTOEigeneSchuleKAoADaten(ID=" + this.ID + ", Curriculum=" + this.Curriculum + ", Koordinator=" + this.Koordinator + ", Berufsorientierungsbuero=" + this.Berufsorientierungsbuero + ", KooperationsvereinbarungAA=" + this.KooperationsvereinbarungAA + ", NutzungReflexionsworkshop=" + this.NutzungReflexionsworkshop + ", NutzungEntscheidungskompetenzI=" + this.NutzungEntscheidungskompetenzI + ", NutzungEntscheidungskompetenzII=" + this.NutzungEntscheidungskompetenzII + ")";
	}

}
