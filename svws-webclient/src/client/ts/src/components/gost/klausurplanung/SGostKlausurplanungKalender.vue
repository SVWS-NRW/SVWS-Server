<template>
	<div class="h-full">
		<div class="flex flex-row items-center mb-5">
			<label for="rgDisplayPeriodUom">Kalendaransicht: </label>
			<svws-ui-radio-group id="rgDisplayPeriodUom" :row="true">
				<svws-ui-radio-option name="rgDisplayPeriodUom" v-model="displayPeriodUom" label="Monat" value="month" />
				<svws-ui-radio-option name="rgDisplayPeriodUom" v-model="displayPeriodUom" label="Woche" value="week" />
			</svws-ui-radio-group>
		</div>

		<div class="flex flex-row h-full">
			<svws-ui-drop-data v-if="jahrgangsdaten?.abiturjahr !== -1" tag="div" :class="dropOverCssClasses()" class="w-1/4 mr-2 p-2" @drop="onDrop($event, null)">
				<ul class="flex flex-col gap-y-1">
					<svws-ui-drag-data tag="li" v-for="termin in termineOhne" :key="termin.id" :data="{termin}" @drag-start="dragStatus(termin)" @drag-end="dragStatus(null)">
						<s-gost-klausurplanung-kalender-termin :kursklausurmanager="kursklausurmanager" :faecher-manager="faecherManager" :map-lehrer="mapLehrer" :termin="termin" />
					</svws-ui-drag-data>
				</ul>
			</svws-ui-drop-data>
			<div class="flex flex-row flex-wrap gap-4 w-full h-full">
				<calendar-view :display-period-uom="displayPeriodUom" :starting-day-of-week="1" :enable-drag-drop="true" :items="termineMit"
					:show-date="showDate" @drop-on-date="onDrop" class="theme-default">
					<template #header="{ headerProps }">
						<calendar-view-header :header-props="headerProps" @input="setShowDate" />
					</template>
					<template #item="{value, top}">
						<svws-ui-drag-data :class="dropOverCssClasses()" tag="div" :data="value" @drag-start="dragStatus(value.originalItem)" @drag-end="dragStatus(null)">
							<s-gost-klausurplanung-kalender-termin :style="top" class="cv-item" :class="value.classes" :kursklausurmanager="kursklausurmanager" :termin="value.originalItem" :faecher-manager="faecherManager" :map-lehrer="mapLehrer" />
						</svws-ui-drag-data>
					</template>
				</calendar-view>
			</div>
		</div>
	</div>
</template>

<script setup lang="ts">
	import { CalendarView, CalendarViewHeader, CalendarMath } from "vue-simple-calendar"
	import "./../../../../../../../node_modules/vue-simple-calendar/dist/style.css";
	import "./../../../../../../../node_modules/vue-simple-calendar/dist/css/default.css";
	import { computed, ref } from "vue";
	import { GostJahrgangsdaten, GostKursklausurManager, GostFaecherManager, LehrerListeEintrag, GostKlausurtermin, GostKursklausur, List } from "@svws-nrw/svws-core";

	const props = defineProps<{
		jahrgangsdaten: GostJahrgangsdaten | undefined;
		kursklausurmanager: () => GostKursklausurManager;
		faecherManager: GostFaecherManager;
		mapLehrer: Map<number, LehrerListeEintrag>;
		patchKlausurtermin: (termin: Partial<GostKlausurtermin>, id: number) => Promise<boolean>;
	}>();

	const displayPeriodUom = ref("month");

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
			startDate: obj.datum != null ? new Date(obj.datum) : null,
		}));
	});

	const onDrop = async (item: GostKlausurtermin, date: Date | null) => {
		const termin = (item === undefined) ? props.kursklausurmanager().gibKlausurtermin(dragTermin.value!.id) : props.kursklausurmanager().gibKlausurtermin(item.id);
		if (termin !== null) {
			if (date !== null) {
				date.setDate(date.getDate() + 1);
				termin.datum = date.toISOString().split('T')[0];
			} else {
				termin.datum = null;
			}
			await props.patchKlausurtermin({datum: termin.datum}, termin.id);
		}
	};


</script>