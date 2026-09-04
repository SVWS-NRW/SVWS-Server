import type { StundenplanKalenderwochenzuordnung } from "@core/core/data/stundenplan/StundenplanKalenderwochenzuordnung";
import type { StundenplanKlassenunterricht } from "@core/core/data/stundenplan/StundenplanKlassenunterricht";
import type { StundenplanKurs } from "@core/core/data/stundenplan/StundenplanKurs";
import type { StundenplanPausenaufsicht } from "@core/core/data/stundenplan/StundenplanPausenaufsicht";
import type { StundenplanPausenzeit } from "@core/core/data/stundenplan/StundenplanPausenzeit";
import type { StundenplanSchiene } from "@core/core/data/stundenplan/StundenplanSchiene";
import type { StundenplanUnterricht } from "@core/core/data/stundenplan/StundenplanUnterricht";
import type { StundenplanZeitraster } from "@core/core/data/stundenplan/StundenplanZeitraster";
import type { StundenplanManager } from "@core/core/utils/stundenplan/StundenplanManager";
import type { List } from "@core/java/util/List";

export type StundenplanAnsichtDragData = StundenplanKlassenunterricht | StundenplanKurs | StundenplanUnterricht | List<StundenplanUnterricht> | StundenplanPausenaufsicht | undefined | StundenplanSchiene;

export type StundenplanAnsichtDropZone = StundenplanZeitraster | StundenplanPausenzeit | undefined;

export interface StundenplanAnsichtProps {
	showSchienen?: boolean;
	hidePausenaufsicht?: boolean;
	textPausenzeit?: string;
	growUnterricht?: boolean;
	modePausenaufsichten?: 'normal' | 'kurz' | 'tooltip' | 'aus';
	hideZeitachse?: boolean;
	hideZeitachsePausenzeiten?: boolean;
	zeitrasterSteps?: 1 | 5 | 10 | 15;
	ignoreEmpty?: boolean;
	manager: () => StundenplanManager;
	wochentyp: () => number;
	kalenderwoche: () => StundenplanKalenderwochenzuordnung | undefined;
	useDragAndDrop?: boolean;
	dragData?: () => StundenplanAnsichtDragData;
	onDrag?: (data: StundenplanAnsichtDragData, event?: DragEvent) => void;
	onDrop?: (zone: StundenplanAnsichtDropZone, wochentyp?: number) => void;
	getSchienen: (wochentag: number, stunde: number, wochentyp: number) => List<StundenplanSchiene>;
	getUnterricht: (wochentag: number, stunde: number, wochentyp: number, schiene: number | null) => List<StundenplanUnterricht>;
	zeitrasterHatUnterrichtMitWochentyp: (wochentag: number, stunde: number) => boolean;
	getPausenzeiten: () => List<StundenplanPausenzeit>;
	schneidenPausenzeitenZeitraster: (wochentag: number) => boolean;
	getPausenzeitenWochentag: (wochentag: number) => List<StundenplanPausenzeit>;
	getPausenaufsichtenPausenzeit: (idPausenzeit: number) => List<StundenplanPausenaufsicht>;
}
