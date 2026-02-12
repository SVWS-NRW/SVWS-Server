import type { ArrayList, Stundenplan, StundenplanListeEintrag, SimpleOperationResponse } from "@core";
import type { Checkpoint, StundenplanListeManager } from "@ui";
import type { RoutingStatus } from "~/router/RoutingStatus";

export interface StundenplanNeuProps {
	manager: () => StundenplanListeManager;
	add: (partial: Partial<Stundenplan>) => Promise<void>;
	addAsCopy: (patchObject: Partial<Stundenplan>, idFromStundenplan: number) => Promise<SimpleOperationResponse>;
	loadAfterAdd: (eintragId: number) => Promise<void>;
	gotoDefaultView: (eintragId?: number | null) => Promise<void>;
	checkpoint: Checkpoint;
	continueRoutingAfterCheckpoint: () => Promise<RoutingStatus>;
	getStundenplanListeEintragVorgaengerabschnitt: () => Promise<ArrayList<StundenplanListeEintrag>>;
}
