import { type StundenplanKalenderwochenzuordnung, type StundenplanManager, type StundenplanPausenaufsicht,
	type StundenplanKurs, type StundenplanKlassenunterricht, type StundenplanUnterricht, type StundenplanZeitraster, type StundenplanPausenzeit } from "@core";

export type StundenplanAnsichtDragData = StundenplanKlassenunterricht | StundenplanKurs | StundenplanUnterricht | StundenplanPausenaufsicht | undefined;

export type StundenplanAnsichtDropZone = StundenplanZeitraster | StundenplanPausenzeit | undefined;

export interface StundenplanAnsichtProps {
	mode?: 'schueler' | 'lehrer' | 'klasse';
	ignoreEmpty?: boolean;
	id: number,
	manager: () => StundenplanManager;
	wochentyp: () => number;
	kalenderwoche: () => StundenplanKalenderwochenzuordnung | undefined;
	useDragAndDrop?: boolean;
	dragAndDropData?: () => StundenplanAnsichtDragData;
	onDrag?: (data: StundenplanAnsichtDragData) => void;
	onDrop?: (data: StundenplanAnsichtDropZone) => void;
}
