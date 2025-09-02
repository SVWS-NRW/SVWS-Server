import type { BenutzerKompetenz, SchuelerKAoADaten, SchuelerListeEintrag } from "@core";
import type { SchuelerKAoAManager } from "@ui";

export interface SchuelerKAoAProps {
	schuelerKaoaManager: () => SchuelerKAoAManager;
	auswahl: () => SchuelerListeEintrag;
	add: (data : Partial<SchuelerKAoADaten>, id : number) => Promise<void>;
	patch: (data : Partial<SchuelerKAoADaten>, idKaoaEntry: number) => Promise<void>;
	delete: (idSchueler: number, idKaoaEntry: number) => Promise<void>;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
}
