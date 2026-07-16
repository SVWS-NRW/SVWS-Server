import type { Kindergarten } from "@core";
import type { KindergaertenListeManager } from "@ui";

export interface KindergaertenDatenProps {
	manager: () => KindergaertenListeManager;
	patch: (data: Partial<Kindergarten>) => Promise<boolean>;
}
