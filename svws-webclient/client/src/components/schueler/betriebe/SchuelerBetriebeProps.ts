import type { SchuelerBetrieb } from "@core/asd/data/schueler/SchuelerBetrieb";
import type { List } from "@core/java/util/List";
import type { SchuelerBetriebeManager } from "@ui/ui/manager/schueler/SchuelerBetriebeManager";

export interface SchuelerBetriebeProps {
	goToBetrieb: (idBetrieb: number) => Promise<void>;
	manager: () => SchuelerBetriebeManager;
	patch: (idSchuelerBetrieb: number, data: Partial<SchuelerBetrieb>) => Promise<boolean>;
	add: (data: Partial<SchuelerBetrieb>) => Promise<SchuelerBetrieb>;
	deleteBetriebe: (idsSchuelerBetriebe: List<number>) => Promise<boolean>;
}
