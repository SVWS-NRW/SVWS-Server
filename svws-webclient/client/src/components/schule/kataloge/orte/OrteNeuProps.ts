import type { BenutzerKompetenz, OrtKatalogEintrag } from "@core";
import type { Checkpoint, OrteListeManager } from "@ui";
import type { RoutingStatus } from "~/router/RoutingStatus";

export interface OrteNeuProps {
	manager: () => OrteListeManager;
	add: (patchObject: Partial<OrtKatalogEintrag>) => Promise<void>;
	goToDefaultView: (idEintrag?: number | null) => Promise<void>;
	checkpoint: Checkpoint;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	continueRoutingAfterCheckpoint: () => Promise<RoutingStatus>;
}
