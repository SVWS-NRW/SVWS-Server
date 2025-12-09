import type { BenutzerKompetenz, Betrieb } from "@core";
import type { BetriebeListeManager } from "../../../../../../../ui/src/ui/managers/kataloge/betriebe/BetriebeListeManager";

export interface BetriebeDatenProps {
	manager: () => BetriebeListeManager,
	benutzerKompetenzen: Set<BenutzerKompetenz>,
	patch: (data: Partial<Betrieb>) => Promise<void>;
}
