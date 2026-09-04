import type { JahrgangsDaten } from "@core/core/data/jahrgang/JahrgangsDaten";
import type { JahrgaengeListeManager } from "@ui/ui/manager/kataloge/JahrgaengeListeManager";

export interface JahrgaengeDatenProps {
	manager: () => JahrgaengeListeManager;
	patch: (data: Partial<JahrgangsDaten>) => Promise<boolean>;
}
