import type { UvPlanungsabschnittSchueler } from '@core/core/data/uv/UvPlanungsabschnittSchueler';
import type { SortByAndOrder } from '@ui/types';

import type { createUvPresenter } from '../UvPresenter';

type Presenter = ReturnType<typeof createUvPresenter>;
export interface UvSchuelerTabellenZeile {
	schueler: UvPlanungsabschnittSchueler;
	idSchueler: number;
	nachname: string;
	vorname: string;
	klasse: string;
	jahrgang: string;
}

export interface UvSchuelerTabellenFilter {
	jahrgang: string | null;
	klasse: string | null;
	suche: string;
}

function schuelerTabellenZeile(schueler: UvPlanungsabschnittSchueler, presenter: Presenter): UvSchuelerTabellenZeile {
	return {
		schueler,
		idSchueler: schueler.idSchueler,
		nachname: schueler.daten.nachname,
		vorname: schueler.daten.vorname,
		klasse: schueler.idKlasse === null ? 'Ohne Klasse' : presenter.klasseKuerzel(schueler.idKlasse),
		jahrgang: presenter.jahrgangKuerzel(schueler.idJahrgang),
	};
}

/** Berechnet Filteroptionen und Tabellenzeilen für alle UV-Schülerlisten. */
export function uvSchuelerTabellenDaten(items: UvPlanungsabschnittSchueler[], presenter: Presenter, filter: UvSchuelerTabellenFilter): {
	jahrgaenge: string[];
	klassen: string[];
	zeilen: UvSchuelerTabellenZeile[];
} {
	const alleZeilen = items.map(item => schuelerTabellenZeile(item, presenter));
	const jahrgaenge = [...new Set(alleZeilen.map(item => item.jahrgang))].sort((a, b) => a.localeCompare(b, 'de', { numeric: true }));
	const klassen = [...new Set(alleZeilen.map(item => item.klasse))].sort((a, b) => a.localeCompare(b, 'de', { numeric: true }));
	const suche = filter.suche.trim().toLocaleLowerCase('de');
	const zeilen = alleZeilen.filter(item => (filter.jahrgang === null || item.jahrgang === filter.jahrgang)
		&& (filter.klasse === null || item.klasse === filter.klasse)
		&& `${item.nachname} ${item.vorname} ${item.klasse}`.toLocaleLowerCase('de').includes(suche));
	return { jahrgaenge, klassen, zeilen };
}

/** Hält betroffene Schüler vor den übrigen und sortiert innerhalb beider Gruppen nach der gewählten Tabellenspalte. */
export function uvSchuelerSortierteZeilen(items: UvSchuelerTabellenZeile[],
	relevantIds: Set<number> | undefined, sortierung: SortByAndOrder): UvSchuelerTabellenZeile[] {
	const wert = (item: UvSchuelerTabellenZeile): string => {
		switch (sortierung.key) {
			case 'vorname': {
				return item.vorname;
			}
			case 'klasse': {
				return item.klasse;
			}
			case 'jahrgang': {
				return item.jahrgang;
			}
			default: {
				return item.nachname;
			}
		}
	};
	return [...items].sort((a, b) => {
		const relevanz = relevantIds === undefined ? 0 : Number(!relevantIds.has(a.idSchueler)) - Number(!relevantIds.has(b.idSchueler));
		if (relevanz !== 0 || sortierung.key === null || sortierung.order === null) {
			return relevanz;
		}
		const richtung = sortierung.order === false ? -1 : 1;
		const vergleich = richtung * wert(a).localeCompare(wert(b), 'de', { numeric: true });
		if (vergleich !== 0) {
			return vergleich;
		}
		const nachname = a.nachname.localeCompare(b.nachname, 'de', { numeric: true });
		return nachname !== 0 ? nachname : a.vorname.localeCompare(b.vorname, 'de', { numeric: true });
	});
}

/** Verknüpft die selektierten Originaleinträge mit den sichtbaren Tabellenzeilen. */
export function uvSchuelerAuswahlZeilen(
	zeilen: UvSchuelerTabellenZeile[],
	selected: UvPlanungsabschnittSchueler[],
	presenter: Presenter
): UvSchuelerTabellenZeile[] {
	const sichtbar = new Map(zeilen.map(item => [item.idSchueler, item]));
	return selected.map(item => sichtbar.get(item.idSchueler) ?? schuelerTabellenZeile(item, presenter));
}

export function uvSchuelerAuswahlItems(items: UvPlanungsabschnittSchueler[], zeilen: UvSchuelerTabellenZeile[]): UvPlanungsabschnittSchueler[] {
	const ids = new Set(zeilen.map(item => item.idSchueler));
	return items.filter(item => ids.has(item.idSchueler));
}
