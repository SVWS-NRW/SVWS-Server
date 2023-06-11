<template>
	<table class="w-full">
		<thead v-if="termin !== null">
			<tr>
				<th class="text-left" colspan="2">St HJ{{ termin?.halbjahr }}K{{ termin?.quartal }}</th>
				<th class="text-right" colspan="4">{{ termin.datum === null ? "Noch kein Datum" : new Date(termin.datum).toLocaleString("de-DE").split(",")[0] }}</th>
			</tr>
			<tr>
				<th colspan="5"></th>
				<td colspan="1" class="text-right">{{ anzahlSuS }} SuS</td>
			</tr>
		</thead>
		<tbody>
			<s-gost-klausurplanung-klausur-common v-for="klausur in klausuren"
				:key="klausur.id"
				:kursklausurmanager="kursklausurmanager"
				:klausur="klausur"
				:map-lehrer="mapLehrer"
				:kursmanager="kursmanager"
				:draggable="klausurDraggable"
				@drag-start-klausur="dragStartKlausur"
				@drag-end-klausur="dragEndKlausur"
				:class="props.klausurCssClasses === undefined ? '' : props.klausurCssClasses(klausur)" />
		</tbody>
	</table>
</template>

<script setup lang="ts">

	import type { GostKursklausurManager, GostKursklausur, GostKlausurtermin, LehrerListeEintrag, KursManager} from "@core";
	import { computed } from 'vue';

	const props = defineProps<{
		termin: GostKlausurtermin | null;
		kursklausurmanager: () => GostKursklausurManager;
		mapLehrer: Map<number, LehrerListeEintrag>;
		kursmanager: KursManager;
		quartal?: number;
		klausurDraggable: boolean;
		klausurCssClasses?: (klausur: GostKursklausur) => void;
	}>();

	const emit = defineEmits<{
		(e: 'dragStartKlausur', data: DragEvent, klausur: GostKursklausur): void;
		(e: 'dragEndKlausur', data: DragEvent): void;
	}>()

	function dragStartKlausur(e: DragEvent, klausur: GostKursklausur) {
		emit("dragStartKlausur", e, klausur);
	}

	function dragEndKlausur(e: DragEvent) {
		emit("dragEndKlausur", e);
	}

	const klausuren = computed(() =>
		props.termin === null ? (props.quartal === undefined || props.quartal <= 0 ? props.kursklausurmanager().getKursklausurenOhneTermin() : props.kursklausurmanager().getKursklausurenOhneTerminByQuartal(props.quartal)) : props.kursklausurmanager().getKursklausurenByTermin(props.termin.id)
	);

	const anzahlSuS = computed(() => {
		let anzahl = 0;
		for(const klausur of klausuren.value.toArray() as GostKursklausur[]) {
			anzahl += klausur.schuelerIds.size();
		}
		return anzahl;
	});

</script>