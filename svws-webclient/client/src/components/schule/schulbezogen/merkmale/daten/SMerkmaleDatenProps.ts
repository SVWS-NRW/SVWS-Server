import type { BenutzerKompetenz, Merkmal } from "@core";
import type { MerkmaleListeManager } from "@ui";

export interface MerkmaleDatenProps {
	manager: () => MerkmaleListeManager;
	benutzerKompetenzen: Set<BenutzerKompetenz>
	patch: (data: Partial<Merkmal>) => Promise<void>;
}
