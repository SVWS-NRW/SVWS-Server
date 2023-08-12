<template>
	<slot name="header">
		<header class="border-b">
			<div class="text-headline-md">
				<span>{{ jahrgangsdaten.jahrgang }} - {{ termin.halbjahr % 2 }}. Hj - {{ termin?.quartal }}. Quartal</span>
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
	<slot name="body">
		<div v-if="!showDetails" class="break-normal">{{ kurzBezeichnungen }}</div>
		<slot name="details" v-if="showDetails">
			<table class="w-full">
				<thead />
				<tbody>
					<svws-ui-drag-data v-for="klausur in klausuren()"
						:key="klausur.id"
						:data="klausur"
						:draggable="klausurDraggable"
						@drag-start="dragStartKlausur($event, klausur)"
						@drag-end="dragEndKlausur($event)"
						tag="tr"
						:class="props.klausurCssClasses === undefined ? '' : props.klausurCssClasses(klausur)">
						<td>{{ props.kursmanager.get(klausur.idKurs)!.kuerzel }}</td>
						<td>{{ mapLehrer.get(props.kursmanager.get(klausur.idKurs)!.lehrer!)?.kuerzel }}</td>
						<td class="text-center">{{ klausur.schuelerIds.size() + "/" + props.kursmanager.get(klausur.idKurs)!.schueler.size() }}</td>
						<td class="text-center">{{ klausur.dauer }}</td>
						<td>&nbsp;</td>
						<td />
					</svws-ui-drag-data>
				</tbody>
			</table>
		</slot>
	</slot>
</template>

<script setup lang="ts">

	import type { GostKursklausurManager, GostKursklausur, GostKlausurtermin, LehrerListeEintrag, KursManager, GostJahrgangsdaten} from "@core";
	import { computed, ref } from 'vue';

	const props = defineProps<{
		jahrgangsdaten: GostJahrgangsdaten;
		showDetails: boolean;
		toggleDetails: boolean;
		termin: GostKlausurtermin;
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

	const klausuren = () => props.kursklausurmanager().getKursklausurenByTermin(props.termin.id);

	const anzahlSuS = computed(() => {
		let anzahl = 0;
		for(const klausur of klausuren().toArray() as GostKursklausur[]) {
			anzahl += klausur.schuelerIds.size();
		}
		return anzahl;
	});

	const kurzBezeichnungen = [...klausuren()].map(k => k.kursKurzbezeichnung).join(", ");

</script>