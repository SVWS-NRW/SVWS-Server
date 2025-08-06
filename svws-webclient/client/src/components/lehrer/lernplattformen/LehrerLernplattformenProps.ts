import type { List, Lernplattform, LehrerLernplattform, BenutzerKompetenz } from "@core";
import type { ApiStatus } from "~/components/ApiStatus";

export interface LehrerLernplattformenProps {
	lehrerLernplattformen: () => List<LehrerLernplattform>;
	mapLernplattformen: Map<number, Lernplattform>;
	patch: (data : Partial<LehrerLernplattform>, idLernplattform: number) => Promise<void>;
	benutzerKompetenzen: Set<BenutzerKompetenz>,
	apiStatus: ApiStatus;
}
