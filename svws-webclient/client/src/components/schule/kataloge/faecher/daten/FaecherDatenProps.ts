import type { FachDaten } from "@core";
import type { FaecherListeManager } from "@ui";

export interface FaecherDatenProps {
	patch: (data: Partial<FachDaten>) => Promise<boolean>;
	manager: () => FaecherListeManager;
}
