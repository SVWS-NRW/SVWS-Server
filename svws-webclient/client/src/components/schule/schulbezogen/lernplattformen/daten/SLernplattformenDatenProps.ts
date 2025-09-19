import type { BenutzerKompetenz, Lernplattform } from "@core";
import type { LernplattformListeManager } from "@ui";

export interface LernplattformenDatenProps {
	patch: (data : Partial<Lernplattform>) => Promise<void>;
	manager: () => LernplattformListeManager,
	benutzerKompetenzen: Set<BenutzerKompetenz>,
}
