import type { JavaMap } from "@core";
import type { EnmManager } from "@ui";

export interface NotenmodulZugangsdatenProps {
	open: (id: number) => Promise<void>;
	manager: () => EnmManager;
	mapEnmInitialKennwoerter: () => JavaMap<number, string>;
	resetPassword: (id: number) => Promise<void>;
	generateInitialPassword: (id: number) => Promise<string>;
	resetTotp: (id: number) => Promise<boolean>;
	set2fa: (art: number, id: number) => Promise<boolean>;
}
