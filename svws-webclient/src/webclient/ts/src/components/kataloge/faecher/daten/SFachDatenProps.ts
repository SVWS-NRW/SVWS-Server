import { FachDaten } from "@svws-nrw/svws-core-ts";

export interface FachDatenProps {
	patch: (data : Partial<FachDaten>) => Promise<void>;
	data: FachDaten;
}