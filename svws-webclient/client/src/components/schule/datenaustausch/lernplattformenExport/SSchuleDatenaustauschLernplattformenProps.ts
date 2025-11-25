import type { BenutzerKompetenz, Lernplattform, List, Schuljahresabschnitt } from "@core";

export interface SchuleDatenaustauschLernplattformenProps {
	benutzerKompetenzen: Set<BenutzerKompetenz>,
	lernplattformen: List<Lernplattform>,
	export: (lernplattform: Lernplattform, datenformat: string) => Promise<Blob | null>,
	schuljahresabschnitt: Schuljahresabschnitt
}

