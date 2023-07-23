<template>
	<div class="flex flex-row gap-4">
		<div class="w-full flex-none sm:-mx-6 lg:-mx-8">
			<div class="w-full inline-block py-2 align-middle sm:px-6 lg:px-8">
				<table class="w-full table-auto border-collapse mb-5">
					<thead>
						<tr>
							<th class="text-center border-2 border-collapse"> &ndash; </th>
							<th class="text-center border-2 border-collapse">Montag</th>
							<th class="text-center border-2 border-collapse">Dienstag</th>
							<th class="text-center border-2 border-collapse">Mittwoch</th>
							<th class="text-center border-2 border-collapse">Donnerstag</th>
							<th class="text-center border-2 border-collapse">Freitag</th>
						</tr>
					</thead>
					<tbody>
						<tr v-for="stunde in getRangeStunden()" :key="stunde">
							<th class="text-center border-2 border-collapse">{{ stunde }}</th>
							<td class="text-center border-2 border-collapse" v-for="wochentag in getRangeWochentage()" :key="wochentag.id">
								<table class="w-full">
									<tr>
										<td v-for="unterricht in unterrichtsdaten(wochentag, stunde)" :key="unterricht.id" class="text-center">
											{{ manager().fachGet(unterricht.idFach)?.kuerzel }}
										</td>
									</tr>
								</table>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
	</div>
</template>

<script setup lang="ts">

	import { type List, type StundenplanUnterricht, Wochentag } from "@core";
	import type { LehrerStundenplanDatenProps } from "./SLehrerStundenplanDatenProps";

	const props = defineProps<LehrerStundenplanDatenProps>();

	function getRangeStunden(): number[] {
		const min = props.manager().zeitrasterGetStundeMin();
		const max = props.manager().zeitrasterGetStundeMax();
		return Array.from({ length: (max-min+1) }, (value, index) => min + index);
	}

	function getRangeWochentage(): Wochentag[] {
		const min = props.manager().getZeitrasterWochentagMin().id;
		const max = props.manager().getZeitrasterWochentagMax().id;
		return Array.from({ length: (max-min+1) }, (value, index) => Wochentag.fromIDorException(min + index));
	}

	function unterrichtsdaten(wochentag: Wochentag, stunde: number) : List<StundenplanUnterricht> | undefined {
		const zeitraster = props.manager().getZeitrasterByWochentagStunde(wochentag, stunde);
		if (zeitraster === null)
			return undefined;
		const unterricht = null; // TODO Stundenplan-Manager-Methode für props.manager().getUnterrichtByWocheZeitrasterId(wochentyp, zeitraster.id, true);
		return (unterricht === null) ? undefined : unterricht;
	}

</script>
