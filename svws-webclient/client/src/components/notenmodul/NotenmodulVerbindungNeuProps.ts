import type { ENMServerConnection } from "@core";
import type { Checkpoint, WenomAuswahlListeManager } from "@ui";
import type { RoutingStatus } from "~/router/RoutingStatus";

export interface NotenmodulVerbindungNeuProps {
	manager: () => WenomAuswahlListeManager;
	addCredentials: (data: Partial<ENMServerConnection>) => Promise<void>;
	gotoDefaultView: (idEintrag?: number | null) => Promise<void>;
	checkpoint: Checkpoint;
	continueRoutingAfterCheckpoint: () => Promise<RoutingStatus>;
}
