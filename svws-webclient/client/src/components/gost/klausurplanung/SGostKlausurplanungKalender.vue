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
								:kursklausurmanager="kursklausurmanager"
								:map-lehrer="mapLehrer"
								:kursmanager="kursmanager"
								:compact="dragData?.id !== termin.id"
								:quartalsauswahl="quartalsauswahl"
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
				<s-gost-klausurplanung-kalender-stundenplan-ansicht :id="33" :kw-auswahl="kwAuswahl"
					:manager="() => stundenplanmanager" :kursmanager="kursmanager" :kursklausurmanager="kursklausurmanager" :wochentyp="() => 0" :kurse-gefiltert="kurseGefiltert" :sum-schreiber="sumSchreiber"
					:on-drop="onDrop" :on-drag="onDrag" :drag-data="() => dragData" :faecher-manager="faecherManager" :map-lehrer="mapLehrer" :check-drop-zone-zeitraster="checkDropZoneZeitraster">
					<template #kwAuswahl>
						<div class="col-span-2 flex gap-0.5 my-auto">
							<svws-ui-button type="icon" class="-my-1 w-7 h-7" @click="navKalenderwoche(-1)" :disabled="!kwAuswahl || !stundenplanmanager.kalenderwochenzuordnungGetPrevOrNull(kwAuswahl)"><i-ri-arrow-left-s-line class="-m-0.5" /></svws-ui-button>
							<svws-ui-select title="Kalenderwoche" v-model="kwAuswahl" :items="kalenderwochen()" :item-text="kw => kw.kw.toString()" headless />
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
		<svws-ui-content-card class="-ml-4">
			<template #title>
				<span class="text-headline-md leading-none inline-flex gap-1">
					<template v-if="anzahlProKwKonflikte(3).isEmpty()">
						<i-ri-checkbox-circle-fill class="text-success -my-1" />
						<span>Keine Konflikte</span>
					</template>
					<template v-else-if="anzahlProKwKonflikte(3).size() > 0">
						<i-ri-alert-fill class="text-error -my-0.5" />
						<span> Konflikte</span>
					</template>
					<template v-else>
						<span class="opacity-50">Konflikte</span>
					</template>
				</span>
			</template>
			<div v-if="anzahlProKwKonflikte(3).size() > 0" class="mt-5">
				<div class="text-headline-md leading-tight mb-3">
					<div class="inline-flex gap-1">{{ anzahlProKwKonflikte(3).size() }} Schüler:innen</div>
					<div class="opacity-50">Drei oder mehr Klausuren in dieser KW</div>
				</div>
				<ul class="flex flex-col gap-5">
					<li v-for="konflikt in anzahlProKwKonflikte(3)" :key="konflikt.getKey()">
						<span class="font-bold">{{ mapSchueler.get(konflikt.getKey())?.vorname + ' ' + mapSchueler.get(konflikt.getKey())?.nachname }}</span>
						<div class="grid grid-cols-3 gap-x-1 gap-y-2 mt-0.5">
							<span v-for="klausur in konflikt.getValue()" :key="klausur.id" class="svws-ui-badge text-center flex-col w-full" :style="`--background-color: ${getBgColor(klausur.kursKurzbezeichnung.split('-')[0])};`">
								<span class="text-button font-medium">{{ klausur.kursKurzbezeichnung }}</span>
								<span class="text-sm font-medium">{{ DateUtils.gibDatumGermanFormat(kursklausurmanager().terminByKursklausur(klausur)!.datum !== null ? kursklausurmanager().terminByKursklausur(klausur)!.datum! : stundenplanmanager.datumGetByKwzAndZeitraster(kwAuswahl, zeitrasterSelected!)) }}</span>
							</span>
						</div>
					</li>
				</ul>
			</div>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">
	import type { Wochentag, StundenplanKalenderwochenzuordnung, List} from "@core";
	import type { GostKlausurplanungDragData, GostKlausurplanungDropZone } from "./SGostKlausurplanung";
	import type { GostKlausurplanungKalenderProps } from "./SGostKlausurplanungKalenderProps";
	import { GostKlausurtermin, StundenplanZeitraster, DateUtils, ZulaessigesFach, ArrayList} from "@core";
	import { computed, ref, onMounted } from "vue";

	const props = defineProps<GostKlausurplanungKalenderProps>();

	// eslint-disable-next-line vue/no-setup-props-destructure
	const kwAuswahl = ref<StundenplanKalenderwochenzuordnung>(props.stundenplanmanager.kalenderwochenzuordnungGetByDatum(new Date().toISOString()));

	function kalenderwochen(): List<StundenplanKalenderwochenzuordnung> {
		return props.stundenplanmanager.kalenderwochenzuordnungGetMengeAsList();
	}

	const dragData = ref<GostKlausurplanungDragData>(undefined);
	const zeitrasterSelected = ref<StundenplanZeitraster | undefined>(undefined);

	const anzahlProKwKonflikte = (threshold: number) => {
		if (dragData.value !== undefined && dragData.value instanceof GostKlausurtermin && zeitrasterSelected.value !== undefined)
			return props.kursklausurmanager().klausurenProSchueleridExceedingKWThresholdByTerminAndDatumAndThreshold(dragData.value, props.stundenplanmanager.datumGetByKwzAndZeitraster(kwAuswahl.value, zeitrasterSelected.value), threshold);
		return props.kursklausurmanager().klausurenProSchueleridExceedingKWThresholdByKwAndThreshold(kwAuswahl.value.kw, threshold);
	}

	const getBgColor = (kuerzel: string | null) => ZulaessigesFach.getByKuerzelASD(kuerzel).getHMTLFarbeRGBA(1.0); // TODO: Fachkuerzel für Kursklausur


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
			for (const klausur of props.kursklausurmanager().kursklausurGetMengeByTerminid(dragData.value.id))
				kursIds.add(klausur.idKurs);
		return props.stundenplanmanager.kursGetMengeGefiltertByWochentypAndWochentagAndStunde(kursIds, 1, day, stunde);
	}

	function sumSchreiber(day: any, stunde: number) {
		const kurse = kurseGefiltert(day, stunde);
		let summe = 0;
		if (dragData.value !== undefined)
			for (const klausur of kurse)
				summe += props.kursklausurmanager().kursklausurGetByTerminidAndKursid(dragData.value.id, klausur)!.schuelerIds.size();
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
		for (const termin of props.kursklausurmanager().terminGetMengeByQuartal(props.quartalsauswahl.value, true))
			if (termin.datum === null)
				a.push(termin);
		return a;
	});

	// const termineMit = computed(() => {
	// 	const terms = props.kursklausurmanager().terminGetMengeAsList();
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
