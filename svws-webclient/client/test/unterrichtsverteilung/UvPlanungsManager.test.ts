import { describe, expect, test } from 'vitest';

import { FachDaten } from '@core/core/data/fach/FachDaten';
import { JahrgangsDaten } from '@core/core/data/jahrgang/JahrgangsDaten';
import { LehrerUnterrichtsfach } from '@core/core/data/lehrer/LehrerUnterrichtsfach';
import { UvBlockungRegel } from '@core/core/data/uv/regel/UvBlockungRegel';
import { UvBlockungRegelPrioritaet } from '@core/core/data/uv/regel/UvBlockungRegelPrioritaet';
import { UvFach } from '@core/core/data/uv/UvFach';
import { UvKlasse } from '@core/core/data/uv/UvKlasse';
import { UvKlassenLehrer } from '@core/core/data/uv/UvKlassenLehrer';
import { UvKursCreateRequest } from '@core/core/data/uv/UvKursCreateRequest';
import { UvKursImportDaten } from '@core/core/data/uv/UvKursImportDaten';
import { UvLehrer } from '@core/core/data/uv/UvLehrer';
import { UvLehrerPflichtstundensoll } from '@core/core/data/uv/UvLehrerPflichtstundensoll';
import { UvLerngruppe } from '@core/core/data/uv/UvLerngruppe';
import { UvLerngruppenLehrer } from '@core/core/data/uv/UvLerngruppenLehrer';
import { UvLerngruppenSchiene } from '@core/core/data/uv/UvLerngruppenSchiene';
import { UvPlanungsabschnitt } from '@core/core/data/uv/UvPlanungsabschnitt';
import { UvPlanungsabschnittLehrer } from '@core/core/data/uv/UvPlanungsabschnittLehrer';
import { UvPlanungsabschnittSchueler } from '@core/core/data/uv/UvPlanungsabschnittSchueler';
import { UvPlanungsabschnittZeitraster } from '@core/core/data/uv/UvPlanungsabschnittZeitraster';
import { UvSchiene } from '@core/core/data/uv/UvSchiene';
import { UvSchuelergruppe } from '@core/core/data/uv/UvSchuelergruppe';
import { UvSchuelergruppeSchueler } from '@core/core/data/uv/UvSchuelergruppeSchueler';
import { UvSchuelergruppeSchuelerCreateRequest } from '@core/core/data/uv/UvSchuelergruppeSchuelerCreateRequest';
import { UvZeitraster } from '@core/core/data/uv/UvZeitraster';
import { UvManager } from '@core/core/utils/uv/UvManager';
import { ArrayList } from '@core/java/util/ArrayList';
import type { List } from '@core/java/util/List';

import { UvPlanungsManager } from '~/components/unterrichtsverteilung/planung/UvPlanungsManager';
import { lehrerAuslastung } from '~/components/unterrichtsverteilung/UvPresenter';

function fixture(jahrgaenge: List<JahrgangsDaten> = new ArrayList<JahrgangsDaten>()) {
	const manager = new UvManager(jahrgaenge, ArrayList.of(Object.assign(new FachDaten(), { id: 1, kuerzel: 'D' })));
	const plan = Object.assign(new UvPlanungsabschnitt(), { id: 1, schuljahr: 2026, gueltigVon: '2026-08-01', gueltigBis: '2027-07-31' });
	manager.planungsabschnittAdd(plan);
	manager.fachAdd(Object.assign(new UvFach(), { id: 1, idFach: 1, gueltigVon: '2026-08-01' }));
	manager.schuelergruppeAdd(Object.assign(new UvSchuelergruppe(), { id: 1, idPlanungsabschnitt: plan.id }));
	manager.klasseAdd(Object.assign(new UvKlasse(), { id: 1, idPlanungsabschnitt: plan.id, idSchuelergruppe: 1 }));
	const gruppe = Object.assign(new UvLerngruppe(), { id: 1, idPlanungsabschnitt: plan.id, idKlasse: 1, idFach: 1, wochenstunden: 4, wochenstundenUnterrichtet: 4 });
	manager.lerngruppeAdd(gruppe);
	for (const id of [1, 2, 3]) {
		manager.lehrerAdd(Object.assign(new UvLehrer(), { id, kuerzel: `L${id}` }));
		manager.planungsabschnittLehrerAdd(Object.assign(new UvPlanungsabschnittLehrer(), { idPlanungsabschnitt: plan.id, idLehrer: id }));
	}
	manager.lehrerPflichtstundensollAdd(Object.assign(new UvLehrerPflichtstundensoll(), { id: 1, idLehrer: 1, pflichtstdSoll: 10, gueltigVon: '2026-08-01' }));
	manager.lerngruppenLehrerAdd(Object.assign(new UvLerngruppenLehrer(), { id: 1, idPlanungsabschnitt: plan.id, idLerngruppe: gruppe.id, idLehrer: 1, reihenfolge: 2, wochenstunden: 4, wochenstundenAngerechnet: 2 }));
	manager.lerngruppenLehrerAdd(Object.assign(new UvLerngruppenLehrer(), { id: 2, idPlanungsabschnitt: plan.id, idLerngruppe: gruppe.id, idLehrer: 2, reihenfolge: 4, wochenstunden: 1, wochenstundenAngerechnet: 1 }));
	return { manager: new UvPlanungsManager(manager, plan, []), gruppe, core: manager, plan };
}

test('Matrix behält den Bereich bei Filtern und zählt nur sichtbare Einsätze', () => {
	const { manager, core, plan, gruppe } = fixture();
	core.schuelergruppeAdd(Object.assign(new UvSchuelergruppe(), { id: 2, idPlanungsabschnitt: plan.id }));
	core.klasseAdd(Object.assign(new UvKlasse(), { id: 2, idPlanungsabschnitt: plan.id, idSchuelergruppe: 2, kuerzel: '09b' }));
	const offen = Object.assign(new UvLerngruppe(), gruppe, { id: 2, idKlasse: 2 });
	core.lerngruppeAdd(offen);

	const filter = { bereich: 'klassen', jahrgang: null, fachSuche: 'd', nurOffene: false, nurEinsaetze: false, idLehrer: 1 } as const;
	const einsaetze = manager.matrixAnsicht({ ...filter, nurEinsaetze: true }, null);
	expect(einsaetze.bereichLerngruppen.map(item => item.id)).toEqual([gruppe.id, offen.id]);
	expect(einsaetze.sichtbareEinsaetze).toBe(1);
	expect(einsaetze.zeilen[0]?.zellen.get('D')).toEqual([gruppe]);

	const offene = manager.matrixAnsicht({ ...filter, nurOffene: true }, null);
	expect(offene.sichtbareLerngruppen).toEqual([offen]);
	expect(offene.offeneAnzahl).toBe(1);
	expect(offene.sichtbareEinsaetze).toBe(0);
});

describe('UV-Planungsmanager – Stunden und Zuordnungen', () => {
	test('deaktiviert alle wirksamen Fixierungen ohne bestehende Regeln zu verändern', () => {
		const { manager, core, plan } = fixture();
		const erste = manager.lehrerFixierungsEntwurf(1, 1);
		erste.id = 10;
		const zweite = manager.lehrerFixierungsEntwurf(1, 1);
		zweite.id = 11;
		const mitRegeln = new UvPlanungsManager(core, plan, [erste, zweite]);
		const aenderungen = mitRegeln.lehrerFixierungUmschaltenRegeln('lerngruppe', 1, 1, 0);
		expect(aenderungen.map(regel => regel.id)).toEqual([10, 11]);
		expect(aenderungen.every(regel => regel.prioritaet === UvBlockungRegelPrioritaet.DEAKTIVIERT.nr)).toBe(true);
		expect(erste.prioritaet).toBe(UvBlockungRegelPrioritaet.SEHR_HOCH.nr);
		expect(zweite.prioritaet).toBe(UvBlockungRegelPrioritaet.SEHR_HOCH.nr);
	});

	test('verwendet eine deaktivierte Fixierung beim erneuten Fixieren wieder', () => {
		const { manager, core, plan } = fixture();
		const vorhanden = manager.lehrerFixierungsEntwurf(1, 1);
		vorhanden.id = 10;
		vorhanden.prioritaet = UvBlockungRegelPrioritaet.DEAKTIVIERT.nr;
		const mitRegeln = new UvPlanungsManager(core, plan, [vorhanden]);
		const aenderungen = mitRegeln.lehrerFixierungUmschaltenRegeln('lerngruppe', 1, 1, 0);
		expect(aenderungen).toHaveLength(1);
		expect(aenderungen[0]?.id).toBe(10);
		expect(aenderungen[0]?.prioritaet).toBe(UvBlockungRegelPrioritaet.SEHR_HOCH.nr);
		expect(vorhanden.prioritaet).toBe(UvBlockungRegelPrioritaet.DEAKTIVIERT.nr);
	});

	test('trennt Einsatzstunden und Deputat und ersetzt beim Bearbeiten nur die betroffene Zuordnung', () => {
		const { manager } = fixture();
		expect(manager.lehrerStunden(1)).toEqual({ verplant: 4, deputat: 2, soll: 10, anrechnung: 0 });
		expect(manager.lehrerRestNachZuordnung(1, -1, 1)).toBe(7);
		expect(manager.lehrerRestNachZuordnung(1, 1, 1)).toBe(9);
		expect([...manager.lehrerEinsatzIds(1)]).toEqual([1]);
	});

	test('behält eine vorhandene fachfremde Lehrkraft, lehnt aber einen Wechsel auf eine bereits zugeordnete oder ungeeignete Lehrkraft ab', () => {
		const { manager, gruppe } = fixture();
		expect(manager.lehrerZuordnungGueltig(gruppe, 1, 1, 2, 4, 2)).toBe(true);
		expect(manager.lehrerZuordnungGueltig(gruppe, 2, 1, 2, 4, 2)).toBe(false);
		expect(manager.lehrerZuordnungGueltig(gruppe, 3, 1, 2, 4, 2)).toBe(false);
		expect(manager.lehrerZuordnungGueltig(gruppe, 1, 999, 2, 4, 2)).toBe(false);
	});

	test('verhindert widersprüchliche Stunden sowohl bei Zuordnung als auch bei Reduzierung der Lerngruppenstunden', () => {
		const { manager, gruppe } = fixture();
		expect(manager.lehrerZuordnungGueltig(gruppe, 1, 1, 2, 1, 2)).toBe(false);
		expect(manager.lerngruppeStundenAenderungGueltig(Object.assign(new UvLerngruppe(), gruppe, { wochenstundenUnterrichtet: 3 }))).toBe(false);
		expect(manager.lerngruppeStundenAenderungGueltig(Object.assign(new UvLerngruppe(), gruppe, { wochenstundenUnterrichtet: 4 }))).toBe(true);
		expect(UvPlanungsManager.lehrerZuordnungswerteGueltig(1, Number.NaN, 4, 0)).toBe(false);
	});

	test('behält zugeordnete Zeitraster auch außerhalb ihrer Gültigkeit in der Auswahl (Widerlegung F09)', () => {
		const { core, plan } = fixture();
		const raster = Object.assign(new UvZeitraster(), { id: 1, bezeichnung: 'Altes Raster', gueltigVon: '2020-08-01', gueltigBis: '2021-07-31' });
		core.zeitrasterAdd(raster);
		core.planungsabschnittZeitrasterAdd(Object.assign(new UvPlanungsabschnittZeitraster(), { idPlanungsabschnitt: plan.id, idZeitraster: raster.id }));
		expect([...core.zeitrasterGetMengeGueltigByPlanungsabschnitt(plan)].map(item => item.id)).toEqual([raster.id]);
	});

	test('ermittelt eine neue Reihenfolge auch nach Löschungen und bei unsortierter Eingabe', () => {
		expect(UvPlanungsManager.naechsteReihenfolge([{ reihenfolge: 4 }, { reihenfolge: 2 }])).toBe(5);
		expect(UvPlanungsManager.naechsteReihenfolge([])).toBe(1);
	});
});


test('Kursimportvorschau verwendet tatsächliche Importzuordnungen und sortiert Jahrgänge ohne Duplikate', () => {
	const core = new UvManager(ArrayList.of(
		Object.assign(new JahrgangsDaten(), { id: 9, kuerzel: '09', sortierung: 1 }),
		Object.assign(new JahrgangsDaten(), { id: 10, kuerzel: '10', sortierung: 2 })
	), ArrayList.of(Object.assign(new FachDaten(), { id: 1, kuerzel: 'D' })));
	core.planungsabschnittAdd(Object.assign(new UvPlanungsabschnitt(), { id: 1, schuljahr: 2026, gueltigVon: '2026-08-01', gueltigBis: '2027-07-31' }));
	core.fachAdd(Object.assign(new UvFach(), { id: 1, idFach: 1, gueltigVon: '2026-08-01' }));
	const daten = new UvKursImportDaten();
	for (const [idSchueler, idJahrgang] of [[1, 10], [2, 9], [3, 10]]) {
		core.planungsabschnittSchuelerAdd(Object.assign(new UvPlanungsabschnittSchueler(), { idPlanungsabschnitt: 1, idSchueler, idJahrgang }));
		daten.schuelergruppenschueler.add(Object.assign(new UvSchuelergruppeSchuelerCreateRequest(), { idPlanungsabschnitt: 1, idSchuelergruppe: -1, idSchueler }));
	}
	for (const idSchuelergruppe of [-1, -2]) {
		daten.kurse.add(Object.assign(new UvKursCreateRequest(), { id: idSchuelergruppe, idSchuelergruppe, idFach: 1, kursart: 'GK', kursnummer: 1 }));
	}
	expect(UvPlanungsManager.kursImportVorschau(core, daten).map(row => row.bezeichnung)).toEqual(['09/10 D-GK1', 'D-GK1']);
});


test('Auslastung bleibt bei fehlendem Soll definiert und begrenzt Überlastung auf 100 Prozent', () => {
	expect(lehrerAuslastung(0, 0)).toEqual({ wert: 0, maximum: 0, prozent: 0 });
	expect(lehrerAuslastung(0, -2)).toEqual({ wert: 2, maximum: 2, prozent: 100 });
	expect(lehrerAuslastung(10, -2)).toEqual({ wert: 12, maximum: 12, prozent: 100 });
	expect(lehrerAuslastung(10, 5).prozent).toBe(50);
});


describe('UV-Planungsmanager – getrennte Ansichten', () => {
	test('stellt zugeordnete Lehrkräfte vor geeignete nach Reststunden und danach die übrigen alphabetisch', () => {
		const { core, plan } = fixture();
		for (const [id, kuerzel, soll] of [[4, 'Z', 15], [5, 'A', 10], [6, 'B', 20]] as const) {
			core.lehrerAdd(Object.assign(new UvLehrer(), { id, kuerzel }));
			core.planungsabschnittLehrerAdd(Object.assign(new UvPlanungsabschnittLehrer(), { idPlanungsabschnitt: plan.id, idLehrer: id }));
			core.lehrerPflichtstundensollAdd(Object.assign(new UvLehrerPflichtstundensoll(), { id, idLehrer: id, pflichtstdSoll: soll, gueltigVon: plan.gueltigVon }));
		}
		for (const id of [4, 5]) {
			core.lehrerUnterrichtsfachAdd(Object.assign(new LehrerUnterrichtsfach(), { id, idLehrer: id, idFach: 1, istKLehrer: false }));
		}
		const manager = new UvPlanungsManager(core, plan, []);
		const filter = { suche: '', auswahl: { art: 'lerngruppen' as const, id: 1 }, idLerngruppe: 1, idLehrer: null, nurGeeignete: false };
		const daten = manager.lehrerAnsicht(filter);
		expect(daten.eintraege.map(item => item.id)).toEqual([1, 2, 4, 5, 6, 3]);
		expect(daten.eintraege.filter(item => item.zugeordnet).map(item => item.id)).toEqual([1, 2]);
		expect(daten.eintraege.filter(item => item.zuweisbar).map(item => item.id)).toEqual([4, 5]);
		expect(manager.lehrerAnsicht({ ...filter, auswahl: null }).eintraege.map(item => item.id)).toEqual([5, 6, 1, 2, 3, 4]);
		expect(manager.lehrerAnsicht({ ...filter, suche: 'Z' }).eintraege.map(item => item.id)).toEqual([4]);
		expect(manager.lehrerAnsicht({ ...filter, suche: 'Z' }).gesamtAnzahl).toBe(6);
	});

	test('trennt gefilterte Lerngruppensummen von der Statistik und zählt mehrere Lehrkräfte nicht doppelt', () => {
		const { core, plan, gruppe } = fixture();
		core.schuelergruppeAdd(Object.assign(new UvSchuelergruppe(), { id: 2, idPlanungsabschnitt: plan.id }));
		core.klasseAdd(Object.assign(new UvKlasse(), { id: 2, idPlanungsabschnitt: plan.id, idSchuelergruppe: 2 }));
		core.lerngruppeAdd(Object.assign(new UvLerngruppe(), gruppe, { id: 2, idKlasse: 2, wochenstunden: 3, wochenstundenUnterrichtet: 3 }));
		const manager = new UvPlanungsManager(core, plan, []);
		const filter = { suche: '', auswahl: null, idLehrer: null, nurLehrerEinsaetze: false, jahrgang: null, fach: null, klasseKurs: null, status: 'offen' };
		const offen = manager.lerngruppenAnsicht(filter);
		expect(offen.anzahl).toBe(1);
		expect(offen.gruppen.flatMap(item => item.lerngruppen.map(lg => lg.id))).toEqual([2]);
		expect(offen.gesamtStunden).toBe(3);
		expect(offen.besetzteStunden).toBe(0);
		expect(manager.statistik()).toEqual({ lerngruppen: 2, besetzteLerngruppen: 1, geplanteStunden: 7, offeneStunden: 3 });
		expect(manager.lerngruppenAnsicht({ ...filter, status: null, idLehrer: 1, nurLehrerEinsaetze: true }).anzahl).toBe(1);
	});

	test('liefert für Sammelaktionen nur vorhandene ungesperrte Objekte unabhängig von der Suche', () => {
		const { core, plan } = fixture();
		core.schuelergruppeAdd(Object.assign(new UvSchuelergruppe(), { id: 2, idPlanungsabschnitt: plan.id }));
		core.klasseAdd(Object.assign(new UvKlasse(), { id: 2, idPlanungsabschnitt: plan.id, idSchuelergruppe: 2, kuerzel: 'Andere Klasse' }));
		core.schieneAdd(Object.assign(new UvSchiene(), { id: 1, idPlanungsabschnitt: plan.id, nummer: 1 }));
		const manager = new UvPlanungsManager(core, plan, []);
		expect([...manager.sammelauswahlIds('klassen')]).toEqual([2]);
		expect(manager.klassenAnsicht('Andere Klasse', null).anzahl).toBe(1);
		expect([...manager.sammelauswahlIds('lerngruppen')]).toEqual([1]);
		expect([...manager.sammelauswahlIds('schienen')]).toEqual([1]);
		expect(manager.schienenAnsicht('kein Treffer', null)).toEqual([]);
		expect([...manager.sammelauswahlIds('schienen')]).toEqual([1]);
		core.schieneRemoveById(1);
		expect(manager.sammelauswahlIds('schienen').size).toBe(0);
	});

	test('behält unbeteiligte Schüler in der Ansicht und kennzeichnet nur die Beziehungen', () => {
		const { core, plan } = fixture(ArrayList.of(Object.assign(new JahrgangsDaten(), { id: 1, kuerzel: '09', sortierung: 1 })));
		core.planungsabschnittSchuelerAdd(Object.assign(new UvPlanungsabschnittSchueler(), { idPlanungsabschnitt: plan.id, idSchueler: 10, idJahrgang: 1, idKlasse: 1 }));
		core.planungsabschnittSchuelerAdd(Object.assign(new UvPlanungsabschnittSchueler(), { idPlanungsabschnitt: plan.id, idSchueler: 20, idJahrgang: 1 }));
		core.schuelergruppeSchuelerAdd(Object.assign(new UvSchuelergruppeSchueler(), { idPlanungsabschnitt: plan.id, idSchuelergruppe: 1, idSchueler: 10 }));
		const manager = new UvPlanungsManager(core, plan, []);
		const daten = manager.schuelerAnsicht({ art: 'klassen', id: 1 });
		expect(daten.schueler.map(item => item.idSchueler)).toEqual([10, 20]);
		expect([...(daten.relevanteIds ?? [])]).toEqual([10]);
		expect(manager.schuelerAnsicht(null).relevanteIds).toBeUndefined();
	});

	test('ordnet betroffene Schienen zuerst ein und sperrt verwendete Schienen für die Sammelauswahl', () => {
		const { core, plan } = fixture();
		for (const id of [1, 2]) {
			core.schieneAdd(Object.assign(new UvSchiene(), { id, idPlanungsabschnitt: plan.id, nummer: id }));
		}
		core.lerngruppenSchieneAdd(Object.assign(new UvLerngruppenSchiene(), { idPlanungsabschnitt: plan.id, idLerngruppe: 1, idSchiene: 2 }));
		const manager = new UvPlanungsManager(core, plan, []);
		expect(manager.schienenAnsicht('', { art: 'lerngruppen', id: 1 }).map(item => item.id)).toEqual([2, 1]);
		expect(manager.schienenAnsicht('', null).map(item => item.id)).toEqual([1, 2]);
		expect([...manager.sammelauswahlIds('schienen')]).toEqual([1]);
	});

});


describe('UV-Planungsmanager – Mitgliedschaftsänderungen', () => {
	test('prüft alle Lehrkräfte vor dem Hinzufügen und entfernt doppelte IDs', () => {
		const { core, plan } = fixture();
		core.lehrerAdd(Object.assign(new UvLehrer(), { id: 4, kuerzel: 'NEU' }));
		core.lehrerAdd(Object.assign(new UvLehrer(), { id: 5, kuerzel: 'ALT', datumAbgang: '2025-07-31' }));
		const manager = new UvPlanungsManager(core, plan, []);
		expect(manager.lehrerPlanungsabschnittHinzufuegenPruefen([4, 4]).map(item => item.id)).toEqual([4]);
		expect(() => manager.lehrerPlanungsabschnittHinzufuegenPruefen([4, 5])).toThrow();
		expect(() => manager.lehrerPlanungsabschnittHinzufuegenPruefen([1])).toThrow();
		expect(() => manager.lehrerPlanungsabschnittHinzufuegenPruefen([999])).toThrow();
		expect(core.lehrerGetMengeByPlanungsabschnitt(plan).size()).toBe(3);
	});

	test('erlaubt nur das Entfernen unbenutzter Mitglieder und berücksichtigt die Klassenleitung', () => {
		const { core, plan } = fixture();
		core.lehrerAdd(Object.assign(new UvLehrer(), { id: 4, kuerzel: 'EXTERN' }));
		const manager = new UvPlanungsManager(core, plan, []);
		expect(manager.lehrerPlanungsabschnittEntfernenPruefen([3, 3]).map(item => item.id)).toEqual([3]);
		expect(() => manager.lehrerPlanungsabschnittEntfernenPruefen([3, 1])).toThrow();
		expect(() => manager.lehrerPlanungsabschnittEntfernenPruefen([4])).toThrow();
		core.klassenLehrerAdd(Object.assign(new UvKlassenLehrer(), { id: 1, idPlanungsabschnitt: plan.id, idKlasse: 1, idLehrer: 3, reihenfolge: 1 }));
		expect(() => manager.lehrerPlanungsabschnittEntfernenPruefen([3])).toThrow();
		expect(core.lehrerGetMengeByPlanungsabschnitt(plan).size()).toBe(3);
	});

	test('weist Schülergruppen und Schüler aus einem anderen Planungsabschnitt zurück', () => {
		const { core, plan } = fixture();
		const andererPlan = Object.assign(new UvPlanungsabschnitt(), { id: 2, schuljahr: 2027, gueltigVon: '2027-08-01', gueltigBis: '2028-07-31' });
		core.planungsabschnittAdd(andererPlan);
		core.schuelergruppeAdd(Object.assign(new UvSchuelergruppe(), { id: 2, idPlanungsabschnitt: andererPlan.id }));
		core.planungsabschnittSchuelerAdd(Object.assign(new UvPlanungsabschnittSchueler(), { idPlanungsabschnitt: plan.id, idSchueler: 10 }));
		core.planungsabschnittSchuelerAdd(Object.assign(new UvPlanungsabschnittSchueler(), { idPlanungsabschnitt: andererPlan.id, idSchueler: 20 }));
		const manager = new UvPlanungsManager(core, plan, []);
		expect(manager.schuelergruppenAenderungPruefen(1, [10, 10]).id).toBe(1);
		expect(() => manager.schuelergruppenAenderungPruefen(2, [10])).toThrow();
		expect(() => manager.schuelergruppenAenderungPruefen(1, [10, 20])).toThrow();
		expect(() => manager.schuelergruppenAenderungPruefen(1, [999])).toThrow();
		expect(core.schuelergruppeSchuelerGetMengeBySchuelergruppe(plan.id, 1).size()).toBe(0);
	});
});


describe('UV-Planungsmanager – Detailgültigkeit und Löschsperren', () => {
	test('verlangt bei Lehrkräften die Zugehörigkeit zum Planungsabschnitt', () => {
		const { core, plan } = fixture();
		core.lehrerAdd(Object.assign(new UvLehrer(), { id: 4, kuerzel: 'EXTERN' }));
		const manager = new UvPlanungsManager(core, plan, []);

		expect(manager.planungsObjektGueltig('lehrer', 1)).toBe(true);
		expect(manager.planungsObjektGueltig('lehrer', 4)).toBe(false);
		expect(manager.planungsObjektGueltig('lehrer', 999)).toBe(false);
		expect(manager.planungsObjektLoeschbar('lehrer', 1)).toBe(false);
	});

	test('weist fremde Objekte zurück und lässt vorhandene deaktivierte Regeln darstellbar', () => {
		const { core, plan } = fixture();
		const andererPlan = Object.assign(new UvPlanungsabschnitt(), { id: 2, schuljahr: 2027, gueltigVon: '2027-08-01', gueltigBis: '2028-07-31' });
		core.planungsabschnittAdd(andererPlan);
		core.schuelergruppeAdd(Object.assign(new UvSchuelergruppe(), { id: 2, idPlanungsabschnitt: andererPlan.id }));
		core.klasseAdd(Object.assign(new UvKlasse(), { id: 2, idPlanungsabschnitt: andererPlan.id, idSchuelergruppe: 2 }));
		core.schieneAdd(Object.assign(new UvSchiene(), { id: 2, idPlanungsabschnitt: andererPlan.id, nummer: 1 }));
		core.planungsabschnittSchuelerAdd(Object.assign(new UvPlanungsabschnittSchueler(), { idPlanungsabschnitt: andererPlan.id, idSchueler: 20 }));
		const regel = Object.assign(new UvBlockungRegel(), { id: 10, prioritaet: UvBlockungRegelPrioritaet.DEAKTIVIERT.nr });
		const manager = new UvPlanungsManager(core, plan, [regel]);

		expect(manager.planungsObjektGueltig('lerngruppen', 1)).toBe(true);
		expect(manager.planungsObjektGueltig('klassen', 1)).toBe(true);
		expect(manager.planungsObjektGueltig('klassen', 2)).toBe(false);
		expect(manager.planungsObjektGueltig('schienen', 2)).toBe(false);
		expect(manager.planungsObjektGueltig('schueler', 20)).toBe(false);
		expect(manager.planungsObjektGueltig('regeln', 10)).toBe(true);
		expect(manager.planungsObjektGueltig('regeln', 999)).toBe(false);
		expect(manager.planungsObjektLoeschbar('regeln', 10)).toBe(false);
		expect(manager.planungsObjektGueltig('matrix', 1)).toBe(false);
	});

	test('verwendet identische Löschsperren für Einzel- und Sammellöschung und lehnt gemischte Anforderungen vollständig ab', () => {
		const { core, plan } = fixture();
		core.schuelergruppeAdd(Object.assign(new UvSchuelergruppe(), { id: 2, idPlanungsabschnitt: plan.id }));
		core.klasseAdd(Object.assign(new UvKlasse(), { id: 2, idPlanungsabschnitt: plan.id, idSchuelergruppe: 2 }));
		const manager = new UvPlanungsManager(core, plan, []);

		for (const id of [1, 2, 999]) {
			expect(manager.planungsObjektLoeschbar('klassen', id)).toBe(manager.sammelauswahlIds('klassen').has(id));
		}
		expect(manager.planungsObjektLoeschbar('klassen', 1)).toBe(false);
		expect(manager.planungsObjektLoeschbar('klassen', 2)).toBe(true);
		expect([...manager.planungsLoeschauswahlPruefen('klassen', [2, 2])]).toEqual([2]);
		expect(() => manager.planungsLoeschauswahlPruefen('klassen', [2, 1])).toThrow();
		expect(() => manager.planungsLoeschauswahlPruefen('klassen', [2, 999])).toThrow();
		expect(manager.planungsLoeschauswahlPruefen('klassen', []).size).toBe(0);
		expect(core.klasseGetByIdOrNull(2)).not.toBeNull();
	});
});


describe('UV-Planungsmanager – Zuordnungskandidaten', () => {
	test('liefert Lehrkraftdaten ohne bereits zugeordnete Lehrkräfte und berücksichtigt die Lehrbefähigung', () => {
		const { core, plan } = fixture();
		core.lehrerUnterrichtsfachAdd(Object.assign(new LehrerUnterrichtsfach(), { id: 3, idLehrer: 3, idFach: 1, istKLehrer: false }));
		const manager = new UvPlanungsManager(core, plan, []);
		const daten = manager.zuordnungsAuswahl({ art: 'lerngruppe-lehrer', idZiel: 1, seite: 'rechts' }, true);
		expect(daten.zugeordnet).toBe(2);
		expect([...daten.verfuegbareIds]).toEqual([3]);
		expect(daten.lehrer.map(item => item.text)).toEqual(['L3']);
		expect(daten.eintraege).toEqual([]);
		expect(manager.lehrerKandidatenSuchen(daten.lehrer, 'L3')).toHaveLength(1);
		expect(manager.lehrerKandidatenSuchen(daten.lehrer, 'L1')).toHaveLength(0);
		expect(daten.verfuegbareIds.size).toBe(1);
	});

	test('schließt bei Klassenlehrkräften nur die Klassenleitungen aus und verlangt keine Fach-Lehrbefähigung', () => {
		const { core, plan } = fixture();
		core.klassenLehrerAdd(Object.assign(new UvKlassenLehrer(), { id: 1, idPlanungsabschnitt: plan.id, idKlasse: 1, idLehrer: 1, reihenfolge: 1 }));
		const manager = new UvPlanungsManager(core, plan, []);
		const daten = manager.zuordnungsAuswahl({ art: 'klasse-lehrer', idZiel: 1, seite: 'links' }, true);
		expect(daten.zugeordnet).toBe(1);
		expect([...daten.verfuegbareIds]).toEqual([2, 3]);
		expect(daten.lehrer.map(item => item.id)).toEqual([2, 3]);
	});

	test('liefert Schüler als Originalobjekte und schließt Gruppenmitglieder aus', () => {
		const { core, plan } = fixture();
		const verfuegbar = Object.assign(new UvPlanungsabschnittSchueler(), { idPlanungsabschnitt: plan.id, idSchueler: 20 });
		core.planungsabschnittSchuelerAdd(Object.assign(new UvPlanungsabschnittSchueler(), { idPlanungsabschnitt: plan.id, idSchueler: 10 }));
		core.planungsabschnittSchuelerAdd(verfuegbar);
		core.schuelergruppeSchuelerAdd(Object.assign(new UvSchuelergruppeSchueler(), { idPlanungsabschnitt: plan.id, idSchuelergruppe: 1, idSchueler: 10 }));
		const manager = new UvPlanungsManager(core, plan, []);
		const daten = manager.zuordnungsAuswahl({ art: 'gruppe-schueler', idZiel: 1, seite: 'rechts' }, false);
		expect(daten.zugeordnet).toBe(1);
		expect([...daten.verfuegbareIds]).toEqual([20]);
		expect(daten.schueler[0]).toBe(verfuegbar);
		expect(daten.eintraege).toEqual([]);
		expect(daten.lehrer).toEqual([]);
	});
});
