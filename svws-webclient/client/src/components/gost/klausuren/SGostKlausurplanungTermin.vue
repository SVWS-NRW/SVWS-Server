<template>
	<div class="flex flex-col grow" :class="{'px-3': !inTooltip}">
		<slot name="klausuren">
			<div v-if="(kursklausuren().size() === 0) && (schuelerklausurtermine().size() === 0)">
				Keine Klausuren
			</div>
			<slot name="kursklausuren" v-if="kursklausuren().size()">
				<svws-ui-table :disable-header="!$slots.tableTitle" :class="{'border-t border-ui-25': !$slots.tableTitle}">
					<template #header>
						<div class="svws-ui-tr" :style="tableRowStyle">
							<div class="svws-ui-td col-span-full">
								<slot name="tableTitle" />
							</div>
						</div>
					</template>
					<template #body>
						<s-gost-klausurplanung-kursklausur-zeile v-for="klausur in kursklausuren()"
							:key="klausur.id"
							:klausur
							:termin
							:badge-termin="termin"
							:row-style="tableRowStyle"
							:row-class="props.klausurCssClasses === undefined ? '' : props.klausurCssClasses(klausur, termin)"
							:draggable="isDraggable(klausur)"
							:on-drag
							:in-tooltip
							:show-kursschiene
							:show-quartal="state.manager.quartalGetByTermin(termin) === -1"
							:show-last-klausurtermin />
					</template>
				</svws-ui-table>
			</slot>
			<slot name="schuelerklausuren" v-if="showSchuelerklausuren && schuelerklausurtermine().size()">
				<div :class="{'mt-2 pt-1': schuelerklausurenNachschreiberAnsicht}">
					<s-gost-klausurplanung-schuelerklausur-table :schuelerklausuren="schuelerklausurtermine()"
						:termin
						:on-drag
						:draggable
						:klausur-css-classes
						:nachschreiber-ansicht="schuelerklausurenNachschreiberAnsicht" />
				</div>
			</slot>
			<slot name="nachschreiberSummary" />
			<div class="mt-3">
				<svws-ui-textarea-input class="text-sm" :headless="(termin.bemerkung === null) || (termin.bemerkung.trim().length === 0)" :rows="1"
					resizeable="none" autoresize placeholder="Bemerkungen zum Termin" :disabled="!hatKompetenzUpdate" :model-value="termin.bemerkung"
					@change="bemerkung => state.patchKlausurtermin(termin.id, {bemerkung})" @click="$event.stopPropagation()" @drop.prevent @dragover.prevent />
			</div>
			<span class="flex w-full justify-between items-center gap-1 text-sm mt-auto pr-2" :class="{'pl-3': inTooltip}">
				<div class="py-3" :class="{'opacity-50': !kursklausuren().size() && (showSchuelerklausuren && !schuelerklausurtermine().size())}">
					<span class="font-bold">{{ state.manager.schuelerklausurterminAktuellGetMengeByTermin(termin).size() }} Schüler, </span>
					<span><span v-if="state.manager.minKlausurdauerGetByTermin(termin, true) < state.manager.maxKlausurdauerGetByTermin(termin, true)">{{ state.manager.minKlausurdauerGetByTermin(termin, true) }} - </span>{{ state.manager.maxKlausurdauerGetByTermin(termin, true) }} Minuten</span>
				</div>
				<slot name="loeschen" />
			</span>
		</slot>
	</div>
</template>

<script setup lang="ts">

	import { computed, type HTMLAttributes } from "vue";
	import type { GostKlausurplanungDragData } from "./SGostKlausurplanung";
	import type { GostKlausurtermin } from "@core/core/data/gost/klausuren/GostKlausurtermin";
	import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
	import { useBenutzerState } from "@ui/states/BenutzerState";
	import { useGostKlausurplanungState } from "@ui/states/GostKlausurplanungState";

	const props = defineProps<{
		termin: GostKlausurtermin;
		klausurCssClasses?: (klausur: GostKlausurplanungDragData, termin: GostKlausurtermin | undefined) => HTMLAttributes["class"];
		onDrag?: (event: DragEvent, data: GostKlausurplanungDragData) => void;
		draggable?: (data: GostKlausurplanungDragData, termin: GostKlausurtermin) => boolean;
		showKursschiene?: boolean;
		showLastKlausurtermin?: boolean;
		showSchuelerklausuren?: boolean;
		schuelerklausurenNachschreiberAnsicht?: boolean;
		showKursklausurenNachschreiber?: boolean;
		inTooltip?: boolean;
	}>();

	const benutzerState = useBenutzerState();
	const state = useGostKlausurplanungState();

	const hatKompetenzUpdate = computed<boolean>(() => benutzerState.benutzerHatKompetenz(BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_AENDERN));

	const kursklausuren = () => state.manager.kursklausurMitNachschreibernGetMengeByTermin(props.termin, props.showKursklausurenNachschreiber);
	const schuelerklausurtermine = () => state.manager.schuelerklausurterminNtGetMengeByTermin(props.termin);

	function isDraggable(klausur: GostKlausurplanungDragData): boolean {
		return (props.onDrag !== undefined) && (props.draggable?.(klausur, props.termin) === true);
	}

	const tableRowStyle = computed<string>(() => {
		let result = "grid-template-columns: 1.25rem 1.75rem 5.75rem minmax(3.25rem, 1fr) minmax(4.75rem, max-content) max-content";
		if (props.showKursschiene === true) {
			result += " max-content";
		}
		if (state.manager.quartalGetByTermin(props.termin) === -1) {
			result += " max-content";
		}
		if (props.showLastKlausurtermin === true) {
			result += " max-content";
		}
		return result;
	});

</script>
