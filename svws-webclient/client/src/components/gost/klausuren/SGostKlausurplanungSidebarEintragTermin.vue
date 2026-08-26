<template>
	<s-gost-klausurplanung-sidebar-eintrag v-bind="$attrs"
		:data="termin"
		:title="titelzeile"
		expandable>
		<template #badge>
			<s-gost-klausurplanung-kurs-badge v-for="klausur in kursklausurenWennKeinTitel" :key="klausur.id" :kursklausur="klausur" :tooltip="false" light />
		</template>
		<template #meta>
			<span class="font-bold">{{ schuelerAnzahl }} Schüler</span>
			<span>{{ terminDauerText }}</span>
			<span v-if="showQuartal" class="opacity-50">{{ terminQuartalText }}</span>
		</template>
		<template #tooltip>
			<dl class="grid grid-cols-[auto_minmax(0,1fr)] gap-x-3 gap-y-1">
				<dt class="col-span-2 text-base font-bold">{{ presenter.terminTitel(termin) }}</dt>
				<dt class="opacity-60">Datum</dt>
				<dd>{{ terminDatumText }}</dd>
				<dt class="opacity-60">Schüler</dt>
				<dd>{{ schuelerAnzahl }}</dd>
				<dt class="opacity-60">Dauer</dt>
				<dd>{{ terminDauerText }}</dd>
				<dt class="opacity-60">Quartal</dt>
				<dd>{{ terminQuartalText }}</dd>
			</dl>
		</template>
		<template v-if="$slots.expanded" #expanded>
			<slot name="expanded" />
		</template>
	</s-gost-klausurplanung-sidebar-eintrag>
</template>

<script setup lang="ts">
	import type { GostKlausurtermin, GostKursklausur } from "@core";
	import { computed } from "vue";
	import { useGostKlausurplanungState } from "@ui";
	import { useKlausurplanungPresenter } from "./SGostKlausurplanungPresenter";

	defineOptions({
		inheritAttrs: false,
	});

	const props = withDefaults(defineProps<{
		termin: GostKlausurtermin;
		kursklausuren: Iterable<GostKursklausur>;
		showQuartal?: boolean;
	}>(), {
		showQuartal: false,
	});

	defineSlots<{
		expanded?: () => unknown;
	}>();

	const state = useGostKlausurplanungState();
	const presenter = useKlausurplanungPresenter(state);

	const terminDatumText = computed<string>(() => presenter.terminDatumText(props.termin));
	const terminLabel = computed<string | undefined>(() => {
		if ((props.termin.bezeichnung !== null) && (props.termin.bezeichnung.trim().length > 0)) {
			return props.termin.bezeichnung;
		}
		return props.termin.istHaupttermin ? undefined : "Nachschreibtermin";
	});
	const titelzeile = computed<string | undefined>(() => {
		const teile: string[] = [];
		if (props.termin.datum !== null) {
			teile.push(terminDatumText.value);
		}
		if (terminLabel.value !== undefined) {
			teile.push(terminLabel.value);
		}
		return teile.length === 0 ? undefined : teile.join(" ");
	});
	const kursklausurenWennKeinTitel = computed<GostKursklausur[]>(() => terminLabel.value === undefined ? [...props.kursklausuren] : []);
	const schuelerAnzahl = computed<number>(() => state.manager.schuelerklausurterminAktuellGetMengeByTermin(props.termin).size());
	const terminDauerText = computed<string>(() => presenter.terminDauerText(props.termin));
	const terminQuartalText = computed<string>(() => presenter.terminQuartalText(props.termin));
</script>
