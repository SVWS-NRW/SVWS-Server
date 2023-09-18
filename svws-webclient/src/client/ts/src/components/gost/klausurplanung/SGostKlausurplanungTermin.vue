<template>
	<div @dragover="if (onDropTermin !== undefined) $event.preventDefault();" @drop="if (onDropTermin !== undefined) onDropTermin!(termin);">
		<slot name="header">
			<header>
				<slot name="title">
					<div>
						{{ termin.bezeichnung !== null ? termin.bezeichnung : [...kursklausurmanager().kursklausurGetMengeByTerminid(termin.id)].map(k => k.kursKurzbezeichnung).join(", ") }}
					</div>
				</slot>
				<div>
					<span class="">{{ kursklausurmanager().schuelerklausurAnzahlGetByTerminid(termin.id) }} SuS</span>
					<slot name="title-rechts">
						<span class="float-right">{{ termin.datum === null ? "Noch kein Datum" : new Date(termin.datum).toLocaleString("de-DE").split(",")[0] }}</span>
					</slot>
				</div>
			</header>
		</slot>
		<slot name="main">
			<main class="border-t pt-2">
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
								:draggable="onDragKlausur !== undefined && (draggableKlausur === undefined || draggableKlausur(klausur))"
								@dragstart="onDragKlausur!(klausur)"
								@dragend="onDragKlausur!(undefined)"
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
			</main>
		</slot>
	</div>
</template>

<script setup lang="ts">

	import type { GostKursklausurManager, GostKursklausur, GostKlausurtermin, LehrerListeEintrag, KursManager} from "@core";
	import type { GostKlausurplanungDragData, GostKlausurplanungDropZone } from "./SGostKlausurplanung";

	const props = defineProps<{
		termin: GostKlausurtermin;
		kursklausurmanager: () => GostKursklausurManager;
		mapLehrer: Map<number, LehrerListeEintrag>;
		kursmanager: KursManager;
		klausurCssClasses?: (klausur: GostKursklausur, termin: GostKlausurtermin | undefined) => void;
		onDragKlausur?: (data: GostKlausurplanungDragData) => void;
		draggableKlausur?: (object: any) => boolean;
		onDropTermin?: (zone: GostKlausurplanungDropZone) => void;
	}>();

	const klausuren = () => props.kursklausurmanager().kursklausurGetMengeByTerminid(props.termin.id);

</script>