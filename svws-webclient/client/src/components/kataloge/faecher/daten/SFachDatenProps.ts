import type { FachDaten, Schulform } from "@core";

export interface FachDatenProps {
	schulform: Schulform;
	patch: (data : Partial<FachDaten>) => Promise<void>;
	data: () => FachDaten;
	mapKatalogeintraege: () => Map<number, FachDaten>;
}