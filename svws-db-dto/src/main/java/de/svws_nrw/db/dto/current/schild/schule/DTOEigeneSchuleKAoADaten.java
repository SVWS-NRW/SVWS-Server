package de.svws_nrw.db.dto.current.schild.schule;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.current.BooleanPlusMinusDefaultPlusConverter;


import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
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
@NamedQuery(name = "DTOEigeneSchuleKAoADaten.all", query = "SELECT e FROM DTOEigeneSchuleKAoADaten e")
@NamedQuery(name = "DTOEigeneSchuleKAoADaten.id", query = "SELECT e FROM DTOEigeneSchuleKAoADaten e WHERE e.ID = :value")
@NamedQuery(name = "DTOEigeneSchuleKAoADaten.id.multiple", query = "SELECT e FROM DTOEigeneSchuleKAoADaten e WHERE e.ID IN :value")
@NamedQuery(name = "DTOEigeneSchuleKAoADaten.curriculum", query = "SELECT e FROM DTOEigeneSchuleKAoADaten e WHERE e.Curriculum = :value")
@NamedQuery(name = "DTOEigeneSchuleKAoADaten.curriculum.multiple", query = "SELECT e FROM DTOEigeneSchuleKAoADaten e WHERE e.Curriculum IN :value")
@NamedQuery(name = "DTOEigeneSchuleKAoADaten.koordinator", query = "SELECT e FROM DTOEigeneSchuleKAoADaten e WHERE e.Koordinator = :value")
@NamedQuery(name = "DTOEigeneSchuleKAoADaten.koordinator.multiple", query = "SELECT e FROM DTOEigeneSchuleKAoADaten e WHERE e.Koordinator IN :value")
@NamedQuery(name = "DTOEigeneSchuleKAoADaten.berufsorientierungsbuero", query = "SELECT e FROM DTOEigeneSchuleKAoADaten e WHERE e.Berufsorientierungsbuero = :value")
@NamedQuery(name = "DTOEigeneSchuleKAoADaten.berufsorientierungsbuero.multiple", query = "SELECT e FROM DTOEigeneSchuleKAoADaten e WHERE e.Berufsorientierungsbuero IN :value")
@NamedQuery(name = "DTOEigeneSchuleKAoADaten.kooperationsvereinbarungaa", query = "SELECT e FROM DTOEigeneSchuleKAoADaten e WHERE e.KooperationsvereinbarungAA = :value")
@NamedQuery(name = "DTOEigeneSchuleKAoADaten.kooperationsvereinbarungaa.multiple", query = "SELECT e FROM DTOEigeneSchuleKAoADaten e WHERE e.KooperationsvereinbarungAA IN :value")
@NamedQuery(name = "DTOEigeneSchuleKAoADaten.nutzungreflexionsworkshop", query = "SELECT e FROM DTOEigeneSchuleKAoADaten e WHERE e.NutzungReflexionsworkshop = :value")
@NamedQuery(name = "DTOEigeneSchuleKAoADaten.nutzungreflexionsworkshop.multiple", query = "SELECT e FROM DTOEigeneSchuleKAoADaten e WHERE e.NutzungReflexionsworkshop IN :value")
@NamedQuery(name = "DTOEigeneSchuleKAoADaten.nutzungentscheidungskompetenzi", query = "SELECT e FROM DTOEigeneSchuleKAoADaten e WHERE e.NutzungEntscheidungskompetenzI = :value")
@NamedQuery(name = "DTOEigeneSchuleKAoADaten.nutzungentscheidungskompetenzi.multiple", query = "SELECT e FROM DTOEigeneSchuleKAoADaten e WHERE e.NutzungEntscheidungskompetenzI IN :value")
@NamedQuery(name = "DTOEigeneSchuleKAoADaten.nutzungentscheidungskompetenzii", query = "SELECT e FROM DTOEigeneSchuleKAoADaten e WHERE e.NutzungEntscheidungskompetenzII = :value")
@NamedQuery(name = "DTOEigeneSchuleKAoADaten.nutzungentscheidungskompetenzii.multiple", query = "SELECT e FROM DTOEigeneSchuleKAoADaten e WHERE e.NutzungEntscheidungskompetenzII IN :value")
@NamedQuery(name = "DTOEigeneSchuleKAoADaten.primaryKeyQuery", query = "SELECT e FROM DTOEigeneSchuleKAoADaten e WHERE e.ID = ?1")
@NamedQuery(name = "DTOEigeneSchuleKAoADaten.primaryKeyQuery.multiple", query = "SELECT e FROM DTOEigeneSchuleKAoADaten e WHERE e.ID IN ?1")
@NamedQuery(name = "DTOEigeneSchuleKAoADaten.all.migration", query = "SELECT e FROM DTOEigeneSchuleKAoADaten e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID", "Curriculum", "Koordinator", "Berufsorientierungsbuero", "KooperationsvereinbarungAA", "NutzungReflexionsworkshop", "NutzungEntscheidungskompetenzI", "NutzungEntscheidungskompetenzII"})
public final class DTOEigeneSchuleKAoADaten {

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
