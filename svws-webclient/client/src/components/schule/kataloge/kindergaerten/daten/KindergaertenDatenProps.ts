import type { Kindergarten } from "@core/core/data/schule/Kindergarten";
import type { KindergaertenListeManager } from "@ui/ui/manager/kataloge/KindergaertenListeManager";

export interface KindergaertenDatenProps {
	manager: () => KindergaertenListeManager;
	patch: (data: Partial<Kindergarten>) => Promise<boolean>;
}
