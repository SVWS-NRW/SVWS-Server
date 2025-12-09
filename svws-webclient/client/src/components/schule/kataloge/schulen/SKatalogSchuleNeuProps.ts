import type { SchulEintrag, BenutzerKompetenz, Schulform } from "@core";
import type { Checkpoint, KatalogSchuleListeManager } from "@ui";
import type { RoutingStatus } from "~/router/RoutingStatus";

export interface KatalogSchuleNeuProps {
	manager: () => KatalogSchuleListeManager;
	add: (patchObject: Partial<SchulEintrag>) => Promise<void>;
	gotoDefaultView: (eintragId?: number | null) => Promise<void>;
	checkpoint: Checkpoint;
	continueRoutingAfterCheckpoint: () => Promise<RoutingStatus>;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	schulform: Schulform;
}
