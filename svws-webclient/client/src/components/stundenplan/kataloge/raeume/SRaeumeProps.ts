import type { Raum } from "@core/core/data/schule/Raum";

export interface RaeumeProps {
	patch: (data: Partial<Raum>) => Promise<void>;
	auswahl: Raum | undefined;
}