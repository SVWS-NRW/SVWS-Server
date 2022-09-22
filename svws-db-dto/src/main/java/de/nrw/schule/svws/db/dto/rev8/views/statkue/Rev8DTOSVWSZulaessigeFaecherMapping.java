package de.nrw.schule.svws.db.dto.rev8.views.statkue;

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
 * Diese Klasse dient als DTO für die Datenbank-View Statkue_SVWS_ZulaessigeFaecher.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Statkue_SVWS_ZulaessigeFaecher")
@NamedQuery(name="Rev8DTOSVWSZulaessigeFaecherMapping.all", query="SELECT e FROM Rev8DTOSVWSZulaessigeFaecherMapping e")
@NamedQuery(name="Rev8DTOSVWSZulaessigeFaecherMapping.fach", query="SELECT e FROM Rev8DTOSVWSZulaessigeFaecherMapping e WHERE e.Fach = :value")
@NamedQuery(name="Rev8DTOSVWSZulaessigeFaecherMapping.fach.multiple", query="SELECT e FROM Rev8DTOSVWSZulaessigeFaecherMapping e WHERE e.Fach IN :value")
@NamedQuery(name="Rev8DTOSVWSZulaessigeFaecherMapping.bezeichnung", query="SELECT e FROM Rev8DTOSVWSZulaessigeFaecherMapping e WHERE e.Bezeichnung = :value")
@NamedQuery(name="Rev8DTOSVWSZulaessigeFaecherMapping.bezeichnung.multiple", query="SELECT e FROM Rev8DTOSVWSZulaessigeFaecherMapping e WHERE e.Bezeichnung IN :value")
@NamedQuery(name="Rev8DTOSVWSZulaessigeFaecherMapping.fachkuerzelatomar", query="SELECT e FROM Rev8DTOSVWSZulaessigeFaecherMapping e WHERE e.FachkuerzelAtomar = :value")
@NamedQuery(name="Rev8DTOSVWSZulaessigeFaecherMapping.fachkuerzelatomar.multiple", query="SELECT e FROM Rev8DTOSVWSZulaessigeFaecherMapping e WHERE e.FachkuerzelAtomar IN :value")
@NamedQuery(name="Rev8DTOSVWSZulaessigeFaecherMapping.kurzbezeichnung", query="SELECT e FROM Rev8DTOSVWSZulaessigeFaecherMapping e WHERE e.Kurzbezeichnung = :value")
@NamedQuery(name="Rev8DTOSVWSZulaessigeFaecherMapping.kurzbezeichnung.multiple", query="SELECT e FROM Rev8DTOSVWSZulaessigeFaecherMapping e WHERE e.Kurzbezeichnung IN :value")
@NamedQuery(name="Rev8DTOSVWSZulaessigeFaecherMapping.aufgabenfeld", query="SELECT e FROM Rev8DTOSVWSZulaessigeFaecherMapping e WHERE e.Aufgabenfeld = :value")
@NamedQuery(name="Rev8DTOSVWSZulaessigeFaecherMapping.aufgabenfeld.multiple", query="SELECT e FROM Rev8DTOSVWSZulaessigeFaecherMapping e WHERE e.Aufgabenfeld IN :value")
@NamedQuery(name="Rev8DTOSVWSZulaessigeFaecherMapping.fachgruppe_id", query="SELECT e FROM Rev8DTOSVWSZulaessigeFaecherMapping e WHERE e.Fachgruppe_ID = :value")
@NamedQuery(name="Rev8DTOSVWSZulaessigeFaecherMapping.fachgruppe_id.multiple", query="SELECT e FROM Rev8DTOSVWSZulaessigeFaecherMapping e WHERE e.Fachgruppe_ID IN :value")
@NamedQuery(name="Rev8DTOSVWSZulaessigeFaecherMapping.schulformenundgliederungen", query="SELECT e FROM Rev8DTOSVWSZulaessigeFaecherMapping e WHERE e.SchulformenUndGliederungen = :value")
@NamedQuery(name="Rev8DTOSVWSZulaessigeFaecherMapping.schulformenundgliederungen.multiple", query="SELECT e FROM Rev8DTOSVWSZulaessigeFaecherMapping e WHERE e.SchulformenUndGliederungen IN :value")
@NamedQuery(name="Rev8DTOSVWSZulaessigeFaecherMapping.schulformenausgelaufen", query="SELECT e FROM Rev8DTOSVWSZulaessigeFaecherMapping e WHERE e.SchulformenAusgelaufen = :value")
@NamedQuery(name="Rev8DTOSVWSZulaessigeFaecherMapping.schulformenausgelaufen.multiple", query="SELECT e FROM Rev8DTOSVWSZulaessigeFaecherMapping e WHERE e.SchulformenAusgelaufen IN :value")
@NamedQuery(name="Rev8DTOSVWSZulaessigeFaecherMapping.ausgelaufeninschuljahr", query="SELECT e FROM Rev8DTOSVWSZulaessigeFaecherMapping e WHERE e.AusgelaufenInSchuljahr = :value")
@NamedQuery(name="Rev8DTOSVWSZulaessigeFaecherMapping.ausgelaufeninschuljahr.multiple", query="SELECT e FROM Rev8DTOSVWSZulaessigeFaecherMapping e WHERE e.AusgelaufenInSchuljahr IN :value")
@NamedQuery(name="Rev8DTOSVWSZulaessigeFaecherMapping.abjahrgang", query="SELECT e FROM Rev8DTOSVWSZulaessigeFaecherMapping e WHERE e.AbJahrgang = :value")
@NamedQuery(name="Rev8DTOSVWSZulaessigeFaecherMapping.abjahrgang.multiple", query="SELECT e FROM Rev8DTOSVWSZulaessigeFaecherMapping e WHERE e.AbJahrgang IN :value")
@NamedQuery(name="Rev8DTOSVWSZulaessigeFaecherMapping.istfremdsprache", query="SELECT e FROM Rev8DTOSVWSZulaessigeFaecherMapping e WHERE e.IstFremdsprache = :value")
@NamedQuery(name="Rev8DTOSVWSZulaessigeFaecherMapping.istfremdsprache.multiple", query="SELECT e FROM Rev8DTOSVWSZulaessigeFaecherMapping e WHERE e.IstFremdsprache IN :value")
@NamedQuery(name="Rev8DTOSVWSZulaessigeFaecherMapping.isthkfs", query="SELECT e FROM Rev8DTOSVWSZulaessigeFaecherMapping e WHERE e.IstHKFS = :value")
@NamedQuery(name="Rev8DTOSVWSZulaessigeFaecherMapping.isthkfs.multiple", query="SELECT e FROM Rev8DTOSVWSZulaessigeFaecherMapping e WHERE e.IstHKFS IN :value")
@NamedQuery(name="Rev8DTOSVWSZulaessigeFaecherMapping.istausregufach", query="SELECT e FROM Rev8DTOSVWSZulaessigeFaecherMapping e WHERE e.IstAusRegUFach = :value")
@NamedQuery(name="Rev8DTOSVWSZulaessigeFaecherMapping.istausregufach.multiple", query="SELECT e FROM Rev8DTOSVWSZulaessigeFaecherMapping e WHERE e.IstAusRegUFach IN :value")
@NamedQuery(name="Rev8DTOSVWSZulaessigeFaecherMapping.istersatzpflichtfs", query="SELECT e FROM Rev8DTOSVWSZulaessigeFaecherMapping e WHERE e.IstErsatzPflichtFS = :value")
@NamedQuery(name="Rev8DTOSVWSZulaessigeFaecherMapping.istersatzpflichtfs.multiple", query="SELECT e FROM Rev8DTOSVWSZulaessigeFaecherMapping e WHERE e.IstErsatzPflichtFS IN :value")
@NamedQuery(name="Rev8DTOSVWSZulaessigeFaecherMapping.istkonfkoop", query="SELECT e FROM Rev8DTOSVWSZulaessigeFaecherMapping e WHERE e.IstKonfKoop = :value")
@NamedQuery(name="Rev8DTOSVWSZulaessigeFaecherMapping.istkonfkoop.multiple", query="SELECT e FROM Rev8DTOSVWSZulaessigeFaecherMapping e WHERE e.IstKonfKoop IN :value")
@NamedQuery(name="Rev8DTOSVWSZulaessigeFaecherMapping.nursii", query="SELECT e FROM Rev8DTOSVWSZulaessigeFaecherMapping e WHERE e.NurSII = :value")
@NamedQuery(name="Rev8DTOSVWSZulaessigeFaecherMapping.nursii.multiple", query="SELECT e FROM Rev8DTOSVWSZulaessigeFaecherMapping e WHERE e.NurSII IN :value")
@NamedQuery(name="Rev8DTOSVWSZulaessigeFaecherMapping.exportasd", query="SELECT e FROM Rev8DTOSVWSZulaessigeFaecherMapping e WHERE e.ExportASD = :value")
@NamedQuery(name="Rev8DTOSVWSZulaessigeFaecherMapping.exportasd.multiple", query="SELECT e FROM Rev8DTOSVWSZulaessigeFaecherMapping e WHERE e.ExportASD IN :value")
@NamedQuery(name="Rev8DTOSVWSZulaessigeFaecherMapping.gueltigvon", query="SELECT e FROM Rev8DTOSVWSZulaessigeFaecherMapping e WHERE e.gueltigVon = :value")
@NamedQuery(name="Rev8DTOSVWSZulaessigeFaecherMapping.gueltigvon.multiple", query="SELECT e FROM Rev8DTOSVWSZulaessigeFaecherMapping e WHERE e.gueltigVon IN :value")
@NamedQuery(name="Rev8DTOSVWSZulaessigeFaecherMapping.gueltigbis", query="SELECT e FROM Rev8DTOSVWSZulaessigeFaecherMapping e WHERE e.gueltigBis = :value")
@NamedQuery(name="Rev8DTOSVWSZulaessigeFaecherMapping.gueltigbis.multiple", query="SELECT e FROM Rev8DTOSVWSZulaessigeFaecherMapping e WHERE e.gueltigBis IN :value")
@NamedQuery(name="Rev8DTOSVWSZulaessigeFaecherMapping.primaryKeyQuery", query="SELECT e FROM Rev8DTOSVWSZulaessigeFaecherMapping e WHERE e.Fach = ?1")
@JsonPropertyOrder({"Fach","Bezeichnung","FachkuerzelAtomar","Kurzbezeichnung","Aufgabenfeld","Fachgruppe_ID","SchulformenUndGliederungen","SchulformenAusgelaufen","AusgelaufenInSchuljahr","AbJahrgang","IstFremdsprache","IstHKFS","IstAusRegUFach","IstErsatzPflichtFS","IstKonfKoop","NurSII","ExportASD","gueltigVon","gueltigBis"})
public class Rev8DTOSVWSZulaessigeFaecherMapping {

	/** Fachkürzel aus der Tabelle StatkueZulaessigeFaecher von ITNRW. */
	@Id
	@Column(name = "Fach")
	@JsonProperty
	public String Fach;

	/** Die Bezeichnung des Faches */
	@Column(name = "Bezeichnung")
	@JsonProperty
	public String Bezeichnung;

	/** atomarisiertes Fachkürzel */
	@Column(name = "FachkuerzelAtomar")
	@JsonProperty
	public String FachkuerzelAtomar;

	/** kürzere Bezeichnung falls gewünscht */
	@Column(name = "Kurzbezeichnung")
	@JsonProperty
	public String Kurzbezeichnung;

	/** das Aufgabenfeld, welchem das Fach zugeordnet ist (1, 2 oder 3) */
	@Column(name = "Aufgabenfeld")
	@JsonProperty
	public Integer Aufgabenfeld;

	/** Die ID der zugeordneten Fachgruppe */
	@Column(name = "Fachgruppe_ID")
	@JsonProperty
	public Integer Fachgruppe_ID;

	/** Gibt die Schulformen an in denen das Fach erlaubt ist (R,H,2020) gibt an dass ein Fach an R nur in H ab 2020 erlaubt ist */
	@Column(name = "SchulformenUndGliederungen")
	@JsonProperty
	public String SchulformenUndGliederungen;

	/** zur Kompatibilität */
	@Column(name = "SchulformenAusgelaufen")
	@JsonProperty
	public String SchulformenAusgelaufen;

	/** zur Kompatibilität */
	@Column(name = "AusgelaufenInSchuljahr")
	@JsonProperty
	public String AusgelaufenInSchuljahr;

	/** Jahrgang ab dem das Fach unterrichtet werden darf */
	@Column(name = "AbJahrgang")
	@JsonProperty
	public String AbJahrgang;

	/** Boolscher Wert ob es eine Fremdsprache ist */
	@Column(name = "IstFremdsprache")
	@JsonProperty
	@Convert(converter=Boolean01Converter.class)
	@JsonSerialize(using=Boolean01ConverterSerializer.class)
	@JsonDeserialize(using=Boolean01ConverterDeserializer.class)
	public Boolean IstFremdsprache;

	/** Boolscher Wert ob es eine HerkunftsFremdsprache ist */
	@Column(name = "IstHKFS")
	@JsonProperty
	@Convert(converter=Boolean01Converter.class)
	@JsonSerialize(using=Boolean01ConverterSerializer.class)
	@JsonDeserialize(using=Boolean01ConverterDeserializer.class)
	public Boolean IstHKFS;

	/** Boolscher Wert ob es eine reguläres Unterrichtsfach ist */
	@Column(name = "IstAusRegUFach")
	@JsonProperty
	@Convert(converter=Boolean01Converter.class)
	@JsonSerialize(using=Boolean01ConverterSerializer.class)
	@JsonDeserialize(using=Boolean01ConverterDeserializer.class)
	public Boolean IstAusRegUFach;

	/** Boolscher Wert ob es eine Ersatzpflichfremdsprache ist */
	@Column(name = "IstErsatzPflichtFS")
	@JsonProperty
	@Convert(converter=Boolean01Converter.class)
	@JsonSerialize(using=Boolean01ConverterSerializer.class)
	@JsonDeserialize(using=Boolean01ConverterDeserializer.class)
	public Boolean IstErsatzPflichtFS;

	/** Boolscher Wert ob es eine eine Religionslehre im KOOP ist */
	@Column(name = "IstKonfKoop")
	@JsonProperty
	@Convert(converter=Boolean01Converter.class)
	@JsonSerialize(using=Boolean01ConverterSerializer.class)
	@JsonDeserialize(using=Boolean01ConverterDeserializer.class)
	public Boolean IstKonfKoop;

	/** Boolscher Wert ob das Fach nur in der SII unterrichtet werden darf */
	@Column(name = "NurSII")
	@JsonProperty
	@Convert(converter=Boolean01Converter.class)
	@JsonSerialize(using=Boolean01ConverterSerializer.class)
	@JsonDeserialize(using=Boolean01ConverterDeserializer.class)
	public Boolean NurSII;

	/** Boolscher Wert ob das Fach für den ASDExport vorgesehen ist */
	@Column(name = "ExportASD")
	@JsonProperty
	@Convert(converter=Boolean01Converter.class)
	@JsonSerialize(using=Boolean01ConverterSerializer.class)
	@JsonDeserialize(using=Boolean01ConverterDeserializer.class)
	public Boolean ExportASD;

	/** Gibt die Gültigkeit ab welchem Schuljahr an */
	@Column(name = "gueltigVon")
	@JsonProperty
	public String gueltigVon;

	/** Gibt die Gültigkeit bis zu welchem Schuljahr an */
	@Column(name = "gueltigBis")
	@JsonProperty
	public String gueltigBis;

	/**
	 * Erstellt ein neues Objekt der Klasse Rev8DTOSVWSZulaessigeFaecherMapping ohne eine Initialisierung der Attribute.
	 */
	private Rev8DTOSVWSZulaessigeFaecherMapping() {
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Rev8DTOSVWSZulaessigeFaecherMapping other = (Rev8DTOSVWSZulaessigeFaecherMapping) obj;
		if (Fach == null) {
			if (other.Fach != null)
				return false;
		} else if (!Fach.equals(other.Fach))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((Fach == null) ? 0 : Fach.hashCode());
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "Rev8DTOSVWSZulaessigeFaecherMapping(Fach=" + this.Fach + ", Bezeichnung=" + this.Bezeichnung + ", FachkuerzelAtomar=" + this.FachkuerzelAtomar + ", Kurzbezeichnung=" + this.Kurzbezeichnung + ", Aufgabenfeld=" + this.Aufgabenfeld + ", Fachgruppe_ID=" + this.Fachgruppe_ID + ", SchulformenUndGliederungen=" + this.SchulformenUndGliederungen + ", SchulformenAusgelaufen=" + this.SchulformenAusgelaufen + ", AusgelaufenInSchuljahr=" + this.AusgelaufenInSchuljahr + ", AbJahrgang=" + this.AbJahrgang + ", IstFremdsprache=" + this.IstFremdsprache + ", IstHKFS=" + this.IstHKFS + ", IstAusRegUFach=" + this.IstAusRegUFach + ", IstErsatzPflichtFS=" + this.IstErsatzPflichtFS + ", IstKonfKoop=" + this.IstKonfKoop + ", NurSII=" + this.NurSII + ", ExportASD=" + this.ExportASD + ", gueltigVon=" + this.gueltigVon + ", gueltigBis=" + this.gueltigBis + ")";
	}

}