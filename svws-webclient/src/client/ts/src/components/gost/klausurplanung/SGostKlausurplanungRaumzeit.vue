<template>
	<svws-ui-sub-nav>
		<s-gost-klausurplanung-quartal-auswahl :quartalsauswahl="quartalsauswahl" />
		<svws-ui-modal-hilfe class="ml-auto"> <s-gost-klausurplanung-raumzeit-hilfe /> </svws-ui-modal-hilfe>
	</svws-ui-sub-nav>

	<div class="page--content page--content--full min-w-fit gap-x-8 2xl:gap-x-16 relative">
		<svws-ui-content-card title="Zu planende Termine" class="flex flex-col">
			<ul class="flex flex-col gap-y-1">
				<li v-for="termin in termine" :key="termin.id" @click="chooseTermin(termin);$event.stopPropagation()" :class="calculatCssClassesTermin(termin)" class="rounded bg-dark-20 p-2"
					@dragover="checkDropZone($event)" @drop="onDrop(termin)">
					<header>
						<div class="text-headline-md">
							<span class="break-normal">{{ kurzBezeichnungen(termin) }}</span>
						</div>
						<div>
							<span class="">{{ termin?.quartal }}. Quartal - {{ anzahlSuS(termin) }} SuS</span>
							<span class="float-right">{{ termin.datum === null ? "Noch kein Datum" : new Date(termin.datum).toLocaleString("de-DE").split(",")[0] }}</span>
						</div>
					</header>
					<table class="w-full border-t" v-if="selectedTermin?.id === termin.id">
						<thead />
						<tbody>
							<tr v-for="klausur in klausuren(termin)"
								:key="klausur.id"
								:data="klausur"
								:class="calculatCssClassesKlausur(klausur)"
								:draggable="isDraggable(klausur)"
								@dragstart="onDrag(klausur)"
								@dragend="onDrag(undefined)">
								<td>{{ props.kursmanager.get(klausur.idKurs)!.kuerzel }}</td>
								<td>{{ mapLehrer.get(props.kursmanager.get(klausur.idKurs)!.lehrer!)?.kuerzel }}</td>
								<td class="text-center">{{ klausur.schuelerIds.size() + "/" + props.kursmanager.get(klausur.idKurs)!.schueler.size() }}</td>
								<td class="text-center">{{ klausur.dauer }}</td>
								<td>&nbsp;</td>
								<td />
							</tr>
						</tbody>
					</table>
				</li>
			</ul>
		</svws-ui-content-card>
		<div v-if="selectedTermin === null">Bitte Termin durch Klick auswählen!</div>
		<div class="h-full" v-else>
			<s-gost-klausurplanung-raumzeit-termin :termin="selectedTermin"
				:kursklausurmanager="kursklausurmanager"
				:faecher-manager="faecherManager"
				:map-lehrer="mapLehrer"
				:kursmanager="kursmanager"
				:raummanager="() => raummanager as GostKlausurraumManager"
				:stundenplanmanager="stundenplanmanager"
				:create-klausurraum="createKlausurraum"
				:loesche-klausurraum="loescheKlausurraum"
				:patch-klausurraum="patchKlausurraum"
				:patch-klausur="patchKlausur"
				:drag-data="() => dragData"
				:on-drag="onDrag"
				:on-drop="onDrop" />
		</div>
	</div>
</template>

<script setup lang="ts">
	import type { GostKlausurraumManager} from '@core';
	import { GostKlausurtermin} from '@core';
	import { GostKlausurraum, GostKursklausur } from '@core';
	import { computed, ref } from 'vue';
	import type { GostKlausurplanungRaumzeitProps } from './SGostKlausurplanungRaumzeitProps';
	import type { GostKlausurplanungDragData, GostKlausurplanungDropZone } from './SGostKlausurplanung';

	const props = defineProps<GostKlausurplanungRaumzeitProps>();

	const raummanager = ref<GostKlausurraumManager | null>(null);

	const chooseTermin = async (termin: GostKlausurtermin) => {
		if (selectedTermin.value === null || termin.id !== selectedTermin.value.id) {
			raummanager.value = await props.erzeugeKlausurraummanager(termin);
			selectedTermin.value = termin;
		}
	}

	const selectedTermin = ref<GostKlausurtermin | null>(null);

	const termine = computed(() => props.kursklausurmanager().terminMitDatumGetMengeByQuartal(props.quartalsauswahl.value));

	const calculatCssClassesTermin = (termin: GostKlausurtermin) => ({
		"bg-green-100": selectedTermin.value !== null && selectedTermin.value.id === termin.id,
	});

	const calculatCssClassesKlausur = (klausur: GostKursklausur) => ({
		"bg-green-500": raummanager.value!.isAlleSchuelerklausurenVerplant(klausur),
		"bg-yellow-500": !raummanager.value!.isAlleSchuelerklausurenVerplant(klausur),
	});


	const dragData = ref<GostKlausurplanungDragData>(undefined);

	function isDraggable(object: any) : boolean {
		if (selectedTermin.value !== null && object instanceof GostKursklausur)
			if (object.idTermin === selectedTermin.value.id)
				return !raummanager.value!.isAlleSchuelerklausurenVerplant(object);
		return false;
	}

	const onDrag = (data: GostKlausurplanungDragData) => {
		dragData.value = data;
	};

	const onDrop = async (zone: GostKlausurplanungDropZone) => {
		if (dragData.value instanceof GostKursklausur)
			if (zone instanceof GostKlausurraum)
				await props.setzeRaumZuSchuelerklausuren(zone, raummanager.value!.schuelerklausurGetMengeByKursklausurid(dragData.value.id), raummanager.value!);
			else if (zone instanceof GostKlausurtermin)
				await props.setzeRaumZuSchuelerklausuren(null, raummanager.value!.schuelerklausurGetMengeByKursklausurid(dragData.value.id), raummanager.value!);
	};

	function isDropZone() : boolean {
		if ((dragData.value === undefined))
			return false;
		return true;
	}

	function checkDropZone(event: DragEvent) {
		if (isDropZone())
			event.preventDefault();
	}

	const klausuren = (termin: GostKlausurtermin) => props.kursklausurmanager().kursklausurGetMengeByTerminid(termin.id);

	function anzahlSuS(termin: GostKlausurtermin) {
		let anzahl = 0;
		for(const klausur of klausuren(termin).toArray() as GostKursklausur[]) {
			anzahl += klausur.schuelerIds.size();
		}
		return anzahl;
	}

	function kurzBezeichnungen(termin: GostKlausurtermin) {
		return [...klausuren(termin)].map(k => k.kursKurzbezeichnung).join(", ");
	}



</script>
