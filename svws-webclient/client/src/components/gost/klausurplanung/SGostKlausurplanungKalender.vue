<template>
	<!--<template v-if="kMan().stundenplanManagerExistsByAbschnitt(abschnitt!.id) && props.kMan().stundenplanManagerExistsByAbschnittAndKW(props.abschnitt!.id, kalenderwoche().jahr, kalenderwoche().kw)">-->
	<template v-if="kMan().stundenplanManagerGeladenByAbschnitt(abschnitt!.id) && kMan().stundenplanManagerExistsByAbschnitt(abschnitt!.id) && kMan().stundenplanManagerGetByAbschnittAndDatumOrNull(props.abschnitt!.id, props.kalenderdatum.value!) !== null">
		<Teleport to=".svws-ui-header--actions" v-if="isMounted">
			<svws-ui-modal-hilfe class="ml-auto"> <s-gost-klausurplanung-kalender-hilfe /> </svws-ui-modal-hilfe>
		</Teleport>
		<Teleport to=".router-tab-bar--subnav" v-if="isMounted">
			<s-gost-klausurplanung-quartal-auswahl :quartalsauswahl :halbjahr :zeige-alle-jahrgaenge :set-zeige-alle-jahrgaenge />
		</Teleport>
		<div class="page page-flex-row">
			<div class="min-w-88 max-w-88 flex flex-col gap-2">
				<div class="text-headline-md">In Planung</div>
				<div class="flex flex-col">
					<div v-if="jahrgangsdaten?.abiturjahr !== -1"
						@drop="onDrop(undefined)"
						@dragover="checkDropZoneTerminAuswahl"
						class="h-full"
						:class="[(terminSelected.value !== undefined && terminSelected.value.datum !== null) ? 'p-4 border-ui-danger ring-4 ring-ui-danger/10 border-2 rounded-xl border-dashed' : '']">
						<div>
							<div class="leading-tight flex flex-col gap-0.5" v-if="kMan().terminOhneDatumGetMengeByAbijahrAndHalbjahrAndQuartal(props.jahrgangsdaten.abiturjahr, props.halbjahr, props.quartalsauswahl.value).isEmpty()">
								<span>Aktuell keine Klausuren zu planen.</span>
								<span class="opacity-50">Bereits geplante Einträge können hier zurückgelegt werden.</span>
							</div>
						</div>
						<ul class="flex flex-col gap-0.5 -mx-3">
							<li v-for="termin in kMan().terminOhneDatumGetMengeByAbijahrAndHalbjahrAndQuartal(props.jahrgangsdaten.abiturjahr, props.halbjahr, props.quartalsauswahl.value)"
								:id="'termin' + termin.id"
								:key="termin.id"
								:data="termin"
								:draggable="isDraggable(termin)"
								@dragstart="onDrag(termin)"
								@dragend="onDrag(undefined)"
								@click="terminSelected.value?.id === termin.id ? onDrag(undefined) : onDrag(termin);$event.stopPropagation()"
								:class="{
									'border bg-ui-contrast-0 rounded-lg border-ui-contrast-10 my-3 cursor-grab': terminSelected.value !== undefined && terminSelected.value.id === termin.id,
									'cursor-pointer hover:bg-ui-contrast-10 rounded-lg pb-1': terminSelected.value !== undefined && terminSelected.value.id !== termin.id || terminSelected.value === undefined,
								}">
								<s-gost-klausurplanung-termin :termin
									:benutzer-kompetenzen
									:k-man
									:compact="terminSelected.value?.id !== termin.id"
									:quartalsauswahl
									:show-last-klausurtermin="true"
									:goto-kalenderdatum
									:goto-raumzeit-termin
									:patch-klausurtermin
									drag-icon>
									<template #datum><span /></template>
								</s-gost-klausurplanung-termin>
							</li>
						</ul>
					</div>
				</div>
			</div>
			<div class="svws-card-stundenplan max-w-480 h-full overflow-auto overflow-y-hidden">
				<template v-if="kalenderdatum">
					<s-gost-klausurplanung-kalender-stundenplan-ansicht :benutzer-kompetenzen
						:id="33"
						:kalenderdatum
						:jahrgangsdaten
						:halbjahr
						:manager="() => stundenplanManager()"
						:k-man
						:wochentyp="() => 0"
						:kurse-gefiltert
						:sum-schreiber
						:on-drop
						:on-drag
						:drag-data="() => terminSelected.value"
						:check-drop-zone-zeitraster
						:zeige-alle-jahrgaenge
						:goto-kalenderdatum
						:goto-raumzeit-termin
						:patch-klausurtermin
						:kalenderwoche
						:kursklausur-mouse-over="() => kursklausurMouseOver">
						<template #kwAuswahl>
							<div class="col-span-2 flex gap-0.5">
								<svws-ui-button type="icon" @click="navKalenderdatum(-7)" :disabled="!berechneKwzDatum(-7)"><span class="icon i-ri-arrow-left-s-line" /></svws-ui-button>
								<svws-ui-select title="Kalenderwoche" v-model="kalenderwochenauswahl" :items="kalenderwochen()" :item-text="kw => stundenplanManager().kalenderwochenzuordnungGetWocheAsString(kw)" headless />
								<svws-ui-button type="icon" @click="navKalenderdatum(+7)" :disabled="!berechneKwzDatum(+7)"><span class="icon i-ri-arrow-right-s-line" /></svws-ui-button>
							</div>
						</template>
					</s-gost-klausurplanung-kalender-stundenplan-ansicht>
				</template>
				<template v-else>
					<svws-ui-select title="Kalenderwoche" v-model="kalenderwochenauswahl" :items="kalenderwochen()"
						:item-text="kw => stundenplanManager().kalenderwochenzuordnungGetWocheAsString(kw)" />
				</template>
			</div>
			<div class="min-w-88 max-w-88 flex flex-col h-full overflow-y-auto">
				<div class="text-headline-md leading-none inline-flex gap-1">
					<template v-if="anzahlProKwKonflikte2(4, false).length === 0">
						<span class="icon i-ri-checkbox-circle-fill icon-ui-success" />
						<span>Keine Konflikte</span>
					</template>
					<template v-else-if="anzahlProKwKonflikte2(4, false).length > 0">
						<span class="icon i-ri-alert-fill icon-ui-danger" />
						<span> Konflikte</span>
					</template>
				</div>
				<div class="mb-12">
					<div v-if="anzahlProKwKonflikte2(4, false).length > 0">
						<div class="text-headline-sm leading-tight mb-6">
							<div class="inline-flex gap-2 justify-between w-full">
								<span>{{ anzahlProKwKonflikte2(4, false).length }} Schüler</span>
								<svws-ui-checkbox class="-mt-1" type="toggle" v-model="showMoreKonflikte">Alle anzeigen</svws-ui-checkbox>
							</div>
							<div class="opacity-50">Mehr als drei Klausuren in dieser Woche</div>
						</div>
						<ul class="flex flex-col gap-3">
							<li v-for="konflikt in anzahlProKwKonflikte(4, false, showMoreKonflikte)" :key="konflikt.getKey()">
								<span class="font-bold">{{ kMan().schuelerGetByIdOrException(konflikt.getKey())?.vorname + ' ' + kMan().schuelerGetByIdOrException(konflikt.getKey())?.nachname }}</span>
								<div class="grid grid-cols-3 gap-x-1 gap-y-2 mt-0.5">
									<span v-for="klausur in konflikt.getValue()" :key="klausur.id" class="svws-ui-badge text-center flex-col w-full" :style="`color: var(--color-text-ui-static); background-color: ${kMan().fachHTMLFarbeRgbaByKursklausur(kMan().kursklausurBySchuelerklausurTermin(klausur))};`" @mouseenter="kursklausurMouseOver = kMan().kursklausurBySchuelerklausurTermin(klausur)" @mouseleave="kursklausurMouseOver=undefined">
										<span class="text-button font-medium">{{ kMan().kursKurzbezeichnungByKursklausur(kMan().kursklausurBySchuelerklausurTermin(klausur)) }}</span>
										<span class="text-sm font-medium">{{ DateUtils.gibDatumGermanFormat(kMan().terminOrExceptionBySchuelerklausurTermin(klausur).datum !== null ? kMan().terminOrExceptionBySchuelerklausurTermin(klausur).datum! : stundenplanManager().datumGetByKwzAndZeitraster(kalenderwoche(), zeitrasterSelected!)) }}</span>									</span>
								</div>
							</li>
							<li v-if="!showMoreKonflikte" class="font-bold opacity-50">+ {{ anzahlProKwKonflikte(4, false, true).length - 3 }} weitere</li>
						</ul>
					</div>
				</div>
				<div>
					<span class="text-headline-md leading-none inline-flex gap-1">
						<template v-if="anzahlProKwKonflikte2(3, true).length === 0">
							<span class="icon i-ri-checkbox-circle-fill icon-ui-success" />
							<span>Keine Warnungen</span>
						</template>
						<template v-else-if="anzahlProKwKonflikte2(3, true).length > 0">
							<span class="icon i-ri-alert-line icon-ui-caution" />
							<span> Warnungen</span>
						</template>
					</span>
					<div v-if="anzahlProKwKonflikte2(3, true).length > 0" class="mt-5">
						<div class="text-headline-sm leading-tight mb-6">
							<div class="inline-flex gap-2 justify-between w-full">
								<span>{{ anzahlProKwKonflikte2(3, true).length }} Schüler</span>
								<svws-ui-checkbox class="-mt-1" type="toggle" v-model="showMoreWarnungen">Alle anzeigen</svws-ui-checkbox>
							</div>
							<div class="opacity-50">Drei Klausuren in dieser Woche</div>
						</div>
						<ul class="flex flex-col gap-3">
							<li v-for="konflikt in anzahlProKwKonflikte(3, true, showMoreWarnungen)" :key="konflikt.getKey()">
								<span class="font-bold">{{ kMan().schuelerGetByIdOrException(konflikt.getKey())?.vorname + ' ' + kMan().schuelerGetByIdOrException(konflikt.getKey())?.nachname }}</span>
								<div class="grid grid-cols-3 gap-x-1 gap-y-2 mt-0.5">
									<span v-for="klausur in konflikt.getValue()" :key="klausur.id" class="svws-ui-badge text-center flex-col w-full" :style="`color: var(--color-text-ui-static); background-color: ${kMan().fachHTMLFarbeRgbaByKursklausur(kMan().kursklausurBySchuelerklausurTermin(klausur))};`" @mouseenter="kursklausurMouseOver = kMan().kursklausurBySchuelerklausurTermin(klausur)" @mouseleave="kursklausurMouseOver=undefined">
										<span class="text-button font-medium">{{ kMan().kursKurzbezeichnungByKursklausur(kMan().kursklausurBySchuelerklausurTermin(klausur)) }}</span>
										<span class="text-sm font-medium">{{ DateUtils.gibDatumGermanFormat(kMan().terminOrExceptionBySchuelerklausurTermin(klausur).datum !== null ? kMan().terminOrExceptionBySchuelerklausurTermin(klausur).datum! : stundenplanManager().datumGetByKwzAndZeitraster(kalenderwoche(), zeitrasterSelected!)) }}</span>
									</span>
								</div>
							</li>
							<li v-if="!showMoreWarnungen" class="font-bold opacity-50">+ {{ anzahlProKwKonflikte(3, true, true).length - 3 }} weitere</li>
						</ul>
					</div>
				</div>
			</div>
		</div>
	</template>
	<s-gost-klausurplanung-modal v-model:show="modalKlausurHatRaeume" text="Der Klausurtermin ist Teil einer jahrgangsübergreifenden Raumplanung. Die Aktion hat daher Auswirkungen auf andere Termine." :weiter="verschiebeKlausurTrotzRaumzuweisung" />
</template>

<script setup lang="ts">
	import { ref, onMounted, computed } from "vue";
	import type { GostKlausurplanungKalenderProps } from "./SGostKlausurplanungKalenderProps";
	import type { GostKlausurplanungDragData, GostKlausurplanungDropZone } from "./SGostKlausurplanung";
	import type { Wochentag, StundenplanKalenderwochenzuordnung, List, GostKursklausur, JavaMapEntry, JavaSet, GostSchuelerklausurTermin} from "@core";
	import { StundenplanZeitraster} from "@core";
	import { GostKlausurtermin, DateUtils, ArrayList, BenutzerKompetenz} from "@core";

	const props = defineProps<GostKlausurplanungKalenderProps>();

	const hatKompetenzUpdate = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_AENDERN));

	const kalenderwoche = () => props.kMan().stundenplanManagerGetByAbschnittAndDatumOrException(props.abschnitt!.id, props.kalenderdatum.value!).kalenderwochenzuordnungGetByDatum(props.kalenderdatum.value!);

	const stundenplanManager = () => props.kMan().stundenplanManagerGetByAbschnittAndDatumOrException(props.abschnitt!.id, DateUtils.gibDatumDesMontagsOfJahrAndKalenderwoche(kalenderwoche().jahr, kalenderwoche().kw));

	const showMoreKonflikte = ref(false);
	const showMoreWarnungen = ref(false);
	const kursklausurMouseOver = ref<GostKursklausur | undefined>(undefined);

	const kalenderwochenauswahl = computed<StundenplanKalenderwochenzuordnung>({
		get: () => {
			return props.kMan().stundenplanManagerGetByAbschnittAndDatumOrException(props.abschnitt!.id, props.kalenderdatum.value!).kalenderwochenzuordnungGetByDatum(props.kalenderdatum.value!);
		},
		set: (value) => {
			void props.gotoKalenderdatum(DateUtils.gibDatumDesMontagsOfJahrAndKalenderwoche(value.jahr, value.kw), props.terminSelected.value);
		},
	});

	function kalenderwochen(): List<StundenplanKalenderwochenzuordnung> {
		return props.kMan().stundenplanManagerKalenderwochenzuordnungenGetMengeByAbschnitt(props.abschnitt!.id);
	}

	const modalKlausurHatRaeume = ref<boolean>(false);

	let klausurMoveDragData: GostKlausurtermin | undefined = undefined;
	let klausurMoveDropZone: GostKlausurplanungDropZone = undefined;

	async function verschiebeKlausurTrotzRaumzuweisung() {
		if (klausurMoveDragData)
			if (klausurMoveDropZone === undefined)
				await props.patchKlausurtermin(klausurMoveDragData.id, {datum: null, startzeit: null});
			else if (klausurMoveDropZone instanceof StundenplanZeitraster) {
				const date = stundenplanManager().datumGetByKwzAndZeitraster(kalenderwoche(), klausurMoveDropZone);
				await props.patchKlausurtermin(klausurMoveDragData.id, {datum: date, startzeit: klausurMoveDropZone.stundenbeginn});
			}
		props.terminSelected.value = undefined;

	}

	const zeitrasterSelected = ref<StundenplanZeitraster | undefined>(undefined);

	const anzahlProKwKonflikte2 = (threshold: number, thresholdOnly: boolean) => {
		let konflikte: JavaSet<JavaMapEntry<number, JavaSet<GostSchuelerklausurTermin>>> | null = null;
		if (props.terminSelected.value !== undefined && zeitrasterSelected.value !== undefined)
			konflikte = props.kMan().klausurenProSchueleridExceedingKWThresholdByTerminAndDatumAndThreshold(props.terminSelected.value, stundenplanManager().datumGetByKwzAndZeitraster(kalenderwoche(), zeitrasterSelected.value), threshold, thresholdOnly).entrySet();
		else
			konflikte = props.kMan().klausurenProSchueleridExceedingKWThresholdByKwAndAbijahrAndThreshold(kalenderwoche().kw, props.jahrgangsdaten.abiturjahr, threshold, thresholdOnly).entrySet();
		return konflikte.toArray() as JavaMapEntry<number, JavaSet<GostSchuelerklausurTermin>>[];
	}

	const anzahlProKwKonflikte = (threshold: number, thresholdOnly: boolean, showMore: boolean) => {
		const konflikte = anzahlProKwKonflikte2(threshold, thresholdOnly);
		return showMore ? konflikte : konflikte.slice(0, 3);
	}

	const berechneKwzDatum = (by: number) => {
		const datum = new Date(props.kalenderdatum.value!);
		datum.setDate(datum.getDate() + by);
		const datumStr = datum.getFullYear() + "-" + (datum.getMonth() + 1).toString().padStart(2, '0') + "-" + datum.getDate().toString().padStart(2, '0');// datum.toLocaleDateString("de-DE").slice(0, 10);
		const stundenplan = by > 0 ? props.kMan().stundenplanManagerGetByAbschnittAndDatumOrAfterOrNull(props.abschnitt!.id, datumStr) : props.kMan().stundenplanManagerGetByAbschnittAndDatumOrBeforeOrNull(props.abschnitt!.id, datumStr);
		if (stundenplan === null)
			return undefined;
		const kw = stundenplan.kalenderwochenzuordnungGetByDatum(datumStr);
		return DateUtils.gibDatumDesMontagsOfJahrAndKalenderwoche(kw.jahr, kw.kw);
	}

	async function navKalenderdatum(by: number) {
		await props.gotoKalenderdatum(berechneKwzDatum(by), props.terminSelected.value);
	}

	function checkDropZoneTerminAuswahl(event: DragEvent) : void {
		if (props.terminSelected.value?.datum !== null)
			event.preventDefault();
	}

	function checkDropZoneZeitraster(event: DragEvent, zeitraster: StundenplanZeitraster | undefined) : void {
		zeitrasterSelected.value = zeitraster;
		event.preventDefault();
	}

	function kurseGefiltert(datum: string, day: Wochentag, stunde: number) {
		const kursIds = new ArrayList<number>();
		if (props.terminSelected.value !== undefined)
			for (const klausur of props.kMan().kursklausurGetMengeByTermin(props.terminSelected.value))
				kursIds.add(klausur.idKurs);
		return props.kMan().stundenplanManagerGetByAbschnittAndDatumOrException(props.abschnitt!.id, datum).kursGetMengeGefiltertByWochentypAndWochentagAndStunde(kursIds, kalenderwoche().wochentyp, day, stunde);
	}

	function sumSchreiber(datum: string, day: Wochentag, stunde: number) {
		const kurse = kurseGefiltert(datum, day, stunde);
		let summe = 0;
		if (props.terminSelected.value !== undefined)
			for (const klausur of kurse)
				summe += props.kMan().kursAnzahlSchuelerGesamtByKursklausur(props.kMan().kursklausurGetByTerminAndKursid(props.terminSelected.value, klausur)!);
		return summe;
	}

	function isDraggable(object: any) : boolean {
		return hatKompetenzUpdate.value;
	}

	const onDrag = (data: GostKlausurplanungDragData) => {
		if (data instanceof GostKlausurtermin) {
			void props.gotoKalenderdatum(undefined, data);
			zeitrasterSelected.value = undefined;
		} else if (data === undefined) {
			void props.gotoKalenderdatum(undefined, undefined);
		}
	};

	const onDrop = async (zone: GostKlausurplanungDropZone) => {
		if (props.terminSelected.value !== undefined) {
			klausurMoveDropZone = zone;
			klausurMoveDragData = props.terminSelected.value;
			if (props.kMan().isKlausurenInFremdraeumenByTermin(props.terminSelected.value))
				modalKlausurHatRaeume.value = true;
			else
				await verschiebeKlausurTrotzRaumzuweisung();
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
	});

</script>

<style lang="postcss">

	@reference "../../../../../ui/src/assets/styles/index.css"

	.svws-kw-auswahl {
		@apply bg-ui-brand text-white rounded-md h-7 -my-1;

		.text-input--headless {
			@apply !px-4 !text-button;
		}

		.svws-dropdown-icon {
			@apply !hidden;
		}
	}

</style>