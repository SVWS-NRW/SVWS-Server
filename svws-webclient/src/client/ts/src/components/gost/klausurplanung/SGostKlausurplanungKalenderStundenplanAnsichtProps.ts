import type { GostFaecherManager, GostKlausurtermin, GostKursklausurManager, KursManager, LehrerListeEintrag, List, StundenplanKalenderwochenzuordnung, Wochentag } from "@core";
import { type StundenplanManager, type StundenplanPausenaufsicht,
	type StundenplanKurs, type StundenplanKlassenunterricht, type StundenplanUnterricht, type StundenplanZeitraster, type StundenplanPausenzeit } from "@core";

export type KlausurplanungKalenderDragData = GostKlausurtermin | undefined;

export type KlausurplanungKalenderDropZone = StundenplanZeitraster | StundenplanPausenzeit | undefined;

export interface SGostKlausurplanungKalenderStundenplanAnsichtProps {
	mode?: 'schueler' | 'lehrer' | 'klasse';
	ignoreEmpty?: boolean;
	id: number;
	kwAuswahl: StundenplanKalenderwochenzuordnung;
	manager: () => StundenplanManager;
	kursmanager: KursManager;
	kursklausurmanager: () => GostKursklausurManager;
	wochentyp: () => number;
	// kalenderwoche: () => StundenplanKalenderwochenzuordnung | undefined;
	useDragAndDrop?: boolean;
	kurseGefiltert: (day: Wochentag, stunde: number) => List<number>;
	sumSchreiber: (day: Wochentag, stunde: number) => number;
	dragData: () => KlausurplanungKalenderDragData;
	onDrag: (data: KlausurplanungKalenderDragData) => void;
	onDrop: (zone: KlausurplanungKalenderDropZone) => void;
	faecherManager: GostFaecherManager;
	mapLehrer: Map<number, LehrerListeEintrag>;

}
