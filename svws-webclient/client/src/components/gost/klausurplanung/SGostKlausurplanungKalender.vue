<template>
	<template v-if="kMan().getStundenplanManagerOrNull()">
		<Teleport to=".svws-ui-header--actions" v-if="isMounted">
			<svws-ui-modal-hilfe class="ml-auto"> <s-gost-klausurplanung-kalender-hilfe /> </svws-ui-modal-hilfe>
		</Teleport>
		<Teleport to=".router-tab-bar--subnav" v-if="isMounted">
			<s-gost-klausurplanung-quartal-auswahl :quartalsauswahl :halbjahr :zeige-alle-jahrgaenge :set-zeige-alle-jahrgaenge />
		</Teleport>
		<div class="page--content page--content--full relative">
			<svws-ui-content-card title="In Planung">
				<div class="flex flex-col">
					<div v-if="jahrgangsdaten?.abiturjahr !== -1"
						@drop="onDrop(undefined)"
						@dragover="checkDropZoneTerminAuswahl"
						class="h-full"
						:class="[(terminSelected.value !== undefined && terminSelected.value.datum !== null) ? 'p-4 border-error ring-4 ring-error/10 border-2 rounded-xl border-dashed' : '']">
						<div>
							<div class="leading-tight flex flex-col gap-0.5" v-if="kMan().terminOhneDatumGetMengeByAbijahrAndHalbjahrAndQuartal(props.jahrgangsdaten.abiturjahr, props.halbjahr, props.quartalsauswahl.value).isEmpty()">
								<span>Aktuell keine Klausuren zu planen.</span>
								<span class="opacity-50">Bereits geplante Einträge können hier zurückgelegt werden.</span>
							</div>
						</div>
						<ul class="flex flex-col gap-0.5 -mx-3">
							<li v-for="termin in kMan().terminOhneDatumGetMengeByAbijahrAndHalbjahrAndQuartal(props.jahrgangsdaten.abiturjahr, props.halbjahr, props.quartalsauswahl.value)"
								:key="termin.id"
								:data="termin"
								:draggable="isDraggable(termin)"
								@dragstart="onDrag(termin)"
								@dragend="onDrag(undefined)"
								@click="terminSelected.value?.id === termin.id ? onDrag(undefined) : onDrag(termin);$event.stopPropagation()"
								:class="{
									'border bg-white dark:bg-black rounded-lg border-black/10 dark:border-white/10 my-3 cursor-grab': terminSelected.value !== undefined && terminSelected.value.id === termin.id,
									'cursor-pointer hover:bg-black/10 dark:hover:bg-white/10 rounded-lg pb-1': terminSelected.value !== undefined && terminSelected.value.id !== termin.id || terminSelected.value === undefined,
								}">
								<s-gost-klausurplanung-termin :termin
									:benutzer-kompetenzen
									:k-man
									:compact="terminSelected.value?.id !== termin.id"
									:quartalsauswahl
									:show-last-klausurtermin="true"
									:goto-kalenderwoche
									:goto-raumzeit-termin
									drag-icon>
									<template #datum><span /></template>
								</s-gost-klausurplanung-termin>
							</li>
						</ul>
					</div>
				</div>
			</svws-ui-content-card>
			<svws-ui-content-card class="svws-card-stundenplan">
				<template v-if="kalenderwoche.value">
					<s-gost-klausurplanung-kalender-stundenplan-ansicht :benutzer-kompetenzen
						:id="33"
						:kalenderwoche
						:jahrgangsdaten
						:halbjahr
						:manager="() => kMan().getStundenplanManager()"
						:k-man
						:wochentyp="() => 0"
						:kurse-gefiltert
						:sum-schreiber
						:on-drop
						:on-drag
						:drag-data="() => terminSelected.value"
						:check-drop-zone-zeitraster
						:zeige-alle-jahrgaenge
						:goto-kalenderwoche
						:goto-raumzeit-termin
						:kursklausur-mouse-over="() => kursklausurMouseOver">
						<template #kwAuswahl>
							<div class="col-span-2 flex gap-0.5 my-auto">
								<svws-ui-button type="icon" class="-my-1 w-7 h-7" @click="navKalenderwoche(-1)" :disabled="!kalenderwoche.value || !kMan().getStundenplanManager().kalenderwochenzuordnungGetPrevOrNull(kalenderwoche.value)"><span class="icon i-ri-arrow-left-s-line -m-0.5" /></svws-ui-button>
								<svws-ui-select class="flex-grow svws-kw-auswahl" title="Kalenderwoche" v-model="kalenderwoche.value" :items="kalenderwochen()" :item-text="(kw: StundenplanKalenderwochenzuordnung) => props.kMan().getStundenplanManager().kalenderwochenzuordnungGetWocheAsString(kw)" headless />
								<svws-ui-button type="icon" class="-my-1 w-7 h-7" @click="navKalenderwoche(+1)" :disabled="!kalenderwoche.value || !kMan().getStundenplanManager().kalenderwochenzuordnungGetNextOrNull(kalenderwoche.value)"><span class="icon i-ri-arrow-right-s-line -m-0.5" /></svws-ui-button>
							</div>
						</template>
					</s-gost-klausurplanung-kalender-stundenplan-ansicht>
				</template>
				<template v-else>
					<svws-ui-select title="Kalenderwoche" v-model="kalenderwoche.value" :items="kalenderwochen()"
						:item-text="(kw: StundenplanKalenderwochenzuordnung) => props.kMan().getStundenplanManager().kalenderwochenzuordnungGetWocheAsString(kw)" />
				</template>
			</svws-ui-content-card>
			<svws-ui-content-card>
				<template #title>
					<span class="text-headline-md leading-none inline-flex gap-1">
						<template v-if="anzahlProKwKonflikte2(4, false).length === 0">
							<span class="icon i-ri-checkbox-circle-fill icon-success -my-1" />
							<span>Keine Konflikte</span>
						</template>
						<template v-else-if="anzahlProKwKonflikte2(4, false).length > 0">
							<span class="icon i-ri-alert-fill icon-error -my-0.5" />
							<span> Konflikte</span>
						</template>
					</span>
				</template>
				<div class="mb-12">
					<div v-if="anzahlProKwKonflikte2(4, false).length > 0">
						<div class="text-headline-sm leading-tight mb-6 -mt-2">
							<div class="inline-flex gap-2 justify-between w-full">
								<span>{{ anzahlProKwKonflikte2(4, false).length }} Schüler</span>
								<svws-ui-checkbox class="-mt-1" type="toggle" v-model="showMoreKonflikte">Alle anzeigen</svws-ui-checkbox>
							</div>
							<div class="opacity-50">Mehr als drei Klausuren in dieser Woche</div>
						</div>
						<ul class="flex flex-col gap-3">
							<li v-for="konflikt in anzahlProKwKonflikte(4, false, showMoreKonflikte)" :key="konflikt.getKey()">
								<span class="font-bold">{{ kMan().getSchuelerMap().get(konflikt.getKey())?.vorname + ' ' + kMan().getSchuelerMap().get(konflikt.getKey())?.nachname }}</span>
								<div class="grid grid-cols-3 gap-x-1 gap-y-2 mt-0.5">
									<span v-for="klausur in konflikt.getValue()" :key="klausur.id" class="svws-ui-badge text-center flex-col w-full" :style="`--background-color: ${kMan().fachHTMLFarbeRgbaByKursklausur(kMan().kursklausurBySchuelerklausurTermin(klausur))};`" @mouseenter="kursklausurMouseOver = kMan().kursklausurBySchuelerklausurTermin(klausur)" @mouseleave="kursklausurMouseOver=undefined">
										<span class="text-button font-medium">{{ kMan().kursKurzbezeichnungByKursklausur(kMan().kursklausurBySchuelerklausurTermin(klausur)) }}</span>
										<span class="text-sm font-medium">{{ DateUtils.gibDatumGermanFormat(kMan().terminOrExceptionBySchuelerklausurTermin(klausur).datum !== null ? kMan().terminOrExceptionBySchuelerklausurTermin(klausur).datum! : kMan().getStundenplanManager().datumGetByKwzAndZeitraster(kalenderwoche.value, zeitrasterSelected!)) }}</span>									</span>
								</div>
							</li>
							<li v-if="!showMoreKonflikte" class="font-bold opacity-50">+ {{ anzahlProKwKonflikte(4, false, true).length - 3 }} weitere</li>
						</ul>
					</div>
				</div>
				<div>
					<span class="text-headline-md leading-none inline-flex gap-1">
						<template v-if="anzahlProKwKonflikte2(3, true).length === 0">
							<span class="icon i-ri-checkbox-circle-fill text-success -my-1" />
							<span>Keine Warnungen</span>
						</template>
						<template v-else-if="anzahlProKwKonflikte2(3, true).length > 0">
							<span class="icon i-ri-alert-line text-highlight -my-0.5" />
							<span> Warnungen</span>
						</template>
					</span>
					<div v-if="anzahlProKwKonflikte2(3, true).length > 0" class="mt-5">
						<div class="text-headline-sm leading-tight mb-6 -mt-2">
							<div class="inline-flex gap-2 justify-between w-full">
								<span>{{ anzahlProKwKonflikte2(3, true).length }} Schüler</span>
								<svws-ui-checkbox class="-mt-1" type="toggle" v-model="showMoreWarnungen">Alle anzeigen</svws-ui-checkbox>
							</div>
							<div class="opacity-50">Drei Klausuren in dieser Woche</div>
						</div>
						<ul class="flex flex-col gap-3">
							<li v-for="konflikt in anzahlProKwKonflikte(3, true, showMoreWarnungen)" :key="konflikt.getKey()">
								<span class="font-bold">{{ kMan().getSchuelerMap().get(konflikt.getKey())?.vorname + ' ' + kMan().getSchuelerMap().get(konflikt.getKey())?.nachname }}</span>
								<div class="grid grid-cols-3 gap-x-1 gap-y-2 mt-0.5">
									<span v-for="klausur in konflikt.getValue()" :key="klausur.id" class="svws-ui-badge text-center flex-col w-full" :style="`--background-color: ${kMan().fachHTMLFarbeRgbaByKursklausur(kMan().kursklausurBySchuelerklausurTermin(klausur))};`" @mouseenter="kursklausurMouseOver = kMan().kursklausurBySchuelerklausurTermin(klausur)" @mouseleave="kursklausurMouseOver=undefined">
										<span class="text-button font-medium">{{ kMan().kursKurzbezeichnungByKursklausur(kMan().kursklausurBySchuelerklausurTermin(klausur)) }}</span>
										<span class="text-sm font-medium">{{ DateUtils.gibDatumGermanFormat(kMan().terminOrExceptionBySchuelerklausurTermin(klausur).datum !== null ? kMan().terminOrExceptionBySchuelerklausurTermin(klausur).datum! : kMan().getStundenplanManager().datumGetByKwzAndZeitraster(kalenderwoche.value, zeitrasterSelected!)) }}</span>
									</span>
								</div>
							</li>
							<li v-if="!showMoreWarnungen" class="font-bold opacity-50">+ {{ anzahlProKwKonflikte(3, true, true).length - 3 }} weitere</li>
						</ul>
					</div>
				</div>
			</svws-ui-content-card>
		</div>
	</template>
	<s-gost-klausurplanung-modal :show="returnModalKlausurHatRaeume()" text="Der Klausurtermin ist Teil einer jahrgangsübergreifenden Raumplanung. Die Aktion hat daher Auswirkungen auf andere Termine." :weiter="verschiebeKlausurTrotzRaumzuweisung" />
</template>

<script setup lang="ts">
	import type { Ref } from "vue";
	import { ref, onMounted, computed } from "vue";
	import type { GostKlausurplanungKalenderProps } from "./SGostKlausurplanungKalenderProps";
	import type { GostKlausurplanungDragData, GostKlausurplanungDropZone } from "./SGostKlausurplanung";
	import type { Wochentag, StundenplanKalenderwochenzuordnung, List, GostKursklausur, JavaMapEntry, JavaSet, GostSchuelerklausurTermin} from "@core";
	import { StundenplanZeitraster} from "@core";
	import { GostKlausurtermin, DateUtils, ArrayList, BenutzerKompetenz} from "@core";

	const props = defineProps<GostKlausurplanungKalenderProps>();

	const hatKompetenzUpdate = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_AENDERN));

	const showMoreKonflikte = ref(false);
	const showMoreWarnungen = ref(false);
	const kursklausurMouseOver = ref<GostKursklausur | undefined>(undefined);

	function kalenderwochen(): List<StundenplanKalenderwochenzuordnung> {
		return props.kMan().getStundenplanManager().kalenderwochenzuordnungGetMengeAsList();
	}

	const modalKlausurHatRaeume = ref<boolean>(false);
	function returnModalKlausurHatRaeume(): () => Ref<boolean> {
		return () => modalKlausurHatRaeume;
	}

	let klausurMoveDragData: GostKlausurtermin | undefined = undefined;
	let klausurMoveDropZone: GostKlausurplanungDropZone = undefined;

	async function verschiebeKlausurTrotzRaumzuweisung() {
		if (klausurMoveDragData)
			if (klausurMoveDropZone === undefined)
				await props.patchKlausurtermin(klausurMoveDragData.id, {datum: null, startzeit: null});
			else if (klausurMoveDropZone instanceof StundenplanZeitraster) {
				const date = props.kMan().getStundenplanManager().datumGetByKwzAndZeitraster(props.kalenderwoche.value, klausurMoveDropZone);
				await props.patchKlausurtermin(klausurMoveDragData.id, {datum: date, startzeit: klausurMoveDropZone.stundenbeginn});
			}
		props.terminSelected.value = undefined;

	}


	const zeitrasterSelected = ref<StundenplanZeitraster | undefined>(undefined);

	const anzahlProKwKonflikte2 = (threshold: number, thresholdOnly: boolean) => {
		let konflikte: JavaSet<JavaMapEntry<number, JavaSet<GostSchuelerklausurTermin>>> | null = null;
		if (props.terminSelected.value !== undefined && zeitrasterSelected.value !== undefined)
			konflikte = props.kMan().klausurenProSchueleridExceedingKWThresholdByTerminAndDatumAndThreshold(props.terminSelected.value, props.kMan().getStundenplanManager().datumGetByKwzAndZeitraster(props.kalenderwoche.value, zeitrasterSelected.value), threshold, thresholdOnly).entrySet();
		else
			konflikte = props.kMan().klausurenProSchueleridExceedingKWThresholdByKwAndAbijahrAndThreshold(props.kalenderwoche.value.kw, props.jahrgangsdaten.abiturjahr, threshold, thresholdOnly).entrySet();
		return konflikte.toArray() as JavaMapEntry<number, JavaSet<GostSchuelerklausurTermin>>[];
	}

	const anzahlProKwKonflikte = (threshold: number, thresholdOnly: boolean, showMore: boolean) => {
		const konflikte = anzahlProKwKonflikte2(threshold, thresholdOnly);
		return showMore ? konflikte : konflikte.slice(0, 3);
	}

	async function navKalenderwoche(by: number) {
		if (by > 0) {
			const nextKw = props.kMan().getStundenplanManager().kalenderwochenzuordnungGetNextOrNull(props.kalenderwoche.value);
			if (nextKw !== null)
				props.kalenderwoche.value = nextKw;
			await props.gotoKalenderwoche(props.kalenderwoche.value);
		} else if (by < 0) {
			const prevKw = props.kMan().getStundenplanManager().kalenderwochenzuordnungGetPrevOrNull(props.kalenderwoche.value);
			if (prevKw !== null)
				props.kalenderwoche.value = prevKw;
			await props.gotoKalenderwoche(props.kalenderwoche.value);
		}
	}

	function checkDropZoneTerminAuswahl(event: DragEvent) : void {
		if (props.terminSelected.value?.datum !== null)
			event.preventDefault();
	}

	function isDropZoneZeitraster(zeitraster: StundenplanZeitraster) : boolean {
		return true;
		// const data = props.dragData();
		// if ((data === undefined) || (data instanceof StundenplanPausenaufsicht))
		// 	return false;
		// if ((data instanceof StundenplanKurs) || (data instanceof StundenplanKlassenunterricht))
		// 	return true;
		// const z = props.manager().zeitrasterGetByIdOrException(data.idZeitraster);
		// return !((z.wochentag === wochentag.id) && (z.unterrichtstunde === stunde));
	}

	function checkDropZoneZeitraster(event: DragEvent, zeitraster: StundenplanZeitraster) : void {
		if (isDropZoneZeitraster(zeitraster)) {
			zeitrasterSelected.value = zeitraster;
			event.preventDefault();
		}
	}

	function kurseGefiltert(day: Wochentag, stunde: number) {
		const kursIds = new ArrayList<number>();
		if (props.terminSelected.value !== undefined)
			for (const klausur of props.kMan().kursklausurGetMengeByTermin(props.terminSelected.value))
				kursIds.add(klausur.idKurs);
		return props.kMan().getStundenplanManager().kursGetMengeGefiltertByWochentypAndWochentagAndStunde(kursIds, props.kalenderwoche.value.wochentyp, day, stunde);
	}

	function sumSchreiber(day: Wochentag, stunde: number) {
		const kurse = kurseGefiltert(day, stunde);
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
			props.terminSelected.value = data;
			// if (data === undefined)
			zeitrasterSelected.value = undefined;
		} else if (data === undefined) {
			props.terminSelected.value = undefined;
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

	// const termineMit = computed(() => {
	// 	const terms = props.kMan().terminGetMengeAsList();
	// 	return (terms.toArray() as GostKlausurtermin[]).map(obj => ({
	// 		...obj,
	// 		startDate: obj.datum !== null ? new Date(obj.datum) : null,
	// 	}));
	// });

	const isMounted = ref(false);
	onMounted(() => {
		isMounted.value = true;
	});

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

.svws-ui-tab-content {
	@apply overflow-y-hidden items-start;

	.page--content {
		@apply h-full py-0 auto-rows-auto;

		.content-card {
			@apply max-h-full pt-8 pb-16 px-4 -mx-4 overflow-y-auto h-[unset];
			scrollbar-gutter: stable;
		}

		.svws-card-stundenplan {
			@apply overflow-y-hidden overflow-x-hidden pb-0;
			scrollbar-gutter: auto;
		}
	}
}
</style>

<style lang="postcss">
.svws-kw-auswahl {
	@apply bg-primary text-white rounded-md h-7 -my-1;

	.text-input--headless {
		@apply !px-4 !text-button ;
	}

	.svws-dropdown-icon {
		@apply !hidden;
	}
}

.svws-card-stundenplan {
	@apply pb-16;

	.content-card--content {
		@apply h-full overflow-x-scroll overflow-y-auto pt-px;
	}

	.svws-ui-stundenplan {
		@apply overflow-visible h-auto pb-8;
	}
}
</style>