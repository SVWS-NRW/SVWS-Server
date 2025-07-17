import type { OrtKatalogEintrag, OrtsteilKatalogEintrag, KatalogEintrag, FoerderschwerpunktEintrag, ReligionEintrag, SchuelerListeManager, SchulEintrag,
	Schulform, ServerMode, BenutzerKompetenz, Haltestelle } from "@core";
import type { Checkpoint } from "@ui";
import type { RoutingStatus } from "~/router/RoutingStatus";
import type { PendingStateManagerSchuelerIndividualdaten } from "~/router/apps/schueler/individualdaten/PendingStateManagerSchuelerIndividualdaten";

export interface SchuelerIndividualdatenGruppenprozesseProps {
	schuelerListeManager: () => SchuelerListeManager;
	mapSchulen: Map<string, SchulEintrag>;
	mapOrte: Map<number, OrtKatalogEintrag>;
	mapOrtsteile: Map<number, OrtsteilKatalogEintrag>;
	mapFahrschuelerarten: Map<number, KatalogEintrag>;
	mapFoerderschwerpunkte: Map<number, FoerderschwerpunktEintrag>;
	mapHaltestellen: Map<number, Haltestelle>
	mapReligionen: Map<number, ReligionEintrag>;
	schulform: Schulform;
	serverMode: ServerMode;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	autofocus: boolean;
	patchMultiple: () => Promise<void>;
	pendingStateManager: () => PendingStateManagerSchuelerIndividualdaten
	checkpoint: Checkpoint;
	continueRoutingAfterCheckpoint: () => Promise<RoutingStatus>;
}
