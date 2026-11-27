import type { UvPlanungsabschnittSchueler } from '@core/core/data/uv/UvPlanungsabschnittSchueler';
import type { UvZuordnungArt } from '@ui/states/UvState';

export const uvBereichArten = [
	{ value: 'matrix', text: 'Matrix', planungsobjekte: false },
	{ value: 'lerngruppen', text: 'Lerngruppen', planungsobjekte: true },
	{ value: 'lehrer', text: 'Lehrkräfte', planungsobjekte: true },
	{ value: 'klassen', text: 'Klassen', planungsobjekte: true },
	{ value: 'schueler', text: 'Schüler', planungsobjekte: true },
	{ value: 'schienen', text: 'Schienen', planungsobjekte: true },
	{ value: 'regeln', text: 'Regeln', planungsobjekte: true },
	{ value: 'details', text: 'Details', planungsobjekte: false },
	{ value: 'statistik', text: 'Statistik', planungsobjekte: false },
] as const;

type UvBereich = typeof uvBereichArten[number];
export type UvBereichArt = UvBereich['value'];

/** Bereiche, deren Einträge als Planungsobjekte ausgewählt werden können. */
export type UvPlanungsAuswahlArt = Extract<UvBereich, { planungsobjekte: true }>['value'];

export function istUvPlanungsAuswahlArt(art: unknown): art is UvPlanungsAuswahlArt {
	return uvBereichArten.some(bereich => bereich.planungsobjekte && bereich.value === art);
}

/** Das vom Routing aufgelöste Detailziel innerhalb der UV-Planung. */
export type UvDetailZiel = UvPlanungsAuswahl | { art: 'abschnitt' };

export interface UvPlanungsAuswahl {
	art: UvPlanungsAuswahlArt;
	id: number;
}

export type UvAuswahlStatus = 'neutral' | 'direkt' | 'bezogen' | 'geeignet' | 'unbeteiligt';

export type { UvZuordnungArt } from '@ui/states/UvState';

export interface UvZuordnungBearbeitung {
	art: UvZuordnungArt;
	idZiel: number;
	seite: 'links' | 'rechts';
}

/** Beschriftungen aller unterstützten Planungszuordnungen. */
export const uvZuordnungTitel: Record<UvZuordnungArt, string> = {
	'lerngruppe-lehrer': 'Lehrkräfte',
	'lerngruppe-schiene': 'Schienen',
	'schiene-lerngruppe': 'Lerngruppen',
	'gruppe-schueler': 'Schüler',
	'klasse-lehrer': 'Klassenlehrkräfte',
	'abschnitt-zeitraster': 'Zeitraster',
	'schiene-jahrgang': 'Jahrgänge',
	'gruppe-jahrgang': 'Jahrgänge',
	'zeitraster-jahrgang': 'Jahrgänge',
};

export function istUvZuordnungArt(art: unknown): art is UvZuordnungArt {
	return typeof art === 'string' && Object.hasOwn(uvZuordnungTitel, art);
}

export interface UvSchuelerAenderungsentwurf {
	daten: Partial<UvPlanungsabschnittSchueler>;
	speichert: boolean;
	fehler: string;
}
