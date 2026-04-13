import type { JavaMap } from "@core";
import type { EnmManager } from "@ui";

export interface NotenmodulZugangsdatenProps {
	open: (id: number) => Promise<void>;
	manager: () => EnmManager;
	mapEnmInitialKennwoerter: () => JavaMap<number, string>;
	updatePassword: (value: string | null, id: number) => Promise<boolean>;
	resetTotp: (is: number) => Promise<boolean>;
}
