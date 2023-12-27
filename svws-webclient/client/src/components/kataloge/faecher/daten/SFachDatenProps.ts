import type { FachDaten, FaecherListeEintrag, Schulform } from "@core";

export interface FachDatenProps {
	schulform: Schulform;
	patch: (data : Partial<FachDaten>) => Promise<void>;
	data: FachDaten;
	mapKatalogeintraege: Map<number, FaecherListeEintrag>;
}