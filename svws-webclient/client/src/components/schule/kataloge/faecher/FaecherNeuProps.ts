import type { BenutzerKompetenz, FachDaten, Schulform } from "@core";
import type { Checkpoint, FaecherListeManager } from "@ui";
import type { RoutingStatus } from "~/router/RoutingStatus";

export interface FaecherNeuProps {
	manager: () => FaecherListeManager;
	add: (patchObject: Partial<FachDaten>) => Promise<void>;
	gotoDefaultView: (idEintrag?: number | null) => Promise<void>;
	checkpoint: Checkpoint;
	continueRoutingAfterCheckpoint: () => Promise<RoutingStatus>;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	schuljahr: number,
	schulform: Schulform;
}
