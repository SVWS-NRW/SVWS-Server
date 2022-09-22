package de.nrw.schule.svws.db.dto.current.schild.schueler.abitur;

import de.nrw.schule.svws.db.DBEntityManager;
import de.nrw.schule.svws.db.converter.current.BooleanPlusMinusDefaultMinusConverter;
import de.nrw.schule.svws.db.converter.current.NoteConverterFromNotenpunkte;
import de.nrw.schule.svws.db.converter.current.gost.GOStBesondereLernleistungConverter;

import de.nrw.schule.svws.core.types.Note;
import de.nrw.schule.svws.core.types.gost.GostBesondereLernleistung;


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
import de.nrw.schule.svws.csv.converter.current.BooleanPlusMinusDefaultMinusConverterSerializer;
import de.nrw.schule.svws.csv.converter.current.BooleanPlusMinusDefaultMinusConverterDeserializer;
import de.nrw.schule.svws.csv.converter.current.NoteConverterFromNotenpunkteSerializer;
import de.nrw.schule.svws.csv.converter.current.NoteConverterFromNotenpunkteDeserializer;
import de.nrw.schule.svws.csv.converter.current.gost.GOStBesondereLernleistungConverterSerializer;
import de.nrw.schule.svws.csv.converter.current.gost.GOStBesondereLernleistungConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle SchuelerAbitur.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "SchuelerAbitur")
@NamedQuery(name="DTOSchuelerAbitur.all", query="SELECT e FROM DTOSchuelerAbitur e")
@NamedQuery(name="DTOSchuelerAbitur.id", query="SELECT e FROM DTOSchuelerAbitur e WHERE e.ID = :value")
@NamedQuery(name="DTOSchuelerAbitur.id.multiple", query="SELECT e FROM DTOSchuelerAbitur e WHERE e.ID IN :value")
@NamedQuery(name="DTOSchuelerAbitur.schueler_id", query="SELECT e FROM DTOSchuelerAbitur e WHERE e.Schueler_ID = :value")
@NamedQuery(name="DTOSchuelerAbitur.schueler_id.multiple", query="SELECT e FROM DTOSchuelerAbitur e WHERE e.Schueler_ID IN :value")
@NamedQuery(name="DTOSchuelerAbitur.schuljahresabschnitts_id", query="SELECT e FROM DTOSchuelerAbitur e WHERE e.Schuljahresabschnitts_ID = :value")
@NamedQuery(name="DTOSchuelerAbitur.schuljahresabschnitts_id.multiple", query="SELECT e FROM DTOSchuelerAbitur e WHERE e.Schuljahresabschnitts_ID IN :value")
@NamedQuery(name="DTOSchuelerAbitur.facharbeitfach", query="SELECT e FROM DTOSchuelerAbitur e WHERE e.FacharbeitFach = :value")
@NamedQuery(name="DTOSchuelerAbitur.facharbeitfach.multiple", query="SELECT e FROM DTOSchuelerAbitur e WHERE e.FacharbeitFach IN :value")
@NamedQuery(name="DTOSchuelerAbitur.facharbeitnotenpunkte", query="SELECT e FROM DTOSchuelerAbitur e WHERE e.FacharbeitNotenpunkte = :value")
@NamedQuery(name="DTOSchuelerAbitur.facharbeitnotenpunkte.multiple", query="SELECT e FROM DTOSchuelerAbitur e WHERE e.FacharbeitNotenpunkte IN :value")
@NamedQuery(name="DTOSchuelerAbitur.fehlstundensumme", query="SELECT e FROM DTOSchuelerAbitur e WHERE e.FehlstundenSumme = :value")
@NamedQuery(name="DTOSchuelerAbitur.fehlstundensumme.multiple", query="SELECT e FROM DTOSchuelerAbitur e WHERE e.FehlstundenSumme IN :value")
@NamedQuery(name="DTOSchuelerAbitur.fehlstundensummeunentschuldigt", query="SELECT e FROM DTOSchuelerAbitur e WHERE e.FehlstundenSummeUnentschuldigt = :value")
@NamedQuery(name="DTOSchuelerAbitur.fehlstundensummeunentschuldigt.multiple", query="SELECT e FROM DTOSchuelerAbitur e WHERE e.FehlstundenSummeUnentschuldigt IN :value")
@NamedQuery(name="DTOSchuelerAbitur.projektkursthema", query="SELECT e FROM DTOSchuelerAbitur e WHERE e.ProjektkursThema = :value")
@NamedQuery(name="DTOSchuelerAbitur.projektkursthema.multiple", query="SELECT e FROM DTOSchuelerAbitur e WHERE e.ProjektkursThema IN :value")
@NamedQuery(name="DTOSchuelerAbitur.fremdsprachesekimanuellgeprueft", query="SELECT e FROM DTOSchuelerAbitur e WHERE e.FremdspracheSekIManuellGeprueft = :value")
@NamedQuery(name="DTOSchuelerAbitur.fremdsprachesekimanuellgeprueft.multiple", query="SELECT e FROM DTOSchuelerAbitur e WHERE e.FremdspracheSekIManuellGeprueft IN :value")
@NamedQuery(name="DTOSchuelerAbitur.blocki_anzahlkurseeingebracht", query="SELECT e FROM DTOSchuelerAbitur e WHERE e.BlockI_AnzahlKurseEingebracht = :value")
@NamedQuery(name="DTOSchuelerAbitur.blocki_anzahlkurseeingebracht.multiple", query="SELECT e FROM DTOSchuelerAbitur e WHERE e.BlockI_AnzahlKurseEingebracht IN :value")
@NamedQuery(name="DTOSchuelerAbitur.blocki_anzahldefiziteeingebracht", query="SELECT e FROM DTOSchuelerAbitur e WHERE e.BlockI_AnzahlDefiziteEingebracht = :value")
@NamedQuery(name="DTOSchuelerAbitur.blocki_anzahldefiziteeingebracht.multiple", query="SELECT e FROM DTOSchuelerAbitur e WHERE e.BlockI_AnzahlDefiziteEingebracht IN :value")
@NamedQuery(name="DTOSchuelerAbitur.blocki_anzahldefizitelk", query="SELECT e FROM DTOSchuelerAbitur e WHERE e.BlockI_AnzahlDefiziteLK = :value")
@NamedQuery(name="DTOSchuelerAbitur.blocki_anzahldefizitelk.multiple", query="SELECT e FROM DTOSchuelerAbitur e WHERE e.BlockI_AnzahlDefiziteLK IN :value")
@NamedQuery(name="DTOSchuelerAbitur.blocki_anzahldefizite0punkte", query="SELECT e FROM DTOSchuelerAbitur e WHERE e.BlockI_AnzahlDefizite0Punkte = :value")
@NamedQuery(name="DTOSchuelerAbitur.blocki_anzahldefizite0punkte.multiple", query="SELECT e FROM DTOSchuelerAbitur e WHERE e.BlockI_AnzahlDefizite0Punkte IN :value")
@NamedQuery(name="DTOSchuelerAbitur.blocki_punktsummenormiert", query="SELECT e FROM DTOSchuelerAbitur e WHERE e.BlockI_PunktsummeNormiert = :value")
@NamedQuery(name="DTOSchuelerAbitur.blocki_punktsummenormiert.multiple", query="SELECT e FROM DTOSchuelerAbitur e WHERE e.BlockI_PunktsummeNormiert IN :value")
@NamedQuery(name="DTOSchuelerAbitur.blocki_notenpunktdurchschnitteingebrachterkurse", query="SELECT e FROM DTOSchuelerAbitur e WHERE e.BlockI_NotenpunktdurchschnittEingebrachterKurse = :value")
@NamedQuery(name="DTOSchuelerAbitur.blocki_notenpunktdurchschnitteingebrachterkurse.multiple", query="SELECT e FROM DTOSchuelerAbitur e WHERE e.BlockI_NotenpunktdurchschnittEingebrachterKurse IN :value")
@NamedQuery(name="DTOSchuelerAbitur.blocki_summenotenpunktegk", query="SELECT e FROM DTOSchuelerAbitur e WHERE e.BlockI_SummeNotenpunkteGK = :value")
@NamedQuery(name="DTOSchuelerAbitur.blocki_summenotenpunktegk.multiple", query="SELECT e FROM DTOSchuelerAbitur e WHERE e.BlockI_SummeNotenpunkteGK IN :value")
@NamedQuery(name="DTOSchuelerAbitur.blocki_summenotenpunktelk", query="SELECT e FROM DTOSchuelerAbitur e WHERE e.BlockI_SummeNotenpunkteLK = :value")
@NamedQuery(name="DTOSchuelerAbitur.blocki_summenotenpunktelk.multiple", query="SELECT e FROM DTOSchuelerAbitur e WHERE e.BlockI_SummeNotenpunkteLK IN :value")
@NamedQuery(name="DTOSchuelerAbitur.blocki_hatzulassung", query="SELECT e FROM DTOSchuelerAbitur e WHERE e.BlockI_HatZulassung = :value")
@NamedQuery(name="DTOSchuelerAbitur.blocki_hatzulassung.multiple", query="SELECT e FROM DTOSchuelerAbitur e WHERE e.BlockI_HatZulassung IN :value")
@NamedQuery(name="DTOSchuelerAbitur.besonderelernleistungart", query="SELECT e FROM DTOSchuelerAbitur e WHERE e.BesondereLernleistungArt = :value")
@NamedQuery(name="DTOSchuelerAbitur.besonderelernleistungart.multiple", query="SELECT e FROM DTOSchuelerAbitur e WHERE e.BesondereLernleistungArt IN :value")
@NamedQuery(name="DTOSchuelerAbitur.besonderelernleistungnotenpunkte", query="SELECT e FROM DTOSchuelerAbitur e WHERE e.BesondereLernleistungNotenpunkte = :value")
@NamedQuery(name="DTOSchuelerAbitur.besonderelernleistungnotenpunkte.multiple", query="SELECT e FROM DTOSchuelerAbitur e WHERE e.BesondereLernleistungNotenpunkte IN :value")
@NamedQuery(name="DTOSchuelerAbitur.besonderelernleistungthema", query="SELECT e FROM DTOSchuelerAbitur e WHERE e.BesondereLernleistungThema = :value")
@NamedQuery(name="DTOSchuelerAbitur.besonderelernleistungthema.multiple", query="SELECT e FROM DTOSchuelerAbitur e WHERE e.BesondereLernleistungThema IN :value")
@NamedQuery(name="DTOSchuelerAbitur.pruefung_punktsumme", query="SELECT e FROM DTOSchuelerAbitur e WHERE e.Pruefung_Punktsumme = :value")
@NamedQuery(name="DTOSchuelerAbitur.pruefung_punktsumme.multiple", query="SELECT e FROM DTOSchuelerAbitur e WHERE e.Pruefung_Punktsumme IN :value")
@NamedQuery(name="DTOSchuelerAbitur.pruefung_anzahldefizite", query="SELECT e FROM DTOSchuelerAbitur e WHERE e.Pruefung_AnzahlDefizite = :value")
@NamedQuery(name="DTOSchuelerAbitur.pruefung_anzahldefizite.multiple", query="SELECT e FROM DTOSchuelerAbitur e WHERE e.Pruefung_AnzahlDefizite IN :value")
@NamedQuery(name="DTOSchuelerAbitur.pruefung_anzahldefizitelk", query="SELECT e FROM DTOSchuelerAbitur e WHERE e.Pruefung_AnzahlDefiziteLK = :value")
@NamedQuery(name="DTOSchuelerAbitur.pruefung_anzahldefizitelk.multiple", query="SELECT e FROM DTOSchuelerAbitur e WHERE e.Pruefung_AnzahlDefiziteLK IN :value")
@NamedQuery(name="DTOSchuelerAbitur.pruefung_hatbestanden", query="SELECT e FROM DTOSchuelerAbitur e WHERE e.Pruefung_hatBestanden = :value")
@NamedQuery(name="DTOSchuelerAbitur.pruefung_hatbestanden.multiple", query="SELECT e FROM DTOSchuelerAbitur e WHERE e.Pruefung_hatBestanden IN :value")
@NamedQuery(name="DTOSchuelerAbitur.abiturnote", query="SELECT e FROM DTOSchuelerAbitur e WHERE e.AbiturNote = :value")
@NamedQuery(name="DTOSchuelerAbitur.abiturnote.multiple", query="SELECT e FROM DTOSchuelerAbitur e WHERE e.AbiturNote IN :value")
@NamedQuery(name="DTOSchuelerAbitur.abiturgesamtpunktzahl", query="SELECT e FROM DTOSchuelerAbitur e WHERE e.AbiturGesamtPunktzahl = :value")
@NamedQuery(name="DTOSchuelerAbitur.abiturgesamtpunktzahl.multiple", query="SELECT e FROM DTOSchuelerAbitur e WHERE e.AbiturGesamtPunktzahl IN :value")
@NamedQuery(name="DTOSchuelerAbitur.verbesserungabpunktzahl", query="SELECT e FROM DTOSchuelerAbitur e WHERE e.VerbesserungAbPunktzahl = :value")
@NamedQuery(name="DTOSchuelerAbitur.verbesserungabpunktzahl.multiple", query="SELECT e FROM DTOSchuelerAbitur e WHERE e.VerbesserungAbPunktzahl IN :value")
@NamedQuery(name="DTOSchuelerAbitur.verbesserungbenoetigepunkte", query="SELECT e FROM DTOSchuelerAbitur e WHERE e.VerbesserungBenoetigePunkte = :value")
@NamedQuery(name="DTOSchuelerAbitur.verbesserungbenoetigepunkte.multiple", query="SELECT e FROM DTOSchuelerAbitur e WHERE e.VerbesserungBenoetigePunkte IN :value")
@NamedQuery(name="DTOSchuelerAbitur.primaryKeyQuery", query="SELECT e FROM DTOSchuelerAbitur e WHERE e.ID = ?1")
@NamedQuery(name="DTOSchuelerAbitur.all.migration", query="SELECT e FROM DTOSchuelerAbitur e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID","Schueler_ID","Schuljahresabschnitts_ID","FacharbeitFach","FacharbeitNotenpunkte","FehlstundenSumme","FehlstundenSummeUnentschuldigt","-","-","-","-","-","-","-","ProjektkursThema","FremdspracheSekIManuellGeprueft","BlockI_AnzahlKurseEingebracht","BlockI_AnzahlDefiziteEingebracht","BlockI_AnzahlDefiziteLK","BlockI_AnzahlDefizite0Punkte","BlockI_PunktsummeNormiert","BlockI_NotenpunktdurchschnittEingebrachterKurse","BlockI_SummeNotenpunkteGK","BlockI_SummeNotenpunkteLK","-","BlockI_HatZulassung","BesondereLernleistungArt","BesondereLernleistungNotenpunkte","BesondereLernleistungThema","Pruefung_Punktsumme","Pruefung_AnzahlDefizite","Pruefung_AnzahlDefiziteLK","Pruefung_hatBestanden","AbiturNote","AbiturGesamtPunktzahl","VerbesserungAbPunktzahl","VerbesserungBenoetigePunkte"})
public class DTOSchuelerAbitur {

	/** Eine eindeutige ID für den Abiturdatensatz */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** Die eindeutige ID des Schülers – verweist auf den Schüler */
	@Column(name = "Schueler_ID")
	@JsonProperty
	public Long Schueler_ID;

	/** ID des Schuljahresabschnittes aus der Tabelle Schuljahresabschnitte */
	@Column(name = "Schuljahresabschnitts_ID")
	@JsonProperty
	public Long Schuljahresabschnitts_ID;

	/** BK: Facharbeit – Fach */
	@Column(name = "FA_Fach")
	@JsonProperty
	public String FacharbeitFach;

	/** BK: Facharbeit – Notenpunkte der Facharbeit */
	@Column(name = "FA_Punkte")
	@JsonProperty
	public Integer FacharbeitNotenpunkte;

	/** Die Summe der Fehlstunden – sofern berechnet */
	@Column(name = "FehlStd")
	@JsonProperty
	public Integer FehlstundenSumme;

	/** Die Summe der unentschuldigten Fehlstunden – sofern berechnet */
	@Column(name = "uFehlStd")
	@JsonProperty
	public Integer FehlstundenSummeUnentschuldigt;

	/** Projektkurs: Das Thema des Projektkurses */
	@Column(name = "Thema_PJK")
	@JsonProperty
	public String ProjektkursThema;

	/** true, falls die zweite Fremsprache in der Sekundarstufe 1 manuell geprüft wurde und vom Algorithmus als gegeben angesehen werden kann. */
	@Column(name = "FS2_SekI_Manuell")
	@JsonProperty
	@Convert(converter=BooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using=BooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using=BooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean FremdspracheSekIManuellGeprueft;

	/** Block I: Die Anzahl der eingebrachten Kurse aus der Qualifikationsphase */
	@Column(name = "Kurse_I")
	@JsonProperty
	public Integer BlockI_AnzahlKurseEingebracht;

	/** Block I: Die Gesamtanzahl der eingebrachten Defizite aus der Qualifikationsphase */
	@Column(name = "Defizite_I")
	@JsonProperty
	public Integer BlockI_AnzahlDefiziteEingebracht;

	/** Block I: Die Anzahl der Defizite im LK-Bereich aus der Qualifikationsphase */
	@Column(name = "LK_Defizite_I")
	@JsonProperty
	public Integer BlockI_AnzahlDefiziteLK;

	/** Block I: Die Anzahl der Defizite mit 0 Punkten */
	@Column(name = "AnzahlKurse_0")
	@JsonProperty
	public Integer BlockI_AnzahlDefizite0Punkte;

	/** Block I: Die normierte Punktesumme aus der Qualifikationsphase */
	@Column(name = "Punktsumme_I")
	@JsonProperty
	public Integer BlockI_PunktsummeNormiert;

	/** Block I: Der Notenpunktdurchschnitt aus den eingebrachten Kurse der Qualifikationsphase */
	@Column(name = "Durchschnitt_I")
	@JsonProperty
	public Double BlockI_NotenpunktdurchschnittEingebrachterKurse;

	/** Block I: Die Summe der erreichten Notepunkte aus dem Grundkurs-Bereich */
	@Column(name = "SummeGK")
	@JsonProperty
	public Integer BlockI_SummeNotenpunkteGK;

	/** Block I: Die Summe der erreichten Notepunkte aus dem Leistungskurs-Bereich */
	@Column(name = "SummeLK")
	@JsonProperty
	public Integer BlockI_SummeNotenpunkteLK;

	/** Block I: Gibt an, ob der Schüler zu den Abiturprüfungen zugelassen ist, d.h. die Bedingungen für Block I erfüllt hat (+: Ja, -: Nein, R: Freiwilliger Rücktritt) */
	@Column(name = "Zugelassen")
	@JsonProperty
	public String BlockI_HatZulassung;

	/** Besondere Lernleistung: Die Art der besonderen Lernleistung(K: Keine, E: Externe, P: in einem Projektkurs) */
	@Column(name = "BLL_Art")
	@JsonProperty
	@Convert(converter=GOStBesondereLernleistungConverter.class)
	@JsonSerialize(using=GOStBesondereLernleistungConverterSerializer.class)
	@JsonDeserialize(using=GOStBesondereLernleistungConverterDeserializer.class)
	public GostBesondereLernleistung BesondereLernleistungArt;

	/** Besondere Lernleistung: Die Notenpunkte, welche bei der besonderen Lernleistung erreicht wurden – einfach gewichtet */
	@Column(name = "BLL_Punkte")
	@JsonProperty
	@Convert(converter=NoteConverterFromNotenpunkte.class)
	@JsonSerialize(using=NoteConverterFromNotenpunkteSerializer.class)
	@JsonDeserialize(using=NoteConverterFromNotenpunkteDeserializer.class)
	public Note BesondereLernleistungNotenpunkte;

	/** Besondere Lernleistung: Das Thema der besonderen Lernleistung */
	@Column(name = "Thema_BLL")
	@JsonProperty
	public String BesondereLernleistungThema;

	/** Block II: Die Punktsumme aus den Abiturprüfungen, NULL falls noch nicht berechnet */
	@Column(name = "Punktsumme_II")
	@JsonProperty
	public Integer Pruefung_Punktsumme;

	/** Block II: Die Anzahl der Defizite bei den Abiturprüfungen, NULL falls noch nicht berechnet */
	@Column(name = "Defizite_II")
	@JsonProperty
	public Integer Pruefung_AnzahlDefizite;

	/** Block II: Die Anzahl der LK-Defizite bei den Abiturprüfungen, NULL falls noch nicht berechnet */
	@Column(name = "LK_Defizite_II")
	@JsonProperty
	public Integer Pruefung_AnzahlDefiziteLK;

	/** Gesamt: Gibt an, ob die Abiturprüfung bestanden wurde */
	@Column(name = "PruefungBestanden")
	@JsonProperty
	@Convert(converter=BooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using=BooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using=BooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean Pruefung_hatBestanden;

	/** Gesamt: Die Abschlussnote des Abiturs */
	@Column(name = "Note")
	@JsonProperty
	public String AbiturNote;

	/** Gesamt: Die Gesamtpunktzahl aus beiden Blöcken (Qualifikationsphase und Abiturprüfung) */
	@Column(name = "GesamtPunktzahl")
	@JsonProperty
	public Integer AbiturGesamtPunktzahl;

	/** Gesamt: Die Gesamtpunktzahl, ab der eine höhere Notenstufe erreicht würde */
	@Column(name = "Notensprung")
	@JsonProperty
	public Integer VerbesserungAbPunktzahl;

	/** Gesamt: Die Anzahl an Punkten, die zur nächsthöheren Note fehlt */
	@Column(name = "FehlendePunktzahl")
	@JsonProperty
	public Integer VerbesserungBenoetigePunkte;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOSchuelerAbitur ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOSchuelerAbitur() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOSchuelerAbitur ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Schueler_ID   der Wert für das Attribut Schueler_ID
	 */
	public DTOSchuelerAbitur(final Long ID, final Long Schueler_ID) {
		if (ID == null) { 
			throw new NullPointerException("ID must not be null");
		}
		this.ID = ID;
		if (Schueler_ID == null) { 
			throw new NullPointerException("Schueler_ID must not be null");
		}
		this.Schueler_ID = Schueler_ID;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOSchuelerAbitur other = (DTOSchuelerAbitur) obj;
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
		return "DTOSchuelerAbitur(ID=" + this.ID + ", Schueler_ID=" + this.Schueler_ID + ", Schuljahresabschnitts_ID=" + this.Schuljahresabschnitts_ID + ", FacharbeitFach=" + this.FacharbeitFach + ", FacharbeitNotenpunkte=" + this.FacharbeitNotenpunkte + ", FehlstundenSumme=" + this.FehlstundenSumme + ", FehlstundenSummeUnentschuldigt=" + this.FehlstundenSummeUnentschuldigt + ", ProjektkursThema=" + this.ProjektkursThema + ", FremdspracheSekIManuellGeprueft=" + this.FremdspracheSekIManuellGeprueft + ", BlockI_AnzahlKurseEingebracht=" + this.BlockI_AnzahlKurseEingebracht + ", BlockI_AnzahlDefiziteEingebracht=" + this.BlockI_AnzahlDefiziteEingebracht + ", BlockI_AnzahlDefiziteLK=" + this.BlockI_AnzahlDefiziteLK + ", BlockI_AnzahlDefizite0Punkte=" + this.BlockI_AnzahlDefizite0Punkte + ", BlockI_PunktsummeNormiert=" + this.BlockI_PunktsummeNormiert + ", BlockI_NotenpunktdurchschnittEingebrachterKurse=" + this.BlockI_NotenpunktdurchschnittEingebrachterKurse + ", BlockI_SummeNotenpunkteGK=" + this.BlockI_SummeNotenpunkteGK + ", BlockI_SummeNotenpunkteLK=" + this.BlockI_SummeNotenpunkteLK + ", BlockI_HatZulassung=" + this.BlockI_HatZulassung + ", BesondereLernleistungArt=" + this.BesondereLernleistungArt + ", BesondereLernleistungNotenpunkte=" + this.BesondereLernleistungNotenpunkte + ", BesondereLernleistungThema=" + this.BesondereLernleistungThema + ", Pruefung_Punktsumme=" + this.Pruefung_Punktsumme + ", Pruefung_AnzahlDefizite=" + this.Pruefung_AnzahlDefizite + ", Pruefung_AnzahlDefiziteLK=" + this.Pruefung_AnzahlDefiziteLK + ", Pruefung_hatBestanden=" + this.Pruefung_hatBestanden + ", AbiturNote=" + this.AbiturNote + ", AbiturGesamtPunktzahl=" + this.AbiturGesamtPunktzahl + ", VerbesserungAbPunktzahl=" + this.VerbesserungAbPunktzahl + ", VerbesserungBenoetigePunkte=" + this.VerbesserungBenoetigePunkte + ")";
	}

}