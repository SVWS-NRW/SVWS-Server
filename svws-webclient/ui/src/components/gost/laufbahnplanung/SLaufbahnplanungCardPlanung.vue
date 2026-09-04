<template>
	<ui-table-grid name="Laufbahnplanung" :header-count="2" :footer-count="3" :manager="() => gridManager">
		<template #header="params">
			<template v-if="params.i === 1">
				<th class="text-left col-span-3 ui-divider">Fachwahlen</th>
				<th class="text-center col-span-2 ui-divider">Sprachen</th>
				<th class="text-center col-span-2 ui-divider">EF</th>
				<th class="text-center col-span-4 ui-divider">Qualifikationsphase</th>
				<th class="text-center">Abitur</th>
			</template>
			<template v-else>
				<th class="text-left">Kürzel</th>
				<th class="text-left">Fach</th>
				<th class="text-center ui-divider">
					<svws-ui-tooltip>
						<span>WS</span>
						<template #content>
							Wochenstunden
						</template>
					</svws-ui-tooltip>
				</th>
				<th class="text-center items-center">Folge</th>
				<th class="text-center ui-divider">
					<svws-ui-tooltip>
						<span>ab JG</span>
						<template #content>
							Ab Jahrgang
						</template>
					</svws-ui-tooltip>
				</th>
				<template v-for="halbjahr in GostHalbjahr.values()" :key="halbjahr.id">
					<th class="text-center ui-divider relative">
						{{ halbjahr.kuerzel }}
						<svws-ui-tooltip v-if="gostLaufbahnplanungState.gostJahrgangsdaten.anzahlKursblockungen[halbjahr.id] > 0">
							<span @click.stop="gostLaufbahnplanungState.gotoKursplanung(halbjahr)" class="cursor-pointer absolute right-1 top-1/2 -translate-y-1/2">
								<span class="icon-sm i-ri-link" />
							</span>
							<template #content>
								Zur {{ halbjahr.kuerzel }}-Kursblockung
							</template>
						</svws-ui-tooltip>
					</th>
				</template>
				<th class="text-center">Fach</th>
			</template>
		</template>
		<template #default="{ row: fach, index: rowIndex }">
			<s-laufbahnplanung-fach :grid-manager :manager :fach :hat-update-kompetenz :row-index />
		</template>
		<template #footer="params">
			<template v-if="params.i === 1">
				<td class="font-bold text-right col-span-5 ui-divider leading-none p-0">
					<span>Kurse</span>
					<svws-ui-tooltip>
						<span class="icon-sm i-ri-question-line mx-1" />
						<template #content>
							{{ manager.getTooltipAnrechenbareKurse() }}
						</template>
					</svws-ui-tooltip>
				</td>
				<td class="text-center leading-none" v-for="hj in GostHalbjahr.values()" :key="hj.id" :class="{ 'ui-divider': (hj.id === 1 || hj.id === 5) }">
					<span class="inline-flex justify-center font-bold rounded-sm w-full h-fit mt-0.5 pb-0.5" :class="manager.getAnrechenbareKurseCSS(hj)">
						{{ manager.getAnrechenbareKurse(hj) }}
					</span>
				</td>
				<td class="text-center leading-none">
					<span class="inline-flex justify-center font-bold rounded-sm w-full my-0.5 pb-0.5" :class="manager.getSummeAnrechenbareKurseCSS()">
						{{ manager.summeAnrechenbareKurse }}
					</span>
				</td>
			</template>
			<template v-else-if="params.i === 2">
				<td class="font-bold text-right col-span-5 ui-divider leading-none p-0">
					<span>Wochenstunden</span>
					<svws-ui-tooltip>
						<span class="icon-sm i-ri-question-line mx-1" />
						<template #content>
							{{ manager.getTooltipWochenstunden() }}
							Die Anzahl der Wochenstunden. Pro Halbjahr sollten etwa <strong>33—36</strong> Wochenstunden gewählt werden.
						</template>
					</svws-ui-tooltip>
				</td>
				<td class="text-center leading-none" v-for="hj in GostHalbjahr.values()" :key="hj.id" :class="{ 'ui-divider': ((hj.id === 1) || (hj.id === 5)) }">
					<span class="inline-flex justify-center font-bold rounded-sm w-full my-0.5 pb-0.5" :class="manager.getWochenstundenCSS(hj)">
						{{ manager.getWochenstunden(hj) }}
					</span>
				</td>
				<td class="text-center leading-none">
					<span class="inline-flex justify-center font-bold rounded-sm w-full my-0.5 pb-0.5" :class="manager.getWochenstundenJahressummeCSS()">
						{{ manager.wochenstundenJahressumme }}
					</span>
				</td>
			</template>
			<template v-else-if="manager.zeigeWochenstundenDurchschnitt()">
				<td class="font-bold text-right col-span-5 ui-divider leading-none p-0">
					<span>Durchschnitt</span>
					<svws-ui-tooltip>
						<span class="icon-sm i-ri-question-line mx-1" />
						<template #content>
							In der EF und Qualifikationsphase sollten jeweils im Durchschnitt <strong>34—36</strong> Wochenstunden erreicht werden.
						</template>
					</svws-ui-tooltip>
				</td>
				<td class="text-center items-center! col-span-2 ui-divider leading-none">
					<span class="inline-flex justify-center font-bold rounded-sm w-full my-0.5 pb-0.5"
						:class="manager.getWochenstundenDurchschnittEFCSS()">
						{{ manager.wochenstundenDurchschnittEF }}
					</span>
				</td>
				<td class="text-center items-center! col-span-4 ui-divider leading-none">
					<span class="inline-flex justify-center font-bold rounded-sm w-full my-0.5 pb-0.5"
						:class="manager.getWochenstundenDurchschnittQCSS()">
						{{ manager.wochenstundenDurchschnittQ }}
					</span>
				</td>
				<td class="text-center leading-none">
					<svws-ui-tooltip>
						<span class="icon-sm i-ri-information-line m-0.5" />
						<template #content>
							<div class="flex flex-col gap-0.5 text-center">
								<span class="flex gap-1 items-center">
									<span class="w-4 h-4 rounded-full svws-ergebnis--not-enough" />
									<span>Zu wenig</span>
								</span>
								<span class="flex gap-1 items-center">
									<span class="w-4 h-4 rounded-full svws-ergebnis--low" />
									<span>Ausreichend</span>
								</span>
								<span class="flex gap-1 items-center">
									<span class="w-4 h-4 rounded-full svws-ergebnis--good" />
									<span>Optimale Anzahl</span>
								</span>
								<span class="flex gap-1 items-center">
									<span class="w-4 h-4 rounded-full svws-ergebnis--more" />
									<span>Mehr als erforderlich</span>
								</span>
							</div>
						</template>
					</svws-ui-tooltip>
				</td>
			</template>
		</template>
	</ui-table-grid>
</template>

<script setup lang="ts">

	import type { GostFach } from "@core/core/data/gost/GostFach";
	import type { Collection } from "@core/java/util/Collection";
	import { useGostLaufbahnplanungState } from "@ui/states/GostLaufbahnplanungState";
	import { GridManager } from "@ui/ui/controls/tablegrid/GridManager";
	import { computed } from "vue";
	import type { LaufbahnplanungUiManager } from "./LaufbahnplanungUiManager";
	import { GostHalbjahr } from "@core/core/types/gost/GostHalbjahr";

	const props = withDefaults(defineProps<{
		manager: LaufbahnplanungUiManager;
		title?: string | undefined;
		hatUpdateKompetenz?: boolean;
	}>(), {
		title: undefined,
		hatUpdateKompetenz: true,
	});

	const gostLaufbahnplanungState = useGostLaufbahnplanungState();

	const gridManager = new GridManager<string, GostFach, Collection<GostFach>>({
		daten: computed<Collection<GostFach>>(() => {
			return props.manager.faecherGefiltert;
		}),
		getRowKey: row => `${row.id}`,
		columns: [
			{ kuerzel: "Kürzel", name: "Kürzel", width: "5rem", hideable: false },
			{ kuerzel: "Bezeichnung", name: "Bezeichnung", width: "16rem", hideable: false },
			{ kuerzel: "WS", name: "Wochenstunden", width: "3rem", hideable: false },
			{ kuerzel: "Folge", name: "Folge", width: "4.5rem", hideable: false },
			{ kuerzel: "ab Jg", name: "ab Jg", width: "4.5rem", hideable: false },
			{ kuerzel: "EF.1", name: "EF.1", width: "4.5rem", hideable: false },
			{ kuerzel: "EF.2", name: "EF.2", width: "4.5rem", hideable: false },
			{ kuerzel: "Q1.1", name: "Q1.1", width: "4.5rem", hideable: false },
			{ kuerzel: "Q1.2", name: "Q1.2", width: "4.5rem", hideable: false },
			{ kuerzel: "Q2.1", name: "Q2.1", width: "4.5rem", hideable: false },
			{ kuerzel: "Q2.2", name: "Q2.2", width: "4.5rem", hideable: false },
			{ kuerzel: "Abiturfach", name: "Abiturfach", width: "4.5rem", hideable: false },
		],
	});

</script>

<style scoped>

	.svws-ui-tfoot--data {
		.tooltip-trigger {
			margin: -0.5rem;
		}
	}

	.svws-ergebnis--not-enough {
		background-color: var(--color-bg-ui-danger);
		color: var(--color-text-ui-ondanger);
	}

	.svws-ergebnis--low {
		background-color: var(--color-bg-ui-warning);
		color: var(--color-text-ui-onwarning);
	}

	.svws-ergebnis--good {
		background-color: var(--color-bg-ui-success-secondary);
		color: var(--color-text-uistatic);
	}

	.svws-ergebnis--more {
		background-color: var(--color-bg-ui-success);
		color: var(--color-text-ui-onsuccess);
	}

</style>
