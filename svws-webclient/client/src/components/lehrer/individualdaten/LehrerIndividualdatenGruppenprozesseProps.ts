import type { OrtKatalogEintrag, OrtsteilKatalogEintrag, Schulform, ServerMode, BenutzerKompetenz, ValidatorKontext } from "@core";
import type { Checkpoint, LehrerListeManager } from "@ui";
import type { RoutingStatus } from "~/router/RoutingStatus";
import type { PendingStateManagerLehrerIndividualdaten } from "~/router/apps/lehrer/individualdaten/PendingStateManagerLehrerIndividualdaten";

export interface LehrerIndividualdatenGruppenprozesseProps {
	lehrerListeManager: () => LehrerListeManager;
	validatorKontext: () => ValidatorKontext;
	schulform: Schulform;
	serverMode: ServerMode;
	benutzerKompetenzen: Set<BenutzerKompetenz>,
	orteById: Map<number, OrtKatalogEintrag>;
	ortsteileById: Map<number, OrtsteilKatalogEintrag>;
	autofocus: boolean;
	patchMultiple: () => Promise<void>;
	pendingStateManager: () => PendingStateManagerLehrerIndividualdaten
	checkpoint: Checkpoint;
	continueRoutingAfterCheckpoint: () => Promise<RoutingStatus>;
}
