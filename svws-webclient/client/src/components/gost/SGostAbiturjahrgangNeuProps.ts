import type { JahrgangsDaten } from "@core";

export interface GostAbiturjahrgangNeuProps {
	mapJahrgaengeOhneAbiJahrgang: () => Map<number, JahrgangsDaten>;
	addAbiturjahrgang: (idJahrgang: number) => Promise<void>;
	getAbiturjahrFuerJahrgang: (idJahrgang: number) => number;
	cancelCreationMode: () => Promise<void>;
}
