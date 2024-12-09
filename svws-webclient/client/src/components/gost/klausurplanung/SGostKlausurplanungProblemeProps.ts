
import type {
	GostHalbjahr,
	GostJahrgangsdaten,
	GostKlausurplanManager,
	GostKlausurtermin,
	GostSchuelerklausur,
	List,
	Schuljahresabschnitt,
	StundenplanManager,
} from "@core";
import type { WritableComputedRef } from "vue";

export interface GostKlausurplanungProblemeProps {
	jahrgangsdaten: GostJahrgangsdaten | undefined;
	halbjahr: GostHalbjahr;
	abschnitt: Schuljahresabschnitt | undefined;
	kMan: () => GostKlausurplanManager;
	quartalsauswahl: WritableComputedRef<0 | 1 | 2>;
	erzeugeSchuelerklausuren: (termine: List<Partial<GostSchuelerklausur>>) => Promise<void>;
	loescheSchuelerklausuren: (termine: List<GostSchuelerklausur>) => Promise<void>;
	erzeugeKursklausurenAusVorgaben: (quartal: number) => Promise<void>;
	gotoVorgaben: () => Promise<void>;
	gotoSchienen: (termin: GostKlausurtermin | undefined) => Promise<void>;
	gotoKalenderdatum: (datum: string | undefined, termin: GostKlausurtermin | undefined) => Promise<void>;
	gotoRaumzeitTermin: (abiturjahr: number, halbjahr: GostHalbjahr, value: number) => Promise<void>;
	gotoNachschreiber: (abiturjahr: number, halbjahr: GostHalbjahr) => Promise<void>;
	gotoStundenplan: () => Promise<void>;
}