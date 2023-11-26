<template>
	<div>
		<div class="text-headline-md">{{ stundenplanmanager.zeitrasterGetWochentageAlsEnumRange().at(DateUtils.gibWochentagDesDatumsISO8601(termin.datum!))?.beschreibung }}, {{ DateUtils.gibDatumGermanFormat(termin.datum!) }}</div>
		<div class="mb-2">ab {{ DateUtils.getStringOfUhrzeitFromMinuten(termin.startzeit!) }} Uhr</div>
		<table class="table-auto w-full border" v-if="raummanager !== null && raummanager.raumGetMengeAsList().size() > 0">
			<thead class="bg-light border-b">
				<tr>
					<th class="border-r" style="width:8%">Raum</th>
					<th colspan="2" class="border-r" style="width:25%">Kursinfos</th>
					<th class="border-r" style="width:15%">Aufsichten</th>
					<th>Bemerkungen</th>
				</tr>
			</thead>
			<tbody>
				<tr v-for="(raum, id) in raummanager.raumGetMengeAsList()" :key="raum.id" class="border-b">
					<td class="border-r text-center">
						{{ raum.idStundenplanRaum !== null ? stundenplanmanager.raumGetByIdOrException(raum.idStundenplanRaum!).kuerzel : "N.N." }}
					</td>
					<td class="border-r" :colspan="raummanager.getGemeinsameKursklausurdauerByKlausurraum(raum) !== null ? 1 : 2">
						<table class="w-full divide-y-2 divide-black divide-dashed" v-if="raummanager.kursklausurGetMengeByRaumid(raum.id).size() > 0">
							<tr class="" v-for="klausur in raummanager.kursklausurGetMengeByRaumid(raum.id)" :key="klausur.id">
								<td>{{ klausur.kursKurzbezeichnung }}</td>
								<td>{{ mapLehrer.get(klausur.idLehrer)?.kuerzel }}</td>
								<td v-if="raummanager.getGemeinsameKursklausurdauerByKlausurraum(raum) === null">{{ raummanager.getKursklausurManager().vorgabeByKursklausur(klausur).dauer }} Min.</td>
							</tr>
						</table>
						<div v-else>
							Raum leer
						</div>
					</td>
					<td class="border-r" v-if="raummanager.getGemeinsameKursklausurdauerByKlausurraum(raum) !== null">{{ raummanager.getGemeinsameKursklausurdauerByKlausurraum(raum) }} Min.</td>
					<td class="border-r">
						<table v-if="true">
							<tr v-for="stunde in raummanager.klausurraumstundeGetMengeByRaumid(raum.id)" :key="stunde.id">
								<td>{{ stundenplanmanager.zeitrasterGetByIdOrException(stunde.idZeitraster).unterrichtstunde }}.</td>
								<td>N.N.</td>
							</tr>
						</table>
						<div v-else>
							Raum leer
						</div>
					</td>
					<td :rowspan="raummanager.raumGetMengeAsList().size()" v-if="id===0">{{ termin.bemerkung }}</td>
				</tr>



				<!-- <tr>
					<th class="border text-center table-fit" :rowspan="raummanager.kursklausurGetMenge().size()">2.-5.<br>bzw.<br>2.-4.</th>
					<td class="border table-fit no-right-border">{{ firstKlausur().klausur.kursKurzbezeichnung }}</td>
					<td class="border no-left-border">({{ mapLehrer.get(firstKlausur().klausur.idLehrer)?.kuerzel }}/{{ raummanager.schuelerklausurGetMengeByRaumidAndKursklausurid(firstKlausur().raum.id, firstKlausur().klausur.id).size() }})</td>
					<td class="border text-center" :rowspan="raummanager.kursklausurGetMengeByRaumid(firstKlausur().raum.id).size()">{{ firstKlausur().raum.idStundenplanRaum !== null ? stundenplanmanager.raumGetByIdOrException(firstKlausur().raum.idStundenplanRaum!).kuerzel : "N.N." }}</td>
					<td :rowspan="raummanager.kursklausurGetMenge().size()"><u>Klausurdauer:</u><br>09:00 - 11:15 Uhr <small>(LK CH<sub>1</sub>, PH<sub>1</sub>)</small><br>09:00 - 12:00 Uhr <small>(LK BI<sub>1</sub>, D<sub>1</sub>, D<sub>2</sub>, E<sub>1</sub>, E<sub>2</sub>, M<sub>1</sub>, PA<sub>1</sub>)</small><br><br><u>2./3. Stunde</u><br>Alle Kurse entfallen.<br><br><u>4./5. Stunde</u><br>Alle Kurse entfallen.<br><br></td>
				</tr>
				<tr v-for="klausur in klausurenRest(firstKlausur().raum)" :key="klausur.id">
					<td class="border table-fit no-right-border">{{ klausur.kursKurzbezeichnung }}</td>
					<td class="border no-left-border">({{ mapLehrer.get(klausur.idLehrer)?.kuerzel }}/{{ raummanager.schuelerklausurGetMengeByRaumidAndKursklausurid(firstKlausur().raum.id, klausur.id).size() }})</td>
				</tr>
				<template v-for="klausur in firstNextKlausur()" :key="klausur.klausur.id">
					<tr>
						<td class="border table-fit no-right-border">{{ klausur.klausur.kursKurzbezeichnung }}</td>
						<td class="border no-left-border">({{ mapLehrer.get(klausur.klausur.idLehrer)?.kuerzel }}/{{ raummanager.schuelerklausurGetMengeByRaumidAndKursklausurid(klausur.raum.id, klausur.klausur.id).size() }})</td>
						<td class="border text-center" :rowspan="raummanager.kursklausurGetMengeByRaumid(klausur.raum.id).size()">{{ klausur.raum.idStundenplanRaum !== null ? stundenplanmanager.raumGetByIdOrException(klausur.raum.idStundenplanRaum!).kuerzel : "N.N." }}</td>
					</tr>
					<tr v-for="klausur2 in klausurenNextRest(klausur.raum)" :key="klausur2.id">
						<td class="border table-fit no-right-border">{{ klausur2.kursKurzbezeichnung }}</td>
						<td class="border no-left-border">({{ mapLehrer.get(klausur2.idLehrer)?.kuerzel }}/{{ raummanager.schuelerklausurGetMengeByRaumidAndKursklausurid(klausur.raum.id, klausur2.id).size() }})</td>
					</tr>
				</template> -->
			</tbody>
		</table>
		<div v-else>Noch keine Planung für diesen Termin.</div>
	</div>
</template>

<script setup lang="ts">
	import type { GostKlausurtermin, GostKlausurraumManager, LehrerListeEintrag, StundenplanManager, GostKlausurraum} from '@core';
	import { ArrayList, DateUtils, List } from '@core';
	import type { Ref} from 'vue';
	import { onMounted, ref } from 'vue';

	const props = defineProps<{
		termin: GostKlausurtermin;
		mapLehrer: Map<number, LehrerListeEintrag>;
		stundenplanmanager: StundenplanManager;
		erzeugeKlausurraummanager: (termin: GostKlausurtermin) => Promise<GostKlausurraumManager>;
	}>();

	const firstKlausur = () => {
		const firstRaum = raummanager.value!.raumGetMengeAsList().get(0);
		const firstKlausur = raummanager.value!.kursklausurGetMengeByRaumid(firstRaum.id).get(0);
		return {raum: firstRaum, klausur: firstKlausur};
	}

	const klausurenRest = (raum: GostKlausurraum) => {
		const klausuren = raummanager.value!.kursklausurGetMengeByRaumid(raum.id);
		klausuren.removeElementAt(0);
		return klausuren;
	}

	const firstNextKlausur = () => {
		const raumListe = new ArrayList(raummanager.value!.raumGetMengeAsList());
		raumListe.removeElementAt(0);
		let ergebnis = [];
		for (const raum of raumListe) {
			ergebnis.push({raum, klausur: raummanager.value!.kursklausurGetMengeByRaumid(raum.id).get(0)});
		}
		return ergebnis;
	}

	const klausurenNextRest = (raum: GostKlausurraum) => {
		const klausuren = raummanager.value!.kursklausurGetMengeByRaumid(raum.id);
		klausuren.removeElementAt(0);
		return klausuren;
	}

	const raummanager: Ref<GostKlausurraumManager | null> = ref(null);

	onMounted(async () => {
		raummanager.value = await props.erzeugeKlausurraummanager(props.termin);
	});



</script>
