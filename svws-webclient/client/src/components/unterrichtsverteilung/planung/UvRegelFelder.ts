import type { UvBlockungRegel } from '@core/core/data/uv/regel/UvBlockungRegel';
import { UvBlockungRegelTyp } from '@core/core/data/uv/regel/UvBlockungRegelTyp';

export type UvRegelFeldArt = 'lehrer' | 'klasse' | 'lerngruppe' | 'fach' | 'jahrgang' | 'anzahl' | 'stunden' | 'tag' | 'stunde' | 'wochentyp' | 'aktivierung';
export interface UvRegelFeld { art: UvRegelFeldArt; name: string }
export interface UvRegelDefinition { typ: UvBlockungRegelTyp; felder: UvRegelFeld[]; liste?: 'lerngruppe' | 'jahrgang'; kategorie: string }

const feld = (art: UvRegelFeldArt, name: string): UvRegelFeld => ({ art, name });
const lehrer = feld('lehrer', 'Lehrkraft');
const klasse = feld('klasse', 'Klasse');
const lerngruppe = feld('lerngruppe', 'Lerngruppe');
const fach = feld('fach', 'Fach');
const anzahl = feld('anzahl', 'Anzahl');
const stunden = feld('stunden', 'Stunden');
const aktivierung = feld('aktivierung', 'Soll-Ist-Abweichung');

const muster: Record<number, { felder: UvRegelFeld[]; liste?: 'lerngruppe' | 'jahrgang'; kategorie: string }> = {
	1: { felder: [anzahl], kategorie: 'Klassenleitung' },
	2: { felder: [klasse, anzahl], kategorie: 'Klassenleitung' },
	3: { felder: [anzahl], kategorie: 'Klassenleitung' },
	4: { felder: [klasse, anzahl], kategorie: 'Klassenleitung' },
	5: { felder: [stunden], kategorie: 'Klassenleitung' },
	6: { felder: [lehrer, stunden], kategorie: 'Klassenleitung' },
	7: { felder: [stunden], kategorie: 'Klassenleitung' },
	8: { felder: [lehrer, stunden], kategorie: 'Klassenleitung' },
	9: { felder: [anzahl], kategorie: 'Klassenleitung' },
	10: { felder: [lehrer, anzahl], kategorie: 'Klassenleitung' },
	11: { felder: [anzahl], kategorie: 'Klassenleitung' },
	12: { felder: [lehrer, anzahl], kategorie: 'Klassenleitung' },
	13: { felder: [lehrer, lerngruppe], kategorie: 'Lerngruppe und Lehrkraft' },
	14: { felder: [lehrer, lerngruppe], kategorie: 'Lerngruppe und Lehrkraft' },
	15: { felder: [lehrer, lerngruppe], kategorie: 'Lerngruppe und Lehrkraft' },
	16: { felder: [lehrer, lerngruppe], kategorie: 'Lerngruppe und Lehrkraft' },
	17: { felder: [lerngruppe, feld('tag', 'Wochentag (0–7)'), feld('stunde', 'Unterrichtsstunde'), feld('wochentyp', 'Wochentyp (0=alle, 1=A, 2=B)')], kategorie: 'Zeit und Kooperation' },
	18: { felder: [lehrer, klasse], kategorie: 'Einsatzgrenzen' },
	19: { felder: [lehrer, feld('jahrgang', 'Jahrgang')], kategorie: 'Einsatzgrenzen' },
	20: { felder: [lehrer, anzahl, fach], kategorie: 'Fach und Jahrgang' },
	21: { felder: [lehrer, anzahl, fach], kategorie: 'Fach und Jahrgang' },
	22: { felder: [lehrer, anzahl], kategorie: 'Einsatzgrenzen' },
	23: { felder: [lehrer, anzahl], kategorie: 'Einsatzgrenzen' },
	24: { felder: [lehrer, fach], liste: 'jahrgang', kategorie: 'Fach und Jahrgang' },
	25: { felder: [lehrer, fach], kategorie: 'Fach und Jahrgang' },
	26: { felder: [lehrer, anzahl], kategorie: 'Einsatzgrenzen' },
	27: { felder: [feld('lehrer', 'Erste Lehrkraft'), feld('lehrer', 'Zweite Lehrkraft')], kategorie: 'Einsatzgrenzen' },
	28: { felder: [lerngruppe, anzahl], kategorie: 'Lerngruppe und Lehrkraft' },
	29: { felder: [], liste: 'lerngruppe', kategorie: 'Lerngruppen gemeinsam' },
	30: { felder: [], liste: 'lerngruppe', kategorie: 'Lerngruppen gemeinsam' },
	31: { felder: [stunden], liste: 'lerngruppe', kategorie: 'Lerngruppen gemeinsam' },
	32: { felder: [lehrer, klasse], kategorie: 'Klassenleitung' },
	33: { felder: [lehrer, klasse], kategorie: 'Klassenleitung' },
	34: { felder: [lehrer, klasse], kategorie: 'Klassenleitung' },
	35: { felder: [lehrer, klasse], kategorie: 'Klassenleitung' },
	36: { felder: [lehrer, klasse], kategorie: 'Klassenleitung' },
	37: { felder: [lehrer, klasse], kategorie: 'Klassenleitung' },
	38: { felder: [lehrer, klasse], kategorie: 'Klassenleitung' },
	39: { felder: [lehrer, klasse], kategorie: 'Klassenleitung' },
	40: { felder: [lehrer, anzahl], kategorie: 'Einsatzgrenzen' },
	41: { felder: [lehrer, anzahl], kategorie: 'Einsatzgrenzen' },
	42: { felder: [lehrer, anzahl], liste: 'lerngruppe', kategorie: 'Lerngruppen gemeinsam' },
	43: { felder: [lehrer, anzahl], liste: 'lerngruppe', kategorie: 'Lerngruppen gemeinsam' },
	44: { felder: [aktivierung], kategorie: 'Soll-Ist-Abweichung' },
	45: { felder: [lehrer, aktivierung], kategorie: 'Soll-Ist-Abweichung' },
};

export const uvRegelDefinitionen: UvRegelDefinition[] = UvBlockungRegelTyp.values().filter(typ => typ.nr > 0).map(typ => ({
	typ, ...muster[typ.nr],
}));

/** Liefert die in einer Regel gespeicherten Objektbezüge. */
export function getUvRegelReferenzen(regel: UvBlockungRegel): { art: UvRegelFeldArt; id: number }[] {
	const definition = uvRegelDefinitionen.find(item => item.typ.nr === regel.typ);
	if (definition === undefined) {
		return [];
	}
	const parameter = [...regel.parameter];
	const result: { art: UvRegelFeldArt; id: number }[] = [];
	definition.felder.forEach((feld, index) => {
		const id = parameter.at(index);
		if (id !== undefined) {
			result.push({ art: feld.art, id });
		}
	});
	if (definition.liste !== undefined) {
		for (const id of parameter.slice(definition.felder.length)) {
			result.push({ art: definition.liste, id });
		}
	}
	return result;
}
