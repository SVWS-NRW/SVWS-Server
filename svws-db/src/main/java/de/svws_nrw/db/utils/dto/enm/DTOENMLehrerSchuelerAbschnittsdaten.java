package de.svws_nrw.db.utils.dto.enm;

import java.util.List;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.current.BooleanPlusMinusDefaultMinusConverter;
import de.svws_nrw.db.converter.current.DatumConverter;
import jakarta.persistence.Cacheable;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;


/**
 * Diese Klasse definiert ein DTO für das Lesen von ENM-Daten
 * eines Lehrers in Bezug auf die Leistungs- und Lernabschnittsdaten
 * von Schülern aus der SVWS-DB.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
public final class DTOENMLehrerSchuelerAbschnittsdaten {

	/** Eine eindeutige ID für die Leistungsdaten des Schülers */
	@Id
	public long leistungID;

	/** Die erteilte Note */
	public String noteKuerzel;

	/** Der Zeitstempel der letzten Änderung an der erteilten Note */
	public String tsNote;

	/** Die erteilte Quartals-Note */
	public String noteKuerzelQuartal;

	/** Der Zeitstempel der letzten Änderung an der erteilten Quartals-Note */
	public String tsNoteQuartal;

	/** Die allgemeine Kursart des Faches (z.B. GK, LK) */
	public String kursart;

	/** gibt an, ob das Fach als Abiturfach belegt wurde (NULL, 1, 2, 3, 4) */
	public String AbiturFach;

	/** die ID des Fachlehrers */
	public Long lehrerID;

	/** die ID des Kurses, sofern es sich um Kursunterricht handelt - ansonsten null */
	public Long kursID;

	/** die ID des Faches */
	public Long fachID;

	/** Die Wochenstunden des Faches. */
	public Integer wochenstunden;

	/** Fachbezogene Fehlstunden */
	public Integer fehlstundenGesamt;
	/** Der Zeitstempel der letzten Änderung an den fachbezogenen Fehlstunden */
	public String tsFehlstundenGesamt;

	/** Fachbezogene Fehlstunden, unentschuldigt */
	public Integer fehlstundenUnentschuldigt;

	/** Der Zeitstempel der letzten Änderung an den fachbezogenen Fehlstunden, unentschuldigt */
	public String tsFehlstundenUnentschuldigt;

	/** Abschnittsbezogene Fehlstunden */
	public Integer fehlstundenSummeGesamt;

	/** Zeitstempel: Abschnittsbezogene Fehlstunden */
	public String tsFehlstundenSummeGesamt;

	/** Abschnittsbezogene Fehlstunden, unentschuldigt */
	public Integer fehlstundenSummeUnentschuldigt;
	/** Zeitstempel: Abschnittsbezogene Fehlstunden, unentschuldigt */
	public String tsFehlstundenSummeUnentschuldigt;

	/** Text für Fachbezogene Lernentwicklung Bemerkung */
	public String fachbezogeneBemerkungen;

	/** Der Zeitstempel der letzten Änderung an den Bemerkungen zur fachbezogenen Lernentwicklung */
	public String tsFachbezogeneBemerkungen;

	/** Die ID des Schuelers zu dem die Leistungsdaten gehören. */
	public long schuelerID;

	/** Eine eindeutige ID für den Lernabschnitt des Schülers */
	public long abschnittID;

	/** Die eindeutige ID für Jahrgang des Lernabschnitts */
	public long jahrgangID;

	/** Die Klasse, in der sich der Schüler im dem Abschnitt befindet */
	public String klasse;

	/** Die Prüfungsordnung, die in dem Lernabschnitt verwendet werden muss. */
	public String pruefungsordnung;

	/** ggf. die Sprache des Bilingualen Zweigs */
	public String BilingualerZweig;

	/** Lernbereichnote Gesellschaftswissenschaft oder Arbeitlehre HA10 */
	public String lernbereich1notenKuerzel;

	/** Lernbereichnote Naturwissenschaft HA10 */
	public String lernbereich2notenKuerzel;

	/** Das Kürzel des Hauptförderschwerpunktes */
	public String foerderschwerpunkt1Kuerzel;

	/** Das Kürzel des weitereren Förderschwerpunktes */
	public String foerderschwerpunkt2Kuerzel;

	/** Gibt an ob der Schüler zieldifferent unterrichtet wird */
	@Convert(converter = BooleanPlusMinusDefaultMinusConverter.class)
	public Boolean ZieldifferentesLernen;

	/** Gibt an ob die Leistung gemahnt wurde. */
	@Convert(converter = BooleanPlusMinusDefaultMinusConverter.class)
	public Boolean istGemahnt;
	/** Der Zeitstempel, wann gesetzt wurde, ob die Leistung gemahnt wurde */
	public String tsIstGemahnt;

	/** Enthält bei erfolgter Mahnung das Mahndatum */
	@Convert(converter = DatumConverter.class)
	public String mahndatum;

	/** Die Zeugnis-Bemerkungen */
	public String zeugnisBemerkungen;

	/** Der Zeitstempel mit den letzten Änderungen an den Zeugnis-Bemerkungen */
	public String tsZeugnisBemerkungen;
	/** Die Bemerkungen zum Arbeits- und Sozialverhalten */
	public String ASV;

	/** Der Zeitstempel mit den letzten Änderungen an den Bemerkungen zum Arbeits- und Sozialverhalten */
	public String tsASV;

	/** Die LELS-Bemerkungen */
	public String LELS;

	/** Der Zeitstempel mit den letzten Änderungen zu der Lern und Leistungsentwicklung (LELS) in den Fächern */
	public String tsLELS;

	/** Die Bemerkungen zum außerunterrichtlichen Engagement */
	public String AUE;

	/** Der Zeitstempel mit den letzten Änderungen an den Bemerkungen zum außerunterrichtlichen Engagement */
	public String tsAUE;

	/** Die ESF-Bemerkungen */
	public String ESF;

	/** Der Zeitstempel mit den letzten Änderungen an der \"Empfehlung der Schulform\" beim Übergang von Primar- nach SekI */
	public String tsESF;

	/** Die Bemerkungen zu den Förderschwerpunkten */
	public String bemerkungFSP;

	/** Der Zeitstempel mit den letzten Änderungen an der Förderschwerpunktbemerkung */
	public String tsBemerkungFSP;

	/** Die Bemerkungen zur Versetzung */
	public String bemerkungVersetzung;

	/** Der Zeitstempel mit den letzten Änderungen an den Bemerkungen zur Versetzung */
	public String tsBemerkungVersetzung;


	/**
	 * Default-Konstruktor für das Erzeugen dieser DBEntity
	 */
	private DTOENMLehrerSchuelerAbschnittsdaten() {
	}


	/**
	 * Stellt eine Anfrage nach den Daten des Externen Notenmoduls (ENM) zu dem
	 * angegebenen Abschnitt aus dem angegebenen Schuljahr bezogen auf den Lehrer,
	 * dessen Kürzel angegeben ist.
	 *
	 * @param conn                   die Datenbankverbindung
	 * @param schuljahresabschnitt   der Schuljahres-Abschnitt aus dem Schuljahr, zu dem die ENM-Daten ermittelt werden sollen
	 * @param lehrerKrz              das Kürzel des Lehrers, dessen ENM-Daten bestimmt werden.
	 *
	 * @return eine Liste mit den DTOs
	 */
	public static List<DTOENMLehrerSchuelerAbschnittsdaten> query(final DBEntityManager conn, final long schuljahresabschnitt, final String lehrerKrz) {
		return conn.queryNative(
				"""
				SELECT
				    la.Schueler_ID as schuelerID,
				    la.ID as abschnittID,
				    la.Jahrgang_ID as jahrgangID,
				    k.Klasse as klasse,
				    la.PruefOrdnung as pruefungsordnung,
				    la.BilingualerZweig as BilingualerZweig,
				    la.Gesamtnote_GS as lernbereich1notenKuerzel,
				    la.Gesamtnote_NW as lernbereich2notenKuerzel,
				    fs1.StatistikKrz as foerderschwerpunkt1Kuerzel,
				    fs2.StatistikKrz as foerderschwerpunkt2Kuerzel,
				    la.ZieldifferentesLernen as ZieldifferentesLernen,
				    la.SumFehlStd as fehlstundenSummeGesamt,
				    enmla.tsSumFehlStd as tsFehlstundenSummeGesamt,
				    la.SumFehlStdU as fehlstundenSummeUnentschuldigt,
				    enmla.tsSumFehlStdU as tsFehlstundenSummeUnentschuldigt,
				    la.ZeugnisBem as zeugnisBemerkungen,
				    enmla.tsZeugnisBem as tsZeugnisBemerkungen,
				    bem.ASV as ASV,
				    enmla.tsASV as tsASV,
				    bem.LELS as LELS,
				    enmla.tsLELS as tsLELS,
				    bem.AUE as AUE,
				    enmla.tsAUE as tsAUE,
				    bem.ESF as ESF,
				    enmla.tsESF as tsESF,
				    bem.BemerkungFSP as bemerkungFSP,
				    enmla.tsBemerkungFSP as tsBemerkungFSP,
				    bem.bemerkungVersetzung as bemerkungVersetzung,
				    enmla.tsBemerkungVersetzung as tsBemerkungVersetzung,
				    ld.ID as leistungID,
				    ld.NotenKrz as noteKuerzel,
				    enmld.tsNotenKrz as tsNote,
				    ld.NotenKrzQuartal as noteKuerzelQuartal,
				    enmld.tsNotenKrzQuartal as tsNoteQuartal,
				    ld.Kursart as kursart,
				    ld.Fachlehrer_ID as lehrerID,
				    ld.Kurs_ID as kursID,
				    ld.Fach_ID as fachID,
				    ld.Wochenstunden as wochenstunden,
				    ld.AbiFach as abiturfach,
				    ld.FehlStd as fehlstundenGesamt,
				    enmld.tsFehlStd as tsFehlstundenGesamt,
				    ld.uFehlStd as fehlstundenUnentschuldigt,
				    enmld.tsuFehlStd as tsFehlstundenUnentschuldigt,
				    ld.Lernentw as fachbezogeneBemerkungen,
				    enmld.tsLernentw as tsFachbezogeneBemerkungen,
				    ld.Warnung as istGemahnt,
				    enmld.tsWarnung as tsIstGemahnt,
				    ld.Warndatum as mahndatum
				FROM
				    SchuelerLernabschnittsdaten la
				        JOIN SchuelerLeistungsdaten ld ON la.ID = ld.Abschnitt_ID
				            AND la.Schuljahresabschnitts_ID = %d AND la.WechselNr = 0
				        JOIN Schueler s ON la.Schueler_ID = s.ID AND s.Geloescht = '-' AND s.Status in (2, 6)
				            AND s.Schuljahresabschnitts_ID = la.Schuljahresabschnitts_ID
				        JOIN K_Lehrer kl ON ld.Fachlehrer_ID = kl.ID AND kl.Kuerzel = '%s'
				        LEFT JOIN K_Foerderschwerpunkt fs1 ON la.Foerderschwerpunkt_ID = fs1.ID
				        LEFT JOIN K_Foerderschwerpunkt fs2 ON la.Foerderschwerpunkt2_ID = fs2.ID
				        LEFT JOIN Klassen k ON la.Klassen_ID = k.ID
				        LEFT JOIN SchuelerLD_PSFachBem bem ON la.ID = bem.Abschnitt_ID
				        LEFT JOIN TimestampsSchuelerLernabschnittsdaten enmla ON la.ID = enmla.ID
				        LEFT JOIN TimestampsSchuelerLeistungsdaten enmld ON ld.ID = enmld.ID
				ORDER BY
				     la.Schueler_ID, la.ID, ld.ID
				;
				""".formatted(schuljahresabschnitt, lehrerKrz), DTOENMLehrerSchuelerAbschnittsdaten.class);
	}


	/**
	 * Stellt eine Anfrage nach den Daten des Externen Notenmoduls (ENM) zu dem
	 * angegebenen Abschnitt aus dem angegebenen Schuljahr bezogen auf den Lehrer,
	 * dessen Kürzel angegeben ist.
	 *
	 * @param conn                   die Datenbankverbindung
	 * @param schuljahresabschnitt   der Schuljahres-Abschnitt aus dem Schuljahr, zu dem die ENM-Daten ermittelt werden sollen
	 *
	 * @return eine Liste mit den DTOs
	 */
	public static List<DTOENMLehrerSchuelerAbschnittsdaten> queryAll(final DBEntityManager conn, final long schuljahresabschnitt) {
		return conn.queryNative(
				"""
				SELECT
				    la.Schueler_ID as schuelerID,
				    la.ID as abschnittID,
				    la.Jahrgang_ID as jahrgangID,
				    k.Klasse as klasse,
				    la.PruefOrdnung as pruefungsordnung,
				    la.BilingualerZweig as BilingualerZweig,
				    la.Gesamtnote_GS as lernbereich1notenKuerzel,
				    la.Gesamtnote_NW as lernbereich2notenKuerzel,
				    fs1.StatistikKrz as foerderschwerpunkt1Kuerzel,
				    fs2.StatistikKrz as foerderschwerpunkt2Kuerzel,
				    la.ZieldifferentesLernen as ZieldifferentesLernen,
				    la.SumFehlStd as fehlstundenSummeGesamt,
				    enmla.tsSumFehlStd as tsFehlstundenSummeGesamt,
				    la.SumFehlStdU as fehlstundenSummeUnentschuldigt,
				    enmla.tsSumFehlStdU as tsFehlstundenSummeUnentschuldigt,
				    la.ZeugnisBem as zeugnisBemerkungen,
				    enmla.tsZeugnisBem as tsZeugnisBemerkungen,
				    bem.ASV as ASV,
				    enmla.tsASV as tsASV,
				    bem.LELS as LELS,
				    enmla.tsLELS as tsLELS,
				    bem.AUE as AUE,
				    enmla.tsAUE as tsAUE,
				    bem.ESF as ESF,
				    enmla.tsESF as tsESF,
				    bem.BemerkungFSP as bemerkungFSP,
				    enmla.tsBemerkungFSP as tsBemerkungFSP,
				    bem.bemerkungVersetzung as bemerkungVersetzung,
				    enmla.tsBemerkungVersetzung as tsBemerkungVersetzung,
				    ld.ID as leistungID,
				    ld.NotenKrz as noteKuerzel,
				    enmld.tsNotenKrz as tsNote,
				    ld.NotenKrzQuartal as noteKuerzelQuartal,
				    enmld.tsNotenKrzQuartal as tsNoteQuartal,
				    ld.Kursart as kursart,
				    ld.Fachlehrer_ID as lehrerID,
				    ld.Kurs_ID as kursID,
				    ld.Fach_ID as fachID,
				    ld.Wochenstunden as wochenstunden,
				    ld.AbiFach as abiturfach,
				    ld.FehlStd as fehlstundenGesamt,
				    enmld.tsFehlStd as tsFehlstundenGesamt,
				    ld.uFehlStd as fehlstundenUnentschuldigt,
				    enmld.tsuFehlStd as tsFehlstundenUnentschuldigt,
				    ld.Lernentw as fachbezogeneBemerkungen,
				    enmld.tsLernentw as tsFachbezogeneBemerkungen,
				    ld.Warnung as istGemahnt,
				    enmld.tsWarnung as tsIstGemahnt,
				    ld.Warndatum as mahndatum
				FROM
				    SchuelerLernabschnittsdaten la
				        JOIN SchuelerLeistungsdaten ld ON la.ID = ld.Abschnitt_ID
				            AND la.Schuljahresabschnitts_ID = %d AND la.WechselNr = 0
				        JOIN Schueler s ON la.Schueler_ID = s.ID AND s.Geloescht = '-' AND s.Status in (2, 6)
				            AND s.Schuljahresabschnitts_ID = la.Schuljahresabschnitts_ID
				        JOIN K_Lehrer kl ON ld.Fachlehrer_ID = kl.ID
				        LEFT JOIN K_Foerderschwerpunkt fs1 ON la.Foerderschwerpunkt_ID = fs1.ID
				        LEFT JOIN K_Foerderschwerpunkt fs2 ON la.Foerderschwerpunkt2_ID = fs2.ID
				        LEFT JOIN Klassen k ON la.Klassen_ID = k.ID
				        LEFT JOIN SchuelerLD_PSFachBem bem ON la.ID = bem.Abschnitt_ID
				        LEFT JOIN TimestampsSchuelerLernabschnittsdaten enmla ON la.ID = enmla.ID
				        LEFT JOIN TimestampsSchuelerLeistungsdaten enmld ON ld.ID = enmld.ID
				ORDER BY
				     la.Schueler_ID, la.ID, ld.ID
				;
				""".formatted(schuljahresabschnitt), DTOENMLehrerSchuelerAbschnittsdaten.class);
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + ((AbiturFach == null) ? 0 : AbiturFach.hashCode());
		result = (prime * result) + (int) (abschnittID ^ (abschnittID >>> 32));
		result = (prime * result) + (int) (jahrgangID ^ (jahrgangID >>> 32));
		result = (prime * result) + ((klasse == null) ? 0 : klasse.hashCode());
		result = (prime * result) + ((fachbezogeneBemerkungen == null) ? 0 : fachbezogeneBemerkungen.hashCode());
		result = (prime * result) + ((fehlstundenGesamt == null) ? 0 : fehlstundenGesamt.hashCode());
		result = (prime * result) + ((fehlstundenUnentschuldigt == null) ? 0 : fehlstundenUnentschuldigt.hashCode());
		result = (prime * result) + ((foerderschwerpunkt1Kuerzel == null) ? 0 : foerderschwerpunkt1Kuerzel.hashCode());
		result = (prime * result) + ((foerderschwerpunkt2Kuerzel == null) ? 0 : foerderschwerpunkt2Kuerzel.hashCode());
		result = (prime * result) + ((kursart == null) ? 0 : kursart.hashCode());
		result = (prime * result) + (int) (kursID ^ (kursID >>> 32));
		result = (prime * result) + (int) (leistungID ^ (leistungID >>> 32));
		result = (prime * result) + ((lernbereich1notenKuerzel == null) ? 0 : lernbereich1notenKuerzel.hashCode());
		result = (prime * result) + ((lernbereich2notenKuerzel == null) ? 0 : lernbereich2notenKuerzel.hashCode());
		result = (prime * result) + ((noteKuerzel == null) ? 0 : noteKuerzel.hashCode());
		result = (prime * result) + ((noteKuerzelQuartal == null) ? 0 : noteKuerzelQuartal.hashCode());
		result = (prime * result) + ((pruefungsordnung == null) ? 0 : pruefungsordnung.hashCode());
		result = (prime * result) + ((BilingualerZweig == null) ? 0 : BilingualerZweig.hashCode());
		return result;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final DTOENMLehrerSchuelerAbschnittsdaten other = (DTOENMLehrerSchuelerAbschnittsdaten) obj;
		if (AbiturFach == null) {
			if (other.AbiturFach != null)
				return false;
		} else if (!AbiturFach.equals(other.AbiturFach))
			return false;
		if (abschnittID != other.abschnittID)
			return false;
		if (jahrgangID != other.jahrgangID)
			return false;
		if (klasse == null) {
			if (other.klasse != null)
				return false;
		} else if (!klasse.equals(other.klasse))
			return false;
		if (fachbezogeneBemerkungen == null) {
			if (other.fachbezogeneBemerkungen != null)
				return false;
		} else if (!fachbezogeneBemerkungen.equals(other.fachbezogeneBemerkungen))
			return false;
		if (fehlstundenGesamt == null) {
			if (other.fehlstundenGesamt != null)
				return false;
		} else if (!fehlstundenGesamt.equals(other.fehlstundenGesamt))
			return false;
		if (fehlstundenUnentschuldigt == null) {
			if (other.fehlstundenUnentschuldigt != null)
				return false;
		} else if (!fehlstundenUnentschuldigt.equals(other.fehlstundenUnentschuldigt))
			return false;
		if (foerderschwerpunkt1Kuerzel == null) {
			if (other.foerderschwerpunkt1Kuerzel != null)
				return false;
		} else if (!foerderschwerpunkt1Kuerzel.equals(other.foerderschwerpunkt1Kuerzel))
			return false;
		if (foerderschwerpunkt2Kuerzel == null) {
			if (other.foerderschwerpunkt2Kuerzel != null)
				return false;
		} else if (!foerderschwerpunkt2Kuerzel.equals(other.foerderschwerpunkt2Kuerzel))
			return false;
		if (kursart == null) {
			if (other.kursart != null)
				return false;
		} else if (!kursart.equals(other.kursart))
			return false;
		if (kursID != other.kursID)
			return false;
		if (leistungID != other.leistungID)
			return false;
		if (lernbereich1notenKuerzel == null) {
			if (other.lernbereich1notenKuerzel != null)
				return false;
		} else if (!lernbereich1notenKuerzel.equals(other.lernbereich1notenKuerzel))
			return false;
		if (lernbereich2notenKuerzel == null) {
			if (other.lernbereich2notenKuerzel != null)
				return false;
		} else if (!lernbereich2notenKuerzel.equals(other.lernbereich2notenKuerzel))
			return false;
		if (noteKuerzel == null) {
			if (other.noteKuerzel != null)
				return false;
		} else if (!noteKuerzel.equals(other.noteKuerzel))
			return false;
		if (noteKuerzelQuartal == null) {
			if (other.noteKuerzelQuartal != null)
				return false;
		} else if (!noteKuerzelQuartal.equals(other.noteKuerzelQuartal))
			return false;
		if (pruefungsordnung == null) {
			if (other.pruefungsordnung != null)
				return false;
		} else if (!pruefungsordnung.equals(other.pruefungsordnung))
			return false;
		if (BilingualerZweig == null) {
			if (other.BilingualerZweig != null)
				return false;
		} else if (!BilingualerZweig.equals(other.BilingualerZweig))
			return false;
		return true;
	}


	/**
	 * Konvertiert die Nummer des Abiturfaches als String in den Integer-Wert.
	 *
	 * @return der Integer-Wert für die Nummer des Abiturfaches
	 */
	public Integer getAbiturFach() {
		try {
			return Integer.parseInt(this.AbiturFach);
		} catch (@SuppressWarnings("unused") final NumberFormatException e) {
			return null;
		}
	}



	@Override
	public String toString() {
		return "DTOLehrerSchuelerAbschnittsdaten [leistungID=" + leistungID + ", kursID=" + kursID + ", notenKrz=" + noteKuerzel
				+ ", notenKrzQuartal=" + noteKuerzelQuartal
				+ ", kursart=" + kursart + ", AbiturFach=" + AbiturFach + ", fehlstundenGesamt=" + fehlstundenGesamt
				+ ", fehlstundenUnentschuldigt=" + fehlstundenUnentschuldigt + ", fachbezogeneBemerkungen="
				+ fachbezogeneBemerkungen + ", abschnittID=" + abschnittID + ", klasse=" + klasse + ", pruefungsordnung=" + pruefungsordnung
				+ ", lernbereich1notenKuerzel=" + lernbereich1notenKuerzel + ", lernbereich2notenKuerzel=" + lernbereich2notenKuerzel
				+ ", foerderschwerpunkt1Kuerzel=" + foerderschwerpunkt1Kuerzel + ", foerderschwerpunkt2Kuerzel="
				+ foerderschwerpunkt2Kuerzel + "]";
	}


}
