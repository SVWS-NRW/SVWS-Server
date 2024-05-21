import type { ReligionEintrag, ReligionListeManager } from "@core";

export interface ReligionDatenProps {
	religionListeManager: () => ReligionListeManager;
	patch: (data : Partial<ReligionEintrag>) => Promise<void>;
}