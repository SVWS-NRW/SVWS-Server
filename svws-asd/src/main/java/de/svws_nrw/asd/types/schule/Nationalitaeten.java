package de.svws_nrw.asd.types.schule;

import java.util.HashMap;

import de.svws_nrw.asd.data.CoreTypeException;
import de.svws_nrw.asd.data.schule.NationalitaetenKatalogEintrag;
import de.svws_nrw.asd.types.CoreTypeSimple;
import de.svws_nrw.asd.utils.CoreTypeDataManager;
import jakarta.validation.constraints.NotNull;

/**
 * Ein Core-Type für die Nationalitäten.<br>
 * Die Daten basieren auf der Codeliste Staat aus der Staats- und Gebietssystematik des Statistischen Bundesamtes.<br>
 * Siehe auch:<ul>
 *   <li><a href="https://www.destatis.de/DE/Methoden/Klassifikationen/Staat-Gebietsystematik/Staatsangehoerigkeitsgebietsschluessel_pdf.pdf?__blob=publicationFile">
 *   Stand vom 1.2.2022 </a> </li>
 *   <li> Eine Übersicht bei xrepository.de findet sich unter der Kennung
 *   "urn:de:bund:destatis:bevoelkerungsstatistik:schluessel:staat". </li>
 *   <li>Übersicht zum <a href="https://www.iso.org/obp/ui/#iso:pub:PUB500001:en"> ISO-Standard 3166</a></li>
 * </ul>
 */
public class Nationalitaeten extends CoreTypeSimple<NationalitaetenKatalogEintrag, Nationalitaeten> {


	/* ----- Die nachfolgenden Attribute werden nicht initialisiert und werden als Cache verwendet, um z.B. den Schuljahres-bezogenen Zugriff zu cachen ----- */

	/** Eine Hashmap mit allen definierten Nationalitäten, zugeordnet zu dem dreistelligen ISO-Code */
	private static final @NotNull HashMap<String, Nationalitaeten> _mapISO3 = new HashMap<>();

	/** Eine Hashmap mit allen definierten Nationalitäten, zugeordnet zu dem zweistelligen ISO-Code */
	private static final @NotNull HashMap<String, Nationalitaeten> _mapISO2 = new HashMap<>();

	/** Eine Hashmap mit allen definierten Nationalitäten, zugeordnet zu DESTATIS-Code */
	private static final @NotNull HashMap<String, Nationalitaeten> _mapDESTATIS = new HashMap<>();



	/**
	 * Erstellt eine Nationalitaeten mit Standardwerten
	 */
	public Nationalitaeten() {
		// nichts zu tun
	}


	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static void init(final @NotNull CoreTypeDataManager<NationalitaetenKatalogEintrag, Nationalitaeten> manager) {
		CoreTypeDataManager.putManager(Nationalitaeten.class, manager);
		_mapISO3.clear();
		_mapISO2.clear();
		_mapDESTATIS.clear();
	}


	/**
	 * Gibt den Daten-Manager für den Zugriff auf die Core-Type-Daten zurück, sofern dieser initialisiert wurde.
	 *
	 * @return der Daten-Manager
	 */
	public static @NotNull CoreTypeDataManager<NationalitaetenKatalogEintrag, Nationalitaeten> data() {
		return CoreTypeDataManager.getManager(Nationalitaeten.class);
	}


	/**
	 * Gibt alle Werte des Core-Types zurück.
	 *
	 * @return die Werte des Core-Types als Array
	 */
	public static @NotNull Nationalitaeten @NotNull [] values() {
		return CoreTypeSimple.valuesByClass(Nationalitaeten.class);
	}

	/**
	 * Erzeugt eine Instance dieser Klasse.
	 */
	@Override
	public Nationalitaeten getInstance() {
		return new Nationalitaeten();
	}


	/**
	 * Gibt eine Map von den dreistelligen ISO-Codes der Nationalitäten auf die zugehörigen Nationalitäten
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *
	 * @return die Map von den Kürzeln der Nationalitäten auf die zugehörigen Nationalitäten
	 */
	private static @NotNull HashMap<String, Nationalitaeten> getMapISO3() {
		if (_mapISO3.isEmpty())
			for (final Nationalitaeten s : Nationalitaeten.values())
				for (final NationalitaetenKatalogEintrag kat : s.historie())
					if (kat.iso3 != null)
						_mapISO3.put(kat.iso3, s);
		return _mapISO3;
	}


	/**
	 * Gibt eine Map von den zweistelligen ISO-Codes der Nationalitäten auf die zugehörigen Nationalitäten
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *
	 * @return die Map von den Kürzeln der Nationalitäten auf die zugehörigen Nationalitäten
	 */
	private static @NotNull HashMap<String, Nationalitaeten> getMapISO2() {
		if (_mapISO2.isEmpty()) {
			for (final Nationalitaeten s : Nationalitaeten.values())
				for (final NationalitaetenKatalogEintrag kat : s.historie())
					if (kat.iso2 != null)
						_mapISO2.put(kat.iso2, s);
		}
		return _mapISO2;
	}


	/**
	 * Gibt eine Map von den DESTATIS-Codes der Nationalitäten auf die zugehörigen Nationalitäten
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *
	 * @return die Map von den Kürzeln der Nationalitäten auf die zugehörigen Nationalitäten
	 */
	private static @NotNull HashMap<String, Nationalitaeten> getMapDESTATIS() {
		if (_mapDESTATIS.isEmpty()) {
			for (final Nationalitaeten s : Nationalitaeten.values())
				for (final NationalitaetenKatalogEintrag kat : s.historie())
					if (kat.codeDEStatis != null)
						_mapDESTATIS.put(kat.codeDEStatis, s);
		}
		return _mapDESTATIS;
	}


	/**
	 * Gibt die Nationalität für den angegebenen dreistelligen ISO-Code nach ISO 3166-1 zurück.
	 *
	 * @param code   der ISO-Code
	 *
	 * @return die Nationalität oder null, falls der Code unbekannt ist
	 */
	public static Nationalitaeten getByISO3(final String code) {
		return getMapISO3().get(code);
	}

	/**
	 * Gibt die Nationalität für den angegebenen zweistelligen ISO-Code nach ISO 3166-1 zurück.
	 *
	 * @param code   der ISO-Code
	 *
	 * @return die Nationalität oder null, falls der Code unbekannt ist
	 */
	public static Nationalitaeten getByISO2(final String code) {
		return getMapISO2().get(code);
	}

	/**
	 * Gibt die Nationalität für den angegebenen DESTATIS-Code zurück.
	 *
	 * @param code   der DESTATIS-Code
	 *
	 * @return die Nationalität oder null, falls der Code unbekannt ist
	 */
	public static Nationalitaeten getByDESTATIS(final String code) {
		return getMapDESTATIS().get(code);
	}

	/**
	 * Gibt die Nationalität DEU zurück.
	 *
	 * @return die Nationalität DEU
	 */
	public static @NotNull Nationalitaeten getDEU() {
		final Nationalitaeten deu = getByISO3("DEU");
		if (deu == null)
			throw new CoreTypeException("Core-Type nicht korrekt initialisiert. DEU kann nicht gefunden werden.");
		return deu;
	}

}
