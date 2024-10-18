import type { Raum } from "@core";

export interface RaeumeProps {
	patch: (data : Partial<Raum>) => Promise<void>;
	auswahl: Raum | undefined;
}