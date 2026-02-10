import type { BenutzerKompetenz, Telefonart } from "@core";
import type { TelefonartenListeManager } from "@ui";

export interface TelefonartenDatenProps {
	patch: (data: Partial<Telefonart>) => Promise<void>;
	manager: () => TelefonartenListeManager,
	benutzerKompetenzen: Set<BenutzerKompetenz>,
}
