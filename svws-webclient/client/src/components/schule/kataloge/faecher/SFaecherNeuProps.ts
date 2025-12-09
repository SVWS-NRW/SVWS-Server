import type { BenutzerKompetenz, FachDaten } from "@core";
import type { Checkpoint, FachListeManager } from "@ui";
import type { RoutingStatus } from "~/router/RoutingStatus";

export interface FaecherNeuProps {
	manager: () => FachListeManager;
	add: (patchObject: Partial<FachDaten>) => Promise<void>;
	gotoDefaultView: (idEintrag?: number | null) => Promise<void>;
	checkpoint: Checkpoint;
	continueRoutingAfterCheckpoint: () => Promise<RoutingStatus>;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
}
