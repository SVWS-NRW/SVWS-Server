import type { ENMServerConnection } from "@core/core/data/enm/ENMServerConnection";
import type { WenomAuswahlListeManager } from "@ui/components/enm/WenomAuswahlListeManager";
import type { Checkpoint } from "@ui/ui/modal/Checkpoint";
import type { RoutingStatus } from "~/router/RoutingStatus";

export interface NotenmodulVerbindungNeuProps {
	manager: () => WenomAuswahlListeManager;
	addCredentials: (data: Partial<ENMServerConnection>) => Promise<void>;
	gotoDefaultView: (idEintrag?: number | null) => Promise<void>;
	checkpoint: Checkpoint;
	continueRoutingAfterCheckpoint: () => Promise<RoutingStatus>;
}
