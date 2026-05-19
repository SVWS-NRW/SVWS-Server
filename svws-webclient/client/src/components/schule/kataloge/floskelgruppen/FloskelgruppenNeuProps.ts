import type { BenutzerKompetenz, Floskelgruppe } from "@core";
import type { RoutingStatus } from "~/router/RoutingStatus";
import type { FloskelgruppenListeManager, Checkpoint } from "@ui";

export interface FloskelgruppenNeuProps {
	manager: () => FloskelgruppenListeManager;
	add: (patchObject: Partial<Floskelgruppe>) => Promise<void>;
	goToDefaultView: (idEintrag?: number | null) => Promise<void>;
	checkpoint: Checkpoint;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	continueRoutingAfterCheckpoint: () => Promise<RoutingStatus>;
}
