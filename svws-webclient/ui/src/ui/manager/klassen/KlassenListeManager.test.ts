import { describe, expect, test, beforeAll } from "vitest";
import { JsonCoreTypeReaderStatic } from "../../../../../core/src/asd/utils/JsonCoreTypeReaderStatic";
import { Schuljahresabschnitt } from "../../../../../core/src/asd/data/schule/Schuljahresabschnitt";
import { KlassenDaten } from "../../../../../core/src/asd/data/klassen/KlassenDaten";
import { Schulform } from "../../../../../core/src/asd/types/schule/Schulform";
import { Schulgliederung } from "../../../../../core/src/asd/types/schule/Schulgliederung";
import type { SchuelerListeEintrag } from "../../../../../core/src/core/data/schueler/SchuelerListeEintrag";
import { Schueler } from "../../../../../core/src/asd/data/schueler/Schueler";
import { JahrgangsDaten } from "../../../../../core/src/core/data/jahrgang/JahrgangsDaten";
import { LehrerListeEintrag } from "../../../../../core/src/core/data/lehrer/LehrerListeEintrag";
import { ArrayList } from "../../../../../core/src/java/util/ArrayList";
import type { List } from "../../../../../core/src/java/util/List";
import { Pair } from "../../../../../core/src/asd/adt/Pair";
import { KlassenListeManager } from "./KlassenListeManager";

describe("Tests für KlassenListeManager", () => {
	// Testet, ob das HTML korrekt gerendert wird
	const schulform: Schulform = Schulform.GY;
	let schuljahresabschnitte: ArrayList<Schuljahresabschnitt> =
		new ArrayList<Schuljahresabschnitt>();
	const klassen: ArrayList<KlassenDaten> = new ArrayList<KlassenDaten>();
	const schueler: ArrayList<SchuelerListeEintrag> =
		new ArrayList<SchuelerListeEintrag>();
	const jahrgaenge: ArrayList<JahrgangsDaten> =
		new ArrayList<JahrgangsDaten>();
	const lehrer: ArrayList<LehrerListeEintrag> =
		new ArrayList<LehrerListeEintrag>();

	beforeAll(async () => {
		const reader = new JsonCoreTypeReaderStatic();
		reader.readAll();
		const sab: Schuljahresabschnitt = new Schuljahresabschnitt();
		sab.id = 1;
		sab.schuljahr = 2020;

		schuljahresabschnitte = new ArrayList();
		schuljahresabschnitte.add(sab);
	});

	test("Initialization | KlassenListeManager should be initialized correctly", () => {
		const klassenListeManager = new KlassenListeManager(
			1,
			1,
			schuljahresabschnitte,
			schulform,
			klassen,
			schueler,
			jahrgaenge,
			lehrer
		);
		expect(klassenListeManager).not.toBeNull();
	});

	test("Initialization | KlassenListeManager should be initialized correctly, even with Schulform = null", () => {
		const klassenListeManager = new KlassenListeManager(
			1,
			1,
			schuljahresabschnitte,
			schulform,
			klassen,
			schueler,
			jahrgaenge,
			lehrer
		);

		expect(klassenListeManager.schulgliederungen.list().toArray()).toEqual(
			Schulgliederung.getBySchuljahrAndSchulform(
				2020,
				schulform
			).toArray()
		);
	});

	test("Initialization | Klasse not in Jahrgänge throws Exception", () => {
		const _klassen: ArrayList<KlassenDaten> = new ArrayList<KlassenDaten>();
		const k = new KlassenDaten();
		k.id = -1;
		k.idJahrgang = 2;
		_klassen.add(k);

		expect(() => {
			new KlassenListeManager(
				1,
				1,
				schuljahresabschnitte,
				schulform,
				_klassen,
				schueler,
				jahrgaenge,
				lehrer
			);
		}).toThrowError("Kein gültiger Schlüsselwert.");
	});

	test("Initialization | Klasse in Jahrgänge throws no Exception", () => {
		const _jahrgaenge: ArrayList<JahrgangsDaten> =
			new ArrayList<JahrgangsDaten>();
		const _klassen: ArrayList<KlassenDaten> = new ArrayList<KlassenDaten>();

		const jgd = new JahrgangsDaten();
		jgd.id = 1;
		_jahrgaenge.add(jgd);

		for (let i = 0; i < 10; i++) {
			const k = new KlassenDaten();
			k.id = -1;
			k.idJahrgang = 1;
			_klassen.add(k);
		}

		const manager = new KlassenListeManager(
			1,
			1,
			schuljahresabschnitte,
			schulform,
			_klassen,
			schueler,
			_jahrgaenge,
			lehrer
		);

		expect(manager).not.toBeNull();
		expect(manager.liste.size()).toBe(10);
	});

	test("Initialization | Klasse not in Jahrgänge throws no Exception if klassen.jahrgaenge is null", () => {
		const _jahrgaenge: ArrayList<JahrgangsDaten> =
			new ArrayList<JahrgangsDaten>();
		const _klassen: ArrayList<KlassenDaten> = new ArrayList<KlassenDaten>();

		const jgd = new JahrgangsDaten();
		jgd.id = 1;
		_jahrgaenge.add(jgd);

		const k = new KlassenDaten();
		k.id = -1;
		// k.idJahrgang bleibt undefined/null
		_klassen.add(k);

		const manager = new KlassenListeManager(
			1,
			1,
			schuljahresabschnitte,
			schulform,
			_klassen,
			schueler,
			_jahrgaenge,
			lehrer
		);

		expect(manager).not.toBeNull();
	});

	test("Update | Updating class data should reflect changes", () => {
		const manager = new KlassenListeManager(
			1,
			1,
			schuljahresabschnitte,
			schulform,
			klassen,
			schueler,
			jahrgaenge,
			lehrer
		);

		const originalData = new KlassenDaten();
		originalData.kuerzel = "A";

		const newData = new KlassenDaten();
		newData.kuerzel = "B";

		// access private method
		const updated = (manager as any).onSetDaten(originalData, newData);

		expect(updated).toBe(true);
		expect(originalData.kuerzel).toBe("B");
	});

	test("Update | Updating class returns true when klassenleitung is set", () => {
		const manager = new KlassenListeManager(
			1,
			1,
			schuljahresabschnitte,
			schulform,
			klassen,
			schueler,
			jahrgaenge,
			lehrer
		);

		manager.auswahlKlassenLeitung = new LehrerListeEintrag();

		const originalData = new KlassenDaten();
		originalData.kuerzel = "A";

		// access private method
		const result = (manager as any).onSetDaten(originalData, originalData);
		expect(result).toBe(true);
	});

	test("Reihenfolge | Klassenleitung Reihenfolge should be updated correctly", () => {
		const klassenleitungen: List<number> = ArrayList.of(1, 2, 3);
		const movedUp = KlassenListeManager.updateReihenfolgeKlassenleitung(
			klassenleitungen,
			2,
			true
		);
		expect(movedUp).toBe(true);
		expect(klassenleitungen.toArray()).toEqual([2, 1, 3]);

		const movedDown = KlassenListeManager.updateReihenfolgeKlassenleitung(
			klassenleitungen,
			2,
			false
		);
		expect(movedDown).toBe(true);
		expect(klassenleitungen.toArray()).toEqual([1, 2, 3]);
	});

	test("Sequence | Class management sequence should be false if list has single element", () => {
		const klassenleitungen: List<number> = ArrayList.of(1);
		const result = KlassenListeManager.updateReihenfolgeKlassenleitung(
			klassenleitungen,
			2,
			true
		);
		expect(result).toBe(false);
	});

	test("Sequence | Upper edge condition", () => {
		const klassenleitungen: List<number> = ArrayList.of(1, 2, 3);
		const result = KlassenListeManager.updateReihenfolgeKlassenleitung(
			klassenleitungen,
			1,
			true
		);
		expect(result).toBe(false);
		expect(klassenleitungen.toArray()).toEqual([1, 2, 3]);
	});

	test("Sequence | Bottom edge condition", () => {
		const klassenleitungen: List<number> = ArrayList.of(1, 2, 3);
		const result = KlassenListeManager.updateReihenfolgeKlassenleitung(
			klassenleitungen,
			3,
			false
		);
		expect(result).toBe(false);
		expect(klassenleitungen.toArray()).toEqual([1, 2, 3]);
	});

	test("Sequence | An error is thrown if the class teacher is not in the list", () => {
		const klassenleitungen: List<number> = ArrayList.of(1, 2);

		expect(() => {
			KlassenListeManager.updateReihenfolgeKlassenleitung(
				klassenleitungen,
				3,
				true
			);
		}).toThrowError(
			"Es wurde keine Klassenleitung mit der angegebenen Klassen- und Lehrer-ID gefunden."
		);
	});

	test("Retrieve Students | Should return a non-null list of students", () => {
		const manager = new KlassenListeManager(
			1,
			1,
			schuljahresabschnitte,
			schulform,
			klassen,
			schueler,
			jahrgaenge,
			lehrer
		);

		const schuelerListe = manager.getSchuelerListe();
		expect(schuelerListe).not.toBeNull();
	});

	test("Compare | KlassenDaten should be compared correctly based on 'klassen' criteria", () => {
		const manager = new KlassenListeManager(
			1,
			1,
			schuljahresabschnitte,
			schulform,
			klassen,
			schueler,
			jahrgaenge,
			lehrer
		);

		const klasse1 = new KlassenDaten();
		klasse1.id = 1;
		klasse1.kuerzel = "A";
		klasse1.schueler = ArrayList.of(
			new Schueler(),
			new Schueler()
		);

		const klasse2 = new KlassenDaten();
		klasse2.id = 2;
		klasse2.kuerzel = "B";
		klasse2.schueler = ArrayList.of(new Schueler());

		manager.orderSet(ArrayList.of(new Pair("klassen", true)));

		// access private method
		const result = (manager as any).compareAuswahl(klasse1, klasse2);

		expect(result).toBeLessThan(0);
	});

	test("Compare | KlassenDaten should be compared correctly based on 'schueleranzahl' criteria ascending", () => {
		const manager = new KlassenListeManager(
			1,
			1,
			schuljahresabschnitte,
			schulform,
			klassen,
			schueler,
			jahrgaenge,
			lehrer
		);

		const klasse1 = new KlassenDaten();
		klasse1.id = 1;
		klasse1.kuerzel = "A";
		klasse1.schueler = ArrayList.of(
			new Schueler(),
			new Schueler()
		);

		const klasse2 = new KlassenDaten();
		klasse2.id = 2;
		klasse2.kuerzel = "B";
		klasse2.schueler = ArrayList.of(new Schueler());

		manager.orderSet(ArrayList.of(new Pair("schueleranzahl", true))); // ascending

		// access private method
		const result = (manager as any).compareAuswahl(klasse1, klasse2);

		expect(result).toBeGreaterThan(0);
	});

	test("Compare | KlassenDaten should be compared correctly based on 'schueleranzahl' criteria descending", () => {
		const manager = new KlassenListeManager(
			1,
			1,
			schuljahresabschnitte,
			schulform,
			klassen,
			schueler,
			jahrgaenge,
			lehrer
		);

		const klasse1 = new KlassenDaten();
		klasse1.id = 1;
		klasse1.kuerzel = "A";
		klasse1.schueler = ArrayList.of(
			new Schueler(),
			new Schueler()
		);

		const klasse2 = new KlassenDaten();
		klasse2.id = 2;
		klasse2.kuerzel = "B";
		klasse2.schueler = ArrayList.of(new Schueler());

		manager.orderSet(ArrayList.of(new Pair("schueleranzahl", false))); // descending

		// access private method
		const result = (manager as any).compareAuswahl(klasse1, klasse2);

		expect(result).toBeLessThan(0);
	});

	test("Compare | DeveloperNotificationException should be thrown for unknown criteria", () => {
		const manager = new KlassenListeManager(
			1,
			1,
			schuljahresabschnitte,
			schulform,
			klassen,
			schueler,
			jahrgaenge,
			lehrer
		);

		const klasse1 = new KlassenDaten();
		klasse1.id = 1;
		klasse1.kuerzel = "A";
		klasse1.schueler = ArrayList.of(
			new Schueler(),
			new Schueler()
		);

		const klasse2 = new KlassenDaten();
		klasse2.id = 2;
		klasse2.kuerzel = "B";
		klasse2.schueler = ArrayList.of(new Schueler());

		manager.orderSet(ArrayList.of(new Pair("unknown", true)));

		expect(() => {
			// access private method
			(manager as any).compareAuswahl(klasse1, klasse2);
		}).toThrowError(
			"Fehler bei der Sortierung. Das Sortierkriterium wird vom Manager nicht unterstützt."
		);
	});
});
