import type { Checkpoint, KatalogSchuleListeManager } from "@ui";
import type { RoutingStatus } from "~/router/RoutingStatus";
import type { SchulEintrag, BenutzerKompetenz } from "@core";

export interface KatalogSchuleNeuProps {
	schuleListeManager: () => KatalogSchuleListeManager;
	add: (patchObject: Partial<SchulEintrag>) => Promise<void>;
	gotoDefaultView: (eintragId?: number | null) => Promise<void>;
	checkpoint: Checkpoint;
	continueRoutingAfterCheckpoint: () => Promise<RoutingStatus>;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
}
