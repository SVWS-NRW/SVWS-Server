import type { Lernplattform } from "@core/core/data/schule/Lernplattform";
import type { LernplattformListeManager } from "@ui/ui/manager/kataloge/LernplattformListeManager";

export interface LernplattformenDatenProps {
	patch: (data: Partial<Lernplattform>) => Promise<boolean>;
	manager: () => LernplattformListeManager,
}
