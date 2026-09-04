import type { SchuelerSchulbesuchMerkmal } from "@core/asd/data/schueler/SchuelerSchulbesuchMerkmal";
import type { SchuelerSchulbesuchSchule } from "@core/asd/data/schueler/SchuelerSchulbesuchSchule";
import type { SchuelerSchulbesuchsdaten } from "@core/asd/data/schueler/SchuelerSchulbesuchsdaten";
import type { List } from "@core/java/util/List";
import type { SchuelerSchulbesuchManager } from "@ui/ui/manager/schueler/SchuelerSchulbesuchManager";

export interface SchuelerSchulbesuchProps {
	manager: () => SchuelerSchulbesuchManager;
	goToSchule: (idSchule: number) => Promise<void>;
	autofocus: boolean;
	patch: (idSchulbesuch: number, data: Partial<SchuelerSchulbesuchsdaten>) => Promise<boolean>;
	addBisherigeSchule: (data: Partial<SchuelerSchulbesuchSchule>) => Promise<void>;
	patchBisherigeSchule: (data: Partial<SchuelerSchulbesuchSchule>) => Promise<void>;
	deleteBisherigeSchulen: (ids: List<number>) => Promise<void>;
	addMerkmal: (data: Partial<SchuelerSchulbesuchMerkmal>) => Promise<void>;
	patchMerkmal: (data: Partial<SchuelerSchulbesuchMerkmal>) => Promise<void>;
	deleteMerkmale: (ids: List<number>) => Promise<void>;
}
