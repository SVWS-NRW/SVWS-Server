import type { OrtKatalogEintrag, OrtsteilKatalogEintrag, BenutzerKompetenz } from "@core";
import type { Checkpoint, LehrerListeManager } from "@ui";
import type { RoutingStatus } from "~/router/RoutingStatus";
import type { PendingStateManagerLehrerIndividualdaten } from "~/router/apps/lehrer/individualdaten/PendingStateManagerLehrerIndividualdaten";

export interface LehrerIndividualdatenGruppenprozesseProps {
	lehrerListeManager: () => LehrerListeManager;
	benutzerKompetenzen: Set<BenutzerKompetenz>,
	orteById: Map<number, OrtKatalogEintrag>;
	ortsteileById: Map<number, OrtsteilKatalogEintrag>;
	autofocus: boolean;
	patchMultiple: () => Promise<void>;
	pendingStateManager: () => PendingStateManagerLehrerIndividualdaten
	checkpoint: Checkpoint;
	continueRoutingAfterCheckpoint: () => Promise<RoutingStatus>;
}
