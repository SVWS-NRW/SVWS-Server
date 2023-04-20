<template>
	<svws-ui-content-card class="pt-8">
		<div class="router-tab-bar--subnav">
			<svws-ui-button size="small" type="transparent" @click.prevent="download_file" title="Wahlbogen herunterladen">Wahlbogen herunterladen</svws-ui-button>
			<svws-ui-button size="small" type="transparent" title="Planung exportieren" @click="export_laufbahnplanung">Exportieren <i-ri-upload-2-line /></svws-ui-button>
			<s-laufbahnplanung-import-modal :import-laufbahnplanung="importLaufbahnplanung" v-slot="{openModal}">
				<svws-ui-button size="small" type="transparent" title="Planung importieren" @click="openModal">Importieren <i-ri-download-2-line /></svws-ui-button>
			</s-laufbahnplanung-import-modal>
			<svws-ui-button size="small" :type="istManuellerModus ? 'error' : 'transparent'" @click="switchManuellerModus" :title="istManuellerModus ? 'Manuellen Modus deaktivieren' : 'Manuellen Modus aktivieren'">
				Manueller Modus
				<template v-if="istManuellerModus">
					<svg width="1.2em" height="1.2em" fill="currentColor" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24"><path d="M3 16V5.75C3 5.06 3.56 4.5 4.25 4.5S5.5 5.06 5.5 5.75V12H6.5V2.75C6.5 2.06 7.06 1.5 7.75 1.5C8.44 1.5 9 2.06 9 2.75V12H10V1.25C10 .56 10.56 0 11.25 0S12.5 .56 12.5 1.25V12H13.5V3.25C13.5 2.56 14.06 2 14.75 2S16 2.56 16 3.25V15H16.75L18.16 11.47C18.38 10.92 18.84 10.5 19.4 10.31L20.19 10.05C21 9.79 21.74 10.58 21.43 11.37L18.4 19C17.19 22 14.26 24 11 24C6.58 24 3 20.42 3 16Z" /></svg>
				</template>
			</svws-ui-button>
			<s-modal-laufbahnplanung-kurswahlen-loeschen @delete="reset_fachwahlen" />
			<s-modal-hilfe class="ml-auto"> <hilfe-laufbahnplanung /> </s-modal-hilfe>
		</div>
		<svws-ui-data-table :items="faechermanager.toList()"
			:columns="cols">
			<template #header>
				<div role="row" class="data-table__tr data-table__thead__tr data-table__thead__tr__compact" :class="{'text-error': istManuellerModus}" :title="istManuellerModus ? 'Manueller Modus aktiviert' : ''">
					<div role="columnheader"
						class="data-table__th data-table__thead__th data-table__th__align-center col-span-3">
						<div class="data-table__th-wrapper">
							<div class="data-table__th-title">
								Fach
							</div>
						</div>
					</div>
					<div role="columnheader"
						class="data-table__th data-table__thead__th data-table__th__align-center col-span-2">
						<div class="data-table__th-wrapper">
							<div class="data-table__th-title">
								Sprachen
							</div>
						</div>
					</div>
					<div role="columnheader"
						class="data-table__th data-table__thead__th data-table__th__align-center col-span-2">
						<div class="data-table__th-wrapper">
							<div class="data-table__th-title">
								EF
							</div>
						</div>
					</div>
					<div role="columnheader"
						class="data-table__th data-table__thead__th data-table__th__align-center col-span-4">
						<div class="data-table__th-wrapper">
							<div class="data-table__th-title">
								Qualifikationsphase
							</div>
						</div>
					</div>
					<div role="columnheader"
						class="data-table__th data-table__thead__th data-table__th__align-center" />
				</div>
				<div role="row" class="data-table__tr data-table__thead__tr data-table__thead__tr__compact" :class="{'text-error': istManuellerModus}" :title="istManuellerModus ? 'Manueller Modus aktiviert' : ''">
					<div role="columnheader"
						class="data-table__th data-table__thead__th data-table__th__align-center">
						<div class="data-table__th-wrapper">
							<div class="data-table__th-title">
								Kürzel
							</div>
						</div>
					</div>
					<div role="columnheader"
						class="data-table__th data-table__thead__th data-table__th__align-left">
						<div class="data-table__th-wrapper">
							<div class="data-table__th-title">
								Bezeichnung
							</div>
						</div>
					</div>
					<div role="columnheader"
						class="data-table__th data-table__thead__th data-table__th__align-center" title="Wochenstunden">
						<div class="data-table__th-wrapper">
							<div class="data-table__th-title">
								WStd.
							</div>
						</div>
					</div>
					<div role="columnheader"
						class="data-table__th data-table__thead__th data-table__th__align-center">
						<div class="data-table__th-wrapper">
							<div class="data-table__th-title">
								Folge
							</div>
						</div>
					</div>
					<div role="columnheader"
						class="data-table__th data-table__thead__th data-table__th__align-center">
						<div class="data-table__th-wrapper">
							<div class="data-table__th-title">
								ab Jg
							</div>
						</div>
					</div>
					<div role="columnheader"
						class="data-table__th data-table__thead__th data-table__th__align-center">
						<div class="data-table__th-wrapper">
							<div class="data-table__th-title">
								EF.1
							</div>
						</div>
					</div>
					<div role="columnheader"
						class="data-table__th data-table__thead__th data-table__th__align-center">
						<div class="data-table__th-wrapper">
							<div class="data-table__th-title">
								EF.2
							</div>
						</div>
					</div>
					<div role="columnheader"
						class="data-table__th data-table__thead__th data-table__th__align-center">
						<div class="data-table__th-wrapper">
							<div class="data-table__th-title">
								Q1.1
							</div>
						</div>
					</div>
					<div role="columnheader"
						class="data-table__th data-table__thead__th data-table__th__align-center">
						<div class="data-table__th-wrapper">
							<div class="data-table__th-title">
								Q1.2
							</div>
						</div>
					</div>
					<div role="columnheader"
						class="data-table__th data-table__thead__th data-table__th__align-center">
						<div class="data-table__th-wrapper">
							<div class="data-table__th-title">
								Q2.1
							</div>
						</div>
					</div>
					<div role="columnheader"
						class="data-table__th data-table__thead__th data-table__th__align-center">
						<div class="data-table__th-wrapper">
							<div class="data-table__th-title">
								Q2.2
							</div>
						</div>
					</div>
					<div role="columnheader"
						class="data-table__th data-table__thead__th data-table__th__align-center">
						<div class="data-table__th-wrapper">
							<div class="data-table__th-title">
								Abiturfach
							</div>
						</div>
					</div>
				</div>
			</template>
			<template #body>
				<template v-for="row in rows" :key="row.id">
					<s-laufbahnplanung-fach :abiturdaten-manager="abiturdatenManager" :faechermanager="faechermanager" :gost-jahrgangsdaten="gostJahrgangsdaten"
						:fach="row" :map-fachkombinationen="mapFachkombinationen" :manueller-modus="istManuellerModus" @update:wahl="onUpdateWahl" />
				</template>
			</template>
			<template #footer>
				<div role="row" class="data-table__tr data-table__tbody__tr data-table__thead__tr__compact">
					<div role="rowheader" class="data-table__th font-bold data-table__thead__th data-table__th__align-right col-span-5"> Anzahl Kurse </div>
					<div role="cell" class="data-table__td col-laufbahnplanung-ergebnis data-table__td__align-center" v-for="(jahrgang, i) in kurszahlen" :key="i">
						<span class="inline-flex justify-center items-center font-bold py-0.5 px-1.5 rounded-md w-full h-full"
							:class="{
								'bg-yellow-400': jahrgang < 10,
								'bg-green-300': jahrgang > 9 && jahrgang < 12,
								'bg-green-600': jahrgang > 11
							}">
							{{ jahrgang }}
						</span>
						<!--
						<i-ri-checkbox-circle-fill class="text-green-700" v-if="jahrgang > 11" />
						<i-ri-checkbox-circle-line class="text-green-300" v-else-if="jahrgang > 9" />
						<i-ri-error-warning-line class="text-yellow-400" v-else-if="jahrgang < 10" />
						-->
					</div>
					<div role="cell" class="data-table__td col-laufbahnplanung-ergebnis data-table__td__align-center">
						<span class="inline-flex justify-center items-center font-bold py-0.5 px-1.5 rounded-md w-full h-full"
							:class="{
								'bg-red-400': kurse_summe < 30,
								'bg-yellow-400': kurse_summe >= 31 && kurse_summe <= 32,
								'bg-green-300': kurse_summe > 32 && kurse_summe < 37,
								'bg-green-600': kurse_summe > 36
							}">
							{{ kurse_summe }}
						</span>
						<!--
						<i-ri-checkbox-circle-fill class="text-green-700" v-if="kurse_summe > 36" />
						<i-ri-checkbox-circle-line class="text-green-300" v-else-if="kurse_summe > 32 && kurse_summe < 37" />
						<i-ri-error-warning-line class="text-yellow-400" v-else-if="kurse_summe >= 31 && kurse_summe <= 32" />
						<i-ri-error-warning-fill class="text-red-400" v-else-if="kurse_summe < 30" />
						-->
					</div>
				</div>
				<div role="row" class="data-table__tr data-table__tbody__tr data-table__thead__tr__compact">
					<div role="rowheader" class="data-table__th font-bold data-table__thead__th data-table__th__align-right col-span-5"> Wochenstunden </div>
					<div role="cell" class="data-table__td col-laufbahnplanung-ergebnis data-table__td__align-center" v-for="(jahrgang, i) in wochenstunden" :key="i">
						<span class="inline-flex justify-center items-center font-bold py-0.5 px-1.5 rounded-md w-full h-full"
							:class="{
								'bg-red-400': jahrgang < 30,
								'bg-yellow-400': jahrgang >= 31 && jahrgang <= 32,
								'bg-green-300': jahrgang > 32 && jahrgang < 37,
								'bg-green-600': jahrgang > 36
							}">
							{{ jahrgang }}
						</span>
					</div>
					<div role="cell" class="data-table__td col-laufbahnplanung-ergebnis data-table__td__align-center">
						<span class="inline-flex justify-center items-center font-bold py-0.5 px-1.5 rounded-md w-full h-full"
							:class="{
								'bg-red-400': wst_summe < 100,
								'bg-yellow-400': wst_summe >= 100 && wst_summe < 102,
								'bg-green-300': wst_summe >= 102 && wst_summe <= 108,
								'bg-green-600': wst_summe > 108
							}">
							{{ wst_summe }}
						</span>
					</div>
				</div>
				<div role="row" class="data-table__tr data-table__tbody__tr data-table__thead__tr__compact">
					<div role="rowheader" class="data-table__th font-bold data-table__thead__th data-table__th__align-right col-span-5"> Durchschnitt </div>
					<div role="cell" class="data-table__td col-laufbahnplanung-ergebnis data-table__td__align-center col-span-2">
						<span class="inline-flex justify-center items-center font-bold py-0.5 px-1.5 rounded-md w-full h-full"
							:class="{
								'bg-red-400': wst_d_ef < 34,
								'bg-green-300': wst_d_ef >= 34 && wst_d_ef < 37,
								'bg-green-600': wst_d_ef >= 37
							}">
							{{ wst_d_ef }}
						</span>
					</div>
					<div role="cell" class="data-table__td col-laufbahnplanung-ergebnis data-table__td__align-center col-span-4">
						<span class="inline-flex justify-center items-center font-bold py-0.5 px-1.5 rounded-md w-full h-full"
							:class="{
								'bg-red-400': wst_d_q < 34,
								'bg-green-300': wst_d_q >= 34 && wst_d_q < 37,
								'bg-green-600': wst_d_q >= 37
							}">
							{{ wst_d_q }}
						</span>
					</div>
					<div role="cell" class="data-table__td col-laufbahnplanung-ergebnis data-table__td__align-center" />
				</div>
			</template>
		</svws-ui-data-table>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type { ComputedRef} from "vue";
	import { computed, ref } from "vue";

	import type { List, GostFach, SchuelerListeEintrag, AbiturdatenManager, GostFaecherManager, GostJahrgangFachkombination, GostSchuelerFachwahl, GostJahrgangsdaten } from "@svws-nrw/svws-core";
	import { GostHalbjahr } from "@svws-nrw/svws-core";
	import type {DataTableColumn} from "@svws-nrw/svws-ui";

	const props = defineProps<{
		abiturdatenManager: AbiturdatenManager;
		faechermanager: GostFaecherManager;
		mapFachkombinationen: Map<number, GostJahrgangFachkombination>;
		gostJahrgangsdaten: GostJahrgangsdaten;
		setWahl: (fachID: number, wahl: GostSchuelerFachwahl) => Promise<void>;
		getPdfWahlbogen: () => Promise<Blob>;
		exportLaufbahnplanung: () => Promise<Blob>;
		importLaufbahnplanung: (data: FormData) => Promise<boolean>;
		item?: SchuelerListeEintrag;
	}>();

	async function onUpdateWahl(fachID: number, wahl: GostSchuelerFachwahl) {
		await props.setWahl(fachID, wahl);
	}

	async function reset_fachwahlen() {
		for (const fachbelegung of props.abiturdatenManager.getFachbelegungen()) {
			const fach = props.abiturdatenManager.getFach(fachbelegung);
			if (fach) {
				const fachwahl = props.abiturdatenManager.getSchuelerFachwahl(fach.id);
				for (const hj  of GostHalbjahr.values()) {
					if (!props.abiturdatenManager.istBewertet(hj))
						fachwahl.halbjahre[hj.id] = null;
				}
				fachwahl.abiturFach = null;
				await onUpdateWahl(fach.id, fachwahl);
			}
		}
	}

	const rows: ComputedRef<List<GostFach>> = computed(() => props.faechermanager.toList());

	const kurszahlen: ComputedRef<number[]> = computed(() => props.abiturdatenManager.getAnrechenbareKurse());

	const kurse_summe: ComputedRef<number> = computed(() => kurszahlen.value.reduce((p, c) => p + c, 0));
	//TODO korrigieren

	const wochenstunden: ComputedRef<number[]> = computed(() => props.abiturdatenManager.getWochenstunden());

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
		link.download = "Wahlbogen.pdf";
		link.target = "_blank";
		link.click();
		URL.revokeObjectURL(link.href);
	}

	async function export_laufbahnplanung() {
		if (props.item === undefined)
			return;
		const gzip = await props.exportLaufbahnplanung();
		const link = document.createElement("a");
		link.href = URL.createObjectURL(gzip);
		link.download = "Laufbahnplanung_Schueler_" + props.item.id + "_" + props.item.nachname + "_" + props.item.vorname + ".lp";
		link.target = "_blank";
		link.click();
		URL.revokeObjectURL(link.href);
	}

	const cols: Array<DataTableColumn> = [
		{ key: "kuerzel", label: "Kürzel", align: 'center'},
		{ key: "bezeichnung", label: "Bezeichnung", align: 'center', span: 3, minWidth: 12 },
		{ key: "wstd", label: "WStd.", align: 'center' },
		{ key: "folge", label: "Folge", align: 'center' },
		{ key: "ab_jg", label: "ab Jg", align: 'center'},
		{ key: "ef1", label: "EF.1" },
		{ key: "ef2", label: "EF.2", align: 'center' },
		{ key: "q1_1", label: "Q1.1", align: 'center' },
		{ key: "q1_2", label: "Q1.2", align: 'center' },
		{ key: "q2_1", label: "Q2.1", align: 'center'},
		{ key: "q2_2", label: "Q2.2", align: 'center'},
		{ key: "abiturfach", label: "Abiturfach", align: 'center'}
	];

</script>

<style scoped lang="postcss">
.col-laufbahnplanung-ergebnis {
	padding: 0.175rem;
}
</style>
