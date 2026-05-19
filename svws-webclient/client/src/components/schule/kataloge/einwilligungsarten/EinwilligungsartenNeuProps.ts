import type { BenutzerKompetenz, Einwilligungsart } from "@core";
import type { Checkpoint, EinwilligungsartenListeManager } from "@ui";
import type { RoutingStatus } from "~/router/RoutingStatus";

export interface EinwilligungsartenNeuProps {
	manager: () => EinwilligungsartenListeManager;
	add: (einwilligungsart: Partial<Einwilligungsart>) => Promise<void>;
	gotoDefaultView: (eintragId?: number | null) => Promise<void>;
	checkpoint: Checkpoint;
	benutzerKompetenzen: Set<BenutzerKompetenz>,
	continueRoutingAfterCheckpoint: () => Promise<RoutingStatus>;
}
