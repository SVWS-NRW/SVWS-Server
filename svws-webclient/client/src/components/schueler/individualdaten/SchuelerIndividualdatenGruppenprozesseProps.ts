import type { SchulEintrag } from "@core/core/data/kataloge/SchulEintrag";
import type { Fahrschuelerart } from "@core/core/data/schule/Fahrschuelerart";
import type { FoerderschwerpunktEintrag } from "@core/core/data/schule/FoerderschwerpunktEintrag";
import type { Haltestelle } from "@core/core/data/schule/Haltestelle";
import type { ReligionEintrag } from "@core/core/data/schule/ReligionEintrag";
import type { Checkpoint } from "@ui/ui/modal/Checkpoint";
import type { RoutingStatus } from "~/router/RoutingStatus";
import type { PendingStateManagerSchuelerIndividualdaten } from "~/router/apps/schueler/individualdaten/PendingStateManagerSchuelerIndividualdaten";
import type { SchuelerListeManager } from "~/states/schueler/SchuelerListeManager";

export interface SchuelerIndividualdatenGruppenprozesseProps {
	schuelerListeManager: () => SchuelerListeManager;
	mapSchulen: Map<string, SchulEintrag>;
	fahrschuelerartenById: Map<number, Fahrschuelerart>;
	foerderschwerpunkteById: Map<number, FoerderschwerpunktEintrag>;
	haltestellenById: Map<number, Haltestelle>
	religionenById: Map<number, ReligionEintrag>;
	autofocus: boolean;
	patchMultiple: () => Promise<void>;
	pendingStateManager: () => PendingStateManagerSchuelerIndividualdaten
	checkpoint: Checkpoint;
	continueRoutingAfterCheckpoint: () => Promise<RoutingStatus>;
}
