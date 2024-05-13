import type { FachDaten, FachListeManager } from "@core";

export interface FachDatenProps {
	patch: (data : Partial<FachDaten>) => Promise<void>;
	fachListeManager: () => FachListeManager;
}