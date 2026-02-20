import type { BenutzerKompetenz, Schwerpunkt } from "@core";
import type { SchwerpunkteListeManager, Checkpoint } from "@ui";
import type { RoutingStatus } from "~/router/RoutingStatus";

export interface SchwerpunkteNeuProps {
	manager: () => SchwerpunkteListeManager;
	add: (patchObject: Partial<Schwerpunkt>) => Promise<void>;
	gotoDefaultView: (eintragId?: number | null) => Promise<void>;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	checkpoint: Checkpoint;
	continueRoutingAfterCheckpoint: () => Promise<RoutingStatus>;
}
