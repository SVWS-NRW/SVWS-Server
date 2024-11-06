import type { SchuelerKAoADaten, SchuelerKAoAManager, SchuelerListeEintrag} from "@core";

export interface SchuelerKAoAProps {
	schuelerKaoaManager: () => SchuelerKAoAManager;
	auswahl: () => SchuelerListeEintrag;
	add: (data : Partial<SchuelerKAoADaten>, id : number) => Promise<void>;
	patch: (data : Partial<SchuelerKAoADaten>, idKaoaEntry: number) => Promise<void>;
	delete: (idSchueler: number, idKaoaEntry: number) => Promise<void>;
}
