<template>
	<div class="flex flex-col border bg-white dark:bg-black rounded-xl cursor-pointer" @drop="onDrop(termin())" @dragover="checkDropZone($event)"
		:class="{
			'shadow-lg shadow-black/5 border-black/10 dark:border-white/10': dragData() === undefined,
			'border-dashed border-svws dark:border-svws ring-4 ring-svws/25': (dragData() !== undefined && dragData() instanceof GostKursklausur && (termin().quartal === dragData()!.quartal) || termin().quartal === 0) && (konflikteTerminDragKlausur === 0),
			'border-dashed border-error dark:border-error': (dragData() !== undefined && dragData() instanceof GostKursklausur && (termin().quartal === dragData()!.quartal) || termin().quartal === 0) && (konflikteTerminDragKlausur > 0 || konflikteTermin() > 0),
			'border-svws/50 dark:border-svws/50 svws-selected': terminSelected,
		}">
		<s-gost-klausurplanung-termin :termin="termin()"
			:kursklausurmanager="kursklausurmanager"
			:map-lehrer="mapLehrer"
			:kursmanager="kursmanager"
			:termin-selected="terminSelected || false"
			:on-drag-klausur="onDrag"
			:klausur-css-classes="klausurCssClasses">
			<template #title>
				<div class="flex gap-2 w-full mb-1">
					<svws-ui-text-input :placeholder="(termin().bezeichnung === null ? (props.kursklausurmanager().kursklausurGetMengeByTerminid(termin().id).size() ? terminTitel() : 'Neuer Termin') : 'Klausurtermin')" :model-value="termin().bezeichnung" @change="bezeichnung => patchKlausurtermin(termin().id, {bezeichnung})" headless />
					<span v-if="(dragData() !== undefined && dragData() instanceof GostKursklausur && (termin().quartal === dragData()!.quartal) || termin().quartal === 0) && (konflikteTerminDragKlausur > 0)" class="inline-flex items-center flex-shrink-0 text-error font-bold text-headline-md -my-1">
						<i-ri-alert-line />
						<span>{{ konflikteTerminDragKlausur >= 0 ? konflikteTerminDragKlausur : konflikteTermin() }}</span>
					</span>
				</div>
			</template>
			<template #actions>
				<svws-ui-button type="transparent" @click="terminQuartalWechseln" :disabled="terminQuartalsWechselMoeglich()" :title="termin().quartal > 0 ? 'Klicken, um alle Quartale zu erlauben' : 'Klicken, um das Quartal festzulegen'" class="group">
					<template v-if="termin().quartal > 0">
						<i-ri-lock-line class="opacity-25 text-sm group-hover:opacity-75" />{{ termin().quartal }}. Quartal
					</template>
					<template v-else>
						<i-ri-lock-unlock-line class="opacity-25 text-sm group-hover:opacity-75" /> Alle
					</template>
				</svws-ui-button>
			</template>
			<template #loeschen>
				<svws-ui-button v-if="loescheKlausurtermine !== undefined && termin !== undefined" type="icon" size="small" class="-mr-1" @click="loescheKlausurtermine(Arrays.asList([termin()]));$event.stopPropagation()"><i-ri-delete-bin-line class="-mx-1.5" /></svws-ui-button>
			</template>
		</s-gost-klausurplanung-termin>
	</div>
</template>

<script setup lang="ts">
	import type { GostKlausurplanungDragData, GostKlausurplanungDropZone } from "./SGostKlausurplanung";
	import { type GostKursklausurManager, GostKursklausur, type GostKlausurtermin, type LehrerListeEintrag, type List, type KursManager, Arrays} from "@core";
	import { computed } from 'vue';

	const props = defineProps<{
		termin: () => GostKlausurtermin;
		kursklausurmanager: () => GostKursklausurManager;
		mapLehrer: Map<number, LehrerListeEintrag>;
		kursmanager: KursManager;
		loescheKlausurtermine?: (termine: List<GostKlausurtermin>) => Promise<void>;
		patchKlausurtermin: (id: number, termin: Partial<GostKlausurtermin>) => Promise<void>;
		klausurCssClasses: (klausur: GostKursklausur, termin: GostKlausurtermin | undefined) => void;
		dragData: () => GostKlausurplanungDragData;
		onDrag: (data: GostKlausurplanungDragData) => void;
		onDrop: (zone: GostKlausurplanungDropZone) => void;
		terminSelected?: boolean;
	}>();

	const klausuren = () => props.kursklausurmanager().kursklausurGetMengeByTerminid(props.termin().id);
	const terminTitel = () => kurzBezeichnungenShort;
	const kurzBezeichnungen = [...klausuren()].map(k => k.kursKurzbezeichnung);
	const kurzBezeichnungenShort = kurzBezeichnungen.length > 3 ? kurzBezeichnungen.slice(0, 3).join(', ') + '...' : kurzBezeichnungen.join(', ');

	function isDropZone() : boolean {
		if ((props.dragData() !== undefined) && (props.dragData() instanceof GostKursklausur))
			if (props.dragData()!.quartal === props.termin().quartal || props.termin().quartal === 0)
				return true;
		return false;
	}

	const terminQuartalsWechselMoeglich = () => props.termin().quartal === 0 && props.kursklausurmanager().quartalGetByTerminid(props.termin().id) === -1;

	async function terminQuartalWechseln() {
		if (props.termin().quartal === 0)
			if (props.kursklausurmanager().quartalGetByTerminid(props.termin().id) > 0)
				await props.patchKlausurtermin(props.termin().id, {quartal: props.kursklausurmanager().quartalGetByTerminid(props.termin().id)});
			else
				return; // TODO Fehlermeldung, Klausuren mit unterschiedlichen Quartale enthalten
		else if (props.termin().quartal > 0 && klausuren().size() > 0)
			await props.patchKlausurtermin(props.termin().id, {quartal: 0});
		else
			await props.patchKlausurtermin(props.termin().id, {quartal: (props.termin().quartal + 1) % 3});
	}

	function checkDropZone(event: DragEvent) {
		if (isDropZone())
			event.preventDefault();
	}

	const konflikteTerminDragKlausur = computed(() => {
		const data = props.dragData();
		if (data instanceof GostKursklausur)
			return props.kursklausurmanager().konflikteAnzahlZuTerminGetByTerminAndKursklausur(props.termin(), data)
		else
			return -1
	});

	const konflikteTermin = () => props.kursklausurmanager().konflikteAnzahlGetByTerminid(props.termin().id);

</script>
