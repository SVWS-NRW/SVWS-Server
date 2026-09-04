import type { SchuelerKAoADaten } from "@core/core/data/schueler/SchuelerKAoADaten";
import type { SchuelerListeEintrag } from "@core/core/data/schueler/SchuelerListeEintrag";
import type { SchuelerKAoAManager } from "@ui/ui/manager/schueler/SchuelerKAoAManager";

export interface SchuelerKAoAProps {
	manager: () => SchuelerKAoAManager;
	auswahl: () => SchuelerListeEintrag;
	add: (data: Partial<SchuelerKAoADaten>, id: number) => Promise<void>;
	patch: (data: Partial<SchuelerKAoADaten>, idKaoaEntry: number) => Promise<boolean>;
	delete: (idSchueler: number, idKaoaEntry: number) => Promise<void>;
}
