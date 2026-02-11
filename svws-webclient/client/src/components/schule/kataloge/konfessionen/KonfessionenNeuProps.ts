import type { BenutzerKompetenz, ReligionEintrag, Schulform } from "@core";
import type { KonfessionenListeManager, Checkpoint } from "@ui";
import type { RoutingStatus } from "~/router/RoutingStatus";

export interface KonfessionenNeuProps {
	manager: () => KonfessionenListeManager;
	schulform: Schulform;
	add: (patchObject: Partial<ReligionEintrag>) => Promise<void>;
	gotoDefaultView: (id?: number | null) => Promise<void>;
	checkpoint: Checkpoint;
	continueRoutingAfterCheckpoint: () => Promise<RoutingStatus>;
	benutzerKompetenzen: Set<BenutzerKompetenz>,
}
