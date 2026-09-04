import type { Fahrschuelerart } from "@core/core/data/schule/Fahrschuelerart";
import type { FahrschuelerartenListeManager } from "@ui/ui/manager/kataloge/FahrschuelerartenListeManager";

export interface FahrschuelerartenDatenProps {
	manager: () => FahrschuelerartenListeManager;
	patch: (data: Partial<Fahrschuelerart>) => Promise<boolean>;
}
