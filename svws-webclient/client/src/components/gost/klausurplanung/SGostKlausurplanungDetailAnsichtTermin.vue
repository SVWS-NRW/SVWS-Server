<template>
	<div>
		<div class="text-base font-bold">{{ stundenplanmanager.zeitrasterGetWochentageAlsEnumRange().at(DateUtils.gibWochentagDesDatumsISO8601(termin.datum!) - 1)?.beschreibung }}, {{ DateUtils.gibDatumGermanFormat(termin.datum!) }}</div>
		<div class="text-base font-bold opacity-50 mb-2">ab {{ DateUtils.getStringOfUhrzeitFromMinuten(termin.startzeit!) }} Uhr</div>
		<table class="table-auto min-w-full border -mx-3 border-black/25" v-if="raummanager !== null && raummanager.raumGetMengeAsList().size() > 0">
			<thead class="border-b border-black/25 text-left text-button">
				<tr>
					<th class="px-3 py-1.5 border-r border-black/25" style="width:8%">Raum</th>
					<th colspan="3" class="px-3 py-1.5 border-r border-black/25" style="width:45%">Kursinfos</th>
					<!--<th class="border-r" style="width:15%">Aufsichten</th>-->
					<th class="px-3 py-1.5">Bemerkungen</th>
				</tr>
			</thead>
			<tbody>
				<tr v-for="(raum, id) in raummanager.raumGetMengeAsList()" :key="raum.id" class="border-b border-black/25">
					<td v-if="raum.idStundenplanRaum !== null" class="border-r text-center border-black/25">
						{{ stundenplanmanager.raumGetByIdOrException(raum.idStundenplanRaum!).kuerzel }}
					</td>
					<td v-else class="border-r text-error text-center border-black/25">
						N.N.
					</td>
					<template v-if="raummanager.kursklausurGetMengeByRaumid(raum.id).size() > 0">
						<td class="border-black/25 border-r" :colspan="kursInfoColspan(raum)">
							<table class="w-full">
								<tr class="border-b last:border-b-0 border-black/25 border-dashed" v-for="klausur in raummanager.kursklausurGetMengeByRaumid(raum.id)" :key="klausur.id">
									<td class="px-3 py-1" :class="{'w-1/2': raummanager.getGemeinsamerKursklausurstartByKlausurraum(raum) !== null && raummanager.getGemeinsameKursklausurdauerByKlausurraum(raum) !== null}">{{ kMan().kursKurzbezeichnungByKursklausur(klausur) }}</td>
									<td class="px-3 py-1" :class="{'w-1/2': raummanager.getGemeinsamerKursklausurstartByKlausurraum(raum) !== null && raummanager.getGemeinsameKursklausurdauerByKlausurraum(raum) !== null}">{{ kMan().kursLehrerKuerzelByKursklausur(klausur) }}</td>
									<td class="pl-3 py-1 text-center" v-if="raummanager.getGemeinsamerKursklausurstartByKlausurraum(raum) === null"><span class="inline-flex">{{ DateUtils.getStringOfUhrzeitFromMinuten(raummanager.getKursklausurManager().startzeitByKursklausur(klausur)!) }} Uhr <span class="icon i-ri-alert-fill ml-2 icon-highlight" v-if="raummanager.getKursklausurManager().hatAbweichendeStartzeitByKursklausur(klausur)" /></span></td>
									<td class="pl-3 py-1 text-center" v-if="raummanager.getGemeinsameKursklausurdauerByKlausurraum(raum) === null">{{ raummanager.getKursklausurManager().vorgabeByKursklausur(klausur).dauer }} Min.</td>
								</tr>
							</table>
						</td>
						<td class="border-r border-black/25 text-center" v-if="raummanager.getGemeinsamerKursklausurstartByKlausurraum(raum) !== null">{{ DateUtils.getStringOfUhrzeitFromMinuten(raummanager.getGemeinsamerKursklausurstartByKlausurraum(raum)!) }} Uhr</td>
						<td class="border-r border-black/25 text-center" v-if="raummanager.getGemeinsameKursklausurdauerByKlausurraum(raum) !== null">{{ raummanager.getGemeinsameKursklausurdauerByKlausurraum(raum) }} Min.</td>
						<!--<td class="border-r p-2s">
							<table class="mx-auto">
								<tr v-for="stunde in raummanager.klausurraumstundeGetMengeByRaumid(raum.id)" :key="stunde.id">
									<td class="text-right pr-1">{{ stundenplanmanager.zeitrasterGetByIdOrException(stunde.idZeitraster).unterrichtstunde }}.</td>
									<td class="pl-1">N.N.</td>
								</tr>
							</table>
						</td>-->
					</template>
					<td v-else colspan="3" class="border-black/25 border-r text-center text-error p-2">
						Keine Klausuren in diesem Raum
					</td>
					<td :rowspan="raummanager.raumGetMengeAsList().size()" v-if="id===0"><span class="px-3 py-1">{{ termin.bemerkung }}</span></td>
				</tr>
			</tbody>
		</table>
		<div v-else class="opacity-50">Noch keine Planung für diesen Termin.</div>
	</div>
</template>

<script setup lang="ts">
	import type { GostKlausurtermin, GostKlausurraumManager, StundenplanManager, GostKlausurraum, GostKursklausurManager } from '@core';
	import { DateUtils } from '@core';
	import type { Ref} from 'vue';
	import { onMounted, ref } from 'vue';

	const props = defineProps<{
		termin: GostKlausurtermin;
		kMan: () => GostKursklausurManager;
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
