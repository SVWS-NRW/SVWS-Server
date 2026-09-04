import type { ReligionEintrag } from "@core/core/data/schule/ReligionEintrag";
import type { KonfessionenListeManager } from "@ui/ui/manager/kataloge/KonfessionenListeManager";

export interface KonfessionenDatenProps {
	manager: () => KonfessionenListeManager;
	patch: (data: Partial<ReligionEintrag>) => Promise<boolean>;
}
