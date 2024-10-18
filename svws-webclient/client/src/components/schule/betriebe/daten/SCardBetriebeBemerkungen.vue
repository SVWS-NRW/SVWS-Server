<template>
	<svws-ui-input-wrapper :grid="2" class="input-wrapper--checkboxes">
		<svws-ui-checkbox :model-value="daten.ausbildungsbetrieb" @update:model-value="ausbildungsbetrieb => patch({ ausbildungsbetrieb } )"> Ausbildungsbetrieb </svws-ui-checkbox>
		<svws-ui-checkbox :model-value="daten.bietetPraktika" @update:model-value="bietetPraktika => patch({ bietetPraktika })"> Bietet Praktikumsplätze </svws-ui-checkbox>
		<svws-ui-checkbox :model-value="daten.Massnahmentraeger" @update:model-value="Massnahmentraeger => patch({ Massnahmentraeger })"> Maßnahmenträger </svws-ui-checkbox>
		<svws-ui-checkbox :model-value="daten.ErwFuehrungszeugnis" @update:model-value="ErwFuehrungszeugnis => patch({ ErwFuehrungszeugnis })"> Erweitertes Führungszeugnis notwendig </svws-ui-checkbox>
		<svws-ui-checkbox :model-value="daten.BelehrungISG" @update:model-value="BelehrungISG => patch({ BelehrungISG })"> Belehrung n. Infektionsschutzgesetz notwendig </svws-ui-checkbox>
	</svws-ui-input-wrapper>
	<svws-ui-spacing />
	<svws-ui-spacing />
	<svws-ui-textarea-input :model-value="daten.bemerkungen" @change="bemerkungen => patch({ bemerkungen: bemerkungen || '' })" placeholder="Bemerkungen" />
</template>

<script setup lang="ts">
	import type { BetriebStammdaten } from "@core";

	const props = defineProps<{
		daten: BetriebStammdaten;
		patch: (data : Partial<BetriebStammdaten>) => Promise<void>;
	}>();

</script>
