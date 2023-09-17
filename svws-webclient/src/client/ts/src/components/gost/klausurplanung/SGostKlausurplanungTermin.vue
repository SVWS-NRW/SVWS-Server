<template>
	<div>
		<slot name="header">
			<header class="border-b">
				<div>
					<span class="">{{ kursklausurmanager().schuelerklausurAnzahlGetByTerminid(termin.id) }} SuS</span>
					<span class="float-right">{{ termin.datum === null ? "Noch kein Datum" : new Date(termin.datum).toLocaleString("de-DE").split(",")[0] }}</span>
				</div>
			</header>
		</slot>
		<slot name="body">
			<slot name="klausuren">
				<table class="w-full">
					<thead>
						<tr>
							<th class="border">Kurs</th>
							<th class="border">Kuerzel</th>
							<th class="border"><i-ri-pencil-line /></th>
							<th class="border"><i-ri-time-line /></th>
							<th class="border" v-if="kursklausurmanager().quartalGetByTerminid(termin.id) === -1">Quartal</th>
							<th class="border">Schiene</th>
						</tr>
					</thead>
					<tbody>
						<tr v-for="klausur in klausuren()"
							:key="klausur.id"
							:data="klausur"
							:draggable="onDrag !== undefined"
							@dragstart="onDrag!(klausur)"
							@dragend="onDrag!(undefined)"
							:class="props.klausurCssClasses === undefined ? '' : props.klausurCssClasses(klausur, termin)">
							<td class="border">{{ props.kursmanager.get(klausur.idKurs)!.kuerzel }}</td>
							<td class="border">{{ mapLehrer.get(props.kursmanager.get(klausur.idKurs)!.lehrer!)?.kuerzel }}</td>
							<td class="border text-center">{{ klausur.schuelerIds.size() + "/" + props.kursmanager.get(klausur.idKurs)!.schueler.size() }}</td>
							<td class="border text-center">{{ klausur.dauer }}</td>
							<td class="border text-center" v-if="kursklausurmanager().quartalGetByTerminid(termin.id) === -1">{{ klausur.quartal }}</td>
							<td class="border text-center">{{ klausur.kursSchiene }}</td>
						</tr>
					</tbody>
				</table>
			</slot>
		</slot>
	</div>
</template>

<script setup lang="ts">

	import type { GostKursklausurManager, GostKursklausur, GostKlausurtermin, LehrerListeEintrag, KursManager} from "@core";
	import type { GostKlausurplanungDragData } from "./SGostKlausurplanung";

	const props = defineProps<{
		termin: GostKlausurtermin;
		kursklausurmanager: () => GostKursklausurManager;
		mapLehrer: Map<number, LehrerListeEintrag>;
		kursmanager: KursManager;
		klausurCssClasses?: (klausur: GostKursklausur, termin: GostKlausurtermin | undefined) => void;
		onDrag?: (data: GostKlausurplanungDragData) => void;

	}>();

	const klausuren = () => props.kursklausurmanager().kursklausurGetMengeByTerminid(props.termin.id);

</script>