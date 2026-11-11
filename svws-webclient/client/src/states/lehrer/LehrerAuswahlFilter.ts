import type { LehrerListeEintrag } from "@core/core/data/lehrer/LehrerListeEintrag";
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { PersonalTyp } from "@core/core/types/PersonalTyp";
import { JavaLong } from "@core/java/lang/JavaLong";
import { JavaString } from "@core/java/lang/JavaString";
import { Arrays } from "@core/java/util/Arrays";
import { HashSet } from "@core/java/util/HashSet";
import type { JavaSet } from "@core/java/util/JavaSet";
import type { List } from "@core/java/util/List";

import { GenericAuswahlFilter } from "../GenericAuswahlFilter";

/** Die Filterwerte der Auswahlliste der Lehrer */
export interface LehrerAuswahlFilterValues {
	/** Die IDs der gewählten Personaltypen */
	personaltypen: JavaSet<number>;
	/** Gibt an, ob nur sichtbare Lehrer angezeigt werden */
	nurSichtbar: boolean;
	/** Gibt an, ob nur für die Statistik relevante Lehrer angezeigt werden */
	nurStatistikrelevant: boolean;
}

/** Die Optionen der Filterfelder der Auswahlliste der Lehrer */
export interface LehrerAuswahlFilterOptions {
	personaltypen: List<PersonalTyp>;
}

/**
 * Der Filter für die Auswahlliste der Lehrer
 */
export class LehrerAuswahlFilter extends GenericAuswahlFilter<LehrerListeEintrag, LehrerAuswahlFilterValues, LehrerAuswahlFilterOptions> {

	/** Das Model für das Auswahlfeld nurSichtbar */
	private readonly _nurSichtbarModel = this.valueModel("nurSichtbar");

	/** Das Model für das Auswahlfeld nurStatistikrelevant */
	private readonly _nurStatistikrelevantModel = this.valueModel("nurStatistikrelevant");

	/** Das Model für das Auswahlfeld personaltypen */
	private readonly _personaltypenModel = this.selectionModel("personaltypen", () => this.options.personaltypen, p => p.id);

	/**
	 * Erzeugt einen neuen Filter für die Lehrer.
	 *
	 * @param entries      die Funktion, welche die Einträge der Lehrerliste liefert
	 * @param idSelected   die Funktion, welche die ID des aktuell gewählten Lehrers oder null liefert
	 */
	public constructor(entries: () => Iterable<LehrerListeEintrag>, idSelected: () => number | null) {
		super(
			{ personaltypen: new HashSet<number>(), nurSichtbar: true, nurStatistikrelevant: true },
			entries, idSelected,
			[{ field: "nachname", ascending: true }, { field: "vorname", ascending: true }, { field: "kuerzel", ascending: true }]
		);
	}

	/** Das Model für das Auswahlfeld der Oberfläche: nurSichtbar */
	public get nurSichtbarModel(): boolean {
		return this._nurSichtbarModel.value;
	}

	public set nurSichtbarModel(value: boolean) {
		this._nurSichtbarModel.value = value;
	}

	/** Das Model für das Auswahlfeld der Oberfläche: nurStatistikrelevant */
	public get nurStatistikrelevantModel(): boolean {
		return this._nurStatistikrelevantModel.value;
	}

	public set nurStatistikrelevantModel(value: boolean) {
		this._nurStatistikrelevantModel.value = value;
	}

	/** Das Model für das Auswahlfeld der Oberfläche: personaltypen */
	public get personaltypenModel(): PersonalTyp[] {
		return this._personaltypenModel.value;
	}

	public set personaltypenModel(value: PersonalTyp[]) {
		this._personaltypenModel.value = value;
	}

	protected createOptions(): LehrerAuswahlFilterOptions {
		return { personaltypen: Arrays.asList(...PersonalTyp.values()) };
	}

	protected checkPassFilter(entry: LehrerListeEintrag, values: LehrerAuswahlFilterValues): boolean {
		if (values.nurSichtbar && !entry.istSichtbar) {
			return false;
		}
		if (values.nurStatistikrelevant && !entry.istRelevantFuerStatistik) {
			return false;
		}
		if (!values.personaltypen.isEmpty()) {
			const personalTyp = JavaString.isEmpty(entry.personTyp) ? null : PersonalTyp.fromKuerzel(entry.personTyp);
			if ((personalTyp === null) || !values.personaltypen.contains(personalTyp.id)) {
				return false;
			}
		}
		return true;
	}

	protected checkPassSearch(entry: LehrerListeEintrag, search: string): boolean {
		const locale = search.toLocaleLowerCase();
		const searchValueIsNumber = /^\d+$/.test(locale.trim());
		return (searchValueIsNumber && entry.id.toString().includes(locale))
			|| entry.nachname.toLocaleLowerCase().includes(locale)
			|| entry.vorname.toLocaleLowerCase().includes(locale)
			|| entry.kuerzel.toLocaleLowerCase().includes(locale);
	}

	protected compare(a: LehrerListeEintrag, b: LehrerListeEintrag): number {
		for (const { field, ascending } of this._state.value.order) {
			let cmp: number;
			if (field === "kuerzel") {
				cmp = JavaString.compareTo(a.kuerzel, b.kuerzel);
			} else if (field === "nachname") {
				cmp = JavaString.compareTo(a.nachname, b.nachname);
			} else if (field === "vorname") {
				cmp = JavaString.compareTo(a.vorname, b.vorname);
			} else {
				throw new DeveloperNotificationException("Fehler bei der Sortierung. Das Sortierkriterium wird vom Filter nicht unterstützt.");
			}
			if (cmp !== 0) {
				return ascending ? cmp : -cmp;
			}
		}
		return JavaLong.compare(a.id, b.id);
	}

	protected hasFilter(values: LehrerAuswahlFilterValues): boolean {
		return !values.personaltypen.isEmpty();
	}

	protected cleanValues(values: LehrerAuswahlFilterValues, options: LehrerAuswahlFilterOptions): LehrerAuswahlFilterValues {
		const personaltypen = new HashSet<number>();
		for (const p of options.personaltypen) {
			if (values.personaltypen.contains(p.id)) {
				personaltypen.add(p.id);
			}
		}
		return { personaltypen, nurSichtbar: values.nurSichtbar, nurStatistikrelevant: values.nurStatistikrelevant };
	}

}
