import type { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import type { SchuelerSchulbesuchManager } from "~/components/schueler/schulbesuch/SchuelerSchulbesuchManager";

export interface SchuelerSchulbesuchProps {
	schulform: Schulform;
	serverMode: ServerMode;
	benutzerKompetenzen: Set<BenutzerKompetenz>,
	manager: () => SchuelerSchulbesuchManager;
	goToSchule : (idSchule : number) => Promise<void>;
	autofocus: boolean;
}
