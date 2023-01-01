<template>
	<div class="flex flex-row gap-4">
		<div class="w-full flex-none sm:-mx-6 lg:-mx-8">
			<div class="w-full inline-block py-2 align-middle sm:px-6 lg:px-8">
				<div v-for="wochentyp in manager?.getWochentypen()" class="w-full rounded-lg shadow">
					<p class="text-lg"></p>
					<table class="w-full table-auto border-collapse mb-5">
						<thead>
							<tr>
								<th class="text-center border-2 border-collapse">Woche {{wochentyp}}</th>
								<th class="text-center border-2 border-collapse">Montag</th>
								<th class="text-center border-2 border-collapse">Dienstag</th>
								<th class="text-center border-2 border-collapse">Mittwoch</th>
								<th class="text-center border-2 border-collapse">Donnerstag</th>
								<th class="text-center border-2 border-collapse">Freitag</th>
							</tr>
						</thead>
						<tbody>
							<tr v-for="stunde in manager?.getMaxStunde()">
								<th class="text-center border-2 border-collapse">{{stunde}}</th>
								<td class="text-center border-2 border-collapse" v-for="wochentag in manager?.getMaxWochentag()">
									<table class="w-full">
										<tr>
											<s-unterricht v-for="unterricht in unterrichtsdaten(wochentyp, wochentag, stunde)" :key="unterricht.idUnterricht" :unterricht="unterricht" />
										</tr>
									</table>
								</td>
							</tr>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
</template>

<script setup lang="ts">

	import { computed, ComputedRef } from "vue";

	import { SchuelerListeEintrag, SchuelerStundenplanManager, StundenplanListeEintrag } from "@svws-nrw/svws-core-ts";
	import { DataStundenplan } from "~/apps/schueler/DataStundenplan";
	import { DataSchuelerStammdaten } from "~/apps/schueler/DataSchuelerStammdaten";

	const props = defineProps<{ id?: number; item?: SchuelerListeEintrag, stammdaten: DataSchuelerStammdaten, stundenplan?: StundenplanListeEintrag, data: DataStundenplan, routename: string }>();
	
	const manager: ComputedRef<SchuelerStundenplanManager | undefined> = computed(() => 
		props.data.daten === undefined ? undefined : new SchuelerStundenplanManager(props.data.daten)
	);

	function unterrichtsdaten(wochentyp: Number, wochentag: number, stunde: number) {
		if (manager.value === undefined)
			return undefined;
		const zeitraster = manager.value.getZeitrasterByWochentagStunde(wochentag, stunde);
		if (zeitraster === null)
			return undefined;
		const unterricht = manager.value.getUnterrichtByWocheZeitrasterId(wochentyp.valueOf(), zeitraster.id, true);
		return (unterricht === null) ? undefined : unterricht;
	}

</script>
