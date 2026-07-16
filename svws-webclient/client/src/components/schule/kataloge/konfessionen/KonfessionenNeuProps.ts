import type { ReligionEintrag } from "@core";
import type { Checkpoint, KonfessionenListeManager } from "@ui";
import type { RoutingStatus } from "~/router/RoutingStatus";

export interface KonfessionenNeuProps {
	manager: () => KonfessionenListeManager;
	add: (patchObject: Partial<ReligionEintrag>) => Promise<void>;
	gotoDefaultView: (id?: number | null) => Promise<void>;
	checkpoint: Checkpoint;
	continueRoutingAfterCheckpoint: () => Promise<RoutingStatus>;
}
