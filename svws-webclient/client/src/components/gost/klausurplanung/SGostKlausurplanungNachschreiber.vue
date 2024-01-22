<template>
	<Teleport to=".svws-ui-header--actions" v-if="isMounted">
		<svws-ui-modal-hilfe class="ml-auto"> <s-gost-klausurplanung-schienen-hilfe /> </svws-ui-modal-hilfe>
	</Teleport>
	<Teleport to=".router-tab-bar--subnav" v-if="isMounted">
		<s-gost-klausurplanung-quartal-auswahl :quartalsauswahl="quartalsauswahl" :halbjahr="halbjahr" />
	</Teleport>

	<div class="page--content page--content--full relative">
		<svws-ui-content-card title="In Planung">
			<div class="flex flex-col" @drop="onDrop(undefined)" @dragover="$event.preventDefault()">
				<s-gost-klausurplanung-schuelerklausur-table :k-man="kMan"
					:schuelerklausuren="kMan().schuelerklausurterminNtAktuellOhneTerminGetMengeByHalbjahrAndQuartal(props.halbjahr, props.quartalsauswahl.value)"
					:map-schueler="mapSchueler"
					:on-drag="onDrag"
					:draggable="() => true">
					<template #noData>
						<div class="leading-tight flex flex-col gap-0.5">
							<span>Aktuell keine Nachschreibklausuren zu planen.</span>
							<span class="opacity-50">Bereits geplante Klausuren können hier zurückgelegt werden.</span>
						</div>
					</template>
				</s-gost-klausurplanung-schuelerklausur-table>
			</div>
		</svws-ui-content-card>
		<svws-ui-content-card>
			<div class="flex justify-between items-start mb-5">
				<div class="flex flex-wrap items-center gap-0.5 w-full">
					<svws-ui-button @click="erzeugeKlausurtermin(quartalsauswahl.value, false)"><i-ri-add-line class="-ml-1" />Termin<template v-if="termine.size() === 0"> hinzufügen</template></svws-ui-button>
				</div>
			</div>
			<div class="grid grid-cols-[repeat(auto-fill,minmax(20rem,1fr))] gap-4 pt-2 -mt-2">
				<template v-if="termine.size()">
					<s-gost-klausurplanung-nachschreiber-termin v-for="termin of termine" :key="termin.id"
						:termin="() => termin"
						:class="undefined"
						:k-man="kMan"
						:map-schueler="mapSchueler"
						:drag-data="dragData"
						:on-drag="onDrag"
						:on-drop="onDrop"
						:draggable="draggable"
						:termin-selected="terminSelected?.id===termin.id"
						@click="terminSelected=(terminSelected?.id===termin.id?undefined:termin);$event.stopPropagation()"
						:loesche-klausurtermine="loescheKlausurtermine"
						:patch-klausurtermin="patchKlausurtermin"
						:klausur-css-classes="klausurCssClasses"
						:show-schuelerklausuren="true" />
				</template>
				<template v-else>
					<div class="shadow-inner rounded-lg h-48" />
					<div class="shadow-inner rounded-lg h-48" />
					<div class="shadow-inner rounded-lg h-48" />
				</template>
			</div>
		</svws-ui-content-card>
		<svws-ui-content-card class="-ml-4">
			<!--<template #title>
				<span class="text-headline-md leading-none inline-flex gap-1">
					<template v-if="klausurKonflikte().size() > 0">
						<i-ri-alert-fill class="text-error -my-0.5" />
						<span>{{ klausurKonflikte().size() }} Kurse mit Konflikten</span>
					</template>
					<template v-else-if="anzahlProKwKonflikte(3).size() > 0">
						<i-ri-alert-fill class="text-error -my-0.5" />
						<span> Konflikte</span>
					</template>
					<template v-else-if="terminSelected !== undefined || dragData !== undefined">
						<i-ri-checkbox-circle-fill class="text-success -my-1" />
						<span>Keine Konflikte</span>
					</template>
					<template v-else>
						<span class="opacity-50">Konflikte</span>
					</template>
				</span>
			</template>
			<div v-if="klausurKonflikte().size() > 0" class="mt-5" :class="{'mb-16': anzahlProKwKonflikte(3).size() > 0}">
				<ul class="flex flex-col gap-3">
					<li v-for="klausur in klausurKonflikte()" :key="klausur.getKey().id">
						<span class="svws-ui-badge" :style="`--background-color: ${getBgColor(klausur.getKey().kursKurzbezeichnung.split('-')[0])};`">{{ klausur.getKey().kursKurzbezeichnung }}</span>
						<div class="leading-tight">
							{{ [...klausur.getValue()].map(sid => mapSchueler.get(sid)?.vorname + ' ' + mapSchueler.get(sid)?.nachname).join(", ") }}
						</div>
					</li>
				</ul>
			</div>
			<div v-if="anzahlProKwKonflikte(3).size() > 0" class="mt-5">
				<div class="text-headline-md leading-tight mb-3">
					<div class="inline-flex gap-1">{{ anzahlProKwKonflikte(3).size() }} Schüler:innen</div>
					<div class="opacity-50">Drei oder mehr Klausuren in einer KW</div>
				</div>
				<ul class="flex flex-col gap-4">
					<li v-for="konflikt in anzahlProKwKonflikte(3)" :key="konflikt.getKey()">
						<span class="font-bold">{{ mapSchueler.get(konflikt.getKey())?.vorname + ' ' + mapSchueler.get(konflikt.getKey())?.nachname }}</span>
						<div class="grid grid-cols-3 gap-x-1 gap-y-2 mt-0.5">
							<span v-for="klausur in konflikt.getValue()" :key="klausur.id" class="svws-ui-badge flex-col w-full" :style="`--background-color: ${getBgColor(klausur.kursKurzbezeichnung.split('-')[0])};`">
								<span class="text-button font-medium">{{ klausur.kursKurzbezeichnung }}</span>
								<span class="text-sm font-medium">{{ DateUtils.gibDatumGermanFormat(kMan().terminByKursklausur(klausur).datum !== null ? kMan().terminByKursklausur(klausur).datum : terminSelected.datum) }}</span>
							</span>
						</div>
					</li>
				</ul>
			</div>
			<div v-else-if="terminSelected === undefined" class="mt-5 opacity-50 flex flex-col gap-2">
				<span>Klicke auf einen Termin oder verschiebe eine Klausur, um Details zu bestehenden bzw. entstehenden Konflikten anzuzeigen.</span>
			</div>-->
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">

	import {GostKursklausur, GostKlausurtermin, GostSchuelerklausurTermin } from "@core";
	import { computed, ref, onMounted } from 'vue';
	import type { GostKlausurplanungDragData, GostKlausurplanungDropZone } from "./SGostKlausurplanung";
	import type { GostKlausurplanungNachschreiberProps } from "./SGostKlausurplanungNachschreiberProps";

	const props = defineProps<GostKlausurplanungNachschreiberProps>();

	const dragData = ref<GostKlausurplanungDragData>(undefined);
	const terminSelected = ref<GostKlausurtermin | undefined>(undefined);

	const onDrag = (data: GostKlausurplanungDragData) => {
		terminSelected.value = undefined;
		dragData.value = data;
	};

	// const klausurKonflikte = () => {
	// 	if (dragData.value !== undefined && terminSelected.value !== undefined) {
	// 		if (dragData.value.quartal === terminSelected.value.quartal || terminSelected.value.quartal === 0)
	// 			return props.kMan().konflikteNeuMapKursklausurSchueleridsByTerminidAndKursklausurid(terminSelected.value.id, dragData.value.id).entrySet();
	// 	} else if (terminSelected.value !== undefined)
	// 		return props.kMan().konflikteMapKursklausurSchueleridsByTerminid(terminSelected.value.id).entrySet();
	// 	return new HashSet<JavaMapEntry<GostKursklausur, JavaSet<number>>>();
	// }

	// const anzahlProKwKonflikte = (threshold: number) => {
	// 	if (dragData.value !== undefined && terminSelected.value !== undefined && dragData.value instanceof GostKursklausur) {
	// 		if (dragData.value.quartal === terminSelected.value.quartal || terminSelected.value.quartal === 0)
	// 			return props.kMan().klausurenProSchueleridExceedingKWThresholdByTerminAndKursklausurAndThreshold(terminSelected.value, dragData.value, threshold);
	// 	} else if (terminSelected.value !== undefined)
	// 		return props.kMan().klausurenProSchueleridExceedingKWThresholdByTerminAndThreshold(terminSelected.value, threshold);
	// 	return new HashSet<number>();
	// }

	function draggable(data: GostKlausurplanungDragData) {
		return data instanceof GostSchuelerklausurTermin;
	}

	const onDrop = async (zone: GostKlausurplanungDropZone) => {
		if (dragData.value instanceof GostKursklausur || dragData.value instanceof GostSchuelerklausurTermin) {
			if (zone === undefined && dragData.value.idTermin !== null)
				await props.patchKlausur(dragData.value, {idTermin: null});
			else if (zone instanceof GostKlausurtermin) {
				if (zone.id !== dragData.value.idTermin) {
					await props.patchKlausur(dragData.value, {idTermin: zone.id});
					terminSelected.value = zone;
				}
			}
		}
	};

	// const dropOverCssClasses = (termin: GostKlausurtermin) => ({
	// 	"bg-success": dragData.value !== undefined && (dragData.value.quartal === termin.quartal || termin.quartal === 0),
	// 	"opacity-25 border-transparent shadow-none": dragData.value !== undefined && (dragData.value.quartal !== termin.quartal && termin.quartal !== 0),
	// });

	const termine = computed(() => props.kMan().terminGetNTMengeByHalbjahrAndQuartal(props.halbjahr, props.quartalsauswahl.value, true));

	const klausurCssClasses = (klausur: GostKursklausur, termin: GostKlausurtermin | undefined) => {
		let konfliktfreiZuFremdtermin = false;
		// for (const oTermin of termine.value) {
		// 	if (oTermin.id !== klausur.idTermin && oTermin.quartal === klausur.quartal || oTermin.quartal === 0)
		// 		konfliktfreiZuFremdtermin = props.kMan().konflikteAnzahlZuTerminGetByTerminAndKursklausur(oTermin, klausur) === 0;
		// 	if (konfliktfreiZuFremdtermin)
		// 		break;
		// }
		const konfliktZuEigenemTermin = termin === undefined || klausur === null ? false : true;//props.kMan().konflikteAnzahlZuEigenemTerminGetByKursklausur(klausur) > 0;
		return {
			"svws-ok": !konfliktZuEigenemTermin && konfliktfreiZuFremdtermin,
			"svws-warning": !konfliktfreiZuFremdtermin,
			"svws-error": konfliktZuEigenemTermin,
		}
	};

	const isMounted = ref(false);
	onMounted(() => {
		isMounted.value = true;
	});

</script>

<style lang="postcss" scoped>
.page--content {
  @apply grid;
  grid-template-columns: minmax(22rem, 0.2fr) 1fr minmax(22rem, 0.2fr);
}
</style>
