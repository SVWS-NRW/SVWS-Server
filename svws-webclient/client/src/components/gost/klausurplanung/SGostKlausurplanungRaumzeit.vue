<template>
	<Teleport to=".svws-ui-header--actions" v-if="isMounted">
		<svws-ui-modal-hilfe class="ml-auto"> <s-gost-klausurplanung-raumzeit-hilfe /> </svws-ui-modal-hilfe>
	</Teleport>
	<div class="page--content page--content--full relative">
		<svws-ui-content-card>
			<template #title>
				<s-gost-klausurplanung-quartal-auswahl :quartalsauswahl="quartalsauswahl" />
			</template>
			<div class="mb-2">
				<div class="text-headline-md">Planung</div>
				<div class="leading-tight flex flex-col gap-0.5 mt-5" v-if="termine().size() === 0">
					<span>Aktuell keine Klausuren zu planen.</span>
					<span class="opacity-50">Um Räume und Startzeiten festzulegen, müssen Klausuren einem Termin zugeordnet sein.</span>
				</div>
			</div>
			<ul class="flex flex-col gap-0.5 -mx-3 mt-2">
				<li v-for="termin in termine()"
					:key="termin.id"
					@click="chooseTermin(termin)"
					:draggable="isDraggable(termin)"
					@dragstart="onDrag(termin)"
					@dragend="onDrag(undefined)"
					:data="termin"
					:class="{
						'border bg-white dark:bg-black rounded-lg border-black/10 dark:border-white/10 my-3': selectedTermin?.id === termin.id,
						'cursor-pointer': selectedTermin?.id !== termin.id,
					}">
					<s-gost-klausurplanung-termin :termin="termin"
						:kursklausurmanager="kursklausurmanager"
						:map-lehrer="mapLehrer"
						:kursmanager="kursmanager"
						:on-drag-klausur="onDrag"
						:draggable-klausur="isDraggable"
						drag-icon
						:on-drop-termin="onDrop"
						:klausur-css-classes="calculatCssClassesKlausur"
						:compact-with-date="selectedTermin?.id !== termin.id">
						<template #main v-if="selectedTermin?.id !== termin.id"><template /></template>
					</s-gost-klausurplanung-termin>
				</li>
			</ul>
		</svws-ui-content-card>
		<div v-if="selectedTermin === null">
			<div class="h-full border-2 border-dashed bg-white dark:bg-black rounded-xl border-black/10 dark:border-white/10 flex items-center justify-center p-3 text-center">
				<span class="opacity-50" v-if="termine().size() > 0">Zum Bearbeiten einen Klausurtermin aus der Planung auswählen.</span>
			</div>
		</div>
		<template v-else>
			<s-gost-klausurplanung-raumzeit-termin :termin="selectedTermin"
				:kursklausurmanager="kursklausurmanager"
				:faecher-manager="faecherManager"
				:map-lehrer="mapLehrer"
				:kursmanager="kursmanager"
				:raummanager="() => (raummanager as GostKlausurraumManager)"
				:stundenplanmanager="stundenplanmanager"
				:create-klausurraum="createKlausurraum"
				:loesche-klausurraum="loescheKlausurraum"
				:patch-klausurraum="patchKlausurraum"
				:patch-klausur="patchKlausur"
				:drag-data="() => dragData"
				:on-drag="onDrag"
				:on-drop="onDrop" />
		</template>
	</div>
</template>

<script setup lang="ts">
	import type { GostKlausurraumManager} from '@core';
	import { GostKlausurtermin} from '@core';
	import { GostKlausurraum, GostKursklausur } from '@core';
	import type { Ref} from 'vue';
	import { ref, onMounted } from 'vue';
	import type { GostKlausurplanungRaumzeitProps } from './SGostKlausurplanungRaumzeitProps';
	import type { GostKlausurplanungDragData, GostKlausurplanungDropZone } from './SGostKlausurplanung';

	const props = defineProps<GostKlausurplanungRaumzeitProps>();

	const raummanager: Ref<GostKlausurraumManager | null> = ref(null);

	const chooseTermin = async (termin: GostKlausurtermin) => {
		if (selectedTermin.value === null || termin.id !== selectedTermin.value.id) {
			raummanager.value = await props.erzeugeKlausurraummanager(termin);
			selectedTermin.value = termin;
		}
	}

	const selectedTermin = ref<GostKlausurtermin | null>(null);

	const termine = () => props.kursklausurmanager().terminMitDatumGetMengeByQuartal(props.quartalsauswahl.value, false);

	const calculatCssClassesKlausur = (klausur: GostKursklausur) => {
		return raummanager.value !== null
			? {
				"text-black/50 dark:text-white/50": raummanager.value.isAlleSchuelerklausurenVerplant(klausur),
				"": !raummanager.value.isAlleSchuelerklausurenVerplant(klausur),
			}
			: {}
	};


	const dragData = ref<GostKlausurplanungDragData>(undefined);

	function isDraggable(object: any) : boolean {
		if (selectedTermin.value !== null && object instanceof GostKursklausur && raummanager.value !== null) {
			if (object.idTermin === selectedTermin.value.id)
				return !raummanager.value.isAlleSchuelerklausurenVerplant(object);
		} else if (selectedTermin.value !== null && object instanceof GostKlausurtermin && raummanager.value !== null) {
			return object.id === selectedTermin.value.id && raummanager.value.schuelerklausurOhneRaumGetMenge().size() > 0;
		}
		return false;
	}

	const onDrag = (data: GostKlausurplanungDragData) => dragData.value = data;

	const onDrop = async (zone: GostKlausurplanungDropZone) => {
		if (dragData.value instanceof GostKursklausur && raummanager.value !== null) {
			if (zone instanceof GostKlausurraum)
				await props.setzeRaumZuSchuelerklausuren(zone, raummanager.value.schuelerklausurGetMengeByKursklausurid(dragData.value.id), raummanager.value);
			else if (zone instanceof GostKlausurtermin) {
				await props.setzeRaumZuSchuelerklausuren(null, raummanager.value.schuelerklausurGetMengeByKursklausurid(dragData.value.id), raummanager.value);
			}
		} else if (dragData.value instanceof GostKlausurtermin && raummanager.value !== null)
			if (zone instanceof GostKlausurraum)
				await props.setzeRaumZuSchuelerklausuren(zone, raummanager.value.schuelerklausurOhneRaumGetMenge(), raummanager.value);
			else if (zone instanceof GostKlausurtermin) {
				await props.setzeRaumZuSchuelerklausuren(null, raummanager.value.schuelerklausurOhneRaumGetMenge(), raummanager.value);
			}
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

	const isMounted = ref(false);

	onMounted(() => {
		isMounted.value = true;
	});

</script>

<style lang="postcss" scoped>
.page--content {
	@apply grid;
	grid-template-columns: minmax(20rem, 0.25fr) 1fr;
}
</style>
