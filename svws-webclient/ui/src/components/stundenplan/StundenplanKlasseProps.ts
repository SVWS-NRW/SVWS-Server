import type { StundenplanKalenderwochenzuordnung } from "../../../../core/src/core/data/stundenplan/StundenplanKalenderwochenzuordnung";
import type { StundenplanManager } from "../../../../core/src/core/utils/stundenplan/StundenplanManager";
import type { StundenplanAnsichtDragData, StundenplanAnsichtDropZone } from "./StundenplanAnsichtProps";

export interface StundenplanKlasseProps {
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
	onDrop?: (zone: StundenplanAnsichtDropZone, wochentyp?: number) => void;
}
