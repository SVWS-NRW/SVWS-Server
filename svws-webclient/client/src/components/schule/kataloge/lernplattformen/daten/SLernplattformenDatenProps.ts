import type { Lernplattform, LernplattformListeManager } from "@core";

export interface LernplattformenDatenProps {
	patch: (data : Partial<Lernplattform>) => Promise<void>;
	lernplattformListeManager: () => LernplattformListeManager,
}
