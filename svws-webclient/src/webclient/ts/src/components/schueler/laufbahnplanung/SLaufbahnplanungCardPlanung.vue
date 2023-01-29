<template>
	<svws-ui-content-card title="Laufbahnplanung">
		<s-modal-hilfe> <hilfe-laufbahnplanung /> </s-modal-hilfe>
		<div class="flex-none sm:-mx-6 lg:-mx-8">
			<div class="inline-block py-2 align-middle sm:px-6 lg:px-8">
				<div class="overflow-hidden rounded-lg shadow">
					<table class="border-collapse text-sm">
						<thead :class="{'bg-slate-100': !manuell, 'bg-red-400': manuell}">
							<tr>
								<td class="border border-[#7f7f7f]/20 text-center" colspan="3"> Fach </td>
								<td class="border border-[#7f7f7f]/20 text-center" colspan="2"> Sprachen </td>
								<td class="border border-[#7f7f7f]/20 text-center" colspan="2"> EF </td>
								<td class="border border-[#7f7f7f]/20 text-center" colspan="4"> Qualifikationsphase </td>
								<td class="border border-[#7f7f7f]/20 px-2 text-center" rowspan="2"> Abitur<br>-fach </td>
							</tr>
							<tr>
								<td class="border border-[#7f7f7f]/20 px-2 text-center"> Kürzel </td>
								<td class="border border-[#7f7f7f]/20 text-center"> Bezeichnung </td>
								<td class="border border-[#7f7f7f]/20 text-center">WStd.</td>
								<td class="border border-[#7f7f7f]/20 text-center">Folge</td>
								<td class="border border-[#7f7f7f]/20 text-center">ab Jg</td>
								<td class="border border-[#7f7f7f]/20 text-center">EF.1</td>
								<td class="border border-[#7f7f7f]/20 text-center">EF.2</td>
								<td class="border border-[#7f7f7f]/20 text-center">Q1.1</td>
								<td class="border border-[#7f7f7f]/20 text-center">Q1.2</td>
								<td class="border border-[#7f7f7f]/20 text-center">Q2.1</td>
								<td class="border border-[#7f7f7f]/20 text-center">Q2.2</td>
							</tr>
						</thead>
						<tr v-for="row in rows" :key="row.id" class="select-none">
							<s-laufbahnplanung-fach :abiturmanager="abiturmanager" :faechermanager="faechermanager" :fach="row" :fachkombinationen="fachkombinationen"
								:data-laufbahn="dataLaufbahn" />
						</tr>
						<thead class="bg-slate-100">
							<tr>
								<td class="border border-[#7f7f7f]/20 text-center" colspan="5" />
								<td class="border border-[#7f7f7f]/20 text-center">EF.1</td>
								<td class="border border-[#7f7f7f]/20 text-center">EF.2</td>
								<td class="border border-[#7f7f7f]/20 text-center">Q1.1</td>
								<td class="border border-[#7f7f7f]/20 text-center">Q1.2</td>
								<td class="border border-[#7f7f7f]/20 text-center">Q2.1</td>
								<td class="border border-[#7f7f7f]/20 text-center">Q2.2</td>
								<td class="border border-[#7f7f7f]/20 text-center" />
							</tr>
						</thead>
						<tr>
							<td class="border border-[#7f7f7f]/20 bg-slate-100 text-center" colspan="5"> Anzahl Kurse </td>
							<td v-for="(jahrgang, i) in kurszahlen" :key="i"
								class="border border-[#7f7f7f]/20 text-center"
								:class="{
									'bg-yellow-400': jahrgang < 10,
									'bg-green-300': jahrgang > 9,
									'bg-green-700': jahrgang > 11
								}">
								{{ jahrgang }}
							</td>
							<td class="border border-[#7f7f7f]/20 bg-slate-100 text-center"
								:class="{
									'bg-red-400': kurse_summe < 30,
									'bg-yellow-400': kurse_summe >= 31 && kurse_summe <= 32,
									'bg-green-300': kurse_summe > 32 && kurse_summe < 37,
									'bg-green-700': kurse_summe > 36
								}">
								{{ kurse_summe }}
							</td>
						</tr>
						<tr>
							<td class="border border-[#7f7f7f]/20 bg-slate-100 text-center" colspan="5"> Wochenstunden </td>
							<td v-for="(jahrgang, i) in wochenstunden" :key="i"
								class="border border-[#7f7f7f]/20 text-center"
								:class="{
									'bg-red-400': jahrgang < 30,
									'bg-yellow-400': jahrgang >= 31 && jahrgang <= 32,
									'bg-green-300': jahrgang > 32 && jahrgang < 37,
									'bg-green-700': jahrgang > 36
								}">
								{{ jahrgang }}
							</td>
							<td class="border border-[#7f7f7f]/20 bg-slate-100 text-center"
								:class="{
									'bg-red-400': wst_summe < 100,
									'bg-yellow-400': wst_summe >= 100 && wst_summe < 102,
									'bg-green-300': wst_summe >= 102 && wst_summe <= 108,
									'bg-green-700': wst_summe > 108
								}">
								{{ wst_summe }}
							</td>
						</tr>
						<tr>
							<td class="border border-[#7f7f7f]/20 bg-slate-100 text-center" colspan="5"> Durchschnitt </td>
							<td colspan="2"
								class="border border-[#7f7f7f]/20 text-center"
								:class="{
									'bg-red-400': wst_d_ef < 34,
									'bg-green-300': wst_d_ef >= 34 && wst_d_ef < 37,
									'bg-green-700': wst_d_ef >= 37
								}">
								{{ wst_d_ef }}
							</td>
							<td colspan="4"
								class="border border-[#7f7f7f]/20 bg-slate-100 text-center"
								:class="{
									'bg-red-400': wst_d_q < 34,
									'bg-green-300': wst_d_q >= 34 && wst_d_q < 37,
									'bg-green-700': wst_d_q >= 37
								}">
								{{ wst_d_q }}
							</td>
							<td class="border border-[#7f7f7f]/20 bg-slate-100" />
						</tr>
					</table>
				</div>
				<div class="flex justify-between gap-1">
					<svws-ui-button @click.prevent="download_file">Wahlbogen herunterladen</svws-ui-button>
					<s-modal-laufbahnplanung-kurswahlen-loeschen @delete="reset_fachwahlen" />
					<svws-ui-button :type="manuell ? 'error':'primary'" @click="manu">Manuellen Modus {{ manuell?"de":"" }}aktivieren</svws-ui-button>
				</div>
			</div>
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import { computed, ComputedRef, ref } from "vue";

	import { List, GostFach, SchuelerListeEintrag, AbiturdatenManager, GostFaecherManager, GostJahrgangFachkombination } from "@svws-nrw/svws-core-ts";
	import { App } from "~/apps/BaseApp";
	import { DataSchuelerLaufbahnplanung } from "~/apps/schueler/DataSchuelerLaufbahnplanung";
	import { DataSchuelerStammdaten } from "~/apps/schueler/DataSchuelerStammdaten";

	const props = defineProps<{
		abiturmanager: AbiturdatenManager;
		faechermanager: GostFaecherManager;
		fachkombinationen: List<GostJahrgangFachkombination>;
		item?: SchuelerListeEintrag;
		stammdaten: DataSchuelerStammdaten;
		dataLaufbahn: DataSchuelerLaufbahnplanung;
	}>();

	const manuell = ref(false)

	function reset_fachwahlen() {
		props.dataLaufbahn.reset_fachwahlen();
	}

	const rows: ComputedRef<List<GostFach>> = computed(() => props.faechermanager.toVector());

	const kurszahlen: ComputedRef<number[]> = computed(() => props.abiturmanager.getAnrechenbareKurse());

	const kurse_summe: ComputedRef<number> = computed(() => kurszahlen.value.reduce((p, c) => p + c, 0));
	//TODO korrigieren

	const wochenstunden: ComputedRef<number[]> = computed(() => props.abiturmanager.getWochenstunden());

	const wst_summe: ComputedRef<number> = computed(() => wochenstunden.value.reduce((p, c) => p + c, 0) / 2);

	const wst_d_ef: ComputedRef<number> = computed(() => (wochenstunden.value[0] + wochenstunden.value[1]) / 2);

	const wst_d_q: ComputedRef<number> = computed(() => {
		const [e, f, ...q] = wochenstunden.value;
		void e, f;
		return q.reduce((p, c) => p + c, 0) / 4;
	});

	function manu() {
		manuell.value = manuell.value ? false:true;
		props.dataLaufbahn.manuelle_eingabe = manuell.value
	}

	function download_file() {
		const id = props.stammdaten.daten?.id;
		if (!id)
			return;
		App.api.getGostSchuelerPDFWahlbogen(App.schema, id).then(blob => {
			const link = document.createElement("a");
			link.href = URL.createObjectURL(blob);
			link.download = "Wahlbogen.pdf";
			link.target = "_blank";
			link.click();
			URL.revokeObjectURL(link.href);
		}).catch(console.error);
	}

</script>
