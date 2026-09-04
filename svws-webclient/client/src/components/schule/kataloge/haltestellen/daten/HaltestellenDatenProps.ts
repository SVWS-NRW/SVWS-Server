import type { Haltestelle } from "@core/core/data/schule/Haltestelle";
import type { HaltestellenListeManager } from "@ui/ui/manager/kataloge/HaltestellenListeManager";

export interface HaltestellenDatenProps {
	manager: () => HaltestellenListeManager;
	patch: (data: Partial<Haltestelle>) => Promise<boolean>;
}
