package de.nrw.schule.svws.db.dto.rev8.views.schildintern;

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
 * Diese Klasse dient als DTO für die Datenbank-View Schildintern_K_Schulnote.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Schildintern_K_Schulnote")
@NamedQuery(name="Rev8DTOSchildInternNote.all", query="SELECT e FROM Rev8DTOSchildInternNote e")
@NamedQuery(name="Rev8DTOSchildInternNote.id", query="SELECT e FROM Rev8DTOSchildInternNote e WHERE e.ID = :value")
@NamedQuery(name="Rev8DTOSchildInternNote.id.multiple", query="SELECT e FROM Rev8DTOSchildInternNote e WHERE e.ID IN :value")
@NamedQuery(name="Rev8DTOSchildInternNote.krz", query="SELECT e FROM Rev8DTOSchildInternNote e WHERE e.Krz = :value")
@NamedQuery(name="Rev8DTOSchildInternNote.krz.multiple", query="SELECT e FROM Rev8DTOSchildInternNote e WHERE e.Krz IN :value")
@NamedQuery(name="Rev8DTOSchildInternNote.art", query="SELECT e FROM Rev8DTOSchildInternNote e WHERE e.Art = :value")
@NamedQuery(name="Rev8DTOSchildInternNote.art.multiple", query="SELECT e FROM Rev8DTOSchildInternNote e WHERE e.Art IN :value")
@NamedQuery(name="Rev8DTOSchildInternNote.bezeichnung", query="SELECT e FROM Rev8DTOSchildInternNote e WHERE e.Bezeichnung = :value")
@NamedQuery(name="Rev8DTOSchildInternNote.bezeichnung.multiple", query="SELECT e FROM Rev8DTOSchildInternNote e WHERE e.Bezeichnung IN :value")
@NamedQuery(name="Rev8DTOSchildInternNote.zeugnisnotenbez", query="SELECT e FROM Rev8DTOSchildInternNote e WHERE e.Zeugnisnotenbez = :value")
@NamedQuery(name="Rev8DTOSchildInternNote.zeugnisnotenbez.multiple", query="SELECT e FROM Rev8DTOSchildInternNote e WHERE e.Zeugnisnotenbez IN :value")
@NamedQuery(name="Rev8DTOSchildInternNote.punkte", query="SELECT e FROM Rev8DTOSchildInternNote e WHERE e.Punkte = :value")
@NamedQuery(name="Rev8DTOSchildInternNote.punkte.multiple", query="SELECT e FROM Rev8DTOSchildInternNote e WHERE e.Punkte IN :value")
@NamedQuery(name="Rev8DTOSchildInternNote.sortierung", query="SELECT e FROM Rev8DTOSchildInternNote e WHERE e.Sortierung = :value")
@NamedQuery(name="Rev8DTOSchildInternNote.sortierung.multiple", query="SELECT e FROM Rev8DTOSchildInternNote e WHERE e.Sortierung IN :value")
@NamedQuery(name="Rev8DTOSchildInternNote.sichtbar", query="SELECT e FROM Rev8DTOSchildInternNote e WHERE e.Sichtbar = :value")
@NamedQuery(name="Rev8DTOSchildInternNote.sichtbar.multiple", query="SELECT e FROM Rev8DTOSchildInternNote e WHERE e.Sichtbar IN :value")
@NamedQuery(name="Rev8DTOSchildInternNote.aenderbar", query="SELECT e FROM Rev8DTOSchildInternNote e WHERE e.Aenderbar = :value")
@NamedQuery(name="Rev8DTOSchildInternNote.aenderbar.multiple", query="SELECT e FROM Rev8DTOSchildInternNote e WHERE e.Aenderbar IN :value")
@NamedQuery(name="Rev8DTOSchildInternNote.gueltigvon", query="SELECT e FROM Rev8DTOSchildInternNote e WHERE e.gueltigVon = :value")
@NamedQuery(name="Rev8DTOSchildInternNote.gueltigvon.multiple", query="SELECT e FROM Rev8DTOSchildInternNote e WHERE e.gueltigVon IN :value")
@NamedQuery(name="Rev8DTOSchildInternNote.gueltigbis", query="SELECT e FROM Rev8DTOSchildInternNote e WHERE e.gueltigBis = :value")
@NamedQuery(name="Rev8DTOSchildInternNote.gueltigbis.multiple", query="SELECT e FROM Rev8DTOSchildInternNote e WHERE e.gueltigBis IN :value")
@NamedQuery(name="Rev8DTOSchildInternNote.primaryKeyQuery", query="SELECT e FROM Rev8DTOSchildInternNote e WHERE e.ID = ?1")
@JsonPropertyOrder({"ID","Krz","Art","Bezeichnung","Zeugnisnotenbez","Punkte","Sortierung","Sichtbar","Aenderbar","gueltigVon","gueltigBis"})
public class Rev8DTOSchildInternNote {

	/** Die ID der Schulnote */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** Das Notenkürzel (z.B. 5+) */
	@Column(name = "Krz")
	@JsonProperty
	public String Krz;

	/** Die Art der Note (N - allgemeine Note, Z - mit Tendenz) */
	@Column(name = "Art")
	@JsonProperty
	public String Art;

	/** Die ausführliche Noten-Bezeichnung (z.B. ausreichend (plus)) */
	@Column(name = "Bezeichnung")
	@JsonProperty
	public String Bezeichnung;

	/** Die Bezeichnung auf einem Zeugnis ohne Tendenzen (z.B. ausreichend) */
	@Column(name = "Zeugnisnotenbez")
	@JsonProperty
	public String Zeugnisnotenbez;

	/** Die Bezeichnung der Note in einer Laufbahn der Sekundarstufe II */
	@Column(name = "Punkte")
	@JsonProperty
	public String Punkte;

	/** Ein Zahlwert, welcher die Sortierreihenfolge der einzelnen Noten spezifiziert */
	@Column(name = "Sortierung")
	@JsonProperty
	public Integer Sortierung;

	/** Gibt an, ob der Noteneintrag sichtbar ist */
	@Column(name = "Sichtbar")
	@JsonProperty
	@Convert(converter=BooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using=BooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using=BooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean Sichtbar;

	/** Gibt an, ob der Noteneintrag änderbar ist */
	@Column(name = "Aenderbar")
	@JsonProperty
	@Convert(converter=BooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using=BooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using=BooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean Aenderbar;

	/** Gibt das Schuljahr an, ab dem die Note verwendet werden kann oder null, falls es keine Einschränkung gibt */
	@Column(name = "gueltigVon")
	@JsonProperty
	public Integer gueltigVon;

	/** Gibt das Schuljahr an, bis zu welchem die Note verwendet werden kann oder null, falls es keine Einschränkung gibt */
	@Column(name = "gueltigBis")
	@JsonProperty
	public Integer gueltigBis;

	/**
	 * Erstellt ein neues Objekt der Klasse Rev8DTOSchildInternNote ohne eine Initialisierung der Attribute.
	 */
	private Rev8DTOSchildInternNote() {
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Rev8DTOSchildInternNote other = (Rev8DTOSchildInternNote) obj;
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
		return "Rev8DTOSchildInternNote(ID=" + this.ID + ", Krz=" + this.Krz + ", Art=" + this.Art + ", Bezeichnung=" + this.Bezeichnung + ", Zeugnisnotenbez=" + this.Zeugnisnotenbez + ", Punkte=" + this.Punkte + ", Sortierung=" + this.Sortierung + ", Sichtbar=" + this.Sichtbar + ", Aenderbar=" + this.Aenderbar + ", gueltigVon=" + this.gueltigVon + ", gueltigBis=" + this.gueltigBis + ")";
	}

}