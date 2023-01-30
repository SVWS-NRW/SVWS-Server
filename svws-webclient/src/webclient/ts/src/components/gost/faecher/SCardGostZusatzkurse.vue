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

	import { computed, ComputedRef, WritableComputedRef } from "vue";

	import { GostHalbjahr } from "@svws-nrw/svws-core-ts";
	import { DataGostJahrgang } from "~/apps/gost/DataGostJahrgang";

	const props = defineProps<{
		jahrgangsdaten: DataGostJahrgang;
	}>();

	const inputBeginnZusatzkurs: ComputedRef<Array<GostHalbjahr>> = computed(
		() => { return [ GostHalbjahr.Q11, GostHalbjahr.Q12, GostHalbjahr.Q21] }
	);

	const inputHatZusatzkursGE: WritableComputedRef<boolean | undefined> = computed({
		get(): boolean | undefined { return props.jahrgangsdaten.daten?.hatZusatzkursGE; },
		set(val: boolean | undefined) { void props.jahrgangsdaten.patch({ hatZusatzkursGE: val }); }
	});

	const inputBeginnZusatzkursGE: WritableComputedRef<GostHalbjahr> = computed({
		get(): GostHalbjahr {
			if (props.jahrgangsdaten.daten === undefined)
				return GostHalbjahr.Q21;
			return GostHalbjahr.fromKuerzel(props.jahrgangsdaten.daten.beginnZusatzkursGE) || GostHalbjahr.Q21;
		},
		set(val: GostHalbjahr) {
			void props.jahrgangsdaten.patch({ beginnZusatzkursGE: val.kuerzel });
		}
	});

	const inputHatZusatzkursSW: WritableComputedRef<boolean | undefined> = computed({
		get(): boolean | undefined { return props.jahrgangsdaten.daten?.hatZusatzkursSW; },
		set(val: boolean | undefined) { void props.jahrgangsdaten.patch({ hatZusatzkursSW: val }); }
	});

	const inputBeginnZusatzkursSW: WritableComputedRef<GostHalbjahr> = computed({
		get(): GostHalbjahr {
			if (props.jahrgangsdaten.daten === undefined)
				return GostHalbjahr.Q21;
			return GostHalbjahr.fromKuerzel(props.jahrgangsdaten.daten.beginnZusatzkursSW) || GostHalbjahr.Q21;
		},
		set(val: GostHalbjahr) {
			void props.jahrgangsdaten.patch({ beginnZusatzkursSW: val.kuerzel });
		}
	});

</script>
