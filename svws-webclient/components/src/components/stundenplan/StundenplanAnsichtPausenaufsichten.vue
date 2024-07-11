<template>
	<!--TODO: Pausenzeiten, wenn Zeitachse deaktiviert ist-->
	<!-- TODO Modi 'normal', 'kurz, 'tooltip' -->
	<template v-for="pause in getPausenzeitenListeByWochentag(wochentag)" :key="pause">
		<div class="svws-ui-stundenplan--pause" :style="posPause(pause.id)" @dragover="checkDropZonePausenzeit($event, pause)" @drop="onDrop(pause)">
			<template v-if="mode === 'normal'">
				<div v-for="pausenaufsicht in getPausenaufsichten(pause.id)" :key="pausenaufsicht.id" class="svws-ui-stundenplan--pausen-aufsicht flex-grow"
					:class="{'cursor-grab': draggable}"
					:draggable @dragstart="onDrag(pausenaufsicht)" @dragend="onDrag(undefined)">
					<div v-if="textPausenzeit !== undefined" class="font-bold col-span-full mb-1"> {{ textPausenzeit }} </div>
					<div> {{ pause.bezeichnung }} </div>
					<div> <span v-if="!hidePausenaufsicht" title="Lehrkraft"> {{ manager().lehrerGetByIdOrException(pausenaufsicht.idLehrer).kuerzel }} </span> </div>
					<div title="Aufsichtsbereiche"> {{ getAufsichtsbereicheString(pausenaufsicht) }}</div>
				</div>
			</template>
			<template v-if="mode === 'kurz'">
				<div class="flex flex-col justify-center content-center h-full text-center pt-1 pb-2">
					<div v-if="textPausenzeit !== undefined" class="font-bold"> {{ textPausenzeit }} </div>
					<div> {{ pause.bezeichnung }} </div>
					<span v-if="!hidePausenaufsicht" title="Lehrkraft" class="max-w-[24ch] leading-none mx-auto">{{ getPausenaufsichtenString(pause.id) }}</span>
				</div>
			</template>
			<template v-if="mode === 'tooltip'">
				<div class="svws-ui-stundenplan--pausen-aufsicht flex-grow flex items-center justify-center">
					<svws-ui-tooltip>
						<span class="inline-flex items-center leading-tight" :class="{ 'flex-col' : !kompakt, 'flex-row': kompakt }">
							<template v-if="kompakt">
								<span class="text-sm">{{ getPausenaufsichten(pause.id).size() }}&nbsp;</span>
								<span class="icon i-ri-cup-line" />
							</template>
							<template v-else>
								<span>{{ pause.bezeichnung }}</span>
								<span class="text-sm">{{ getPausenaufsichten(pause.id).size() }} Aufsichten</span>
							</template>
						</span>
						<template #content>
							<div class="font-bold mb-2"> {{ pause.bezeichnung }} </div>
							<div v-for="pausenaufsicht in getPausenaufsichten(pause.id)" :key="pausenaufsicht.id" class="grid grid-cols-2 gap-2 items-center" :class="{'cursor-grab': draggable}"
								:draggable @dragstart="onDrag(pausenaufsicht)" @dragend="onDrag(undefined)">
								<div> <span v-if="!hidePausenaufsicht" title="Lehrkraft"> {{ manager().lehrerGetByIdOrException(pausenaufsicht.idLehrer).kuerzel }} </span> </div>
								<div title="Aufsichtsbereiche"> {{ getAufsichtsbereicheString(pausenaufsicht) }}</div>
							</div>
						</template>
					</svws-ui-tooltip>
				</div>
			</template>
		</div>
	</template>
</template>

<script setup lang="ts">

	import type { StundenplanPausenaufsicht } from '../../../../core/src/core/data/stundenplan/StundenplanPausenaufsicht';
	import type { StundenplanPausenzeit } from '../../../../core/src/core/data/stundenplan/StundenplanPausenzeit';
	import type { StundenplanManager } from '../../../../core/src/core/utils/stundenplan/StundenplanManager';
	import type { List } from '../../../../core/src/java/util/List';
	import type { StundenplanAnsichtDragData, StundenplanAnsichtDropZone } from './StundenplanAnsichtProps';

	const props = defineProps<{
		mode: 'normal' | 'kurz' | 'tooltip' | 'aus';
		kompakt: boolean;
		manager: () => StundenplanManager;
		wochentag: number;
		getPausenzeitenListeByWochentag: (wochentag: number) => List<StundenplanPausenzeit>;
		textPausenzeit: string | undefined;
		posPause: (idPausenzeit: number) => string;
		getPausenaufsichten: (idPausenzeit: number) => List<StundenplanPausenaufsicht>;
		hidePausenaufsicht: boolean;
		draggable: boolean;
		onDrag: (data: StundenplanAnsichtDragData, event?: DragEvent) => void;
		onDrop: (zone: StundenplanAnsichtDropZone, wochentyp?: number) => void;
		checkDropZonePausenzeit: (event: DragEvent, pause : StundenplanPausenzeit) => void;
	}>();

	function getPausenaufsichtenString(idPausenzeit: number) {
		let text = "";
		for (const pausenaufsicht of props.getPausenaufsichten(idPausenzeit)) {
			if (text !== '')
				text += ', ';
			text += props.manager().lehrerGetByIdOrException(pausenaufsicht.idLehrer).kuerzel;
		}
		return text;
	}

	function getAufsichtsbereicheString(pausenaufsicht: StundenplanPausenaufsicht): string {
		let result = "";
		for (const zuordnung of pausenaufsicht.bereiche) {
			const bereich = props.manager().aufsichtsbereichGetByIdOrException(zuordnung.idAufsichtsbereich);
			if (result !== "")
				result += ",";
			result += bereich.kuerzel;
		}
		return result;
	}

</script>