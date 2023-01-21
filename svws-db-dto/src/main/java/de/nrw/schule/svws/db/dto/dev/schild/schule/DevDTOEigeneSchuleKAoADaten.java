package de.nrw.schule.svws.db.dto.dev.schild.schule;

import de.nrw.schule.svws.db.DBEntityManager;
import de.nrw.schule.svws.db.converter.current.BooleanPlusMinusDefaultPlusConverter;


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
import de.nrw.schule.svws.csv.converter.current.BooleanPlusMinusDefaultPlusConverterSerializer;
import de.nrw.schule.svws.csv.converter.current.BooleanPlusMinusDefaultPlusConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle EigeneSchule_KAoADaten.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "EigeneSchule_KAoADaten")
@NamedQuery(name="DevDTOEigeneSchuleKAoADaten.all", query="SELECT e FROM DevDTOEigeneSchuleKAoADaten e")
@NamedQuery(name="DevDTOEigeneSchuleKAoADaten.id", query="SELECT e FROM DevDTOEigeneSchuleKAoADaten e WHERE e.ID = :value")
@NamedQuery(name="DevDTOEigeneSchuleKAoADaten.id.multiple", query="SELECT e FROM DevDTOEigeneSchuleKAoADaten e WHERE e.ID IN :value")
@NamedQuery(name="DevDTOEigeneSchuleKAoADaten.curriculum", query="SELECT e FROM DevDTOEigeneSchuleKAoADaten e WHERE e.Curriculum = :value")
@NamedQuery(name="DevDTOEigeneSchuleKAoADaten.curriculum.multiple", query="SELECT e FROM DevDTOEigeneSchuleKAoADaten e WHERE e.Curriculum IN :value")
@NamedQuery(name="DevDTOEigeneSchuleKAoADaten.koordinator", query="SELECT e FROM DevDTOEigeneSchuleKAoADaten e WHERE e.Koordinator = :value")
@NamedQuery(name="DevDTOEigeneSchuleKAoADaten.koordinator.multiple", query="SELECT e FROM DevDTOEigeneSchuleKAoADaten e WHERE e.Koordinator IN :value")
@NamedQuery(name="DevDTOEigeneSchuleKAoADaten.berufsorientierungsbuero", query="SELECT e FROM DevDTOEigeneSchuleKAoADaten e WHERE e.Berufsorientierungsbuero = :value")
@NamedQuery(name="DevDTOEigeneSchuleKAoADaten.berufsorientierungsbuero.multiple", query="SELECT e FROM DevDTOEigeneSchuleKAoADaten e WHERE e.Berufsorientierungsbuero IN :value")
@NamedQuery(name="DevDTOEigeneSchuleKAoADaten.kooperationsvereinbarungaa", query="SELECT e FROM DevDTOEigeneSchuleKAoADaten e WHERE e.KooperationsvereinbarungAA = :value")
@NamedQuery(name="DevDTOEigeneSchuleKAoADaten.kooperationsvereinbarungaa.multiple", query="SELECT e FROM DevDTOEigeneSchuleKAoADaten e WHERE e.KooperationsvereinbarungAA IN :value")
@NamedQuery(name="DevDTOEigeneSchuleKAoADaten.nutzungreflexionsworkshop", query="SELECT e FROM DevDTOEigeneSchuleKAoADaten e WHERE e.NutzungReflexionsworkshop = :value")
@NamedQuery(name="DevDTOEigeneSchuleKAoADaten.nutzungreflexionsworkshop.multiple", query="SELECT e FROM DevDTOEigeneSchuleKAoADaten e WHERE e.NutzungReflexionsworkshop IN :value")
@NamedQuery(name="DevDTOEigeneSchuleKAoADaten.nutzungentscheidungskompetenzi", query="SELECT e FROM DevDTOEigeneSchuleKAoADaten e WHERE e.NutzungEntscheidungskompetenzI = :value")
@NamedQuery(name="DevDTOEigeneSchuleKAoADaten.nutzungentscheidungskompetenzi.multiple", query="SELECT e FROM DevDTOEigeneSchuleKAoADaten e WHERE e.NutzungEntscheidungskompetenzI IN :value")
@NamedQuery(name="DevDTOEigeneSchuleKAoADaten.nutzungentscheidungskompetenzii", query="SELECT e FROM DevDTOEigeneSchuleKAoADaten e WHERE e.NutzungEntscheidungskompetenzII = :value")
@NamedQuery(name="DevDTOEigeneSchuleKAoADaten.nutzungentscheidungskompetenzii.multiple", query="SELECT e FROM DevDTOEigeneSchuleKAoADaten e WHERE e.NutzungEntscheidungskompetenzII IN :value")
@NamedQuery(name="DevDTOEigeneSchuleKAoADaten.primaryKeyQuery", query="SELECT e FROM DevDTOEigeneSchuleKAoADaten e WHERE e.ID = ?1")
@NamedQuery(name="DevDTOEigeneSchuleKAoADaten.all.migration", query="SELECT e FROM DevDTOEigeneSchuleKAoADaten e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID","Curriculum","Koordinator","Berufsorientierungsbuero","KooperationsvereinbarungAA","NutzungReflexionsworkshop","NutzungEntscheidungskompetenzI","NutzungEntscheidungskompetenzII"})
public class DevDTOEigeneSchuleKAoADaten {

	/** ID des KAoA-Dateneintrags für die Schule */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** KAOA Curriculumsangaben */
	@Column(name = "Curriculum")
	@JsonProperty
	@Convert(converter=BooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using=BooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using=BooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean Curriculum;

	/** KAOA Koordinator */
	@Column(name = "Koordinator")
	@JsonProperty
	@Convert(converter=BooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using=BooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using=BooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean Koordinator;

	/** Schule hat Beruforientierungsbüro */
	@Column(name = "Berufsorientierungsbuero")
	@JsonProperty
	@Convert(converter=BooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using=BooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using=BooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean Berufsorientierungsbuero;

	/** Kooperationsvereinbarung KAOA geschlossen */
	@Column(name = "KooperationsvereinbarungAA")
	@JsonProperty
	@Convert(converter=BooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using=BooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using=BooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean KooperationsvereinbarungAA;

	/** Reflexionsworkshops werden genutzt */
	@Column(name = "NutzungReflexionsworkshop")
	@JsonProperty
	@Convert(converter=BooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using=BooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using=BooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean NutzungReflexionsworkshop;

	/** Nutzung der Entscheidungskompetenzen 1 */
	@Column(name = "NutzungEntscheidungskompetenzI")
	@JsonProperty
	@Convert(converter=BooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using=BooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using=BooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean NutzungEntscheidungskompetenzI;

	/** Nutzung der Entscheidungskompetenzen 2 */
	@Column(name = "NutzungEntscheidungskompetenzII")
	@JsonProperty
	@Convert(converter=BooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using=BooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using=BooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean NutzungEntscheidungskompetenzII;

	/**
	 * Erstellt ein neues Objekt der Klasse DevDTOEigeneSchuleKAoADaten ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DevDTOEigeneSchuleKAoADaten() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DevDTOEigeneSchuleKAoADaten ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Curriculum   der Wert für das Attribut Curriculum
	 * @param Koordinator   der Wert für das Attribut Koordinator
	 * @param Berufsorientierungsbuero   der Wert für das Attribut Berufsorientierungsbuero
	 * @param KooperationsvereinbarungAA   der Wert für das Attribut KooperationsvereinbarungAA
	 * @param NutzungReflexionsworkshop   der Wert für das Attribut NutzungReflexionsworkshop
	 * @param NutzungEntscheidungskompetenzI   der Wert für das Attribut NutzungEntscheidungskompetenzI
	 * @param NutzungEntscheidungskompetenzII   der Wert für das Attribut NutzungEntscheidungskompetenzII
	 */
	public DevDTOEigeneSchuleKAoADaten(final Long ID, final Boolean Curriculum, final Boolean Koordinator, final Boolean Berufsorientierungsbuero, final Boolean KooperationsvereinbarungAA, final Boolean NutzungReflexionsworkshop, final Boolean NutzungEntscheidungskompetenzI, final Boolean NutzungEntscheidungskompetenzII) {
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
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DevDTOEigeneSchuleKAoADaten other = (DevDTOEigeneSchuleKAoADaten) obj;
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
		return "DevDTOEigeneSchuleKAoADaten(ID=" + this.ID + ", Curriculum=" + this.Curriculum + ", Koordinator=" + this.Koordinator + ", Berufsorientierungsbuero=" + this.Berufsorientierungsbuero + ", KooperationsvereinbarungAA=" + this.KooperationsvereinbarungAA + ", NutzungReflexionsworkshop=" + this.NutzungReflexionsworkshop + ", NutzungEntscheidungskompetenzI=" + this.NutzungEntscheidungskompetenzI + ", NutzungEntscheidungskompetenzII=" + this.NutzungEntscheidungskompetenzII + ")";
	}

}