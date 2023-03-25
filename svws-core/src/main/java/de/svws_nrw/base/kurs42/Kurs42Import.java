package de.svws_nrw.base.kurs42;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.function.Function;

import de.svws_nrw.base.CsvReader;
import de.svws_nrw.core.adt.Pair;
import de.svws_nrw.core.data.gost.GostBlockungKurs;
import de.svws_nrw.core.data.gost.GostBlockungKursLehrer;
import de.svws_nrw.core.data.gost.GostBlockungRegel;
import de.svws_nrw.core.data.gost.GostBlockungSchiene;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.core.types.gost.GostKursart;
import de.svws_nrw.core.types.kursblockung.GostKursblockungRegelTyp;

/**
 * Diese Klasse dient dem Import einer Kurs42-Blockung. Sie liest einen
 * Text-Export aus Kurs-42 ein und interpretiert diese Dateien so, dass
 * sie mit den Daten aus der SVWS-Datenbank abgeglichen werden könne. 
 */
public class Kurs42Import {
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
	public final Vector<GostBlockungRegel> regeln = new Vector<>();
	
	/** Eine Liste der Kurse der neuen Blockung */
	public final Vector<GostBlockungKurs> kurse = new Vector<>();
	
	/** Eine Liste der Schienen der neuen Blockung */
	public final Vector<GostBlockungSchiene> schienen = new Vector<>();

	/** Die Kurs-Schienen-Zuordnungen der Blockung */
	public final Vector<Pair<Long, Long>> zuordnung_kurs_schiene = new Vector<>();

	/** Die Kurs-Schüler-Zuordnungen der Blockung */
	public final Vector<Pair<Long, Long>> zuordnung_kurs_schueler = new Vector<>();

	/** Eine Map von der DB-ID des Schülers auf das Kurs42-Import-Objekt */
	private final HashMap<Long, Kurs42DataSchueler> mapSchuelerByID = new HashMap<>();

	/** Eine Map von dem Unique-Schlüssel des Kurs42-Schülers auf das Kurs42-Import-Objekt */
	private final HashMap<String, Kurs42DataSchueler> mapSchuelerByKey = new HashMap<>();

	/** Eine Map von dem Unique-Schlüssel des Kurs42-Schülers auf die DB-ID */
	private final HashMap<String, Long> mapSchuelerKeyToID = new HashMap<>();

	/** Eine Map von der DB-ID des Kurs42-Schülers auf den Unique-Schlüssel  */
	private final HashMap<Long, String> mapSchuelerIDToKey = new HashMap<>();

	/** Eine Map von dem Fach-Kürzel zu der ID des Faches */
	private final HashMap<String, Long> mapFachKuerzelToID = new HashMap<>();

	/** Eine Map von der Kurs-ID der neuen Blockung auf das Kurs42-Import-Objekt */
	private final HashMap<Long, Kurs42DataKurse> mapKursByID = new HashMap<>();

	/** Eine Map von dem Namen des Kurses auf das Kurs42-Import-Objekt */
	private final HashMap<String, Kurs42DataKurse> mapKursByName = new HashMap<>();

	/** Eine Map von der Kurs-ID der neuen Blockung auf den Namen des Kurses */
	private final HashMap<String, Long> mapKursNameToID = new HashMap<>();

	/** Eine Map von der Schienen-ID der neuen Blockung auf das Kurs42-Import-Objekt */
	private final HashMap<Long, Kurs42DataSchienen> mapSchieneByID = new HashMap<>();

	/** Eine Map von der Schienen-Nummer auf die Schienen ID der neuen Blockung */
	private final HashMap<Integer, Long> mapSchieneNrToID = new HashMap<>();
	
	/** Eine Map von der Schienen-ID der neuen Blockung auf den Namen der Schienen */
	private final HashMap<Long, String> mapSchieneIDToName = new HashMap<>();

	private final HashMap<String, Integer> counterKurse = new HashMap<>(); 

	
	private final static Function<Kurs42DataSchueler, String> getSchuelerKey = (Kurs42DataSchueler schueler) -> {
		return schueler.Name + ";" + schueler.Vorname + ";" + schueler.GebDat + ";" + schueler.Geschlecht;
	};
	
	private final static Function<Kurs42DataFachwahlen, String> getSchuelerKeyFW = (Kurs42DataFachwahlen schuelerFW) -> {
		return schuelerFW.Name + ";" + schuelerFW.Vorname + ";" + schuelerFW.GebDat + ";" + schuelerFW.Geschlecht;
	};

	/**
	 * Liest die Informationen aus dem Kurs-42-Text-Export unter dem
	 * angegebenen Pfad ein.
	 * 
	 * @param parent            der Pfad unter dem die Kurs42-Text-Export-Dateien liegen
	 * @param schulnummer       die Schulnummer der Schule, die die Daten importiert
	 * @param istQuartalsmodus  gibt an, ob die importierende Schule im Quartalsmodus arbeitet oder nicht
	 * @param mapLehrer         eine Map, welchen von den Lehrer-Kürzeln auf deren ID abbildet
	 * 
	 * @throws IOException   falls die Dateien nicht erfolgreich gelesen werden können.
	 */
	public Kurs42Import(Path parent, int schulnummer, boolean istQuartalsmodus, Map<String, Long> mapLehrer) throws IOException {
		this.k42Blockung = new Kurs42DataBlockung(parent.resolve("Blockung.txt"));
		this.k42Schueler = CsvReader.from(parent.resolve("Schueler.txt"), Kurs42DataSchueler.class);
		this.k42Faecher = CsvReader.from(parent.resolve("Faecher.txt"), Kurs42DataFaecher.class);
		this.k42Kurse = CsvReader.from(parent.resolve("Kurse.txt"), Kurs42DataKurse.class);
		this.k42Schienen = CsvReader.from(parent.resolve("Schienen.txt"), Kurs42DataSchienen.class);
		this.k42Blockplan = CsvReader.from(parent.resolve("Blockplan.txt"), Kurs42DataBlockplan.class);
		this.k42Fachwahlen = CsvReader.from(parent.resolve("Fachwahlen.txt"), Kurs42DataFachwahlen.class);
		if (!("" + schulnummer).equals(k42Blockung.Schulnummer))
			throw new IOException("Die Schulnummer der Schule stimmt nicht mit der Schulnummer des Kurs42-Exportes überein. Die Daten können daher nicht importiert werden.");
		this.name = (k42Blockung.Bezeichnung == null) || "".equals(k42Blockung.Bezeichnung) ? "Blockung importiert aus Kurs42" : k42Blockung.Bezeichnung;
		this.halbjahr = GostHalbjahr.fromJahrgangUndHalbjahr(k42Blockung.Jahrgang, istQuartalsmodus ? (k42Blockung.Abschnitt / 2) : k42Blockung.Abschnitt);
		this.abiturjahrgang = halbjahr.getAbiturjahrFromSchuljahr(k42Blockung.Jahr);
		for (Kurs42DataSchueler schueler : k42Schueler) {
			long id = schueler.DB_IdNr;
			String key = getSchuelerKey.apply(schueler);
			mapSchuelerByID.put(id, schueler);
			mapSchuelerByKey.put(key, schueler);
			mapSchuelerIDToKey.put(id, key);
			mapSchuelerKeyToID.put(key, id);
		}
		for (Kurs42DataFaecher fach : k42Faecher) {
			mapFachKuerzelToID.put(fach.Krz, fach.IDNr);
		}
		long curKursID = 0;
		long curRegelID = 0;
		for (Kurs42DataKurse k42Kurs : k42Kurse) {
			long id = curKursID++;
			Long fachID = mapFachKuerzelToID.get(k42Kurs.Fach);	
			if (fachID == null)
				throw new IOException("Das bei den Kursen angegeben Fach mit dem Kürzel " + k42Kurs.Fach + " existiert nicht in der Liste der Fächer. Die zu importierenden Daten sind inkonsistent. Der Import wird abgebrochen.");
			GostKursart kursart = GostKursart.fromKuerzel(k42Kurs.Kursart);
			String kursartKey = fachID + ";" + kursart.id;
			
			int kursnummer = counterKurse.getOrDefault(kursartKey, 0) + 1;
			counterKurse.put(kursartKey, kursnummer);
			
			GostBlockungKurs kurs = new GostBlockungKurs();
			kurs.id = id;
			kurs.fach_id = fachID;
			kurs.kursart = kursart.id;
			try {
				kurs.nummer = Integer.parseInt(k42Kurs.ParallelKursNr);
			} catch (@SuppressWarnings("unused") NumberFormatException nfe) {
				kurs.nummer = kursnummer;
			}
			kurs.istKoopKurs = false;   // Kann dies ggf. aus den Kurs42-Daten bestimmt werden?
			kurs.suffix = "";
			kurs.wochenstunden = k42Kurs.Std;
			kurs.anzahlSchienen = k42Kurs.Schienenzahl;
			if (k42Kurs.Lehrer != null && !"".equals(k42Kurs.Lehrer) && !"--".equals(k42Kurs.Lehrer)) {
				Long lehrerID = mapLehrer.get(k42Kurs.Lehrer);
				if (lehrerID == null)
					throw new IOException("Das bei den Kursen angegeben Lehrer-Kürzel " + k42Kurs.Lehrer + " existiert nicht in der Lehrer-Liste. Die zu importierenden Daten sind inkonsistent oder passen nicht zu der SVWS-Datenbank. Der Import wird abgebrochen.");
				GostBlockungKursLehrer kl = new GostBlockungKursLehrer();
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
					String[] schienen = k42Kurs.Gesperrt.substring(1, k42Kurs.Gesperrt.length()-1).split(",");
					for (String schiene : schienen) {
						GostBlockungRegel regel = new GostBlockungRegel();
						regel.id = curRegelID++;
						regel.typ = GostKursblockungRegelTyp.KURS_SPERRE_IN_SCHIENE.typ;
						regel.parameter.add(id);  // Kurs-ID
						regel.parameter.add(Long.parseLong(schiene) + 1);  // Die Schienen-Nummer (1-indiziert)
						regeln.add(regel);
					}
				} catch (NumberFormatException nfe) {
					throw new IOException(nfe);
				}
			}
		}
		long curSchienenID = 0;
		for (Kurs42DataSchienen k42Schiene : k42Schienen) {
			long id = curSchienenID++;
			mapSchieneByID.put(id, k42Schiene);
			mapSchieneIDToName.put(id, k42Schiene.Bezeichnung);
			mapSchieneNrToID.put(k42Schiene.Id, id);
			GostBlockungSchiene schiene = new GostBlockungSchiene();
			schiene.id = id;
			schiene.nummer = k42Schiene.Id;
			schiene.bezeichnung = k42Schiene.Bezeichnung;
			this.schienen.add(schiene);
		}
		for (Kurs42DataBlockplan bp : k42Blockplan) {
			Long id = mapKursNameToID.get(bp.Kursbezeichnung);
			if (id == null)
				throw new IOException("Der im Blockplan angegebene Kurs existiert nicht in der Liste der Kurse. Die zu importierenden Daten sind inkonsistent. Der Import wird abgebrochen.");
			int schienenNr = bp.Schiene + 1; // der SVWS-Server verwendet eine 1-Indizierung => Umwandlung
			Long schienenID = mapSchieneNrToID.get(schienenNr);
			if (schienenID == null)
				throw new IOException("Die im Blockplan angegebene Schienennummer " + bp.Schiene + " existiert nicht in der Schienen-Liste. Die zu importierenden Daten sind inkonsistent. Der Import wird abgebrochen.");
			this.zuordnung_kurs_schiene.add(new Pair<>(id, schienenID));
			if (bp.Fixiert != 0) {
				GostBlockungRegel regel = new GostBlockungRegel();
				regel.id = curRegelID++;
				regel.typ = GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ;
				regel.parameter.add(id);  // Kurs-ID
				regel.parameter.add((long) schienenNr);  // Die Schienen-Nummer (1-indiziert)
				regeln.add(regel);
			}
		}
		for (Kurs42DataFachwahlen fw : k42Fachwahlen) {
			String schuelerKey = getSchuelerKeyFW.apply(fw);
			Long kursID = mapKursNameToID.get(fw.Kurs);
			if (kursID == null)
				throw new IOException("Der bei den Fachwahlen angegebene Kurs (" + fw.Kurs + ") existiert nicht in der Schülerliste. Die zu importierenden Daten sind inkonsistent. Der Import wird abgebrochen.");
			Long schuelerID = mapSchuelerKeyToID.get(schuelerKey);
			if (schuelerID == null)
				throw new IOException("Der bei den Fachwahlen angegebene Datensatz enthält Schülerdaten (" + schuelerKey + "), die in der Schülerliste nicht existieren. Die zu importierenden Daten sind inkonsistent. Der Import wird abgebrochen.");
			this.zuordnung_kurs_schueler.add(new Pair<>(kursID, schuelerID));
		}
	}

}
