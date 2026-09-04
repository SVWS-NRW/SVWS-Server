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
			<svws-ui-button @click="show = true" type="secondary"><span class="icon i-ri-printer-line" /> Stundenplan drucken</svws-ui-button>
			<svws-ui-modal-hilfe> <hilfe-raum-stundenplan /> </svws-ui-modal-hilfe>
		</Teleport>
		<div v-if="raum === null">Dieser Stundenplan hat noch keine Räume</div>
		<stundenplan-raum v-else class="min-w-fit h-full w-2/3 overflow-scroll pr-4" :id="raum.id" :manager="stundenplanManager"
			:wochentyp="() => wochentypAnzeige" :kalenderwoche="() => undefined" mode-pausenaufsichten="aus" :ignore-empty />
		<svws-ui-modal v-model:show="show" size="medium">
			<template #modalTitle>Stundenplan drucken</template>
			<template #modalContent>
				<report-parameters :reportvorlage="ReportingReportvorlage.STUNDENPLANUNG_V_RAUM_STUNDENPLAN"
					:id-hauptdaten-objekt="stundenplanManager().getStundenplan().id" :ids-hauptdaten="[raum?.id ?? -1]" :ids-detaildaten="[]" />
			</template>
		</svws-ui-modal>
	</div>
</template>

<script setup lang="ts">

	import { computed, shallowRef, ref } from "vue";
	import type { StundenplanRaumProps } from "./SStundenplanRaumProps";
	import { StundenplanRaum } from "@core/core/data/stundenplan/StundenplanRaum";
	import { ArrayList } from "@core/java/util/ArrayList";
	import type { List } from "@core/java/util/List";
	import { useRegionSwitch } from "@ui/ui/composables/useRegionSwitch";
	import { ReportingReportvorlage } from "@core/core/types/reporting/ReportingReportvorlage";

	const props = defineProps<StundenplanRaumProps>();

	const { focusHelpVisible, focusSwitchingEnabled } = useRegionSwitch();

	const _raum = shallowRef<StundenplanRaum | null>(null);
	const wochentypAnzeige = shallowRef<number>(0);
	const show = ref(false);

	const raum = computed<StundenplanRaum | null>({
		get: () => {
			if (_raum.value !== null) {
				try {
					return props.stundenplanManager().raumGetByIdOrException(_raum.value.id);
				} catch {
					return null;
				}
			}
			if (props.stundenplanManager().raumGetMengeVerwendetAsList().size() > 0) {
				return props.stundenplanManager().raumGetMengeVerwendetAsList().get(0);
			} else {
				return null;
			}
		},
		set: (value) => _raum.value = value,
	});

	const ignoreEmpty = computed<boolean>({
		get: () => props.ganzerStundenplanRaeume(),
		set: (value) => void props.setGanzerStundenplanRaeume(value),
	});

	function wochentypen(): List<number> {
		let modell = props.stundenplanManager().getWochenTypModell();
		if (modell <= 1) {
			modell = 0;
		}
		const result = new ArrayList<number>();
		for (let n = 0; n <= modell; n++) {
			result.add(n);
		}
		return result;
	}

</script>
