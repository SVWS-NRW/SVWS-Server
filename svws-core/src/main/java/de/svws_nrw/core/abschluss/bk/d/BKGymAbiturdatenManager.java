package de.svws_nrw.core.abschluss.bk.d;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.asd.data.schueler.Sprachbelegung;
import de.svws_nrw.asd.data.schule.BeruflichesGymnasiumPruefungsordnungAnlageKatalogEintrag;
import de.svws_nrw.asd.data.schule.BeruflichesGymnasiumStundentafel;
import de.svws_nrw.asd.types.schule.BeruflichesGymnasiumPruefungsordnungAnlage;
import de.svws_nrw.asd.types.schule.Schulgliederung;
import de.svws_nrw.core.abschluss.bk.d.markieren.BKGymAbiturMarkierungsalgorithmus;
import de.svws_nrw.core.data.bk.abi.BKGymAbiturFachbelegung;
import de.svws_nrw.core.data.bk.abi.BKGymAbiturMarkierungsalgorithmusErgebnis;
import de.svws_nrw.core.data.bk.abi.BKGymAbiturdaten;
import de.svws_nrw.core.data.bk.abi.BKGymBelegpruefungErgebnis;
import de.svws_nrw.core.data.bk.abi.BKGymFach;
import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.core.utils.bk.BKGymFaecherManager;
import de.svws_nrw.core.utils.schueler.SprachendatenUtils;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse stellt Methoden zur Verfügung um die angegebenen Abiturdaten zu bearbeiten und Auswertungen durchzuführen.
 */
public class BKGymAbiturdatenManager {

	/** Der Stundentafel-Manager */
	private final @NotNull BKGymStundentafelManager stundentafelManager;

	/** Der Fachbelegung-Manager */
	private final @NotNull BKGymFachbelegungManager fachbelegungManager;

	/** Die Abiturdaten des Schülers */
	private final @NotNull BKGymAbiturdaten abidaten;

	/** Die Schulgliederung des Bildungsgangs des Schülers */
	private final @NotNull Schulgliederung gliederung;

	/** Der Fachklassen-Schlüssel des Bildungsgangs des Schülers */
	private final @NotNull String fks;

	/** Die Anlage, die zur Schulgliederung und Fachklasse gehört */
	private final @NotNull BeruflichesGymnasiumPruefungsordnungAnlage anlage;

	/** Der Manager für die Fächer des beruflichen Gymnasiums */
	private final @NotNull BKGymFaecherManager faecherManager;

	/** Das Halbjahr, bis zu welchem die Belegprüfung durchgeführt werden soll */
	private final @NotNull GostHalbjahr bisHalbjahr;

	/** Ob eine zweite Fremdsprache in der SekI vier Jahre lang belegt wurde */
	private final boolean zweiteFremdspracheInSekIErfuellt;

	/** Der Belegprüfungsalgorithmus */
	private final @NotNull BKGymBelegpruefung belegpruefung;

	/** Der Markierungsalgorithmus */
	private final @NotNull BKGymAbiturMarkierungsalgorithmus markieren;

	/** Die Menge der Belegprüfungsfehler, die bei den durchgeführten Belegprüfungen aufgetreten sind. */
	private @NotNull List<BKGymBelegungsfehler> belegpruefungsfehler = new ArrayList<>();

	/** Gibt an, ob die Belegprüfung insgesamt erfolgreich war oder nicht. */
	private boolean belegpruefungErfolgreich = false;

	/** Das Ergebnis des Markierungsalgorithmus */
	private BKGymAbiturMarkierungsalgorithmusErgebnis ergebnisMarkierungsalgorithmus = null;




	/**
	 * Erstellt ein neues Manager-Objekt, welches mit den übergebenen Abiturdaten verknüpft wird.
	 *
	 * @param abidaten         die Abiturdaten des Schülers
	 * @param gliederung       die Schulgliederung des Bildungsgangs des Schülers
	 * @param fks              der fünfstellige Fachklassenschlüssel des Bildungsgangs des Schülers
	 * @param faecherManager   der Manager für die Fächer
	 * @param bisHalbjahr      die Art der Belegprüfung - bis zu welchem Halbjahr geprüft werden soll
	 */
	public BKGymAbiturdatenManager(final @NotNull BKGymAbiturdaten abidaten, final @NotNull Schulgliederung gliederung, final @NotNull String fks,
			final @NotNull BKGymFaecherManager faecherManager, final @NotNull GostHalbjahr bisHalbjahr) {
		this.abidaten = abidaten;
		this.gliederung = gliederung;
		this.fks = fks;
		this.faecherManager = faecherManager;
		this.fachbelegungManager = new BKGymFachbelegungManager(this);
		this.bisHalbjahr = bisHalbjahr;
		this.zweiteFremdspracheInSekIErfuellt = istZweiteFremdspracheInSekIErfuellt();
		this.anlage = bestimmeAnlage();
		final @NotNull List<BeruflichesGymnasiumStundentafel> tafeln = getStundentafeln();
		this.stundentafelManager = new BKGymStundentafelManager(fachbelegungManager, tafeln);
		this.belegpruefung = getBelegpruefung();
		this.markieren = new BKGymAbiturMarkierungsalgorithmus(this);
	}


		/**
	 * Führte die Schritte zur Belegprüfung aus
	 */
	private void belegPruefung() {
		this.belegpruefung.pruefe();
		this.belegpruefungsfehler = this.belegpruefung.getBelegungsfehler();
		this.belegpruefungErfolgreich = this.belegpruefung.istErfolgreich();
	}


	/**
	 * Markiert zuerst die Kurse und führt dann eine Prüfung der Zulassung durch
	 */
	private void zulassungsPruefung() {
		this.ergebnisMarkierungsalgorithmus = markieren.berechne();
	}


	/**
	 * Ermittelt die Anlage zu einer Fachklasse in der Schulgliederung D01.
	 *
	 * @return die Anlage
	 */
	private @NotNull BeruflichesGymnasiumPruefungsordnungAnlage bestimmeAnlage() {
		return switch (gliederung) {
			case D01 -> getAnlageD01();
			case D02 -> getAnlageD02();
			default ->
				throw new DeveloperNotificationException("Die Belegprüfung für die Schulgliederung " + gliederung.name() + " wird noch nicht unterstützt.");
		};
	}


	private @NotNull BeruflichesGymnasiumPruefungsordnungAnlage getAnlageD01() {
		return switch (fks) {
			case "10100" -> BeruflichesGymnasiumPruefungsordnungAnlage.D6;
			case "10200" -> BeruflichesGymnasiumPruefungsordnungAnlage.D1;
			case "10300" -> BeruflichesGymnasiumPruefungsordnungAnlage.D7;
			case "10400" -> BeruflichesGymnasiumPruefungsordnungAnlage.D8;
			case "10500" -> BeruflichesGymnasiumPruefungsordnungAnlage.D2;
			case "10600" -> BeruflichesGymnasiumPruefungsordnungAnlage.D3;
			case "10700" -> BeruflichesGymnasiumPruefungsordnungAnlage.D4;
			case "10900" -> BeruflichesGymnasiumPruefungsordnungAnlage.D12;
			case "11100" -> BeruflichesGymnasiumPruefungsordnungAnlage.D9;
			case "11200" -> BeruflichesGymnasiumPruefungsordnungAnlage.D13;
			case "11400" -> BeruflichesGymnasiumPruefungsordnungAnlage.D10;
			case "11500" -> BeruflichesGymnasiumPruefungsordnungAnlage.D3a;
			default -> throw new DeveloperNotificationException("Die Belegprüfung für die Schulgliederung " + gliederung.name()
					+ " und den Fachklassenschlüssel " + fks + " wird noch nicht unterstützt.");
		};
	}


	private @NotNull BeruflichesGymnasiumPruefungsordnungAnlage getAnlageD02() {
		return switch (fks) {
			case "10100" -> BeruflichesGymnasiumPruefungsordnungAnlage.D14;
			case "10200" -> BeruflichesGymnasiumPruefungsordnungAnlage.D27;
			case "10300" -> BeruflichesGymnasiumPruefungsordnungAnlage.D22;
			case "10400" -> BeruflichesGymnasiumPruefungsordnungAnlage.D23;
			case "10600" -> BeruflichesGymnasiumPruefungsordnungAnlage.D25;
			case "10700" -> BeruflichesGymnasiumPruefungsordnungAnlage.D15;
			case "10900" -> BeruflichesGymnasiumPruefungsordnungAnlage.D19;
			case "11000" -> BeruflichesGymnasiumPruefungsordnungAnlage.D16;
			case "11100" -> BeruflichesGymnasiumPruefungsordnungAnlage.D17;
			case "11300" -> BeruflichesGymnasiumPruefungsordnungAnlage.D18;
			case "11400" -> BeruflichesGymnasiumPruefungsordnungAnlage.D20;
			case "11500" -> BeruflichesGymnasiumPruefungsordnungAnlage.D21;
			case "12000" -> BeruflichesGymnasiumPruefungsordnungAnlage.D17a;
			case "12100" -> BeruflichesGymnasiumPruefungsordnungAnlage.D15a;
			case "12200" -> BeruflichesGymnasiumPruefungsordnungAnlage.D28;
			default -> throw new DeveloperNotificationException("Die Belegprüfung für die Schulgliederung " + gliederung.name()
					+ " und den Fachklassenschlüssel " + fks + " wird noch nicht unterstützt.");
		};
	}


	/**
	 * Erstellt die zugehörige Belegprüfung mit den Abiturdaten anhand des übergebenen Bildungsganges.
	 *
	 * @return der Belegprüfungsalgorithmus
	 */
	private @NotNull BKGymBelegpruefung getBelegpruefung() {
		return new BKGymBelegpruefung(this);
	}


	/**
	 * Getter für den Zugriff auf den Manager der Fachbelegung
	 *
	 * @return den fachbelegungManager
	 */
	public final @NotNull BKGymFachbelegungManager getFachbelegungManager() {
		return fachbelegungManager;
	}


	/**
	 * Getter für den Zugriff auf den Manager der Stundentafeln
	 *
	 * @return den stundentafelManager
	 */
	public final @NotNull BKGymStundentafelManager getStundentafelManager() {
		return stundentafelManager;
	}


	/**
	 * Getter für den Zugriff auf den Fächer-Manager
	 *
	 * @return den faecherManager
	 */
	public final @NotNull BKGymFaecherManager getFaecherManager() {
		return faecherManager;
	}


	/**
	 * Getter für den Zugriff auf die Abiturdaten
	 *
	 * @return die Abiturdaten
	 */
	public @NotNull BKGymAbiturdaten getAbidaten() {
		return abidaten;
	}


	/**
	 * Getter für den Zugriff auf die Schulgliederung des Bildungsganges
	 *
	 * @return die Schulgliederung des Bildungsganges
	 */
	public @NotNull Schulgliederung getGliederung() {
		return gliederung;
	}


	/**
	 * Getter für den Zugriff auf den Fachklassenschlüssel des Bildungsganges
	 *
	 * @return der Fachklassenschlüssel des Bildungsganges
	 */
	public @NotNull String getFachklassenschluessel() {
		return fks;
	}


	/**
	 * Getter für den Zugriff auf den Status der zweiten Fremdsprache
	 *
	 * @return ob die zweite Fremdsprache in der SI ausreichend belegt war.
	 */
	public boolean getZweiteFremdspracheInSekIErfuellt() {
		return zweiteFremdspracheInSekIErfuellt;
	}


	/**
	 * Prüft ob eine Facharbeit vorhanden ist
	 * Das Fach wird hier nicht einbezogen, sondern beim Markieren geprüft
	 *
	 * @return true, wenn Facharbeit vorhanden ist, sonst false
	 */
	public boolean istFacharbeitVorhanden() {
		final Integer notenpunkte = getAbidaten().facharbeitNotenpunkte;
		return (notenpunkte != null) && (notenpunkte > 0);
	}


	/**
	 * Getter für den Zugriff auf das Schuljahr in dem das Abitur stattfindet
	 *
	 * @return das Schuljahr des Abiturs
	 */
	public int getSchuljahrAbitur() {
		return this.abidaten.schuljahrAbitur;
	}


	/**
	 * Gibt das Ergebnis der Belegprüfung zurück. Dieses enthält eine Liste der Fehler, die bei der Belegprüfung
	 * festgestellt wurden und ob diese erfolgreich gewesen ist oder nicht.
	 *
	 * @return das Ergebnis der Belegprüfung
	 */
	public @NotNull BKGymBelegpruefungErgebnis getBelegpruefungErgebnis() {
		belegPruefung();
		final @NotNull BKGymBelegpruefungErgebnis ergebnis = new BKGymBelegpruefungErgebnis();
		ergebnis.erfolgreich = belegpruefungErfolgreich;
		for (int i = 0; i < belegpruefungsfehler.size(); i++) {
			final @NotNull BKGymBelegungsfehler fehler = belegpruefungsfehler.get(i);
			ergebnis.fehlercodes.add(new BKGymBelegpruefungErgebnisFehler(fehler));
		}
		return ergebnis;
	}


	/**
	 * Gibt das Ergebnis des Markierungsalgorithmus zurück. Dieses enthält, ob der Algorithmus erfolgreich gewesen ist
	 * und im Fehlerfall den Log des Ergebnisses.
	 *
	 * @return das Ergebnis der Markierungsalgorithmus
	 */
	public @NotNull BKGymAbiturMarkierungsalgorithmusErgebnis getErgebnisMarkierungsalgorithmus() {
		zulassungsPruefung();
		if (this.ergebnisMarkierungsalgorithmus == null)
			return new BKGymAbiturMarkierungsalgorithmusErgebnis();
		return this.ergebnisMarkierungsalgorithmus;
	}


	/**
	 * liefert zu einer fachID die Fachbezeichnung
	 *
	 * @param id   die ID des Fachs
	 *
	 * @return die Fachbezeichnung
	 */
	public @NotNull String getBezeichnungByFachID(final long id) {
		return faecherManager.getBezeichnungByFachID(id);
	}

	/**
	 * Prüft, ob es sich bei der Fachbelegung um eine Belegung einer Fremdsprache handelt.
	 *
	 * @param fb   die Fachbelegung
	 *
	 * @return true, wenn es sich um eine Fremdsprachenbelegung handelt, und ansonsten false
	 */
	public boolean istFremdsprachenbelegung(final @NotNull BKGymAbiturFachbelegung fb) {
		// Prüfe, ob das Fach in der Fächerliste des Abiturjahrgangs überhaupt existiert
		final BKGymFach fbFach = faecherManager.get(fb.fachID);
		if ((fbFach == null) || (fbFach.bezeichnung == null))
			return false;
		return fbFach.istFremdsprache;
	}


	/**
	 * Liefert die Stundentafeln, die zur APO-BK-Anlage dieses Managers gehören
	 *
	 * @return die Liste der Stundentafeln
	 */
	public @NotNull List<BeruflichesGymnasiumStundentafel> getStundentafeln() {
		// Bestimme die Liste der für die Prüfungsordnung möglichen Stundentafeln - Gebe im Fehlerfall eine leere Liste zurück
		final @NotNull List<BeruflichesGymnasiumStundentafel> result = new ArrayList<>();
		final int schuljahr = getSchuljahrAbitur();
		final BeruflichesGymnasiumPruefungsordnungAnlageKatalogEintrag poke = anlage.daten(schuljahr);
		if (poke == null)
			return result;
		return poke.stundentafeln;
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
	 * Ermittelt, ob in der SekI eine zweite Fremdsprache über vier Jahre belegt wurde anhand der Sprachdaten in
	 * den AbiDaten.
	 *
	 * @return true, wenn die Belegung einer zweiten Fremdsprache nicht ununterbrochen über vier Jahre belegt war.
	 */
	private boolean istZweiteFremdspracheInSekIErfuellt() {
		for (final @NotNull Sprachbelegung belegung : abidaten.sprachendaten.belegungen) {
			if ((belegung.reihenfolge == null) || (belegung.belegungVonJahrgang == null) || (belegung.belegungBisJahrgang == null)
					|| (belegung.belegungVonAbschnitt == null) || (belegung.belegungBisAbschnitt == null))
				continue;
			if (!belegung.sprache.equals("E")) {
				int anzHalbjahre = (SprachendatenUtils.getJahrgangNumerisch(belegung.belegungBisJahrgang)
						- SprachendatenUtils.getJahrgangNumerisch(belegung.belegungVonJahrgang) + 1) * 2;
				anzHalbjahre += belegung.belegungBisAbschnitt - belegung.belegungVonAbschnitt - 1;
				if (anzHalbjahre >= 8)
					return true;
			}
		}
		return false;
	}
}
