import type { BenutzerKompetenz, Schulform, ServerMode, SchuelerSchulbesuchSchule, List } from "@core";
import type { SchuelerSchulbesuchManager } from "~/components/schueler/schulbesuch/SchuelerSchulbesuchManager";

export interface SchuelerSchulbesuchProps {
	schulform: Schulform;
	serverMode: ServerMode;
	benutzerKompetenzen: Set<BenutzerKompetenz>,
	manager: () => SchuelerSchulbesuchManager;
	goToSchule: (idSchule : number) => Promise<void>;
	autofocus: boolean;
	addSchuelerSchulbesuchSchule: (data : Partial<SchuelerSchulbesuchSchule>) => Promise<void>;
	patchSchuelerSchulbesuchSchule: (id: number, data : Partial<SchuelerSchulbesuchSchule>) => Promise<void>;
	deleteSchuelerSchulbesuchSchulen: (ids: List<number>) => Promise<void>;
}
