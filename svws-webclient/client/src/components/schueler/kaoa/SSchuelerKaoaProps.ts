import type { SchuelerKAoADaten, SchuelerKAoAManager, SchuelerListeEintrag} from "@core";

export interface SchuelerKAoAProps {
	schuelerKaoaManager: () => SchuelerKAoAManager;
	auswahl: () => SchuelerListeEintrag;
	addKaoaDaten: (data : Partial<SchuelerKAoADaten>, id : number) => Promise<void>;
	patchKaoaDaten: (data : Partial<SchuelerKAoADaten>, idKaoaEntry: number) => Promise<void>;
	deleteKaoaDaten: (idSchueler: number, idKaoaEntry: number) => Promise<void>;
}
