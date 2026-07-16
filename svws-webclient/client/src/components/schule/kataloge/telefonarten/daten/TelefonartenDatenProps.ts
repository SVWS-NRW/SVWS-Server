import type { Telefonart } from "@core";
import type { TelefonartenListeManager } from "@ui";

export interface TelefonartenDatenProps {
	patch: (data: Partial<Telefonart>) => Promise<boolean>;
	manager: () => TelefonartenListeManager,
}
