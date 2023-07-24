<template>
	<Teleport to=".router-tab-bar--subnav-target">
		<svws-ui-sub-nav>
			<s-gost-klausurplanung-quartal-auswahl :quartalsauswahl="quartalsauswahl" />
			<svws-ui-radio-group id="rgDisplayPeriodUom" :row="true">
				<svws-ui-radio-option name="rgDisplayPeriodUom" v-model="displayPeriodUom" label="Monat" value="month" />
				<svws-ui-radio-option name="rgDisplayPeriodUom" v-model="displayPeriodUom" label="Woche" value="week" />
			</svws-ui-radio-group>
			<svws-ui-modal-hilfe class="ml-auto"> <s-gost-klausurplanung-kalender-hilfe /> </svws-ui-modal-hilfe>
		</svws-ui-sub-nav>
	</Teleport>

	<svws-ui-content-card @click="dragTermin = null" class="page--content page--content--full min-w-fit gap-x-8 2xl:gap-x-16 relative">
		<div class="flex gap-4 mt-4 h-screen">
			<div class="flex flex-col w-1/4 h-full">
				<div class="text-headline-md">Zu verplanen:</div>
				<svws-ui-drop-data v-if="jahrgangsdaten?.abiturjahr !== -1"
					:class="dropOverCssClasses()"
					@drop="onDrop($event, null, -1)"
					class="h-full">
					<ul class="flex flex-col gap-y-1">
						<svws-ui-drag-data tag="li" v-for="termin in termineOhne"
							:key="termin.id"
							:data="termin"
							@drag-start="dragTermin = termin"
							@drag-end="dragTermin = null">
							<s-gost-klausurplanung-kalender-termin :kursklausurmanager="kursklausurmanager"
								:faecher-manager="faecherManager"
								:map-lehrer="mapLehrer"
								:termin="termin"
								:kursmanager="kursmanager"
								:class="{'bg-green-100': dragTermin !== null && dragTermin.id === termin.id}"
								@click="dragTermin = (dragTermin === null ? termin : null);$event.stopPropagation()" />
						</svws-ui-drag-data>
					</ul>
				</svws-ui-drop-data>
			</div>
			<div class="w-full">
				<calendar-view :display-period-uom="displayPeriodUom"
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
	import type { GostJahrgangsdaten, GostKursklausurManager, GostKursklausur, GostFaecherManager, LehrerListeEintrag, GostKlausurtermin, KursManager, StundenplanManager, StundenplanZeitraster} from "@core";
	import { ArrayList} from "@core";
	import { Wochentag } from "@core";
	import type { GostKlausurplanungKalenderProps } from "./SGostKlausurplanungKalenderProps";

	const props = defineProps<GostKlausurplanungKalenderProps>();

	const displayPeriodUom = ref("month");

	const currentWeekNumber = 0;
	const numberDays = 5;

	const showDate = ref(new Date());

	const setShowDate = (d: Date) => {
		showDate.value = d;
	}

	function formatDate(date: Date | null) {
		if (date === null)
			return null;
		var d = new Date(date),
			month = '' + (d.getMonth() + 1),
			day = '' + d.getDate(),
			year = d.getFullYear();

		if (month.length < 2)
			month = '0' + month;
		if (day.length < 2)
			day = '0' + day;

		return [year, month, day].join('-');
	}

	const dropOverCssClasses = () => ({
		"bg-green-100": dragTermin.value !== null && dragTermin.value.datum !== null,
	});

	const kursInfos = (idKurs: number) => {
		const test = props.kursmanager.get(idKurs);
		return test!.kuerzel/* + " " + props.kursklausurmanager().getKursklausurByTerminKurs(dragTermin.value!.id, idKurs)!.schuelerIds.size() + "/??"*/;
	}

	function kurseGefiltert(day: any, stunde: number) {
		const kursIds = new ArrayList<number>();
		if (dragTermin.value !== null) {
			for (const klausur of props.kursklausurmanager().getKursklausurenByTermin(dragTermin.value.id).toArray() as GostKursklausur[]) {
				kursIds.add(klausur.idKurs);
			}
		}
		return props.stundenplanmanager.getKurseGefiltert( kursIds, 1, Wochentag.fromIDorException(day.getDay()), stunde);
	}

	function sumSchreiber(day: any, stunde: number) {
		const kurse = kurseGefiltert(day, stunde);
		let summe = 0;
		for (const klausur of kurse) {
			summe += props.kursklausurmanager().getKursklausurByTerminKurs(dragTermin.value!.id, klausur)!.schuelerIds.size();
		}
		return summe;
	}

	const dragTermin = ref<GostKlausurtermin | null>(null);

	const termineOhne = computed(() => (props.kursklausurmanager().getKlausurtermineByQuartal(props.quartalsauswahl.value).toArray() as GostKlausurtermin[]).filter(termin => termin.datum === null));

	const termineMit = computed(() => {
		const terms = props.kursklausurmanager().getKlausurtermine();
		return (terms.toArray() as GostKlausurtermin[]).map(obj => ({
			...obj,
			startDate: obj.datum !== null ? new Date(obj.datum) : null,
		}));
	});

	const onDrop = async (item: GostKlausurtermin, date: Date | null, stunde: number) => {
		const termin = (item === undefined) ? props.kursklausurmanager().gibKlausurtermin(dragTermin.value!.id) : props.kursklausurmanager().gibKlausurtermin(item.id);
		if (termin !== null) {
			await props.patchKlausurterminDatum(termin.id, {datum: formatDate(date), startzeit: date !== null ? props.stundenplanmanager.getZeitrasterByWochentagStunde(Wochentag.fromIDorException(date.getDay()), stunde).stundenbeginn : null});
		}
	};

</script>

<style lang="postcss">

.dow0, .dow6 { display: none}
.cv-item.offset1 {
	left: calc((100% / v-bind(numberDays)));
}
.cv-item.offset2 {
	left: calc((200% / v-bind(numberDays)));
}
.cv-item.offset3 {
	left: calc((300% / v-bind(numberDays)));
}
.cv-item.offset4 {
	left: calc((400% / v-bind(numberDays)));
}
.cv-item.offset5 {
	left: calc((500% / v-bind(numberDays)));
}
.cv-item.offset6 {
	left: calc((600% / v-bind(numberDays)));
}

.cv-item.span1 {
	width: calc((100% / v-bind(numberDays)) - 0.05em);
}
.cv-item.span2 {
	width: calc((200% / v-bind(numberDays)) - 0.05em);
}
.cv-item.span3 {
	width: calc((300% / v-bind(numberDays)) - 0.05em);
}
.cv-item.span4 {
	width: calc((400% / v-bind(numberDays)) - 0.05em);
}
.cv-item.span5 {
	width: calc((500% / v-bind(numberDays)) - 0.05em);
}
.cv-item.span6 {
	width: calc((600% / v-bind(numberDays)) - 0.05em);
}
.cv-item.span7 {
	width: calc((700% / v-bind(numberDays)) - 0.05em);
}

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
