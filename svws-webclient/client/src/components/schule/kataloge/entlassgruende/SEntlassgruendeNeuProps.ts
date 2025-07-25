import type { BenutzerKompetenz, EntlassgruendeListeManager, KatalogEntlassgrund } from "@core";
import type { RoutingStatus } from "~/router/RoutingStatus";
import type { Checkpoint } from "@ui";

export interface EntlassgruendeNeuProps {
	manager: () => EntlassgruendeListeManager;
	addEntlassgrund: (patchObject: Partial<KatalogEntlassgrund>) => Promise<void>;
	goToDefaultView: (idEintrag?: number | null) => Promise<void>;
	checkpoint: Checkpoint;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	continueRoutingAfterCheckpoint: () => Promise<RoutingStatus>;
}
