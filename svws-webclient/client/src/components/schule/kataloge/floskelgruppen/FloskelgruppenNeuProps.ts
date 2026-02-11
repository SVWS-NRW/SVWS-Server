import type { BenutzerKompetenz, Floskelgruppe, Schulform } from "@core";
import type { RoutingStatus } from "~/router/RoutingStatus";
import type { Checkpoint } from "@ui";
import type { FloskelgruppenListeManager } from "@ui";

export interface FloskelgruppenNeuProps {
	manager: () => FloskelgruppenListeManager;
	add: (patchObject: Partial<Floskelgruppe>) => Promise<void>;
	schuljahr: number,
	schulform: Schulform,
	goToDefaultView: (idEintrag?: number | null) => Promise<void>;
	checkpoint: Checkpoint;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	continueRoutingAfterCheckpoint: () => Promise<RoutingStatus>;
}
