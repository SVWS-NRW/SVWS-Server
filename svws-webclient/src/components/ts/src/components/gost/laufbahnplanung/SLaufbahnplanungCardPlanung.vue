<template>
	<svws-ui-content-card :title="title" class="table--with-background sticky top-8">
		<svws-ui-data-table :items="abiturdatenManager().faecher().faecher()" :columns="cols" panel-height overflow-x-hidden>
			<template #header>
				<div role="row" class="data-table__tr data-table__thead__tr data-table__thead__tr__compact" :class="{'text-error': manuellerModus}" :title="manuellerModus ? 'Manueller Modus aktiviert' : ''">
					<div role="columnheader" class="data-table__th data-table__thead__th data-table__th__align-center col-span-3 data-table__th__separate">
						<div class="data-table__th-wrapper">
							<div class="data-table__th-title">
								Fach
							</div>
						</div>
					</div>
					<div role="columnheader" class="data-table__th data-table__thead__th data-table__th__align-center col-span-2 data-table__th__separate">
						<div class="data-table__th-wrapper">
							<div class="data-table__th-title">
								Sprachen
							</div>
						</div>
					</div>
					<div role="columnheader" class="data-table__th data-table__thead__th data-table__th__align-center col-span-2 data-table__th__separate">
						<div class="data-table__th-wrapper">
							<div class="data-table__th-title">
								EF
							</div>
						</div>
					</div>
					<div role="columnheader" class="data-table__th data-table__thead__th data-table__th__align-center col-span-4 data-table__th__separate">
						<div class="data-table__th-wrapper">
							<div class="data-table__th-title">
								Qualifikationsphase
							</div>
						</div>
					</div>
					<div role="columnheader" class="data-table__th data-table__thead__th data-table__th__align-center">
						<div class="data-table__th-wrapper">
							<div class="data-table__th-title">
								Abitur
							</div>
						</div>
					</div>
				</div>
				<div role="row" class="data-table__tr data-table__thead__tr data-table__thead__tr__compact" :class="{'text-error': manuellerModus}" :title="manuellerModus ? 'Manueller Modus aktiviert' : ''">
					<svws-ui-table-cell thead>
						Kürzel
					</svws-ui-table-cell>
					<svws-ui-table-cell thead>
						Bezeichnung
					</svws-ui-table-cell>
					<svws-ui-table-cell thead separate align="center" tooltip="Wochenstunden">
						WS
					</svws-ui-table-cell>
					<svws-ui-table-cell thead align="center">
						Folge
					</svws-ui-table-cell>
					<svws-ui-table-cell thead align="center" separate tooltip="Ab Jahrgang">
						ab Jg
					</svws-ui-table-cell>
					<svws-ui-table-cell thead align="center">
						EF.1
					</svws-ui-table-cell>
					<svws-ui-table-cell thead align="center" separate>
						EF.2
					</svws-ui-table-cell>
					<svws-ui-table-cell thead align="center">
						Q1.1
					</svws-ui-table-cell>
					<svws-ui-table-cell thead align="center">
						Q1.2
					</svws-ui-table-cell>
					<svws-ui-table-cell thead align="center">
						Q2.1
					</svws-ui-table-cell>
					<svws-ui-table-cell thead align="center" separate>
						Q2.2
					</svws-ui-table-cell>
					<svws-ui-table-cell thead align="center">
						Fach
					</svws-ui-table-cell>
				</div>
			</template>
			<template #body>
				<template v-for="row in abiturdatenManager().faecher().faecher()" :key="row.id">
					<s-laufbahnplanung-fach :abiturdaten-manager="abiturdatenManager" :gost-jahrgangsdaten="gostJahrgangsdaten"
						:fach="row" :manueller-modus="manuellerModus" @update:wahl="onUpdateWahl" :ignoriere-sprachenfolge="ignoriereSprachenfolge" />
				</template>
			</template>
			<template #footer>
				<div role="row" class="data-table__tr data-table__tbody__tr data-table__thead__tr__compact">
					<div role="rowheader" class="data-table__th font-bold data-table__thead__th data-table__th__align-right col-span-5 data-table__th__separate gap-1">
						<span>Anrechenbare Kurse</span>
						<svws-ui-tooltip>
							<i-ri-information-line class="text-black/50" />
							<template #content>
								Pro Halbjahr sollten <strong>10–11</strong> Kurse gewählt werden. Insgesamt sind <strong>33–36</strong> Kurse als Summe aus der Qualifikationsphase optimal.
							</template>
						</svws-ui-tooltip>
					</div>
					<div role="cell" class="data-table__td col-laufbahnplanung-ergebnis data-table__td__align-center" v-for="(jahrgang, i) in kurszahlen" :key="i" :class="{'data-table__th__separate': (i === 1 || i === 5)}">
						<span class="inline-flex justify-center items-center font-bold py-0.5 px-1.5 rounded-md w-full h-full"
							:class="{
								'bg-amber-400': jahrgang < 10,
								'bg-lime-500': jahrgang > 9 && jahrgang < 12,
								'bg-lime-700 text-white': jahrgang > 11
							}">
							{{ jahrgang }}
						</span>
					</div>
					<div role="cell" class="data-table__td col-laufbahnplanung-ergebnis data-table__td__align-center data-table__th__separate">
						<span class="inline-flex justify-center items-center font-bold py-0.5 px-1.5 rounded-md w-full h-full"
							:class="{
								'bg-rose-700 text-white': kurse_summe < 30,
								'bg-amber-400': kurse_summe >= 31 && kurse_summe <= 32,
								'bg-lime-500': kurse_summe > 32 && kurse_summe < 37,
								'bg-lime-700 text-white': kurse_summe > 36
							}">
							{{ kurse_summe }}
						</span>
					</div>
				</div>
				<div role="row" class="data-table__tr data-table__tbody__tr data-table__thead__tr__compact">
					<div role="rowheader" class="data-table__th font-bold data-table__thead__th data-table__th__align-right col-span-5 data-table__th__separate gap-1">
						<span>Wochenstunden</span>
						<svws-ui-tooltip>
							<i-ri-information-line class="text-black/50" />
							<template #content>
								Pro Halbjahr sollten <strong>33–36</strong> Wochenstunden erreicht werden. Insgesamt sind <strong>102–108</strong> Wochenstunden als Summe aus der Qualifikationsphase optimal.
							</template>
						</svws-ui-tooltip>
					</div>
					<div role="cell" class="data-table__td col-laufbahnplanung-ergebnis data-table__td__align-center" v-for="(jahrgang, i) in wochenstunden" :key="i" :class="{'data-table__th__separate': (i === 1 || i === 5)}">
						<span class="inline-flex justify-center items-center font-bold py-0.5 px-1.5 rounded-md w-full h-full"
							:class="{
								'bg-rose-700 text-white': jahrgang < 30,
								'bg-amber-400': jahrgang >= 31 && jahrgang <= 32,
								'bg-lime-500': jahrgang > 32 && jahrgang < 37,
								'bg-lime-700 text-white': jahrgang > 36
							}">
							{{ jahrgang }}
						</span>
					</div>
					<div role="cell" class="data-table__td col-laufbahnplanung-ergebnis data-table__td__align-center">
						<span class="inline-flex justify-center items-center font-bold py-0.5 px-1.5 rounded-md w-full h-full"
							:class="{
								'bg-rose-700 text-white': wst_summe < 100,
								'bg-amber-400': wst_summe >= 100 && wst_summe < 102,
								'bg-lime-500': wst_summe >= 102 && wst_summe <= 108,
								'bg-lime-700 text-white': wst_summe > 108
							}">
							{{ wst_summe }}
						</span>
					</div>
				</div>
				<div role="row" class="data-table__tr data-table__tbody__tr data-table__thead__tr__compact">
					<div role="rowheader" class="data-table__th font-bold data-table__thead__th data-table__th__align-right col-span-5 data-table__th__separate gap-1">
						<span>Durchschnitt</span>
						<svws-ui-tooltip>
							<i-ri-information-line class="text-black/50" />
							<template #content>
								In der EF und Qualifikationsphase sollten jeweils im Durchschnitt <strong>34–36</strong> Wochenstunden erreicht werden.
							</template>
						</svws-ui-tooltip>
					</div>
					<div role="cell" class="data-table__td col-laufbahnplanung-ergebnis data-table__td__align-center col-span-2 data-table__th__separate">
						<span class="inline-flex justify-center items-center font-bold py-0.5 px-1.5 rounded-md w-full h-full"
							:class="{
								'bg-rose-700 text-white': wst_d_ef < 34,
								'bg-lime-500': wst_d_ef >= 34 && wst_d_ef < 37,
								'bg-lime-700 text-white': wst_d_ef >= 37
							}">
							{{ wst_d_ef }}
						</span>
					</div>
					<div role="cell" class="data-table__td col-laufbahnplanung-ergebnis data-table__td__align-center col-span-4 data-table__th__separate">
						<span class="inline-flex justify-center items-center font-bold py-0.5 px-1.5 rounded-md w-full h-full"
							:class="{
								'bg-rose-700 text-white': wst_d_q < 34,
								'bg-lime-500': wst_d_q >= 34 && wst_d_q < 37,
								'bg-lime-700 text-white': wst_d_q >= 37
							}">
							{{ wst_d_q }}
						</span>
					</div>
					<div role="cell" class="data-table__td col-laufbahnplanung-ergebnis data-table__td__align-center">
						<svws-ui-tooltip>
							<i-ri-information-line class="text-headline-md text-black/50" />
							<template #content>
								<div class="flex flex-col gap-0.5 text-center">
									<span class="flex gap-1 items-center">
										<span class="w-4 h-4 rounded-full bg-rose-700" />
										<span>Zu wenig</span>
									</span>
									<span class="flex gap-1 items-center">
										<span class="w-4 h-4 rounded-full bg-amber-400" />
										<span>Ausreichend</span>
									</span>
									<span class="flex gap-1 items-center">
										<span class="w-4 h-4 rounded-full bg-lime-500" />
										<span>Optimale Anzahl</span>
									</span>
									<span class="flex gap-1 items-center">
										<span class="w-4 h-4 rounded-full bg-lime-700" />
										<span>Mehr als erforderlich</span>
									</span>
								</div>
							</template>
						</svws-ui-tooltip>
					</div>
				</div>
			</template>
		</svws-ui-data-table>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type { ComputedRef } from "vue";
	import {computed } from "vue";

	import type { AbiturdatenManager, GostSchuelerFachwahl, GostJahrgangsdaten } from "@core";
	import type { DataTableColumn } from "@svws-nrw/svws-ui";

	const props = withDefaults(defineProps<{
		abiturdatenManager: () => AbiturdatenManager;
		gostJahrgangsdaten: GostJahrgangsdaten;
		setWahl: (fachID: number, wahl: GostSchuelerFachwahl) => Promise<void>;
		manuellerModus: boolean;
		ignoriereSprachenfolge? : boolean;
		title?: string | undefined;
	}>(), {
		title: undefined,
		ignoriereSprachenfolge: false,
	});

	async function onUpdateWahl(fachID: number, wahl: GostSchuelerFachwahl) {
		await props.setWahl(fachID, wahl);
	}

	const kurszahlen: ComputedRef<number[]> = computed(() => props.abiturdatenManager().getAnrechenbareKurse());

	const kurse_summe: ComputedRef<number> = computed(() => {
		const k = kurszahlen.value;
		return k[2] + k[3] + k[4] + k[5];
	});

	const wochenstunden: ComputedRef<number[]> = computed(() => props.abiturdatenManager().getWochenstunden());

	const wst_summe: ComputedRef<number> = computed(() => wochenstunden.value.reduce((p, c) => p + c, 0) / 2);

	const wst_d_ef: ComputedRef<number> = computed(() => (wochenstunden.value[0] + wochenstunden.value[1]) / 2);

	const wst_d_q: ComputedRef<number> = computed(() => {
		const [e, f, ...q] = wochenstunden.value;
		return q.reduce((p, c) => p + c, 0) / 4;
	});

	const cols: Array<DataTableColumn> = [
		{ key: "kuerzel", label: "Kürzel", align: 'center', minWidth: 5, span: 0.75},
		{ key: "bezeichnung", label: "Bezeichnung", align: 'center', span: 3, minWidth: 12 },
		{ key: "wstd", label: "WS", tooltip: "Wochenstunden", align: 'center', fixedWidth: 3 },
		{ key: "folge", label: "Folge", align: 'center', fixedWidth: 4.5 },
		{ key: "ab_jg", label: "ab Jg", align: 'center', fixedWidth: 4.5 },
		{ key: "ef1", label: "EF.1", align: 'center', fixedWidth: 4.5 },
		{ key: "ef2", label: "EF.2", align: 'center', fixedWidth: 4.5 },
		{ key: "q1_1", label: "Q1.1", align: 'center', fixedWidth: 4.5 },
		{ key: "q1_2", label: "Q1.2", align: 'center', fixedWidth: 4.5 },
		{ key: "q2_1", label: "Q2.1", align: 'center', fixedWidth: 4.5 },
		{ key: "q2_2", label: "Q2.2", align: 'center', fixedWidth: 4.5 },
		{ key: "abiturfach", label: "Abiturfach", align: 'center', fixedWidth: 4.5 }
	];

</script>

<style scoped lang="postcss">
.col-laufbahnplanung-ergebnis {
	padding: 0.175rem;
}
</style>
