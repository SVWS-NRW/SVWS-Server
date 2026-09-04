import type { JahrgangsDaten } from "@core/core/data/jahrgang/JahrgangsDaten";

export interface GostAbiturjahrgangNeuProps {
	mapJahrgaengeOhneAbiJahrgang: () => Map<number, JahrgangsDaten>;
	addAbiturjahrgang: (idJahrgang: number) => Promise<void>;
	getAbiturjahrFuerJahrgang: (idJahrgang: number) => number;
	cancelCreationMode: () => Promise<void>;
}
