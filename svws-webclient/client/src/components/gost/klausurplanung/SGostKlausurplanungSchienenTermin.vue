<template>
	<div class="svws-klausurplanung-schienen-termin flex flex-col border bg-white dark:bg-black rounded-xl cursor-pointer" @drop="onDrop(termin())" @dragover="checkDropZone($event)"
		:class="{
			'shadow-lg shadow-black/5 border-black/10 dark:border-white/10': dragData() === undefined,
			'border-dashed border-svws dark:border-svws ring-4 ring-svws/25': (dragData() !== undefined && dragData() instanceof GostKursklausur && (termin().quartal === kMan().vorgabeByKursklausur(dragData() as GostKursklausur).quartal) || termin().quartal === 0) && (konflikteTerminDragKlausur === 0),
			'border-dashed border-error dark:border-error': (dragData() !== undefined && dragData() instanceof GostKursklausur && (termin().quartal === kMan().vorgabeByKursklausur(dragData() as GostKursklausur).quartal) || termin().quartal === 0) && (konflikteTerminDragKlausur > 0 || konflikteTermin() > 0),
			'border-svws/50 dark:border-svws/50 svws-selected': terminSelected,
		}">
		<s-gost-klausurplanung-termin :termin="termin()"
			:benutzer-kompetenzen
			:k-man
			:termin-selected="terminSelected || false"
			:draggable
			:on-drag
			:show-kursschiene="true"
			:klausur-css-classes
			:show-schuelerklausuren
			:patch-klausur
			:goto-kalenderdatum
			:goto-raumzeit-termin
			:create-schuelerklausur-termin>
			<template #title>
				<div class="flex gap-2 w-full mb-1">
					<svws-ui-text-input :placeholder="(termin().bezeichnung === null ? (kMan().kursklausurGetMengeByTermin(termin()).size() ? terminTitel() : 'Neuer Termin') : 'Klausurtermin')" :model-value="termin().bezeichnung" @change="bezeichnung => patchKlausurtermin(termin().id, {bezeichnung})" headless />
					<span v-if="(dragData() !== undefined && dragData() instanceof GostKursklausur && (termin().quartal === kMan().vorgabeByKursklausur(dragData() as GostKursklausur).quartal) || termin().quartal === 0) && (konflikteTerminDragKlausur > 0) || konflikteTermin()" class="inline-flex items-center flex-shrink-0 text-error font-bold text-headline-md -my-1">
						<span class="icon i-ri-alert-line" />
						<span>{{ konflikteTerminDragKlausur >= 0 ? konflikteTerminDragKlausur : konflikteTermin() }}</span>
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
				<svws-ui-button v-if="loescheKlausurtermine !== undefined && termin !== undefined" :disabled="!hatKompetenzUpdate" type="icon" size="small" class="-mr-1" @click="loescheKlausurtermine(Arrays.asList([termin()]));$event.stopPropagation()"><span class="icon i-ri-delete-bin-line -mx-1.5" /></svws-ui-button>
			</template>
		</s-gost-klausurplanung-termin>
	</div>
</template>

<script setup lang="ts">
	import type { GostKlausurplanungDragData, GostKlausurplanungDropZone } from "./SGostKlausurplanung";
	import type { GostHalbjahr, GostKlausurenCollectionSkrsKrsData} from "@core";
	import { BenutzerKompetenz} from "@core";
	import { type GostKlausurplanManager, GostKursklausur, type GostKlausurtermin, type List, Arrays, GostSchuelerklausurTermin} from "@core";
	import { computed } from 'vue';

	const props = withDefaults(defineProps<{
		benutzerKompetenzen: Set<BenutzerKompetenz>,
		termin: () => GostKlausurtermin;
		kMan: () => GostKlausurplanManager;
		loescheKlausurtermine?: (termine: List<GostKlausurtermin>) => Promise<void>;
		patchKlausurtermin: (id: number, termin: Partial<GostKlausurtermin>) => Promise<void>;
		klausurCssClasses: (klausur: GostKlausurplanungDragData, termin: GostKlausurtermin | undefined) => void;
		dragData: () => GostKlausurplanungDragData;
		onDrag: (data: GostKlausurplanungDragData) => void;
		onDrop: (zone: GostKlausurplanungDropZone) => void;
		draggable: (data: GostKlausurplanungDragData) => boolean;
		patchKlausur: (klausur: GostKursklausur | GostSchuelerklausurTermin, patch: Partial<GostKursklausur | GostSchuelerklausurTermin>) => Promise<GostKlausurenCollectionSkrsKrsData>;
		createSchuelerklausurTermin: (id: number) => Promise<void>;
		terminSelected?: boolean;
		showSchuelerklausuren?: boolean;
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

	const terminQuartalsWechselMoeglich = () => !(props.termin().quartal === 0 && props.kMan().quartalGetByTermin(props.termin()) === -1);

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

	function isDropZone() : boolean {
		if (props.dragData() !== undefined) {
			if (props.dragData() instanceof GostKursklausur) {
				if (props.kMan().vorgabeByKursklausur(props.dragData() as GostKursklausur).quartal === props.termin().quartal || props.termin().quartal === 0)
					return true;
			} else if (props.dragData() instanceof GostSchuelerklausurTermin) {
				return true;
			}
		}
		return false;
	}

	function checkDropZone(event: DragEvent) {
		if (isDropZone())
			event.preventDefault();
	}

	const konflikteTerminDragKlausur = computed(() => {
		const data = props.dragData();
		if (data instanceof GostKursklausur)
			return props.kMan().konflikteAnzahlZuTerminGetByTerminAndKursklausur(props.termin(), data)
		else
			return -1
	});

	const konflikteTermin = () => props.kMan().konflikteAnzahlGetByTermin(props.termin());

</script>
