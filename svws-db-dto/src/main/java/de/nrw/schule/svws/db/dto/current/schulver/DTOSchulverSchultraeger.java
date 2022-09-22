package de.nrw.schule.svws.db.dto.current.schulver;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle Schulver_Schultraeger.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Schulver_Schultraeger")
@NamedQuery(name="DTOSchulverSchultraeger.all", query="SELECT e FROM DTOSchulverSchultraeger e")
@NamedQuery(name="DTOSchulverSchultraeger.schulnr", query="SELECT e FROM DTOSchulverSchultraeger e WHERE e.SchulNr = :value")
@NamedQuery(name="DTOSchulverSchultraeger.schulnr.multiple", query="SELECT e FROM DTOSchulverSchultraeger e WHERE e.SchulNr IN :value")
@NamedQuery(name="DTOSchulverSchultraeger.regschl", query="SELECT e FROM DTOSchulverSchultraeger e WHERE e.RegSchl = :value")
@NamedQuery(name="DTOSchulverSchultraeger.regschl.multiple", query="SELECT e FROM DTOSchulverSchultraeger e WHERE e.RegSchl IN :value")
@NamedQuery(name="DTOSchulverSchultraeger.kore", query="SELECT e FROM DTOSchulverSchultraeger e WHERE e.KoRe = :value")
@NamedQuery(name="DTOSchulverSchultraeger.kore.multiple", query="SELECT e FROM DTOSchulverSchultraeger e WHERE e.KoRe IN :value")
@NamedQuery(name="DTOSchulverSchultraeger.koho", query="SELECT e FROM DTOSchulverSchultraeger e WHERE e.KoHo = :value")
@NamedQuery(name="DTOSchulverSchultraeger.koho.multiple", query="SELECT e FROM DTOSchulverSchultraeger e WHERE e.KoHo IN :value")
@NamedQuery(name="DTOSchulverSchultraeger.abez1", query="SELECT e FROM DTOSchulverSchultraeger e WHERE e.ABez1 = :value")
@NamedQuery(name="DTOSchulverSchultraeger.abez1.multiple", query="SELECT e FROM DTOSchulverSchultraeger e WHERE e.ABez1 IN :value")
@NamedQuery(name="DTOSchulverSchultraeger.abez2", query="SELECT e FROM DTOSchulverSchultraeger e WHERE e.ABez2 = :value")
@NamedQuery(name="DTOSchulverSchultraeger.abez2.multiple", query="SELECT e FROM DTOSchulverSchultraeger e WHERE e.ABez2 IN :value")
@NamedQuery(name="DTOSchulverSchultraeger.abez3", query="SELECT e FROM DTOSchulverSchultraeger e WHERE e.ABez3 = :value")
@NamedQuery(name="DTOSchulverSchultraeger.abez3.multiple", query="SELECT e FROM DTOSchulverSchultraeger e WHERE e.ABez3 IN :value")
@NamedQuery(name="DTOSchulverSchultraeger.plz", query="SELECT e FROM DTOSchulverSchultraeger e WHERE e.PLZ = :value")
@NamedQuery(name="DTOSchulverSchultraeger.plz.multiple", query="SELECT e FROM DTOSchulverSchultraeger e WHERE e.PLZ IN :value")
@NamedQuery(name="DTOSchulverSchultraeger.ort", query="SELECT e FROM DTOSchulverSchultraeger e WHERE e.Ort = :value")
@NamedQuery(name="DTOSchulverSchultraeger.ort.multiple", query="SELECT e FROM DTOSchulverSchultraeger e WHERE e.Ort IN :value")
@NamedQuery(name="DTOSchulverSchultraeger.strasse", query="SELECT e FROM DTOSchulverSchultraeger e WHERE e.Strasse = :value")
@NamedQuery(name="DTOSchulverSchultraeger.strasse.multiple", query="SELECT e FROM DTOSchulverSchultraeger e WHERE e.Strasse IN :value")
@NamedQuery(name="DTOSchulverSchultraeger.telvorw", query="SELECT e FROM DTOSchulverSchultraeger e WHERE e.TelVorw = :value")
@NamedQuery(name="DTOSchulverSchultraeger.telvorw.multiple", query="SELECT e FROM DTOSchulverSchultraeger e WHERE e.TelVorw IN :value")
@NamedQuery(name="DTOSchulverSchultraeger.telefon", query="SELECT e FROM DTOSchulverSchultraeger e WHERE e.Telefon = :value")
@NamedQuery(name="DTOSchulverSchultraeger.telefon.multiple", query="SELECT e FROM DTOSchulverSchultraeger e WHERE e.Telefon IN :value")
@NamedQuery(name="DTOSchulverSchultraeger.sf", query="SELECT e FROM DTOSchulverSchultraeger e WHERE e.SF = :value")
@NamedQuery(name="DTOSchulverSchultraeger.sf.multiple", query="SELECT e FROM DTOSchulverSchultraeger e WHERE e.SF IN :value")
@NamedQuery(name="DTOSchulverSchultraeger.oeffpri", query="SELECT e FROM DTOSchulverSchultraeger e WHERE e.OeffPri = :value")
@NamedQuery(name="DTOSchulverSchultraeger.oeffpri.multiple", query="SELECT e FROM DTOSchulverSchultraeger e WHERE e.OeffPri IN :value")
@NamedQuery(name="DTOSchulverSchultraeger.kurzbez", query="SELECT e FROM DTOSchulverSchultraeger e WHERE e.KurzBez = :value")
@NamedQuery(name="DTOSchulverSchultraeger.kurzbez.multiple", query="SELECT e FROM DTOSchulverSchultraeger e WHERE e.KurzBez IN :value")
@NamedQuery(name="DTOSchulverSchultraeger.schbetrschl", query="SELECT e FROM DTOSchulverSchultraeger e WHERE e.SchBetrSchl = :value")
@NamedQuery(name="DTOSchulverSchultraeger.schbetrschl.multiple", query="SELECT e FROM DTOSchulverSchultraeger e WHERE e.SchBetrSchl IN :value")
@NamedQuery(name="DTOSchulverSchultraeger.schbetrschldatum", query="SELECT e FROM DTOSchulverSchultraeger e WHERE e.SchBetrSchlDatum = :value")
@NamedQuery(name="DTOSchulverSchultraeger.schbetrschldatum.multiple", query="SELECT e FROM DTOSchulverSchultraeger e WHERE e.SchBetrSchlDatum IN :value")
@NamedQuery(name="DTOSchulverSchultraeger.schuelerzahlasd", query="SELECT e FROM DTOSchulverSchultraeger e WHERE e.SchuelerZahlASD = :value")
@NamedQuery(name="DTOSchulverSchultraeger.schuelerzahlasd.multiple", query="SELECT e FROM DTOSchulverSchultraeger e WHERE e.SchuelerZahlASD IN :value")
@NamedQuery(name="DTOSchulverSchultraeger.schuelerzahlvs", query="SELECT e FROM DTOSchulverSchultraeger e WHERE e.SchuelerZahlVS = :value")
@NamedQuery(name="DTOSchulverSchultraeger.schuelerzahlvs.multiple", query="SELECT e FROM DTOSchulverSchultraeger e WHERE e.SchuelerZahlVS IN :value")
@NamedQuery(name="DTOSchulverSchultraeger.artdertraegerschaft", query="SELECT e FROM DTOSchulverSchultraeger e WHERE e.ArtDerTraegerschaft = :value")
@NamedQuery(name="DTOSchulverSchultraeger.artdertraegerschaft.multiple", query="SELECT e FROM DTOSchulverSchultraeger e WHERE e.ArtDerTraegerschaft IN :value")
@NamedQuery(name="DTOSchulverSchultraeger.schultraegernr", query="SELECT e FROM DTOSchulverSchultraeger e WHERE e.SchultraegerNr = :value")
@NamedQuery(name="DTOSchulverSchultraeger.schultraegernr.multiple", query="SELECT e FROM DTOSchulverSchultraeger e WHERE e.SchultraegerNr IN :value")
@NamedQuery(name="DTOSchulverSchultraeger.schulgliederung", query="SELECT e FROM DTOSchulverSchultraeger e WHERE e.Schulgliederung = :value")
@NamedQuery(name="DTOSchulverSchultraeger.schulgliederung.multiple", query="SELECT e FROM DTOSchulverSchultraeger e WHERE e.Schulgliederung IN :value")
@NamedQuery(name="DTOSchulverSchultraeger.ganztagsbetrieb", query="SELECT e FROM DTOSchulverSchultraeger e WHERE e.Ganztagsbetrieb = :value")
@NamedQuery(name="DTOSchulverSchultraeger.ganztagsbetrieb.multiple", query="SELECT e FROM DTOSchulverSchultraeger e WHERE e.Ganztagsbetrieb IN :value")
@NamedQuery(name="DTOSchulverSchultraeger.aktiv", query="SELECT e FROM DTOSchulverSchultraeger e WHERE e.Aktiv = :value")
@NamedQuery(name="DTOSchulverSchultraeger.aktiv.multiple", query="SELECT e FROM DTOSchulverSchultraeger e WHERE e.Aktiv IN :value")
@NamedQuery(name="DTOSchulverSchultraeger.gueltigvon", query="SELECT e FROM DTOSchulverSchultraeger e WHERE e.gueltigVon = :value")
@NamedQuery(name="DTOSchulverSchultraeger.gueltigvon.multiple", query="SELECT e FROM DTOSchulverSchultraeger e WHERE e.gueltigVon IN :value")
@NamedQuery(name="DTOSchulverSchultraeger.gueltigbis", query="SELECT e FROM DTOSchulverSchultraeger e WHERE e.gueltigBis = :value")
@NamedQuery(name="DTOSchulverSchultraeger.gueltigbis.multiple", query="SELECT e FROM DTOSchulverSchultraeger e WHERE e.gueltigBis IN :value")
@NamedQuery(name="DTOSchulverSchultraeger.primaryKeyQuery", query="SELECT e FROM DTOSchulverSchultraeger e WHERE e.SchulNr = ?1")
@NamedQuery(name="DTOSchulverSchultraeger.all.migration", query="SELECT e FROM DTOSchulverSchultraeger e WHERE e.SchulNr IS NOT NULL")
@JsonPropertyOrder({"SchulNr","RegSchl","KoRe","KoHo","ABez1","ABez2","ABez3","PLZ","Ort","Strasse","TelVorw","Telefon","SF","OeffPri","KurzBez","SchBetrSchl","SchBetrSchlDatum","SchuelerZahlASD","SchuelerZahlVS","ArtDerTraegerschaft","SchultraegerNr","Schulgliederung","Ganztagsbetrieb","Aktiv","gueltigVon","gueltigBis"})
public class DTOSchulverSchultraeger {

	/** Schulver Tabelle IT.NRW: Schulträgernummer des Schulträgers */
	@Id
	@Column(name = "SchulNr")
	@JsonProperty
	public String SchulNr;

	/** Schulver Tabelle IT.NRW: Regionalschlüssel des Schulträgers */
	@Column(name = "RegSchl")
	@JsonProperty
	public String RegSchl;

	/** Schulver Tabelle IT.NRW: ??? */
	@Column(name = "KoRe")
	@JsonProperty
	public String KoRe;

	/** Schulver Tabelle IT.NRW: ??? */
	@Column(name = "KoHo")
	@JsonProperty
	public String KoHo;

	/** Schulver Tabelle IT.NRW: Bezeichnung 1 des Schulträgers */
	@Column(name = "ABez1")
	@JsonProperty
	public String ABez1;

	/** Schulver Tabelle IT.NRW: Bezeichnung 2  des Schulträgers */
	@Column(name = "ABez2")
	@JsonProperty
	public String ABez2;

	/** Schulver Tabelle IT.NRW: Bezeichnung 3 des Schulträgers */
	@Column(name = "ABez3")
	@JsonProperty
	public String ABez3;

	/** Schulver Tabelle IT.NRW: PLZ des Schulträgers */
	@Column(name = "PLZ")
	@JsonProperty
	public String PLZ;

	/** Schulver Tabelle IT.NRW: Ort des Schulträgers */
	@Column(name = "Ort")
	@JsonProperty
	public String Ort;

	/** Schulver Tabelle IT.NRW: Straße des Schulträgers */
	@Column(name = "Strasse")
	@JsonProperty
	public String Strasse;

	/** Schulver Tabelle IT.NRW: Vorwahl des Schulträgers */
	@Column(name = "TelVorw")
	@JsonProperty
	public String TelVorw;

	/** Schulver Tabelle IT.NRW: Telefonnummer des Schulträgers */
	@Column(name = "Telefon")
	@JsonProperty
	public String Telefon;

	/** Schulver Tabelle IT.NRW: Ist immer 00 ??? */
	@Column(name = "SF")
	@JsonProperty
	public String SF;

	/** Schulver Tabelle IT.NRW: Öffentlicher oder privater Schulträger */
	@Column(name = "OeffPri")
	@JsonProperty
	public String OeffPri;

	/** Schulver Tabelle IT.NRW: Kurzbezeichnung des Schulträgers */
	@Column(name = "KurzBez")
	@JsonProperty
	public String KurzBez;

	/** Schulver Tabelle IT.NRW: Schulbetriebsschlüssel des Schulträgers */
	@Column(name = "SchBetrSchl")
	@JsonProperty
	public Integer SchBetrSchl;

	/** Schulver Tabelle IT.NRW: Datum des Schulbetriebsschlüssels */
	@Column(name = "SchBetrSchlDatum")
	@JsonProperty
	public String SchBetrSchlDatum;

	/** Schulver Tabelle IT.NRW: Schülerzahl laut ASD */
	@Column(name = "SchuelerZahlASD")
	@JsonProperty
	public Integer SchuelerZahlASD;

	/** Schulver Tabelle IT.NRW: Schülerzahl laut VS */
	@Column(name = "SchuelerZahlVS")
	@JsonProperty
	public Integer SchuelerZahlVS;

	/** Schulver Tabelle IT.NRW: Art der Trägerschaft des Schulträgers */
	@Column(name = "ArtDerTraegerschaft")
	@JsonProperty
	public String ArtDerTraegerschaft;

	/** Schulver Tabelle IT.NRW: leer siehe SchulNr */
	@Column(name = "SchultraegerNr")
	@JsonProperty
	public String SchultraegerNr;

	/** Schulver Tabelle IT.NRW: leer Gliederung */
	@Column(name = "Schulgliederung")
	@JsonProperty
	public String Schulgliederung;

	/** Schulver Tabelle IT.NRW: Leer Ganztagsbetrieb */
	@Column(name = "Ganztagsbetrieb")
	@JsonProperty
	public String Ganztagsbetrieb;

	/** Schulver Tabelle IT.NRW: Akitv ja nein des Schulträgers */
	@Column(name = "Aktiv")
	@JsonProperty
	public Integer Aktiv;

	/** Schulver Tabelle IT.NRW: Gibt die Gültigkeit ab welchem Schuljahr an */
	@Column(name = "gueltigVon")
	@JsonProperty
	public Integer gueltigVon;

	/** Schulver Tabelle IT.NRW: Gibt die Gültigkeit bis zu welchem Schuljahr an */
	@Column(name = "gueltigBis")
	@JsonProperty
	public Integer gueltigBis;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOSchulverSchultraeger ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOSchulverSchultraeger() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOSchulverSchultraeger ohne eine Initialisierung der Attribute.
	 * @param SchulNr   der Wert für das Attribut SchulNr
	 * @param Aktiv   der Wert für das Attribut Aktiv
	 */
	public DTOSchulverSchultraeger(final String SchulNr, final Integer Aktiv) {
		if (SchulNr == null) { 
			throw new NullPointerException("SchulNr must not be null");
		}
		this.SchulNr = SchulNr;
		if (Aktiv == null) { 
			throw new NullPointerException("Aktiv must not be null");
		}
		this.Aktiv = Aktiv;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOSchulverSchultraeger other = (DTOSchulverSchultraeger) obj;
		if (SchulNr == null) {
			if (other.SchulNr != null)
				return false;
		} else if (!SchulNr.equals(other.SchulNr))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((SchulNr == null) ? 0 : SchulNr.hashCode());
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "DTOSchulverSchultraeger(SchulNr=" + this.SchulNr + ", RegSchl=" + this.RegSchl + ", KoRe=" + this.KoRe + ", KoHo=" + this.KoHo + ", ABez1=" + this.ABez1 + ", ABez2=" + this.ABez2 + ", ABez3=" + this.ABez3 + ", PLZ=" + this.PLZ + ", Ort=" + this.Ort + ", Strasse=" + this.Strasse + ", TelVorw=" + this.TelVorw + ", Telefon=" + this.Telefon + ", SF=" + this.SF + ", OeffPri=" + this.OeffPri + ", KurzBez=" + this.KurzBez + ", SchBetrSchl=" + this.SchBetrSchl + ", SchBetrSchlDatum=" + this.SchBetrSchlDatum + ", SchuelerZahlASD=" + this.SchuelerZahlASD + ", SchuelerZahlVS=" + this.SchuelerZahlVS + ", ArtDerTraegerschaft=" + this.ArtDerTraegerschaft + ", SchultraegerNr=" + this.SchultraegerNr + ", Schulgliederung=" + this.Schulgliederung + ", Ganztagsbetrieb=" + this.Ganztagsbetrieb + ", Aktiv=" + this.Aktiv + ", gueltigVon=" + this.gueltigVon + ", gueltigBis=" + this.gueltigBis + ")";
	}

}