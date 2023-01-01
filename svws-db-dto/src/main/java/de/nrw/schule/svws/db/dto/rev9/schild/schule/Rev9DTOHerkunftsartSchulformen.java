package de.nrw.schule.svws.db.dto.rev9.schild.schule;

import de.nrw.schule.svws.db.DBEntityManager;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
/**
 * Diese Klasse dient als DTO für die Datenbanktabelle Herkunftsart_Schulformen.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@IdClass(Rev9DTOHerkunftsartSchulformenPK.class)
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Herkunftsart_Schulformen")
@NamedQuery(name="Rev9DTOHerkunftsartSchulformen.all", query="SELECT e FROM Rev9DTOHerkunftsartSchulformen e")
@NamedQuery(name="Rev9DTOHerkunftsartSchulformen.herkunftsart_id", query="SELECT e FROM Rev9DTOHerkunftsartSchulformen e WHERE e.Herkunftsart_ID = :value")
@NamedQuery(name="Rev9DTOHerkunftsartSchulformen.herkunftsart_id.multiple", query="SELECT e FROM Rev9DTOHerkunftsartSchulformen e WHERE e.Herkunftsart_ID IN :value")
@NamedQuery(name="Rev9DTOHerkunftsartSchulformen.schulform_kuerzel", query="SELECT e FROM Rev9DTOHerkunftsartSchulformen e WHERE e.Schulform_Kuerzel = :value")
@NamedQuery(name="Rev9DTOHerkunftsartSchulformen.schulform_kuerzel.multiple", query="SELECT e FROM Rev9DTOHerkunftsartSchulformen e WHERE e.Schulform_Kuerzel IN :value")
@NamedQuery(name="Rev9DTOHerkunftsartSchulformen.kurzbezeichnung", query="SELECT e FROM Rev9DTOHerkunftsartSchulformen e WHERE e.KurzBezeichnung = :value")
@NamedQuery(name="Rev9DTOHerkunftsartSchulformen.kurzbezeichnung.multiple", query="SELECT e FROM Rev9DTOHerkunftsartSchulformen e WHERE e.KurzBezeichnung IN :value")
@NamedQuery(name="Rev9DTOHerkunftsartSchulformen.bezeichnung", query="SELECT e FROM Rev9DTOHerkunftsartSchulformen e WHERE e.Bezeichnung = :value")
@NamedQuery(name="Rev9DTOHerkunftsartSchulformen.bezeichnung.multiple", query="SELECT e FROM Rev9DTOHerkunftsartSchulformen e WHERE e.Bezeichnung IN :value")
@NamedQuery(name="Rev9DTOHerkunftsartSchulformen.primaryKeyQuery", query="SELECT e FROM Rev9DTOHerkunftsartSchulformen e WHERE e.Herkunftsart_ID = ?1 AND e.Schulform_Kuerzel = ?2")
@NamedQuery(name="Rev9DTOHerkunftsartSchulformen.all.migration", query="SELECT e FROM Rev9DTOHerkunftsartSchulformen e WHERE e.Herkunftsart_ID IS NOT NULL AND e.Schulform_Kuerzel IS NOT NULL")
@JsonPropertyOrder({"Herkunftsart_ID","Schulform_Kuerzel","KurzBezeichnung","Bezeichnung"})
public class Rev9DTOHerkunftsartSchulformen {

	/** die ID der Herkunftsart */
	@Id
	@Column(name = "Herkunftsart_ID")
	@JsonProperty
	public Long Herkunftsart_ID;

	/** das Kürzel der Schulform */
	@Id
	@Column(name = "Schulform_Kuerzel")
	@JsonProperty
	public String Schulform_Kuerzel;

	/** Eine kurze Bezeichnung der Herkunftsart */
	@Column(name = "KurzBezeichnung")
	@JsonProperty
	public String KurzBezeichnung;

	/** Die Bezeichnung der Herkunftsart */
	@Column(name = "Bezeichnung")
	@JsonProperty
	public String Bezeichnung;

	/**
	 * Erstellt ein neues Objekt der Klasse Rev9DTOHerkunftsartSchulformen ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private Rev9DTOHerkunftsartSchulformen() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse Rev9DTOHerkunftsartSchulformen ohne eine Initialisierung der Attribute.
	 * @param Herkunftsart_ID   der Wert für das Attribut Herkunftsart_ID
	 * @param Schulform_Kuerzel   der Wert für das Attribut Schulform_Kuerzel
	 * @param KurzBezeichnung   der Wert für das Attribut KurzBezeichnung
	 * @param Bezeichnung   der Wert für das Attribut Bezeichnung
	 */
	public Rev9DTOHerkunftsartSchulformen(final Long Herkunftsart_ID, final String Schulform_Kuerzel, final String KurzBezeichnung, final String Bezeichnung) {
		if (Herkunftsart_ID == null) { 
			throw new NullPointerException("Herkunftsart_ID must not be null");
		}
		this.Herkunftsart_ID = Herkunftsart_ID;
		if (Schulform_Kuerzel == null) { 
			throw new NullPointerException("Schulform_Kuerzel must not be null");
		}
		this.Schulform_Kuerzel = Schulform_Kuerzel;
		if (KurzBezeichnung == null) { 
			throw new NullPointerException("KurzBezeichnung must not be null");
		}
		this.KurzBezeichnung = KurzBezeichnung;
		if (Bezeichnung == null) { 
			throw new NullPointerException("Bezeichnung must not be null");
		}
		this.Bezeichnung = Bezeichnung;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Rev9DTOHerkunftsartSchulformen other = (Rev9DTOHerkunftsartSchulformen) obj;
		if (Herkunftsart_ID == null) {
			if (other.Herkunftsart_ID != null)
				return false;
		} else if (!Herkunftsart_ID.equals(other.Herkunftsart_ID))
			return false;

		if (Schulform_Kuerzel == null) {
			if (other.Schulform_Kuerzel != null)
				return false;
		} else if (!Schulform_Kuerzel.equals(other.Schulform_Kuerzel))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((Herkunftsart_ID == null) ? 0 : Herkunftsart_ID.hashCode());

		result = prime * result + ((Schulform_Kuerzel == null) ? 0 : Schulform_Kuerzel.hashCode());
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "Rev9DTOHerkunftsartSchulformen(Herkunftsart_ID=" + this.Herkunftsart_ID + ", Schulform_Kuerzel=" + this.Schulform_Kuerzel + ", KurzBezeichnung=" + this.KurzBezeichnung + ", Bezeichnung=" + this.Bezeichnung + ")";
	}

}