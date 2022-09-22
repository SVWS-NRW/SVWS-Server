package de.nrw.schule.svws.db.dto.rev8.schild.intern;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle Schildintern_KAoA_Kategorie.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Schildintern_KAoA_Kategorie")
@NamedQuery(name="Rev8DTOKAoAKategorie.all", query="SELECT e FROM Rev8DTOKAoAKategorie e")
@NamedQuery(name="Rev8DTOKAoAKategorie.id", query="SELECT e FROM Rev8DTOKAoAKategorie e WHERE e.ID = :value")
@NamedQuery(name="Rev8DTOKAoAKategorie.id.multiple", query="SELECT e FROM Rev8DTOKAoAKategorie e WHERE e.ID IN :value")
@NamedQuery(name="Rev8DTOKAoAKategorie.gueltigvon", query="SELECT e FROM Rev8DTOKAoAKategorie e WHERE e.gueltigVon = :value")
@NamedQuery(name="Rev8DTOKAoAKategorie.gueltigvon.multiple", query="SELECT e FROM Rev8DTOKAoAKategorie e WHERE e.gueltigVon IN :value")
@NamedQuery(name="Rev8DTOKAoAKategorie.gueltigbis", query="SELECT e FROM Rev8DTOKAoAKategorie e WHERE e.gueltigBis = :value")
@NamedQuery(name="Rev8DTOKAoAKategorie.gueltigbis.multiple", query="SELECT e FROM Rev8DTOKAoAKategorie e WHERE e.gueltigBis IN :value")
@NamedQuery(name="Rev8DTOKAoAKategorie.k_kuerzel", query="SELECT e FROM Rev8DTOKAoAKategorie e WHERE e.K_Kuerzel = :value")
@NamedQuery(name="Rev8DTOKAoAKategorie.k_kuerzel.multiple", query="SELECT e FROM Rev8DTOKAoAKategorie e WHERE e.K_Kuerzel IN :value")
@NamedQuery(name="Rev8DTOKAoAKategorie.k_beschreibung", query="SELECT e FROM Rev8DTOKAoAKategorie e WHERE e.K_Beschreibung = :value")
@NamedQuery(name="Rev8DTOKAoAKategorie.k_beschreibung.multiple", query="SELECT e FROM Rev8DTOKAoAKategorie e WHERE e.K_Beschreibung IN :value")
@NamedQuery(name="Rev8DTOKAoAKategorie.k_jahrgaenge", query="SELECT e FROM Rev8DTOKAoAKategorie e WHERE e.K_Jahrgaenge = :value")
@NamedQuery(name="Rev8DTOKAoAKategorie.k_jahrgaenge.multiple", query="SELECT e FROM Rev8DTOKAoAKategorie e WHERE e.K_Jahrgaenge IN :value")
@NamedQuery(name="Rev8DTOKAoAKategorie.primaryKeyQuery", query="SELECT e FROM Rev8DTOKAoAKategorie e WHERE e.ID = ?1")
@NamedQuery(name="Rev8DTOKAoAKategorie.all.migration", query="SELECT e FROM Rev8DTOKAoAKategorie e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID","gueltigVon","gueltigBis","K_Kuerzel","K_Beschreibung","K_Jahrgaenge"})
public class Rev8DTOKAoAKategorie {

	/** Schildintern Tabelle: Eindeutige ID für den Datensatz */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** Schildintern Tabelle: Der Datensatz ist gültig ab dem Datum */
	@Column(name = "gueltigVon")
	@JsonProperty
	public Integer gueltigVon;

	/** Schildintern Tabelle: Der Datensatz ist gültig bis zu dem Datum */
	@Column(name = "gueltigBis")
	@JsonProperty
	public Integer gueltigBis;

	/** Schildintern Tabelle: Kürzel der Kategorie */
	@Column(name = "K_Kuerzel")
	@JsonProperty
	public String K_Kuerzel;

	/** Schildintern Tabelle: Beschreibung (Klartext) der Kategorie */
	@Column(name = "K_Beschreibung")
	@JsonProperty
	public String K_Beschreibung;

	/** Schildintern Tabelle: Jahrgangsstufen in denen der Eintrag gemacht werden darf */
	@Column(name = "K_Jahrgaenge")
	@JsonProperty
	public String K_Jahrgaenge;

	/**
	 * Erstellt ein neues Objekt der Klasse Rev8DTOKAoAKategorie ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private Rev8DTOKAoAKategorie() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse Rev8DTOKAoAKategorie ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param K_Kuerzel   der Wert für das Attribut K_Kuerzel
	 */
	public Rev8DTOKAoAKategorie(final Long ID, final String K_Kuerzel) {
		if (ID == null) { 
			throw new NullPointerException("ID must not be null");
		}
		this.ID = ID;
		if (K_Kuerzel == null) { 
			throw new NullPointerException("K_Kuerzel must not be null");
		}
		this.K_Kuerzel = K_Kuerzel;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Rev8DTOKAoAKategorie other = (Rev8DTOKAoAKategorie) obj;
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
		return "Rev8DTOKAoAKategorie(ID=" + this.ID + ", gueltigVon=" + this.gueltigVon + ", gueltigBis=" + this.gueltigBis + ", K_Kuerzel=" + this.K_Kuerzel + ", K_Beschreibung=" + this.K_Beschreibung + ", K_Jahrgaenge=" + this.K_Jahrgaenge + ")";
	}

}