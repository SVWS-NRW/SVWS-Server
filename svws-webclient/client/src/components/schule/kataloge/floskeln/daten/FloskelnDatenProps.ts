import type { Floskel } from "@core/core/data/schule/Floskel";
import type { FloskelnListeManager } from "@ui/ui/manager/kataloge/FloskelnListeManager";

export interface FloskelnDatenProps {
	manager: () => FloskelnListeManager;
	patch: (data: Partial<Floskel>) => Promise<boolean>;
}
