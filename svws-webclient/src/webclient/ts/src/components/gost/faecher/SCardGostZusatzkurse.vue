<template>
	<svws-ui-content-card title="Angebot von Zusatzkursen">
		<div class="input-wrapper-1-col">
			<svws-ui-checkbox v-model="inputHatZusatzkursGE">Zusatzkurs in GE wird angeboten</svws-ui-checkbox>
			<svws-ui-multi-select
				v-model="inputBeginnZusatzkursGE"
				title="Beginn Zusatzkurs GE"
				:items="inputBeginnZusatzkurs"
				:item-text="(i: GostHalbjahr) => i.kuerzel"
				:disabled="!inputHatZusatzkursGE"
			/>
			<svws-ui-checkbox v-model="inputHatZusatzkursSW">Zusatzkurs in SW wird angeboten</svws-ui-checkbox>
			<svws-ui-multi-select
				v-model="inputBeginnZusatzkursSW"
				title="Beginn Zusatzkurs SW"
				:items="inputBeginnZusatzkurs"
				:item-text="(i: GostHalbjahr) => i.kuerzel"
				:disabled="!inputHatZusatzkursSW"
			/>
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import { computed, ComputedRef, WritableComputedRef } from "vue";

	import { GostHalbjahr } from "@svws-nrw/svws-core-ts";
	import { injectMainApp, Main } from "~/apps/Main";

	const main: Main = injectMainApp();
	const app = main.apps.gost;

	const inputBeginnZusatzkurs: ComputedRef<Array<GostHalbjahr>> = computed(
		() => { return [ GostHalbjahr.Q11, GostHalbjahr.Q12, GostHalbjahr.Q21] }
	);

	const inputHatZusatzkursGE: WritableComputedRef<boolean | undefined> = computed({
		get(): boolean | undefined { return app.dataJahrgang.daten?.hatZusatzkursGE; },
		set(val: boolean | undefined) { app.dataJahrgang.patch({ hatZusatzkursGE: val }); }
	});

	const inputBeginnZusatzkursGE: WritableComputedRef<GostHalbjahr> = computed({
		get(): GostHalbjahr { 
			if (app.dataJahrgang.daten === undefined)
				return GostHalbjahr.Q21;
			return GostHalbjahr.fromKuerzel(app.dataJahrgang.daten.beginnZusatzkursGE) || GostHalbjahr.Q21; 
		},
		set(val: GostHalbjahr) { 
			app.dataJahrgang.patch({ beginnZusatzkursGE: val.kuerzel }); 
		}
	});

	const inputHatZusatzkursSW: WritableComputedRef<boolean | undefined> = computed({
		get(): boolean | undefined { return app.dataJahrgang.daten?.hatZusatzkursSW; },
		set(val: boolean | undefined) { app.dataJahrgang.patch({ hatZusatzkursSW: val }); }
	});

	const inputBeginnZusatzkursSW: WritableComputedRef<GostHalbjahr> = computed({
		get(): GostHalbjahr { 
			if (app.dataJahrgang.daten === undefined)
				return GostHalbjahr.Q21;
			return GostHalbjahr.fromKuerzel(app.dataJahrgang.daten.beginnZusatzkursSW) || GostHalbjahr.Q21; 
		},
		set(val: GostHalbjahr) { 
			app.dataJahrgang.patch({ beginnZusatzkursSW: val.kuerzel }); 
		}
	});

</script>
