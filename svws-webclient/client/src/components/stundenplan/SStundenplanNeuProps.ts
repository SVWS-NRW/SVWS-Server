import type { ArrayList, Stundenplan, StundenplanListeEintrag } from "@core";
import type { Checkpoint, StundenplanListeManager } from "@ui";
import type { RoutingStatus } from "~/router/RoutingStatus";

export interface StundenplanNeuProps {
	manager: () => StundenplanListeManager;
	addAsCopy: (patchObject: Partial<Stundenplan>, idFromStundenplan: number | undefined) => Promise<void>;
	gotoDefaultView: (eintragId?: number | null) => Promise<void>;
	checkpoint: Checkpoint;
	continueRoutingAfterCheckpoint: () => Promise<RoutingStatus>;
	getStundenplanListeEintragVorgaengerabschnitt: () => Promise<ArrayList<StundenplanListeEintrag>>;
}
