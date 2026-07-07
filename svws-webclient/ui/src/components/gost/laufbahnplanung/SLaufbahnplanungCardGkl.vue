<template>
	<div class="flex flex-col gap-4">
		<div class="text-headline-md">Gleichwertige komplexe Lernleistungen definieren</div>
		<div class="flex flex-col gap-6">
			<div class="flex flex-col gap-3">
				<div class="font-bold">Einführungsphase</div>
				<div class="grid grid-cols-3 gap-4">
					<ui-select label="Sprachen" v-model="gklEFSprachlich" :manager="managerEFSprachlich" :removable="false" />
					<ui-select label="GWs" v-model="gklEF_GW" :manager="managerEFGesellschaftswissenschaftlich" :removable="false" />
					<ui-select label="NWs" v-model="gklEF_NW" :manager="managerEFNaturwissenschaftlich" :removable="false" />
				</div>
			</div>
			<div class="flex flex-col gap-3">
				<div class="font-bold">Qualifikationsphase</div>
				<div class="grid grid-cols-3 gap-4">
					<ui-select label="Sprachen" v-model="gklQSprachlich" :manager="managerQSprachlich" :removable="false" />
					<ui-select label="GWs" v-model="gklQ_GW" :manager="managerQGesellschaftswissenschaftlich" :removable="false" />
					<ui-select label="NWs" v-model="gklQ_NW" :manager="managerQNaturwissenschaftlich" :removable="false" />
				</div>
			</div>
		</div>
	</div>
</template>

<script setup lang="ts">

	import { computed, type ComputedRef } from "vue";
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
		return `${auswahl.halbjahr.kuerzel}.${auswahl.vorgabe.quartal} ${textFach(auswahl.fach)}`;
	}

	function textFach(fach: GostFach): string {
		const bezeichnung = fach.bezeichnung;
		if ((bezeichnung === null) || (bezeichnung.length === 0)) {
			return fach.kuerzel;
		}
		return bezeichnung.length <= 15 ? bezeichnung : `${bezeichnung.substring(0, 15)}...`;
	}

</script>
