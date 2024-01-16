<template>
	<Teleport to=".svws-ui-header--actions" v-if="isMounted">
		<svws-ui-modal-hilfe class="ml-auto"> <s-gost-klausurplanung-kalender-hilfe /> </svws-ui-modal-hilfe>
	</Teleport>
	<Teleport to=".router-tab-bar--subnav" v-if="isMounted">
		<s-gost-klausurplanung-quartal-auswahl :quartalsauswahl="quartalsauswahl" :halbjahr="halbjahr" />
	</Teleport>
	<div class="page--content page--content--full relative">
		<svws-ui-content-card title="In Planung">
			<div class="flex flex-col">
				<div v-if="jahrgangsdaten?.abiturjahr !== -1"
					@drop="onDrop(undefined)"
					@dragover="checkDropZoneTerminAuswahl"
					class="h-full">
					<div>
						<div class="leading-tight flex flex-col gap-0.5" v-if="termineOhne.length === 0">
							<span>Aktuell keine Klausuren zu planen.</span>
							<span class="opacity-50">Bereits geplante Einträge können hier zurückgelegt werden.</span>
						</div>
					</div>
					<ul class="flex flex-col gap-0.5 -mx-3">
						<li v-for="termin in termineOhne"
							:key="termin.id"
							:data="termin"
							:draggable="isDraggable(termin)"
							@dragstart="onDrag(termin)"
							@dragend="onDrag(undefined)"
							@click="dragData?.id === termin.id ? onDrag(undefined) : onDrag(termin);$event.stopPropagation()"
							:class="{
								'border bg-white dark:bg-black rounded-lg border-black/10 dark:border-white/10 my-3 cursor-grab': dragData !== undefined && dragData.id === termin.id,
								'cursor-pointer hover:bg-black/10 dark:hover:bg-white/10 rounded-lg pb-1': dragData !== undefined && dragData.id !== termin.id || dragData === undefined,
							}">
							<s-gost-klausurplanung-termin :termin="termin"
								:k-man="kMan"
								:map-schueler="mapSchueler"
								:compact="dragData?.id !== termin.id"
								:quartalsauswahl="quartalsauswahl"
								:show-last-klausurtermin="true"
								drag-icon>
								<template #datum><span /></template>
							</s-gost-klausurplanung-termin>
						</li>
					</ul>
				</div>
			</div>
		</svws-ui-content-card>
		<svws-ui-content-card>
			<template v-if="kwAuswahl">
				<s-gost-klausurplanung-kalender-stundenplan-ansicht :id="33" :kw-auswahl="kwAuswahl" :map-schueler="mapSchueler"
					:manager="() => stundenplanmanager" :k-man="kMan" :wochentyp="() => 0" :kurse-gefiltert="kurseGefiltert" :sum-schreiber="sumSchreiber"
					:on-drop="onDrop" :on-drag="onDrag" :drag-data="() => dragData" :check-drop-zone-zeitraster="checkDropZoneZeitraster">
					<template #kwAuswahl>
						<div class="col-span-2 flex gap-0.5 my-auto">
							<svws-ui-button type="icon" class="-my-1 w-7 h-7" @click="navKalenderwoche(-1)" :disabled="!kwAuswahl || !stundenplanmanager.kalenderwochenzuordnungGetPrevOrNull(kwAuswahl)"><i-ri-arrow-left-s-line class="-m-0.5" /></svws-ui-button>
							<svws-ui-select class="flex-grow svws-kw-auswahl bg-svws text-white rounded-md h-7" title="Kalenderwoche" v-model="kwAuswahl" :items="kalenderwochen()" :item-text="(kw: StundenplanKalenderwochenzuordnung) => props.stundenplanmanager.kalenderwochenzuordnungGetWocheAsString(kw)" headless />
							<svws-ui-button type="icon" class="-my-1 w-7 h-7" @click="navKalenderwoche(+1)" :disabled="!kwAuswahl || !stundenplanmanager.kalenderwochenzuordnungGetNextOrNull(kwAuswahl)"><i-ri-arrow-right-s-line class="-m-0.5" /></svws-ui-button>
						</div>
					</template>
				</s-gost-klausurplanung-kalender-stundenplan-ansicht>
			</template>
			<template v-else>
				<svws-ui-select title="Kalenderwoche" v-model="kwAuswahl" :items="kalenderwochen()"
					:item-text="(kw: StundenplanKalenderwochenzuordnung) => props.stundenplanmanager.kalenderwochenzuordnungGetWocheAsString(kw)" />
			</template>
		</svws-ui-content-card>
		<div class="-ml-4 space-y-6">
			<svws-ui-content-card class="border p-2">
				<template #title>
					<span class="text-headline-md leading-none inline-flex gap-1">
						<template v-if="anzahlProKwKonflikte2(4, false).length == 0">
							<i-ri-checkbox-circle-fill class="text-success -my-1" />
							<span>Keine Konflikte</span>
						</template>
						<template v-else-if="anzahlProKwKonflikte2(4, false).length > 0">
							<i-ri-alert-fill class="text-error -my-0.5" />
							<span> Konflikte</span>
						</template>
					</span>
				</template>
				<div v-if="anzahlProKwKonflikte2(4, false).length > 0" class="mt-5">
					<div class="text-headline-md leading-tight mb-3">
						<div class="inline-flex gap-1">{{ anzahlProKwKonflikte2(4, false).length }} Schüler:innen</div><svws-ui-checkbox class="float-right" type="toggle" v-model="showMoreKonflikte">Alle anzeigen</svws-ui-checkbox>
						<div class="opacity-50">Mehr als drei Klausuren in dieser Woche</div>
					</div>
					<ul class="flex flex-col gap-5">
						<li v-for="(konflikt, no) in anzahlProKwKonflikte(4, false, showMoreKonflikte)" :key="konflikt.getKey()" :class="showMoreKonflikte ? '' : 'opacity-' + (100 - no * 40 + (no === 2 ? 5 : 0))">
							<span class="font-bold">{{ mapSchueler.get(konflikt.getKey())?.vorname + ' ' + mapSchueler.get(konflikt.getKey())?.nachname }}</span>
							<div class="grid grid-cols-3 gap-x-1 gap-y-2 mt-0.5">
								<span v-for="klausur in konflikt.getValue()" :key="klausur.id" class="svws-ui-badge text-center flex-col w-full" :style="`--background-color: ${kMan().fachBgColorByKursklausur(klausur)};`">
									<span class="text-button font-medium">{{ kMan().kursKurzbezeichnungByKursklausur(klausur) }}</span>
									<span class="text-sm font-medium">{{ DateUtils.gibDatumGermanFormat(kMan().terminByKursklausur(klausur)!.datum !== null ? kMan().terminByKursklausur(klausur)!.datum! : stundenplanmanager.datumGetByKwzAndZeitraster(kwAuswahl, zeitrasterSelected!)) }}</span>
								</span>
							</div>
						</li>
					</ul>
				</div>
			</svws-ui-content-card>
			<svws-ui-content-card class="border p-2 op">
				<template #title>
					<span class="text-headline-md leading-none inline-flex gap-1">
						<template v-if="anzahlProKwKonflikte2(3, true).length == 0">
							<i-ri-checkbox-circle-fill class="text-success -my-1" />
							<span>Keine Warnungen</span>
						</template>
						<template v-else-if="anzahlProKwKonflikte2(3, true).length > 0">
							<i-ri-alert-fill class="text-highlight -my-0.5" />
							<span> Warnungen</span>
						</template>
					</span>
				</template>
				<div v-if="anzahlProKwKonflikte2(3, true).length > 0" class="mt-5">
					<div class="text-headline-md leading-tight mb-3">
						<div class="inline-flex gap-1">{{ anzahlProKwKonflikte2(3, true).length }} Schüler:innen</div><svws-ui-checkbox class="float-right" type="toggle" v-model="showMoreWarnungen">Alle anzeigen</svws-ui-checkbox>
						<div class="opacity-50">Drei Klausuren in dieser Woche</div>
					</div>
					<ul class="flex flex-col gap-5">
						<li v-for="(konflikt, no) in anzahlProKwKonflikte(3, true, showMoreWarnungen)" :key="konflikt.getKey()" :class="showMoreWarnungen ? '' : 'opacity-' + (100 - no * 40 + (no === 2 ? 5 : 0))">
							<span class="font-bold">{{ mapSchueler.get(konflikt.getKey())?.vorname + ' ' + mapSchueler.get(konflikt.getKey())?.nachname }}</span>
							<div class="grid grid-cols-3 gap-x-1 gap-y-2 mt-0.5">
								<span v-for="klausur in konflikt.getValue()" :key="klausur.id" class="svws-ui-badge text-center flex-col w-full" :style="`--background-color: ${kMan().fachBgColorByKursklausur(klausur)};`">
									<span class="text-button font-medium">{{ kMan().kursKurzbezeichnungByKursklausur(klausur) }}</span>
									<span class="text-sm font-medium">{{ DateUtils.gibDatumGermanFormat(kMan().terminByKursklausur(klausur)!.datum !== null ? kMan().terminByKursklausur(klausur)!.datum! : stundenplanmanager.datumGetByKwzAndZeitraster(kwAuswahl, zeitrasterSelected!)) }}</span>
								</span>
							</div>
						</li>
					</ul>
				</div>
			</svws-ui-content-card>
		</div>
	</div>
</template>

<script setup lang="ts">
	import type { Wochentag, StundenplanKalenderwochenzuordnung, List, GostKursklausur, HashSet, JavaMapEntry, JavaSet} from "@core";
	import type { GostKlausurplanungDragData, GostKlausurplanungDropZone } from "./SGostKlausurplanung";
	import type { GostKlausurplanungKalenderProps } from "./SGostKlausurplanungKalenderProps";
	import { GostKlausurtermin, StundenplanZeitraster, DateUtils, ZulaessigesFach, ArrayList} from "@core";
	import { computed, ref, onMounted } from "vue";

	const props = defineProps<GostKlausurplanungKalenderProps>();
	const showMoreKonflikte = ref(false);
	const showMoreWarnungen = ref(false);

	// eslint-disable-next-line vue/no-setup-props-destructure
	const kwAuswahl = ref<StundenplanKalenderwochenzuordnung>(props.stundenplanmanager.kalenderwochenzuordnungGetByDatum(new Date().toISOString()));

	function kalenderwochen(): List<StundenplanKalenderwochenzuordnung> {
		return props.stundenplanmanager.kalenderwochenzuordnungGetMengeAsList();
	}

	const dragData = ref<GostKlausurplanungDragData>(undefined);
	const zeitrasterSelected = ref<StundenplanZeitraster | undefined>(undefined);

	const anzahlProKwKonflikte2 = (threshold: number, thresholdOnly: boolean) => {
		let konflikte: JavaSet<JavaMapEntry<number, HashSet<GostKursklausur>>> | null = null;
		if (dragData.value !== undefined && dragData.value instanceof GostKlausurtermin && zeitrasterSelected.value !== undefined)
			konflikte = props.kMan().klausurenProSchueleridExceedingKWThresholdByTerminAndDatumAndThreshold(dragData.value, props.stundenplanmanager.datumGetByKwzAndZeitraster(kwAuswahl.value, zeitrasterSelected.value), threshold, thresholdOnly).entrySet();
		konflikte = props.kMan().klausurenProSchueleridExceedingKWThresholdByKwAndThreshold(kwAuswahl.value.kw, threshold, thresholdOnly).entrySet();
		return konflikte.toArray() as JavaMapEntry<number, HashSet<GostKursklausur>>[];
	}

	const anzahlProKwKonflikte = (threshold: number, thresholdOnly: boolean, showMore: boolean) => {
		let konflikte = anzahlProKwKonflikte2(threshold, thresholdOnly);
		return showMore ? konflikte : konflikte.slice(0, 3);
	}

	function navKalenderwoche(by: number) {
		if (by > 0) {
			const nextKw = props.stundenplanmanager.kalenderwochenzuordnungGetNextOrNull(kwAuswahl.value);
			if (nextKw !== null)
				kwAuswahl.value = nextKw;
		} else if (by < 0) {
			const prevKw = props.stundenplanmanager.kalenderwochenzuordnungGetPrevOrNull(kwAuswahl.value);
			if (prevKw !== null)
				kwAuswahl.value = prevKw;
		}
	}

	function checkDropZoneTerminAuswahl(event: DragEvent) : void {
		if (dragData.value instanceof GostKlausurtermin && dragData.value?.datum !== null)
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
		if (dragData.value !== undefined && dragData.value instanceof GostKlausurtermin)
			for (const klausur of props.kMan().kursklausurGetMengeByTerminid(dragData.value.id))
				kursIds.add(klausur.idKurs);
		return props.stundenplanmanager.kursGetMengeGefiltertByWochentypAndWochentagAndStunde(kursIds, 1, day, stunde);
	}

	function sumSchreiber(day: any, stunde: number) {
		const kurse = kurseGefiltert(day, stunde);
		let summe = 0;
		if (dragData.value !== undefined)
			for (const klausur of kurse)
				summe += props.kMan().kursAnzahlSchuelerGesamtByKursklausur(props.kMan().kursklausurGetByTerminidAndKursid(dragData.value.id, klausur)!);
		return summe;
	}

	function isDraggable(object: any) : boolean {
		return true;
	}

	const onDrag = (data: GostKlausurplanungDragData) => {
		dragData.value = data;
		if (data === undefined)
			zeitrasterSelected.value = undefined;
	};

	const onDrop = async (zone: GostKlausurplanungDropZone) => {
		if (dragData.value instanceof GostKlausurtermin)
			if (zone === undefined)
				await props.patchKlausurtermin(dragData.value.id, {datum: null, startzeit: null});
			else if (zone instanceof StundenplanZeitraster) {
				const date = props.stundenplanmanager.datumGetByKwzAndZeitraster(kwAuswahl.value, zone);
				await props.patchKlausurtermin(dragData.value.id, {datum: date, startzeit: date !== null ? zone.stundenbeginn : null});
			}
	};

	const termineOhne = computed(() => {
		const a = [];
		for (const termin of props.kMan().terminGetMengeByHalbjahrAndQuartal(props.halbjahr, props.quartalsauswahl.value, true))
			if (termin.datum === null)
				a.push(termin);
		return a;
	});

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

.svws-kw-auswahl {
  .wrapper--headless {
    @apply opacity-0 -ml-4 mt-1;
  }
}
</style>
