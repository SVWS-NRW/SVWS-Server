package de.svws_nrw.db.dto.migration.schild.schule;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.migration.MigrationBooleanPlusMinusDefaultPlusConverter;


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
@NamedQuery(name="MigrationDTOEigeneSchuleKAoADaten.all", query="SELECT e FROM MigrationDTOEigeneSchuleKAoADaten e")
@NamedQuery(name="MigrationDTOEigeneSchuleKAoADaten.id", query="SELECT e FROM MigrationDTOEigeneSchuleKAoADaten e WHERE e.ID = :value")
@NamedQuery(name="MigrationDTOEigeneSchuleKAoADaten.id.multiple", query="SELECT e FROM MigrationDTOEigeneSchuleKAoADaten e WHERE e.ID IN :value")
@NamedQuery(name="MigrationDTOEigeneSchuleKAoADaten.curriculum", query="SELECT e FROM MigrationDTOEigeneSchuleKAoADaten e WHERE e.Curriculum = :value")
@NamedQuery(name="MigrationDTOEigeneSchuleKAoADaten.curriculum.multiple", query="SELECT e FROM MigrationDTOEigeneSchuleKAoADaten e WHERE e.Curriculum IN :value")
@NamedQuery(name="MigrationDTOEigeneSchuleKAoADaten.koordinator", query="SELECT e FROM MigrationDTOEigeneSchuleKAoADaten e WHERE e.Koordinator = :value")
@NamedQuery(name="MigrationDTOEigeneSchuleKAoADaten.koordinator.multiple", query="SELECT e FROM MigrationDTOEigeneSchuleKAoADaten e WHERE e.Koordinator IN :value")
@NamedQuery(name="MigrationDTOEigeneSchuleKAoADaten.berufsorientierungsbuero", query="SELECT e FROM MigrationDTOEigeneSchuleKAoADaten e WHERE e.Berufsorientierungsbuero = :value")
@NamedQuery(name="MigrationDTOEigeneSchuleKAoADaten.berufsorientierungsbuero.multiple", query="SELECT e FROM MigrationDTOEigeneSchuleKAoADaten e WHERE e.Berufsorientierungsbuero IN :value")
@NamedQuery(name="MigrationDTOEigeneSchuleKAoADaten.kooperationsvereinbarungaa", query="SELECT e FROM MigrationDTOEigeneSchuleKAoADaten e WHERE e.KooperationsvereinbarungAA = :value")
@NamedQuery(name="MigrationDTOEigeneSchuleKAoADaten.kooperationsvereinbarungaa.multiple", query="SELECT e FROM MigrationDTOEigeneSchuleKAoADaten e WHERE e.KooperationsvereinbarungAA IN :value")
@NamedQuery(name="MigrationDTOEigeneSchuleKAoADaten.nutzungreflexionsworkshop", query="SELECT e FROM MigrationDTOEigeneSchuleKAoADaten e WHERE e.NutzungReflexionsworkshop = :value")
@NamedQuery(name="MigrationDTOEigeneSchuleKAoADaten.nutzungreflexionsworkshop.multiple", query="SELECT e FROM MigrationDTOEigeneSchuleKAoADaten e WHERE e.NutzungReflexionsworkshop IN :value")
@NamedQuery(name="MigrationDTOEigeneSchuleKAoADaten.nutzungentscheidungskompetenzi", query="SELECT e FROM MigrationDTOEigeneSchuleKAoADaten e WHERE e.NutzungEntscheidungskompetenzI = :value")
@NamedQuery(name="MigrationDTOEigeneSchuleKAoADaten.nutzungentscheidungskompetenzi.multiple", query="SELECT e FROM MigrationDTOEigeneSchuleKAoADaten e WHERE e.NutzungEntscheidungskompetenzI IN :value")
@NamedQuery(name="MigrationDTOEigeneSchuleKAoADaten.nutzungentscheidungskompetenzii", query="SELECT e FROM MigrationDTOEigeneSchuleKAoADaten e WHERE e.NutzungEntscheidungskompetenzII = :value")
@NamedQuery(name="MigrationDTOEigeneSchuleKAoADaten.nutzungentscheidungskompetenzii.multiple", query="SELECT e FROM MigrationDTOEigeneSchuleKAoADaten e WHERE e.NutzungEntscheidungskompetenzII IN :value")
@NamedQuery(name="MigrationDTOEigeneSchuleKAoADaten.schulnreigner", query="SELECT e FROM MigrationDTOEigeneSchuleKAoADaten e WHERE e.SchulnrEigner = :value")
@NamedQuery(name="MigrationDTOEigeneSchuleKAoADaten.schulnreigner.multiple", query="SELECT e FROM MigrationDTOEigeneSchuleKAoADaten e WHERE e.SchulnrEigner IN :value")
@NamedQuery(name="MigrationDTOEigeneSchuleKAoADaten.primaryKeyQuery", query="SELECT e FROM MigrationDTOEigeneSchuleKAoADaten e WHERE e.ID = ?1")
@NamedQuery(name="MigrationDTOEigeneSchuleKAoADaten.all.migration", query="SELECT e FROM MigrationDTOEigeneSchuleKAoADaten e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID","Curriculum","Koordinator","Berufsorientierungsbuero","KooperationsvereinbarungAA","NutzungReflexionsworkshop","NutzungEntscheidungskompetenzI","NutzungEntscheidungskompetenzII","SchulnrEigner"})
public class MigrationDTOEigeneSchuleKAoADaten {

	/** ID des KAoA-Dateneintrags für die Schule */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** KAOA Curriculumsangaben */
	@Column(name = "Curriculum")
	@JsonProperty
	@Convert(converter=MigrationBooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using=MigrationBooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using=MigrationBooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean Curriculum;

	/** KAOA Koordinator */
	@Column(name = "Koordinator")
	@JsonProperty
	@Convert(converter=MigrationBooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using=MigrationBooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using=MigrationBooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean Koordinator;

	/** Schule hat Beruforientierungsbüro */
	@Column(name = "Berufsorientierungsbuero")
	@JsonProperty
	@Convert(converter=MigrationBooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using=MigrationBooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using=MigrationBooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean Berufsorientierungsbuero;

	/** Kooperationsvereinbarung KAOA geschlossen */
	@Column(name = "KooperationsvereinbarungAA")
	@JsonProperty
	@Convert(converter=MigrationBooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using=MigrationBooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using=MigrationBooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean KooperationsvereinbarungAA;

	/** Reflexionsworkshops werden genutzt */
	@Column(name = "NutzungReflexionsworkshop")
	@JsonProperty
	@Convert(converter=MigrationBooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using=MigrationBooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using=MigrationBooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean NutzungReflexionsworkshop;

	/** Nutzung der Entscheidungskompetenzen 1 */
	@Column(name = "NutzungEntscheidungskompetenzI")
	@JsonProperty
	@Convert(converter=MigrationBooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using=MigrationBooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using=MigrationBooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean NutzungEntscheidungskompetenzI;

	/** Nutzung der Entscheidungskompetenzen 2 */
	@Column(name = "NutzungEntscheidungskompetenzII")
	@JsonProperty
	@Convert(converter=MigrationBooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using=MigrationBooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using=MigrationBooleanPlusMinusDefaultPlusConverterDeserializer.class)
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
	public boolean equals(Object obj) {
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