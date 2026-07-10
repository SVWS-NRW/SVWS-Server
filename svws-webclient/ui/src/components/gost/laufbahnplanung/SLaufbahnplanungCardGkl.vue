<template>
	<div class="flex flex-col gap-4">
		<div class="text-headline-md">Gleichwertige komplexe Lernleistungen</div>
		<ui-table-grid name="Gleichwertige komplexe Lernleistungen" :manager="() => gridManager">
			<template #header>
				<td class="ui-divider" />
				<td class="text-center ui-divider">Einführungsphase</td>
				<td class="text-center">Qualifikationsphase</td>
			</template>
			<template #default="{ row }">
				<template v-if="row === 1">
					<td class="aufgabenfeld1 ui-divider text-uistatic text-left pt-1">
						<svws-ui-tooltip>
							Aufgabenfeld I
							<template #content>
								Sprachlich-Literarisch-Künstlerisch
							</template>
						</svws-ui-tooltip>
					</td>
					<td class="ui-divider">
						<ui-select v-model="gklEFSprachlich" :manager="managerEFSprachlich" :removable="false" headless />
					</td>
					<td>
						<ui-select v-model="gklQSprachlich" :manager="managerQSprachlich" :removable="false" headless />
					</td>
				</template>
				<template v-else-if="row === 2">
					<td class="aufgabenfeld2 ui-divider text-uistatic text-left pt-1">
						<svws-ui-tooltip>
							Aufgabenfeld II
							<template #content>
								Gesellschaftswissenschaftlich
							</template>
						</svws-ui-tooltip>
					</td>
					<td class="ui-divider">
						<ui-select v-model="gklEF_GW" :manager="managerEFGesellschaftswissenschaftlich" :removable="false" headless />
					</td>
					<td>
						<ui-select v-model="gklQ_GW" :manager="managerQGesellschaftswissenschaftlich" :removable="false" headless />
					</td>
				</template>
				<template v-else-if="row === 3">
					<td class="aufgabenfeld3 ui-divider text-uistatic text-left pt-1">
						<svws-ui-tooltip>
							Aufgabenfeld III
							<template #content>
								Mathematisch-Naturwissenschaftlich-Technisch
							</template>
						</svws-ui-tooltip>
					</td>
					<td class="ui-divider">
						<ui-select v-model="gklEF_NW" :manager="managerEFNaturwissenschaftlich" :removable="false" headless />
					</td>
					<td>
						<ui-select v-model="gklQ_NW" :manager="managerQNaturwissenschaftlich" :removable="false" headless />
					</td>
				</template>
			</template>
		</ui-table-grid>
	</div>
</template>

<script setup lang="ts">

	import type { ComputedRef } from "vue";
	import { computed } from "vue";
	import type { GostFach } from "../../../../../core/src/core/data/gost/GostFach";
	import { GostFachbereich } from "../../../../../core/src/core/types/gost/GostFachbereich";
	import { GostHalbjahr } from "../../../../../core/src/core/types/gost/GostHalbjahr";
	import { GostKursart } from "../../../../../core/src/core/types/gost/GostKursart";
	import { GostSchriftlichkeit } from "../../../../../core/src/core/types/gost/GostSchriftlichkeit";
	import { SelectManager } from "../../../ui/controls/select/manager/SelectManager";
	import type { GostKlausurvorgabeEintrag } from "../../../states/GostLaufbahnplanungState";
	import { useGostLaufbahnplanungState } from "../../../states/GostLaufbahnplanungState";
	import type { List } from "../../../../../core/src/java/util/List";
	import { ArrayList } from "../../../../../core/src/java/util/ArrayList";
	import type { Comparator } from "../../../../../core/src/java/util/Comparator";
	import { JavaInteger } from "../../../../../core/src/java/lang/JavaInteger";
	import { Fach } from "../../../../../core/src/asd/types/fach/Fach";
	import { GridManager } from "../../../ui/controls/tablegrid/GridManager";

	const gostLaufbahnplanungState = useGostLaufbahnplanungState();

	const gklEFSprachlich = computed<GostKlausurvorgabeEintrag | null>({
		get: () => gostLaufbahnplanungState.getKlausurvorgabe(gostLaufbahnplanungState.gklWahlen.idKlausurvorgabeEF_Sprachen),
		set: (value) => void gostLaufbahnplanungState.patchGKLWahlen({ idKlausurvorgabeEF_Sprachen: value?.vorgabe.id ?? null }),
	});

	const gklEF_GW = computed<GostKlausurvorgabeEintrag | null>({
		get: () => gostLaufbahnplanungState.getKlausurvorgabe(gostLaufbahnplanungState.gklWahlen.idKlausurvorgabeEF_GW),
		set: (value) => void gostLaufbahnplanungState.patchGKLWahlen({ idKlausurvorgabeEF_GW: value?.vorgabe.id ?? null }),
	});

	const gklEF_NW = computed<GostKlausurvorgabeEintrag | null>({
		get: () => gostLaufbahnplanungState.getKlausurvorgabe(gostLaufbahnplanungState.gklWahlen.idKlausurvorgabeEF_NW),
		set: (value) => void gostLaufbahnplanungState.patchGKLWahlen({ idKlausurvorgabeEF_NW: value?.vorgabe.id ?? null }),
	});

	const gklQSprachlich = computed<GostKlausurvorgabeEintrag | null>({
		get: () => gostLaufbahnplanungState.getKlausurvorgabe(gostLaufbahnplanungState.gklWahlen.idKlausurvorgabeQ_Sprachen),
		set: (value) => void gostLaufbahnplanungState.patchGKLWahlen({ idKlausurvorgabeQ_Sprachen: value?.vorgabe.id ?? null }),
	});

	const gklQ_GW = computed<GostKlausurvorgabeEintrag | null>({
		get: () => gostLaufbahnplanungState.getKlausurvorgabe(gostLaufbahnplanungState.gklWahlen.idKlausurvorgabeQ_GW),
		set: (value) => void gostLaufbahnplanungState.patchGKLWahlen({ idKlausurvorgabeQ_GW: value?.vorgabe.id ?? null }),
	});

	const gklQ_NW = computed<GostKlausurvorgabeEintrag | null>({
		get: () => gostLaufbahnplanungState.getKlausurvorgabe(gostLaufbahnplanungState.gklWahlen.idKlausurvorgabeQ_NW),
		set: (value) => void gostLaufbahnplanungState.patchGKLWahlen({ idKlausurvorgabeQ_NW: value?.vorgabe.id ?? null }),
	});

	const faecherEFSprachlich = computed(() => getFaecher(GostFachbereich.SPRACHLICH_LITERARISCH_KUENSTLERISCH, GostHalbjahr.getEinfuehrungsphase()));
	const faecherEFGesellschaftswissenschaftlich = computed(() => getFaecher(GostFachbereich.GESELLSCHAFTSWISSENSCHAFTLICH_MIT_RELIGION, GostHalbjahr.getEinfuehrungsphase()));
	const faecherEFNaturwissenschaftlich = computed(() => getFaecher(GostFachbereich.MATHEMATISCH_NATURWISSENSCHAFTLICH, GostHalbjahr.getEinfuehrungsphase()));
	const faecherQSprachlich = computed(() => getFaecher(GostFachbereich.SPRACHLICH_LITERARISCH_KUENSTLERISCH, GostHalbjahr.getQualifikationsphase()));
	const faecherQGesellschaftswissenschaftlich = computed(() => getFaecher(GostFachbereich.GESELLSCHAFTSWISSENSCHAFTLICH_MIT_RELIGION, GostHalbjahr.getQualifikationsphase()));
	const faecherQNaturwissenschaftlich = computed(() => getFaecher(GostFachbereich.MATHEMATISCH_NATURWISSENSCHAFTLICH, GostHalbjahr.getQualifikationsphase()));

	const managerEFSprachlich = getSelectManager(faecherEFSprachlich);
	const managerEFGesellschaftswissenschaftlich = getSelectManager(faecherEFGesellschaftswissenschaftlich);
	const managerEFNaturwissenschaftlich = getSelectManager(faecherEFNaturwissenschaftlich);
	const managerQSprachlich = getSelectManager(faecherQSprachlich);
	const managerQGesellschaftswissenschaftlich = getSelectManager(faecherQGesellschaftswissenschaftlich);
	const managerQNaturwissenschaftlich = getSelectManager(faecherQNaturwissenschaftlich);

	const gridManager = new GridManager<string, number, Array<number>>({
		daten: computed<Array<number>>(() => [1, 2, 3]),
		getRowKey: row => `Aufgabenfeld ${row}`,
		columns: [
			{ kuerzel: "Aufgabenfeld", name: "Aufgabenfeld", width: "9rem", hideable: false },
			{ kuerzel: "Einführungsphase", name: "Einführungsphase", width: "1fr", hideable: false },
			{ kuerzel: "Qualifikationsphase", name: "Qualifikationsphase", width: "1fr", hideable: false },
		],
	});

	function getFaecher(bereich: GostFachbereich, halbjahre: GostHalbjahr[]): List<GostKlausurvorgabeEintrag> {
		const result = new ArrayList<GostKlausurvorgabeEintrag>;
		const manager = gostLaufbahnplanungState.abiturdatenManager;

		// Durchwandere alle Fachbelegungen, die zu dem Fachbereich gehören, jeweils für die jeweiligen Halbjahre
		for (const fachbelegung of manager.getFachbelegungen(bereich)) {
			const fach = manager.getFach(fachbelegung);
			if (fach === null) {
				continue;
			}
			for (const halbjahr of halbjahre) {
				// Prüfe, ob eine schriftliche Grundkursbelegung vorliegt oder nicht.
				if (!manager.pruefeBelegungMitSchriftlichkeitEinzeln(fachbelegung, GostSchriftlichkeit.SCHRIFTLICH, halbjahr)
					|| !manager.pruefeBelegungMitKursart(fachbelegung, GostKursart.GK, halbjahr)) {
					continue;
				}

				// Prüfe, ob in dem Halbjahr eine GKL möglich ist
				result.addAll(gostLaufbahnplanungState.istGKLMoeglich(fachbelegung.fachID, halbjahr));
			}
		}
		result.sort(<Comparator<GostKlausurvorgabeEintrag>>{ compare: (a, b) => {
			let tmp = GostFachbereich.compareGostFach(a.fach, b.fach);
			if (tmp !== 0) {
				return tmp;
			}
			tmp = a.halbjahr.compareTo(b.halbjahr);
			if (tmp !== 0) {
				return tmp;
			}
			return JavaInteger.compare(a.vorgabe.quartal, b.vorgabe.quartal);
		} });
		return result;
	}

	function getSelectManager(options: ComputedRef<List<GostKlausurvorgabeEintrag>>): SelectManager<GostKlausurvorgabeEintrag> {
		return new SelectManager({
			options,
			optionDisplayText: textAuswahl,
			selectionDisplayText: textAuswahl,
		});
	}

	function textAuswahl(auswahl: GostKlausurvorgabeEintrag): string {
		return `${auswahl.halbjahr.kuerzel}-${auswahl.vorgabe.quartal} ${textFach(auswahl.fach)}`;
	}

	function textFach(fach: GostFach): string {
		const bezeichnung = fach.bezeichnung;
		if ((bezeichnung === null) || (bezeichnung.length === 0)) {
			return fach.kuerzel;
		}
		return bezeichnung.length <= 20 ? bezeichnung : `${bezeichnung.substring(0, 20)}...`;
	}

	const bgColorAufgabenfeld1 = Fach.D.getHMTLFarbeRGBA(2030, 1);
	const bgColorAufgabenfeld2 = Fach.GE.getHMTLFarbeRGBA(2030, 1);
	const bgColorAufgabenfeld3 = Fach.M.getHMTLFarbeRGBA(2030, 1);


</script>

<style scoped>

	td.aufgabenfeld1 {
		background-color: v-bind(bgColorAufgabenfeld1);
		color: var(--color-text-uistatic);
	}

	td.aufgabenfeld2 {
		background-color: v-bind(bgColorAufgabenfeld2);
		color: var(--color-text-uistatic);
	}

	td.aufgabenfeld3 {
		background-color: v-bind(bgColorAufgabenfeld3);
		color: var(--color-text-uistatic);
	}

</style>
