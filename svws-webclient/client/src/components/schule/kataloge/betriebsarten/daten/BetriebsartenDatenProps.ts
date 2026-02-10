import type { BenutzerKompetenz, Betriebsart } from "@core";
import type { BetriebsartenListeManager } from "@ui";

export interface BetriebsartenDatenProps {
	patch: (data: Partial<Betriebsart>) => Promise<void>;
	manager: () => BetriebsartenListeManager,
	benutzerKompetenzen: Set<BenutzerKompetenz>;
}