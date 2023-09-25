<template>
	<svws-ui-content-card title="Angebot von Zusatzkursen">
		<svws-ui-input-wrapper :grid="4">
			<svws-ui-checkbox type="toggle" class="my-auto" v-model="inputHatZusatzkursGE">Zusatzkurs GE</svws-ui-checkbox>
			<svws-ui-select class="col-span-3" title="Beginn Zusatzkurs GE" v-model="inputBeginnZusatzkursGE" :items="inputBeginnZusatzkurs" :item-text="(i: GostHalbjahr) => i.kuerzel" :disabled="!inputHatZusatzkursGE" />
			<svws-ui-checkbox type="toggle" class="my-auto" v-model="inputHatZusatzkursSW">Zusatzkurs SW</svws-ui-checkbox>
			<svws-ui-select class="col-span-3" title="Beginn Zusatzkurs SW" v-model="inputBeginnZusatzkursSW" :items="inputBeginnZusatzkurs" :item-text="(i: GostHalbjahr) => i.kuerzel" :disabled="!inputHatZusatzkursSW" />
		</svws-ui-input-wrapper>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type { ComputedRef, WritableComputedRef } from "vue";
	import { computed } from "vue";

	import type { GostJahrgangsdaten } from "@core";
	import { GostHalbjahr } from "@core";

	const props = defineProps<{
		patchJahrgangsdaten: (data: Partial<GostJahrgangsdaten>, abiturjahr : number) => Promise<boolean>;
		jahrgangsdaten: () => GostJahrgangsdaten;
	}>();

	const inputBeginnZusatzkurs: ComputedRef<Array<GostHalbjahr>> = computed(
		() => { return [ GostHalbjahr.Q11, GostHalbjahr.Q12, GostHalbjahr.Q21] }
	);

	const inputHatZusatzkursGE: WritableComputedRef<boolean> = computed({
		get: () => props.jahrgangsdaten().hatZusatzkursGE,
		set: (value) => { void props.patchJahrgangsdaten({ hatZusatzkursGE: value }, props.jahrgangsdaten().abiturjahr); }
	});

	const inputBeginnZusatzkursGE: WritableComputedRef<GostHalbjahr> = computed({
		get: () => GostHalbjahr.fromKuerzel(props.jahrgangsdaten().beginnZusatzkursGE) || GostHalbjahr.Q21,
		set: (value) => void props.patchJahrgangsdaten({ beginnZusatzkursGE: value.kuerzel }, props.jahrgangsdaten().abiturjahr)
	});

	const inputHatZusatzkursSW: WritableComputedRef<boolean> = computed({
		get: () => props.jahrgangsdaten().hatZusatzkursSW,
		set: (value) => void props.patchJahrgangsdaten({ hatZusatzkursSW: value }, props.jahrgangsdaten().abiturjahr)
	});

	const inputBeginnZusatzkursSW: WritableComputedRef<GostHalbjahr> = computed({
		get: () => GostHalbjahr.fromKuerzel(props.jahrgangsdaten().beginnZusatzkursSW) || GostHalbjahr.Q21,
		set: (value) => void props.patchJahrgangsdaten({ beginnZusatzkursSW: value.kuerzel }, props.jahrgangsdaten().abiturjahr)
	});

</script>
