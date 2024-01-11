import type { StundenplanKalenderwochenzuordnung, StundenplanManager, StundenplanPausenaufsicht, StundenplanKurs, StundenplanKlassenunterricht, StundenplanUnterricht, StundenplanZeitraster, StundenplanPausenzeit, List, StundenplanSchiene } from "@core";

export type StundenplanAnsichtDragData = StundenplanKlassenunterricht | StundenplanKurs | StundenplanUnterricht | List<StundenplanUnterricht> | StundenplanPausenaufsicht | undefined | StundenplanSchiene;

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
	onDrag?: (data: StundenplanAnsichtDragData, event?: DragEvent) => void;
	onDrop?: (zone: StundenplanAnsichtDropZone) => void;
}
