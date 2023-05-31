<template>
	<svws-ui-content-card class="pt-8">
		<svws-ui-radio-group id="rgDisplayPeriodUom" :row="true">
			<svws-ui-radio-option name="rgDisplayPeriodUom" v-model="displayPeriodUom" label="Monat" value="month" />
			<svws-ui-radio-option name="rgDisplayPeriodUom" v-model="displayPeriodUom" label="Woche" value="week" />
		</svws-ui-radio-group>
		<div class="flex h-full gap-4 mt-4">
			<svws-ui-drop-data v-if="jahrgangsdaten?.abiturjahr !== -1" tag="div" :class="dropOverCssClasses()" class="w-1/4" @drop="onDrop($event, null, -1)">
				<ul class="flex flex-col gap-y-1">
					<svws-ui-drag-data tag="li" v-for="termin in termineOhne" :key="termin.id" :data="termin" @drag-start="dragStatus(termin)" @drag-end="dragStatus(null)">
						<s-gost-klausurplanung-kalender-termin :kursklausurmanager="kursklausurmanager" :faecher-manager="faecherManager" :map-lehrer="mapLehrer" :termin="termin" :kursmanager="kursmanager" />
					</svws-ui-drag-data>
				</ul>
			</svws-ui-drop-data>
			<div class="flex flex-row flex-wrap gap-4 w-full h-full">
				<calendar-view :display-period-uom="displayPeriodUom" :display-period-count="displayPeriodUom === 'month' ? 1 : 3" :starting-day-of-week="1" :enable-drag-drop="false" :items="termineMit"
					:show-date="showDate" @drop-on-date="onDrop" class="theme-default" current-period-label="Aktuell" :display-week-numbers="true">
					<template #header="{ headerProps }">
						<calendar-view-header :header-props="headerProps" @input="setShowDate" />
					</template>
					<template #item="{value, top, itemTop}">
						<svws-ui-drag-data :class="dropOverCssClasses()" tag="div" :data="value" @drag-start="dragStatus(value.originalItem)" @drag-end="dragStatus(null)">
							<s-gost-klausurplanung-kalender-termin-short :item-top="itemTop" :style="top" class="cv-item" :class="value.classes" :kursklausurmanager="kursklausurmanager" :termin="value.originalItem" :faecher-manager="faecherManager" :map-lehrer="mapLehrer" :kursmanager="kursmanager" />
						</svws-ui-drag-data>
					</template>
					<template #dayContent="{day}">
						<table class="w-full">
							<tr class="border-b last:border-0" v-for="stunde in 6" :key="stunde">
								<td class="border-r">{{ stunde }}</td>
								<svws-ui-drop-data @drop="onDrop($event, day, stunde)" class="w-full" tag="td">
									&nbsp;&nbsp;
								</svws-ui-drop-data>
							</tr>
						</table>
					</template>
					<template #weekNumber="{numberInYear}">
						{{ currentWeekNumber = numberInYear }}
					</template>
				</calendar-view>
			</div>
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">
	import { CalendarView, CalendarViewHeader } from "vue-simple-calendar"
	import "./../../../../../../../node_modules/vue-simple-calendar/dist/style.css";
	import "./../../../../../../../node_modules/vue-simple-calendar/dist/css/default.css";
	import { computed, ref } from "vue";
	import type { GostJahrgangsdaten, GostKursklausurManager, GostFaecherManager, LehrerListeEintrag, GostKlausurtermin, KursManager, StundenplanManager} from "@svws-nrw/svws-core";
	import { Wochentag } from "@svws-nrw/svws-core/dist/core/types/Wochentag";

	const props = defineProps<{
		jahrgangsdaten: GostJahrgangsdaten | undefined;
		kursklausurmanager: () => GostKursklausurManager;
		faecherManager: GostFaecherManager;
		mapLehrer: Map<number, LehrerListeEintrag>;
		kursmanager: KursManager;
		patchKlausurtermin: (termin: Partial<GostKlausurtermin>, id: number) => Promise<boolean>;
		stundenplanmanager: StundenplanManager;
	}>();

	const displayPeriodUom = ref("month");

	const currentWeekNumber = 0;

	const showDate = ref(new Date());

	const setShowDate = (d: Date) => {
		showDate.value = d;
	}

	const dropOverCssClasses = () => ({
		"bg-green-100": dragTermin.value !== null && dragTermin.value.datum !== null,
	});

	const dragTermin = ref<GostKlausurtermin | null>(null);
	const dragStatus = (termin: GostKlausurtermin | null) => dragTermin.value = termin;

	const termineOhne = computed(() => (props.kursklausurmanager().getKlausurtermine().toArray() as GostKlausurtermin[]).filter(termin => termin.datum === null));

	const termineMit = computed(() => {
		const terms = props.kursklausurmanager().getKlausurtermine();
		return (terms.toArray() as GostKlausurtermin[]).map(obj => ({
			...obj,
			startDate: obj.datum !== null ? new Date(obj.datum) : null,
		}));
	});

	const onDrop = async (item: GostKlausurtermin, date: Date | null, stunde: number) => {
		console.log(date);
		const termin = (item === undefined) ? props.kursklausurmanager().gibKlausurtermin(dragTermin.value!.id) : props.kursklausurmanager().gibKlausurtermin(item.id);
		if (termin !== null) {
			if (date !== null) {
				date.setDate(date.getDate() + 1);
				termin.datum = date.toISOString().split('T')[0];
				props.stundenplanmanager.getZeitrasterByWochentagStunde(Wochentag.MONTAG, stunde);
				console.log(props.stundenplanmanager.getZeitrasterByWochentagStunde(Wochentag.MONTAG, stunde));
				termin.startzeit = props.stundenplanmanager.getZeitrasterByWochentagStunde(Wochentag.MONTAG, stunde).stundenbeginn;
				console.log(termin);
			} else {
				termin.datum = null;
			}
			await props.patchKlausurtermin({datum: termin.datum, startzeit: termin.startzeit}, termin.id);
		}
	};

	//const onDrop = async (item: GostKlausurtermin, date: Date | null) => {
	// 	const termin = (item === undefined) ? props.kursklausurmanager().gibKlausurtermin(dragTermin.value!.id) : props.kursklausurmanager().gibKlausurtermin(item.id);
	// 	if (termin !== null) {
	// 		if (date !== null) {
	// 			date.setDate(date.getDate() + 1);
	// 			termin.datum = date.toISOString().split('T')[0];
	// 		} else {
	// 			termin.datum = null;
	// 		}
	// 		await props.patchKlausurtermin({datum: termin.datum}, termin.id);
	// 	}
	// };


</script>

<style lang="postcss">
.theme-default .cv-header,
.theme-default .cv-header-day {
	@apply bg-white;
}

.theme-default .cv-header {
	@apply justify-between;
}

.theme-default .cv-header .periodLabel {
	@apply flex-grow-0 -order-1 m-0;
	@apply text-headline-sm;
}

.theme-default .cv-header button {
	@apply text-black;
}

.cv-header-days, .cv-header-day, .cv-weeks, .cv-week, .cv-day, .cv-item {
	@apply border-black/25;
}

.theme-default .cv-weeks {
	min-height: 32rem;
}

.theme-default .cv-weeknumber {
	@apply bg-white text-black border-black/25 text-sm flex items-center justify-center;
	padding: .2em;
}

.theme-default .cv-weeknumber span {
	@apply m-0 relative top-0 left-0 transform-none;
}

.theme-default .cv-day.past {
	@apply text-black text-opacity-25 bg-white;
}

.theme-default .cv-day.outsideOfMonth {
	@apply bg-light text-black text-opacity-25;
}

.theme-default .cv-day.today {
	@apply text-primary font-bold bg-white;
}

.theme-default .cv-day[aria-selected="true"] {
	@apply bg-primary/25 text-primary font-bold;
}

.theme-default .cv-item {
	@apply rounded;
	border-color: #e0e0f0;
	background-color: #e7e7ff;
	text-overflow: ellipsis;
}

.theme-default .cv-item.purple {
	background-color: #f0e0ff;
	border-color: #e7d7f7;
}

.theme-default .cv-item.orange {
	background-color: #ffe7d0;
	border-color: #f7e0c7;
}

.theme-default .cv-item.continued::before,
.theme-default .cv-item.toBeContinued::after {
	content: " \21e2 ";
	color: #999;
}

.theme-default .cv-item.toBeContinued {
	border-right-style: none;
	border-top-right-radius: 0;
	border-bottom-right-radius: 0;
}

.theme-default .cv-item.isHovered.hasUrl {
	text-decoration: underline;
}

.theme-default .cv-item.continued {
	border-left-style: none;
	border-top-left-radius: 0;
	border-bottom-left-radius: 0;
}

.theme-default .cv-item .startTime,
.theme-default .cv-item .endTime {
	@apply font-bold text-primary;
}

/* Drag and drop */

.theme-default .cv-day.draghover {
	box-shadow: inset 0 0 0.2em 0.2em primary;
}
</style>
