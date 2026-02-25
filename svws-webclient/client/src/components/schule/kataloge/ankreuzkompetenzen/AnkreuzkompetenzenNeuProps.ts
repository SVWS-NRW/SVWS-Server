import type { Ankreuzkompetenz, BenutzerKompetenz } from "@core";
import type { Checkpoint, AnkreuzkompetenzenListeManager } from "@ui";
import type { RoutingStatus } from "~/router/RoutingStatus";

export interface AnkreuzkompetenzenNeuProps {
	manager: () => AnkreuzkompetenzenListeManager;
	add: (patchObject: Partial<Ankreuzkompetenz>) => Promise<void>;
	gotoDefaultView: (eintragId?: number | null) => Promise<void>;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	checkpoint: Checkpoint;
	continueRoutingAfterCheckpoint: () => Promise<RoutingStatus>;
}
