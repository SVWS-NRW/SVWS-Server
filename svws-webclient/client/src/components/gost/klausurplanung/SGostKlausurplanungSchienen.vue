<template>
	<Teleport to=".svws-ui-header--actions" v-if="isMounted">
		<svws-ui-modal-hilfe class="ml-auto"> <s-gost-klausurplanung-schienen-hilfe /> </svws-ui-modal-hilfe>
	</Teleport>
	<Teleport to=".router-tab-bar--subnav" v-if="isMounted">
		<s-gost-klausurplanung-quartal-auswahl :quartalsauswahl="quartalsauswahl" :halbjahr="halbjahr" />
	</Teleport>
	<svws-ui-modal :show="showModalAutomatischBlocken" size="small">
		<template #modalTitle>
			Automatisch blocken
		</template>
		<template #modalContent>
			<svws-ui-radio-group :row="true">
				<svws-ui-radio-option v-for="a in KlausurterminblockungAlgorithmen.values()" :key="a.id" :value="a" v-model="algMode" :name="a.bezeichnung" :label="a.bezeichnung" />
			</svws-ui-radio-group>
			<svws-ui-spacing />
			<svws-ui-radio-group :row="true">
				<svws-ui-radio-option v-for="k in KlausurterminblockungModusKursarten.values()" :key="k.id" :value="k" v-model="lkgkMode" :name="k.bezeichnung" :label="k.bezeichnung" />
			</svws-ui-radio-group>
			<svws-ui-spacing :size="2" />
			<svws-ui-checkbox type="toggle" v-model="blockeGleicheLehrkraft" v-if="algMode.__ordinal === KlausurterminblockungAlgorithmen.NORMAL.__ordinal" class="text-left">
				Gleicher Termin falls gleiche Lehrkraft, Fach und Kursart
			</svws-ui-checkbox>
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="showModalAutomatischBlocken().value = false"> Abbrechen </svws-ui-button>
			<svws-ui-button type="primary" @click="blocken"> Blocken </svws-ui-button>
		</template>
	</svws-ui-modal>

	<div class="page--content page--content--full relative">
		<svws-ui-content-card title="In Planung">
			<div class="flex flex-col p-1" @drop="onDrop(undefined)" @dragover="$event.preventDefault()" :class="[(dragData !== undefined && dragData instanceof GostKursklausur && dragData.idTermin !== null) ? 'ring-offset-8 ring-4 ring-error/20 rounded-xl' : '' ]">
				<svws-ui-table :items="props.kMan().kursklausurOhneTerminGetMengeByAbijahrAndHalbjahrAndQuartal(props.jahrgangsdaten.abiturjahr, props.halbjahr, props.quartalsauswahl.value)" :columns="cols">
					<template #noData>
						<div class="leading-tight flex flex-col gap-0.5">
							<span>Aktuell keine Klausuren zu planen.</span>
							<span class="opacity-50">Bereits geplante Einträge können hier zurückgelegt werden.</span>
						</div>
					</template>
					<template #header(schriftlich)>
						<svws-ui-tooltip>
							<span class="icon i-ri-group-line" />
							<template #content>Schriftlich/Insgesamt im Kurs</template>
						</svws-ui-tooltip>
					</template>
					<template #header(dauer)>
						<svws-ui-tooltip>
							<span class="icon i-ri-time-line" />
							<template #content>Dauer in Minuten</template>
						</svws-ui-tooltip>
					</template>
					<template #body>
						<div v-for="klausur in props.kMan().kursklausurOhneTerminGetMengeByAbijahrAndHalbjahrAndQuartal(props.jahrgangsdaten.abiturjahr, props.halbjahr, props.quartalsauswahl.value)" class="svws-ui-tr cursor-grab active:cursor-grabbing" role="row"
							:key="klausur.id"
							:data="klausur"
							:draggable="draggable(klausur)"
							@dragstart="onDrag(klausur)"
							@dragend="onDrag(undefined)"
							:class="klausurCssClasses(klausur, undefined)">
							<div class="svws-ui-td">
								<span v-if="hatKompetenzUpdate" class="icon i-ri-draggable -m-0.5 -ml-4 -mr-1" />
								<span class="svws-ui-badge" :style="`--background-color: ${kMan().fachHTMLFarbeRgbaByKursklausur(klausur)};`">{{ kMan().kursKurzbezeichnungByKursklausur(klausur) }}</span>
							</div>
							<div class="svws-ui-td">{{ kMan().kursLehrerKuerzelByKursklausur(klausur) }}</div>
							<div class="svws-ui-td svws-align-right">{{ kMan().schuelerklausurGetMengeByKursklausur(klausur).size() + "/" + kMan().kursAnzahlSchuelerGesamtByKursklausur(klausur) }}</div>
							<div class="svws-ui-td svws-align-right">{{ kMan().vorgabeByKursklausur(klausur).dauer }}</div>
							<div class="svws-ui-td svws-align-right"><span class="opacity-50">{{ kMan().kursSchieneByKursklausur(klausur).isEmpty() ? "-" : kMan().kursSchieneByKursklausur(klausur).get(0) }}</span></div>
							<div class="svws-ui-td svws-align-right -mr-0.5" v-if="!quartalsauswahl.value"><span class="opacity-50">{{ kMan().vorgabeByKursklausur(klausur).quartal }}.</span></div>
						</div>
					</template>
					<template #actions>
						<svws-ui-button :disabled="!hatKompetenzUpdate" class="-mr-3" type="transparent" @click="erzeugeKursklausurenAusVorgabenOrModal" title="Erstelle Klausuren aus den Vorgaben"><span class="icon i-ri-upload-2-line" />Aus Vorgaben erstellen</svws-ui-button>
					</template>
				</svws-ui-table>
			</div>
		</svws-ui-content-card>
		<svws-ui-content-card>
			<div class="flex justify-between items-start mb-5">
				<div class="flex flex-wrap items-center gap-0.5 w-full">
					<svws-ui-button :disabled="!hatKompetenzUpdate" @click="erzeugeKlausurtermin(quartalsauswahl.value, true)"><span class="icon i-ri-add-line -ml-1" />Termin<template v-if="termine.size() === 0"> hinzufügen</template></svws-ui-button>
					<svws-ui-button type="transparent" @click="showModalAutomatischBlocken().value = true" :disabled="!hatKompetenzUpdate || props.kMan().kursklausurOhneTerminGetMengeByAbijahrAndHalbjahrAndQuartal(jahrgangsdaten.abiturjahr, props.halbjahr, props.quartalsauswahl.value).size() === 0"><span class="icon i-ri-sparkling-line" />Automatisch blocken <svws-ui-spinner :spinning="loading" /></svws-ui-button>
					<svws-ui-button type="transparent" :disabled="!hatKompetenzUpdate" class="hover--danger ml-auto" @click="terminSelected.value = undefined; loescheKlausurtermine(termine)" v-if="termine.size() > 0" title="Alle Termine löschen"><span class="icon i-ri-delete-bin-line" />Alle löschen</svws-ui-button>
				</div>
			</div>
			<div class="grid grid-cols-[repeat(auto-fill,minmax(22rem,1fr))] gap-4 pt-2 -mt-2">
				<template v-if="termine.size()">
					<s-gost-klausurplanung-schienen-termin v-for="termin of termine" :key="termin.id"
						:id="'termin' + termin.id"
						:benutzer-kompetenzen
						:termin="() => termin"
						:class="dropOverCssClasses(termin)"
						:k-man
						:drag-data="() => dragData"
						@dragover="terminSelected.value=termin"
						@dragleave="terminSelected.value=undefined"
						:on-drag
						:on-drop
						:draggable
						:termin-selected="props.terminSelected.value?.id===termin.id"
						@click="gotoSchienen(props.terminSelected.value?.id===termin.id?undefined:termin);$event.stopPropagation()"
						:loesche-klausurtermine
						:patch-klausurtermin
						:klausur-css-classes
						:create-schuelerklausur-termin
						:patch-klausur
						:goto-kalenderwoche
						:goto-raumzeit-termin />
				</template>
				<template v-else>
					<div class="shadow-inner rounded-lg h-48" />
					<div class="shadow-inner rounded-lg h-48" />
					<div class="shadow-inner rounded-lg h-48" />
				</template>
			</div>
		</svws-ui-content-card>
		<svws-ui-content-card class="-ml-4">
			<template #title>
				<span class="text-headline-md leading-none inline-flex gap-1">
					<template v-if="klausurKonflikte().size() > 0">
						<span class="icon i-ri-alert-fill icon-error -my-0.5" />
						<span>{{ klausurKonflikte().size() }} Kurse mit Konflikten</span>
					</template>
					<template v-else-if="anzahlProKwKonflikte(3).size() > 0">
						<span class="icon i-ri-alert-fill icon-error -my-0.5" />
						<span> Konflikte</span>
					</template>
					<template v-else-if="terminSelected !== undefined || dragData !== undefined">
						<span class="icon i-ri-checkbox-circle-fill icon-success -my-1" />
						<span>Keine Konflikte</span>
					</template>
					<template v-else>
						<span class="opacity-50">Konflikte</span>
					</template>
				</span>
			</template>
			<div v-if="klausurKonflikte().size() > 0" class="mt-5" :class="{'mb-16': anzahlProKwKonflikte(3).size() > 0}">
				<ul class="flex flex-col gap-3">
					<li v-for="klausur in klausurKonflikte()" :key="klausur.getKey().id">
						<span class="svws-ui-badge" :style="`--background-color: ${ kMan().fachHTMLFarbeRgbaByKursklausur(klausur.getKey()) };`">{{ kMan().kursKurzbezeichnungByKursklausur(klausur.getKey()) }}</span>
						<div class="leading-tight">
							{{ [...klausur.getValue()].map(sid => kMan().getSchuelerMap().get(sid)?.vorname + ' ' + kMan().getSchuelerMap().get(sid)?.nachname).join(", ") }}
						</div>
					</li>
				</ul>
			</div>
			<div v-if="anzahlProKwKonflikte(3).size() > 0" class="mt-5">
				<div class="text-headline-md leading-tight mb-3">
					<div class="inline-flex gap-1">{{ anzahlProKwKonflikte(3).size() }} Schüler</div>
					<div class="opacity-50">Drei oder mehr Klausuren in einer KW</div>
				</div>
				<ul class="flex flex-col gap-4">
					<li v-for="konflikt in anzahlProKwKonflikte(3)" :key="konflikt.getKey()">
						<span class="font-bold">{{ kMan().getSchuelerMap().get(konflikt.getKey())?.vorname + ' ' + kMan().getSchuelerMap().get(konflikt.getKey())?.nachname }}</span>
						<div class="grid grid-cols-3 gap-x-1 gap-y-2 mt-0.5">
							<span v-for="klausur in konflikt.getValue()" :key="klausur.id" class="svws-ui-badge flex-col w-full" :style="`--background-color: ${kMan().fachHTMLFarbeRgbaByKursklausur(kMan().kursklausurBySchuelerklausurTermin(klausur))};`">
								<span class="text-button font-medium">{{ kMan().kursKurzbezeichnungByKursklausur(kMan().kursklausurBySchuelerklausurTermin(klausur)) }}</span>
								<span class="text-sm font-medium">{{ getDatum(kMan().kursklausurBySchuelerklausurTermin(klausur)) }}</span>
							</span>
						</div>
					</li>
				</ul>
			</div>
			<div v-else-if="terminSelected === undefined" class="mt-5 opacity-50 flex flex-col gap-2">
				<span>Klicke auf einen Termin oder verschiebe eine Klausur, um Details zu bestehenden bzw. entstehenden Konflikten anzuzeigen.</span>
			</div>
		</svws-ui-content-card>
		<s-gost-klausurplanung-modal :show="returnModalVorgaben()" :text="modalError" :jump-to="gotoVorgaben" jump-to_text="Zu den Klausurvorgaben" abbrechen_text="OK" />
		<s-gost-klausurplanung-modal :show="returnModalKlausurHatRaeume()" text="Die Kursklausur hat bereits eine oder mehrere Raumzuweisungen. Beim Fortfahren werden diese gelöscht." :weiter="verschiebeKlausurTrotzRaumzuweisung" />
	</div>
</template>

<script setup lang="ts">

	import type { GostSchuelerklausurTermin, JavaMapEntry, JavaSet, List} from "@core";
	import { BenutzerKompetenz, OpenApiError } from "@core";
	import {GostKursklausur, GostKlausurtermin, HashSet, KlausurterminblockungAlgorithmen, GostKlausurterminblockungDaten, KlausurterminblockungModusKursarten, KlausurterminblockungModusQuartale, DateUtils } from "@core";
	import type { Ref } from 'vue';
	import { computed, ref, onMounted } from 'vue';
	import type { GostKlausurplanungSchienenProps } from './SGostKlausurplanungSchienenProps';
	import type { GostKlausurplanungDragData, GostKlausurplanungDropZone } from "./SGostKlausurplanung";
	import type {DataTableColumn} from "@ui";

	const _showModalAutomatischBlocken = ref<boolean>(false);
	const showModalAutomatischBlocken = () => _showModalAutomatischBlocken;

	const props = defineProps<GostKlausurplanungSchienenProps>();

	const hatKompetenzUpdate = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_AENDERN));

	const loading = ref<boolean>(false);

	const dragData = ref<GostKlausurplanungDragData>(undefined);

	const onDrag = (data: GostKlausurplanungDragData) => {
		props.terminSelected.value = undefined;
		dragData.value = data;
	};

	function getDatum(klausur: GostKursklausur) {
		const termin = props.kMan().terminOrNullByKursklausur(klausur);
		if (termin !== null && termin.datum !== null)
			return DateUtils.gibDatumGermanFormat(termin.datum);
		if (props.terminSelected.value !== undefined && props.terminSelected.value.datum !== null)
			return DateUtils.gibDatumGermanFormat(props.terminSelected.value.datum);
		return "N.N."
	}

	const modalVorgaben = ref<boolean>(false);
	function returnModalVorgaben(): () => Ref<boolean> {
		return () => modalVorgaben;
	}

	let klausurMoveDropZone: GostKlausurplanungDropZone = undefined;
	let klausurMoveDragData: GostKlausurplanungDragData = undefined;
	const modalKlausurHatRaeume = ref<boolean>(false);
	function returnModalKlausurHatRaeume(): () => Ref<boolean> {
		return () => modalKlausurHatRaeume;
	}

	const modalError = ref<string | undefined>(undefined);

	async function erzeugeKursklausurenAusVorgabenOrModal() {
		try {
			await props.erzeugeKursklausurenAusVorgaben(props.quartalsauswahl.value);
		} catch(err) {
			if (err instanceof OpenApiError) {
				modalError.value = await err.response?.text();
				modalVorgaben.value = true;
			} else
				throw err;
		}
	}

	const klausurKonflikte = () => {
		if (dragData.value !== undefined && props.terminSelected.value !== undefined && dragData.value instanceof GostKursklausur) {
			if (props.kMan().vorgabeByKursklausur(dragData.value).quartal === props.terminSelected.value.quartal || props.terminSelected.value.quartal === 0)
				return props.kMan().konflikteNeuMapKursklausurSchueleridsByTerminAndKursklausur(props.terminSelected.value, dragData.value).entrySet();
		} else if (props.terminSelected.value !== undefined)
			return props.kMan().konflikteMapKursklausurSchueleridsByTermin(props.terminSelected.value).entrySet();
		return new HashSet<JavaMapEntry<GostKursklausur, JavaSet<number>>>();
	}

	const anzahlProKwKonflikte = (threshold: number) => {
		if (dragData.value !== undefined && props.terminSelected.value !== undefined && dragData.value instanceof GostKursklausur) {
			if (props.kMan().vorgabeByKursklausur(dragData.value).quartal === props.terminSelected.value.quartal || props.terminSelected.value.quartal === 0)
				return props.kMan().klausurenProSchueleridExceedingKWThresholdByTerminAndKursklausurAndThreshold(props.terminSelected.value, dragData.value, threshold).entrySet();
		} else if (props.terminSelected.value !== undefined)
			return props.kMan().klausurenProSchueleridExceedingKWThresholdByTerminAndThreshold(props.terminSelected.value, threshold).entrySet();
		return new HashSet<JavaMapEntry<number, List<GostSchuelerklausurTermin>>>();
	}

	const onDrop = async (zone: GostKlausurplanungDropZone) => {
		if (dragData.value instanceof GostKursklausur) {
			const klausur = dragData.value;
			klausurMoveDropZone = zone;
			klausurMoveDragData = dragData.value;
			if (klausur.idTermin !== null && props.kMan().hatRaumzuteilungByKursklausur(klausur)) {
				modalKlausurHatRaeume.value = true;
				return;
			} else {
				await verschiebeKlausurTrotzRaumzuweisung();
			}
		}
	};

	async function verschiebeKlausurTrotzRaumzuweisung() {
		if (klausurMoveDragData instanceof GostKursklausur) {
			if (klausurMoveDropZone === undefined && klausurMoveDragData.idTermin !== null)
				await props.patchKlausur(klausurMoveDragData, {idTermin: null});
			else if (klausurMoveDropZone instanceof GostKlausurtermin) {
				const termin = klausurMoveDropZone;
				if (termin.id !== klausurMoveDragData.idTermin) {
					await props.patchKlausur(klausurMoveDragData, {idTermin: termin.id});
					props.terminSelected.value = klausurMoveDropZone;
				}
			}
		}
	}



	const dropOverCssClasses = (termin: GostKlausurtermin) => ({
		"bg-success": dragData.value !== undefined && (props.kMan().vorgabeByKursklausur(dragData.value as GostKursklausur).quartal === termin.quartal || termin.quartal === 0),
		"opacity-25 border-transparent shadow-none": dragData.value !== undefined && (props.kMan().vorgabeByKursklausur(dragData.value as GostKursklausur).quartal !== termin.quartal && termin.quartal !== 0),
	});

	const termine = computed(() => props.kMan().terminHtGetMengeByAbijahrAndHalbjahrAndQuartal(props.jahrgangsdaten.abiturjahr, props.halbjahr, props.quartalsauswahl.value));

	const algMode = ref<KlausurterminblockungAlgorithmen>(KlausurterminblockungAlgorithmen.NORMAL);
	const lkgkMode = ref<KlausurterminblockungModusKursarten>(KlausurterminblockungModusKursarten.BEIDE);
	const blockeGleicheLehrkraft = ref(false);

	function draggable(data: GostKlausurplanungDragData) {
		return hatKompetenzUpdate.value && data instanceof GostKursklausur;
	}

	const blocken = async () => {
		loading.value = true;
		showModalAutomatischBlocken().value = false;
		const daten = new GostKlausurterminblockungDaten();
		daten.klausuren = props.kMan().kursklausurOhneTerminGetMengeByAbijahrAndHalbjahrAndQuartal(props.jahrgangsdaten.abiturjahr, props.halbjahr, props.quartalsauswahl.value);
		daten.konfiguration.modusQuartale = KlausurterminblockungModusQuartale.GETRENNT.id;
		daten.konfiguration.algorithmus = algMode.value.id;
		daten.konfiguration.modusKursarten = lkgkMode.value.id;
		daten.konfiguration.regelBeiTerminenGleicheLehrkraftFachKursart = blockeGleicheLehrkraft.value;
		await props.blockenKursklausuren(daten);
		loading.value = false;
	};

	const klausurCssClasses = (kl: GostKlausurplanungDragData, termin: GostKlausurtermin | undefined) => {
		const klausur = kl as GostKursklausur;
		// let konfliktfreiZuFremdtermin = false;
		// for (const oTermin of termine.value) {
		// 	if (oTermin.id !== klausur.idTermin && oTermin.quartal === props.kMan().vorgabeByKursklausur(klausur).quartal || oTermin.quartal === 0)
		// 		konfliktfreiZuFremdtermin = props.kMan().konflikteAnzahlZuTerminGetByTerminAndKursklausur(oTermin, klausur) === 0;
		// 	if (konfliktfreiZuFremdtermin)
		// 		break;
		// }
		const konfliktZuEigenemTermin = termin === undefined ? false : props.kMan().konflikteAnzahlZuEigenemTerminGetByKursklausur(klausur) > 0;
		return {
			// "svws-ok": !konfliktZuEigenemTermin && konfliktfreiZuFremdtermin,
			// "bg-yellow-300": !konfliktfreiZuFremdtermin,
			"bg-red-200": konfliktZuEigenemTermin,
		}
	};

	const isMounted = ref(false);
	onMounted(() => {
		isMounted.value = true;
		if (props.terminSelected.value) {
			const scrollToElement = document.getElementById("termin" + props.terminSelected.value.id);
			if (scrollToElement)
				scrollToElement.scrollIntoView({ behavior: 'smooth' });
		}
	});

	function calculateColumns() {
		const cols: DataTableColumn[] = [
			{ key: "kurs", label: "Kurs", span: 1.25 },
			{ key: "kuerzel", label: "Lehrkraft" },
			{ key: "schriftlich", label: "Schriftlich", span: 0.5, align: "right", minWidth: 3.25 },
			{ key: "dauer", label: "Dauer", tooltip: "Dauer in Minuten", span: 0.5, align: "right", minWidth: 3.25 },
			{ key: "kursSchiene", label: "S", tooltip: "Schiene", span: 0.25, align: "right", minWidth: 2.75 },
		];

		if (props.quartalsauswahl.value === 0) {
			cols.push({ key: "quartal", label: "Q", tooltip: "Quartal", span: 0.25, align: "right", minWidth: 2.75 })
		}

		return cols;
	}

	const cols = computed(() => calculateColumns());

</script>

<style lang="postcss" scoped>
.page--content {
  	@apply grid;
  	grid-template-columns: minmax(22rem, 0.2fr) 1fr minmax(22rem, 0.2fr);
}

.svws-ui-tab-content {
	@apply overflow-y-hidden items-start;

	.page--content {
		@apply h-full py-0 auto-rows-auto;

		.content-card {
			@apply max-h-full pt-8 pb-16 px-4 -mx-4 overflow-y-auto h-[unset];
			scrollbar-gutter: stable;
		}
	}
}
</style>
