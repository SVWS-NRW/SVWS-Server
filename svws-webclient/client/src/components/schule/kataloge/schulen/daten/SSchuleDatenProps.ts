import type { SchulEintrag, KatalogSchuleListeManager } from "@core";

export interface SchuleDatenProps {
	schuljahr: number;
	patch: (data : Partial<SchulEintrag>) => Promise<void>;
	schuleListeManager: () => KatalogSchuleListeManager;
}
