import type { SimpleOperationResponse } from "@core/core/data/SimpleOperationResponse";
import type { Stundenplan } from "@core/core/data/stundenplan/Stundenplan";
import type { StundenplanListeEintrag } from "@core/core/data/stundenplan/StundenplanListeEintrag";
import type { ArrayList } from "@core/java/util/ArrayList";
import type { StundenplanListeManager } from "@ui/ui/manager/stundenplan/StundenplanListeManager";
import type { Checkpoint } from "@ui/ui/modal/Checkpoint";
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
