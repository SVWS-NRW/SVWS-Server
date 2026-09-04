import type { Einwilligungsart } from "@core/core/data/schule/Einwilligungsart";
import type { EinwilligungsartenListeManager } from "@ui/ui/manager/kataloge/EinwilligungsartenListeManager";

export interface EinwilligungsartenDatenProps {
	manager: () => EinwilligungsartenListeManager,
	patch: (data: Partial<Einwilligungsart>) => Promise<boolean>;
}
