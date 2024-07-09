import type { StundenplanKalenderwochenzuordnung } from "../../../../core/src/core/data/stundenplan/StundenplanKalenderwochenzuordnung";
import type { StundenplanKlassenunterricht } from "../../../../core/src/core/data/stundenplan/StundenplanKlassenunterricht";
import type { StundenplanKurs } from "../../../../core/src/core/data/stundenplan/StundenplanKurs";
import type { StundenplanPausenaufsicht } from "../../../../core/src/core/data/stundenplan/StundenplanPausenaufsicht";
import type { StundenplanPausenzeit } from "../../../../core/src/core/data/stundenplan/StundenplanPausenzeit";
import type { StundenplanSchiene } from "../../../../core/src/core/data/stundenplan/StundenplanSchiene";
import type { StundenplanUnterricht } from "../../../../core/src/core/data/stundenplan/StundenplanUnterricht";
import type { StundenplanZeitraster } from "../../../../core/src/core/data/stundenplan/StundenplanZeitraster";
import type { StundenplanManager } from "../../../../core/src/core/utils/stundenplan/StundenplanManager";
import type { List } from "../../../../core/src/java/util/List";

export type StundenplanAnsichtDragData = StundenplanKlassenunterricht | StundenplanKurs | StundenplanUnterricht | List<StundenplanUnterricht> | StundenplanPausenaufsicht | undefined | StundenplanSchiene;

export type StundenplanAnsichtDropZone = StundenplanZeitraster | StundenplanPausenzeit | undefined;

export interface StundenplanAnsichtLehrerProps {
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
