import type { BenutzerKompetenz, SchuelerSchwerpunkt as Schwerpunkt } from "@core";
import type { SchwerpunkteListeManager } from "@ui";

export interface SchwerpunkteDatenProps {
	patch: (data: Partial<Schwerpunkt>) => Promise<void>;
	manager: () => SchwerpunkteListeManager,
	benutzerKompetenzen: Set<BenutzerKompetenz>;
}
