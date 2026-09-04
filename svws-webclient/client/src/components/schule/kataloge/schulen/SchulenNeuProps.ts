import type { SchulEintrag } from "@core/core/data/kataloge/SchulEintrag";
import type { SchulenListeManager } from "@ui/ui/manager/kataloge/SchulenListeManager";
import type { Checkpoint } from "@ui/ui/modal/Checkpoint";
import type { RoutingStatus } from "~/router/RoutingStatus";

export interface SchulenNeuProps {
	manager: () => SchulenListeManager;
	add: (patchObject: Partial<SchulEintrag>) => Promise<void>;
	gotoDefaultView: (eintragId?: number | null) => Promise<void>;
	checkpoint: Checkpoint;
	continueRoutingAfterCheckpoint: () => Promise<RoutingStatus>;
}
