<template>
	<svws-ui-content-card title="Angebot von Zusatzkursen">
		<svws-ui-input-wrapper :grid="4">
			<svws-ui-checkbox :disabled="!hatUpdateKompetenz" type="toggle" class="my-auto" v-model="inputHatZusatzkursGE">Zusatzkurs GE</svws-ui-checkbox>
			<svws-ui-select :disabled="!hatUpdateKompetenz || !inputHatZusatzkursGE" class="col-span-3" title="Beginn Zusatzkurs GE" v-model="inputBeginnZusatzkursGE" :items="inputBeginnZusatzkurs" :item-text="i => i.kuerzel" />
			<svws-ui-checkbox :disabled="!hatUpdateKompetenz" type="toggle" class="my-auto" v-model="inputHatZusatzkursSW">Zusatzkurs SW</svws-ui-checkbox>
			<svws-ui-select :disabled="!hatUpdateKompetenz || !inputHatZusatzkursSW" class="col-span-3" title="Beginn Zusatzkurs SW" v-model="inputBeginnZusatzkursSW" :items="inputBeginnZusatzkurs" :item-text="i => i.kuerzel" />
		</svws-ui-input-wrapper>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import { computed } from "vue";
	import type { GostJahrgangsdaten } from "@core";
	import { GostHalbjahr } from "@core";

	const props = defineProps<{
		patchJahrgangsdaten: (data: Partial<GostJahrgangsdaten>, abiturjahr : number) => Promise<boolean>;
		jahrgangsdaten: () => GostJahrgangsdaten;
		hatUpdateKompetenz: boolean;
	}>();

	const inputBeginnZusatzkurs = [GostHalbjahr.Q11, GostHalbjahr.Q12, GostHalbjahr.Q21];

	const inputHatZusatzkursGE = computed<boolean>({
		get: () => props.jahrgangsdaten().hatZusatzkursGE,
		set: (value) => { void props.patchJahrgangsdaten({ hatZusatzkursGE: value }, props.jahrgangsdaten().abiturjahr); }
	});

	const inputBeginnZusatzkursGE = computed<GostHalbjahr>({
		get: () => GostHalbjahr.fromKuerzel(props.jahrgangsdaten().beginnZusatzkursGE) || GostHalbjahr.Q21,
		set: (value) => void props.patchJahrgangsdaten({ beginnZusatzkursGE: value.kuerzel }, props.jahrgangsdaten().abiturjahr)
	});

	const inputHatZusatzkursSW = computed<boolean>({
		get: () => props.jahrgangsdaten().hatZusatzkursSW,
		set: (value) => void props.patchJahrgangsdaten({ hatZusatzkursSW: value }, props.jahrgangsdaten().abiturjahr)
	});

	const inputBeginnZusatzkursSW = computed<GostHalbjahr>({
		get: () => GostHalbjahr.fromKuerzel(props.jahrgangsdaten().beginnZusatzkursSW) || GostHalbjahr.Q21,
		set: (value) => void props.patchJahrgangsdaten({ beginnZusatzkursSW: value.kuerzel }, props.jahrgangsdaten().abiturjahr)
	});

</script>
