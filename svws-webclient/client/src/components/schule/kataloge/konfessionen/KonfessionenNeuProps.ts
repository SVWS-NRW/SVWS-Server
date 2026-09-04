import type { ReligionEintrag } from "@core/core/data/schule/ReligionEintrag";
import type { KonfessionenListeManager } from "@ui/ui/manager/kataloge/KonfessionenListeManager";
import type { Checkpoint } from "@ui/ui/modal/Checkpoint";
import type { RoutingStatus } from "~/router/RoutingStatus";

export interface KonfessionenNeuProps {
	manager: () => KonfessionenListeManager;
	add: (patchObject: Partial<ReligionEintrag>) => Promise<void>;
	gotoDefaultView: (id?: number | null) => Promise<void>;
	checkpoint: Checkpoint;
	continueRoutingAfterCheckpoint: () => Promise<RoutingStatus>;
}
