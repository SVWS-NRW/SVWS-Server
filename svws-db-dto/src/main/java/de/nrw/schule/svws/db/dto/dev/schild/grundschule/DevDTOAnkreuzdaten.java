package de.nrw.schule.svws.db.dto.dev.schild.grundschule;

import de.nrw.schule.svws.db.DBEntityManager;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
/**
 * Diese Klasse dient als DTO für die Datenbanktabelle K_Ankreuzdaten.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "K_Ankreuzdaten")
@NamedQuery(name="DevDTOAnkreuzdaten.all", query="SELECT e FROM DevDTOAnkreuzdaten e")
@NamedQuery(name="DevDTOAnkreuzdaten.id", query="SELECT e FROM DevDTOAnkreuzdaten e WHERE e.ID = :value")
@NamedQuery(name="DevDTOAnkreuzdaten.id.multiple", query="SELECT e FROM DevDTOAnkreuzdaten e WHERE e.ID IN :value")
@NamedQuery(name="DevDTOAnkreuzdaten.textstufe1", query="SELECT e FROM DevDTOAnkreuzdaten e WHERE e.TextStufe1 = :value")
@NamedQuery(name="DevDTOAnkreuzdaten.textstufe1.multiple", query="SELECT e FROM DevDTOAnkreuzdaten e WHERE e.TextStufe1 IN :value")
@NamedQuery(name="DevDTOAnkreuzdaten.textstufe2", query="SELECT e FROM DevDTOAnkreuzdaten e WHERE e.TextStufe2 = :value")
@NamedQuery(name="DevDTOAnkreuzdaten.textstufe2.multiple", query="SELECT e FROM DevDTOAnkreuzdaten e WHERE e.TextStufe2 IN :value")
@NamedQuery(name="DevDTOAnkreuzdaten.textstufe3", query="SELECT e FROM DevDTOAnkreuzdaten e WHERE e.TextStufe3 = :value")
@NamedQuery(name="DevDTOAnkreuzdaten.textstufe3.multiple", query="SELECT e FROM DevDTOAnkreuzdaten e WHERE e.TextStufe3 IN :value")
@NamedQuery(name="DevDTOAnkreuzdaten.textstufe4", query="SELECT e FROM DevDTOAnkreuzdaten e WHERE e.TextStufe4 = :value")
@NamedQuery(name="DevDTOAnkreuzdaten.textstufe4.multiple", query="SELECT e FROM DevDTOAnkreuzdaten e WHERE e.TextStufe4 IN :value")
@NamedQuery(name="DevDTOAnkreuzdaten.textstufe5", query="SELECT e FROM DevDTOAnkreuzdaten e WHERE e.TextStufe5 = :value")
@NamedQuery(name="DevDTOAnkreuzdaten.textstufe5.multiple", query="SELECT e FROM DevDTOAnkreuzdaten e WHERE e.TextStufe5 IN :value")
@NamedQuery(name="DevDTOAnkreuzdaten.bezeichnungsonst", query="SELECT e FROM DevDTOAnkreuzdaten e WHERE e.BezeichnungSONST = :value")
@NamedQuery(name="DevDTOAnkreuzdaten.bezeichnungsonst.multiple", query="SELECT e FROM DevDTOAnkreuzdaten e WHERE e.BezeichnungSONST IN :value")
@NamedQuery(name="DevDTOAnkreuzdaten.primaryKeyQuery", query="SELECT e FROM DevDTOAnkreuzdaten e WHERE e.ID = ?1")
@NamedQuery(name="DevDTOAnkreuzdaten.all.migration", query="SELECT e FROM DevDTOAnkreuzdaten e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID","TextStufe1","TextStufe2","TextStufe3","TextStufe4","TextStufe5","BezeichnungSONST"})
public class DevDTOAnkreuzdaten {

	/** ID des Datensatzes für die Angaben zu den Ankreuzkompetenzen */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** Bezeichnung Stufe 1 */
	@Column(name = "TextStufe1")
	@JsonProperty
	public String TextStufe1;

	/** Bezeichnung Stufe 2 */
	@Column(name = "TextStufe2")
	@JsonProperty
	public String TextStufe2;

	/** Bezeichnung Stufe 3 */
	@Column(name = "TextStufe3")
	@JsonProperty
	public String TextStufe3;

	/** Bezeichnung Stufe 4 */
	@Column(name = "TextStufe4")
	@JsonProperty
	public String TextStufe4;

	/** Bezeichnung Stufe 5 */
	@Column(name = "TextStufe5")
	@JsonProperty
	public String TextStufe5;

	/** Bezeichnung Zusatzstufe */
	@Column(name = "BezeichnungSONST")
	@JsonProperty
	public String BezeichnungSONST;

	/**
	 * Erstellt ein neues Objekt der Klasse DevDTOAnkreuzdaten ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DevDTOAnkreuzdaten() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DevDTOAnkreuzdaten ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 */
	public DevDTOAnkreuzdaten(final Long ID) {
		if (ID == null) { 
			throw new NullPointerException("ID must not be null");
		}
		this.ID = ID;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DevDTOAnkreuzdaten other = (DevDTOAnkreuzdaten) obj;
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
		return "DevDTOAnkreuzdaten(ID=" + this.ID + ", TextStufe1=" + this.TextStufe1 + ", TextStufe2=" + this.TextStufe2 + ", TextStufe3=" + this.TextStufe3 + ", TextStufe4=" + this.TextStufe4 + ", TextStufe5=" + this.TextStufe5 + ", BezeichnungSONST=" + this.BezeichnungSONST + ")";
	}

}