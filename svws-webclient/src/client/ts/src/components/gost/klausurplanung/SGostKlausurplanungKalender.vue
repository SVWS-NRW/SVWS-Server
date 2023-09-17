<template>
	<svws-ui-sub-nav>
		<s-gost-klausurplanung-quartal-auswahl :quartalsauswahl="quartalsauswahl" />
		<svws-ui-modal-hilfe class="ml-auto"> <s-gost-klausurplanung-kalender-hilfe /> </svws-ui-modal-hilfe>
	</svws-ui-sub-nav>

	<div class="page--content page--content--full min-w-fit gap-x-8 2xl:gap-x-16 relative">
		<svws-ui-content-card class="w-full">
			<div class="flex gap-4 mt-4 h-screen w-full">
				<div class="flex flex-col h-full w-1/4">
					<div class="text-headline-md">Zu verplanen:</div>
					<div v-if="jahrgangsdaten?.abiturjahr !== -1"
						:class="dropOverCssClasses()"
						@drop="onDrop(undefined)"
						@dragover="checkDropZoneTerminAuswahl"
						class="h-full">
						<ul class="flex flex-col gap-y-1">
							<li v-for="termin in termineOhne"
								:key="termin.id"
								:data="termin"
								:draggable="isDraggable(termin)"
								@dragstart="onDrag(termin)"
								@dragend="onDrag(undefined)">
								<s-gost-klausurplanung-termin :termin="termin"
									:kursklausurmanager="kursklausurmanager"
									:map-lehrer="mapLehrer"
									:kursmanager="kursmanager"
									class="rounded bg-dark-20 p-2"
									:class="{'bg-green-100': dragData !== undefined && dragData.id === termin.id}" />
							</li>
						</ul>
					</div>
				</div>
				<div class="w-full">
					<div class="flex flex-row gap-2 mb-2">
						<svws-ui-button @click="navKalenderwoche(-1)"><i-ri-arrow-left-double-fill /></svws-ui-button>
						<svws-ui-multi-select title="Kalenderwoche" v-model="kwAuswahl" :items="kalenderwochen()"
							:class="{'print:hidden': !kwAuswahl}"
							removable
							:item-text="(kw: StundenplanKalenderwochenzuordnung) => props.stundenplanmanager.kalenderwochenzuordnungGetWocheAsString(kw)" />
						<svws-ui-button @click="navKalenderwoche(+1)"><i-ri-arrow-right-double-fill /></svws-ui-button>
					</div>

					<s-gost-klausurplanung-kalender-stundenplan-ansicht :id="33" :kw-auswahl="kwAuswahl"
						:manager="() => stundenplanmanager" :kursmanager="kursmanager" :kursklausurmanager="kursklausurmanager" :wochentyp="() => 0" :kurse-gefiltert="kurseGefiltert" :sum-schreiber="sumSchreiber"
						:on-drop="onDrop" :on-drag="onDrag" :drag-data="() => dragData" :faecher-manager="faecherManager" :map-lehrer="mapLehrer" />
				</div>
			</div>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">
	import { computed, ref } from "vue";
	import { GostKlausurtermin, StundenplanZeitraster} from "@core";
	import { ArrayList} from "@core";
	import type { Wochentag , GostKursklausur, StundenplanKalenderwochenzuordnung, List} from "@core";
	import type { GostKlausurplanungKalenderProps } from "./SGostKlausurplanungKalenderProps";
	import type { GostKlausurplanungDragData, GostKlausurplanungDropZone } from "./SGostKlausurplanung";

	const props = defineProps<GostKlausurplanungKalenderProps>();

	const kwAuswahl = ref<StundenplanKalenderwochenzuordnung>(props.stundenplanmanager.kalenderwochenzuordnungGetByDatum(new Date().toISOString()));

	function kalenderwochen(): List<StundenplanKalenderwochenzuordnung> {
		return props.stundenplanmanager.kalenderwochenzuordnungGetMengeAsList();
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

	const dropOverCssClasses = () => ({
		"bg-green-100": dragData.value !== null// && dragData.value.datum !== null,
	});

	function checkDropZoneTerminAuswahl(event: DragEvent) : void {
		if (dragData.value instanceof GostKlausurtermin && dragData.value?.datum !== null)
			event.preventDefault();
	}

	function kurseGefiltert(day: Wochentag, stunde: number) {
		const kursIds = new ArrayList<number>();
		if (dragData.value !== undefined && dragData.value instanceof GostKlausurtermin) {
			for (const klausur of props.kursklausurmanager().kursklausurGetMengeByTerminid(dragData.value.id).toArray() as GostKursklausur[]) {
				kursIds.add(klausur.idKurs);
			}
		}
		return props.stundenplanmanager.kursGetMengeGefiltertByWochentypAndWochentagAndStunde( kursIds, 1, day, stunde);
	}

	function sumSchreiber(day: any, stunde: number) {
		const kurse = kurseGefiltert(day, stunde);
		let summe = 0;
		for (const klausur of kurse) {
			summe += props.kursklausurmanager().kursklausurGetByTerminidAndKursid(dragData.value!.id, klausur)!.schuelerIds.size();
		}
		return summe;
	}

	const dragData = ref<GostKlausurplanungDragData>(undefined);

	function isDraggable(object: any) : boolean {
		return true;
	}

	const onDrag = (data: GostKlausurplanungDragData) => {
		dragData.value = data;
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

	const termineOhne = computed(() => (props.kursklausurmanager().terminGetMengeByQuartal(props.quartalsauswahl.value, true).toArray() as GostKlausurtermin[]).filter(termin => termin.datum === null));

	// const termineMit = computed(() => {
	// 	const terms = props.kursklausurmanager().terminGetMengeAsList();
	// 	return (terms.toArray() as GostKlausurtermin[]).map(obj => ({
	// 		...obj,
	// 		startDate: obj.datum !== null ? new Date(obj.datum) : null,
	// 	}));
	// });


</script>

<style lang="postcss">
</style>
