import type { Lernplattform } from "@core";
import type { LernplattformListeManager } from "@ui";

export interface LernplattformenDatenProps {
	patch: (data: Partial<Lernplattform>) => Promise<boolean>;
	manager: () => LernplattformListeManager,
}
