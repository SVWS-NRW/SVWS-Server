<template>
	<div class="grid gap-4" :class="{ [`grid-cols-${getRangeWochentage().length + 1}`]: true }">
		<div class="text-center"> &ndash; </div>
		<div v-for="wochentag in getRangeWochentage()" :key="wochentag.id" class="text-center"> {{ wochentage[wochentag.id] }}  </div>
		<template v-for="stunde in getRangeStunden()" :key="stunde">
			<div class="text-center"> {{ stunde }} </div>
			<div v-for="wochentag in getRangeWochentage()" :key="wochentag.id">
				<div v-if="hatWochentypAllgemein(wochentag, stunde)" class="flex flex-col gap-2">
					<div v-for="unterricht in unterrichtsdaten(wochentag, stunde, 0)" :key="unterricht.id" class="text-center">
						{{ manager().fachGetByIdOrException(unterricht.idFach)?.kuerzel }}
					</div>
				</div>
				<div v-else-if="hatWochentypSpeziell(wochentag, stunde)" class="flex flex-row gap-2">
					<div v-for="wochentyp in manager().getWochenTypModell()" :key="wochentyp" class="flex flex-col gap-2">
						<div v-for="unterricht in unterrichtsdaten(wochentag, stunde, wochentyp)" :key="unterricht.id"
							class="grid gap-1 grid-cols-2 text-center">
							<div class="col-span-2"> Woche {{ wochentyp }} </div>
							<div> {{ manager().fachGetByIdOrException(unterricht.idFach)?.kuerzel }} </div>
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
