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
							'border bg-white dark:bg-black rounded-lg border-black/10 dark:border-white/10 my-3': raummanager() !== undefined && raummanager()!.getHauptTermin().id === termin.id,
							'cursor-pointer hover:bg-black/10 dark:hover:bg-white/10 pb-1 rounded-lg': raummanager() !== undefined && raummanager()!.getHauptTermin().id !== termin.id,
						}">
						<s-gost-klausurplanung-termin :termin
							:k-man
							:on-drag
							:draggable="isDraggable"
							drag-icon
							:klausur-css-classes="calculatCssClassesKlausur"
							:compact-with-date="raummanager() === undefined || raummanager()!.getHauptTermin().id !== termin.id"
							:show-kursklausuren-nachschreiber="true">
							<template #main v-if="raummanager() === undefined || raummanager()!.getHauptTermin().id !== termin.id"><template /></template>
						</s-gost-klausurplanung-termin>
					</li>
				</ul>
			</svws-ui-content-card>
			<svws-ui-content-card v-if="raummanager() === undefined">
				<div class="h-full rounded-lg shadow-inner flex items-center justify-center py-8 px-3 text-center">
					<span class="opacity-50" v-if="termine().size() > 0">Zum Bearbeiten einen Klausurtermin aus der Planung auswählen.</span>
				</div>
			</svws-ui-content-card>
			<template v-else>
				<s-gost-klausurplanung-raumzeit-termin :termin="raummanager()!.getHauptTermin()"
					:k-man
					:raummanager="() => raummanager()!"
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
	import type { GostKlausurraumManager} from '@core';
	import { GostSchuelerklausurTermin, ListUtils} from '@core';
	import { GostKlausurtermin} from '@core';
	import { GostKlausurraum, GostKursklausur } from '@core';
	import { ref, onMounted } from 'vue';
	import type { GostKlausurplanungRaumzeitProps } from './SGostKlausurplanungRaumzeitProps';
	import type { GostKlausurplanungDragData, GostKlausurplanungDropZone } from './SGostKlausurplanung';

	const props = defineProps<GostKlausurplanungRaumzeitProps>();

	// const raummanager = ref<GostKlausurraumManager>(new GostKlausurraumManager());

	const chooseTermin = async (termin: GostKlausurtermin) => {
		await props.setRaumTermin(termin);
		// if (props.terminauswahl.value === null || termin.id !== props.terminauswahl.value.id) {
		// 	props.raummanager() = await props.erzeugeKlausurraummanager(termin);
		// 	props.terminauswahl.value = termin;
		await props.gotoTermin(termin.id);
		// }

	}

	// const selectedTermin = ref<GostKlausurtermin | null>(null);

	const termine = () => props.kMan().terminMitDatumGetMengeByHalbjahrAndQuartal(props.jahrgangsdaten.abiturjahr, props.halbjahr, props.quartalsauswahl.value, false);

	const calculatCssClassesKlausur = (kl: GostKlausurplanungDragData, termin: GostKlausurtermin | undefined) => {
		const klausur = kl as GostKursklausur;
		const rm = props.raummanager();
		return rm !== undefined
			? {
				"text-black/50 dark:text-white/50": rm.isAlleSchuelerklausurenVerplant(klausur),
				"": !rm.isAlleSchuelerklausurenVerplant(klausur),
			}
			: {}
	};


	const dragData = ref<GostKlausurplanungDragData>(undefined);

	function isDraggable(object: any) : boolean {
		const rm = props.raummanager();
		if (object instanceof GostKursklausur && rm !== undefined) {
			//if (object.idTermin === props.terminauswahl.value.id)
			return !rm.isAlleSchuelerklausurenVerplant(object);
		} else if (object instanceof GostKlausurtermin && rm !== undefined) {
			return object.id === rm.getHauptTermin().id && rm.schuelerklausurOhneRaumGetMenge().size() > 0;
		} else if (object instanceof GostSchuelerklausurTermin && rm !== undefined) {
			return true;
		}
		return false;
	}

	const onDrag = (data: GostKlausurplanungDragData) => dragData.value = data;

	const onDrop = async (zone: GostKlausurplanungDropZone) => {
		const rm = props.raummanager();
		if (dragData.value instanceof GostKursklausur && rm !== undefined) {
			if (zone instanceof GostKlausurraum)
				await props.setzeRaumZuSchuelerklausuren(zone, rm.schuelerklausurGetMengeByKursklausurid(dragData.value.id), rm);
			else if (zone instanceof GostKlausurtermin) {
				await props.setzeRaumZuSchuelerklausuren(null, rm.schuelerklausurGetMengeByKursklausurid(dragData.value.id), rm);
			}
		} else if (dragData.value instanceof GostKlausurtermin && rm !== undefined) {
			if (zone instanceof GostKlausurraum)
				await props.setzeRaumZuSchuelerklausuren(zone, rm.schuelerklausurOhneRaumGetMenge(), rm);
			else if (zone instanceof GostKlausurtermin) {
				await props.setzeRaumZuSchuelerklausuren(null, rm.schuelerklausurOhneRaumGetMenge(), rm);
			}
		} else if (dragData.value instanceof GostSchuelerklausurTermin && rm !== undefined) {
			if (zone instanceof GostKlausurraum)
				await props.setzeRaumZuSchuelerklausuren(zone, ListUtils.create1(dragData.value as GostSchuelerklausurTermin), rm);
			else if (zone instanceof GostKlausurtermin) {
				await props.setzeRaumZuSchuelerklausuren(null, ListUtils.create1(dragData.value), rm);
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
