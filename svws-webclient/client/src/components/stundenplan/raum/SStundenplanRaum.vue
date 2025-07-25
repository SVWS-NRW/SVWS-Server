<template>
	<div class="page page-flex-row max-w-480">
		<Teleport to=".svws-sub-nav-target" defer>
			<svws-ui-sub-nav :focus-switching-enabled :focus-help-visible>
				<div class="ml-4 flex gap-2 max-w-1/2 min-w1/3 items-center justify-between leading-none">
					<div class="flex gap-2 items-center">
						<div class="text-button font-bold mr-1 -mt-px">Raum:</div>
						<svws-ui-select headless title="Raum" v-model="raum" :items="stundenplanManager().raumGetMengeVerwendetAsList()" :item-text="i => i.kuerzel" autocomplete
							:item-filter="(i, text)=> i.filter(k => k.kuerzel.includes(text.toLocaleLowerCase()))" :item-sort="() => 0" type="transparent" focus-class-sub-nav />
					</div>
					<svws-ui-button type="secondary" @click.stop="ignoreEmpty = !ignoreEmpty" title="Ganzen Stundenplan anzeigen, auch leere Stunden">
						<span class="grow text-nowrap">{{ ignoreEmpty ? 'Keine leeren Stunden':'Alle Stunden' }}</span>
					</svws-ui-button>
					<div v-if="stundenplanManager().getWochenTypModell() > 0" class="flex gap-2 items-center">
						<div class="text-button font-bold mr-1 -mt-px">Wochentyp:</div>
						<svws-ui-select headless title="Wochentyp" v-model="wochentypAnzeige" :items="wochentypen()" class="print:!hidden" type="transparent"
							:disabled="wochentypen().size() <= 0" :item-text="wt => stundenplanManager().stundenplanGetWochenTypAsString(wt)" />
					</div>
				</div>
			</svws-ui-sub-nav>
		</Teleport>
		<Teleport to=".svws-ui-header--actions" defer>
			<s-stundenplan-raum-drucken-modal v-if="raum" v-slot="{ openModal }" :get-p-d-f :api-status :manager="stundenplanManager()" :raum>
				<svws-ui-button @click="openModal" type="secondary"><span class="icon i-ri-printer-line" /> Stundenplan drucken</svws-ui-button>
			</s-stundenplan-raum-drucken-modal>
			<svws-ui-modal-hilfe> <hilfe-raum-stundenplan /> </svws-ui-modal-hilfe>
		</Teleport>
		<div v-if="raum === undefined">Dieser Stundenplan hat noch keine Räume</div>
		<stundenplan-raum v-else class="min-w-fit h-full w-2/3 overflow-scroll pr-4" :id="raum.id" :manager="stundenplanManager"
			:wochentyp="() => wochentypAnzeige" :kalenderwoche="() => undefined" mode-pausenaufsichten="aus" :ignore-empty />
	</div>
</template>

<script setup lang="ts">

	import { computed, shallowRef } from "vue";
	import type { List, StundenplanRaum } from "@core";
	import { ArrayList } from "@core";
	import { useRegionSwitch } from "@ui";
	import type { StundenplanRaumProps } from "./SStundenplanRaumProps";

	const props = defineProps<StundenplanRaumProps>();

	const { focusHelpVisible, focusSwitchingEnabled } = useRegionSwitch();

	const _raum = shallowRef<StundenplanRaum | undefined>();
	const wochentypAnzeige = shallowRef<number>(0);

	const raum = computed<StundenplanRaum | undefined>({
		get: () => {
			if (_raum.value !== undefined)
				try {
					return props.stundenplanManager().raumGetByIdOrException(_raum.value.id);
				} catch (e) {
					return undefined;
				}
			if (props.stundenplanManager().raumGetMengeVerwendetAsList().size() > 0)
				return props.stundenplanManager().raumGetMengeVerwendetAsList().get(0);
			else
				return undefined;
		},
		set: (value) => _raum.value = value,
	});

	const ignoreEmpty = computed<boolean>({
		get: () => props.ganzerStundenplanRaeume(),
		set: (value) => void props.setGanzerStundenplanRaeume(value),
	})

	function wochentypen(): List<number> {
		let modell = props.stundenplanManager().getWochenTypModell();
		if (modell <= 1)
			modell = 0;
		const result = new ArrayList<number>();
		for (let n = 0; n <= modell; n++)
			result.add(n);
		return result;
	}

</script>
