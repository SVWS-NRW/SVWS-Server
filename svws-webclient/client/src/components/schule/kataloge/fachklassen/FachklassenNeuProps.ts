import type { Checkpoint, FachklassenListeManager } from "@ui";
import type { BenutzerKompetenz, FachklasseEintrag } from "@core";
import type { RoutingStatus } from "~/router/RoutingStatus";

export interface FachklassenNeuProps {
	manager: () => FachklassenListeManager;
	add: (patchObject: Partial<FachklasseEintrag>) => Promise<void>;
	gotoDefaultView: (idEintrag?: number | null) => Promise<void>;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	checkpoint: Checkpoint;
	continueRoutingAfterCheckpoint: () => Promise<RoutingStatus>;
}
