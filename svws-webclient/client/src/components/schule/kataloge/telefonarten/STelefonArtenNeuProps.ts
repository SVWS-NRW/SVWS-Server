import type { BenutzerKompetenz, TelefonArt } from "@core";
import type { Checkpoint, TelefonArtListeManager } from "@ui";
import type { RoutingStatus } from "~/router/RoutingStatus";

export interface STelefonArtenNeuProps {
	manager: () => TelefonArtListeManager;
	add: (patchObject: Partial<TelefonArt>) => Promise<void>;
	gotoDefaultView: (eintragId?: number | null) => Promise<void>;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	checkpoint: Checkpoint;
	continueRoutingAfterCheckpoint: () => Promise<RoutingStatus>;
}
