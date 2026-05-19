import type { BenutzerKompetenz, Floskel } from "@core";
import type { RoutingStatus } from "~/router/RoutingStatus";
import type { Checkpoint, FloskelnListeManager } from "@ui";

export interface FloskelnNeuProps {
	manager: () => FloskelnListeManager;
	add: (patchObject: Partial<Floskel>) => Promise<void>;
	goToDefaultView: (idEintrag?: number | null) => Promise<void>;
	checkpoint: Checkpoint;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	continueRoutingAfterCheckpoint: () => Promise<RoutingStatus>;
}
