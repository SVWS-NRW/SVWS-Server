import type { SchulEintrag } from "@core/core/data/kataloge/SchulEintrag";
import type { FoerderschwerpunktEintrag } from "@core/core/data/schule/FoerderschwerpunktEintrag";
import type { Haltestelle } from "@core/core/data/schule/Haltestelle";
import type { Telefonart } from "@core/core/data/schule/Telefonart";

export interface SchuelerIndividualdatenProps {
	mapSchulen: Map<string, SchulEintrag>;
	foerderschwerpunkteById: Map<number, FoerderschwerpunktEintrag>;
	haltestellenById: Map<number, Haltestelle>
	mapTelefonArten: Map<number, Telefonart>
	zeigeAlles: boolean;
}
