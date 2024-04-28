<template>
	<template v-if="hatStundenplanManager">
		<Teleport to=".svws-ui-header--actions" v-if="isMounted">
			<svws-ui-modal-hilfe class="ml-auto"> <s-gost-klausurplanung-raumzeit-hilfe /> </svws-ui-modal-hilfe>
		</Teleport>
		<Teleport to=".router-tab-bar--subnav" v-if="isMounted">
			<s-gost-klausurplanung-quartal-auswahl :quartalsauswahl :halbjahr :zeige-alle-jahrgaenge :set-zeige-alle-jahrgaenge />
		</Teleport>
		<div class="page--content page--content--full relative">
			<svws-ui-content-card title="In Planung">
				<div class="mb-2">
					<div class="leading-tight flex flex-col gap-0.5" v-if="termine().size() === 0">
						<span>Aktuell keine Klausuren zu planen.</span>
						<span class="opacity-50">Um Räume und Startzeiten festzulegen, müssen Klausuren einem Termin zugeordnet sein.</span>
					</div>
				</div>
				<ul class="flex flex-col gap-0.5 -mx-3">
					<li v-for="termin in termine()"
						:key="termin.id"
						@click="chooseTermin(termin)"
						:draggable="isDraggable(termin)"
						@dragstart="onDrag(termin)"
						@dragend="onDrag(undefined)"
						@dragover="checkDropZone($event)"
						@drop="onDrop(termin)"
						:data="termin"
						:class="{
							'border bg-white dark:bg-black rounded-lg border-black/10 dark:border-white/10 my-3': terminauswahl.value?.id === termin.id,
							'cursor-pointer hover:bg-black/10 dark:hover:bg-white/10 pb-1 rounded-lg': terminauswahl.value?.id !== termin.id,
						}">
						<s-gost-klausurplanung-termin :termin
							:k-man
							:on-drag
							:draggable="isDraggable"
							drag-icon
							:klausur-css-classes="calculatCssClassesKlausur"
							:compact-with-date="terminauswahl.value?.id !== termin.id"
							:show-kursklausuren-nachschreiber="true">
							<template #main v-if="terminauswahl.value?.id !== termin.id"><template /></template>
						</s-gost-klausurplanung-termin>
					</li>
				</ul>
			</svws-ui-content-card>
			<svws-ui-content-card v-if="terminauswahl.value === null">
				<div class="h-full rounded-lg shadow-inner flex items-center justify-center py-8 px-3 text-center">
					<span class="opacity-50" v-if="termine().size() > 0">Zum Bearbeiten einen Klausurtermin aus der Planung auswählen.</span>
				</div>
			</svws-ui-content-card>
			<template v-else>
				<s-gost-klausurplanung-raumzeit-termin :termin="terminauswahl.value"
					:k-man
					:raummanager="() => (raummanager as GostKlausurraumManager)"
					:stundenplanmanager="stundenplanmanager()"
					:create-klausurraum
					:loesche-klausurraum
					:patch-klausurraum
					:patch-klausur
					:drag-data="() => dragData"
					:on-drag
					:on-drop
					:zeige-alle-jahrgaenge />
			</template>
		</div>
	</template>
</template>

<script setup lang="ts">
	import { GostKlausurraumManager, GostSchuelerklausurTermin, ListUtils} from '@core';
	import { GostKlausurtermin} from '@core';
	import { GostKlausurraum, GostKursklausur } from '@core';
	import { ref, onMounted } from 'vue';
	import type { GostKlausurplanungRaumzeitProps } from './SGostKlausurplanungRaumzeitProps';
	import type { GostKlausurplanungDragData, GostKlausurplanungDropZone } from './SGostKlausurplanung';

	const props = defineProps<GostKlausurplanungRaumzeitProps>();

	const raummanager = ref<GostKlausurraumManager>(new GostKlausurraumManager());

	const chooseTermin = async (termin: GostKlausurtermin) => {
		if (props.terminauswahl.value === null || termin.id !== props.terminauswahl.value.id) {
			raummanager.value = await props.erzeugeKlausurraummanager(termin);
			props.terminauswahl.value = termin;
			console.log(props.terminauswahl.value?.id);
		}

	}

	// const selectedTermin = ref<GostKlausurtermin | null>(null);

	const termine = () => props.kMan().terminMitDatumGetMengeByHalbjahrAndQuartal(props.jahrgangsdaten.abiturjahr, props.halbjahr, props.quartalsauswahl.value, false);

	const calculatCssClassesKlausur = (kl: GostKlausurplanungDragData, termin: GostKlausurtermin | undefined) => {
		const klausur = kl as GostKursklausur;
		return raummanager.value !== null
			? {
				"text-black/50 dark:text-white/50": raummanager.value.isAlleSchuelerklausurenVerplant(klausur),
				"": !raummanager.value.isAlleSchuelerklausurenVerplant(klausur),
			}
			: {}
	};


	const dragData = ref<GostKlausurplanungDragData>(undefined);

	function isDraggable(object: any) : boolean {
		if (props.terminauswahl.value !== null && object instanceof GostKursklausur && raummanager.value !== null) {
			//if (object.idTermin === props.terminauswahl.value.id)
			return !raummanager.value.isAlleSchuelerklausurenVerplant(object);
		} else if (props.terminauswahl.value !== null && object instanceof GostKlausurtermin && raummanager.value !== null) {
			return object.id === props.terminauswahl.value.id && raummanager.value.schuelerklausurOhneRaumGetMenge().size() > 0;
		} else if (props.terminauswahl.value !== null && object instanceof GostSchuelerklausurTermin && raummanager.value !== null) {
			return true;
		}
		return false;
	}

	const onDrag = (data: GostKlausurplanungDragData) => dragData.value = data;

	const onDrop = async (zone: GostKlausurplanungDropZone) => {
		if (dragData.value instanceof GostKursklausur && raummanager.value !== null) {
			if (zone instanceof GostKlausurraum)
				await props.setzeRaumZuSchuelerklausuren(zone, raummanager.value.schuelerklausurGetMengeByKursklausurid(dragData.value.id), raummanager.value as GostKlausurraumManager);
			else if (zone instanceof GostKlausurtermin) {
				await props.setzeRaumZuSchuelerklausuren(null, raummanager.value.schuelerklausurGetMengeByKursklausurid(dragData.value.id), raummanager.value as GostKlausurraumManager);
			}
		} else if (dragData.value instanceof GostKlausurtermin && raummanager.value !== null) {
			if (zone instanceof GostKlausurraum)
				await props.setzeRaumZuSchuelerklausuren(zone, raummanager.value.schuelerklausurOhneRaumGetMenge(), raummanager.value as GostKlausurraumManager);
			else if (zone instanceof GostKlausurtermin) {
				await props.setzeRaumZuSchuelerklausuren(null, raummanager.value.schuelerklausurOhneRaumGetMenge(), raummanager.value as GostKlausurraumManager);
			}
		} else if (dragData.value instanceof GostSchuelerklausurTermin && raummanager.value !== null) {
			if (zone instanceof GostKlausurraum)
				await props.setzeRaumZuSchuelerklausuren(zone, ListUtils.create1(dragData.value as GostSchuelerklausurTermin), raummanager.value as GostKlausurraumManager);
			else if (zone instanceof GostKlausurtermin) {
				await props.setzeRaumZuSchuelerklausuren(null, ListUtils.create1(dragData.value), raummanager.value as GostKlausurraumManager);
			}
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

.svws-ui-tab-content {
	@apply overflow-y-hidden items-start;

	.page--content {
		@apply h-full py-0 auto-rows-auto;

		.content-card {
			@apply max-h-full pt-8 pb-16 px-4 -mx-4 overflow-y-auto h-[unset];
			scrollbar-gutter: stable;
		}
	}
}
</style>
