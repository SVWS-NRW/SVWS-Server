import type { List, SchuelerBetrieb, Schulform } from "@core";
import type { SchuelerBetriebeManager } from "@ui";

export interface SchuelerBetriebeProps {
	goToBetrieb: (idBetrieb: number) => Promise<void>;
	manager: () => SchuelerBetriebeManager;
	patch: (idSchuelerBetrieb: number, data: Partial<SchuelerBetrieb>) => Promise<boolean>;
	add: (data: Partial<SchuelerBetrieb>) => Promise<SchuelerBetrieb>;
	deleteEntries: (idsSchuelerBetriebe: List<number>) => Promise<boolean>;
	schulform: Schulform;
}
