<template>
	<slot name="header">
		<header class="border-b">
			<div class="text-headline-md">
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
					<tr v-for="klausur in klausuren()"
						:key="klausur.id"
						:data="klausur"
						:draggable="true"
						@dragstart="onDragTermin(klausur)"
						@dragend="onDragTermin(undefined)"
						:class="props.klausurCssClasses === undefined ? '' : props.klausurCssClasses(klausur, termin)">
						<td>{{ props.kursmanager.get(klausur.idKurs)!.kuerzel }}</td>
						<td>{{ mapLehrer.get(props.kursmanager.get(klausur.idKurs)!.lehrer!)?.kuerzel }}</td>
						<td class="text-center">{{ klausur.schuelerIds.size() + "/" + props.kursmanager.get(klausur.idKurs)!.schueler.size() }}</td>
						<td class="text-center">{{ klausur.dauer }}</td>
						<td>&nbsp;</td>
						<td />
					</tr>
				</tbody>
			</table>
		</slot>
	</slot>
</template>

<script setup lang="ts">

	import type { GostKursklausurManager, GostKursklausur, GostKlausurtermin, LehrerListeEintrag, KursManager, GostJahrgangsdaten} from "@core";
	import { computed, ref } from 'vue';
	import type { GostKlausurplanungDragData } from "./SGostKlausurplanung";

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
		klausurCssClasses?: (klausur: GostKursklausur, termin: GostKlausurtermin | undefined) => void;
		onDrag?: (data: GostKlausurplanungDragData) => void;

	}>();

	function onDragTermin(data: GostKlausurplanungDragData) {
		if (props.onDrag !== undefined)
			props.onDrag(data);
	}

	const showDetails = ref(props.showDetails);

	const klausuren = () => props.kursklausurmanager().kursklausurGetMengeByTerminid(props.termin.id);

	const anzahlSuS = computed(() => {
		let anzahl = 0;
		for(const klausur of klausuren().toArray() as GostKursklausur[]) {
			anzahl += klausur.schuelerIds.size();
		}
		return anzahl;
	});

	const kurzBezeichnungen = [...klausuren()].map(k => k.kursKurzbezeichnung).join(", ");

</script>