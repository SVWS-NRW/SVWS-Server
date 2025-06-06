import type { BenutzerKompetenz, Merkmal, MerkmaleListeManager } from "@core";

export interface MerkmaleDatenProps {
	manager: () => MerkmaleListeManager;
	benutzerKompetenzen: Set<BenutzerKompetenz>
	patch: (data: Partial<Merkmal>) => Promise<void>;
}
