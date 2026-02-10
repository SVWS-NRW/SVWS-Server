import type { JavaMap } from "@core";
import type { EnmManager } from "@ui";

export interface NotenmodulZugangsdatenProps {
	manager: () => EnmManager;
	mapEnmInitialKennwoerter: () => JavaMap<number, string>;
}
