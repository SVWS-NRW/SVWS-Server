<template>
	<svws-ui-content-card class="table--with-background sticky top-8">
		<Teleport to=".router-tab-bar--subnav-target" v-if="isMounted">
			<svws-ui-sub-nav>
				<svws-ui-button size="small" type="transparent" @click.prevent="download_file" title="Wahlbogen herunterladen">Wahlbogen herunterladen</svws-ui-button>
				<svws-ui-button size="small" type="transparent" title="Planung exportieren" @click="export_laufbahnplanung">Exportieren <i-ri-upload-2-line /></svws-ui-button>
				<s-laufbahnplanung-import-modal :import-laufbahnplanung="importLaufbahnplanung" v-slot="{openModal}">
					<svws-ui-button size="small" type="transparent" title="Planung importieren" @click="openModal">Importieren…<i-ri-download-2-line /></svws-ui-button>
				</s-laufbahnplanung-import-modal>
				<svws-ui-button size="small" :type="zwischenspeicher === undefined ? 'transparent' : 'error'" title="Planung merken" @click="saveLaufbahnplanung">Planung merken</svws-ui-button>
				<svws-ui-button size="small" type="danger" title="Planung merken" @click="restoreLaufbahnplanung" v-if="zwischenspeicher !== undefined">Planung wiederherstellen</svws-ui-button>
				<svws-ui-button size="small" :type="istManuellerModus ? 'error' : 'transparent'" @click="switchManuellerModus" :title="istManuellerModus ? 'Manuellen Modus deaktivieren' : 'Manuellen Modus aktivieren'">
					Manueller Modus
					<template v-if="istManuellerModus">
						<svg width="1.2em" height="1.2em" fill="currentColor" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24"><path d="M3 16V5.75C3 5.06 3.56 4.5 4.25 4.5S5.5 5.06 5.5 5.75V12H6.5V2.75C6.5 2.06 7.06 1.5 7.75 1.5C8.44 1.5 9 2.06 9 2.75V12H10V1.25C10 .56 10.56 0 11.25 0S12.5 .56 12.5 1.25V12H13.5V3.25C13.5 2.56 14.06 2 14.75 2S16 2.56 16 3.25V15H16.75L18.16 11.47C18.38 10.92 18.84 10.5 19.4 10.31L20.19 10.05C21 9.79 21.74 10.58 21.43 11.37L18.4 19C17.19 22 14.26 24 11 24C6.58 24 3 20.42 3 16Z" /></svg>
					</template>
				</svws-ui-button>
				<s-modal-laufbahnplanung-kurswahlen-loeschen @delete="reset_fachwahlen" />
				<svws-ui-modal-hilfe class="ml-auto"> <hilfe-laufbahnplanung /> </svws-ui-modal-hilfe>
			</svws-ui-sub-nav>
		</Teleport>
		<svws-ui-data-table :items="abiturdatenManager().faecher().faecher()"
			:columns="cols" panel-height overflow-x-hidden>
			<template #header>
				<div role="row" class="data-table__tr data-table__thead__tr data-table__thead__tr__compact" :class="{'text-error': istManuellerModus}" :title="istManuellerModus ? 'Manueller Modus aktiviert' : ''">
					<div role="columnheader"
						class="data-table__th data-table__thead__th data-table__th__align-center col-span-3 data-table__th__separate">
						<div class="data-table__th-wrapper">
							<div class="data-table__th-title">
								Fach
							</div>
						</div>
					</div>
					<div role="columnheader"
						class="data-table__th data-table__thead__th data-table__th__align-center col-span-2 data-table__th__separate">
						<div class="data-table__th-wrapper">
							<div class="data-table__th-title">
								Sprachen
							</div>
						</div>
					</div>
					<div role="columnheader"
						class="data-table__th data-table__thead__th data-table__th__align-center col-span-2 data-table__th__separate">
						<div class="data-table__th-wrapper">
							<div class="data-table__th-title">
								EF
							</div>
						</div>
					</div>
					<div role="columnheader"
						class="data-table__th data-table__thead__th data-table__th__align-center col-span-4 data-table__th__separate">
						<div class="data-table__th-wrapper">
							<div class="data-table__th-title">
								Qualifikationsphase
							</div>
						</div>
					</div>
					<div role="columnheader"
						class="data-table__th data-table__thead__th data-table__th__align-center">
						<div class="data-table__th-wrapper">
							<div class="data-table__th-title">
								Abitur
							</div>
						</div>
					</div>
				</div>
				<div role="row" class="data-table__tr data-table__thead__tr data-table__thead__tr__compact" :class="{'text-error': istManuellerModus}" :title="istManuellerModus ? 'Manueller Modus aktiviert' : ''">
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
						:fach="row" :manueller-modus="istManuellerModus" @update:wahl="onUpdateWahl" />
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
	import {computed, onMounted, ref} from "vue";

	import type { SchuelerListeEintrag, AbiturdatenManager, GostSchuelerFachwahl, GostJahrgangsdaten, GostLaufbahnplanungDaten } from "@core";
	import { GostHalbjahr } from "@core";
	import type { DataTableColumn } from "@svws-nrw/svws-ui";

	const props = defineProps<{
		abiturdatenManager: () => AbiturdatenManager;
		gostJahrgangsdaten: GostJahrgangsdaten;
		setWahl: (fachID: number, wahl: GostSchuelerFachwahl) => Promise<void>;
		getPdfWahlbogen: () => Promise<Blob>;
		exportLaufbahnplanung: () => Promise<Blob>;
		importLaufbahnplanung: (data: FormData) => Promise<boolean>;
		item: SchuelerListeEintrag;
		zwischenspeicher?: GostLaufbahnplanungDaten;
		saveLaufbahnplanung: () => Promise<void>;
		restoreLaufbahnplanung: () => Promise<void>;
	}>();

	async function onUpdateWahl(fachID: number, wahl: GostSchuelerFachwahl) {
		await props.setWahl(fachID, wahl);
	}

	async function reset_fachwahlen() {
		for (const fachbelegung of props.abiturdatenManager().getFachbelegungen()) {
			const fach = props.abiturdatenManager().getFach(fachbelegung);
			if (fach) {
				const fachwahl = props.abiturdatenManager().getSchuelerFachwahl(fach.id);
				for (const hj of GostHalbjahr.values()) {
					if (!props.abiturdatenManager().istBewertet(hj))
						fachwahl.halbjahre[hj.id] = null;
				}
				fachwahl.abiturFach = null;
				await onUpdateWahl(fach.id, fachwahl);
			}
		}
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

	const istManuellerModus = ref(false)
	function switchManuellerModus() {
		istManuellerModus.value = istManuellerModus.value ? false : true;
	}

	async function download_file() {
		const pdf = await props.getPdfWahlbogen();
		const link = document.createElement("a");
		link.href = URL.createObjectURL(pdf);
		link.download = "Laufbahnplanung_" + props.gostJahrgangsdaten.abiturjahr + "_" + props.gostJahrgangsdaten.jahrgang + "_"
			+ props.item.nachname + "_" + props.item.vorname + "-" + props.item.id + ".pdf";
		link.target = "_blank";
		link.click();
		URL.revokeObjectURL(link.href);
	}

	async function export_laufbahnplanung() {
		const gzip = await props.exportLaufbahnplanung();
		const link = document.createElement("a");
		link.href = URL.createObjectURL(gzip);
		link.download = "Laufbahnplanung_" + props.gostJahrgangsdaten.abiturjahr + "_" + props.gostJahrgangsdaten.jahrgang + "_"
			+ props.item.nachname + "_" + props.item.vorname + "-" + props.item.id + ".lp";
		link.target = "_blank";
		link.click();
		URL.revokeObjectURL(link.href);
	}

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

	// Check if component is mounted
	const isMounted = ref(false);
	onMounted(() => {
		isMounted.value = true;
	});

</script>

<style scoped lang="postcss">
.col-laufbahnplanung-ergebnis {
	padding: 0.175rem;
}
</style>
