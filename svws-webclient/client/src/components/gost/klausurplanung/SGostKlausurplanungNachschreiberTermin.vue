<template>
	<div class="flex flex-col bg-ui-100 rounded-xl cursor-pointer" @drop="onDrop(termin())" @dragover="checkDropZone($event, termin())" :class="dragData === undefined || isDropZone(termin()) ? '' : 'opacity-35'">
		<s-gost-klausurplanung-termin :termin="termin()"
			:benutzer-kompetenzen
			:k-man
			:termin-selected="terminSelected || false"
			:draggable
			:on-drag
			:show-kursschiene="true"
			:klausur-css-classes
			:patch-klausur
			:show-schuelerklausuren
			:goto-kalenderdatum
			:patch-klausurtermin
			:goto-raumzeit-termin>
			<template #title>
				<div class="flex gap-2 w-full mb-1">
					<svws-ui-text-input :disabled="termin().istHaupttermin" :placeholder="(termin().bezeichnung === null ? (props.kMan().kursklausurGetMengeByTermin(termin()).size() ? terminTitel() : 'Neuer Nachschreibtermin') : 'Klausurtermin')" :model-value="termin().bezeichnung" @change="bezeichnung => patchKlausurtermin(termin().id, {bezeichnung})" headless />
					<span v-if="(dragData !== undefined && dragData instanceof GostSchuelerklausurTermin && (termin().quartal === kMan().vorgabeBySchuelerklausurTermin(dragData).quartal) || termin().quartal === 0) && (konflikteTerminDragKlausur > 0)" class="inline-flex items-center shrink-0 text-ui-danger font-bold text-headline-md -my-1">
						<span class="icon i-ri-alert-line icon-ui-caution" />
						<span>{{ konflikteTerminDragKlausur >= 0 ? konflikteTerminDragKlausur : 2 }}</span>
					</span>
				</div>
			</template>
			<template #actions>
				<svws-ui-button type="transparent" @click="terminQuartalWechseln" :disabled="!hatKompetenzUpdate || !terminQuartalsWechselMoeglich()" :title="termin().quartal > 0 ? 'Klicken, um alle Quartale zu erlauben' : 'Klicken, um das Quartal festzulegen'" class="group">
					<template v-if="termin().quartal > 0">
						<span class="icon i-ri-lock-line opacity-25 group-hover:opacity-75" />{{ termin().quartal }}. Quartal
					</template>
					<template v-else>
						<span class="icon i-ri-lock-unlock-line opacity-25 group-hover:opacity-75" /> Alle
					</template>
				</svws-ui-button>
			</template>
			<template #loeschen>
				<svws-ui-button :disabled="!hatKompetenzUpdate" v-if="loescheKlausurtermine !== undefined && termin !== undefined" type="trash" size="small" @click="(termin().istHaupttermin ? updateKlausurblockung(kMan().patchKlausurterminNachschreiberZuglassenFalse(termin())) : loescheKlausurtermine(Arrays.asList([termin()])));$event.stopPropagation()" />
			</template>
		</s-gost-klausurplanung-termin>
	</div>
</template>


<script setup lang="ts">

	import type { GostKlausurplanungDragData, GostKlausurplanungDropZone } from "./SGostKlausurplanung";
	import type { GostHalbjahr, GostKlausurenCollectionSkrsKrsData, GostKlausurenUpdate} from "@core";
	import { BenutzerKompetenz} from "@core";
	import { GostKursklausur} from "@core";
	import { type GostKlausurplanManager, type GostKlausurtermin, type List, Arrays, GostSchuelerklausurTermin} from "@core";
	import { computed } from 'vue';

	const props = withDefaults(defineProps<{
		benutzerKompetenzen: Set<BenutzerKompetenz>,
		termin: () => GostKlausurtermin;
		kMan: () => GostKlausurplanManager;
		loescheKlausurtermine?: (termine: List<GostKlausurtermin>) => Promise<void>;
		patchKlausurtermin: (id: number, termin: Partial<GostKlausurtermin>) => Promise<void>;
		klausurCssClasses: (klausur: GostKlausurplanungDragData, termin: GostKlausurtermin | undefined) => void;
		dragData: GostKlausurplanungDragData;
		onDrag: (event: DragEvent, data: GostKlausurplanungDragData) => void;
		onDrop: (zone: GostKlausurplanungDropZone) => void;
		draggable: (data: GostKlausurplanungDragData) => boolean;
		terminSelected?: boolean;
		showSchuelerklausuren?: boolean;
		patchKlausur: (klausur: GostKursklausur | GostSchuelerklausurTermin, patch: Partial<GostKursklausur | GostSchuelerklausurTermin>) => Promise<GostKlausurenCollectionSkrsKrsData>;
		updateKlausurblockung: (update: GostKlausurenUpdate) => Promise<void>;
		gotoKalenderdatum: (datum: string | undefined, termin: GostKlausurtermin | undefined) => Promise<void>;
		gotoRaumzeitTermin: (abiturjahr: number, halbjahr: GostHalbjahr, value: number) => Promise<void>;

	}>(), {
		loescheKlausurtermine: undefined,
		showSchuelerklausuren: false,
	});

	const hatKompetenzUpdate = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_AENDERN));

	const klausuren = () => props.kMan().kursklausurGetMengeByTermin(props.termin());
	const terminTitel = () => kurzBezeichnungenShort;
	const kurzBezeichnungen = [...klausuren()].map(k => props.kMan().kursKurzbezeichnungByKursklausur(k));
	const kurzBezeichnungenShort = kurzBezeichnungen.length > 3 ? kurzBezeichnungen.slice(0, 3).join(', ') + '...' : kurzBezeichnungen.join(', ');

	const terminQuartalsWechselMoeglich = () => !props.termin().istHaupttermin && !(props.termin().quartal === 0 && props.kMan().quartalGetByTermin(props.termin()) === -1);

	async function terminQuartalWechseln() {
		const terminQuartal = props.kMan().quartalGetByTermin(props.termin());
		if (props.termin().quartal === 0)
			if (terminQuartal > 0)
				await props.patchKlausurtermin(props.termin().id, { quartal: terminQuartal });
			else
				return; // TODO Fehlermeldung, Klausuren mit unterschiedlichen Quartale enthalten
		else if (props.termin().quartal > 0 && props.kMan().schuelerklausurterminGetMengeByTermin(props.termin()).size() > 0)
			await props.patchKlausurtermin(props.termin().id, {quartal: 0});
		else
			await props.patchKlausurtermin(props.termin().id, {quartal: (props.termin().quartal + 1) % 3});
	}

	function isDropZone(termin: GostKlausurtermin) : boolean {
		if (props.dragData !== undefined) {
			if (props.dragData instanceof GostKursklausur) {
				return false;
			} else if (props.dragData instanceof GostSchuelerklausurTermin) {
				return props.kMan().schuelerklausurterminByTerminAndSchuelerid(termin, props.kMan().schuelerklausurGetByIdOrException(props.dragData.idSchuelerklausur).idSchueler) === null;
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
			return props.kMan().konfliktPaarGetMengeTerminAndSchuelerklausurtermin(props.termin(), props.dragData).size()
		} else
			return -1
	});

</script>
