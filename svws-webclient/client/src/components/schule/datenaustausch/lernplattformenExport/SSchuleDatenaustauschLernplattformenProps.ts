import type { BenutzerKompetenz, Lernplattform, List } from "@core";

export interface SchuleDatenaustauschLernplattformenProps {
	benutzerKompetenzen: Set<BenutzerKompetenz>,
	lernplattformen: List<Lernplattform>,
	export: (lernplattform: Lernplattform, datenformat: string) => Promise<Blob | null>,
}

