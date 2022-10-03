package de.nrw.schule.svws.db.dto.current.schild.intern;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle Schildintern_PruefOrd_Optionen.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@IdClass(DTOInternPruefungsOrdnungOptionenPK.class)
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Schildintern_PruefOrd_Optionen")
@NamedQuery(name="DTOInternPruefungsOrdnungOptionen.all", query="SELECT e FROM DTOInternPruefungsOrdnungOptionen e")
@NamedQuery(name="DTOInternPruefungsOrdnungOptionen.op_schulformen", query="SELECT e FROM DTOInternPruefungsOrdnungOptionen e WHERE e.OP_Schulformen = :value")
@NamedQuery(name="DTOInternPruefungsOrdnungOptionen.op_schulformen.multiple", query="SELECT e FROM DTOInternPruefungsOrdnungOptionen e WHERE e.OP_Schulformen IN :value")
@NamedQuery(name="DTOInternPruefungsOrdnungOptionen.op_pokrz", query="SELECT e FROM DTOInternPruefungsOrdnungOptionen e WHERE e.OP_POKrz = :value")
@NamedQuery(name="DTOInternPruefungsOrdnungOptionen.op_pokrz.multiple", query="SELECT e FROM DTOInternPruefungsOrdnungOptionen e WHERE e.OP_POKrz IN :value")
@NamedQuery(name="DTOInternPruefungsOrdnungOptionen.op_krz", query="SELECT e FROM DTOInternPruefungsOrdnungOptionen e WHERE e.OP_Krz = :value")
@NamedQuery(name="DTOInternPruefungsOrdnungOptionen.op_krz.multiple", query="SELECT e FROM DTOInternPruefungsOrdnungOptionen e WHERE e.OP_Krz IN :value")
@NamedQuery(name="DTOInternPruefungsOrdnungOptionen.op_abgangsart_b", query="SELECT e FROM DTOInternPruefungsOrdnungOptionen e WHERE e.OP_Abgangsart_B = :value")
@NamedQuery(name="DTOInternPruefungsOrdnungOptionen.op_abgangsart_b.multiple", query="SELECT e FROM DTOInternPruefungsOrdnungOptionen e WHERE e.OP_Abgangsart_B IN :value")
@NamedQuery(name="DTOInternPruefungsOrdnungOptionen.op_abgangsart_nb", query="SELECT e FROM DTOInternPruefungsOrdnungOptionen e WHERE e.OP_Abgangsart_NB = :value")
@NamedQuery(name="DTOInternPruefungsOrdnungOptionen.op_abgangsart_nb.multiple", query="SELECT e FROM DTOInternPruefungsOrdnungOptionen e WHERE e.OP_Abgangsart_NB IN :value")
@NamedQuery(name="DTOInternPruefungsOrdnungOptionen.op_art", query="SELECT e FROM DTOInternPruefungsOrdnungOptionen e WHERE e.OP_Art = :value")
@NamedQuery(name="DTOInternPruefungsOrdnungOptionen.op_art.multiple", query="SELECT e FROM DTOInternPruefungsOrdnungOptionen e WHERE e.OP_Art IN :value")
@NamedQuery(name="DTOInternPruefungsOrdnungOptionen.op_typ", query="SELECT e FROM DTOInternPruefungsOrdnungOptionen e WHERE e.OP_Typ = :value")
@NamedQuery(name="DTOInternPruefungsOrdnungOptionen.op_typ.multiple", query="SELECT e FROM DTOInternPruefungsOrdnungOptionen e WHERE e.OP_Typ IN :value")
@NamedQuery(name="DTOInternPruefungsOrdnungOptionen.op_bildungsgang", query="SELECT e FROM DTOInternPruefungsOrdnungOptionen e WHERE e.OP_Bildungsgang = :value")
@NamedQuery(name="DTOInternPruefungsOrdnungOptionen.op_bildungsgang.multiple", query="SELECT e FROM DTOInternPruefungsOrdnungOptionen e WHERE e.OP_Bildungsgang IN :value")
@NamedQuery(name="DTOInternPruefungsOrdnungOptionen.op_name", query="SELECT e FROM DTOInternPruefungsOrdnungOptionen e WHERE e.OP_Name = :value")
@NamedQuery(name="DTOInternPruefungsOrdnungOptionen.op_name.multiple", query="SELECT e FROM DTOInternPruefungsOrdnungOptionen e WHERE e.OP_Name IN :value")
@NamedQuery(name="DTOInternPruefungsOrdnungOptionen.op_kommentar", query="SELECT e FROM DTOInternPruefungsOrdnungOptionen e WHERE e.OP_Kommentar = :value")
@NamedQuery(name="DTOInternPruefungsOrdnungOptionen.op_kommentar.multiple", query="SELECT e FROM DTOInternPruefungsOrdnungOptionen e WHERE e.OP_Kommentar IN :value")
@NamedQuery(name="DTOInternPruefungsOrdnungOptionen.op_jahrgaenge", query="SELECT e FROM DTOInternPruefungsOrdnungOptionen e WHERE e.OP_Jahrgaenge = :value")
@NamedQuery(name="DTOInternPruefungsOrdnungOptionen.op_jahrgaenge.multiple", query="SELECT e FROM DTOInternPruefungsOrdnungOptionen e WHERE e.OP_Jahrgaenge IN :value")
@NamedQuery(name="DTOInternPruefungsOrdnungOptionen.op_bkindex", query="SELECT e FROM DTOInternPruefungsOrdnungOptionen e WHERE e.OP_BKIndex = :value")
@NamedQuery(name="DTOInternPruefungsOrdnungOptionen.op_bkindex.multiple", query="SELECT e FROM DTOInternPruefungsOrdnungOptionen e WHERE e.OP_BKIndex IN :value")
@NamedQuery(name="DTOInternPruefungsOrdnungOptionen.op_bkanl_typ", query="SELECT e FROM DTOInternPruefungsOrdnungOptionen e WHERE e.OP_BKAnl_Typ = :value")
@NamedQuery(name="DTOInternPruefungsOrdnungOptionen.op_bkanl_typ.multiple", query="SELECT e FROM DTOInternPruefungsOrdnungOptionen e WHERE e.OP_BKAnl_Typ IN :value")
@NamedQuery(name="DTOInternPruefungsOrdnungOptionen.op_reihenfolge", query="SELECT e FROM DTOInternPruefungsOrdnungOptionen e WHERE e.OP_Reihenfolge = :value")
@NamedQuery(name="DTOInternPruefungsOrdnungOptionen.op_reihenfolge.multiple", query="SELECT e FROM DTOInternPruefungsOrdnungOptionen e WHERE e.OP_Reihenfolge IN :value")
@NamedQuery(name="DTOInternPruefungsOrdnungOptionen.gueltigvon", query="SELECT e FROM DTOInternPruefungsOrdnungOptionen e WHERE e.gueltigVon = :value")
@NamedQuery(name="DTOInternPruefungsOrdnungOptionen.gueltigvon.multiple", query="SELECT e FROM DTOInternPruefungsOrdnungOptionen e WHERE e.gueltigVon IN :value")
@NamedQuery(name="DTOInternPruefungsOrdnungOptionen.gueltigbis", query="SELECT e FROM DTOInternPruefungsOrdnungOptionen e WHERE e.gueltigBis = :value")
@NamedQuery(name="DTOInternPruefungsOrdnungOptionen.gueltigbis.multiple", query="SELECT e FROM DTOInternPruefungsOrdnungOptionen e WHERE e.gueltigBis IN :value")
@NamedQuery(name="DTOInternPruefungsOrdnungOptionen.primaryKeyQuery", query="SELECT e FROM DTOInternPruefungsOrdnungOptionen e WHERE e.OP_Schulformen = ?1 AND e.OP_POKrz = ?2 AND e.OP_Krz = ?3 AND e.OP_Art = ?4 AND e.OP_Bildungsgang = ?5 AND e.OP_Name = ?6 AND e.OP_Jahrgaenge = ?7 AND e.OP_Reihenfolge = ?8")
@NamedQuery(name="DTOInternPruefungsOrdnungOptionen.all.migration", query="SELECT e FROM DTOInternPruefungsOrdnungOptionen e WHERE e.OP_Schulformen IS NOT NULL AND e.OP_POKrz IS NOT NULL AND e.OP_Krz IS NOT NULL AND e.OP_Art IS NOT NULL AND e.OP_Bildungsgang IS NOT NULL AND e.OP_Name IS NOT NULL AND e.OP_Jahrgaenge IS NOT NULL AND e.OP_Reihenfolge IS NOT NULL")
@JsonPropertyOrder({"OP_Schulformen","OP_POKrz","OP_Krz","OP_Abgangsart_B","OP_Abgangsart_NB","OP_Art","OP_Typ","OP_Bildungsgang","OP_Name","OP_Kommentar","OP_Jahrgaenge","OP_BKIndex","OP_BKAnl_Typ","OP_Reihenfolge","gueltigVon","gueltigBis"})
public class DTOInternPruefungsOrdnungOptionen {

	/** Schildintern Tabelle: Schulformen für die die Optionen gelten */
	@Id
	@Column(name = "OP_Schulformen")
	@JsonProperty
	public String OP_Schulformen;

	/** Schildintern Tabelle: Kürzel der Prüfungsordung */
	@Id
	@Column(name = "OP_POKrz")
	@JsonProperty
	public String OP_POKrz;

	/** Schildintern Tabelle: Angezeigter Kurztext in Schild-NRW */
	@Id
	@Column(name = "OP_Krz")
	@JsonProperty
	public String OP_Krz;

	/** Schildintern Tabelle: Abgangsart in der Statistik B */
	@Column(name = "OP_Abgangsart_B")
	@JsonProperty
	public String OP_Abgangsart_B;

	/** Schildintern Tabelle: Abgangsart in der Statistik NB */
	@Column(name = "OP_Abgangsart_NB")
	@JsonProperty
	public String OP_Abgangsart_NB;

	/** Schildintern Tabelle: A=Allgemein B=Berufsbildend */
	@Id
	@Column(name = "OP_Art")
	@JsonProperty
	public String OP_Art;

	/** Schildintern Tabelle: Abschlusskürzel in Schild-NRW */
	@Column(name = "OP_Typ")
	@JsonProperty
	public String OP_Typ;

	/** Schildintern Tabelle: Bildungsgang A oder B */
	@Id
	@Column(name = "OP_Bildungsgang")
	@JsonProperty
	public String OP_Bildungsgang;

	/** Schildintern Tabelle: Text des Abschlusses */
	@Id
	@Column(name = "OP_Name")
	@JsonProperty
	public String OP_Name;

	/** Schildintern Tabelle: Paragraph in der BASS (veraltet?) */
	@Column(name = "OP_Kommentar")
	@JsonProperty
	public String OP_Kommentar;

	/** Schildintern Tabelle: zulässig für diese Jahrgänge */
	@Id
	@Column(name = "OP_Jahrgaenge")
	@JsonProperty
	public String OP_Jahrgaenge;

	/** Schildintern Tabelle: zulässig für BKIndex */
	@Column(name = "OP_BKIndex")
	@JsonProperty
	public String OP_BKIndex;

	/** Schildintern Tabelle: zulässig für diese Gliederungen */
	@Column(name = "OP_BKAnl_Typ")
	@JsonProperty
	public String OP_BKAnl_Typ;

	/** Schildintern Tabelle: Reihenfolge */
	@Id
	@Column(name = "OP_Reihenfolge")
	@JsonProperty
	public Integer OP_Reihenfolge;

	/** Schildintern Tabelle: Gültig ab Schuljahr */
	@Column(name = "gueltigVon")
	@JsonProperty
	public Integer gueltigVon;

	/** Schildintern Tabelle: Gültig bis Schuljahr */
	@Column(name = "gueltigBis")
	@JsonProperty
	public Integer gueltigBis;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOInternPruefungsOrdnungOptionen ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOInternPruefungsOrdnungOptionen() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOInternPruefungsOrdnungOptionen ohne eine Initialisierung der Attribute.
	 * @param OP_Schulformen   der Wert für das Attribut OP_Schulformen
	 * @param OP_POKrz   der Wert für das Attribut OP_POKrz
	 * @param OP_Krz   der Wert für das Attribut OP_Krz
	 * @param OP_Art   der Wert für das Attribut OP_Art
	 * @param OP_Bildungsgang   der Wert für das Attribut OP_Bildungsgang
	 * @param OP_Name   der Wert für das Attribut OP_Name
	 * @param OP_Jahrgaenge   der Wert für das Attribut OP_Jahrgaenge
	 * @param OP_Reihenfolge   der Wert für das Attribut OP_Reihenfolge
	 */
	public DTOInternPruefungsOrdnungOptionen(final String OP_Schulformen, final String OP_POKrz, final String OP_Krz, final String OP_Art, final String OP_Bildungsgang, final String OP_Name, final String OP_Jahrgaenge, final Integer OP_Reihenfolge) {
		if (OP_Schulformen == null) { 
			throw new NullPointerException("OP_Schulformen must not be null");
		}
		this.OP_Schulformen = OP_Schulformen;
		if (OP_POKrz == null) { 
			throw new NullPointerException("OP_POKrz must not be null");
		}
		this.OP_POKrz = OP_POKrz;
		if (OP_Krz == null) { 
			throw new NullPointerException("OP_Krz must not be null");
		}
		this.OP_Krz = OP_Krz;
		if (OP_Art == null) { 
			throw new NullPointerException("OP_Art must not be null");
		}
		this.OP_Art = OP_Art;
		if (OP_Bildungsgang == null) { 
			throw new NullPointerException("OP_Bildungsgang must not be null");
		}
		this.OP_Bildungsgang = OP_Bildungsgang;
		if (OP_Name == null) { 
			throw new NullPointerException("OP_Name must not be null");
		}
		this.OP_Name = OP_Name;
		if (OP_Jahrgaenge == null) { 
			throw new NullPointerException("OP_Jahrgaenge must not be null");
		}
		this.OP_Jahrgaenge = OP_Jahrgaenge;
		if (OP_Reihenfolge == null) { 
			throw new NullPointerException("OP_Reihenfolge must not be null");
		}
		this.OP_Reihenfolge = OP_Reihenfolge;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOInternPruefungsOrdnungOptionen other = (DTOInternPruefungsOrdnungOptionen) obj;
		if (OP_Schulformen == null) {
			if (other.OP_Schulformen != null)
				return false;
		} else if (!OP_Schulformen.equals(other.OP_Schulformen))
			return false;

		if (OP_POKrz == null) {
			if (other.OP_POKrz != null)
				return false;
		} else if (!OP_POKrz.equals(other.OP_POKrz))
			return false;

		if (OP_Krz == null) {
			if (other.OP_Krz != null)
				return false;
		} else if (!OP_Krz.equals(other.OP_Krz))
			return false;

		if (OP_Art == null) {
			if (other.OP_Art != null)
				return false;
		} else if (!OP_Art.equals(other.OP_Art))
			return false;

		if (OP_Bildungsgang == null) {
			if (other.OP_Bildungsgang != null)
				return false;
		} else if (!OP_Bildungsgang.equals(other.OP_Bildungsgang))
			return false;

		if (OP_Name == null) {
			if (other.OP_Name != null)
				return false;
		} else if (!OP_Name.equals(other.OP_Name))
			return false;

		if (OP_Jahrgaenge == null) {
			if (other.OP_Jahrgaenge != null)
				return false;
		} else if (!OP_Jahrgaenge.equals(other.OP_Jahrgaenge))
			return false;

		if (OP_Reihenfolge == null) {
			if (other.OP_Reihenfolge != null)
				return false;
		} else if (!OP_Reihenfolge.equals(other.OP_Reihenfolge))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((OP_Schulformen == null) ? 0 : OP_Schulformen.hashCode());

		result = prime * result + ((OP_POKrz == null) ? 0 : OP_POKrz.hashCode());

		result = prime * result + ((OP_Krz == null) ? 0 : OP_Krz.hashCode());

		result = prime * result + ((OP_Art == null) ? 0 : OP_Art.hashCode());

		result = prime * result + ((OP_Bildungsgang == null) ? 0 : OP_Bildungsgang.hashCode());

		result = prime * result + ((OP_Name == null) ? 0 : OP_Name.hashCode());

		result = prime * result + ((OP_Jahrgaenge == null) ? 0 : OP_Jahrgaenge.hashCode());

		result = prime * result + ((OP_Reihenfolge == null) ? 0 : OP_Reihenfolge.hashCode());
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "DTOInternPruefungsOrdnungOptionen(OP_Schulformen=" + this.OP_Schulformen + ", OP_POKrz=" + this.OP_POKrz + ", OP_Krz=" + this.OP_Krz + ", OP_Abgangsart_B=" + this.OP_Abgangsart_B + ", OP_Abgangsart_NB=" + this.OP_Abgangsart_NB + ", OP_Art=" + this.OP_Art + ", OP_Typ=" + this.OP_Typ + ", OP_Bildungsgang=" + this.OP_Bildungsgang + ", OP_Name=" + this.OP_Name + ", OP_Kommentar=" + this.OP_Kommentar + ", OP_Jahrgaenge=" + this.OP_Jahrgaenge + ", OP_BKIndex=" + this.OP_BKIndex + ", OP_BKAnl_Typ=" + this.OP_BKAnl_Typ + ", OP_Reihenfolge=" + this.OP_Reihenfolge + ", gueltigVon=" + this.gueltigVon + ", gueltigBis=" + this.gueltigBis + ")";
	}

}