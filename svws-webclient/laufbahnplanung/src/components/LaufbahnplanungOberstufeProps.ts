import type { ApiFile } from "@core/api/BaseApi";
import type { AbiturdatenManager } from "@core/core/abschluss/gost/AbiturdatenManager";
import type { GostBelegpruefungErgebnis } from "@core/core/abschluss/gost/GostBelegpruefungErgebnis";
import type { GostJahrgangsdaten } from "@core/core/data/gost/GostJahrgangsdaten";
import type { GostLaufbahnplanungDaten } from "@core/core/data/gost/GostLaufbahnplanungDaten";
import type { GostSchuelerFachwahl } from "@core/core/data/gost/GostSchuelerFachwahl";
import type { SchuelerListeEintrag } from "@core/core/data/schueler/SchuelerListeEintrag";

export interface LaufbahnplanungOberstufeProps {
	setWahl: (fachID: number, wahl: GostSchuelerFachwahl) => Promise<void>;
	setGostBelegpruefungsArt: (value: 'ef1'|'gesamt'|'auto') => Promise<void>;
	exportLaufbahnplanung: () => Promise<ApiFile>;
	importLaufbahnplanung: (data: FormData) => Promise<string | null>;
	schueler: SchuelerListeEintrag,
	gostJahrgangsdaten: GostJahrgangsdaten;
	gostBelegpruefungsArt: () => 'ef1'|'gesamt'|'auto';
	gostBelegpruefungErgebnis: () => GostBelegpruefungErgebnis;
	abiturdatenManager: () => AbiturdatenManager;
	id?: number;
	zwischenspeicher?: GostLaufbahnplanungDaten;
	saveLaufbahnplanung: () => Promise<void>;
	restoreLaufbahnplanung: () => Promise<void>;
	resetFachwahlen: () => Promise<void>;
	modus: 'manuell'|'normal'|'hochschreiben';
	setModus: (modus: 'manuell'|'normal'|'hochschreiben') => Promise<void>;
}
