import type { SchuelerKAoADaten, SchuelerKAoAManager, SchuelerListeEintrag} from "@core";

export interface SchuelerKAoAProps {
	patch: (data : Partial<SchuelerKAoADaten>) => Promise<void>;
	schuelerKaoaManager: () => SchuelerKAoAManager;
	auswahl: () => SchuelerListeEintrag;
	addKaoaDaten: (data : Partial<SchuelerKAoADaten>, id : number) => Promise<void>;
	deleteKaoaDaten: (idSchueler: number, idKaoaEntry: number) => Promise<void>;
}
