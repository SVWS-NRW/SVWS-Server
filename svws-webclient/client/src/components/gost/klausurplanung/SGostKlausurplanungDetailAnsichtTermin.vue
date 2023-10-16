<template>
	<table class="table-auto w-full border" v-if="raummanager !== null">
		<thead class="bg-gray">
			<tr>
				<th style="width:12%">Datum</th>
				<th style="width:6%">Stunde</th>
				<th colspan="2" style="width:15%">Kurs</th>
				<th style="width:8%">Raum</th>
				<!--<th colspan="2" style="width:15%">Aufsicht</th>-->
				<th>Bemerkungen</th>
			</tr>
		</thead>
		<tbody>
			<tr>
				<th class="text-center " :rowspan="raummanager.kursklausurGetMenge().size()">{{ DateUtils.gibDatumGermanFormat(termin.datum!) }}</th>
				<th class="border text-center table-fit" :rowspan="raummanager.kursklausurGetMenge().size()">2.-5.<br>bzw.<br>2.-4.</th>
				<td class="border table-fit no-right-border">{{ firstKlausur().klausur.kursKurzbezeichnung }}</td>
				<td class="border no-left-border">({{ mapLehrer.get(firstKlausur().klausur.idLehrer)?.kuerzel }}/{{ raummanager.schuelerklausurGetMengeByRaumidAndKursklausurid(firstKlausur().raum.id, firstKlausur().klausur.id).size() }})</td>
				<td class="border text-center" :rowspan="raummanager.kursklausurGetMengeByRaumid(firstKlausur().raum.id).size()">{{ firstKlausur().raum.idStundenplanRaum !== null ? stundenplanmanager.raumGetByIdOrException(firstKlausur().raum.idStundenplanRaum!).kuerzel : "N.N." }}</td>
				<!--<td class="border table-fit no-right-border">2. - 3.<br>4. - 5.</td>
				<td class="border table-fit no-left-border">BEN<br>EHS</td>-->
				<td :rowspan="raummanager.kursklausurGetMenge().size()"><u>Klausurdauer:</u><br>09:00 - 11:15 Uhr <small>(LK CH<sub>1</sub>, PH<sub>1</sub>)</small><br>09:00 - 12:00 Uhr <small>(LK BI<sub>1</sub>, D<sub>1</sub>, D<sub>2</sub>, E<sub>1</sub>, E<sub>2</sub>, M<sub>1</sub>, PA<sub>1</sub>)</small><br><br><u>2./3. Stunde</u><br>Alle Kurse entfallen.<br><br><u>4./5. Stunde</u><br>Alle Kurse entfallen.<br><br></td>
			</tr>
			<tr v-for="klausur in klausurenRest(firstKlausur().raum)" :key="klausur.id">
				<td class="border table-fit no-right-border">{{ klausur.kursKurzbezeichnung }}</td>
				<td class="border no-left-border">({{ mapLehrer.get(klausur.idLehrer)?.kuerzel }}/{{ raummanager.schuelerklausurGetMengeByRaumidAndKursklausurid(firstKlausur().raum.id, klausur.id).size() }})</td>
				<!--<td class="border table-fit no-right-border">2. - 3.<br>4. - 5.</td>
				<td class="border table-fit no-left-border">BEN<br>EHS</td>-->
			</tr>
			<template v-for="klausur in firstNextKlausur()" :key="klausur.klausur.id">
				<tr>
					<td class="border table-fit no-right-border">{{ klausur.klausur.kursKurzbezeichnung }}</td>
					<td class="border no-left-border">({{ mapLehrer.get(klausur.klausur.idLehrer)?.kuerzel }}/{{ raummanager.schuelerklausurGetMengeByRaumidAndKursklausurid(klausur.raum.id, klausur.klausur.id).size() }})</td>
					<td class="border text-center" :rowspan="raummanager.kursklausurGetMengeByRaumid(klausur.raum.id).size()">{{ klausur.raum.idStundenplanRaum !== null ? stundenplanmanager.raumGetByIdOrException(klausur.raum.idStundenplanRaum!).kuerzel : "N.N." }}</td>
					<!--<td class="border table-fit no-right-border">2. - 3.<br>4. - 5.</td>
					<td class="border table-fit no-left-border">BEN<br>EHS</td>-->
				</tr>
				<tr v-for="klausur2 in klausurenNextRest(klausur.raum)" :key="klausur2.id">
					<td class="border table-fit no-right-border">{{ klausur2.kursKurzbezeichnung }}</td>
					<td class="border no-left-border">({{ mapLehrer.get(klausur2.idLehrer)?.kuerzel }}/{{ raummanager.schuelerklausurGetMengeByRaumidAndKursklausurid(klausur.raum.id, klausur2.id).size() }})</td>
					<!--<td class="border table-fit no-right-border">2. - 3.<br>4. - 5.</td>
					<td class="border table-fit no-left-border">BEN<br>EHS</td>-->
				</tr>
			</template>
		</tbody>
	</table>
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
