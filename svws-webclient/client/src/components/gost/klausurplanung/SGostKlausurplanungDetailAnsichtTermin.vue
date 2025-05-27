<template>
	<div>
		<div class="text-base font-bold">{{ DateUtils.gibWochentagNameDesDatumsISO8601(termin.datum!) }}, {{ DateUtils.gibDatumGermanFormat(termin.datum!) }}</div>
		<div class="text-base font-bold opacity-50 mb-2">ab {{ DateUtils.getStringOfUhrzeitFromMinuten(kMan().minKlausurstartzeitByTermin(termin, false)) }} Uhr</div>
		<template v-if="kMan().stundenplanManagerGetByTerminOrNull(termin) !== null">
			<table class="table-auto min-w-full border -mx-3 border-ui-25" v-if="kMan().raumGetMengeByTermin(termin).size() > 0">
				<thead class="border-b border-ui-25 text-left text-button">
					<tr>
						<th class="px-3 py-1.5 border-r border-ui-25" style="width:8%">Raum</th>
						<th colspan="3" class="px-3 py-1.5 border-r border-ui-25" style="width:45%">Kursinfos</th>
						<!--<th class="border-r" style="width:15%">Aufsichten</th>-->
						<th class="px-3 py-1.5">Bemerkungen</th>
					</tr>
				</thead>
				<tbody>
					<tr v-for="(raum, id) in kMan().raumGetMengeByTermin(termin)" :key="raum.id" class="border-b border-ui-25">
						<td v-if="raum.idStundenplanRaum !== null" class="border-r text-center border-ui-25">
							{{ kMan().stundenplanraumGetByKlausurraum(raum).kuerzel }}
						</td>
						<td v-else class="border-r text-ui-danger text-center border-ui-25">
							N.N.
						</td>
						<template v-if="kMan().kursklausurGetMengeByRaum(raum, false).size() > 0">
							<td class="border-ui-25 border-r" :colspan="kursInfoColspan(raum)">
								<table class="w-full">
									<tr class="border-b last:border-b-0 border-ui-25 border-dashed" v-for="klausur in kMan().kursklausurGetMengeByRaum(raum, false)" :key="klausur.id">
										<td class="px-3 py-1" :class="{'w-1/2': kMan().getGemeinsamerKursklausurstartByKlausurraum(raum) !== null && kMan().getGemeinsameKursklausurdauerByKlausurraum(raum) !== null}">{{ kMan().kursKurzbezeichnungByKursklausur(klausur) }}</td>
										<td class="px-3 py-1" :class="{'w-1/2': kMan().getGemeinsamerKursklausurstartByKlausurraum(raum) !== null && kMan().getGemeinsameKursklausurdauerByKlausurraum(raum) !== null}">{{ kMan().kursLehrerKuerzelByKursklausur(klausur) }}</td>
										<td class="pl-3 py-1 text-center" v-if="kMan().getGemeinsamerKursklausurstartByKlausurraum(raum) === null"><span class="inline-flex">{{ DateUtils.getStringOfUhrzeitFromMinuten(kMan().startzeitByKursklausurOrException(klausur)) }} Uhr <span class="icon i-ri-alert-fill ml-2 icon-ui-caution" v-if="kMan().hatAbweichendeStartzeitByKursklausur(klausur)" /></span></td>
										<td class="pl-3 py-1 text-center" v-if="kMan().getGemeinsameKursklausurdauerByKlausurraum(raum) === null">{{ kMan().vorgabeByKursklausur(klausur).dauer }} Min.</td>
									</tr>
								</table>
							</td>
							<td class="border-r border-ui-25 text-center" v-if="kMan().getGemeinsamerKursklausurstartByKlausurraum(raum) !== null">{{ DateUtils.getStringOfUhrzeitFromMinuten(kMan().getGemeinsamerKursklausurstartByKlausurraum(raum)!) }} Uhr</td>
							<td class="border-r border-ui-25 text-center" v-if="kMan().getGemeinsameKursklausurdauerByKlausurraum(raum) !== null">{{ kMan().getGemeinsameKursklausurdauerByKlausurraum(raum) }} Min.</td>
							<!--<td class="border-r p-2s">
								<table class="mx-auto">
									<tr v-for="stunde in raummanager.klausurraumstundeGetMengeByRaumid(raum.id)" :key="stunde.id">
										<td class="text-right pr-1">{{ stundenplanmanager.zeitrasterGetByIdOrException(stunde.idZeitraster).unterrichtstunde }}.</td>
										<td class="pl-1">N.N.</td>
									</tr>
								</table>
							</td>-->
						</template>
						<td v-else colspan="3" class="border-ui-25 border-r text-center text-ui-danger p-2">
							Keine Klausuren in diesem Raum
						</td>
						<td :rowspan="kMan().raumGetMengeByTermin(termin).size()" v-if="id===0" class="space-y-3">
							<div v-if="termin.bemerkung !== null && termin.bemerkung.trim().length > 0" class="px-3 py-1">{{ termin.bemerkung }}</div>
							<div>
								<div v-for="r in kMan().raumGetMengeByTermin(termin)" :key="r.id">
									<template v-if="r.bemerkung !== null && r.bemerkung.trim().length > 0">
										<span class="font-bold">Raum {{ kMan().stundenplanraumGetByKlausurraumOrNull(r) !== null ? kMan().stundenplanraumGetByKlausurraum(r).kuerzel : "N.N." }}:</span>
										{{ r.bemerkung }}
									</template>
								</div>
							</div>
							<div>
								<div v-for="klausur in kMan().kursklausurGetMengeByTermin(termin)" :key="klausur.id">
									<template v-if="klausur.bemerkung !== null && klausur.bemerkung.trim().length > 0">
										<span class="font-bold">Kurs {{ kMan().kursKurzbezeichnungByKursklausur(klausur) }}:</span>
										{{ klausur.bemerkung }}
									</template>
								</div>
							</div>
						</td>
					</tr>
				</tbody>
			</table>
			<div v-else class="opacity-50">Noch keine Planung für diesen Termin.</div>
		</template>
		<svws-ui-badge v-else :type="'error'" :size="'big'">
			Kein Stundenplan für diesen Termin definiert.
		</svws-ui-badge>
	</div>
</template>

<script setup lang="ts">
	import type { GostKlausurtermin, GostKlausurraum, GostKlausurplanManager, Schuljahresabschnitt } from '@core';
	import { DateUtils } from '@core';

	const props = defineProps<{
		termin: GostKlausurtermin;
		abschnitt: Schuljahresabschnitt | undefined;
		kMan: () => GostKlausurplanManager;
	}>();

	const kursInfoColspan = (raum: GostKlausurraum) => {
		let colspan = 1;
		colspan += props.kMan().getGemeinsameKursklausurdauerByKlausurraum(raum) !== null ? 0 : 1;
		colspan += props.kMan().getGemeinsamerKursklausurstartByKlausurraum(raum) !== null ? 0 : 1;
		return colspan;
	}

</script>
