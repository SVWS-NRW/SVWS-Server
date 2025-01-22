import type { ENMDaten, JavaMap } from "@core";

export interface EnmLehrerCredentialsProps {
	enmDaten: () => ENMDaten;
	mapInitialKennwoerter: () => JavaMap<number, string>;
}
