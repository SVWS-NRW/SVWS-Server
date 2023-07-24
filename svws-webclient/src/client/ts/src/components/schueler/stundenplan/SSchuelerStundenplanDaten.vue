<template>
	<div class="grid gap-4 rounded-md select-none" :class="cols">
		<div class="text-center rounded-md"> &ndash; </div>
		<div v-for="wochentag in manager().zeitrasterGetWochentageAlsEnumRange()" :key="wochentag.id" class="text-center rounded-md bg-svws-300">
			<div> {{ wochentage[wochentag.id] }} </div>
		</div>
		<template v-for="stunde in manager().zeitrasterGetStundenRange()" :key="stunde">
			<div class="grid gap-1 grid-cols-1 text-center rounded-md bg-yellow-200">
				<div> {{ stunde }}. Stunde </div>
				<div v-for="zeiten in manager().unterrichtsstundeGetUhrzeitenAsStrings(stunde)" :key="zeiten">
					{{ zeiten }}
				</div>
			</div>
			<div v-for="wochentag in manager().zeitrasterGetWochentageAlsEnumRange()" :key="wochentag.id" class="bg-yellow-100">
				<div v-if="manager().zeitrasterHatUnterrichtMitWochentyp0ByWochentagAndStunde(wochentag, stunde)" class="flex flex-col gap-2">
					<div v-for="unterricht in manager().unterrichtGetMengeByWochentagAndStundeAndWochentypOrEmptyList(wochentag, stunde, 0)" :key="unterricht.id"
						class="grid gap-1 grid-cols-2 text-center rounded-md bg-cyan-100">
						<div> {{ manager().unterrichtGetByIDStringOfFachOderKursKuerzel(unterricht.id) }} </div>
						<div> {{ manager().unterrichtGetByIDStringOfRaeume(unterricht.id) }} </div>
						<div> {{ manager().unterrichtGetByIDLehrerFirstAsStringOrEmpty(unterricht.id) }} </div>
					</div>
				</div>
				<div v-else-if="manager().zeitrasterHatUnterrichtMitWochentyp1BisNByWochentagAndStunde(wochentag, stunde)" class="flex flex-row gap-2">
					<div v-for="wochentyp in manager().getWochenTypModell()" :key="wochentyp" class="flex flex-col gap-2 bg-cyan-100">
						<div v-for="unterricht in manager().unterrichtGetMengeByWochentagAndStundeAndWochentypOrEmptyList(wochentag, stunde, wochentyp)" :key="unterricht.id"
							class="grid gap-1 grid-cols-2 text-center rounded-md">
							<div class="col-span-2"> ({{ wochentyp }}) </div>
							<div> {{ manager().unterrichtGetByIDStringOfFachOderKursKuerzel(unterricht.id) }} </div>
							<div> {{ manager().unterrichtGetByIDStringOfRaeume(unterricht.id) }} </div>
							<div> {{ manager().unterrichtGetByIDLehrerFirstAsStringOrEmpty(unterricht.id) }} </div>
						</div>
					</div>
				</div>
			</div>
		</template>
	</div>
</template>

<script setup lang="ts">

	import type { SchuelerStundenplanDatenProps } from "./SSchuelerStundenplanDatenProps";
	import { computed } from "vue";

	const wochentage = ['Sonntag', 'Montag', 'Dienstag', 'Mittwoch', 'Donnerstag', 'Freitag', 'Samstag', 'Sonntag' ];

	const props = defineProps<SchuelerStundenplanDatenProps>();

	const cols = computed(() => {
		const colcount = (props.manager().zeitrasterGetWochentageAlsEnumRange().length + 1);
		const result = {
			'grid-cols-1': colcount === 1,
			'grid-cols-2': colcount === 2,
			'grid-cols-3': colcount === 3,
			'grid-cols-4': colcount === 4,
			'grid-cols-5': colcount === 5,
			'grid-cols-6': colcount === 6,
			'grid-cols-7': colcount === 7
		};
		return result;
	});

</script>
