import type { GostHalbjahr, GostJahrgangsdaten, GostKursklausur, GostKlausurplanManager, List, StundenplanKalenderwochenzuordnung, Wochentag, BenutzerKompetenz } from "@core";
import { type StundenplanManager, type StundenplanZeitraster } from "@core";
import type { GostKlausurplanungDragData, GostKlausurplanungDropZone } from "./SGostKlausurplanung";
import type { WritableComputedRef } from "vue";

export interface SGostKlausurplanungKalenderStundenplanAnsichtProps {
	benutzerKompetenzen: Set<BenutzerKompetenz>,
	mode?: 'schueler' | 'lehrer' | 'klasse';
	jahrgangsdaten: GostJahrgangsdaten;
	halbjahr: GostHalbjahr;
	ignoreEmpty?: boolean;
	id: number;
	kalenderwoche: WritableComputedRef<StundenplanKalenderwochenzuordnung>;
	manager: () => StundenplanManager;
	kMan: () => GostKlausurplanManager;
	wochentyp: () => number;
	// kalenderwoche: () => StundenplanKalenderwochenzuordnung | undefined;
	useDragAndDrop?: boolean;
	kurseGefiltert: (day: Wochentag, stunde: number) => List<number>;
	sumSchreiber: (day: Wochentag, stunde: number) => number;
	dragData: () => GostKlausurplanungDragData;
	onDrag: (data: GostKlausurplanungDragData) => void;
	onDrop: (zone: GostKlausurplanungDropZone) => void;
	checkDropZoneZeitraster: (event: DragEvent, zeitraster: StundenplanZeitraster) => void;
	zeigeAlleJahrgaenge: () => boolean;
	kursklausurMouseOver: () => GostKursklausur | undefined;
}
