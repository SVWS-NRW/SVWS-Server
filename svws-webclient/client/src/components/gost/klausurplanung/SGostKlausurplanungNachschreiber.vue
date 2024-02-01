<template>
	<Teleport to=".svws-ui-header--actions" v-if="isMounted">
		<svws-ui-modal-hilfe class="ml-auto"> <s-gost-klausurplanung-schienen-hilfe /> </svws-ui-modal-hilfe>
	</Teleport>
	<Teleport to=".router-tab-bar--subnav" v-if="isMounted">
		<s-gost-klausurplanung-quartal-auswahl :quartalsauswahl="quartalsauswahl" :halbjahr="halbjahr" />
	</Teleport>

	<svws-ui-modal v-if="showModalTerminGrund" :show="showModalTerminGrund" size="big">
		<template #modalTitle>
			Nachschreiber in folgenden Hauptterminen zulassen:
		</template>
		<template #modalContent>
			<svws-ui-table :columns="cols">
				<template #noData>
					<slot name="noData">
				&nbsp;
					</slot>
				</template>

				<template #body>
					<div v-for="termin in kMan().terminGetHTMengeByHalbjahrAndQuartal(halbjahr, quartalsauswahl.value, true)"
						:key="termin.id"
						class="svws-ui-tr" role="row" :title="cols.map(c => c.tooltip !== undefined ? c.tooltip : c.label).join(', ')">
						<div class="svws-ui-td" role="cell">
							<svws-ui-checkbox :disabled="kMan().schuelerklausurterminNtByTermin(termin).size() > 0" v-model="termin.nachschreiberZugelassen" @update:model-value="patchKlausurtermin(termin.id, { 'nachschreiberZugelassen': termin.nachschreiberZugelassen } )" />
						</div>
						<div class="svws-ui-td" role="cell">
							{{ termin.datum !== null ? DateUtils.gibDatumGermanFormat(termin.datum) : "N.N." }}
						</div>
						<div class="svws-ui-td" role="cell">
							{{ kMan().schuelerklausurterminGetMengeByTerminid(termin.id).size() }}
						</div>
						<div class="svws-ui-td" role="cell">
							{{ [...kMan().kursklausurGetMengeByTerminid(termin.id)].map(k => kMan().kursKurzbezeichnungByKursklausur(k)).join(", ") }}
						</div>
					</div>
				</template>
			</svws-ui-table>
		</template>
		<template #modalActions>
			<svws-ui-button type="primary" @click="_showModalTerminGrund = false"> OK </svws-ui-button>
		</template>
	</svws-ui-modal>

	<div class="page--content page--content--full relative">
		<svws-ui-content-card title="In Planung">
			<div class="flex flex-col" @drop="onDrop(undefined)" @dragover="$event.preventDefault()">
				<s-gost-klausurplanung-schuelerklausur-table :k-man="kMan"
					:schuelerklausuren="kMan().schuelerklausurterminNtAktuellOhneTerminGetMengeByHalbjahrAndQuartal(props.halbjahr, props.quartalsauswahl.value)"
					:map-schueler="mapSchueler"
					:on-drag="onDrag"
					:draggable="() => true">
					<template #noData>
						<div class="leading-tight flex flex-col gap-0.5">
							<span>Aktuell keine Nachschreibklausuren zu planen.</span>
							<span class="opacity-50">Bereits geplante Klausuren können hier zurückgelegt werden.</span>
						</div>
					</template>
				</s-gost-klausurplanung-schuelerklausur-table>
			</div>
		</svws-ui-content-card>
		<svws-ui-content-card>
			<div class="flex justify-between items-start mb-5">
				<div class="flex flex-wrap items-center gap-0.5 w-full">
					<svws-ui-button @click="erzeugeKlausurtermin(quartalsauswahl.value, false)"><i-ri-add-line class="-ml-1" />Neuer Nachschreibtermin</svws-ui-button>
					<svws-ui-button type="secondary" @click="_showModalTerminGrund = true"><i-ri-checkbox-circle-line class="-ml-1" />Haupttermin zulassen</svws-ui-button>
					<svws-ui-button type="secondary" @click="blocken"><i-ri-sparkling-line />Automatisch blocken <svws-ui-spinner :spinning="loading" /></svws-ui-button>
				</div>
			</div>
			<div class="grid grid-cols-[repeat(auto-fill,minmax(20rem,1fr))] gap-4 pt-2 -mt-2">
				<template v-if="termine.size()">
					<s-gost-klausurplanung-nachschreiber-termin v-for="termin of termine" :key="termin.id"
						:termin="() => termin"
						:class="undefined"
						:k-man="kMan"
						:map-schueler="mapSchueler"
						:drag-data="dragData"
						:on-drag="onDrag"
						:on-drop="onDrop"
						:draggable="draggable"
						:termin-selected="terminSelected?.id===termin.id"
						@click="terminSelected=(terminSelected?.id===termin.id?undefined:termin);$event.stopPropagation()"
						:loesche-klausurtermine="loescheKlausurtermine"
						:patch-klausurtermin="patchKlausurtermin"
						:klausur-css-classes="klausurCssClasses"
						:show-schuelerklausuren="true" />
				</template>
				<template v-else>
					<div class="shadow-inner rounded-lg h-48" />
					<div class="shadow-inner rounded-lg h-48" />
					<div class="shadow-inner rounded-lg h-48" />
				</template>
			</div>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">

	import {GostKursklausur, DateUtils, GostKlausurtermin, GostSchuelerklausurTermin, GostNachschreibterminblockungKonfiguration } from "@core";
	import { computed, ref, onMounted } from 'vue';
	import type { GostKlausurplanungDragData, GostKlausurplanungDropZone } from "./SGostKlausurplanung";
	import type { GostKlausurplanungNachschreiberProps } from "./SGostKlausurplanungNachschreiberProps";
	import type { DataTableColumn } from "@ui";

	const _showModalTerminGrund = ref<boolean>(false);
	const showModalTerminGrund = () => _showModalTerminGrund;

	const loading = ref<boolean>(false);

	const props = defineProps<GostKlausurplanungNachschreiberProps>();

	const dragData = ref<GostKlausurplanungDragData>(undefined);
	const terminSelected = ref<GostKlausurtermin | undefined>(undefined);

	const onDrag = (data: GostKlausurplanungDragData) => {
		terminSelected.value = undefined;
		dragData.value = data;
	};

	async function blocken() {
		loading.value = true;
		const config = new GostNachschreibterminblockungKonfiguration();
		config.termine = termine.value;
		config.schuelerklausurtermine = props.kMan().schuelerklausurterminNtAktuellOhneTerminGetMengeByHalbjahrAndQuartal(props.halbjahr, props.quartalsauswahl.value);
		await props.blockenNachschreibklausuren(config);
		loading.value = false;
	}

	// const klausurKonflikte = () => {
	// 	if (dragData.value !== undefined && terminSelected.value !== undefined) {
	// 		if (dragData.value.quartal === terminSelected.value.quartal || terminSelected.value.quartal === 0)
	// 			return props.kMan().konflikteNeuMapKursklausurSchueleridsByTerminidAndKursklausurid(terminSelected.value.id, dragData.value.id).entrySet();
	// 	} else if (terminSelected.value !== undefined)
	// 		return props.kMan().konflikteMapKursklausurSchueleridsByTerminid(terminSelected.value.id).entrySet();
	// 	return new HashSet<JavaMapEntry<GostKursklausur, JavaSet<number>>>();
	// }

	// const anzahlProKwKonflikte = (threshold: number) => {
	// 	if (dragData.value !== undefined && terminSelected.value !== undefined && dragData.value instanceof GostKursklausur) {
	// 		if (dragData.value.quartal === terminSelected.value.quartal || terminSelected.value.quartal === 0)
	// 			return props.kMan().klausurenProSchueleridExceedingKWThresholdByTerminAndKursklausurAndThreshold(terminSelected.value, dragData.value, threshold);
	// 	} else if (terminSelected.value !== undefined)
	// 		return props.kMan().klausurenProSchueleridExceedingKWThresholdByTerminAndThreshold(terminSelected.value, threshold);
	// 	return new HashSet<number>();
	// }

	function draggable(data: GostKlausurplanungDragData) {
		return data instanceof GostSchuelerklausurTermin;
	}

	const onDrop = async (zone: GostKlausurplanungDropZone) => {
		if (dragData.value instanceof GostKursklausur || dragData.value instanceof GostSchuelerklausurTermin) {
			if (zone === undefined && dragData.value.idTermin !== null)
				await props.patchKlausur(dragData.value, {idTermin: null});
			else if (zone instanceof GostKlausurtermin) {
				if (zone.id !== dragData.value.idTermin) {
					await props.patchKlausur(dragData.value, {idTermin: zone.id});
					terminSelected.value = zone;
				}
			}
		}
	};

	// const dropOverCssClasses = (termin: GostKlausurtermin) => ({
	// 	"bg-success": dragData.value !== undefined && (dragData.value.quartal === termin.quartal || termin.quartal === 0),
	// 	"opacity-25 border-transparent shadow-none": dragData.value !== undefined && (dragData.value.quartal !== termin.quartal && termin.quartal !== 0),
	// });

	const termine = computed(() => props.kMan().terminGetNTMengeByHalbjahrAndQuartal(props.halbjahr, props.quartalsauswahl.value, true));

	const klausurCssClasses = (klausur: GostKursklausur, termin: GostKlausurtermin | undefined) => {
		let konfliktfreiZuFremdtermin = false;
		// for (const oTermin of termine.value) {
		// 	if (oTermin.id !== klausur.idTermin && oTermin.quartal === klausur.quartal || oTermin.quartal === 0)
		// 		konfliktfreiZuFremdtermin = props.kMan().konflikteAnzahlZuTerminGetByTerminAndKursklausur(oTermin, klausur) === 0;
		// 	if (konfliktfreiZuFremdtermin)
		// 		break;
		// }
		const konfliktZuEigenemTermin = termin === undefined || klausur === null ? false : true;//props.kMan().konflikteAnzahlZuEigenemTerminGetByKursklausur(klausur) > 0;
		return {
			"svws-ok": !konfliktZuEigenemTermin && konfliktfreiZuFremdtermin,
			"svws-warning": !konfliktfreiZuFremdtermin,
			"svws-error": konfliktZuEigenemTermin,
		}
	};

	const isMounted = ref(false);
	onMounted(() => {
		isMounted.value = true;
	});

	function calculateColumns() {
		const cols: DataTableColumn[] = [
			{ key: "id", label: "", fixedWidth: 2 },
			{ key: "datum", label: "Datum", fixedWidth: 8 },
			{ key: "size", label: "#SuS", fixedWidth: 4 },
			{ key: "faecher", label: "Kurse" },
		];

		return cols;
	}

	const cols = computed(() => calculateColumns());

</script>

<style lang="postcss" scoped>
.page--content {
  @apply grid;
  grid-template-columns: minmax(22rem, 0.2fr) 1fr minmax(22rem, 0.2fr);
}
</style>
