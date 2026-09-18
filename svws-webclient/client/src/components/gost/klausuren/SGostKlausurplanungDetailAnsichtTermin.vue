<template>
	<div>
		<div class="text-base font-bold">{{ DateUtils.gibWochentagNameDesDatumsISO8601(termin.datum!) }}, {{ DateUtils.gibDatumGermanFormat(termin.datum!) }}</div>
		<div class="text-base font-bold opacity-50 mb-2">ab {{ DateUtils.getStringOfUhrzeitFromMinuten(state.manager.minKlausurstartzeitByTermin(termin, false)) }} Uhr</div>
		<template v-if="state.manager.stundenplanManagerGetByTerminOrNull(termin) !== null">
			<table class="table-auto min-w-full border -mx-3 border-ui-25" v-if="state.manager.raumGetMengeByTermin(termin).size() > 0">
				<thead class="border-b border-ui-25 text-center text-button">
					<tr>
						<th class="px-3 py-1.5 border-r border-ui-25" style="width:8%">Raum</th>
						<th colspan="3" class="px-3 py-1.5 border-r border-ui-25" style="width:45%">Kursinfos</th>
						<th class="px-3 py-1.5 border-r border-ui-25">Bemerkungen</th>
						<th v-if="zeigeBetroffeneUnterrichte" class="w-px whitespace-nowrap px-3 py-1.5 border-l border-ui-25">
							<div>Betroffene Unterrichte</div>
							<div class="font-normal">Schüler anwesend</div>
						</th>
					</tr>
				</thead>
				<tbody>
					<tr v-for="(raum, id) in state.manager.raumGetMengeByTermin(termin)" :key="raum.id" class="border-ui-25"
						:class="{
							'border-b': id < state.manager.raumGetMengeByTermin(termin).size() - 1,
						}">
						<td v-if="raum.idStundenplanRaum !== null" class="align-top border-r text-center border-ui-25">
							{{ state.manager.stundenplanraumGetByKlausurraum(raum).kuerzel }}
						</td>
						<td v-else class="align-top border-r text-ui-danger text-center border-ui-25">
							N.N.
						</td>
						<template v-if="state.manager.kursklausurGetMengeByRaum(raum, false).size() > 0">
							<td class="align-top border-ui-25 border-r" :colspan="kursInfoColspan(raum)">
								<table class="w-full">
									<tr class="border-b last:border-b-0 border-ui-25 border-dashed" v-for="klausur in state.manager.kursklausurGetMengeByRaum(raum, false)" :key="klausur.id">
										<td class="px-3 py-1" :class="{'w-1/2': (state.manager.getGemeinsamerKursklausurstartByKlausurraum(raum) !== null) && (state.manager.getGemeinsameKursklausurdauerByKlausurraum(raum) !== null)}">{{ state.manager.kursKurzbezeichnungByKursklausur(klausur) }}</td>
										<td class="px-3 py-1" :class="{'w-1/2': (state.manager.getGemeinsamerKursklausurstartByKlausurraum(raum) !== null) && (state.manager.getGemeinsameKursklausurdauerByKlausurraum(raum) !== null)}">{{ state.manager.kursLehrerKuerzelByKursklausur(klausur) }}</td>
										<td class="pl-3 py-1 text-center" v-if="state.manager.getGemeinsamerKursklausurstartByKlausurraum(raum) === null"><span class="inline-flex">{{ DateUtils.getStringOfUhrzeitFromMinuten(state.manager.startzeitByKlausurraumAndKursklausurOrException(raum, klausur)) }} Uhr <span class="icon i-ri-alert-fill ml-2 icon-ui-caution" v-if="state.manager.hatAbweichendeStartzeitByRaumAndKursklausur(raum, klausur)" /></span></td>
										<td class="pl-3 py-1 text-center" v-if="state.manager.getGemeinsameKursklausurdauerByKlausurraum(raum) === null">{{ state.manager.vorgabeByKursklausur(klausur).dauer }} Min.</td>
									</tr>
								</table>
							</td>
							<td class="align-top border-r border-ui-25 text-center" v-if="state.manager.getGemeinsamerKursklausurstartByKlausurraum(raum) !== null">{{ DateUtils.getStringOfUhrzeitFromMinuten(state.manager.getGemeinsamerKursklausurstartByKlausurraum(raum)!) }} Uhr</td>
							<td class="align-top border-r border-ui-25 text-center" v-if="state.manager.getGemeinsameKursklausurdauerByKlausurraum(raum) !== null">{{ state.manager.getGemeinsameKursklausurdauerByKlausurraum(raum) }} Min.</td>
						</template>
						<td v-else colspan="3" class="border-ui-25 border-r text-center text-ui-danger p-2">
							Keine Klausuren in diesem Raum
						</td>
						<td :rowspan="state.manager.raumGetMengeByTermin(termin).size()" v-if="id===0" class="space-y-3">
							<div v-if="(termin.bemerkung !== null) && (termin.bemerkung.trim().length > 0)" class="px-3 py-1">{{ termin.bemerkung }}</div>
							<div>
								<div v-for="r in state.manager.raumGetMengeByTermin(termin)" :key="r.id">
									<template v-if="(r.bemerkung !== null) && (r.bemerkung.trim().length > 0)">
										<span class="font-bold">Raum {{ state.manager.stundenplanraumGetByKlausurraumOrNull(r) !== null ? state.manager.stundenplanraumGetByKlausurraum(r).kuerzel : "N.N." }}:</span>
										{{ r.bemerkung }}
									</template>
								</div>
							</div>
							<div>
								<div v-for="klausur in state.manager.kursklausurGetMengeByTermin(termin)" :key="klausur.id">
									<template v-if="(klausur.bemerkung !== null) && (klausur.bemerkung.trim().length > 0)">
										<span class="font-bold">Kurs {{ state.manager.kursKurzbezeichnungByKursklausur(klausur) }}:</span>
										{{ klausur.bemerkung }}
									</template>
								</div>
							</div>
						</td>
						<td v-if="(id === 0) && zeigeBetroffeneUnterrichte" :rowspan="state.manager.raumGetMengeByTermin(termin).size()" class="w-px align-top space-y-3 whitespace-nowrap border-l border-ui-25 px-3 py-1">
							<table v-for="zeitrasterKurse in state.manager.stundenplankursGetMengeByTermin(termin)" :key="zeitrasterKurse.a.id" class="w-auto">
								<thead>
									<tr>
										<th colspan="2" class="pb-1 text-left">{{ zeitrasterKurse.a.unterrichtstunde }}. Stunde</th>
									</tr>
								</thead>
								<tbody>
									<tr v-for="eintrag in unterrichtAnwesenheiten(termin, zeitrasterKurse.a, zeitrasterKurse.b)" :key="eintrag.kurs.id"
										:class="{
											'text-ui-danger font-bold': eintrag.anteil < betroffeneUnterrichteAnwesenheitsschwelle,
										}">
										<td class="pr-3">{{ eintrag.kurs.bezeichnung }}:</td>
										<td class="text-right">{{ eintrag.anzahlVerbleibend }}</td>
										<td class="px-1 text-center">/</td>
										<td class="text-left">{{ eintrag.anzahlGesamt }}</td>
										<td class="text-center pl-3 whitespace-nowrap">({{ eintrag.anteilText }} %)</td>
									</tr>
								</tbody>
							</table>
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
	import type { GostKlausurraum } from '@core/core/data/gost/klausuren/GostKlausurraum';
	import type { GostKlausurtermin } from '@core/core/data/gost/klausuren/GostKlausurtermin';
	import type { StundenplanKurs } from '@core/core/data/stundenplan/StundenplanKurs';
	import type { StundenplanZeitraster } from '@core/core/data/stundenplan/StundenplanZeitraster';
	import { DateUtils } from '@core/core/utils/DateUtils';
	import type { List } from '@core/java/util/List';
	import { useGostKlausurplanungState } from '@ui/states/GostKlausurplanungState';


	const props = defineProps<{
		termin: GostKlausurtermin;
		zeigeBetroffeneUnterrichte: boolean;
		betroffeneUnterrichteAnwesenheitsschwelle: number;
	}>();
	const state = useGostKlausurplanungState();

	const kursInfoColspan = (raum: GostKlausurraum) => {
		let colspan = 1;
		colspan += state.manager.getGemeinsameKursklausurdauerByKlausurraum(raum) === null ? 1 : 0;
		colspan += state.manager.getGemeinsamerKursklausurstartByKlausurraum(raum) === null ? 1 : 0;
		return colspan;
	};

	function unterrichtAnwesenheiten(termin: GostKlausurtermin, zeitraster: StundenplanZeitraster, kurse: List<StundenplanKurs>) {
		const result: { kurs: StundenplanKurs; anzahlVerbleibend: number; anzahlGesamt: number; anteil: number; anteilText: string }[] = [];
		for (const kurs of kurse) {
			const anzahlGesamt = state.manager.schuelerGetMengeByStundenplankurs(kurs).size();
			const anzahlVerbleibend = state.manager.schuelerGetMengeVerbleibendByTerminAndZeitrasterAndStundenplankurs(termin, zeitraster, kurs).size();
			const anteil = anzahlGesamt === 0 ? 0 : Math.round(anzahlVerbleibend * 100 / anzahlGesamt);
			result.push({ kurs, anzahlVerbleibend, anzahlGesamt, anteil, anteilText: anteil.toLocaleString('de-DE') });
		}
		return result;
	}

</script>
