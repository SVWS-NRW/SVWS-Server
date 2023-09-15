<template>
	<svws-ui-content-card :title="title">
		<svws-ui-table :items="abiturdatenManager().faecher().faecher()" :columns="cols" has-background>
			<template #header>
				<div role="row" class="svws-ui-tr">
					<div role="columnheader" class="svws-ui-td col-span-3 svws-divider">
						Fachwahlen
					</div>
					<div role="columnheader" class="svws-ui-td svws-align-center col-span-2 svws-divider">
						Sprachen
					</div>
					<div role="columnheader" class="svws-ui-td svws-align-center col-span-2 svws-divider">
						EF
					</div>
					<div role="columnheader" class="svws-ui-td svws-align-center col-span-4 svws-divider">
						Qualifikationsphase
					</div>
					<div role="columnheader" class="svws-ui-td svws-align-center">
						Abitur
					</div>
				</div>
				<div role="row" class="svws-ui-tr">
					<div class="svws-ui-td">
						Kürzel
					</div>
					<div class="svws-ui-td">
						Fach
					</div>
					<div class="svws-ui-td svws-align-center svws-divider">
						<svws-ui-tooltip>
							<span>WS</span>
							<template #content>
								Wochenstunden
							</template>
						</svws-ui-tooltip>
					</div>
					<div class="svws-ui-td svws-align-center svws-no-padding">
						Folge
					</div>
					<div class="svws-ui-td svws-align-center svws-divider svws-no-padding">
						<svws-ui-tooltip>
							<span>ab JG</span>
							<template #content>
								Ab Jahrgang
							</template>
						</svws-ui-tooltip>
					</div>
					<div class="svws-ui-td svws-align-center svws-divider svws-no-padding">
						EF.1
					</div>
					<div class="svws-ui-td svws-align-center svws-divider svws-no-padding">
						EF.2
					</div>
					<div class="svws-ui-td svws-align-center svws-divider svws-no-padding">
						Q1.1
					</div>
					<div class="svws-ui-td svws-align-center svws-divider svws-no-padding">
						Q1.2
					</div>
					<div class="svws-ui-td svws-align-center svws-divider svws-no-padding">
						Q2.1
					</div>
					<div class="svws-ui-td svws-align-center svws-divider svws-no-padding">
						Q2.2
					</div>
					<div class="svws-ui-td svws-align-center svws-no-padding">
						Fach
					</div>
				</div>
			</template>
			<template #body>
				<template v-for="row in abiturdatenManager().faecher().faecher()" :key="row.id">
					<s-laufbahnplanung-fach :abiturdaten-manager="abiturdatenManager" :gost-jahrgangsdaten="gostJahrgangsdaten"
						:fach="row" :modus="modus" @update:wahl="onUpdateWahl" :ignoriere-sprachenfolge="ignoriereSprachenfolge" />
				</template>
			</template>
			<template #dataFooter>
				<div role="row" class="svws-ui-tr">
					<div role="rowheader" class="svws-ui-td font-bold svws-align-right col-span-5 svws-divider gap-1">
						<span>Anrechenbare Kurse</span>
						<svws-ui-tooltip>
							<i-ri-question-line class="-m-0.5 mx-0.5" />
							<template #content>
								Pro Halbjahr sollten <strong>10–11</strong> Kurse gewählt werden. Insgesamt sind <strong>33–36</strong> Kurse als Summe aus der Qualifikationsphase optimal.
							</template>
						</svws-ui-tooltip>
					</div>
					<div role="cell" class="svws-ui-td svws-align-center svws-no-padding" v-for="(jahrgang, i) in kurszahlen" :key="i" :class="{'svws-divider': (i === 1 || i === 5)}">
						<span class="svws-ergebnis-badge"
							:class="{
								'svws-ergebnis--not-enough': jahrgang < 9,
								'svws-ergebnis--low': jahrgang < 10 && jahrgang > 8,
								'svws-ergebnis--good': jahrgang > 9 && jahrgang < 12,
								'svws-ergebnis--more': jahrgang > 11
							}">
							{{ jahrgang }}
						</span>
					</div>
					<div role="cell" class="svws-ui-td svws-align-center svws-no-padding">
						<span class="svws-ergebnis-badge"
							:class="{
								'svws-ergebnis--not-enough': kurse_summe < 30,
								'svws-ergebnis--low': kurse_summe >= 31 && kurse_summe <= 32,
								'svws-ergebnis--good': kurse_summe > 32 && kurse_summe < 37,
								'svws-ergebnis--more': kurse_summe > 36
							}">
							{{ kurse_summe }}
						</span>
					</div>
				</div>
				<div role="row" class="svws-ui-tr">
					<div role="rowheader" class="svws-ui-td font-bold svws-align-right col-span-5 svws-divider">
						<span>Wochenstunden</span>
						<svws-ui-tooltip>
							<i-ri-question-line class="-m-0.5 mx-0.5" />
							<template #content>
								Pro Halbjahr sollten <strong>33–36</strong> Wochenstunden erreicht werden. Insgesamt sind <strong>102–108</strong> Wochenstunden als Summe aus der Qualifikationsphase optimal.
							</template>
						</svws-ui-tooltip>
					</div>
					<div role="cell" class="svws-ui-td svws-align-center svws-no-padding" v-for="(jahrgang, i) in wochenstunden" :key="i" :class="{'svws-divider': (i === 1 || i === 5)}">
						<span class="svws-ergebnis-badge"
							:class="{
								'svws-ergebnis--not-enough': jahrgang < 30,
								'svws-ergebnis--low': jahrgang >= 31 && jahrgang <= 32,
								'svws-ergebnis--good': jahrgang > 32 && jahrgang < 37,
								'svws-ergebnis--more': jahrgang > 36
							}">
							{{ jahrgang }}
						</span>
					</div>
					<div role="cell" class="svws-ui-td svws-align-center svws-no-padding">
						<span class="svws-ergebnis-badge"
							:class="{
								'svws-ergebnis--not-enough': wst_summe < 100,
								'svws-ergebnis--low': wst_summe >= 100 && wst_summe < 102,
								'svws-ergebnis--good': wst_summe >= 102 && wst_summe <= 108,
								'svws-ergebnis--more': wst_summe > 108
							}">
							{{ wst_summe }}
						</span>
					</div>
				</div>
				<div role="row" class="svws-ui-tr">
					<div role="rowheader" class="svws-ui-td font-bold svws-align-right col-span-5 svws-divider">
						<span>Durchschnitt</span>
						<svws-ui-tooltip>
							<i-ri-question-line class="-m-0.5 mx-0.5" />
							<template #content>
								In der EF und Qualifikationsphase sollten jeweils im Durchschnitt <strong>34–36</strong> Wochenstunden erreicht werden.
							</template>
						</svws-ui-tooltip>
					</div>
					<div role="cell" class="svws-ui-td svws-align-center svws-no-padding col-span-2 svws-divider">
						<span class="svws-ergebnis-badge"
							:class="{
								'svws-ergebnis--not-enough': wst_d_ef < 34,
								'svws-ergebnis--good': wst_d_ef >= 34 && wst_d_ef < 37,
								'svws-ergebnis--more': wst_d_ef >= 37
							}">
							{{ wst_d_ef }}
						</span>
					</div>
					<div role="cell" class="svws-ui-td svws-align-center svws-no-padding col-span-4 svws-divider">
						<span class="svws-ergebnis-badge"
							:class="{
								'svws-ergebnis--not-enough': wst_d_q < 34,
								'svws-ergebnis--good': wst_d_q >= 34 && wst_d_q < 37,
								'svws-ergebnis--more': wst_d_q >= 37
							}">
							{{ wst_d_q }}
						</span>
					</div>
					<div role="cell" class="svws-ui-td svws-align-center">
						<svws-ui-tooltip>
							<i-ri-information-line class="-m-0.5" />
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
					</div>
				</div>
			</template>
		</svws-ui-table>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type { ComputedRef } from "vue";
	import {computed } from "vue";

	import type { AbiturdatenManager, GostSchuelerFachwahl, GostJahrgangsdaten } from "@core";
	import type { DataTableColumn } from "@ui";

	const props = withDefaults(defineProps<{
		abiturdatenManager: () => AbiturdatenManager;
		gostJahrgangsdaten: GostJahrgangsdaten;
		setWahl: (fachID: number, wahl: GostSchuelerFachwahl) => Promise<void>;
		modus?: 'normal' | 'manuell' | 'hochschreiben';
		ignoriereSprachenfolge? : boolean;
		title?: string | undefined;
	}>(), {
		title: undefined,
		modus: 'normal',
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

<style lang="postcss" scoped>
.svws-ergebnis-badge {
  @apply inline-flex justify-center items-center font-bold py-0.5 px-1.5 rounded-lg w-full h-full border-2 border-white dark:border-black;
}

.svws-ui-tfoot--data {
  .tooltip-trigger {
    @apply -m-2;
  }
}

.svws-ergebnis--not-enough {
  @apply bg-error text-white;
}

.svws-ergebnis--low {
  @apply bg-amber-400;
}

.svws-ergebnis--good {
  @apply bg-success;
}

.svws-ergebnis--more {
  @apply bg-lime-700 text-white;
}
</style>
