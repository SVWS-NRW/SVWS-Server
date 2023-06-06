import type { Raum } from "@core";

export interface RaumDatenProps {
	patch: (data : Partial<Raum>) => Promise<void>;
	data: Raum;
}