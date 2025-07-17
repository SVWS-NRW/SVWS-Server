<template>
	<template v-if="props.abschnitt !== undefined && props.kMan().stundenplanManagerGeladenByAbschnitt(props.abschnitt.id)">
		<Teleport to=".svws-ui-header--actions" v-if="isMounted">
			<svws-ui-modal-hilfe class="ml-auto"> <s-gost-klausurplanung-raumzeit-hilfe /> </svws-ui-modal-hilfe>
		</Teleport>
		<Teleport to=".router-tab-bar--subnav" v-if="isMounted">
			<s-gost-klausurplanung-quartal-auswahl :quartalsauswahl :halbjahr :zeige-alle-jahrgaenge :set-zeige-alle-jahrgaenge />
		</Teleport>
		<div class="page page-flex-row">
			<div class="min-w-120 max-w-120 flex flex-col gap-2">
				<div class="text-headline-md">In Planung</div>
				<div class="leading-tight flex flex-col gap-0.5" v-if="termine().size() === 0">
					<span>Aktuell keine Klausuren zu planen.</span>
					<span class="opacity-50">Um Räume und Startzeiten festzulegen, müssen Klausuren einem Termin zugeordnet sein.</span>
				</div>
				<ul class="flex flex-col gap-0.5 overflow-auto pr-5">
					<template v-for="termin in termine()" :key="termin.id">
						<li :id="'termin' + termin.id" @click="kMan().stundenplanManagerGetByTerminOrNull(termin) !== null && chooseTermin(termin)"
							:draggable="isDraggable(undefined, termin)" @dragstart="onDrag($event, termin)" @dragend="onDrag($event, undefined)"
							@dragover="checkDropZone($event)" @drop="onDrop(termin)"
							:data="termin"
							:class="{
								'border bg-ui-100 rounded-lg border-ui-10 my-3': terminSelected.value !== undefined && terminSelected.value.id === termin.id,
								'cursor-pointer hover:bg-ui-75 pb-1 rounded-lg': terminSelected.value !== undefined && terminSelected.value.id !== termin.id,
							}">
							<s-gost-klausurplanung-termin :termin :k-man :benutzer-kompetenzen
								:on-drag :draggable="isDraggable" drag-icon :klausur-css-classes="calculatCssClassesKlausur"
								:compact-with-date="terminSelected.value === undefined || terminSelected.value.id !== termin.id"
								:goto-kalenderdatum :patch-klausurtermin :goto-raumzeit-termin="gotoTermin"
								:show-kursklausuren-nachschreiber="true" :hide-button-raeume-planen="true">
								<template #main v-if="(terminSelected.value === undefined) || (terminSelected.value.id !== termin.id)"><div /></template>
							</s-gost-klausurplanung-termin>
						</li>
					</template>
				</ul>
			</div>
			<template v-if="(terminSelected.value === undefined) || (terminSelected.value.datum === null)">
				<div class="h-full rounded-lg shadow-inner flex items-center justify-center py-8 px-3 text-center">
					<span class="opacity-50" v-if="termine().size() > 0">Zum Bearbeiten einen Klausurtermin aus der Planung auswählen.</span>
				</div>
			</template>
			<template v-else>
				<s-gost-klausurplanung-raumzeit-termin :termin="terminSelected.value" :goto-termin
					:k-man :benutzer-kompetenzen :create-klausurraum :loesche-klausurraum :patch-klausurraum :patch-klausur
					:drag-data="() => dragData" :on-drag :on-drop
					:zeige-alle-jahrgaenge :setze-raum-zu-schuelerklausuren
					:get-config-value :set-config-value />
			</template>
		</div>
	</template>
</template>


<script setup lang="ts">

	import type { List } from '@core';
	import { ArrayList, BenutzerKompetenz, GostHalbjahr, GostKlausurraumRich, GostSchuelerklausurTermin, ListUtils } from '@core';
	import { GostKlausurtermin } from '@core';
	import { GostKlausurraum, GostKursklausur } from '@core';
	import { ref, onMounted, computed } from 'vue';
	import type { GostKlausurplanungRaumzeitProps } from './SGostKlausurplanungRaumzeitProps';
	import type { GostKlausurplanungDragData, GostKlausurplanungDropZone } from './SGostKlausurplanung';

	const props = defineProps<GostKlausurplanungRaumzeitProps>();

	const hatKompetenzUpdate = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_AENDERN));

	const chooseTermin = async (termin: GostKlausurtermin) => {
		props.setRaumTermin(termin);
		await props.gotoTermin(termin.abijahr, GostHalbjahr.fromIDorException(termin.halbjahr), termin.id);
	}

	const termine = () => props.kMan().terminMitDatumGetMengeByAbijahrAndHalbjahrAndQuartal(props.jahrgangsdaten.abiturjahr, props.halbjahr, props.quartalsauswahl.value);

	const calculatCssClassesKlausur = (kl: GostKlausurplanungDragData, termin: GostKlausurtermin | undefined) => {
		const klausur = kl as GostKursklausur;
		return {
			"text-ui-50": props.kMan().isKursklausurAlleSchuelerklausurenVerplant(klausur, termin ? termin : null),
			"": !props.kMan().isKursklausurAlleSchuelerklausurenVerplant(klausur, termin ? termin : null),
		}
	};


	const dragData = ref<GostKlausurplanungDragData>(undefined);

	const isDraggable = (object: any, termin: GostKlausurtermin) => {
		if (!hatKompetenzUpdate.value)
			return false;
		if (object instanceof GostKursklausur) {
			//if (object.idTermin === props.terminauswahl.value.id)
			return !props.kMan().isKursklausurAlleSchuelerklausurenVerplant(object, termin);
		} else if (object instanceof GostKlausurtermin && props.terminSelected.value) {
			return object.id === props.terminSelected.value.id && props.kMan().schuelerklausurOhneRaumGetMengeByTermin(props.terminSelected.value).size() > 0;
		} else if (object instanceof GostSchuelerklausurTermin) {
			return true;
		}
		return false;
	}

	const onDrag = (event: DragEvent, data: GostKlausurplanungDragData) => dragData.value = data;

	const onDrop = async (zone: GostKlausurplanungDropZone) => {
		const data = dragData.value;
		if (zone instanceof GostKlausurraum) {
			const rRaum = new GostKlausurraumRich(zone, null);
			if (data instanceof GostKursklausur && props.terminSelected.value)
				rRaum.schuelerklausurterminIDs = mapIDs(props.kMan().schuelerklausurterminAktuellGetMengeByTerminAndKursklausurMultijahrgang(props.terminSelected.value, data, true));
			else if (data instanceof GostKlausurtermin && props.terminSelected.value)
				rRaum.schuelerklausurterminIDs = mapIDs(props.kMan().schuelerklausurOhneRaumGetMengeByTermin(props.terminSelected.value));
			else if (data instanceof GostSchuelerklausurTermin)
				rRaum.schuelerklausurterminIDs = ListUtils.create1(data.id);
			if (!rRaum.schuelerklausurterminIDs.isEmpty())
				await props.setzeRaumZuSchuelerklausuren(ListUtils.create1(rRaum), false);
		} else if (zone instanceof GostKlausurtermin) {
			const rRaum = new GostKlausurraumRich();
			if (data instanceof GostKursklausur && props.terminSelected.value)
				rRaum.schuelerklausurterminIDs = mapIDs(props.kMan().schuelerklausurterminAktuellGetMengeByTerminAndKursklausur(props.terminSelected.value, data));
			else if (data instanceof GostSchuelerklausurTermin)
				rRaum.schuelerklausurterminIDs = ListUtils.create1(data.id);
			if (!rRaum.schuelerklausurterminIDs.isEmpty())
				await props.setzeRaumZuSchuelerklausuren(ListUtils.create1(rRaum), true);
		}
	};

	function mapIDs(skts: List<GostSchuelerklausurTermin>) {
		const numList = new ArrayList<number>();
		for (const skt of skts)
			numList.add(skt.id);
		return numList;
	}

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
		if (props.terminSelected.value) {
			const scrollToElement = document.getElementById("termin" + props.terminSelected.value.id);
			if (scrollToElement)
				scrollToElement.scrollIntoView({ behavior: 'smooth', block: "nearest" });
		}
	});

</script>
