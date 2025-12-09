import type { BenutzerKompetenz, Betrieb } from "@core";
import type { BetriebeListeManager } from "../../../../../../../ui/src/ui/manager/kataloge/BetriebeListeManager";

export interface BetriebeDatenProps {
	manager: () => BetriebeListeManager,
	benutzerKompetenzen: Set<BenutzerKompetenz>,
	patch: (data: Partial<Betrieb>) => Promise<void>;
}
