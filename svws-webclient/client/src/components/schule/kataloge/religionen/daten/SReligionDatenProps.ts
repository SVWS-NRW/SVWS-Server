import type { ReligionEintrag } from "@core";
import type { ReligionListeManager } from "@ui";

export interface ReligionDatenProps {
	religionListeManager: () => ReligionListeManager;
	patch: (data : Partial<ReligionEintrag>) => Promise<void>;
}
