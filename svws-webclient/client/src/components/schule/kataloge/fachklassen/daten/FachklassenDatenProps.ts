import type { FachklasseEintrag } from "@core/core/data/schule/FachklasseEintrag";
import type { FachklassenListeManager } from "@ui/ui/manager/kataloge/FachklassenListeManager";

export interface FachklassenDatenProps {
	patch: (data: Partial<FachklasseEintrag>) => Promise<boolean>;
	manager: () => FachklassenListeManager,
}
