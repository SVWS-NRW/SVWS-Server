import type { Checkpoint } from "@ui";
import type { RoutingStatus } from "~/router/RoutingStatus";
import type { BenutzerKompetenz, SchuelerNeu, SchuelerStammdaten, Schulform } from "@core";
import type { SchuelerNeuManager } from "../../../../../ui/src/ui/manager/schueler/SchuelerNeuManager";

export interface SchuelerNeuProps {
	manager: () => SchuelerNeuManager;
	add: (addObject: Partial<SchuelerNeu>) => Promise<SchuelerStammdaten>;
	gotoDefaultView: (idEintrag?: number | null) => Promise<void>;
	schulform: Schulform,
	benutzerKompetenzen: Set<BenutzerKompetenz>,
	checkpoint: Checkpoint;
	continueRoutingAfterCheckpoint: () => Promise<RoutingStatus>;
}
