import type { Fahrschuelerart } from "@core";
import type { FahrschuelerartenListeManager } from "@ui";

export interface FahrschuelerartenDatenProps {
	manager: () => FahrschuelerartenListeManager;
	patch: (data: Partial<Fahrschuelerart>) => Promise<boolean>;
}
