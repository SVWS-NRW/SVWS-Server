import { type StundenplanKalenderwochenzuordnung, type StundenplanManager, type StundenplanPausenaufsicht,
	type StundenplanKurs, type StundenplanKlassenunterricht, type StundenplanUnterricht, type StundenplanZeitraster, type StundenplanPausenzeit } from "@core";

export type StundenplanAnsichtDragData = StundenplanKlassenunterricht | StundenplanKurs | StundenplanUnterricht | StundenplanPausenaufsicht | undefined;

export type StundenplanAnsichtDropZone = StundenplanZeitraster | StundenplanPausenzeit | undefined;

export interface StundenplanAnsichtProps {
	mode?: 'schueler' | 'lehrer' | 'klasse' | 'planung';
	modePausenaufsichten?: 'normal' | 'kurz' | 'tooltip' | 'aus';
	showZeitachse?: boolean;
	zeitrasterSteps?: 1 | 5 | 10 | 15;
	ignoreEmpty?: boolean;
	id: number,
	manager: () => StundenplanManager;
	wochentyp: () => number;
	kalenderwoche: () => StundenplanKalenderwochenzuordnung | undefined;
	useDragAndDrop?: boolean;
	dragData?: () => StundenplanAnsichtDragData;
	onDrag?: (data: StundenplanAnsichtDragData) => void;
	onDrop?: (zone: StundenplanAnsichtDropZone) => void;
}
