<template>
	<div>
		<div class="text-headline-md">{{ stundenplanmanager.zeitrasterGetWochentageAlsEnumRange().at(DateUtils.gibWochentagDesDatumsISO8601(termin.datum!))?.beschreibung }}, {{ DateUtils.gibDatumGermanFormat(termin.datum!) }}</div>
		<div class="mb-2">ab {{ DateUtils.getStringOfUhrzeitFromMinuten(termin.startzeit!) }} Uhr</div>
		<table class="table-auto w-full border" v-if="raummanager !== null && raummanager.raumGetMengeAsList().size() > 0">
			<thead class="bg-light border-b">
				<tr>
					<th class="border-r" style="width:8%">Raum</th>
					<th colspan="3" class="border-r" style="width:45%">Kursinfos</th>
					<th class="border-r" style="width:15%">Aufsichten</th>
					<th>Bemerkungen</th>
				</tr>
			</thead>
			<tbody>
				<tr v-for="(raum, id) in raummanager.raumGetMengeAsList()" :key="raum.id" class="border-b">
					<td v-if="raum.idStundenplanRaum !== null" class="border-r text-center">
						{{ stundenplanmanager.raumGetByIdOrException(raum.idStundenplanRaum!).kuerzel }}
					</td>
					<td v-else class="border-r text-error text-center border-black">
						N.N.
					</td>
					<template v-if="raummanager.kursklausurGetMengeByRaumid(raum.id).size() > 0">
						<td class="border-r p-2" :colspan="kursInfoColspan(raum)">
							<table class="w-full divide-y-2 divide-black divide-dotted">
								<tr class="" v-for="klausur in raummanager.kursklausurGetMengeByRaumid(raum.id)" :key="klausur.id">
									<td class="text-right pr-1">{{ klausur.kursKurzbezeichnung }}</td>
									<td class="pl-1">{{ mapLehrer.get(klausur.idLehrer)?.kuerzel }}</td>
									<td class="text-center" v-if="raummanager.getGemeinsamerKursklausurstartByKlausurraum(raum) === null"><span class="inline-flex">{{ DateUtils.getStringOfUhrzeitFromMinuten(raummanager.getKursklausurManager().startzeitByKursklausur(klausur)!) }} Uhr <i-ri-alert-fill class="ml-2 text-error" v-if="raummanager.getKursklausurManager().hatAbweichendeStartzeitByKursklausur(klausur)" /></span></td>
									<td class="text-center" v-if="raummanager.getGemeinsameKursklausurdauerByKlausurraum(raum) === null">{{ raummanager.getKursklausurManager().vorgabeByKursklausur(klausur).dauer }} Min.</td>
								</tr>
							</table>
						</td>
						<td class="border-r text-center" v-if="raummanager.getGemeinsamerKursklausurstartByKlausurraum(raum) !== null">{{ DateUtils.getStringOfUhrzeitFromMinuten(raummanager.getGemeinsamerKursklausurstartByKlausurraum(raum)!) }} Uhr</td>
						<td class="border-r text-center" v-if="raummanager.getGemeinsameKursklausurdauerByKlausurraum(raum) !== null">{{ raummanager.getGemeinsameKursklausurdauerByKlausurraum(raum) }} Min.</td>
						<td class="border-r p-2s">
							<table class="mx-auto">
								<tr v-for="stunde in raummanager.klausurraumstundeGetMengeByRaumid(raum.id)" :key="stunde.id">
									<td class="text-right pr-1">{{ stundenplanmanager.zeitrasterGetByIdOrException(stunde.idZeitraster).unterrichtstunde }}.</td>
									<td class="pl-1">N.N.</td>
								</tr>
							</table>
						</td>
					</template>
					<td v-else colspan="4" class="border-black border-r text-center text-error p-2">
						Keine Klausuren in diesem Raum
					</td>
					<td :rowspan="raummanager.raumGetMengeAsList().size()" v-if="id===0">{{ termin.bemerkung }}</td>
				</tr>
			</tbody>
		</table>
		<div v-else>Noch keine Planung für diesen Termin.</div>
	</div>
</template>

<script setup lang="ts">
	import type { GostKlausurtermin, GostKlausurraumManager, LehrerListeEintrag, StundenplanManager, GostKlausurraum} from '@core';
	import { DateUtils } from '@core';
	import type { Ref} from 'vue';
	import { onMounted, ref } from 'vue';

	const props = defineProps<{
		termin: GostKlausurtermin;
		mapLehrer: Map<number, LehrerListeEintrag>;
		stundenplanmanager: StundenplanManager;
		erzeugeKlausurraummanager: (termin: GostKlausurtermin) => Promise<GostKlausurraumManager>;
	}>();

	const kursInfoColspan = (raum: GostKlausurraum) => {
		let colspan = 1;
		colspan += raummanager.value!.getGemeinsameKursklausurdauerByKlausurraum(raum) !== null ? 0 : 1;
		colspan += raummanager.value!.getGemeinsamerKursklausurstartByKlausurraum(raum) !== null ? 0 : 1;
		return colspan;
	}

	const raummanager: Ref<GostKlausurraumManager | null> = ref(null);

	onMounted(async () => {
		raummanager.value = await props.erzeugeKlausurraummanager(props.termin);
	});



</script>
