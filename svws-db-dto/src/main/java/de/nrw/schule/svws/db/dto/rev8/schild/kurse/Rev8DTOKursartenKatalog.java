package de.nrw.schule.svws.db.dto.rev8.schild.kurse;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle KursartenKatalog.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "KursartenKatalog")
@NamedQuery(name="Rev8DTOKursartenKatalog.all", query="SELECT e FROM Rev8DTOKursartenKatalog e")
@NamedQuery(name="Rev8DTOKursartenKatalog.id", query="SELECT e FROM Rev8DTOKursartenKatalog e WHERE e.ID = :value")
@NamedQuery(name="Rev8DTOKursartenKatalog.id.multiple", query="SELECT e FROM Rev8DTOKursartenKatalog e WHERE e.ID IN :value")
@NamedQuery(name="Rev8DTOKursartenKatalog.kuerzel", query="SELECT e FROM Rev8DTOKursartenKatalog e WHERE e.Kuerzel = :value")
@NamedQuery(name="Rev8DTOKursartenKatalog.kuerzel.multiple", query="SELECT e FROM Rev8DTOKursartenKatalog e WHERE e.Kuerzel IN :value")
@NamedQuery(name="Rev8DTOKursartenKatalog.nummer", query="SELECT e FROM Rev8DTOKursartenKatalog e WHERE e.Nummer = :value")
@NamedQuery(name="Rev8DTOKursartenKatalog.nummer.multiple", query="SELECT e FROM Rev8DTOKursartenKatalog e WHERE e.Nummer IN :value")
@NamedQuery(name="Rev8DTOKursartenKatalog.bezeichnung", query="SELECT e FROM Rev8DTOKursartenKatalog e WHERE e.Bezeichnung = :value")
@NamedQuery(name="Rev8DTOKursartenKatalog.bezeichnung.multiple", query="SELECT e FROM Rev8DTOKursartenKatalog e WHERE e.Bezeichnung IN :value")
@NamedQuery(name="Rev8DTOKursartenKatalog.bemerkungen", query="SELECT e FROM Rev8DTOKursartenKatalog e WHERE e.Bemerkungen = :value")
@NamedQuery(name="Rev8DTOKursartenKatalog.bemerkungen.multiple", query="SELECT e FROM Rev8DTOKursartenKatalog e WHERE e.Bemerkungen IN :value")
@NamedQuery(name="Rev8DTOKursartenKatalog.kuerzelallg", query="SELECT e FROM Rev8DTOKursartenKatalog e WHERE e.KuerzelAllg = :value")
@NamedQuery(name="Rev8DTOKursartenKatalog.kuerzelallg.multiple", query="SELECT e FROM Rev8DTOKursartenKatalog e WHERE e.KuerzelAllg IN :value")
@NamedQuery(name="Rev8DTOKursartenKatalog.bezeichnungallg", query="SELECT e FROM Rev8DTOKursartenKatalog e WHERE e.BezeichnungAllg = :value")
@NamedQuery(name="Rev8DTOKursartenKatalog.bezeichnungallg.multiple", query="SELECT e FROM Rev8DTOKursartenKatalog e WHERE e.BezeichnungAllg IN :value")
@NamedQuery(name="Rev8DTOKursartenKatalog.erlaubtgost", query="SELECT e FROM Rev8DTOKursartenKatalog e WHERE e.ErlaubtGOSt = :value")
@NamedQuery(name="Rev8DTOKursartenKatalog.erlaubtgost.multiple", query="SELECT e FROM Rev8DTOKursartenKatalog e WHERE e.ErlaubtGOSt IN :value")
@NamedQuery(name="Rev8DTOKursartenKatalog.gueltigvon", query="SELECT e FROM Rev8DTOKursartenKatalog e WHERE e.gueltigVon = :value")
@NamedQuery(name="Rev8DTOKursartenKatalog.gueltigvon.multiple", query="SELECT e FROM Rev8DTOKursartenKatalog e WHERE e.gueltigVon IN :value")
@NamedQuery(name="Rev8DTOKursartenKatalog.gueltigbis", query="SELECT e FROM Rev8DTOKursartenKatalog e WHERE e.gueltigBis = :value")
@NamedQuery(name="Rev8DTOKursartenKatalog.gueltigbis.multiple", query="SELECT e FROM Rev8DTOKursartenKatalog e WHERE e.gueltigBis IN :value")
@NamedQuery(name="Rev8DTOKursartenKatalog.primaryKeyQuery", query="SELECT e FROM Rev8DTOKursartenKatalog e WHERE e.ID = ?1")
@NamedQuery(name="Rev8DTOKursartenKatalog.all.migration", query="SELECT e FROM Rev8DTOKursartenKatalog e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID","Kuerzel","Nummer","Bezeichnung","Bemerkungen","KuerzelAllg","BezeichnungAllg","ErlaubtGOSt","gueltigVon","gueltigBis"})
public class Rev8DTOKursartenKatalog {

	/** ID der Kursart */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** Kürzel der Kursart */
	@Column(name = "Kuerzel")
	@JsonProperty
	public String Kuerzel;

	/** Der numerische Schlüssel für die amtliche Schulstatistik */
	@Column(name = "Nummer")
	@JsonProperty
	public String Nummer;

	/** Die textuelle Bezeichnung der Kursart */
	@Column(name = "Bezeichnung")
	@JsonProperty
	public String Bezeichnung;

	/** Ergänzende Bemerkungen zu der Kursart (z.B. gemäß § 9 Abs. 2, 3  SchulG) */
	@Column(name = "Bemerkungen")
	@JsonProperty
	public String Bemerkungen;

	/** Das Kürzel der verallgemeinerten Kursart, falls angegeben (z.B. GK für GKM, GKS, AB3, etc.) */
	@Column(name = "KuerzelAllg")
	@JsonProperty
	public String KuerzelAllg;

	/** Die textuelle Bezeichnung der verallgemeinerten Kursart, falls angegeben */
	@Column(name = "BezeichnungAllg")
	@JsonProperty
	public String BezeichnungAllg;

	/** Gibt an, ob die Kursart in der gymnasialen Oberstufe erlaubt ist oder nicht (1 - true / 0 - false) */
	@Column(name = "ErlaubtGOSt")
	@JsonProperty
	@Convert(converter=Boolean01Converter.class)
	@JsonSerialize(using=Boolean01ConverterSerializer.class)
	@JsonDeserialize(using=Boolean01ConverterDeserializer.class)
	public Boolean ErlaubtGOSt;

	/** Gibt die Gültigkeit ab welchem Schuljahr an */
	@Column(name = "gueltigVon")
	@JsonProperty
	public Integer gueltigVon;

	/** Gibt die Gültigkeit bis zu welchem Schuljahr an */
	@Column(name = "gueltigBis")
	@JsonProperty
	public Integer gueltigBis;

	/**
	 * Erstellt ein neues Objekt der Klasse Rev8DTOKursartenKatalog ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private Rev8DTOKursartenKatalog() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse Rev8DTOKursartenKatalog ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Kuerzel   der Wert für das Attribut Kuerzel
	 * @param Nummer   der Wert für das Attribut Nummer
	 * @param Bezeichnung   der Wert für das Attribut Bezeichnung
	 * @param ErlaubtGOSt   der Wert für das Attribut ErlaubtGOSt
	 */
	public Rev8DTOKursartenKatalog(final Long ID, final String Kuerzel, final String Nummer, final String Bezeichnung, final Boolean ErlaubtGOSt) {
		if (ID == null) { 
			throw new NullPointerException("ID must not be null");
		}
		this.ID = ID;
		if (Kuerzel == null) { 
			throw new NullPointerException("Kuerzel must not be null");
		}
		this.Kuerzel = Kuerzel;
		if (Nummer == null) { 
			throw new NullPointerException("Nummer must not be null");
		}
		this.Nummer = Nummer;
		if (Bezeichnung == null) { 
			throw new NullPointerException("Bezeichnung must not be null");
		}
		this.Bezeichnung = Bezeichnung;
		if (ErlaubtGOSt == null) { 
			throw new NullPointerException("ErlaubtGOSt must not be null");
		}
		this.ErlaubtGOSt = ErlaubtGOSt;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Rev8DTOKursartenKatalog other = (Rev8DTOKursartenKatalog) obj;
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
		return "Rev8DTOKursartenKatalog(ID=" + this.ID + ", Kuerzel=" + this.Kuerzel + ", Nummer=" + this.Nummer + ", Bezeichnung=" + this.Bezeichnung + ", Bemerkungen=" + this.Bemerkungen + ", KuerzelAllg=" + this.KuerzelAllg + ", BezeichnungAllg=" + this.BezeichnungAllg + ", ErlaubtGOSt=" + this.ErlaubtGOSt + ", gueltigVon=" + this.gueltigVon + ", gueltigBis=" + this.gueltigBis + ")";
	}

}