import type { OrtKatalogEintrag, OrtsteilKatalogEintrag, FoerderschwerpunktEintrag, ReligionEintrag, SchulEintrag, Schulform,
	ServerMode, BenutzerKompetenz, Haltestelle, Fahrschuelerart } from "@core";
import type { Checkpoint, SchuelerListeManager } from "@ui";
import type { RoutingStatus } from "~/router/RoutingStatus";
import type { PendingStateManagerSchuelerIndividualdaten } from "~/router/apps/schueler/individualdaten/PendingStateManagerSchuelerIndividualdaten";

export interface SchuelerIndividualdatenGruppenprozesseProps {
	schuelerListeManager: () => SchuelerListeManager;
	mapSchulen: Map<string, SchulEintrag>;
	orteById: Map<number, OrtKatalogEintrag>;
	ortsteileById: Map<number, OrtsteilKatalogEintrag>;
	fahrschuelerartenById: Map<number, Fahrschuelerart>;
	foerderschwerpunkteById: Map<number, FoerderschwerpunktEintrag>;
	haltestellenById: Map<number, Haltestelle>
	religionenById: Map<number, ReligionEintrag>;
	schulform: Schulform;
	serverMode: ServerMode;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	autofocus: boolean;
	patchMultiple: () => Promise<void>;
	pendingStateManager: () => PendingStateManagerSchuelerIndividualdaten
	checkpoint: Checkpoint;
	continueRoutingAfterCheckpoint: () => Promise<RoutingStatus>;
}
