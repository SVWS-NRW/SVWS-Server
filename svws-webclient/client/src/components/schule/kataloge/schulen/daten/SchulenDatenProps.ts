import type { SchulEintrag } from "@core";
import type { SchulenListeManager } from "@ui";

export interface SchulenDatenProps {
	patch: (data: Partial<SchulEintrag>) => Promise<boolean>;
	manager: () => SchulenListeManager;
}
