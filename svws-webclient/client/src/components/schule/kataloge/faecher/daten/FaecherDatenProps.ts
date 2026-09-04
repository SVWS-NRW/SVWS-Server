import type { FachDaten } from "@core/core/data/fach/FachDaten";
import type { FaecherListeManager } from "@ui/ui/manager/kataloge/FaecherListeManager";

export interface FaecherDatenProps {
	patch: (data: Partial<FachDaten>) => Promise<boolean>;
	manager: () => FaecherListeManager;
}
