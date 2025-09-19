package de.svws_nrw.asd.validate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import de.svws_nrw.asd.adt.PairNN;
import de.svws_nrw.asd.data.CoreTypeData;
import de.svws_nrw.asd.data.CoreTypeException;
import de.svws_nrw.asd.types.schule.Schulform;
import jakarta.validation.constraints.NotNull;

/**
 * Ein Manager, um die Validatoren zu verwalten. Die Fehlerart-Kontexte
 * der Validatoren werden aus einer JSON-Datei geladen und entsprechende
 * Methoden zur Abfrage und Änderung bereit gestellt.
 */
public final class ValidatorManager {

	/** Die Version der Fehlerart-Kontexte */
	private static long _version;

	/** Die Fehlerart-Kontexte für jeden Validator als Historienliste */
	private static @NotNull Map<String, List<ValidatorFehlerartKontext>> _data;

	/** Die ValidatorManager pro Schulform für den SVWS-Kontext */
	private static @NotNull Map<Schulform, ValidatorManager> _managerSVWS = new HashMap<>();

	/** Die ValidatorManager pro Schulform für deb Zebras-Kontext */
	private static @NotNull Map<Schulform, ValidatorManager> _managerZebras = new HashMap<>();

	/** Die Schulform, für den der ValidatorManager gilt */
	private final @NotNull Schulform _schulform;

	/** Die Umgebung, für den der ValidatorManager erzeugt wurde: true = ZeBrAS ; false = SVWS */
	private final boolean _isZebras;


	/* ----- Die nachfolgenden Attribute werden nicht initialisiert und werden als Cache verwendet, um z.B. den Schuljahres-bezogenen Zugriff zu cachen ----- */

	/** Eine geschachtelte Map, die einem Schuljahr eine Map mit der Zuordnung der Validatoren zu den Prüfschritten und deren Fehlerarten für die Schulform _schulform */
	private final @NotNull HashMap<Integer, HashMap<String, HashMap<Integer, ValidatorFehlerart>>> _mapSchuljahrValidatornameToFehlerart = new HashMap<>();

	/** Eine geschachtelte Map, die einem Schuljahr eine Map mit der Zuordnung der Validatoren zu den Fehlercode-Präfixen des Validators */
	private final @NotNull HashMap<Integer, HashMap<String, String>> _mapSchuljahrValidatornameToFehlercodePraefix = new HashMap<>();


	/**
	 * Erstellt einen neuen Manager für die übergebene Schulform und die Entsprechene Validierungsumgebung
	 * (Zebras oder SVWS)
	 *
	 * @param zebras      die Umgebung, in der gerade validiert wird: true: ZeBrAS  false: SVWS
	 * @param schulform   die Schulform, für die gerade
	 */
	private ValidatorManager(final @NotNull Schulform schulform, final boolean zebras) {
		this._schulform = schulform;
		this._isZebras = zebras;
	}


	/**
	 * Initialisierung des Validators mit den Daten, die aus einem json eingelesen wurden.
	 *
	 * @param version	Die Versionsnummer der Daten zu den Fehlerart-Kontexten.
	 * @param data		Die aus der JSON-Datei eingelesenen Daten.
	 */
	public static void init(final long version, @NotNull final Map<String, List<ValidatorFehlerartKontext>> data) {
		_version = version;
		_data = data;
		_managerSVWS = new HashMap<>();
		_managerZebras = new HashMap<>();

		// TODO Überprüfung ob alle Validatoren in der Json aufgeführt sind

		// Führe Prüfungen auf die einzelnen Einträge zu den Fehlerarten durch
		for (final Entry<String, List<ValidatorFehlerartKontext>> entry : _data.entrySet()) {
			final @NotNull String validatorName = entry.getKey();
			final @NotNull List<ValidatorFehlerartKontext> list = entry.getValue();

			// Überprüfung ob die Schulformen, die eingetragen sind, zu dem entsprechenden Zeitpunkt beim Core-Type überhaupt gültig sind
			// Hierbei wird sich an der Default-Definition für die Prüfschritte orientiert
			final @NotNull HashMap<String, List<PairNN<Integer, Integer>>> mapZeitraeumeBySchulform = new HashMap<>();
			for (final @NotNull ValidatorFehlerartKontext eintrag : list) {
				ValidatorFehlerartKontextPruefschritt prfDefault = null; // Die Default-Defintion der Fehlerarberten bei Prüfschritten
				for (final @NotNull ValidatorFehlerartKontextPruefschritt prf : eintrag.pruefschritte) {
					if (prf.nummer < -1)
						throw new CoreTypeException(
								"Fehler bei der Definition der Prüfschritte. Der Validator %s hat eine Nummer für einen Prüfschritt angegeben, der kleiner als -1 ist."
										.formatted(validatorName));
					if (prf.nummer == -1) {
						prfDefault = prf;
						final @NotNull PairNN<Integer, Integer> zeitraum = createZeitraum(eintrag.gueltigVon, eintrag.gueltigBis);
						addZeitraum(mapZeitraeumeBySchulform, zeitraum, prf.muss);
						addZeitraum(mapZeitraeumeBySchulform, zeitraum, prf.kann);
						addZeitraum(mapZeitraeumeBySchulform, zeitraum, prf.hinweis);
					}
				}
				if (prfDefault == null)
					throw new CoreTypeException("Fehler bei der Definition der Prüfschritte. Der Validator %s hat keine Default-Definition für Prüfschritte."
							.formatted(validatorName));
			}
			for (final Entry<String, List<PairNN<Integer, Integer>>> zeitraeume : mapZeitraeumeBySchulform.entrySet()) {
				final @NotNull List<CoreTypeData> l = new ArrayList<>();
				final Schulform sf = Schulform.valueOf(zeitraeume.getKey());
				if (sf != null)
					l.addAll(sf.historie());
				if (!pruefeAufZeitraumueberdeckung(validatorName, createSchulformZeitraumListe(l), zeitraeume.getValue()))
					throw new CoreTypeException(
							"Fehler beim Prüfen der Schulform. Der Validator %s hat ungültige Schulform-Zeitraum-Kombinationen.".formatted(validatorName));
			}
		}
	}


	/**
	 * Gibt den Manager für die Schulform und Umgebung zurück, wobei er erzeugt wird, wenn
	 * er nicht existiert.
	 *
	 * @param schulform  die Schulform, für die der Manager benötigt wird
	 * @param isZebras   die entsprechende Umgebung
	 *
	 * @return der Validator-Manager
	 */
	public static @NotNull ValidatorManager getManager(@NotNull final Schulform schulform, final boolean isZebras) {
		if (isZebras) {
			ValidatorManager vm = _managerZebras.get(schulform);
			if (vm == null) {
				vm = new ValidatorManager(schulform, true);
				_managerZebras.put(schulform, vm);
			}
			return vm;
		}

		ValidatorManager vm = _managerSVWS.get(schulform);
		if (vm == null) {
			vm = new ValidatorManager(schulform, false);
			_managerSVWS.put(schulform, vm);
		}
		return vm;
	}


	/**
	 * Gibt die Version der Fehler-Kontext-Daten zurück.
	 *
	 * @return die Version
	 */
	public static long getVersion() {
		return _version;
	}


	/**
	 * Gibt die Liste der Validatorennamen als nicht-leeres Set zurück.
	 *
	 * @return das nicht-leeres Set der Validatoren-Namen
	 */
	public static @NotNull Set<String> getValidatornamenAsSet() {
		return _data.keySet();
	}


	/**
	 * Gibt die Historie der Fehlerart-Kontexte für den angegebenen Validator zurück.
	 *
	 * @param validator   der kanonische Name des Validators
	 *
	 * @return die Historie
	 */
	public static @NotNull List<ValidatorFehlerartKontext> getValidatorHistorie(final @NotNull String validator) {
		final List<ValidatorFehlerartKontext> tmp = _data.get(validator);
		if (tmp == null)
			throw new CoreTypeException("Der Validator " + validator + " existiert nicht in 'validatoren.json'.");
		return tmp;
	}


	/**
	 * Liefert für das angegebene Schuljahr die Map von dem Validatornamen zu der Fehlerart.
	 * Ist der Cache für das Schuljahr noch nicht aufgebaut, so wird dieser erstellt.
	 *
	 * @param schuljahr   das zu prüfende Schuljahr
	 *
	 * @return die Map, die für das gegebene Schuljahr die Fehlerart pro Validator enthält
	 */
	private @NotNull HashMap<String, HashMap<Integer, ValidatorFehlerart>> getValidatornameToFehlerartCache(final int schuljahr) {
		final @NotNull HashMap<String, HashMap<Integer, ValidatorFehlerart>> mapValidatorToFehlerart = computeIfAbsentValidatornameToFehlerart(schuljahr);
		// Prüfe, ob die Einträge im Cache sind. Wenn nicht, dann erzeuge die Daten im Cache
		if (mapValidatorToFehlerart.isEmpty())
			createCache(schuljahr);
		return mapValidatorToFehlerart;
	}


	/**
	 * Holt das Objekt aus der HashMap oder erzeugt es wenn es nicht vorhanden ist.
	 *
	 * @param schuljahr  das Schuljahr, für das das Objekt geholt wird
	 *
	 * @return das benötigte Objekt
	 */
	private @NotNull HashMap<String, HashMap<Integer, ValidatorFehlerart>> computeIfAbsentValidatornameToFehlerart(final int schuljahr) {
		HashMap<String, HashMap<Integer, ValidatorFehlerart>> mapValidatorToFehlerart = _mapSchuljahrValidatornameToFehlerart.get(schuljahr);
		if (mapValidatorToFehlerart == null) {
			mapValidatorToFehlerart = new HashMap<>();
			_mapSchuljahrValidatornameToFehlerart.put(schuljahr, mapValidatorToFehlerart);
		}
		return mapValidatorToFehlerart;
	}


	/**
	 * Liefert für das angegebene Schuljahr die Map von dem Validatornamen zu dem Fehlercode-Präfix.
	 * Ist der Cache für das Schuljahr noch nicht aufgebaut, so wird dieser erstellt.
	 *
	 * @param schuljahr   das zu prüfende Schuljahr
	 *
	 * @return die Map, die für das gegebene Schuljahr das Fehlercode-Präfix pro Validator enthält
	 */
	private @NotNull HashMap<String, String> getValidatornameToFehlercodePraefixCache(final int schuljahr) {
		final @NotNull HashMap<String, String> mapValidatorToFehlercodePraefix = computeIfAbsentValidatornameToFehlercodePraefix(schuljahr);
		// Prüfe, ob die Einträge im Cache sind. Wenn nicht, dann erzeuge die Daten im Cache
		if (mapValidatorToFehlercodePraefix.isEmpty())
			createCache(schuljahr);
		return mapValidatorToFehlercodePraefix;
	}


	/**
	 * Holt das Objekt aus der HashMap oder erzeugt es wenn es nicht vorhanden ist.
	 *
	 * @param schuljahr  das Schuljahr, für das das Objekt geholt wird
	 *
	 * @return das benötigte Objekt
	 */
	private @NotNull HashMap<String, String> computeIfAbsentValidatornameToFehlercodePraefix(final int schuljahr) {
		HashMap<String, String> mapValidatorToFehlercodePraefix = _mapSchuljahrValidatornameToFehlercodePraefix.get(schuljahr);
		if (mapValidatorToFehlercodePraefix == null) {
			mapValidatorToFehlercodePraefix = new HashMap<>();
			_mapSchuljahrValidatornameToFehlercodePraefix.put(schuljahr, mapValidatorToFehlercodePraefix);
		}
		return mapValidatorToFehlercodePraefix;
	}


	/**
	 * Holt das Objekt aus der HashMap oder erzeugt es wenn es nicht vorhanden ist.
	 *
	 * @param art - die Fehlerart, für die die Liste ggfs. erzeugt wird
	 * @param map - die HashMap mit den ArrayLists
	 *
	 * @return das benötigte Objekt
	 */
	private static @NotNull List<String> computeIfAbsentFehlerartValidator(final @NotNull ValidatorFehlerart art,
			final @NotNull Map<ValidatorFehlerart, List<String>> map) {
		List<String> list = map.get(art);
		if (list == null) {
			list = new ArrayList<>();
			map.put(art, list);
		}
		return list;
	}


	/**
	 * holt das Objekt aus der HashMap oder erzeugt es wenn es nicht vorhanden ist.

	 * @param schulform - die Fehlerart, für die die Liste ggfs. erzeugt wird
	 * @param map - die HashMap mit den ArrayLists
	 * @return das benötigte Objekt
	 */
	private static @NotNull List<PairNN<Integer, Integer>> computeIfAbsentZeitraeumeSchulform(final @NotNull String schulform,
			final @NotNull HashMap<String, List<PairNN<Integer, Integer>>> map) {
		List<PairNN<Integer, Integer>> list = map.get(schulform);
		if (list == null) {
			list = new ArrayList<>();
			map.put(schulform, list);
		}
		return list;
	}


	/**
	 * Erstellt den Cache für das angegeben Schuljahr.
	 *
	 * @param schuljahr   das Schuljahr
	 */
	private void createCache(final int schuljahr) {
		final @NotNull HashMap<String, HashMap<Integer, ValidatorFehlerart>> mapValidatorToFehlerart = computeIfAbsentValidatornameToFehlerart(schuljahr);
		mapValidatorToFehlerart.clear();
		final @NotNull HashMap<String, String> mapValidatorToFehlercodePraefix = computeIfAbsentValidatornameToFehlercodePraefix(schuljahr);
		mapValidatorToFehlercodePraefix.clear();

		// Definiert ein Set zur Erkennung von Duplikaten bei den Fehlercode-Präfixen der Validatoren zu erkennen
		final Set<String> praefixe = new HashSet<>();
		for (final Entry<String, List<ValidatorFehlerartKontext>> entry : _data.entrySet()) {
			final @NotNull String validatorName = entry.getKey();
			final @NotNull List<ValidatorFehlerartKontext> list = entry.getValue();
			final @NotNull HashMap<Integer, ValidatorFehlerart> mapPruefschrittToFehlerart = new HashMap<>();
			mapValidatorToFehlerart.put(validatorName, mapPruefschrittToFehlerart);

			for (final @NotNull ValidatorFehlerartKontext eintrag : list) {
				// Überprüfe bei den einzelnen Prüfschritten die Zuordnung der Fehlerarten bei der Schulform
				for (final @NotNull ValidatorFehlerartKontextPruefschritt prf : eintrag.pruefschritte) {
					final boolean hasHart = prf.muss.contains(_schulform.name());
					final boolean hasMuss = prf.kann.contains(_schulform.name());
					final boolean hasHinweis = prf.hinweis.contains(_schulform.name());
					if ((hasHart && hasMuss) || (hasMuss && hasHinweis) || (hasHart && hasHinweis))
						throw new CoreTypeException(
								"Ein Validator kann bei einer Schulform nicht bei einem Prüfschritt gleichzeitig bei mehreren Fehlerarten aktiv sein.");
				}

				// ... überprüfe Umgebung und Schuljahr ...
				final boolean validatorAktivInUmgebungUndSchuljahr = (_isZebras ? eintrag.zebras : eintrag.svws)
						&& ((eintrag.gueltigVon == null) || (eintrag.gueltigVon <= schuljahr))
						&& ((eintrag.gueltigBis == null) || (schuljahr <= eintrag.gueltigBis));
				if (validatorAktivInUmgebungUndSchuljahr) {
					// ... ob das Fehlercode-Präfix eindeutig ist (aber nur für den Historieneinträge des Schuljahres des gleichen Core-Type-Wertes)
					if (praefixe.contains(eintrag.praefix))
						throw new CoreTypeException(
								"Das Fehlercode-Präfix eines Validators muss eindeutig sein. Das Präfix %s wurde mehrfach verwendet."
										.formatted(eintrag.praefix));
					praefixe.add(eintrag.praefix);
					mapValidatorToFehlercodePraefix.put(validatorName, eintrag.praefix);
					for (final @NotNull ValidatorFehlerartKontextPruefschritt prf : eintrag.pruefschritte) {
						// ... und befülle den Cache
						final boolean hasHart = prf.muss.contains(_schulform.name());
						final boolean hasMuss = prf.kann.contains(_schulform.name());
						final boolean hasHinweis = prf.hinweis.contains(_schulform.name());
						if (hasHart)
							mapPruefschrittToFehlerart.put(prf.nummer, ValidatorFehlerart.MUSS);
						else if (hasMuss)
							mapPruefschrittToFehlerart.put(prf.nummer, ValidatorFehlerart.KANN);
						else if (hasHinweis)
							mapPruefschrittToFehlerart.put(prf.nummer, ValidatorFehlerart.HINWEIS);
						else
							mapPruefschrittToFehlerart.put(prf.nummer, ValidatorFehlerart.UNGENUTZT);
					}
				}
			}
		}
	}


	/**
	 * Gibt die Fehlerart eines Validators für das angegebene Schuljahr zurück.
	 *
	 * @param schuljahr      das Schuljahr
	 * @param validator      der kanonische Name des Validators
	 * @param pruefschritt   die Nummer des Prüfschrittes
	 *
	 * @return die Fehlerart des Validators für das angegebene Schuljahr
	 */
	public ValidatorFehlerart getFehlerartBySchuljahrAndValidatorNameAndPruefschritt(final int schuljahr, final @NotNull String validator,
			final int pruefschritt) {
		if (pruefschritt < -1)
			return null;
		final HashMap<Integer, ValidatorFehlerart> mapPruefschritt = getValidatornameToFehlerartCache(schuljahr).get(validator);
		if (mapPruefschritt == null)
			return null;
		if (pruefschritt >= 0) {
			final ValidatorFehlerart art = mapPruefschritt.get(pruefschritt);
			if (art != null)
				return art;
		}
		final ValidatorFehlerart art = mapPruefschritt.get(-1);
		return (art == null) ? ValidatorFehlerart.UNGENUTZT : art;
	}


	/**
	 * Gibt die Fehlerart eines Validators für das angegebene Schuljahr zurück.
	 *
	 * @param <T>            der Type des Validators
	 * @param schuljahr      das Schuljahr
	 * @param validator      die Klasse des Validators
	 * @param pruefschritt   die Nummer des Prüfschrittes
	 *
	 * @return die Fehlerart des Validators für das angegebene Schuljahr
	 */
	public <T extends Validator> @NotNull ValidatorFehlerart getFehlerartBySchuljahrAndValidatorClassAndPruefschritt(final int schuljahr,
			final @NotNull Class<T> validator, final int pruefschritt) {
		final ValidatorFehlerart tmp = getFehlerartBySchuljahrAndValidatorNameAndPruefschritt(schuljahr, validator.getCanonicalName(), pruefschritt);
		return (tmp == null) ? ValidatorFehlerart.UNGENUTZT : tmp;
	}


	/**
	 * Setzt die Fehlerart eines Prüfschrittes eines Validators für das angegebene Schuljahr.
	 *
	 * @param schuljahr      das Schuljahr
	 * @param validator      der kanonische Name des Validators
	 * @param fehlerart      die Fehlerart des Validators
	 * @param pruefschritt   die Nummer des Prüfschrittes
	 */
	public void setFehlerartBySchuljahr(final int schuljahr, final @NotNull String validator, final @NotNull ValidatorFehlerart fehlerart,
			final int pruefschritt) {
		// Ändere Cache ValidatornameToFehlerart
		final @NotNull HashMap<String, HashMap<Integer, ValidatorFehlerart>> mapValidator = getValidatornameToFehlerartCache(schuljahr);
		HashMap<Integer, ValidatorFehlerart> mapPruefschritt = mapValidator.get(validator);
		if (mapPruefschritt == null) {
			mapPruefschritt = new HashMap<>();
			mapValidator.put(validator, mapPruefschritt);
		}
		mapPruefschritt.put(pruefschritt, fehlerart);
	}


	/**
	 * Prüft, ob der übergebene Validator in dem angegebenen Schuljahr aktiv ist oder nicht.
	 *
	 * @param schuljahr      das Schuljahr
	 * @param validator      der kanonische Name des Validators
	 *
	 * @return true, falls der Validator in dem Schuljahr aktiv ist.
	 */
	public boolean isValidatorActiveInSchuljahr(final int schuljahr, final @NotNull String validator) {
		final @NotNull HashMap<String, HashMap<Integer, ValidatorFehlerart>> mapValidator = getValidatornameToFehlerartCache(schuljahr);
		final HashMap<Integer, ValidatorFehlerart> mapPruefschritt = mapValidator.get(validator);
		if (mapPruefschritt == null)
			return false;
		final ValidatorFehlerart fa = mapPruefschritt.get(-1);
		return (fa != null) && (fa != ValidatorFehlerart.UNGENUTZT);
	}


	/**
	 * Prüft, ob der übergebene Prüfschritt des übergebenen Validators in dem angegebenen Schuljahr aktiv ist oder nicht.
	 *
	 * @param schuljahr      das Schuljahr
	 * @param validator      der kanonische Name des Validators
	 * @param pruefschritt   die Nummer des Prüfschrittes
	 *
	 * @return true, falls der Validator in dem Schuljahr aktiv ist.
	 */
	public boolean isPruefschrittActiveInSchuljahr(final int schuljahr, final @NotNull String validator, final int pruefschritt) {
		final @NotNull HashMap<String, HashMap<Integer, ValidatorFehlerart>> mapValidator = getValidatornameToFehlerartCache(schuljahr);
		final HashMap<Integer, ValidatorFehlerart> mapPruefschritt = mapValidator.get(validator);
		if (mapPruefschritt == null)
			return false;
		final ValidatorFehlerart fa = mapPruefschritt.get(pruefschritt);
		return (fa != null) && (fa != ValidatorFehlerart.UNGENUTZT);
	}


	/**
	 * Gibt das Fehlercode-Präfix eines Validators für das angegebene Schuljahr zurück.
	 *
	 * @param schuljahr      das Schuljahr
	 * @param validator      der kanonische Name des Validators
	 *
	 * @return das Fehlercode-Präfix des Validators für das angegebene Schuljahr
	 */
	public @NotNull String getFehlercodePraefixBySchuljahrAndValidatorName(final int schuljahr, final @NotNull String validator) {
		final String code = getValidatornameToFehlercodePraefixCache(schuljahr).get(validator);
		if (code == null)
			throw new ValidatorException("Fehler beim Zugriff auf den Fehlercode-Präfix für den Validator %s im Schuljahr %d.".formatted(validator, schuljahr));
		return code;
	}


	/**
	 * Gibt das Fehlercode-Präfix eines Validators für das angegebene Schuljahr zurück.
	 *
	 * @param <T>            der Type des Validators
	 * @param schuljahr      das Schuljahr
	 * @param validator      die Klasse des Validators
	 *
	 * @return das Fehlercode-Präfix des Validators für das angegebene Schuljahr
	 */
	public <T extends Validator> @NotNull String getFehlercodePraefixBySchuljahrAndValidatorClass(final int schuljahr, final @NotNull Class<T> validator) {
		return getFehlercodePraefixBySchuljahrAndValidatorName(schuljahr, validator.getCanonicalName());
	}


	/**
	 * Trägt aus der Liste von Schulformen den angegebenen Zeitraum in die Liste Zeiträume der jeweiligen Schulform ein.
	 *
	 * @param mapZeitraeumeBySchulform		Die map, die für jede Schulform die Liste der gültigen Zeiträume speichert
	 * @param zeitraum						Ein Zeitraum, in dem die Schulformen in der Liste schulformen gültig sind
	 * @param schulformen					Die Liste der in dem Zeitraum gültigen Schulformen.
	 */
	private static void addZeitraum(final @NotNull HashMap<String, List<PairNN<Integer, Integer>>> mapZeitraeumeBySchulform,
			final @NotNull PairNN<Integer, Integer> zeitraum, final @NotNull List<String> schulformen) {
		for (final @NotNull String schulform : schulformen) {
			final @NotNull List<PairNN<Integer, Integer>> zeitraeumeBySchulform = computeIfAbsentZeitraeumeSchulform(schulform, mapZeitraeumeBySchulform);
			zeitraeumeBySchulform.add(zeitraum);
		}
	}


	/**
	 * Bildet aus der Historie der Schulformen eine Liste der Zeiträume.
	 *
	 * @param historie      die Historie der Schulformen
	 *
	 * @return die Liste der Zeiträume
	 */
	private static @NotNull List<PairNN<Integer, Integer>> createSchulformZeitraumListe(final @NotNull List<CoreTypeData> historie) {
		final @NotNull List<PairNN<Integer, Integer>> zeitraeume = new ArrayList<>();
		for (final @NotNull CoreTypeData eintrag : historie)
			zeitraeume.add(createZeitraum(eintrag.gueltigVon, eintrag.gueltigBis));
		return zeitraeume;
	}


	/**
	 * Prüft ob die Zeiträumen der zweiten Liste komplett innerhalb der Zeiträume der ersten Liste liegen. In diesem Zusammenhang wird geprüft,
	 * ob alle Zeiträume, wo ein Validator gültig sein soll auch durch die Gültigkeit bei der entsprechenden Schulform abgedeckt ist. <br>
	 * <br>
	 * Kurzbeschreibung des Algorithmus: <br>
	 *
	 * Beide Zeitstrahlen können als ggfs. unterbrochene Linien aufgefasst werden. Dort wo der Zeitstrahl 'obermenge' unterbrochen ist, muss
	 * der Zeitstrahl 'untermenge' auch unterbrochen sein. Falls nicht wird false zurückgegeben. <br>
	 * <br>
	 * Gültige Beispiele für 'obermenge' enthält 'untermenge': <br>
	 * Zeitstrahl obermenge:  a) -------   b) --------   ----------   c) --------   ---------- <br>
	 * Zeitstrahl untermenge:    -------       -----                       -----    ---------- <br>
	 * scanPoints:               ^     ^      ^^   ^ .   .        .      ^ ^   ^^   ^        ^   // . werden nicht mehr geprüft, da Ergebnis fest steht <br>
	 * <br>
	 * Ungültige Beispiele für 'obermenge' enthält 'untermenge' <br>
	 * Zeitstrahl obermenge:  a)   -----   b) --------   ----------   c) --------   ---------- <br>
	 * Zeitstrahl untermenge:    -------       --------                   -----    ---------- <br>
	 * scanPoints:               ^ .   .      ^^     ^^  .        .      ^^   ^ ^  ^.       ..   // . werden nicht mehr geprüft, da Ergebnis fest steht <br>
	 * <br>
	 * Der Position des Scanpoints wird für das Verfahren nicht benötigt (Es ist immer der kleinere der beiden Punkte die mit iObermenge und iUntermenge
	 * referenziert werden.) <br>
	 *
	 * @param validatorName   der Name des Validators
	 * @param obermenge       die Liste der Zeiträume, die die Zeiträume der Untermenge beinhaltet
	 * @param untermenge      die Liste der Zeiträume, die überprüft wird, ob sie in der Liste der Obermenge beinhaltet ist.
	 *
	 * @return true, falls untermenge wirklich eine Untermenge von Obermenge ist und ansonsten false
	 */
	private static boolean pruefeAufZeitraumueberdeckung(final @NotNull String validatorName,
			final @NotNull List<PairNN<Integer, Integer>> obermenge,
			final @NotNull List<PairNN<Integer, Integer>> untermenge) {
		// leere Listen beinhalten nichts -> return false, falls untermenge nicht auch leer ist, sonst true
		if (obermenge.isEmpty())
			return untermenge.isEmpty();

		final List<Integer> listObermenge = getZeitraumListe(validatorName, obermenge);
		final List<Integer> listUntermenge = getZeitraumListe(validatorName, untermenge);
		int iObermenge = 0;
		int iUntermenge = 0;
		do {
			if (iUntermenge >= listUntermenge.size())
				return true;
			if (iObermenge >= listObermenge.size())
				return false;
			// zum nächsten Scanpoint wechseln
			if (listObermenge.get(iObermenge).intValue() == listUntermenge.get(iUntermenge).intValue()) {
				iObermenge++;
				iUntermenge++;
			} else {
				if (listObermenge.get(iObermenge) < listUntermenge.get(iUntermenge))
					iObermenge++;
				else
					iUntermenge++;
			}
			// Test am Scanpoint, Abbruch sobald feststeht, dass untermenge keine Untermenge ist.
			// Ergibt die modulo-2 Berechnung des Index an dieser Stelle am Sccanpoint das Ergebnis 0
			// liegt eine Lücke vor und der Index zeigt auf das Ende der Lücke.
			// Ergibt die modulo-2 Berechnung des Index an dieser Stelle am Sccanpoint das Ergebnis 1
			// liegt keine Lücke vor und der Index zeigt auf den Anfang der folgenden Lücke.
			// Das While wird verlassen sobald obermenge%2=0 und untermenge%2=1 ist und false als Return-Wert feststeht.
		} while ((iObermenge % 2 == 1) || (iUntermenge % 2 == 0));
		return false;
	}


	/**
	 * Erstellt ein Liste mit den Jahreszahlen, welche immer eine gerade Anzahl von Einträgen hat. Ein Paar besteht aus Zeitraum-Werten von
	 * und bis. Das nächste Paar wird nur eingetragen, wenn eine Lücke vorhanden ist, so dass ggf. Zeiträume zusammengefasst werden.
	 * Die Zeiträume sind werden hier in der Form [von,bis[ erwartet: Schuljahr 'von' ist Teil des Zeitraums und Schuljahr 'bis' nicht,
	 * so dass kontinuierliche Intervalle entstehen. <br>
	 * <br>
	 * Beispiel: 2021-2022, 2023-null => 2021-(2022+1), 2023-null (in aufrufenden Klassen so umgesetzt)
	 *
	 * @param validatorName   der Name des Validators
	 * @param vbs              die Liste mit den Zeitraum-Paaren
	 *
	 * @return Liste mit den Jahreszahlen, welche die Paare von gültigen Zeiträumen für den Algorithmus aufbereitet enthält.
	 */
	private static @NotNull List<Integer> getZeitraumListe(final @NotNull String validatorName, final @NotNull List<PairNN<Integer, Integer>> vbs) {
		final List<Integer> list = new ArrayList<>();
		int i = 0;

		list.add(vbs.get(0).a); // Eintragen des Beginns
		while (i + 1 < vbs.size()) {
			// Prüfe auf überlappende Zeiträume, die nicht erlaubt sind.
			if (vbs.get(i).b > vbs.get(i + 1).a)
				throw new CoreTypeException("Fehler beim prüfen der Zeiträume bei dem Validator '%s'. Die Zeiträume von %s sind überlappend definiert."
						.formatted(validatorName, vbs.get(0).getClass().getSimpleName()));
			if (vbs.get(i).b < vbs.get(i + 1).a) {
				// Lücke gefunden
				list.add(vbs.get(i).b);
				list.add(vbs.get(i + 1).a);
			}
			i++;
		}
		list.add(vbs.get(i).b);

		return list;
	}

	/**
	 * Erzeugt ein PairNN, dass den Anfangszeitpunkt und den Endzeitpunkt enthält. Die Zeiträume sind werden
	 * hier in der Form [von,bis[ erwartet, Schuljahr 'von' ist Teil des Zeitraums Schuljahr 'bis' nicht,
	 * Die null-Werte aus gueltigVon und gueltigBis werden in 0 bzw. MAX_VALUE übersetzt, sowie der
	 * gueltigBis-Wert um 1 erhöht, damit kontinuierliche  Zeiträume entstehen können.
	 *
	 * @param von  Beginn des Zeitraums
	 * @param bis  Ende des Zeitraums
	 *
	 * @return Das Zeitraum-Paar mit übersetzten Null-Werten.
	 */
	private static @NotNull PairNN<Integer, Integer> createZeitraum(final Integer von, final Integer bis) {
		/* Die null-Werte aus gueltigVon und gueltigBis werden in MIN_VALUE bzw. MAX_VALUE übersetzt, sowie der gueltigBis-Wert
		 * um 1 erhöht, damit kontinuierliche  Zeiträume entstehen können. */
		final @NotNull Integer v = (von == null ? Integer.MIN_VALUE : von);
		final @NotNull Integer b = (bis == null ? Integer.MAX_VALUE : bis + 1);
		return new PairNN<>(v, b);
	}

}
