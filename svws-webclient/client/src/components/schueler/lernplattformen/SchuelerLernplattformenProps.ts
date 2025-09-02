import type { List, Lernplattform, SchuelerLernplattform, BenutzerKompetenz } from "@core";
import type { ApiStatus } from "~/components/ApiStatus";

export interface SchuelerLernplattformenProps {
	schuelerLernplattformen: () => List<SchuelerLernplattform>;
	mapLernplattformen: Map<number, Lernplattform>;
	patch: (data : Partial<SchuelerLernplattform>, idLernplattform: number) => Promise<void>;
	benutzerKompetenzen: Set<BenutzerKompetenz>,
	apiStatus: ApiStatus;
}
