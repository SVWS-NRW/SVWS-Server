import type { SchuelerSchulbesuchSchule, List, SchuelerSchulbesuchMerkmal, SchuelerSchulbesuchsdaten } from "@core";
import type { SchuelerSchulbesuchManager } from "@ui";

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
