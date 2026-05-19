import type { KursDaten, BenutzerKompetenz } from "@core";
import type { Checkpoint, KursListeManager } from "@ui";
import type { RoutingStatus } from "~/router/RoutingStatus";

export interface KurseNeuProps {
	manager: () => KursListeManager;
	add: (patchObject: Partial<KursDaten>) => Promise<void>;
	gotoDefaultView: (eintragId?: number | null) => Promise<void>;
	checkpoint: Checkpoint;
	continueRoutingAfterCheckpoint: () => Promise<RoutingStatus>;
	goToDefaultView: (idEintrag?: number | null) => Promise<void>;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
}
