<template>
	<svws-ui-content-card class="pt-8">
		<div class="router-tab-bar--subnav">
			<svws-ui-button size="small" type="transparent" @click.prevent="download_file" title="Wahlbogen herunterladen">Wahlbogen herunterladen</svws-ui-button>
			<svws-ui-button size="small" type="transparent" title="Planung exportieren">Exportieren <i-ri-download-2-line /></svws-ui-button>
			<svws-ui-button size="small" type="transparent" title="Planung importieren">Importieren <i-ri-upload-2-line /></svws-ui-button>
			<svws-ui-button size="small" :type="istManuellerModus ? 'error' : 'transparent'" @click="switchManuellerModus" :title="istManuellerModus ? 'Manuellen Modus deaktivieren' : 'Manuellen Modus aktivieren'">
				Manueller Modus
				<template v-if="istManuellerModus">
					<i-ri-lock-unlock-line />
				</template>
			</svws-ui-button>
			<s-modal-laufbahnplanung-kurswahlen-loeschen @delete="reset_fachwahlen" />
			<s-modal-hilfe class="ml-auto"> <hilfe-laufbahnplanung /> </s-modal-hilfe>
		</div>
		<div class="sticky h-8 -mt-8 -top-8 bg-white z-10" />
		<div class="v-table--container">
			<table class="v-table--complex table-auto w-full">
				<thead :class="{'text-error': istManuellerModus}" :title="istManuellerModus ? 'Manueller Modus aktiviert' : ''">
					<tr>
						<th class="text-center" colspan="3"> Fach </th>
						<th class="text-center" colspan="2"> Sprachen </th>
						<th class="text-center" colspan="2"> EF </th>
						<th class="text-center" colspan="4"> Qualifikationsphase </th>
						<th class="text-center" rowspan="2"> Abitur-<br>fach </th>
					</tr>
					<tr>
						<th class="text-center"> Kürzel </th>
						<th class="text-center"> Bezeichnung </th>
						<th class="text-center" title="Wochenstunden">WStd.</th>
						<th class="text-center">Folge</th>
						<th class="text-center">ab Jg</th>
						<th class="text-center">EF.1</th>
						<th class="text-center">EF.2</th>
						<th class="text-center">Q1.1</th>
						<th class="text-center">Q1.2</th>
						<th class="text-center">Q2.1</th>
						<th class="text-center">Q2.2</th>
					</tr>
				</thead>
				<tbody>
					<template v-for="row in rows" :key="row.id">
						<s-laufbahnplanung-fach :abiturdaten-manager="abiturdatenManager" :faechermanager="faechermanager" :gost-jahrgangsdaten="gostJahrgangsdaten"
							:fach="row" :map-fachkombinationen="mapFachkombinationen" :manueller-modus="istManuellerModus" @update:wahl="onUpdateWahl" />
					</template>
				</tbody>
				<tfoot>
					<tr>
						<th class="text-center" colspan="5" />
						<th class="text-center">EF.1</th>
						<th class="text-center">EF.2</th>
						<th class="text-center">Q1.1</th>
						<th class="text-center">Q1.2</th>
						<th class="text-center">Q2.1</th>
						<th class="text-center">Q2.2</th>
						<th class="text-center" />
					</tr>
					<tr>
						<th class="text-right" colspan="5"> Anzahl Kurse </th>
						<td v-for="(jahrgang, i) in kurszahlen" :key="i"
							class="text-center cell--padding-sm"
							:class="{
								'bg-yellow-400': jahrgang < 10,
								'bg-green-300': jahrgang > 9,
								'bg-green-600': jahrgang > 11
							}">
							<span class="inline-block py-0.5 px-1.5 rounded w-full h-full"
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
						</td>
						<td class="text-center cell--padding-sm"
							:class="{
								'bg-red-400': kurse_summe < 30,
								'bg-yellow-400': kurse_summe >= 31 && kurse_summe <= 32,
								'bg-green-300': kurse_summe > 32 && kurse_summe < 37,
								'bg-green-600': kurse_summe > 36
							}">
							<span class="inline-block py-0.5 px-1.5 rounded w-full h-full"
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
						</td>
					</tr>
					<tr>
						<th class="text-right" colspan="5"> Wochenstunden </th>
						<td v-for="(jahrgang, i) in wochenstunden" :key="i"
							class="text-center cell--padding-sm"
							:class="{
								'bg-red-400': jahrgang < 30,
								'bg-yellow-400': jahrgang >= 31 && jahrgang <= 32,
								'bg-green-300': jahrgang > 32 && jahrgang < 37,
								'bg-green-600': jahrgang > 36
							}">
							<span class="inline-block py-0.5 px-1.5 rounded w-full h-full"
								:class="{
									'bg-red-400': jahrgang < 30,
									'bg-yellow-400': jahrgang >= 31 && jahrgang <= 32,
									'bg-green-300': jahrgang > 32 && jahrgang < 37,
									'bg-green-600': jahrgang > 36
								}">
								{{ jahrgang }}
							</span>
						</td>
						<td class="text-center cell--padding-sm"
							:class="{
								'bg-red-400': wst_summe < 100,
								'bg-yellow-400': wst_summe >= 100 && wst_summe < 102,
								'bg-green-300': wst_summe >= 102 && wst_summe <= 108,
								'bg-green-600': wst_summe > 108
							}">
							<span class="inline-block py-0.5 px-1.5 rounded w-full h-full"
								:class="{
									'bg-red-400': wst_summe < 100,
									'bg-yellow-400': wst_summe >= 100 && wst_summe < 102,
									'bg-green-300': wst_summe >= 102 && wst_summe <= 108,
									'bg-green-600': wst_summe > 108
								}">
								{{ wst_summe }}
							</span>
						</td>
					</tr>
					<tr>
						<th class="text-right" colspan="5"> Durchschnitt </th>
						<td colspan="2"
							class="text-center cell--padding-sm"
							:class="{
								'bg-red-400': wst_d_ef < 34,
								'bg-green-300': wst_d_ef >= 34 && wst_d_ef < 37,
								'bg-green-600': wst_d_ef >= 37
							}">
							<span class="inline-block py-0.5 px-1.5 rounded w-full h-full"
								:class="{
									'bg-red-400': wst_d_ef < 34,
									'bg-green-300': wst_d_ef >= 34 && wst_d_ef < 37,
									'bg-green-600': wst_d_ef >= 37
								}">
								{{ wst_d_ef }}
							</span>
						</td>
						<td colspan="4"
							class="text-center cell--padding-sm"
							:class="{
								'bg-red-400': wst_d_q < 34,
								'bg-green-300': wst_d_q >= 34 && wst_d_q < 37,
								'bg-green-600': wst_d_q >= 37
							}">
							<span class="inline-block py-0.5 px-1.5 rounded w-full h-full"
								:class="{
									'bg-red-400': wst_d_q < 34,
									'bg-green-300': wst_d_q >= 34 && wst_d_q < 37,
									'bg-green-600': wst_d_q >= 37
								}">
								{{ wst_d_q }}
							</span>
						</td>
						<td class="" />
					</tr>
				</tfoot>
			</table>
		</div>
		<div class="sticky h-8 -mb-8 -bottom-8 bg-white z-10" />
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import { computed, ComputedRef, ref } from "vue";

	import { List, GostFach, SchuelerListeEintrag, AbiturdatenManager, GostFaecherManager, GostJahrgangFachkombination,
		GostHalbjahr, GostSchuelerFachwahl, GostJahrgangsdaten } from "@svws-nrw/svws-core-ts";

	const props = defineProps<{
		abiturdatenManager: AbiturdatenManager;
		faechermanager: GostFaecherManager;
		mapFachkombinationen: Map<number, GostJahrgangFachkombination>;
		gostJahrgangsdaten: GostJahrgangsdaten;
		setWahl: (fachID: number, wahl: GostSchuelerFachwahl) => Promise<void>;
		getPdfWahlbogen: () => Promise<Blob>;
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
						fachwahl[hj.toString() as 'EF1' | 'EF2' | 'Q11' | 'Q12' | 'Q21' | 'Q22'] = null;
				}
				fachwahl.abiturFach = null;
				await onUpdateWahl(fach.id, fachwahl);
			}
		}
	}

	const rows: ComputedRef<List<GostFach>> = computed(() => props.faechermanager.toVector());

	const kurszahlen: ComputedRef<number[]> = computed(() => props.abiturdatenManager.getAnrechenbareKurse());

	const kurse_summe: ComputedRef<number> = computed(() => kurszahlen.value.reduce((p, c) => p + c, 0));
	//TODO korrigieren

	const wochenstunden: ComputedRef<number[]> = computed(() => props.abiturdatenManager.getWochenstunden());

	const wst_summe: ComputedRef<number> = computed(() => wochenstunden.value.reduce((p, c) => p + c, 0) / 2);

	const wst_d_ef: ComputedRef<number> = computed(() => (wochenstunden.value[0] + wochenstunden.value[1]) / 2);

	const wst_d_q: ComputedRef<number> = computed(() => {
		const [e, f, ...q] = wochenstunden.value;
		void e, f;
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

</script>
