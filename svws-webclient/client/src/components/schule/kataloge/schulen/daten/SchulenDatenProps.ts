import type { SchulEintrag } from "@core/core/data/kataloge/SchulEintrag";
import type { SchulenListeManager } from "@ui/ui/manager/kataloge/SchulenListeManager";

export interface SchulenDatenProps {
	patch: (data: Partial<SchulEintrag>) => Promise<boolean>;
	manager: () => SchulenListeManager;
}
