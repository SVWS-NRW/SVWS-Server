import type { Checkpoint, OrtsteileListeManager } from "@ui";
import type { BenutzerKompetenz, OrtsteilKatalogEintrag } from "@core";
import type { RoutingStatus } from "~/router/RoutingStatus";

export interface OrtsteileNeuProps {
	manager: () => OrtsteileListeManager;
	add: (ortsteil: Partial<OrtsteilKatalogEintrag>) => Promise<void>;
	goToDefaultView: (idortsteil?: number | null) => Promise<void>;
	checkpoint: Checkpoint;
	benutzerKompetenzen: Set<BenutzerKompetenz>,
	continueRoutingAfterCheckpoint: () => Promise<RoutingStatus>;
}
