import type { SchulEintrag, BenutzerKompetenz } from "@core";
import type { Checkpoint, SchulenListeManager } from "@ui";
import type { RoutingStatus } from "~/router/RoutingStatus";

export interface SchulenNeuProps {
	manager: () => SchulenListeManager;
	add: (patchObject: Partial<SchulEintrag>) => Promise<void>;
	gotoDefaultView: (eintragId?: number | null) => Promise<void>;
	checkpoint: Checkpoint;
	continueRoutingAfterCheckpoint: () => Promise<RoutingStatus>;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
}
