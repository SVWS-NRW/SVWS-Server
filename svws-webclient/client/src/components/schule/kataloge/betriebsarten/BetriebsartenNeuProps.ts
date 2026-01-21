import type { BenutzerKompetenz, Betriebsart } from "@core";
import type { BetriebsartenListeManager, Checkpoint } from "@ui";
import type { RoutingStatus } from "~/router/RoutingStatus";

export interface BetriebsartenNeuProps {
	manager: () => BetriebsartenListeManager;
	add: (patchObject: Partial<Betriebsart>) => Promise<void>;
	gotoDefaultView: (eintragId?: number | null) => Promise<void>;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	checkpoint: Checkpoint;
	continueRoutingAfterCheckpoint: () => Promise<RoutingStatus>;
}
