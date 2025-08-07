import type { BenutzerKompetenz, TelefonArt } from "@core";
import type { TelefonArtListeManager } from "@ui";

export interface TelefonArtenDatenProps {
	patch: (data : Partial<TelefonArt>) => Promise<void>;
	telefonArtListeManager: () => TelefonArtListeManager,
	benutzerKompetenzen: Set<BenutzerKompetenz>,
}
