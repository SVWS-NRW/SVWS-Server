import type { BenutzerKompetenz, JahrgangsDaten, Schulform } from "@core";
import type { Checkpoint, JahrgaengeListeManager } from "@ui";
import type { RoutingStatus } from "~/router/RoutingStatus";

export interface JahrgaengeNeuProps {
	manager: () => JahrgaengeListeManager;
	schuljahr: number;
	schulform: Schulform;
	add: (patchObject: Partial<JahrgangsDaten>) => Promise<void>;
	goToDefaultView: (idEintrag?: number | null) => Promise<void>;
	checkpoint: Checkpoint;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	continueRoutingAfterCheckpoint: () => Promise<RoutingStatus>;
}
