import type { SchulEintrag } from "@core/core/data/kataloge/SchulEintrag";
import type { FoerderschwerpunktEintrag } from "@core/core/data/schule/FoerderschwerpunktEintrag";
import type { Haltestelle } from "@core/core/data/schule/Haltestelle";
import type { Checkpoint } from "@ui/ui/modal/Checkpoint";

import type { PendingStateManagerSchuelerIndividualdaten } from "~/router/apps/schueler/individualdaten/PendingStateManagerSchuelerIndividualdaten";
import type { RoutingStatus } from "~/router/RoutingStatus";

export interface SchuelerIndividualdatenGruppenprozesseProps {
	mapSchulen: Map<string, SchulEintrag>;
	foerderschwerpunkteById: Map<number, FoerderschwerpunktEintrag>;
	haltestellenById: Map<number, Haltestelle>
	pendingStateManager: () => PendingStateManagerSchuelerIndividualdaten
	checkpoint: Checkpoint;
	continueRoutingAfterCheckpoint: () => Promise<RoutingStatus>;
}
