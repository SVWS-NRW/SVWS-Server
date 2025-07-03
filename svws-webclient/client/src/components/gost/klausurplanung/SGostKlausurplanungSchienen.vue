<template>
	<Teleport to=".svws-ui-header--actions" v-if="isMounted">
		<svws-ui-modal-hilfe class="ml-auto"> <s-gost-klausurplanung-schienen-hilfe /> </svws-ui-modal-hilfe>
	</Teleport>
	<Teleport to=".router-tab-bar--subnav" v-if="isMounted">
		<s-gost-klausurplanung-quartal-auswahl :quartalsauswahl="quartalsauswahl" :halbjahr="halbjahr" />
	</Teleport>
	<svws-ui-modal v-model:show="showModalAutomatischBlocken" size="small">
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
			<svws-ui-button type="secondary" @click="showModalAutomatischBlocken = false"> Abbrechen </svws-ui-button>
			<svws-ui-button type="primary" @click="blocken"> Blocken </svws-ui-button>
		</template>
	</svws-ui-modal>
	<div class="page page-flex-row">
		<div class="min-w-fit max-w-fit flex flex-col gap-2" @drop="onDrop(undefined)" @dragover="$event.preventDefault()" :class="[(dragData !== undefined && dragData instanceof GostKursklausur && dragData.idTermin !== null) ? 'ring-offset-8 ring-4 ring-ui-danger/20 rounded-xl' : '' ]">
			<h3 class="text-headline-md" title="In Planung">In Planung</h3>
			<svws-ui-table selectable v-model="selected" :items="props.kMan().kursklausurOhneTerminGetMengeByAbijahrAndHalbjahrAndQuartal(props.jahrgangsdaten.abiturjahr, props.halbjahr, props.quartalsauswahl.value)" :columns="cols">
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
				<!-- { key: "kurs", label: "Kurs", minWidth: 6.25 },
			{ key: "kuerzel", label: "Lehrkraft" },
			{ key: "schriftlich", label: "Schriftlich", span: 0.5, align: "center", minWidth: 3.25 },
			{ key: "dauer", label: "Dauer", tooltip: "Dauer in Minuten", span: 0.5, align: "right", minWidth: 3.25 },
			{ key: "kursSchiene", label: "S", tooltip: "Schiene", span: 0.25, align: "right", minWidth: 2.75 }, -->
				<template #cell(kurs)="{ rowData }">
					<div class="-ml-2" :data="rowData" :draggable="draggable(rowData)" @dragstart="onDrag(rowData)" @dragend="onDrag(undefined)">
						<span v-if="hatKompetenzUpdate" class="icon i-ri-draggable" />
						<svws-ui-tooltip :hover="false" :indicator="false">
							<template #content>
								<s-gost-klausurplanung-kursliste :k-man :kursklausur="rowData" :patch-klausur :create-schuelerklausur-termin :benutzer-kompetenzen />
							</template>
							<span class="svws-ui-badge hover:opacity-75" :style="`color: var(--color-text-uistatic); background-color: ${ kMan().fachHTMLFarbeRgbaByKursklausur(rowData) };`">{{ kMan().kursKurzbezeichnungByKursklausur(rowData) }}</span>
							<svws-ui-tooltip>
								<template #content>
									<div v-if="kMan().vorgabeByKursklausur(rowData).bemerkungVorgabe !== null && kMan().vorgabeByKursklausur(rowData).bemerkungVorgabe!.trim().length > 0">
										<h3 class="border-b text-headline-md">Bemerkung zur Vorgabe</h3>
										<p>{{ kMan().vorgabeByKursklausur(rowData).bemerkungVorgabe }}</p>
									</div>
									<div v-if="rowData.bemerkung !== null && rowData.bemerkung.trim().length > 0">
										<h3 class="border-b text-headline-md">Bemerkung zur Kursklausur</h3>
										<p>{{ rowData.bemerkung }}</p>
									</div>
								</template>
								<span class="icon i-ri-edit-2-line icon-ui-brand" v-if="(rowData.bemerkung !== null && rowData.bemerkung.trim().length > 0) || (kMan().vorgabeByKursklausur(rowData).bemerkungVorgabe !== null && kMan().vorgabeByKursklausur(rowData).bemerkungVorgabe!.trim().length > 0)" />
							</svws-ui-tooltip>
						</svws-ui-tooltip>
					</div>
				</template>
				<template #cell(kuerzel)="{ rowData }">
					{{ kMan().kursLehrerKuerzelByKursklausur(rowData) }}
				</template>
				<template #cell(schriftlich)="{ rowData }">
					{{ kMan().schuelerklausurGetMengeByKursklausur(rowData).size() + "/" + kMan().kursAnzahlSchuelerGesamtByKursklausur(rowData) }}
				</template>
				<template #cell(dauer)="{ rowData }">
					{{ kMan().vorgabeByKursklausur(rowData).dauer }}
				</template>
				<template #cell(kursSchiene)="{ rowData }">
					<span class="opacity-50">{{ kMan().kursSchieneByKursklausur(rowData).isEmpty() ? "-" : kMan().kursSchieneByKursklausur(rowData).get(0) }}</span>
				</template>
				<template v-if="!quartalsauswahl.value" #cell(quartal)="{ rowData }">
					{{ kMan().vorgabeByKursklausur(rowData).quartal }}
				</template>
				<template #actions>
					<svws-ui-button type="trash" :disabled="selected.length===0" @click="loescheKursklausuren(selected);selected = []" />
					<svws-ui-button :disabled="!hatKompetenzUpdate" class="-mr-3" type="transparent" @click="erzeugeKursklausurenAusVorgabenOrModal" title="Erstelle Klausuren aus den Vorgaben"><span class="icon i-ri-upload-2-line" />Aus Vorgaben erstellen</svws-ui-button>
				</template>
			</svws-ui-table>
		</div>
		<div class="min-w-fit grow h-full flex flex-col gap-4">
			<div class="flex justify-between items-start">
				<div class="flex flex-wrap items-center gap-2 w-full">
					<svws-ui-button :disabled="!hatKompetenzUpdate" @click="erzeugeKlausurtermin(quartalsauswahl.value, true)"><span class="icon i-ri-add-line -ml-1" />Termin<template v-if="termine.size() === 0"> hinzufügen</template></svws-ui-button>
					<svws-ui-button type="transparent" @click="showModalAutomatischBlocken = true" :disabled="!hatKompetenzUpdate || props.kMan().kursklausurOhneTerminGetMengeByAbijahrAndHalbjahrAndQuartal(jahrgangsdaten.abiturjahr, props.halbjahr, props.quartalsauswahl.value).size() === 0"><span class="icon i-ri-sparkling-line" />Automatisch blocken <svws-ui-spinner :spinning="loading" /></svws-ui-button>
					<svws-ui-button type="transparent" :disabled="!hatKompetenzUpdate" class="hover--danger ml-auto" @click="terminSelected.value = undefined; loescheKlausurtermine(termine)" v-if="termine.size() > 0" title="Alle Termine löschen"><span class="icon i-ri-delete-bin-line" />Alle löschen</svws-ui-button>
				</div>
			</div>
			<div class="grow overflow-auto grid gap-4 pt-2 -mt-2" style="grid-template-columns: repeat(auto-fill,minmax(22rem,1fr));">
				<template v-if="termine.size()">
					<template v-for="termin of termine" :key="termin.id">
						<s-gost-klausurplanung-schienen-termin :id="'termin' + termin.id"
							class="gost_klausurtermin" :class="dropOverCssClasses(termin)"
							:draggable :on-drag :on-drop :drag-data="() => dragData"
							@dragover="terminSelected.value=termin" @dragleave="terminSelected.value=undefined"
							@click="gotoSchienen(props.terminSelected.value?.id===termin.id?undefined:termin);$event.stopPropagation()"
							:benutzer-kompetenzen :k-man :termin="() => termin" :termin-selected="props.terminSelected.value?.id===termin.id"
							:loesche-klausurtermine :patch-klausurtermin :klausur-css-classes :create-schuelerklausur-termin
							:patch-klausur :goto-kalenderdatum :goto-raumzeit-termin />
					</template>
				</template>
				<template v-else>
					<div class="shadow-inner rounded-lg h-48" />
					<div class="shadow-inner rounded-lg h-48" />
					<div class="shadow-inner rounded-lg h-48" />
				</template>
			</div>
		</div>
		<div class="min-w-88 max-w-88 flex flex-col h-full overflow-y-auto">
			<div class="text-headline-md leading-none inline-flex gap-1">
				<template v-if="klausurKonflikte().size() > 0">
					<span class="icon i-ri-alert-fill icon-ui-danger" />
					<span>{{ klausurKonflikte().size() }} Kurse mit Konflikten</span>
				</template>
				<template v-else-if="anzahlProKwKonflikte(4).size() > 0">
					<span class="icon i-ri-alert-fill icon-ui-danger" />
					<span> Konflikte</span>
				</template>
				<template v-else-if="terminSelected !== undefined || dragData !== undefined">
					<span class="icon i-ri-checkbox-circle-fill icon-ui-success" />
					<span>Keine Konflikte</span>
				</template>
				<template v-else>
					<span class="opacity-50">Konflikte</span>
				</template>
			</div>
			<div v-if="klausurKonflikte().size() > 0" class="mt-5" :class="{'mb-16': anzahlProKwKonflikte(4).size() > 0}">
				<ul class="flex flex-col gap-3">
					<li v-for="klausur in klausurKonflikte()" :key="klausur.getKey().id">
						<span class="svws-ui-badge" :style="`color: var(--color-text-uistatic); background-color: ${ kMan().fachHTMLFarbeRgbaByKursklausur(klausur.getKey()) };`">{{ kMan().kursKurzbezeichnungByKursklausur(klausur.getKey()) }}</span>
						<div class="leading-tight">
							{{ [...klausur.getValue()].map(sid => kMan().schuelerGetByIdOrException(sid)?.vorname + ' ' + kMan().schuelerGetByIdOrException(sid)?.nachname).join(", ") }}
						</div>
					</li>
				</ul>
			</div>
			<div v-if="anzahlProKwKonflikte(4).size() > 0" class="mt-5">
				<div class="text-headline-md leading-tight mb-3">
					<div class="inline-flex gap-1">{{ anzahlProKwKonflikte(4).size() }} Schüler</div>
					<div class="opacity-50">Drei oder mehr Klausuren in einer KW</div>
				</div>
				<ul class="flex flex-col gap-4">
					<li v-for="konflikt in anzahlProKwKonflikte(4)" :key="konflikt.getKey()">
						<span class="font-bold">{{ kMan().schuelerGetByIdOrException(konflikt.getKey())?.vorname + ' ' + kMan().schuelerGetByIdOrException(konflikt.getKey())?.nachname }}</span>
						<div class="grid grid-cols-3 gap-x-1 gap-y-2 mt-0.5">
							<span v-for="klausur in konflikt.getValue()" :key="klausur.id" class="svws-ui-badge flex-col w-full" :style="`color: var(--color-text-uistatic); background-color: ${kMan().fachHTMLFarbeRgbaByKursklausur(kMan().kursklausurBySchuelerklausurTermin(klausur))};`">
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
		</div>
		<s-gost-klausurplanung-modal v-model:show="modalVorgaben" :text="modalError" :jump-to="gotoVorgaben" jump-to-text="Zu den Klausurvorgaben" abbrechen-text="OK" />
		<s-gost-klausurplanung-modal v-model:show="modalKlausurHatRaeume" text="Die Kursklausur hat bereits eine oder mehrere Raumzuweisungen. Beim Fortfahren werden diese gelöscht." :weiter="verschiebeKlausurTrotzRaumzuweisung" />
	</div>
</template>

<script setup lang="ts">

	import type { GostSchuelerklausurTermin, JavaMapEntry, JavaSet, List} from "@core";
	import { BenutzerKompetenz } from "@core";
	import {GostKursklausur, GostKlausurtermin, HashSet, KlausurterminblockungAlgorithmen, GostKlausurterminblockungDaten, KlausurterminblockungModusKursarten, KlausurterminblockungModusQuartale, DateUtils } from "@core";
	import { computed, ref, onMounted, onUnmounted, shallowRef } from 'vue';
	import type { GostKlausurplanungSchienenProps } from './SGostKlausurplanungSchienenProps';
	import type { GostKlausurplanungDragData, GostKlausurplanungDropZone } from "./SGostKlausurplanung";
	import type {DataTableColumn} from "@ui";

	const showModalAutomatischBlocken = ref<boolean>(false);

	const props = defineProps<GostKlausurplanungSchienenProps>();

	const hatKompetenzUpdate = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_AENDERN));

	const loading = ref<boolean>(false);

	const dragData = ref<GostKlausurplanungDragData>(undefined);
	const selected = shallowRef<GostKursklausur[]>([]);

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

	let klausurMoveDropZone: GostKlausurplanungDropZone = undefined;
	let klausurMoveDragData: GostKlausurplanungDragData = undefined;
	const modalKlausurHatRaeume = ref<boolean>(false);

	const modalError = ref<string | undefined>(undefined);

	async function erzeugeKursklausurenAusVorgabenOrModal() {
		const ergebnis = await props.erzeugeKursklausurenAusVorgaben(props.quartalsauswahl.value);
		if (ergebnis.description !== null) {
			modalError.value = ergebnis.description;
			modalVorgaben.value = true;
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
		"bg-ui-success": dragData.value !== undefined && (props.kMan().vorgabeByKursklausur(dragData.value as GostKursklausur).quartal === termin.quartal || termin.quartal === 0),
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
		showModalAutomatischBlocken.value = false;
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
		const konfliktZuEigenemTermin = termin === undefined ? false : props.kMan().konflikteAnzahlZuEigenemTerminGetByKursklausur(klausur) > 0;
		return {
			"bg-ui-danger text-ui-ondanger": konfliktZuEigenemTermin,
		}
	};

	const isMounted = ref(false);
	onMounted(() => {
		isMounted.value = true;
		if (props.terminSelected.value) {
			const scrollToElement = document.getElementById("termin" + props.terminSelected.value.id);
			if (scrollToElement)
				scrollToElement.scrollIntoView({ behavior: 'smooth', block: "nearest" });
		}
		window.addEventListener('click', handleClick);
	});

	onUnmounted(() => {
		window.removeEventListener('click', handleClick);
	});

	function calculateColumns() {
		const cols: DataTableColumn[] = [
			{ key: "kurs", label: "Kurs", minWidth: 6.25 },
			{ key: "kuerzel", label: "Lehrkraft" },
			{ key: "schriftlich", label: "Schriftlich", span: 0.5, align: "center", minWidth: 3.25 },
			{ key: "dauer", label: "Dauer", tooltip: "Dauer in Minuten", span: 0.5, align: "right", minWidth: 3.25 },
			{ key: "kursSchiene", label: "S", tooltip: "Schiene", span: 0.25, align: "right", minWidth: 2.75 },
		];
		if (props.quartalsauswahl.value === 0)
			cols.push({ key: "quartal", label: "Q", tooltip: "Quartal", span: 0.25, align: "right", minWidth: 2.75 })
		return cols;
	}

	const tableRowStyle = computed<string>(() => "grid-template-columns: minmax(6.25rem, 1fr) minmax(4rem, 1fr) minmax(3.25rem, 0.5fr) minmax(3.25rem, 0.5fr) minmax(2.75rem, 0.25fr)"
		+ ((props.quartalsauswahl.value === 0) ? " minmax(2.75rem, 0.25fr)" : ""));

	function handleClick(e: MouseEvent) {
		if (props.terminSelected.value === undefined)
			return;
		let target = e.target as HTMLElement | null;
		let isInsideTermin = false;
		while (target) {
			if (target.classList.contains("gost_klausurtermin") || target.classList.contains("tooltip")) {
				isInsideTermin = true;
				break;
			}
			target = target.parentElement;
		}
		if (!isInsideTermin) {
			void props.gotoSchienen(undefined);
		}
	}

	const cols = computed(() => calculateColumns());

</script>
