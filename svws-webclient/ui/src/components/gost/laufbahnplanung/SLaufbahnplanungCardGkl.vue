<template>
	<div class="flex flex-col gap-4">
		<div class="text-headline-md">Gleichwertige komplexe Lernleistungen definieren</div>
		<div class="flex flex-col gap-6">
			<div class="flex flex-col gap-3">
				<div class="font-bold">Einführungsphase</div>
				<div class="grid grid-cols-3 gap-4">
					<ui-select label="Sprachen" v-model="auswahl.ef.sprachlich" :manager="managerEFSprachlich" :removable="false" />
					<ui-select label="GWs" v-model="auswahl.ef.gesellschaftswissenschaftlich" :manager="managerEFGesellschaftswissenschaftlich" :removable="false" />
					<ui-select label="NWs" v-model="auswahl.ef.naturwissenschaftlich" :manager="managerEFNaturwissenschaftlich" :removable="false" />
				</div>
			</div>
			<div class="flex flex-col gap-3">
				<div class="font-bold">Qualifikationsphase</div>
				<div class="grid grid-cols-3 gap-4">
					<ui-select label="Sprachen" v-model="auswahl.q.sprachlich" :manager="managerQSprachlich" :removable="false" />
					<ui-select label="GWs" v-model="auswahl.q.gesellschaftswissenschaftlich" :manager="managerQGesellschaftswissenschaftlich" :removable="false" />
					<ui-select label="NWs" v-model="auswahl.q.naturwissenschaftlich" :manager="managerQNaturwissenschaftlich" :removable="false" />
				</div>
			</div>
		</div>
	</div>
</template>

<script setup lang="ts">

	import { computed, reactive, type ComputedRef } from "vue";
	import type { AbiturdatenManager } from "../../../../../core/src/core/abschluss/gost/AbiturdatenManager";
	import type { GostFach } from "../../../../../core/src/core/data/gost/GostFach";
	import { GostFachbereich } from "../../../../../core/src/core/types/gost/GostFachbereich";
	import { GostHalbjahr } from "../../../../../core/src/core/types/gost/GostHalbjahr";
	import { GostKursart } from "../../../../../core/src/core/types/gost/GostKursart";
	import { GostSchriftlichkeit } from "../../../../../core/src/core/types/gost/GostSchriftlichkeit";
	import { SelectManager } from "../../../ui/controls/select/manager/SelectManager";

	const props = defineProps<{
		abiturdatenManager: () => AbiturdatenManager;
	}>();

	const auswahl = reactive({
		ef: {
			sprachlich: null as GostFach | null,
			gesellschaftswissenschaftlich: null as GostFach | null,
			naturwissenschaftlich: null as GostFach | null,
		},
		q: {
			sprachlich: null as GostFach | null,
			gesellschaftswissenschaftlich: null as GostFach | null,
			naturwissenschaftlich: null as GostFach | null,
		},
	});

	const halbjahreEF = [GostHalbjahr.EF1, GostHalbjahr.EF2];
	const halbjahreQ = [GostHalbjahr.Q11, GostHalbjahr.Q12, GostHalbjahr.Q21, GostHalbjahr.Q22];

	const faecherEFSprachlich = computed<GostFach[]>(() => getFaecher(GostFachbereich.SPRACHLICH_LITERARISCH_KUENSTLERISCH, halbjahreEF));
	const faecherEFGesellschaftswissenschaftlich = computed<GostFach[]>(() => getFaecher(GostFachbereich.GESELLSCHAFTSWISSENSCHAFTLICH_MIT_RELIGION, halbjahreEF));
	const faecherEFNaturwissenschaftlich = computed<GostFach[]>(() => getFaecher(GostFachbereich.MATHEMATISCH_NATURWISSENSCHAFTLICH, halbjahreEF));
	const faecherQSprachlich = computed<GostFach[]>(() => getFaecher(GostFachbereich.SPRACHLICH_LITERARISCH_KUENSTLERISCH, halbjahreQ));
	const faecherQGesellschaftswissenschaftlich = computed<GostFach[]>(() => getFaecher(GostFachbereich.GESELLSCHAFTSWISSENSCHAFTLICH_MIT_RELIGION, halbjahreQ));
	const faecherQNaturwissenschaftlich = computed<GostFach[]>(() => getFaecher(GostFachbereich.MATHEMATISCH_NATURWISSENSCHAFTLICH, halbjahreQ));

	const managerEFSprachlich = getSelectManager(faecherEFSprachlich);
	const managerEFGesellschaftswissenschaftlich = getSelectManager(faecherEFGesellschaftswissenschaftlich);
	const managerEFNaturwissenschaftlich = getSelectManager(faecherEFNaturwissenschaftlich);
	const managerQSprachlich = getSelectManager(faecherQSprachlich);
	const managerQGesellschaftswissenschaftlich = getSelectManager(faecherQGesellschaftswissenschaftlich);
	const managerQNaturwissenschaftlich = getSelectManager(faecherQNaturwissenschaftlich);

	function getFaecher(bereich: GostFachbereich, halbjahre: GostHalbjahr[]): GostFach[] {
		const result: GostFach[] = [];
		const manager = props.abiturdatenManager();
		for (const fach of bereich.getFaecher()) {
			for (const gostFach of manager.faecher().getByKuerzel(fach.name())) {
				const fachbelegung = manager.getFachbelegungByID(gostFach.id);
				if (halbjahre.some(halbjahr => manager.pruefeBelegungMitSchriftlichkeitEinzeln(fachbelegung, GostSchriftlichkeit.SCHRIFTLICH, halbjahr)
					&& manager.pruefeBelegungMitKursart(fachbelegung, GostKursart.GK, halbjahr))) {
					result.push(gostFach);
				}
			}
		}
		return result.sort((a, b) => GostFachbereich.compareGostFach(a, b));
	}

	function getSelectManager(options: ComputedRef<GostFach[]>): SelectManager<GostFach> {
		return new SelectManager({
			options,
			optionDisplayText: textFach,
			selectionDisplayText: textFach,
		});
	}

	function textFach(fach: GostFach): string {
		const bezeichnung = fach.bezeichnung;
		if ((bezeichnung === null) || (bezeichnung.length === 0)) {
			return fach.kuerzel;
		}
		return bezeichnung.length <= 15 ? bezeichnung : `${bezeichnung.substring(0, 15)}...`;
	}

</script>
