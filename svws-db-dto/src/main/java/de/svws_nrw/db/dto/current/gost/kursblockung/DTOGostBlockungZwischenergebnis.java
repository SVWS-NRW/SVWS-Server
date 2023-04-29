package de.svws_nrw.db.dto.current.gost.kursblockung;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.current.Boolean01Converter;


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
import de.svws_nrw.csv.converter.current.Boolean01ConverterSerializer;
import de.svws_nrw.csv.converter.current.Boolean01ConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle Gost_Blockung_Zwischenergebnisse.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Gost_Blockung_Zwischenergebnisse")
@NamedQuery(name = "DTOGostBlockungZwischenergebnis.all", query = "SELECT e FROM DTOGostBlockungZwischenergebnis e")
@NamedQuery(name = "DTOGostBlockungZwischenergebnis.id", query = "SELECT e FROM DTOGostBlockungZwischenergebnis e WHERE e.ID = :value")
@NamedQuery(name = "DTOGostBlockungZwischenergebnis.id.multiple", query = "SELECT e FROM DTOGostBlockungZwischenergebnis e WHERE e.ID IN :value")
@NamedQuery(name = "DTOGostBlockungZwischenergebnis.blockung_id", query = "SELECT e FROM DTOGostBlockungZwischenergebnis e WHERE e.Blockung_ID = :value")
@NamedQuery(name = "DTOGostBlockungZwischenergebnis.blockung_id.multiple", query = "SELECT e FROM DTOGostBlockungZwischenergebnis e WHERE e.Blockung_ID IN :value")
@NamedQuery(name = "DTOGostBlockungZwischenergebnis.istmarkiert", query = "SELECT e FROM DTOGostBlockungZwischenergebnis e WHERE e.IstMarkiert = :value")
@NamedQuery(name = "DTOGostBlockungZwischenergebnis.istmarkiert.multiple", query = "SELECT e FROM DTOGostBlockungZwischenergebnis e WHERE e.IstMarkiert IN :value")
@NamedQuery(name = "DTOGostBlockungZwischenergebnis.istvorlage", query = "SELECT e FROM DTOGostBlockungZwischenergebnis e WHERE e.IstVorlage = :value")
@NamedQuery(name = "DTOGostBlockungZwischenergebnis.istvorlage.multiple", query = "SELECT e FROM DTOGostBlockungZwischenergebnis e WHERE e.IstVorlage IN :value")
@NamedQuery(name = "DTOGostBlockungZwischenergebnis.primaryKeyQuery", query = "SELECT e FROM DTOGostBlockungZwischenergebnis e WHERE e.ID = ?1")
@NamedQuery(name = "DTOGostBlockungZwischenergebnis.all.migration", query = "SELECT e FROM DTOGostBlockungZwischenergebnis e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID", "Blockung_ID", "IstMarkiert", "IstVorlage"})
public final class DTOGostBlockungZwischenergebnis {

	/** ID der Zwischenergebnisses einer Blockung (generiert) */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long ID;

	/** ID der Blockung */
	@Column(name = "Blockung_ID")
	@JsonProperty
	public long Blockung_ID;

	/** Gibt an, ob das Zwischenergebnis von einem Benutzer markiert wurde oder nicht: 1 - true, 0 - false  */
	@Column(name = "IstMarkiert")
	@JsonProperty
	@Convert(converter = Boolean01Converter.class)
	@JsonSerialize(using = Boolean01ConverterSerializer.class)
	@JsonDeserialize(using = Boolean01ConverterDeserializer.class)
	public Boolean IstMarkiert;

	/** Gibt an, ob das Zwischenergebnis im Zusammenhang mit der Erstellen einer Blockung erstellt wurde und somit als Vorlage für Regeldefinitionen dient oder nicht: 1 - true, 0 - false. Die Vorlage kann zu einem späteren Zeitpunkt ggf. auf ein anderes (berechnetes) Ergebnis umgesetzt werden.In diesem Fall müssten jedoch alle anderen Ergebnisse der Blockungsdefinition entfernt werden. */
	@Column(name = "IstVorlage")
	@JsonProperty
	@Convert(converter = Boolean01Converter.class)
	@JsonSerialize(using = Boolean01ConverterSerializer.class)
	@JsonDeserialize(using = Boolean01ConverterDeserializer.class)
	public Boolean IstVorlage;

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
	 * @param IstMarkiert   der Wert für das Attribut IstMarkiert
	 * @param IstVorlage   der Wert für das Attribut IstVorlage
	 */
	public DTOGostBlockungZwischenergebnis(final long ID, final long Blockung_ID, final Boolean IstMarkiert, final Boolean IstVorlage) {
		this.ID = ID;
		this.Blockung_ID = Blockung_ID;
		this.IstMarkiert = IstMarkiert;
		this.IstVorlage = IstVorlage;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOGostBlockungZwischenergebnis other = (DTOGostBlockungZwischenergebnis) obj;
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
		return "DTOGostBlockungZwischenergebnis(ID=" + this.ID + ", Blockung_ID=" + this.Blockung_ID + ", IstMarkiert=" + this.IstMarkiert + ", IstVorlage=" + this.IstVorlage + ")";
	}

}
