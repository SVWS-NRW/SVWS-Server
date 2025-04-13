import type { TelefonArt, TelefonArtListeManager } from "@core";

export interface TelefonArtenDatenProps {
	patch: (data : Partial<TelefonArt>) => Promise<void>;
	telefonArtListeManager: () => TelefonArtListeManager,
}
