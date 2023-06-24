<template>
	<slot name="header">
		<header v-if="termin !== null" class="border-b">
			<div class="text-headline-md">
				<span>St HJ{{ termin?.halbjahr }}K{{ termin?.quartal }}</span>
				<SvwsUiToggle v-if="toggleDetails" class="float-right" v-model="showDetails" @click="$event.stopPropagation()">
					Details zeigen
				</SvwsUiToggle>
			</div>
			<div>
				<span class="">{{ anzahlSuS }} SuS</span>
				<span class="float-right">{{ termin.datum === null ? "Noch kein Datum" : new Date(termin.datum).toLocaleString("de-DE").split(",")[0] }}</span>
			</div>
		</header>
	</slot>
	<div v-if="!showDetails" class="break-normal">{{ kurzBezeichnungen }}</div>
	<table v-if="showDetails" class="w-full">
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
	import { computed, ref } from 'vue';

	const props = defineProps<{
		showDetails: boolean;
		toggleDetails: boolean;
		termin: GostKlausurtermin | null;
		kursklausurmanager: () => GostKursklausurManager;
		mapLehrer: Map<number, LehrerListeEintrag>;
		kursmanager: KursManager;
		quartal?: number;
		klausurDraggable: boolean;
		klausurCssClasses?: (klausur: GostKursklausur) => void;
	}>();

	const showDetails = ref(props.showDetails);

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

	const klausuren = props.termin === null ? (props.quartal === undefined || props.quartal <= 0 ? props.kursklausurmanager().getKursklausurenOhneTermin() : props.kursklausurmanager().getKursklausurenOhneTerminByQuartal(props.quartal)) : props.kursklausurmanager().getKursklausurenByTermin(props.termin.id);

	const anzahlSuS = computed(() => {
		let anzahl = 0;
		for(const klausur of klausuren.toArray() as GostKursklausur[]) {
			anzahl += klausur.schuelerIds.size();
		}
		return anzahl;
	});

	const kurzBezeichnungen = [...klausuren].map(k => k.kursKurzbezeichnung).join(", ");

</script>