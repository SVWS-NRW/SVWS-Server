import type { BenutzerKompetenz, List, ServerMode } from "@core";
import type { BetriebeListeManager } from "../../../../../../../ui/src/ui/managers/kataloge/betriebe/BetriebeListeManager";

export interface BetriebeGruppenprozesseProps {
	serverMode: ServerMode;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	manager: () => BetriebeListeManager;
	deleteCheck: () => [boolean, List<string>];
	delete: () => Promise<[boolean, List<string | null>]>;
	gotoDefaultView: (idBetrieb?: number | null) => Promise<void>;
}
