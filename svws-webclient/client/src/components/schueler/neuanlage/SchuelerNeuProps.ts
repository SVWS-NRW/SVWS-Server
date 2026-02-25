import type { RoutingStatus } from "~/router/RoutingStatus";
import type { BenutzerKompetenz, SchuelerNeu, SchuelerStammdaten, Schulform } from "@core";
import type { SchuelerNeuManager, Checkpoint } from "@ui";

export interface SchuelerNeuProps {
	manager: () => SchuelerNeuManager;
	add: (addObject: Partial<SchuelerNeu>) => Promise<SchuelerStammdaten>;
	gotoDefaultView: (idEintrag?: number | null) => Promise<void>;
	gotToSchnelleingabe: (idSchueler: number) => Promise<void>;
	schulform: Schulform,
	benutzerKompetenzen: Set<BenutzerKompetenz>,
	checkpoint: Checkpoint;
	continueRoutingAfterCheckpoint: () => Promise<RoutingStatus>;
}
