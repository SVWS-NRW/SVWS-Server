import type { SchuelerNeu } from "@core/asd/data/schueler/SchuelerNeu";
import type { SchuelerStammdaten } from "@core/asd/data/schueler/SchuelerStammdaten";
import type { SchuelerNeuManager } from "@ui/ui/manager/schueler/SchuelerNeuManager";
import type { Checkpoint } from "@ui/ui/modal/Checkpoint";
import type { RoutingStatus } from "~/router/RoutingStatus";

export interface SchuelerNeuProps {
	manager: () => SchuelerNeuManager;
	add: (addObject: Partial<SchuelerNeu>) => Promise<SchuelerStammdaten>;
	gotoDefaultView: (idEintrag?: number | null) => Promise<void>;
	gotToSchnelleingabe: (idSchueler: number) => Promise<void>;
	checkpoint: Checkpoint;
	continueRoutingAfterCheckpoint: () => Promise<RoutingStatus>;
}
