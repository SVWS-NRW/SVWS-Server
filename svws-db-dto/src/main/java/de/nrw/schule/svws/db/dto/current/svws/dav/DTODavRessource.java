package de.nrw.schule.svws.db.dto.current.svws.dav;

import de.nrw.schule.svws.db.DBEntityManager;
import de.nrw.schule.svws.db.converter.current.DatumUhrzeitConverter;


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
import de.nrw.schule.svws.csv.converter.current.DatumUhrzeitConverterSerializer;
import de.nrw.schule.svws.csv.converter.current.DatumUhrzeitConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle DavRessources.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "DavRessources")
@NamedQuery(name="DTODavRessource.all", query="SELECT e FROM DTODavRessource e")
@NamedQuery(name="DTODavRessource.id", query="SELECT e FROM DTODavRessource e WHERE e.ID = :value")
@NamedQuery(name="DTODavRessource.id.multiple", query="SELECT e FROM DTODavRessource e WHERE e.ID IN :value")
@NamedQuery(name="DTODavRessource.davressourcecollection_id", query="SELECT e FROM DTODavRessource e WHERE e.DavRessourceCollection_ID = :value")
@NamedQuery(name="DTODavRessource.davressourcecollection_id.multiple", query="SELECT e FROM DTODavRessource e WHERE e.DavRessourceCollection_ID IN :value")
@NamedQuery(name="DTODavRessource.uid", query="SELECT e FROM DTODavRessource e WHERE e.UID = :value")
@NamedQuery(name="DTODavRessource.uid.multiple", query="SELECT e FROM DTODavRessource e WHERE e.UID IN :value")
@NamedQuery(name="DTODavRessource.lastmodified", query="SELECT e FROM DTODavRessource e WHERE e.lastModified = :value")
@NamedQuery(name="DTODavRessource.lastmodified.multiple", query="SELECT e FROM DTODavRessource e WHERE e.lastModified IN :value")
@NamedQuery(name="DTODavRessource.kalendertyp", query="SELECT e FROM DTODavRessource e WHERE e.KalenderTyp = :value")
@NamedQuery(name="DTODavRessource.kalendertyp.multiple", query="SELECT e FROM DTODavRessource e WHERE e.KalenderTyp IN :value")
@NamedQuery(name="DTODavRessource.kalenderstart", query="SELECT e FROM DTODavRessource e WHERE e.KalenderStart = :value")
@NamedQuery(name="DTODavRessource.kalenderstart.multiple", query="SELECT e FROM DTODavRessource e WHERE e.KalenderStart IN :value")
@NamedQuery(name="DTODavRessource.kalenderende", query="SELECT e FROM DTODavRessource e WHERE e.KalenderEnde = :value")
@NamedQuery(name="DTODavRessource.kalenderende.multiple", query="SELECT e FROM DTODavRessource e WHERE e.KalenderEnde IN :value")
@NamedQuery(name="DTODavRessource.ressource", query="SELECT e FROM DTODavRessource e WHERE e.ressource = :value")
@NamedQuery(name="DTODavRessource.ressource.multiple", query="SELECT e FROM DTODavRessource e WHERE e.ressource IN :value")
@NamedQuery(name="DTODavRessource.primaryKeyQuery", query="SELECT e FROM DTODavRessource e WHERE e.ID = ?1")
@NamedQuery(name="DTODavRessource.all.migration", query="SELECT e FROM DTODavRessource e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID","DavRessourceCollection_ID","UID","lastModified","KalenderTyp","KalenderStart","KalenderEnde","ressource"})
public class DTODavRessource {

	/** ID der Dav Ressource */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** Id der Ressourcensammlung, deren Teil diese Ressource ist */
	@Column(name = "DavRessourceCollection_ID")
	@JsonProperty
	public Long DavRessourceCollection_ID;

	/** Die UID der Ressource */
	@Column(name = "UID")
	@JsonProperty
	public String UID;

	/** Das Datum an dem die Ressource zuletzt geändert wurde, als Synctoken einsetzbar */
	@Column(name = "lastModified")
	@JsonProperty
	@Convert(converter=DatumUhrzeitConverter.class)
	@JsonSerialize(using=DatumUhrzeitConverterSerializer.class)
	@JsonDeserialize(using=DatumUhrzeitConverterDeserializer.class)
	public String lastModified;

	/** Die Art der Kalenderressource, wenn es eine Kalenderressource ist */
	@Column(name = "KalenderTyp")
	@JsonProperty
	public String KalenderTyp;

	/** Der Start der Kalenderressource, wenn es eine Kalenderressource ist */
	@Column(name = "KalenderStart")
	@JsonProperty
	@Convert(converter=DatumUhrzeitConverter.class)
	@JsonSerialize(using=DatumUhrzeitConverterSerializer.class)
	@JsonDeserialize(using=DatumUhrzeitConverterDeserializer.class)
	public String KalenderStart;

	/** Das Ende der Kalenderressource, wenn es eines Kalenderressource ist */
	@Column(name = "KalenderEnde")
	@JsonProperty
	@Convert(converter=DatumUhrzeitConverter.class)
	@JsonSerialize(using=DatumUhrzeitConverterSerializer.class)
	@JsonDeserialize(using=DatumUhrzeitConverterDeserializer.class)
	public String KalenderEnde;

	/** Die Daten der Ressource */
	@Column(name = "ressource")
	@JsonProperty
	public byte[] ressource;

	/**
	 * Erstellt ein neues Objekt der Klasse DTODavRessource ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTODavRessource() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTODavRessource ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param DavRessourceCollection_ID   der Wert für das Attribut DavRessourceCollection_ID
	 * @param UID   der Wert für das Attribut UID
	 * @param lastModified   der Wert für das Attribut lastModified
	 * @param KalenderTyp   der Wert für das Attribut KalenderTyp
	 * @param KalenderStart   der Wert für das Attribut KalenderStart
	 * @param KalenderEnde   der Wert für das Attribut KalenderEnde
	 * @param ressource   der Wert für das Attribut ressource
	 */
	public DTODavRessource(final Long ID, final Long DavRessourceCollection_ID, final String UID, final String lastModified, final String KalenderTyp, final String KalenderStart, final String KalenderEnde, final byte[] ressource) {
		if (ID == null) { 
			throw new NullPointerException("ID must not be null");
		}
		this.ID = ID;
		if (DavRessourceCollection_ID == null) { 
			throw new NullPointerException("DavRessourceCollection_ID must not be null");
		}
		this.DavRessourceCollection_ID = DavRessourceCollection_ID;
		if (UID == null) { 
			throw new NullPointerException("UID must not be null");
		}
		this.UID = UID;
		if (lastModified == null) { 
			throw new NullPointerException("lastModified must not be null");
		}
		this.lastModified = lastModified;
		if (KalenderTyp == null) { 
			throw new NullPointerException("KalenderTyp must not be null");
		}
		this.KalenderTyp = KalenderTyp;
		if (KalenderStart == null) { 
			throw new NullPointerException("KalenderStart must not be null");
		}
		this.KalenderStart = KalenderStart;
		if (KalenderEnde == null) { 
			throw new NullPointerException("KalenderEnde must not be null");
		}
		this.KalenderEnde = KalenderEnde;
		if (ressource == null) { 
			throw new NullPointerException("ressource must not be null");
		}
		this.ressource = ressource;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTODavRessource other = (DTODavRessource) obj;
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
		return "DTODavRessource(ID=" + this.ID + ", DavRessourceCollection_ID=" + this.DavRessourceCollection_ID + ", UID=" + this.UID + ", lastModified=" + this.lastModified + ", KalenderTyp=" + this.KalenderTyp + ", KalenderStart=" + this.KalenderStart + ", KalenderEnde=" + this.KalenderEnde + ", ressource=" + this.ressource + ")";
	}

}