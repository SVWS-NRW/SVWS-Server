import type { GostHalbjahr, GostJahrgangsdaten, GostKursklausur, GostKlausurplanManager, List, StundenplanKalenderwochenzuordnung, Wochentag, BenutzerKompetenz, GostKlausurtermin } from "@core";
import { type StundenplanManager, type StundenplanZeitraster } from "@core";
import type { GostKlausurplanungDragData, GostKlausurplanungDropZone } from "./SGostKlausurplanung";

export interface SGostKlausurplanungKalenderStundenplanAnsichtProps {
	benutzerKompetenzen: Set<BenutzerKompetenz>,
	mode?: 'schueler' | 'lehrer' | 'klasse';
	jahrgangsdaten: GostJahrgangsdaten;
	halbjahr: GostHalbjahr;
	ignoreEmpty?: boolean;
	id: number;
	kalenderwoche: () => StundenplanKalenderwochenzuordnung;
	manager: () => StundenplanManager;
	kMan: () => GostKlausurplanManager;
	wochentyp: () => number;
	// kalenderwoche: () => StundenplanKalenderwochenzuordnung | undefined;
	useDragAndDrop?: boolean;
	kurseGefiltert: (datum: string, day: Wochentag, stunde: number) => List<number>;
	patchKlausurtermin: (id: number, termin: Partial<GostKlausurtermin>) => Promise<void>;
	sumSchreiber: (datum: string, day: Wochentag, stunde: number) => number;
	dragData: () => GostKlausurplanungDragData;
	onDrag: (data: GostKlausurplanungDragData) => void;
	onDrop: (zone: GostKlausurplanungDropZone) => void;
	checkDropZoneZeitraster: (event: DragEvent, zeitraster: StundenplanZeitraster | undefined) => void;
	zeigeAlleJahrgaenge: () => boolean;
	kursklausurMouseOver: () => GostKursklausur | undefined;
	gotoKalenderdatum: (datum: string | undefined, termin: GostKlausurtermin | undefined) => Promise<void>;
	gotoRaumzeitTermin: (abiturjahr: number, halbjahr: GostHalbjahr, value: number) => Promise<void>;
}
