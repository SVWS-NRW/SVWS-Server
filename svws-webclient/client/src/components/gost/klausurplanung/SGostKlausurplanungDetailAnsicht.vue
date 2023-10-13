<template>
	<div class="page--content page--content--full relative flex-col">
		<table v-for="termin in kursklausurmanager().terminMitDatumGetMenge()" :key="termin.id" class="table-auto w-full border">
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
					<th class="text-center " :rowspan="kursklausurmanager().kursklausurGetMengeByTerminid(termin.id).size()">{{ DateUtils.gibDatumGermanFormat(termin.datum!) }}</th>
					<th class="border text-center table-fit" :rowspan="kursklausurmanager().kursklausurGetMengeByTerminid(termin.id).size()">2.-5.<br>bzw.<br>2.-4.</th>
					<td class="border table-fit no-right-border">{{ firstKlausur(termin.id).kursKurzbezeichnung }}</td>
					<td class="border no-left-border">(BEN/20)</td>
					<td class="border text-center ">0.50</td>
					<!--<td class="border table-fit no-right-border">2. - 3.<br>4. - 5.</td>
					<td class="border table-fit no-left-border">BEN<br>EHS</td>-->
					<td :rowspan="kursklausurmanager().kursklausurGetMengeByTerminid(termin.id).size()"><u>Klausurdauer:</u><br>09:00 - 11:15 Uhr <small>(LK CH<sub>1</sub>, PH<sub>1</sub>)</small><br>09:00 - 12:00 Uhr <small>(LK BI<sub>1</sub>, D<sub>1</sub>, D<sub>2</sub>, E<sub>1</sub>, E<sub>2</sub>, M<sub>1</sub>, PA<sub>1</sub>)</small><br><br><u>2./3. Stunde</u><br>Alle Kurse entfallen.<br><br><u>4./5. Stunde</u><br>Alle Kurse entfallen.<br><br></td>
				</tr>
				<tr v-for="klausur in klausurenRest(termin.id)" :key="klausur.id">
					<td class="border table-fit no-right-border">{{ klausur.kursKurzbezeichnung }}</td>
					<td class="border no-left-border">(BEN/20)</td>
					<td class="border text-center ">0.50</td>
					<!--<td class="border table-fit no-right-border">2. - 3.<br>4. - 5.</td>
					<td class="border table-fit no-left-border">BEN<br>EHS</td>-->
				</tr>
			</tbody>
		</table>
	</div>
</template>

<script setup lang="ts">
	import type { GostKursklausur} from '@core';
	import { ArrayList,  DateUtils } from '@core';
	import type { GostKlausurplanungKalenderProps } from './SGostKlausurplanungKalenderProps';

	const props = defineProps<GostKlausurplanungKalenderProps>();

	const firstKlausur = (idTermin: number) => props.kursklausurmanager().kursklausurGetMengeByTerminid(idTermin).get(0);

	const klausurenRest = (idTermin: number) => {
		const klausuren = new ArrayList<GostKursklausur>(props.kursklausurmanager().kursklausurGetMengeByTerminid(idTermin));
		klausuren.removeElementAt(0);
		return klausuren;
	}
</script>
