import type { BenutzerKompetenz, Haltestelle } from "@core";
import type { RoutingStatus } from "~/router/RoutingStatus";
import type { Checkpoint, HaltestellenListeManager } from "@ui";

export interface HaltestellenNeuProps {
	manager: () => HaltestellenListeManager;
	addHaltestelle: (patchObject: Partial<Haltestelle>) => Promise<void>;
	goToDefaultView: (idEintrag?: number | null) => Promise<void>;
	checkpoint: Checkpoint;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	continueRoutingAfterCheckpoint: () => Promise<RoutingStatus>;
}
