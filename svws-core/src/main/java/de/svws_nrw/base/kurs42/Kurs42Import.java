package de.svws_nrw.base.kurs42;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.ArrayList;
import java.util.function.Function;

import de.svws_nrw.asd.adt.Pair;
import de.svws_nrw.base.CsvReader;
import de.svws_nrw.core.adt.map.HashMap2D;
import de.svws_nrw.core.data.gost.GostBlockungKurs;
import de.svws_nrw.core.data.gost.GostBlockungKursLehrer;
import de.svws_nrw.core.data.gost.GostBlockungRegel;
import de.svws_nrw.core.data.gost.GostBlockungSchiene;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.core.types.gost.GostKursart;
import de.svws_nrw.core.types.kursblockung.GostKursblockungRegelTyp;

/**
 * Diese Klasse dient dem Import einer Kurs42-Blockung. Sie liest einen
 * Text-Export aus Kurs-42 ein und interpretiert diese Dateien so, dass
 * sie mit den Daten aus der SVWS-Datenbank abgeglichen werden könne.
 */
public class Kurs42Import {

	/** Der Logger */
	private final Logger logger;

	/** Die Menge der Schüler in der SVWS-DB zur Überprüfung auf gültige Schüler-IDs */
	private final Set<Long> setSchueler;

	/** Die Menge der SchienenNummern, die bei Kursen vorkommen - z.B. bei Regeln */
	private final Set<Integer> setSchienenBeiKursen = new HashSet<>();

	/** Die Rohdaten zur Blockung aus dem Kurs42 */
	private final Kurs42DataBlockung k42Blockung;

	/** Die Rohdaten zu den Schülern aus Kurs42 */
	private final List<Kurs42DataSchueler> k42Schueler;

	/** Die Rohdaten zu den Fächern aus Kurs42 */
	private final List<Kurs42DataFaecher> k42Faecher;

	/** Die Rohdaten zu den Kursen der Blockung aus Kurs42 */
	private final List<Kurs42DataKurse> k42Kurse;

	/** Die Rohdaten zu den Schienen aus Kurs42 */
	private final List<Kurs42DataSchienen> k42Schienen;

	/** Die Rohdaten zu dem Blockplan aus Kurs42 */
	private final List<Kurs42DataBlockplan> k42Blockplan;

	/** Die Rohdaten zu den Fachwahlen aus Kurs42 */
	private final List<Kurs42DataFachwahlen> k42Fachwahlen;


	/** Der Name der Blockung */
	public final String name;

	/** Das Jahr, in welchem diese Stufe Abitur machen wird. */
	public final int abiturjahrgang;

	/** Das Halbjahr, für welches die Blockung erzeugt wurde */
	public final GostHalbjahr halbjahr;

	/** Eine Liste der Blockungs-Regeln */
	public final List<GostBlockungRegel> regeln = new ArrayList<>();

	/** Eine Liste der Kurse der neuen Blockung */
	public final List<GostBlockungKurs> kurse = new ArrayList<>();

	/** Eine Liste der Schienen der neuen Blockung */
	public final List<GostBlockungSchiene> schienen = new ArrayList<>();

	/** Die Kurs-Schienen-Zuordnungen der Blockung */
	public final HashMap2D<Long, Long, Pair<Long, Long>> zuordnung_kurs_schiene = new HashMap2D<>();

	/** Die Kurs-Schüler-Zuordnungen der Blockung */
	public final HashMap2D<Long, Long, Pair<Long, Long>> zuordnung_kurs_schueler = new HashMap2D<>();

	/** Eine Map von der DB-ID des Schülers auf das Kurs42-Import-Objekt */
	private final Map<Long, Kurs42DataSchueler> mapSchuelerByID = new HashMap<>();

	/** Eine Map von dem Unique-Schlüssel des Kurs42-Schülers auf das Kurs42-Import-Objekt */
	private final Map<String, Kurs42DataSchueler> mapSchuelerByKey = new HashMap<>();

	/** Eine Map von dem Unique-Schlüssel des Kurs42-Schülers auf die DB-ID */
	private final Map<String, Long> mapSchuelerKeyToID = new HashMap<>();

	/** Eine Map von der DB-ID des Kurs42-Schülers auf den Unique-Schlüssel  */
	private final Map<Long, String> mapSchuelerIDToKey = new HashMap<>();

	/** Eine Map von dem Fach-Kürzel zu der ID des Faches */
	private final Map<String, Long> mapFachKuerzelToID = new HashMap<>();

	/** Eine Map von der Kurs-ID der neuen Blockung auf das Kurs42-Import-Objekt */
	private final Map<Long, Kurs42DataKurse> mapKursByID = new HashMap<>();

	/** Eine Map von dem Namen des Kurses auf das Kurs42-Import-Objekt */
	private final Map<String, Kurs42DataKurse> mapKursByName = new HashMap<>();

	/** Eine Map von der Kurs-ID der neuen Blockung auf den Namen des Kurses */
	private final Map<String, Long> mapKursNameToID = new HashMap<>();

	/** Eine Map von der Schienen-ID der neuen Blockung auf das Kurs42-Import-Objekt */
	private final Map<Long, Kurs42DataSchienen> mapSchieneByID = new HashMap<>();

	/** Eine Map von der Schienen-Nummer auf die Schienen ID der neuen Blockung */
	private final Map<Integer, Long> mapSchieneNrToID = new HashMap<>();

	/** Eine Map von der Schienen-ID der neuen Blockung auf den Namen der Schienen */
	private final Map<Long, String> mapSchieneIDToName = new HashMap<>();

	private final Map<String, Integer> counterKurse = new HashMap<>();


	private static final Function<Kurs42DataSchueler, String> getSchuelerKey =
			(final Kurs42DataSchueler schueler) -> schueler.Name + ";" + schueler.Vorname + ";" + schueler.GebDat + ";" + schueler.Geschlecht;

	private static final Function<Kurs42DataFachwahlen, String> getSchuelerKeyFW =
			(final Kurs42DataFachwahlen schuelerFW) -> schuelerFW.Name + ";" + schuelerFW.Vorname + ";" + schuelerFW.GebDat + ";" + schuelerFW.Geschlecht;

	/**
	 * Liest die Informationen aus dem Kurs-42-Text-Export unter dem
	 * angegebenen Pfad ein.
	 *
	 * @param parent        der Pfad unter dem die Kurs42-Text-Export-Dateien liegen
	 * @param schulnummer   die Schulnummer der Schule, die die Daten importiert
	 * @param mapLehrer     eine Map, welchen von den Lehrer-Kürzeln auf deren ID abbildet
	 * @param setSchueler   ein Set, mit den Schüler-IDs aus der SVWS-DBs
	 * @param logger        der Logger
	 *
	 * @throws IOException   falls die Dateien nicht erfolgreich gelesen werden können.
	 */
	public Kurs42Import(final Path parent, final int schulnummer, final Map<String, Long> mapLehrer, final Set<Long> setSchueler, final Logger logger)
			throws IOException {
		this.logger = logger;
		this.setSchueler = setSchueler;
		this.k42Blockung = new Kurs42DataBlockung(parent.resolve("Blockung.txt"));
		this.k42Schueler = CsvReader.fromKurs42(parent.resolve("Schueler.txt"), Kurs42DataSchueler.class);
		this.k42Faecher = CsvReader.fromKurs42(parent.resolve("Faecher.txt"), Kurs42DataFaecher.class);
		this.k42Kurse = CsvReader.fromKurs42(parent.resolve("Kurse.txt"), Kurs42DataKurse.class);
		this.k42Schienen = CsvReader.fromKurs42(parent.resolve("Schienen.txt"), Kurs42DataSchienen.class);
		this.k42Blockplan = CsvReader.fromKurs42(parent.resolve("Blockplan.txt"), Kurs42DataBlockplan.class);
		this.k42Fachwahlen = CsvReader.fromKurs42(parent.resolve("Fachwahlen.txt"), Kurs42DataFachwahlen.class);
		if (!("" + schulnummer).equals(k42Blockung.Schulnummer))
			throw new IOException(
					"Die Schulnummer der Schule stimmt nicht mit der Schulnummer des Kurs42-Exportes überein. Die Daten können daher nicht importiert werden.");
		this.name = ((k42Blockung.Bezeichnung == null) || "".equals(k42Blockung.Bezeichnung)) ? "Blockung importiert aus Kurs42" : k42Blockung.Bezeichnung;
		final int abschnitt = (k42Blockung.Abschnitt > 2) ? 2 : k42Blockung.Abschnitt;
		this.halbjahr = GostHalbjahr.fromJahrgangUndHalbjahr(k42Blockung.Jahrgang, abschnitt);
		this.abiturjahrgang = halbjahr.getAbiturjahrFromSchuljahr(k42Blockung.Jahr);
		for (final Kurs42DataSchueler schueler : k42Schueler) {
			final long id = schueler.DB_IdNr;
			final String key = getSchuelerKey.apply(schueler);
			mapSchuelerByID.put(id, schueler);
			mapSchuelerByKey.put(key, schueler);
			mapSchuelerIDToKey.put(id, key);
			mapSchuelerKeyToID.put(key, id);
		}
		for (final Kurs42DataFaecher fach : k42Faecher) {
			mapFachKuerzelToID.put(fach.Krz, fach.IDNr);
		}
		long curRegelID = 0;
		curRegelID = initKurse(curRegelID, mapLehrer);
		initSchienen();
		curRegelID = initBlockplan(curRegelID);
		initKurseSchienenanzahlKorrektur();
		initFachwahlen();
	}

	private long initKurse(final long firstRegelID, final Map<String, Long> mapLehrer) throws IOException {
		long curRegelID = firstRegelID;
		long curKursID = 0;
		for (final Kurs42DataKurse k42Kurs : k42Kurse) {
			final long id = curKursID++;
			final Long fachID = mapFachKuerzelToID.get(k42Kurs.Fach);
			if (fachID == null)
				throw new IOException("Das bei den Kursen angegeben Fach mit dem Kürzel " + k42Kurs.Fach
						+ " existiert nicht in der Liste der Fächer. Die zu importierenden Daten sind inkonsistent. Der Import wird abgebrochen.");
			final GostKursart kursart = GostKursart.fromKuerzel(k42Kurs.Kursart);
			final String kursartKey = fachID + ";" + kursart.id;

			final int kursnummer = counterKurse.getOrDefault(kursartKey, 0) + 1;
			counterKurse.put(kursartKey, kursnummer);

			final GostBlockungKurs kurs = new GostBlockungKurs();
			kurs.id = id;
			kurs.fach_id = fachID;
			kurs.kursart = kursart.id;
			try {
				kurs.nummer = Integer.parseInt(k42Kurs.ParallelKursNr);
			} catch (@SuppressWarnings("unused") final NumberFormatException nfe) {
				kurs.nummer = kursnummer;
			}
			kurs.istKoopKurs = false;   // Kann dies ggf. aus den Kurs42-Daten bestimmt werden?
			kurs.suffix = "";
			kurs.wochenstunden = k42Kurs.Std;
			kurs.anzahlSchienen = k42Kurs.Schienenzahl;
			if ((k42Kurs.Lehrer != null) && !"".equals(k42Kurs.Lehrer) && !"--".equals(k42Kurs.Lehrer)) {
				final Long lehrerID = mapLehrer.get(k42Kurs.Lehrer);
				if (lehrerID == null) {
					logger.logLn(
							"Das bei den Kursen angegeben Lehrer-Kürzel %s existiert nicht in der Lehrer-Liste. Die zu importierenden Daten sind inkonsistent. Dem Kurs wird kein Lehrer zugeordnet."
									.formatted(k42Kurs.Lehrer));
					continue;
				}
				final GostBlockungKursLehrer kl = new GostBlockungKursLehrer();
				kl.id = lehrerID;
				kl.kuerzel = k42Kurs.Lehrer;
				kl.reihenfolge = 1;
				kl.wochenstunden = k42Kurs.Std;
				kurs.lehrer.add(kl);
			}
			this.kurse.add(kurs);
			mapKursByID.put(id, k42Kurs);
			mapKursByName.put(k42Kurs.Name, k42Kurs);
			mapKursNameToID.put(k42Kurs.Name, id);
			if ((k42Kurs.Gesperrt != null) && (k42Kurs.Gesperrt.startsWith("[")) && (k42Kurs.Gesperrt.endsWith("]"))) {
				try {
					final String[] tmpSchienen = k42Kurs.Gesperrt.substring(1, k42Kurs.Gesperrt.length() - 1).split(",");
					for (final String schiene : tmpSchienen) {
						if ("".equals(schiene.trim()))
							continue;
						final int schienenNummer = Integer.parseInt(schiene.trim()) + 1;   // Die Schienen-Nummer (1-indiziert)
						setSchienenBeiKursen.add(schienenNummer);
						final GostBlockungRegel regel = new GostBlockungRegel();
						regel.id = curRegelID++;
						regel.typ = GostKursblockungRegelTyp.KURS_SPERRE_IN_SCHIENE.typ;
						regel.parameter.add(id);  // Kurs-ID
						regel.parameter.add((long) schienenNummer);
						regeln.add(regel);
					}
				} catch (final NumberFormatException nfe) {
					throw new IOException(nfe);
				}
			}
		}
		return curRegelID;
	}

	private void initSchienen() {
		long curSchienenID = 0;
		for (final Kurs42DataSchienen k42Schiene : k42Schienen) {
			final long id = curSchienenID++;
			mapSchieneByID.put(id, k42Schiene);
			mapSchieneIDToName.put(id, k42Schiene.Bezeichnung);
			mapSchieneNrToID.put(k42Schiene.Id, id);
			final GostBlockungSchiene schiene = new GostBlockungSchiene();
			schiene.id = id;
			schiene.nummer = k42Schiene.Id;
			schiene.bezeichnung = k42Schiene.Bezeichnung;
			this.schienen.add(schiene);
			this.setSchienenBeiKursen.remove(schiene.nummer);
		}
		// Ergänze Schienen für die Schienennummern, die bei den Kursen als Fixierungen und Sperrungen vorkommen, aber nicht in der Schienen-Datei vorhanden sind
		for (final int schienenNr : this.setSchienenBeiKursen) {
			final long id = curSchienenID++;
			final Kurs42DataSchienen k42Schiene = new Kurs42DataSchienen();
			k42Schiene.Id = schienenNr;
			k42Schiene.Bezeichnung = "Schiene " + schienenNr;
			k42Schiene.KopplungsId = "";
			k42Schiene.Stundenplan = "";
			k42Schienen.add(k42Schiene);
			mapSchieneByID.put(id, k42Schiene);
			mapSchieneIDToName.put(id, k42Schiene.Bezeichnung);
			mapSchieneNrToID.put(k42Schiene.Id, id);
			final GostBlockungSchiene schiene = new GostBlockungSchiene();
			schiene.id = id;
			schiene.nummer = k42Schiene.Id;
			schiene.bezeichnung = k42Schiene.Bezeichnung;
			this.schienen.add(schiene);
		}
		this.setSchienenBeiKursen.clear();
	}

	private long initBlockplan(final long firstRegelID) throws IOException {
		long curRegelID = firstRegelID;
		for (final Kurs42DataBlockplan bp : k42Blockplan) {
			final Long id = mapKursNameToID.get(bp.Kursbezeichnung);
			final int schienenNr = bp.Schiene + 1; // der SVWS-Server verwendet eine 1-Indizierung => Umwandlung
			if (id == null) {
				logger.logLn(
						"Der im Blockplan angegebene Kurs '%s' existiert nicht in der Liste der Kurse. Die zu importierenden Daten sind inkonsistent. Dem Kurs wird nicht die Schiene %d zugeordnet."
								.formatted(bp.Kursbezeichnung, schienenNr));
				continue;
			}
			final Long schienenID = mapSchieneNrToID.get(schienenNr);
			if (schienenID == null)
				throw new IOException("Die im Blockplan angegebene Schienennummer " + bp.Schiene
						+ " existiert nicht in der Schienen-Liste. Die zu importierenden Daten sind inkonsistent. Der Import wird abgebrochen.");
			this.zuordnung_kurs_schiene.put(id, schienenID, new Pair<>(id, schienenID));
			if (bp.Fixiert != 0) {
				final GostBlockungRegel regel = new GostBlockungRegel();
				regel.id = curRegelID++;
				regel.typ = GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ;
				regel.parameter.add(id);  // Kurs-ID
				regel.parameter.add((long) schienenNr);  // Die Schienen-Nummer (1-indiziert)
				regeln.add(regel);
			}
		}
		return curRegelID;
	}

	private void initKurseSchienenanzahlKorrektur() {
		for (final GostBlockungKurs kurs : kurse) {
			final int zugeordnet = zuordnung_kurs_schiene.getSubMapSizeOrZero(kurs.id);
			if (kurs.anzahlSchienen != zugeordnet) {
				logger.logLn(
						"Der Kurs %s hat laut den Kurs42-Daten %x Schienen, aber er ist %x Schienen zugeordnet. Der letzte Wert wird als richtig übernommen."
								.formatted(mapKursByID.get(kurs.id).Name, kurs.anzahlSchienen, zugeordnet));
				kurs.anzahlSchienen = zugeordnet;
			}
		}
	}

	private void initFachwahlen() throws IOException {
		for (final Kurs42DataFachwahlen fw : k42Fachwahlen) {
			final String schuelerKey = getSchuelerKeyFW.apply(fw);
			final Long kursID = mapKursNameToID.get(fw.Kurs);
			if (kursID == null) {
				logger.logLn(
						"Der bei den Fachwahlen angegebene Kurs (%s) existiert nicht in der Liste der Kurse. Die zu importierenden Daten sind inkonsistent."
								.formatted(fw.Kurs));
				continue;
			}
			final Long schuelerID = mapSchuelerKeyToID.get(schuelerKey);
			if (schuelerID == null)
				throw new IOException("Der bei den Fachwahlen angegebene Datensatz enthält Schülerdaten (" + schuelerKey
						+ "), die in der Schülerliste nicht existieren. Die zu importierenden Daten sind inkonsistent. Der Import wird abgebrochen.");
			if (!setSchueler.contains(schuelerID)) {
				logger.logLn("Der Schüler mit der ID %d existiert nicht in der SVWS-DB. Die Kurs-Schüler-Zuordnung wird beim Import ignoriert."
						.formatted(schuelerID));
				continue;
			}
			this.zuordnung_kurs_schueler.put(kursID, schuelerID, new Pair<>(kursID, schuelerID));
		}
	}

}
