<template>
	<svws-ui-sub-nav>
		<s-gost-klausurplanung-quartal-auswahl :quartalsauswahl="quartalsauswahl" />
		<svws-ui-modal-hilfe class="ml-auto"> <s-gost-klausurplanung-kalender-hilfe /> </svws-ui-modal-hilfe>
	</svws-ui-sub-nav>

	<svws-ui-content-card class="page--content page--content--full min-w-fit gap-x-8 2xl:gap-x-16 relative">
		<div class="flex gap-4 mt-4 h-screen">
			<div class="flex flex-col w-1/4 h-full">
				<div class="text-headline-md">Zu verplanen:</div>
				<svws-ui-drop-data v-if="jahrgangsdaten?.abiturjahr !== -1"
					:class="dropOverCssClasses()"
					@drop="onDrop($event)"
					class="h-full">
					<ul class="flex flex-col gap-y-1">
						<li v-for="termin in termineOhne"
							:key="termin.id"
							:data="termin"
							:draggable="isDraggable(termin)"
							@dragstart="onDrag(termin)"
							@dragend="onDrag(undefined)">
							<s-gost-klausurplanung-kalender-termin :kursklausurmanager="kursklausurmanager"
								:jahrgangsdaten="jahrgangsdaten"
								:faecher-manager="faecherManager"
								:map-lehrer="mapLehrer"
								:termin="termin"
								:kursmanager="kursmanager"
								:class="{'bg-green-100': dragData !== undefined && dragData.id === termin.id}" />
						</li>
					</ul>
				</svws-ui-drop-data>
			</div>
			<div class="w-full">
				<svws-ui-multi-select title="Kalenderwoche" v-model="kwAuswahl" :items="kalenderwochen()"
					:class="{'print:hidden': !kwAuswahl}"
					removable
					:item-text="(kw: StundenplanKalenderwochenzuordnung) => props.stundenplanmanager.kalenderwochenzuordnungGetWocheAsString(kw)" />

				<s-gost-klausurplanung-kalender-stundenplan-ansicht :id="33" :kw-auswahl="kwAuswahl"
					:manager="() => stundenplanmanager" :kursmanager="kursmanager" :kursklausurmanager="kursklausurmanager" :wochentyp="() => 0" :kurse-gefiltert="kurseGefiltert" :sum-schreiber="sumSchreiber"
					:on-drop="onDrop" :on-drag="onDrag" :drag-data="() => dragData" :faecher-manager="faecherManager" :map-lehrer="mapLehrer" />
				<!--<calendar-view :display-period-uom="displayPeriodUom"
					:display-period-count="displayPeriodUom === 'month' ? 1 : 2"
					:starting-day-of-week="1"
					:enable-drag-drop="false"
					:disabled_items="termineMit"
					:show-date="showDate"
					disabled_drop-on-date="onDrop"
					class="theme-default"
					current-period-label="Aktuell"
					:display-week-numbers="true">
					<template #header="{ headerProps }">
						<calendar-view-header :header-props="headerProps" @input="setShowDate" />
					</template>
					<template #dayContent="{day}">
						<StundenplanTag :tag="day.getDay()" v-if="day.getDay() < 6 && day.getDay() > 0">
							<StundenplanEntry v-for="stunde of stundenplanmanager.getListZeitrasterZuWochentag(Wochentag.fromIDorException(day.getDay()))"
								:key="stunde.id"
								:entry="stunde"
								class="hover:bg-slate-400 select-none cursor-pointer">
								<svws-ui-drop-data class="h-full w-full" @drop="onDrop($event, day, stunde.unterrichtstunde)">
									<StundenplanStunde :stunde="stunde">
										<span v-if="dragTermin !== null && sumSchreiber(day, stunde.unterrichtstunde) > 0">{{ sumSchreiber(day, stunde.unterrichtstunde) }}</span>
										<span v-for="kurs in kurseGefiltert(day, stunde.unterrichtstunde)" :key="kurs">{{ kursInfos(kurs) }}&nbsp;</span>
										<svws-ui-drag-data v-if="!kursklausurmanager().getKlausurtermineByDatumUhrzeit(formatDate(day)!, stunde, stundenplanmanager).isEmpty()"
											:data="kursklausurmanager().getKlausurtermineByDatumUhrzeit(formatDate(day)!, stunde, stundenplanmanager).get(0)"
											@drag-start="dragTermin = kursklausurmanager().getKlausurtermineByDatumUhrzeit(formatDate(day)!, stunde, stundenplanmanager).get(0)"
											@drag-end="dragTermin = null">
											<s-gost-klausurplanung-kalender-termin-short :kursklausurmanager="kursklausurmanager"
												:termin="kursklausurmanager().getKlausurtermineByDatumUhrzeit(formatDate(day)!, stunde, stundenplanmanager).get(0)"
												:faecher-manager="faecherManager"
												:map-lehrer="mapLehrer"
												:kursmanager="kursmanager"
												:class="{'opacity-40': dragTermin !== null}" />
										</svws-ui-drag-data>
									</StundenplanStunde>
								</svws-ui-drop-data>
							</StundenplanEntry>
						</StundenplanTag>
					</template>
				</calendar-view>-->
			</div>
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">
	import { computed, ref } from "vue";
	import { GostKlausurtermin, StundenplanZeitraster} from "@core";
	import { ArrayList} from "@core";
	import type { Wochentag , GostKursklausur, StundenplanKalenderwochenzuordnung, List} from "@core";
	import type { GostKlausurplanungKalenderProps } from "./SGostKlausurplanungKalenderProps";
	import type { KlausurplanungKalenderDragData, KlausurplanungKalenderDropZone } from "./SGostKlausurplanungKalenderStundenplanAnsichtProps";

	const props = defineProps<GostKlausurplanungKalenderProps>();

	const kwAuswahl = ref<StundenplanKalenderwochenzuordnung>(props.stundenplanmanager.kalenderwochenzuordnungGetByDatum(new Date().toISOString()));

	function kalenderwochen(): List<StundenplanKalenderwochenzuordnung> {
		return props.stundenplanmanager.kalenderwochenzuordnungGetMengeAsList();
	}

	const dropOverCssClasses = () => ({
		"bg-green-100": dragData.value !== null// && dragData.value.datum !== null,
	});

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

	const dragData = ref<KlausurplanungKalenderDragData>(undefined);

	function isDraggable(object: any) : boolean {
		return true;
	}

	const onDrag = (data: KlausurplanungKalenderDragData) => {
		dragData.value = data;
	};

	const onDrop = async (zone: KlausurplanungKalenderDropZone) => {
		if (dragData.value instanceof GostKlausurtermin)
			if (zone instanceof StundenplanZeitraster) {
				const date = props.stundenplanmanager.datumGetBy(kwAuswahl.value, zone);
				await props.patchKlausurtermin(dragData.value.id, {datum: date, startzeit: date !== null ? zone.stundenbeginn : null});
			}
	};

	const termineOhne = computed(() => (props.kursklausurmanager().terminGetMengeByQuartal(props.quartalsauswahl.value).toArray() as GostKlausurtermin[]).filter(termin => termin.datum === null));

	const termineMit = computed(() => {
		const terms = props.kursklausurmanager().terminGetMengeAsList();
		return (terms.toArray() as GostKlausurtermin[]).map(obj => ({
			...obj,
			startDate: obj.datum !== null ? new Date(obj.datum) : null,
		}));
	});


</script>

<style lang="postcss">
</style>
