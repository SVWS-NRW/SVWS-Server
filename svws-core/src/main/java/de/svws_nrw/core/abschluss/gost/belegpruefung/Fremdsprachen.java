package de.svws_nrw.core.abschluss.gost.belegpruefung;

import java.util.List;
import java.util.ArrayList;

import de.svws_nrw.core.abschluss.gost.AbiturdatenManager;
import de.svws_nrw.core.abschluss.gost.GostBelegpruefung;
import de.svws_nrw.core.abschluss.gost.GostBelegpruefungsArt;
import de.svws_nrw.core.abschluss.gost.GostBelegungsfehler;
import de.svws_nrw.core.data.gost.AbiturFachbelegung;
import de.svws_nrw.core.data.gost.GostFach;
import de.svws_nrw.core.types.gost.GostAbiturFach;
import de.svws_nrw.core.types.gost.GostFachbereich;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.core.types.gost.GostKursart;
import de.svws_nrw.core.types.gost.GostSchriftlichkeit;
import de.svws_nrw.core.utils.gost.GostFachUtils;
import de.svws_nrw.core.utils.schueler.SprachendatenUtils;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse gruppiert die Belegprüfungen für einen Schüler für die Prüfung der EF1 bzw.
 * für die Gesamtprüfungen, welche im Bereich der Fremdsprachen durchgeführt werden.
 */
public final class Fremdsprachen extends GostBelegpruefung {

	/// Die Belegungen für alle Fächer der Fremdsprachen
	private @NotNull List<AbiturFachbelegung> _fremdsprachen = new ArrayList<>();

	/// Die Belegungen für alle neu einsetzenden Fremdsprachen
	private @NotNull List<AbiturFachbelegung> _fremdsprachenNeu = new ArrayList<>();

	/// Die Belegungen für alle fortgeführten Fremdsprachen
	private @NotNull List<AbiturFachbelegung> _fremdsprachenFortgefuehrt = new ArrayList<>();

	/// Die Belegungen von bilingualen Sachfächern
	private @NotNull List<AbiturFachbelegung> _biliSachfaecher = new ArrayList<>();

	/** Die Anzahl der durchgehenden bzw. potenziell durchgehenden Belegungen - nur schriftlich (für die Schwerpunktberechnung
	 * - hier zählt auch ein bilinguales Sachfach, wo die Fremdsprache der Unterrichtsprache aus der Sek I nicht fortgeführt wurde) */
	private int _anzahlDurchgehendSchriftlich;



	/**
	 * Erstellt eine neue Belegprüfung für dir Fremdsprachen.
	 *
	 * @param manager         der Daten-Manager für die Abiturdaten
	 * @param pruefungsArt   die Art der durchzuführenden Prüfung (z.B. EF.1 oder GESAMT)
	 */
	public Fremdsprachen(final @NotNull AbiturdatenManager manager, final @NotNull GostBelegpruefungsArt pruefungsArt) {
		super(manager, pruefungsArt);
	}



	@Override
	protected void init() {
		_fremdsprachen = manager.getRelevanteFachbelegungen(GostFachbereich.FREMDSPRACHE);
		_fremdsprachenNeu = manager.filterFremdspracheNeuEinsetzend(_fremdsprachen);
		_fremdsprachenFortgefuehrt = manager.filterFremdspracheFortgefuehrt(_fremdsprachen);
		_biliSachfaecher = manager.getFachbelegungenBilingual();
		_anzahlDurchgehendSchriftlich = 0;
	}


	@Override
	protected void pruefeEF1() {
		pruefeEF1Sprachenfolge();
		pruefeEF1Fremdsprache1();
		pruefeEF1FremdsprachenfolgeZweiteFremdsprache();
		pruefeEF1Schriftlichkeit();
		pruefeEF1AnzahlDurchgehenedeSprachen();
		pruefeEF1BilingualeSachfaecher();
		pruefeEF1BilingualenBildungsgang();
	}


	/**
	 * EF.1: Prüft bei der Sprachenfolge, ob eine gemäß Sprachenfolge fortgeführte
	 * Fremdsprache fehlerhafterweise als neu einsetzende Fremdsprache belegt wurde.
	 */
	private void pruefeEF1Sprachenfolge() {
		if (manager.hatFortgefuehrteFremdspracheInSprachendaten(_fremdsprachenNeu))
			addFehler(GostBelegungsfehler.FS_20);
		if (manager.hatNeuEinsetzendeFremdspracheInSprachendaten(_fremdsprachenFortgefuehrt))
			addFehler(GostBelegungsfehler.FS_21);
		if (!SprachendatenUtils.hatSprachbelegung(manager.getSprachendaten(), "E"))
			addFehler(GostBelegungsfehler.FS_22_INFO);
	}


	/**
	 * Prüft, ob eine gültige Fremdsprachenbelegung mit den EF.1-Wahlen möglich ist.
	 */
	private void pruefeEF1Fremdsprache1() {

		boolean gefundenFremdsprachenbelegung = false;
		boolean gefundenFortgefuehrteFremdsprachenbelegungOhneSprachenfolge = false;
		boolean gefundenFortgefuehrteFremdspracheAlsNeueinsetzende = false;

		int anzahlFortgefuehrteFremdsprachen = 0;
		int anzahlFortgefuehrteFremdsprachenEFBelegbar = 0;
		int anzahlFortgefuehrteFremdsprachenEFBelegbarFehlerMuendlich = 0;
		int anzahlFortgefuehrteFremdsprachenDurchgehendBelegbar = 0;
		int anzahlFortgefuehrteFremdsprachenDurchgehendBelegbarFehlerMuendlich = 0;

		// Anzahl der generell fortführbaren Sprachen gemäß Sprachenfolge und Sprachprüfungen ermitteln
		final int anzahlFortfuehrbareFremdsprachen = SprachendatenUtils.getFortfuehrbareSprachenInGOSt(manager.getSprachendaten()).size();

		// Durchlaufe die gewählten Fächer, welche per Fachdefinition eine fortgeführte Fremdsprache darstellen
		for (final @NotNull AbiturFachbelegung abiFachbelegung : _fremdsprachenFortgefuehrt) {

			// Wenn die Sprache nicht in EF.1 belegt wurde, kann es keine Sprache sein, die als fortgeführte (und durchgängig belegte)
			// Fremdsprache gewertet werden kann.
			if (!manager.pruefeBelegung(abiFachbelegung, GostHalbjahr.EF1)) {
				continue;
			}
			gefundenFremdsprachenbelegung = true;

			final GostFach gostFach = manager.getFach(abiFachbelegung);
			if ((gostFach != null) && !gostFach.kuerzel.equals("")) {
				// Prüfe, ob das gewählte Fremdsprachenfach vom Schüler als fortgeführte Fremdsprache belegt werden kann.
				if (SprachendatenUtils.istFortfuehrbareSpracheInGOSt(manager.getSprachendaten(), gostFach.kuerzel.substring(0, 1))) {
					anzahlFortgefuehrteFremdsprachen += 1;
					// Prüfe, ob diese Sprache gemäß den Vorgaben des Jahrgangs durchgängig belegt werden kann UND schriftlich belegt wurde in EF.1.
					if (manager.pruefeBelegungDurchgehendBelegbar(abiFachbelegung, GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.EF1)) {
						// Die Sprache wurde gültig belegt
						anzahlFortgefuehrteFremdsprachenDurchgehendBelegbar += 1;
					} else if (manager.pruefeBelegungDurchgehendBelegbar(abiFachbelegung, GostSchriftlichkeit.MUENDLICH, GostHalbjahr.EF1)) {
						// Die Sprache kann gemäß den Vorgaben des Jahrgangs durchgängig belegt werden UND wurde in EF.1 mündlich belegt.
						// Eine korrekte Sprache ist zwar vorhanden, aber die Belegung ist fehlerhaft, da in der EF alle Sprachen
						// schriftlich belegt werden müssen.
						anzahlFortgefuehrteFremdsprachenDurchgehendBelegbar += 1;
						anzahlFortgefuehrteFremdsprachenDurchgehendBelegbarFehlerMuendlich += 1;
					} else if ((gostFach.istMoeglichEF1) && (gostFach.istMoeglichEF2)) {
						// Es wurde also eine fortgeführte Fremdsprache gefunden, die aber unabhängig von der Belegung nicht durchgängig belegt werden kann.
						// Prüfe daher, ob die Sprache wenigstens in der EF belegt werden kann.
						anzahlFortgefuehrteFremdsprachenEFBelegbar += 1;
						if (manager.pruefeBelegungMitSchriftlichkeit(abiFachbelegung, GostSchriftlichkeit.MUENDLICH, GostHalbjahr.EF1)) {
							anzahlFortgefuehrteFremdsprachenEFBelegbarFehlerMuendlich += 1;
						}
					}
				} else {
					// Es wurde ein Fach einer fortgeführten Fremdsprache belegt, ohne das die Sprachenfolge oder die Sprachprüfung dazu passt. Gib eine Fehlermeldung aus.
					gefundenFortgefuehrteFremdsprachenbelegungOhneSprachenfolge = true;
					addFehler(GostBelegungsfehler.FS_23);
				}
			}
		}

		// Es wurde eine mündlich belegte Fremdsprache in der EF gefunden, gebe den entsprechenden Fehler daher aus.
		if ((anzahlFortgefuehrteFremdsprachenDurchgehendBelegbarFehlerMuendlich + anzahlFortgefuehrteFremdsprachenEFBelegbarFehlerMuendlich) > 0)
			addFehler(GostBelegungsfehler.FS_12);

		// Wenn alle fortgeführten Fremdsprachen mündlich belegt sind, gib eine weitere Fehlermeldung aus.
		if ((anzahlFortgefuehrteFremdsprachen > 0) && (anzahlFortgefuehrteFremdsprachen == (anzahlFortgefuehrteFremdsprachenDurchgehendBelegbarFehlerMuendlich
				+ anzahlFortgefuehrteFremdsprachenEFBelegbarFehlerMuendlich)))
			addFehler(GostBelegungsfehler.FS_16);

		// Es wurde eine passende Fremdsprache zur Erfüllung der Bedingung der ersten Fremdsprache gefunden.
		// Sollte diese nur mündlich belegt sein, wurde vorher ein Fehler ergänzt
		if (anzahlFortgefuehrteFremdsprachenDurchgehendBelegbar > 0)
			return;

		// Bisher wurde keine passende fortgeführte Fremdsprache gefunden. Betrachte nun Fälle mit neu einsetzender Sprache
		int anzahlNeueinsetzendeFremdsprachenDurchgehendBelegbar = 0;
		int anzahlNeueinsetzendeFremdsprachenDurchgehendBelegbarFehlerMuendlich = 0;

		// Durchlaufe die gewählten Fächer, welche per Fachdefinition eine neu einsetzende Fremdsprache darstellen.
		for (final @NotNull AbiturFachbelegung abiFachbelegung : _fremdsprachenNeu) {

			// Wenn die Sprache nicht in EF.1 belegt wurde, kann es keine Sprache sein, die als neu einsetzende
			// Fremdsprache gewertet werden kann.
			if (!manager.pruefeBelegung(abiFachbelegung, GostHalbjahr.EF1))
				continue;
			gefundenFremdsprachenbelegung = true;

			final GostFach gostFach = manager.getFach(abiFachbelegung);
			if ((gostFach != null) && !gostFach.kuerzel.equals("")) {
				// Prüfe, ob dieses Fach vorher nicht bereits als Sprache vom Schüler belegt wurde
				if (SprachendatenUtils.istNeueinsetzbareSpracheInGOSt(manager.getSprachendaten(), gostFach.kuerzel.substring(0, 1))) {
					// Prüfe, ob diese Sprache gemäß den Vorgaben des Jahrgangs durchgängig belegt werden kann UND schriftlich belegt wurde.
					if (manager.pruefeBelegungDurchgehendBelegbar(abiFachbelegung, GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.EF1)) {
						// Die Sprache wurde gültig belegt
						anzahlNeueinsetzendeFremdsprachenDurchgehendBelegbar += 1;
					} else if (manager.pruefeBelegungDurchgehendBelegbar(abiFachbelegung, GostSchriftlichkeit.MUENDLICH, GostHalbjahr.EF1)) {
						// Die Sprache wurde gemäß den Vorgaben des Jahrgangs durchgängig belegt UND mündlich belegt.
						// Es ist zwar eine korrekte Sprache vorhanden, aber die Belegung ist fehlerhaft, da in der EF alle Sprachen
						// schriftlich belegt werden müssen.
						anzahlNeueinsetzendeFremdsprachenDurchgehendBelegbar += 1;
						anzahlNeueinsetzendeFremdsprachenDurchgehendBelegbarFehlerMuendlich += 1;
					}
				} else {
					// Die neu einsetzende Fremdsprache ist vorher schon einmal belegt worden.
					addFehler(GostBelegungsfehler.FS_20);
					gefundenFortgefuehrteFremdspracheAlsNeueinsetzende = true;
				}
			}
		}

		// Eine neu einsetzende Fremdsprache wurde mündlich belegt, gib einen Fehler aus.
		if (anzahlNeueinsetzendeFremdsprachenDurchgehendBelegbarFehlerMuendlich > 0)
			addFehler(GostBelegungsfehler.FS_12);

		// Wenn zwar eine reguläre Belegung einer Sprache gefunden wurde, die aber nicht durchgängig belegt werden kann, gib eine entsprechende Fehlermeldung aus.
		if ((gefundenFremdsprachenbelegung
				&& !(gefundenFortgefuehrteFremdsprachenbelegungOhneSprachenfolge || gefundenFortgefuehrteFremdspracheAlsNeueinsetzende))
				&& ((anzahlFortgefuehrteFremdsprachenDurchgehendBelegbar + anzahlNeueinsetzendeFremdsprachenDurchgehendBelegbar) == 0))
			addFehler(GostBelegungsfehler.FS_11);

		// Wenn nur in der EF belegbare, fortgeführte Sprachen gefunden wurden, muss zusätzlich eine neu einsetzende Fremdsprache belegt werden.
		// Fehlt die neu einsetzende Fremdsprache, so wird ein Fehler ausgegeben.
		if (anzahlFortgefuehrteFremdsprachenEFBelegbar > 0) {
			if (anzahlNeueinsetzendeFremdsprachenDurchgehendBelegbar == 0)
				addFehler(GostBelegungsfehler.FS_10);
			return;
		}

		// Wenn keine durchgehend belegbare neu einsetzende Fremdsprache belegt wurde, gibt einen Fehler aus,
		// da der folgende Fall mit einer Sprachprüfung darauf angewiesen wäre.
		if (anzahlNeueinsetzendeFremdsprachenDurchgehendBelegbar == 0) {
			addFehler(GostBelegungsfehler.FS_18);
			return;
		}

		// Wenn keine in der EF belegte, fortgeführte Fremdsprache gefunden wird, dann müsste eine Sprachfeststellungsprüfung am Ende der EF stattfinden.
		// Zusätzlich zu dieser Prüfung müsste dann noch eine neu einsetzende Fremdsprache belegt werden.
		if (((anzahlFortgefuehrteFremdsprachenDurchgehendBelegbar + anzahlFortgefuehrteFremdsprachenEFBelegbar) == 0)
				&& (anzahlNeueinsetzendeFremdsprachenDurchgehendBelegbar > 0)) {
			// In diesem Fall muss die neueinsetzende Fremdsprache schriftlich belegt sein. Ist das nicht der Fall, gib einen Fehler aus.
			if (anzahlNeueinsetzendeFremdsprachenDurchgehendBelegbarFehlerMuendlich > 0) {
				addFehler(GostBelegungsfehler.FS_18);
			}
			// Da an dieser Stelle keine fortführbaren Sprachen vorhanden sind, dann muss eine Sprachprüfung Ende EF stattfinden, darauf muss hingewiesen werden.
			if (SprachendatenUtils.hatSprachfeststellungspruefungAufEFNiveau(manager.getSprachendaten())) {
				addFehler(GostBelegungsfehler.FS_19_INFO);
			} else {
				// Ein Schüler ohne Sprachprüfung und fortführbare Sprachen sollte keine Berechtigung für die Oberstufe erhalten haben.
				// Gebe daher Fehlermeldung zu einer evtl. fehlerhaften Sprachenfolge aus.
				if (anzahlFortfuehrbareFremdsprachen == 0) {
					addFehler(GostBelegungsfehler.FS_25);
				} else { // Andernfalls gib eine Fehlermeldung aus, dass eine vorhandene Sprache belegt werden muss
					addFehler(GostBelegungsfehler.FS_18);
					if (!SprachendatenUtils.hatZweiSprachenAb5Bis7MitMin4JahrenDauerEndeSekI(manager.getSprachendaten()))
						addFehler(GostBelegungsfehler.FS_24);
				}
			}
		}
	}


	/**
	 * Prüft, ob eine zweite Fremdsprache in der Sek I vorhanden ist und prüft sonst auf eine neu
	 * einsetzende Fremdsprache.
	 */
	private void pruefeEF1FremdsprachenfolgeZweiteFremdsprache() {

		// Sind zwei mind. vierjährige Sprachen in der Sek-I vorhanden oder entsprechende Sprachprüfungen?
		if (SprachendatenUtils.hatZweiSprachenAb5Bis7MitMin4JahrenDauerEndeSekI(manager.getSprachendaten()))
			return;

		// Wenn die geforderten zwei Sprachen noch nicht belegt wurden, ist dann wenigstens eine vierjährige Sprache in der Sek-I vorhanden oder eine entsprechende Sprachprüfung?
		if (SprachendatenUtils.hatEineSpracheAb5bis7MitMin4JahrenDauerEndeSekI(manager.getSprachendaten())) {

			// Prüfe, ob ergänzend eine neu einsetzende Fremdsprache in EF.1 schriftlich belegt wurde
			if (manager.pruefeBelegungExistiertMitSchriftlichkeitEinzeln(_fremdsprachenNeu, GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.EF1))
				return;

			// Wenn keine neue Fremdsprache vorhanden ist, findet sich dann eine 2. Fremdsprache ab Klasse 8 (bzw. 9)?
			if (SprachendatenUtils.hatEineSpracheAb8MitMin2JahrenDauerEndeSekI(manager.getSprachendaten())) {
				// Prüfe, ob die zweite Fremdsprache in EF.1 schriftlich belegt wurde
				final AbiturFachbelegung zweiteFremdsprache =
						manager.getSprachbelegung(SprachendatenUtils.getEineSpracheAb8MitMin2JahrenDauerEndeSekI(manager.getSprachendaten()));
				// Die Sprache muss, um als zweite Fremdsprache gewertet werden zu können, bis Ende EF.2 belegt werden.
				if (!manager.pruefeBelegungMitSchriftlichkeitEinzeln(zweiteFremdsprache, GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.EF1))
					addFehler(GostBelegungsfehler.FS_13);
				return;
			}
		}

		// Es gibt somit keine Belegung einer Fremdsprache über 4 Jahre in der Sekundarstufe I. Daher muss bereits ein Fehler bei der Prüfung der ersten Fremdsprache
		// aufgetreten sein. Wenn jetzt auch keine neu einsetzende Fremdsprache belegt ist, gebe dies zusätzlich als Fehler aus.
		if (!manager.pruefeBelegungExistiertMitSchriftlichkeitEinzeln(_fremdsprachenNeu, GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.EF1)) {
			addFehler(GostBelegungsfehler.FS_14);
		}
	}


	/**
	 * Prüft, ob alle Fremdsprachen in der EF.1 schriftlich belegt wurden.
	 */
	private void pruefeEF1Schriftlichkeit() {
		if (_fremdsprachen == null)
			return;
		for (final AbiturFachbelegung fachbelegung : _fremdsprachen)
			if (manager.pruefeBelegungMitSchriftlichkeitEinzeln(fachbelegung, GostSchriftlichkeit.BELIEBIG, GostHalbjahr.EF1)
					&& !manager.pruefeBelegungMitSchriftlichkeitEinzeln(fachbelegung, GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.EF1)) {
				addFehler(GostBelegungsfehler.FS_12);
				break;
			}
	}


	/**
	 * Zähle alle Fremdsprachen, die durchgehend schriftlich belegt wurden.
	 * Hierzu zählt auch die Unterrichtssprache eines bilingualen Sachfachs als zweite durchgehende
	 * Fremdsprache, sofern dieses durchgehende und schriftlich belegt werden kann.
	 */
	private void pruefeEF1AnzahlDurchgehenedeSprachen() {
		// Zähle die durchgängig belegbaren schriftlich belegten Fremdsprachen für die Prüfung des Schwerpunktes
		final List<AbiturFachbelegung> fremdsprachenDurchgehend = manager.filterBelegungenMitSchriftlichkeit(
				manager.filterDurchgehendBelegbar(_fremdsprachen), GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.EF1);
		_anzahlDurchgehendSchriftlich = fremdsprachenDurchgehend.size();

		// Eine Prüfung der bilingualen Sachfächer ist nur nötig, sofern nur eine durchgängig belegbare schriftlich belegte Fremdsprache vorliegt
		if (_anzahlDurchgehendSchriftlich != 1)
			return;
		final GostFach fsDurchgehend = manager.getFach(fremdsprachenDurchgehend.get(0));
		if (fsDurchgehend == null) // Dieser Fehlerfall sollte nicht auftreten...
			return;
		final String fremdspracheDurchgehend = GostFachUtils.getFremdsprache(fsDurchgehend);
		if (fremdspracheDurchgehend == null) // Dieser Fehlerfall sollte nicht auftreten...
			return;

		// Bestimme die bilingualen Sachfächer, die durchgehend belegbar sind und in der EF.1 schriftlich gewählt wurden
		final List<AbiturFachbelegung> biliSachfaecherDurchgehendSchriftlich = manager.filterBelegungenMitSchriftlichkeit(
				manager.filterDurchgehendBelegbar(_biliSachfaecher), GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.EF1);
		// Prüfe bei diesen Fächern, ob eines davon für einen möglichen Sprachenschwerpunkt genutzt werden kann
		for (final AbiturFachbelegung biliSachfach : biliSachfaecherDurchgehendSchriftlich) {
			// Stelle sicher, dass die belegte Fremdsprache nicht als Unterrichtssprache erneut gezählt wird
			final GostFach fach = manager.getFach(biliSachfach);
			if ((fach == null) || (fremdspracheDurchgehend.equals(fach.biliSprache)))
				continue;
			// Werte das bilinguale Sachfach für einen möglichen Sprachenschwerpunkt und beende die Prüfung dann
			_anzahlDurchgehendSchriftlich++;
			return;
		}
	}



	/**
	 * Prüft, ob die Bedingungen für die Wahl eines bilingualen Sachfaches erfüllt sind, sofern eines
	 * in der EF.1 belegt wurde.
	 */
	private void pruefeEF1BilingualeSachfaecher() {
		if (_biliSachfaecher == null)
			return;

		// Prüfe alle belegten bilingualen Sachfächer...
		for (final AbiturFachbelegung biliSachfach : _biliSachfaecher) {
			// Prüfe, ob die Unterrichtssprache des bilingualen Sachfaches in der Sekundarstufe 1 mindestens zwei Jahre lang belegt wurde
			final GostFach fach = manager.getFach(biliSachfach);
			if (fach == null)
				continue;
			final String biliSprache = fach.biliSprache;
			if (!SprachendatenUtils.hatSprachbelegungMitMin2JahrenDauerEndeSekI(manager.getSprachendaten(), biliSprache)) {
				addFehler(GostBelegungsfehler.BIL_14);
				continue;
			}

			// Prüfe, ob die Fremdsprache schriftlich belegt wurde oder das bilinguale Sachfach schriftlich belegt wurde. Ist dies
			// der Fall, so kann das bilinguale Sachfach bei einer geeigneten Weiterbelegung zu einem möglichen Sprachenschwerpunkt beitragen.
			final AbiturFachbelegung fremdsprache = manager.getSprachbelegung(biliSprache);
			if (manager.pruefeBelegungMitSchriftlichkeitEinzeln(fremdsprache, GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.EF1)
					|| manager.pruefeBelegungMitSchriftlichkeitEinzeln(biliSachfach, GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.EF1))
				continue;

			// Das bilinguale Sachfach kann somit nicht zum Sprachenschwerpunkt beitragen. Gebe einen entsprechenden Hinweis...
			addFehler(GostBelegungsfehler.BIL_4_INFO);
		}
	}


	/**
	 * Prüfe, ob die Bedingungen für den bilingualen Bildungsgang erfüllt sind, sofern ein solcher vom Schüler gewählt wurde.
	 */
	private void pruefeEF1BilingualenBildungsgang() {
		// Prüfe, ob der Schüler im bilingualen Bildungsgang ist
		final String biligualeSprache = manager.getBiligualenBildungsgang();
		if (biligualeSprache == null)
			return;

		// Prüfe, ob die Fremdsprache des bilingualen Bildungsganges schriftlich gewählt wurde und durchgehend belegbar ist
		final AbiturFachbelegung biliSprache = manager.getSprachbelegung(biligualeSprache);
		if (!manager.pruefeBelegungDurchgehendBelegbar(biliSprache, GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.EF1))
			addFehler(GostBelegungsfehler.BIL_10);

		// Prüfe, ob kein bilinguales Sachfach gewählt wurde.
		if ((_biliSachfaecher == null) || (_biliSachfaecher.isEmpty())) {
			addFehler(GostBelegungsfehler.BIL_15);
			return;
		}

		// Prüfe, ob nur ein bilinguales Sachfach gewählt wurde.
		if (_biliSachfaecher.size() < 2)
			addFehler(GostBelegungsfehler.BIL_11_INFO);
	}




	@Override
	protected void pruefeGesamt() {
		pruefeGesamtSprachenfolge();
		pruefeGesamtFremdsprache1();
		pruefeGesamtFremdsprachenfolgeZweiteFremdsprache();
		pruefeGesamtSchriftlichkeit();
		pruefeGesamtAnzahlDurchgehenedeSprachen();
		pruefeGesamtBilingualeSachfaecher();
		pruefeGesamtBilingualenBildungsgang();
	}



	/**
	 * Gesamt: Prüft bei der Sprachenfolge, ob eine gemäß Sprachenfolge fortgeführte
	 * Fremdsprache fehlerhafterweise als neu einsetzende Fremdsprache belegt wurde.
	 */
	private void pruefeGesamtSprachenfolge() {
		if (manager.hatFortgefuehrteFremdspracheInSprachendaten(_fremdsprachenNeu))
			addFehler(GostBelegungsfehler.FS_20);
		if (manager.hatNeuEinsetzendeFremdspracheInSprachendaten(_fremdsprachenFortgefuehrt))
			addFehler(GostBelegungsfehler.FS_21);
		if (!SprachendatenUtils.hatSprachbelegung(manager.getSprachendaten(), "E"))
			addFehler(GostBelegungsfehler.FS_22_INFO);
	}



	/**
	 * Prüft, ob eine gültige Fremdsprachenbelegung in Bezug auf eine durchgehende Belegung möglich ist.
	 */
	private void pruefeGesamtFremdsprache1() {

		// ### Abschnitt 1 ###
		// Fortgeführte Fremdsprachen prüfen, ob diese als durchgängig belegte Fremdsprachen gelten können.

		int anzahlFortgefuehrteFremdsprachenEFBelegt = 0;
		int anzahlFortgefuehrteFremdsprachenDurchgehendBelegt = 0;
		int anzahlFortgefuehrteFremdsprachenBelegtFehlerMuendlichEF = 0;
		int anzahlFortgefuehrteFremdsprachenDurchgehendBelegtFehlerMuendlichEF = 0;

		// Anzahl der generell fortführbaren Sprachen gemäß Sprachenfolge und Sprachprüfungen ermitteln
		final int anzahlFortfuehrbareFremdsprachen = SprachendatenUtils.getFortfuehrbareSprachenInGOSt(manager.getSprachendaten()).size();

		// Durchlaufe die gewählten Fächer, welche per Fachdefinition eine fortgeführte Fremdsprache darstellen
		for (final @NotNull AbiturFachbelegung abiFachbelegung : _fremdsprachenFortgefuehrt) {

			// Wenn die Sprache nicht in EF.1 belegt wurde, kann es keine Sprache sein, die als fortgeführte (und durchgängig belegte)
			// Fremdsprache gewertet werden kann.
			if (!manager.pruefeBelegung(abiFachbelegung, GostHalbjahr.EF1)) {
				continue;
			}

			final GostFach gostFach = manager.getFach(abiFachbelegung);
			if ((gostFach != null) && !gostFach.kuerzel.equals("")) {
				// Prüfe, ob das gewählte Fremdsprachenfach vom Schüler als fortgeführte Fremdsprache belegt werden kann.
				if (SprachendatenUtils.istFortfuehrbareSpracheInGOSt(manager.getSprachendaten(), gostFach.kuerzel.substring(0, 1))) {

					// Prüfe, ob diese Sprache durchgängig von EF.1 bis EF.2 belegt wurde.
					if (manager.pruefeBelegung(abiFachbelegung, GostHalbjahr.EF1, GostHalbjahr.EF2))
						anzahlFortgefuehrteFremdsprachenEFBelegt += 1;

					// Prüfe, ob die belegte Fremdsprache in der EF.1 und EF.2 schriftlich belegt war. Andernfalls liegt ein Fehler vor.
					if (manager.pruefeBelegungMitSchriftlichkeit(abiFachbelegung, GostSchriftlichkeit.MUENDLICH, GostHalbjahr.EF1)
							|| manager.pruefeBelegungMitSchriftlichkeit(abiFachbelegung, GostSchriftlichkeit.MUENDLICH, GostHalbjahr.EF2))
						anzahlFortgefuehrteFremdsprachenBelegtFehlerMuendlichEF += 1;

					// Prüfe, ob diese Sprache durchgängig von EF.1 bis Q2.2 belegt wurde. Dabei ist die Schriftlichkeit ab Q1.1 nicht zwingend erforderlich.
					if (manager.pruefeBelegung(abiFachbelegung, GostHalbjahr.EF1, GostHalbjahr.EF2, GostHalbjahr.Q11, GostHalbjahr.Q12, GostHalbjahr.Q21,
							GostHalbjahr.Q22)) {
						anzahlFortgefuehrteFremdsprachenDurchgehendBelegt += 1;
						// Prüfe, ob die belegte Fremdsprache in der EF.1 und EF.2 schriftlich belegt war. Andernfalls liegt ein Fehler vor.
						if (manager.pruefeBelegungMitSchriftlichkeit(abiFachbelegung, GostSchriftlichkeit.MUENDLICH, GostHalbjahr.EF1)
								|| manager.pruefeBelegungMitSchriftlichkeit(abiFachbelegung, GostSchriftlichkeit.MUENDLICH, GostHalbjahr.EF2))
							anzahlFortgefuehrteFremdsprachenDurchgehendBelegtFehlerMuendlichEF += 1;
					}
				} else {
					// Es wurde ein Fach einer fortgeführten Fremdsprache belegt, ohne das die Sprachenfolge oder die Sprachprüfung dazu passt. Gib eine Fehlermeldung aus.
					addFehler(GostBelegungsfehler.FS_23);
				}
			}
		}

		// Es wurde eine mündlich belegte Fremdsprache in der EF gefunden, gebe den entsprechenden Fehler daher aus.
		if (anzahlFortgefuehrteFremdsprachenBelegtFehlerMuendlichEF > 0)
			addFehler(GostBelegungsfehler.FS_12);

		// Wenn alle durchgängig belegten, fortgeführten Fremdsprachen in mindestens einem EF Halbjahr mündlich belegt sind, gib eine weitere Fehlermeldung aus.
		if ((anzahlFortgefuehrteFremdsprachenDurchgehendBelegt > 0)
				&& (anzahlFortgefuehrteFremdsprachenDurchgehendBelegt == anzahlFortgefuehrteFremdsprachenDurchgehendBelegtFehlerMuendlichEF))
			addFehler(GostBelegungsfehler.FS_16);

		// Es wurde eine passende Fremdsprache zur Erfüllung der Bedingung der ersten Fremdsprache gefunden.
		// Sollte diese nur mündlich belegt sein, wurde vorher ein Fehler ergänzt
		if (anzahlFortgefuehrteFremdsprachenDurchgehendBelegt > 0)
			return;


		// ### Abschnitt 2 ###
		// Bisher wurde keine passende fortgeführte Fremdsprache gefunden. Betrachte nun Fälle mit neu einsetzender Sprache

		int anzahlNeueinsetzendeFremdsprachenDurchgehendBelegt = 0;
		int anzahlNeueinsetzendeFremdsprachenBelegtFehlerMuendlichEF = 0;

		// Durchlaufe die gewählten Fächer, welche per Fachdefinition eine neu einsetzende Fremdsprache darstellen.
		for (final @NotNull AbiturFachbelegung abiFachbelegung : _fremdsprachenNeu) {

			// Wenn die Sprache nicht in EF.1 belegt wurde, kann es keine Sprache sein, die als neu einsetzende
			// Fremdsprache gewertet werden kann.
			if (!manager.pruefeBelegung(abiFachbelegung, GostHalbjahr.EF1))
				continue;

			final GostFach gostFach = manager.getFach(abiFachbelegung);
			if ((gostFach != null) && !gostFach.kuerzel.equals("")) {
				// Prüfe, ob dieses Fach vorher nicht bereits als Sprache vom Schüler belegt wurde
				if (SprachendatenUtils.istNeueinsetzbareSpracheInGOSt(manager.getSprachendaten(), gostFach.kuerzel.substring(0, 1))) {
					// Prüfe, ob diese Sprache durchgängig belegt wurde.
					if (manager.pruefeBelegung(abiFachbelegung, GostHalbjahr.EF1, GostHalbjahr.EF2, GostHalbjahr.Q11, GostHalbjahr.Q12, GostHalbjahr.Q21,
							GostHalbjahr.Q22)) {
						anzahlNeueinsetzendeFremdsprachenDurchgehendBelegt += 1;
					}

					// Prüfe, ob die belegte, neu einsetzende Fremdsprache in der EF.1 und EF.2 schriftlich belegt war. Andernfalls liegt ein Fehler vor.
					if (manager.pruefeBelegungMitSchriftlichkeit(abiFachbelegung, GostSchriftlichkeit.MUENDLICH, GostHalbjahr.EF1)
							|| manager.pruefeBelegungMitSchriftlichkeit(abiFachbelegung, GostSchriftlichkeit.MUENDLICH, GostHalbjahr.EF2)) {
						anzahlNeueinsetzendeFremdsprachenBelegtFehlerMuendlichEF += 1;
					}
				} else {
					// Die neu einsetzende Fremdsprache ist vorher schon einmal belegt worden.
					addFehler(GostBelegungsfehler.FS_20);
				}
			}
		}

		// Eine neu einsetzende Fremdsprache wurde mündlich belegt, gib einen Fehler aus.
		if (anzahlNeueinsetzendeFremdsprachenBelegtFehlerMuendlichEF > 0) {
			addFehler(GostBelegungsfehler.FS_12);
		}


		// Für die folgenden Fälle gilt weiterhin: Es liegt KEINE durchgehend belegte fortgeführte Fremdsprache vor.
		if (anzahlNeueinsetzendeFremdsprachenDurchgehendBelegt > 0) {
			// Für eine gültige Laufbahn muss entweder eine Sprachprüfung Ende EF stattgefunden haben ...
			if (SprachendatenUtils.hatSprachfeststellungspruefungAufEFNiveau(manager.getSprachendaten())) {
				addFehler(GostBelegungsfehler.FS_19_INFO);
				return;
			}
			// ... oder eine fortgeführte Fremdsprache bis Ende EF belegt worden sein.
			// Fehlt diese, gib einen Fehler aus und prüfe auf weitere Fehler.
			if (anzahlFortgefuehrteFremdsprachenEFBelegt == 0) {
				addFehler(GostBelegungsfehler.FS_10);

				// Ein Schüler ohne Sprachprüfung und fortführbare Sprachen sollte keine Berechtigung für die Oberstufe erhalten haben.
				// Gebe daher Fehlermeldung zu einer evtl. fehlerhaften Sprachenfolge aus.
				if (anzahlFortfuehrbareFremdsprachen == 0) {
					addFehler(GostBelegungsfehler.FS_25);
				} else { // Andernfalls gib eine Fehlermeldung aus, dass eine vorhandene Sprache belegt werden muss
					if (!SprachendatenUtils.hatZweiSprachenAb5Bis7MitMin4JahrenDauerEndeSekI(manager.getSprachendaten()))
						addFehler(GostBelegungsfehler.FS_24);
				}
			}
		} else {
			// Hier liegt WEDER eine durchgängig belegte fortgeführte NOCH eine durchgängig belegte neu einsetzende Fremdsprache vor.
			// Gebe eine entsprechende Fehlermeldung aus. Da FS_10 einen Hinweis auf eine weitere fortgeführte Fremdsprache in der EF enthält,
			// gebe bei einer Sprachprüfung am EF den Fehler FS_18 aus (ohne den zusätzlichen Hinweis in FS_10).
			if (SprachendatenUtils.hatSprachfeststellungspruefungAufEFNiveau(manager.getSprachendaten())) {
				addFehler(GostBelegungsfehler.FS_18);
			} else {
				addFehler(GostBelegungsfehler.FS_10);
			}
		}

	}



	/**
	 * Prüft, ob eine zweite Fremdsprache in der Sek I vorhanden ist und prüft sonst auf eine neu
	 * einsetzende Fremdsprache.
	 */
	private void pruefeGesamtFremdsprachenfolgeZweiteFremdsprache() {

		// Sind zwei mind. vierjährige Sprachen in der Sek-I vorhanden oder entsprechende Sprachprüfungen?
		if (SprachendatenUtils.hatZweiSprachenAb5Bis7MitMin4JahrenDauerEndeSekI(manager.getSprachendaten()))
			return;

		// Wenn die geforderten zwei Sprachen noch nicht belegt wurden, ist dann wenigstens eine vierjährige Sprache in der Sek-I vorhanden oder eine entsprechende Sprachprüfung?
		if (SprachendatenUtils.hatEineSpracheAb5bis7MitMin4JahrenDauerEndeSekI(manager.getSprachendaten())) {

			// Prüfe, ob ergänzend eine neu einsetzende Fremdsprache in EF.1 schriftlich belegt wurde
			if (manager.pruefeBelegungExistiert(_fremdsprachenNeu, GostHalbjahr.EF1, GostHalbjahr.EF2, GostHalbjahr.Q11, GostHalbjahr.Q12, GostHalbjahr.Q21,
					GostHalbjahr.Q22))
				return;

			// Wenn keine neue Fremdsprache vorhanden ist, findet sich dann eine 2. Fremdsprache ab Klasse 8 (bzw. 9)?
			if (SprachendatenUtils.hatEineSpracheAb8MitMin2JahrenDauerEndeSekI(manager.getSprachendaten())) {
				// Prüfe, ob die zweite Fremdsprache in EF.1 schriftlich belegt wurde
				final AbiturFachbelegung zweiteFremdsprache =
						manager.getSprachbelegung(SprachendatenUtils.getEineSpracheAb8MitMin2JahrenDauerEndeSekI(manager.getSprachendaten()));
				// Die Sprache muss, um als zweite Fremdsprache gewertet werden zu können, bis Ende EF.2 belegt werden.
				if (!manager.pruefeBelegungMitSchriftlichkeit(zweiteFremdsprache, GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.EF1, GostHalbjahr.EF2))
					addFehler(GostBelegungsfehler.FS_13);
				return;
			}
		}

		// Es gibt somit keine Belegung einer Fremdsprache über 4 Jahre in der Sekundarstufe I. Daher muss bereits ein Fehler bei der Prüfung der ersten Fremdsprache
		// aufgetreten sein. Wenn jetzt auch keine neu einsetzende Fremdsprache belegt ist, gebe dies zusätzlich als Fehler aus.
		if (!manager.pruefeBelegungExistiert(_fremdsprachenNeu, GostHalbjahr.EF1, GostHalbjahr.EF2, GostHalbjahr.Q11, GostHalbjahr.Q12, GostHalbjahr.Q21,
				GostHalbjahr.Q22)) {
			addFehler(GostBelegungsfehler.FS_14);
		}
	}


	/**
	 * Prüft, ob eine gültige Fremdsprachenbelegung in Bezug auf die Schriftlichkeit und LK-Wahl vorhanden ist.
	 */
	private void pruefeGesamtSchriftlichkeit() {
		// Prüfe, ob eine neu einsetzende Fremdsprache in den Halbjahren EF.1 bis Q2.1 mündlich belegt wurde
		if (manager.pruefeBelegungExistiertHatMindestensEinmalSchriftlichkeit(_fremdsprachenNeu, GostSchriftlichkeit.MUENDLICH,
				GostHalbjahr.EF1, GostHalbjahr.EF2, GostHalbjahr.Q11, GostHalbjahr.Q12, GostHalbjahr.Q21))
			addFehler(GostBelegungsfehler.FS_15);

		// Prüft, ob eine neu einsetzende Fremdsprache in einem der Halbjahre der Qualifikationsphase als Leistungskurs belegt wurde. Dies ist nicht zulässig.
		if (manager.pruefeBelegungExistiertHatMindestensEinmalKursart(_fremdsprachenNeu, GostKursart.LK, GostHalbjahr.Q11, GostHalbjahr.Q12, GostHalbjahr.Q21,
				GostHalbjahr.Q22))
			addFehler(GostBelegungsfehler.FS_17);

		// Prüfe, ob eine fortgeführte Fremdsprache in der Einführungsphase mündlich belegt wurde
		if (manager.pruefeBelegungExistiertErfuelltNichtFallsBelegt(_fremdsprachenFortgefuehrt, GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.EF1,
				GostHalbjahr.EF2))
			addFehler(GostBelegungsfehler.FS_12);

		// Prüfe, ob eine fortgeführte Fremdsprache durchgehend (außer Q2.2) schriftlich belegt wurde
		if (manager.pruefeBelegungDurchgehendBelegtExistiert(_fremdsprachenFortgefuehrt, GostSchriftlichkeit.SCHRIFTLICH,
				GostHalbjahr.EF1, GostHalbjahr.EF2, GostHalbjahr.Q11, GostHalbjahr.Q12, GostHalbjahr.Q21))
			return;

		// Es wurde keine fortgeführte Fremdsprache durchgehend (außer Q2.2) schriftlich belegt, existiert denn eine neu einsetzende mit dieser Belegung?
		if (!manager.pruefeBelegungDurchgehendBelegtExistiert(_fremdsprachenNeu, GostSchriftlichkeit.SCHRIFTLICH,
				GostHalbjahr.EF1, GostHalbjahr.EF2, GostHalbjahr.Q11, GostHalbjahr.Q12, GostHalbjahr.Q21)) {
			addFehler(GostBelegungsfehler.FS_11);
			return;
		}

		// Prüfe, ob eine Muttersprachenprüfung stattgefunden hat und eine fortgeführte Fremdsprache in der EF schriftlich belegt wurde
		if (SprachendatenUtils.hatSprachfeststellungspruefungAufEFNiveau(manager.getSprachendaten())
				&& manager.pruefeBelegungExistiertMitSchriftlichkeit(_fremdsprachenFortgefuehrt, GostSchriftlichkeit.SCHRIFTLICH,
						GostHalbjahr.EF1, GostHalbjahr.EF2))
			return;

		// Prüfe, ob eine Muttersprachenprüfung stattgefunden hat und eine neu einsetzende Fremdsprache durchgängig belegt wurde
		if (SprachendatenUtils.hatSprachfeststellungspruefungAufEFNiveau(manager.getSprachendaten())
				&& manager.pruefeBelegungDurchgehendBelegtExistiert(_fremdsprachenNeu, GostSchriftlichkeit.SCHRIFTLICH,
						GostHalbjahr.EF1, GostHalbjahr.EF2, GostHalbjahr.Q11, GostHalbjahr.Q12, GostHalbjahr.Q21))
			return;

		// Es liegt somit ein Belegungsfehler vor, wenn keine fortgeführte Fremdsprache in EF.1 schriftliche belegt wurde...
		if (!manager.pruefeBelegungExistiertMitSchriftlichkeit(_fremdsprachenFortgefuehrt, GostSchriftlichkeit.SCHRIFTLICH,
				GostHalbjahr.EF1, GostHalbjahr.EF2))
			addFehler(GostBelegungsfehler.FS_16);
	}




	/**
	 * Zähle alle Fremdsprachen, die durchgehend schriftlich belegt wurden.
	 * Hierzu zählt auch die Unterrichtssprache eines bilingualen Sachfachs als zweite durchgehende
	 * Fremdsprache, sofern dieses durchgehend und schriftlich belegt wurde.
	 */
	private void pruefeGesamtAnzahlDurchgehenedeSprachen() {
		// Zähle die durchgängig schriftlich belegten Fremdsprachen für die Prüfung des Schwerpunktes
		final @NotNull List<AbiturFachbelegung> fremdsprachenDurchgehend = manager.filterBelegungen(_fremdsprachen, GostHalbjahr.EF1, GostHalbjahr.EF2,
				GostHalbjahr.Q11, GostHalbjahr.Q12, GostHalbjahr.Q21, GostHalbjahr.Q22);
		final @NotNull List<AbiturFachbelegung> fremdsprachenDurchgehendSchriftlich = manager.filterBelegungenMitSchriftlichkeit(fremdsprachenDurchgehend,
				GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.EF1, GostHalbjahr.EF2, GostHalbjahr.Q11, GostHalbjahr.Q12, GostHalbjahr.Q21);
		_anzahlDurchgehendSchriftlich = fremdsprachenDurchgehendSchriftlich.size();

		// Eine Prüfung der bilingualen Sachfächer ist nur nötig, sofern nur eine durchgängig belegbare schriftlich belegte Fremdsprache vorliegt
		if (_anzahlDurchgehendSchriftlich != 1)
			return;
		final GostFach fsDurchgehend = manager.getFach(fremdsprachenDurchgehendSchriftlich.get(0));
		if (fsDurchgehend == null) // Dieser Fehlerfall sollte nicht auftreten...
			return;
		final String fremdspracheDurchgehend = GostFachUtils.getFremdsprache(fsDurchgehend);
		if (fremdspracheDurchgehend == null) // Dieser Fehlerfall sollte nicht auftreten...
			return;

		// Bestimme die bilingualen Sachfächer, die durchgehend schriftlich belegt wurden
		final @NotNull List<AbiturFachbelegung> biliSachfaecherDurchgehend = manager.filterBelegungen(_biliSachfaecher, GostHalbjahr.EF1,
				GostHalbjahr.EF2, GostHalbjahr.Q11, GostHalbjahr.Q12, GostHalbjahr.Q21, GostHalbjahr.Q22);
		final @NotNull List<AbiturFachbelegung> biliSachfaecherDurchgehendSchriftlich =
				manager.filterBelegungenMitSchriftlichkeit(biliSachfaecherDurchgehend,
						GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.EF1, GostHalbjahr.EF2, GostHalbjahr.Q11, GostHalbjahr.Q12, GostHalbjahr.Q21);
		// Prüfe bei diesen Fächern, ob eines davon für einen möglichen Sprachenschwerpunkt genutzt werden kann
		for (final AbiturFachbelegung biliSachfach : biliSachfaecherDurchgehendSchriftlich) {
			// Stelle sicher, dass die belegte Fremdsprache nicht als Unterrichtssprache erneut gezählt wird
			final GostFach fach = manager.getFach(biliSachfach);
			if ((fach == null) || (fremdspracheDurchgehend.equals(fach.biliSprache)))
				continue;
			// Werte das bilinguale Sachfach für einen möglichen Sprachenschwerpunkt und beende die Prüfung dann
			_anzahlDurchgehendSchriftlich++;
			return;
		}
	}



	/**
	 * Prüft, ob die Bedingungen für die Wahl eines bilingualen Sachfaches erfüllt sind, sofern eines
	 * belegt wurde.
	 */
	private void pruefeGesamtBilingualeSachfaecher() {
		if (_biliSachfaecher == null)
			return;

		// Prüfe alle belegten bilingualen Sachfächer...
		for (final AbiturFachbelegung biliSachfach : _biliSachfaecher) {
			// Prüfe, ob die Unterrichtssprache des bilingualen Sachfaches in der Sekundarstufe I mindestens zwei Jahre lang belegt wurde
			final GostFach biliFach = manager.getFach(biliSachfach);
			if ((biliFach == null) || (!SprachendatenUtils.hatSprachbelegungMitMin2JahrenDauerEndeSekI(manager.getSprachendaten(), biliFach.biliSprache)))
				addFehler(GostBelegungsfehler.BIL_14);
		}
	}



	/**
	 * Prüfe, ob die Bedingungen für ein bilinguales Abitur erfüllt sind, sofern ein solches vom
	 * Schüler gewählt wurde.
	 */
	private void pruefeGesamtBilingualenBildungsgang() {
		// Prüfe, ob der Schüler im bilingualen Bildungsgang ist
		final String biligualeSprache = manager.getBiligualenBildungsgang();
		if (biligualeSprache == null)
			return;

		// Prüfe, ob die Fremdsprache des bilingualen Bildungsganges in der EF schriftlich gewählt wurde und danach als LK gewählt wurde
		final AbiturFachbelegung biliSprache = manager.getSprachbelegung(biligualeSprache);
		if ((!manager.pruefeBelegungMitSchriftlichkeit(biliSprache, GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.EF1, GostHalbjahr.EF2)
				|| (!manager.pruefeBelegungMitKursart(biliSprache, GostKursart.LK, GostHalbjahr.Q11, GostHalbjahr.Q12, GostHalbjahr.Q21, GostHalbjahr.Q22))))
			addFehler(GostBelegungsfehler.BIL_10);

		// Prüfe, ob kein bilinguales Sachfach in der EF gewählt wurde.
		final List<AbiturFachbelegung> biliSachfaecherEF = manager.filterBelegungen(_biliSachfaecher, GostHalbjahr.EF1, GostHalbjahr.EF2);
		if (biliSachfaecherEF.isEmpty()) {
			addFehler(GostBelegungsfehler.BIL_15);
			return;
		}

		// Prüfe, ob nur ein bilinguales Sachfach in der EF gewählt wurde.
		if (biliSachfaecherEF.size() < 2)
			addFehler(GostBelegungsfehler.BIL_11_INFO);

		// Prüfe, ob mindestens eines der Sachfächer von EF.1 bis Q2.2 belegt und von Q1.1 bis Q2.1 schriftlich belegt wurde
		boolean hatBiliSachfaecherDurchgehendSchriftlich = false;
		if (_biliSachfaecher != null) {
			for (final AbiturFachbelegung fach : _biliSachfaecher) {
				if (manager.pruefeDurchgaengigkeitSchriftlich(fach)) {
					hatBiliSachfaecherDurchgehendSchriftlich = true;
					break;
				}
			}
		}
		if (!hatBiliSachfaecherDurchgehendSchriftlich)
			addFehler(GostBelegungsfehler.BIL_12);

		// Prüfe, ob ein zum bilingualen Bildungsgang zugehöriges Sachfach 3. oder 4. Abiturfach ist
		if (!manager.pruefeExistiertAbiFach(_biliSachfaecher, GostAbiturFach.AB3, GostAbiturFach.AB4))
			addFehler(GostBelegungsfehler.BIL_13);
	}



	/**
	 * Gibt die Anzahl der durchgehend schriftlich belegten bzw. belegbaren Fremdsprachen zurück.
	 * Durchgehend schriftlich bedeutet, dass das Fach mind. von EF.1 bis Q2.1 schriftlich belegt wurde.
	 * Hierfür kommen Fremdsprachen und ggf. ein bilinguales Sachfach infrage, dessen Unterrichtssprache
	 * nicht durchgehend schriftlich belegt.
	 *
	 * @return die Anzahl der durchgehend schriftlich belegten bzw. belegbaren Fremdsprachen zurück.
	 */
	public int getAnzahlDurchgehendSchritflichBelegt() {
		return _anzahlDurchgehendSchriftlich;
	}

}
