<template>
	<div class="stundenplan" :class="cols">
		<div class="stundenplan-cell"> &ndash; </div>
		<div v-for="wochentag in manager().zeitrasterGetWochentageAlsEnumRange()" :key="wochentag.id" class="stundenplan-cell bg-svws-300">
			<div> {{ wochentage[wochentag.id] }} </div>
		</div>
		<template v-for="stunde in manager().zeitrasterGetStundenRange()" :key="stunde">
			<div class="stundenplan-cell grid-cols-1 bg-yellow-200">
				<div> {{ stunde }}. Stunde </div>
				<div v-for="zeiten in manager().unterrichtsstundeGetUhrzeitenAsStrings(stunde)" :key="zeiten">
					{{ zeiten }}
				</div>
			</div>
			<div v-for="wochentag in manager().zeitrasterGetWochentageAlsEnumRange()" :key="wochentag.id" class="bg-yellow-100">
				<div v-if="(wochentyp() !== 0) && (manager().zeitrasterHatUnterrichtByWochentagAndStundeAndWochentyp(wochentag, stunde, 0) || manager().zeitrasterHatUnterrichtByWochentagAndStundeAndWochentyp(wochentag, stunde, wochentyp()))" class="flex flex-col gap-2">
					<div v-for="unterricht in manager().unterrichtGetMengeByWochentagAndStundeAndWochentypAndInklusiveOrEmptyList(wochentag, stunde, wochentyp(), true)" :key="unterricht.id"
						class="stundenplan-cell bg-cyan-100" :class="mode === 'schueler' ? 'grid-cols-2' : mode === 'lehrer' ? 'grid-cols-3' : 'grid-cols-4'">
						<div> {{ manager().unterrichtGetByIDStringOfFachOderKursKuerzel(unterricht.id) }} </div>
						<div> {{ unterricht.wochentyp !== 0 ? "" + unterricht.wochentyp : "&nbsp;" }} </div>
						<div v-if="mode !== 'lehrer'"> {{ manager().unterrichtGetByIDLehrerFirstAsStringOrEmpty(unterricht.id) }} </div>
						<div> {{ manager().unterrichtGetByIDStringOfRaeume(unterricht.id) }} </div>
					</div>
				</div>
				<div v-if="(wochentyp() === 0) && manager().zeitrasterHatUnterrichtMitWochentyp0ByWochentagAndStunde(wochentag, stunde)" class="flex flex-col gap-2">
					<div v-for="unterricht in manager().unterrichtGetMengeByWochentagAndStundeAndWochentypOrEmptyList(wochentag, stunde, 0)" :key="unterricht.id"
						class="stundenplan-cell bg-cyan-100" :class="mode === 'schueler' ? 'grid-cols-2' : mode === 'lehrer' ? 'grid-cols-3' : 'grid-cols-4'">
						<div> {{ manager().unterrichtGetByIDStringOfFachOderKursKuerzel(unterricht.id) }} </div>
						<div> &nbsp; </div>
						<div v-if="mode !== 'lehrer'"> {{ manager().unterrichtGetByIDLehrerFirstAsStringOrEmpty(unterricht.id) }} </div>
						<div> {{ manager().unterrichtGetByIDStringOfRaeume(unterricht.id) }} </div>
					</div>
				</div>
				<div v-else-if="(wochentyp() === 0) && manager().zeitrasterHatUnterrichtMitWochentyp1BisNByWochentagAndStunde(wochentag, stunde)" class="flex flex-row gap-2">
					<div v-for="wt in manager().getWochenTypModell()" :key="wt" class="flex flex-col gap-2 bg-cyan-100">
						<div v-for="unterricht in manager().unterrichtGetMengeByWochentagAndStundeAndWochentypOrEmptyList(wochentag, stunde, wt)" :key="unterricht.id"
							class="stundenplan-cell" :class="mode === 'schueler' ? 'grid-cols-2' : mode === 'lehrer' ? 'grid-cols-3' : 'grid-cols-4'">
							<div> {{ manager().unterrichtGetByIDStringOfFachOderKursKuerzel(unterricht.id) }} </div>
							<div> {{ wt }} </div>
							<div v-if="mode !== 'lehrer'"> {{ manager().unterrichtGetByIDLehrerFirstAsStringOrEmpty(unterricht.id) }} </div>
							<div> {{ manager().unterrichtGetByIDStringOfRaeume(unterricht.id) }} </div>
						</div>
					</div>
				</div>
			</div>
		</template>
	</div>
</template>

<script setup lang="ts">

	import type { StundenplanAnsichtProps } from "./StundenplanAnsichtProps";
	import { computed } from "vue";

	const wochentage = ['Sonntag', 'Montag', 'Dienstag', 'Mittwoch', 'Donnerstag', 'Freitag', 'Samstag', 'Sonntag' ];

	const props = defineProps<StundenplanAnsichtProps>();

	const cols = computed(() => {
		const colcount = props.manager().zeitrasterGetWochentageAlsEnumRange().length;
		const result = {
			'stundenplan-col1': colcount === 1,
			'stundenplan-col2': colcount === 2,
			'stundenplan-col3': colcount === 3,
			'stundenplan-col4': colcount === 4,
			'stundenplan-col5': colcount === 5,
			'stundenplan-col6': colcount === 6,
			'stundenplan-col7': colcount === 7,
		};
		return result;
	});

</script>

<style lang="postcss" scoped>

	.stundenplan {
		@apply grid gap-4 rounded-md select-none
	}

	.stundenplan-col1 {
		grid-template-columns: 12rem repeat(1, minmax(0, 1fr))
	}

	.stundenplan-col2 {
		grid-template-columns: 12rem repeat(2, minmax(0, 1fr))
	}

	.stundenplan-col3 {
		grid-template-columns: 12rem repeat(3, minmax(0, 1fr))
	}

	.stundenplan-col4 {
		grid-template-columns: 12rem repeat(4, minmax(0, 1fr))
	}

	.stundenplan-col5 {
		grid-template-columns: 12rem repeat(5, minmax(0, 1fr))
	}

	.stundenplan-col6 {
		grid-template-columns: 12rem repeat(6, minmax(0, 1fr))
	}

	.stundenplan-col7 {
		grid-template-columns: 12rem repeat(7, minmax(0, 1fr))
	}

	.stundenplan-cell {
		@apply text-center rounded-md grid gap-1
	}

</style>
