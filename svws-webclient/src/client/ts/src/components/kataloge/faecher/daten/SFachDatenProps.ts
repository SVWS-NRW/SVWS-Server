import type { FachDaten, FaecherListeEintrag } from "@core";

export interface FachDatenProps {
	patch: (data : Partial<FachDaten>) => Promise<void>;
	data: FachDaten;
	mapKatalogeintraege: Map<number, FaecherListeEintrag>;
}