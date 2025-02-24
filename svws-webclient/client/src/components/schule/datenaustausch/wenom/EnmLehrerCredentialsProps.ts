import type { ENMDaten, JavaMap } from "@core";

export interface EnmLehrerCredentialsProps {
	enmDaten: () => ENMDaten;
	mapEnmInitialKennwoerter: () => JavaMap<number, string>;
}
