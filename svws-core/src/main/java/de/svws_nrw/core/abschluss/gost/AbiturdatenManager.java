package de.svws_nrw.core.abschluss.gost;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.svws_nrw.asd.data.NoteKatalogEintrag;
import de.svws_nrw.asd.data.schueler.Sprachendaten;
import de.svws_nrw.asd.types.Note;
import de.svws_nrw.asd.types.fach.Fach;
import de.svws_nrw.core.abschluss.gost.belegpruefung.AbiFaecher;
import de.svws_nrw.core.abschluss.gost.belegpruefung.Allgemeines;
import de.svws_nrw.core.abschluss.gost.belegpruefung.Deutsch;
import de.svws_nrw.core.abschluss.gost.belegpruefung.FachWaehlbar;
import de.svws_nrw.core.abschluss.gost.belegpruefung.Fachkombinationen;
import de.svws_nrw.core.abschluss.gost.belegpruefung.Fremdsprachen;
import de.svws_nrw.core.abschluss.gost.belegpruefung.GesellschaftswissenschaftenUndReligion;
import de.svws_nrw.core.abschluss.gost.belegpruefung.KurszahlenUndWochenstunden;
import de.svws_nrw.core.abschluss.gost.belegpruefung.Latinum;
import de.svws_nrw.core.abschluss.gost.belegpruefung.LiterarischKuenstlerisch;
import de.svws_nrw.core.abschluss.gost.belegpruefung.Mathematik;
import de.svws_nrw.core.abschluss.gost.belegpruefung.Naturwissenschaften;
import de.svws_nrw.core.abschluss.gost.belegpruefung.Projektkurse;
import de.svws_nrw.core.abschluss.gost.belegpruefung.Schwerpunkt;
import de.svws_nrw.core.abschluss.gost.belegpruefung.Sport;
import de.svws_nrw.core.abschluss.gost.belegpruefung.abi2030.Abi30BelegpruefungAbiFaecher;
import de.svws_nrw.core.abschluss.gost.belegpruefung.abi2030.Abi30BelegpruefungAllgemeines;
import de.svws_nrw.core.abschluss.gost.belegpruefung.abi2030.Abi30BelegpruefungDeutsch;
import de.svws_nrw.core.abschluss.gost.belegpruefung.abi2030.Abi30BelegpruefungFachWaehlbar;
import de.svws_nrw.core.abschluss.gost.belegpruefung.abi2030.Abi30BelegpruefungFachkombinationen;
import de.svws_nrw.core.abschluss.gost.belegpruefung.abi2030.Abi30BelegpruefungFremdsprachen;
import de.svws_nrw.core.abschluss.gost.belegpruefung.abi2030.Abi30BelegpruefungGesellschaftswissenschaftenUndReligion;
import de.svws_nrw.core.abschluss.gost.belegpruefung.abi2030.Abi30BelegpruefungKurszahlenUndWochenstunden;
import de.svws_nrw.core.abschluss.gost.belegpruefung.abi2030.Abi30BelegpruefungLatinum;
import de.svws_nrw.core.abschluss.gost.belegpruefung.abi2030.Abi30BelegpruefungLiterarischKuenstlerisch;
import de.svws_nrw.core.abschluss.gost.belegpruefung.abi2030.Abi30BelegpruefungMathematik;
import de.svws_nrw.core.abschluss.gost.belegpruefung.abi2030.Abi30BelegpruefungNaturwissenschaften;
import de.svws_nrw.core.abschluss.gost.belegpruefung.abi2030.Abi30BelegpruefungProjektkurse;
import de.svws_nrw.core.abschluss.gost.belegpruefung.abi2030.Abi30BelegpruefungSchwerpunkt;
import de.svws_nrw.core.abschluss.gost.belegpruefung.abi2030.Abi30BelegpruefungSport;
import de.svws_nrw.core.adt.map.ArrayMap;
import de.svws_nrw.core.adt.map.HashMap2D;
import de.svws_nrw.core.data.gost.AbiturFachbelegung;
import de.svws_nrw.core.data.gost.AbiturFachbelegungHalbjahr;
import de.svws_nrw.core.data.gost.AbiturKursMarkierung;
import de.svws_nrw.core.data.gost.Abiturdaten;
import de.svws_nrw.core.data.gost.GostFach;
import de.svws_nrw.core.data.gost.GostJahrgangFachkombination;
import de.svws_nrw.core.data.gost.GostJahrgangsdaten;
import de.svws_nrw.core.data.gost.GostSchuelerFachwahl;
import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import de.svws_nrw.core.types.ServerMode;
import de.svws_nrw.core.types.gost.AbiturBelegungsart;
import de.svws_nrw.core.types.gost.GostAbiturFach;
import de.svws_nrw.core.types.gost.GostBesondereLernleistung;
import de.svws_nrw.core.types.gost.GostFachbereich;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.core.types.gost.GostKursart;
import de.svws_nrw.core.types.gost.GostSchriftlichkeit;
import de.svws_nrw.core.utils.gost.GostFachUtils;
import de.svws_nrw.core.utils.gost.GostFaecherManager;
import de.svws_nrw.core.utils.schueler.SprachendatenUtils;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse stellt Methoden zur Verfügung um die angegebenen Abiturdaten zu
 * bearbeiten und Auswertungen durchzuführen.
 *
 * Die Implementierung enthält Teile von experimentellem Code. Für diesen gilt folgendes:
 *
 * Bei dieser Implementierung handelt es sich um eine Umsetzung in Bezug auf möglichen zukünftigen
 * Änderungen in der APO-GOSt. Diese basiert auf der aktuellen Implementierung und integriert Aspekte
 * aus dem Eckpunktepapier und auf in den Schulleiterdienstbesprechungen erläuterten Vorhaben.
 * Sie dient der Evaluierung von möglichen Umsetzungsvarianten und als Vorbereitung einer späteren
 * Implementierung der Belegprüfung. Insbesondere sollen erste Versuche mit Laufbahnen mit einem
 * 5. Abiturfach und Projektkursen erprobt werden. Detailaspekte können erst nach Erscheinen der APO-GOSt
 * umgesetzt werden.
 * Es handelt sich also um experimentellen Code, der keine Rückschlüsse auf Details einer zukünftigen APO-GOSt
 * erlaubt.
 */
public class AbiturdatenManager {

	/** der Modus, in welchem der Server betrieben wird, um einen experimentellen Modus zu unterstützen. */
	private final @NotNull ServerMode servermode;

	/** Das Abiturdaten-Objekt, welches mithilfe dieses Managers bearbeitet wird */
	private final @NotNull Abiturdaten abidaten;

	/** Die Informationen zu den Jahrgangsdaten, sofern welche vorhanden sind - ansonsten null */
	private final GostJahrgangsdaten _jahrgangsdaten;

	/** Der Manager für die Fächer und Fachkombinationen der gymnasialen Oberstufe für diesen Abiturjahrgang */
	private final @NotNull GostFaecherManager faecherManager;

	/** Die Art der durchzuführenden Belegprüfung */
	private final @NotNull GostBelegpruefungsArt pruefungsArt;

	/** Eine HashMap, welche den schnellen Zugriff auf eine Fachbelegung anhand der Fach-ID ermöglicht */
	private final @NotNull Map<Long, AbiturFachbelegung> mapFachbelegungByFachID = new HashMap<>();

	/** Eine HashMap, welche den schnellen Zugriff auf die Fachbelegungen für ein Fach-Kürzel ermöglicht (können mehrere sein - Beispiel billingual mit Wechsel */
	private final @NotNull Map<String, List<AbiturFachbelegung>> mapFachbelegungenByFachKuerzel = new HashMap<>();

	/** Eine HashMap für den schnellen Zugriff auf die Fachbelegungen, welche in dem Halbjahr eine gültige Belegung haben */
	private final @NotNull Map<GostHalbjahr, ArrayList<AbiturFachbelegung>> mapFachbelegungenByGostHalbjahr = new ArrayMap<>(GostHalbjahr.values());

	/** Eine HashMap2D für den schnellen Zugriff auf die Halbjahresbelegungen anhand der Fach-UD und der ID des GostHalbjahres */
	private final @NotNull HashMap2D<Long, Integer, AbiturFachbelegungHalbjahr> mapFachbelegungHalbjahrByFachIDAndHalbjahrID = new HashMap2D<>();

	/** Eine HashMap, welche den schnellen Zugriff auf die Fachbelegungen über den Fachbereich ermöglicht */
	private final @NotNull Map<GostFachbereich, ArrayList<AbiturFachbelegung>> mapFachbereiche =
			new ArrayMap<>(GostFachbereich.values());

	/** Eine HashMap, welche den schnellen Zugriff auf die Prüfungsordnungs-relevanten Fachbelegungen über den Fachbereich ermöglicht */
	private final @NotNull Map<GostFachbereich, ArrayList<AbiturFachbelegung>> mapFachbereicheRelevant =
			new ArrayMap<>(GostFachbereich.values());


	/** Die Prüfungsergebnisse der einzelnen Teilprüfungen der Belegprüfung */
	private @NotNull List<GostBelegpruefung> belegpruefungen = new ArrayList<>();

	/** Die zuletzt durchgeführte Belegprüfung bezüglich der Kurszahlen und der Wochenstunden */
	private KurszahlenUndWochenstunden belegpruefungKurszahlenUndWochenstunden = null;

	/** Die zuletzt durchgeführte Belegprüfung bezüglich der Kurszahlen und der Wochenstunden - experimenteller Code */
	private Abi30BelegpruefungKurszahlenUndWochenstunden abi30BelegpruefungKurszahlenUndWochenstunden = null;

	/** Die Menge der Belegprüfungsfehler, die bei den durchgeführten Belegprüfungen aufgetreten sind. */
	private @NotNull List<GostBelegungsfehler> belegpruefungsfehler = new ArrayList<>();

	/** Gibt an, ob die Belegprüfung insgesamt erfolgreich war oder nicht. */
	private boolean belegpruefungErfolgreich = false;

	/** Das Ergebnis des Markierungsalgorithmus */
	private @NotNull GostAbiturMarkierungsalgorithmusErgebnis markierungsErgebnis = new GostAbiturMarkierungsalgorithmusErgebnis();

	/** Das Ergebnis des Markierprüfungsalgorithmus */
	private @NotNull GostAbiturMarkierungspruefungErgebnis markierpruefungsergebnis = new GostAbiturMarkierungspruefungErgebnis();


	/**
	 * Erstellt ein neues Manager-Objekt, welches mit den übergebenen Abiturdaten verknüpft wird.
	 *
	 * @param servermode     der Modus, in welchem der Server betrieben wird, um einen experimentellen Modus zu unterstützen
	 * @param abidaten       die Abiturdaten
	 * @param gostJahrgang   die Informationen zu dem Abiturjahrgang
	 * @param faecherManager der Manager für die Fächer und Fachkombinationen der Gymnasialen Oberstufe
	 * @param pruefungsArt   die Art der Belegpruefung (z.B. EF1 oder GESAMT)
	 */
	public AbiturdatenManager(final @NotNull ServerMode servermode, final @NotNull Abiturdaten abidaten, final GostJahrgangsdaten gostJahrgang,
			final @NotNull GostFaecherManager faecherManager, final @NotNull GostBelegpruefungsArt pruefungsArt) {
		this.servermode = servermode;
		this.abidaten = abidaten;
		this._jahrgangsdaten = gostJahrgang;
		this.faecherManager = faecherManager;
		this.pruefungsArt = pruefungsArt;
		init();
	}


	/**
	 * Initialisiert die Belegprüfung der Art pruefungs_art für einen Schüler durch, dessen Abiturdaten mit dem angegebenen
	 * Manager verwaltet werden.
	 *
	 * @param pruefungsArt    die Art der Prüfung, die durchgeführt wird
	 *
	 * @return eine Liste mit den durchgefuehrten Belegpruefungen
	 */
	private @NotNull List<GostBelegpruefung> getPruefungenDefault(final @NotNull GostBelegpruefungsArt pruefungsArt) {
		final @NotNull ArrayList<GostBelegpruefung> pruefungen = new ArrayList<>();
		pruefungen.add(new Deutsch(this, pruefungsArt));
		final @NotNull Fremdsprachen pruefungFremdsprachen = new Fremdsprachen(this, pruefungsArt);
		pruefungen.add(pruefungFremdsprachen);
		pruefungen.add(new Latinum(this, pruefungsArt));
		pruefungen.add(new LiterarischKuenstlerisch(this, pruefungsArt));
		pruefungen.add(new GesellschaftswissenschaftenUndReligion(this, pruefungsArt));
		pruefungen.add(new Mathematik(this, pruefungsArt));
		final @NotNull Naturwissenschaften pruefungNaturwissenschaften = new Naturwissenschaften(this, pruefungsArt);
		pruefungen.add(pruefungNaturwissenschaften);
		pruefungen.add(new Sport(this, pruefungsArt));
		final @NotNull Projektkurse pruefungProjektkurse = new Projektkurse(this, pruefungsArt);
		pruefungen.add(pruefungProjektkurse);
		// Die Prüfung zu dem Schwerpunkt muss nach den Prüfungen des naturwissenschaftlichen und der Fremdsprachen durchgeführt werden, da hier eine Abhängigkeit besteht.
		pruefungen.add(new Schwerpunkt(this, pruefungsArt, pruefungFremdsprachen, pruefungNaturwissenschaften));
		pruefungen.add(new AbiFaecher(this, pruefungsArt));
		// Die Prüfung der Kurszahlen und Wochenstunden ist abhängig von den Projektkursergebnissen - sie muss nach den Projektkursergebnissen durchgeführt werden!!!
		belegpruefungKurszahlenUndWochenstunden = new KurszahlenUndWochenstunden(this, pruefungsArt, pruefungProjektkurse);
		pruefungen.add(belegpruefungKurszahlenUndWochenstunden);
		pruefungen.add(new Allgemeines(this, pruefungsArt));
		// Die Prüfung von schulspezifischen Fachkombinationen
		pruefungen.add(new Fachkombinationen(this, pruefungsArt));
		// Die Prüfung der schulspezifischen Wählbarkeit von Fächern
		pruefungen.add(new FachWaehlbar(this, pruefungsArt));
		return pruefungen;
	}

	/**
	 * Einbinden von experimentellem Code:
	 * Initialisiert die Belegprüfung der Art pruefungs_art für einen Schüler durch, dessen Abiturdaten mit dem angegebenen
	 * Manager verwaltet werden.
	 *
	 * @param pruefungsArt    die Art der Prüfung, die durchgeführt wird
	 *
	 * @return eine Liste mit den durchgefuehrten Belegpruefungen
	 */
	private @NotNull List<GostBelegpruefung> getPruefungenAbi2030(final @NotNull GostBelegpruefungsArt pruefungsArt) {
		final @NotNull ArrayList<GostBelegpruefung> pruefungen = new ArrayList<>();
		pruefungen.add(new Abi30BelegpruefungDeutsch(this, pruefungsArt));
		final @NotNull Abi30BelegpruefungFremdsprachen pruefungFremdsprachen = new Abi30BelegpruefungFremdsprachen(this, pruefungsArt);
		pruefungen.add(pruefungFremdsprachen);
		pruefungen.add(new Abi30BelegpruefungLatinum(this, pruefungsArt));
		pruefungen.add(new Abi30BelegpruefungLiterarischKuenstlerisch(this, pruefungsArt));
		pruefungen.add(new Abi30BelegpruefungGesellschaftswissenschaftenUndReligion(this, pruefungsArt));
		pruefungen.add(new Abi30BelegpruefungMathematik(this, pruefungsArt));
		final @NotNull Abi30BelegpruefungNaturwissenschaften pruefungNaturwissenschaften = new Abi30BelegpruefungNaturwissenschaften(this, pruefungsArt);
		pruefungen.add(pruefungNaturwissenschaften);
		pruefungen.add(new Abi30BelegpruefungSport(this, pruefungsArt));
		final @NotNull Abi30BelegpruefungProjektkurse pruefungProjektkurse = new Abi30BelegpruefungProjektkurse(this, pruefungsArt);
		pruefungen.add(pruefungProjektkurse);
		// Die Prüfung zu dem Schwerpunkt muss nach den Prüfungen des naturwissenschaftlichen und der Fremdsprachen durchgeführt werden, da hier eine Abhängigkeit besteht.
		pruefungen.add(new Abi30BelegpruefungSchwerpunkt(this, pruefungsArt, pruefungFremdsprachen, pruefungNaturwissenschaften));
		pruefungen.add(new Abi30BelegpruefungAbiFaecher(this, pruefungsArt, pruefungProjektkurse));
		// Die Prüfung der Kurszahlen und Wochenstunden ist abhängig von den Projektkursergebnissen - sie muss nach den Projektkursergebnissen durchgeführt werden!!!
		abi30BelegpruefungKurszahlenUndWochenstunden = new Abi30BelegpruefungKurszahlenUndWochenstunden(this, pruefungsArt, pruefungProjektkurse);
		pruefungen.add(abi30BelegpruefungKurszahlenUndWochenstunden);
		pruefungen.add(new Abi30BelegpruefungAllgemeines(this, pruefungsArt));
		// Die Prüfung von schulspezifischen Fachkombinationen
		pruefungen.add(new Abi30BelegpruefungFachkombinationen(this, pruefungsArt));
		// Die Prüfung der schulspezifischen Wählbarkeit von Fächern
		pruefungen.add(new Abi30BelegpruefungFachWaehlbar(this, pruefungsArt));
		return pruefungen;
	}

	/**
	 * Führt die Belegprüfung der Art pruefungs_art für einen Schüler durch, dessen Abiturdaten mit dem angegebenen
	 * Manager verwaltet werden.
	 *
	 * @param pruefungsArt    die Art der Prüfung, die durchgeführt wird
	 *
	 * @return eine Liste mit den durchgefuehrten Belegpruefungen
	 */
	public @NotNull List<GostBelegpruefung> getPruefungen(final @NotNull GostBelegpruefungsArt pruefungsArt) {
		if (nutzeExperimentellenCode(this.servermode, abidaten.abiturjahr)) // Führe ggf. den experimentellen Code aus
			return this.getPruefungenAbi2030(pruefungsArt);
		return this.getPruefungenDefault(pruefungsArt);
	}


	/**
	 * Initialisiert bzw. reinitialisert die Datenstrukturen, die für den schnellen Zugriff auf die Daten
	 * eingerichtet werden.
	 */
	public void init() {
		if (abidaten == null)
			return;
		initMaps();
		// Führe die Belegprüfung durch
		belegpruefungen = getPruefungen(pruefungsArt);
		for (final @NotNull GostBelegpruefung belegpruefung : belegpruefungen)
			belegpruefung.pruefe();
		belegpruefungsfehler = GostBelegpruefung.getBelegungsfehlerAlle(belegpruefungen);
		belegpruefungErfolgreich = GostBelegpruefung.istErfolgreich(belegpruefungsfehler);
		// Führe ggf. den Markierungsalgorithmus durch...
		if (istBewertetQualifikationsPhase()) {
			if (nutzeExperimentellenCode(this.servermode, abidaten.abiturjahr)) {
				// Führe ggf. den experimentellen Code aus
				markierungsErgebnis = Abi30GostAbiturMarkierungsalgorithmus.berechne(this, belegpruefungen);
			} else {
				markierungsErgebnis = GostAbiturMarkierungsalgorithmus.berechne(this, belegpruefungen);
			}
		}
		// ... und ggf. auch eine Prüfung der aktuellen Markierungen
		pruefeMarkierungen();
	}


	/**
	 * Prüft ob, bei dem übergeben Servermode und dem übergebenen Abiturjahr der experimentelle Code
	 * für das Abitur ab 2030 eingesetzt werden soll.
	 *
	 * @param servermode   der Server-Mode
	 * @param abiturjahr   das Abiturjahr
	 *
	 * @return true, wenn der experimentelle Code genutzt werden soll, und ansonsten false
	 */
	public static boolean nutzeExperimentellenCode(final ServerMode servermode, final int abiturjahr) {
		return ((abiturjahr >= 2030) && (servermode == ServerMode.DEV));
	}


	/**
	 * Initialisiert bzw. reinitialisiert die Map für den schnellen Zugriff auf Fachbelegungen
	 * anhand des Fachbereichs, der Fach-ID oder des Halbjahres.
	 */
	private void initMaps() {
		// Leere die HashMaps und erstelle ggf. neue Listen für die Zuordnung von Abitur-Fachbelegungen
		mapFachbelegungByFachID.clear();
		mapFachbelegungenByFachKuerzel.clear();
		mapFachbelegungHalbjahrByFachIDAndHalbjahrID.clear();
		mapFachbereiche.clear();
		mapFachbereicheRelevant.clear();
		for (final @NotNull GostFachbereich fachbereich : GostFachbereich.values()) {
			mapFachbereiche.put(fachbereich, new ArrayList<>());
			mapFachbereicheRelevant.put(fachbereich, new ArrayList<>());
		}
		mapFachbelegungenByGostHalbjahr.clear();
		for (final @NotNull GostHalbjahr halbjahr : GostHalbjahr.values())
			mapFachbelegungenByGostHalbjahr.put(halbjahr, new ArrayList<>());

		// Durchwandere alle belegten Fächer und weise diese den Fachbereichen zu
		final @NotNull List<AbiturFachbelegung> fachbelegungen = abidaten.fachbelegungen;
		for (final AbiturFachbelegung fachbelegung : fachbelegungen) {
			final GostFach fach = getFach(fachbelegung);
			if (fach == null)
				continue;
			mapFachbelegungByFachID.put(fachbelegung.fachID, fachbelegung);
			final List<AbiturFachbelegung> tmpListFachbelegungenByFachKuerzel =
					mapFachbelegungenByFachKuerzel.computeIfAbsent(fach.kuerzel, k -> new ArrayList<>());
			if (tmpListFachbelegungenByFachKuerzel != null)
				tmpListFachbelegungenByFachKuerzel.add(fachbelegung);
			if (zaehleBelegung(fachbelegung) > 0) { // Filtere ggf. leere Belegungen
				final @NotNull List<GostFachbereich> fachbereiche = GostFachbereich.getBereiche(fach);
				for (final @NotNull GostFachbereich fachbereich : fachbereiche) {
					List<AbiturFachbelegung> listFachbelegungen = mapFachbereiche.get(fachbereich);
					if (listFachbelegungen != null)
						listFachbelegungen.add(fachbelegung);
					if (fach.istPruefungsordnungsRelevant) {
						listFachbelegungen = mapFachbereicheRelevant.get(fachbereich);
						if (listFachbelegungen != null)
							listFachbelegungen.add(fachbelegung);
					}
				}
			}
			for (final @NotNull GostHalbjahr halbjahr : GostHalbjahr.values()) {
				final AbiturFachbelegungHalbjahr belegungHalbjahr = fachbelegung.belegungen[halbjahr.id];
				if ((belegungHalbjahr == null) || (belegungHalbjahr.kursartKuerzel == null))
					continue;
				mapFachbelegungHalbjahrByFachIDAndHalbjahrID.put(fachbelegung.fachID, halbjahr.id, belegungHalbjahr);
				if (halbjahr.istQualifikationsphase() && (Note.fromKuerzel(belegungHalbjahr.notenkuerzel) == Note.UNGENUEGEND))
					continue;
				final List<AbiturFachbelegung> tmp = mapFachbelegungenByGostHalbjahr.get(halbjahr);
				if (tmp == null) // Sollte nicht auftreten...
					continue;
				tmp.add(fachbelegung);
			}
		}
	}


	/**
	 * Fasst die bilingualen und nicht biligualen Fachbelegungen unter dem Fach der
	 * letzten Halbjahresbelegung zusammen und initialisiert den Manager danach neu.
	 *
	 * @return true, wenn das Kombinieren erfolgreich durchgeführt wurde, und ansonsten false
	 */
	public boolean kombiniereFachbelegungenEinesFaches() {
		for (final @NotNull List<AbiturFachbelegung> belegungen : mapFachbelegungenByFachKuerzel.values()) {
			// Prüfe, ob mehrere Fachbelegungen existieren
			if (belegungen.size() < 2)
				continue;
			// Bestimme die Fachbelegung, welche die letzte Belegung hat und die anderen Halbjahresbelegungen "aufnehmen" soll
			AbiturFachbelegung ziel = null;
			@NotNull GostHalbjahr letztes = GostHalbjahr.EF2;
			for (final @NotNull AbiturFachbelegung belegung : belegungen) {
				GostHalbjahr halbjahr = letztes.next();
				while (halbjahr != null) {
					final AbiturFachbelegungHalbjahr belegungHalbjahr = belegung.belegungen[halbjahr.id];
					if ((belegungHalbjahr != null) && (belegungHalbjahr.kursartKuerzel != null) && (belegungHalbjahr.notenkuerzel != null)
							&& (!"".equals(belegungHalbjahr.notenkuerzel))) {
						ziel = belegung;
						letztes = halbjahr;
					}
					halbjahr = halbjahr.next();
				}
			}
			// Prüfe, ob es sich um leere Belegungen gehandelt hat (sollte nicht vorkommen)
			if (ziel == null)
				continue;
			// Kopiere die Halbjahresbelegungen, sofern diese keine gültige Halbjahresbelegung der Zielbelegung überschreiben würden
			for (final @NotNull AbiturFachbelegung belegung : belegungen) {
				if (belegung == ziel)
					continue;
				for (final @NotNull GostHalbjahr halbjahr : GostHalbjahr.values()) {
					// Prüfe, ob die Zielbelegung eine gültige Halbjahresbelegung hat
					final AbiturFachbelegungHalbjahr zielHalbjahr = ziel.belegungen[halbjahr.id];
					if ((zielHalbjahr != null) && (zielHalbjahr.kursartKuerzel != null) && (zielHalbjahr.notenkuerzel != null)
							&& (!"".equals(zielHalbjahr.notenkuerzel)))
						continue;
					// Wenn nicht, dann prüfe, ob die Halbjahrsbelegung gültig ist und setze diese ggf.
					final AbiturFachbelegungHalbjahr belegungHalbjahr = belegung.belegungen[halbjahr.id];
					if ((belegungHalbjahr != null) && (belegungHalbjahr.kursartKuerzel != null) && (belegungHalbjahr.notenkuerzel != null)) {
						ziel.belegungen[halbjahr.id] = belegungHalbjahr;
					}
				}
				abidaten.fachbelegungen.remove(belegung);
			}
		}
		init();
		return true;
	}

	/**
	 * Gibt die von diesem Manager verwalteten Abiturdaten zurück.
	 *
	 * @return die Abiturdaten des Managers
	 */
	public @NotNull Abiturdaten daten() {
		return this.abidaten;
	}


	/**
	 * Gibt den zugehörigen Fächer-Manager zurück.
	 *
	 * @return der Fächer-Manager
	 */
	public @NotNull GostFaecherManager faecher() {
		return this.faecherManager;
	}


	/**
	 * Gibt die Jahrgangsdaten für den Abiturjahrgang zurück.
	 *
	 * @return die Jahrgangsdaten
	 *
	 * @throws DeveloperNotificationException falls keine Jahrgangsdaten vorliegen
	 */
	public @NotNull GostJahrgangsdaten jahrgangsdaten() {
		if (this._jahrgangsdaten == null)
			throw new DeveloperNotificationException("Es liegen keine Jahrgangsdaten vor.");
		return this._jahrgangsdaten;
	}


	/**
	 * gibt die gewählte Prüfungsart zurück
	 *
	 * @return entweder Gesamtprüfung oder EF1-Prüfung
	 */
	public @NotNull GostBelegpruefungsArt getPruefungsArt() {
		return this.pruefungsArt;
	}

	/**
	 * Gibt zurück, ob das angegebene Halbjahr bereits bewertet ist oder nicht.
	 *
	 * @param halbjahr   das Halbjahr
	 *
	 * @return true, falls es bereits bewertet ist
	 */
	public boolean istBewertet(final @NotNull GostHalbjahr halbjahr) {
		return abidaten.bewertetesHalbjahr[halbjahr.id];
	}


	/**
	 * Gibt zurück, ob alle Halbjahr der Qualifikationsphase bewertet sind oder nicht.
	 *
	 * @return true, falls alle Halbjahre bewertet sind, und ansonsten false
	 */
	public boolean istBewertetQualifikationsPhase() {
		for (final @NotNull GostHalbjahr hj : GostHalbjahr.getQualifikationsphase())
			if (!istBewertet(hj))
				return false;
		return true;
	}


	/**
	 * Liefert die in den Abiturdaten enthaltenen Sprachendaten.
	 *
	 * @return Die Sprachendaten (siehe {@link Sprachendaten})
	 */
	public @NotNull Sprachendaten getSprachendaten() {
		return abidaten.sprachendaten;
	}


	/**
	 * Liefert das Abiturjahr
	 *
	 * @return das Abiturjahr
	 */
	public int getAbiturjahr() {
		return abidaten.abiturjahr;
	}


	/**
	 * Liefert das Schuljahr, in dem das Abitur gemacht wird
	 *
	 * @return das Schuljahr
	 */
	public int getSchuljahr() {
		return abidaten.schuljahrAbitur;
	}


	/**
	 * Gibt zurück, ob es sich bei der Halbjahresbelegung um eine Belegung handelt, welche mit
	 * null Punkten abgeschlossen wurde und welche daher als nicht belegter Kurs zu werten ist.
	 *
	 * @param halbjahresbelegung   die Halbjahresbelegung eines Kurses
	 *
	 * @return true, fall es sich um einen Null-Punkte-Kurs in der Qualifikationsphase handelt.
	 */
	public static boolean istNullPunkteBelegungInQPhase(final @NotNull AbiturFachbelegungHalbjahr halbjahresbelegung) {
		final GostHalbjahr hj = GostHalbjahr.fromKuerzel(halbjahresbelegung.halbjahrKuerzel);
		if ((hj == null) || (hj.istEinfuehrungsphase()))
			return false;
		return Note.fromKuerzel(halbjahresbelegung.notenkuerzel) == Note.UNGENUEGEND;
	}


	private static String getSchuelerFachwahlFromBelegung(final @NotNull AbiturFachbelegung belegung, final @NotNull GostHalbjahr halbjahr) {
		AbiturFachbelegungHalbjahr halbjahresbelegung = belegung.belegungen[halbjahr.id];
		if (halbjahresbelegung == null) {
			halbjahresbelegung = new AbiturFachbelegungHalbjahr();
			halbjahresbelegung.halbjahrKuerzel = halbjahr.kuerzel;
			belegung.belegungen[halbjahr.id] = halbjahresbelegung;
		}
		final GostKursart kursart = GostKursart.fromKuerzel(halbjahresbelegung.kursartKuerzel);
		if (kursart == null)
			return ("".equals(halbjahresbelegung.kursartKuerzel) ? null : halbjahresbelegung.kursartKuerzel);
		if (istNullPunkteBelegungInQPhase(halbjahresbelegung))
			return null;
		if ((kursart == GostKursart.ZK) || (kursart == GostKursart.LK))
			return kursart.kuerzel;
		return halbjahresbelegung.schriftlich ? "S" : "M";
	}


	/**
	 * Bestimmt die Schüler-Fachwahl für das Fach mit der übergebenen ID
	 *
	 * @param fachID   die ID des Faches
	 *
	 * @return die Schüler-Fachwahl
	 */
	public @NotNull GostSchuelerFachwahl getSchuelerFachwahl(final long fachID) {
		final AbiturFachbelegung belegung = getFachbelegungByID(fachID);
		if (belegung == null)
			return new GostSchuelerFachwahl();
		final @NotNull GostSchuelerFachwahl wahl = new GostSchuelerFachwahl();
		wahl.halbjahre[0] = getSchuelerFachwahlFromBelegung(belegung, GostHalbjahr.EF1);
		wahl.halbjahre[1] = getSchuelerFachwahlFromBelegung(belegung, GostHalbjahr.EF2);
		wahl.halbjahre[2] = getSchuelerFachwahlFromBelegung(belegung, GostHalbjahr.Q11);
		wahl.halbjahre[3] = getSchuelerFachwahlFromBelegung(belegung, GostHalbjahr.Q12);
		wahl.halbjahre[4] = getSchuelerFachwahlFromBelegung(belegung, GostHalbjahr.Q21);
		wahl.halbjahre[5] = getSchuelerFachwahlFromBelegung(belegung, GostHalbjahr.Q22);
		wahl.abiturFach = belegung.abiturFach;
		return wahl;
	}


	/**
	 * Bestimmt die Schüler-Fachwahlen aller belegten Fächer.
	 *
	 * @return die Map mit den Schüler-Fachwahlen
	 */
	public @NotNull Map<Long, GostSchuelerFachwahl> getSchuelerFachwahlen() {
		final @NotNull HashMap<Long, GostSchuelerFachwahl> fachwahlen = new HashMap<>();
		final @NotNull List<AbiturFachbelegung> fachbelegungen = abidaten.fachbelegungen;
		for (final AbiturFachbelegung fb : fachbelegungen)
			fachwahlen.put(fb.fachID, getSchuelerFachwahl(fb.fachID));
		return fachwahlen;
	}


	/**
	 * Liefert das Fach der gymnasialen Oberstufe für die angegeben Abiturfachbelegung.
	 *
	 * @param belegung   die Fachbelegung (siehe {@link AbiturFachbelegung})
	 *
	 * @return das Fach der gymnasialen Oberstufe (siehe {@link GostFach})
	 */
	public GostFach getFach(final AbiturFachbelegung belegung) {
		if (belegung == null)
			return null;
		return faecherManager.get(belegung.fachID);
	}


	/**
	 * Prüft, ob die Fachbelegung in einem der übergebenen Fachbereiche liegt.
	 *
	 * @param belegung   die Belegung
	 * @param bereiche   die Fachbereiche
	 *
	 * @return true, wenn die Fachbelegung in einem der Bereiche liegt und ansonsten false
	 */
	public boolean hatFachbereich(final AbiturFachbelegung belegung, final @NotNull GostFachbereich... bereiche) {
		if ((belegung == null) || (bereiche.length == 0))
			return false;
		final GostFach fach = this.getFach(belegung);
		if (fach == null)
			return false;
		for (final @NotNull GostFachbereich bereich : bereiche)
			if (bereich.hat(fach))
				return true;
		return false;
	}


	/**
	 * Prüft, ob eine Belegung der Abiturfächer in einem der übergebenen Fachbereiche liegt.
	 *
	 * @param bereiche   die Fachbereiche
	 *
	 * @return true, wenn eine der Fachbelegungen in einem der Fachbereiche liegt und ansonsten false
	 */
	public boolean hatFachbereichInAbiturfaechern(final @NotNull GostFachbereich... bereiche) {
		for (final @NotNull AbiturFachbelegung belegung : abidaten.fachbelegungen) {
			final GostAbiturFach abiturFach = GostAbiturFach.fromID(belegung.abiturFach);
			if ((abiturFach != null) && this.hatFachbereich(belegung, bereiche))
				return true;
		}
		return false;
	}


	/**
	 * Prüft, ob eine Belegung der Leistungskurse in einem der übergebenen Fachbereiche liegt.
	 *
	 * @param bereiche   die Fachbereiche
	 *
	 * @return true, wenn eine Belegung der Leistungskurse in einem der Fachbereiche liegt und ansonsten false
	 */
	public boolean hatFachbereichInLKAbiturfaechern(final @NotNull GostFachbereich... bereiche) {
		for (final @NotNull AbiturFachbelegung belegung : abidaten.fachbelegungen) {
			final GostAbiturFach abiturFach = GostAbiturFach.fromID(belegung.abiturFach);
			if (abiturFach != null)
				continue;
			if ((abiturFach != GostAbiturFach.LK1) && (abiturFach != GostAbiturFach.LK2))
				continue;
			if (this.hatFachbereich(belegung, bereiche))
				return true;
		}
		return false;
	}


	/**
	 * Prüft, ob eine Belegung der Abitur-Grundkurse in einem der übergebenen Fachbereiche liegt.
	 *
	 * @param bereiche   die Fachbereiche
	 *
	 * @return true, wenn eine Belegung der Abitur-Grundkurse in einem der Fachbereiche liegt und ansonsten false
	 */
	public boolean hatFachbereichInGKAbiturfaechern(final @NotNull GostFachbereich... bereiche) {
		for (final @NotNull AbiturFachbelegung belegung : abidaten.fachbelegungen) {
			final GostAbiturFach abiturFach = GostAbiturFach.fromID(belegung.abiturFach);
			if (abiturFach != null)
				continue;
			if ((abiturFach == GostAbiturFach.LK1) || (abiturFach == GostAbiturFach.LK2))
				continue;
			if (this.hatFachbereich(belegung, bereiche))
				return true;
		}
		return false;
	}


	/**
	 * Prüft, ob in den Abiturdaten mindestens eine Fachbelegung existiert oder nicht.
	 * Wichtig: Bei dieser Methode wird auch eine 0-Punkte-Belegung als Fachbelegung gezählt!
	 *
	 * @return true, wenn mindestens eine Fachbelegung existiert, und ansonsten false
	 */
	public boolean existsFachbelegung() {
		final @NotNull List<AbiturFachbelegung> fachbelegungen = abidaten.fachbelegungen;
		for (final AbiturFachbelegung fb : fachbelegungen) {
			final GostFach fach = getFach(fb);
			if (fach == null)
				continue;
			for (int i = 0; i < GostHalbjahr.maxHalbjahre; i++)
				if (fb.belegungen[i] != null)
					return true;
		}
		return false;
	}


	/**
	 * Prüft, ob das Fach in allen angegebenen Halbjahren belegt wurde.
	 * Ist die Fachbelegung null, so schlägt die Prüfung fehl. Wird bei einer gültigen Fachbelegung kein Halbjahr
	 * angegeben, so ist die Prüfung erfolgreich, da kein Halbjahr geprüft werden muss.
	 *
	 * @param fachbelegung      die zu prüfende Fachbelegung
	 * @param halbjahre         die zu prüfenden Halbjahre
	 *
	 * @return true, falls das Fach in den Halbjahren belegt wurde, sonst false
	 */
	public boolean pruefeBelegung(final AbiturFachbelegung fachbelegung, final @NotNull GostHalbjahr... halbjahre) {
		if (fachbelegung == null)
			return false;
		if (halbjahre.length == 0)
			return true;
		for (final GostHalbjahr halbjahr : halbjahre) {
			final AbiturFachbelegungHalbjahr belegungHalbjahr = fachbelegung.belegungen[halbjahr.id];
			if ((belegungHalbjahr == null) || (belegungHalbjahr.kursartKuerzel == null))
				return false;
			if (istNullPunkteBelegungInQPhase(belegungHalbjahr))
				return false;
		}
		return true;
	}


	/**
	 * Bestimmt die Anzahl der Fachbelegungen, die dem Fach zugeordnet sind.
	 * Wird keine gültige Fachbelegung übergeben, so wird 0 zurückgegeben.
	 *
	 * @param fachbelegung   die Fachbelegung
	 *
	 * @return die Anzahl der Belegungen des Faches
	 */
	public int zaehleBelegung(final AbiturFachbelegung fachbelegung) {
		if (fachbelegung == null)
			return 0;
		int anzahl = 0;
		for (int i = 0; i < GostHalbjahr.maxHalbjahre; i++) {
			final AbiturFachbelegungHalbjahr belegungHalbjahr = fachbelegung.belegungen[i];
			if ((belegungHalbjahr != null) && (!istNullPunkteBelegungInQPhase(belegungHalbjahr)))
				anzahl++;
		}
		return anzahl;
	}


	/**
	 * Zählt die Anzahl der Halbjahresbelegungen für die angegebene Fachbelegung in den angegeben Halbjahren.
	 * Ist die Fachbelegung null, so wird 0 zurückgegeben. Wird bei einer gültigen Fachbelegung kein Halbjahr
	 * angegeben, so wird ebenfalls 0 zurückgegeben.
	 *
	 * @param fachbelegung   die Fachbelegung
	 * @param halbjahre      die Halbjahre
	 *
	 * @return die Anzahl der Belegungen in den Halbjahren
	 */
	public int zaehleHalbjahresbelegungen(final AbiturFachbelegung fachbelegung, final @NotNull GostHalbjahr... halbjahre) {
		if (fachbelegung == null)
			return 0;
		if (halbjahre.length == 0)
			return 0;
		int anzahl = 0;
		for (final @NotNull GostHalbjahr halbjahr : halbjahre) {
			final AbiturFachbelegungHalbjahr belegungHalbjahr = fachbelegung.belegungen[halbjahr.id];
			if ((belegungHalbjahr != null) && (!istNullPunkteBelegungInQPhase(belegungHalbjahr)))
				anzahl++;
		}
		return anzahl;
	}


	/**
	 * Zählt die Anzahl der Belegungen für die angegebenen Fachbelegungen in den angegeben Halbjahren.
	 * Ist die Fachbelegung null, so wird 0 zurückgegeben. Wird bei einer gültigen Fachbelegung kein Halbjahr
	 * angegeben, so wird ebenfalls 0 zurückgegeben.
	 *
	 * @param fachbelegungen      die Fachbelegungen
	 * @param halbjahre           die Halbjahre
	 *
	 * @return die Anzahl der Belegungen in den Halbjahren und den Fächern
	 */
	public int zaehleBelegungInHalbjahren(final List<AbiturFachbelegung> fachbelegungen, final @NotNull GostHalbjahr... halbjahre) {
		if (fachbelegungen == null)
			return 0;
		if (halbjahre.length == 0)
			return 0;
		int anzahl = 0;
		for (final @NotNull AbiturFachbelegung fachbelegung : fachbelegungen) {
			for (final @NotNull GostHalbjahr halbjahr : halbjahre) {
				final AbiturFachbelegungHalbjahr belegungHalbjahr = fachbelegung.belegungen[halbjahr.id];
				if ((belegungHalbjahr != null) && (!istNullPunkteBelegungInQPhase(belegungHalbjahr)))
					anzahl++;
			}
		}
		return anzahl;
	}


	/**
	 * Zählt die Anzahl der 0-Punkte-Belegungen aller Fachbelegungen in der Qualifikationsphase.
	 *
	 * @return die Anzahl der 0-Punkte-Belegungen aller Fachbelegungen in der Qualifikationsphase
	 */
	public int zaehleNullPunkteBelegungenInQPhase() {
		int anzahl = 0;
		for (final @NotNull AbiturFachbelegung fachbelegung : abidaten.fachbelegungen) {
			for (final @NotNull GostHalbjahr halbjahr : GostHalbjahr.getQualifikationsphase()) {
				final AbiturFachbelegungHalbjahr belegungHalbjahr = fachbelegung.belegungen[halbjahr.id];
				if ((belegungHalbjahr != null) && (istNullPunkteBelegungInQPhase(belegungHalbjahr)))
					anzahl++;
			}
		}
		return anzahl;
	}


	/**
	 * Prüft, ob die Belegung des Faches in den angegebenen Halbjahren der angegebenen Kursart entspricht.
	 * Ist die Fachbelegung null, so schlägt die Prüfung fehl. Wird bei einer gültigen Fachbelegung kein Halbjahr
	 * angegeben, so ist die Prüfung erfolgreich, da kein Halbjahr geprüft werden muss.
	 *
	 * @param fachbelegung      die zu prüfende Fachnbelegung
	 * @param kursart           die zu prüfende Kursart
	 * @param halbjahre         die zu prüfenden Halbjahre
	 *
	 * @return true, falls die Schriftlichkeit in den Halbjahren gegeben ist, sonst false
	 */
	public boolean pruefeBelegungMitKursart(final AbiturFachbelegung fachbelegung, final @NotNull GostKursart kursart,
			final @NotNull GostHalbjahr... halbjahre) {
		if (fachbelegung == null)
			return false;
		if (halbjahre.length == 0)
			return true;
		for (final GostHalbjahr halbjahr : halbjahre) {
			final AbiturFachbelegungHalbjahr belegungHalbjahr = fachbelegung.belegungen[halbjahr.id];
			if ((belegungHalbjahr == null) || (kursart != GostKursart.fromKuerzel(belegungHalbjahr.kursartKuerzel))
					|| (istNullPunkteBelegungInQPhase(belegungHalbjahr)))
				return false;
		}
		return true;
	}


	/**
	 * Prüft, ob eine Fachbelegung existiert, welche in den angegebenen Halbjahren der angegebenen Kursart
	 * entspricht.
	 * Ist keine Fachbelegung angegeben, so schlägt die Prüfung fehl. Wird bei einer gültigen
	 * Fachbelegung kein Halbjahr angegeben, so ist die Prüfung erfolgreich, da kein Halbjahr geprüft werden muss.
	 *
	 * @param fachbelegungen    die zu prüfenden Fachnbelegungen
	 * @param kursart           die zu prüfende Kursart
	 * @param halbjahre         die zu prüfenden Halbjahre
	 *
	 * @return true, falls die Schriftlichkeit in den Halbjahren gegeben ist, sonst false
	 */
	public boolean pruefeBelegungExistiertMitKursart(final List<AbiturFachbelegung> fachbelegungen, final @NotNull GostKursart kursart,
			final @NotNull GostHalbjahr... halbjahre) {
		if ((fachbelegungen == null) || (fachbelegungen.isEmpty()))
			return false;
		if (halbjahre.length == 0)
			return true;
		for (final AbiturFachbelegung fachbelegung : fachbelegungen) {
			if (pruefeBelegungMitKursart(fachbelegung, kursart, halbjahre))
				return true;
		}
		return false;
	}


	/**
	 * Prüft, ob die Belegung des Faches in den angegebenen Halbjahren mindestens einmal die angegebenen Kursart hat.
	 * Ist die Fachbelegung null, so schlägt die Prüfung fehl. Wird bei einer gültigen Fachbelegung kein Halbjahr
	 * angegeben, so ist die Prüfung nicht erfolgreich, da kein Halbjahr geprüft werden muss und somit die Kursart nicht
	 * einmal existiert.
	 *
	 * @param fachbelegung      die zu prüfende Fachnbelegung
	 * @param kursart           die zu prüfende Kursart
	 * @param halbjahre         die zu prüfenden Halbjahre
	 *
	 * @return true, falls die Kursart mindestens einmal in den Halbjahren gegeben ist, sonst false
	 */
	public boolean pruefeBelegungHatMindestensEinmalKursart(final AbiturFachbelegung fachbelegung, final @NotNull GostKursart kursart,
			final @NotNull GostHalbjahr... halbjahre) {
		if (fachbelegung == null)
			return false;
		if (halbjahre.length == 0)
			return false;
		for (final GostHalbjahr halbjahr : halbjahre) {
			final AbiturFachbelegungHalbjahr belegungHalbjahr = fachbelegung.belegungen[halbjahr.id];
			if (belegungHalbjahr == null)
				continue;
			if ((kursart == GostKursart.fromKuerzel(belegungHalbjahr.kursartKuerzel))
					&& (!istNullPunkteBelegungInQPhase(belegungHalbjahr)))
				return true;
		}
		return false;
	}



	/**
	 * Prüft, ob die Belegung des Faches in dem angegebenen Halbjahr der angegebenen Schriftlichkeit entspricht.
	 * Ist die Fachbelegung null, so schlägt die Prüfung fehl.
	 *
	 * @param fachbelegung      die zu prüfende Fachnbelegung
	 * @param schriftlichkeit   die zu prüfende Schriftlichkeit
	 * @param halbjahr          das zu prüfende Halbjahr
	 *
	 * @return true, falls die Schriftlichkeit in dem Halbjahr gegeben ist, sonst false
	 */
	public boolean pruefeBelegungMitSchriftlichkeitEinzeln(final AbiturFachbelegung fachbelegung, final @NotNull GostSchriftlichkeit schriftlichkeit,
			final @NotNull GostHalbjahr halbjahr) {
		if (fachbelegung == null)
			return false;
		final AbiturFachbelegungHalbjahr belegungHalbjahr = fachbelegung.belegungen[halbjahr.id];
		if ((belegungHalbjahr == null) || (istNullPunkteBelegungInQPhase(belegungHalbjahr)))
			return false;
		switch (schriftlichkeit) {
			case BELIEBIG:
				return true;
			case SCHRIFTLICH:
				return belegungHalbjahr.schriftlich;
			case MUENDLICH:
				return !belegungHalbjahr.schriftlich;
			default:
				return false;
		}
	}


	/**
	 * Prüft, ob die Belegung des Faches in den angegebenen Halbjahren der angegebenen Schriftlichkeit entspricht.
	 * Ist die Fachbelegung null, so schlägt die Prüfung fehl. Wird bei einer gültigen Fachbelegung kein Halbjahr
	 * angegeben, so ist die Prüfung erfolgreich, da kein Halbjahr geprüft werden muss.
	 *
	 * @param fachbelegung      die zu prüfende Fachnbelegung
	 * @param schriftlichkeit   die zu prüfende Schriftlichkeit
	 * @param halbjahre         die zu prüfenden Halbjahre
	 *
	 * @return true, falls die Schriftlichkeit in den Halbjahren gegeben ist, sonst false
	 */
	public boolean pruefeBelegungMitSchriftlichkeit(final AbiturFachbelegung fachbelegung, final @NotNull GostSchriftlichkeit schriftlichkeit,
			final @NotNull GostHalbjahr... halbjahre) {
		if (fachbelegung == null)
			return false;
		if (halbjahre.length == 0)
			return true;
		for (final GostHalbjahr halbjahr : halbjahre)
			if (!pruefeBelegungMitSchriftlichkeitEinzeln(fachbelegung, schriftlichkeit, halbjahr))
				return false;
		return true;
	}


	/**
	 * Prüft, ob eine Belegung des Faches in den angegebenen Halbjahren nicht der angegebenen Schriftlichkeit entspricht.
	 * Ist die Fachbelegung null, so schlägt die Prüfung fehl. Wird bei einer gültigen Fachbelegung kein Halbjahr
	 * angegeben, so ist die Prüfung erfolgreich, da kein Halbjahr geprüft werden muss.
	 *
	 * @param fachbelegung      die zu prüfende Fachnbelegung
	 * @param schriftlichkeit   die zu prüfende Schriftlichkeit
	 * @param halbjahre         die zu prüfenden Halbjahre
	 *
	 * @return true, falls die Schriftlichkeit in den Halbjahren nicht gegeben ist, sonst false
	 */
	public boolean pruefeBelegungErfuelltNicht(final AbiturFachbelegung fachbelegung, final @NotNull GostSchriftlichkeit schriftlichkeit,
			final @NotNull GostHalbjahr... halbjahre) {
		if (fachbelegung == null)
			return false;
		if (halbjahre.length == 0)
			return true;
		for (final GostHalbjahr halbjahr : halbjahre) {
			final AbiturFachbelegungHalbjahr belegungHalbjahr = fachbelegung.belegungen[halbjahr.id];
			if ((belegungHalbjahr == null) || (istNullPunkteBelegungInQPhase(belegungHalbjahr))
					|| ((schriftlichkeit != GostSchriftlichkeit.BELIEBIG)
							&& (((schriftlichkeit == GostSchriftlichkeit.SCHRIFTLICH) && (!belegungHalbjahr.schriftlich))
									|| ((schriftlichkeit == GostSchriftlichkeit.MUENDLICH) && (belegungHalbjahr.schriftlich)))))
				return true;
		}
		return false;
	}


	/**
	 * Prüft, ob eine Belegung des Faches in den angegebenen Halbjahren nicht der angegebenen Schriftlichkeit entspricht,
	 * sofern es in dem Halbjahr überhaupt belegt wurde..
	 * Ist die Fachbelegung null, so schlägt die Prüfung fehl. Wird bei einer gültigen Fachbelegung kein Halbjahr
	 * angegeben, so ist die Prüfung erfolgreich, da kein Halbjahr geprüft werden muss.
	 *
	 * @param fachbelegung      die zu prüfende Fachnbelegung
	 * @param schriftlichkeit   die zu prüfende Schriftlichkeit
	 * @param halbjahre         die zu prüfenden Halbjahre
	 *
	 * @return true, falls die Schriftlichkeit in den Halbjahren nicht gegeben ist, sonst false
	 */
	public boolean pruefeBelegungErfuelltNichtFallsBelegt(final AbiturFachbelegung fachbelegung, final @NotNull GostSchriftlichkeit schriftlichkeit,
			final @NotNull GostHalbjahr... halbjahre) {
		if (fachbelegung == null)
			return false;
		if (halbjahre.length == 0)
			return true;
		for (final GostHalbjahr halbjahr : halbjahre) {
			final AbiturFachbelegungHalbjahr belegungHalbjahr = fachbelegung.belegungen[halbjahr.id];
			if ((belegungHalbjahr == null) || (istNullPunkteBelegungInQPhase(belegungHalbjahr)))
				continue;
			if (((schriftlichkeit != GostSchriftlichkeit.BELIEBIG)
					&& (((schriftlichkeit == GostSchriftlichkeit.SCHRIFTLICH) && (!belegungHalbjahr.schriftlich))
							|| ((schriftlichkeit == GostSchriftlichkeit.MUENDLICH) && (belegungHalbjahr.schriftlich)))))
				return true;
		}
		return false;
	}


	/**
	 * Prüft, ob die Belegung des Faches in den angegebenen Halbjahren mindestens einmal die angegebene Schritflichkeit hat.
	 * Ist die Fachbelegung null, so schlägt die Prüfung fehl. Wird bei einer gültigen Fachbelegung kein Halbjahr
	 * angegeben, so ist die Prüfung nicht erfolgreich, da kein Halbjahr geprüft werden muss und somit die Schriftlichkeit nicht
	 * einmal existiert.
	 *
	 * @param fachbelegung      die zu prüfende Fachbelegung
	 * @param schriftlichkeit   die zu prüfende Schriftlichkeit
	 * @param halbjahre         die zu prüfenden Halbjahre
	 *
	 * @return true, falls die angegebene Schriftlichkeit mindestens einmal in den Halbjahren gegeben ist, sonst false
	 */
	public boolean pruefeBelegungHatMindestensEinmalSchriftlichkeit(final AbiturFachbelegung fachbelegung, final @NotNull GostSchriftlichkeit schriftlichkeit,
			final @NotNull GostHalbjahr... halbjahre) {
		if (fachbelegung == null)
			return false;
		if (halbjahre.length == 0)
			return false;
		for (final GostHalbjahr halbjahr : halbjahre) {
			final AbiturFachbelegungHalbjahr belegungHalbjahr = fachbelegung.belegungen[halbjahr.id];
			if ((belegungHalbjahr == null) || (istNullPunkteBelegungInQPhase(belegungHalbjahr)))
				continue;
			if ((schriftlichkeit.istSchriftlich == null) || (schriftlichkeit.istSchriftlich.equals(belegungHalbjahr.schriftlich)))
				return true;
		}
		return false;
	}




	/**
	 * Prüft, ob eine Fachbelegung existiert, welche in den angegebenen Halbjahren mindestens einmal die angegebene
	 * Schritflichkeit hat.
	 * Ist die Fachbelegung null, so schlägt die Prüfung fehl. Wird bei einer gültigen Fachbelegung kein Halbjahr
	 * angegeben, so ist die Prüfung nicht erfolgreich, da kein Halbjahr geprüft werden muss und somit die Schriftlichkeit nicht
	 * einmal existiert.
	 *
	 * @param fachbelegungen    die zu prüfenden Fachbelegungen
	 * @param schriftlichkeit   die zu prüfende Schriftlichkeit
	 * @param halbjahre         die zu prüfenden Halbjahre
	 *
	 * @return true, falls die angegebene Schriftlichkeit bei einer Fachbelegung mindestens einmal in den Halbjahren gegeben ist, sonst false
	 */
	public boolean pruefeBelegungExistiertHatMindestensEinmalSchriftlichkeit(final List<AbiturFachbelegung> fachbelegungen,
			final @NotNull GostSchriftlichkeit schriftlichkeit, final @NotNull GostHalbjahr... halbjahre) {
		if ((fachbelegungen == null) || (fachbelegungen.isEmpty()))
			return false;
		if (halbjahre.length == 0)
			return false;
		for (final AbiturFachbelegung fachbelegung : fachbelegungen)
			if (pruefeBelegungHatMindestensEinmalSchriftlichkeit(fachbelegung, schriftlichkeit, halbjahre))
				return true;
		return false;
	}




	/**
	 * Prüft, ob die Belegung eines der angegebenen Fächer mit den angegebenen Halbjahren existiert.
	 * Ist keine Fachbelegung gegeben, so schlägt die Prüfung fehl. Wird bei einer gültigen Fachbelegung kein Halbjahr
	 * angegeben, so ist die Prüfung erfolgreich, da kein Halbjahr geprüft werden muss.
	 * In dieser Methode wird ggf. auch geprüft, ob weitere Fachbelegungen existieren, welche das gleiche
	 * Statistik-Kürzel haben und Ersatzweise eine Halbjahres-Belegung ersetzen können. Dies ist z.B. bei bilingualen
	 * Fächern nötig oder bei der Unterscheidung von Sport-Profilen.
	 *
	 * @param fachbelegungen    die zu prüfenden Fachnbelegungen
	 * @param halbjahre         die zu prüfenden Halbjahre
	 *
	 * @return true, falls eine Fachbelegung mit den Halbjahren existiert, sonst false
	 */
	public boolean pruefeBelegungExistiert(final List<AbiturFachbelegung> fachbelegungen, final @NotNull GostHalbjahr... halbjahre) {
		if (fachbelegungen == null)
			return false;
		if ((halbjahre == null) || (halbjahre.length == 0))
			return true;
		for (final AbiturFachbelegung fachbelegung : fachbelegungen) {
			// Beachte alle Fachbelegungen von Fächern des gleichen Statistik-Faches - dies kann bei bilingualen Fächern wichtig sein
			final GostFach fach = faecherManager.get(fachbelegung.fachID);
			if (fach == null)
				continue;
			final List<AbiturFachbelegung> alleBelegungen = getFachbelegungByFachkuerzel(fach.kuerzel);
			if ((alleBelegungen == null) || (alleBelegungen.isEmpty()))
				continue;
			boolean hatBelegung = true;
			for (final GostHalbjahr halbjahr : halbjahre) {
				boolean hatHalbjahresBelegung = false;
				for (final AbiturFachbelegung aktFachbelegung : alleBelegungen) {
					final AbiturFachbelegungHalbjahr belegungHalbjahr = aktFachbelegung.belegungen[halbjahr.id];
					if ((belegungHalbjahr != null) && (!istNullPunkteBelegungInQPhase(belegungHalbjahr))) {
						hatHalbjahresBelegung = true;
						break;
					}
				}
				if (!hatHalbjahresBelegung) {
					hatBelegung = false;
					break;
				}
			}
			if (hatBelegung)
				return true;
		}
		return false;
	}


	/**
	 * Prüft, ob die Belegung eines der angegebenen Fächer mit dem angegebenen Halbjahr existiert.
	 * Ist keine Fachbelegung gegeben, so schlägt die Prüfung fehl.
	 * In dieser Methode wird ggf. auch geprüft, ob weitere Fachbelegungen existieren, welche das gleiche
	 * Statistik-Kürzel haben und Ersatzweise eine Halbjahres-Belegung ersetzen können. Dies ist z.B. bei bilingualen
	 * Fächern nötig oder bei der Unterscheidung von Sport-Profilen.
	 *
	 * @param fachbelegungen    die zu prüfenden Fachnbelegungen
	 * @param halbjahr          das zu prüfende Halbjahr
	 *
	 * @return true, falls eine Fachbelegung mit dem Halbjahr existiert, sonst false
	 */
	public boolean pruefeBelegungExistiertEinzeln(final List<AbiturFachbelegung> fachbelegungen, final @NotNull GostHalbjahr halbjahr) {
		if (fachbelegungen == null)
			return false;
		for (final AbiturFachbelegung fachbelegung : fachbelegungen) {
			// Beachte alle Fachbelegungen von Fächern des gleichen Statistik-Faches - dies kann bei bilingualen Fächern wichtig sein
			final GostFach fach = faecherManager.get(fachbelegung.fachID);
			if (fach == null)
				continue;
			final List<AbiturFachbelegung> alleBelegungen = getFachbelegungByFachkuerzel(fach.kuerzel);
			if (alleBelegungen.isEmpty())
				continue;
			for (final AbiturFachbelegung aktFachbelegung : alleBelegungen) {
				final AbiturFachbelegungHalbjahr belegungHalbjahr = aktFachbelegung.belegungen[halbjahr.id];
				if ((belegungHalbjahr != null) && (!istNullPunkteBelegungInQPhase(belegungHalbjahr)))
					return true;
			}
		}
		return false;
	}


	/**
	 * Prüft, ob die Belegung eines der angegebenen Fächer mit einer durchgängigen Belegung existiert,
	 * die zumindest in der Q1 und der Q2.1 schriftlich ist.
	 * In dieser Methode wird ggf. auch geprüft, ob weitere Fachbelegungen existieren, welche das gleiche
	 * Statistik-Kürzel haben und Ersatzweise eine Halbjahres-Belegung ersetzen können. Dies ist bei bilingualen
	 * Fächern nötig.
	 * Ist keine Fachbelegung gegeben, so schlägt die Prüfung fehl.
	 *
	 * @param fachbelegungen    die zu prüfenden Fachnbelegungen
	 *
	 * @return true, falls eine durchgehend schriftliche Fachbelegung existiert, sonst false
	 */
	public boolean pruefeBelegungExistiertDurchgehendSchriftlich(final List<AbiturFachbelegung> fachbelegungen) {
		if (fachbelegungen == null)
			return false;
		for (final AbiturFachbelegung fachbelegung : fachbelegungen) {
			// Beachte alle Fachbelegungen von Fächern des gleichen Statistik-Faches - dies kann bei bilingualen Fächern wichtig sein
			final GostFach fach = faecherManager.get(fachbelegung.fachID);
			if (fach == null)
				continue;
			final List<AbiturFachbelegung> alleBelegungen = getFachbelegungByFachkuerzel(fach.kuerzel);
			if ((alleBelegungen == null) || (alleBelegungen.isEmpty()))
				continue;
			boolean hatBelegung = true;
			for (final GostHalbjahr halbjahr : GostHalbjahr.values()) {
				boolean hatHalbjahresBelegung = false;
				for (final AbiturFachbelegung aktFachbelegung : alleBelegungen) {
					final AbiturFachbelegungHalbjahr belegungHalbjahr = aktFachbelegung.belegungen[halbjahr.id];
					if ((belegungHalbjahr != null) && (!istNullPunkteBelegungInQPhase(belegungHalbjahr))
							&& (((halbjahr != GostHalbjahr.Q11) && (halbjahr != GostHalbjahr.Q12) && (halbjahr != GostHalbjahr.Q21))
									|| (belegungHalbjahr.schriftlich))) {
						hatHalbjahresBelegung = true;
						// "break;" wird hier nicht verwendet, da bei der fehlerhaften Belegung von inhaltsgleichen Fächern (gleiches Statistik-Kürzel) im selben Halbjahr diese Prüfung dennoch korrekt ablaufen sollte
					}
				}
				if (!hatHalbjahresBelegung) {
					hatBelegung = false;
					break;
				}
			}
			if (hatBelegung)
				return true;
		}
		return false;
	}


	/**
	 * Prüft, ob die Belegung eines der angegebenen Fächer in dem angegebenen Halbjahr der angegebenen Schriftlichkeit entspricht.
	 * Ist keine Fachbelegung gegeben, so schlägt die Prüfung fehl.
	 *
	 * @param fachbelegungen    die zu prüfenden Fachnbelegungen
	 * @param schriftlichkeit   die zu prüfende Schriftlichkeit
	 * @param halbjahr          das zu prüfende Halbjahr
	 *
	 * @return true, falls bei einer Fachbelegung die Schriftlichkeit in dem Halbjahr gegeben ist, sonst false
	 */
	public boolean pruefeBelegungExistiertMitSchriftlichkeitEinzeln(final List<AbiturFachbelegung> fachbelegungen,
			final @NotNull GostSchriftlichkeit schriftlichkeit, final @NotNull GostHalbjahr halbjahr) {
		if (fachbelegungen == null)
			return false;
		for (final AbiturFachbelegung fachbelegung : fachbelegungen) {
			if (pruefeBelegungMitSchriftlichkeitEinzeln(fachbelegung, schriftlichkeit, halbjahr))
				return true;
		}
		return false;
	}


	/**
	 * Prüft, ob die Belegung eines der angegebenen Fächer in den angegebenen Halbjahren der angegebenen Schriftlichkeit entspricht.
	 * Ist keine Fachbelegung gegeben, so schlägt die Prüfung fehl. Wird bei einer gültigen Fachbelegung kein Halbjahr
	 * angegeben, so ist die Prüfung erfolgreich, da kein Halbjahr geprüft werden muss.
	 *
	 * @param fachbelegungen    die zu prüfenden Fachnbelegungen
	 * @param schriftlichkeit   die zu prüfende Schriftlichkeit
	 * @param halbjahre         die zu prüfenden Halbjahre
	 *
	 * @return true, falls bei einer Fachbelegung die Schriftlichkeit in den Halbjahren gegeben ist, sonst false
	 */
	public boolean pruefeBelegungExistiertMitSchriftlichkeit(final List<AbiturFachbelegung> fachbelegungen,
			final @NotNull GostSchriftlichkeit schriftlichkeit, final GostHalbjahr... halbjahre) {
		if (fachbelegungen == null)
			return false;
		for (final AbiturFachbelegung fachbelegung : fachbelegungen) {
			if (pruefeBelegungMitSchriftlichkeit(fachbelegung, schriftlichkeit, halbjahre))
				return true;
		}
		return false;
	}


	/**
	 * Prüft, ob die Belegung eines der angegebenen Fächer in den angegebenen Halbjahren mindestens einmal die angegebene Kursart
	 * hat. Ist keine Fachbelegung gegeben, so schlägt die Prüfung fehl. Wird bei einer gültigen Fachbelegung kein Halbjahr
	 * angegeben, so ist die Prüfung erfolgreich, da kein Halbjahr geprüft werden muss.
	 *
	 * @param fachbelegungen    die zu prüfenden Fachnbelegungen
	 * @param kursart           die zu prüfende Kursart
	 * @param halbjahre         die zu prüfenden Halbjahre
	 *
	 * @return true, falls die Kursart bei einer Fachbelegung mindestens einmal in den Halbjahren gegeben ist, sonst false
	 */
	public boolean pruefeBelegungExistiertHatMindestensEinmalKursart(final List<AbiturFachbelegung> fachbelegungen, final @NotNull GostKursart kursart,
			final GostHalbjahr... halbjahre) {
		if (fachbelegungen == null)
			return false;
		for (final AbiturFachbelegung fachbelegung : fachbelegungen) {
			if (pruefeBelegungHatMindestensEinmalKursart(fachbelegung, kursart, halbjahre))
				return true;
		}
		return false;
	}


	/**
	 * Prüft, ob die Belegung eines der angegebenen Fächer in den angegebenen Halbjahren existiert,
	 * bei welchem in mind. einem der Halbjahren die angebene Schriftlichkeit nicht gegeben ist.
	 * Ist keine Fachbelegung gegeben, so schlägt die Prüfung fehl. Wird bei einer gültigen Fachbelegung kein Halbjahr
	 * angegeben, so ist die Prüfung erfolgreich, da kein Halbjahr geprüft werden muss.
	 *
	 * @param fachbelegungen    die zu prüfenden Fachnbelegungen
	 * @param schriftlichkeit   die zu prüfende Schriftlichkeit
	 * @param halbjahre         die zu prüfenden Halbjahre
	 *
	 * @return true oder false (siehe oben)
	 */
	public boolean pruefeBelegungExistiertErfuelltNicht(final List<AbiturFachbelegung> fachbelegungen,
			final @NotNull GostSchriftlichkeit schriftlichkeit, final GostHalbjahr... halbjahre) {
		if (fachbelegungen == null)
			return false;
		for (final AbiturFachbelegung fachbelegung : fachbelegungen) {
			if (pruefeBelegungErfuelltNicht(fachbelegung, schriftlichkeit, halbjahre))
				return true;
		}
		return false;
	}


	/**
	 * Prüft, ob die Belegung eines der angegebenen Fächer in den angegebenen Halbjahren existiert,
	 * bei welchem in mind. einem der Halbjahren die angebene Schriftlichkeit nicht gegeben ist, sofern
	 * das Fach überhaupt belegt wurde.
	 * Ist keine Fachbelegung gegeben, so schlägt die Prüfung fehl. Wird bei einer gültigen Fachbelegung kein Halbjahr
	 * angegeben, so ist die Prüfung erfolgreich, da kein Halbjahr geprüft werden muss.
	 *
	 * @param fachbelegungen    die zu prüfenden Fachnbelegungen
	 * @param schriftlichkeit   die zu prüfende Schriftlichkeit
	 * @param halbjahre         die zu prüfenden Halbjahre
	 *
	 * @return true oder false (siehe oben)
	 */
	public boolean pruefeBelegungExistiertErfuelltNichtFallsBelegt(final List<AbiturFachbelegung> fachbelegungen,
			final @NotNull GostSchriftlichkeit schriftlichkeit, final GostHalbjahr... halbjahre) {
		if (fachbelegungen == null)
			return false;
		for (final AbiturFachbelegung fachbelegung : fachbelegungen) {
			if (pruefeBelegungErfuelltNichtFallsBelegt(fachbelegung, schriftlichkeit, halbjahre))
				return true;
		}
		return false;
	}



	/**
	 * Prüft, ob die Belegung des Faches in den angegebenen Halbjahren der angegebenen Schriftlichkeit entspricht
	 * und das Fach durchgehend belegbar ist.
	 * Ist die Fachbelegung null, so schlägt die Prüfung fehl. Wird bei einer gültigen Fachbelegung kein Halbjahr
	 * angegeben, so ist die Prüfung erfolgreich, da kein Halbjahr geprüft werden muss.
	 *
	 * @param fachbelegung      die zu prüfende Fachnbelegung
	 * @param schriftlichkeit   die zu prüfende Schriftlichkeit
	 * @param halbjahre         die zu prüfenden Halbjahre
	 *
	 * @return true, falls die Schriftlichkeit in den Halbjahren gegeben ist, sonst false
	 */
	public boolean pruefeBelegungDurchgehendBelegbar(final AbiturFachbelegung fachbelegung, final @NotNull GostSchriftlichkeit schriftlichkeit,
			final GostHalbjahr... halbjahre) {
		if (fachbelegung == null)
			return false;
		if (!GostFachUtils.istDurchgehendBelegbarBisQ22(getFach(fachbelegung)))
			return false;
		return pruefeBelegungMitSchriftlichkeit(fachbelegung, schriftlichkeit, halbjahre);
	}


	/**
	 * Prüft, ob die Belegung eines der angegebenen Fächer in den angegebenen Halbjahren der angegebenen Schriftlichkeit entspricht
	 * und das Fach durchgängig belegbar ist.
	 * Ist keine Fachbelegung gegeben, so schlägt die Prüfung fehl. Wird bei einer gültigen Fachbelegung kein Halbjahr
	 * angegeben, so ist die Prüfung erfolgreich, da kein Halbjahr geprüft werden muss.
	 *
	 * @param fachbelegungen    die zu prüfenden Fachbelegungen
	 * @param schriftlichkeit   die zu prüfende Schriftlichkeit
	 * @param halbjahre         die zu prüfenden Halbjahre
	 *
	 * @return true, falls bei einer Fachbelegung die Schriftlichkeit in den Halbjahren gegeben ist, sonst false
	 */
	public boolean pruefeBelegungDurchgehendBelegbarExistiert(final List<AbiturFachbelegung> fachbelegungen,
			final @NotNull GostSchriftlichkeit schriftlichkeit, final GostHalbjahr... halbjahre) {
		if (fachbelegungen == null)
			return false;
		for (final AbiturFachbelegung fachbelegung : fachbelegungen) {
			if (pruefeBelegungDurchgehendBelegbar(fachbelegung, schriftlichkeit, halbjahre))
				return true;
		}
		return false;
	}



	/**
	 * Prüft, ob die Belegung eines der angegebenen Fächer in den angegebenen Halbjahren der angegebenen Schriftlichkeit entspricht
	 * und das Fach durchgängig belegt ist.
	 * Ist keine Fachbelegung gegeben, so schlägt die Prüfung fehl. Wird bei einer gültigen Fachbelegung kein Halbjahr
	 * angegeben, so ist die Prüfung erfolgreich sofern das Fach durchgängig belegt wurde, da kein Halbjahr auf die
	 * Schriftlichkeit geprüft werden muss.
	 *
	 * @param fachbelegungen    die zu prüfenden Fachbelegungen
	 * @param schriftlichkeit   die zu prüfende Schriftlichkeit
	 * @param halbjahre         die zu prüfenden Halbjahre
	 *
	 * @return true, falls bei eine Fachbelegung durchgängig belegt wurde und die Schriftlichkeit in den Halbjahren gegeben ist, sonst false
	 */
	public boolean pruefeBelegungDurchgehendBelegtExistiert(final List<AbiturFachbelegung> fachbelegungen,
			final @NotNull GostSchriftlichkeit schriftlichkeit, final GostHalbjahr... halbjahre) {
		if (fachbelegungen == null)
			return false;
		for (final AbiturFachbelegung fachbelegung : fachbelegungen) {
			if (pruefeBelegung(fachbelegung, GostHalbjahr.EF1, GostHalbjahr.EF2, GostHalbjahr.Q11, GostHalbjahr.Q12, GostHalbjahr.Q21, GostHalbjahr.Q22)
					&& pruefeBelegungMitSchriftlichkeit(fachbelegung, schriftlichkeit, halbjahre))
				return true;
		}
		return false;
	}




	/**
	 * Prüft, ob die Fachbelegung in mindestens einem der Halbjahre die angegebene Kursart aufweist.
	 * Existiert die Fachbelegung nicht (null), so kommt die Kursart auch nicht vor.
	 *
	 * @param fachbelegung   die Fachbelegung
	 * @param kursart        die Kursart
	 *
	 * @return true, falls mindestens einmal die Kursart belegt wurde, sonst false
	 */
	public boolean pruefeAufKursart(final AbiturFachbelegung fachbelegung, final @NotNull GostKursart kursart) {
		if (fachbelegung == null)
			return false;
		for (final AbiturFachbelegungHalbjahr belegunghalbjahr : fachbelegung.belegungen) {
			if ((belegunghalbjahr != null) && (!istNullPunkteBelegungInQPhase(belegunghalbjahr))
					&& (GostKursart.fromKuerzel(belegunghalbjahr.kursartKuerzel) == kursart))
				return true;
		}
		return false;
	}


	/**
	 * Filtert die Fachbelegungen und gibt nur die Fachbelegungen zurück, bei denen die
	 * Kursart existiert.
	 * Wird keine Fachbelegung übergeben (null oder leere Liste), so kommt auch keine
	 * Belegung mit der Kursart vor.
	 *
	 * @param fachbelegungen   die Fachbelegungen
	 * @param kursart          die Kursart
	 *
	 * @return eine Liste mit den Fachbelegungen, welche die kursart haben
	 */
	public @NotNull List<AbiturFachbelegung> filterBelegungKursartExistiert(final List<AbiturFachbelegung> fachbelegungen,
			final @NotNull GostKursart kursart) {
		final @NotNull ArrayList<AbiturFachbelegung> result = new ArrayList<>();
		if ((fachbelegungen == null) || (fachbelegungen.isEmpty()))
			return result;
		for (final AbiturFachbelegung fachbelegung : fachbelegungen) {
			if (pruefeAufKursart(fachbelegung, kursart))
				result.add(fachbelegung);
		}
		return result;
	}


	/**
	 * Prüft, ob die Fachbelegung eine durchgängige Belegung hat. Zusatzkurse können nicht für eine
	 * durchgängige Belegung zählen.
	 *
	 * @param fachbelegung   die zu prüfende Fachbelegung
	 *
	 * @return true, wenn die Belegung durchgängig ist.
	 */
	public boolean pruefeDurchgaengigkeit(final AbiturFachbelegung fachbelegung) {
		if ((fachbelegung == null) || (pruefeAufKursart(fachbelegung, GostKursart.ZK)))
			return false;
		return pruefeBelegung(fachbelegung, GostHalbjahr.EF1, GostHalbjahr.EF2, GostHalbjahr.Q11, GostHalbjahr.Q12, GostHalbjahr.Q21, GostHalbjahr.Q22);
	}


	/**
	 * Zählt die Fachbelegungen, welche eine durchgängige Belegung aufweisen. Zusatzkurse zählen
	 * nicht für eine durchgängige Belegung.
	 * In dieser Methode wird ggf. auch geprüft, ob weitere Fachbelegungen existieren, welche das gleiche
	 * Statistik-Kürzel haben und Ersatzweise eine Halbjahres-Belegung ersetzen können. Dies ist bei bilingualen
	 * Fächern nötig.
	 *
	 * @param fachbelegungen   die zu überprüfenden Fachbelegungen
	 *
	 * @return die Anzahl der durchgängigen Belegungen
	 */
	public int zaehleDurchgaengigeBelegungen(final List<AbiturFachbelegung> fachbelegungen) {
		if (fachbelegungen == null)
			return 0;
		int anzahl = 0;
		for (final AbiturFachbelegung fachbelegung : fachbelegungen) {
			final GostFach fach = faecherManager.get(fachbelegung.fachID);
			if (fach == null)
				continue;
			if (fachbelegung.belegungen[GostHalbjahr.EF1.id] == null)
				continue;
			final List<AbiturFachbelegung> alleBelegungen = getFachbelegungByFachkuerzel(fach.kuerzel);
			if ((alleBelegungen == null) || (alleBelegungen.isEmpty()))
				continue;
			boolean hatBelegung = true;
			// Beachte zur Fortsetzung alle Fachbelegungen von Fächern des gleichen Statistik-Faches - dies kann bei bilingualen Fächern wichtig sein
			final @NotNull GostHalbjahr @NotNull [] halbjahre = { GostHalbjahr.EF1, GostHalbjahr.Q11, GostHalbjahr.Q12, GostHalbjahr.Q21, GostHalbjahr.Q22 };
			for (final GostHalbjahr halbjahr : halbjahre) {
				boolean hatHalbjahresBelegung = false;
				for (final AbiturFachbelegung aktFachbelegung : alleBelegungen) {
					final AbiturFachbelegungHalbjahr belegungHalbjahr = aktFachbelegung.belegungen[halbjahr.id];
					if ((belegungHalbjahr != null) && (!istNullPunkteBelegungInQPhase(belegungHalbjahr))) {
						hatHalbjahresBelegung = true;
						break;
					}
				}
				if (!hatHalbjahresBelegung) {
					hatBelegung = false;
					break;
				}
			}
			if (hatBelegung)
				anzahl++;
		}
		return anzahl;
	}


	/**
	 * Prüft, ob die Fachbelegung eine durchgängige Belegung hat und prüft die Schriftlichkeit
	 * in der Qualifikationsphase. Ein Fach in der Qualifikationsphase gilt als Schriftlich belegt,
	 * sofern die ersten 3 Halbjahre der Qualifikationsphase schriftlich belegt wurden.
	 * - Zusatzkurse können nicht für eine durchgängige Belegung zählen.
	 *
	 * @param fachbelegung   die zu prüfende die zu überprüfenden Fachbelegung
	 *
	 * @return true, wenn die Belegung durchgängig ist und die Schriftlichkeit den Anforderungen genügt.
	 */
	public boolean pruefeDurchgaengigkeitSchriftlich(final AbiturFachbelegung fachbelegung) {
		if (!pruefeDurchgaengigkeit(fachbelegung))
			return false;
		return pruefeBelegungMitSchriftlichkeit(fachbelegung, GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.Q11, GostHalbjahr.Q12, GostHalbjahr.Q21);
	}


	/**
	 * Prüft, ob unter den angegebenen Fachbelegungen ein Fach als Abiturfach von einem der angegebenen Arten
	 * gewählt wurde. Wird keine Art angebeben, so wird jede Fachbelegung akzeptiert und true zurückgegeben.
	 *
	 * @param fachbelegungen   die Fachbelegungen
	 * @param arten            die Arten der Abiturfächer
	 *
	 * @return true, falls unter den Fachbelegungen mindestens ein Fach als Abiturfach von einem der
	 *         angegebenen Arten gewählt wurde und false sonst
	 */
	public boolean pruefeExistiertAbiFach(final List<AbiturFachbelegung> fachbelegungen, final GostAbiturFach... arten) {
		if ((arten == null) || (arten.length == 0))
			return true;
		if (fachbelegungen == null)
			return false;
		for (final AbiturFachbelegung fachbelegung : fachbelegungen)
			for (final GostAbiturFach art : arten) {
				final GostAbiturFach abiturFach = GostAbiturFach.fromID(fachbelegung.abiturFach);
				if (abiturFach == art)
					return true;
			}
		return false;
	}


	/**
	 * Prüft, ob ein Abiturfach der übergebenen Art (1-4) existiert oder nicht.
	 *
	 * @param art   die Art des Abiturfaches (siehe {@link GostAbiturFach}
	 *
	 * @return true, falls die Art des Abiturfaches belegt wurde und ansonsten false
	 */
	public boolean hatAbiFach(final @NotNull GostAbiturFach art) {
		for (final @NotNull AbiturFachbelegung fachbelegung : abidaten.fachbelegungen) {
			final GostAbiturFach abiturFach = GostAbiturFach.fromID(fachbelegung.abiturFach);
			if (abiturFach == art)
				return true;
		}
		return false;
	}


	/**
	 * Prüft anhand des Statistik-Kürzels, ob in dem angegebenen Halbjahr eine doppelte Fachbelegung
	 * vorliegt oder nicht. Bei den Fremdsprachen werden nur unterschiedliche Fremdsprachen in einem Halbjahr
	 * akzeptiert und es dürfen mehrere Vertiefungsfächer (VX) in einem Halbjahr vorkommen. Fächer, die
	 * als nicht Prüfungsordnungs-relevant markiert sind werden ignoriert
	 *
	 * @param halbjahr   das zu prüfende Halbjahr
	 *
	 * @return true, falls eine doppelte Belegung vorliegt, sonst false
	 */
	public boolean hatDoppelteFachbelegungInHalbjahr(final @NotNull GostHalbjahr halbjahr) {
		final @NotNull HashSet<String> set = new HashSet<>();
		final @NotNull List<AbiturFachbelegung> fachbelegungen = abidaten.fachbelegungen;
		for (final AbiturFachbelegung fb : fachbelegungen) {
			final GostFach fach = getFach(fb);
			if ((fach == null) || (!fach.istPruefungsordnungsRelevant))
				continue;
			final AbiturFachbelegungHalbjahr belegungHalbjahr = getBelegungHalbjahr(fb, halbjahr, GostSchriftlichkeit.BELIEBIG);
			if ((belegungHalbjahr == null) || (istNullPunkteBelegungInQPhase(belegungHalbjahr)))
				continue;
			String kuerzel = GostFachUtils.getFremdsprache(fach);
			if (kuerzel == null)
				kuerzel = (fach.kuerzel == null) ? "" : fach.kuerzel;
			if (!set.add(kuerzel) && (!"VX".equals(kuerzel)))
				return true;
		}
		return false;
	}


	/**
	 * Prüft anhand des Statistik-Kürzels, ob in einem der angegebenen Halbjahre eine doppelte Fachbelegung
	 * vorliegt oder nicht. Bei den Fremdsprachen werden nur unterschiedliche Fremdsprachen in einem Halbjahr
	 * akzeptiert und es dürfen mehrere Vertiefungsfächer (VX) in einem Halbjahr vorkommen.
	 *
	 * @param halbjahre   die zu prüfenden Halbjahre
	 *
	 * @return true, falls eine doppelte Belegung vorliegt, sonst false
	 */
	public boolean hatDoppelteFachbelegung(final @NotNull GostHalbjahr... halbjahre) {
		if (halbjahre.length == 0)
			return false;
		for (final GostHalbjahr halbjahr : halbjahre)
			if (hatDoppelteFachbelegungInHalbjahr(halbjahr))
				return true;
		return false;
	}


	/**
	 * Gibt zurück, ob der Projektkurs als besondere Lernleistung verwendet wird.
	 *
	 * @return true, falls der Projektkurs als besondere Lernleistung verwendet wird
	 */
	public boolean istProjektKursBesondereLernleistung() {
		return (GostBesondereLernleistung.PROJEKTKURS.is(abidaten.besondereLernleistung));
	}



	/**
	 * Bestimmt die Fachbelegung des Faches mit der angegebenen ID
	 *
	 * @param fachID   die ID des Faches
	 *
	 * @return die Fachbelegung oder null, falls keine vorhanden ist
	 */
	public AbiturFachbelegung getFachbelegungByID(final long fachID) {
		return mapFachbelegungByFachID.get(fachID);
	}


	/**
	 * Bestimmt die erste Fachbelegung mit dem angegebenen Statistik-Kürzel
	 *
	 * @param kuerzel          das Kürzel des Faches, kann null sein (dann wird auch null zurückgegeben)
	 *
	 * @return die Fachbelegung oder null, falls keine vorhanden ist
	 */
	public AbiturFachbelegung getFachbelegungByKuerzel(final String kuerzel) {
		if ((kuerzel == null) || ("".equals(kuerzel)))
			return null;
		final @NotNull List<AbiturFachbelegung> fachbelegungen = abidaten.fachbelegungen;
		for (final AbiturFachbelegung fb : fachbelegungen) {
			final GostFach fach = getFach(fb);
			if ((fach != null) && (kuerzel.equals(fach.kuerzel)))
				return fb;
		}
		return null;
	}


	/**
	 * Liefert alle Fachbelegungen der Abiturdaten, welche den angegebenen Fachbereichen zuzuordnen sind.
	 * Wird kein Fachbereich angegeben, so werden alle Fachbelegungen der Abiturdaten zurückgegeben.
	 *
	 * @param fachbereiche   die Fachbereiche
	 *
	 * @return eine Liste der Fachbelegungen aus den Fachbereichen
	 */
	public @NotNull List<AbiturFachbelegung> getFachbelegungen(final GostFachbereich... fachbereiche) {
		if ((fachbereiche == null) || (fachbereiche.length == 0))
			return abidaten.fachbelegungen;
		final @NotNull ArrayList<AbiturFachbelegung> result = new ArrayList<>();
		for (final GostFachbereich fachbereich : fachbereiche) {
			final List<AbiturFachbelegung> fachbelegungen = mapFachbereiche.get(fachbereich);
			if (fachbelegungen == null)
				continue;
			result.addAll(fachbelegungen);
		}
		return result;
	}


	/**
	 * Liefert alle Prüfungsordnungs-relevanten Fachbelegungen der Abiturdaten, welche den angegebenen Fachbereichen
	 * zuzuordnen sind.
	 * Wird kein Fachbereich angegeben, so werden alle Fachbelegungen der Abiturdaten zurückgegeben.
	 *
	 * @param fachbereiche   die Fachbereiche
	 *
	 * @return eine Liste der Fachbelegungen aus den Fachbereichen
	 */
	public @NotNull List<AbiturFachbelegung> getRelevanteFachbelegungen(final GostFachbereich... fachbereiche) {
		if ((fachbereiche == null) || (fachbereiche.length == 0))
			return abidaten.fachbelegungen;
		final @NotNull ArrayList<AbiturFachbelegung> result = new ArrayList<>();
		for (final GostFachbereich fachbereich : fachbereiche) {
			final List<AbiturFachbelegung> fachbelegungen = mapFachbereicheRelevant.get(fachbereich);
			if (fachbelegungen == null)
				continue;
			result.addAll(fachbelegungen);
		}
		return result;
	}


	/**
	 * Liefert alle Fachbelegungen, die bilingual unterrichtet wurden.
	 *
	 * @return eine Liste der Fachbelegungen
	 */
	public @NotNull List<AbiturFachbelegung> getFachbelegungenBilingual() {
		final @NotNull List<AbiturFachbelegung> result = new ArrayList<>();
		final @NotNull List<AbiturFachbelegung> fachbelegungen = abidaten.fachbelegungen;
		for (final AbiturFachbelegung fb : fachbelegungen) {
			if (zaehleBelegung(fb) <= 0)
				continue;
			final GostFach fach = getFach(fb);
			if ((fach != null) && (!GostFachbereich.FREMDSPRACHE.hat(fach)) && (!GostFachbereich.DEUTSCH.hat(fach))
					&& (fach.biliSprache != null) && (!"D".equals(fach.biliSprache)))
				result.add(fb);
		}
		return result;
	}



	/**
	 * Filtert die Fachbelegungen auf neu einsetzende Fremdsprachen.
	 *
	 * @param fachbelegungen   die zu filternden Fachbelegungen
	 *
	 * @return die gefilterten Fachbelegungen
	 */
	public @NotNull List<AbiturFachbelegung> filterFremdspracheNeuEinsetzend(final List<AbiturFachbelegung> fachbelegungen) {
		if (fachbelegungen == null)
			return Collections.emptyList();
		final @NotNull ArrayList<AbiturFachbelegung> result = new ArrayList<>();
		for (final AbiturFachbelegung fb : fachbelegungen) {
			final GostFach fach = getFach(fb);
			if ((fach != null) && fach.istFremdsprache && fach.istFremdSpracheNeuEinsetzend)
				result.add(fb);
		}
		return result;
	}


	/**
	 * Filtert die Fachbelegungen auf fortgeführte Fremdsprachen.
	 *
	 * @param fachbelegungen   die zu filternden Fachbelegungen
	 *
	 * @return die gefilterten Fachbelegungen
	 */
	public @NotNull List<AbiturFachbelegung> filterFremdspracheFortgefuehrt(final List<AbiturFachbelegung> fachbelegungen) {
		if (fachbelegungen == null)
			return Collections.emptyList();
		final @NotNull ArrayList<AbiturFachbelegung> result = new ArrayList<>();
		for (final AbiturFachbelegung fb : fachbelegungen) {
			final GostFach fach = getFach(fb);
			if ((fach != null) && fach.istFremdsprache && !fach.istFremdSpracheNeuEinsetzend)
				result.add(fb);
		}
		return result;
	}


	/**
	 * Filtert die Fachbelegungen danach, ob sie durchgehend belegbar sind
	 *
	 * @param fachbelegungen   die zu filternden Fachbelegungen
	 *
	 * @return die gefilterten Fachbelegungen
	 */
	public @NotNull List<AbiturFachbelegung> filterDurchgehendBelegbar(final List<AbiturFachbelegung> fachbelegungen) {
		final @NotNull ArrayList<AbiturFachbelegung> result = new ArrayList<>();
		if (fachbelegungen == null)
			return result;
		for (final AbiturFachbelegung fb : fachbelegungen) {
			final GostFach fach = getFach(fb);
			if (GostFachUtils.istDurchgehendBelegbarBisQ22(fach))
				result.add(fb);
		}
		return result;
	}


	/**
	 * Filtert die Fachbelegungen. Es werden nur Fachbelegungen behalten, die in den angegebenen Halbjahren eine Belegung aufweisen.
	 * Wird kein Halbjahr angegeben, so wird nichts gefiltert, da kein Halbjahr geprüft werden muss.
	 *
	 * @param fachbelegungen    die zu filternden Fachbelegungen
	 * @param halbjahre         die Halbjahre, die belegt sein müssen
	 *
	 * @return die gefilterten Fachbelegungen
	 */
	public @NotNull List<AbiturFachbelegung> filterBelegungen(final List<AbiturFachbelegung> fachbelegungen,
			final GostHalbjahr... halbjahre) {
		if (fachbelegungen == null)
			return Collections.emptyList();
		final @NotNull ArrayList<AbiturFachbelegung> result = new ArrayList<>();
		for (final AbiturFachbelegung fb : fachbelegungen) {
			if (pruefeBelegung(fb, halbjahre))
				result.add(fb);
		}
		return result;
	}


	/**
	 * Bestimmt die Menge der unterschiedlichen Statistik-Fächer in der angegebenen Fachbelegungen.
	 *
	 * @param fachbelegungen   die Fachbelegungen
	 *
	 * @return die Menge der Statistik-Fächer
	 */
	private @NotNull Set<Fach> getMengeStatistikFaecher(final @NotNull List<AbiturFachbelegung> fachbelegungen) {
		final @NotNull HashSet<Fach> faecher = new HashSet<>();
		for (final AbiturFachbelegung fb : fachbelegungen) {
			final GostFach fach = faecherManager.get(fb.fachID);
			if (fach == null)
				continue;
			faecher.add(Fach.getBySchluesselOrDefault(fach.kuerzel));
		}
		return faecher;
	}


	/**
	 * Diese Methode zählt die Anzahl der angegebenen Fachbelegungen, welche in allen
	 * Halbjahren belegt sind. Dabei werden Fachbelegungen, welche dem gleichem Statistik-Fach
	 * zuzuordnen sind zusammengefasst. Dies ist bei der Abwahl von bilingualen Sachfächern
	 * relevant.
	 *
	 * @param fachbelegungen   die zu zählenden Fachbelegungen
	 *
	 * @return die Anzahl der Fachbelegungen
	 */
	public int zaehleBelegungenDurchgaengig(final List<AbiturFachbelegung> fachbelegungen) {
		if (fachbelegungen == null)
			return 0;
		// Bestimme zunächst die Menge der unterschiedlichen Statistik-Fächer
		final @NotNull Set<Fach> faecher = getMengeStatistikFaecher(fachbelegungen);
		// Bestimme nun die Anzahl der Fachbelegungen, die in den Halbjahren existieren.
		int count = 0;
		for (final Fach zulFach : faecher) {
			boolean vorhanden = true;
			for (final GostHalbjahr halbjahr : GostHalbjahr.values()) {
				boolean belegung_vorhanden = false;
				for (final AbiturFachbelegung fb : fachbelegungen) {
					final GostFach fbFach = faecherManager.get(fb.fachID);
					if (fbFach == null)
						continue;
					final @NotNull Fach fbZulFach = Fach.getBySchluesselOrDefault(fbFach.kuerzel);
					final AbiturFachbelegungHalbjahr belegungHalbjahr = fb.belegungen[halbjahr.id];
					if ((zulFach == fbZulFach) && (belegungHalbjahr != null) && (!istNullPunkteBelegungInQPhase(belegungHalbjahr))) {
						belegung_vorhanden = true;
						break;
					}
				}
				if (!belegung_vorhanden) {
					vorhanden = false;
					break;
				}
			}
			if (vorhanden)
				count++;
		}
		return count;
	}


	private boolean istBelegungDurchgaengigSchriftlichInQPhase(final @NotNull Fach zulFach, final @NotNull GostHalbjahr halbjahr,
			final @NotNull AbiturFachbelegung fb) {
		final GostFach fbFach = faecherManager.get(fb.fachID);
		if (fbFach == null)
			return false;
		final @NotNull Fach fbZulFach = Fach.getBySchluesselOrDefault(fbFach.kuerzel);
		if (zulFach == fbZulFach) {
			final AbiturFachbelegungHalbjahr belegungHalbjahr = fb.belegungen[halbjahr.id];
			if ((belegungHalbjahr != null) && (!istNullPunkteBelegungInQPhase(belegungHalbjahr))) {
				boolean istSchriftlichkeitOK = true;
				if (((halbjahr == GostHalbjahr.Q11) || (halbjahr == GostHalbjahr.Q12) || (halbjahr == GostHalbjahr.Q21))
						&& (!belegungHalbjahr.schriftlich))
					istSchriftlichkeitOK = false;
				if (istSchriftlichkeitOK)
					return true;
			}
		}
		return false;
	}


	/**
	 * Diese Methode zählt die Anzahl der angegebenen Fachbelegungen, welche in allen
	 * Halbjahren belegt sind. Dabei werden Fachbelegungen, welche dem gleichem Statistik-Fach
	 * zuzuordnen sind zusammengefasst. Dies ist bei der Abwahl von bilingualen Sachfächern
	 * relevant.
	 *
	 * @param fachbelegungen   die zu zählenden Fachbelegungen
	 *
	 * @return die Anzahl der Fachbelegungen
	 */
	public int zaehleBelegungenDurchgaengigSchriftlichInQPhase(final List<AbiturFachbelegung> fachbelegungen) {
		if (fachbelegungen == null)
			return 0;
		// Bestimme zunächst die Menge der unterschiedlichen Statistik-Fächer
		final @NotNull Set<Fach> faecher = getMengeStatistikFaecher(fachbelegungen);
		// Bestimme nun die Anzahl der Fachbelegungen, die in den Halbjahren existieren.
		int count = 0;
		for (final Fach zulFach : faecher) {
			boolean vorhanden = true;
			for (final GostHalbjahr halbjahr : GostHalbjahr.values()) {
				boolean belegung_vorhanden = false;
				for (final AbiturFachbelegung fb : fachbelegungen) {
					if (istBelegungDurchgaengigSchriftlichInQPhase(zulFach, halbjahr, fb)) {
						belegung_vorhanden = true;
						break;
					}
				}
				if (!belegung_vorhanden) {
					vorhanden = false;
					break;
				}
			}
			if (vorhanden)
				count++;
		}
		return count;
	}


	/**
	 * Filtert die Fachbelegungen. Es werden nur Belegungen behalten, die in den angegebenen Halbjahren die geforderte Schriftlichkeit aufweisen.
	 * Wird kein Halbjahr angegeben, so wird nichts gefiltert, da kein Halbjahr geprüft werden muss.
	 *
	 * @param fachbelegungen    die zu filternden Fachbelegungen
	 * @param schriftlichkeit   die geforderte Schriftlichkeit
	 * @param halbjahre         die Halbjahre, die belegt sein müssen
	 *
	 * @return die gefilterten Fachbelegungen
	 */
	public @NotNull List<AbiturFachbelegung> filterBelegungenMitSchriftlichkeit(final List<AbiturFachbelegung> fachbelegungen,
			final @NotNull GostSchriftlichkeit schriftlichkeit, final GostHalbjahr... halbjahre) {
		if (fachbelegungen == null)
			return Collections.emptyList();
		final @NotNull ArrayList<AbiturFachbelegung> result = new ArrayList<>();
		for (final AbiturFachbelegung fb : fachbelegungen) {
			if (pruefeBelegungMitSchriftlichkeit(fb, schriftlichkeit, halbjahre))
				result.add(fb);
		}
		return result;
	}


	/**
	 * Liefert die erste Fachbelegung für den Fachbereich - sofern eine existiert
	 *
	 * @param fachbereich   der Fachbereich
	 *
	 * @return die Fachbelegung oder null
	 */
	public AbiturFachbelegung getFachbelegung(final @NotNull GostFachbereich fachbereich) {
		final ArrayList<AbiturFachbelegung> faecher = mapFachbereiche.get(fachbereich);
		if ((faecher == null) || (faecher.isEmpty()))
			return null;
		return faecher.get(0);
	}


	/**
	 * Liefert die erste Prüfungsordnungs-relevante Fachbelegung für den Fachbereich - sofern eine existiert
	 *
	 * @param fachbereich   der Fachbereich
	 *
	 * @return die Fachbelegung oder null
	 */
	public AbiturFachbelegung getRelevanteFachbelegung(final @NotNull GostFachbereich fachbereich) {
		final ArrayList<AbiturFachbelegung> faecher = mapFachbereicheRelevant.get(fachbereich);
		if ((faecher == null) || (faecher.isEmpty()))
			return null;
		return faecher.get(0);
	}


	/**
	 * Liefert alle Fachbelegungen mit dem angegebenen Statistk-Kürzel des Faches
	 *
	 * @param kuerzel   das Kürzel des Faches
	 *
	 * @return eine Liste mit den Fachbelegungen
	 */
	public @NotNull List<AbiturFachbelegung> getFachbelegungByFachkuerzel(final String kuerzel) {
		final @NotNull List<AbiturFachbelegung> fachbelegungen = new ArrayList<>();
		if (kuerzel == null)
			return fachbelegungen;
		final @NotNull List<AbiturFachbelegung> tmpFachbelegungen = abidaten.fachbelegungen;
		for (final AbiturFachbelegung fachbelegung : tmpFachbelegungen) {
			final GostFach fach = faecherManager.get(fachbelegung.fachID);
			if ((fach == null) || (!kuerzel.equals(fach.kuerzel)))
				continue;
			fachbelegungen.add(fachbelegung);
		}
		return fachbelegungen;
	}


	/**
	 * Prüft, ob der Kurs in dem angegebenen Halbjahr mit der angegebenen Schriftlichkeit belegt ist
	 * und gibt ggf. die Belegung zurück.
	 *
	 * @param fachbelegung   die Abiturfachbelegung aus welcher die Belegungsinformationen für das Halbjahr entnommen wird
	 * @param halbjahr       das Halbjahr, in welchem die Belegung gesucht wird.
	 * @param schriftlich    gibt an, ob das Fach schriftlich oder mündlich belegt sein muss
	 *
	 * @return die Belegungsinformationen zu dem Fach
	 */
	public AbiturFachbelegungHalbjahr getBelegungHalbjahr(final @NotNull AbiturFachbelegung fachbelegung, final @NotNull GostHalbjahr halbjahr,
			final @NotNull GostSchriftlichkeit schriftlich) {
		final AbiturFachbelegungHalbjahr belegungHalbjahr = fachbelegung.belegungen[halbjahr.id];
		return ((belegungHalbjahr != null) && (!istNullPunkteBelegungInQPhase(belegungHalbjahr))
				&& ((schriftlich == GostSchriftlichkeit.BELIEBIG)
						|| ((schriftlich == GostSchriftlichkeit.SCHRIFTLICH) && (belegungHalbjahr.schriftlich))
						|| ((schriftlich == GostSchriftlichkeit.MUENDLICH) && (!belegungHalbjahr.schriftlich))))
								? belegungHalbjahr : null;
	}


	/**
	 * Liefert die erste Sprachbelegung - sofern eine existiert
	 *
	 * @param sprache   das einstellige Kürzel der Sprache
	 *
	 * @return die Fachbelegung für die Sprache
	 */
	public AbiturFachbelegung getSprachbelegung(final String sprache) {
		if (sprache == null)
			return null;
		final @NotNull List<AbiturFachbelegung> fachbelegungen = abidaten.fachbelegungen;
		for (final AbiturFachbelegung fb : fachbelegungen) {
			final GostFach fach = getFach(fb);
			if ((fach == null) || (!GostFachUtils.istFremdsprachenfach(fach, sprache)))
				continue;
			if (sprache.equals(GostFachUtils.getFremdsprache(fach)))
				return fb;
		}
		return null;
	}


	/**
	 * Prüft, ob das Fach mit der übergebenen ID als Abiturfach möglich ist und mit welcher Kursart
	 * dieses Fach aufgrund der Belegungen im Abitur gewählt werden kann.
	 *
	 * @param id   die ID des Fachs
	 *
	 * @return die mögliche Kursart des Faches im Abitur oder null, falls das Fach nicht als Abiturfach
	 *         ausgewählt werden kann.
	 */
	public GostKursart getMoeglicheKursartAlsAbiturfach(final long id) {
		if (nutzeExperimentellenCode(this.servermode, abidaten.abiturjahr)) // Führe ggf. den experimentellen Code aus
			return _getAbi30MoeglicheKursartAlsAbiturfach(id);
		final GostFach fach = faecherManager.get(id);
		if ((fach == null) || (!fach.istPruefungsordnungsRelevant))
			return null;
		final AbiturFachbelegung belegung = getFachbelegungByID(id);
		if ((belegung == null) || (belegung.letzteKursart == null))
			return null;
		final GostKursart kursart = GostKursart.fromKuerzel(belegung.letzteKursart);
		if ((kursart == null) || ((kursart == GostKursart.LK) && (!fach.istMoeglichAbiLK))
				|| ((kursart == GostKursart.GK) && (!fach.istMoeglichAbiGK))
				|| ((kursart != GostKursart.GK) && (kursart != GostKursart.LK)))
			return null;
		// LK ?
		if (kursart == GostKursart.LK)
			return pruefeBelegungMitKursart(belegung, kursart, GostHalbjahr.Q11, GostHalbjahr.Q12, GostHalbjahr.Q21, GostHalbjahr.Q22)
					? kursart : null;
		// GK ?
		if (belegung.belegungen[GostHalbjahr.Q22.id] == null)
			return null;
		// Bestimme die Fachbelegungen für das Fach bzw. Fächer mit gleichem Statistikkürzel bzw. mit den anderen Konfessionen
		// im Falle einer Religion, da ein Konfessionswechsel ggf. auch die Belegung als Abiturfach erlaubt
		final @NotNull List<AbiturFachbelegung> fachbelegungen = GostFachbereich.RELIGION.hat(fach)
				? getFachbelegungen(GostFachbereich.RELIGION)
				: getFachbelegungByFachkuerzel(fach.kuerzel);
		return (pruefeBelegungExistiertMitSchriftlichkeit(fachbelegungen, GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.Q11)
				&& pruefeBelegungExistiertMitSchriftlichkeit(fachbelegungen, GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.Q12)
				&& pruefeBelegungExistiertMitSchriftlichkeit(fachbelegungen, GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.Q21))
						? kursart : null;
	}


	/**
	 * Experimenteller Code:
	 *
	 * Prüft, ob das Fach mit der übergebenen ID als Abiturfach möglich ist und mit welcher Kursart
	 * dieses Fach aufgrund der Belegungen im Abitur gewählt werden kann.
	 *
	 * @param id   die ID des Fachs
	 *
	 * @return die mögliche Kursart des Faches im Abitur oder null, falls das Fach nicht als Abiturfach
	 *         ausgewählt werden kann.
	 */
	private GostKursart _getAbi30MoeglicheKursartAlsAbiturfach(final long id) {
		final GostFach fach = faecherManager.get(id);
		if ((fach == null) || (!fach.istPruefungsordnungsRelevant))
			return null;
		final AbiturFachbelegung belegung = getFachbelegungByID(id);
		if ((belegung == null) || (belegung.letzteKursart == null))
			return null;
		final GostKursart kursart = GostKursart.fromKuerzel(belegung.letzteKursart);
		if ((kursart == null) || (kursart != GostKursart.GK) && (kursart != GostKursart.LK) && (kursart != GostKursart.PJK))
			return null;
		// LK ?
		if (kursart == GostKursart.LK) {
			if (!fach.istMoeglichAbiLK)
				return null;
			return pruefeBelegungMitKursart(belegung, kursart, GostHalbjahr.Q11, GostHalbjahr.Q12, GostHalbjahr.Q21, GostHalbjahr.Q22)
					? kursart : null;
		}
		// PJK ?
		if (kursart == GostKursart.PJK) {
			if (!fach.istMoeglichAbiGK)
				return null;
			// Prüfe Belegung des Projektkurses
			if (!pruefeBelegungMitKursart(belegung, kursart, GostHalbjahr.Q21, GostHalbjahr.Q22))
				return null;
			// Prüfe die Belegung des Leitfaches und dessen Schriftlichkeit
			final AbiturFachbelegung leitfach = getFachbelegungByKuerzel(fach.projektKursLeitfach1Kuerzel);
			if (!pruefeBelegungMitKursart(leitfach, GostKursart.GK, GostHalbjahr.EF1, GostHalbjahr.EF2, GostHalbjahr.Q11, GostHalbjahr.Q12))
				return null;
			if (!pruefeBelegungMitSchriftlichkeit(leitfach, GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.Q11, GostHalbjahr.Q12))
				return null;
			return kursart;
		}
		// GK ?
		if (!fach.istMoeglichAbiGK)
			return null;
		if (belegung.belegungen[GostHalbjahr.Q22.id] == null)
			return null;
		// Bestimme die Fachbelegungen für das Fach bzw. Fächer mit gleichem Statistikkürzel bzw. mit den anderen Konfessionen
		// im Falle einer Religion, da ein Konfessionswechsel ggf. auch die Belegung als Abiturfach erlaubt
		final @NotNull List<AbiturFachbelegung> fachbelegungen = GostFachbereich.RELIGION.hat(fach)
				? getFachbelegungen(GostFachbereich.RELIGION)
				: getFachbelegungByFachkuerzel(fach.kuerzel);
		return (pruefeBelegungExistiertMitSchriftlichkeit(fachbelegungen, GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.Q11)
				&& pruefeBelegungExistiertMitSchriftlichkeit(fachbelegungen, GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.Q12)
				&& pruefeBelegungExistiertMitSchriftlichkeit(fachbelegungen, GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.Q21))
						? kursart : null;
	}


	/**
	 * Liefert für die übergebene Fachbelegung die Halbjahre, in denen das Fach mit einer der angebenen
	 * Kursarten belegt wurde. Ist keine Kursart angegeben, so werden die Halbjahre aller Belegungen
	 * zurückgegeben. Ist keine Fachbelegung angegeben, so wird eine leere Liste zurückgegeben.
	 *
	 * @param fachbelegung   die Fachbelegung
	 * @param kursarten      die Kursarten
	 *
	 * @return eine Liste der Halbjahre in den das Fach mit einer der Kursarten belegt wurde
	 */
	public @NotNull List<GostHalbjahr> getHalbjahreKursart(final AbiturFachbelegung fachbelegung, final GostKursart... kursarten) {
		final @NotNull ArrayList<GostHalbjahr> halbjahre = new ArrayList<>();
		if (fachbelegung != null) {
			for (final AbiturFachbelegungHalbjahr belegungHalbjahr : fachbelegung.belegungen) {
				if ((belegungHalbjahr == null) || (istNullPunkteBelegungInQPhase(belegungHalbjahr)))
					continue;
				final GostHalbjahr halbjahr = GostHalbjahr.fromKuerzel(belegungHalbjahr.halbjahrKuerzel);
				if (halbjahr == null)
					continue;
				if ((kursarten == null) || (kursarten.length == 0)) {
					halbjahre.add(halbjahr);
					continue;
				}
				for (final GostKursart kursart : kursarten) {
					if (kursart.equals(GostKursart.fromKuerzel(belegungHalbjahr.kursartKuerzel))) {
						halbjahre.add(halbjahr);
						break;
					}
				}
			}
		}
		return halbjahre;
	}



	/**
	 * Gibt die Sprache des bilingualen Bildungsgang zurück oder null, falls keiner gewählt wurde.
	 *
	 * @return die Sprache des bilingualen Bildungsgang oder null
	 */
	public String getBiligualenBildungsgang() {
		return abidaten.bilingualeSprache;
	}


	/**
	 * Prüft bei der Sprachenfolge, ob eine laut Sprachenfolge fortgeführte
	 * Fremdsprache fehlerhafterweise als neu einsetzende Fremdsprache belegt wurde.
	 * Übergebene Fachbelegungen, die keine Fremdsprachen sind werden ignoriert.
	 *
	 * @param fremdsprachen   die zu prüfenden Fachbelegungen
	 *
	 * @return true, falls eine fortgeführte Fremdsprache bei den übergebenen
	 *         Fachbelegungen existiert, ansonsten false
	 */
	public boolean hatFortgefuehrteFremdspracheInSprachendaten(final List<AbiturFachbelegung> fremdsprachen) {
		if (fremdsprachen == null)
			return false;
		if (abidaten.sprachendaten == null)
			return false;
		for (final AbiturFachbelegung fs : fremdsprachen) {
			final GostFach fach = getFach(fs);
			if ((fach == null) || (!fach.istFremdsprache))
				continue;
			if (SprachendatenUtils.istFortfuehrbareSpracheInGOSt(abidaten.sprachendaten, GostFachUtils.getFremdsprache(fach))) {
				return true;
			}
		}
		return false;
	}


	/**
	 * Prüft bei der Sprachenfolge, ob für eine laut Sprachenfolge neu einsetzende
	 * Fremdsprache fehlerhafterweise ein Kurs in einer fortgeführten Fremdsprache belegt wurde.
	 * Übergebene Fachbelegungen, die keine Fremdsprachen sind werden ignoriert.
	 *
	 * @param fremdsprachen   die zu prüfenden Fachbelegungen
	 *
	 * @return true, falls eine neu einsetzende Fremdsprache bei den übergebenen
	 *         Fachbelegungen existiert, ansonsten false
	 */
	public boolean hatNeuEinsetzendeFremdspracheInSprachendaten(final List<AbiturFachbelegung> fremdsprachen) {
		if (fremdsprachen == null)
			return false;
		if (abidaten.sprachendaten == null)
			return false;
		for (final AbiturFachbelegung fs : fremdsprachen) {
			final GostFach fach = getFach(fs);
			if ((fach == null) || (!fach.istFremdsprache))
				continue;
			if (SprachendatenUtils.istNeueinsetzbareSpracheInGOSt(abidaten.sprachendaten, GostFachUtils.getFremdsprache(fach))) {
				return true;
			}
		}
		return false;
	}


	/**
	 * Prüft, ob die Belegung seit der EF1 vorhanden ist. Hierbei werden
	 * Zusatz-, Vertiefungs- und Projektkurse auch als später einsetzend akzeptiert.
	 * Dies gilt auch für Literatur, instrumental- und vokal-praktische Kurse sowie
	 * für Religion und Philosophie.
	 *
	 * @param fachbelegung   die Abiturfachbelegungen, die geprüft werden
	 *
	 * @return true, falls das Fach seit EF1 durchgängig belegt wurde oder eine der Ausnahmen zutrifft, sonst false
	 */
	public boolean istBelegtSeitEF(final @NotNull AbiturFachbelegung fachbelegung) {
		final GostFach fach = getFach(fachbelegung);
		if (fach == null)
			return false;
		if (GostFachbereich.LITERARISCH_KUENSTLERISCH_ERSATZ.hat(fach))
			return true;
		if (GostFachbereich.RELIGION.hat(fach))
			return true;
		if ("PL".equals(fach.kuerzel))
			return true;
		for (final AbiturFachbelegungHalbjahr belegungHalbjahr : fachbelegung.belegungen) {
			if ((belegungHalbjahr == null) || (istNullPunkteBelegungInQPhase(belegungHalbjahr)))
				continue;
			final GostHalbjahr halbjahr = GostHalbjahr.fromKuerzel(belegungHalbjahr.halbjahrKuerzel);
			final GostKursart kursart = GostKursart.fromKuerzel(belegungHalbjahr.kursartKuerzel);
			if ((halbjahr == null) || (kursart == null))
				continue;
			if ((kursart == GostKursart.ZK) || (kursart == GostKursart.PJK) || (kursart == GostKursart.VTF))
				continue;
			// Prüfe, ob das vorige Halbjahr auch belegt wurde
			final GostHalbjahr prevHalbjahr = halbjahr.previous();
			if (prevHalbjahr == null)
				continue;
			final AbiturFachbelegungHalbjahr belegungHalbjahrVorher = fachbelegung.belegungen[prevHalbjahr.id];
			if ((belegungHalbjahrVorher == null) || (istNullPunkteBelegungInQPhase(belegungHalbjahrVorher))) {
				// Prüfe, ob eine Belegung des gleichen Statistik-Faches im Halbjahr zuvor existiert - dies kann bei bilingualen Fächern auftreten
				final List<AbiturFachbelegung> alleBelegungen = getFachbelegungByFachkuerzel(fach.kuerzel);
				if ((alleBelegungen == null) || (alleBelegungen.size() <= 1))
					return false;
				if (!pruefeBelegungExistiert(alleBelegungen, prevHalbjahr))
					return false;
			}
		}
		return true;
	}


	/**
	 * Gibt zurück, ob ein Zusatzkurs in Geschichte in diesem Jahrgang angeboten
	 * wird oder nicht.
	 *
	 * @return true, wenn einer angeboten wird und ansonsten false
	 */
	public boolean istErlaubtZusatzkursGE() {
		if (_jahrgangsdaten == null)
			return true; // Default: Zusatzkurse werden angeboten
		return _jahrgangsdaten.hatZusatzkursGE;
	}


	/**
	 * Gibt das erste Halbjahr für Zusatzkurse in Geschichte
	 * in diesem Jahrgang zurück.
	 *
	 * @return das erste Halbjahr für Zusatzkurse in Geschichte
	 */
	public @NotNull GostHalbjahr getBeginnZusatzkursGE() {
		if (_jahrgangsdaten == null)
			return GostHalbjahr.Q21; // Default: Zusatzkurse in der Q2
		final GostHalbjahr hj = GostHalbjahr.fromKuerzel(_jahrgangsdaten.beginnZusatzkursGE);
		if (hj == null)
			return GostHalbjahr.Q21; // Default: Zusatzkurse in der Q2
		return hj;
	}


	/**
	 * Gibt zurück, ob ein Zusatzkurs in Sozialwissenschaften in diesem Jahrgang
	 * angeboten wird oder nicht.
	 *
	 * @return true, wenn einer angeboten wird und ansonsten false
	 */
	public boolean istErlaubtZusatzkursSW() {
		if (_jahrgangsdaten == null)
			return true; // Default: Zusatzkurse werden angeboten
		return _jahrgangsdaten.hatZusatzkursSW;
	}


	/**
	 * Gibt das erste Halbjahr für Zusatzkurse in Sozialwissenschaften
	 * in diesem Jahrgang zurück.
	 *
	 * @return das erste Halbjahr für Zusatzkurse in Sozialwissenschaften
	 */
	public @NotNull GostHalbjahr getBeginnZusatzkursSW() {
		if (_jahrgangsdaten == null)
			return GostHalbjahr.Q21; // Default: Zusatzkurse in der Q2
		final GostHalbjahr hj = GostHalbjahr.fromKuerzel(_jahrgangsdaten.beginnZusatzkursSW);
		if (hj == null)
			return GostHalbjahr.Q21; // Default: Zusatzkurse in der Q2
		return hj;
	}


	/**
	 * Gibt alle Fachkombination zurück, welche in der EF.1 gültig sind.
	 *
	 * @return die Liste mit den Fachkombinationen
	 */
	public @NotNull List<GostJahrgangFachkombination> getFachkombinationenEF1() {
		final @NotNull List<GostJahrgangFachkombination> kombis = new ArrayList<>();
		for (final @NotNull GostJahrgangFachkombination kombi : faecherManager.getFachkombinationen())
			if (kombi.gueltigInHalbjahr[GostHalbjahr.EF1.id])
				kombis.add(kombi);
		return kombis;
	}


	/**
	 * Gibt alle Fachkombination zurück.
	 *
	 * @return die Liste mit den Fachkombinationen
	 */
	public @NotNull List<GostJahrgangFachkombination> getFachkombinationenGesamt() {
		return faecherManager.getFachkombinationen();
	}


	/**
	 * Gibt das Ergebnis der Belegprüfung zurück. Dieses enthält eine Liste der Fehler, die bei der Belegprüfung
	 * festgestellt wurden und ob diese erfolgreich gewesen ist oder nicht.
	 *
	 * @return das Ergebnis der Belegprüfung
	 */
	public @NotNull GostBelegpruefungErgebnis getBelegpruefungErgebnis() {
		final @NotNull GostBelegpruefungErgebnis ergebnis = new GostBelegpruefungErgebnis();
		ergebnis.erfolgreich = belegpruefungErfolgreich;
		for (int i = 0; i < belegpruefungsfehler.size(); i++) {
			final @NotNull GostBelegungsfehler fehler = belegpruefungsfehler.get(i);
			ergebnis.fehlercodes.add(new GostBelegpruefungErgebnisFehler(fehler, pruefungsArt));
		}
		// TODO Ergänze das Ergebnis um einen Log der Belegprüfung
		return ergebnis;
	}

	/**
	 * Gibt das Belegprüfungs-Objekt für die KurszahlenUndWochenstunden zurück, welches für
	 * die Belegprüfung genutzt wurde und die Statistiken dazu erstellt hat.
	 *
	 * @return das Belegprüfungs-Objekt für die KurszahlenUndWochenstunden
	 */
	private @NotNull KurszahlenUndWochenstunden getKurszahlenUndWochenstunden() {
		if (this.belegpruefungKurszahlenUndWochenstunden == null)
			throw new NullPointerException("Die Belegprüfung zu Kurszahlen und Wochenstunden wurde noch nicht erstellt und durchgeführt.");
		return this.belegpruefungKurszahlenUndWochenstunden;
	}

	/**
	 * Experimenteller Code:
	 * Gibt das Belegprüfungs-Objekt für die KurszahlenUndWochenstunden zurück, welches für
	 * die Belegprüfung genutzt wurde und die Statistiken dazu erstellt hat.
	 *
	 * @return das Belegprüfungs-Objekt für die KurszahlenUndWochenstunden
	 */
	private @NotNull Abi30BelegpruefungKurszahlenUndWochenstunden getAbi30KurszahlenUndWochenstunden() {
		if (this.abi30BelegpruefungKurszahlenUndWochenstunden == null)
			throw new NullPointerException("Die Belegprüfung zu Kurszahlen und Wochenstunden wurde noch nicht erstellt und durchgeführt.");
		return this.abi30BelegpruefungKurszahlenUndWochenstunden;
	}


	/**
	 * Berechnet die Wochenstunden, welche von dem Schüler in den einzelnen
	 * Halbjahren der gymnasialen Oberstufe für das Abitur relevant belegt wurden.
	 *
	 * @return ein Array mit den Wochenstunden für die sechs Halbjahre
	 */
	public @NotNull int[] getWochenstunden() {
		if (nutzeExperimentellenCode(this.servermode, abidaten.abiturjahr)) { // Führe ggf. den experimentellen Code aus
			final @NotNull Abi30BelegpruefungKurszahlenUndWochenstunden kuw = getAbi30KurszahlenUndWochenstunden();
			final @NotNull int[] stunden = new int[] { 0, 0, 0, 0, 0, 0 };
			for (final GostHalbjahr hj : GostHalbjahr.values())
				stunden[hj.id] = kuw.getWochenstunden(hj);
			return stunden;
		}
		final @NotNull KurszahlenUndWochenstunden kuw = getKurszahlenUndWochenstunden();
		final @NotNull int[] stunden = new int[] { 0, 0, 0, 0, 0, 0 };
		for (final GostHalbjahr hj : GostHalbjahr.values())
			stunden[hj.id] = kuw.getWochenstunden(hj);
		return stunden;
	}


	/**
	 * Gibt die Anzahl der Wochenstunden für die Einführungsphase zurück.
	 *
	 * @return die Anzahl der Wochenstunden
	 */
	public int getWochenstundenEinfuehrungsphase() {
		if (nutzeExperimentellenCode(this.servermode, abidaten.abiturjahr)) { // Führe ggf. den experimentellen Code aus
			final @NotNull Abi30BelegpruefungKurszahlenUndWochenstunden kuw = getAbi30KurszahlenUndWochenstunden();
			return kuw.getWochenstundenEinfuehrungsphase();
		}
		final @NotNull KurszahlenUndWochenstunden kuw = getKurszahlenUndWochenstunden();
		return kuw.getWochenstundenEinfuehrungsphase();
	}


	/**
	 * Gibt die Anzahl der Wochenstunden für die Qualifikationsphase zurück.
	 *
	 * @return die Anzahl der Wochenstunden
	 */
	public int getWochenstundenQualifikationsphase() {
		if (nutzeExperimentellenCode(this.servermode, abidaten.abiturjahr)) { // Führe ggf. den experimentellen Code aus
			final @NotNull Abi30BelegpruefungKurszahlenUndWochenstunden kuw = getAbi30KurszahlenUndWochenstunden();
			return kuw.getWochenstundenQualifikationsphase();
		}
		final @NotNull KurszahlenUndWochenstunden kuw = getKurszahlenUndWochenstunden();
		return kuw.getWochenstundenQualifikationsphase();
	}


	/**
	 * Berechnet die Anzahl der anrechenbaren Kurse, welche von dem Schüler in den einzelnen
	 * Halbjahren der gymnasialen Oberstufe für das Abitur belegt wurden.
	 *
	 * @return ein Array mit den anrechenbaren Kursen für die sechs Halbjahre
	 */
	public @NotNull int[] getAnrechenbareKurse() {
		if (nutzeExperimentellenCode(this.servermode, abidaten.abiturjahr)) { // Führe ggf. den experimentellen Code aus
			final @NotNull Abi30BelegpruefungKurszahlenUndWochenstunden kuw = getAbi30KurszahlenUndWochenstunden();
			final @NotNull int[] anzahl = new int[] { 0, 0, 0, 0, 0, 0 };
			for (final GostHalbjahr hj : GostHalbjahr.values())
				anzahl[hj.id] = kuw.getKurszahlenAnrechenbar(hj);
			return anzahl;
		}
		final @NotNull KurszahlenUndWochenstunden kuw = getKurszahlenUndWochenstunden();
		final @NotNull int[] anzahl = new int[] { 0, 0, 0, 0, 0, 0 };
		for (final GostHalbjahr hj : GostHalbjahr.values())
			anzahl[hj.id] = kuw.getKurszahlenAnrechenbar(hj);
		return anzahl;
	}


	/**
	 * Gibt die Anzahl der anrechenbaren Kurse für den Block I des Abiturs
	 * zurück.
	 *
	 * @return die anrechenbaren Kursen für Block I des Abiturs
	 */
	public int getAnrechenbareKurseBlockI() {
		if (nutzeExperimentellenCode(this.servermode, abidaten.abiturjahr)) { // Führe ggf. den experimentellen Code aus
			final @NotNull Abi30BelegpruefungKurszahlenUndWochenstunden kuw = getAbi30KurszahlenUndWochenstunden();
			return kuw.getBlockIAnzahlAnrechenbar();
		}
		final @NotNull KurszahlenUndWochenstunden kuw = getKurszahlenUndWochenstunden();
		return kuw.getBlockIAnzahlAnrechenbar();
	}


	/**
	 * Gibt für die Notenpunkte für die Halbjahresbelegung zurück
	 *
	 * @param belegungHalbjahr   die Halbjahresbelegung
	 *
	 * @return die Notenpunkte oder null, falls das Fach in dem Halbjahr nicht gewählt wurde
	 */
	public Integer getNotenpunkteOfFachbelegungHalbjahr(final AbiturFachbelegungHalbjahr belegungHalbjahr) {
		if (belegungHalbjahr == null)
			return null;
		final NoteKatalogEintrag nke = Note.fromKuerzel(belegungHalbjahr.notenkuerzel).daten(getSchuljahr());
		return (nke == null) ? null : nke.notenpunkte;
	}


	/**
	 * Gibt für die übergebene Fach-ID und das übergebene Halbjahr die Notenpunkte zurück, sofern eine
	 * Belegung vorliegt, welche eine Note hat, und ansonsten null.
	 *
	 * @param idFach     die ID des Faches
	 * @param halbjahr   das Halbjahr
	 *
	 * @return die Notenpunkte oder null
	 */
	public Integer getNotenpunkteByFachIDAndHalbjahr(final long idFach, final @NotNull GostHalbjahr halbjahr) {
		return getNotenpunkteOfFachbelegungHalbjahr(mapFachbelegungHalbjahrByFachIDAndHalbjahrID.getOrNull(idFach, halbjahr.id));
	}


	/**
	 * Prüft, ob bei der Fachbelegung in allen Halbjahren der Qualifikationsphase eine Belegung vorliegt und
	 * der Kurs jeweils als angerechnet markiert ist.
	 *
	 * @param belegung   die Fachbelegung
	 *
	 * @return true, falls alle Kurse der Fachbelegung in der Qualifikationsphase als angerechnet markiert sind, und ansonsten false
	 */
	public boolean hatMarkierungQualifikationsphase(final AbiturFachbelegung belegung) {
		if (belegung == null)
			return false;
		for (final @NotNull GostHalbjahr halbjahr : GostHalbjahr.getQualifikationsphase()) {
			final AbiturFachbelegungHalbjahr belegungHalbjahr = belegung.belegungen[halbjahr.id];
			if ((belegungHalbjahr == null) || (belegungHalbjahr.block1gewertet == null) || (!belegungHalbjahr.block1gewertet))
				return false;
		}
		return true;
	}


	/**
	 * Prüft, ob eine Belegung eines der angegebenen Fächer mit den angegebenen Halbjahren existiert,
	 * bei dem alle Halbjahre der Qualifikationsphase markiert sind und keine 0-Punkte Kurse beinhalten.
	 * Die erste gefundene Belegung wird dann zurückgegeben.
	 * Ist keine solche Fachbelegung gegeben, so schlägt die Prüfung fehl und es wird null zurückgegeben.
	 * In dieser Methode wird ggf. auch geprüft, ob weitere Fachbelegungen existieren, welche das gleiche
	 * Statistik-Kürzel haben und Ersatzweise eine Halbjahres-Belegung ersetzen können. Dies ist z.B. bei bilingualen
	 * Fächern nötig oder bei der Unterscheidung von Sport-Profilen. In einem solchen Fall wird
	 * die erste Belegung für das Fach zurückgegeben, welches evtl. nicht alle Belegungen beinhaltet
	 *
	 * @param fachbelegungen    die zu prüfenden Fachbelegungen
	 *
	 * @return eine Fachbelegung mit den Markierung in den Halbjahren der Qualifikationsphase und ansonsten null
	 */
	public AbiturFachbelegung pruefeMarkierungExistiertDurchgaengig(final List<AbiturFachbelegung> fachbelegungen) {
		if (fachbelegungen == null)
			return null;
		for (final AbiturFachbelegung fachbelegung : fachbelegungen) {
			// Beachte alle Fachbelegungen von Fächern des gleichen Statistik-Faches - dies kann bei bilingualen Fächern wichtig sein
			final GostFach fach = faecherManager.get(fachbelegung.fachID);
			if (fach == null)
				continue;
			final List<AbiturFachbelegung> alleBelegungen = getFachbelegungByFachkuerzel(fach.kuerzel);
			if ((alleBelegungen == null) || (alleBelegungen.isEmpty()))
				continue;
			boolean hatBelegung = true;
			for (final GostHalbjahr halbjahr : GostHalbjahr.getQualifikationsphase()) {
				boolean hatHalbjahresBelegung = false;
				for (final AbiturFachbelegung aktFachbelegung : alleBelegungen) {
					final AbiturFachbelegungHalbjahr belegungHalbjahr = aktFachbelegung.belegungen[halbjahr.id];
					if ((belegungHalbjahr != null) && (!istNullPunkteBelegungInQPhase(belegungHalbjahr))
							&& (belegungHalbjahr.block1gewertet != null) && (belegungHalbjahr.block1gewertet)) {
						hatHalbjahresBelegung = true;
						break;
					}
				}
				if (!hatHalbjahresBelegung) {
					hatBelegung = false;
					break;
				}
			}
			if (hatBelegung)
				return fachbelegung;
		}
		return null;
	}


	/**
	 * Zählt die Anzahl der Markierungen bei der Fachbelegung in allen Halbjahren der Qualifikationsphase.
	 * Ist belegung null, so wird 0 zurückgegeben.
	 *
	 * @param belegung   die Fachbelegung
	 *
	 * @return die Anzahl der Markierungen bei der Fachbelegung
	 */
	public int zaehleMarkierungenQualifikationsphase(final AbiturFachbelegung belegung) {
		if (belegung == null)
			return 0;
		int count = 0;
		for (final @NotNull GostHalbjahr halbjahr : GostHalbjahr.getQualifikationsphase()) {
			final AbiturFachbelegungHalbjahr belegungHalbjahr = belegung.belegungen[halbjahr.id];
			if ((belegungHalbjahr == null) || (belegungHalbjahr.block1gewertet == null) || (!belegungHalbjahr.block1gewertet))
				continue;
			count++;
		}
		return count;
	}


	/**
	 * Zähle die Anzahl der Markierungen bei den Fachbelegungen in allen Halbjahren der Qualifikationsphase.
	 *
	 * @param belegungen   die Fachbelegungen
	 *
	 * @return die Anzahl der Markierungen bei den Fachbelegungen
	 */
	public int zaehleAlleMarkierungenQualifikationsphase(final @NotNull List<AbiturFachbelegung> belegungen) {
		if (belegungen.isEmpty())
			return 0;
		int count = 0;
		for (final @NotNull AbiturFachbelegung belegung : belegungen) {
			for (final @NotNull GostHalbjahr halbjahr : GostHalbjahr.getQualifikationsphase()) {
				final AbiturFachbelegungHalbjahr belegungHalbjahr = belegung.belegungen[halbjahr.id];
				if ((belegungHalbjahr == null) || (belegungHalbjahr.block1gewertet == null) || (!belegungHalbjahr.block1gewertet))
					continue;
				count++;
			}
		}
		return count;
	}


	/**
	 * Zähle die Anzahl der markierten Fachbelegungen ohne Wertung oder mit 0 Punkten in allen Halbjahren der Qualifikationsphase.
	 *
	 * @param belegungen   die Fachbelegungen
	 *
	 * @return die Anzahl der markierten Fachbelegungen ohne Wertung oder mit 0 Punkten bei den Fachbelegungen
	 */
	public int zaehleMarkierungenOhneWertungOderMitNullPunkten(final @NotNull List<AbiturFachbelegung> belegungen) {
		if (belegungen.isEmpty())
			return 0;
		int count = 0;
		for (final @NotNull AbiturFachbelegung belegung : belegungen) {
			for (final @NotNull GostHalbjahr halbjahr : GostHalbjahr.getQualifikationsphase()) {
				final AbiturFachbelegungHalbjahr belegungHalbjahr = belegung.belegungen[halbjahr.id];
				if ((belegungHalbjahr == null) || (belegungHalbjahr.block1gewertet == null) || (!belegungHalbjahr.block1gewertet))
					continue;
				final Integer np = getNotenpunkteOfFachbelegungHalbjahr(belegungHalbjahr);
				if ((np == null) || (np == 0))
					count++;
			}
		}
		return count;
	}


	/**
	 * Zähle die Anzahl der markierten Defizite bei den Fachbelegungen in allen Halbjahren der Qualifikationsphase.
	 *
	 * @param belegungen   die Fachbelegungen
	 *
	 * @return die Anzahl der Markierungen mit Defiziten bei den Fachbelegungen
	 */
	public int zaehleMarkierungenMitDefiziten(final @NotNull List<AbiturFachbelegung> belegungen) {
		if (belegungen.isEmpty())
			return 0;
		int count = 0;
		for (final @NotNull AbiturFachbelegung belegung : belegungen) {
			for (final @NotNull GostHalbjahr halbjahr : GostHalbjahr.getQualifikationsphase()) {
				final AbiturFachbelegungHalbjahr belegungHalbjahr = belegung.belegungen[halbjahr.id];
				if ((belegungHalbjahr == null) || (belegungHalbjahr.block1gewertet == null) || (!belegungHalbjahr.block1gewertet))
					continue;
				final Integer np = getNotenpunkteOfFachbelegungHalbjahr(belegungHalbjahr);
				if ((np != null) && (np < 5))
					count++;
			}
		}
		return count;
	}


	/**
	 * Zähle die Anzahl der nicht markierten Kurse ohne Defizite bei den Fachbelegungen in allen Halbjahren der Qualifikationsphase.
	 *
	 * @param belegungen   die Fachbelegungen
	 *
	 * @return die Anzahl der nicht markierten Kurse ohne Defiziten bei den Fachbelegungen
	 */
	public int zaehleOhneMarkierungenUndOhneDefizite(final @NotNull List<AbiturFachbelegung> belegungen) {
		if (belegungen.isEmpty())
			return 0;
		int count = 0;
		for (final @NotNull AbiturFachbelegung belegung : belegungen) {
			for (final @NotNull GostHalbjahr halbjahr : GostHalbjahr.getQualifikationsphase()) {
				final AbiturFachbelegungHalbjahr belegungHalbjahr = belegung.belegungen[halbjahr.id];
				if ((belegungHalbjahr == null) || (belegungHalbjahr.block1gewertet == null) || (!belegungHalbjahr.block1gewertet))
					continue;
				final Integer np = getNotenpunkteOfFachbelegungHalbjahr(belegungHalbjahr);
				if ((np != null) && (np >= 5))
					count++;
			}
		}
		return count;
	}


	/**
	 * Berechnet den Durchschnitte der Notenpunkte aller markierten Kurse.
	 * Dabei fließen die Notenpunkte der Leistungskurse doppelt ein.
	 *
	 * @return der Durchschnitte der Notenpunkte aller markierten Kurse
	 */
	public double berechneMarkierungenDurchschnittspunkte() {
		double summe = 0.0;
		int count = 0;
		for (final @NotNull AbiturFachbelegung belegung : this.abidaten.fachbelegungen) {
			final GostKursart kursart = GostKursart.fromKuerzel(belegung.letzteKursart);
			if (kursart == null)
				continue;
			for (final @NotNull GostHalbjahr halbjahr : GostHalbjahr.getQualifikationsphase()) {
				final AbiturFachbelegungHalbjahr belegungHalbjahr = belegung.belegungen[halbjahr.id];
				if ((belegungHalbjahr == null) || (belegungHalbjahr.block1gewertet == null) || (!belegungHalbjahr.block1gewertet))
					continue;
				final Integer np = getNotenpunkteOfFachbelegungHalbjahr(belegungHalbjahr);
				if ((np == null) || (np == 0))
					continue;
				if (kursart == GostKursart.LK) {
					summe += 2.0 * np;
					count += 2;
				} else {
					summe += np;
					count++;
				}
			}
		}
		return summe / count;
	}


	/**
	 * Prüft, ob bei der Fachbelegung in dem angegebenen Halbjahr der Qualifikationsphase eine Belegung vorliegt und
	 * der Kurs jeweils als angerechnet markiert ist.
	 *
	 * @param belegung   die Fachbelegung
	 * @param halbjahr   das Halbjahr
	 *
	 * @return true, falls alle Kurse der Fachbelegung in der Qualifikationsphase als angerechnet markiert sind, und ansonsten false
	 */
	public boolean hatMarkierungHalbjahr(final AbiturFachbelegung belegung, final @NotNull GostHalbjahr halbjahr) {
		if (belegung == null)
			return false;
		final AbiturFachbelegungHalbjahr belegungHalbjahr = belegung.belegungen[halbjahr.id];
		if ((belegungHalbjahr == null) || (belegungHalbjahr.block1gewertet == null) || (!belegungHalbjahr.block1gewertet))
			return false;
		return true;
	}


	/**
	 * Ermittelt die Informationen zu markierten Kursen in dem angegeben Halbjahr und gibt diese in einem Array
	 * zurück. Diese sind:
	 * - Notenpunktsumme mit doppelter Wertung der LKs (Index 0)
	 * - Anzahl der Kurse (Index 1)
	 * - Anzahl der Kurse mit doppelter Zählung der LKs (Index 2)
	 * - Anzahl der markierten Defizite im LK-Bereich (Index 3)
	 * - Anzahl der markierten Defizite im GK-Bereich (Index 4)
	 * - Anzahl der markierten Defizite (Index 5)
	 *
	 * @param halbjahr   das Halbjahr
	 *
	 * @return die Informationen zu den markierten Kursen
	 */
	public @NotNull int[] getKursinformationenOfMarkierteKurseByHalbjahr(final @NotNull GostHalbjahr halbjahr) {
		final List<AbiturFachbelegung> belegungen = mapFachbelegungenByGostHalbjahr.get(halbjahr);
		final int[] result = new int[6];
		result[0] = 0; // Notenpunktsumme
		result[1] = 0; // AnzahlKurse
		result[2] = 0; // AnzahlKurse mit LKs doppelt
		result[3] = 0; // Defizite im LK-Bereich
		result[4] = 0; // Defizite im GK-Bereich
		result[5] = 0; // Defizite Gesamt
		if (belegungen == null)
			return result;
		for (final @NotNull AbiturFachbelegung belegung : belegungen) {
			final AbiturFachbelegungHalbjahr belegungHalbjahr = belegung.belegungen[halbjahr.id];
			if ((belegungHalbjahr == null) || (!belegungHalbjahr.block1gewertet))
				continue;
			final boolean istLKBelegung = GostKursart.LK.kuerzel.equals(belegungHalbjahr.kursartKuerzel);
			final Integer np = getNotenpunkteOfFachbelegungHalbjahr(belegungHalbjahr);
			if ((np != null) && (np != 0)) {
				if (istLKBelegung) {
					result[0] += np * 2;
					result[1]++;
					result[2] += 2;
					if (np < 5)
						result[3]++;
				} else {
					result[0] += np;
					result[1]++;
					result[2]++;
					if (np < 5)
						result[4]++;
				}
			}
		}
		result[5] = result[3] + result[4];
		return result;
	}


	/**
	 * Gibt das Ergebnis des Markierprüfungsalgorithmus zurück. Dieses enthält, ob der Algorithmus erfolgreich gewesen ist
	 * und im Fehlerfall den Log des Prüfungsergebnisses.
	 *
	 * @return das Ergebnis der Markierprüfungsalgorithmus
	 */
	public @NotNull GostAbiturMarkierungspruefungErgebnis getErgebnisMarkierpruefung() {
		return this.markierpruefungsergebnis;
	}


	/**
	 * Führt eine Prüfung der aktuellen Markierungen der Kurse durch.
	 */
	public void pruefeMarkierungen() {
		if (!istBewertetQualifikationsPhase()) {
			markierpruefungsergebnis.erfolgreich = false;
			markierpruefungsergebnis.log.clear();
			markierpruefungsergebnis.log.add("Es liegen noch nicht Bewertungen für alle Halbjahre des Qualifikationsphase vor.");
			return;
		}
		if (nutzeExperimentellenCode(this.servermode, abidaten.abiturjahr)) {
			// Führe ggf. den experimentellen Code aus
			markierpruefungsergebnis = Abi30GostAbiturMarkierungspruefung.pruefe(this, belegpruefungen);
		} else {
			markierpruefungsergebnis = GostAbiturMarkierungspruefung.pruefe(this, belegpruefungen);
		}
	}


	/**
	 * Gibt das Ergebnis des Markierungsalgorithmus zurück. Dieses enthält, ob der Algorithmus erfolgreich gewesen ist
	 * und im Erfolgsfall ein Liste für die einzelnen Belegungen der Qualifikationsphase, ob diese markiert wurden.
	 *
	 * @return das Ergebnis des Markierungsalgorithmus
	 */
	public @NotNull GostAbiturMarkierungsalgorithmusErgebnis getErgebnisMarkierungsalgorithmus() {
		return this.markierungsErgebnis;
	}


	/**
	 * Führt die Berechnung der Zulassung und Nutzung der zuvor markierten Kurse durch.
	 */
	public void pruefeZulassung() {
		// Gehe die einzelnen Fachbelegungen durch und aktualisiere die Ergebnisse für Block I
		abidaten.block1AnzahlKurse = 0;
		abidaten.block1DefiziteGesamt = 0;
		abidaten.block1DefiziteLK = 0;
		abidaten.block1PunktSummeGK = 0;
		abidaten.block1PunktSummeLK = 0;
		for (final @NotNull AbiturFachbelegung fachbelegung : abidaten.fachbelegungen) {
			double summeKurseFach = 0.0;
			int punktSummeEinfach = 0;
			fachbelegung.block1PunktSumme = 0;
			for (final @NotNull GostHalbjahr halbjahr : GostHalbjahr.getQualifikationsphase()) {
				final AbiturFachbelegungHalbjahr belegungHalbjahr = fachbelegung.belegungen[halbjahr.id];
				if ((belegungHalbjahr == null) || (belegungHalbjahr.block1gewertet == null) || (!belegungHalbjahr.block1gewertet))
					continue;
				final boolean istLK = GostKursart.LK.kuerzel.equals(belegungHalbjahr.kursartKuerzel);
				final Note note = Note.fromKuerzel(belegungHalbjahr.notenkuerzel);
				final NoteKatalogEintrag nke = note.getKatalogEintrag(this.getSchuljahr());
				if ((nke == null) || (nke.notenpunkte == null))
					continue;
				punktSummeEinfach += nke.notenpunkte;
				final int notenpunkte = nke.notenpunkte * (istLK ? 2 : 1);
				fachbelegung.block1PunktSumme += notenpunkte;
				summeKurseFach++;
				abidaten.block1AnzahlKurse++;
				if (istLK) {
					abidaten.block1PunktSummeLK += notenpunkte;
					if (nke.notenpunkte < 5)
						abidaten.block1DefiziteLK++;
				} else {
					abidaten.block1PunktSummeGK += notenpunkte;
				}
				if (nke.notenpunkte < 5)
					abidaten.block1DefiziteGesamt++;
			}
			fachbelegung.block1NotenpunkteDurchschnitt = (summeKurseFach == 0.0) ? null : (punktSummeEinfach / summeKurseFach);
		}
		final double summeNotenpunkte = abidaten.block1PunktSummeLK + abidaten.block1PunktSummeGK;
		final double anzahlKurse = (abidaten.block1AnzahlKurse + 8.0); // LK-Belegungen doppelt zählen, also + 2*4, da auch die Notenpunkte doppelt gezählt wurden
		abidaten.block1PunktSummeNormiert = (int) Math.round((40.0 * summeNotenpunkte) / anzahlKurse);
		abidaten.block1NotenpunkteDurchschnitt = Math.round((summeNotenpunkte / anzahlKurse) * 100.0) / 100.0;
		abidaten.block1Zulassung = (abidaten.block1PunktSummeNormiert >= 200)
				&& (((abidaten.block1AnzahlKurse >= 35) && (abidaten.block1AnzahlKurse <= 37) && (abidaten.block1DefiziteGesamt <= 7))
						|| ((abidaten.block1AnzahlKurse >= 38) && (abidaten.block1DefiziteGesamt <= 8)));
		pruefeMarkierungen();
		if (!markierpruefungsergebnis.erfolgreich)
			abidaten.block1Zulassung = false;
	}


	/**
	 * Wendet das Ergebnis des Markierungsergebnis auf diese Belegung an.
	 *
	 * @return true, wenn es erfolgreich angewendet wurde, und ansonsten false
	 */
	public boolean applyErgebnisMarkierungsalgorithmus() {
		// Wenn der Algorithmus nicht erfolgreich gelaufen ist, dann sollte er auch nicht angewendet werden...
		if (!this.markierungsErgebnis.erfolgreich) {
			abidaten.block1Zulassung = null;
			return false;
		}
		// Gehe die einzelnen Markierungs-Einträge durch und wende diese die Abiturdaten an
		for (final @NotNull GostAbiturMarkierungsalgorithmusMarkierung markierung : this.markierungsErgebnis.markierungen) {
			final AbiturFachbelegung belegung = getFachbelegungByID(markierung.idFach);
			if (belegung == null)
				return false;
			final GostHalbjahr halbjahr = GostHalbjahr.fromID(markierung.idHalbjahr);
			if (halbjahr == null)
				return false;
			final AbiturFachbelegungHalbjahr belegungHalbjahr = belegung.belegungen[markierung.idHalbjahr];
			if (belegungHalbjahr == null)
				return false;
			belegungHalbjahr.block1gewertet = markierung.markiert;
		}
		pruefeZulassung();
		return true;
	}


	/**
	 * Gibt die Belegungsart für die Halbjahresbelegung zurück: Nicht belegt, mündlich oder schriftlich
	 *
	 * @param belegungHalbjahr   die Halbjahresbelegung
	 *
	 * @return die Belegungsart
	 */
	public static @NotNull AbiturBelegungsart getBelegungsartFromHalbjahresbelegung(final AbiturFachbelegungHalbjahr belegungHalbjahr) {
		if (belegungHalbjahr == null)
			return AbiturBelegungsart.NICHT_BELEGT;
		return belegungHalbjahr.schriftlich ? AbiturBelegungsart.SCHRIFTLICH : AbiturBelegungsart.MUENDLICH;
	}


	/**
	 * Gibt die Markierungsinformation für die Halbjahresbelegung zurück
	 *
	 * @param belegungHalbjahr   die Halbjahresbelegung
	 *
	 * @return die Markierungsinformation
	 */
	public static AbiturKursMarkierung getKursmarkierungFromHalbjahresbelegung(final AbiturFachbelegungHalbjahr belegungHalbjahr) {
		if (belegungHalbjahr == null)
			return null;
		final boolean istGewertet = (belegungHalbjahr.block1gewertet != null) && (belegungHalbjahr.block1gewertet);
		final boolean istKursAufZeugnis = istGewertet || ((belegungHalbjahr.block1kursAufZeugnis != null) && (belegungHalbjahr.block1kursAufZeugnis));
		return new AbiturKursMarkierung(istGewertet, istKursAufZeugnis);
	}


	/**
	 * Wandelt das Noten-Kürzel der übergebenen Halbjahresbelegung in den zugehörigen Notenpunkte-String um.
	 *
	 * @param belegungHalbjahr   die Halbjahresbelegung
	 *
	 * @return die Notenpunkte als zweistelliger String, "AT" oder null
	 */
	public String getNotenpunkteStringHalbjahresbelegung(final AbiturFachbelegungHalbjahr belegungHalbjahr) {
		if (belegungHalbjahr == null)
			return null;
		final Note tmpNote = Note.fromKuerzel(belegungHalbjahr.notenkuerzel);
		if (tmpNote == Note.ATTEST)
			return belegungHalbjahr.notenkuerzel;
		if (!tmpNote.istNote(getSchuljahr()))
			return null;
		final NoteKatalogEintrag nke = tmpNote.daten(getSchuljahr());
		if ((nke == null) || (nke.notenpunkte == null))
			return null;
		return (nke.notenpunkte < 10) ? ("0" + nke.notenpunkte) : ("" + nke.notenpunkte);
	}


	/**
	 * Wandelt das übergebene Noten-Kürzel in die zugehörigen Notepunkte um.
	 *
	 * @param kuerzel   das Notenkürzel
	 *
	 * @return die Notenpunkte oder null
	 */
	public Integer getNotenpunkteFromKuerzel(final String kuerzel) {
		return getNotenpunkteFromKuerzelInternal(kuerzel, getSchuljahr());
	}


	/**
	 * Wandelt das übergebene Noten-Kürzel in die zugehörigen Notepunkte um.
	 *
	 * @param kuerzel     das Notenkürzel
	 * @param schuljahr   das Schuljahr, in welchem die Abiturprüfung stattfindet
	 *
	 * @return die Notenpunkte oder null
	 */
	private static Integer getNotenpunkteFromKuerzelInternal(final String kuerzel, final int schuljahr) {
		final Note tmpNote = Note.fromKuerzel(kuerzel);
		if (!tmpNote.istNote(schuljahr))
			return null;
		final NoteKatalogEintrag nke = tmpNote.daten(schuljahr);
		return (nke == null) ? null : nke.notenpunkte;
	}


	/**
	 * Diese Methode berechnet das Prüfungsergebnis für Block II und das Gesamtergebnis,
	 * sofern die Daten vollständig vorliegen. Ist dies nicht der Fall, so wird das Ergebnis soweit
	 * wie möglich berechnet. Diese Methode setzt die vorherige Berechnung der Zulassung voraus.
	 *
	 * @param abidaten   die Abiturdaten, welche zur Berechnung verwendet werden
	 *
	 * @return true, wenn die Berechnung vollständig durchgeführt werden konnte
	 */
	public static boolean berechnePruefungsergebnis(final @NotNull Abiturdaten abidaten) {
		// Bestimme die Fachbelegungen der Abiturfächer und sortiere diese
		final @NotNull List<AbiturFachbelegung> abiBelegungen = new ArrayList<>();
		for (final @NotNull AbiturFachbelegung fachbelegung : abidaten.fachbelegungen)
			if (fachbelegung.abiturFach != null)
				abiBelegungen.add(fachbelegung);

		// Bestimme die Anzahl der Abiturfächer (BLL zählt ggf. als 5. Abiturfach), um die Punkte pro Notenpunkt festzulegen
		final boolean hatBLL = !"K".equals(abidaten.besondereLernleistung);
		final int faktor = hatBLL ? 4 : 5;

		// Bestimme die Prüfungsergebnisse in den Abiturfächern (erster Durchgang ohne mdl. Prüfungen im 1.-3. Fach)
		int summe = 0;
		int defizite = 0;
		int defiziteLK = 0;
		int fehlendeNoten = 0;
		for (final @NotNull AbiturFachbelegung abibelegung : abiBelegungen) {
			final Integer npPruefung = getNotenpunkteFromKuerzelInternal(abibelegung.block2NotenKuerzelPruefung, abidaten.schuljahrAbitur);
			if (npPruefung == null) {
				abibelegung.block2PunkteZwischenstand = null;
				abibelegung.block2Punkte = null;
				abibelegung.block2MuendlichePruefungAbweichung = null;
				fehlendeNoten++;
			} else {
				abibelegung.block2PunkteZwischenstand = faktor * npPruefung;
				abibelegung.block2Punkte = abibelegung.block2PunkteZwischenstand;
				// nur bis Abiturjahrgang 2019, ab 2020 gibt es keine Abweichungsprüfungen mehr
				abibelegung.block2MuendlichePruefungAbweichung = (abidaten.abiturjahr <= 2019)
						&& (abibelegung.block1NotenpunkteDurchschnitt != null)
						&& (Math.abs(abibelegung.block1NotenpunkteDurchschnitt - npPruefung) >= 4.0);
				summe += abibelegung.block2PunkteZwischenstand;
				if (npPruefung < 5) {
					defizite++;
					if ((abibelegung.abiturFach == 1) || (abibelegung.abiturFach == 2))
						defiziteLK++;
				}
			}
		}

		// Füge ggf. das Ergebnis der BLL noch hinzu
		if (hatBLL) {
			final Integer npPruefung = getNotenpunkteFromKuerzelInternal(abidaten.besondereLernleistungNotenKuerzel, abidaten.schuljahrAbitur);
			if (npPruefung == null) {
				fehlendeNoten++;
			} else {
				if (npPruefung < 5)
					defizite++;
				summe += faktor * npPruefung;
			}
		}

		// Prüfe, ob Bestehensprüfungen im 1. - 3. Fach nötig sind.
		final boolean hatPflichtPruefungen = (fehlendeNoten == 0)
				&& ((defizite > (2 + (hatBLL ? 1 : 0))) || (defiziteLK > 1) || (summe < 100));
		final boolean hatNotenPruefung = (fehlendeNoten == 0);

		// Bestimme die Prüfungsergebnisse in den Abiturfächern (zweiter Durchgang ggf. mit mdl. Prüfungen im 1.-3. Fach)
		int fehlendeNotenMdlPflicht = 0;
		int fehlendeNotenMdlPflichtLK = 0;
		int fehlendeNotenMdlFreiwillig = 0;
		for (final @NotNull AbiturFachbelegung abibelegung : abiBelegungen) {
			final Integer npPruefung = getNotenpunkteFromKuerzelInternal(abibelegung.block2NotenKuerzelPruefung, abidaten.schuljahrAbitur);
			// Wenn noch keine Note gesetzt ist, dann kann auch noch keine weitere Information gesetzt sein...
			if (npPruefung == null) {
				abibelegung.block2MuendlichePruefungBestehen = null;
				abibelegung.block2MuendlichePruefungFreiwillig = null;
				abibelegung.block2MuendlichePruefungNotenKuerzel = null;
				abibelegung.block2Punkte = null;
				continue;
			}
			abibelegung.block2MuendlichePruefungBestehen = (hatPflichtPruefungen && (abibelegung.abiturFach != null) && (abibelegung.abiturFach <= 3));
			final boolean hatMdlAbweichung = (abibelegung.block2MuendlichePruefungAbweichung != null) && abibelegung.block2MuendlichePruefungAbweichung;
			final boolean hatMdlBestehen = abibelegung.block2MuendlichePruefungBestehen;
			final boolean hatMdlFreiwillig = (abibelegung.block2MuendlichePruefungFreiwillig != null) && abibelegung.block2MuendlichePruefungFreiwillig;
			final Integer npPruefungMdl = getNotenpunkteFromKuerzelInternal(abibelegung.block2MuendlichePruefungNotenKuerzel, abidaten.schuljahrAbitur);
			if (npPruefungMdl == null) {
				if (hatMdlBestehen || hatMdlAbweichung || hatMdlFreiwillig) {
					fehlendeNoten++;
					if (hatMdlBestehen || hatMdlAbweichung) {
						fehlendeNotenMdlPflicht++;
						if ((abibelegung.abiturFach == 1) || (abibelegung.abiturFach == 2))
							fehlendeNotenMdlPflichtLK++;
					}
					if (hatMdlFreiwillig)
						fehlendeNotenMdlFreiwillig++;
				} else {
					abibelegung.block2MuendlichePruefungNotenKuerzel = null;
					abibelegung.block2MuendlichePruefungReihenfolge = null;
					abibelegung.block2Punkte = abibelegung.block2PunkteZwischenstand;
				}
			} else {
				// Ist überhaupt eine Prüfung angesetzt?
				if ((!hatMdlAbweichung) && (!hatMdlBestehen) && (!hatMdlFreiwillig)) {
					abibelegung.block2MuendlichePruefungNotenKuerzel = null;
					abibelegung.block2MuendlichePruefungReihenfolge = null;
					abibelegung.block2Punkte = abibelegung.block2PunkteZwischenstand;
				} else {
					final int punkte = (int) Math.round((npPruefung * 2 + npPruefungMdl) * faktor / 3.0);
					abibelegung.block2Punkte = punkte;
					// ggf. ist durch die mdl. Prüfung ein Defizit verschwunden...
					final int punkteVorher = (abibelegung.block2PunkteZwischenstand == null) ? 0 : abibelegung.block2PunkteZwischenstand;
					final int punkteNachher = abibelegung.block2Punkte;
					summe += (punkteNachher - punkteVorher);
					if ((punkteVorher < 5 * faktor) && (punkteNachher >= 5 * faktor)) {
						defizite--;
						if ((abibelegung.abiturFach == 1) || (abibelegung.abiturFach == 2))
							defiziteLK--;
					}
					if ((punkteVorher >= 5 * faktor) && (punkteNachher < 5 * faktor)) {
						defizite++;
						if ((abibelegung.abiturFach == 1) || (abibelegung.abiturFach == 2))
							defiziteLK++;
					}
				}
			}
		}

		// Berechne die Gesamtqualifikation
		abidaten.block2DefiziteGesamt = defizite;
		abidaten.block2DefiziteLK = defiziteLK;
		abidaten.block2PunktSumme = summe;
		abidaten.gesamtPunkte = ((abidaten.block1PunktSummeNormiert == null) ? null : abidaten.block1PunktSummeNormiert + summe);
		if (abidaten.gesamtPunkte == null) {
			abidaten.note = null;
			abidaten.gesamtPunkteVerbesserung = null;
			abidaten.gesamtPunkteVerschlechterung = null;
		} else {
			double note = Math.floor((5.0 + (2.0 / 3.0) - abidaten.gesamtPunkte / 180.0) * 10.0) / 10.0;
			if (note < 1.0)
				note = 1.0;
			if (note <= 4.0) {
				final @NotNull String strNote = "" + note;
				abidaten.note = (strNote.length() <= 3) ? strNote : strNote.substring(0, 3);
				if (note == 1.0) {
					abidaten.gesamtPunkteVerbesserung = null;
				} else {
					final int punkteBesser = (int) (Math.round((5.0 + (2.0 / 3.0) - note) * 180.0) + 1);
					abidaten.gesamtPunkteVerbesserung = punkteBesser;
				}
				if (note == 4.0) {
					abidaten.gesamtPunkteVerschlechterung = null;
				} else {
					final int punkteSchlechter = (int) (Math.round((5.0 + (2.0 / 3.0) - (note - 0.1)) * 180.0) + 1);
					abidaten.gesamtPunkteVerschlechterung = punkteSchlechter;
				}
			} else {
				abidaten.note = null;
				abidaten.gesamtPunkteVerbesserung = null;
				abidaten.gesamtPunkteVerschlechterung = null;
			}
		}

		// Stelle fest, ob noch Prüfungsdaten fehlen oder nicht. Wenn nicht, dann prüfe, ob das Abitur bestanden ist
		if (hatNotenPruefung && (fehlendeNotenMdlFreiwillig == 0)) {
			final boolean hatBestanden = (defizite <= (2 + (hatBLL ? 1 : 0))) && (defiziteLK <= 1) && (summe >= 100);
			if (hatBestanden)
				abidaten.pruefungBestanden = true;
			else if ((fehlendeNotenMdlPflicht > 0) && ((fehlendeNotenMdlPflichtLK != 0) || (defiziteLK < 2)))
				abidaten.pruefungBestanden = null;
			else
				abidaten.pruefungBestanden = false;
		} else {
			abidaten.pruefungBestanden = null;
		}
		return true;
	}

}
