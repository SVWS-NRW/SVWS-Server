import type { Floskel } from "@core";
import type { FloskelnListeManager } from "@ui";

export interface FloskelnDatenProps {
	manager: () => FloskelnListeManager;
	patch: (data: Partial<Floskel>) => Promise<boolean>;
}
