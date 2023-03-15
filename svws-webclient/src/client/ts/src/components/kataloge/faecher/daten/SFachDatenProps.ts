import { FachDaten, FaecherListeEintrag } from "@svws-nrw/svws-core";

export interface FachDatenProps {
	patch: (data : Partial<FachDaten>) => Promise<void>;
	data: FachDaten;
	mapKatalogeintraege: Map<number, FaecherListeEintrag>;
}