<template>
	<s-gost-klausurplanung-card :class="cardClass"
		interactive
		:selected
		:accent="!termin.istHaupttermin"
		:warning
		:drop-state
		:drop-allowed
		@drop="emit('drop', $event)">
		<div class="text-headline-md leading-none px-3 pt-3 pb-2">
			<div class="flex gap-2 w-full mb-1">
				<svws-ui-text-input :disabled="titleDisabled" :placeholder="titlePlaceholder" :model-value="termin.bezeichnung" @change="bezeichnung => state.patchKlausurtermin(termin.id, { bezeichnung })" headless />
				<span v-if="conflictCount > 0" class="inline-flex items-center shrink-0 text-ui-danger font-bold text-headline-md -my-1">
					<span class="icon i-ri-alert-line icon-ui-caution" />
					<span>{{ conflictCount }}</span>
				</span>
				<span class="border rounded-md p-1 text-button" v-if="showJahrgang">{{ GostHalbjahr.fromIDorException(termin.halbjahr).jahrgang }}</span>
			</div>
			<div class="flex justify-between w-full gap-1 items-center">
				<div>
					<template v-if="termin.datum === null">
						<span class="opacity-25 inline-flex items-center gap-1">
							<span class="icon i-ri-calendar-2-line" />
							<svws-ui-button class="whitespace-nowrap" type="transparent" :disabled="!hatKompetenzUpdate" @click="gotoKalenderdatum(undefined, termin);$event.stopPropagation()" title="Datum setzen" size="small"><span class="icon i-ri-link" /> Datum setzen</svws-ui-button>
						</span>
					</template>
					<template v-else>
						<span class="opacity-50 inline-flex items-center gap-1">
							<span>{{ presenter.terminDatumText(termin) }}</span>
							<svws-ui-button class="whitespace-nowrap" :disabled="!hatKompetenzUpdate" type="transparent" @click="gotoRaumzeitTermin(termin.abiturjahrgang, GostHalbjahr.fromIDorException(termin.halbjahr), termin.id);$event.stopPropagation()" title="Räume planen" size="small"><span class="icon i-ri-link" /> Räume planen</svws-ui-button>
						</span>
					</template>
				</div>
				<div class="flex gap-0.5 items-center -mr-2 -my-1">
					<svws-ui-button type="transparent" @click="terminQuartalWechseln" :disabled="quartalActionDisabled" :title="termin.quartal > 0 ? 'Klicken, um alle Quartale zu erlauben' : 'Klicken, um das Quartal festzulegen'" class="group whitespace-nowrap">
						<template v-if="termin.quartal > 0">
							<span class="icon i-ri-lock-line opacity-25 group-hover:opacity-75" />{{ termin.quartal }}. Quartal
						</template>
						<template v-else>
							<span class="icon i-ri-lock-unlock-line opacity-25 group-hover:opacity-75" /> Alle
						</template>
					</svws-ui-button>
				</div>
			</div>
		</div>
		<slot />
	</s-gost-klausurplanung-card>
</template>

<script setup lang="ts">
	import { computed, type HTMLAttributes } from "vue";
	import type { GostHalbjahr as GostHalbjahrType, GostKlausurtermin } from "@core";
	import { BenutzerKompetenz, GostHalbjahr } from "@core";
	import { useBenutzerState, useGostKlausurplanungState } from "@ui";
	import type { KlausurplanungDropState } from "./SGostKlausurplanungDragUtils";
	import { useKlausurplanungPresenter } from "./SGostKlausurplanungPresenter";

	const props = withDefaults(defineProps<{
		termin: GostKlausurtermin;
		cardClass?: HTMLAttributes["class"];
		selected?: boolean;
		warning?: boolean;
		dropState?: KlausurplanungDropState;
		dropAllowed?: boolean;
		conflictCount?: number;
		titlePlaceholder: string;
		titleDisabled?: boolean;
		quartalDisabled?: boolean;
		showJahrgang?: boolean;
		gotoKalenderdatum: (datum: string | undefined, termin: GostKlausurtermin | undefined) => Promise<void>;
		gotoRaumzeitTermin: (abiturjahr: number, halbjahr: GostHalbjahrType, value: number) => Promise<void>;
	}>(), {
		cardClass: undefined,
		selected: false,
		warning: false,
		dropState: undefined,
		dropAllowed: false,
		conflictCount: 0,
		titleDisabled: false,
		quartalDisabled: false,
		showJahrgang: false,
	});

	const emit = defineEmits<{
		drop: [event: DragEvent];
	}>();

	const state = useGostKlausurplanungState();
	const benutzerState = useBenutzerState();
	const presenter = useKlausurplanungPresenter(state);

	const hatKompetenzUpdate = computed<boolean>(() => benutzerState.benutzerHatKompetenz(BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_AENDERN));
	const terminQuartalsWechselMoeglich = computed<boolean>(() => !((props.termin.quartal === 0) && (state.manager.quartalGetByTermin(props.termin) === -1)));
	const quartalActionDisabled = computed<boolean>(() => !hatKompetenzUpdate.value || props.quartalDisabled || !terminQuartalsWechselMoeglich.value);

	async function terminQuartalWechseln(): Promise<void> {
		const terminQuartal = state.manager.quartalGetByTermin(props.termin);
		if (props.termin.quartal === 0) {
			if (terminQuartal > 0) {
				await state.patchKlausurtermin(props.termin.id, { quartal: terminQuartal });
			}
			return;
		}
		if ((props.termin.quartal > 0) && (state.manager.schuelerklausurterminGetMengeByTermin(props.termin).size() > 0)) {
			await state.patchKlausurtermin(props.termin.id, { quartal: 0 });
		} else {
			await state.patchKlausurtermin(props.termin.id, { quartal: (props.termin.quartal + 1) % 3 });
		}
	}
</script>
