<template>
	<div class="flex flex-col bg-white dark:bg-black rounded-xl cursor-pointer" @drop="onDrop(termin())" @dragover="checkDropZone($event, termin())">
		<s-gost-klausurplanung-termin :termin="termin()"
			:k-man="kMan"
			:termin-selected="terminSelected || false"
			:draggable="draggable"
			:on-drag="onDrag"
			:show-kursschiene="true"
			:klausur-css-classes="klausurCssClasses"
			:patch-klausur="patchKlausur"
			:show-schuelerklausuren="showSchuelerklausuren">
			<template #title>
				<div class="flex gap-2 w-full mb-1">
					<svws-ui-text-input :disabled="termin().istHaupttermin" :placeholder="(termin().bezeichnung === null ? (props.kMan().kursklausurGetMengeByTerminid(termin().id).size() ? terminTitel() : 'Neuer Nachschreibtermin') : 'Klausurtermin')" :model-value="termin().bezeichnung" @change="bezeichnung => patchKlausurtermin(termin().id, {bezeichnung})" headless />
					<span v-if="(dragData !== undefined && dragData instanceof GostSchuelerklausurTermin && (termin().quartal === kMan().vorgabeBySchuelerklausurTermin(dragData).quartal) || termin().quartal === 0) && (konflikteTerminDragKlausur > 0)" class="inline-flex items-center flex-shrink-0 text-error font-bold text-headline-md -my-1">
						<i-ri-alert-line />
						<span>{{ konflikteTerminDragKlausur >= 0 ? konflikteTerminDragKlausur : 2 }}</span>
					</span>
				</div>
			</template>
			<template #actions>
				<svws-ui-button type="transparent" @click="terminQuartalWechseln" :disabled="!terminQuartalsWechselMoeglich()" :title="termin().quartal > 0 ? 'Klicken, um alle Quartale zu erlauben' : 'Klicken, um das Quartal festzulegen'" class="group">
					<template v-if="termin().quartal > 0">
						<i-ri-lock-line class="opacity-25 text-sm group-hover:opacity-75" />{{ termin().quartal }}. Quartal
					</template>
					<template v-else>
						<i-ri-lock-unlock-line class="opacity-25 text-sm group-hover:opacity-75" /> Alle
					</template>
				</svws-ui-button>
			</template>
			<template #loeschen>
				<svws-ui-button v-if="loescheKlausurtermine !== undefined && termin !== undefined" type="icon" size="small" class="-mr-1" @click="(termin().istHaupttermin ? updateKlausurblockung(kMan().patchKlausurterminNachschreiberZuglassenFalse(termin())) : loescheKlausurtermine(Arrays.asList([termin()])));$event.stopPropagation()"><i-ri-delete-bin-line class="-mx-1.5" /></svws-ui-button>
			</template>
		</s-gost-klausurplanung-termin>
	</div>
</template>

<script setup lang="ts">
	import type { GostKlausurplanungDragData, GostKlausurplanungDropZone } from "./SGostKlausurplanung";
	import type { GostKlausurenCollectionSkrsKrs, GostKlausurenUpdate} from "@core";
	import { type GostKursklausurManager, GostKursklausur, type GostKlausurtermin, type List, Arrays, GostSchuelerklausurTermin} from "@core";
	import { computed } from 'vue';

	const props = withDefaults(defineProps<{
		termin: () => GostKlausurtermin;
		kMan: () => GostKursklausurManager;
		loescheKlausurtermine?: (termine: List<GostKlausurtermin>) => Promise<void>;
		patchKlausurtermin: (id: number, termin: Partial<GostKlausurtermin>) => Promise<void>;
		klausurCssClasses: (klausur: GostKursklausur, termin: GostKlausurtermin | undefined) => void;
		dragData: GostKlausurplanungDragData;
		onDrag: (data: GostKlausurplanungDragData) => void;
		onDrop: (zone: GostKlausurplanungDropZone) => void;
		draggable: (data: GostKlausurplanungDragData) => boolean;
		terminSelected?: boolean;
		showSchuelerklausuren?: boolean;
		patchKlausur: (klausur: GostKursklausur | GostSchuelerklausurTermin, patch: Partial<GostKursklausur | GostSchuelerklausurTermin>) => Promise<GostKlausurenCollectionSkrsKrs>;
		updateKlausurblockung: (update: GostKlausurenUpdate) => Promise<void>;
	}>(), {
		loescheKlausurtermine: undefined,
		showSchuelerklausuren: false,
	});

	const klausuren = () => props.kMan().kursklausurGetMengeByTerminid(props.termin().id);
	const terminTitel = () => kurzBezeichnungenShort;
	const kurzBezeichnungen = [...klausuren()].map(k => props.kMan().kursKurzbezeichnungByKursklausur(k));
	const kurzBezeichnungenShort = kurzBezeichnungen.length > 3 ? kurzBezeichnungen.slice(0, 3).join(', ') + '...' : kurzBezeichnungen.join(', ');

	const terminQuartalsWechselMoeglich = () => !props.termin().istHaupttermin && !(props.termin().quartal === 0 && props.kMan().quartalGetByTerminid(props.termin().id) === -1);

	async function terminQuartalWechseln() {
		const terminQuartal = props.kMan().quartalGetByTerminid(props.termin().id);
		if (props.termin().quartal === 0)
			if (terminQuartal > 0)
				await props.patchKlausurtermin(props.termin().id, { quartal: terminQuartal });
			else
				return; // TODO Fehlermeldung, Klausuren mit unterschiedlichen Quartale enthalten
		else if (props.termin().quartal > 0 && props.kMan().schuelerklausurterminGetMengeByTerminid(props.termin().id).size() > 0)
			await props.patchKlausurtermin(props.termin().id, {quartal: 0});
		else
			await props.patchKlausurtermin(props.termin().id, {quartal: (props.termin().quartal + 1) % 3});
	}

	function isDropZone(termin: GostKlausurtermin) : boolean {
		if (props.dragData !== undefined) {
			if (props.dragData instanceof GostKursklausur) {
				return false;
			} else if (props.dragData instanceof GostSchuelerklausurTermin) {
				return props.kMan().schuelerklausurterminByTerminidAndSchuelerid(termin.id, props.kMan().schuelerklausurGetByIdOrException(props.dragData.idSchuelerklausur).idSchueler) === null;
			}
		}
		return false;
	}

	function checkDropZone(event: DragEvent, termin: GostKlausurtermin) {
		if (isDropZone(termin))
			event.preventDefault();
	}

	const konflikteTerminDragKlausur = computed(() => {
		if (props.dragData instanceof GostSchuelerklausurTermin) {
			return props.kMan().konflikteAnzahlZuTerminGetByTerminAndSchuelerklausurtermin(props.termin(), props.dragData)
		} else
			return -1
	});

</script>
