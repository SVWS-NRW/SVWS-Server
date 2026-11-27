import { istUvZuordnungArt, type UvZuordnungArt } from './UvPlanung';

export const uvDragFormat = 'application/x-svws-uv';

export type UvDragAktion = 'hinzufuegen' | 'entfernen';

const lehrerDragFormat = {
	hinzufuegen: `${uvDragFormat}-lehrer-hinzufuegen`,
	entfernen: `${uvDragFormat}-lehrer-entfernen`,
};

/** Gemeinsames Datenformat für die drei UV-Bearbeitungsabläufe. */
export type UvDragDaten =
	| { typ: 'lehrer-mitgliedschaft' | 'lerngruppe-entwurf'; aktion: UvDragAktion; id: number }
	| { typ: 'zuordnung'; aktion: 'hinzufuegen'; id: number; art: UvZuordnungArt; idZiel: number }
	| { typ: 'zuordnung'; aktion: 'entfernen'; id: number; art: 'gruppe-schueler'; idZiel: number };

export function uvDragDatenSchreiben(transfer: DataTransfer, daten: UvDragDaten): void {
	transfer.setData(uvDragFormat, JSON.stringify(daten));
	if (daten.typ === 'lehrer-mitgliedschaft') {
		transfer.setData(lehrerDragFormat[daten.aktion], '1');
	}
	transfer.effectAllowed = daten.typ === 'lehrer-mitgliedschaft' || daten.aktion === 'entfernen' ? 'move' : 'copy';
}

/** Beim Überfahren sind die Formate verfügbar, die Nutzdaten erst beim Ablegen. */
export function uvDragLehrerAktionLesen(transfer: DataTransfer): UvDragAktion | null {
	if (transfer.types.includes(lehrerDragFormat.hinzufuegen)) {
		return 'hinzufuegen';
	}
	if (transfer.types.includes(lehrerDragFormat.entfernen)) {
		return 'entfernen';
	}
	return null;
}

/** Liest ausschließlich vollständige UV-Daten; fremde oder ungültige Drag-Inhalte werden ignoriert. */
export function uvDragDatenLesen(transfer: DataTransfer): UvDragDaten | null {
	const inhalt = transfer.getData(uvDragFormat);
	if (inhalt === '') {
		return null;
	}
	try {
		const daten: unknown = JSON.parse(inhalt);
		if (typeof daten !== 'object' || daten === null || !('typ' in daten) || !('aktion' in daten) || !('id' in daten)
			|| (daten.aktion !== 'hinzufuegen' && daten.aktion !== 'entfernen')
			|| typeof daten.id !== 'number' || !Number.isSafeInteger(daten.id) || daten.id < 0) {
			return null;
		}
		if (daten.typ === 'lehrer-mitgliedschaft' || daten.typ === 'lerngruppe-entwurf') {
			return { typ: daten.typ, aktion: daten.aktion, id: daten.id };
		}
		if (daten.typ !== 'zuordnung' || !('art' in daten) || !istUvZuordnungArt(daten.art) || !('idZiel' in daten)
			|| typeof daten.idZiel !== 'number' || !Number.isSafeInteger(daten.idZiel) || daten.idZiel < 0) {
			return null;
		}
		const zuordnung = { typ: daten.typ, id: daten.id, idZiel: daten.idZiel } as const;
		if (daten.aktion === 'hinzufuegen') {
			return { ...zuordnung, aktion: daten.aktion, art: daten.art };
		}
		return daten.art === 'gruppe-schueler' ? { ...zuordnung, aktion: daten.aktion, art: daten.art } : null;
	} catch {
		return null;
	}
}
