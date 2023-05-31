import type { Raum } from "@svws-nrw/svws-core";

export interface RaumDatenProps {
	patch: (data : Partial<Raum>) => Promise<void>;
	data: Raum;
}