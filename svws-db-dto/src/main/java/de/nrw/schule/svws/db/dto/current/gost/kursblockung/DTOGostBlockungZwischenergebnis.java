package de.nrw.schule.svws.db.dto.current.gost.kursblockung;

import de.nrw.schule.svws.db.DBEntityManager;
import de.nrw.schule.svws.db.converter.current.Boolean01Converter;


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
import de.nrw.schule.svws.csv.converter.current.Boolean01ConverterSerializer;
import de.nrw.schule.svws.csv.converter.current.Boolean01ConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle Gost_Blockung_Zwischenergebnisse.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Gost_Blockung_Zwischenergebnisse")
@NamedQuery(name="DTOGostBlockungZwischenergebnis.all", query="SELECT e FROM DTOGostBlockungZwischenergebnis e")
@NamedQuery(name="DTOGostBlockungZwischenergebnis.id", query="SELECT e FROM DTOGostBlockungZwischenergebnis e WHERE e.ID = :value")
@NamedQuery(name="DTOGostBlockungZwischenergebnis.id.multiple", query="SELECT e FROM DTOGostBlockungZwischenergebnis e WHERE e.ID IN :value")
@NamedQuery(name="DTOGostBlockungZwischenergebnis.blockung_id", query="SELECT e FROM DTOGostBlockungZwischenergebnis e WHERE e.Blockung_ID = :value")
@NamedQuery(name="DTOGostBlockungZwischenergebnis.blockung_id.multiple", query="SELECT e FROM DTOGostBlockungZwischenergebnis e WHERE e.Blockung_ID IN :value")
@NamedQuery(name="DTOGostBlockungZwischenergebnis.anzahlumwaehler", query="SELECT e FROM DTOGostBlockungZwischenergebnis e WHERE e.AnzahlUmwaehler = :value")
@NamedQuery(name="DTOGostBlockungZwischenergebnis.anzahlumwaehler.multiple", query="SELECT e FROM DTOGostBlockungZwischenergebnis e WHERE e.AnzahlUmwaehler IN :value")
@NamedQuery(name="DTOGostBlockungZwischenergebnis.bewertung", query="SELECT e FROM DTOGostBlockungZwischenergebnis e WHERE e.Bewertung = :value")
@NamedQuery(name="DTOGostBlockungZwischenergebnis.bewertung.multiple", query="SELECT e FROM DTOGostBlockungZwischenergebnis e WHERE e.Bewertung IN :value")
@NamedQuery(name="DTOGostBlockungZwischenergebnis.istmarkiert", query="SELECT e FROM DTOGostBlockungZwischenergebnis e WHERE e.IstMarkiert = :value")
@NamedQuery(name="DTOGostBlockungZwischenergebnis.istmarkiert.multiple", query="SELECT e FROM DTOGostBlockungZwischenergebnis e WHERE e.IstMarkiert IN :value")
@NamedQuery(name="DTOGostBlockungZwischenergebnis.istdupliziert", query="SELECT e FROM DTOGostBlockungZwischenergebnis e WHERE e.IstDupliziert = :value")
@NamedQuery(name="DTOGostBlockungZwischenergebnis.istdupliziert.multiple", query="SELECT e FROM DTOGostBlockungZwischenergebnis e WHERE e.IstDupliziert IN :value")
@NamedQuery(name="DTOGostBlockungZwischenergebnis.primaryKeyQuery", query="SELECT e FROM DTOGostBlockungZwischenergebnis e WHERE e.ID = ?1")
@NamedQuery(name="DTOGostBlockungZwischenergebnis.all.migration", query="SELECT e FROM DTOGostBlockungZwischenergebnis e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID","Blockung_ID","AnzahlUmwaehler","Bewertung","IstMarkiert","IstDupliziert"})
public class DTOGostBlockungZwischenergebnis {

	/** Kursblockung der Gymnasialen Oberstufe - Zwischenergebnis einer Blockung: ID der Zwischenergebnisses (generiert) */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** Kursblockung der Gymnasialen Oberstufe - Zwischenergebnis einer Blockung: ID der Blockung */
	@Column(name = "Blockung_ID")
	@JsonProperty
	public Long Blockung_ID;

	/** Kursblockung der Gymnasialen Oberstufe - Zwischenergebnis einer Blockung: Die Anzahl der Umwähler */
	@Column(name = "AnzahlUmwaehler")
	@JsonProperty
	public Integer AnzahlUmwaehler;

	/** Kursblockung der Gymnasialen Oberstufe - Zwischenergebnis einer Blockung: Ein Wert zur Bewertung der Blockung */
	@Column(name = "Bewertung")
	@JsonProperty
	public Long Bewertung;

	/** Kursblockung der Gymnasialen Oberstufe - Zwischenergebnis einer Blockung: Gibt an, ob das Zwischenergebnis von einem Benutzer markiert wurde oder nicht: 1 - true, 0 - false  */
	@Column(name = "IstMarkiert")
	@JsonProperty
	@Convert(converter=Boolean01Converter.class)
	@JsonSerialize(using=Boolean01ConverterSerializer.class)
	@JsonDeserialize(using=Boolean01ConverterDeserializer.class)
	public Boolean IstMarkiert;

	/** Kursblockung der Gymnasialen Oberstufe - Zwischenergebnis einer Blockung: Gibt an, ob das Zwischenergebnis mit einer anderen Blockungsdefinition dupliziert wurde und nur als Vorlage für Regeldefinitionen dient oder nicht: 1 - true, 0 - false  */
	@Column(name = "IstDupliziert")
	@JsonProperty
	@Convert(converter=Boolean01Converter.class)
	@JsonSerialize(using=Boolean01ConverterSerializer.class)
	@JsonDeserialize(using=Boolean01ConverterDeserializer.class)
	public Boolean IstDupliziert;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOGostBlockungZwischenergebnis ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOGostBlockungZwischenergebnis() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOGostBlockungZwischenergebnis ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Blockung_ID   der Wert für das Attribut Blockung_ID
	 * @param AnzahlUmwaehler   der Wert für das Attribut AnzahlUmwaehler
	 * @param Bewertung   der Wert für das Attribut Bewertung
	 * @param IstMarkiert   der Wert für das Attribut IstMarkiert
	 * @param IstDupliziert   der Wert für das Attribut IstDupliziert
	 */
	public DTOGostBlockungZwischenergebnis(final Long ID, final Long Blockung_ID, final Integer AnzahlUmwaehler, final Long Bewertung, final Boolean IstMarkiert, final Boolean IstDupliziert) {
		if (ID == null) { 
			throw new NullPointerException("ID must not be null");
		}
		this.ID = ID;
		if (Blockung_ID == null) { 
			throw new NullPointerException("Blockung_ID must not be null");
		}
		this.Blockung_ID = Blockung_ID;
		if (AnzahlUmwaehler == null) { 
			throw new NullPointerException("AnzahlUmwaehler must not be null");
		}
		this.AnzahlUmwaehler = AnzahlUmwaehler;
		if (Bewertung == null) { 
			throw new NullPointerException("Bewertung must not be null");
		}
		this.Bewertung = Bewertung;
		if (IstMarkiert == null) { 
			throw new NullPointerException("IstMarkiert must not be null");
		}
		this.IstMarkiert = IstMarkiert;
		if (IstDupliziert == null) { 
			throw new NullPointerException("IstDupliziert must not be null");
		}
		this.IstDupliziert = IstDupliziert;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOGostBlockungZwischenergebnis other = (DTOGostBlockungZwischenergebnis) obj;
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
		return "DTOGostBlockungZwischenergebnis(ID=" + this.ID + ", Blockung_ID=" + this.Blockung_ID + ", AnzahlUmwaehler=" + this.AnzahlUmwaehler + ", Bewertung=" + this.Bewertung + ", IstMarkiert=" + this.IstMarkiert + ", IstDupliziert=" + this.IstDupliziert + ")";
	}

}