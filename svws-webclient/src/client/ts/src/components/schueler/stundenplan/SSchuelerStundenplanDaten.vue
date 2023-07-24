<template>
	<div class="grid gap-4 rounded-md" :class="cols">
		<div class="text-center rounded-md"> &ndash; </div>
		<div v-for="wochentag in getRangeWochentage()" :key="wochentag.id" class="text-center rounded-md bg-svws-300">
			{{ wochentage[wochentag.id] }}
		</div>
		<template v-for="stunde in getRangeStunden()" :key="stunde">
			<div class="text-center rounded-md bg-yellow-200"> {{ stunde }} </div>
			<div v-for="wochentag in getRangeWochentage()" :key="wochentag.id" class="bg-yellow-100">
				<div v-if="hatWochentypAllgemein(wochentag, stunde)" class="flex flex-col gap-2">
					<div v-for="unterricht in unterrichtsdaten(wochentag, stunde, 0)" :key="unterricht.id"
						class="grid gap-1 grid-cols-2 text-center rounded-md bg-cyan-100">
						<div> {{ manager().unterrichtGetByIDStringOfFachOderKursKuerzel(unterricht.id) }} </div>
						<div> {{ manager().unterrichtGetByIDStringOfRaeume(unterricht.id) }} </div>
						<div> {{ manager().unterrichtGetByIDLehrerFirstAsStringOrEmpty(unterricht.id) }} </div>
					</div>
				</div>
				<div v-else-if="hatWochentypSpeziell(wochentag, stunde)" class="flex flex-row gap-2">
					<div v-for="wochentyp in manager().getWochenTypModell()" :key="wochentyp" class="flex flex-col gap-2 bg-cyan-100">
						<div v-for="unterricht in unterrichtsdaten(wochentag, stunde, wochentyp)" :key="unterricht.id"
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

	import { type List, type StundenplanUnterricht, Wochentag } from "@core";
	import type { SchuelerStundenplanDatenProps } from "./SSchuelerStundenplanDatenProps";
	import { computed } from "vue";

	const wochentage = ['Sonntag', 'Montag', 'Dienstag', 'Mittwoch', 'Donnerstag', 'Freitag', 'Samstag', 'Sonntag' ];

	const props = defineProps<SchuelerStundenplanDatenProps>();

	function getRangeStunden(): number[] {
		const min = props.manager().zeitrasterGetStundeMin();
		const max = props.manager().zeitrasterGetStundeMax();
		return Array.from({ length: (max-min+1) }, (value, index) => min + index);
	}

	function getRangeWochentage(): Wochentag[] {
		const min = props.manager().zeitrasterGetWochentagMin();
		const max = props.manager().zeitrasterGetWochentagMax();
		return Array.from({ length: (max-min+1) }, (value, index) => Wochentag.fromIDorException(min + index));
	}

	const cols = computed(() => {
		const colcount = (getRangeWochentage().length + 1);
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

	function hatWochentypAllgemein(wochentag: Wochentag, stunde: number) : boolean {
		if (!props.manager().zeitrasterExistsByWochentagAndStunde(wochentag.id, stunde))
			return false;
		const zeitraster = props.manager().zeitrasterGetByWochentagAndStundeOrException(wochentag.id, stunde);
		return props.manager().zeitrasterHatUnterrichtMitWochentyp0(zeitraster.id);
	}

	function hatWochentypSpeziell(wochentag: Wochentag, stunde: number) : boolean {
		if (!props.manager().zeitrasterExistsByWochentagAndStunde(wochentag.id, stunde))
			return false;
		const zeitraster = props.manager().zeitrasterGetByWochentagAndStundeOrException(wochentag.id, stunde);
		return props.manager().zeitrasterHatUnterrichtMitWochentyp1BisN(zeitraster.id);
	}

	function unterrichtsdaten(wochentag: Wochentag, stunde: number, wochentyp: number) : List<StundenplanUnterricht> | undefined {
		if (!props.manager().zeitrasterExistsByWochentagAndStunde(wochentag.id, stunde))
			return undefined;
		const zeitraster = props.manager().zeitrasterGetByWochentagAndStundeOrException(wochentag.id, stunde);
		const unterricht = props.manager().unterrichtGetMengeByZeitrasterIdAndWochentypOrEmptyList(zeitraster.id, wochentyp);
		return (unterricht === null) ? undefined : unterricht;
	}

</script>
