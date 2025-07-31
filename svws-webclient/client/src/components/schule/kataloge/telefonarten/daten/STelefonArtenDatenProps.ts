import type { TelefonArt } from "@core";
import { TelefonArtListeManager } from "@ui";

export interface TelefonArtenDatenProps {
	patch: (data : Partial<TelefonArt>) => Promise<void>;
	telefonArtListeManager: () => TelefonArtListeManager,
}
