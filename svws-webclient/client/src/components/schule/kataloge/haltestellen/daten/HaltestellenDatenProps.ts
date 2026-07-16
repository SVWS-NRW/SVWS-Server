import type { Haltestelle } from "@core";
import type { HaltestellenListeManager } from "@ui";

export interface HaltestellenDatenProps {
	manager: () => HaltestellenListeManager;
	patch: (data: Partial<Haltestelle>) => Promise<boolean>;
}
