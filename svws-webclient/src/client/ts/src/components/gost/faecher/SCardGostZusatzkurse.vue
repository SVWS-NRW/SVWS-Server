<template>
	<svws-ui-content-card title="Angebot von Zusatzkursen">
		<div class="input-wrapper-1-col">
			<svws-ui-checkbox v-model="inputHatZusatzkursGE">Zusatzkurs in GE wird angeboten</svws-ui-checkbox>
			<svws-ui-multi-select title="Beginn Zusatzkurs GE" v-model="inputBeginnZusatzkursGE"
				:items="inputBeginnZusatzkurs" :item-text="(i: GostHalbjahr) => i.kuerzel" :disabled="!inputHatZusatzkursGE" />
			<svws-ui-checkbox v-model="inputHatZusatzkursSW">Zusatzkurs in SW wird angeboten</svws-ui-checkbox>
			<svws-ui-multi-select title="Beginn Zusatzkurs SW" v-model="inputBeginnZusatzkursSW"
				:items="inputBeginnZusatzkurs" :item-text="(i: GostHalbjahr) => i.kuerzel" :disabled="!inputHatZusatzkursSW" />
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type { ComputedRef, WritableComputedRef } from "vue";
	import { computed } from "vue";

	import type { GostJahrgangsdaten } from "@svws-nrw/svws-core";
	import { GostHalbjahr } from "@svws-nrw/svws-core";

	const props = defineProps<{
		patchJahrgangsdaten: (data: Partial<GostJahrgangsdaten>, abiturjahr : number) => Promise<boolean>;
		jahrgangsdaten: GostJahrgangsdaten;
	}>();

	const inputBeginnZusatzkurs: ComputedRef<Array<GostHalbjahr>> = computed(
		() => { return [ GostHalbjahr.Q11, GostHalbjahr.Q12, GostHalbjahr.Q21] }
	);

	const inputHatZusatzkursGE: WritableComputedRef<boolean> = computed({
		get: () => props.jahrgangsdaten.hatZusatzkursGE,
		set: (value) => { void props.patchJahrgangsdaten({ hatZusatzkursGE: value }, props.jahrgangsdaten.abiturjahr); }
	});

	const inputBeginnZusatzkursGE: WritableComputedRef<GostHalbjahr> = computed({
		get: () => GostHalbjahr.fromKuerzel(props.jahrgangsdaten.beginnZusatzkursGE) || GostHalbjahr.Q21,
		set: (value) => void props.patchJahrgangsdaten({ beginnZusatzkursGE: value.kuerzel }, props.jahrgangsdaten.abiturjahr)
	});

	const inputHatZusatzkursSW: WritableComputedRef<boolean> = computed({
		get: () => props.jahrgangsdaten.hatZusatzkursSW,
		set: (value) => void props.patchJahrgangsdaten({ hatZusatzkursSW: value }, props.jahrgangsdaten.abiturjahr)
	});

	const inputBeginnZusatzkursSW: WritableComputedRef<GostHalbjahr> = computed({
		get: () => GostHalbjahr.fromKuerzel(props.jahrgangsdaten.beginnZusatzkursSW) || GostHalbjahr.Q21,
		set: (value) => void props.patchJahrgangsdaten({ beginnZusatzkursSW: value.kuerzel }, props.jahrgangsdaten.abiturjahr)
	});

</script>
