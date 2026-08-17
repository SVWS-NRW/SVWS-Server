import type { SchuelerKAoADaten, SchuelerListeEintrag } from "@core";
import type { SchuelerKAoAManager } from "@ui";

export interface SchuelerKAoAProps {
	manager: () => SchuelerKAoAManager;
	auswahl: () => SchuelerListeEintrag;
	add: (data: Partial<SchuelerKAoADaten>, id: number) => Promise<void>;
	patch: (data: Partial<SchuelerKAoADaten>, idKaoaEntry: number) => Promise<boolean>;
	delete: (idSchueler: number, idKaoaEntry: number) => Promise<void>;
}
