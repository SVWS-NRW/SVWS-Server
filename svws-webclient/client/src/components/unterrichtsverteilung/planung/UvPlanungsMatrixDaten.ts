import type { UvLerngruppe } from "@core/core/data/uv/UvLerngruppe";

export interface UvMatrixFilter {
	bereich: 'alle' | 'klassen' | 'kurse';
	jahrgang: string | null;
	fachSuche: string;
	nurOffene: boolean;
	nurEinsaetze: boolean;
	idLehrer: number | null;
}
export interface UvMatrixZeile {
	id: string;
	text: string;
	jahrgang: string;
	typ: 'klasse' | 'kurs';
	offen: number;
	jahrgangWechsel: boolean;
	zellen: Map<string, UvLerngruppe[]>;
}

export interface UvMatrixEintrag {
	lerngruppe: UvLerngruppe;
	jahrgang: string;
	fach: string;
	bezeichnung: string;
	offen: boolean;
	prioritaet: number;
	fachPrioritaet: number;
}

export interface UvMatrixAnsicht {
	bereichLerngruppen: UvLerngruppe[];
	jahrgaenge: string[];
	sichtbareLerngruppen: UvLerngruppe[];
	einsatzIds: Set<number>;
	sichtbareEinsaetze: number;
	offeneAnzahl: number;
	faecher: { id: number | null; kuerzel: string }[];
	zeilen: UvMatrixZeile[];
}

/** Baut die gefilterte Matrix aus den vom Planungsmanager vorbereiteten Einträgen. */
export function matrixDatenAufbereiten(
	eintraege: UvMatrixEintrag[],
	filter: UvMatrixFilter,
	einsatzIds: Set<number>,
	fachDaten: ReadonlyMap<string, { id: number; reihenfolge: number }>
): UvMatrixAnsicht {
	const jahrgaenge = [...new Set(eintraege.map(item => item.jahrgang))].sort((a, b) => a.localeCompare(b, 'de', { numeric: true }));
	const fachSuche = filter.fachSuche.trim().toLocaleLowerCase('de');
	const sichtbar = eintraege.filter(item => (filter.jahrgang === null || item.jahrgang === filter.jahrgang)
		&& (!filter.nurOffene || item.offen)
		&& (!filter.nurEinsaetze || filter.idLehrer === null || einsatzIds.has(item.lerngruppe.id))
		&& item.fach.toLocaleLowerCase('de').includes(fachSuche));
	const sichtbareLerngruppen = sichtbar.map(item => item.lerngruppe);
	const sichtbareEinsaetze = sichtbar.filter(item => einsatzIds.has(item.lerngruppe.id)).length;
	const offeneAnzahl = sichtbar.filter(item => item.offen).length;

	const faecher = matrixFachspalten(sichtbar, fachDaten);
	const zeilen = matrixZeilen(sichtbar);
	return { bereichLerngruppen: eintraege.map(item => item.lerngruppe), jahrgaenge, sichtbareLerngruppen, einsatzIds, sichtbareEinsaetze, offeneAnzahl, faecher, zeilen };
}

function matrixFachspalten(sichtbar: UvMatrixEintrag[], fachDaten: ReadonlyMap<string, { id: number; reihenfolge: number }>): UvMatrixAnsicht['faecher'] {
	const fachPrioritaet = new Map<string, number>();
	for (const item of sichtbar) {
		fachPrioritaet.set(item.fach, Math.min(fachPrioritaet.get(item.fach) ?? 2, item.fachPrioritaet));
	}
	return [...fachPrioritaet.keys()].sort((a, b) => {
		const relevanz = (fachPrioritaet.get(a) ?? 2) - (fachPrioritaet.get(b) ?? 2);
		if (relevanz !== 0) {
			return relevanz;
		}
		const reihenfolge = (fachDaten.get(a)?.reihenfolge ?? Number.MAX_SAFE_INTEGER) - (fachDaten.get(b)?.reihenfolge ?? Number.MAX_SAFE_INTEGER);
		return reihenfolge !== 0 ? reihenfolge : a.localeCompare(b, 'de');
	}).map(kuerzel => ({ id: fachDaten.get(kuerzel)?.id ?? null, kuerzel }));
}

function matrixZeilen(sichtbar: UvMatrixEintrag[]): UvMatrixZeile[] {
	const zeilenMap = new Map<string, UvMatrixZeile>();
	const zeilenPrioritaeten = new Map<string, number>();
	const eintraegeById = new Map(sichtbar.map(item => [item.lerngruppe.id, item]));
	for (const item of sichtbar) {
		const gruppe = item.lerngruppe;
		const istKurs = gruppe.idKurs !== null;
		const id = istKurs ? `j-${item.jahrgang}` : `k-${gruppe.idKlasse}`;
		let zeile = zeilenMap.get(id);
		if (zeile === undefined) {
			zeile = { id, text: istKurs ? item.jahrgang : item.bezeichnung, jahrgang: item.jahrgang,
				typ: istKurs ? 'kurs' : 'klasse', offen: 0, jahrgangWechsel: false, zellen: new Map() };
			zeilenMap.set(id, zeile);
		}
		zeile.offen += Number(item.offen);
		zeilenPrioritaeten.set(id, Math.min(zeilenPrioritaeten.get(id) ?? 2, item.prioritaet === 0 ? 0 : 2));
		const zelle = zeile.zellen.get(item.fach) ?? [];
		zelle.push(gruppe);
		zeile.zellen.set(item.fach, zelle);
	}
	for (const zeile of zeilenMap.values()) {
		for (const zelle of zeile.zellen.values()) {
			zelle.sort((a, b) => {
				const eintragA = eintraegeById.get(a.id);
				const eintragB = eintraegeById.get(b.id);
				const prioritaet = (eintragA?.prioritaet ?? 2) - (eintragB?.prioritaet ?? 2);
				return prioritaet !== 0 ? prioritaet : (eintragA?.bezeichnung ?? '').localeCompare(eintragB?.bezeichnung ?? '', 'de', { numeric: true });
			});
		}
	}
	const zeilen = [...zeilenMap.values()].sort((a, b) => {
		const relevanz = (zeilenPrioritaeten.get(a.id) ?? 2) - (zeilenPrioritaeten.get(b.id) ?? 2);
		if (relevanz !== 0) {
			return relevanz;
		}
		const jahrgang = a.jahrgang.localeCompare(b.jahrgang, 'de', { numeric: true });
		if (jahrgang !== 0) {
			return jahrgang;
		}
		if (a.typ !== b.typ) {
			return a.typ === 'klasse' ? -1 : 1;
		}
		return a.text.localeCompare(b.text, 'de', { numeric: true });
	});
	for (const [index, zeile] of zeilen.entries()) {
		zeile.jahrgangWechsel = index > 0 && zeile.jahrgang !== zeilen[index - 1]?.jahrgang;
	}
	return zeilen;
}
