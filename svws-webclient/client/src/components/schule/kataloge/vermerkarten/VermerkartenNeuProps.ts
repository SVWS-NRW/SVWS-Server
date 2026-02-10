import type { BenutzerKompetenz, VermerkartEintrag } from "@core";
import type { Checkpoint, VermerkartenListeManager } from "@ui";
import type { RoutingStatus } from "~/router/RoutingStatus";

export interface VermerkartenNeuProps {
	manager: () => VermerkartenListeManager;
	add: (patchObject: Partial<VermerkartEintrag>) => Promise<void>;
	goToDefaultView: (eintragId?: number | null) => Promise<void>;
	checkpoint: Checkpoint;
	continueRoutingAfterCheckpoint: () => Promise<RoutingStatus>;
	benutzerKompetenzen: Set<BenutzerKompetenz>,
}
