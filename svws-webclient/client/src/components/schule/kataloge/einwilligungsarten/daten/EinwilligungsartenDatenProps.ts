import type { Einwilligungsart } from "@core";
import type { EinwilligungsartenListeManager } from "@ui";

export interface EinwilligungsartenDatenProps {
	manager: () => EinwilligungsartenListeManager,
	patch: (data: Partial<Einwilligungsart>) => Promise<boolean>;
}
