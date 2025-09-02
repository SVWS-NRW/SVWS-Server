import type { BenutzerKompetenz, LehrerStammdaten, OrtKatalogEintrag, OrtsteilKatalogEintrag } from "@core";
import type { Checkpoint, LehrerListeManager } from "@ui";
import type { RoutingStatus } from "~/router/RoutingStatus";

export interface LehrerNeuProps {
	lehrerListeManager: () => LehrerListeManager;
	add: (patchObject: Partial<LehrerStammdaten>) => Promise<void>;
	gotoDefaultView: (eintragId?: number | null) => Promise<void>;
	checkpoint: Checkpoint;
	continueRoutingAfterCheckpoint: () => Promise<RoutingStatus>;
	mapOrte: Map<number, OrtKatalogEintrag>;
	mapOrtsteile: Map<number, OrtsteilKatalogEintrag>;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
}
