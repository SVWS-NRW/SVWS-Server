import type { JahrgangsDaten, Schulform } from "@core";

export interface JahrgangDatenProps {
	schuljahr: number;
	schulform: Schulform;
	patch: (data : Partial<JahrgangsDaten>) => Promise<void>;
	data: () => JahrgangsDaten;
	mapJahrgaenge: Map<number, JahrgangsDaten>;
}