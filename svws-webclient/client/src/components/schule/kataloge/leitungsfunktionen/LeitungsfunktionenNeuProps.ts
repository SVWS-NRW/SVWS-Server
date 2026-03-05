import type { BenutzerKompetenz, Leitungsfunktion } from "@core";
import type { RoutingStatus } from "~/router/RoutingStatus";
import type { Checkpoint, LeitungsfunktionenListeManager } from "@ui";

export interface LeitungsfunktionenNeuProps {
	manager: () => LeitungsfunktionenListeManager;
	add: (patchObject: Partial<Leitungsfunktion>) => Promise<void>;
	goToDefaultView: (idEintrag?: number | null) => Promise<void>;
	checkpoint: Checkpoint;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	continueRoutingAfterCheckpoint: () => Promise<RoutingStatus>;
}